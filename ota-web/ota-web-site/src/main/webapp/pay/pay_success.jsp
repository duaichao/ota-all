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
    
    <c:if test="${USER_AGENT eq 'pc'}">
    <link rel="icon" href="${picctx}/${company.ICON}">
	<link rel="stylesheet" href="${ctx}/resources/css/bootstrap.min.css">
	<link rel="stylesheet" href="${ctx}/resources/css/reset.css">
	<link rel="stylesheet" href="${ctx}/resources/css/common.css">
	<link rel="stylesheet" href="${ctx}/resources/css/yuding.css">
	<script type="text/javascript" src="${ctx}/resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/toolbar.js"></script>
	</c:if>
	
	<title>支付成功</title>
    
    <c:if test="${USER_AGENT eq 'mobile'}">
    <script type="text/javascript" src="${ctx}/resources/icon.js"></script>
    <style type="text/css">
    	.main{height:100%; width:100%;}
    	.icon {
	       width: 1em; height: 1em;
	       vertical-align: -0.15em;
	       fill: currentColor;
	       overflow: hidden;
	    }
		.fs{font-size:60px;}
		.content{color:#3e3e3e;position: absolute; top: 50%;margin-top: -50px;width:100%; text-align:center; height:45px; line-height:45px;}
    </style>
    <script type="text/javascript">
    </script>
    </c:if>
	
</head>
<body>
	<c:if test="${USER_AGENT eq 'pc'}">
	<c:set var="header_title" value="支付提示" />
	<%@ include file="../commons/header.jsp"%>
	<!-- 订单 -->
	<div class="main-box container">
		<!-- 流程 -->
		<div class="progress">
			<div class="progress-bar progress-bar-success" style="width: 25%">
				<span>选择产品</span>
				<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
			</div>
			<div class="progress-bar progress-bar-success" style="width: 25%">
				<span>填写与核对</span>
				<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
			</div>
			<div class="progress-bar progress-bar-success" style="width: 25%">
				<span>在线支付</span>
				<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
			</div>
			<div class="progress-bar progress-bar-success" style="width: 25%">
				<span>付款成功</span>
				<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>
			</div>
		</div>
		<div class="alert alert-success" role="alert">
			<h2 class="pay-font">恭喜您，订单支付成功！
			<c:if test="${USER_AGENT eq 'pc'}">
			<a target="_self" href="${ctx}/user/sec/center">查看订单</a>
			</c:if>
			<c:if test="${USER_AGENT eq 'mobile'}">	
			<a target="_self" href="${ctx}/user/sec/order">查看订单</a>
			</c:if>
			，了解更多出游信息，请<a href="${ctx}/">返回首页</a>继续浏览。</h2>
		</div>
	</div>
	<!-- 页尾 -->
	<%@ include file="../commons/footer.jsp"%>
	</c:if>
	
	<c:if test="${USER_AGENT eq 'mobile'}">
		<div class="main">
		<div class="content">
			<svg class="icon fs" aria-hidden="true">
			    <use xlink:href="#icon-chenggong"></use>
			</svg>
			<br>
			手机支付成功，请关闭窗口！
		</div>
		</div>
	</c:if>
</body>    
</html>
