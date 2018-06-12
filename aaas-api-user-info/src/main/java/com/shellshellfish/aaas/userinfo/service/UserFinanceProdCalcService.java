package com.shellshellfish.aaas.userinfo.service;

import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.model.dao.UiUser;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface UserFinanceProdCalcService {


    BigDecimal calcYieldRate(String userUuid, Long prodId, String startDate, String endDate);

    BigDecimal calcYieldRate(String userUuid, String startDate, String endDate);

    void dailyCalculation() throws Exception;

    void dailyCalculation(String date) throws Exception;

    void dailyCalculation(String date, List<UiUser> uiUsers) throws Exception;

    BigDecimal getAssert(String userUuid, Long prodId) throws Exception;

    List<Map<String, Object>> getCalcYieldof7days(String fundCode, String type, String date)
            throws Exception;

    void calculateProductAsset(UiProductDetail detail, String uuid, Long prodId, String date);

    void calculateFromZzInfo(Long userProdId, String fundCode, String date) throws Exception;

}
