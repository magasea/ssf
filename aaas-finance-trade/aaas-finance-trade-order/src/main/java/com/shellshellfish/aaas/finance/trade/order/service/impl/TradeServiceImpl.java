package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.common.enums.TradeBrokerIdEnum;
import com.shellshellfish.aaas.common.grpc.trade.pay.BindBankCard;
import com.shellshellfish.aaas.common.utils.BankUtil;
import com.shellshellfish.aaas.finance.trade.grpc.BindCardInfo;
import com.shellshellfish.aaas.finance.trade.grpc.BindCardResult;
import com.shellshellfish.aaas.finance.trade.grpc.TradeServiceGrpc.TradeServiceImplBase;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdTradeBankDic;
import com.shellshellfish.aaas.finance.trade.order.repositories.mysql.TrdTradeBankDicRepository;
import com.shellshellfish.aaas.finance.trade.order.service.PayService;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

/**
 * @Author pierre
 * 18-1-22
 */
@Service
public class TradeServiceImpl extends TradeServiceImplBase {

	private static final Logger logger = LoggerFactory.getLogger(TradeServiceImpl.class);

	@Autowired
	PayService payService;


	@Autowired
	TrdTradeBankDicRepository trdTradeBankDicRepository;

	public void bindBankCard(BindCardInfo bankCardInfo,
							 StreamObserver<BindCardResult> responseObserver) {
		BindCardResult.Builder resultBuilder = BindCardResult.newBuilder();

		final String errorMsg = "-1";

		String bankCardNo = bankCardInfo.getCardNo();
		String bankName = BankUtil.getNameOfBank(bankCardNo);
		bankName = BankUtil.getZZBankNameFromOriginBankName(bankName);

		TrdTradeBankDic trdTradeBankDic = trdTradeBankDicRepository.findByBankNameAndTraderBrokerId
				(bankName, TradeBrokerIdEnum.ZhongZhenCaifu.getTradeBrokerId());

		String tradeNo;
		if (trdTradeBankDic != null) {
			BindBankCard bindBankCard = new BindBankCard();
			bindBankCard.setBankCardNum(bankCardInfo.getCardNo());
			bindBankCard.setUserId(bankCardInfo.getUserId());
			bindBankCard.setBankCode(trdTradeBankDic.getBankCode());
			bindBankCard.setCellphone(bankCardInfo.getUserPhone());
			bindBankCard.setTradeBrokerId(trdTradeBankDic.getTraderBrokerId());
			bindBankCard.setUserPid(bankCardInfo.getIdCardNo());
			bindBankCard.setUserName(bankCardInfo.getUserName());

			try {
				tradeNo = payService.bindCard(bindBankCard);
			} catch (ExecutionException e) {
				logger.error(e.getMessage());
				tradeNo = errorMsg;
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
				tradeNo = errorMsg;
			}
		} else {
			tradeNo = errorMsg;
		}

		if (tradeNo == null)
			tradeNo = errorMsg;

		resultBuilder.setTradeacco(tradeNo);

		responseObserver.onNext(resultBuilder.build());
		responseObserver.onCompleted();

	}
}
