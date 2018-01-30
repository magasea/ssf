package com.shellshellfish.aaas.userinfo.service;

/**
 * Created by chenwei on 2018- 一月 - 26
 */
public interface CheckProductJobService {

  /**
   * 查询数据库里面ui_prod 和 ui_prod_detail
   * 如果所有的ui_prod_detail的状态都是null, 那么把
   * ui_prod 的对应记录设置成 FAILED
   */
  public void checkProductsAndSetStatus();

}
