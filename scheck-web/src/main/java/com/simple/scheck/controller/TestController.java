package com.simple.scheck.controller;

import com.simple.scheck.dto.form.UserForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.simple.scheck.service.TestService;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
@RestController
@RequestMapping("/user/")
@Api(value = "用户接口")
public class TestController {

    private static Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private TestService service;

    @ApiOperation(value = "新增用户")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String insertUser(@Validated @RequestBody UserForm form) {
        logger.info("调用hello");
        return service.sayHello();
    }

}
