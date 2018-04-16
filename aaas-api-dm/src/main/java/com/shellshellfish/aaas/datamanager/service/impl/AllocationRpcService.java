package com.shellshellfish.aaas.datamanager.service.impl;

import com.shellshellfish.aaas.asset.allocation.AssetAllocationServiceGrpc.AssetAllocationServiceBlockingStub;
import com.shellshellfish.aaas.asset.allocation.FundGroupIndexQuery;
import com.shellshellfish.aaas.asset.allocation.FundGroupIndexResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AllocationRpcService {


    @Autowired
    AssetAllocationServiceBlockingStub allocationBlockingStub;


    public FundGroupIndexResult getFundGroupIndex(String groupId, String subGroupId) {
        FundGroupIndexQuery.Builder builder = FundGroupIndexQuery.newBuilder();
        builder.setGroupId(groupId);
        builder.setSubGroupId(subGroupId);
        return allocationBlockingStub.getAnnualVolatilityAndAnnualYield(builder.build());
    }


}
