<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE HTML>
<html manifest="">
<head>
    <%@ include file="/commons/meta-ext.jsp"%>
    <%@ include file="/commons/meta-ext-init.jsp"%>
    <title>账户管理 - 网上充值</title>
    <style>
		.bank-list li {
			border: 1px solid #ddd;
			cursor: pointer;
			color: #6a6a6a;
			font-size: 14px;
			float: left;
			height: 32px;
			line-height: 32px;
			padding: 1px;
			width: 166px;
			margin: 0 5px 10px 5px;
			position: relative;
		}
		.bank-list li.focus {
			border: 2px solid #f60;
			padding: 0;
			z-index: 2;
		}
		.bank-list li .check{position:absolute;right:0px;bottom:-8px;color:#f60;display:none;}
		.bank-list li.focus .check{display:block;}
		.bank-list li  label{position:relative;width:100px;}
		.bank-list li label span{position:absolute;bottom:3px;width:80px;margin-left:3px;}
		.bank-list li .money-color{top: -15px;}
    </style>
    <script type="text/javascript">
    	var entityId = '${param.paramId}';
    	var rechargeId = '${id}';
    	var money = '${param.money}';
    	var rechargeNo = '${no}';
    </script>
</head>
<body>
</body>
</html>