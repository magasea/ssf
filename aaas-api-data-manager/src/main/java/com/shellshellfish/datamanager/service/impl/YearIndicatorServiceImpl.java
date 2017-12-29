package com.shellshellfish.datamanager.service.impl;


import com.shellshellfish.aaas.datamanager.YearIndicatorQuery;
import com.shellshellfish.aaas.datamanager.YearIndicatorRpc;
import com.shellshellfish.aaas.datamanager.YearIndicatorServiceGrpc.YearIndicatorServiceImplBase;
import com.shellshellfish.datamanager.model.FundYearIndicator;
import com.shellshellfish.datamanager.repositories.MongoFundYearIndicatorRepository;
import com.shellshellfish.datamanager.service.YearIndicatorService;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
public class YearIndicatorServiceImpl extends YearIndicatorServiceImplBase implements YearIndicatorService {


	@Autowired
	MongoFundYearIndicatorRepository fundYearIndicatorRepository;


	@Override
	public void getHistoryNetByCodeAndQuerydate(YearIndicatorQuery query, StreamObserver<YearIndicatorRpc> responseObserver) {
		List<FundYearIndicator> fundYearIndicatorList = fundYearIndicatorRepository.getHistoryNetByCodeAndQuerydate(query.getCode(), query.getQueryDate());

		YearIndicatorRpc.Builder builder = YearIndicatorRpc.newBuilder();
		if (fundYearIndicatorList != null && fundYearIndicatorList.size() > 0) {
			copyYearIndicatorProperities(builder,fundYearIndicatorList.get(0));
		}

		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
	}


	/**
	 * @param builder
	 * @param fundYearIndicator
	 */
	private void copyYearIndicatorProperities(YearIndicatorRpc.Builder builder, FundYearIndicator fundYearIndicator) {

		if (!StringUtils.isEmpty(fundYearIndicator.getCode()))
			builder.setCode(fundYearIndicator.getCode());

		if (!StringUtils.isEmpty(fundYearIndicator.getEnddate()))
			builder.setEnddate(fundYearIndicator.getEnddate());


		if (!StringUtils.isEmpty(fundYearIndicator.getNavaccum()))
			builder.setNavaccum(fundYearIndicator.getNavaccum());

		if (!StringUtils.isEmpty(fundYearIndicator.getNavaccumreturnp()))
			builder.setNavaccumreturnp(fundYearIndicator.getNavaccumreturnp());

		if (!StringUtils.isEmpty(fundYearIndicator.getNavunit()))
			builder.setNavunit(fundYearIndicator.getNavunit());

		if (!StringUtils.isEmpty(fundYearIndicator.getQuerydate()))
			builder.setQuerydate(fundYearIndicator.getQuerydate());

		if (!StringUtils.isEmpty(fundYearIndicator.getStdate()))
			builder.setStdate(fundYearIndicator.getStdate());


	}
}
