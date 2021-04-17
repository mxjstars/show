document.write("<script language='javascript' src='js/PublicMethod.js'></script>");//引入公共方法
var IndustryText = "";//行业所有文本
var IndustryTextBoool = false;//行业鼠标是否移出文字
var IndustryDivBool = false;//行业鼠标是否移出弹出的div
var interp = null;//行业文字计时器

var SenceText = "";//场景所有文本
var SenceTextBoool = false;//场景鼠标是否移出文字
var SenceDivBool = false;//场景鼠标是否移出弹出的div
var interc = null;//场景文字计时器

//获取行业title
function GetIndustry(Industry) {
    var pd = { "t": "1", "Gid": "2" };
    $.ajax({
        type: "post",
        url:"getHY_list.do",
        data: pd,
        dataType: "json",
        success: function (data) {
            if (data.status != "-1") {
                $(Industry).empty();
                IndustryText = data.tbCode;
                $.each(data.tbCode, function (i, item) {
                    if (i == 8) {
                        return false;
                    }
                    var title = item.msg;//显示文本
                    var values = item.codeId;//值
                    $(Industry).append('<p style="padding:0 7px;" value="' + values + '" onclick="IndustrySelect(this)">' + title + '</p>');
                });
                $('#hymore').show();
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}
//获取场景title
function GetSence(Sence) {
    var pd = { "t": "1", "Gid": "3" };
    $.ajax({
        type: "post",
        url:"getCJ_list.do",
        data: pd,
        dataType: "json",
        success: function (data) {
            if (data.status != "-1") {
                $(Sence).empty();
                SenceText = data.tbCode;
                $.each(data.tbCode, function (i, item) {
                    if (i == 8) {
                        return false;
                    }
                    var title = item.msg;//显示文本
                    var values = item.codeId;//值
                    $(Sence).append('<p style="padding:0 7px;" value="' + values + '" onclick="SenceSelect(this)">' + title + '</p>');
                });
                $('#cjmore').show();
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}


//行业鼠标移入文字
function IndustryOverp(IndustryThis) {
    IndustryTextBoool = true;
    $('#SenceMoreDiv').hide();
    var cur = $(IndustryThis);//当前对象
    var x = cur.position().top + 39;
    var y = cur.position().left - 90;
    var more = $(IndustryThis);//更多的文字

    if ($('#IndustryMoreDiv').length == 0) {
        var Context = '<section  id="IndustryMoreDiv" onMouseOver="IndustryOver()" onMouseOut="IndustryOut()" style="width:230px; position:absolute; left:' + parseInt(y) + 'px; top:' + parseInt(x) + 'px;  padding-top:10px; padding-bottom:10px; z-index:200; onverflow:hidden; text-overflow:clip; overflow:hidden;" class="bennTitleMore">';
        Context += '<img src="sysImg/xiaoqiu.png" style="top:4px; left:110px; position:absolute;" />';
        Context += '<div id="IndustryMoreDiv2" style="border:solid 1px #f39800; margin:0px auto; background-color:white;">';
        $.each(IndustryText, function (i, item) {
            var title = item.msg;//显示文本
            var values = item.code_id;//值
            Context += '<p value="' + values + '" onclick="IndustrySelect(this)">' + title + '</p>';
        });
        Context += "</div>";
        Context += '</section>';
        $('#moreIn').html(Context);

        var thead = $('#IndustryMoreDiv').height();
        $('#IndustryMoreDiv2').css("height", thead + "px");
        $('#IndustryMoreDiv').show();
        var css = '<style> #IndustryMoreDiv { animation:IndustryMoreDivkEY 1s 1; } @keyframes IndustryMoreDivkEY { from { height:0px; }  to { height:' + thead + 'px; } }</style>';
        $('head').append(css);


    }
    else {
        $('#IndustryMoreDiv').show();
    }
}
//行业鼠标移出文字
function IndustryOutp() {
    IndustryTextBoool = false;
    interp = window.setInterval(function () {
        if (IndustryTextBoool == false && IndustryDivBool == false) {
            var cur = $('#IndustryMoreDiv');//当前对象
            cur.hide();
            //cur.remove();
            //window.clearInterval(interp);
        }
    }, 100);
}
//行业鼠标移入div
function IndustryOver() {
    IndustryDivBool = true;
}
//行业鼠标移出弹出的DIv
function IndustryOut(e) {
    IndustryDivBool = false;
}


//场景鼠标移入文字
function SenceOverp(SenceThis) {
    SenceTextBoool = true;
    $('#IndustryMoreDiv').hide();
    var cur = $(SenceThis);//当前对象
    var x = cur.position().top + 39;
    var y = cur.position().left - 90;
    var more = $(SenceThis);//更多的文字
    if ($('#SenceMoreDiv').length == 0) {
        var Context = '<section id="SenceMoreDiv" onMouseOver="SenceOver()" onMouseOut="SenceOut()" style="width:230px; position:absolute; left:' + parseInt(y) + 'px; top:' + parseInt(x) + 'px;  padding-top:10px; padding-bottom:10px; z-index:100; onverflow:hidden; text-overflow:clip; overflow:hidden;" class="bennTitleMore">';
        Context += '<img src="sysImg/xiaoqiu.png" style="top:4px; left:110px; position:absolute;" />';
        Context += '<div id="SenceMoreDiv2" style="border:solid 1px #f39800; margin:0px auto; background-color:white;">';
        $.each(SenceText, function (i, item) {
            var title = item.msg;//显示文本
            var values = item.code_id;//值
            Context += '<p value="' + values + '" onclick="SenceSelect(this)">' + title + '</p>';
        });
        Context += "</div>";
        Context += '</section>';
        $('#moreCen').html(Context);

        var thead = $('#SenceMoreDiv').height();
        $('#SenceMoreDiv2').css("height", thead + "px");
        $('#SenceMoreDiv').show();
        var css = '<style> #SenceMoreDiv { animation:SenceMoreDivkEY 1s 1; } @keyframes SenceMoreDivkEY { from { height:0px; }  to { height:' + thead + 'px; } }</style>';
        $('head').append(css);
    }
    else {
        $('#SenceMoreDiv').show();
    }


}
//场景鼠标移出文字
function SenceOutp() {
    SenceTextBoool = false;
    interp = window.setInterval(function () {
        if (SenceTextBoool == false && SenceDivBool == false) {
            var cur = $('#SenceMoreDiv');//当前对象
            cur.hide();
            //cur.remove();
            //window.clearInterval(interp);
        }
    }, 100);
}
//场景鼠标移入div
function SenceOver() {
    SenceDivBool = true;
}
//场景鼠标移出弹出的DIv
function SenceOut() {
    SenceDivBool = false;
}


//行业title 点击事件
function IndustrySelect(InduThis) {
    var value = $(InduThis).attr('value');
    var listp = $('#listIndustryDiv p');
    listp.css('background-color', '');
    listp.css('color', '#666666');

    if ($(InduThis).parent().parent().parent().attr('id') != 'moreIn') {
        var widthsize = $(InduThis).css('width');//获取点击的宽度
        widthsize = parseInt(widthsize.replace('px', '')) + 10 + "px";
        var leftsize = $(InduThis).position().left +10;//获取左距离
        var fdc = $('#fdbg');//获取浮动层
        $('#fdbg').css('background-color', '#f39800');
        fdc.animate({
            left: leftsize + "px",
            width: widthsize,
        }, 300, function () {
            $(InduThis).css("color", "#fff");
        });
    }
    else {
        $('#fdbg').css('background-color', '');
        $(InduThis).css('background-color', '#f39800');
        $(InduThis).css("color", "#fff");
    }
    Hid = value;
    GetContext();
}

//场景title 点击事件
function SenceSelect(SenceThis) {
    var value = $(SenceThis).attr('value');
    var listp = $('#listSenceDiv p');
    listp.css('background-color', '');
    listp.css('color', '#666666');

    if ($(SenceThis).parent().parent().parent().attr('id') != 'moreCen') {
        var widthsize = $(SenceThis).css('width');//获取点击的宽度
        widthsize = parseInt(widthsize.replace('px', '')) + 10 + "px";
        var leftsize = $(SenceThis).position().left + 10;//获取左距离

        var fdc = $('#cjbg');//获取浮动层
        $('#cjbg').css('background-color', '#f39800');
        fdc.animate({
            left: leftsize + "px",
            width: widthsize,
        }, 300, function () {
            $(SenceThis).css("color", "#fff");
        });

    }
    else {
        $('#cjbg').css('background-color', '');
        $(SenceThis).css('background-color', '#f39800');
        $(SenceThis).css("color", "#fff");
    }
    Cid = value;
    GetContext();
}


//点击全部还是免费
function Fclick(btnThis, a) {
    $(btnThis).parent().children('div:eq(0)').css('background-color', '');
    $(btnThis).parent().children('div:eq(1)').css('background-color', '');
    $(btnThis).parent().children('div:eq(0)').css('color', '#f39800');
    $(btnThis).parent().children('div:eq(1)').css('color', '#f39800');

    var fdc = $('#ftag');//获取浮动层
    var widthsize = $(btnThis).css('width');//获取点击的宽度
    var leftsize = $(btnThis).position().left;

    fdc.animate({
        left: leftsize + "px",
        width: widthsize
    }, 200, function () {
        $(btnThis).css('color', 'white');
        if (a == 0) {
            fdc.css("border-radius", "6px 0px 0px 6px");
        }
        else {
            fdc.css("border-radius", "0px 6px 6px 0px");
        }
    });

    Ftype = a;
    GetContext();
}

//点击最新发布还是最受欢迎
function Newclick(btnThis, a) {
    $(btnThis).parent().children('div:eq(0)').css('background-color', '');
    $(btnThis).parent().children('div:eq(1)').css('background-color', '');
    $(btnThis).parent().children('div:eq(0)').css('color', '#f39800');
    $(btnThis).parent().children('div:eq(1)').css('color', '#f39800');

    var fdc = $('#ztag');//获取浮动层
    var widthsize = $(btnThis).css('width');//获取点击的宽度
    var leftsize = $(btnThis).position().left;
    fdc.animate({
        left: leftsize + "px",
        width: widthsize,
    }, 200, function () {
        $(btnThis).css('color', 'white');
        if (a == 0) {
            fdc.css("border-radius", "6px 0px 0px 6px");
        }
        else {
            fdc.css("border-radius", "0px 6px 6px 0px");
        }
    });

    newType = a;
    GetContext();
}

//查询
var Hid = '0';//行业id 0为全部
var Cid = '0';//场景id 0为全部
var Ftype = '0';//全部还是免费 全部为0 免费为1
var newType = '0';//0最受欢迎 1为最新发布
var PageInt = 1;//当前页数
var SerachStr = '';//查询信息

//获取下一页
function nextPage() {
    PageInt++;
    GetContext(1);
}
var GetCount = 0;
function serach()
{
    PageInt = 1;
    GetContext();
}
//获取页面详细信息
function GetContext(a) {
    var pd = { "t": "1", "H": Hid, "C": Cid, "orderByFree": Ftype, "OrderByNew": newType, "PageInt": PageInt, "CountRow": "24", 'SerachStr': SerachStr };
    $.ajax({
        type: "post",
        url:"getM_list.do",
        data: pd,
        dataType: "json",
        success: function (data) {
            if (data.status != "-1") {
                var ContentDiv = $('#ContentDiv');
                if (a != 1) {
                    ContentDiv.empty();
                    ContentDiv.append('<section class="tempDiv" onclick="ClickNone()">' +
                       '<section style="height:160px; position:relative;">' +
                           '<div style="border:solid 5px #8F9395; height:60px; width:0px; position:absolute; top:60px; left:90px;"></div>' +
                           '<div style="border:solid 5px #8F9395; width:50px; position:absolute; top:90px; left:65px;"></div>' +
                       '</section>' +
                       '<section style="text-align:center; font-size:18px; color:#66667c; font-weight:700;">' +
                           '&nbsp;空白模板' +
                       '</section>' +
                   '</section>');
                    GetCount = 0;
                }
               
                
                $.each(data.tsTemps, function (i, item) {
                    GetCount = +i;
                    var Html = '<section onclick="ClickTemp(\'' + item.tempCode + '\')" class="tempDiv" onMouseOver="tempMouseOver(this)" onMouseOut="tempMouseOut(this)">' +
                                    '<section style="height:176px; position:relative; margin-left:2px; margin-top:2px;">' +
                                        '<img src="http://'+ window.location.host+'/show' + item.cover + '" style="width:176px; height:176px; border-radius:5px 5px 0px 0px;">' +
                                        (parseInt(item.Money) > 0 ? '<div class="tempTopTitleQ"><font class="tempFont">钱</font></div>' : '<div class="tempTopTitle"><font class="tempFont">免</font></div>') +
                                    '</section>' +
                                    '<section style="font-size:15px; padding-left:10px; color:#666; padding-top:10px;">' +
                                        '<p >' + item.tempName + '</p>' +
                                        '<div><img src="sysImg/圆角三角形 浅灰.png" style="width:17px; height:17px; margin-top:10px;" /></div>' +
                                        '<div style="position:absolute; bottom:15px; left:35px; font-size:12px; color:#999;">' + item.mouseclick + '</div>' +
                                   '</section>' +
                                    '<div id="qr' + item.tempCode + '" style="display:none; position:absolute;top:0px;left:0px; z-index:4;">' +

                                    '</div>' +
                                '</section>';
                    ContentDiv.append(Html);

                    var qrcode = new QRCode(document.getElementById("qr" + item.tempCode), {
                        width: 180,//设置宽高
                        height: 180
                    });
                    qrcode.makeCode('http://' + window.location.host + '/show/web/senceCreate/view.html?c=view&id=' + item.senceCodeId + '&preview=preview');
                });
                if (GetCount < 23) {
                    $('#GetMore').hide();
                }
                else {
                    $('#GetMore').show();
                }
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

//点击模板页
function ClickTemp(temp_code) {
    //添加模板查看次数
    var pd = { "t": "2", "temp_code": temp_code };
    //RunFunInSrervice(pd, '../Tools/temp.ashx');

    EjectIdent('TempDetailed.html?temp_code=' + temp_code, '您要使用的场景', 800, 650);
}
//点击空白模板
function ClickNone() {
    //检查是否登录
    var pd = { "t": "1" };
    $.ajax({
        type: "post",
        url: "loginOnCheck.do",
        data: pd,
        dataType: "json",
        success: function (data) {
            if (data.status == "-1") {
                //打开登录
                window.parent.EjectIdent('LogIn.html', '欢迎光临，登录系统!', 500, 500);
            }
            else {
                EjectIdent('SelectHy.html', '新建场景', 500, 200);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

//返回顶部
function GetTop() {
    $('body,html').animate({ scrollTop: 0 }, 600);
    return false;
}
