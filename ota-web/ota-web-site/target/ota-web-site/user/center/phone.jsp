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
	<%@ include file="../../commons/meta-css.jsp"%>
	<link rel="stylesheet" href="${ctx}/resources/css/user.css">
	<%@ include file="../../commons/meta-js.jsp"%>
	<script type="text/javascript">
		var n = 120;
		$(function () {
		
			$('[data-toggle="tooltip"]').tooltip();
			/**
    		 * 验证码倒计时
    		 */
    		$('#sendBtn').click(function(){
    			var mobile = $('#mobile').val(), oldMobile = $('#oldMobile').val();
    			if(mobile != '' && mobile.length == 11 && oldMobile != '' && oldMobile.length == 11 && mobile != oldMobile){
    				$.ajax({  
						type: "post",
						url: "${ctx}/sms/send?mobile="+mobile+'&oldMobile='+oldMobile+'&type=change_phone_tip',
						dataType:"json",
						success: function(data, textStatus){
							var tip = ['短信已发送，请等待！','短信已发送，请等待！','发送失败，请检查你的手机号码！','发送失败，请检查你的手机号码！','发送失败，请检查你的手机号码！','原手机号码填写错误，请检查！']
							$('#tipContent').html(tip[0-data.statusCode]);
							$('#tipModal').modal('show');
						}
					});
				
	    			$(this).hide();
	    			$('#countBtn').show();
	    			c();
    			}else{
    				$('#tipContent').html('请填写格式正确的手机号码！');
					$('#tipModal').modal('show');
    			}
    		});
    		
    		$('#cpb').click(function(){
    			var mobile = $('#mobile').val(), oldMobile = $('#oldMobile').val(), code = $('#code').val();
    			if(mobile != '' && mobile.length == 11 && oldMobile != '' && oldMobile.length == 11 && mobile != oldMobile && code != '' && code.length == 6){
    				$.ajax({  
						type: "post",
						url: "${ctx}/user/sec/change/phone?mobile="+mobile+'&oldMobile='+oldMobile+'&type=change_phone_tip&code='+code,
						dataType:"json",
						success: function(data, textStatus){
							var tip = ['手机号码修改成功！','请填写格式正确的手机号码！','验证码填写错误！']
							
							$('#tipContent').html(tip[0-data.statusCode]);
							$('#tipModal').modal('show');
								
							$('#mobile').val('');
							$('#oldMobile').val('');
							$('#code').val('');
							
						}
					});
    			}else{
    				$('#tipContent').html('请填写格式正确的手机号码和验证码！');
					$('#tipModal').modal('show');
    			}
    		});
    		
		});
		
		function c(){
   			if(n == 0){
  					n = 120;
  					$('#sendBtn').show();
  					$('#countBtn').hide();
  					$('#countBtn').html('请等待'+n+'s');
  					return false;
  				}else{
  					$('#countBtn').html('请等待'+(n--)+'s');
  				}
   			setTimeout(function(){
   				c();
   			},1000);
   		}
	</script>
</head>
<body>
	<c:if test="${USER_AGENT eq 'mobile'}">
	<c:set var="header_title" value="修改手机" />
	<%@ include file="../../commons/header.jsp"%>
	</c:if>
	<div class="usr-box">
		<h2><span>修改手机</span></h2>
		<dl class="dl-horizontal">
		  	<dt>原手机号码：</dt>
		  	<dd>
		  		<div class="col-sm-4 col-xs-12 padding-leftnone">
		  			<input class="form-control input-sm" name="oldMobile" id="oldMobile" type="text">
		  		</div>
		  	</dd>
		  	<dt>现手机号码：</dt>
		  	<dd>
		  		<div class="col-sm-4 col-xs-12 padding-leftnone">
		  			<input class="form-control input-sm" name="mobile" id="mobile" type="text">
		  		</div>
		  	</dd>
		  	<dt>验证码：</dt>
		  	<dd class="form-group">
		  		<div class="col-sm-4 col-xs-12 padding-leftnone">
		  			<input class="form-control input-sm" name="code" id="code" type="text" placeholder="请输入验证码">
		  		</div>
		  		<div class="col-sm-2 col-xs-12 padding-leftnone">
			    	<a href="javascript:void(0);" target="_self" class="btn btn-default disabled" id="countBtn" style="display: none;" role="button">请等待120s</a>
			    	<a href="javascript:void(0);" target="_self" class="btn btn-success" style="display: block;" id="sendBtn" role="button">发送校验码</a>
			    </div>
		  	</dd>
		  	<dd>
		  		<div class="col-sm-2 col-xs-12 padding-leftnone">
		  			<button type="button" class="btn btn-sign btn-block" id="cpb">确认</button>
		  		</div>
		  	</dd>
		</dl>
	</div>
	<div id="tipModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h3 class="modal-title" id="myModalLabel">友情提示</h3>
				</div>
				<div class="modal-body">
					<h3 id="tipContent"></h3>
				</div>
			</div>
		</div>
	</div>
	<c:if test="${USER_AGENT eq 'mobile'}">
	<%@ include file="../../commons/m-user-footer.jsp"%>
	</c:if>
</body>    
</html>
