using Bll;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Text;
using System.Data;

namespace web.Tools
{
    /// <summary>
    /// _interface 的摘要说明
    /// </summary>
    public class _interface : IHttpHandler, System.Web.SessionState.IRequiresSessionState
    {
        //用户未登录 返回-1
        public void ProcessRequest(HttpContext context)
        {

            
            string t = HttpContext.Current.Request["t"];//干什么用的

            if (t == "preview")
            {
                //预览场景
                preview(context);
            }
            else
            {
                HttpContext.Current.Session.Timeout = 3000;
                if (HttpContext.Current.Session["userID"] == null)
                {
                    context.Response.Write("{\"status\":\"-1\"}");
                    return;
                }
                switch (t)
                {
                    case "pageList":
                        //获取场景页面基本信息
                        pageList(context);
                        break;
                    case "design":
                        //获取场景基本信息
                        design(context);
                        break;
                    case "savePage":
                        //保存页面
                        savePage(context);
                        break;
                    case "savePageName":
                        //保存页面名称
                        savePageName(context);
                        break;
                    case "pageSort":
                        //调整页面顺序
                        pageSort(context);
                        break;
                    case "createPage":
                        //创建页面
                        createPage(context);
                        break;
                    case "delPage":
                        //删除页面
                        delPage(context);
                        break;
                    case "copyPage":
                        //复制页面
                        copyPage(context);
                        break;
                    case "getFileList":
                        //获取文件列表
                        getFileList(context);
                        break;
                    case "delFile":
                        //删除文件
                        delFile(context);
                        break;
                    case "getSceneSetting":
                        //获取场景基本配置
                        getSceneSetting(context);
                        break;
                    case "publish":
                        //发布场景
                        publish(context);
                        break;
                    case "uploadCoverImg":
                        //设置封面
                        uploadCoverImg(context);
                        break;
                    case "typeList":
                        //获取类别
                        typeList(context);
                        break;
                }
            }
        }
        /// <summary>
        /// 整理json
        /// </summary>
        /// <param name="str"></param>
        public string MentStr(string str)
        {
            return str.Replace("'", "\"").Replace("\r\n", "");
        }

