package com.shellshellfish.datamanager.scheduler;


import com.shellshellfish.datamanager.service.FundUpdateJobService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CheckFundsCSVInfoJob implements Job {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${shellshellfish.csvFilePath}")
    String csvFilePath;

    @Autowired
    private FundUpdateJobService fundUpdateJobService;

    public void execute(JobExecutionContext context) throws JobExecutionException {

        logger.info("Job ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());

        fundUpdateJobService.checkAndUpdateFunds(csvFilePath);

        logger.info("Next job scheduled @ {}", context.getNextFireTime());
    }
}
