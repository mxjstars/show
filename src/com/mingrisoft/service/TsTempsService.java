package com.mingrisoft.service;

import java.util.List;

import com.mingrisoft.model.TsTemps;

public interface TsTempsService {
	
	List<TsTemps> queryTempList(TsTemps record);
	
	TsTemps selectByPrimaryKey(Integer tempId);
	
	TsTemps selectBytempCode(String record);

}
