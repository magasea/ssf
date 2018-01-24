package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.userinfo.model.dao.UiUser;
import com.shellshellfish.aaas.userinfo.service.UserFinanceProdCalcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

@Component
@Scope("prototype")
public class FinanceProdCalculate implements Callable<Integer> {

	private static final Logger logger = LoggerFactory.getLogger(FinanceProdCalculate.class);

	private UserFinanceProdCalcService userFinanceProdCalcService;

	private CountDownLatch countDownLatch;

	private List<UiUser> list;

	private String date;

	public FinanceProdCalculate(UserFinanceProdCalcService userFinanceProdCalcService,
								String date, List<UiUser> list, CountDownLatch countDownLatch) {
		this.userFinanceProdCalcService = userFinanceProdCalcService;
		this.date = date;
		this.list = list;
		this.countDownLatch = countDownLatch;
	}

	@Override
	public Integer call() {
		try {
			logger.debug("FinanceProdCalculate thread start.");
			userFinanceProdCalcService.dailyCalculation(date, list);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			countDownLatch.countDown();
		}
		return 1;
	}
}
