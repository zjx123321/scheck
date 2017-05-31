package com.simple.scheck.dao;

import com.simple.scheck.dto.entity.User;

import java.util.List;

public interface UserMapper extends BaseDao<User, Integer>{

    List<User> selectList();

}