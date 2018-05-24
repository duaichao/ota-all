<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE HTML>
<html manifest="">
<head>
    <%@ include file="/commons/meta-ext.jsp"%>
    <title>产品 购买 交通</title>
    <script type='text/javascript'>
    	var orderId = '${param.orderId}',ruleId = '${param.ruleId}',trafficId='${param.trafficId}',queryDate = '${param.queryDate}',prices = Ext.decode('${basePrices}');
		var od =  '${detail}'==''?null:Ext.decode('${detail}');
		Ext.application({
			name: 'app',
			appFolder:cfg.getJsPath()+'/app',
			autoCreateViewport:'app.view.produce.traffic.buy',
		    controllers:'app.controller.produce.traffic.buy'
		});
		var formatQueryDate = Ext.Date.format(Ext.Date.parse(queryDate,'Ymd'),'Y-m-d');
	</script>
    <style>
		.checkout{
			border: 3px solid #FFE7AE;
			background-color: #FFF9EA;
			padding: 10px;
		}
		.checkout ul li{
			border-bottom:2px dotted #E5DAC0;
			height:30px;
			line-height:30px;
			position:relative;
		}
		.checkout i.eye{position:absolute;bottom:0px;right:0px;font-size:16px;cursor:pointer;color:#427fed;}
		.checkout ul li.total{height:40px;line-height:40px;}
		.checkout ul li label{display:inline-block;float:left;width:40%;text-align:right;position:relative;}
		.checkout ul li label.title{color:#999;font-size:12px;font-weight:normal;width:20%;text-align:left;}
		.checkout ul li.total label.title{color:#888;font-size:14px;}
		.checkout ul li label.th{visibility:hidden;}
		.checkout ul li label.th.show{visibility:visible;}
		.checkout ul li label b{font-weight:normal;color:#888;}
		.x-panel-header-default{background-color:#f1f1f1;}
		.x-panel-header-title-default{color:#666;font: 18px/32px "Microsoft Yahei",simsun;}
    </style>
</head>
<body>
</body>
</html>