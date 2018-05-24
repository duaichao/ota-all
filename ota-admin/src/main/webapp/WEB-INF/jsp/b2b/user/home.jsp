<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE HTML>
<html manifest="">
<head>
    <%@ include file="/commons/meta-ext.jsp"%>
    <%@ include file="/commons/meta-ext-init.jsp"%>
    <title>xsx</title>
    <style>
    	.total .item{line-height:35px;height:35px;}
    	.total .item table td{font-size:14px; text-align:center;width:7%;border:solid #d1d1d1 1px;}
    	.total .item table.header-tb td{border-bottom:none;border-top:none;background:#f5f5f5;}
    	.total .item table.today-tb td{border-bottom:none;border-top:none;}
    	.total .item table.total-tb td{border-bottom:none;border-top:none;}
    	
    	.head{width:110px;margin-left:10px;display:inline-block;height:30px;margin-top:5px;background:#427fed;line-height:25px;padding:0 10px;}
    	.head i{font-size:18px;top:3px;}
    	
    	.l20{left:20px;}
    	.x-docked-summary{
			background:#FFF8DC!important;  
			border-top:1px solid #d1d1d1!important; 
		}
		.x-docked-summary table,.x-docked-summary table td.x-grid-cell{background-color:transparent!important;}
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
    	
    	.data-view .data-view-item.block:before{font-size:20px;content: "\e6bb";left:-25px;top:40px;color:#427fed;position:absolute;}
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
    	
    	
    	
    	.x-field.check{border:1px solid #e1e1e1;cursor:pointer;}
		
		.x-radio-default-cell .x-grid-cell-inner{padding:0px;}
		
		.x-form-item-body-default.x-form-checkboxgroup-body{padding:0px;}
    	.x-field.check{border:1px solid #f1f1f1;padding:3px 15px 3px 5px;position:relative;}
    	.x-field.check span.cktxt{color:#ccc;cursor:pointer;}
    	.x-field.check.x-form-cb-checked{border-color:#427fed;}
    	.x-field.check.x-form-cb-checked span.cktxt{color:#427fed;}
    	
    	
		.x-btn.blue .x-btn-inner{color:#fff;font-weight:normal;font-size:14px;}
    	.docked-tools {border:none;border-bottom:2px solid #427fed!important;}
    	.docked-tools a.x-btn{height:35px;line-height:35px;display:inline-block;}
    	.docked-tools a.x-btn.float{top:0px!important;}
    </style>
    <script type="text/javascript">
    	var waitDoData = Ext.decode('${data}');
    	var citys = Ext.decode('${city}');
    	var departs = Ext.decode('${departs}');
    	var companys = Ext.decode('${companys}');
    </script>
    <script type="text/javascript" src="${ctx }/resources/js/echarts/echarts.js"></script>
    <script type="text/javascript" src="${ctx }/resources/js/app/ChartUtil.js"></script>
</head>
<body>
</body>
</html>