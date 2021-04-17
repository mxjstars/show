package com.mingrisoft.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mingrisoft.model.TbCode;
import com.mingrisoft.model.TbScene;
import com.mingrisoft.model.TbShowUser;
import com.mingrisoft.model.TsTemps;
import com.mingrisoft.modelNew.TbCodeNew;
import com.mingrisoft.modelNew.TbSceneNew;
import com.mingrisoft.modelNew.TsTempsNew;
import com.mingrisoft.service.TbCodeService;
import com.mingrisoft.service.TbSceneService;
import com.mingrisoft.service.TbShowUserService;
import com.mingrisoft.service.TsTempsService;

@Controller
public class HttpWebController extends BaseController {

	@Autowired(required = true)
	private TbCodeService tbCodeService;

	@Autowired(required = true)
	private TsTempsService tsTempsService;

	@Autowired(required = true)
	private TbSceneService tbSceneService;
	
	@Autowired(required = true)
	private TbShowUserService tbShowUserService;

	/**
	 * 
	 * 获取行业列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/web/getHY_list")
	@ResponseBody
	public TbCodeNew getHYList(HttpServletRequest request) {

		TbCodeNew tcn = new TbCodeNew();

		TbCode tbCode = new TbCode();

		tbCode.setGroupId(2);

		List<TbCode> tbCodes = tbCodeService.queryTempList(tbCode);

		if (tbCodes == null) {
			tcn.setStatus("-1");
			return tcn;
		} else {
			tcn.setStatus("1");
			tcn.setTbCode(tbCodes);
		}

		return tcn;
	}

	/**
	 * 
	 * 获取场景列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/web/getCJ_list")
	@ResponseBody
	public TbCodeNew getCJList(HttpServletRequest request) {

		TbCodeNew tcn = new TbCodeNew();

		TbCode tbCode = new TbCode();

		tbCode.setGroupId(3);

		List<TbCode> tbCodes = tbCodeService.queryTempList(tbCode);

		if (tbCodes == null) {
			tcn.setStatus("-1");
			return tcn;
		} else {
			tcn.setStatus("1");
			tcn.setTbCode(tbCodes);
		}

		return tcn;
	}

	/**
	 * 
	 * 获取场景列表---Gid
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/web/getCJ_listNew")
	@ResponseBody
	public TbCodeNew getCJListNew(HttpServletRequest request) {

		TbCodeNew tcn = new TbCodeNew();

		String Gid = request.getParameter("Gid");

		TbCode tbCode = new TbCode();

		tbCode.setGroupId(Integer.valueOf(Gid));
		tbCode.setState(0);

		List<TbCode> tbCodes = tbCodeService.queryTempList(tbCode);

		if (tbCodes == null) {
			tcn.setStatus("-1");
			return tcn;
		} else {
			tcn.setStatus("1");
			tcn.setTbCode(tbCodes);
		}

		return tcn;
	}

	/**
	 * 
	 * 获取模板列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/web/getM_list")
	@ResponseBody
	public TsTempsNew getMList(HttpServletRequest request) {

		TsTempsNew ttn = new TsTempsNew();

		TsTemps tsTemps = new TsTemps();

		List<TsTemps> tsTempslist = tsTempsService.queryTempList(tsTemps);

		if (tsTempslist == null) {
			ttn.setStatus("-1");
			return ttn;
		} else {
			ttn.setStatus("1");
			ttn.setTsTemps(tsTempslist);
		}

		return ttn;
	}

	/**
	 * 
	 * 获取模板
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/web/get_tsTemps")
	@ResponseBody
	public TsTemps getTsTemps(HttpServletRequest request) {
		TsTemps tsTemps = new TsTemps();

		String id = request.getParameter("temp_code");

		tsTemps = tsTempsService.selectBytempCode(id);

		return tsTemps;

	}

	/**
	 * 
	 * 创建场景模板
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/web/createTemp")
	@ResponseBody
	public TbCodeNew createTemp(HttpServletRequest request) {

		TbCodeNew tcn = new TbCodeNew();
		HttpSession session = request.getSession(true);

		if (session.getAttribute("UserId") == null) {
			tcn.setStatus("-1");
			return tcn;
		}

		String hyId = request.getParameter("hyid");

		String sceneId = UUID.randomUUID().toString();

		String userId = session.getAttribute("UserId").toString();

		try {
			tbSceneService.createTemp(hyId, sceneId, userId);
			tcn.setStatus(sceneId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			tcn.setStatus("-1");
			return tcn;
		}

		return tcn;
	}

	/**
	 * 
	 * 获取个人场景列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/web/getTemp")
	@ResponseBody
	public TbSceneNew getTemp(HttpServletRequest request) {

		TbSceneNew tsn = new TbSceneNew();
		HttpSession session = request.getSession(true);

		if (session.getAttribute("UserId") == null) {
			tsn.setStatus("-1");
			return tsn;
		}

		String userId = session.getAttribute("UserId").toString();

		String selectStr = request.getParameter("selectStr");//模板名称

		String hnagye = request.getParameter("hnagye");//场景类型

		String PageInt = request.getParameter("PageInt");//起始数

		String CountRow = request.getParameter("CountRow");//每页数量
		
		TbScene bScene = new TbScene();
		
		bScene.setAuthor(userId);
		bScene.setSceneName(selectStr);
		bScene.setSceneCustomId(Integer.valueOf(hnagye));
		
		double a = Double.valueOf(PageInt).doubleValue();
		double b = Double.valueOf(CountRow).doubleValue();
		
		double c = 0;
		
		double d = 0;
		
		List<TbScene> tbScenes =null;
		
		if(a>1){
			
			c = a*b;
			
			d = c + 10;
			
			tbScenes = tbSceneService.getTemp(bScene, (int)c, (int)d);
		}else{
			
			
			tbScenes = tbSceneService.getTemp(bScene, 0, Integer.valueOf(CountRow));
		}
		
		if(tbScenes.size()>0){
			tsn.setStatus("0");
			tsn.setTbScene(tbScenes);
			
		}else{
			tsn.setStatus("-1");
			return tsn;
		}
		
		return tsn;
	}
	
	/**
	 * 
	 * 获取个人场景列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/web/delTemp")
	@ResponseBody
	public TbSceneNew delTemp(HttpServletRequest request) {
		TbSceneNew tsn = new TbSceneNew();
		HttpSession session = request.getSession(true);
		
		if (session.getAttribute("UserId") == null) {
			tsn.setStatus("-1");
			return tsn;
		}
		
		String id = request.getParameter("id");
		
		tbSceneService.deleteByPrimaryKey(id);
		
		return tsn;
	}
	
	/**
	 * 
	 * 复制场景
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/web/copeSence")
	@ResponseBody
	public TbSceneNew copeSence(HttpServletRequest request) {
		
		TbSceneNew tsn = new TbSceneNew();
		
		String id = request.getParameter("id");
		
		try {
			
			tbSceneService.copeSence(id);
			
			tsn.setStatus("0");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			tsn.setStatus("-1");
		}
		
		return tsn;
	}
	
	/**
	 * 
	 * 赠送场景
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/web/giveSence")
	@ResponseBody
	public TbSceneNew giveSence(HttpServletRequest request) {
		
		TbSceneNew tsn = new TbSceneNew();
		
		String id = request.getParameter("id");
		
		String uName = request.getParameter("uName");
		
		TbShowUser tsu = new TbShowUser();
		tsu.setUserName(uName);
		TbShowUser tbShowUser = tbShowUserService.selectByNamePwd(tsu);
		
		if(tbShowUser==null){
			
			tsn.setStatus("-1");
			
			return tsn;
		}
		
		try {
			
			tbSceneService.giveSence(id,tbShowUser.getUserId());
			
			tsn.setStatus("0");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			tsn.setStatus("-1");
		}
		
		return tsn;
	}
	
	
}
