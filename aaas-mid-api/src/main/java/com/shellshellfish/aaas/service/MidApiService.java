package com.shellshellfish.aaas.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.shellshellfish.aaas.dto.FinanceProductCompo;

/**
 * 作一些数据处理
 * @author developer4
 *
 */
@Service
public interface MidApiService {
	/**
	 * 获取产品的code，名字，净值增长和精致增长率图表数据
	 * @param groupId
	 * @param subGroupId
	 * @return
	 */
	Map<String,Object> getPrdNPVIncrement(String groupId,String subGroupId);
	
	
}
