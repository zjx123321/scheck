package com.simple.scheck.controller;

import com.github.pagehelper.PageInfo;
import com.simple.scheck.dto.entity.User;
import com.simple.scheck.dto.form.UserForm;
import com.simple.scheck.rest.ResultList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.simple.scheck.service.UserService;

import java.util.List;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
@RestController
@RequestMapping("/user/")
@Api(value = "用户接口")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService service;

    @ApiOperation(value = "新增用户")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String insertUser(@Validated @RequestBody UserForm form) {
        logger.info("新增用户", form);
        User user = new User();
        service.insertSelective(user);
        return "Success";
    }

    @ApiOperation(value = "查询所有用户")
    @RequestMapping(value = "findAll", method = RequestMethod.POST)
    public ResultList<User> findAllUser() {
        logger.info("查询所有用户");
        PageInfo<User> list = service.selectList();
        return ResultList.list(list);
    }

}
