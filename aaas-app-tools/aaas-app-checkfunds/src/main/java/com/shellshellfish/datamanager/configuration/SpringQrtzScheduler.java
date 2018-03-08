package com.shellshellfish.datamanager.configuration;


import com.shellshellfish.datamanager.scheduler.CheckFundsCSVInfoJob;
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

    @Value("${cron.frequency.jobwithcrontrigger}")
    String cronExpr;

    @Value("${cron.frequency.jobpreorderpayflowcheck}")
    String cronExprJobpreorderpayflowcheck;

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

//
//    @Bean
//    public JobDetailFactoryBean jobDetail() {
//
//        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
//        jobDetailFactory.setJobClass(CheckFundsBuyJob.class);
//        jobDetailFactory.setName("Qrtz_Job_Detail");
//        jobDetailFactory.setDescription("Invoke Sample Job service...");
//        jobDetailFactory.setDurability(true);
//        return jobDetailFactory;
//    }


    @Bean
    public Scheduler scheduler(Trigger triggerGetZZConfirmInfoToUpdatePayFlow, JobDetail
        jobGetZZConfirmInfoToUpdatePayFlow, Trigger triggerGetZZConfirmInfoToTrggerPreOrder, JobDetail jobGetZZConfirmInfoToTrggerPreOrder) throws
        SchedulerException, IOException {

        StdSchedulerFactory factory = new StdSchedulerFactory();
//        factory.initialize(new ClassPathResource("quartz.properties").getInputStream());

        logger.debug("Getting a handle to the Scheduler");
        Scheduler scheduler = factory.getScheduler();
        scheduler.setJobFactory(springBeanJobFactory());

        //schedule getZZConfirmInfoToUpdatePayFlow
        scheduler.scheduleJob(jobGetZZConfirmInfoToUpdatePayFlow, triggerGetZZConfirmInfoToUpdatePayFlow);
        scheduler.scheduleJob(jobGetZZConfirmInfoToTrggerPreOrder, triggerGetZZConfirmInfoToTrggerPreOrder);
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
    public JobDetail jobGetZZConfirmInfoToUpdatePayFlow() {
        JobKey jobKey = new JobKey("Qrtz_Job_GetZZConfirmInfoToUpdatePayFlow", "pay");
        JobDetail job = JobBuilder.newJob(CheckFundsCSVInfoJob.class).withIdentity(jobKey)
            .withDescription("Invoke GetZZConfirmInfoToUpdatePayFlow Job service...").build();
        return  job;
    }

    @Bean
    public Trigger  triggerGetZZConfirmInfoToUpdatePayFlow() {


        Trigger trigger = TriggerBuilder
            .newTrigger()
            .withIdentity("Qrtz_Trigger_GetZZConfirmInfoToUpdatePayFlow", "pay")
            .withSchedule(CronScheduleBuilder.cronSchedule(cronExpr))
            .build();
        return trigger;
    }

}
