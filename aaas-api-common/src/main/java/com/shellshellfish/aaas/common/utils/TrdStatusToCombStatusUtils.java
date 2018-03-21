package com.shellshellfish.aaas.common.utils;

import com.shellshellfish.aaas.common.enums.CombinedStatusEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.enums.UserRiskLevelEnum;
import com.shellshellfish.aaas.common.enums.ZZRiskAbilityEnum;

/**
 * Created by chenwei on 2018- 一月 - 18
 */

public class TrdStatusToCombStatusUtils {
  public static CombinedStatusEnum getCSEFromTSE(TrdOrderStatusEnum trdOrderStatusEnum){
    switch (trdOrderStatusEnum){
      case PAYWAITCONFIRM:
        return CombinedStatusEnum.WAITCONFIRM;
      case SELLWAITCONFIRM:
        return CombinedStatusEnum.WAITCONFIRM;
      case CONFIRMED:
        return CombinedStatusEnum.CONFIRMED;
      case FAILED:
        return CombinedStatusEnum.CONFIRMEDFAILED;
      case WAITSELL:
        return CombinedStatusEnum.WAITCONFIRM;
      case WAITPAY:
        return CombinedStatusEnum.WAITCONFIRM;
      case SELLCONFIRMED:
        return CombinedStatusEnum.CONFIRMED;
      default:
        return CombinedStatusEnum.CONFIRMEDFAILED;
    }
  }

}
