package com.shellshellfish.aaas.finance.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shellshellfish.aaas.finance.model.dao.Fundresources;
import com.shellshellfish.aaas.finance.repository.mongo3.FundResourceRepository;
import com.shellshellfish.aaas.finance.service.CombinationService;
import com.shellshellfish.aaas.finance.trade.model.TradeLimitResult;
import com.shellshellfish.aaas.finance.trade.service.FundTradeApiService;

@Service("combinationService")
@Transactional
public class CombinationServiceImpl implements CombinationService {

	@Autowired
	AssetAllocationServiceImpl assetAllocationService;
	
	@Autowired
	FundTradeApiService fundTradeApiService;
	
	@Autowired
	FundResourceRepository fundResourceRepository;

	private final static Logger logger = LoggerFactory.getLogger(CombinationServiceImpl.class);

	@Override
	public Map<String, Object> getCombinationServices(String groupId, String subGroupId, String code) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> linksMap = new HashMap<String, Object>();
		List<Map<String, Object>> relateList = new ArrayList();

		Date dt = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDD");
		String tradeDate = sdf.format(dt);

		// 近一年涨幅
		// Map<String, Object> differRangeYMap = new HashMap<String, Object>();
		// differRangeYMap.put("href",
		// "/api/ssf-finance/product-groups/{groupId}/sub-groups/{subGroupId}/differRangeYs?codes="
		// + code + "TradeDate=" + tradeDate);
		// differRangeYMap.put("name", "DifferRangeY");
		// relateList.add(differRangeYMap);
		result.put("DifferRangeY", "+7.5%");
		// 日涨幅
		// Map<String, Object> differRangeMap = new HashMap<String, Object>();
		// differRangeMap.put("href",
		// "/api/ssf-finance/product-groups/{groupId}/sub-groups/{subGroupId}/differRanges?codes="
		// + code + "TradeDate=" + tradeDate);
		// differRangeMap.put("name", "DifferRange");
		// relateList.add(differRangeMap);
		result.put("DifferRange", "+1.5%");
		// 净值
		// Map<String, Object> navunitMap = new HashMap<String, Object>();
		// navunitMap.put("href",
		// "/api/ssf-finance/product-groups/{groupId}/sub-groups/{subGroupId}/navunits?codes="
		// + code + "TradeDate=" + tradeDate);
		// navunitMap.put("name", "NAVUNIT");
		// relateList.add(navunitMap);
		result.put("NAVUNIT", "1.1980");

		// 收益走势
		Map<String, Object> navreturnstmtMap = new HashMap<String, Object>();
		navreturnstmtMap.put("href", "/api/ssf-finance/product-groups//" + groupId + "/sub-groups/" + subGroupId
				+ "/combinations/" + code + "/navbenchreturnstmts?ReportDate=" + tradeDate + "&Span=" + 4);
		navreturnstmtMap.put("name", "NAVRETURNSTMT");
		relateList.add(navreturnstmtMap);

		// 历史净值
		Map<String, Object> historyValueMap = new HashMap<String, Object>();
		historyValueMap.put("href", "/api/ssf-finance/product-groups/" + groupId + "/sub-groups/" + subGroupId
				+ "/historyValues?codes=" + code + "&ReportDate=" + tradeDate + "&Span=" + 4);
		historyValueMap.put("name", "NAVRETURNSTMT");
		relateList.add(historyValueMap);

		// 交易须知
		Map<String, Object> tradeInformationMap = new HashMap<String, Object>();
		tradeInformationMap.put("href",
				"/api/ssf-finance/product-groups/" + groupId + "/sub-groups/" + subGroupId + "/navreturnstmts");
		tradeInformationMap.put("name", "NAVRETURNSTMT");
		relateList.add(tradeInformationMap);
		result.put("tradeInformationMap", "交易流程，费率");

