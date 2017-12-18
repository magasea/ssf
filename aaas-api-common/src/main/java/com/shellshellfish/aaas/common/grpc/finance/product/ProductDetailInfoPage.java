package com.shellshellfish.aaas.common.grpc.finance.product;

import java.util.List;
import java.util.Map;

/**
 * Created by chenwei on 2017- 十二月 - 15
 */

public class ProductDetailInfoPage {

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

  public Integer getGroupId() {
    return groupId;
  }

  public void setGroupId(Integer groupId) {
    this.groupId = groupId;
  }

  public Integer getProdId() {
    return prodId;
  }

  public void setProdId(Integer prodId) {
    this.prodId = prodId;
  }

  public List<Map<String, Object>> getItems() {
    return items;
  }

  public void setItems(List<Map<String, Object>> items) {
    this.items = items;
  }

  Integer pageNum;
  Integer pageSize;
  Integer groupId;
  Integer prodId;

  List<Map<String, Object>> items;



}
