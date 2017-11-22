package com.shellshellfish.aaas.userinfo.model.dao.userinfo;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Document(collection = "ui_assectdailyrept")
public class UiAssetDailyRept {



  public BigInteger getUserId() {
    return userId;
  }

  public void setUserId(BigInteger userId) {
    this.userId = userId;
  }

  public Long getDate() {
    return date;
  }

  public void setDate(Long date) {
    this.date = date;
  }

  public BigDecimal getDailyProfit() {
    return dailyProfit;
  }

  public void setDailyProfit(BigDecimal dailyProfit) {
    this.dailyProfit = dailyProfit;
  }

  public BigDecimal getAccumulateProfitRate() {
    return accumulateProfitRate;
  }

  public void setAccumulateProfitRate(BigDecimal accumulateProfitRate) {
    this.accumulateProfitRate = accumulateProfitRate;
  }

  public BigDecimal getAccumulateProfit() {
    return accumulateProfit;
  }

  public void setAccumulateProfit(BigDecimal accumulateProfit) {
    this.accumulateProfit = accumulateProfit;
  }

  public String getCurrentProducts() {
    return currentProducts;
  }

  public void setCurrentProducts(String currentProducts) {
    this.currentProducts = currentProducts;
  }

  @JsonIgnore
  @Id
  private String id;
  @JsonIgnore
  private BigInteger userId;
  private Long date;
  private BigDecimal dailyProfit;
  private BigDecimal accumulateProfitRate;
  private BigDecimal accumulateProfit;
  private String currentProducts;

  private BigInteger createdBy;


  private Long createdDate;

  private BigInteger lastModifiedBy;

  private Long lastModifiedDate;

//  @Override
//  public String toString() {
//    long time = date;
//    SimpleDateFormat sdf = new SimpleDateFormat();
//    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//    sdf.format(new Date(time));
//    return "UiAssetDailyRept{" +
//        "userId=" + userId +
//        ", date=" + sdf.format(new Date(time)) +
//        ", dailyProfit=" + dailyProfit +
//        ", accumulateProfitRate=" + accumulateProfitRate +
//        ", accumulateProfit=" + accumulateProfit +
//        ", currentProducts='" + currentProducts + '\'' +
//        '}';
//  }

  @Override
  public String toString() {
    return "UiAssetDailyRept{" +
        "id='" + id + '\'' +
        ", userId=" + userId +
        ", date=" + date +
        ", dailyProfit=" + dailyProfit +
        ", accumulateProfitRate=" + accumulateProfitRate +
        ", accumulateProfit=" + accumulateProfit +
        ", currentProducts='" + currentProducts + '\'' +
        ", createdBy=" + createdBy +
        ", createdDate=" + createdDate +
        ", lastModifiedBy=" + lastModifiedBy +
        ", lastModifiedDate=" + lastModifiedDate +
        '}';
  }
}
