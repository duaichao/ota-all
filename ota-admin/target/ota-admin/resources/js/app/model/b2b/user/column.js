Ext.define('app.model.b2b.user.column', {
	constructor:function(){
		return [
		//Ext.create('Ext.grid.RowNumberer',{width:35}),
		{
			xtype: 'widgetcolumn',
        	width:45,
        	dataIndex:'FACE',
        	text:'',
        	sortable: false,
            menuDisabled: true,
        	widget: {
        		xtype:'image',
        		height:25,
                cls:'d-img-size-auto',
                listeners:{
    				afterrender:function(img){
    					var rec = img.getWidgetRecord(),face=cfg.getPicCtx()+'/'+rec.get('FACE');
    					util.imageLoader(face,Ext.emptyFn,function(){
    						img.setSrc(cfg.getImgPath()+'/noface.gif');
    					});
    					var err = 'javascript:this.src=\''+cfg.getImgPath()+'/noface.gif\'';
    					img.getEl().set({'data-qtip':"<img class=\"d-img-size-auto\" style=\"width:90px;height:90px;\" src=\""+face+"\" onerror=\""+err+"\"/>"});
    					img.setSrc(face);
    				}
    			}
            }
		},
		{
	        text: '登录名',
	        width:120,tdCls:'pt-4',
	        dataIndex: 'USER_NAME'
	    },{
	        text: '类型',
	        width:120,tdCls:'pt-4',
	        dataIndex: 'USER_TYPE'
	    },{
	        text: '部门',
	        width:120,tdCls:'pt-4',
	        dataIndex: 'TEXT'
	    },{
	        text: '公司',
	        width:240,tdCls:'pt-4',
	        dataIndex: 'COMPANY'
	    },{
	        text: '总公司',
	        width:240,tdCls:'pt-4',
	        dataIndex: 'PARENT_COMPANY'
	    },{
	        text: '城市',
	        width:60,tdCls:'pt-4',
	        dataIndex: 'CITY_NAME'
	    },{
	        text: '姓名',
	        width:120,tdCls:'pt-4',
	        dataIndex: 'CHINA_NAME'
	    },{
	        text: '手机',
	        width:150,tdCls:'pt-4',
	        dataIndex: 'MOBILE'
	    },{
	        text: '生日',
	        width:100,tdCls:'pt-4',
	        dataIndex: 'BIRTHDAY'
	    },{
	        text: '座机',
	        width:120,tdCls:'pt-4',
	        dataIndex: 'PHONE'
	    },{
	        text: '传真',
	        width:120,tdCls:'pt-4',
	        dataIndex: 'FAX'
	    },{
	        text: 'Email',
	        width:180,tdCls:'pt-4',
	        dataIndex: 'EMAIL'
	    },{
	        text: '状态',
	        width:80,tdCls:'pt-4',
	        dataIndex: 'IS_USE',
	        renderer: function(value){
	        	return value==1?'<span style="color:red">禁用</span>':'启用';
	        }
	    },{
	        text: '登录IP',
	        width:120,tdCls:'pt-4',
	        dataIndex: 'LOGIN_IP'
	    },{
	        text: '最后登录',
	        width:90,tdCls:'pt-4',
	        dataIndex: 'LOGIN_TIME'
	    }];
	}
});