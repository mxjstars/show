package com.mingrisoft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mingrisoft.dao.TsTempsMapper;
import com.mingrisoft.model.TsTemps;
import com.mingrisoft.service.TsTempsService;

@Service
public class TsTempsServiceImpl implements TsTempsService{
	
	@Autowired(required = true)
	private TsTempsMapper tsTempsMapper;

	@Override
	public List<TsTemps> queryTempList(TsTemps record) {
		// TODO Auto-generated method stub
		return tsTempsMapper.queryTempList(record);
	}

	@Override
	public TsTemps selectByPrimaryKey(Integer tempId) {
		// TODO Auto-generated method stub
		return tsTempsMapper.selectByPrimaryKey(tempId);
	}

	@Override
	public TsTemps selectBytempCode(String record) {
		// TODO Auto-generated method stub
		return tsTempsMapper.selectBytempCode(record);
	}

}
