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
			
			/**
    		 * 验证码倒计时
    		 */
    		 var n = 120;
    		$('#sendBtn').click(function(){
    			var mobile = $('#mobile').val();
    			if(mobile != ''){
    				
    				
    				$.ajax({  
						type: "post",
						url: "${ctx}/sms/send?mobile="+mobile+'&type=find_pwd_tip',
						dataType:"json",
						success: function(data, textStatus){
							var tip = ['短信已发送，请等待！','短信已发送，请等待！','发送失败，请检查你的手机号码！','发送失败，请检查你的手机号码！','手机号码已被注册', '手机号码不存在']
							if(!data.success){
								$('#tipContent').html(tip[0-data.statusCode]);
								$('#tipModal').modal('show');
								
							}
						}
					});
					
					
	    			$(this).hide();
	    			$('#countBtn').show();
	    			c();
    			}
    		});
    		
    		function c(){
    			if(n == 0){
   					n = 120;
   					$('#sendBtn').show();
   					$('#countBtn').hide();
   					$('#countBtn').html(n+'s后可重新获取');
   					return false;
   				}else{
   					$('#countBtn').html((n--)+'s后可重新获取');
   				}
    			setTimeout(function(){
    				c();
    			},1000);
    		}
			
			$('#findPwdForm').submit(function(){
				var mobile = $('#mobile').val(), code = $('#code').val();
				if(mobile == '' || code == ''){
					return false;					
				}
			});	
			
		});
	</script>
					    
</head>
<body>
<div class="contain-reg">
	<a href="${ctx}/" title="返回首页" target="_self" class="login-logo"><img src="${picctx}/${company.LOGO}" title="${company.TITLE}" width="128"></a>
	<div class="reg-top">
		<a target="_self" href="${ctx}/user/to/login" target="_self" class="reg-topbtn">登录</a>
	</div>
	<div class="reg-form">
		<div class="reg-formtit">找回密码</div>
		<div class="reg-formcon">
			<form class="form-horizontal" method="post" action="${ctx}/user/find/pwd" id="findPwdForm">
				<div class="progress">
					<div class="progress-bar progress-bar-success" style="width: 33%">
				    	<span>填写账号信息</span>
				    	<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
				  	</div>
				  	<div class="progress-bar progress-bar-default" style="width: 34%">
				    	<span>重置密码</span>
				    	<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
				  	</div>
				  	<div class="progress-bar progress-bar-default" style="width: 33%">
				    	<span>完成</span>
				    	<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
				  	</div>
				</div>
			  	<div class="form-group">
				    <label class="col-md-4 control-label" for="formGroupInput">手机号码：</label>
				    <div class="col-md-4">
				      	<input class="form-control" type="text" id="mobile" name="pm[MOBILE]" placeholder="请输入手机号码">
				    </div>
			  	</div>
			  	<div class="form-group">
				    <label class="col-md-4 control-label" for="formGroupInput">校验码：</label>
				    <div class="col-md-2">
				      	<input class="form-control" type="text" id="code" name="pm[CODE]" maxlength="6" placeholder="请输入校验码">
				    </div>
				    <div class="col-md-2">
				    	<a href="javascript:void(0)" target="_self" class="btn btn-default disabled" id="countBtn" style="display: none;"role="button">120s后可重新获取</a>
				    	<a href="javascript:void(0)" target="_self" class="btn btn-success" id="sendBtn" role="button">重新发送校验码</a>
				    </div>
				    <!--<div class="col-md-2 mt7 tips-clr">
				    	<span class="glyphicon glyphicon-remove-sign" aria-hidden="true"></span>&nbsp;请输入正确的校验码
				    </div>-->
			  	</div>
			  	<div class="form-group">
				    <label class="col-md-4 control-label" for="formGroupInput"></label>
				    <div class="col-md-4">
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
				<h3 id="tipContent"></h3>
			</div>
		</div>
	</div>
</div>
</body>    
</html>
