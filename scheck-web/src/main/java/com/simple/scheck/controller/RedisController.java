package com.simple.scheck.controller;

import com.simple.scheck.service.RedisService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController("/redis")
public class RedisController {

    @Resource
    private RedisService redisService;

    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public void insert(@RequestParam("key") String key, @RequestParam("value") String value) {
        System.out.println("开始插入。。。。。");

        redisService.insert(key, value);

        System.out.println("结束插入。。。。。");
    }

}
