//获取场景页面基本信息
function GetScenePageBase(sceneCode) {
    var pd = { "t": "GetScenePageBase", "sceneCode": sceneCode };
    $.ajax({
        type: "post",
        url: "Tools/interface.ashx",
        data: pd,
        dataType: "json",
        success: function (data) {
            //data为获取到的值
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}
//获取场景各个页面基本信息
function GetScenePageBase(pageID) {
    var pd = { "t": "GetSceneAttr", "pageID": pageID };
    $.ajax({
        type: "post",
        url: "Tools/interface.ashx",
        data: pd,
        dataType: "json",
        success: function (data) {
            //data为获取到的值
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}
//保存页面
function GetScenePageBase(elements, sceneId, name, num) {
    var pd = { "t": "SavePage", "elements": elements, "sceneId": sceneId, "name": name, "num": num };
    $.ajax({
        type: "post",
        url: "Tools/interface.ashx",
        data: pd,
        dataType: "json",
        success: function (data) {
            //data为获取到的值
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}