package com.szbsc.quartz;

import java.util.concurrent.atomic.AtomicInteger;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.szbsc.service.UserService;

/**
 * 用户模块工作负载
 * @author Zhidong.Guo
 *
 */
@DisallowConcurrentExecution
public class UserJob extends QuartzJobBean {

	private static final Logger logger = LoggerFactory.getLogger(UserJob.class);

	@Autowired
	private UserService userService;
	
	private static final AtomicInteger counts = new AtomicInteger();
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("【定时任务】第【{}】次执行,用户数量:{}", counts.incrementAndGet(), this.userService.countAll());
	}

}
