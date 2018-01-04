package com.shellshellfish.aaas.finance.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shellshellfish.aaas.common.utils.URLutils;
import com.shellshellfish.aaas.finance.model.dto.FundCompany;
import com.shellshellfish.aaas.finance.model.dto.FundManager;
import com.shellshellfish.aaas.finance.model.dto.HistoryList;
import com.shellshellfish.aaas.finance.service.GroupDetailsService;
import com.sun.org.apache.bcel.internal.generic.FNEG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author pierre
 * 17-12-21
 */

@Service
public class GroupDetailsServiceImpl implements GroupDetailsService {


	@Value("${api-data-manager-url}")
	private String dataManagerBaseUrl;

	@Autowired
	RestTemplate restTemplate;


	/**
	 * 历史净值
	 * code:基金代码
	 * period: 1: 3month,2: 6month,3: 1year,4: 3year
	 *
	 * @param methodUrl
	 * @param params
	 * @return
	 */
	@Override
	public String getHistoryList(String methodUrl, Map params) {

		return  connectDataManager(methodUrl, params);

	}

	/**
	 * 获取基金经理信息
	 *
	 * @param methodUrl
	 * @param params
	 * @return
	 */
	@Override
	public FundManager getFundManager(String methodUrl, Map params) {
		String origin = connectDataManager(methodUrl, params);
		if (origin != null) {
			return prepareFundManager(origin);
		}
		return null;
	}

	@Override
	public FundCompany getFundCompany(String methodUrl, Map params) {
		String origin = connectDataManager(methodUrl, params);
		if (origin != null) {
			return prepareFundCompany(origin);
		}
		return null;
	}

	@Override
	public String connectDataManager(String methodUrl, Map params) {
		ResponseEntity<String> entity =
				restTemplate.getForEntity(URLutils.prepareParameters(dataManagerBaseUrl + methodUrl, params), String.class);
		if (entity != null && entity.getStatusCode().equals(HttpStatus.OK))
			return entity.getBody();

		return null;
	}


	/**
	 * 拆分基金经理的数据
	 * 原始数据：
	 * <p>
	 * {
	 * "manager": "董阳阳",
	 * "workingdays": "1,077.0000",
	 * "avgearningrate": 8.5501,
	 * """joblist""": [
	 * {
	 * "jobitem": "华夏成长混合|||2015-01-07|||10.6593"
	 * },
	 * {
	 * "jobitem": "华夏新机遇混合|||2017-09-08|||4.5041"
	 * },
	 * {
	 * "jobitem": "华夏圆和混合|||2016-12-29|||11.49"
	 * }
	 * ],
	 * "fundnum": 3
	 * }
	 *
	 * @param origin
	 * @return
	 */
	private FundManager prepareFundManager(String origin) {
		JSONObject originJson = JSONObject.parseObject(origin);
		if (originJson.size() == 0)
			return null;

		FundManager manager = new FundManager();
		manager.setWorkingdays(originJson.getString("workingdays"));
		manager.setManagerName(originJson.getString("manager"));
		manager.setAvgearningrate(originJson.getString("avgearningrate"));


		JSONArray joblist = originJson.getJSONArray("joblist");

		List<FundManager.JobDetail> jobDetailList = new ArrayList<>(joblist.size());
		for (int i = 0; i < joblist.size(); i++) {
			String jobitem = joblist.getJSONObject(i).getString("jobitem");
			String[] params = jobitem.replace("|||", ",").split(",");
			if (params == null || params.length != 3) {
				continue;
			}

			jobDetailList.add(manager.new JobDetail(params[0], params[1], params[2]));
		}
		manager.setJobDetails(jobDetailList);
		return manager;
	}

	private FundCompany prepareFundCompany(String origin) {
		JSONObject originJson = JSONObject.parseObject(origin);
		if (originJson.size() == 0)
			return null;

		FundCompany company = new FundCompany();
		company.setCompanyName(originJson.getString("fundcompany"));
		company.setFundnum(originJson.getInteger("fundnum"));
		company.setScale(originJson.getString("scale"));

		JSONArray fundlist = originJson.getJSONArray("fundlist");

		List<FundCompany.FundDetail> fundDetailList = new ArrayList<>(fundlist.size());
		for (int i = 0; i < fundlist.size(); i++) {
			String funditem = fundlist.getJSONObject(i).getString("funditem");
			String[] params = funditem.replace("|||", ",").split(",");
			if (params == null || params.length != 3) {
				continue;
			}

			fundDetailList.add(company.new FundDetail(params[0], params[1], params[2]));
		}
		company.setFundDetails(fundDetailList);
		return company;
	}

}
