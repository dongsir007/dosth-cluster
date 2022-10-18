package com.szbsc.controller;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.szbsc.dosth.entity.User;
import com.szbsc.dosth.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRepository accountRepository;
	
	@RequestMapping("/list")
	public List<User> list() {
		return this.accountRepository.findAll();
	}
	
	@RequestMapping("/insert")
	public void insert() {
		User account;
		for (int i = 0; i < 30; i++) {
			log.info("新增第"+(i+1)+"个账户");
			account = new User();
			account.setId(UUID.randomUUID().toString());
			account.setName("A" + new Random().nextInt());
			this.accountRepository.save(account);
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}