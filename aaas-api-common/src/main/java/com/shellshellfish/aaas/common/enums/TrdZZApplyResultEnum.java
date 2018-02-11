package com.shellshellfish.aaas.common.enums;

/**
 * Created by chenwei on 2018- 二月 - 11
 */
public enum TrdZZApplyResultEnum {
  SUCCESS(0000,"成功"),
  IFMISMATCHPARAM(1000,"接口方法与参数不对应"),
  TIMEGT5(1001,"时间差超过 5 分钟"),
  PLATFORMNOTEXIST(1002,"平台不存在！请检查参数"),
  VERFCODEMISMATCH(1003,"校验码不匹配"),
  RISKQUERYNOTFINISH(1004,"您未填写基金风险问卷"),
  MISSINGPARAM1(1006,"缺少本接口必传参数"),
  MISSINGPARAM2(1007,"缺少本接口必传参数"),
  USERNOTEXIST(1008,"该用户不存在"),
  CARDALREADYEXIST(1009,"此卡已存在"),
  BANKCARDNUMMISMATCHBANK(1010,"银行卡号与银行不匹配"),
  BUFAILED(1011,"银联验证失败"),
  USERNOTHOLDACC(1012,"该用户无此交易账号"),
  OUTSIDEORDERDUP(1013,"该外部订单号重复"),
  COMPANYPRIVPARAMERR(1014,"商户私有参数错误"),
  MISMUSTPARAMALLSITE(1015,"缺少全站必传参数"),
  INFOEMPTY(1016,"查询信息为空"),
  NETWORKERR(1018,"网络错误"),
  BIZLGYERR(1019,"业务逻辑错误"),
  UKNERR(1020,"未知错误"),
  PARAMTYPEERR(1021,"参数类型或格式错误"),
  FUNDNOTAUTH(1022,"无代销该基金权限，或基金不存在"),
  NOSUCHTRDACC(1023,"平台下无此交易账号"),
  USERWITHNOFUNDACC(1024,"该用户尚未开基金户"),
  TRANSFERGOLDERR(1025,"换算金价异常"),
  EXCEEDMAX(1026,"超过最大可用克数"),
  TRDACCOFUNDCODENONEXIST(1027,"不存在的交易账号或基金代码"),
  NOTENOUGHSHARE(1028,"可用份额不足"),
  NOTENOUGHMONEY(1029,"可消费金额不足");

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  int code;
  String comment;
  TrdZZApplyResultEnum(int code, String comment){
    this.code = code;
    this.comment = comment;
  }

  public static String getComment(int code){

    for (TrdZZApplyResultEnum enumItem : TrdZZApplyResultEnum.values()) {
      if (enumItem.getCode() == code) {
        return enumItem.getComment();
      }
    }
    throw new IllegalArgumentException("input status:"+code+" is illeagal");
  }


  public static TrdZZApplyResultEnum getByCode(int code){
    for (TrdZZApplyResultEnum enumItem : TrdZZApplyResultEnum.values()) {
      if (enumItem.getCode() == code) {
        return enumItem;
      }
    }
    throw new IllegalArgumentException("input status:"+code+" is illeagal");
  }
}
