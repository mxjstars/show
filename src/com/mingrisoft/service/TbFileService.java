package com.mingrisoft.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mingrisoft.model.TbFile;

public interface TbFileService {
	
	List<TbFile> queryTempList(TbFile record,@Param("offset") int offset, @Param("limit") int limit);    
	
	int insert(TbFile record);
	
	int deleteByPrimaryKey(String fileid);

}
