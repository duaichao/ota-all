<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="${picctx}/${company.ICON}">
	<title>${company.TITLE}</title>
	<%@ include file="../commons/meta-css.jsp"%>
	<link rel="stylesheet" href="${ctx}/resources/css/login.css">
	<%@ include file="../commons/meta-js.jsp"%>
	<script type="text/javascript">
		$(function(){
		});
	</script>
					    
</head>
<body>
<div class="contain-reg">
	<a href="${ctx}/" target="_self" title="返回首页" class="login-logo"><img src="${picctx}/${company.LOGO}" title="${company.TITLE}" width="128"></a>
	<div class="reg-top">
		<a href="${ctx}/user/to/login" target="_self" target="_self" class="reg-topbtn">登录</a>
	</div>
	<div class="reg-form">
		<div class="reg-formtit">找回密码</div>
		<div class="reg-formcon">
			<form class="form-horizontal">
				<div class="progress">
					<div class="progress-bar progress-bar-success" style="width: 33%">
				    	<span>填写账号信息</span>
				    	<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
				  	</div>
				  	<div class="progress-bar progress-bar-success" style="width: 34%">
				    	<span>重置密码</span>
				    	<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
				  	</div>
				  	<div class="progress-bar progress-bar-success" style="width: 33%">
				    	<span>完成</span>
				    	<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
				  	</div>
				</div>
			  	<div class="alert alert-success" role="alert">
					<h1 class="pay-font">恭喜您，重置密码成功！</h1><br />
					<h3 class="pay-font">了解更多出游信息，请<a target="_self" href="${ctx}/">返回首页</a>继续浏览。</h3><br />
				</div>
			</form>
		</div>
	</div>
</div>
<div class="fullscreen-bg-reg">
	<img src="${ctx}/resources/images/full_page_background01.jpg" style="opacity: 0; height: auto; margin-left: 0px; visibility: visible; width: 1920px; margin-top: -285px;">
</div>
</body>    
</html>
