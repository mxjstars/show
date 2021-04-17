package com.mingrisoft.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mingrisoft.dao.TbShowUserMapper;
import com.mingrisoft.model.TbShowUser;
import com.mingrisoft.service.TbShowUserService;

@Service
public class TbShowUserServiceImpl implements TbShowUserService{
	
	@Autowired(required = true)
	private TbShowUserMapper tbShowUserMapper;

	@Override
	public int deleteByPrimaryKey(String userId) {
		// TODO Auto-generated method stub
		return tbShowUserMapper.deleteByPrimaryKey(userId);
	}

	@Override
	public int insert(TbShowUser record) {
		// TODO Auto-generated method stub
		return tbShowUserMapper.insert(record);
	}

	@Override
	public int insertSelective(TbShowUser record) {
		// TODO Auto-generated method stub
		return tbShowUserMapper.insertSelective(record);
	}

	@Override
	public TbShowUser selectByPrimaryKey(String userId) {
		// TODO Auto-generated method stub
		return tbShowUserMapper.selectByPrimaryKey(userId);
	}

	@Override
	public int updateByPrimaryKeySelective(TbShowUser record) {
		// TODO Auto-generated method stub
		return tbShowUserMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TbShowUser record) {
		// TODO Auto-generated method stub
		return tbShowUserMapper.updateByPrimaryKey(record);
	}

	@Override
	public TbShowUser selectByNamePwd(TbShowUser record) {
		// TODO Auto-generated method stub
		return tbShowUserMapper.selectByNamePwd(record);
	}

	@Override
	public int sendEmail() {
		// TODO Auto-generated method stub
		return 0;
	}

}
