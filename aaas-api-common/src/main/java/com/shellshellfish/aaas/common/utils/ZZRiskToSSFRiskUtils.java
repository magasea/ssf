package com.shellshellfish.aaas.common.utils;

import com.shellshellfish.aaas.common.enums.UserRiskLevelEnum;
import com.shellshellfish.aaas.common.enums.ZZRiskAbilityEnum;

/**
 * Created by chenwei on 2018- 一月 - 18
 */

public class ZZRiskToSSFRiskUtils {
  public static ZZRiskAbilityEnum getZZRiskAbilityFromSSFRisk(UserRiskLevelEnum userRiskLevelEnum){
    switch (userRiskLevelEnum){
      case CONSERV:
        return ZZRiskAbilityEnum.CONSERV;
      case STABLE:
        return ZZRiskAbilityEnum.STABLE;
      case BALANCE:
        return ZZRiskAbilityEnum.BALANCE;
      case INPROVING:
        return ZZRiskAbilityEnum.INPROVING;
      case AGGRESSIVE:
        return ZZRiskAbilityEnum.AGGRESSIVE;
    }
    throw new IllegalArgumentException("wrone input parameter:"+ userRiskLevelEnum);
  }

}
