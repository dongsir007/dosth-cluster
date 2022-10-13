package com.szbsc.service;

import com.szbsc.entity.Account;

public interface AccountService {
	public Account findByAccountId(String accountId);
}