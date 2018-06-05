package com.shellshellfish.aaas.finance.trade.pay.service;

import com.shellshellfish.aaas.common.grpc.datacollection.DCDailyFunds;
import java.util.List;

/**
 * Created by developer4 on 2018- 六月 - 04
 */

public interface DataCollectionService {
    public List<DCDailyFunds> getFundDataOfDay(List<String> codes, String startOfDay, String
        endOfDay);
}
