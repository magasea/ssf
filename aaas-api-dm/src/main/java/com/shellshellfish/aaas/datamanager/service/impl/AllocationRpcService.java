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


    public FundGroupIndexResult getFundGroupIndex(String groupId, String subGroupId, int oemId) {
        FundGroupIndexQuery.Builder builder = FundGroupIndexQuery.newBuilder();
        builder.setGroupId(groupId);
        builder.setSubGroupId(subGroupId);
        builder.setOemId(oemId);
        return allocationBlockingStub.getAnnualVolatilityAndAnnualYield(builder.build());
    }


}
