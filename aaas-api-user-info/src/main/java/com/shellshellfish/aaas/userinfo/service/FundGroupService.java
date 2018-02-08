package com.shellshellfish.aaas.userinfo.service;

import java.text.ParseException;
import java.util.Map;

/**
 * @Author pierre
 * 17-12-29
 */
public interface FundGroupService {

	Map getGroupDetails(String uuid, Long prodId, String buyDate) throws ParseException;
}
