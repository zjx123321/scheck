package com.simple.scheck.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.simple.scheck.dao.UserMapper;
import com.simple.scheck.dto.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/5/21 0021.
 */
@Service
public class UserService extends BaseService<User, Integer>{

    @Autowired
    private UserMapper userMapper;

    public PageInfo<User> selectList() {
        PageHelper.startPage(1, 1);
        List<User> list = userMapper.selectList();
        PageInfo<User> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

}
