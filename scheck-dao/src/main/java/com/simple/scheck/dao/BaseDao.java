package com.simple.scheck.dao;

import java.io.Serializable;

/**
 * Created by dell on 2017/5/27.
 */
public interface BaseDao <T extends Serializable, ID extends Serializable>{

    int deleteByPrimaryKey(ID id);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(ID id);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);

}
