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
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.common.utils.URLutils;
import com.shellshellfish.aaas.userinfo.model.DailyAmount;
import com.shellshellfish.aaas.userinfo.model.FundIncome;
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
	@Qualifier("zhongZhengMongoTemplate")
	private MongoTemplate mongoTemplate;


	@Override
	public Map getGroupDetails(String userUuid, Long productId, String buyDate) throws ParseException {
		Map<String, Object> result = new HashMap<String,Object>();
		UiProductDTO uiProductDTO = uiProductService.getProductByProdId(productId);
		result.put("investDate", uiProductDTO.getUpdateDate());
		result.put("investDays", DateUtil.getDaysToNow(new Date(uiProductDTO.getUpdateDate())));
		result.put("combinationName", uiProductDTO.getProdName());
		
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
//		buyDate = buyDate.substring(0,10);
//		buyDate = buyDate.replaceAll("-", "");
		
		String selectDate = "";
		Query query = new Query();
		query.addCriteria(Criteria.where("userUuid").is(userUuid))
				.addCriteria(Criteria.where("date").gte(buyDate))
				.addCriteria(Criteria.where("userProdId").is(productId));
		query.with(new Sort(Sort.DEFAULT_DIRECTION.DESC, "date"));
		List<DailyAmount> dailyAmountList = mongoTemplate.find(query, DailyAmount.class);
		if(dailyAmountList!=null&&dailyAmountList.size()>0){
			Map<String,BigDecimal> dailyAmountMap = new HashMap<String,BigDecimal>();
			for (int i = 0; i < dailyAmountList.size(); i++) {
				DailyAmount dailyAmount = dailyAmountList.get(i);
				BigDecimal asset = BigDecimal.ZERO;
				if (dailyAmountMap.get(dailyAmount.getDate()) == null) {
					asset = dailyAmount.getAsset();
					dailyAmountMap.put(dailyAmount.getDate(), asset);
				} else {
					asset = dailyAmountMap.get(dailyAmount.getDate()).add(dailyAmount.getAsset());
					dailyAmountMap.put(dailyAmount.getDate(), asset);
				}
				if (asset.compareTo(BigDecimal.ZERO) > 0) {
					if (StringUtils.isEmpty(selectDate)) {
						selectDate = dailyAmount.getDate();
					} else {
						buyDate = dailyAmount.getDate();
					}
				}
			}
		}
		
		//int days = 6; //计算6天的值
		//遍历赋值
		while (true) {
			Map<String,Object> dateValueMap = new HashMap<String,Object>();
//			String dayBeforeSelectDate = DateUtil.getSystemDatesAgo(-days - 1);
			if (selectDate.equals(buyDate)) {
				break;
			}
			dateValueMap.put("time", selectDate);
			// 调用对应的service
			BigDecimal value = null;
			try {
				value = userFinanceProdCalcService.calcYieldRate(userUuid, productId, buyDate, selectDate);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			dateValueMap.put("value", value);
			resultList.add(dateValueMap);
			
			int year = Integer.parseInt(selectDate.substring(0,4));
			int month = Integer.parseInt(selectDate.substring(4,6));
			int day = Integer.parseInt(selectDate.substring(6,8));
			LocalDate localDate = LocalDate.of(year, month, day);
			localDate =  localDate.minusDays(1);
			selectDate = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		}
		Collections.reverse(resultList);
		result.put("accumulationIncomes", resultList);

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
