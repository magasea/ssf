package com.shellshellfish.aaas.zhongzhengapi.service;

import com.shellshellfish.aaas.common.grpc.zzapi.ApplyResult;
import com.shellshellfish.aaas.common.grpc.zzapi.WalletApplyResult;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZAplyCfmInfo;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZDiscountInfo;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZFundInfo;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZFundShareInfo;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZRiskCmtResult;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZSellWltRlt;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZTradeLimit;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZWltAplyInfo;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZWltInfoRlt;
import com.shellshellfish.aaas.zhongzhengapi.model.BankZhongZhenInfo;
import com.shellshellfish.aaas.zhongzhengapi.model.CancelTradeResult;
import com.shellshellfish.aaas.zhongzhengapi.model.SellResult;
import com.shellshellfish.aaas.zhongzhengapi.model.ZZBonusInfo;
import com.shellshellfish.aaas.zhongzhengapi.model.ZZBuyResult;
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
   * @param sellType 0 - 普通赎回 1 - 快速赎回
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
   * @param outsideOrderNo
   * @param applySerial
   * @param trdAcco
   * @param pid
   * @return
   * @throws Exception
   */
  List<ApplyResult> getApplyResults(String outsideOrderNo, String applySerial, String trdAcco,
      String pid) throws  Exception;


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
  List<ZZAplyCfmInfo> getConfirmResult(String trdAcco, String applySerial, String outsideOrderNo,
      String pid)
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
   * @return
   * @throws Exception
   */
  public List<ZZFundShareInfo> getFundShare(String pid) throws Exception;

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


  /**
   *
   * @param pid
   * @param applySerial
   * @return
   */
  public List<ZZWltAplyInfo> getWalletApply(String pid, String outsideOrderNo,
      String applySerial) throws Exception;

  /**
   *
   * @return
   */
  public List<ZZFundInfo> getAllFundInfo() throws Exception;

  /**
   *
   * @param fundCode
   * @return
   */
  public List<ZZTradeLimit> getTradeLimit(String fundCode) throws Exception;


  /**
   *
   * @param pid
   * @param tradeAcco
   * @param fundCode
   * @param fundSum
   * @param outsideOrderNo
   * @return
   */
  public ZZBuyResult buyFund(String pid, String tradeAcco, String fundCode, String fundSum,
      String outsideOrderNo) throws Exception;

  /**
   *
   * @param pid
   * @param fundCode
   * @param startDate
   * @return
   */
  public List<ZZBonusInfo> getBonusInfo(String pid, String fundCode, String startDate)
      throws Exception;

  /**
   *
   * @param pid
   * @param fundCode
   * @return
   */
  public List<ZZDiscountInfo> getDiscountInfo(String pid, String fundCode) throws Exception;


  /**
   *
   * @param pid
   * @param riskAbility
   * @return
   */
  public ZZRiskCmtResult commitRiskLevel(String pid, Integer riskAbility) throws Exception;


}
