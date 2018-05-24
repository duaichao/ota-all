Ext.define('app.model.b2b.power.column', {
	constructor:function(){
		return [
		{
	        text: '按钮名称',
	        flex:1,
	        dataIndex: 'text'
	    },{
	        text: '状态',
	        width:80,
	        dataIndex: 'isUse',
	        renderer: function(value){
	        	return value==1?'<span style="color:red">禁用</span>':'启用';
	        }
	    }];
	}
});