		// 基金经理
		Map<String, Object> mgrMap = new HashMap<String, Object>();
		mgrMap.put("href", "/api/ssf-finance/product-groups/" + groupId + "/sub-groups/" + subGroupId + "/mgrs");
		mgrMap.put("name", "MGR");
		relateList.add(mgrMap);
		result.put("mgr", "张群林");

		// 基金公司
		Map<String, Object> companyMap = new HashMap<String, Object>();
		companyMap.put("href",
				"/api/ssf-finance/product-groups/" + groupId + "/sub-groups/" + subGroupId + "/fund-company-infos");
		companyMap.put("name", "fundCompany");
		relateList.add(companyMap);
		result.put("mgr", "南方基金管理公司");

		// 基金概况
		Map<String, Object> generalMap = new HashMap<String, Object>();
		generalMap.put("href",
				"/api/ssf-finance/product-groups/" + groupId + "/sub-groups/" + subGroupId + "/fund-generals");
		generalMap.put("name", "general");
		relateList.add(generalMap);
		result.put("mgr", "成立5年，8.9亿");

		// 基金公告
		Map<String, Object> noticeMap = new HashMap<String, Object>();
		noticeMap.put("href", "/api/ssf-finance/product-groups/" + groupId + "/sub-groups/" + subGroupId + "/notices");
		noticeMap.put("name", "notice");
		relateList.add(noticeMap);
		result.put("mgr", "招募说明书、分红通知");
		linksMap.put("related", relateList);

		Map<String, Object> selfMap = new HashMap<String, Object>();
		selfMap.put("href", "/api/ssf-finance/product-groups/" + groupId + "/sub-groups/" + subGroupId
				+ "/combinations/" + code + "/details");
		selfMap.put("describedBy", "/api/ssf-finance/product-groups/" + groupId + "/sub-groups/" + subGroupId
				+ "/combinations/" + code + "/details.json");
		linksMap.put("self", selfMap);

		// 历史业绩
		List<Map<String, Object>> historyList = new ArrayList<>();
		Map<String, Object> history = new HashMap<String, Object>();
		history.put("timePeriod", "近一个月");
		history.put("updownRange", "0.00%");
		history.put("samerankinglist", "158/272");
		historyList.add(history);

		history = new HashMap<String, Object>();
		history.put("timePeriod", "近3个月");
		history.put("updownRange", "0.08%");
		history.put("samerankinglist", "202/272");
		historyList.add(history);

		history = new HashMap<String, Object>();
		history.put("timePeriod", "近6个月");
		history.put("updownRange", "0.00%");
		history.put("samerankinglist", "158/272");
		historyList.add(history);

		history = new HashMap<String, Object>();
		history.put("timePeriod", "近1年");
		history.put("updownRange", "0.08%");
		history.put("samerankinglist", "202/272");
		historyList.add(history);

		history = new HashMap<String, Object>();
		history.put("timePeriod", "近3年");
		history.put("updownRange", "0.00%");
		history.put("samerankinglist", "158/272");
		historyList.add(history);

		history = new HashMap<String, Object>();
		history.put("timePeriod", "今年来");
		history.put("updownRange", "0.08%");
		history.put("samerankinglist", "202/272");
		historyList.add(history);
		result.put("histrory", historyList);

		result.put("_links", linksMap);

