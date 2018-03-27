package com.shellshellfish.aaas.tools.fundcheck.scheduler;


import com.shellshellfish.aaas.tools.fundcheck.service.FundUpdateJobService;
import java.nio.file.Paths;
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

    @Value("${shellshellfish.csvFundFileOriginName}")
    String csvFundFileOriginName;

    @Value("${shellshellfish.csvBaseFileOriginName}")
    String csvBaseFileOriginName;

    @Autowired
    private FundUpdateJobService fundUpdateJobService;

    public void execute(JobExecutionContext context) throws JobExecutionException {

        logger.info("Job ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());

//        fundUpdateJobService.checkAndUpdateFunds(Paths.get( csvFilePath,
//            csvFundFileOriginName).toString());
//
//        fundUpdateJobService.checkAndUpdateFunds(Paths.get( csvFilePath,
//            csvBaseFileOriginName).toString());

        fundUpdateJobService.pullInfoBaseOnFundAndBaseKeyInfo();

        logger.info("Next job scheduled @ {}", context.getNextFireTime());
    }
}
