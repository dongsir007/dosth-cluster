package com.szbsc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class SzbscAuthApplication {
	public static void main(String[] args) {
		SpringApplication.run(SzbscAuthApplication.class, args);
	}
}
