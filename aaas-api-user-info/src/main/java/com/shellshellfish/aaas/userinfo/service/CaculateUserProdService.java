package com.shellshellfish.aaas.userinfo.service;

import com.shellshellfish.aaas.userinfo.model.dao.MongoPendingRecords;
import java.security.NoSuchAlgorithmException;

/**
 * Created by chenwei on 2018- 六月 - 08
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
   * update CaculateBase table with the mongoPendingRecords
   * @param mongoPendingRecords
   * @return
   */
  public boolean updateCaculateBase(MongoPendingRecords mongoPendingRecords);

  /**
   * use CaculateBase to caculate result
   * @param userProdId
   * @param fundCode
   * @return
   */
  public boolean caculateQuantityByUserProdIdAndFundCode(Long userProdId, String fundCode)
      throws NoSuchAlgorithmException;

  /**
   * check all userProdId entry
   */
  void updateCaculateBase();
}
