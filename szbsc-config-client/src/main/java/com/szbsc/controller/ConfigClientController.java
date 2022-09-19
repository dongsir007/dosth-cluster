package com.szbsc.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope //开启更新功能
@RequestMapping("client")
public class ConfigClientController {
	@Value("${from}")
    private String fromValue;

    /**
     * 返回配置文件中的值
     */
    @GetMapping("/from")
    public String returnFormValue(){
        return this.fromValue;
    }
}