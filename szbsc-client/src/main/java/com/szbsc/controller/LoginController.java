package com.szbsc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
	@RequestMapping("/login")
	public String login(@RequestParam String userName, @RequestParam String password) {
		return null;
	}
}