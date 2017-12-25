package com.shellshellfish.aaas.datacollection.server.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by chenwei on 2017- 十二月 - 25
 */
@Document(collection = "dayindicator")
public class DayIndicator {

  String id;
  String avgprice;
  String discountrate;
  String low;
  String differrange;
  String open;
  Long update;
  String high;
  String amplitude ;
  String turn;
  String close;
  String amount;
  String differ;
  String code;
  Long querydate;
  String volume;
  String discount;
  String preclose;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAvgprice() {
    return avgprice;
  }

  public void setAvgprice(String avgprice) {
    this.avgprice = avgprice;
  }

  public String getDiscountrate() {
    return discountrate;
  }

  public void setDiscountrate(String discountrate) {
    this.discountrate = discountrate;
  }

  public String getLow() {
    return low;
  }

  public void setLow(String low) {
    this.low = low;
  }

  public String getDifferrange() {
    return differrange;
  }

  public void setDifferrange(String differrange) {
    this.differrange = differrange;
  }

  public String getOpen() {
    return open;
  }

  public void setOpen(String open) {
    this.open = open;
  }

  public Long getUpdate() {
    return update;
  }

  public void setUpdate(Long update) {
    this.update = update;
  }

  public String getHigh() {
    return high;
  }

  public void setHigh(String high) {
    this.high = high;
  }

  public String getAmplitude() {
    return amplitude;
  }

  public void setAmplitude(String amplitude) {
    this.amplitude = amplitude;
  }

  public String getTurn() {
    return turn;
  }

  public void setTurn(String turn) {
    this.turn = turn;
  }

  public String getClose() {
    return close;
  }

  public void setClose(String close) {
    this.close = close;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getDiffer() {
    return differ;
  }

  public void setDiffer(String differ) {
    this.differ = differ;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Long getQuerydate() {
    return querydate;
  }

  public void setQuerydate(Long querydate) {
    this.querydate = querydate;
  }

  public String getVolume() {
    return volume;
  }

  public void setVolume(String volume) {
    this.volume = volume;
  }

  public String getDiscount() {
    return discount;
  }

  public void setDiscount(String discount) {
    this.discount = discount;
  }

  public String getPreclose() {
    return preclose;
  }

  public void setPreclose(String preclose) {
    this.preclose = preclose;
  }
}
