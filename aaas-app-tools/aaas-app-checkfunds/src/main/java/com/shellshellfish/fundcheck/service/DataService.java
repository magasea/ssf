package com.shellshellfish.fundcheck.service;

import com.shellshellfish.fundcheck.model.FundCodes;
import java.util.HashMap;
import java.util.List;


public interface DataService {

	//基金代码列表
	List<FundCodes> getAllFundCodes();


	//基金经理信息
	HashMap<String, Object> getFundManager(String name);

	//基金概况
	HashMap<String, Object> getFundInfoBycode(String code);

	//基金公司详细信息
	HashMap<String, Object> getFundCompanyDetailInfo(String name);

	//历史净值
	HashMap<String, Object> getHistoryNetvalue(String code, String type, String date);

	//基金价值指标信息:日涨幅,近一年涨幅,净值,分级类型,评级
	HashMap<String, Object> getFundValueInfo(String code, String date);

}
