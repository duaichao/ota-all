<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE HTML>
<html manifest="">
<head>
    <%@ include file="/commons/meta-ext.jsp"%>
    <%@ include file="/commons/meta-ext-init.jsp"%>
    <title>B2B短信</title>
    <script type="text/javascript" src="http://at.alicdn.com/t/font_atmk4swe160tfbt9.js"></script>
    <script type="text/javascript">
    	var useCount = '${useCount}'||'0';
    </script>
    <style>
    	.icon {
	       width: 1em; height: 1em;
	       vertical-align: -0.35em;
	       fill: currentColor;
	       overflow: hidden;
	    }
		.bank-list li {
			border: 3px dotted #d1d1d1;
			cursor: pointer;
			color: #6a6a6a;
			float: left;
			height: 40px;
			line-height: 35px;
			width: 166px;
			margin: 0 5px 10px 5px;
			position: relative;
		}
		.bank-list li.focus {
			border: 3px solid #FF5722;
			padding: 0;
			z-index: 2;
		}
		.bank-list li input{visibility:hidden;}
		.bank-list li .check{position:absolute;right:0px;top:10px;color:#FF5722;display:none;}
		.bank-list li.focus .check{display:block;}
		.bank-list li  label{position:relative;width:120px;}
		.bank-list li label span{position:absolute;bottom:3px;width:100px;margin-left:3px;font-size: 14px;}
		.bank-list li .money-color{top: -15px;}
    </style>
</head>
<body>
</body>
</html>