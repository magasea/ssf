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
	 * 获取产品的code，名字，净值增长值
	 * @param groupId
	 * @param subGroupId
	 * @return
	 */
	Map<String,Object> getPrdNPVList(String groupId,String subGroupId);
	
	/**
	 * 获取产品的年收益率和最大回撤率
	 * @param groupId
	 * @param subGroupId
	 * @return
	 */
	Map<String,Object> getExpAnnualAndMaxReturn(String groupId,String subGroupId);
	
	/**
	 * 获取优化方案后的结果
	 * @param riskLevel
	 * @param invstTerm
	 * @return
	 */
	Map<String,Object> getOptAdjustment(String riskLevel,String invstTerm);
	
}
