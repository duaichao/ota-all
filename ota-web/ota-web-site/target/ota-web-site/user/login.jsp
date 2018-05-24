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
	<script type="text/javascript" src="${ctx}/resources/js/RSA/RSA.min.js"></script>
	
	<script type="text/javascript">
	
		$(function(){
			var loginError = '${loginError}';
			if(loginError != ''){
				$('.toolbar-tip').show();
			}
			$('#loginForm').submit(function(){
				$('.toolbar-tip').hide();
				var USER_NAME = $.trim($('#USER_NAME').val());
				var PASSWORD = $.trim($('#PASSWORD').val());
				if(USER_NAME == '' || PASSWORD == ''){
					$('.toolbar-tip').show();
					return false;
				}	
				var key,result;
				setMaxDigits(130);
				key = new RSAKeyPair("${e}","","${m}");
				result = encryptedString(key, encodeURIComponent($('#PASSWORD').val()));
				$('#PASSWORD').val(result);
			});
		});
	</script>
	<c:if test="${USER_AGENT eq 'pc'}"><base target="_blank"></c:if>
</head>
<body>
<!-- 焦点图 -->
	
<div class="contain">
	<a href="${ctx}/" target="_self" title="返回首页" class="login-logo"><img src="${picctx}/${company.LOGO}" title="${company.TITLE}" width="128"></a>
	<div class="sign-form">
		<form action="${ctx}/user/login" method="post" id="loginForm" target="_self">
			<div class="toolbar-tip" style="display:none;">
		      	<div class="tooltip-arrow"></div>
		      	<div class="tooltip-inner toolbar-danger">
		      		<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>&nbsp;请输入正确的用户名和密码！					
		      	</div>
		    </div>
		  	<div class="form-group">
		    	<input type="text" class="form-control" name="pm[USER_NAME]" id="USER_NAME" placeholder="手机号码">
		  	</div>
		  	<div class="form-group">
		    	<input type="password" class="form-control" name="pm[PASSWORD]" id="PASSWORD" placeholder="密码">
		  	</div>
		  	<div class="form-group">
		  		<label>
			      <input type="checkbox" id="autoLogin" name="autoLogin"> 记住密码
			    </label>
			    <a target="_self" href="${ctx}/user/find_pwd.jsp" class="toolbar-link">忘记密码？</a>
		  	</div>
		  	<div class="form-group">
		  		<button type="submit" class="btn btn-sign btn-block">登录</button>
		  	</div>
		  	<div class="form-group">
		  		<div class="form-textright">还没有注册？<a target="_self" href="${ctx}/user/to/reg" class="toolbar-reg">立即注册</a></div>
		  	</div>
		</form>
	</div>
</div>
<div class="fullscreen-bg-login">
	<img src="${ctx}/resources/images/full_page_background02.jpg" style="opacity: 0; height: auto; margin-left: 0px; visibility: visible; width: 1920px; margin-top: -285px;">
</div>
</body>
</html>
