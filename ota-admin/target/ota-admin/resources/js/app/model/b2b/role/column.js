Ext.define('app.model.b2b.role.column', {
	constructor:function(){
		return [
		{
	        text: '角色名称',
	        flex:1,
	        dataIndex: 'ROLE_NAME'
	    },{
	        text: '类型',
	        width:80,
	        dataIndex: 'ROLE_TYPE',
	        renderer: function(v){
	        	if(v=='1'){
	        		return '站长';
	        	}
	        	if(v=='2'){
	        		return '供应商';
	        	}
	        	if(v=='3'){
	        		return '旅行社';
	        	}
	        }
	    },{
	        text: '状态',
	        width:80,
	        dataIndex: 'IS_USE',
	        renderer: function(value){
	        	return value==1?'<span style="color:red">禁用</span>':'启用';
	        }
	    }];
	}
});