		return result;
	}

	@Override
	public Map<String, Object> fundUpDown(String groupId, String subGroupId, String code) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> linksMap = new HashMap<String, Object>();
		// 近一年涨幅
		String differRangeY = "+7.5%";
		// 日涨幅
		String differRangeD = "+1.5%";
		// 净值
		String navunit = "1.1980";

		Map<String, Object> selfMap = new HashMap<String, Object>();
		selfMap.put("href", "/api/ssf-finance/product-groups/" + groupId + "/sub-groups/" + subGroupId
				+ "/combinations/" + code + "/fund-increase-infos");
		selfMap.put("describedBy", "/api/ssf-finance/product-groups/" + groupId + "/sub-groups/" + subGroupId
				+ "/combinations/" + code + "/fund-increase-infos.json");
		linksMap.put("self", selfMap);
		result.put("_links", linksMap);

		result.put("differRangeY", differRangeY);
		result.put("differRangeD", differRangeD);
		result.put("navunit", navunit);
		return result;
	}

	@Override
	public Map<String, Object> navbenchreturnstmt(String groupId, String subGroupId, String code, String reportDate,
			String span) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> linksMap = new HashMap<String, Object>();
		Map<String, Object> selfMap = new HashMap<String, Object>();
		
		selfMap.put("href", "/api/ssf-finance/product-groups/" + groupId + "/sub-groups/" + subGroupId
				+ "/combinations/" + code + "/navbenchreturnstmts");
		selfMap.put("describedBy", "/api/ssf-finance/product-groups/" + groupId + "/sub-groups/" + subGroupId
				+ "/combinations/" + code + "/navbenchreturnstmts.json");
		linksMap.put("self", selfMap);
		result.put("_links", linksMap);
		
		result.put("navbenchreturnstmt", "");
		return result;
	}

	@Override
	public Map<String, Object> navhistories(String groupId, String subGroupId, String code) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> linksMap = new HashMap<String, Object>();
		Map<String, Object> selfMap = new HashMap<String, Object>();
		
		selfMap.put("href", "/api/ssf-finance/product-groups/" + groupId + "/sub-groups/" + subGroupId
				+ "/combinations/" + code + "/navhistories");
		selfMap.put("describedBy", "/api/ssf-finance/product-groups/" + groupId + "/sub-groups/" + subGroupId
				+ "/combinations/" + code + "/navhistories.json");
		linksMap.put("self", selfMap);
		result.put("_links", linksMap);
		
		result.put("navhistorie", "");
		return result;
	}
	
	@Override
	public Map<String, Object> achievementhistories(String groupId, String subGroupId, String code) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> linksMap = new HashMap<String, Object>();
		Map<String, Object> selfMap = new HashMap<String, Object>();
		
		selfMap.put("href", "/api/ssf-finance/product-groups/" + groupId + "/sub-groups/" + subGroupId
				+ "/combinations/" + code + "/achievementhistories");
		selfMap.put("describedBy", "/api/ssf-finance/product-groups/" + groupId + "/sub-groups/" + subGroupId
				+ "/combinations/" + code + "/achievementhistories.json");
		linksMap.put("self", selfMap);
		result.put("_links", linksMap);
		
		result.put("navhistorie", "");
		return result;
	}

	@Override
	public Map<String, Object> getTradeLimits(String code, String businflag) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<TradeLimitResult> tradeLimitList = new ArrayList();
		try {
			if(businflag==null){
				businflag = "";
			}
			tradeLimitList = fundTradeApiService.getTradeLimits(code,businflag);
		} catch (Exception e) {
			logger.error("exception:",e);
		}
		Map<String, Object> linksMap = new HashMap<String, Object>();
		Map<String, Object> selfMap = new HashMap<String, Object>();
		
		selfMap.put("href", "/api/ssf-finance/tradenotices/" + code);
		selfMap.put("describedBy", "/api/ssf-finance/tradenotices/" + code + ".json");
		linksMap.put("self", selfMap);
		result.put("_links", linksMap);
		if(tradeLimitList!=null&&tradeLimitList.size()>0){
			result.put("_items", tradeLimitList);
			result.put("_totals", tradeLimitList.size());
		} else {
			result.put("_items", null);
			result.put("_totals", 0);
		}
		
		return result;
	}

	@Override
	public Fundresources getMgrlongestyears(String code) {
		code = code+".OF";
		Fundresources fundResources = fundResourceRepository.findOneByCode(code);
		List<Fundresources> fundList = fundResourceRepository.findAll();
		Fundresources resource = new Fundresources();
		resource.setCode("001");
		resource.setMgrage("0000");
		fundResourceRepository.save(resource);
		return fundResources;
	}

}
