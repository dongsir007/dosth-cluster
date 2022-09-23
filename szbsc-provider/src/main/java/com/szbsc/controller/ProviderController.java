package com.szbsc.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/provider")
public class ProviderController {
	
	@Value("${server.port}")
	private Integer port;
	
	@RequestMapping("/hello")
	public String test(@RequestParam String name) {
		System.err.println("Say hello to " + name + " from port [" + this.port + "]");
		return "Say hello to " + name + " from port [" + this.port + "]";
	}
}