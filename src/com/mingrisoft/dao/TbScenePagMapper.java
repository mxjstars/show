package com.mingrisoft.dao;

import java.util.List;

import com.mingrisoft.model.TbScenePag;

public interface TbScenePagMapper {
    int deleteByPrimaryKey(Integer scenePagId);

    int insert(TbScenePag record);

    int insertSelective(TbScenePag record);

    TbScenePag selectByPrimaryKey(Integer scenePagId);
    
    List<TbScenePag> selectBySceneCode(String scenePagId);

    int updateByPrimaryKeySelective(TbScenePag record);

    int updateByPrimaryKeyWithBLOBs(TbScenePag record);

    int updateByPrimaryKey(TbScenePag record);
    
    List<TbScenePag> queryTempList(TbScenePag record);
}