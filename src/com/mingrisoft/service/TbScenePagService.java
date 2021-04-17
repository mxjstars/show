package com.mingrisoft.service;

import java.util.List;

import com.mingrisoft.model.TbScenePag;

public interface TbScenePagService {
	
	List<TbScenePag> queryTempList(TbScenePag record);
	
	TbScenePag selectByPrimaryKey(Integer scenePagId);
	
	int updateByPrimaryKeySelective(TbScenePag record);
	
	TbScenePag creatNew(Integer record);
	
	int deleteByPrimaryKey(Integer scenePagId);
	
	TbScenePag copyNew(Integer record);

}