        /// <summary>
        /// 获取场景基本属性信息
        /// </summary>
        /// <param name="context"></param>
        public void pageList(HttpContext context)
        {
            //场景id
            string sceneCode = HttpContext.Current.Request["sceneCode"];
            sceneBll sbll = new sceneBll();
            List<Model.scene_pag> list = sbll.GetPageListBySceneCode(sceneCode);
            if (list.Count > 0)
            {
                //页面
                StringBuilder pagestr = new StringBuilder();
                foreach (Model.scene_pag m in list)
                {
                    pagestr.Append(@"{
                            'id': '" + m.scene_pag_id + @"',
                            'sceneId': '" + sceneCode + @"',
                            'num': '" + m.num + @"',
                            'name': '" + m.pageName + @"',
                            'properties': null,
                            'elements': null,
                            'scene': null
                        },");
                }
                //成功
                string sucStr = @"{
                            'success': true,
                            'code': '200',
                            'msg': 'success',
                            'obj': null,
                            'map': null,
                            'list': [
                                " + pagestr.ToString().Trim(',') + @"
                            ]
                        }
                    ";
                context.Response.Write(MentStr(sucStr));
            }
            else
            {
                //失败
                string failStr = @"{
                        'success': false,
                        'code': '403',
                        'msg': '基础数据服务器获取失败',
                        'obj': null,
                        'map': null,
                        'list': null
                    }";
                context.Response.Write(MentStr(failStr));
            }
        }

        /// <summary>
        /// 获取场景页面属性
        /// </summary>
        /// <param name="context"></param>
        public void design(HttpContext context)
        {
            //页面id
            string pageID = HttpContext.Current.Request["pageID"];
            sceneBll sbll = new sceneBll();
            Model.scene_pag mp = sbll.GetpageModel(pageID);
            if (mp != null)
            {
                //获取场景信息
                Model.scene mc = sbll.GetModel(mp.scene_code);
                string sucStr = @"{
                    'success': true, 
                    'code': '200', 
                    'msg': 'success', 
                    'obj': {
                        'id': '" + pageID + @"',
                        'sceneId': '" + mp.scene_code + @"',
                        'num': '" + mp.num + @"',
                        'name': '" + mp.pageName + @"',
                        'properties': null,
                        'elements': 
                           " + (mp.content_text == "" ? "[]" : mp.content_text) + @"
                        , 
                        'scene': {
                            'id': '" + mp.scene_code + @"',
                            'name': '" + mc.scene_name + @"',
                            'createUser': '" + mc.author + @"',
                            'createTime': '" + mc.addtime + @"',
                            'type': '" + mc.scene_typeid + @"',
                            'pageMode': '" + mc.movietype + @"',
                            'bgAudio':'" + mc.musicUrl + @"',
                            'isTpl': '0',
                            'isPromotion': '0',
                            'status': '1',
                            'openLimit': '0',
                            'submitLimit': '0',
                            'startDate': null,
                            'endDate': null,
                            'accessCode': null,
                            'thirdCode': null,
                            'updateTime': '1426038857000',
                            'publishTime': '1426038857000',
                            'applyTemplate': '0',
                            'applyPromotion': '0',
                            'sourceId': null,
                            'code': '" + mp.scene_code + @"',
                            'description': '" + mc.des + @"',
                            'sort': '0',
                            'pageCount': '0',
                            'dataCount': '0',
                            'showCount': '0',
                            'userLoginName': null,
                            'userName': null
                        }
        }, 
                    'map': null,
                    'list': null
                    }";
                context.Response.Write(MentStr(sucStr));
            }
            else
            {
                string failStr = @"{
                    'success': false,
                    'code': '403',
                    'msg': '页面基础数据服务器获取失败',
                    'obj': null,
                    'map': null,
                    'list': null
                }";
                context.Response.Write(MentStr(failStr));
            }
        }

        /// <summary>
        /// 保存页面信息
        /// </summary>
        /// <param name="context"></param>
        public void savePage(HttpContext context)
        {
            string msg = "";
            string id = HttpContext.Current.Request["id"];
            string content_text = HttpContext.Current.Request["elements"];
            string scene_code = HttpContext.Current.Request["sceneId"];
            string pageName = HttpContext.Current.Request["name"];
            string num = HttpContext.Current.Request["num"];
            string bgAudio = HttpContext.Current.Request["bgAudio"];

            sceneBll bll = new sceneBll();

            Model.scene_pag mp = new Model.scene_pag();
            mp.scene_pag_id = id;
            mp.content_text = content_text;
            mp.scene_code = scene_code;
            mp.pageName = pageName;
            mp.num = num;
            mp.bgAudio = bgAudio;
            if (bll.SavePage(mp))
            {
                msg = @"
                {
                    'success': true, 
                    'code': '200', 
                    'msg': 'success', 
                    'obj': null, 
                    'map': null, 
                    'list': null}";
            }
            else
            {
                msg = @"{
                    'success': false,
                    'code': '403',
                    'msg': '保存失败',
                    'obj': null,
                    'map': null,
                    'list': null
                }";
            }
            context.Response.Write(MentStr(msg));
        }

        /// <summary>
        /// 修改页面名称
        /// </summary>
        /// <param name="context"></param>
        public void savePageName(HttpContext context)
        {
            sceneBll bll = new sceneBll();
            string id = HttpContext.Current.Request["id"];
            string name = HttpContext.Current.Request["name"];
            if (bll.UpdatePageName(id, name))
            {
                string sucStr = @"{
                    'success': true, 
                    'code': '200', 
                    'msg': '操作成功', 
                    'obj': null, 
                    'map': null, 
                    'list': null
                }";
                context.Response.Write(MentStr(sucStr));
            }
            else
            {
                string failStr = @"{
                    'success': false,
                    'code': '403',
                    'msg': '页面名称保存失败',
                    'obj': null,
                    'map': null,
                    'list': null
                }";
                context.Response.Write(MentStr(failStr));
            }
        }

        /// <summary>
        /// 调整页面顺序
        /// </summary>
        /// <param name="context"></param>
        public void pageSort(HttpContext context)
        {
            string pageid = HttpContext.Current.Request["pageid"];
            string num = HttpContext.Current.Request["num"];
            sceneBll bll = new sceneBll();
            if (bll.UpdateSize(int.Parse(pageid), int.Parse(num)))
            {
                string sucStr = @" {
                    'success': true, 
                    'code': '200', 
                    'msg': '操作成功', 
                    'obj': null, 
                    'map': null, 
                    'list': null
                 }";
                context.Response.Write(MentStr(sucStr));
            }
            else
            {
                string failStr = @"  {
                    'success': false,
                    'code': '403',
                    'msg': '页面顺序调整失败',
                    'obj': null,
                    'map': null,
                    'list': null
                }";
                context.Response.Write(MentStr(failStr));
            }

        }

        /// <summary>
        /// 创建页面
        /// </summary>
        /// <param name="context"></param>
        public void createPage(HttpContext context)
        {
            string id = HttpContext.Current.Request["id"];
            sceneBll bll = new sceneBll();
            string sceneCode = bll.AddUpdate(id);//获取场景编码
            Model.scene sm = bll.GetModel(sceneCode.Split('|')[0]);//获取场景信息
            if (sceneCode != "")
            {
                string sucStr = @"{
                    'success': true,
                    'code': '200',
                    'msg': 'success',
                    'obj': {
                        'id': " + id + @",
                        'sceneId': '" + sceneCode.Split('|')[0] + @"',
                        'num': '" + sceneCode.Split('|')[1] + @"',
                        'name': null,
                        'properties': null,
                        'elements': null,
                        'scene': {
                            'id': '" + sceneCode.Split('|')[0] + @"',
                            'name': '" + sm.scene_name + @"',
                            'createUser': '" + sm.author + @"',
                            'createTime': '" + sm.addtime + @"',
                            'type': '" + sm.scene_typeid + @"',
                            'pageMode': '" + sm.movietype + @"',
                            'isTpl': '0',
                            'isPromotion': '0',
                            'status': '1',
                            'openLimit': '0',
                            'submitLimit': '0',
                            'startDate': null,
                            'endDate': null,
                            'accessCode': null,
                            'thirdCode': null,
                            'updateTime': '1426039827000',
                            'publishTime': '1426039827000',
                            'applyTemplate': '0',
                            'applyPromotion': '0',
                            'sourceId': null,
                            'code': 'U705UCE43R',
                            'description': '',
                            'sort': '0',
                            'pageCount': '0',
                            'dataCount': '0',
                            'showCount': '0',
                            'userLoginName': null,
                            'userName': null
                        }
                    },
                    'map': null,
                    'list': null,
                    'iscopy':'false'
                }";
                context.Response.Write(MentStr(sucStr));
            }
            else
            {
                string failStr = @" {
                    'success': false,
                    'code': '403',
                    'msg': '创建新页面失败',
                    'obj': null,
                    'map': null,
                    'list': null
                }";
                context.Response.Write(MentStr(failStr));
            }

        }

        /// <summary>
        /// 删除页面
        /// </summary>
        /// <param name="context"></param>
        public void delPage(HttpContext context)
        {
            string id = HttpContext.Current.Request["id"];
            sceneBll bll = new sceneBll();
            string sceneCode = bll.DeletePage(id);//获取场景编码
            if (sceneCode != "")
            {
                string msg = @"{
                'success': true, 
                'code': '200', 
                'msg': '删除成功', 
                'obj': null, 
                'map': null, 
                'list': null}";
                context.Response.Write(MentStr(msg));
            }
            else
            {
                string msg = @"{
                    'success': false,
                    'code': '403',
                    'msg': '创建新页面失败',
                    'obj': null,
                    'map': null,
                    'list': null
                }";
                context.Response.Write(MentStr(msg));
            }
        }

        /// <summary>
        /// 复制页面
        /// </summary>
        /// <param name="context"></param>
        public void copyPage(HttpContext context)
        {
            string id = HttpContext.Current.Request["id"];
            sceneBll bll = new sceneBll();
            string sceneCode = bll.CopyPage(id);
            Model.scene sm = bll.GetModel(sceneCode.Split('|')[0]);//获取场景信息
            if (sceneCode != "")
            {
                string msg = @"{
                    'success': true,
                    'code': '200',
                    'msg': 'success',
                    'obj': {
                        'id': '" + id + @"',
                        'sceneId': '" + sceneCode.Split('|')[0] + @"',
                        'num': '" + sceneCode.Split('|')[1] + @"',
                        'name': '" + sceneCode.Split('|')[2] + @"',
                        'properties': null,
                        'elements': null,
                        'scene': {
                            'id': '" + sceneCode.Split('|')[0] + @"',
                            'name': '" + sm.scene_name + @"',
                            'createUser': '" + sm.author + @"',
                           'createTime': '" + sm.addtime + @"',
                            'type': '" + sm.scene_typeid + @"',
                            'pageMode': '" + sm.movietype + @"',
                            'isTpl': '0',
                            'isPromotion': '0',
                            'status': '1',
                            'openLimit': '0',
                            'submitLimit': '0',
                            'startDate': null,
                            'endDate': null,
                            'accessCode': null,
                            'thirdCode': null,
                            'updateTime': '1426039827000',
                            'publishTime': '1426039827000',
                            'applyTemplate': '0',
                            'applyPromotion': '0',
                            'sourceId': null,
                            'code': 'U705UCE43R',
                            'description': '',
                            'sort': '0',
                            'pageCount': '0',
                            'dataCount': '0',
                            'showCount': '0',
                            'userLoginName': null,
                            'userName': null
                        }
                    },
                    'map': null,
                    'list': null,
                    'iscopy':'true'
                }";
                context.Response.Write(MentStr(msg));
            }
            else
            {
                string msg = @"{
                    'success': false,
                    'code': '403',
                    'msg': '创建新页面失败',
                    'obj': null,
                    'map': null,
                    'list': null
                }";
                context.Response.Write(MentStr(msg));
            }
        }

        /// <summary>
        /// 获取文件列表
        /// </summary>
        /// <param name="context"></param>
        /// <returns></returns>
        public void getFileList(HttpContext context)
        {
            string pageNo = HttpContext.Current.Request["pageNo"];
            string pageSize = HttpContext.Current.Request["pageSize"];
            string fileType = HttpContext.Current.Request["fileType"];
            string userId = HttpContext.Current.Session["userID"].ToString();
            sceneBll bll = new sceneBll();
            try
            {
                List<Model.file> list = bll.GetFileList(int.Parse(pageNo), int.Parse(pageSize), int.Parse(fileType), userId);
                StringBuilder lstr = new StringBuilder();
                foreach (Model.file m in list)
                {
                    lstr.Append(@"{
                            'id': '" + m.Fileid + @"',
                            'name': '" + m.fileUserName + @"',
                            'extName': '" + m.extName + @"',
                            'fileType': '" + m.fileType + @"',
                            'bizType': '101',
                            'path': '" + m.path + @"',
                            'tmbPath': '" + m.path + @"',
                            'createTime': '" + m.addtime + @"',
                            'createUser': '" + m.userid + @"',
                            'sort': '0',
                            'size': '26',
                            'status': '1'
                        },");

                }
                //count总数
                string msg = @"{
                    'success': true, 
                    'code': '200', 
                    'msg': 'success', 
                    'obj': null, 
                    'map': {
                        'count': '" + bll.GetFileCount(userId, int.Parse(fileType)).ToString() + @"',
                        'pageNo': '" + pageNo + @"', 
                        'pageSize': '" + pageSize + @"'
                    }, 
                    'list': [
                        " + lstr.ToString().Trim(',') + @" 
                    ]
    }";
                context.Response.Write(MentStr(msg));
            }
            catch (Exception ex)
            {
                string msg = @"{
                    'success': false,
                    'code': '403',
                    'msg': '获取文件失败',
                    'obj': null,
                    'map': null,
                    'list': null
                }";
                context.Response.Write(MentStr(msg));
            }
        }

        /// <summary>
        /// 删除文件（没有完成,文件删除）
        /// </summary>
        /// <param name="context"></param>
        public void delFile(HttpContext context)
        {
            string delid = "";
            string fileid = HttpContext.Current.Request["fileid"];
            string[] fileidlist = fileid.Split(',');
            foreach (string str in fileidlist)
            {
                delid += "'" + str + "',";
            }
            sceneBll bll = new sceneBll();
            if (bll.DeleteFile(delid.Trim(',')) != "")
            {
                string msg = @"{
                        'success': true, 
                        'code': '200', 
                        'msg': 'success', 
                        'obj': null, 
                        'map': null, 
                        'list': null
                    }
                ";
                context.Response.Write(MentStr(msg));
            }
            else
            {
                string msg = @"{
                    'success': false,
                    'code': '403',
                    'msg': '删除文件失败',
                    'obj': null,
                    'map': null,
                    'list': null
                }";
                context.Response.Write(MentStr(msg));
            }
        }

        /// <summary>
        /// 获取场景基本配置
        /// </summary>
        /// <param name="context"></param>
        public void getSceneSetting(HttpContext context)
        {
            string sceneId = HttpContext.Current.Request["sceneId"];
            sceneBll bll = new sceneBll();
            Model.scene m = bll.GetModel(sceneId);
            if (m != null)
            {
                string msg = @"{               
                    'success': true,            
		            'code': '200',            
                    'msg': 'success',            
                    'obj': {'id': '" + sceneId + @"', 
                            'name': '" + m.scene_name + @"', 
                            'createUser': '" + m.author + @"', 
                            'type': '" + m.scene_custom_id + @"', 
                            'createTime': '" + m.addtime + @"', 
                            'cover': '" + m.cover + @"', 
                             'code': '" + m.qrCode + @"', 
                             'description':'" + m.des + @"', 
                            'isTpl': '0', 
                            'isShow': '0', 
                            'updateTime': '" + m.addtime + @"',           
                            'publishTime': '" + m.addtime + @"'
                            },'map': null,
                             'list': null
                   }";
                context.Response.Write(MentStr(msg));
            }
            else
            {
                string msg = @"{
                        'success': false,
                        'code': '403',
                        'msg': '获取场景基本配置失败',
                        'obj': null,
                        'map': null,
                        'list': null
                    }
                        ";
                context.Response.Write(MentStr(msg));
            }
        }

        /// <summary>
        /// 发布场景
        /// </summary>
        /// <param name="context"></param>
        public void publish(HttpContext context)
        {
            string id = HttpContext.Current.Request["id"];
            string name = HttpContext.Current.Request["name"];
            string createUser = HttpContext.Current.Request["createUser"];
            string type = HttpContext.Current.Request["type"];
            string createTime = HttpContext.Current.Request["createTime"];
            string cover = HttpContext.Current.Request["cover"];
            string code = HttpContext.Current.Request["code"];
            string description = HttpContext.Current.Request["description"];
            sceneBll bll = new sceneBll();
            if (bll.publish(id, name, createUser, type, createTime, cover, code, description) != "")
            {
                string msg = @" {
                    'success': true, 
                    'code': 200, 
                    'msg': 'success', 
                    'obj': null, 
                    'map': null, 
                    'list': null
                }";
                context.Response.Write(MentStr(msg));
            }
            else
            {
                string msg = @" {
                    'success': false,
                    'code': 403,
                    'msg': '发布失败',
                    'obj': null,
                    'map': null,
                    'list': null
                }";
                context.Response.Write(MentStr(msg));
            }

        }

        /// <summary>
        /// 预览场景
        /// </summary>
        /// <param name="context"></param>
        public void preview(HttpContext context)
        {
            string id = HttpContext.Current.Request["id"];
            sceneBll bll = new sceneBll();
            Model.scene sm = bll.GetModel(id);//场景信息
            if (sm.author != null)
            {
                List<Model.scene_pag> mplist = bll.GetPageListBySceneCode(id);//场景页面
                StringBuilder pStr = new StringBuilder();
                foreach (Model.scene_pag m in mplist)
                {
                    pStr.Append(@"{'id': " + m.scene_pag_id + @", 
                    'sceneId': '" + id + @"',
                    'num': '" + m.num + @"',
                    'name': '" + m.pageName + @"',
                    'properties': null,
                    'elements': " + (m.content_text == "" ? "[]" : m.content_text) + @" , 
                    'scene': null},");
                }
                string msg = @"{
                'success': true, 
                'code': '200', 
                'msg': '操作成功', 
                'obj': { 
                    'id': '" + id + @"', 
                    'name': '" + sm.scene_name + @"', 
                    'createUser': '" + sm.author + @"', 
                    'type':'" + sm.scene_custom_id + @"', 
                    'pageMode': '" + sm.movietype + @"', 
                    'cover': '" + sm.cover + @"', 
                    'bgAudio':'" + sm.musicUrl + @"',
                    'code': '" + id + @"', 
                    'description': '" + sm.des + @"', 
                    'updateTime': '" + sm.addtime + @"', 
                    'createTime': '" + sm.addtime + @"', 
                    'publishTime': '" + sm.addtime + @"',
                    'property':{'triggerLoop':true,'eqAdType':1,'hideEqAd':false}
                }, 
                'map': null, 
                'list': [
                    " + pStr.ToString().Trim(',') + @"
                ]
                }";
                context.Response.Write(MentStr(msg));
            }
            else
            {
                TempsBll tbll = new TempsBll();
                Model.temp tmodel = tbll.GetModelTemp(id);//模板信息

                if (tmodel.author == null)
                {
                    string msg = @" {
                            'success': false,
                            'code': '403',
                            'msg': '预览失败',
                            'obj': null,
                            'map': null,
                            'list': null
                        }";
                    context.Response.Write(MentStr(msg));
                }
                else
                {
                    /////////////////////////////////////////////////////////
                    List<Model.temp_pag> mplist = tbll.GetPageListByTempCode(id);//场景页面
                    StringBuilder pStr = new StringBuilder();
                    foreach (Model.temp_pag m in mplist)
                    {
                        pStr.Append(@"{'id': " + m.pag_id + @", 
                                'sceneId': '" + id + @"',
                                'num': '" + m.num + @"',
                                'name': '" + m.pageName + @"',
                                'properties': null,
                                'elements': " + (m.content_text == "" ? "[]" : m.content_text) + @" , 
                                'scene': null},");
                    }
                    string msg = @"{
                            'success': true, 
                            'code': '200', 
                            'msg': '操作成功', 
                            'obj': { 
                                'id': '" + id + @"', 
                                'name': '" + tmodel.temp_name + @"', 
                                'createUser': '" + tmodel.author + @"', 
                                'type':'" + tmodel.scene_custom_id + @"', 
                                'pageMode': '" + tmodel.movietype + @"', 
                                'cover': '" + tmodel.cover + @"', 
                                'bgAudio':'" + tmodel.musicUrl + @"',
                                'code': '" + id + @"', 
                                'description': '" + tmodel.des + @"', 
                                'updateTime': '" + tmodel.addtime + @"', 
                                'createTime': '" + tmodel.addtime + @"', 
                                'publishTime': '" + tmodel.addtime + @"',
                                'property':{'triggerLoop':true,'eqAdType':1,'hideEqAd':false}
                            }, 
                            'map': null, 
                            'list': [
                                " + pStr.ToString().Trim(',') + @"
                            ]
                            }";
                    context.Response.Write(MentStr(msg));
                    /////////////////////////////////////////////////////////
                }
            }
        }

        /// <summary>
        /// 设置封面
        /// </summary>
        /// <param name="context"></param>
        public void uploadCoverImg(HttpContext context)
        {
            string src = HttpContext.Current.Request["src"];
            string fileType = HttpContext.Current.Request["fileType"];
            string x = HttpContext.Current.Request["x"];
            string y = HttpContext.Current.Request["y"];
            string w = HttpContext.Current.Request["w"];
            string h = HttpContext.Current.Request["h"];
            string id = HttpContext.Current.Request["id"];
            sceneBll bll = new sceneBll();
            if (bll.UpdateCover(src, fileType, int.Parse(x), int.Parse(y), int.Parse(w), int.Parse(h), id))
            {
                string msg = @"{
                 'success':true,
                  'code':200,
                  'msg':'操作成功',
                   'obj':'" + src + @"',
		    'map': {
                    'id':25467357,
				'path':'" + src + @"',
				'src':'" + src + @"',
				'y':'" + y + @"',
				'w':'" + w + @"',
				'h':'" + h + @"',
				'x':'" + x + @"',
				'index':'',
				'fileType':'" + fileType + @"'

                },
                     'list':null
}
            ";
                context.Response.Write(MentStr(msg));
            }
            else
            {
                string msg = @"{
                        'success': false, 
                        'code': 403, 
                        'msg': '上传缩略图失败', 
                        'obj': null, 
                        'map': null, 
                        'list': null
                    }
                ";
                context.Response.Write(MentStr(msg));
            }
        }

        /// <summary>
        /// 获取类别
        /// </summary>
        /// <param name="context"></param>
        public void typeList(HttpContext context)
        {
            CodeBll bll = new CodeBll();
            DataTable dt = bll.GetTableByGroup("2", 0);
            string l = "";
            if (dt != null)
            {
                foreach (DataRow dr in dt.Rows)
                {
                    l += "{'id':'" + dr["code_id"] + "','name':'" + dr["msg"] + "','value':'" + dr["code_id"] + "','type':'scene_type','sort':1,'status':1,'remark':null},";
                }
                string msg = @"{
                     'success':true,
                     'code':200,
                     'msg':'success',
                     'obj':null, 
                     'map':null, 
                      'list':[
                                " + l.ToString().Trim(',') + @"
                             ]
                }";
                context.Response.Write(MentStr(msg));
            }
            else
            {
                string msg = @"{
                        'success': false, 
                        'code': 403, 
                        'msg': '获取场景类型失败', 
                        'obj': null, 
                        'map': null, 
                        'list': null
                    }";
                context.Response.Write(MentStr(msg));
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