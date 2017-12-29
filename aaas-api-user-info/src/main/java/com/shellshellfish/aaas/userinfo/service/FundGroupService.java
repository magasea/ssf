package com.shellshellfish.aaas.userinfo.service;

import java.util.Map;

/**
 * @Author pierre
 * 17-12-29
 */
public interface FundGroupService {

	Map getGroupDetails(String userUUID, Long productId);
}
