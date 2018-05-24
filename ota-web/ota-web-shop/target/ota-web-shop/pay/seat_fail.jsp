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
    
    <title>占座失败</title>
    
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
</head>
<body>
	<div class="main">
	<div class="content">
		<svg class="icon fs" aria-hidden="true">
		    <use xlink:href="#icon-shibai"></use>
		</svg>
		<br>
		占座失败！请联系网站客服！订单编号：${sParaTemp.out_trade_no}
	</div>
	</div>
</body>    
</html>