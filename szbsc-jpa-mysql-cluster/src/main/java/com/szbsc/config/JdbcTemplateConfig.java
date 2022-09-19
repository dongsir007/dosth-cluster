package com.szbsc.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class JdbcTemplateConfig {

	@Bean(name = "jdbcTemplateMaster")
	public NamedParameterJdbcTemplate jdbcTemplateMaster(@Qualifier("masterDataSource") DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}
	
	@Bean(name = "jdbcTemplateSlave")
	public NamedParameterJdbcTemplate jdbcTemplateSlave(@Qualifier("slaveDataSource") DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}
}