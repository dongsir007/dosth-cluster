package com.szbsc.config;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.szbsc.quartz.UserJob;
import com.szbsc.quartz.UserJob2;

/**
 * 定时任务配置信息
 * @author Zhidong.Guo
 *
 */
@Configuration
public class QuartzScheduleConfig {
	public static class UserJobConfig {
		/**
		 * 工作负载名称
		 */
		private static final String JOB_NAME = "userJob1";
		/**
		 * 触发器名称
		 */
		private static final String TRIGGER_NAME = "userJob1Trigger";
		
		@Bean
		public JobDetail userJob() {
			return JobBuilder.newJob(UserJob.class)
					.withIdentity(JOB_NAME)
					.storeDurably()
					.build();
		}
		
		@Bean
		public Trigger userJobTrigger() {
			SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
					.withIntervalInSeconds(5)
					.repeatForever();
			return TriggerBuilder.newTrigger()
					.forJob(JOB_NAME)
					.withIdentity(TRIGGER_NAME)
					.withSchedule(scheduleBuilder)
					.build();
		}
	}
	
	public static class UserJob2Config {

		/**
		 * 工作负载名称
		 */
		private static final String JOB_NAME = "userJob2";
		/**
		 * cron表达式
		 */
		private static final String CRON_EXP = "0/10 * * * * ? *";
		/**
		 * 触发器名称
		 */
		private static final String TRIGGER_NAME = "userJob2Trigger";
		
		@Bean
		public JobDetail userJob2() {
			return JobBuilder.newJob(UserJob2.class)
					.withIdentity(JOB_NAME)
					.storeDurably()
					.build();
		}
		
		@Bean
		public Trigger userJob2Trigger() {
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(CRON_EXP);
			return TriggerBuilder.newTrigger()
					.forJob(JOB_NAME)
					.withIdentity(TRIGGER_NAME)
					.withSchedule(scheduleBuilder)
					.build();
		}
	}
}