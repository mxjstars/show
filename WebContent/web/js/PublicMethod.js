//公共方法

//调用服务器端方法
function RunFunInSrervice(parameter, ASHXurl)
{
    var pd = parameter;
    $.ajax({
        type: "post",
        url: ASHXurl,
        data: pd,
        dataType: "json",
        success: function (data) {
            return data;
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}
//获取url参数
function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}