package com.shellshellfish.aaas.finance.trade.pay.model.dao.mongo;

import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by chenwei on 2018- 一月 - 31
 */
@Document(collection = "fund_netinfo")
public class MongoFundNetInfo {
  @Id
  String id;
  @Field(value = "fund_code")
  String fundCode;
  @Field(value = "unit_net")
  String unitNet;
  @Field(value = "accum_net")
  String accumNet;
  @Field(value = "chng_pct")
  String chngPct;

  @Field( value = "tenthou_unit_incm")
  String tenThouUnitIncm;

  @Field(value = "trade_date")
  @Indexed(direction = IndexDirection.DESCENDING)
  String tradeDate;



  public String getFundCode() {
    return fundCode;
  }

  public void setFundCode(String fundCode) {
    this.fundCode = fundCode;
  }

  public String getUnitNet() {
    return unitNet;
  }

  public void setUnitNet(String unitNet) {
    this.unitNet = unitNet;
  }

  public String getAccumNet() {
    return accumNet;
  }

  public void setAccumNet(String accumNet) {
    this.accumNet = accumNet;
  }

  public String getChngPct() {
    return chngPct;
  }

  public void setChngPct(String chngPct) {
    this.chngPct = chngPct;
  }

  public String getTradeDate() {
    return tradeDate;
  }

  public void setTradeDate(String tradeDate) {
    this.tradeDate = tradeDate;
  }
}
