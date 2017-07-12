package com.simple.scheck.dao;

import com.simple.scheck.dto.entity.OperationLog;

public interface OperationLogMapper extends BaseDao{
    int deleteByPrimaryKey(Integer id);

    int insert(OperationLog record);

    int insertSelective(OperationLog record);

    OperationLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OperationLog record);

    int updateByPrimaryKey(OperationLog record);
}