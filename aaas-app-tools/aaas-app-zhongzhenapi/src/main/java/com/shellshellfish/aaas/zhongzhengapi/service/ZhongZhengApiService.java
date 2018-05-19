package com.shellshellfish.aaas.zhongzhengapi.service;

import com.shellshellfish.aaas.common.grpc.zzapi.ApplyResult;
import com.shellshellfish.aaas.common.grpc.zzapi.WalletApplyResult;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZSellWltRlt;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZWltInfoRlt;
import com.shellshellfish.aaas.zhongzhengapi.model.BankZhongZhenInfo;
import com.shellshellfish.aaas.zhongzhengapi.model.CancelTradeResult;
import com.shellshellfish.aaas.zhongzhengapi.model.SellResult;
import java.util.List;

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

  /**
   *
   * @param outsideOrderNo
   * @param trdAcco
   * @return
   */
  List<ApplyResult> getApplyResultByOutSideOrderNo(String outsideOrderNo, String trdAcco, String pid)
      throws Exception;


  /**
   *
   * @param applySerial
   * @param trdAcco
   * @param pid
   * @return
   * @throws Exception
   */
  List<ApplyResult> getApplyResultByApplySerial(String applySerial, String trdAcco, String pid)
      throws Exception;


  /**
   *
   * @param trdAcco
   * @return
   */
  List<ApplyResult> getApplyResultByTrdAcco(String trdAcco, String pid)
      throws Exception;


  /**
   *
   * @param trdAcco
   * @return
   */
  List<ApplyResult> getConfirmResultByTrdAcco(String trdAcco, String pid)
      throws Exception;


  /**
   *
   * @param trdAcco
   * @param pid
   * @param applySum
   * @param outsideOrderNo
   * @return
   */
  public WalletApplyResult applyWallet(String trdAcco, String pid, String applySum, String
      outsideOrderNo ) throws Exception;

  /**
   *
   * @param pid
   * @return
   * @throws Exception
   */
  public List<ZZWltInfoRlt> getWltInfo(String pid) throws Exception;


  /**
   *
   * @param pid
   * @param sellNum
   * @param outsideOrderNo
   * @param tradeAcco
   * @return
   */
  public ZZSellWltRlt sellWallet(String pid, String sellNum, String outsideOrderNo, String
      tradeAcco) throws Exception;

}
