package com.mingrisoft.dao;

import com.mingrisoft.model.TbShowUser;

public interface TbShowUserMapper {
    int deleteByPrimaryKey(String userId);

    int insert(TbShowUser record);

    int insertSelective(TbShowUser record);

    TbShowUser selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(TbShowUser record);

    int updateByPrimaryKey(TbShowUser record);
    
    TbShowUser selectByNamePwd(TbShowUser record);
}