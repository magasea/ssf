package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.common.grpc.trade.order.TrdOrderDetail;
import com.shellshellfish.aaas.finance.trade.order.GenOrderIdAndFundCode;
import com.shellshellfish.aaas.finance.trade.order.GetOrderDetailInfoByPage;
import com.shellshellfish.aaas.finance.trade.order.OrderRpcServiceGrpc.OrderRpcServiceBlockingStub;
import com.shellshellfish.aaas.grpc.common.OrderDetail;
import com.shellshellfish.aaas.grpc.common.UserProdId;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.service.OrderRpcService;
import com.shellshellfish.aaas.userinfo.service.UserInfoService;
import com.shellshellfish.aaas.userinfo.utils.MyBeanUtils;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Created by chenwei on 2018- 二月 - 09
 */
@Service
public class OrderGrpcServiceImpl implements OrderRpcService {

    private static final Logger logger = LoggerFactory.getLogger(OrderGrpcServiceImpl.class);

    @Autowired
    OrderRpcServiceBlockingStub tradeOrderServiceBlockingStub;


    @Autowired
    UserInfoService userInfoService;


    @Autowired
    UserInfoRepository userInfoRepository;


    @Override
    public String getBankCardNumberByUserProdId(Long userProdId) {
        UserProdId.Builder npiBuilder = UserProdId.newBuilder();
        npiBuilder.setUserProdId(userProdId);
        return tradeOrderServiceBlockingStub.getBankCardNumByUserProdId(npiBuilder.build()).getUserBankCardnum();
    }

    @Override
    public List<TrdOrderDetail> getAllTrdOrderDetail(Long userProdId) {
        UserProdId.Builder builder = UserProdId.newBuilder();
        builder.setUserProdId(userProdId);
        List<OrderDetail> orderDetails =
        tradeOrderServiceBlockingStub.getAllOrderDetail(builder.build()).getOrderDetailResultList();
        List<TrdOrderDetail> trdOrderDetails = new ArrayList<>();
        orderDetails.forEach(
            grpcOrderDetail -> {
                TrdOrderDetail trdOrderDetail = new TrdOrderDetail();
                MyBeanUtils.mapEntityIntoDTO(grpcOrderDetail, trdOrderDetail);
                trdOrderDetails.add(trdOrderDetail);
            }
        );
        return trdOrderDetails;
    }

    @Override
    public List<TrdOrderDetail> getLatestOrderDetail(Long userProdId) {
        UserProdId.Builder builder = UserProdId.newBuilder();
        builder.setUserProdId(userProdId);
        List<OrderDetail> orderDetails =
            tradeOrderServiceBlockingStub.getLatestOrderDetail(builder.build()).getOrderDetailResultList();
        List<TrdOrderDetail> trdOrderDetails = new ArrayList<>();
        orderDetails.forEach(
            grpcOrderDetail -> {
                TrdOrderDetail trdOrderDetail = new TrdOrderDetail();
                MyBeanUtils.mapEntityIntoDTO(grpcOrderDetail, trdOrderDetail);
                trdOrderDetails.add(trdOrderDetail);
            }
        );
        return trdOrderDetails;
    }

    @Override
    public List<TrdOrderDetail> getOrderDetailByGenOrderIdAndFundCode(String orderId,
        String fundCode) {
        GenOrderIdAndFundCode.Builder goiafcBuilder = GenOrderIdAndFundCode.newBuilder();
        goiafcBuilder.setFundCode(fundCode);
        goiafcBuilder.setOrderId(orderId);
        List<OrderDetail> orderDetails =
        tradeOrderServiceBlockingStub.getOrderDetailByParams(goiafcBuilder.build()
        ).getOrderDetailResultList();
        List<TrdOrderDetail> trdOrderDetails = new ArrayList<>();
        orderDetails.forEach(
            grpcOrderDetail -> {
                TrdOrderDetail trdOrderDetail = new TrdOrderDetail();
                MyBeanUtils.mapEntityIntoDTO(grpcOrderDetail, trdOrderDetail);
                trdOrderDetails.add(trdOrderDetail);
            }
        );
        return trdOrderDetails;
    }

    @Override
    public List<TrdOrderDetail> getOrderDetailByUserProdIdAndFundCodeAndTrdType(Long userProdId,
        String fundCode, Integer trdType) {
        GenOrderIdAndFundCode.Builder goiafcBuilder = GenOrderIdAndFundCode.newBuilder();
        goiafcBuilder.setFundCode(fundCode);
        goiafcBuilder.setUserProdId(userProdId);
        goiafcBuilder.setTrdType(trdType);
        List<OrderDetail> orderDetails =
            tradeOrderServiceBlockingStub.getOrderDetailByParams(goiafcBuilder.build()
            ).getOrderDetailResultList();
        List<TrdOrderDetail> trdOrderDetails = new ArrayList<>();
        orderDetails.forEach(
            grpcOrderDetail -> {
                TrdOrderDetail trdOrderDetail = new TrdOrderDetail();
                MyBeanUtils.mapEntityIntoDTO(grpcOrderDetail, trdOrderDetail);
                trdOrderDetails.add(trdOrderDetail);
            }
        );
        return trdOrderDetails;
    }

    @Override
    public List<TrdOrderDetail> getOrderDetailByApplySerial(String applySerial) {
        GenOrderIdAndFundCode.Builder goiafcBuilder = GenOrderIdAndFundCode.newBuilder();
        goiafcBuilder.setApplySerial(applySerial);
        List<OrderDetail> orderDetails =
            tradeOrderServiceBlockingStub.getOrderDetailByParams(goiafcBuilder.build()
            ).getOrderDetailResultList();
        List<TrdOrderDetail> trdOrderDetails = new ArrayList<>();
        orderDetails.forEach(
            grpcOrderDetail -> {
                TrdOrderDetail trdOrderDetail = new TrdOrderDetail();
                MyBeanUtils.mapEntityIntoDTO(grpcOrderDetail, trdOrderDetail);
                trdOrderDetails.add(trdOrderDetail);
            }
        );
        return trdOrderDetails;
    }

    @Override
    public Page<TrdOrderDetail> getFailedOrderInfos(int pageNo, int pageSize) {

        try {
            GetOrderDetailInfoByPage.Builder godipBuilder = GetOrderDetailInfoByPage.newBuilder();
            godipBuilder.setPageNo(pageNo);
            godipBuilder.setPageSize(pageSize);

            int totalPages = tradeOrderServiceBlockingStub.getFailedOrderDetails
                (godipBuilder.build()).getTotalPages();
            List<OrderDetail> orderDetails = tradeOrderServiceBlockingStub.getFailedOrderDetails
                (godipBuilder.build()).getOrderDetailResultList();

            if(pageNo >= totalPages || CollectionUtils.isEmpty(orderDetails)){
                logger.info("all page retrieved");
            }
            List<TrdOrderDetail> trdOrderDetailList = new ArrayList<>();
            trdOrderDetailList = MyBeanUtils.convertList(orderDetails, TrdOrderDetail.class);
            Page<TrdOrderDetail> pages = new PageImpl<>(trdOrderDetailList);

            return pages;
        } catch (Exception e) {
            logger.error("Error:", e);
        }
        return null;
    }
}
