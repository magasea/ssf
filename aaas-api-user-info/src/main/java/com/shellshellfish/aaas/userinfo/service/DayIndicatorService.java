package com.shellshellfish.aaas.userinfo.service;


import com.shellshellfish.aaas.datamanager.DayIndicatorQuery;
import com.shellshellfish.aaas.datamanager.DayIndicatorRpc;

import java.util.List;
import java.util.concurrent.ExecutionException;


public interface DayIndicatorService {

	 List<DayIndicatorRpc> getDayIndicatorsByCode(DayIndicatorQuery query) throws ExecutionException, InterruptedException;
}
