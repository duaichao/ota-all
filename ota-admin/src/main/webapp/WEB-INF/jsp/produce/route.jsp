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
    <style>
    	.x-autocontainer-form-item, .x-anchor-form-item, .x-vbox-form-item, .x-table-form-item{margin-bottom:0px;}
    	label.inline_checkbox{color:#999;height:26px;line-height:26px;}
    	.tag-split-btn.x-btn-default-small.remove.sel{background:#fff;}
    	.data-view{padding:2px 1px 0px 0px;}
    	.data-view.nop{padding:2px 1px 0px 1px;}
    	.dtitle,.data-view .data-view-item span{color:#999;font-size:16px;font-family: Segoe UI;margin-left:2px;}
    	.day-view{overflow:hidden;}
    	.day-view .x-toolbar{background:transparent;}
    	.dtitle:before,.data-view .data-view-item,.day-view .x-toolbar .x-box-inner{font-family:"iconfont";font-size:18px;color:#427fed;font-style:normal;overflow:visible;}
    	
    	.day-view .x-toolbar .x-box-inner:before{content: "\e6be";position:absolute;left:-10px;top:6px;}
    	.day-view .x-toolbar.first-tool .x-box-inner:before{content: "\e6bc";font-size:14px;left:-8px;top:4px;}
    	.day-view .x-toolbar.no-sign .x-box-inner:before{content: "";font-size:0px;}
    	
    	
    	
    	.data-view .data-view-item:before{font-size:16px;content: "\e69c";}
    	.data-view .data-view-item.plan:before{font-size:20px;content:"\e6be";}
    	.data-view .data-view-item.plan span{font-family:"\5fae\8f6f\96c5\9ed1";font-size:14px;}
    	
    	.data-view .data-view-item.block:before{
    		font-size:24px;content: "\e64a";left:-35px;top:40px;color:#427fed;position:absolute;
    		filter:progid:DXImageTransform.Microsoft.BasicImage(rotation=1);
			-moz-transform: rotate(90deg);
			-o-transform: rotate(90deg);
			-webkit-transform: rotate(90deg);
			transform: rotate(90deg);
    	}
    	.data-view .data-view-item.block:first-child:before{content: "";}
    	
    	.data-view .data-view-item.block .step{position:absolute;width:30px;height:30px;line-height:30px;font-family: Segoe UI;top:-5px;right:-10px;}
    	.data-view .data-view-item.block .step i{font-size:20px;}
    	
    	.x-form-fieldcontainer.time{padding:3px 20px 3px 0px;border:1px dashed #fff;}
    	.x-form-fieldcontainer.time.hover{background:#e6f1f9;}
    	.x-form-fieldcontainer.time.select{background:#e6f1f9;border:1px dashed #157fcc;}
    	.x-form-fieldcontainer.time.select .x-form-item-body:after{font-family:"iconfont";content: "\e62a";color:#427fed;position:absolute;right:0px;bottom:0px;}
    	.x-form-fieldcontainer.time.select .x-form-item-body .x-field .x-form-item-body:after{content: "";}
    	
    	
    	.x-form-fieldcontainer.time .del{visibility:hidden;cursor:pointer;}
    	.x-form-fieldcontainer.time.hover .del{visibility:visible;}
    	
    	
    	.dtitle{font-size:20px;padding:8px 0;}
    	.dtitle:before{font-size:24px;content: "\e69c";color:#427fed;}
    	
    	.line-point{width:10px;height:2px;background:#eaeaea;}
    	.pressed .x-form-trigger-wrap{border-color:#427fed;}
    	.pressed .x-form-trigger-wrap:after{font-family:"iconfont";content: "\e62a";position:absolute;right:0px;bottom:0px; color:#427fed;}
    	.remove .x-btn-glyph{color:#f40;}
    	
    	.x-form-item-body-default.x-form-checkboxgroup-body{padding:0px;}
    	
    	
    	
    	.x-field.check{cursor:pointer;}
		
		.x-radio-default-cell .x-grid-cell-inner{padding:0px;}
		
		.x-form-item-body-default.x-form-checkboxgroup-body{padding:0px;}
    	.x-field.check{position:relative;}
    	.x-field.check span.cktxt{color:#ccc;cursor:pointer;}
    	.x-field.check.x-form-cb-checked{}
    	.x-field.check.x-form-cb-checked span.cktxt{color:#427fed;}
    	
    </style>
    <script type="text/javascript">
    	var dynamicParamsSupplyName = '${dynamicParamsSupplyName}';
    </script>
</head>
<body>
</body>
</html>