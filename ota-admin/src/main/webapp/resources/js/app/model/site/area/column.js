Ext.define('app.model.site.area.column', {
	constructor:function(){
		return [
		{
	        text: '区域名',
	        flex:1,
	        dataIndex: 'TEXT'
	    },{
	        text: '状态',
	        width:50,
	        dataIndex: 'IS_USE',
	        renderer: function(value){
	        	return value==1?'<span style="color:red">禁用</span>':'启用';
	        }
	    }];
	}
});