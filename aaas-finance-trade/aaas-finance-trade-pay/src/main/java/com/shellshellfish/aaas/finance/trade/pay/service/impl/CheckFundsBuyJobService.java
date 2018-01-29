package com.shellshellfish.aaas.finance.trade.pay.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shellshellfish.aaas.common.enums.SystemUserEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdZZCheckStatusEnum;
import com.shellshellfish.aaas.common.utils.ZZStatsToOrdStatsUtils;
import com.shellshellfish.aaas.finance.trade.pay.message.BroadcastMessageProducers;
import com.shellshellfish.aaas.common.grpc.trade.pay.ApplyResult;
import com.shellshellfish.aaas.finance.trade.pay.model.dao.TrdPayFlow;

import com.shellshellfish.aaas.finance.trade.pay.repositories.TrdPayFlowRepository;
import com.shellshellfish.aaas.finance.trade.pay.service.FundTradeApiService;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.pay.service.OrderService;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class CheckFundsBuyJobService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    TrdPayFlowRepository trdPayFlowRepository;

    @Autowired
    FundTradeApiService fundTradeApiService;

    @Autowired
    BroadcastMessageProducers broadcastMessageProducers;

    @Autowired
    OrderService orderService;

    public void executeSampleJob() {

        logger.info("The sample job has begun...");
        checkBuyPayFlows();
        checkReedemPayFlows();
        //
    }

    public void checkBuyPayFlows(){
        //先查一遍购买未确认状态的payFlow

        List<TrdPayFlow> trdPayFlows = trdPayFlowRepository
            .findAllByTradeConfirmShareIsAndTrdTypeIs(0L, TrdOrderOpTypeEnum.BUY.getOperation
                ());

        if(!CollectionUtils.isEmpty(trdPayFlows)) {
            ApplyResult applyResult = null;
            String userPid = null;
            String outsideOrderno = null;
            for (TrdPayFlow trdPayFlow : trdPayFlows) {
                try {
                    // TODO: replace userId with userUuid
                    userPid = orderService.getPidFromTrdAccoBrokerId(trdPayFlow);
                    outsideOrderno = trdPayFlow.getOutsideOrderno();
                    if(StringUtils.isEmpty(outsideOrderno)){
                        logger.error("if the outsideOrderno is empty, the payflow is of old "
                            + "process with outsideOrderno as the orderDetailId");
                        outsideOrderno = ""+trdPayFlow.getOrderDetailId();
                    }
                    applyResult = fundTradeApiService.getApplyResultByOutsideOrderNo
                        (TradeUtil.getZZOpenId(userPid), outsideOrderno);
                    if (null != applyResult && !StringUtils
                        .isEmpty(applyResult.getApplyshare())) {
                        com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlowMsg =
                            new com.shellshellfish.aaas.common.message.order.TrdPayFlow();
                        trdPayFlow.setUpdateBy(SystemUserEnum.SYSTEM_USER_ENUM.getUserId());
                        trdPayFlow.setUpdateDate(Instant.now().getEpochSecond());
                        trdPayFlow.setBuyDiscount(TradeUtil.getLongNumWithMul100(applyResult
                            .getCommisiondiscount()));
                        trdPayFlow.setOutsideOrderno(applyResult.getOutsideorderno
                            ());
                        trdPayFlow.setId(trdPayFlow.getId());
                        trdPayFlow.setApplySerial(applyResult.getApplyserial());
                        TrdOrderOpTypeEnum opTypeEnum = ZZStatsToOrdStatsUtils
                            .getTrdOrdOpTypeFromCallingCode(Integer
                                .valueOf(applyResult.getCallingcode()));
                        trdPayFlow.setTrdStatus(ZZStatsToOrdStatsUtils
                            .getOrdDtlStatFromZZStats(TrdZZCheckStatusEnum.getByStatus(
                                Integer.valueOf(applyResult.getConfirmflag())),opTypeEnum).getStatus());
                        trdPayFlow.setBuyFee(TradeUtil.getLongNumWithMul100(applyResult
                            .getPoundage()));
                        updateByCheckAboutSumNum(trdPayFlow, applyResult);
                        trdPayFlow.setOutsideOrderno(applyResult.getOutsideorderno());
                        trdPayFlow.setUpdateDate(TradeUtil.getUTCTime());
                        trdPayFlow.setUpdateBy(SystemUserEnum.SYSTEM_USER_ENUM.getUserId());
                        BeanUtils.copyProperties(trdPayFlow, trdPayFlowMsg);
                        trdPayFlowRepository.save(trdPayFlow);
                        broadcastMessageProducers.sendMessage(trdPayFlowMsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                } finally {
                    if(null == applyResult){
                        logger.error("failed to retrieve applyResult with pid:" + userPid + ""
                            + " and outsideOrdernu:"+ outsideOrderno);
                    }
                    logger.info("Sample job has finished...");
                }
            }
        }
    }

    private void updateByCheckAboutSumNum(TrdPayFlow trdPayFlow, ApplyResult applyResult){
        if (!StringUtils.isEmpty(applyResult.getTradeconfirmshare())) {
            trdPayFlow.setTradeConfirmShare(TradeUtil.getLongNumWithMul100
                (applyResult.getTradeconfirmshare()));
        }
        if (!StringUtils.isEmpty(applyResult.getTradeconfirmsum())) {
            trdPayFlow.setTradeConfirmSum(TradeUtil.getLongNumWithMul100
                (applyResult.getTradeconfirmsum()));
        }
        if (!StringUtils.isEmpty(applyResult.getApplyshare())) {
            trdPayFlow.setTradeTargetShare(TradeUtil.getLongNumWithMul100
                (applyResult.getApplyshare()));
        }
        if (!StringUtils.isEmpty(applyResult.getApplysum())) {
            trdPayFlow.setTradeTargetSum(TradeUtil.getLongNumWithMul100
                (applyResult.getApplysum()));
        }
    }

    public void checkReedemPayFlows(){
        //先查一遍赎回未确认状态的payFlow
        List<TrdPayFlow> trdPayFlows = trdPayFlowRepository
            .findAllByTradeConfirmShareIsAndTrdTypeIs(0L, TrdOrderOpTypeEnum.REDEEM.getOperation
                ());
        if(!CollectionUtils.isEmpty(trdPayFlows)) {
            ApplyResult applyResult = null;
            String userPid = null;
            String outsideOrderno = null;
            for (TrdPayFlow trdPayFlow : trdPayFlows) {
                try {
                    // TODO: replace userId with userUuid
                    userPid = orderService.getPidFromTrdAccoBrokerId(trdPayFlow);
                    outsideOrderno = trdPayFlow.getOutsideOrderno();
                    if(StringUtils.isEmpty(outsideOrderno)){
                        logger.error("if the outsideOrderno is empty, the payflow is of old "
                            + "process with outsideOrderno as the orderDetailId");
                        outsideOrderno = ""+trdPayFlow.getOrderDetailId();
                    }
                    applyResult = fundTradeApiService.getApplyResultByOutsideOrderNo
                        (TradeUtil.getZZOpenId(userPid), outsideOrderno);
                    if (null != applyResult && !StringUtils
                        .isEmpty(applyResult.getApplyshare())) {
                        com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlowMsg =
                            new com.shellshellfish.aaas.common.message.order.TrdPayFlow();
                        trdPayFlow.setUpdateBy(SystemUserEnum.SYSTEM_USER_ENUM.getUserId());
                        trdPayFlow.setUpdateDate(Instant.now().getEpochSecond());
                        trdPayFlow.setBuyDiscount(TradeUtil.getLongNumWithMul100(applyResult
                            .getCommisiondiscount()));
                        trdPayFlow.setOutsideOrderno(applyResult.getOutsideorderno
                            ());
                        trdPayFlow.setId(trdPayFlow.getId());
                        trdPayFlow.setApplySerial(applyResult.getApplyserial());
                        TrdOrderOpTypeEnum opTypeEnum = ZZStatsToOrdStatsUtils
                            .getTrdOrdOpTypeFromCallingCode(Integer
                                .valueOf(applyResult.getCallingcode()));
                        trdPayFlow.setTrdStatus(ZZStatsToOrdStatsUtils
                            .getOrdDtlStatFromZZStats(TrdZZCheckStatusEnum.getByStatus(
                                Integer.valueOf(applyResult.getConfirmflag())),opTypeEnum).getStatus());
                        trdPayFlow.setBuyFee(TradeUtil.getLongNumWithMul100(applyResult
                            .getPoundage()));
                        updateByCheckAboutSumNum(trdPayFlow, applyResult);


                        trdPayFlow.setOutsideOrderno(applyResult.getOutsideorderno());
                        trdPayFlow.setUpdateDate(TradeUtil.getUTCTime());
                        trdPayFlow.setUpdateBy(SystemUserEnum.SYSTEM_USER_ENUM.getUserId());
                        BeanUtils.copyProperties(trdPayFlow, trdPayFlowMsg);
                        trdPayFlowRepository.save(trdPayFlow);
                        broadcastMessageProducers.sendMessage(trdPayFlowMsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                } finally {
                    if(null == applyResult){
                        logger.error("failed to retrieve applyResult with pid:" + userPid + ""
                            + " and outsideOrdernu:"+ outsideOrderno);
                    }
                    logger.info("Sample job has finished...");
                }
            }
        }
    }
    public void executePreOrderStatus(){
        logger.info("The sample job has begun...");
        Instant.now().getEpochSecond();

        List<TrdPayFlow> trdPayFlows = trdPayFlowRepository
            .findAllByTradeConfirmShareIsAndTrdTypeIs(0L, TrdOrderOpTypeEnum.PREORDER.getOperation
                ());
        if(!CollectionUtils.isEmpty(trdPayFlows)) {
            for (TrdPayFlow trdPayFlow : trdPayFlows) {
                try {
                    com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlowMsg =
                    updateTrdPayFlowAndMakeMsg(trdPayFlow);
                    if(null != trdPayFlowMsg){
                        broadcastMessageProducers.sendMessage(trdPayFlowMsg);
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                }
            }
        }
    }


    private com.shellshellfish.aaas.common.message.order.TrdPayFlow updateTrdPayFlowAndMakeMsg
        (TrdPayFlow trdPayFlow) throws JsonProcessingException {
        // TODO: replace userId with userUuid
        String userId = null;
        if (trdPayFlow.getUserId() == 5605) {
            //这个用户是用uuid调的中证接口,以后走正式流程后都用userId来查中证接口
            userId = "shellshellfish";
        } else {
            userId = Long.toString(trdPayFlow.getUserId());
        }
        ApplyResult applyResult = fundTradeApiService.getApplyResultByOutsideOrderNo(userId, "" + trdPayFlow.getOrderDetailId());
        if (null != applyResult && !StringUtils
            .isEmpty(applyResult.getApplyshare())) {
            com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlowMsg =
                new com.shellshellfish.aaas.common.message.order.TrdPayFlow();
            trdPayFlowMsg.setUpdateBy(SystemUserEnum.SYSTEM_USER_ENUM.getUserId());
            trdPayFlowMsg.setUpdateDate(Instant.now().getEpochSecond());
            trdPayFlowMsg.setBuyDiscount(TradeUtil.getLongNumWithMul100(applyResult
                .getCommisiondiscount()));
            trdPayFlowMsg.setId(Long.valueOf(applyResult.getOutsideorderno()));
            trdPayFlowMsg.setApplySerial(applyResult.getApplyserial());
            TrdOrderOpTypeEnum opTypeEnum = ZZStatsToOrdStatsUtils
                .getTrdOrdOpTypeFromCallingCode(Integer
                    .valueOf(applyResult.getCallingcode()));
            trdPayFlowMsg.setTrdStatus(ZZStatsToOrdStatsUtils
                .getOrdDtlStatFromZZStats(TrdZZCheckStatusEnum.getByStatus(
                    Integer.valueOf(applyResult.getConfirmflag())), opTypeEnum).getStatus());
            trdPayFlowMsg.setBuyFee(TradeUtil.getLongNumWithMul100(applyResult
                .getPoundage()));
            if (!StringUtils.isEmpty(applyResult.getTradeconfirmshare())) {
                trdPayFlowMsg.setTradeConfirmShare(TradeUtil.getLongNumWithMul100
                    (applyResult.getTradeconfirmshare()));
            }
            if (!StringUtils.isEmpty(applyResult.getTradeconfirmsum())) {
                trdPayFlowMsg.setTradeConfirmSum(TradeUtil.getLongNumWithMul100
                    (applyResult.getTradeconfirmsum()));
            }
            if (!StringUtils.isEmpty(applyResult.getApplyshare())) {
                trdPayFlowMsg.setTradeTargetShare(TradeUtil.getLongNumWithMul100
                    (applyResult.getApplyshare()));
            }
            if (!StringUtils.isEmpty(applyResult.getApplysum())) {
                trdPayFlowMsg.setTradeTargetSum(TradeUtil.getLongNumWithMul100
                    (applyResult.getApplysum()));
            }
            trdPayFlowMsg.setOutsideOrderno(applyResult.getOutsideorderno());
            trdPayFlow.setUpdateDate(TradeUtil.getUTCTime());
            trdPayFlow.setUpdateBy(SystemUserEnum.SYSTEM_USER_ENUM.getUserId());
            BeanUtils.copyProperties(trdPayFlow, trdPayFlowMsg);
            trdPayFlowRepository.save(trdPayFlow);
            return trdPayFlowMsg;
        }
        return null;

    }
}
