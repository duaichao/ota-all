<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <title>136旅游</title>
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta charset="UTF-8">
  <script type="text/javascript" src="${ctx }/resources/js/include-ext.js"></script>
  <script type="text/javascript">
  	Ext.onReady(function(){
  		
		Ext.create('Ext.Button', {
		    text: '重置账户锁资源',
		    renderTo:'btClear',
		    handler: function() {
		        Ext.Ajax.request({
					url:'${ctx}/accountThreadClear',
					success: function(response){
						var data = Ext.decode(response.responseText);
						Ext.Msg.alert('提示', '重置成功',function(){
							Ext.get('account').update(data.message);
						});
				    }
				});
		    }
		});
		
		Ext.create('Ext.Button', {
		    text: '测试webService',
		    renderTo:'btClear',
		    handler: function() {
		        Ext.Ajax.request({
					url:'${ctx}/serviceTest',
					success: function(response){
						var data = Ext.decode(response.responseText);
						Ext.Msg.alert('提示', '测试webService',function(){
							Ext.get('service_status').update(data.message);
						});
				    }
				});
		    }
		});
		
		Ext.create('Ext.Button', {
		    text: '开启webService',
		    renderTo:'btClear',
		    handler: function() {
		        Ext.Ajax.request({
					url:'${ctx}/fwsieo',
					success: function(response){
						var data = Ext.decode(response.responseText);
						Ext.Msg.alert('提示', '开启webService'+data.message,function(){
							
						});
				    }
				});
		    }
		});
		
		Ext.create('Ext.Button', {
		    text: '关闭webService',
		    renderTo:'btClear',
		    handler: function() {
		        Ext.Ajax.request({
					url:'${ctx}/fwsiec',
					success: function(response){
						var data = Ext.decode(response.responseText);
						Ext.Msg.alert('提示', '关闭webService 成功',function(){
							
						});
				    }
				});
		    }
		});
	});
	
  </script>
</head>
<body>
	短信线程<br/>
	${sms}
	<hr/>
	账户锁
	<div id="account">${account}</div>
	<hr/>
	login
	<div id="login">${login}</div>
	<hr/>
	service_type
	<div id="service_type">${service_type}</div>
	<hr/>
	service_status
	<div id="service_status">${service_status}</div>
	<hr/>
	service_info
	<div id="service_info">${service_info}</div>
	<div id="btClear"></div>
	<!-- 版权 -->
	<div class="footer">
		<p>Copyright &copy; 2012-2017 All Rights Reserved 琼ICP备16003588号 版权所有</p>
	</div>
</body>
</html>
