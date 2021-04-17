package com.mingrisoft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mingrisoft.dao.TbCodeMapper;
import com.mingrisoft.model.TbCode;
import com.mingrisoft.service.TbCodeService;

@Service
public class TbCodeServiceImpl implements TbCodeService {
	
	@Autowired (required = true)
	private TbCodeMapper tbCodeMapper;

	@Override
	public List<TbCode> queryTempList(TbCode record) {
		// TODO Auto-generated method stub
		return tbCodeMapper.queryTempList(record);
	}

}
