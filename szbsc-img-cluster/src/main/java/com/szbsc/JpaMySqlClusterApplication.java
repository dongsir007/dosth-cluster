package com.szbsc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@ComponentScan
public class JpaMySqlClusterApplication {
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(new Class<?>[] { JpaMySqlClusterApplication.class });
		application.setAllowBeanDefinitionOverriding(true);
		application.run(args);
	}
}