package com.shellshellfish.aaas.finance.trade.order.service;

import com.shellshellfish.aaas.common.grpc.zzapi.ApplyResult;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZBankInfo;
import com.shellshellfish.aaas.common.grpc.zzapi.ZZFundInfo;
import com.shellshellfish.aaas.tools.zhongzhengapi.ZZFundBaseInfo;
import java.util.List;

/**
 * Created by chenwei on 2018- 五月 - 07
 */
public interface ZZApiService {
  List<ZZBankInfo> getZZSupportedBanks() throws Exception;

  /**
   *
   * @param trdAcco
   * @param pid
   * @param applySerial
   * @return
   * @throws Exception
   */
  List<ApplyResult> getZZApplyResultByApplySerial(String trdAcco, String pid, String applySerial)
      throws Exception;

  /**
   *
   * @return
   */
  List<ZZFundInfo> getAllZZFundInfo() throws Exception;

}
