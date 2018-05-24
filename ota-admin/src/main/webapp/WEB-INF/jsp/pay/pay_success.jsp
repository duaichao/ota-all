<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>136旅游 支付完成提示</title>
		<link rel="shortcut icon" href="${ctx}/favicon.ico" />
		<link rel="stylesheet" type="text/css" href="${ctx }/resources/css/global.css" />
		<script type="text/javascript" src="${ctx }/resources/js/include-ext.js"></script>
		<script type="text/javascript">
			Ext.onReady(function(){
				var win = Ext.create('Ext.window.Window',{
					title:'<span style="font-size:16px;color:#fff"><i class="iconfont" style="position:relative;top:0px;font-size:15px;">&#xe71e;</i> 支付成功</span>',
					width:400,
					height:180,
					bodyPadding:30,
					html:[
						'<i class="iconfont green-color" style="font-size:30px;position:relative;top:5px;">&#xe6b0;</i> ',
						'<span class="f18">您的订单支付完成，成功支付${pay_money}元</span>',
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