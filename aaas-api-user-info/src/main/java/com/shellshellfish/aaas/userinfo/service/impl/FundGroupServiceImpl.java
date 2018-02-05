package com.shellshellfish.aaas.userinfo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.common.utils.URLutils;
import com.shellshellfish.aaas.userinfo.model.FundIncome;
import com.shellshellfish.aaas.userinfo.model.dao.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dto.UiProductDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UiProductDetailDTO;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoBankCardsRepository;
import com.shellshellfish.aaas.userinfo.service.FundGroupService;
import com.shellshellfish.aaas.userinfo.service.UiProductService;
import com.shellshellfish.aaas.userinfo.service.UserInfoService;
import com.shellshellfish.aaas.userinfo.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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
	UserInfoService userInfoService;


	@Override
	public Map getGroupDetails(String userUuid, Long productId) {

		Map<String, Object> result = new HashMap();

		UiProductDTO uiProductDTO = uiProductService.getProductByProdId(productId);
		result.put("investDate", uiProductDTO.getUpdateDate());
		result.put("investDays", DateUtil.getDaysToNow(new Date(uiProductDTO.getUpdateDate())));
		result.put("combinationName", uiProductDTO.getProdName());

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
