Ext.define('app.model.site.company.employeeColumn', {
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
		},{
	        text: '登录名',
	        width:120,
	        tdCls:'pt-4',
	        dataIndex: 'USER_NAME',
	        renderer:function(v,c,r){
	        	var h = [],isManager = r.get('IS_MANAGER')||'',
	        		isConunselor = r.get('IS_COUNSELOR')||'',
	        		isContacts = r.get('IS_CONTACTS')||'',
	        		isCaiWu = r.get('IS_FINANCE')||'';
	        	if(isManager=='1'){
	        		h.push('<i class="iconfont blue-color f16" data-qtip="经理">&#xe627;</i> ');
	        	}
	        	if(isConunselor=='1'){
	        		h.push('<i class="iconfont green-color f18" data-qtip="顾问">&#xe638;</i> ');
	        	}
	        	if(isContacts=='1'){
	        		h.push('<i class="iconfont green-color f18" data-qtip="客服">&#xe639;</i> ');
	        	}
	        	if(isCaiWu=='1'){
	        		h.push('<i class="iconfont orange-color f18" data-qtip="财务短信通知">&#xe636;</i> ');
	        	}
	        	h.push(v);
	        	return h.join('');
	        }
	    },{
	        text: '姓名',
	        width:120,
	        tdCls:'pt-4',
	        dataIndex: 'CHINA_NAME'
	    },{
	        text: '生日',
	        tdCls:'pt-4',
	        hidden:true,
	        width:120,
	        dataIndex: 'BIRTHDAY'
	    },{
	        text: '座机',
	        tdCls:'pt-4',
	        width:120,
	        dataIndex: 'PHONE'
	    },{
	        text: '传真',
	        tdCls:'pt-4',
	        width:120,
	        dataIndex: 'FAX'
	    },{
	        text: '手机',
	        tdCls:'pt-4',
	        width:150,
	        dataIndex: 'MOBILE'
	    },{
	        text: 'Email',
	        tdCls:'pt-4',
	        flex:1,
	        dataIndex: 'EMAIL'
	    },{
	        text: '状态',
	        tdCls:'pt-4',
	        width:80,
	        dataIndex: 'IS_USE',
	        renderer: function(value){
	        	return value==1?'<span style="color:red">禁用</span>':'启用';
	        }
	    },{
	        text: '最后登录',
	        tdCls:'pt-4',
	        width:100,
	        dataIndex: 'LOGIN_TIME'
	    }];
	}
});