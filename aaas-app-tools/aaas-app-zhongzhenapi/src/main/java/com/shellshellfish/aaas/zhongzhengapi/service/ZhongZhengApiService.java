package com.shellshellfish.aaas.zhongzhengapi.service;

import com.shellshellfish.aaas.zhongzhengapi.model.BankZhongZhenInfo;
import com.shellshellfish.aaas.zhongzhengapi.model.CancelTradeResult;
import com.shellshellfish.aaas.zhongzhengapi.model.SellResult;
import java.util.List;
import java.util.Map;

/**
 * Created by chenwei on 2018- 四月 - 03
 */
public interface ZhongZhengApiService {


  List<BankZhongZhenInfo> getSupportBankList();

  /**
   * 撤单
   * @param applySerial
   * @return
   */
  CancelTradeResult cancelTrade(String applySerial, String pid);

  /**
   *
   * @param sellNum
   * @param outsideOrderNo
   * @param trdAcco
   * @param fundCode
   * @param sellType
   * @return
   */
  SellResult sellFund(String sellNum, String outsideOrderNo, String trdAcco, String
      fundCode, String sellType, String pid) throws Exception;


}
