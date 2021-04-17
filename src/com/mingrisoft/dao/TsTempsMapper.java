package com.mingrisoft.dao;

import java.util.List;

import com.mingrisoft.model.TsTemps;

public interface TsTempsMapper {
    int deleteByPrimaryKey(Integer tempId);

    int insert(TsTemps record);

    int insertSelective(TsTemps record);

    TsTemps selectByPrimaryKey(Integer tempId);

    int updateByPrimaryKeySelective(TsTemps record);

    int updateByPrimaryKey(TsTemps record);
    
    List<TsTemps> queryTempList(TsTemps record);
    
    TsTemps selectBytempCode(String record);
}