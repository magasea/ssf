package com.shellshellfish.aaas.common.grpc.datacollection;

public class DCDailyFunds {

  String id;

  String code;

  Double navaccum = Double.MIN_VALUE;

  Double navadj = Double.MIN_VALUE;

  Long navLatestDate;

  String navreturnrankingp;

  String navreturnrankingpctp;

  Double navsimiavgreturnp = Double.MIN_VALUE;

  Double navunit = Double.MIN_VALUE;

  Long querydate;

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
