package com.shellshellfish.aaas.common.utils;

import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.enums.TrdPayFlowStatusEnum;
import com.shellshellfish.aaas.common.enums.ZZBizOpEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chenwei on 2018- 一月 - 04
 */

public class ZZStatsToOrdStatsUtils {

  static Logger logger = LoggerFactory.getLogger(ZZStatsToOrdStatsUtils.class);

  public static TrdOrderStatusEnum getOrdDtlStatFromZZStats(TrdPayFlowStatusEnum inputEnum,
      TrdOrderOpTypeEnum trdOrderOpTypeEnum){
    switch (inputEnum){
      case NOTHANDLED:
        if(trdOrderOpTypeEnum == TrdOrderOpTypeEnum.BUY){
          return TrdOrderStatusEnum.PAYWAITCONFIRM;
        }else if(trdOrderOpTypeEnum == TrdOrderOpTypeEnum.REDEEM){
          return TrdOrderStatusEnum.SELLWAITCONFIRM;
        }
      case CONFIRMSUCCESS:
        return TrdOrderStatusEnum.CONFIRMED;
      case CONFIRMFAILED:
        return TrdOrderStatusEnum.FAILED;
      case REALTIMECONFIRMSUCESS:
        return TrdOrderStatusEnum.CONFIRMED;
      case PARTCONFIRMED:
        return TrdOrderStatusEnum.PARTIALCONFIRMED;
      default:
        logger.error("input is not correct enum");
        return TrdOrderStatusEnum.FAILED;
    }
  }


  public static TrdOrderStatusEnum getOrdDtlStatFromZZStats(String inputEnumVal,
      TrdOrderOpTypeEnum trdOrderOpTypeEnum){
    TrdPayFlowStatusEnum trdPayFlowStatusEnum = null;
    try{
      trdPayFlowStatusEnum = TrdPayFlowStatusEnum.valueOf(inputEnumVal);
    }catch (Exception ex){
      ex.printStackTrace();
      logger.error(ex.getMessage());
    }

    switch (trdPayFlowStatusEnum){
      case NOTHANDLED:
        if(trdOrderOpTypeEnum == TrdOrderOpTypeEnum.BUY){
          return TrdOrderStatusEnum.PAYWAITCONFIRM;
        }else if(trdOrderOpTypeEnum == TrdOrderOpTypeEnum.REDEEM){
          return TrdOrderStatusEnum.SELLWAITCONFIRM;
        }
      case CONFIRMSUCCESS:
        return TrdOrderStatusEnum.CONFIRMED;
      case CONFIRMFAILED:
        return TrdOrderStatusEnum.FAILED;
      case REALTIMECONFIRMSUCESS:
        return TrdOrderStatusEnum.CONFIRMED;
      case PARTCONFIRMED:
        return TrdOrderStatusEnum.PARTIALCONFIRMED;
      default:
        logger.error("input is not correct enum");
        return TrdOrderStatusEnum.FAILED;
    }
  }

  public static TrdOrderOpTypeEnum getTrdOrdOpTypeFromCallingCode(int callingCode){

      if(callingCode ==  ZZBizOpEnum.PURCHASE.getOptVal()){
        return TrdOrderOpTypeEnum.BUY;
      }else if(callingCode == ZZBizOpEnum.CANCEL.getOptVal()) {
        return TrdOrderOpTypeEnum.CANCEL;
      }else if(callingCode == ZZBizOpEnum.PURCHASE.getOptVal()){
        return TrdOrderOpTypeEnum.BUY;
      }else if(callingCode == ZZBizOpEnum.REDEEM.getOptVal()){
        return TrdOrderOpTypeEnum.REDEEM;
      }
      else{
        logger.error("cannot find corresponding enum for callingCode:"+ callingCode);
        return TrdOrderOpTypeEnum.UNDEFINED;
      }

    }

}
