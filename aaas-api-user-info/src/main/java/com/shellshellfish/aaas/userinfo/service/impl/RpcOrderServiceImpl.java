package com.shellshellfish.aaas.userinfo.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shellshellfish.aaas.finance.trade.order.BindCardInfo;
import com.shellshellfish.aaas.finance.trade.order.BindCardResult;
import com.shellshellfish.aaas.finance.trade.order.OrderDetail;
import com.shellshellfish.aaas.finance.trade.order.OrderDetailQueryInfo;
import com.shellshellfish.aaas.finance.trade.order.OrderDetailResult;
import com.shellshellfish.aaas.finance.trade.order.OrderQueryInfo;
import com.shellshellfish.aaas.finance.trade.order.OrderResult;
import com.shellshellfish.aaas.finance.trade.order.OrderRpcServiceGrpc.OrderRpcServiceBlockingStub;

import com.shellshellfish.aaas.userinfo.exception.UserInfoException;
import com.shellshellfish.aaas.userinfo.model.dao.UiUser;
import com.shellshellfish.aaas.userinfo.model.dto.BankCardDTO;
import com.shellshellfish.aaas.userinfo.model.dto.BankcardDetailBodyDTO;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.service.RpcOrderService;
import com.shellshellfish.aaas.userinfo.service.UserInfoService;
import com.shellshellfish.aaas.userinfo.utils.BankUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @Author pierre 18-1-31
 */
@Service
public class RpcOrderServiceImpl implements RpcOrderService {


	private static  final Logger logger = LoggerFactory.getLogger(RpcOrderServiceImpl.class);

	@Autowired
	OrderRpcServiceBlockingStub orderRpcServiceBlockingStub;


	@Autowired
	UserInfoService userInfoService;

	@Autowired
	UserInfoRepository userInfoRepository;

	@Override
	public OrderResult getOrderInfoByProdIdAndOrderStatus(Long userProdId, Integer orderStatus) {
		OrderQueryInfo.Builder builder = OrderQueryInfo.newBuilder();
		builder.setOrderStatus(orderStatus);
		builder.setUserProdId(userProdId);

		OrderResult orderResult = orderRpcServiceBlockingStub.getOrderInfo(builder.build());


		return orderResult;
	}

	@Override
	public List<OrderDetail> getOrderDetails(Long userProdId, Integer orderDetailStatus) {
		OrderDetailQueryInfo.Builder builder = OrderDetailQueryInfo.newBuilder();
		builder.setOrderDetailStatus(orderDetailStatus);
		builder.setUserProdId(userProdId);

		OrderDetailResult orderDetailResult = orderRpcServiceBlockingStub
				.getOrderDetail(builder.build());
		return orderDetailResult.getOrderDetailResultList();

	}

	@Override
	public String openAccount(BankcardDetailBodyDTO bankcardDetailBodyDTO) throws Exception {

		BindCardInfo.Builder bankCardInfo = BindCardInfo.newBuilder();
		bankCardInfo.setBankName(bankcardDetailBodyDTO.getBankName());
		bankCardInfo.setCardNo(bankcardDetailBodyDTO.getCardNumber());
		bankCardInfo.setIdCardNo(bankcardDetailBodyDTO.getCardUserPid());
		bankCardInfo.setUserName(bankcardDetailBodyDTO.getCardUserName());
		bankCardInfo.setUserId(bankcardDetailBodyDTO.getUserId());

		bankCardInfo.setUserPhone(bankcardDetailBodyDTO.getCardCellphone());
		Optional<UiUser> uiUser = userInfoRepository.findById(bankcardDetailBodyDTO.getUserId());
		if(null != uiUser.get().getRiskLevel()){
			bankCardInfo.setRiskLevel(uiUser.get().getRiskLevel());
		}
		BindCardResult bindCardResult = orderRpcServiceBlockingStub.openAccount(bankCardInfo.build());
		String tradeacco = bindCardResult.getTradeacco();

		if (tradeacco == null || tradeacco.equals("-1")) {
			throw new Exception(bindCardResult.getErrInfo().getErrMsg());
		}

		return tradeacco;
	}

	@Override
	public BankCardDTO createBankCard(BankcardDetailBodyDTO bankcardDetailVo) throws Exception {
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
				logger.error("此卡暂不支持!");
				throw new UserInfoException("404", "此卡暂不支持!");
			}
		}
		params.put("bankName", object.toString());
		bankcardDetailVo.setBankName(object.toString());

		UiUser uiUser = userInfoRepository.findByUuid(bankcardDetailVo.getUserUuid());

		if (uiUser == null) {
			logger.error("当前用户不存在");
			throw new UserInfoException("404", "当前用户不存在");
		}

		bankcardDetailVo.setUserId(uiUser.getId());
		params.put("userId",uiUser.getId());
		params.forEach((k, v) -> {
			if (null == v || StringUtils.isEmpty(v.toString())) {
				logger.error("no {}'s value in params", k.toString());
				throw new IllegalArgumentException("no " + k.toString() + "'s value in params");
			}
		});

		BankCardDTO bankIsExist = userInfoService.getUserInfoBankCard(bankcardDetailVo.getCardNumber());
		if (bankIsExist != null && bankIsExist.getCardNumber() != null) {
			logger.error("银行卡号已经存在，请重新输入");
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
