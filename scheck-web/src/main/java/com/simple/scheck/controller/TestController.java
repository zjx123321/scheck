package com.simple.scheck.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.simple.scheck.service.TestService;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
@Controller
public class TestController {

    @Autowired
    private TestService service;

    @RequestMapping("/")
    @ResponseBody
    String hello() {
        return service.sayHello();
    }

}
