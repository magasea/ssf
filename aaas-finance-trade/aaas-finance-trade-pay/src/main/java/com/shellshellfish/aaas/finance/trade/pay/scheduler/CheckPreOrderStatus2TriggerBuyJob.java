package com.shellshellfish.aaas.finance.trade.pay.scheduler;


import com.shellshellfish.aaas.finance.trade.pay.service.impl.CheckFundsTradeJobService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckPreOrderStatus2TriggerBuyJob implements Job {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CheckFundsTradeJobService jobService;

    public void execute(JobExecutionContext context) throws JobExecutionException {

        logger.info("Job ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());

//        jobService.executePreOrderStatus();

        logger.info("Next job scheduled @ {}", context.getNextFireTime());
    }
}
