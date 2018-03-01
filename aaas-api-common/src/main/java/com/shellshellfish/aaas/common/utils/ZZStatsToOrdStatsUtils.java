package com.shellshellfish.aaas.common.utils;

import static com.shellshellfish.aaas.common.enums.ZZKKStatusEnum.KKSUCCESS;

import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.enums.TrdZZCheckStatusEnum;
import com.shellshellfish.aaas.common.enums.ZZBizOpEnum;
import com.shellshellfish.aaas.common.enums.ZZKKStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chenwei on 2018- 一月 - 04
 */

public class ZZStatsToOrdStatsUtils {

  static Logger logger = LoggerFactory.getLogger(ZZStatsToOrdStatsUtils.class);

  public static TrdOrderStatusEnum getOrdDtlStatFromZZStats(TrdZZCheckStatusEnum inputEnum,
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
    TrdZZCheckStatusEnum trdZZCheckStatusEnum = null;
    try{
      trdZZCheckStatusEnum = TrdZZCheckStatusEnum.valueOf(inputEnumVal);
    }catch (Exception ex){
      logger.error("exception:",ex);
      logger.error(ex.getMessage());
    }

    switch (trdZZCheckStatusEnum){
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
        logger.error("input is not correct enums");
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
        logger.error("cannot find corresponding enums for callingCode:"+ callingCode);
        return TrdOrderOpTypeEnum.UNDEFINED;
      }

    }

    public static TrdOrderStatusEnum getOrdStatByZZKKStatus(ZZKKStatusEnum zzkkStatusEnum,
        TrdOrderOpTypeEnum trdOrderOpTypeEnum){
        if(zzkkStatusEnum == ZZKKStatusEnum.KKSUCCESS || zzkkStatusEnum == ZZKKStatusEnum.WAITCONFIRM){
          if(trdOrderOpTypeEnum == TrdOrderOpTypeEnum.BUY){
            return TrdOrderStatusEnum.PAYWAITCONFIRM;
          }else if(trdOrderOpTypeEnum == TrdOrderOpTypeEnum.REDEEM){
            return TrdOrderStatusEnum.SELLWAITCONFIRM;
          }
        }else {
          return TrdOrderStatusEnum.FAILED;
        }
        throw new IllegalArgumentException("zzkkStatusEnum:" + zzkkStatusEnum.getStatus() +
            "trdOrderOpTypeEnum:" + trdOrderOpTypeEnum.getOperation());
    }



}
