package com.mingrisoft.controller;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.kaptcha.Constants;
import com.mingrisoft.model.LoginStatus;
import com.mingrisoft.model.TbScene;
import com.mingrisoft.model.TbShowUser;
import com.mingrisoft.modelNew.LoginStatusNew;
import com.mingrisoft.service.TbSceneService;
import com.mingrisoft.service.TbShowUserService;

@Controller
public class TbShowUserController extends BaseController {

	@Autowired(required = true)
	private TbShowUserService tbShowUserService;

	@Autowired(required = true)
	private TbSceneService tbSceneService;

	/**
	 * 
	 * 用户注册 RegUser
	 */
	@RequestMapping(value = "/web/RegUser")
	@ResponseBody
	public LoginStatus RegUser(HttpServletRequest request) {

		LoginStatus login = new LoginStatus();

		String RegType = request.getParameter("RegType");
		String values = request.getParameter("values");
		String pwd = request.getParameter("pwd");
		String neckName = request.getParameter("neckName");
		String sex = request.getParameter("sex");
		String yzm = request.getParameter("yzm");

		TbShowUser tbShowUser = new TbShowUser();

		tbShowUser.setUserName(values);

		TbShowUser tbShowUserNew = tbShowUserService.selectByNamePwd(tbShowUser);

		if (tbShowUserNew != null) {
			login.setStatus("用户名重复！");
			return login;
		}

		// 如果是邮箱注册
		if (RegType == "1") {

			// 发送邮件
			
			login.setStatus("1");
			return login;

		}

		return login;
	}

	/**
	 * 用户登录
	 * 
	 * @param request
	 * @param LoginStatus
	 * @param tbShowUser
	 * @return
	 */
	@RequestMapping(value = "/web/loginOnTest")
	@ResponseBody
	public LoginStatus loginOnTest(HttpServletRequest request, LoginStatus LoginStatus, TbShowUser tbShowUser) {

		String UserName = request.getParameter("UserName");
		String pwd = request.getParameter("pwd");
		String code = request.getParameter("code");

		HttpSession session = request.getSession();

		String codeSys = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);

		if (codeSys != "" && code != "") {
			if (!codeSys.toLowerCase().equals(code.toLowerCase())) {
				LoginStatus.setStatus("验证码输入错误！");
				return LoginStatus;
			} else if (codeSys.toLowerCase().equals(code.toLowerCase())) {

				tbShowUser.setUserName(UserName);
				tbShowUser.setUserPwd(pwd);

				TbShowUser tsu = tbShowUserService.selectByNamePwd(tbShowUser);

				if (tsu != null) {
					LoginStatus.setStatus("0");
					session.setAttribute("UserId", tsu.getUserId());
				} else {
					LoginStatus.setStatus("用户名或密码错误！");
				}

				session.setAttribute("UserName", UserName);
				session.setAttribute("pwd", pwd);
				return LoginStatus;
			}
		} else {
			LoginStatus.setStatus("请输入验证码");
			return LoginStatus;
		}

		return LoginStatus;

	}

	/**
	 * 验证登录
	 * 
	 * @param request
	 * @param LoginStatus
	 * @return
	 */
	@RequestMapping(value = "/web/loginOnCheck")
	@ResponseBody
	public LoginStatus loginOnCheck(HttpServletRequest request, LoginStatus LoginStatus) {

		HttpSession session = request.getSession(true);
		String t = request.getParameter("t");

		if (t != null && t.equals("5")) {
			session.setAttribute("UserName", null);
			return LoginStatus;
		}

		if (session.getAttribute("UserName") == null) {
			LoginStatus.setStatus("-1");
		} else {
			LoginStatus.setStatus("0");
		}

		return LoginStatus;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param request
	 * @param LoginStatus
	 * @return
	 */
	@RequestMapping(value = "/web/getLogin")
	@ResponseBody
	public LoginStatusNew getLogin(HttpServletRequest request) {

		HttpSession session = request.getSession(true);

		LoginStatusNew lsn = new LoginStatusNew();

		if (session.getAttribute("UserName") == null) {

			lsn.setStatus("-1");

		} else {

			TbShowUser tbShowUser = new TbShowUser();

			tbShowUser.setUserName((String) session.getAttribute("UserName"));

			TbShowUser tsu = tbShowUserService.selectByNamePwd(tbShowUser);

			if (tsu != null) {
				lsn.setStatus("0");
				lsn.setUserName(tsu.getUserName());
				lsn.setUserType(tsu.getUserTypeCodeId() + "");
				lsn.setUserTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(tsu.getAddtime()));
			} else {
				lsn.setStatus("-1");
			}
		}

		return lsn;

	}

	/**
	 * 用户场景数量
	 * 
	 */
	@RequestMapping(value = "/web/getCount")
	@ResponseBody
	public LoginStatusNew getCount(HttpServletRequest request) {

		LoginStatusNew lsn = new LoginStatusNew();

		HttpSession session = request.getSession(true);

		if (session.getAttribute("UserId") == null) {

			lsn.setStatus("-1");

		} else {

			TbScene tbScene = new TbScene();

			tbScene.setAuthor((String) session.getAttribute("UserId"));

			List<TbScene> tbScenes = tbSceneService.queryTempList(tbScene);

			lsn.setStatus(tbScenes.size() + "");
		}

		return lsn;

	}

	/**
	 * 获取头像
	 * 
	 */
	@RequestMapping(value = "/web/getHeadImg")
	@ResponseBody
	public LoginStatus GetHeadImg(HttpServletRequest request, LoginStatus LoginStatus) {

		HttpSession session = request.getSession(true);

		TbShowUser tbShowUser = new TbShowUser();
		tbShowUser.setUserName(session.getAttribute("UserName").toString());

		TbShowUser tsu = tbShowUserService.selectByNamePwd(tbShowUser);

		LoginStatus.setStatus(tsu.getHeadimg());

		return LoginStatus;
	}

}
