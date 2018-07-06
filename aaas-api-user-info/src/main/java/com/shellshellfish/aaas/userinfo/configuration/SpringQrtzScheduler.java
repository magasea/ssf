package com.shellshellfish.aaas.userinfo.configuration;

import com.shellshellfish.aaas.userinfo.scheduler.CheckCaculateBaseJob;
import com.shellshellfish.aaas.userinfo.scheduler.CheckPendingRecordsJob;
import com.shellshellfish.aaas.userinfo.scheduler.CheckPendingrecd4Navadj;
import com.shellshellfish.aaas.userinfo.scheduler.PatchCheckPendingRecords;
import java.io.IOException;
import javax.annotation.PostConstruct;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
@ConditionalOnExpression("'${using.spring.schedulerFactory}'=='true'")
public class SpringQrtzScheduler {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${cron.frequency.jobCheckPendingRecords}")
    String cronExpr;

    @Value("${cron.frequency.jobUpdateAndCaculateUPD}")
    String cronUpdateAndCaculate;

    @Value("${cron.frequency.jobPatchChkPendingRecordByZZInfo}")
    String cronPatchChkPendingRecordByZZInfo;

    @Value("${cron.frequency.jobPatchChkPendingRecordNavadj}")
    String cronPatchChkPendingRecordNavadj;

    @PostConstruct
    public void init() {
        logger.info("Hello world from Spring...:{} \n :{}",cronExpr, cronUpdateAndCaculate);
    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        logger.debug("Configuring Job factory");

        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public Scheduler scheduler(Trigger triggerCheckPendingRecords, JobDetail jobCheckPendingRecords) throws
        SchedulerException, IOException {

        StdSchedulerFactory factory = new StdSchedulerFactory();
//        factory.initialize(new ClassPathResource("quartz.properties").getInputStream());

        logger.debug("Getting a handle to the Scheduler");
        Scheduler scheduler = factory.getScheduler();
        scheduler.setJobFactory(springBeanJobFactory());
        scheduler.scheduleJob(jobCheckPendingRecords ,triggerCheckPendingRecords );
        scheduler.scheduleJob(jobCaculateAndUpdate() ,triggerCalculateAndUpdate() );
        scheduler.scheduleJob(jobPatchChkPendingRecordByZZInfo() ,
            triggerPatchChkPendingRecordByZZInfo() );
        scheduler.scheduleJob(jobPatchChkPendingRecordNavadj() ,
            triggerPatchChkPendingRecordNavadj() );
        logger.debug("Starting Scheduler threads");
        scheduler.start();
        return scheduler;
    }


    @Bean
    public JobDetail jobCheckPendingRecords() {
        JobKey jobKey = new JobKey("Qrtz_Job_CheckPendingRecords", "userInfo");
        JobDetail job = JobBuilder.newJob(CheckPendingRecordsJob.class).withIdentity(jobKey).storeDurably()
            .withDescription("Invoke CheckPendingRecords Job service...").build();
        return  job;
    }

    @Bean
    public JobDetail jobCaculateAndUpdate() {
        JobKey jobKey = new JobKey("Qrtz_Job_CaculateAndUpdate", "userInfo");
        JobDetail job = JobBuilder.newJob(CheckCaculateBaseJob.class).withIdentity(jobKey).storeDurably()
            .withDescription("Invoke CalculateAndUpdate Job service...").build();
        return  job;
    }



    @Bean
    public JobDetail jobPatchChkPendingRecordByZZInfo() {
        JobKey jobKey = new JobKey("Qrtz_Job_PatchChkPendingRecordByZZInfo", "userInfo");
        JobDetail job = JobBuilder.newJob(PatchCheckPendingRecords.class).withIdentity(jobKey).storeDurably()
            .withDescription("Invoke CalculateAndUpdate Job service...").build();
        return  job;
    }

    @Bean
    public JobDetail jobPatchChkPendingRecordNavadj() {
        JobKey jobKey = new JobKey("Qrtz_Job_PatchChkPendingRecordNavadj", "userInfo");
        JobDetail job = JobBuilder.newJob(CheckPendingrecd4Navadj.class).withIdentity(jobKey).storeDurably()
            .withDescription("Invoke CalculateAndUpdate Job service...").build();
        return  job;
    }

    @Bean
    public Trigger  triggerCheckPendingRecords() {


        Trigger trigger = TriggerBuilder
            .newTrigger()
            .withIdentity("Qrtz_Trigger_CheckPendingRecords", "userInfo")
            .withSchedule(CronScheduleBuilder.cronSchedule(cronExpr))
            .build();
        return trigger;
    }

    @Bean
    public Trigger  triggerCalculateAndUpdate() {


        Trigger trigger = TriggerBuilder
            .newTrigger()
            .withIdentity("Qrtz_Trigger_CalculateAndUpdate", "userInfo")
            .withSchedule(CronScheduleBuilder.cronSchedule(cronUpdateAndCaculate))
            .build();
        return trigger;
    }

    @Bean
    public Trigger  triggerPatchChkPendingRecordByZZInfo() {


        Trigger trigger = TriggerBuilder
            .newTrigger()
            .withIdentity("Qrtz_Trigger_PatchChkPendingRecordByZZInfo", "userInfo")
            .withSchedule(CronScheduleBuilder.cronSchedule(cronPatchChkPendingRecordByZZInfo))
            .build();
        return trigger;
    }

    @Bean
    public Trigger  triggerPatchChkPendingRecordNavadj() {


        Trigger trigger = TriggerBuilder
            .newTrigger()
            .withIdentity("Qrtz_Trigger_PatchChkPendingRecordNavadj", "userInfo")
            .withSchedule(CronScheduleBuilder.cronSchedule(cronPatchChkPendingRecordNavadj))
            .build();
        return trigger;
    }
//    @Bean
//    public JobDetail jobGetZZConfirmInfoToTrggerPreOrder() {
//
//        return newJob().ofType(CheckPreOrderStatus2TriggerBuyJob.class).storeDurably().withIdentity(JobKey.jobKey("Qrtz_Job_GetZZConfirmInfoToTrggerPreOrder")).withDescription("Invoke GetZZConfirmInfoToTrggerPreOrder Job service...").build();
//    }

//    @Bean
//    public Trigger triggerGetZZConfirmInfoToTrggerPreOrder() {
//
//        Trigger trigger = TriggerBuilder
//            .newTrigger()
//            .withIdentity("Qrtz_Trigger_GetZZConfirmInfoToTrggerPreOrder", "pay")
//            .withSchedule(CronScheduleBuilder.cronSchedule(cronExprJobpreorderpayflowcheck))
//            .build();
//        return trigger;
//    }
}
