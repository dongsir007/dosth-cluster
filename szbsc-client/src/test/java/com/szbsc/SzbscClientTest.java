package com.szbsc;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.feign.dto.FeignUser;
import com.szbsc.rpc.UserRpcService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SzbscClientTest {
	
	@Autowired
	private UserRpcService userRpcService;
	
	@Test
	public void a() {
		System.err.println(this.userRpcService);
		List<FeignUser> list = this.userRpcService.list();
		System.err.println(list.size());
	}
}