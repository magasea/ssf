package com.shellshellfish.datamanager.service;

import com.shellshellfish.datamanager.model.JsonResult;

public interface OptimizationService {
	JsonResult financeFront();
	JsonResult getFinanceFront();
	JsonResult checkPrdDetails(String groupId, String subGroupId);
	JsonResult getPrdDetails(String groupId, String subGroupId);
    JsonResult checkPrdDetails2(String groupId, String subGroupId);
}
