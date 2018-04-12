package com.shellshellfish.aaas.finance.trade.pay.config;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import com.shellshellfish.aaas.finance.trade.pay.scheduler.CheckFundsBuyJob;
import com.shellshellfish.aaas.finance.trade.pay.scheduler.CheckPreOrderStatus2TriggerBuyJob;
import java.io.IOException;
import java.text.ParseException;
import javax.annotation.PostConstruct;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
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
        JobDetail job = JobBuilder.newJob(CheckFundsBuyJob.class).withIdentity(jobKey).storeDurably()
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

    @Bean
    public JobDetail jobGetZZConfirmInfoToTrggerPreOrder() {

        return newJob().ofType(CheckPreOrderStatus2TriggerBuyJob.class).storeDurably().withIdentity(JobKey.jobKey("Qrtz_Job_GetZZConfirmInfoToTrggerPreOrder")).withDescription("Invoke GetZZConfirmInfoToTrggerPreOrder Job service...").build();
    }

    @Bean
    public Trigger triggerGetZZConfirmInfoToTrggerPreOrder() {

        Trigger trigger = TriggerBuilder
            .newTrigger()
            .withIdentity("Qrtz_Trigger_GetZZConfirmInfoToTrggerPreOrder", "pay")
            .withSchedule(CronScheduleBuilder.cronSchedule(cronExprJobpreorderpayflowcheck))
            .build();
        return trigger;
    }
}
