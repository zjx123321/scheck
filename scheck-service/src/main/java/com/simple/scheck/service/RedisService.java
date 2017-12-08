package com.simple.scheck.service;

import com.simple.scheck.util.RedisUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Resource
    private RedisUtils redisUtils;

    public void insert(String key, String value){
        redisUtils.setex(key, value, 1, TimeUnit.MINUTES);
    }

}
