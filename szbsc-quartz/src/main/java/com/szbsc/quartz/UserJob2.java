package com.szbsc.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 用户工作负载2
 * @author Zhidong.Guo
 *
 */
public class UserJob2 extends QuartzJobBean {
	
	private static final Logger logger = LoggerFactory.getLogger(UserJob2.class);

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("--------------定时任务开始执行----------------");
	}

}
