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

</head>
<body>
<div class="contain">
	<a href="${ctx}/" title="返回首页" class="login-logo"><img src="${picctx}/${company.LOGO}" title="${company.TITLE}" width="128"></a>
	<div class="sign-form">
		<p class="error-num">404</p>
		<p class="error-tit">ERROR</p>
		<p>很抱歉，</p>
		<p>你要访问的页面</p>
		<p>出现错误了...</p>
		<a href="${ctx}/" class="error-index">回到首页</a>
	</div>
</div>
<div class="fullscreen-bg-error">
	<img src="${ctx}/resources/images/full_page_background04.jpg" style="opacity: 0; height: auto; margin-left: 0px; visibility: visible; width: 1920px; margin-top: -285px;">
</div>
</body>
</html>
