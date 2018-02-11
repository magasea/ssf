package com.shellshellfish.aaas.userinfo.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shellshellfish.aaas.finance.trade.grpc.BindCardInfo;
import com.shellshellfish.aaas.finance.trade.grpc.TradeServiceGrpc;
import com.shellshellfish.aaas.finance.trade.pay.FundNetInfo;
import com.shellshellfish.aaas.finance.trade.pay.FundNetInfos;
import com.shellshellfish.aaas.finance.trade.pay.FundNetQuery;
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc.PayRpcServiceBlockingStub;
import com.shellshellfish.aaas.userinfo.exception.UserInfoException;
import com.shellshellfish.aaas.userinfo.model.dao.UiUser;
import com.shellshellfish.aaas.userinfo.model.dto.BankCardDTO;
import com.shellshellfish.aaas.userinfo.model.dto.BankcardDetailBodyDTO;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.service.PayGrpcService;
import com.shellshellfish.aaas.userinfo.service.UserInfoService;
import com.shellshellfish.aaas.userinfo.utils.BankUtil;
import java.util.HashMap;
import java.util.List;
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
public class PayGrpcServiceImpl implements PayGrpcService {

	private static final Logger logger = LoggerFactory.getLogger(PayGrpcServiceImpl.class);

	@Autowired
	PayRpcServiceBlockingStub payRpcServiceBlockingStub;

	@Override
	public List<FundNetInfo> getFundNetInfosFromZZ(String userPid, String fundCode, int days) {
		FundNetQuery.Builder fnqBuilder = FundNetQuery.newBuilder();
		fnqBuilder.setUserPid(userPid);
		fnqBuilder.addFundCode(fundCode);
		fnqBuilder.setTradeDays(days);
		return payRpcServiceBlockingStub.getLatestFundNet(fnqBuilder.build()).getFundNetInfoList();

	}


}
