package com.shellshellfish.aaas.userinfo.service.impl;

import com.mongodb.bulk.BulkWriteResult;
import com.shellshellfish.aaas.common.enums.MonetaryFundEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.grpc.trade.order.TrdOrderDetail;
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.common.utils.TradeUtil;

import com.shellshellfish.aaas.userinfo.model.dao.CoinFundYieldRate;
import com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdZZInfo;
import com.shellshellfish.aaas.userinfo.model.dao.MongoUserFundQuantityLog;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.repositories.funds.MongoCoinFundYieldRateRepository;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUiTrdZZInfoRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import com.shellshellfish.aaas.userinfo.service.CheckUiProductDetailService;
import com.shellshellfish.aaas.userinfo.service.OrderRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Autowired
    MongoCoinFundYieldRateRepository mongoCoinFundYieldRateRepository;

    @Autowired
    MongoUiTrdZZInfoRepo mongoUiTrdZZInfoRepo;

    Logger logger = LoggerFactory.getLogger(CheckUiProductDetailServiceImpl.class);

    private static final String DATE_FORMAT_PATTERN = "yyyyMMdd";
    //不可以直接更新，需要手动确认之后才可以更新
    private static final Integer CAN_NOT_UPDATE = 0;

    @Override
    public void check() {
        List<Long> userProdIdList = uiProductRepo.getAllId();
        List<MongoUserFundQuantityLog> mongoUserFundQuantityLogList = new ArrayList<>(userProdIdList.size());
        for (Long userProdId : userProdIdList) {
            List<MongoUiTrdZZInfo> mongoUiTrdZZInfoList = mongoUiTrdZZInfoRepo.findAllByUserProdId(userProdId);
//            List<OrderDetail> orderDetailList = orderRpcService.getAllTrdOrderDetail(userProdId);
            Map fundQuantityMap = calculateFundQuantity(mongoUiTrdZZInfoList);
            if (CollectionUtils.isEmpty(fundQuantityMap)) {
                continue;
            }
            Map fundStatusMap = getOrderDetailStatus(userProdId);
//            updateFundQuantity(fundQuantityMap, statusMap, userProdId);
            boolean isConsistent = checkFundQuantity(fundQuantityMap, fundStatusMap, userProdId);
            Map<String, Long> quantityMap = new HashMap(fundQuantityMap.size());
            Map<String, Integer> statusMap = new HashMap(fundStatusMap.size());

            //　spot can`t exists in mongo key
            for (Object object : fundQuantityMap.keySet()) {
                Long value = (Long) fundQuantityMap.get(object);
                String key = object.toString().replace(".", "");
                quantityMap.put(key, value);
            }

            for (Object object : fundStatusMap.keySet()) {
                Integer value = (Integer) fundStatusMap.get(object);
                String key = object.toString().replace(".", "");
                statusMap.put(key, value);
            }
            if (!isConsistent) {
                // recording the  inconsistent information
                MongoUserFundQuantityLog mongoUserFundQuantityLog = new MongoUserFundQuantityLog();
                mongoUserFundQuantityLog.setCreateTime(TradeUtil.getUTCTime());
                mongoUserFundQuantityLog.setUserProdId(userProdId);
                mongoUserFundQuantityLog.setUpdateTime(TradeUtil.getUTCTime());
                mongoUserFundQuantityLog.setUiTrdZZInfoList(mongoUiTrdZZInfoList);
                mongoUserFundQuantityLog.setFundQuantityMap(quantityMap);
                mongoUserFundQuantityLog.setProductStatusMap(statusMap);
                mongoUserFundQuantityLog.setCanUpdate(CAN_NOT_UPDATE);
                mongoUserFundQuantityLogList.add(mongoUserFundQuantityLog);
            }
        }
        if (!CollectionUtils.isEmpty(mongoUserFundQuantityLogList)) {
            saveOrUpdate(mongoUserFundQuantityLogList);
        }
    }

    private Map getOrderDetailStatus(Long userProdId) {
        List<TrdOrderDetail> orderDetailList = orderRpcService.getLatestOrderDetail(userProdId);
        Map statusMap = new HashMap(8);
        for (TrdOrderDetail orderDetail : orderDetailList) {
            statusMap.put(orderDetail.getFundCode(), orderDetail.getOrderDetailStatus());
        }
        return statusMap;
    }


    /**
     * 计算当前用户所持有的份额
     *
     * @param mongoUiTrdZZInfoList
     * @return Map  key = fundCode value = fundQuantity
     */
    private Map calculateFundQuantity(List<MongoUiTrdZZInfo> mongoUiTrdZZInfoList) {
        Long fundQuantity;
        Map<String, Long> fundQuantityMap = new HashMap<>(8);
        for (MongoUiTrdZZInfo mongoUiTrdZZInfo : mongoUiTrdZZInfoList) {
            String fundCode = mongoUiTrdZZInfo.getFundCode();
            fundQuantity = Optional.of(fundQuantityMap).map(m -> m.get(fundCode)).orElse(0L);
            int status = mongoUiTrdZZInfo.getTradeStatus();
            if (!TrdOrderStatusEnum.isConfirmed(status)) {
                continue;
            }

            if (TrdOrderOpTypeEnum.BUY.getOperation() == mongoUiTrdZZInfo.getTradeType()) {
                if (MonetaryFundEnum.containsCode(mongoUiTrdZZInfo.getFundCode())) {
                    //货币基金净值恒为一，　每日份额变化　，需要除以复权单位精致，以比拟于普通基金
                    //FIXME updateDate 有可能不准确　应该是mongo 库ssfui.ui_trdzzinfo　的confirm_date
                    BigDecimal navadj = getMonetaryFundNavAdj(fundCode, mongoUiTrdZZInfo.getConfirmDate());
                    fundQuantity += (mongoUiTrdZZInfo.getTradeConfirmShare() / navadj.longValue());
                } else {
                    fundQuantity += mongoUiTrdZZInfo.getTradeConfirmShare();
                }
            } else if (TrdOrderOpTypeEnum.REDEEM.getOperation() == mongoUiTrdZZInfo.getTradeType()) {
                if (MonetaryFundEnum.containsCode(fundCode)) {
                    BigDecimal navadj = getMonetaryFundNavAdj(fundCode, mongoUiTrdZZInfo.getConfirmDate());
                    fundQuantity -= (mongoUiTrdZZInfo.getTradeConfirmShare() / navadj.longValue());
                } else {
                    fundQuantity -= mongoUiTrdZZInfo.getTradeConfirmShare();
                }
            }
            fundQuantityMap.put(fundCode, fundQuantity);
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
            update.set("ui_trd_zz_info", mongoUserFundQuantityLog.getUiTrdZZInfoList());
            update.set("product_status", mongoUserFundQuantityLog.getProductStatusMap());
            update.set("fund_quantity", mongoUserFundQuantityLog.getFundQuantityMap());
            update.setOnInsert("create_time", mongoUserFundQuantityLog.getCreateTime());
            update.setOnInsert("can_update", mongoUserFundQuantityLog.getCanUpdate());
            updates.add(Pair.of(query, update));
        }
        ops.upsert(updates);
        return ops.execute();
    }

    //获取货币基金的复权单位精致
    private BigDecimal getMonetaryFundNavAdj(String fundCode, String confirmedDate) {
        //货币基金使用附权单位净值
        LocalDate date = InstantDateUtil.format(confirmedDate, DATE_FORMAT_PATTERN).plusDays(1);
        Long time = InstantDateUtil.getEpochMillsOfZero(date);
        CoinFundYieldRate coinFundYieldRate = mongoCoinFundYieldRateRepository
                .findFirstByCodeAndQueryDateBefore(fundCode, time,
                        new Sort(new Sort.Order(Sort.Direction.DESC, "querydate")));
        return Optional.ofNullable(coinFundYieldRate).map(m -> m.getNavadj()).orElse(BigDecimal.ONE);

    }

    public static void main(String[] args) {
        Map map = new HashMap();
        map.put("123.", "123");
        map.put("456.", "456");
        map.put("789.", "789");

        map.forEach((key, value) -> {
            key = key.toString().replace(",", "");
        });


        map.forEach((k, v) -> System.out.println(k));
    }
}
