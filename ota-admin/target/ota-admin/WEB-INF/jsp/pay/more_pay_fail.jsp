<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>136旅游 多次支付失败提示</title>
		<link rel="shortcut icon" href="${ctx}/favicon.ico" />
		<link rel="stylesheet" type="text/css" href="${ctx }/resources/css/global.css" />
		<script type="text/javascript" src="${ctx }/resources/js/include-ext.js"></script>
		<script type="text/javascript">
			function closeme(){ var browserName=navigator.appName; if (browserName=="Netscape") { window.open('','_parent',''); window.close(); } else if (browserName=="Microsoft Internet Explorer") { window.opener = "whocares"; window.close(); } }
			Ext.onReady(function(){
				var win = Ext.create('Ext.window.Window',{
					title:'<span style="font-size:16px;color:#fff"><i class="iconfont" style="position:relative;top:0px;font-size:15px;">&#xe6ae;</i> 多次支付失败提示</span>',
					width:360,
					height:180,
					bodyPadding:30,
					modal:true,
					draggable:false,
					resizable:false,
					maximizable:false,
					html:[
						'<i class="iconfont red-color" style="font-size:30px;relative;top:5px;">&#xe6ae;</i> ',
						'<span class="f18">订单不可多次支付！</span>',
						'<p style="color:#999;padding-top:10px;padding-left:33px;">如页面未自动关闭请手动关闭</p>'
					].join(''),
					buttons:[{
						text:'关闭',
						handler:function(){
							win.close();
						}
					}],
					listeners:{
						'beforeclose':function(){
							var userAgent = navigator.userAgent;
							if (userAgent.indexOf("Firefox") != -1 || userAgent.indexOf("Chrome") !=-1) {
							   window.location.href="about:blank";
							} else {
							   window.opener = null;
							   window.open("", "_self");
							   window.close();
							};
						}
					}
				}).show();
			});
		</script>
	</head>
	<body>
	</body>
</html>