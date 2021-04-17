package com.mingrisoft.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.mingrisoft.model.TbCode;
import com.mingrisoft.model.TbFile;
import com.mingrisoft.model.TbScene;
import com.mingrisoft.model.TbScenePag;
import com.mingrisoft.model.TbShowUser;
import com.mingrisoft.modelNew.ComModel_1;
import com.mingrisoft.modelNew.ComModel_2;
import com.mingrisoft.modelNew.ComModel_3;
import com.mingrisoft.modelNew.ComModel_4;
import com.mingrisoft.modelNew.ComModel_5;
import com.mingrisoft.modelNew.ComModel_6;
import com.mingrisoft.modelNew.ComModel_7;
import com.mingrisoft.modelNew.ScenePageListNew;
import com.mingrisoft.service.TbCodeService;
import com.mingrisoft.service.TbFileService;
import com.mingrisoft.service.TbScenePagService;
import com.mingrisoft.service.TbSceneService;
import com.mingrisoft.service.TbShowUserService;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

@Controller
public class WebInterfaceController extends BaseController {

	@Autowired(required = true)
	private TbScenePagService tbScenePagService;

	@Autowired(required = true)
	private TbSceneService tbSceneService;

	@Autowired(required = true)
	private TbFileService tbFileService;

	@Autowired(required = true)
	private TbCodeService tbCodeService;

	@Autowired(required = true)
	private TbShowUserService tbShowUserService;

	/**
	 * 
	 * scene.pageList
	 * 
	 */
	@RequestMapping(value = "/web/scene_pageList")
	@ResponseBody
	public ComModel_1 scenePageList(HttpServletRequest request) {

		ComModel_1 C1 = new ComModel_1();

		String sceneCode = request.getParameter("sceneCode");

		TbScenePag tbScenePag = new TbScenePag();

		tbScenePag.setSceneCode(sceneCode);

		List<TbScenePag> tbScenePags = tbScenePagService.queryTempList(tbScenePag);

		if (tbScenePags != null && tbScenePags.size() > 0) {

			TbScene tbScene = tbSceneService.selectBySceneCode(sceneCode);

			C1.setSuccess("true");
			C1.setCode(200);
			C1.setMsg("success");
			List<Map<String, Object>> sublist = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < tbScenePags.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", tbScenePags.get(i).getScenePagId());
				map.put("sceneId", tbScene.getSceneId());
				map.put("num", tbScenePags.get(i).getNum());
				map.put("name", tbScenePags.get(i).getPagename());
				map.put("properties", null);
				map.put("elements", null);
				map.put("scene", null);

				sublist.add(map);
			}
			C1.setList(sublist);
		} else {
			C1.setSuccess("false");
			C1.setCode(403);
			C1.setMsg("基础数据服务器获取失败");
		}

