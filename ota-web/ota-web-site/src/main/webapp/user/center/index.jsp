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
	<script type="text/javascript" src="${ctx}/resources/js/common.js"></script>
	<script type="text/javascript">
		$(function () {
			$('[data-toggle="tooltip"]').tooltip();
			$('#topSearchForm').parent().hide();
		})
	</script>
</head>
<body>
	<%@ include file="../../commons/header.jsp"%>
	<div class="main-box container">
		<!-- 面包屑 -->
		<ol class="breadcrumb">
		  	<li><a target="_self" href="${ctx}/">首页</a></li>
		  	<li class="active">会员中心</li>
		</ol>
		<!-- 会员列表左 -->
		<div class="list-group usr-menu col-md-2">
		  	<a href="${ctx}/user/sec/center/main" target="workFrame" class="list-group-item usr-menu-cur">会员中心<span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span></a>
		  	<a href="${ctx}/user/sec/order" target="workFrame" class="list-group-item">我的订单<span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span></a>
		  	<a href="${ctx}/user/center/info.jsp" target="workFrame" class="list-group-item">我的资料<span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span></a>
		  	<a href="${ctx}/user/center/pwd.jsp" target="workFrame" class="list-group-item">修改密码<span class="glyphicon glyphicon-menu-right" aria-hidden="true"></span></a>
		</div>
		<!-- 会员列表右 -->
		<div class="col-md-10">
			<iframe src="${ctx}/user/sec/center/main" marginheight="0" marginwidth="0" frameborder="0" onload="setIframeHeight('workFrame')" scrolling="no" width="100%" id="workFrame" name="workFrame"></iframe>
  		</div>
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
	<!-- 页尾 -->
	<%@ include file="../../commons/footer.jsp"%>
</body>    
</html>
