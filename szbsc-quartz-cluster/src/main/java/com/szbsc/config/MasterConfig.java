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
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = "entityManagerFactoryMaster", 
		transactionManagerRef = "transactionManagerMaster", 
		basePackages = { "com.szbsc.dosth.repository" })
public class MasterConfig {
	@Autowired
	@Qualifier("masterDataSource")
	private DataSource masterDataSource;
	
	@Primary
	@Bean(name = "entityManagerMaster")
	public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
		return entityManagerFactoryMaster(builder).getObject().createEntityManager();
	}

	@Primary
	@Bean(name = "entityManagerFactoryMaster")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryMaster(EntityManagerFactoryBuilder builder) {
		return builder
				.dataSource(masterDataSource)
				.properties(this.getVendorProperties())
				.packages("com.szbsc.dosth.entity")
				.persistenceUnit("masterPersistenceUnit")
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

	@Primary
	@Bean(name = "transactionManagerMaster")
	public PlatformTransactionManager transactionManagerMaster(EntityManagerFactoryBuilder builder) {
		return new JpaTransactionManager(entityManagerFactoryMaster(builder).getObject());
	}
}