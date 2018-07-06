package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.finance.trade.order.service.UserInfoService;
import com.shellshellfish.aaas.userinfo.grpc.ConfirmedOutsideOrderIds;
import com.shellshellfish.aaas.userinfo.grpc.SellPersentProducts;
import com.shellshellfish.aaas.userinfo.grpc.SellProducts;
import com.shellshellfish.aaas.userinfo.grpc.SellProductsResult;
import com.shellshellfish.aaas.userinfo.grpc.UserBankInfo;
import com.shellshellfish.aaas.userinfo.grpc.UserId;
import com.shellshellfish.aaas.userinfo.grpc.UserIdOrUUIDQuery;
import com.shellshellfish.aaas.userinfo.grpc.UserInfo;
import com.shellshellfish.aaas.userinfo.grpc.UserInfoServiceGrpc;
import com.shellshellfish.aaas.userinfo.grpc.UserInfoServiceGrpc.UserInfoServiceFutureStub;
import io.grpc.ManagedChannel;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by chenwei on 2018- 一月 - 17
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
	Logger logger = LoggerFactory.getLogger(getClass());

	UserInfoServiceFutureStub userInfoServiceFutureStub;

	@Autowired
	ManagedChannel managedUIChannel;

	@PostConstruct
	public void init() {
		userInfoServiceFutureStub = UserInfoServiceGrpc.newFutureStub(managedUIChannel);
	}

	@Override
	public UserBankInfo getUserBankInfo(Long userId) throws ExecutionException, InterruptedException {
		UserIdOrUUIDQuery.Builder builder = UserIdOrUUIDQuery.newBuilder();
		builder.setUserId(userId);

		com.shellshellfish.aaas.userinfo.grpc.UserBankInfo userBankInfo =
				userInfoServiceFutureStub.getUserBankInfo(builder.build()).get();
		return userBankInfo;
	}


	@Override
	public UserBankInfo getUserBankInfo(String userUUID) throws ExecutionException,
			InterruptedException {
		UserIdOrUUIDQuery.Builder builder = UserIdOrUUIDQuery.newBuilder();
		builder.setUuid(userUUID);
		com.shellshellfish.aaas.userinfo.grpc.UserBankInfo userBankInfo =
				userInfoServiceFutureStub.getUserBankInfo(builder.build()).get();
		return userBankInfo;
	}

	@Override
	public UserInfo getUserInfoByUserId(Long userId) throws ExecutionException, InterruptedException {
		UserId.Builder uiBuilder = UserId.newBuilder();
		uiBuilder.setUserId(userId);
		return userInfoServiceFutureStub.getUserInfo(uiBuilder.build()).get();

	}

	@Override
	public SellProductsResult checkSellProducts(SellProducts sellProducts)
			throws ExecutionException, InterruptedException {
		return userInfoServiceFutureStub.sellUserProducts(sellProducts).get();

	}

	@Override
	public SellProductsResult checkSellPercentProducts(SellPersentProducts sellPersentProducts)
			throws ExecutionException, InterruptedException {
		return userInfoServiceFutureStub.sellPersentUserProducts(sellPersentProducts).get();
	}

	@Override
  public SellProducts rollbackSellProducts(SellProducts sellProducts)
      throws ExecutionException, InterruptedException {
    return userInfoServiceFutureStub.rollbackUserProducts(sellProducts).get();
  }

	@Override
	public String getUserPidByBankCard(String bankCardNo) {
		UserIdOrUUIDQuery.Builder builder = UserIdOrUUIDQuery.newBuilder();
		if(StringUtils.isEmpty(bankCardNo)){
			logger.error("invalid bankcardNo:{}", bankCardNo);
			return null;
		}
		builder.setBankCardNo(bankCardNo);
		UserBankInfo userBankInfo = null;
		try {
			userBankInfo = userInfoServiceFutureStub.getUserBankInfo(builder.build()).get();
		} catch (Exception e) {
			logger.error("error calling userInfoServiceFutureStub.getUserBankInfo", e);
			return null;
		}
		return userBankInfo.getUserPid();
	}

	@Override
	public List<String> getNeedHandleOutsideOrderIds(List<String> outsideOrderIds) {
		ConfirmedOutsideOrderIds.Builder cfooiBuilder = ConfirmedOutsideOrderIds.newBuilder();
		outsideOrderIds.forEach(
				outsideOrderId -> cfooiBuilder.addOutsideOrderId(outsideOrderId)
		);
		try {
			return userInfoServiceFutureStub.checkAbsentPendingRecordsOrders(cfooiBuilder.build()).get().getOutsideOrderIdList();
		} catch (Exception e) {
			logger.error("Error:",e);
		}
		return  null;
	}
}
