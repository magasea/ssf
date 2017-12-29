package com.shellshellfish.aaas.userinfo.model.dto;

/**
 * Created by chenwei on 2017- 十二月 - 25
 */

public class UiProductDetailDTO {

  private long id;
  private long userProdId;
  private String fundCode;
  private String fundName;
  private int fundShare;
  private int fundQuantity;
  private long updateBy;
  private long updateDate;
  private long createBy;
  private long createDate;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getUserProdId() {
    return userProdId;
  }

  public void setUserProdId(long userProdId) {
    this.userProdId = userProdId;
  }

  public String getFundCode() {
    return fundCode;
  }

  public void setFundCode(String fundCode) {
    this.fundCode = fundCode;
  }

  public String getFundName() {
    return fundName;
  }

  public void setFundName(String fundName) {
    this.fundName = fundName;
  }

  public int getFundShare() {
    return fundShare;
  }

  public void setFundShare(int fundShare) {
    this.fundShare = fundShare;
  }

  public int getFundQuantity() {
    return fundQuantity;
  }

  public void setFundQuantity(int fundQuantity) {
    this.fundQuantity = fundQuantity;
  }

  public long getUpdateBy() {
    return updateBy;
  }

  public void setUpdateBy(long updateBy) {
    this.updateBy = updateBy;
  }

  public long getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(long updateDate) {
    this.updateDate = updateDate;
  }

  public long getCreateBy() {
    return createBy;
  }

  public void setCreateBy(long createBy) {
    this.createBy = createBy;
  }

  public long getCreateDate() {
    return createDate;
  }

  public void setCreateDate(long createDate) {
    this.createDate = createDate;
  }

  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (int) (userProdId ^ (userProdId >>> 32));
    result = 31 * result + (fundCode != null ? fundCode.hashCode() : 0);
    result = 31 * result + (fundName != null ? fundName.hashCode() : 0);
    result = 31 * result + fundShare;
    result = 31 * result + fundQuantity;
    result = 31 * result + (int) (updateBy ^ (updateBy >>> 32));
    result = 31 * result + (int) (updateDate ^ (updateDate >>> 32));
    result = 31 * result + (int) (createBy ^ (createBy >>> 32));
    result = 31 * result + (int) (createDate ^ (createDate >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "UiProductDetailDTO{" +
            "id=" + id +
            ", userProdId=" + userProdId +
            ", fundCode='" + fundCode + '\'' +
            ", fundName='" + fundName + '\'' +
            ", fundShare=" + fundShare +
            ", fundQuantity=" + fundQuantity +
            ", updateBy=" + updateBy +
            ", updateDate=" + updateDate +
            ", createBy=" + createBy +
            ", createDate=" + createDate +
            '}';
  }
}
