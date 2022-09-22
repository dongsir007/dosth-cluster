package com.szbsc.service.impl;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.szbsc.service.UserService;

/**
 * 用户业务层实现类
 * @author Zhidong.Guo
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public int countAll() {
		int count = Math.abs(new Random().nextInt());
		logger.info("用户数量为{}", count);
		return count;
	}

}