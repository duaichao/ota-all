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
		$(function () {
		
			$('[data-toggle="tooltip"]').tooltip();
			
			$('.showPwd').click(function(t){
				var t = $(this).prev();
				if(t.attr('type') == 'password'){
					t.attr('type', 'text');
				}else{
					t.attr('type', 'password');
				}
			});	
			
			$('#updatePwd').click(function(){
				var oldPwd = $('#oldPwd').val(), newPwd = $('#newPwd').val(); 
				if(oldPwd == '' || oldPwd.length < 6 || oldPwd.length > 20 || newPwd == '' || newPwd.length < 6 || newPwd.length > 20){
					$('#tipContent').html('请输入正确的原密码和新密码');
					$('#tipModal').modal('show');
					return false;
				}
				
				$.ajax({  
					type: "post",
					url: "${ctx}/user/sec/update/pwd?oldPwd="+oldPwd+'&newPwd='+newPwd,
					dataType:"json",
					success: function(data, textStatus){
					
						var tip = ['密码修改成功', '原密码错误', '密码格式不正确'];
						$('#tipContent').html(tip[0-data.statusCode]);
						$('#tipModal').modal('show');
						
// 						$(parent.document).find('#tipContent').html(tip[0-data.statusCode]);
// 						$(parent.document).find('#tipModal').modal('show');

						$('#oldPwd').val('');
						$('#newPwd').val(''); 
					}
				});
			});
		})
	</script>
</head>
<body>
	<c:if test="${USER_AGENT eq 'mobile'}">
	<c:set var="header_title" value="修改密码" />
	<%@ include file="../../commons/header.jsp"%>
	</c:if>
	<div class="usr-box">
		<h2><span>修改密码</span></h2>
		<dl class="dl-horizontal">
		  	<dt>当前密码：</dt>
		  	<dd>
		  		<div class="col-sm-4 col-xs-12 padding-leftnone input-group">
		  			<input class="form-control input-sm" name="oldPwd" id="oldPwd" type="password" placeholder="6-20位字母、数字和符号">
		  			<span class="input-group-addon showPwd"><a href="javascript:void(0);" target="_self" style="color:#333;" title="显示密码"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></a></span>
		  		</div>
		  	</dd>
		  	<dt>新密码：</dt>
		  	<dd>
		  		<div class="col-sm-4 col-xs-12 padding-leftnone input-group">
		  			<input class="form-control input-sm" name="newPwd" id="newPwd" type="password" placeholder="6-20位字母、数字和符号">
		  			<span class="input-group-addon showPwd"><a href="javascript:void(0);" target="_self" style="color:#333;" title="显示密码"><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span></a></span>
		  		</div>
		  	</dd>
		  	<dd>
		  		<div class="col-sm-2 col-xs-12 padding-leftnone padding-rightnone" >
		  			<a href="javascript:void(0);" target="_self" id="updatePwd" class="btn btn-sign btn-block" data-toggle="modal">保存</a>
		  		</div>
		  	</dd>
		</dl>
	</div>
	<!-- 弹出窗口 -->
	<div class="modal fade" id="tipModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h3 class="modal-title" id="myModalLabel">提示</h3>
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
