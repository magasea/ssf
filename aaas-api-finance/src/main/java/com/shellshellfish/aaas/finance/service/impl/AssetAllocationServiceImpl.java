package com.shellshellfish.aaas.finance.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.shellshellfish.aaas.finance.returnType.FundAllReturn;
import com.shellshellfish.aaas.finance.returnType.FundReturn;
import com.shellshellfish.aaas.finance.returnType.PerformanceVolatilityReturn;
import com.shellshellfish.aaas.finance.returnType.ReturnType;
import com.shellshellfish.aaas.finance.returnType.RevenueContributionReturn;
import com.shellshellfish.aaas.finance.service.AssetAllocationService;

@Service
public class AssetAllocationServiceImpl implements AssetAllocationService {

	@Value("${shellshellfish.asset-allocation-url}")
	private String url;

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 查询所有基金组合
	 * 
	 * @return
	 */
	@Override
	public FundAllReturn getProduct() {
		return restTemplate.getForEntity(url + "/api/asset-allocation/products", FundAllReturn.class).getBody();
	}

	/**
	 * 按照ID查询基金组合明细
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public FundReturn selectById(String id, String subGroupId, Integer oemid) {
		return restTemplate
				.getForEntity(url + "/api/asset-allocation/product-groups/" + id + "/sub-groups/" + subGroupId + "/" + oemid,
						FundReturn.class)
				.getBody();
	}
	
	/**
     * 产品类别比重
     * @param fund_group_id
     * @param fund_group_sub_id
     * @return
     */
	@Override
	public ReturnType getProportionOne(String groupId, String subGroupId, Integer oemid) {
		return restTemplate
				.getForEntity(url + "/api/asset-allocation/products/" + groupId + "/sub-groups/" + subGroupId+"/one-type?oemid=" + oemid,
						ReturnType.class)
				.getBody();
	}

	/**
	 * 预期年化收益(action=calcExpectedAnnualizedReturn),
	 * 预期最大回撤(action=calcExpectedMaxPullback)
	 * 
	 * @param id
	 * @param returntype
	 * @param subGroupId
	 * @return
	 */
	@Override
	public Map<String, Object> selectReturnAndPullback(String id, String subGroupId, String returntype) {
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("returntype", returntype);
		return restTemplate.postForEntity(
				url + "/api/asset-allocation/product-groups/" + id + "/sub-groups/" + subGroupId + "/opt",
				requestEntity, Map.class).getBody();
	}

	/**
	 * 配置收益贡献
	 * 
	 * @return
	 */
	@Override
	public RevenueContributionReturn getRevenueContribution(String id, String subGroupId) {
		return restTemplate.getForEntity(
				url + "/api/asset-allocation/product-groups/" + id + "/sub-groups/" + subGroupId + "/contributions",
				RevenueContributionReturn.class).getBody();
	}

	/**
	 * 有效前沿线
	 * 
	 * @return
	 */
	@Override
	public RevenueContributionReturn efficientFrontier(String id, String subGroupId) {
		return restTemplate.getForEntity(url + "/api/asset-allocation/products/" + id + "/sub-groups/" + subGroupId
				+ "/effective-frontier-points", RevenueContributionReturn.class).getBody();
	}

	/**
	 * 预期收益率调整 风险率调整 最优组合(有效前沿线)
	 * 
	 * @param id
	 * @param riskValue
	 * @param returnValue
	 * @return
	 */
	@Override
	public FundReturn getinterval(String id, String riskValue, String returnValue) {
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("riskValue", riskValue);
		requestEntity.add("returnValue", returnValue);
		return restTemplate.postForEntity(url + "/api/asset-allocation/product-groups/" + id + "/optimizations",
				requestEntity, FundReturn.class).getBody();
	}

	/**
	 * 风险控制
	 * 
	 * @param id
	 * @param subGroupId
	 * @return
	 */
	@Override
	public RevenueContributionReturn getRiskController(String id, String subGroupId) {
		return restTemplate.getForEntity(
				url + "/api/asset-allocation/product-groups/" + id + "/sub-groups/" + subGroupId + "/risk-controls",
				RevenueContributionReturn.class).getBody();
	}

	/**
	 * 风险控制手段与通知
	 * 
	 * @param
	 * @return
	 */
	@Override
	public RevenueContributionReturn getmeansAndNoticesRetrun(String id) {
		return restTemplate.getForEntity(url + "/api/asset-allocation/products/" + id + "/risk-notifications",
				RevenueContributionReturn.class).getBody();
	}

	/**
	 * 模拟历史年化业绩与模拟历史年化波动率
	 * 
	 * @param id
	 * @param cust_risk
	 * @param investment_horizon
	 * @return
	 */
	@Override
	public PerformanceVolatilityReturn getPerformanceVolatility(String id, String cust_risk,
			String investment_horizon) {
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("riskLevel", cust_risk);
		requestEntity.add("investmentPeriod", investment_horizon);
		return restTemplate.postForEntity(url + "/api/asset-allocation/product-groups", requestEntity, PerformanceVolatilityReturn.class).getBody();
	}

	/**
	 * 滑动条分段数据
	 * 
	 * @param id
	 * @param slidebarType
	 * @return
	 */
	@Override
	public RevenueContributionReturn getScaleMark(String id, String slidebarType) {
		return restTemplate.getForEntity(
				url + "/api/asset-allocation/product-groups/" + id + "/slidebar-points?slidebarType=" + slidebarType,
				RevenueContributionReturn.class).getBody();
	}
	
	/**
	 * 组合收益率(最大回撤)走势图     (几个月以来每天)
	 * @param id
	 * @param subGroupId
	 * @param month
	 * @param returnType 查询类型（income：收益率，其他的：最大回撤）
	 * @return
	 */
	@Override
	public ReturnType getPortfolioYield(String id,String subGroupId,int month,String returnType,Integer oemid) {
		return restTemplate.getForEntity(
				url + "/api/asset-allocation/product-groups/"+id+"/sub-groups/"+subGroupId+"/portfolio-yield?mouth="+month+"&returnType="+returnType+"&oemId=" + oemid,
				ReturnType.class).getBody();
	}
	/**
	 * 返回首页五个产品
	 * @param id
	 * @param subGroupId
	 * @param month
	 * @param returnType 查询类型（income：收益率，其他的：最大回撤）
	 * @return
	 */
	@Override
	public ReturnType getPerformanceVolatilityHomePage() {
		return restTemplate.getForEntity(url + "/api/asset-allocation/products/home-page",ReturnType.class).getBody();
	}
}
