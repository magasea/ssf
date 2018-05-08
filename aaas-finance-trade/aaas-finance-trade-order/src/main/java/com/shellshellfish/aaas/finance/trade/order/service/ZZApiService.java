package com.shellshellfish.aaas.finance.trade.order.service;

import com.shellshellfish.aaas.finance.trade.order.model.ZZBankInfo;
import java.util.List;

/**
 * Created by developer4 on 2018- 五月 - 07
 */
public interface ZZApiService {
  List<ZZBankInfo> getZZSupportedBanks() throws Exception;

}
