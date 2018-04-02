package com.shellshellfish.aaas.common.enums;

/**
 * Created by chenwei on 2018- 三月 - 15
 */

public enum FundClassEnum {
  COMMERSIAL(1,"商品型基金"), QDII(2, "QDII基金"), COMMONSTOCK(3, "普通股票型基金"), PASIDX(4,"被动指数型基金"),
  ENHANCEIDX(5,"增强指数型基金"), LIKESTOK(6,"偏股混合型基金"), PROTECTBASE(7,"保本型基金"), LONGDEBTT(8,"长期纯债型基金"),
  COMB2(9,"混合债券型基金（二级）"),COMB1(10,"混合债券型基金（一级）"),LIKEDEBIT(11,"偏债混合型基金"), MONEY(12,"货币市场型基金"), BALANCEDHYBRID(13,"平衡混合型基金");
  int fundClassId;
  String fundClassComment;

  FundClassEnum(int fundClassId, String fundClassComment){
    this.fundClassId = fundClassId;
    this.fundClassComment = fundClassComment;
  }

  public int getFundClassId() {
    return fundClassId;
  }

  public void setFundClassId(int fundClassId) {
    this.fundClassId = fundClassId;
  }

  public String getFundClassComment() {
    return fundClassComment;
  }

  public void setFundClassComment(String fundClassComment) {
    this.fundClassComment = fundClassComment;
  }
  
  public static String getComment(int fundClassId){

    for (FundClassEnum enumItem : FundClassEnum.values()) {
      if (enumItem.getFundClassId() == fundClassId) {
        return enumItem.getFundClassComment();
      }
    }
    throw new IllegalArgumentException("input operation:"+fundClassId+" is illeagal");
  }
  
  public static int getFundClassId(String fundClassComment) {

    for (FundClassEnum enumItem : FundClassEnum.values()) {
      if (fundClassComment.equals(enumItem.getFundClassComment())) {
        return enumItem.getFundClassId();
      }
    }
    throw new IllegalArgumentException("input operation:" + fundClassComment + " is illeagal");
  }
}
