package com.shellshellfish.aaas.transfer.service.impl;

import com.shellshellfish.aaas.common.utils.URLutils;
import com.shellshellfish.aaas.transfer.service.RiskService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @Author pierre 18-1-29
 */
@Service
public class RiskServiceImpl implements RiskService {

	@Value("${shellshellfish.userinfo-url}")
	String userInfoUrl;

	@Autowired
	RestTemplate restTemplate;


	@Override
	public Boolean isAppropriateRishLevel(String uuid, Long prodId) {
		String isAppropriateRishLevelUrl = "/api/userinfo/isAppropriateRishLevel";

		Map<String, String> params = new HashMap<>(2);

		params.put("userId", String.valueOf(uuid));
		params.put("prodId", String.valueOf(prodId));

		Boolean isAppropriate = restTemplate
				.getForEntity(URLutils
						.prepareParameters(userInfoUrl + isAppropriateRishLevelUrl, params), Boolean.TYPE)
				.getBody();

		if (isAppropriate == null) {
			isAppropriate = false;
		}

		return isAppropriate;

	}
}
