package com.shellshellfish.aaas.userinfo.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSONObject;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.common.utils.URLutils;
import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.model.DailyAmount;
import com.shellshellfish.aaas.userinfo.model.FundIncome;
import com.shellshellfish.aaas.userinfo.model.PortfolioInfo;
import com.shellshellfish.aaas.userinfo.model.dto.ProductsDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UiProductDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UiProductDetailDTO;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoBankCardsRepository;
import com.shellshellfish.aaas.userinfo.service.FundGroupService;
import com.shellshellfish.aaas.userinfo.service.UiProductService;
import com.shellshellfish.aaas.userinfo.service.UserFinanceProdCalcService;
import com.shellshellfish.aaas.userinfo.service.UserInfoService;
import com.shellshellfish.aaas.userinfo.utils.DateUtil;

/**
 * @Author pierre 17-12-29
 */
@Service
public class FundGroupServiceImpl implements FundGroupService {


	Logger logger = LoggerFactory.getLogger(FundGroupServiceImpl.class.getName());


	@Autowired
	RestTemplate restTemplate;


	@Value("${aaas-api-finance-url}")
	private String apiFinanceUrl;

	@Autowired
	UiProductService uiProductService;

	@Autowired
	UserInfoBankCardsRepository userInfoBankCardsRepository;
	
	@Autowired
	UserFinanceProdCalcService userFinanceProdCalcService;


	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	UserInfoRepoService userInfoRepoService;
	
	@Autowired
	@Qualifier("zhongZhengMongoTemplate")
	private MongoTemplate mongoTemplate;


	@Override
	public Map getGroupDetails(String userUuid, Long productId, String buyDate) throws Exception {
		Map<String, Object> result = new HashMap<String,Object>();
		ProductsDTO productDTO = userInfoRepoService.findByProdId(productId + "");
		result.put("investDate", productDTO.getUpdateDate());
		result.put("investDays", DateUtil.getDaysToNow(new Date(productDTO.getUpdateDate())));
		result.put("combinationName", productDTO.getProdName());
		result.put("chartTitle", "累计收益率走势图");
		
		Long userId = userInfoRepoService.getUserIdFromUUID(userUuid);
		// 总资产
		Map<String, PortfolioInfo> portfolioInfoMap = userInfoService.getCalculateTotalAndRate(userUuid, userId, productDTO);
		List<Map<String, Object>> portfolioList = new ArrayList<Map<String, Object>>();
		if (portfolioInfoMap != null && portfolioInfoMap.size() > 0) {
			Map<String, Object> portfolioMap = new HashMap<String, Object>();
			if (portfolioInfoMap != null && portfolioInfoMap.size() > 0) {
				for (String key : portfolioInfoMap.keySet()) {
					portfolioMap = new HashMap<String, Object>();
					portfolioMap.put("date", key);
					PortfolioInfo portfolioInfo = portfolioInfoMap.get(key);
					BigDecimal value = portfolioInfo.getTotalIncomeRate();
					if(value!=null){
						value = value.multiply(new BigDecimal("100"));
						value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
					}
					portfolioMap.put("value",value);
					portfolioList.add(portfolioMap);
				}
			}
		}
		Collections.sort(portfolioList, new Comparator<Map<String, Object>>() {
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				int map1value = Integer.parseInt(o1.get("date") + "");
				int map2value = Integer.parseInt(o2.get("date") + "");
				return map1value - map2value;
			}
		});
		
		result.put("accumulationIncomes", portfolioList);

		List<UiProductDetailDTO> uiProductDetailDTOList = uiProductService
				.getProductDetailsByProdId(productId);
		List<Map> fundIncomes = new ArrayList<>(uiProductDetailDTOList.size());
		for (int i = 0; i < uiProductDetailDTOList.size(); i++) {
			UiProductDetailDTO uiProductDetailDTO = uiProductDetailDTOList.get(i);
			String fundCode = uiProductDetailDTOList.get(i).getFundCode();
			Map fundIncomeInfo = new HashMap(3);
			fundIncomeInfo.put("fundCode", fundCode);
			fundIncomeInfo.put("fundName", uiProductDetailDTO.getFundName());
			fundIncomeInfo.put("todayIncome", getFundInome(fundCode, userUuid));
			fundIncomes.add(fundIncomeInfo);
		}
		result.put("fundIncomes", fundIncomes);
		return result;
	}


	private String getFundInome(String fundCode, String userUuid) {
		String getFunIncome_url = "/api/ssf-finance/getFundIncome";
		Map params = new HashMap();
		userUuid = Optional.ofNullable(userInfoService.getUserInfoBankCards(userUuid))
				.map(m -> m.get(0)).map(m -> m.getUserPid()).orElse("-1");
		params.put("fundCode", fundCode);
		params.put("userUuid", TradeUtil.getZZOpenId(userUuid));
		String originStr = restTemplate
				.getForObject(URLutils.prepareParameters(apiFinanceUrl + getFunIncome_url, params),
						String.class, params);
		if (StringUtils.isEmpty(originStr)) {
			return "0";
		}

		FundIncome fundIncome = JSONObject.parseObject(originStr, FundIncome.class);
		return fundIncome.getIncomes();
	}


}
