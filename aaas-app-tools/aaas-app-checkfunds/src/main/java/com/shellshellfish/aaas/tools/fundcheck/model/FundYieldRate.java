package com.shellshellfish.aaas.tools.fundcheck.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "fund_yieldrate")
public class FundYieldRate {


  @Id
  @Field( value = "_id")
  String id;
  @Indexed(name ="code", direction = IndexDirection.DESCENDING)
  @Field( value = "code")
  String code;
  @Field( value = "ACCUMULATEDNAV")
  Double navaccum = Double.MIN_VALUE;
  @Field( value = "ADJUSTEDNAV")
  Double navadj = Double.MIN_VALUE;
  @Field( value = "navlatestdate")
  Long navLatestDate;
  @Field( value = "navreturnrankingp")
  String navreturnrankingp;
  @Field( value = "navreturnrankingpctp")
  String navreturnrankingpctp;
  @Field( value = "navsimiavgreturnp")
  Double navsimiavgreturnp = Double.MIN_VALUE;
  @Field( value = "UNITNAV")
  Double navunit = Double.MIN_VALUE;
  @Indexed(name ="querydate", direction = IndexDirection.DESCENDING)
  @Field( value = "querydate")
  Long querydate;
  @Field( value = "update")
  Long update;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Double getNavaccum() {
    return navaccum;
  }

  public void setNavaccum(Double navaccum) {
    this.navaccum = navaccum;
  }

  public Double getNavadj() {
    return navadj;
  }

  public void setNavadj(Double navadj) {
    this.navadj = navadj;
  }

  public Long getNavLatestDate() {
    return navLatestDate;
  }

  public void setNavLatestDate(Long navLatestDate) {
    this.navLatestDate = navLatestDate;
  }

  public String getNavreturnrankingp() {
    return navreturnrankingp;
  }

  public void setNavreturnrankingp(String navreturnrankingp) {
    this.navreturnrankingp = navreturnrankingp;
  }

  public String getNavreturnrankingpctp() {
    return navreturnrankingpctp;
  }

  public void setNavreturnrankingpctp(String navreturnrankingpctp) {
    this.navreturnrankingpctp = navreturnrankingpctp;
  }

  public Double getNavsimiavgreturnp() {
    return navsimiavgreturnp;
  }

  public void setNavsimiavgreturnp(Double navsimiavgreturnp) {
    this.navsimiavgreturnp = navsimiavgreturnp;
  }

  public Double getNavunit() {
    return navunit;
  }

  public void setNavunit(Double navunit) {
    this.navunit = navunit;
  }

  public Long getQuerydate() {
    return querydate;
  }

  public void setQuerydate(Long querydate) {
    this.querydate = querydate;
  }

  public Long getUpdate() {
    return update;
  }

  public void setUpdate(Long update) {
    this.update = update;
  }
}
