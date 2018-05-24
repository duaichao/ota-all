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
			
			$('#findPwdForm').submit(function(){
				var password = $('#password').val();
				if(password == '' || password.length < 6 || password.length > 20){
					$('#tipModal').modal('show');
					return false;					
				}
			});
			
			$('#showPwd').click(function(){
				var t = $('#password').attr('type');
				if(t == 'password'){
					$('#password').attr('type', 'text');
				}else{
					$('#password').attr('type', 'password');
				}
			});	 
			
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
			<form class="form-horizontal" action="${ctx}/user/edit/pwd" method="post" id="findPwdForm">
				<div class="progress">
					<div class="progress-bar progress-bar-success" style="width: 33%">
				    	<span>填写账号信息</span>
				    	<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
				  	</div>
				  	<div class="progress-bar progress-bar-success" style="width: 34%">
				    	<span>重置密码</span>
				    	<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
				  	</div>
				  	<div class="progress-bar progress-bar-default" style="width: 33%">
				    	<span>完成</span>
				    	<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
				  	</div>
				</div>
			  	<div class="form-group">
				    <label class="col-md-4 control-label" for="formGroupInput">登录密码：</label>
				    <div class="input-group col-md-4" style="padding-left:15px; padding-right:15px;" id="showPwd">
				      	<input class="form-control" type="password" name="pm[PASSWORD]" id="password" maxlength="20" placeholder="6-20位字母、数字和符号">
				      	<span class="input-group-addon"><a target="_self" href="javascript:void(0);" style="color:#333;" title="显示密码"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></a></span>
				    </div>
			  	</div>
			  	<div class="form-group">
				    <label class="col-md-4 control-label" for="formGroupInput"></label>
				    <div class="col-md-4">
				    	<input name="pm[CODE]" type="hidden" id="code" value="${param.CODE}" />
				    	<input name="pm[MOBILE]" type="hidden" id="mobile" value="${param.MOBILE}" />
				    	<input name="pm[ID]" type="hidden" id="id" value="${param.ID}" />
				    	<button type="submit" class="btn btn-sign btn-block">下一步</button>
				    </div>
				</div>
			</form>
		</div>
	</div>
</div>
<div class="fullscreen-bg-reg">
	<img src="${ctx}/resources/images/full_page_background01.jpg" style="opacity: 0; height: auto; margin-left: 0px; visibility: visible; width: 1920px; margin-top: -285px;">
</div>
<div id="tipModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h3 class="modal-title" id="myModalLabel">友情提示</h3>
			</div>
			<div class="modal-body">
				<h3 id="tipContent">密码是6-20位字母、数字和符号</h3>
			</div>
		</div>
	</div>
</div>
</body>    
</html>
