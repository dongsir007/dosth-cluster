package com.szbsc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.szbsc.dosth.entity.Account;
import com.szbsc.dosth.repository.AccountRepository;

@RestController
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@RequestMapping("/list")
	public List<Account> list() {
		return this.accountRepository.findAll();
	}
}