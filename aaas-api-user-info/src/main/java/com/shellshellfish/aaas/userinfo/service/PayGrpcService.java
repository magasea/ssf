package com.shellshellfish.aaas.userinfo.service;

import com.shellshellfish.aaas.finance.trade.pay.FundNetInfo;
import java.util.List;

/**
 * @Author pierre 18-1-20
 */
public interface PayGrpcService {


	List<FundNetInfo> getFundNetInfosFromZZ(String userPid, String fundCode, int days);
}
