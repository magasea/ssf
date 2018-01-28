package com.shellshellfish.aaas.userinfo.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shellshellfish.aaas.finance.trade.grpc.BindCardInfo;
import com.shellshellfish.aaas.finance.trade.grpc.TradeServiceGrpc;
import com.shellshellfish.aaas.userinfo.exception.UserInfoException;
import com.shellshellfish.aaas.userinfo.model.dao.UiUser;
import com.shellshellfish.aaas.userinfo.model.dto.BankCardDTO;
import com.shellshellfish.aaas.userinfo.model.dto.BankcardDetailBodyDTO;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.service.OpenAccountService;
import com.shellshellfish.aaas.userinfo.service.UserInfoService;
import com.shellshellfish.aaas.userinfo.utils.BankUtil;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @Author pierre 18-1-20
 */

@Service
public class OpenAccountServiceImpl implements OpenAccountService {

	private static final Logger logger = LoggerFactory.getLogger(OpenAccountServiceImpl.class);


	@Autowired
	TradeServiceGrpc.TradeServiceBlockingStub tradeOrderServiceBlockingStub;


	@Autowired
	UserInfoService userInfoService;


	@Autowired
	UserInfoRepository userInfoRepository;


	@Override
	public String openAccount(BankcardDetailBodyDTO bankcardDetailBodyDTO) {
		final String errMsg = "-1";
		BindCardInfo.Builder bankCardInfo = BindCardInfo.newBuilder();
		bankCardInfo.setBankName(bankcardDetailBodyDTO.getBankName());
		bankCardInfo.setCardNo(bankcardDetailBodyDTO.getCardNumber());
		bankCardInfo.setIdCardNo(bankcardDetailBodyDTO.getCardUserPid());
		bankCardInfo.setUserName(bankcardDetailBodyDTO.getCardUserName());
		bankCardInfo.setUserId(bankcardDetailBodyDTO.getUserId());
		bankCardInfo.setUserPhone(bankcardDetailBodyDTO.getCardCellphone());

		String tradeacco = tradeOrderServiceBlockingStub.bindBankCard(bankCardInfo.build())
				.getTradeacco();

		if (tradeacco == null || errMsg.equals(tradeacco)) {
			return errMsg;
		}

		return tradeacco;
	}

	@Override
	public BankCardDTO createBankCard(BankcardDetailBodyDTO bankcardDetailVo) {
		logger.info("addBankCardWithDetailInfo method run..");
		Map<String, Object> result = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> params = mapper
				.convertValue(bankcardDetailVo, new TypeReference<Map<String, Object>>() {
				});

		Object object = params.get("bankName");
		if (object == null || "".equals(object)) {
			object = BankUtil.getNameOfBank(params.get("cardNumber").toString());
			if (StringUtils.isEmpty(object)) {
				throw new UserInfoException("404", "银行卡号不正确");
			}
		}
		params.put("bankName", object.toString());
		bankcardDetailVo.setBankName(object.toString());

		UiUser uiUser = userInfoRepository.findByUuid(bankcardDetailVo.getUserUuid());

		if (uiUser == null) {
			throw new UserInfoException("404", "当前用户不存在");
		}

		bankcardDetailVo.setUserId(uiUser.getId());
		params.put("userId",uiUser.getId());
		params.forEach((k, v) -> {
			if (null == v || StringUtils.isEmpty(v.toString())) {
				throw new IllegalArgumentException("no " + k.toString() + "'s value in params");
			}
		});

		BankCardDTO bankIsExist = userInfoService.getUserInfoBankCard(bankcardDetailVo.getCardNumber());
		if (bankIsExist != null && bankIsExist.getCardNumber() != null) {
			throw new UserInfoException("404", "银行卡号已经存在，请重新输入");
		}

		/*中正开户，正常开户（银行卡相关信息正确）之后才能才能保存银行卡信息*/
		String tradeNo = openAccount(bankcardDetailVo);

		if (tradeNo == null || "-1".equals(tradeNo)) {
			logger.error("failed to open an account in zhongzheng");
			result.put("msg", "开户失败！");
			return null;
		}

		logger.info("success to open an account in zhongzheng tradeacco:{}",tradeNo);

		BankCardDTO bankCard;
		try {
			bankCard = userInfoService.createBankcard(params);
		} catch (Exception e) {
			logger.error(e.getMessage());
			bankCard = null;
		}
		return bankCard;
	}
}