		return C1;
	}

	/**
	 * 
	 * scene.design
	 * 
	 */
	@RequestMapping(value = "/web/scene_design")
	@ResponseBody
	public ComModel_2 sceneDesign(HttpServletRequest request) {

		ComModel_2 C2 = new ComModel_2();

		String pageID = request.getParameter("pageID");

		TbScenePag tbScenePag = tbScenePagService.selectByPrimaryKey(Integer.valueOf(pageID));

		if (tbScenePag != null) {

			TbScene tbScene = tbSceneService.selectBySceneCode(tbScenePag.getSceneCode());

			if (tbScene == null) {

				C2.setSuccess("false");
				C2.setCode(403);
				C2.setMsg("基础数据服务器获取失败");

				return C2;
			}

			C2.setSuccess("true");
			C2.setCode(200);
			C2.setMsg("success");


			Map<String, Object> MAPscene = new HashMap<String, Object>();
			MAPscene.put("id", tbScene.getSceneId());
			MAPscene.put("name", tbScene.getSceneName());
			MAPscene.put("createUser", tbScene.getAuthor());
			MAPscene.put("createTime", Long.valueOf("1425998747000"));
			MAPscene.put("type", tbScene.getSceneCustomId());
			MAPscene.put("pageMode", tbScene.getMovietype());
			MAPscene.put("isTpl", Integer.valueOf("0"));
			MAPscene.put("isPromotion", Integer.valueOf("0"));
			MAPscene.put("status", Integer.valueOf("1"));
			MAPscene.put("openLimit", Integer.valueOf("0"));
			MAPscene.put("submitLimit", Integer.valueOf("0"));
			MAPscene.put("startDate", null);
			MAPscene.put("endDate", null);
			MAPscene.put("accessCode", null);
			MAPscene.put("thirdCode", null);
			MAPscene.put("updateTime", "1426038857000");
			MAPscene.put("publishTime", "1426038857000");
			MAPscene.put("applyTemplate", "0");
			MAPscene.put("applyPromotion", "0");
			MAPscene.put("sourceId", null);
			MAPscene.put("code", tbScene.getSceneCode());
			MAPscene.put("description", tbScene.getDes());
			MAPscene.put("sort", "0");
			MAPscene.put("pageCount", "0");
			MAPscene.put("dataCount", "0");
			MAPscene.put("showCount", tbScene.getMouseclick());
			MAPscene.put("userLoginName", null);
			MAPscene.put("userName", null);

			List<Map<String, Object>> Listelements = new ArrayList<Map<String, Object>>();

			if (tbScenePag.getContentText() != null && tbScenePag.getContentText().length() > 0) {

				JSONArray json = JSONArray.fromObject(tbScenePag.getContentText());

				if (json.size() > 0) {
					for (int i = 0; i < json.size(); i++) {

						Map<String, Object> MAPelements = new HashMap<String, Object>();

						JSONObject job = json.getJSONObject(i);
						Iterator it = job.keys();
						while (it.hasNext()) {
							String key = String.valueOf(it.next());
							if (key.equals("css")) {

								JSONObject Cssjson = JSONObject.fromObject(job.get(key).toString());
								Iterator itcss = Cssjson.keys();
								Map<String, Object> MAPcss = new HashMap<String, Object>();
								while (itcss.hasNext()) {
									String keycss = String.valueOf(itcss.next());
									Object valuecss = Cssjson.get(keycss);
									MAPcss.put(keycss, valuecss);
								}
								MAPelements.put(key, MAPcss);
							} else {

								Object value = job.get(key);
								if (value.equals("null")) {
									MAPelements.put(key, "");
								} else {
									MAPelements.put(key, value);
								}
							}
						}
						Listelements.add(MAPelements);
					}
				}
			}

			Map<String, Object> map = new HashMap<String, Object>();

			map.put("id", tbScenePag.getScenePagId());
			map.put("sceneId", tbScene.getSceneId());
			map.put("num", tbScenePag.getNum());
			map.put("name", null);
			map.put("properties", null);
			map.put("elements", Listelements);
			map.put("scene", MAPscene);

			C2.setObj(map);

		} else {
			C2.setSuccess("false");
			C2.setCode(403);
			C2.setMsg("基础数据服务器获取失败");
		}

		return C2;
	}

	/**
	 * 
	 * scene.savePage
	 * 
	 */
	@RequestMapping(value = "/web/scene_savePage")
	@ResponseBody
	public ScenePageListNew sceneSavePage(HttpServletRequest request) {

		ScenePageListNew spl = new ScenePageListNew();

		String id = request.getParameter("id");
		String sceneId = request.getParameter("sceneId");
		String num = request.getParameter("num");
		String name = request.getParameter("name");
		String elements = request.getParameter("elements");

		try {
			TbScenePag tbScenePag = tbScenePagService.selectByPrimaryKey(Integer.valueOf(id));
			tbScenePag.setContentText(elements);
			tbScenePag.setPagename(name);
			tbScenePag.setNum(Integer.valueOf(num));

			tbScenePagService.updateByPrimaryKeySelective(tbScenePag);

			spl.setSuccess("true");
			spl.setCode(200);
			spl.setMsg("success");

		} catch (Exception e) {
			spl.setSuccess("false");
			spl.setCode(403);
			spl.setMsg("保存失败");
		}

		return spl;
	}

	/**
	 * 
	 * scene.savePageName
	 * 
	 */
	@RequestMapping(value = "/web/scene_savePageName")
	@ResponseBody
	public ScenePageListNew sceneSavePageName(HttpServletRequest request) {

		ScenePageListNew spl = new ScenePageListNew();

		String id = request.getParameter("id");
		String sceneId = request.getParameter("sceneId");
		String name = request.getParameter("name");

		try {

			TbScenePag tbScenePag = tbScenePagService.selectByPrimaryKey(Integer.valueOf(id));

			tbScenePag.setPagename(name);

			tbScenePagService.updateByPrimaryKeySelective(tbScenePag);

			spl.setSuccess("true");
			spl.setCode(200);
			spl.setMsg("success");

		} catch (Exception e) {
			spl.setSuccess("false");
			spl.setCode(403);
			spl.setMsg("保存失败");
		}

		return spl;
	}

	/**
	 * 
	 * scene.pageSort
	 * 
	 */
	@RequestMapping(value = "/web/scene_pageSort")
	@ResponseBody
	public ScenePageListNew scenePageSort(HttpServletRequest request) {

		ScenePageListNew spl = new ScenePageListNew();

		int id = Integer.valueOf(request.getParameter("id"));

		int pos = Integer.valueOf(request.getParameter("pos"));

		TbScenePag tbScenePag = tbScenePagService.selectByPrimaryKey(id);

		if (tbScenePag != null) {
			TbScenePag tbScenePagNew = new TbScenePag();
			tbScenePagNew.setSceneCode(tbScenePag.getSceneCode());
			List<TbScenePag> tbScenePags = tbScenePagService.queryTempList(tbScenePagNew);
			for (TbScenePag tsp : tbScenePags) {
				if (tsp.getScenePagId() == id) {
					tsp.setNum(pos);
					if (tsp.getPagename() == null) {
						tsp.setPagename("第" + pos + "页");
					}
					tbScenePagService.updateByPrimaryKeySelective(tsp);
					continue;
				}
				if (tbScenePag.getNum() < pos) {
					if (tsp.getNum() <= pos && tsp.getNum() > tbScenePag.getNum()) {
						tsp.setNum(tsp.getNum() - 1);
						if (tsp.getPagename() == null) {
							tsp.setPagename("第" + (tsp.getNum() - 1) + "页");
						}
						tbScenePagService.updateByPrimaryKeySelective(tsp);
						continue;
					}
				} else if (tbScenePag.getNum() > pos) {
					if (tsp.getNum() >= pos && tsp.getNum() < tbScenePag.getNum()) {
						tsp.setNum(tsp.getNum() + 1);
						if (tsp.getPagename() == null) {
							tsp.setPagename("第" + (tsp.getNum() + 1) + "页");
						}
						tbScenePagService.updateByPrimaryKeySelective(tsp);
						continue;
					}
				}
			}
			spl.setSuccess("true");
			spl.setCode(200);
			spl.setMsg("success");
		} else {

			spl.setSuccess("false");
			spl.setCode(403);
			spl.setMsg("页面顺序调整失败");
		}

		return spl;

	}

	/**
	 * 
	 * scene.createPage
	 * 
	 */
	@RequestMapping(value = "/web/scene_createPage")
	@ResponseBody
	public ComModel_3 sceneCreatePage(HttpServletRequest request) {

		ComModel_3 C3 = new ComModel_3();

		int id = Integer.valueOf(request.getParameter("id"));

		try {

			TbScenePag tbScenePagNew = tbScenePagService.creatNew(id);

			TbScene tbScene = tbSceneService.selectBySceneCode(tbScenePagNew.getSceneCode());

			C3.setSuccess("true");
			C3.setCode(200);
			C3.setMsg("success");
			C3.setIscopy("false-----24622");

			Map<String, Object> MAPimage = new HashMap<String, Object>();
			MAPimage.put("imgSrc", "default_thum.jpg");
			MAPimage.put("isAdvancedUser", false);

			Map<String, Object> MAPscene = new HashMap<String, Object>();
			MAPscene.put("id", tbScene.getSceneId());
			MAPscene.put("name", tbScene.getSceneName());
			MAPscene.put("createUser", tbScene.getAuthor());
			MAPscene.put("createTime", Long.valueOf("1425998747000"));
			MAPscene.put("type", tbScene.getSceneTypeid());
			MAPscene.put("pageMode", Long.valueOf("0"));
			MAPscene.put("image", MAPimage);
			MAPscene.put("isTpl", Long.valueOf("0"));
			MAPscene.put("isPromotion", Long.valueOf("0"));
			MAPscene.put("status", Long.valueOf("1"));
			MAPscene.put("openLimit", Long.valueOf("0"));
			MAPscene.put("submitLimit", Long.valueOf("0"));
			MAPscene.put("startDate", null);
			MAPscene.put("endDate", null);
			MAPscene.put("accessCode", null);
			MAPscene.put("thirdCode", null);
			MAPscene.put("updateTime", Long.valueOf("1425998747000"));
			MAPscene.put("publishTime", Long.valueOf("1425998747000"));
			MAPscene.put("applyTemplate", Long.valueOf("0"));
			MAPscene.put("applyPromotion", Long.valueOf("0"));
			MAPscene.put("sourceId", null);
			MAPscene.put("code", "U705UCE43R");
			MAPscene.put("description", "");
			MAPscene.put("sort", Long.valueOf("0"));
			MAPscene.put("pageCount", Long.valueOf("0"));
			MAPscene.put("dataCount", Long.valueOf("0"));
			MAPscene.put("showCount", Long.valueOf("0"));
			MAPscene.put("userLoginName", null);
			MAPscene.put("userName", null);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", tbScenePagNew.getScenePagId());
			map.put("sceneId", tbScene.getSceneId());
			map.put("num", tbScenePagNew.getNum());
			map.put("name", null);
			map.put("properties", null);
			map.put("elements", null);
			map.put("scene", MAPscene);

			C3.setObj(map);

		} catch (Exception e) {
			C3.setSuccess("false");
			C3.setCode(403);
			C3.setMsg("创建新页面失败");
		}

		return C3;

	}

	/**
	 * 
	 * scene.delPage
	 * 
	 */
	@RequestMapping(value = "/web/scene_delPage")
	@ResponseBody
	public ScenePageListNew sceneDelPage(HttpServletRequest request) {

		ScenePageListNew spl = new ScenePageListNew();

		int id = Integer.valueOf(request.getParameter("id"));

		try {
			tbScenePagService.deleteByPrimaryKey(id);
			spl.setSuccess("true");
			spl.setCode(200);
			spl.setMsg("success");
		} catch (Exception e) {
			spl.setSuccess("false");
			spl.setCode(403);
			spl.setMsg("删除页面失败");
		}

		return spl;

	}

	/**
	 * 
	 * scene.copyPage
	 * 
	 */
	@RequestMapping(value = "/web/scene_copyPage")
	@ResponseBody
	public ComModel_3 sceneCopyPage(HttpServletRequest request) {

		ComModel_3 C3 = new ComModel_3();

		int id = Integer.valueOf(request.getParameter("id"));

		try {
			TbScenePag tbScenePag = tbScenePagService.copyNew(id);
			TbScene tbScene = tbSceneService.selectBySceneCode(tbScenePag.getSceneCode());

			Map<String, Object> MAPimage = new HashMap<String, Object>();
			MAPimage.put("imgSrc", "default_thum.jpg");
			MAPimage.put("isAdvancedUser", false);

			Map<String, Object> MAPscene = new HashMap<String, Object>();
			MAPscene.put("id", tbScene.getSceneId());
			MAPscene.put("name", tbScene.getSceneName());
			MAPscene.put("createUser", tbScene.getAuthor());
			MAPscene.put("createTime", Long.valueOf("1425998747000"));
			MAPscene.put("type", tbScene.getSceneTypeid());
			MAPscene.put("pageMode", Long.valueOf("0"));
			MAPscene.put("image", MAPimage);
			MAPscene.put("isTpl", Long.valueOf("0"));
			MAPscene.put("isPromotion", Long.valueOf("0"));
			MAPscene.put("status", Long.valueOf("1"));
			MAPscene.put("openLimit", Long.valueOf("0"));
			MAPscene.put("submitLimit", Long.valueOf("0"));
			MAPscene.put("startDate", null);
			MAPscene.put("endDate", null);
			MAPscene.put("accessCode", null);
			MAPscene.put("thirdCode", null);
			MAPscene.put("updateTime", Long.valueOf("1425998747000"));
			MAPscene.put("publishTime", Long.valueOf("1425998747000"));
			MAPscene.put("applyTemplate", Long.valueOf("0"));
			MAPscene.put("applyPromotion", Long.valueOf("0"));
			MAPscene.put("sourceId", null);
			MAPscene.put("code", "U705UCE43R");
			MAPscene.put("description", "");
			MAPscene.put("sort", Long.valueOf("0"));
			MAPscene.put("pageCount", Long.valueOf("0"));
			MAPscene.put("dataCount", Long.valueOf("0"));
			MAPscene.put("showCount", Long.valueOf("0"));
			MAPscene.put("userLoginName", null);
			MAPscene.put("userName", null);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", tbScenePag.getScenePagId());
			map.put("sceneId", tbScene.getSceneId());
			map.put("num", tbScenePag.getNum());
			map.put("name", null);
			map.put("properties", null);
			map.put("elements", null);
			map.put("scene", MAPscene);

			C3.setObj(map);

			C3.setSuccess("true");
			C3.setCode(200);
			C3.setMsg("success");
			C3.setIscopy("true-----24626");
		} catch (Exception e) {
			C3.setSuccess("false");
			C3.setCode(403);
			C3.setMsg("复制页面失败");
		}

		return C3;

	}

	/**
	 * 
	 * scene.getFileList
	 * 
	 */
	@RequestMapping(value = "/web/scene_getFileList")
	@ResponseBody
	public ComModel_4 sceneGetFileList(HttpServletRequest request) {

		ComModel_4 C4 = new ComModel_4();

		String pageNo = request.getParameter("pageNo");

		String pageSize = request.getParameter("pageSize");

		String fileType = request.getParameter("fileType");

		TbFile tbFile = new TbFile();

		tbFile.setFiletype(fileType);

		double a = Double.valueOf(pageNo).doubleValue();
		double b = Double.valueOf(pageSize).doubleValue();

		double c = 0;

		double d = 0;

		List<TbFile> tbFiles = null;

		if (a > 1) {

			c = a * b;

			d = c + 10;

			tbFiles = tbFileService.queryTempList(tbFile, (int) c, (int) d);

		} else {

			tbFiles = tbFileService.queryTempList(tbFile, 0, Integer.valueOf(pageSize));
		}

		if (tbFiles != null) {
			C4.setSuccess("true");
			C4.setCode(200);
			C4.setMsg("success");

			Map<String, Object> MAPmap = new HashMap<String, Object>();
			MAPmap.put("count", tbFiles.size());
			MAPmap.put("pageNo", Integer.valueOf(pageNo));
			MAPmap.put("pageSize", Integer.valueOf(pageSize));

			C4.setMap(MAPmap);

			List<Map<String, Object>> listList = new ArrayList<Map<String, Object>>();

			for (TbFile tf : tbFiles) {
				Map<String, Object> MAPlist = new HashMap<String, Object>();
				MAPlist.put("id", tf.getFileid());
				MAPlist.put("name", tf.getFilename());
				MAPlist.put("extName", tf.getExtname());
				MAPlist.put("fileType", Integer.valueOf(tf.getFiletype()));
				MAPlist.put("bizType", Long.valueOf("101"));
				MAPlist.put("path", tf.getPath());
				MAPlist.put("tmbPath", tf.getPath());
				MAPlist.put("createTime", Long.valueOf("1426209633000"));
				MAPlist.put("createUser", tf.getUserid());
				MAPlist.put("sort", Long.valueOf("0"));
				MAPlist.put("size", Long.valueOf("26"));
				MAPlist.put("status", Long.valueOf("1"));

				listList.add(MAPlist);
			}

			C4.setList(listList);

		} else {
			C4.setSuccess("false");
			C4.setCode(403);
			C4.setMsg("获取文件失败");
		}

		return C4;

	}

	/**
	 * 
	 * scene.uploadFile
	 * 
	 */
	@RequestMapping(value = "/web/scene_uploadFile")
	@ResponseBody
	public ScenePageListNew sceneUploadFile(HttpServletRequest request) {
		ScenePageListNew spl = new ScenePageListNew();

		HttpSession session = request.getSession();

		String fileType = request.getParameter("fileType");

		String fileFormat = "";

		String url = "";

		String fileName = "";

		String attachmentOriginalName = "";

		String attachmentName = "";

		try {
			MultipartFile attachmentFile = ((MultipartHttpServletRequest) request).getFile("file");
			String uploadDir = request.getSession().getServletContext().getRealPath("/showUpload");

			fileName = System.currentTimeMillis() + "";
			InputStream stream = attachmentFile.getInputStream();
			attachmentOriginalName = attachmentFile.getOriginalFilename();// 文件源名
			fileFormat = attachmentOriginalName.substring(attachmentOriginalName.lastIndexOf(".") + 1,
					attachmentOriginalName.length());
			attachmentName = fileName + "." + fileFormat;

			File dirPath = new File(uploadDir);
			if (!dirPath.exists()) {
				dirPath.mkdirs();
			}

			OutputStream bos = null;

			url = uploadDir + "//" + attachmentName;

			try {
				bos = new FileOutputStream(url);
				int bytesRead;
				byte[] buffer = new byte[8192];
				while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
					bos.write(buffer, 0, bytesRead);
				}
			} finally {
				if (bos != null) {
					bos.close();
				}
				if (stream != null) {
					stream.close();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			spl.setSuccess("false");
			spl.setCode(403);
			spl.setMsg("上传文件失败");
			return spl;
		}

		TbFile tbFile = new TbFile();
		String idd = UUID.randomUUID().toString();
		tbFile.setFileid(idd);
		tbFile.setExtname(fileFormat);
		tbFile.setFiletype(fileType);
		tbFile.setPath("/show/showUpload/" + attachmentName);
		tbFile.setAddtime(new Date());
		tbFile.setUserid((String) session.getAttribute("UserId"));
		tbFile.setFilename(fileName);
		tbFile.setFileusername(attachmentOriginalName);

		try {
			tbFileService.insert(tbFile);
			spl.setSuccess("true");
			spl.setCode(200);
			spl.setMsg("success");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", idd);
			map.put("name", fileName);
			map.put("extName", fileFormat);
			map.put("fileType", Integer.valueOf(fileType));
			map.put("path", "/show/showUpload/" + attachmentName);
			map.put("tmbPath", "/show/showUpload/" + attachmentName);
			map.put("createTime", Long.valueOf("1426209412922"));
			map.put("createUser", (String) session.getAttribute("UserId"));
			map.put("bizType", Long.valueOf("0"));
			map.put("sort", Long.valueOf("0"));
			map.put("size", Long.valueOf("2"));
			map.put("status", Long.valueOf("1"));

			spl.setObj(map);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			spl.setSuccess("false");
			spl.setCode(403);
			spl.setMsg("上传文件失败");
			return spl;
		}

		return spl;
	}

	/**
	 * 
	 * scene.delFile
	 * 
	 */
	@RequestMapping(value = "/web/scene_delFile")
	@ResponseBody
	public ScenePageListNew sceneDelFile(HttpServletRequest request) {
		ScenePageListNew spl = new ScenePageListNew();

		String id = request.getParameter("id");

		try {
			tbFileService.deleteByPrimaryKey(id);

			spl.setSuccess("true");
			spl.setCode(200);
			spl.setMsg("success");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			spl.setSuccess("false");
			spl.setCode(403);
			spl.setMsg("删除文件失败");
		}

		return spl;
	}

	/**
	 * 
	 * scene.getSceneSetting
	 * 
	 */
	@RequestMapping(value = "/web/scene_getSceneSetting")
	@ResponseBody
	public ScenePageListNew sceneGetSceneSetting(HttpServletRequest request) {
		ScenePageListNew spl = new ScenePageListNew();
		String sceneId = request.getParameter("sceneId");

		TbScene tbScene = tbSceneService.selectBySceneCode(sceneId);

		if (tbScene != null) {
			spl.setSuccess("true");
			spl.setCode(200);
			spl.setMsg("success");

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", tbScene.getSceneId());
			map.put("name", tbScene.getSceneName());
			map.put("createUser", tbScene.getAuthor());
			map.put("createTime", Long.valueOf("1425998747000"));
			map.put("type", tbScene.getSceneCustomId());
			map.put("pageMode", Long.valueOf("0"));
			map.put("eqcode", "");
			map.put("cover", tbScene.getCover());
			map.put("code", tbScene.getSceneCode());
			map.put("description", "testDetail");
			map.put("isTpl", Long.valueOf("0"));
			map.put("isShow", Long.valueOf("0"));
			map.put("updateTime", tbScene.getAddtime());
			map.put("publishTime", Long.valueOf("1464168275"));

			spl.setObj(map);

		} else {
			spl.setSuccess("false");
			spl.setCode(403);
			spl.setMsg("获取场景基本配置失败");
		}

		return spl;
	}

	/**
	 * 
	 * scene.publish
	 * 
	 */
	@RequestMapping(value = "/web/scene_publish")
	@ResponseBody
	public ScenePageListNew scenePublish(HttpServletRequest request) {
		ScenePageListNew spl = new ScenePageListNew();

		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String createUser = request.getParameter("createUser");
		String type = request.getParameter("type");
		String cover = request.getParameter("cover");
		String code = request.getParameter("code");
		String description = request.getParameter("description");
		String isTpl = request.getParameter("isTpl");
		String isShow = request.getParameter("isShow");

		TbScene tbScene = tbSceneService.selectByPrimaryKey(Integer.valueOf(id));

		if (tbScene != null) {

			tbScene.setSceneName(name);
			tbScene.setAuthor(createUser);
			tbScene.setSceneCustomId(Integer.valueOf(type));
			tbScene.setCover(cover);
			tbScene.setDes(description);
			try {
				tbSceneService.updateByPrimaryKeySelective(tbScene);

				spl.setSuccess("true");
				spl.setCode(200);
				spl.setMsg("success");

			} catch (Exception e) {
				// TODO Auto-generated catch block
				spl.setSuccess("false");
				spl.setCode(403);
				spl.setMsg("发布失败");

				return spl;
			}

		} else {
			spl.setSuccess("false");
			spl.setCode(403);
			spl.setMsg("发布失败");
		}

		return spl;
	}

	/**
	 * 
	 * scene.preview
	 * 
	 */
	@RequestMapping(value = "/web/scene_preview")
	@ResponseBody
	public ScenePageListNew scenePreview(HttpServletRequest request) {
		ScenePageListNew spl = new ScenePageListNew();

		String id = request.getParameter("id");
		TbScene tbScene = tbSceneService.selectByPrimaryKey(Integer.valueOf(id));

		if (tbScene != null) {

			TbScenePag tbScenePag = new TbScenePag();

			tbScenePag.setSceneCode(tbScene.getSceneCode());

			List<TbScenePag> tbScenePags = tbScenePagService.queryTempList(tbScenePag);

			Map<String, Object> mapbgAudio = new HashMap<String, Object>();
			mapbgAudio.put("url", tbScene.getCover());
			mapbgAudio.put("type", tbScene.getSceneCustomId());

			Map<String, Object> mapproperty = new HashMap<String, Object>();
			mapproperty.put("triggerLoop", true);
			mapproperty.put("eqAdType", Long.valueOf("1"));
			mapproperty.put("hideEqAd", false);

			Map<String, Object> mapObj = new HashMap<String, Object>();
			mapObj.put("id", tbScene.getSceneId());
			mapObj.put("name", tbScene.getSceneName());
			mapObj.put("createUser", tbScene.getAuthor());
			mapObj.put("type", tbScene.getSceneCustomId());
			mapObj.put("pageMode", Long.valueOf("0"));
			mapObj.put("cover", tbScene.getCover());
			mapObj.put("bgAudio", mapbgAudio);
			mapObj.put("code", tbScene.getSceneCode());
			mapObj.put("description", tbScene.getDes());
			mapObj.put("updateTime", Long.valueOf("1426045746000"));
			mapObj.put("createTime", Long.valueOf("1426045746000"));
			mapObj.put("property", mapproperty);
			mapObj.put("publishTime", Long.valueOf("1426045746000"));

			spl.setObj(mapObj);

			if (tbScenePags.size() > 0) {

				List<Map<String, Object>> List = new ArrayList<Map<String, Object>>();

				for (TbScenePag tsp : tbScenePags) {

					List<Map<String, Object>> Listelements = new ArrayList<Map<String, Object>>();

					if (tsp.getContentText() != null && tsp.getContentText().length() > 0) {
						JSONArray json = JSONArray.fromObject(tsp.getContentText());

						if (json.size() > 0) {
							for (int i = 0; i < json.size(); i++) {

								Map<String, Object> MAPelements = new HashMap<String, Object>();

								JSONObject job = json.getJSONObject(i);
								Iterator it = job.keys();
								while (it.hasNext()) {
									String key = String.valueOf(it.next());
									if (key.equals("css")) {

										JSONObject Cssjson = JSONObject.fromObject(job.get(key).toString());
										Iterator itcss = Cssjson.keys();
										Map<String, Object> MAPcss = new HashMap<String, Object>();
										while (itcss.hasNext()) {
											String keycss = String.valueOf(itcss.next());
											String valuecss = Cssjson.get(keycss).toString();
											MAPcss.put(keycss, valuecss);
										}

										MAPelements.put(key, MAPcss);
									} else {

										String value = job.get(key).toString();

										if (value.equals("null")) {
											MAPelements.put(key, "");
										} else {
											MAPelements.put(key, value);
										}
									}
								}
								Listelements.add(MAPelements);
							}
						}
					}

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", tsp.getScenePagId());
					map.put("sceneId", tbScene.getSceneId());
					map.put("name", null);
					map.put("properties", null);
					map.put("elements", Listelements);
					map.put("scene", null);

					List.add(map);
				}

				spl.setList(List);
				
				spl.setSuccess("true");
				spl.setCode(200);
				spl.setMsg("操作成功");

			}

		} else {
			spl.setSuccess("false");
			spl.setCode(403);
			spl.setMsg("预览失败");
		}

		return spl;
	}

	/**
	 * 
	 * scene.uploadCoverImg
	 * 
	 */
	@RequestMapping(value = "/web/scene_uploadCoverImg")
	@ResponseBody
	public ComModel_5 sceneUploadCoverImg(HttpServletRequest request) {
		ComModel_5 C5 = new ComModel_5();

		String id = request.getParameter("id");

		String src = request.getParameter("src");

		String fileType = request.getParameter("fileType");

		String x = request.getParameter("x");

		String y = request.getParameter("y");

		String w = request.getParameter("w");

		String h = request.getParameter("h");

		TbScene tbScene = tbSceneService.selectByPrimaryKey(Integer.valueOf(id));

		if (tbScene != null) {

			tbScene.setCover(src);
			tbScene.setFiletype(fileType);
			tbScene.setX(Integer.valueOf(x));
			tbScene.setY(Integer.valueOf(y));
			tbScene.setW(Integer.valueOf(w));
			tbScene.setH(Integer.valueOf(h));

			tbSceneService.updateByPrimaryKeySelective(tbScene);

			C5.setSuccess("true");
			C5.setCode(200);
			C5.setMsg("操作成功");
			C5.setObj(src);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", tbScene.getSceneId());
			map.put("path", src);
			map.put("src", src);
			map.put("y", y);
			map.put("w", w);
			map.put("h", h);
			map.put("x", x);
			map.put("index", "");
			map.put("fileType", fileType);

			C5.setMap(map);

		} else {
			C5.setSuccess("false");
			C5.setCode(403);
			C5.setMsg("上传缩略图失败");
		}

		return C5;
	}

	/**
	 * 
	 * scene.typeList
	 * 
	 */
	@RequestMapping(value = "/web/scene_typeList")
	@ResponseBody
	public ScenePageListNew sceneTypeList(HttpServletRequest request) {
		ScenePageListNew spl = new ScenePageListNew();

		TbCode tbCode = new TbCode();
		tbCode.setGroupId(2);// 获取行业类型
		List<TbCode> tbCodes = tbCodeService.queryTempList(tbCode);

		if (tbCodes != null) {
			spl.setSuccess("true");
			spl.setCode(200);
			spl.setMsg("success");

			List<Map<String, Object>> List = new ArrayList<Map<String, Object>>();

			int i = 1;
			for (TbCode tc : tbCodes) {

				Map<String, Object> map = new HashMap<String, Object>();

				map.put("id", tc.getCodeId());
				map.put("name", tc.getMsg());
				map.put("value", tc.getState() + "");
				map.put("type", "scene_type");
				map.put("sort", i);
				map.put("status", Long.valueOf("1"));
				map.put("remark", null);

				List.add(map);

				i++;
			}

			spl.setList(List);

		} else {
			spl.setSuccess("false");
			spl.setCode(403);
			spl.setMsg("获取场景类型失败");
		}

		return spl;
	}

	/**
	 * 
	 * scene.userAuthor
	 * 
	 */
	@RequestMapping(value = "/web/scene_userAuthor")
	@ResponseBody
	public ComModel_6 sceneUserAuthor(HttpServletRequest request, HttpServletResponse response) {

		ComModel_6 C6 = new ComModel_6();

		HttpSession session = request.getSession();

		if (session.getAttribute("UserName") == null) {

			response.setStatus(401);
			C6.setSuccess(false);
			C6.setCode(1001);
			C6.setMsg("请先登录");

		} else {

			C6.setSuccess(true);
			C6.setCode(200);
			C6.setMsg("操作成功");
			C6.setObj(100);
			C6.setMap("");
		}

		return C6;
	}

	/**
	 * 
	 * scene.reLogin
	 * 
	 */
	@RequestMapping(value = "/web/scene_reLogin")
	@ResponseBody
	public ComModel_7 scenereLogin(HttpServletRequest request, HttpServletResponse response, TbShowUser tbShowUser) {

		ComModel_7 C7 = new ComModel_7();

		HttpSession session = request.getSession();

		String UserName = request.getParameter("username");
		String pwd = request.getParameter("password");

		tbShowUser.setUserName(UserName);
		tbShowUser.setUserPwd(pwd);

		TbShowUser tsu = tbShowUserService.selectByNamePwd(tbShowUser);

		if (tsu != null) {
			session.setAttribute("UserId", tsu.getUserId());
			session.setAttribute("UserName", UserName);
			session.setAttribute("pwd", pwd);

			C7.setSuccess(true);
			C7.setCode(200);
			C7.setMsg("success");

		} else {

			C7.setSuccess(false);
			C7.setCode(1003);
			C7.setMsg("登录失败");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("isValidateCodeLogin", false);
			C7.setMap(map);

		}

		return C7;
	}
}
