package com.shellshellfish.aaas.userinfo.service;

import com.shellshellfish.aaas.userinfo.model.PortfolioInfo;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.model.dao.UiUser;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface UserFinanceProdCalcService {


	BigDecimal calcTotalDailyAsset(String userUuid) throws Exception;

	BigDecimal calcDailyAsset(String userUuid, Long prodId, String fundCode, String date)
			throws Exception;

	void calcIntervalAmount(String userUuid, Long prodId, String fundCode, String startDate)
			throws Exception;

	void initDailyAmount(String userUuid, Long prodId, Long userProdId, String date, String fundCode);

	BigDecimal calcYieldValue(String userUuid, Long prodId, String startDate, String endDate);

	BigDecimal calcYieldRate(String userUuid, Long prodId, String startDate, String endDate);

	BigDecimal calcYieldValue(String userUuid, String startDate, String endDate);

	BigDecimal calcYieldRate(String userUuid, String startDate, String endDate);

	void dailyCalculation() throws Exception;

	void dailyCalculation(String date) throws Exception;

	void dailyCalculation(String date, List<UiUser> uiUsers) throws Exception;

	BigDecimal getAssert(String userUuid, Long prodId) throws Exception;

	PortfolioInfo calculateProductValue(String userUuid, Long prodId,
			String startDate, String endDate);

	List<Map<String, Object>> getCalcYieldof7days(String fundCode, String type, String date)
			throws Exception;

	void calculateProductAsset(UiProductDetail detail, String uuid, Long prodId, String date);
}
