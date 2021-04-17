using System;
using System.Collections;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Net.Mail;
using System.Reflection;
using System.Text;
using System.Text.RegularExpressions;
using System.Web;
using System.Web.Script.Serialization;

namespace web.Tools
{
    public static class f
    {
        public static string ToJson(this DataTable dt)
        {
            JavaScriptSerializer javaScriptSerializer = new JavaScriptSerializer();

            javaScriptSerializer.MaxJsonLength = Int32.MaxValue; //取得最大数值
            ArrayList arrayList = new ArrayList();
            foreach (DataRow dataRow in dt.Rows)
            {
                Dictionary<string, object> dictionary = new Dictionary<string, object>();  //实例化一个参数集合
                foreach (DataColumn dataColumn in dt.Columns)
                {
                    dictionary.Add(dataColumn.ColumnName, dataRow[dataColumn.ColumnName].ToString());
                }
                arrayList.Add(dictionary); //ArrayList集合中添加键值
            }

            return "{root:" + javaScriptSerializer.Serialize(arrayList) + "}";  //返回一个json字符串
        }

        /// <summary>     
        /// 过滤xss攻击脚本     
        /// </summary>     
        /// <param name="input">传入字符串</param>     
        /// <returns>过滤后的字符串</returns>     
        public static string FilterXSS(string html)
        {
            if (string.IsNullOrEmpty(html)) return string.Empty;
            // CR(0a) ，LF(0b) ，TAB(9) 除外，过滤掉所有的不打印出来字符.     
            // 目的防止这样形式的入侵 ＜java\0script＞     
            // 注意：\n, \r,  \t 可能需要单独处理，因为可能会要用到     
            string ret = System.Text.RegularExpressions.Regex.Replace(
                html, "([\x00-\x08][\x0b-\x0c][\x0e-\x20])", string.Empty);
            //替换所有可能的16进制构建的恶意代码     
            //<IMG SRC=&#X40&#X61&#X76&#X61&#X73&#X63&#X72&#X69&#X70&#X74
            //&#X3A&#X61&_#X6C&#X65&#X72&#X74&#X28&#X27&#X58&#X53&#X53&#X27&#X29>     
            string chars = "abcdefghijklmnopqrstuvwxyz" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890" +
                        "!@#$%^&*()~`;:?+/={}[]-_|'\"\\";
            for (int i = 0; i < chars.Length; i++)
            {
                ret =
                    System.Text.RegularExpressions.Regex.Replace(ret,
                        string.Concat("(&#[x|X]0{0,}",
                            Convert.ToString((int)chars[i], 16).ToLower(),
                            ";?)"),
                        chars[i].ToString(),
                        System.Text.RegularExpressions.RegexOptions.IgnoreCase);
            }
            //过滤\t, \n, \r构建的恶意代码   
            string[] keywords = {"javascript", "vbscript", "expression",
                "applet", "meta", "xml", "blink", "link", "style",
               "script", "embed", "object", "iframe", "frame",
               "frameset", "ilayer", "layer", "bgsound", "title",
                "base" ,"onabort", "onactivate", "onafterprint",
                "onafterupdate", "onbeforeactivate", "onbeforecopy",
                "onbeforecut", "onbeforedeactivate", "onbeforeeditfocus",
                "onbeforepaste", "onbeforeprint", "onbeforeunload",
                "onbeforeupdate", "onblur", "onbounce", "oncellchange",
                "onchange", "onclick", "oncontextmenu", "oncontrolselect",
                "oncopy", "oncut", "ondataavailable", "ondatasetchanged",
                "ondatasetcomplete", "ondblclick", "ondeactivate",
                "ondrag", "ondragend", "ondragenter", "ondragleave",
                "ondragover", "ondragstart", "ondrop", "onerror",
                "onerrorupdate", "onfilterchange", "onfinish",
                "onfocus", "onfocusin", "onfocusout", "onhelp",
                "onkeydown", "onkeypress", "onkeyup", "onlayoutcomplete",
                "onload", "onlosecapture", "onmousedown", "onmouseenter",
                "onmouseleave", "onmousemove", "onmouseout", "onmouseover",
                "onmouseup", "onmousewheel", "onmove", "onmoveend",
                "onmovestart", "onpaste", "onpropertychange",
                "onreadystatechange", "onreset", "onresize",
                "onresizeend", "onresizestart", "onrowenter",
                "onrowexit", "onrowsdelete", "onrowsinserted",
                "onscroll", "onselect", "onselectionchange",
                "onselectstart", "onstart", "onstop", "onsubmit",
                "onunload"};
            bool found = true;
            while (found)
            {
                var retBefore = ret;
                for (int i = 0; i < keywords.Length; i++)
                {
                    string pattern = "/";
                    for (int j = 0; j < keywords[i].Length; j++)
                    {
                        if (j > 0)
                            pattern = string.Concat(pattern,
                                '(', "(&#[x|X]0{0,8}([9][a][b]);?)?",
                                "|(&#0{0,8}([9][10][13]);?)?",
                                ")?");
                        pattern = string.Concat(pattern, keywords[i][j]);
                    }
                    string replacement =
                        string.Concat(keywords[i].Substring(0, 2),
                            "＜x＞", keywords[i].Substring(2));
                    ret =
                        System.Text.RegularExpressions.Regex.Replace(ret,
                            pattern, replacement,
                            System.Text.RegularExpressions.RegexOptions.IgnoreCase);
                    if (ret == retBefore)
                        found = false;
                }
            }
            return ret;
        }

