package com.szbsc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.collections.Sets;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.szbsc.quartz.UserJob;
import com.szbsc.quartz.UserJob2;

@SpringBootTest
@RunWith(SpringRunner.class)
public class QuartzClusterTest {
	
	private static final Logger logger = LoggerFactory.getLogger(QuartzClusterTest.class);
	
	private static final String USER_JOB_1_NAME = "userJob1";
	private static final String USER_JOB_1_TRIGGER_NAME = "userJob1Trigger";

	private static final String USER_JOB_2_NAME = "userJob2";
	private static final String USER_JOB_2_TRIGGER_NAME = "userJob2Trigger";
	
	private static final String USER_JOB_2_CRON = "0/5 * * * * ? *";
	
	@Autowired
	private Scheduler scheduler;
	
	@Test
	public void addUserJob1Config() throws SchedulerException {
		// 创建JobDetail
		JobDetail jobDetail = JobBuilder.newJob(UserJob.class)
				.withIdentity(USER_JOB_1_NAME)
				.storeDurably()
				.build();
		// 创建Trigger
		SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
				.withIntervalInSeconds(5)
				.repeatForever();
		Trigger trigger = TriggerBuilder.newTrigger()
				.forJob(jobDetail)
				.withIdentity(USER_JOB_1_TRIGGER_NAME)
				.withSchedule(scheduleBuilder)
				.build();
		// 不覆盖已有任务
//		scheduler.scheduleJob(jobDetail, trigger);
		
		// 覆盖已有任务
		scheduler.scheduleJob(jobDetail, Sets.newSet(trigger), true);
	}
	
	@Test
	public void addUserJob2Config() throws SchedulerException {
		// 创建JobDetail
		JobDetail jobDetail = JobBuilder.newJob(UserJob2.class)
				.withIdentity(USER_JOB_2_NAME)
				.storeDurably()
				.build();
		// 创建Trigger
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(USER_JOB_2_CRON);
		TriggerKey triggerKey = TriggerKey.triggerKey(USER_JOB_2_TRIGGER_NAME);
		
		Trigger trigger = null;
		if (scheduler.checkExists(triggerKey)) {
			logger.info("删除已存在的trigger以及job");
			trigger = scheduler.getTrigger(triggerKey);
			scheduler.pauseTrigger(trigger.getKey());
			scheduler.unscheduleJob(trigger.getKey());
			scheduler.deleteJob(JobKey.jobKey(USER_JOB_2_NAME));
		}
		trigger = TriggerBuilder.newTrigger()
				.forJob(jobDetail)
				.withIdentity(USER_JOB_2_TRIGGER_NAME)
				.withSchedule(scheduleBuilder)
				.build();
		
		// 不覆盖已有任务
//		scheduler.scheduleJob(jobDetail, trigger);
		
		// 覆盖已有任务
		scheduler.scheduleJob(jobDetail, Sets.newSet(trigger), true);
	}
}