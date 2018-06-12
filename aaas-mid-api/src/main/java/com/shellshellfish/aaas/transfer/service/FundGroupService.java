package com.shellshellfish.aaas.transfer.service;

import java.util.Map;

/**
 * @Author pierre.chen
 * @Date 18-5-10
 */
public interface FundGroupService {

    Map getMyProductDetail(String userProdId, String uuid, String groupId, String subGroupId);
}
