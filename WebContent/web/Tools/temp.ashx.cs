using Bll;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Web;

namespace web.Tools
{
    /// <summary>
    /// temp 的摘要说明
    /// </summary>
    public class temp : IHttpHandler, System.Web.SessionState.IRequiresSessionState
    {
        //返回 -1 为失败
        public void ProcessRequest(HttpContext context)
        {
            string t = HttpContext.Current.Request.Form["t"];//干什么用的
            switch (t)
            {
                case "1":
                    GetTemp(context);
                    break;
                case "2":
                    AddMouseClick(context);
                    break;
                case "3":
                    GetModel(context);
                    break;
                case "4":
                    CreateTemp(context);
                    break;
                case "5":
                    GetSence(context);
                    break;
                case "6":
                    DeleteSence(context);
                    break;
                case "7":
                    CopeSence(context);
                    break;
                case "8":
                    GiveSence(context);
                    break;
                case "9":
                    UseTemp(context);
                    break;
                case "10":
                    GetCount(context);
                    break;
            }
        }

        /// <summary>
        /// 创建模板
        /// </summary>
        /// <param name="context"></param>
        public void CreateTemp(HttpContext context)
        {
            if (HttpContext.Current.Session["userID"] == null)
            {
                context.Response.Write("{\"status\":\"-1\"}");
                return;
            }
            sceneBll bll = new sceneBll();
            string hyid = HttpContext.Current.Request.Form["hyid"];
            string userId = HttpContext.Current.Session["userID"].ToString();
            string sceneCode = Guid.NewGuid().ToString();
            bll.DefaultScene(userId, hyid, sceneCode);
            bll.DefaultScenePage(sceneCode);
            context.Response.Write("{\"status\":\"" + sceneCode + "\"}");
        }

        /// <summary>
        /// 获取模板
        /// </summary>
        /// <param name="context"></param>
        public void GetTemp(HttpContext context)
        {
            string H = HttpContext.Current.Request.Form["H"];
            string C = HttpContext.Current.Request.Form["C"];
            string orderByFree = HttpContext.Current.Request.Form["orderByFree"];
            string OrderByNew = HttpContext.Current.Request.Form["OrderByNew"];
            string PageInt = HttpContext.Current.Request.Form["PageInt"];
            string CountRow = HttpContext.Current.Request.Form["CountRow"];
            string SerachStr = HttpContext.Current.Request.Form["SerachStr"];
            TempsBll BLL = new TempsBll();
            DataTable dt = BLL.GetTable(int.Parse(H), int.Parse(C), int.Parse(orderByFree), int.Parse(OrderByNew), int.Parse(PageInt), int.Parse(CountRow), f.MyEncodeInputString(SerachStr));
            if (dt == null)
            {
                context.Response.Write("{\"status\":\"-1\"}");
                return;
            }
            string json = f.ToJson(dt);
            json = json.Replace("\"", "\\\"");
            context.Response.Write("{\"status\":\"" + json + "\"}");
        }

        /// <summary>
        /// 添加点击次数
        /// </summary>
        /// <param name="context"></param>
        public void AddMouseClick(HttpContext context)
        {
            string temp_code = HttpContext.Current.Request.Form["temp_code"];
            TempsBll bll = new TempsBll();
            bll.AddNum(temp_code);
            context.Response.Write("{\"status\":\"0\"}");
            return;
        }

        /// <summary>
        /// 获取模板详细信息
        /// </summary>
        /// <param name="context"></param>
        public void GetModel(HttpContext context)
        {
            string temp_code = HttpContext.Current.Request.Form["temp_code"];
            TempsBll bll = new TempsBll();
            Model.temp t = bll.GetModel(temp_code);
            context.Response.Write("{\"status\":\"0\",\"cover\":\"" + t.cover + "\",\"des\":\"" + t.des + "\",\"authorName\":\"" + t.authorName + "\",\"author\":\"" + t.author + "\",\"MouseClick\":\"" + t.MouseClick + "\",\"Money\":\"" + t.Money + "\"}");
            return;
        }

