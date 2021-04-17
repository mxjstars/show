package com.mingrisoft.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mingrisoft.dao.TbScenePagMapper;
import com.mingrisoft.model.TbScenePag;
import com.mingrisoft.service.TbScenePagService;

@Service
public class TbScenePagServiceImpl implements TbScenePagService{
	
	@Autowired(required = true)
	private TbScenePagMapper tbScenePagMapper;

	@Override
	public List<TbScenePag> queryTempList(TbScenePag record) {
		// TODO Auto-generated method stub
		return tbScenePagMapper.queryTempList(record);
	}

	@Override
	public TbScenePag selectByPrimaryKey(Integer scenePagId) {
		// TODO Auto-generated method stub
		return tbScenePagMapper.selectByPrimaryKey(scenePagId);
	}

	@Override
	public int updateByPrimaryKeySelective(TbScenePag record) {
		// TODO Auto-generated method stub
		return tbScenePagMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public TbScenePag creatNew(Integer record) {
		// TODO Auto-generated method stub
		TbScenePag tbScenePag = new TbScenePag();
		
		TbScenePag tbScenePagA = tbScenePagMapper.selectByPrimaryKey(record);
		
		int nums = tbScenePagA.getNum();
		
		TbScenePag tbScenePagB = new TbScenePag();
		
		tbScenePagB.setSceneCode(tbScenePagA.getSceneCode());
		
		List<TbScenePag> tbScenePaglist = tbScenePagMapper.queryTempList(tbScenePagB);
		
		for(TbScenePag tsp : tbScenePaglist){
			if(tsp.getNum()>nums){
				tsp.setNum(tsp.getNum()+2);
				if(tsp.getPagename()==null){
					tsp.setPagename("第"+(tsp.getNum()+2)+"页");
				}
				tbScenePagMapper.updateByPrimaryKeySelective(tsp);
			}
		}
		
		tbScenePagB.setNum(nums+1);
		if(tbScenePagB.getPagename()==null){
			tbScenePagB.setPagename("第"+(nums+1)+"页");
		}
		
		tbScenePagMapper.insert(tbScenePagB);
		
		List<TbScenePag> tbScenePaglistA = tbScenePagMapper.queryTempList(tbScenePagB);
		
		for(TbScenePag tsp : tbScenePaglistA){
			if(tsp.getNum()==(nums+1)){
				tbScenePag = tsp;
				break;
			}
		}
		
		return tbScenePag;
	}

	@Override
	public int deleteByPrimaryKey(Integer scenePagId) {
		// TODO Auto-generated method stub
		TbScenePag tbScenePag = tbScenePagMapper.selectByPrimaryKey(scenePagId);
		
		int nums = tbScenePag.getNum();
		
		String code = tbScenePag.getSceneCode();
		
		tbScenePagMapper.deleteByPrimaryKey(scenePagId);
		
		TbScenePag tbScenePagA = new TbScenePag();
		
		tbScenePagA.setSceneCode(code);
		
		List<TbScenePag> tbScenePaglist = tbScenePagMapper.queryTempList(tbScenePagA);
		
		for(TbScenePag tsp : tbScenePaglist){
			if(tsp.getNum()>nums){
				tsp.setNum(tsp.getNum()-1);
				tbScenePagMapper.updateByPrimaryKeySelective(tsp);
			}
		}
		
		return 0;
	}

	@Override
	public TbScenePag copyNew(Integer record) {
		// TODO Auto-generated method stub
		TbScenePag r = new TbScenePag();
		
		TbScenePag tbScenePagA = tbScenePagMapper.selectByPrimaryKey(record);
		
		int nums = tbScenePagA.getNum();
		
		String code = tbScenePagA.getSceneCode();
		
		TbScenePag tbScenePagB = new TbScenePag();
		
		tbScenePagB.setSceneCode(code);
		
		List<TbScenePag> tbScenePaglist = tbScenePagMapper.queryTempList(tbScenePagB);
		for(TbScenePag tsp : tbScenePaglist){
			if(tsp.getNum()>nums){
				tsp.setNum(tsp.getNum()+2);
				tbScenePagMapper.updateByPrimaryKeySelective(tsp);
			}
		}
		
		tbScenePagB.setNum(nums+1);
		tbScenePagB.setContentText(tbScenePagA.getContentText());
		
		tbScenePagMapper.insert(tbScenePagB);
		
		List<TbScenePag> tbScenePaglistA = tbScenePagMapper.queryTempList(tbScenePagB);
		for(TbScenePag tsp : tbScenePaglistA){
			if(tsp.getNum()==(nums+1)){
				r = tsp;
				break;
			}
		}
		return r;
	}

}
