package com.shellshellfish.datamanager.service;

import com.shellshellfish.aaas.datamanager.FundInfos;
import java.util.List;


/**
 * Created by chenwei on 2017- 十二月 - 29
 */

public interface FundInfoGrpcService {

  FundInfos getPriceOfCodes(List<String> codes);

}
