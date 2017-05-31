package com.simple.scheck.service;

import com.simple.scheck.dao.BaseDao;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * Created by dell on 2017/5/31.
 */
public class  BaseService<T extends Serializable,ID extends Serializable>{

    private BaseDao<T,ID> baseDao;

    public int deleteByPrimaryKey(ID id) {
        Assert.isNull(id, "id 不能为空");
        return baseDao.deleteByPrimaryKey(id);
    }

    public int insert(T record) {
        Assert.isNull(record, "record 不能为空");
        return baseDao.insert(record);
    }

    public int insertSelective(T record) {
        Assert.isNull(record, "record 不能为空");
        return baseDao.insertSelective(record);
    }

    public T selectByPrimaryKey(ID id) {
        Assert.isNull(id, "id 不能为空");
        return baseDao.selectByPrimaryKey(id);
    }

    public int updateByPrimaryKeySelective(T record) {
        Assert.isNull(record, "record 不能为空");
        return baseDao.updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(T record) {
        Assert.isNull(record, "record 不能为空");
        return baseDao.updateByPrimaryKey(record);
    }

}
