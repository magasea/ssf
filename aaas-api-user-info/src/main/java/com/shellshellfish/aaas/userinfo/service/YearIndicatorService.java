package com.shellshellfish.aaas.userinfo.service;


import com.shellshellfish.aaas.datamanager.YearIndicatorQuery;
import com.shellshellfish.aaas.datamanager.YearIndicatorRpc;

import java.util.concurrent.ExecutionException;


public interface YearIndicatorService {

	/**
	 * 查询历史净值   rpc 调用data-manager
	 * @param query
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	 YearIndicatorRpc getHistoryNetByCodeAndQuerydate(YearIndicatorQuery query) throws ExecutionException, InterruptedException;
}
