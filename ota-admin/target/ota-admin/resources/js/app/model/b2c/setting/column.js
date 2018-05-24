Ext.define('app.model.b2c.setting.column', {
	constructor:function(){
		return [
		Ext.create('Ext.grid.RowNumberer',{width:35}),
		/*{
	        text: '微店二维码',
	        width:100,
	        dataIndex: 'CODE',
	        renderer: function(value,c,r){
	        	return '<img src="'+cfg.getPicCtx()+'/'+value+'" height="50" width="50"/>';
	        }
	    },*/{
	        text: '网站名',
	        flex:1,
	        dataIndex: 'TITLE',
	        renderer: function(value,c,r){
	        	return '<div class="f14 blue-color">'+(value||'')+'</div>';
	        }
	    },{
	        text: '域名',
	        width:300,
	        dataIndex: 'DOMAIN',
	        renderer: function(value,c,r){
	        	return value;
	        }
	    },{
	        text: '备案号',
	        width:150,
	        dataIndex: 'RECORD_NO',
	        renderer: function(value,c,r){
	        	return value;
	        }
	    },{
	        text: '手机',
	        width:100,
	        dataIndex: 'PHONE1',
	        renderer: function(value,c,r){
	        	return value;
	        }
	    },{
	        text: 'QQ',
	        width:100,
	        dataIndex: 'QQ1',
	        renderer: function(value,c,r){
	        	return value;
	        }
	    },{
	        text: 'LOGO',
	        width:100,
	        dataIndex: 'LOGO',
	        renderer: function(value,c,r){
	        	return value;
	        }
	    },{
	        text: '徽标',
	        width:100,
	        dataIndex: 'ICON',
	        renderer: function(value,c,r){
	        	return value;
	        }
	    }/*,{
	        text: '支付宝',
	        width:100,
	        dataIndex: 'SELLER_EMAIL',
	        renderer: function(value,c,r){
	        	return value;
	        }
	    }*/,{
	        text: '状态',
	        width:80,
	        dataIndex: 'IS_USE',
	        renderer: function(value){
	        	return value==1?'<span style="color:red">禁用</span>':'启用';
	        }
	    }]
	}
});