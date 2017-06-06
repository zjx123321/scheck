package com.simple.scheck.service;

import com.simple.scheck.dao.BaseDao;
import com.simple.scheck.dao.OperationLogMapper;
import com.simple.scheck.dto.entity.OperationLog;

import com.simple.scheck.exception.AbstractException;
import com.simple.scheck.exception.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dell on 2017/6/1.
 */
@Service
public class OprLogService extends BaseService<OperationLog, Integer>{

    private Logger logger = LoggerFactory.getLogger(OprLogService.class);

    @Autowired
    private OperationLogMapper operationLogMapper;

    public void insert(OperationLog log) {
        try {
            insertSelective(log);
        }catch (AbstractException e){
            logger.error("{}:数据库操作日志保存失败", e.getCode());
        }catch (Exception e){
            logger.error("数据库操作日志保存失败", e);
        }
    }

    protected void initBaseDao() {
        baseDao = operationLogMapper;
    }

}
