<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE HTML>
<html manifest="">
<head>
    <%@ include file="/commons/meta-ext.jsp"%>
    <title>产品 交通 支付</title>
    <script type='text/javascript'>
    	var order =  Ext.decode('${order}'),code=parseInt("${param.code}");
		Ext.application({
			name: 'app',
			appFolder:cfg.getJsPath()+'/app',
			autoCreateViewport:'app.view.produce.payfinish',
		    controllers:'app.controller.produce.payfinish'
		});
		var proType = '${param.type}';
		var discount = '${discount}'==''?null:Ext.decode('${discount}');
	</script>
</head>
<body>
</body>
</html>