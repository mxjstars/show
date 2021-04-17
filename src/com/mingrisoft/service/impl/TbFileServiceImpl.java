package com.mingrisoft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mingrisoft.dao.TbFileMapper;
import com.mingrisoft.model.TbFile;
import com.mingrisoft.service.TbFileService;

@Service
public class TbFileServiceImpl implements TbFileService{
	@Autowired(required = true)
	private TbFileMapper tbFileMapper;

	@Override
	public int insert(TbFile record) {
		// TODO Auto-generated method stub
		return tbFileMapper.insert(record);
	}

	@Override
	public int deleteByPrimaryKey(String fileid) {
		// TODO Auto-generated method stub
		return tbFileMapper.deleteByPrimaryKey(fileid);
	}

	@Override
	public List<TbFile> queryTempList(TbFile record,int offset, int limit) {
		// TODO Auto-generated method stub
		return tbFileMapper.queryTempList(record, offset, limit);
	}

}
