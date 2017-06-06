package com.simple.scheck.service;

import com.simple.scheck.dao.BaseDao;
import com.simple.scheck.exception.AbstractException;
import com.simple.scheck.exception.DBException;
import com.simple.scheck.exception.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.Serializable;

/**
 * Created by dell on 2017/5/31.
 */
public abstract class  BaseService<T extends Serializable,ID extends Serializable> {

    public BaseDao<T,ID> baseDao;

    protected void deleteByPrimaryKey(ID id) throws AbstractException{
        Assert.notNull(id, "id 不能为空");
        dealResult(baseDao.deleteByPrimaryKey(id));
    }

    protected void insert(T record) throws AbstractException{
        Assert.notNull(record, "record 不能为空");
//        initBaseDao();
        dealResult(baseDao.insert(record));
    }

    protected void insertSelective(T record) throws AbstractException{
        Assert.notNull(record, "record 不能为空");
//        initBaseDao();
        dealResult(baseDao.insertSelective(record));
    }

    protected T selectByPrimaryKey(ID id) {
        Assert.notNull(id, "id 不能为空");
        return baseDao.selectByPrimaryKey(id);
    }

    protected void updateByPrimaryKeySelective(T record) throws AbstractException{
        Assert.notNull(record, "record 不能为空");
        dealResult(baseDao.updateByPrimaryKeySelective(record));
    }

    protected void updateByPrimaryKey(T record) throws AbstractException{
        Assert.notNull(record, "record 不能为空");
        dealResult(baseDao.updateByPrimaryKey(record));
    }

    private void dealResult(int result) throws AbstractException{
        if(result == 0) {
            throw new DBException(StatusCode.DB_ERROR, "数据库处理失败");
        }
    }

    @PostConstruct
    protected abstract void initBaseDao();

}
