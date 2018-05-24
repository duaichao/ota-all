<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE HTML>
<html manifest="">
<head>
    <%@ include file="/commons/meta-ext.jsp"%>
    <script type='text/javascript'>
		var currViewInPath = '.stat.sale.parent';
		currViewInPath = currViewInPath.substring(1,currViewInPath.length);
		var autoView = util.getModuleAutoView(currViewInPath),
			autoController = currViewInPath;
		Ext.application({
			name: 'app',
			appFolder:cfg.getJsPath()+'/app',
			autoCreateViewport:autoView,
		    controllers:autoController
		});
		var currVisitType = '${PARAMS_COMPANY_TYPE}';//1供应商   2,3,4,5,6旅行社
    	var currVisitCompanyId='${PARAMS_COMPANY_ID}';//当前访问的公司id
    	var citys = Ext.decode('${city}');
    	var currVisitCompanyName = '${PARAMS_COMPANY_NAME}';
	</script>
    <script type="text/javascript" src="${ctx }/resources/js/echarts/echarts.js"></script>
    <script type="text/javascript" src="${ctx }/resources/js/app/ChartUtil.js"></script>
    <title>统计 销售 站长 或总公司</title>
    <style>
    	.total .item{line-height:21px;}
    	.total .item label{font-size:14px;}
    	.total .item .nb{font-style:normal; font-size:14px;}
    	.total .item li{display:inline-block;float:left;width:70px;}
    	.total .item li.title{width:45px;}
    	.total .item li.amount{width:120px;}
    	.total .clear{clear:both;}
    </style>
    
</head>
<body>
</body>
</html>