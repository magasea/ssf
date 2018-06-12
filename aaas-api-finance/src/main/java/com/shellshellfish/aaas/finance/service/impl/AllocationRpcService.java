package com.shellshellfish.aaas.finance.service.impl;

import com.shellshellfish.aaas.asset.allocation.AssetAllocationServiceGrpc.AssetAllocationServiceBlockingStub;
import com.shellshellfish.aaas.asset.allocation.FundGroupIndexQuery;
import com.shellshellfish.aaas.asset.allocation.FundGroupIndexResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AllocationRpcService {

    @Autowired
    AssetAllocationServiceBlockingStub allocationBlockingStub;

    /**
     * 获取组合历史波动率和历史风险率
     */
    public FundGroupIndexResult getAnnualVolatilityAndAnnualYield(String groupId, String subGroupId, int oemId) {
        FundGroupIndexQuery.Builder builder = FundGroupIndexQuery.newBuilder();
        builder.setOemId(oemId);
        builder.setGroupId(groupId);
        builder.setSubGroupId(subGroupId);
        return allocationBlockingStub.getAnnualVolatilityAndAnnualYield(builder.build());
    }
}
