package com.mingrisoft.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mingrisoft.model.TbScene;

public interface TbSceneService {

    int deleteByPrimaryKey(String record);

    int insert(TbScene record);

    int insertSelective(TbScene record);

    TbScene selectByPrimaryKey(Integer sceneId);

    int updateByPrimaryKeySelective(TbScene record);

    int updateByPrimaryKey(TbScene record);
    
    TbScene selectBySceneCode(String record);

    List<TbScene> queryTempList(TbScene record);
    
    int createTemp(String hyId,String sceneId,String userId);
    
    List<TbScene> getTemp(TbScene record,@Param("offset") int offset, @Param("limit") int limit);
    
    int copeSence(String record);
    
    int giveSence(String record,String name);
}
