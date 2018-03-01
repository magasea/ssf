package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.model.DailyAmount;
import com.shellshellfish.aaas.userinfo.model.PortfolioInfo;
import com.shellshellfish.aaas.userinfo.model.dto.ProductsDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UiProductDetailDTO;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoBankCardsRepository;
import com.shellshellfish.aaas.userinfo.repositories.zhongzheng.MongoDailyAmountRepository;
import com.shellshellfish.aaas.userinfo.service.FundGroupService;
import com.shellshellfish.aaas.userinfo.service.UiProductService;
import com.shellshellfish.aaas.userinfo.service.UserFinanceProdCalcService;
import com.shellshellfish.aaas.userinfo.service.UserInfoService;
import com.shellshellfish.aaas.userinfo.utils.DateUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

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
	MongoDailyAmountRepository mongoDailyAmountRepository;

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	UserInfoRepoService userInfoRepoService;

	@Autowired
	@Qualifier("zhongZhengMongoTemplate")
	private MongoTemplate mongoTemplate;


	@Override
	public Map getGroupDetails(String userUuid, Long productId, String buyDate) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		ProductsDTO productDTO = userInfoRepoService.findByProdId(productId + "");
		result.put("investDate", productDTO.getUpdateDate());
		result.put("investDays", DateUtil.getDaysToNow(new Date(productDTO.getUpdateDate())));
		result.put("combinationName", productDTO.getProdName());
		result.put("chartTitle", "累计收益率走势图");

		Long userId = userInfoRepoService.getUserIdFromUUID(userUuid);
		// 总资产
		Map<String, PortfolioInfo> portfolioInfoMap = userInfoService
				.getCalculateTotalAndRate(userUuid, userId, productDTO);
		List<Map<String, Object>> portfolioList = new ArrayList<Map<String, Object>>();
		if (portfolioInfoMap != null && portfolioInfoMap.size() > 0) {
			Map<String, Object> portfolioMap;
			if (portfolioInfoMap != null && portfolioInfoMap.size() > 0) {
				for (String key : portfolioInfoMap.keySet()) {
					portfolioMap = new HashMap<>();
					portfolioMap.put("date", key);
					PortfolioInfo portfolioInfo = portfolioInfoMap.get(key);
					BigDecimal value = portfolioInfo.getTotalIncomeRate();
					if (value != null) {
						value = value.multiply(new BigDecimal("100"));
						value = value.setScale(2, BigDecimal.ROUND_HALF_UP);
					}
					portfolioMap.put("value", value);
					portfolioList.add(portfolioMap);
				}
			}
		}
		Collections.sort(portfolioList, (o1, o2) -> {
			int map1value = Integer.parseInt(o1.get("date") + "");
			int map2value = Integer.parseInt(o2.get("date") + "");
			return map1value - map2value;
		});

		result.put("accumulationIncomes", portfolioList);

		List<UiProductDetailDTO> uiProductDetailDTOList = uiProductService
				.getProductDetailsByProdId(productId);
		List<Map> fundIncomes = new ArrayList<>(uiProductDetailDTOList.size());
		BigDecimal dailyIncome = BigDecimal.ZERO;
		for (int i = 0; i < uiProductDetailDTOList.size(); i++) {
			UiProductDetailDTO uiProductDetailDTO = uiProductDetailDTOList.get(i);
			String fundCode = uiProductDetailDTOList.get(i).getFundCode();
			Map fundIncomeInfo = new HashMap(3);
			fundIncomeInfo.put("fundCode", fundCode);
			fundIncomeInfo.put("fundName", uiProductDetailDTO.getFundName());
			BigDecimal todayIncome = getFundInome(fundCode, uiProductDetailDTO.getUserProdId());
			fundIncomeInfo.put("todayIncome", todayIncome.setScale(2, RoundingMode.HALF_UP));
			fundIncomes.add(fundIncomeInfo);
			dailyIncome.add(todayIncome);
		}
		result.put("fundIncomes", fundIncomes);
		result.put("dailyIncome", dailyIncome.setScale(2, RoundingMode.HALF_UP));
		return result;
	}


	private BigDecimal getFundInome(String fundCode, Long userProdId) {
		List<DailyAmount> dailyAmountList = mongoDailyAmountRepository
				.findAllByUserProdIdAndFundCode(userProdId, fundCode, new Sort(
						Direction.DESC, "date"));

		if (CollectionUtils.isEmpty(dailyAmountList) || dailyAmountList.size() == 1) {
			return BigDecimal.ZERO;
		}

		DailyAmount today = dailyAmountList.get(0);
		DailyAmount yesterday = dailyAmountList.get(1);
		BigDecimal internalAmount = today.getBonus().add(today.getSellAmount())
				.subtract(today.getBuyAmount());

		return today.getAsset().subtract(yesterday.getAsset()).add(internalAmount);
	}


}
