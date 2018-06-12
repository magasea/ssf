package com.shellshellfish.aaas.userinfo.scheduler;

import com.shellshellfish.aaas.userinfo.service.CaculateUserProdService;
import com.shellshellfish.aaas.userinfo.service.CheckPendingRecordsService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.quartz.Job;

/**
 * Created by chenwei on 2018- 六月 - 11
 */

public class PatchCheckPendingRecords implements Job{
  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private CheckPendingRecordsService checkPendingRecordsService;

  public void execute(JobExecutionContext context) throws JobExecutionException {

    logger.info("Job ** {} ** fired @ {}", context.getJobDetail().getKey().getName(), context.getFireTime());
    checkPendingRecordsService.patchChkPendingRecordFromZZInfo();

    logger.info("Next job scheduled @ {}", context.getNextFireTime());
  }
}
