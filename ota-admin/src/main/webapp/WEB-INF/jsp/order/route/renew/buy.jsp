<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE HTML>
<html manifest="">
<head>
    <%@ include file="/commons/meta-ext.jsp"%>
    <title>添加补单 线路</title>
    <script type="text/javascript">
    var routeId='${param.routeId}',
   		orderId = '${param.orderId}',
   		planId = '',
   		planName = '',
   		goDate = '',
   		musters = '${musters}'==''?null:Ext.decode('${musters}'),
   		prices = '${basePrices}'==''?null:Ext.decode('${basePrices}'),
   		others = '${others}'==''?null:Ext.decode('${others}');
	var hasRoute = routeDetail =  '${routeDetail}'==''?false:true,
		routeDetail =  '${routeDetail}'==''?{TITLE:'请选择线路产品',FEATURE:'线路产品特色',INTER_SINGLE_ROOM:0,RETAIL_SINGLE_ROOM:0}:Ext.decode('${routeDetail}'),
		od =  '${detail}'==''?null:Ext.decode('${detail}');
    	Ext.application({
			name: 'app',
			appFolder:cfg.getJsPath()+'/app',
			autoCreateViewport:'app.view.order.route.renew.buy',
		    controllers:'app.controller.order.route.renew.buy'
		});
    </script>
    <style>
		.checkout{
			background: #ffe0b2;
		}
		.checkout ul li{
			height:30px;
			line-height:30px;
			position:relative;
		}
		.checkout ul li label{display:inline-block;float:left;width:30%;text-align:right;position:relative;}
	</style>
</head>
<body>
</body>
</html>