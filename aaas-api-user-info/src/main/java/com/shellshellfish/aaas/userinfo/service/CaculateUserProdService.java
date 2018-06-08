package com.shellshellfish.aaas.userinfo.service;

/**
 * Created by developer4 on 2018- 六月 - 08
 */
public interface CaculateUserProdService {

  /**
   * update CaculateBase table which is the base for caculate quantity of fund of user product
   * @param userProdId
   * @param fundCode
   * @return
   */
  public boolean updateCaculateBase(Long userProdId, String fundCode);

  /**
   * use CaculateBase to caculate result
   * @param userProdId
   * @param fundCode
   * @return
   */
  public boolean caculateQuantityByUserProdIdAndFundCode(Long userProdId, String fundCode);

  /**
   * check all userProdId entry
   */
  void updateCaculateBase();
}
