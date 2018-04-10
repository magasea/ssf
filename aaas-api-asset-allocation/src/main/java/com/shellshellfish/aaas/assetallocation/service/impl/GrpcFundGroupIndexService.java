package com.shellshellfish.aaas.assetallocation.service.impl;

import com.shellshellfish.aaas.asset.allocation.AssetAllocationServiceGrpc;
import com.shellshellfish.aaas.asset.allocation.FundGroupIndexQuery;
import com.shellshellfish.aaas.asset.allocation.FundGroupIndexResult;
import com.shellshellfish.aaas.assetallocation.neo.entity.FundGroupIndex;
import com.shellshellfish.aaas.assetallocation.neo.mapper.FundGroupHistoryMapper;
import com.shellshellfish.aaas.assetallocation.neo.mapper.FundGroupIndexMapper;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GrpcFundGroupIndexService extends AssetAllocationServiceGrpc.AssetAllocationServiceImplBase {

    @Autowired
    FundGroupIndexMapper fundGroupIndexMapper;

    @Autowired
    FundGroupHistoryMapper fundGroupHistoryMapper;

    @Override
    public void getAnnualVolatilityAndAnnualYield(FundGroupIndexQuery request,
                                                  StreamObserver<FundGroupIndexResult> responseObserver) {
        String groupId = request.getGroupId();
        String subGroupId = request.getSubGroupId();
        FundGroupIndex fundGroupIndex = fundGroupIndexMapper.findByGroupIdAndSubGroupId(groupId, subGroupId);
        Double maxRetracement = Optional.ofNullable(fundGroupHistoryMapper.getMaxDrawDown(groupId, subGroupId)).orElse(0D);
        FundGroupIndexResult.Builder builder = FundGroupIndexResult.newBuilder();
        builder.setHistoricalAnnualVolatility(fundGroupIndex.getHistoricalAnnualVolatility());
        builder.setHistoricalAnnualYeild(fundGroupIndex.getHistoricalAnnualYield());
        builder.setMaxRetracement(maxRetracement);
        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

}
