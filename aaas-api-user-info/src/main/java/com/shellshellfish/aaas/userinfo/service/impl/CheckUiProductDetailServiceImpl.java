package com.shellshellfish.aaas.userinfo.service.impl;

import com.mongodb.bulk.BulkWriteResult;
import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.order.OrderDetail;
import com.shellshellfish.aaas.userinfo.model.dao.MongoUserFundQuantityLog;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import com.shellshellfish.aaas.userinfo.service.CheckUiProductDetailService;
import com.shellshellfish.aaas.userinfo.service.OrderRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @Author pierre.chen
 * @Date 18-5-16
 */
@Service
public class CheckUiProductDetailServiceImpl implements CheckUiProductDetailService {


    @Autowired
    UiProductRepo uiProductRepo;

    @Autowired
    OrderRpcService orderRpcService;

    @Autowired
    UiProductDetailRepo uiProductDetailRepo;

    @Autowired
    MongoTemplate mongoTemplate;

    Logger logger = LoggerFactory.getLogger(CheckUiProductDetailServiceImpl.class);

    @Override
    public void check() {
        List<Long> userProdIdList = uiProductRepo.getAllId();
        List<MongoUserFundQuantityLog> mongoUserFundQuantityLogList = new ArrayList<>(userProdIdList.size());
        for (Long userProdId : userProdIdList) {
            List<OrderDetail> orderDetailList = orderRpcService.getAllTrdOrderDetail(userProdId);
            Map fundQuantityMap = calculateFundQuantity(orderDetailList);
            if (CollectionUtils.isEmpty(fundQuantityMap)) {
                continue;
            }
            Map statusMap = getOrderDetailStatus(userProdId);
//            updateFundQuantity(fundQuantityMap, statusMap, userProdId);
            boolean isConsistent = checkFundQuantity(fundQuantityMap, statusMap, userProdId);

            if (!isConsistent) {
                // recording the  inconsistent information
                MongoUserFundQuantityLog mongoUserFundQuantityLog = new MongoUserFundQuantityLog();
                mongoUserFundQuantityLog.setCreateTime(TradeUtil.getUTCTime());
                mongoUserFundQuantityLog.setUserProdId(userProdId);
                mongoUserFundQuantityLog.setUpdateTime(TradeUtil.getUTCTime());
                mongoUserFundQuantityLog.setOrderDetails(orderDetailList);
                mongoUserFundQuantityLog.setFundQuantityMap(fundQuantityMap);
                mongoUserFundQuantityLog.setProductStatusMap(statusMap);
                mongoUserFundQuantityLogList.add(mongoUserFundQuantityLog);
            }
        }
        if (!CollectionUtils.isEmpty(mongoUserFundQuantityLogList)) {
            saveOrUpdate(mongoUserFundQuantityLogList);
        }
    }

    private Map getOrderDetailStatus(Long userProdId) {
        List<OrderDetail> orderDetailList = orderRpcService.getLatestOrderDetail(userProdId);
        Map statusMap = new HashMap(8);
        for (OrderDetail orderDetail : orderDetailList) {
            statusMap.put(orderDetail.getFundCode(), orderDetail.getOrderDetailStatus());
        }
        return statusMap;
    }


    /**
     * 计算当前用户所持有的份额
     *
     * @param orderDetailList
     * @return Map  key = fundCode value = fundQuantity
     */
    private Map calculateFundQuantity(List<OrderDetail> orderDetailList) {
        Long fundQuantity;
        Map<String, Long> fundQuantityMap = new HashMap<>(8);
        for (OrderDetail orderDetail : orderDetailList) {
            fundQuantity = Optional.of(fundQuantityMap).map(m -> m.get(orderDetail.getFundCode())).orElse(0L);
            int status = orderDetail.getOrderDetailStatus();
            if (!TrdOrderStatusEnum.isConfirmed(status)) {
                continue;
            }
            //FIXME 货币基金的情况需要特殊处理 货币基金份额= fundNumConfirmed/货币基金的累计净值
            if (TrdOrderOpTypeEnum.BUY.getOperation() == orderDetail.getTradeType()) {
                fundQuantity += orderDetail.getFundNumConfirmed();
            } else if (TrdOrderOpTypeEnum.REDEEM.getOperation() == orderDetail.getTradeType()) {
                fundQuantity -= orderDetail.getFundNumConfirmed();
            }
            fundQuantityMap.put(orderDetail.getFundCode(), fundQuantity);
        }
        return fundQuantityMap;
    }

