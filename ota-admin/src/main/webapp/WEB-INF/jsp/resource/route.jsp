<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE HTML>
<html manifest="">
<head>
    <%@ include file="/commons/meta-ext.jsp"%>
    <link href="${ctx }/resources/js/kindeditor/themes/default/default.css" rel="stylesheet" />
	<script src="${ctx }/resources/js/kindeditor/kindeditor-all-min.js"></script>
	<script src="${ctx }/resources/js/kindeditor/lang/zh_CN.js"></script>
	
	<script type="text/javascript" src="${ctx }/resources/js/ext/src/ux/upload/plupload/plupload.full.min.js"></script>
    <script type="text/javascript" src="${ctx }/resources/js/ext/src/ux/upload/plupload/i18n/zh_CN.js"></script>
    <%@ include file="/commons/meta-ext-init.jsp"%>
    <title>产品 线路</title>
    <script type="text/javascript">
    	var dynamicParamsTrafficStatus = '${param.dynamicParamsTrafficStatus}';
    </script>
    <style>
    	.bbarck .x-form-cb-default,.bbarck .x-form-cb-label-default{margin-top:4px;}
    	.data-view{padding:2px 1px 0px 8px;}
    	.data-view.nop{padding:2px 1px 0px 1px;}
    	.dtitle,.data-view .data-view-item span{color:#999;font-size:16px;font-family: Segoe UI;margin-left:2px;}
    	.day-view{overflow:hidden;}
    	.day-view .x-toolbar{background:transparent;}
    	.dtitle:before,.data-view .data-view-item,.day-view .x-toolbar .x-box-inner{font-family:"iconfont";font-size:18px;color:#427fed;font-style:normal;overflow:visible;}
    	
    	.day-view .x-toolbar .x-box-inner:before{content: "\e6a3";font-size:14px;position:absolute;left:-8px;top:6px;}
    	
    	.day-view .x-toolbar.first-tool{padding: 2px 0 0px 8px;border:none;}
    	.day-view .x-toolbar.first-tool .x-box-inner:before{content: "\e68d";font-size:16px;left:-8px;top:4px;}
    	.day-view .x-toolbar.no-sign .x-box-inner:before{content: "";font-size:0px;}
    	
    	
    	/*.data-view .data-view-item{color:#999;}
    	.dtitle,.data-view .x-item-selected,.data-view .x-item-selected span{color:#427fed;}*/
    	
    	.data-view .data-view-item:before{font-size:16px;content: "\e6a2";top:2px;position:relative;}
    	.data-view .data-view-item.plan:before{font-size:15px;content:"\e683";}
    	.data-view .data-view-item.plan span{font-family:"\5fae\8f6f\96c5\9ed1";font-size:14px;}
    	
    	.data-view .data-view-item.block:before{font-size:20px;content: "\e6bb";left:-25px;top:40px;color:#427fed;position:absolute;}
    	.data-view .data-view-item.block:first-child:before{content: "";}
    	
    	.data-view .data-view-item.block .step{position:absolute;width:30px;height:30px;line-height:30px;font-family: Segoe UI;top:-5px;right:-10px;}
    	.data-view .data-view-item.block .step i{font-size:20px;}
    	
    	.x-form-fieldcontainer.time{padding:3px 20px 3px 0px;border:1px dashed #fff;}
    	.x-form-fieldcontainer.time.hover{background:#e6f1f9;}
    	.x-form-fieldcontainer.time.select{background:#e6f1f9;border:1px dashed #157fcc;}
    	.x-form-fieldcontainer.time.select .x-form-item-body:after{font-family:"iconfont";content: "\e618";color:#427fed;position:absolute;right:0px;bottom:0px;}
    	.x-form-fieldcontainer.time.select .x-form-item-body .x-field .x-form-item-body:after{content: "";}
    	
    	
    	.x-form-fieldcontainer.time .del{visibility:hidden;cursor:pointer;}
    	.x-form-fieldcontainer.time.hover .del{visibility:visible;}
    	
    	
    	.dtitle{font-size:20px;padding:8px 0;}
    	.dtitle:before{font-size:24px;content: "\e6a2";color:#427fed;position:relative;top:2px;}
    	
    	.line-point{width:10px;height:2px;background:#eaeaea;}
    	.pressed .x-form-trigger-wrap{border-color:#427fed;}
    	.pressed .x-form-trigger-wrap:after{font-family:"iconfont";content: "\e62a";position:absolute;right:0px;bottom:0px; color:#427fed;}
    	.remove .x-btn-glyph{color:#f40;}
    	
    	
    	
    </style>
</head>
<body>
</body>
</html>