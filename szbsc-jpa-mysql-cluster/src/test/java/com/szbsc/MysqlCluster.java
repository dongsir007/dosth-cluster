package com.szbsc;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.szbsc.dosth.entity.User;
import com.szbsc.dosth.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = {com.szbsc.JpaMySqlClusterApplication.class}, properties = {"application.yml"})
@RunWith(SpringRunner.class)
public class MysqlCluster {
	
	@Autowired
	public UserRepository accountRepository;
	
	@Test
	public void a() {
		log.info(">>>>" + this.accountRepository);
		List<User> list = this.accountRepository.findAll();
		list.stream().forEach(System.out::println);
	}
}