    /**
     * 更新用户所持有的基金份额
     *
     * @param fundQuantityMap key = ${fundCode}   value = ${fundQuantity}
     * @param userProdId
     * @return 更新的数据条数
     */
    private int updateFundQuantity(Map<String, Long> fundQuantityMap, Map<String, Integer> statusMap, Long userProdId) {
        int total = 0;
        for (String key : fundQuantityMap.keySet()) {
            Long fundQuantity = fundQuantityMap.get(key);
            Integer status = statusMap.get(key);
            int num = uiProductDetailRepo.updateFundQuantity(key, fundQuantity.intValue(), status, userProdId);
            if (num > 0) {
                total += num;
                logger.info("update fundQuantity of ui_product_details  fundCode :{},fundQuantity:{},status:{}," +
                                "userProdId:{}",
                        key, fundQuantity, status, userProdId);
            }
        }
        return total;
    }

    /**
     * 检测不一致的数据
     *
     * @param fundQuantityMap key = ${fundCode}   value = ${fundQuantity}
     * @param userProdId
     * @return 是否一致
     */
    private boolean checkFundQuantity(Map<String, Long> fundQuantityMap, Map<String, Integer> statusMap, Long
            userProdId) {
        List<UiProductDetail> uiProductDetailList = uiProductDetailRepo.findAllByUserProdId(userProdId);

        Map<String, Integer> uiProdDetailStatusMap = new HashMap(uiProductDetailList.size());
        Map<String, Integer> uiProdDetailQuantityMap = new HashMap(uiProductDetailList.size());
        for (UiProductDetail uiProductDetail : uiProductDetailList) {
            uiProdDetailStatusMap.put(uiProductDetail.getFundCode(), uiProductDetail.getStatus());
            uiProdDetailQuantityMap.put(uiProductDetail.getFundCode(), uiProductDetail.getFundQuantity());
        }


        for (String key : fundQuantityMap.keySet()) {
            Long fundQuantity = fundQuantityMap.get(key);
            Integer status = statusMap.get(key);
            //份额不符，或者份额不符合
            if (fundQuantity.intValue() != uiProdDetailQuantityMap.get(key) ||
                    status != uiProdDetailStatusMap.get(key)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 批量更新
     *
     * @param mongoUserFundQuantityLogList
     * @return
     */
    public BulkWriteResult saveOrUpdate(List<MongoUserFundQuantityLog> mongoUserFundQuantityLogList) {
        BulkOperations ops = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, MongoUserFundQuantityLog.class);
        List<Pair<Query, Update>> updates = new ArrayList<>();
        for (MongoUserFundQuantityLog mongoUserFundQuantityLog : mongoUserFundQuantityLogList) {
            Criteria criteria = new Criteria();
            criteria.andOperator(
                    Criteria.where("user_prod_id").is(mongoUserFundQuantityLog.getUserProdId()));
            Query query = new Query(criteria);
            Update update = new Update();
            update.set("update_time", mongoUserFundQuantityLog.getUpdateTime());
            update.set("order_details", mongoUserFundQuantityLog.getOrderDetails());
            update.set("product_status", mongoUserFundQuantityLog.getProductStatusMap());
            update.set("fund_quantity", mongoUserFundQuantityLog.getFundQuantityMap());
            update.setOnInsert("create_time", mongoUserFundQuantityLog.getCreateTime());
            updates.add(Pair.of(query, update));
        }
        ops.upsert(updates);
        return ops.execute();
    }
}
