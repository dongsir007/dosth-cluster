package com.szbsc.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.szbsc.entity.Account;
import com.szbsc.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	@Override
	public Account findByAccountId(String accountId) {
		if (StringUtils.isEmpty(accountId)) {
			return null;
		}
		Account account = new Account();
		account.setAccountId(accountId);
		account.setUserName("小明");
		account.setPassword("123");
		return account;
	}
}