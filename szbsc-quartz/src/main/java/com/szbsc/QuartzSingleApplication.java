package com.szbsc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 定时任务单机模式
 * @author Zhidong.Guo
 *
 */
@SpringBootApplication
public class QuartzSingleApplication {
	public static void main(String[] args) {
		SpringApplication.run(QuartzSingleApplication.class, args);
	}
}