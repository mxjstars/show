package com.mingrisoft.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mingrisoft.model.TbFile;

public interface TbFileMapper {
    int deleteByPrimaryKey(String fileid);

    int insert(TbFile record);

    int insertSelective(TbFile record);

    TbFile selectByPrimaryKey(String fileid);

    int updateByPrimaryKeySelective(TbFile record);

    int updateByPrimaryKey(TbFile record);
    
    List<TbFile> queryTempList(@Param("record")TbFile record,@Param("offset") int offset, @Param("limit") int limit);  


}