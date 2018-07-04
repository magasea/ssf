package com.shellshellfish.aaas.common.enums;

public enum TrdFailtureStatusEnum {
  SHARENOTENOUGH(1309,"1309:可用份额不足"),NETERROR(1018,"1018:网络错误"),APPLYSHARESMALL(1019,"1019:申请值太小"),
  BOOKSHARELESSHOLD(1326,"1326:赎回后全商户下，账面份额低于最低可持有份额"),OUTORDERIDREPEAT(1013,"1013:该外部订单号重复"),
  USERLOGINFAILTURE(1303,"1303:用户登录失败"),FUNDSTOPCONTRADE(2000,"1019:基金当前已停止该交易"),
  FUNDSTATESTOPTRADE(2001,"1019:基金状态[停止交易],不能做[024]交易"),FUNDNOTSUPTRADE(2002,"不支持当前业务"),
  PARAMEXCEPTION(2003,"1329:金额/份额参数异常，必须大于0.01"),FUNDSTOPTRADE(2004,"1335:基金停止交易");
  private int status;
  private String comment;

  TrdFailtureStatusEnum(int status, String comment) {
    this.status = status;
    this.comment = comment;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  //处理交易失败错误原因
  public static String getTradeFailReson(String errMsg) {
    if(errMsg==null)
      return "未知原因，请联系客服";
    if(errMsg.contains(TrdFailtureStatusEnum.NETERROR.getComment())){
      return  "网络错误";
    }else if(errMsg.contains(TrdFailtureStatusEnum.OUTORDERIDREPEAT.getComment())){
      return "外部订单号重复";
    }else if(errMsg.contains(TrdFailtureStatusEnum.APPLYSHARESMALL.getComment())){
      return  "申请值太小，低于最小交易限额";
    }else if(errMsg.contains(TrdFailtureStatusEnum.BOOKSHARELESSHOLD.getComment())){
      return  "赎回后全商户下，账面份额低于最低可持有份额";
    }else if(errMsg.contains(TrdFailtureStatusEnum.SHARENOTENOUGH.getComment())){
      return  "可用份额不足";
    }else if(errMsg.contains(TrdFailtureStatusEnum.USERLOGINFAILTURE.getComment())){
      return  "用户登录失败";
    }else if(errMsg.contains(TrdFailtureStatusEnum.FUNDSTOPCONTRADE.getComment())){
      return  "基金当前已停止该交易";
    }else if(errMsg.contains(TrdFailtureStatusEnum.FUNDSTATESTOPTRADE.getComment())){
      return  "基金状态[停止交易],不能做[024]交易";
    }else if(errMsg.contains(TrdFailtureStatusEnum.FUNDNOTSUPTRADE.getComment())){
      return  "不支持当前业务";
    }else if(errMsg.contains(TrdFailtureStatusEnum.PARAMEXCEPTION.getComment())){
      return  "金额/份额参数异常，必须大于0.01";
    }else if(errMsg.contains(TrdFailtureStatusEnum.FUNDSTOPTRADE.getComment())){
      return  "基金停止交易";
    } else {
      return  "未知原因，请联系客服";
    }
  }

}