        /// <summary>
        /// 过滤sql注入
        /// </summary>
        /// <param name="inputString"></param>
        /// <returns></returns>
        public static string MyEncodeInputString(string inputString)
        {
            //要替换的敏感字
            string SqlStr = @"and|or|exec|execute|insert|select|delete|update|alter|create|drop|count|\*|chr|char|asc|mid|substring|master|truncate|declare|xp_cmdshell|restore|backup|net +user|net +localgroup +administrators";
            try
            {
                if ((inputString != null) && (inputString != String.Empty))
                {
                    string str_Regex = @"\b(" + SqlStr + @")\b";

                    Regex Regex = new Regex(str_Regex, RegexOptions.IgnoreCase);
                    //string s = Regex.Match(inputString).Value; 
                    MatchCollection matches = Regex.Matches(inputString);
                    for (int i = 0; i < matches.Count; i++)
                        inputString = inputString.Replace(matches[i].Value, "[" + matches[i].Value + "]");
                }
            }
            catch
            {
                return "";
            }
            return inputString;

        }


        /// <summary>
        /// 发送邮件
        /// </summary>
        /// <param name="userEmail"></param>
        /// <param name="code"></param>
        /// <param name="t">t==0 注册验证码 1重置密码 </param>
        public static void SendEmail(string userEmail, string code, int t)
        {
            //0  checkEmail.aspx
            //1  PwdEmail.aspx
            string url = HttpContext.Current.Request.Url.Host;
            if (t == 0)
            {
                SendEmail(userEmail, "SHOW注册验证", url + "/checkEmail.aspx?code=" + code + "&un=" + userEmail);
            }
            else
            {
                SendEmail(userEmail, "SHOW找回密码", url + "/PwdEmail.aspx?code=" + code + "&un=" + userEmail);
            }
        }
        /// <summary>
        /// 发送邮件
        /// </summary>
        /// <param name="mailTo">要发送的邮箱</param>
        /// <param name="mailSubject">邮箱主题</param>
        /// <param name="mailContent">邮箱内容</param>
        /// <returns>返回发送邮箱的结果</returns>
        public static bool SendEmail(string mailTo, string mailSubject, string mailContent)
        {
            // 设置发送方的邮件信息,例如使用网易的smtp
            string smtpServer = "smtp.163.com"; //SMTP服务器
            string mailFrom = "youanmingzhu@163.com"; //登陆用户名
            string userPassword = "19891122";//登陆密码

            // 邮件服务设置
            SmtpClient smtpClient = new SmtpClient();
            smtpClient.DeliveryMethod = SmtpDeliveryMethod.Network;//指定电子邮件发送方式
            smtpClient.Host = smtpServer; //指定SMTP服务器
            smtpClient.Credentials = new System.Net.NetworkCredential(mailFrom, userPassword);//用户名和密码

            // 发送邮件设置        
            MailMessage mailMessage = new MailMessage(mailFrom, mailTo); // 发送人和收件人
            mailMessage.Subject = mailSubject;//主题
            mailMessage.Body = mailContent;//内容
            mailMessage.BodyEncoding = Encoding.UTF8;//正文编码
            mailMessage.IsBodyHtml = true;//设置为HTML格式
            mailMessage.Priority = MailPriority.Low;//优先级

            try
            {
                smtpClient.Send(mailMessage); // 发送邮件
                return true;
            }
            catch (SmtpException ex)
            {
                return false;
            }
        }
    }
}