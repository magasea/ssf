package com.shellshellfish.aaas.finance.trade.pay.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shellshellfish.aaas.common.enums.SystemUserEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.enums.TrdZZCheckStatusEnum;
import com.shellshellfish.aaas.common.message.order.MongoUiTrdZZInfo;
import com.shellshellfish.aaas.common.utils.MyBeanUtils;
import com.shellshellfish.aaas.common.utils.ZZStatsToOrdStatsUtils;
import com.shellshellfish.aaas.finance.trade.pay.message.BroadcastMessageProducers;
import com.shellshellfish.aaas.common.grpc.trade.pay.ApplyResult;
import com.shellshellfish.aaas.finance.trade.pay.model.ConfirmResult;
import com.shellshellfish.aaas.finance.trade.pay.model.dao.mysql.TrdPayFlow;

import com.shellshellfish.aaas.finance.trade.pay.repositories.mysql.TrdPayFlowRepository;
import com.shellshellfish.aaas.finance.trade.pay.service.FundTradeApiService;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.pay.service.OrderService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class CheckFundsTradeJobService {

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
            String applySerial = null;
            List<TrdPayFlow> trdPayFlowListToGetConfirmInfo = new ArrayList<>();
            for (TrdPayFlow trdPayFlow : trdPayFlows) {
                try {
                    // TODO: replace userId with userUuid
                    userPid = orderService.getPidFromTrdAccoBrokerId(trdPayFlow);
                    applySerial = trdPayFlow.getApplySerial();
                    if(StringUtils.isEmpty(applySerial)){
                        logger.error("if the applySerial is empty, the payflow is of not need to "
                            + "check ");
                        continue;
                    }
                    applyResult = fundTradeApiService.getApplyResultByApplySerial
                        (TradeUtil.getZZOpenId(userPid), applySerial);
                    if (null != applyResult && !StringUtils
                        .isEmpty(applyResult.getApplyshare())) {
                        com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlowMsg =
                            new com.shellshellfish.aaas.common.message.order.TrdPayFlow();
                        trdPayFlow.setUpdateBy(SystemUserEnum.SYSTEM_USER_ENUM.getUserId());
                        trdPayFlow.setUpdateDate(Instant.now().getEpochSecond());
                        trdPayFlow.setTrdApplyDate(applyResult.getApplydate());
                        trdPayFlow.setBuyDiscount(TradeUtil.getLongNumWithMul100(applyResult
                            .getCommisiondiscount()));
                        trdPayFlow.setOutsideOrderno(applyResult.getOutsideorderno
                            ());
                        trdPayFlow.setId(trdPayFlow.getId());
                        trdPayFlow.setApplySerial(applyResult.getApplyserial());
                        TrdOrderOpTypeEnum opTypeEnum = ZZStatsToOrdStatsUtils
                            .getTrdOrdOpTypeFromCallingCode(Integer
                                .valueOf(applyResult.getCallingcode()));
                        int queryStatus = ZZStatsToOrdStatsUtils
                            .getOrdDtlStatFromZZStats(TrdZZCheckStatusEnum.getByStatus(
                                Integer.valueOf(applyResult.getConfirmflag())),opTypeEnum)
                            .getStatus();
                        if(trdPayFlow.getTrdStatus() == queryStatus){
                            logger.error("There is no status change for applySerial:{}, current "
                                    + "status:{} queryStatus:{}", applySerial, trdPayFlow
                                .getTrdStatus(), queryStatus);
                            continue;
                        }
                        trdPayFlow.setTrdStatus(ZZStatsToOrdStatsUtils
                            .getOrdDtlStatFromZZStats(TrdZZCheckStatusEnum.getByStatus(
                                Integer.valueOf(applyResult.getConfirmflag())),opTypeEnum).getStatus());
                        if(trdPayFlow.getTrdStatus() == TrdOrderStatusEnum.CONFIRMED.getStatus()){
                            trdPayFlowListToGetConfirmInfo.add(trdPayFlow);
                        }
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
                } catch (Exception ex) {
                    logger.error("exception:",ex);

                } finally {
                    if(null == applyResult){
                        logger.error("failed to retrieve applyResult with pid:" + userPid + ""
                            + " and applySerial:"+ applySerial);
                    }
                    logger.info("Sample job has finished...");
                }
            }
            CheckAndSendConfirmInfo(trdPayFlowListToGetConfirmInfo);
        }
    }

    private void CheckAndSendConfirmInfo(List<TrdPayFlow> trdPayFlowListToGetConfirmInfo) {
        if(CollectionUtils.isEmpty(trdPayFlowListToGetConfirmInfo)){
            logger.info("there is no confirm trdPayFlow to handle");
            return;
        }
        String userPid;
        String applySerial;
        for(TrdPayFlow trdPayFlow: trdPayFlowListToGetConfirmInfo){
            try {
                userPid = orderService.getPidFromTrdAccoBrokerId(trdPayFlow);
            } catch (Exception ex) {
                logger.error("exception:",ex);
                logger.error("failed to retrieve userPid for trdPayFlow with userId:{} in "
                    + "applySerial:{}"+ trdPayFlow.getUserId(), trdPayFlow.getApplySerial());
                continue;
            }
            applySerial = trdPayFlow.getApplySerial();
            if(StringUtils.isEmpty(applySerial)){
                logger.error("the applySerial is empty, the payflow is not need to be handle");
                continue;
            }
            List<ConfirmResult> confirmResults = null;
            try{
                confirmResults = fundTradeApiService.getConfirmResultsBySerial(TradeUtil.getZZOpenId
                    (userPid), applySerial);
            }catch (Exception ex){
                logger.error("exception:",ex);
                logger.error("failed to get confirmResults with userPid:" + userPid + " "
                    + "applySerial:" + applySerial + " errMsg:" + ex.getMessage());
            }
            //now compond the message for sending
            if(CollectionUtils.isEmpty(confirmResults)){
               logger.error("there is no confirm information for applySerial:" + applySerial);
               continue;
            }
            ConfirmResult confirmResult = confirmResults.get(0);
            MongoUiTrdZZInfo mongoUiTrdZZInfo = new MongoUiTrdZZInfo();
            MyBeanUtils.mapEntityIntoDTO(trdPayFlow, mongoUiTrdZZInfo);
            MyBeanUtils.mapEntityIntoDTO(confirmResult, mongoUiTrdZZInfo);
            mongoUiTrdZZInfo.setBankName(confirmResult.getBankname());
            mongoUiTrdZZInfo.setBankAcco(confirmResult.getBankacco());
            mongoUiTrdZZInfo.setBusinFlagStr(confirmResult.getBusinflagStr());
            mongoUiTrdZZInfo.setApplyDate(confirmResult.getApplydate());
            mongoUiTrdZZInfo.setApplySerial(confirmResult.getApplyserial());
            mongoUiTrdZZInfo.setTradeStatus(trdPayFlow.getTrdStatus());
            mongoUiTrdZZInfo.setConfirmDate(confirmResult.getConfirmdate());
            mongoUiTrdZZInfo.setOutSideOrderNo(confirmResult.getOutsideorderno());
            mongoUiTrdZZInfo.setTradeType(trdPayFlow.getTrdType());
            mongoUiTrdZZInfo.setMelonMethod(confirmResult.getMelonmethod());
            mongoUiTrdZZInfo.setOriApplyDate(confirmResult.getOriapplydate());
            mongoUiTrdZZInfo.setBankSerial(confirmResult.getBankSerial());
            mongoUiTrdZZInfo.setFee(TradeUtil.getLongNumWithMul100(confirmResult.getPoundage()));
            broadcastMessageProducers.sendConfirmMessage(mongoUiTrdZZInfo);
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
            .findAllByTradeConfirmSumIsAndTrdTypeIs(0L, TrdOrderOpTypeEnum.REDEEM.getOperation
                ());
        if(!CollectionUtils.isEmpty(trdPayFlows)) {
            ApplyResult applyResult = null;
            String userPid = null;
//            String outsideOrderno = null;
            String applySerial = null;
            List<TrdPayFlow> trdPayFlowListToGetConfirmInfo = new ArrayList<>();
            for (TrdPayFlow trdPayFlow : trdPayFlows) {
                try {
                    // TODO: replace userId with userUuid
                    userPid = orderService.getPidFromTrdAccoBrokerId(trdPayFlow);
//                    outsideOrderno = trdPayFlow.getOutsideOrderno();
                    applySerial = trdPayFlow.getApplySerial();
                    if(StringUtils.isEmpty(applySerial)){
                        logger.error("if the applySerial is empty , the payFlow is of Id:{}",
                            trdPayFlow.getId());
                    }
                    applyResult = fundTradeApiService.getApplyResultByApplySerial
                        (TradeUtil.getZZOpenId(userPid), applySerial);
                    if (null != applyResult && !StringUtils
                        .isEmpty(applyResult.getApplyshare())) {
                        com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlowMsg =
                            new com.shellshellfish.aaas.common.message.order.TrdPayFlow();
                        trdPayFlow.setUpdateBy(SystemUserEnum.SYSTEM_USER_ENUM.getUserId());
                        trdPayFlow.setUpdateDate(Instant.now().getEpochSecond());
                        if(!StringUtils.isEmpty(applyResult.getApplydate())){
                            trdPayFlow.setTrdApplyDate(applyResult.getApplydate());
                        }else{
                            trdPayFlow.setTrdApplyDate("-1");
                        }
                        if(!StringUtils.isEmpty(applyResult.getTradeconfirmsum()) &&
                        !StringUtils.isEmpty(applyResult.getTradeconfirmshare())){
                            trdPayFlow.setApplydateUnitvalue(TradeUtil.getLongFromDividByBD(applyResult
                                    .getTradeconfirmsum(), applyResult.getTradeconfirmshare()));
                        }else{
                            trdPayFlow.setApplydateUnitvalue(-1);
                        }

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

                        trdPayFlow.setTrdType(opTypeEnum.getOperation());
                        trdPayFlow.setOutsideOrderno(applyResult.getOutsideorderno());
                        trdPayFlow.setUpdateDate(TradeUtil.getUTCTime());
                        trdPayFlow.setUpdateBy(SystemUserEnum.SYSTEM_USER_ENUM.getUserId());
                        BeanUtils.copyProperties(trdPayFlow, trdPayFlowMsg);
                        trdPayFlowRepository.save(trdPayFlow);
                        broadcastMessageProducers.sendMessage(trdPayFlowMsg);
                        if(trdPayFlow.getTrdStatus() == TrdOrderStatusEnum.CONFIRMED.getStatus()
                            || trdPayFlow.getTrdStatus() == TrdOrderStatusEnum.SELLCONFIRMED.getStatus()){
                            trdPayFlowListToGetConfirmInfo.add(trdPayFlow);
                        }
                    }
                } catch (Exception ex) {
                    logger.error("exception:",ex);

                } finally {
                    if(null == applyResult){
                        logger.error("failed to retrieve applyResult with pid:" + userPid + ""
                            + " and applySerial:"+ applySerial);
                    }
                    logger.info("Sample job has finished...");
                }
            }
            CheckAndSendConfirmInfo(trdPayFlowListToGetConfirmInfo);
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
                } catch (JsonProcessingException ex) {
                    logger.error("exception:",ex);

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
            if(!StringUtils.isEmpty(applyResult.getApplydate())){
                trdPayFlow.setTrdApplyDate(applyResult.getApplydate());
            }else{
                trdPayFlow.setTrdApplyDate("-1");
            }
            if(!StringUtils.isEmpty(applyResult.getTradeconfirmsum()) &&
                !StringUtils.isEmpty(applyResult.getTradeconfirmshare())){
                trdPayFlow.setApplydateUnitvalue(TradeUtil.getLongFromDividByBD(applyResult
                    .getTradeconfirmsum(), applyResult.getTradeconfirmshare()));
            }else{
                trdPayFlow.setApplydateUnitvalue(-1);
            }
            BeanUtils.copyProperties(trdPayFlow, trdPayFlowMsg);
            trdPayFlowRepository.save(trdPayFlow);
            return trdPayFlowMsg;
        }
        return null;

    }
}
