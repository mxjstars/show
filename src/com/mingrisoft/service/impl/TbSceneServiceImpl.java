package com.mingrisoft.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mingrisoft.dao.TbSceneMapper;
import com.mingrisoft.dao.TbScenePagMapper;
import com.mingrisoft.model.TbScene;
import com.mingrisoft.model.TbScenePag;
import com.mingrisoft.service.TbSceneService;

@Service
public class TbSceneServiceImpl implements TbSceneService {

	@Autowired(required = true)
	private TbSceneMapper tbSceneMapper;

	@Autowired(required = true)
	private TbScenePagMapper tbScenePagMapper;

	@Override
	public int deleteByPrimaryKey(String record) {
		// TODO Auto-generated method stub

		TbScene tbScene = tbSceneMapper.selectBySceneCode(record);

		List<TbScenePag> tbScenePag = tbScenePagMapper.selectBySceneCode(record);

		tbSceneMapper.deleteByPrimaryKey(tbScene.getSceneId());

		for (TbScenePag t : tbScenePag) {
			tbScenePagMapper.deleteByPrimaryKey(t.getScenePagId());
		}

		return 0;
	}

	@Override
	public int insert(TbScene record) {
		// TODO Auto-generated method stub
		return tbSceneMapper.insert(record);
	}

	@Override
	public int insertSelective(TbScene record) {
		// TODO Auto-generated method stub
		return tbSceneMapper.insert(record);
	}

	@Override
	public TbScene selectByPrimaryKey(Integer sceneId) {
		// TODO Auto-generated method stub
		return tbSceneMapper.selectByPrimaryKey(sceneId);
	}

	@Override
	public int updateByPrimaryKeySelective(TbScene record) {
		// TODO Auto-generated method stub
		return tbSceneMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(TbScene record) {
		// TODO Auto-generated method stub
		return tbSceneMapper.updateByPrimaryKey(record);
	}

	@Override
	public TbScene selectBySceneCode(String record) {
		// TODO Auto-generated method stub
		return tbSceneMapper.selectBySceneCode(record);
	}

	@Override
	public List<TbScene> queryTempList(TbScene record) {
		// TODO Auto-generated method stub
		return tbSceneMapper.queryTempList(record);
	}

	@Override
	public int createTemp(String hyId, String sceneId, String userId) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		TbScene tbScene = new TbScene();
		tbScene.setSceneCode(sceneId);
		tbScene.setSceneName("默认名称");
		tbScene.setAddtime(new Date());
		tbScene.setStateCodeId(0);
		tbScene.setVisitNum(0);
		tbScene.setUseNum(0);
		tbScene.setDicCodeId(12);
		tbScene.setCover("show/web/default_thum.jpg");
		tbScene.setSceneCustomId(Integer.valueOf(hyId));
		tbScene.setJsFileId(0);
		tbScene.setCssFileId(0);
		tbScene.setSh(1);
		tbScene.setMovietype(0);
		tbScene.setMusicurl("");
		tbScene.setVideourl("");
		tbScene.setTj(1);
		tbScene.setAuthor(userId);
		tbScene.setQrcode("");
		tbScene.setUsernum(0);
		tbScene.setMouseclick(0);

		TbScenePag tbScenePag = new TbScenePag();
		tbScenePag.setContentText("");
		tbScenePag.setSceneCode(sceneId);
		tbScenePag.setNum(1);
		tbScenePag.setPagename("第1页");

		tbSceneMapper.insert(tbScene);

		tbScenePagMapper.insert(tbScenePag);

		return 0;
	}

	@Override
	public List<TbScene> getTemp(TbScene record, int offset, int limit) {
		// TODO Auto-generated method stub
		return tbSceneMapper.getTemp(record, offset, limit);
	}

	@Override
	public int copeSence(String record) {
		String code = UUID.randomUUID().toString();
		// TODO Auto-generated method stub
		TbScene tbScene = tbSceneMapper.selectBySceneCode(record);
		if (tbScene != null) {
			TbScene ts = new TbScene();
			ts = tbScene;
			ts.setSceneId(null);
			ts.setSceneCode(code);
			tbSceneMapper.insert(ts);
			
			List<TbScenePag> tbScenePags = tbScenePagMapper.selectBySceneCode(record);
			for(TbScenePag tsp : tbScenePags){
				TbScenePag t = new TbScenePag();
				t = tsp;
				t.setScenePagId(null);
				t.setSceneCode(code);
				tbScenePagMapper.insert(t);
			}
		}
		return 0;
	}

	@Override
	public int giveSence(String record, String name) {
		String code = UUID.randomUUID().toString();
		// TODO Auto-generated method stub
		TbScene tbScene = tbSceneMapper.selectBySceneCode(record);
		if (tbScene != null) {
			TbScene ts = new TbScene();
			ts = tbScene;
			ts.setSceneId(null);
			ts.setAuthor(name);
			ts.setSceneCode(code);
			tbSceneMapper.insert(ts);
			
			List<TbScenePag> tbScenePags = tbScenePagMapper.selectBySceneCode(record);
			for(TbScenePag tsp : tbScenePags){
				TbScenePag t = new TbScenePag();
				t = tsp;
				t.setScenePagId(null);
				t.setSceneCode(code);
				tbScenePagMapper.insert(t);
			}
		}
		return 0;
	}

}
