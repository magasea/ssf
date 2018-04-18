package com.shellshellfish.aaas.datamanager.service;

import com.shellshellfish.aaas.datamanager.model.JsonResult;

public interface OptimizationService {
	JsonResult financeFront();
	JsonResult getFinanceFront();
	JsonResult checkPrdDetails(String groupId, String subGroupId);
	JsonResult getPrdDetails(String groupId, String subGroupId);
    JsonResult checkPrdDetails2(String groupId, String subGroupId);
    JsonResult getFinanceFront(int size, int pageSize);
    JsonResult checkPrdDetailsVer2(String groupId, String subGroupId);
    JsonResult getPrdDetailsVer2(String groupId, String subGroupId, Integer pageSize,
        Integer pageIndex);
}
