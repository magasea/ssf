package com.shellshellfish.aaas.userinfo.configuration;

import com.shellshellfish.aaas.userinfo.scheduler.CheckProductsJob;
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
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
@ConditionalOnExpression("'${using.spring.schedulerFactory}'=='true'")
public class SpringQrtzScheduler {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationContext applicationContext;



    @Value("${cron.frequency.jobinvalidprodcheck}")
    String cronExprJobInvalidProdCheck;

    @PostConstruct
    public void init() {
        logger.info("Hello world from Spring...");
    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        logger.debug("Configuring Job factory");

        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }



    @Bean
    public Scheduler scheduler(Trigger triggerInvalidProdCheck, JobDetail
        jobInvalidProdCheck) throws
        SchedulerException, IOException {
        StdSchedulerFactory factory = new StdSchedulerFactory();
        logger.debug("Getting a handle to the Scheduler");
        Scheduler scheduler = factory.getScheduler();
        scheduler.setJobFactory(springBeanJobFactory());
        scheduler.scheduleJob(jobInvalidProdCheck, triggerInvalidProdCheck);
        logger.debug("Starting Scheduler threads");
        scheduler.start();
        return scheduler;
    }

//    @Bean
//    public JobDetail jobDetail() {
//
//        return newJob().ofType(CheckFundsBuyJob.class).storeDurably().withIdentity(JobKey.jobKey("Qrtz_Job_Detail")).withDescription("Invoke Sample Job service...").build();
//    }

    @Bean
    public JobDetail jobInvalidProdCheck() {
        JobKey jobKey = new JobKey("Qrtz_Job_jobInvalidProdCheck", "userInfo");
        JobDetail job = JobBuilder.newJob(CheckProductsJob.class).withIdentity(jobKey)
            .withDescription("Invoke jobInvalidProdCheck Job service...").build();
        return  job;
    }

    @Bean
    public Trigger  triggerInvalidProdCheck() {


        Trigger trigger = TriggerBuilder
            .newTrigger()
            .withIdentity("Qrtz_Trigger_InvalidProdCheck", "userInfo")
            .withSchedule(CronScheduleBuilder.cronSchedule(cronExprJobInvalidProdCheck))
            .build();
        return trigger;
    }



}
