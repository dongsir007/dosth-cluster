package com.szbsc.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(DataSourceConfiguration.class);
	
	@Bean(name = "masterDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.master")
	@Primary
	public DataSource masterDataSource() {
		logger.info("create master datasource...");
		return DataSourceBuilder.create().build();
	}
	
	@Bean(name = "slaveDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.slave")
	public DataSource slaveDataSource() {
		logger.info("create slave datasource...");
		return DataSourceBuilder.create().build();
	}
}