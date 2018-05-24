Ext.define('app.model.site.company.roleColumn', {
	constructor:function(){
		return [
		{
	        text: '角色名称',
	        flex:1,
	        dataIndex: 'ROLE_NAME',
	        renderer:function(v,c,r){
	        	var h = v;
	        	if(r.get('SYNC_CNT')>0){
	        		h = '<i class="iconfont green-color f20">&#xe659;</i> '+h;
	        	}
	        	return h;
	        }
	    },{
	        text: '类型',
	        width:80,
	        dataIndex: 'ROLE_TYPE',
	        renderer: function(v){
	        	if(v=='0'){
	        		return '公司';
	        	}
	        	if(v=='1'){
	        		return '员工';
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