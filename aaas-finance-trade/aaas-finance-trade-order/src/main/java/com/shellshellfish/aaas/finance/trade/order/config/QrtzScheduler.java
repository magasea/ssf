package com.shellshellfish.aaas.finance.trade.order.config;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import com.shellshellfish.aaas.finance.trade.order.scheduler.CheckFundsOrderJob;
import com.shellshellfish.aaas.finance.trade.order.scheduler.PatchOrderJob;
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
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
@ConditionalOnExpression("'${using.spring.schedulerFactory}'=='true'")
public class QrtzScheduler {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${cron.frequency.jobCheckOrderWithPay}")
    String cronCheckOrderWithPay;

    @PostConstruct
    public void init() {
        logger.info("Hello world from Quartz...");
    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        logger.debug("Configuring Job factory");

        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public Scheduler scheduler() throws SchedulerException, IOException {

        StdSchedulerFactory factory = new StdSchedulerFactory();
//        factory.initialize(new ClassPathResource("quartz.properties").getInputStream());

        logger.info("Getting a handle to the Scheduler");
        Scheduler scheduler = factory.getScheduler();
        scheduler.setJobFactory(springBeanJobFactory());
        scheduler.scheduleJob(jobPatchOrder(), triggerPatchOrder());

        logger.info("Starting Scheduler threads");
        scheduler.start();
        return scheduler;
    }



    @Bean
    public JobDetail jobPatchOrder() {
        JobKey jobKey = new JobKey("Qrtz_Job_PatchOrder", "order");
        JobDetail job = JobBuilder.newJob(PatchOrderJob.class).withIdentity(jobKey).storeDurably()
            .withDescription("Invoke PatchOrder Job service...").build();
        return  job;
    }

    @Bean
    public Trigger triggerPatchOrder() {
            Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("Qrtz_Trigger_PatchOrder", "order")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronCheckOrderWithPay))
                .build();
            return trigger;
    }
}
