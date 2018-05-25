package com.shellshellfish.aaas.zhongzhengapi.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by chenwei on 2018- 五月 - 23
 */
@Document(collection = "fundrisk_check_log")
public class FundRiskCheckLog implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  @Field( value = "fund_code")
  private String fundCode;

  @Field( value = "fund_risk")
  private int fundRisk;

  @Field(value="can_buy")
  private Boolean canBuy;

  @Field(value="check_date")
  private Date checkDate;

  @Field(value="apply_serial")
  private String applySerial;

  @Field(value="apply_sum")
  private Long applySum;

  @Field(value="apply_num")
  private Long applyNum;

  @Field(value="confirm_sum")
  private Long confirmSum;

  @Field(value="confirm_num")
  private Long confirmNum;

  @Field(value = "status")
  private String status;

  @Field(value = "outside_order_no")
  private String outsideOrderNo;

  public String getFundCode() {
    return fundCode;
  }

  public void setFundCode(String fundCode) {
    this.fundCode = fundCode;
  }

  public int getFundRisk() {
    return fundRisk;
  }

  public void setFundRisk(int fundRisk) {
    this.fundRisk = fundRisk;
  }

  public Boolean getCanBuy() {
    return canBuy;
  }

  public void setCanBuy(Boolean canBuy) {
    this.canBuy = canBuy;
  }

  public Date getCheckDate() {
    return checkDate;
  }

  public void setCheckDate(Date checkDate) {
    this.checkDate = checkDate;
  }

  public String getApplySerial() {
    return applySerial;
  }

  public void setApplySerial(String applySerial) {
    this.applySerial = applySerial;
  }

  public Long getApplySum() {
    return applySum;
  }

  public void setApplySum(Long applySum) {
    this.applySum = applySum;
  }

  public Long getApplyNum() {
    return applyNum;
  }

  public void setApplyNum(Long applyNum) {
    this.applyNum = applyNum;
  }

  public Long getConfirmSum() {
    return confirmSum;
  }

  public void setConfirmSum(Long confirmSum) {
    this.confirmSum = confirmSum;
  }

  public Long getConfirmNum() {
    return confirmNum;
  }

  public void setConfirmNum(Long confirmNum) {
    this.confirmNum = confirmNum;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getOutsideOrderNo() {
    return outsideOrderNo;
  }

  public void setOutsideOrderNo(String outsideOrderNo) {
    this.outsideOrderNo = outsideOrderNo;
  }
}
