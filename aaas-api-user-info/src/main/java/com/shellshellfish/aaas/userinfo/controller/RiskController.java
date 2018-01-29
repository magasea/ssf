package com.shellshellfish.aaas.userinfo.controller;

import com.shellshellfish.aaas.common.enums.UserRiskLevelEnum;
import com.shellshellfish.aaas.userinfo.service.UserInfoService;
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
	 * @param userId
	 * @param prodId
	 * @return
	 */
	@GetMapping("/isAppropriateRishLevel")
	public Boolean isAppropriateRishLevel(@RequestParam @NotNull Long userId,
			@RequestParam @NotNull Long prodId) {

		Long groupId = prodId;

		final String getRishLevelMethodUrl = "/api/asset-allocation/product-groups/{groupId}/risk-level";

		Integer userRiskLevel = userInfoService.getUserRishLevel(userId);

		String prodRishLevel = restTemplate
				.getForEntity(assetAlloctionUrl + getRishLevelMethodUrl, String.class, groupId).getBody();

		Integer prodLevel = UserRiskLevelEnum.getLevelFromProdRisk(prodRishLevel);

		return userRiskLevel >= prodLevel;

	}

}