<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE HTML>
<html manifest="">
<head>
    <%@ include file="/commons/meta-ext.jsp"%>
    <title>产品 购买 线路</title>
    <script type='text/javascript'>
    	var msg = '${msg}';
    	if(msg!=''){
    		Ext.onReady(function(){
	    		var win = Ext.create('Ext.window.Window',{
		   			title: util.windowTitle('','信息提示',''),
		   			width:300,
		   			height:150,
		   			draggable:false,
					resizable:false,
					closable:false,
					modal:true,
		   			bodyStyle:'background:#fff;padding:10px;',
		   			layout:'fit',
		   			items:[{
		   				html:'<h3 class="alert-box">线路过期、出团日期过期</h3>'
		   			}],
		   			buttons:[{
		   				text:'确定',
		   				handler:function(){
		   					util.redirectTo('produce/route');
		   				}
		   			}
		   			/*,{
		   				text:'点错了',
		   				cls:'disable',
		   				handler:function(){
		   					win.close();
		   				}
		   			}*/
		   			]
		   		});
		   		win.show();
	   		});
    	}else{
	    	
	    	var routeId='${param.routeId}',
	    		orderId = '${param.orderId}',
	    		planId = '${param.planId}',
	    		planName = '${planName}',
	    		goDate = '${param.goDate}',
	    		prices = '${basePrices}'==''?null:Ext.decode('${basePrices}'),
	    		musters = '${musters}'==''?null:Ext.decode('${musters}'),
	    		discount = '${discount}'==''?null:Ext.decode('${discount}'),
	    		others = '${others}'==''?null:Ext.decode('${others}');
			var routeDetail =  '${routeDetail}'==''?null:Ext.decode('${routeDetail}'),
				od =  '${detail}'==''?null:Ext.decode('${detail}');
			Ext.application({
				name: 'app',
				appFolder:cfg.getJsPath()+'/app',
				autoCreateViewport:'app.view.produce.route.buy',
			    controllers:'app.controller.produce.route.buy'
			});
		}
	</script>
	<style>
		.checkout{padding:0 10px;
		}
		.checkout ul li{
			border-bottom:1px dotted #d3d3d3;
			height:30px;
			line-height:30px;
			
			position:relative;
		}
		.checkout i.eye{position:absolute;bottom:0px;right:0px;font-size:16px;cursor:pointer;color:#427fed;}
		.checkout ul li.total{height:40px;line-height:40px;}
		.checkout ul li label{display:inline-block;float:left;width:35%;text-align:right;position:relative;}
		.checkout ul li label.title{width:30%;color:#666;font-size:12px;font-weight:normal;width:30%;text-align:left;overflow:hidden;word-break:keep-all;white-space:nowrap;}
		.checkout ul li.total label.title{color:#666;font-size:14px;}
		.checkout ul .discount{padding:5px;}
		.checkout ul li label.th{visibility:hidden;}
		.checkout ul li label.th.show{visibility:visible;}
		.checkout ul li label b{font-weight:normal;color:#666;}
		.x-panel-header-default{background-color:#f1f1f1;}
		.x-panel-header-title-default{color:#666;font: 18px/32px "Microsoft Yahei",simsun;}
		
		.data-view .data-view-item{font-family:"iconfont";font-size:18px;color:#427fed;font-style:normal;overflow:visible;}
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
    	
    	.x-field.check{cursor:pointer;}
		
		.x-radio-default-cell .x-grid-cell-inner{padding:0px;}
		
		.x-form-item-body-default.x-form-checkboxgroup-body{padding:0px;}
    	.x-field.check{position:relative;}
    	.x-field.check span.cktxt{color:#ccc;cursor:pointer;}
    	.x-field.check.x-form-cb-checked{}
    	.x-field.check.x-form-cb-checked span.cktxt{color:#427fed;}
    	
    	
    	
		#discountElement .x-panel-default-outer-border-trl,
		#discountElement .x-panel-default-outer-border-rbl{border-color:#ff6d00 !important; }
		#discountElement .x-panel-header-default{background:transparent;}
		#discountElement .x-panel-header-title-default > .x-title-text-default{color:#ff6d00;font-weight:700;}
		
		.earnest{height:20px;line-height:20px;}
		.earnest li{display:inline-block;}
	</style>
</head>
<body>
</body>
</html>