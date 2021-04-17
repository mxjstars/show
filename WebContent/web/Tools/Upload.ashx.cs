using Bll;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;
using web.Tools.XiuXiuPost;

namespace web.Tools
{
    /// <summary>
    /// Upload 的摘要说明
    /// </summary>
    public class Upload : IHttpHandler, System.Web.SessionState.IRequiresSessionState
    {

        public void ProcessRequest(HttpContext context)
        {
            if (HttpContext.Current.Session["userID"] == null)
            {
                context.Response.Write("{\"status\":\"-1\"}");
                return;
            }

            uploadFile(context);
        }
        /// <summary>
        /// 上传文件（没有完成,文件上传）
        /// </summary>
        public void uploadFile(HttpContext context)
        {
            string name = null;
            string userimgName = "";
            if (context.Request.Files.Count > 0)
            {
                XiuXiuPostImage img = new XiuXiuPostImage(context);
                name = img.Save();
                userimgName = img.FileUserName;
            }
            //   /upload/User201653198236.png
            string fileType = HttpContext.Current.Request["fileType"];//文件类型
            string[] strArr = name.Split('/');
            string fileName = strArr[strArr.Length - 1];
            string filePath = name;
            string userId = HttpContext.Current.Session["userID"].ToString();
            sceneBll bll = new sceneBll();
            string id = bll.AddFile(fileName, filePath, userId, fileType, userimgName);
            if (id != "")
            {
                string msg = @"{
                'success': true, 
                'code': 200, 
                'msg': 'success', 
                'obj': {
                    'id': '" + id + @"',
                    'name': '" + fileName + @"',
                    'extName': '" + fileName.Split('.')[1] + @"',
                    'fileType': " + fileType + @",
                    'path': '" + filePath + @"',
                    'tmbPath': '" + filePath + @"',
                    'createTime': '" + DateTime.Now.ToString() + @"',
                    'createUser': '" + userId + @"',
                    'bizType': 0, 
                    'sort': 0, 
                    'size': 2, 
                    'status': 1
                }, 
                'map': null, 
                'list': null}";
                context.Response.Write(MentStr(msg));
            }
            else
            {
                string msg = @"{
                    'success': false,
                    'code': 403,
                    'msg': '上传文件失败',
                    'obj': null,
                    'map': null,
                    'list': null
                }";
                context.Response.Write(msg);
            }
        }

        public bool IsReusable
        {
            get
            {
                return false;
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

    }
    namespace XiuXiuPost
    {

        /// <summary>
        /// 上传抽象类
        /// </summary>
        public abstract class XiuXiuImage
        {
            /// <summary>
            /// 基类保存
            /// </summary>
            /// <returns>返回保存路径+文件名</returns>
            public virtual string Save()
            {
                string fileName = this.GetFileName();
                if (null == fileName) return null;

                string root = HttpContext.Current.Server.MapPath(path);

                if (!Directory.Exists(root))
                {
                    Directory.CreateDirectory(root);
                }

                this.FileName = Path.Combine(root, fileName);
                string[] paths = { path, fileName };
                //保存方法


                return string.Join("/", paths);
            }



            public XiuXiuImage()
            {
                path = path == null ? "/upload" : path;
            }

            /// <summary>
            /// 确定上传类型
            /// </summary>
            protected bool IsUplodType
            {
                get
                {
                    string extension = this.GetExtension();
                    return ".jpg .gif .png .mp3".IndexOf(extension) >= 0 ? true : false;
                }
            }

            private string _fileName = null;
            /// <summary>
            /// 最终保存路径
            /// </summary>
            protected string FileName
            {
                set { _fileName = value; }
                get { return _fileName; }
            }
            //用户上传的文件名
            public string FileUserName { get; set; }

            /// <summary>
            /// 配置文件路径 无配置上传到XiuXiuUpload
            /// </summary>
            protected string path = null;//ConfigurationManager.AppSettings["XiuXiuImageSavePath"];

            /// <summary>
            /// 获取拓展名
            /// </summary>
            /// <returns></returns>
            protected abstract string GetExtension();

            /// <summary>
            /// 获取最终保存文件名
            /// </summary>
            /// <returns></returns>
            protected string GetFileName()
            {
                string extension = this.GetExtension();

                if (null == extension) return null;
                else
                {
                    string name = this.GetName();
                    string[] imgName = { "User", name, extension };
                    return string.Join("", imgName);
                }
            }
            /// <summary>
            /// 获取保存文件名
            /// </summary>
            /// <returns></returns>
            private string GetName()
            {
                DateTime uploadTime = DateTime.Now;
                string[] times = { uploadTime.Year.ToString(), uploadTime.Month.ToString(), uploadTime.Day.ToString(),
                                 uploadTime.Hour.ToString(), uploadTime.Millisecond.ToString(), uploadTime.Second.ToString() };
                return string.Join("", times);
            }
        }
        /// <summary>
        /// POST接收
        /// </summary>
        public sealed class XiuXiuPostImage : XiuXiuImage
        {
            private HttpFileCollection xiuxiuFiles = null;
            HttpContext contextBase = null;
            public XiuXiuPostImage(HttpContext context)
            {
                this.xiuxiuFiles = context.Request.Files;
                contextBase = context;
            }
            /// <summary>
            /// 上传文件个数
            /// </summary>
            public int Count
            {
                get { return xiuxiuFiles.Count; }
            }
            /// <summary>
            /// 保存图片,成功返回文件路径,失败null
            /// 非图片格式返回错误信息
            /// </summary>
            /// <returns></returns>
            public override string Save()
            {
                if (!this.IsUplodType)
                {
                    return "Only allowed to upload pictures.";
                }
                string returnName = base.Save();
                if (this.FileName != null)
                {
                    this.File.SaveAs(this.FileName);
                    return returnName;
                }
                return null;
            }

            private HttpPostedFile File
            {
                get { return this.Count >= 1 ? xiuxiuFiles[0] : null; }
            }

            protected override string GetExtension()
            {
                this.FileUserName = this.File.FileName;
                return null == this.File ? null : Path.GetExtension(this.File.FileName);
            }


        }
    }
}