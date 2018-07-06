package com.shellshellfish.aaas.userinfo.scheduler;


import com.shellshellfish.aaas.userinfo.service.CheckPendingRecordsService;
import com.shellshellfish.aaas.userinfo.service.CheckProductJobService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckPendingRecordsJob implements Job {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CheckPendingRecordsService checkPendingRecordsService;

    public void execute(JobExecutionContext context) throws JobExecutionException {

        logger.info("Job ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());

        checkPendingRecordsService.checkPendingRecords();

        logger.info("Next job scheduled @ {}", context.getNextFireTime());
    }
}
