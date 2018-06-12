package com.shellshellfish.aaas.userinfo.scheduler;


import com.shellshellfish.aaas.userinfo.service.CaculateUserProdService;
import com.shellshellfish.aaas.userinfo.service.CheckPendingRecordsService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckCaculateBaseJob implements Job {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CaculateUserProdService caculateUserProdService;

    public void execute(JobExecutionContext context) throws JobExecutionException {

        logger.info("Job ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());
        caculateUserProdService.updateCaculateBase();

        logger.info("Next job scheduled @ {}", context.getNextFireTime());
    }
}