        /// <summary>
        /// 获取此人的场景列表
        /// </summary>
        /// <param name="context"></param>
        public void GetSence(HttpContext context)
        {
            if (HttpContext.Current.Session["userID"] != null)
            {
                string userId = HttpContext.Current.Session["userID"].ToString();
                string selectStr= HttpContext.Current.Request.Form["selectStr"];
                string hnagye= HttpContext.Current.Request.Form["hnagye"];
                string PageInt = HttpContext.Current.Request.Form["PageInt"];
                string CountRow = HttpContext.Current.Request.Form["CountRow"];
                sceneBll bll = new sceneBll();
                DataTable dt = bll.GetSenceByUserId(userId, selectStr, hnagye, int.Parse(PageInt), int.Parse(CountRow));
                if (dt == null)
                {
                    context.Response.Write("{\"status\":\"-1\"}");
                    return;
                }
                string json = f.ToJson(dt);
                json = json.Replace("\"", "\\\"");
                context.Response.Write("{\"status\":\"" + json + "\"}");
            }
            else
            {
                context.Response.Write("{\"status\":\"-1\"}");
                return;
            }
        }

        /// <summary>
        /// 删除场景
        /// </summary>
        /// <param name="context"></param>
        public void DeleteSence(HttpContext context)
        {
            if (HttpContext.Current.Session["userID"] != null)
            {
                string id = HttpContext.Current.Request.Form["id"];
                sceneBll bll = new sceneBll();
                if (bll.DeleteSence(id))
                {
                    context.Response.Write("{\"status\":\"0\"}");
                    return;
                }
                else
                {
                    context.Response.Write("{\"status\":\"-1\"}");
                    return;
                }
            }
            else
            {
                context.Response.Write("{\"status\":\"-1\"}");
                return;
            }
        }

        /// <summary>
        /// 复制场景
        /// </summary>
        /// <param name="context"></param>
        public void CopeSence(HttpContext context)
        {
            if (HttpContext.Current.Session["userID"] != null)
            {
                sceneBll bll = new sceneBll();
                string id = HttpContext.Current.Request.Form["id"];
                if (bll.CopeSence(id, HttpContext.Current.Session["userID"].ToString()))
                {
                    context.Response.Write("{\"status\":\"0\"}");
                    return;
                }
                else
                {
                    context.Response.Write("{\"status\":\"-1\"}");
                    return;
                }
            }
            else
            {
                context.Response.Write("{\"status\":\"-1\"}");
                return;
            }

        }
        /// <summary>
        /// 赠送场景
        /// </summary>
        /// <param name="context"></param>
        public void GiveSence(HttpContext context)
        {
            if (HttpContext.Current.Session["userID"] != null)
            {
                sceneBll bll = new sceneBll();
                string id = HttpContext.Current.Request.Form["id"];
                string uName = HttpContext.Current.Request.Form["uName"];
                show_userBll ubll = new show_userBll();
                string uid = ubll.GetUidByuserName(uName);
                if (uid == "")
                {
                    context.Response.Write("{\"status\":\"-1\"}");
                    return;
                }
                if (bll.CopeSence(id, uid))
                {
                    context.Response.Write("{\"status\":\"0\"}");
                    return;
                }
                else
                {
                    context.Response.Write("{\"status\":\"-1\"}");
                    return;
                }
            }
            else
            {
                context.Response.Write("{\"status\":\"-1\"}");
                return;
            }
        }
        /// <summary>
        /// 用模板
        /// </summary>
        /// <param name="context"></param>
        public void UseTemp(HttpContext context)
        {
            if (HttpContext.Current.Session["userID"] != null)
            {
                string temp_code = HttpContext.Current.Request.Form["temp_code"];
                string userId = HttpContext.Current.Session["userID"].ToString();
                TempsBll bll = new TempsBll();
                string msg = bll.CopeTemp(temp_code, userId);
                context.Response.Write("{\"status\":\"" + msg + "\"}");
            }
            else
            {
                context.Response.Write("{\"status\":\"-1\"}");
                return;
            }
        }
        /// <summary>
        /// 获取用户拥有场景的数量
        /// </summary>
        /// <param name="context"></param>
        public void GetCount(HttpContext context)
        {
            if (HttpContext.Current.Session["userID"] != null)
            {
                sceneBll bll = new sceneBll();
                int count=bll.GetSenceCount(HttpContext.Current.Session["userID"].ToString());
                context.Response.Write("{\"status\":\""+ count + "\"}");
            }
            else
            {
                context.Response.Write("{\"status\":\"-1\"}");
                return;
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