package com.shellshellfish.aaas.finance.service;

import java.util.Map;

import com.shellshellfish.aaas.finance.returnType.FundAllReturn;
import com.shellshellfish.aaas.finance.returnType.FundReturn;
import com.shellshellfish.aaas.finance.returnType.PerformanceVolatilityReturn;
import com.shellshellfish.aaas.finance.returnType.ReturnType;
import com.shellshellfish.aaas.finance.returnType.RevenueContributionReturn;

public interface AssetAllocationService {
	public FundAllReturn getProduct();
	public FundReturn selectById(String id, String subGroupId, Integer oemid);
	public Map<String,Object> selectReturnAndPullback(String id, String subGroupId, String returntype);
	public RevenueContributionReturn getRevenueContribution(String id,String subGroupId);
	public RevenueContributionReturn efficientFrontier(String id,String subGroupId);
	public FundReturn getinterval(String id, String riskValue,String returnValue);
	public RevenueContributionReturn getRiskController(String id,String subGroupId);
	public RevenueContributionReturn getmeansAndNoticesRetrun(String id);
	public PerformanceVolatilityReturn getPerformanceVolatility(String id,String cust_risk,String investment_horizon);
	public RevenueContributionReturn getScaleMark(String id,String slidebarType);
	public ReturnType getPortfolioYield(String id, String subGroupId, int month, String returnType, Integer oemid);
	public ReturnType getPerformanceVolatilityHomePage();
	public ReturnType getProportionOne(String groupId, String subGroupId, Integer oemid);
}