package com.shellshellfish.aaas.userinfo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.shellshellfish.aaas.common.enums.UserRiskLevelEnum;
import com.shellshellfish.aaas.userinfo.service.UserInfoService;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/userinfo")
public class RiskController {

	@Value("${api-asset-alloction-url}")
	private String assetAlloctionUrl;


	@Autowired
	RestTemplate restTemplate;

	@Autowired
	UserInfoService userInfoService;

	public static final Logger logger = LoggerFactory.getLogger(RiskController.class);


	/**
	 *
	 * @param uuid
	 * @param prodId
	 * @return
	 */
	@GetMapping("/isAppropriateRishLevel")
	public Boolean isAppropriateRishLevel(@RequestParam @NotNull String uuid,
			@RequestParam @NotNull Long prodId) {

		Long groupId = prodId;

		final String getRishLevelMethodUrl = "/api/asset-allocation/product-groups/{groupId}/risk-level";

		Integer userRiskLevel = userInfoService.getUserRishLevel(uuid);

		String result = restTemplate
				.getForEntity(assetAlloctionUrl + getRishLevelMethodUrl, String.class, groupId).getBody();

		JSONObject jsonObject = JSON.parseObject(result);
		JSONArray jsonArray = jsonObject.getJSONArray("_items");
		String prodRishLevel = "";
		if (jsonArray.size() > 0) {
			prodRishLevel = jsonArray.getJSONObject(0).getString("riskLevel");
		}

		Integer prodLevel = UserRiskLevelEnum.getLevelFromProdRisk(prodRishLevel);

//		if (userRiskLevel == UserRiskLevelEnum.CONSERV.getRiskLevel()) {
//			//保守型不能买任何组合  (组合中单发额基金风险等级高)
//			return false;
//		}

		if (userRiskLevel == UserRiskLevelEnum.STABLE.getRiskLevel()
				&& prodLevel > UserRiskLevelEnum.CONSERV.getRiskLevel()) {
			//稳健型智能购买保守型组合  (组合中单个基金风险等级高)
			return false;
		}

		return userRiskLevel >= prodLevel;

	}

}