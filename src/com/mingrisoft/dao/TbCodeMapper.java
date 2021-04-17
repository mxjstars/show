package com.mingrisoft.dao;

import java.util.List;

import com.mingrisoft.model.TbCode;

public interface TbCodeMapper {
    int deleteByPrimaryKey(Integer codeId);

    int insert(TbCode record);

    int insertSelective(TbCode record);

    TbCode selectByPrimaryKey(Integer codeId);

    int updateByPrimaryKeySelective(TbCode record);

    int updateByPrimaryKey(TbCode record);
    
    List<TbCode> queryTempList(TbCode record);
}