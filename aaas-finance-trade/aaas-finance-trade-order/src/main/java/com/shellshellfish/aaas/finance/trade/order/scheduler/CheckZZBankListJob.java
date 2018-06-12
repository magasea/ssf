package com.shellshellfish.aaas.finance.trade.order.scheduler;


import com.shellshellfish.aaas.finance.trade.order.service.impl.CheckFundsOrderJobService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckZZBankListJob implements Job {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CheckFundsOrderJobService jobService;

    public void execute(JobExecutionContext context) throws JobExecutionException {

        logger.info("Job ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());

        jobService.executeSampleJob();

        logger.info("Next job scheduled @ {}", context.getNextFireTime());
    }
}
