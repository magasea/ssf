package com.shellshellfish.aaas.common.grpc.finance.product;

/**
 * Created by chenwei on 2017- 十二月 - 15
 */

public class ProductDetailQueryInfo {

  public Integer getPageNum() {
    return pageNum;
  }

  public void setPageNum(Integer pageNum) {
    this.pageNum = pageNum;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Integer getProdId() {
    return prodId;
  }

  public void setProdId(Integer prodId) {
    this.prodId = prodId;
  }

  public Integer getGroupId() {
    return groupId;
  }

  public void setGroupId(Integer groupId) {
    this.groupId = groupId;
  }

  Integer pageNum = 1;//理财产品分页页号
  Integer pageSize = 2;//理财分页页面size
  Integer prodId = 3;//理财产品Id
  Integer groupId = 4; //理财产品类组id
}
