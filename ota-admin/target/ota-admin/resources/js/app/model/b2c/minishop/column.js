Ext.define('app.model.b2c.minishop.column', {
	constructor:function(){
		return [
		Ext.create('Ext.grid.RowNumberer',{width:35}),
		{
	        text: '微店二维码',
	        width:100,
	        dataIndex: 'CODE',
	        renderer: function(value,c,r){
	        	c.tdAttr = 'data-qwidth="320" data-qtip="<img style=\'width:300px;height:300px;\' src=\''+cfg.getPicCtx()+'/'+value+'\' />"';
	        	return '<img src="'+cfg.getPicCtx()+'/'+value+'" height="50" width="50"/>';
	        }
	    },{
	        text: '微信二维码',
	        width:100,
	        dataIndex: 'WX_IMG',
	        renderer: function(value,c,r){
	        	c.tdAttr = 'data-qwidth="320" data-qtip="<img style=\'width:300px;height:300px;\' src=\''+cfg.getPicCtx()+'/'+value+'\' />"';
	        	return '<img src="'+cfg.getPicCtx()+'/'+value+'" height="50" width="50"/>';
	        }
	    },{
	    	text:'登陆名',
	    	width:100,
	    	dataIndex:'USER_NAME',
	    	renderer: function(value,c,r){
	        	return value;
	        }
	    },{
	        text: '微店名',
	        width:200,
	        dataIndex: 'TITLE',
	        renderer: function(value,c,r){
	        	return '<div class="f14 blue-color">'+(value||'')+'</div>';
	        }
	    },{
	    	text:'公司',
	    	width:300,
	    	dataIndex:'COMPANY_NAME',
	    	renderer: function(value,c,r){
	    		if(r.get('ACCOUNT_USER_NAME')){
	    			return '<div>'+value+'</div><div style="margin-top:5px" class="ht20 green-color"><i class="iconfont">&#xe638;</i> '+r.get('ACCOUNT_USER_NAME')+'</div>';
	    		}
	        	return value;
	        }
	    },{
	        text: '域名',
	        width:160,
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
	        text: '店主',
	        width:100,
	        dataIndex: 'MANAGER',
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
	        text: '状态',
	        width:80,
	        dataIndex: 'IS_USE',
	        renderer: function(value){
	        	return value==1?'<span style="color:red">禁用</span>':'启用';
	        }
	    }]
	}
});