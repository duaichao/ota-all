<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE HTML>
<html manifest="">
<head>
		
    <%@ include file="/commons/meta-ext.jsp"%>
    <link href="${ctx }/resources/js/kindeditor/themes/default/default.css" rel="stylesheet" />
	<script src="${ctx }/resources/js/kindeditor/kindeditor-all-min.js"></script>
	<script src="${ctx }/resources/js/kindeditor/lang/zh_CN.js"></script>
    <%@ include file="/commons/meta-ext-init.jsp"%>
    <!--  <script type="text/javascript" src="${ctx }/resources/js/ext/src/ux/upload/plupload/plupload.full.min.js"></script>
    <script type="text/javascript" src="${ctx }/resources/js/ext/src/ux/upload/plupload/i18n/zh_CN.js"></script>-->
    
    
    <title>站群管理 公司管理</title>
    <style type="text/css">
    	.x-form-multiselect-body .x-boundlist .x-mask {
		    background: none;
		}
		
		.x-form-itemselector-body .x-form-item {
		    margin: 0;
		}
		
		.x-form-itemselector-top {
		    background-image: url(${ctx}/resources/images/top2.gif);
		}
		.x-form-itemselector-up {
		    background-image: url(${ctx}/resources/images/up2.gif);
		}
		.x-form-itemselector-add {
		    background-image: url(${ctx}/resources/images/right2.gif);
		}
		.x-form-itemselector-remove {
		    background-image: url(${ctx}/resources/images/left2.gif);
		}
		.x-form-itemselector-down {
		    background-image: url(${ctx}/resources/images/down2.gif);
		}
		.x-form-itemselector-bottom {
		    background-image: url(${ctx}/resources/images/bottom2.gif);
		}
		
		
		
		div.block-item img {
			border-radius: 50%;
		    -webkit-box-shadow: 0 4px 2px -2px rgba(0, 0, 0, 0.2);
		    -moz-box-shadow: 0 4px 2px -2px rgba(0, 0, 0, 0.2);
		    box-shadow: 0 4px 2px -2px rgba(0, 0, 0, 0.2);
		    margin: 10px 8px 8px 0;
		    border: 4px solid #fff;
		}
		
		div.block-item {
			position:relative;border:1px solid #eee;
		    float: left;
		    padding: 8px;
		    margin: 7px;
		/*    margin: 10px 0 0 25px;*/
		    text-align: center;
		    line-height: 14px;
		    color: #333;
		    font-size: 10px;
		    font-family: "Helvetica Neue",sans-serif;
		    width: 150px;
		    height:150px;
		    overflow: hidden;
		    cursor: pointer;
		    background-color: #fff;
		    -webkit-border-radius: 2px;
		    border-radius: 2px;
		    -webkit-box-shadow: 0 2px 2px 0 rgba(0,0,0,0.14),0 3px 1px -2px rgba(0,0,0,0.12),0 1px 5px 0 rgba(0,0,0,0.2);
		    box-shadow: 0 2px 2px 0 rgba(0,0,0,0.14),0 3px 1px -2px rgba(0,0,0,0.12),0 1px 5px 0 rgba(0,0,0,0.2);
		    overflow: hidden;
		    position: relative;
		    -webkit-transition: box-shadow 200ms cubic-bezier(0.4,0.0,0.2,1);
		    transition: box-shadow 200ms cubic-bezier(0.4,0.0,0.2,1)
		}
		.x-ie6 div.block-item,
		.x-ie7 div.block-item,
		.x-ie8 div.block-item {
		    border-top: none;
		    padding: 3px 2px;
		    margin: 2px;
		}
		
		div.block-item-hover {
		    background-color: #f1f1f1;
		    
		}
		
		.x-item-selected {
		    background-color: #D3E1F1 !important;
		}
		
		div.block-item strong {
		    display: block;
		    margin-bottom:5px;
		}
		div.block-item span {
		    color:#999;
		}
		div.block-item div.tags {
		    position:absolute;left:0px;top:0px;background:#eee;padding:4px 10px 4px 4px;border-bottom-right-radius:10px;color:#909090;
		}
		div.block-item div.info {
			display:none;
		    position:absolute;right:0px;top:0px;padding:0px 0px 10px 10px;
		}
		div.block-item-hover div.tags,
		div.x-view-item-focused div.tags,
		div.x-item-selected div.tags{background:#427fed;color:#fff;}
		
		
		div.block-item-hover div.info,
		div.x-view-item-focused div.info,
		div.x-item-selected div.info{display:block;}
    </style>
</head>
<body>
</body>
</html>