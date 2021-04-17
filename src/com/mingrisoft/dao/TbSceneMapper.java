package com.mingrisoft.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mingrisoft.model.TbScene;

public interface TbSceneMapper {
    int deleteByPrimaryKey(Integer sceneId);

    int insert(TbScene record);

    int insertSelective(TbScene record);

    TbScene selectByPrimaryKey(Integer sceneId);
    
    TbScene selectBySceneCode(String record);

    int updateByPrimaryKeySelective(TbScene record);

    int updateByPrimaryKey(TbScene record);
    
    List<TbScene> queryTempList(TbScene record);
    
    List<TbScene> getTemp(@Param("record")TbScene record,@Param("offset") int offset, @Param("limit") int limit);
}