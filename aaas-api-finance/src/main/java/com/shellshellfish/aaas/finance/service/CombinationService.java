package com.shellshellfish.aaas.finance.service;

import java.util.Map;
import com.shellshellfish.aaas.finance.model.dao.Fundresources;

public interface CombinationService {
	Map<String, Object> getCombinationServices(String groupId,String subGroupId,String code);

	Map<String, Object> fundUpDown(String groupId, String subGroupId, String code);
	
	Map<String, Object> navbenchreturnstmt(String groupId, String subGroupId, String code, String reportDate, String span);

	Map<String, Object> navhistories(String groupId, String subGroupId, String code);

	Map<String, Object> achievementhistories(String groupId, String subGroupId, String code);

	Map<String, Object> getTradeLimits(String code, String businflag);

	Fundresources getMgrlongestyears(String code);
}