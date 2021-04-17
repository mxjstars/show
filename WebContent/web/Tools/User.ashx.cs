using Bll;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace web.Tools
{
    /// <summary>
    /// User 的摘要说明
    /// </summary>
    public class User : IHttpHandler, System.Web.SessionState.IRequiresSessionState
    {
        //返回 -1 为失败
        public void ProcessRequest(HttpContext context)
        {
            string t = HttpContext.Current.Request.Form["t"];//干什么用的
            switch (t)
            {
                case "1":
                    CheckUser(context);
                    break;
                case "2":
                    Login(context);
                    break;
                case "3":
                    RegUser(context);
                    break;
                case "4":
                    GetUserContext(context);
                    break;
                case "5":
                    Out(context);
                    break;
                case "6":
                    GetHeadImg(context);
                    break;
                case "7":
                    SetPwd_phone(context);
                    break;
                case "8":
                    ResPwd(context);
                    break;
                case "9":
                    SetPwd_Email(context);
                    break;
                case "10":
                    ResEmailPwd(context);
                    break;
                case "11":
                    SelectBind(context);
                    break;
                case "12":
                    UpdateEmail(context);
                    break;
                case "13":
                    UpdatePhone(context);
                    break;
                case "14":
                    UpdatePwd(context);
                    break;
            }
        }
        /// <summary>
        /// 修改密码
        /// </summary>
        /// <param name="context"></param>
        public void UpdatePwd(HttpContext context)
        {
            if (HttpContext.Current.Session["userName"] != null)
            {
                string newpwd = HttpContext.Current.Request.Form["newpwd"];
                string pwd = HttpContext.Current.Request.Form["pwd"];
                show_userBll bll = new show_userBll();
                if (bll.SelectUserByNamePwd(HttpContext.Current.Session["userName"].ToString(), pwd) != "")
                {
                    bll.Updatepwd(HttpContext.Current.Session["userName"].ToString(), newpwd);
                    context.Response.Write("{\"status\":\"0\"}");
                }
                else
                {
                    context.Response.Write("{\"status\":\"-2\"}");
                }
            }
            else
            {
                context.Response.Write("{\"status\":\"-1\"}");
            }
        }
        /// <summary>
        /// 修改手机
        /// </summary>
        /// <param name="context"></param>
        public void UpdatePhone(HttpContext context)
        {
            if (HttpContext.Current.Session["userName"] != null)
            {
                string phone = HttpContext.Current.Request.Form["phone"];
                string pwd = HttpContext.Current.Request.Form["pwd"];
                show_userBll bll = new show_userBll();
                if (bll.SelectUserByNamePwd(HttpContext.Current.Session["userName"].ToString(), pwd) != "")
                {
                    bll.UpdatePhone(HttpContext.Current.Session["userName"].ToString(), phone);
                    context.Response.Write("{\"status\":\"0\"}");
                }
                else
                {
                    context.Response.Write("{\"status\":\"-2\"}");
                }
            }
            else
            {
                context.Response.Write("{\"status\":\"-1\"}");
            }
        }
        /// <summary>
        /// 修改邮箱
        /// </summary>
        /// <param name="context"></param>
        public void UpdateEmail(HttpContext context)
        {
            if (HttpContext.Current.Session["userName"] != null)
            {
                string email = HttpContext.Current.Request.Form["email"];
                string pwd = HttpContext.Current.Request.Form["pwd"];
                show_userBll bll = new show_userBll();
                if (bll.SelectUserByNamePwd(HttpContext.Current.Session["userName"].ToString(), pwd) != "")
                {
                    bll.UpdateEmail(HttpContext.Current.Session["userName"].ToString(), email);
                    context.Response.Write("{\"status\":\"0\"}");
                }
                else
                {
                    context.Response.Write("{\"status\":\"-2\"}");
                }
            }
            else
            {
                context.Response.Write("{\"status\":\"-1\"}");
            }
        }
        /// <summary>
        /// 设置邮箱密码
        /// </summary>
        /// <param name="context"></param>
        public void ResEmailPwd(HttpContext context)
        {
            string userName = HttpContext.Current.Session["userName"].ToString();
            show_userBll bll = new show_userBll();
            string pwd = HttpContext.Current.Request.Form["pwd"];
            bll.UpdatePwd(userName, pwd);
            context.Response.Write("{\"status\":\"0\"}");
        }
        /// <summary>
        /// 邮箱修改密码
        /// </summary>
        /// <param name="context"></param>
        public void SetPwd_Email(HttpContext context)
        {
            string userName = HttpContext.Current.Request.Form["UserName"];
            string code = HttpContext.Current.Request.Form["code"];
            show_userBll bll = new show_userBll();
            //是否由此用户名
            if (!bll.CheckUserNname(userName))
            {
                context.Response.Write("{\"status\":\"-4\"}");
                return;
            }

            if (HttpContext.Current.Session["code"] == null)
            {
                context.Response.Write("{\"status\":\"-3\"}");
                return;
            }
            if (code != HttpContext.Current.Session["code"].ToString())
            {
                context.Response.Write("{\"status\":\"-1\"}");
                return;
            }
            f.SendEmail(userName, Guid.NewGuid().ToString(), 1);
            context.Response.Write("{\"status\":\"0\"}");
        }

        /// <summary>
        /// 设置密码
        /// </summary>
        /// <param name="context"></param>
        public void ResPwd(HttpContext context)
        {
            string userName = HttpContext.Current.Request.Form["UserName"];
            string phoneCode = HttpContext.Current.Request.Form["phoneCode"];
            string code = HttpContext.Current.Request.Form["code"];
            show_userBll bll = new show_userBll();
            //是否由此用户名
            if (!bll.CheckUserNname(userName))
            {
                context.Response.Write("{\"status\":\"-4\"}");
                return;
            }
            //获取验证码
            string dbyzm = bll.GetCode(userName);

            if (dbyzm != phoneCode)
            {
                context.Response.Write("{\"status\":\"-2\"}");
                return;
            }
            string pwd = HttpContext.Current.Request.Form["pwd"];
            bll.UpdatePwd(userName, pwd);
            context.Response.Write("{\"status\":\"0\"}");
        }

        /// <summary>
        /// 手机重置密码 -1为验证码不正确 -2为手机验证码不正确 -3为验证码过期
        /// </summary>
        /// <param name="context"></param>
        public void SetPwd_phone(HttpContext context)
        {
            string userName = HttpContext.Current.Request.Form["UserName"];
            string phoneCode = HttpContext.Current.Request.Form["phoneCode"];
            string code = HttpContext.Current.Request.Form["code"];
            show_userBll bll = new show_userBll();
            //是否由此用户名
            if (!bll.CheckUserNname(userName))
            {
                context.Response.Write("{\"status\":\"-4\"}");
                return;
            }

            if (HttpContext.Current.Session["code"] == null)
            {
                context.Response.Write("{\"status\":\"-3\"}");
                return;
            }
            if (code != HttpContext.Current.Session["code"].ToString())
            {
                context.Response.Write("{\"status\":\"-1\"}");
                return;
            }

            //获取验证码
            string dbyzm = bll.GetCode(userName);

            if (dbyzm != phoneCode)
            {
                context.Response.Write("{\"status\":\"-2\"}");
                return;
            }
            context.Response.Write("{\"status\":\"0\"}");
        }

        /// <summary>
        /// 获取头像
        /// </summary>
        /// <param name="context"></param>
        public void GetHeadImg(HttpContext context)
        {
            if (HttpContext.Current.Session["UserName"] != null)
            {
                string userName = HttpContext.Current.Session["UserName"].ToString();
                show_userBll bll = new show_userBll();
                string img = bll.GetHeadImg(userName);
                context.Response.Write("{\"status\":\"" + img + "\"}");
            }
        }
        //退出
        public void Out(HttpContext context)
        {
            HttpContext.Current.Session["UserName"] = null;
            HttpContext.Current.Session["userID"] = null;
            context.Response.Write("{\"status\":\"0\"}");
        }
        //检查用户是否登录 
        public void CheckUser(HttpContext context)
        {
            if (HttpContext.Current.Session["UserName"] == null)
            {
                context.Response.Write("{\"status\":\"-1\"}");
                return;
            }
            else
            {
                context.Response.Write("{\"status\":\"0\"}");
            }
        }
        //用户登录
        public void Login(HttpContext context)
        {
            string UserName = HttpContext.Current.Request.Form["UserName"];
            string pwd = HttpContext.Current.Request.Form["pwd"];
            string code = HttpContext.Current.Request.Form["code"];
            if (HttpContext.Current.Session["code"] == null)
            {
                context.Response.Write("{\"status\":\"验证码过期，请点击验证码，进行刷新！\"}");
                return;
            }
            if (UserName == "" || pwd == "")
            {
                context.Response.Write("{\"status\":\"用户名密码不能为空!\"}");
                return;
            }
            if (code.ToLower() == HttpContext.Current.Session["code"].ToString().ToLower())
            {
                show_userBll bll = new show_userBll();
                string userID = bll.SelectUserByNamePwd(UserName, pwd);
                if (userID == "")
                {
                    context.Response.Write("{\"status\":\"用户名密码错误!\"}");
                    return;
                }
                string Usercode = bll.GetUserState(UserName);
                if (Usercode == "1")
                {
                    context.Response.Write("{\"status\":\"用户不可用!\"}");
                    return;
                }
                if (Usercode == "99")
                {
                    context.Response.Write("{\"status\":\"你是恶意用户，不能登录!\"}");
                    return;
                }
                HttpContext.Current.Session["userID"] = userID;
                HttpContext.Current.Session["UserName"] = UserName;
                context.Response.Write("{\"status\":\"0\"}");
                return;
            }
            else
            {
                context.Response.Write("{\"status\":\"验证码错误\"}");
                return;
            }
        }
        //注册用户
        public void RegUser(HttpContext context)
        {
            string RegType = HttpContext.Current.Request.Form["RegType"];
            string values = HttpContext.Current.Request.Form["values"];
            string pwd = HttpContext.Current.Request.Form["pwd"];
            string neckName = HttpContext.Current.Request.Form["neckName"];
            string sex = HttpContext.Current.Request.Form["sex"];
            string yzm = HttpContext.Current.Request.Form["yzm"];

            show_userBll bll = new show_userBll();

            //如果是邮箱注册
            if (RegType == "1")
            {
                //发送邮件
                string code = bll.GetCode(values);
                if (code != "")
                {
                    //发送邮件
                    f.SendEmail(values, code, 0);
                    context.Response.Write("{\"status\":\"1\"}");
                    return;
                }
            }

            if (bll.CheckUserName(values))
            {
                context.Response.Write("{\"status\":\"用户名重复!\"}");
                return;
            }

            //获取验证码
            string dbyzm = bll.GetCode(values);

            if (RegType == "0")
            {
                if (dbyzm != yzm)
                {
                    context.Response.Write("{\"status\":\"验证码不正确!\"}");
                    return;
                }
            }

            bll.AddUserName(values, pwd, int.Parse(RegType), sex, neckName, values);
            HttpContext.Current.Session["UserName"] = values;
            if (RegType == "0")
            {
                context.Response.Write("{\"status\":\"0\"}");
            }
            else
            {
                //发送邮件
                string code = Guid.NewGuid().ToString();
                f.SendEmail(values, code, 0);
                context.Response.Write("{\"status\":\"1\"}");
            }
        }


        //获取用户信息
        public void GetUserContext(HttpContext context)
        {
            show_userBll bll = new show_userBll();
            if (HttpContext.Current.Session["UserName"] != null)
            {
                string userName = HttpContext.Current.Session["UserName"].ToString();
                Model.User u = bll.GetModelByUserName(userName);
                CodeBll codebll = new CodeBll();
                string uT = codebll.GetStrByGroupCode("6", u.user_type_code_id);
                DateTime dt = DateTime.Parse(u.addtime);
                
                context.Response.Write("{\"userName\":\"" + u.user_name + "\",\"userType\":\"" + uT + "\",\"userTime\":\"" + dt.ToString("yyyy-mm-dd hh:mm:ss") + "\",\"status\":\"0\"}");
            }
            else
            {
                context.Response.Write("{\"status\":\"-1\"}");
            }
        }

        //查询用户是否绑定手机和邮箱
        public void SelectBind(HttpContext context)
        {
            show_userBll bll = new show_userBll();
            if (HttpContext.Current.Session["UserName"] != null)
            {
                List<string> list = bll.GetUserBind(HttpContext.Current.Session["UserName"].ToString());
                context.Response.Write("{\"Phone\":\"" + list[0] + "\",\"email\":\"" + list[1] + "\",\"status\":\"0\"}");
            }
            else
            {
                context.Response.Write("{\"status\":\"-1\"}");
            }
        }
        public bool IsReusable
        {
            get
            {
                return false;
            }
        }
    }
}