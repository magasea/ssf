package com.shellshellfish.aaas.finance.trade.pay.service.impl;

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

    public void executeSampleJob() {

        logger.info("The sample job has begun...");
        Instant.now().getEpochSecond();

            List<TrdPayFlow> trdPayFlows = trdPayFlowRepository
                .findAllByFundSumConfirmedIsAndPayTypeIs(0L, TrdOrderOpTypeEnum.BUY.getOperation
                    ());
            if(!CollectionUtils.isEmpty(trdPayFlows)) {
                for (TrdPayFlow trdPayFlow : trdPayFlows) {
                    try {
                        // TODO: replace userId with userUuid
                        String userId = null;
                        if (trdPayFlow.getUserId() == 5605) {
                            //这个用户是用uuid调的中证接口,以后走正式流程后都用userId来查中证接口
                            userId = "shellshellfish";
                        } else {
                            userId = Long.toString(trdPayFlow.getUserId());
                        }
                        ApplyResult applyResult = fundTradeApiService.getApplyResultByOutsideOrderNo
                            (userId, "" + trdPayFlow.getOrderDetailId());
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
//                            TrdOrderStatusEnum trdOrderStatusEnum = ZZStatsToOrdStatsUtils
//                                .getOrdDtlStatFromZZStats
//                                (TrdZZCheckStatusEnum
//                                .getByStatus(Integer.valueOf(applyResult.getConfirmflag())),
//                                opTypeEnum);
                            trdPayFlowMsg.setTrdStatus(ZZStatsToOrdStatsUtils
                                .getOrdDtlStatFromZZStats(TrdZZCheckStatusEnum.getByStatus(
                                    Integer.valueOf(applyResult.getConfirmflag())),opTypeEnum).getStatus());
                            trdPayFlowMsg.setBuyFee(TradeUtil.getLongNumWithMul100(applyResult
                                .getPoundage()));
                            if(!StringUtils.isEmpty(applyResult.getTradeconfirmshare())){
                                trdPayFlowMsg.setFundSumConfirmed(TradeUtil. getLongNumWithMul100
                                    (applyResult.getTradeconfirmshare()));
                            }
                            trdPayFlowMsg.setOutsideOrderno(applyResult.getOutsideorderno());
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
                        logger.info("Sample job has finished...");
                    }
                }
            }
    }
}
