package com.shellshellfish.aaas.datacollection.server.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by chenwei on 2018- 一月 - 05
 */
@Getter
@Setter
@Document(collection = "fundbaseclose")
public class FundBaseClose {
  @Id
  @Field( value = "_id")
  String id;

  //yyyy-MM-dd
  @Indexed(name ="date", direction = IndexDirection.DESCENDING)
  @Field( value =  "date")
  String date;

  @Field( value = "000300SH" )
  String sh000300;

  @Field( value = "H11001CSI" )
  String h11001csi;

  @Field( value = "000905SH")
  String sh00905;

  @Field( value = "GDAXIGI")
  String gdaxigi;

  @Field(value = "datestamp")
  Long dateStamp;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getSh000300() {
    return sh000300;
  }

  public void setSh000300(String sh000300) {
    this.sh000300 = sh000300;
  }

  public String getH11001csi() {
    return h11001csi;
  }

  public void setH11001csi(String h11001csi) {
    this.h11001csi = h11001csi;
  }

  public String getSh00905() {
    return sh00905;
  }

  public void setSh00905(String sh00905) {
    this.sh00905 = sh00905;
  }

  public String getGdaxigi() {
    return gdaxigi;
  }

  public void setGdaxigi(String gdaxigi) {
    this.gdaxigi = gdaxigi;
  }

  public Long getDateStamp() {
    return dateStamp;
  }

  public void setDateStamp(Long dateStamp) {
    this.dateStamp = dateStamp;
  }
}
