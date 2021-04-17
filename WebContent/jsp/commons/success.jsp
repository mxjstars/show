<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<title>Success Page</title>
<%@ include file="../../../jsp/commons/taglibs.jsp"%>
<%@ include file="../../../jsp/commons/inc.jsp"%>
<script type="text/javascript">
	
	function close(){ 
		//window.opener.location.href = window.opener.location.href;
		//open(' ', '_self').close();
		window.opener = null;
		window.open(' ','_self');
		window.close();
	}
	
	var time = 5;
	function closeWindow(){
		
		window.setTimeout('closeWindow()',1000)
		
		if(time>0){
			document.getElementById("dao").innerHTML="倒计时<font color=red>"+time+"</font>秒后关闭当前窗口";
			time--;
		}else{
			window.opener = null;
			window.open(' ','_self');
			window.close();
		}
	}
	
</script>
</head>

<body onload="closeWindow();">

	<div id="content">
		<img alt="system internal error" src="${ctx}/jsp/images/success.gif" style="padding-top:30px;"/>
		<h3>
			${msg}<br />
		</h3>
		
		<a id="close" href="javascript:close()">点击关闭</a>
		<span id="dao"></span>
		
	</div>
</body>
</html>