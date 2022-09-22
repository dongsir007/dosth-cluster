package com.szbsc.config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = "entityManagerFactorySlave", 
		transactionManagerRef = "transactionManagerSlave", 
		basePackages = { "com.szbsc.dosth.repository" })
public class SlaveConfig {
	@Autowired
	@Qualifier("slaveDataSource")
	private DataSource slaveDataSource;
	
	@Bean(name = "entityManagerSlave")
	public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
		return entityManagerFactorySlave(builder).getObject().createEntityManager();
	}
	
	@Bean(name = "entityManagerFactorySlave")
	public LocalContainerEntityManagerFactoryBean entityManagerFactorySlave(EntityManagerFactoryBuilder builder) {
		return builder
				.dataSource(slaveDataSource)
				.properties(this.getVendorProperties())
				.packages("com.szbsc.dosth.entity")
				.persistenceUnit("slavePersistenceUnit")
				.build();
	}

	private Map<String, String> getVendorProperties() {
		Map<String, String> properties = new HashMap<>();
		properties.put("hibernate.hbm2dll.auto", "none");
		properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
		properties.put("hibernate.physical_naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
	    properties.put("hibernate.implicit_naming_strategy", "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy");
		return properties;
	}
	
	@Bean(name = "transactionManagerSlave")
	public PlatformTransactionManager transactionManagerSlave(EntityManagerFactoryBuilder builder) {
		return new JpaTransactionManager(entityManagerFactorySlave(builder).getObject());
	}
}