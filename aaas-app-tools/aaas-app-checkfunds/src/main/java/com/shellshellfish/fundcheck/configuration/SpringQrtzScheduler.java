package com.shellshellfish.fundcheck.configuration;


import com.shellshellfish.fundcheck.scheduler.CheckFundsCSVInfoJob;
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


    @Value("${cron.frequency.jobCheckFunds}")
    String cronExprJobCheckFunds;

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
    public Scheduler scheduler(Trigger triggerCheckFunds, JobDetail
        jobCheckFunds) throws
        SchedulerException, IOException {
        StdSchedulerFactory factory = new StdSchedulerFactory();
        logger.debug("Getting a handle to the Scheduler");
        Scheduler scheduler = factory.getScheduler();
        scheduler.setJobFactory(springBeanJobFactory());
        scheduler.scheduleJob(jobCheckFunds, triggerCheckFunds);
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
    public JobDetail jobCheckFunds() {
        JobKey jobKey = new JobKey("Qrtz_Job_CheckFunds", "checkers");
        JobDetail job = JobBuilder.newJob(CheckFundsCSVInfoJob.class).withIdentity(jobKey)
            .withDescription("Invoke Qrtz_Job_CheckFunds Job service...").build();
        return  job;
    }

    @Bean
    public Trigger  triggerCheckFunds() {


        Trigger trigger = TriggerBuilder
            .newTrigger()
            .withIdentity("Qrtz_Job_CheckFunds", "checkers")
            .withSchedule(CronScheduleBuilder.cronSchedule(cronExprJobCheckFunds))
            .build();
        return trigger;
    }

}
