package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.finance.trade.pay.FundNetInfo;
import com.shellshellfish.aaas.finance.trade.pay.FundNetQuery;
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc.PayRpcServiceBlockingStub;
import com.shellshellfish.aaas.userinfo.service.PayGrpcService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
