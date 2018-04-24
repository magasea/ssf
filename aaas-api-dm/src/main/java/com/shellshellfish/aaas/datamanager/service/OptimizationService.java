package com.shellshellfish.aaas.datamanager.service;

import com.shellshellfish.aaas.datamanager.model.JsonResult;

public interface OptimizationService {
	JsonResult financeFront(Integer oemid);
	JsonResult getFinanceFront();
	JsonResult checkPrdDetails(String groupId, String subGroupId, Integer oemid);
	JsonResult getPrdDetails(String groupId, String subGroupId, Integer oemid);
    JsonResult checkPrdDetails2(String groupId, String subGroupId, Integer oemid);
    JsonResult getFinanceFront(Integer size, Integer pageSize, Integer oemid);
    JsonResult getPrdDetailsVer2(String groupId, String subGroupId, Integer pageSize,
        Integer pageIndex, Integer oemid);
    JsonResult checkPrdDetailsVer2(String groupId, String subGroupId, Integer oemid);
}
