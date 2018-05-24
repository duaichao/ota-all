Ext.define('app.controller.b2b.user', {
	extend: 'app.controller.common.BaseController',
	views:['b2b.userGrid','Ext.ux.form.SearchField'],
	config: {
		control: {
			'gridpanel[itemId=basegrid]': {
	             itemclick:'onGridItemClick'
	        },
			'toolbar button[itemId=add]': {
	             click: 'onAdd'
	         },
	         'toolbar button[itemId=edit]': {
	             click: 'onEdit'
	         },
	         'toolbar button[itemId=del]': {
	             click: 'onDel'
	         },
	         'toolbar button[itemId=query]': {
	             click: 'onQuery'
	         },
	         'toolbar button[itemId=switch]': {
	             click: 'onSwitch'
	         },'toolbar button[itemId=setrole]': {
	             click: 'setRole'
	         },'toolbar button[itemId=resetPwd]': {
	             click: 'resetPwd'
	         }
		},
		refs: {
			roleGrid: 'gridpanel[itemId=rolegrid]',
			roleToolbar:'toolbar[itemId=roletool]',
            userGrid: 'gridpanel[itemId=basegrid]'
        }
	},
	onBaseGridRender:function(g){
		var me = this;
		setTimeout(function(){
		Ext.Ajax.request({
		    url: util.getPowerUrl(),
		    success: function(response, opts) {
		        var obj = Ext.decode(response.responseText),
		        	items = obj.children,
		        	ubtn = [],
		        	rbtn = [];
		        for(var i=0;i<items.length;i++){
		        	if(items[i].itemId == 'setrole'){
		        		rbtn.push(items[i]);
		        	}else{
		        		ubtn.push(items[i]);
		        	}
		        }
		        ubtn.push('->');
		        ubtn.push({
		        	emptyText:'搜索登录名/姓名',
		        	xtype:'searchfield',
		        	store:g.getStore()
		        });
		        me.getRoleToolbar().removeAll();
		        me.getRoleToolbar().add(rbtn);
		        util.createGridTbar(g,ubtn);
		    }
		});
		},500);
	},
	onGridItemClick:function(g,record, item, index, e, eOpts ){
		var me = this;
		Ext.Ajax.request({
			url:cfg.getCtx()+'/b2b/role/user',
			params:{userId:me.getUserGrid().getSelection()[0].get('ID')},
			success:function(response){
				if(response.responseText!=''){
					var ids = Ext.decode(response.responseText),
						rg = me.getRoleGrid(),
						sm = rg.getSelectionModel(),
						selectModels = [];
					sm.deselectAll();
					rg.getStore().each(function(){
						for(var i=0;i<ids.length;i++){
							if(ids[i]==this.get('ID')){
								selectModels.push(this);
							}
						}
					});
					sm.select(selectModels);
				}
			}
		});
	},
	createSaveWindow:function(title,btn){
	    var hideEdit = false;
	    if(title.indexOf('修改')!=-1){
	    	hideEdit = true;
	    }
	    var types=[
	        	{value:'01',text:'管理员'},
	        	{value:'0101',text:'管理员子账号'},
	        	{value:'02',text:'供应商'},
	        	{value:'0201',text:'供应商子账号'},
	        	{value:'03',text:'旅行社'},
	        	{value:'0301',text:'旅行社子账号'}
	        ],
	        typeStore = util.createComboStore(types),
	    	me= this,win = Ext.create('Ext.window.Window',{
			title:util.windowTitle(util.glyphToFont(btn.glyph),title,''),
			width:950,
			height:410,
			modal:true,
			draggable:false,
			resizable:false,
   			layout:'fit',
			items:[{
				xtype:'form',
				fieldDefaults: {
			        labelAlign: 'right',
			        labelWidth: 90
			    },
			    layout: 'border',
			    items: [{
			    	width:300,
					padding:'10 0 0 0',
					xtype:'panel',
					layout:'anchor',
					region:'east',
					itemId:'accountfieldset',
					hidden:hideEdit,
					style:'border-left:1px solid #d1d1d1;',
					items:[{
						xtype:'hidden',
						name:'pm[ID]'
					},{
			        	fieldLabel:'登录名',
			        	maxLength:20,
						maxLengthText:'最多可填写20个中文字或字母数字',
			        	name : 'pm[USER_NAME]',
						xtype: 'textfield',
						allowBlank: hideEdit
			        },{
			        	fieldLabel: '启用账户',
			        	hidden:true,
			        	checked: true,
					    xtype: 'checkbox',
		                name: 'pm[OPEN_ACCOUNT]',
		                inputValue: '1'
			        },{
			        	fieldLabel:'密码',
			        	name : 'pm[USER_PWD]',
					    xtype: 'textfield',
					    inputType: 'password',
					    allowBlank: hideEdit
			        },{
			        	fieldLabel:'确认密码',
			        	name : 'aginPassword',
					    xtype: 'textfield',
					    inputType: 'password',
					    allowBlank: hideEdit,
					    validator: function(value) {
				            var password1 = this.previousSibling('[inputType=password]');
				            if(hideEdit){return true;}else{
				            return (value === password1.getValue()) ? true : '两次密码不一致';
				            }
				        }
			        },{
						xtype:'combo',
						name:'pm[USER_TYPE]',
						hiddenName:'pm[USER_TYPE]',
						fieldLabel:'用户类型',
						forceSelection: true,
						allowBlank: hideEdit,
			            valueField: 'value',
			            displayField: 'text',
			            store:typeStore,
			            minChars: 0,
			            queryMode: 'local',
			            typeAhead: true
			        }]
			    },{
			    	xtype:'panel',
					layout:'anchor',
					region:'center',
			        padding:'10 20 0 0',
			        defaultType: 'textfield',
			        items:[{
			        	xtype: 'textfield',
			        	fieldLabel:'姓名',
			        	width:180,
			        	allowBlank: false,
			        	name:'pm[CHINA_NAME]'
			        },{
			        	anchor:'100%',
		        		fieldLabel:'身份证号',
		        		name:'pm[ID_CARD]'
			        },{
			        	xtype:'fieldcontainer',
			        	layout:'hbox',
			        	fieldLabel:'性别/生日',
			        	items:[{
				        	xtype:'radiogroup',
				        	width:100,
				        	defaults:{
				        		margin:'0 0 0 5'
				        	},
				        	items: [
				                {boxLabel: '男', name: 'pm[SEX]', inputValue: 1, checked: true},
				                {boxLabel: '女', name: 'pm[SEX]', inputValue: 2}
				            ]
				        },{
				        	emptyText:'生日',
				        	width:150,
				        	margin:'0 0 0 20',
				        	xtype: 'datefield',
				        	format:'Y-m-d',
				            name:'pm[BIRTHDAY]',
				            maxValue: new Date()
				        }]
			        },{
			        	fieldLabel:'手机',
			        	width:200,
			        	readOnly:true,
			        	hidden:true,
			        	name:'pm[MOBILE]'
			        },{
			        	xtype:'fieldcontainer',
			        	layout:'hbox',
			        	fieldLabel:'座机/传真',
			        	items:[{
			        		xtype:'textfield',
				        	flex:1,
				        	name:'pm[PHONE]'
				        },{
				        	xtype:'textfield',
				        	emptyText:'传真',
				        	margin:'0 0 0 10',
				        	flex:1,
				        	name:'pm[FAX]'
				        }]
			        },{
			        	xtype:'fieldcontainer',
			        	layout:'hbox',
			        	fieldLabel:'QQ1/2',
			        	items:[{
			        		xtype: 'textfield',
			        		flex:1,
				        	name:'pm[QQ1]'
				        },{
				        	margin:'0 0 0 10',
				        	flex:1,
				        	xtype: 'textfield',
			        		name:'pm[QQ2]'
				        }]
			        },{
			        	name:'pm[EMAIL]',
			        	anchor:'100%',
			        	vtype: 'email',
			        	fieldLabel:'Email'
			        }]
			    }],
				buttons: [{
			        text: '保存',
			        disabled: true,
			        formBind: true,
			        itemId:'save',
			        handler:function(btn){
			        	var form = btn.up('form'),
			        		win = form.up('window');
			        	me.onSave(form.getForm(),win);
			        }
			    }]
			}]
		});
		return win;
	},
	onAdd:function(btn){
		this.createSaveWindow('添加用户',btn).show();
	},
	onSave:function(form,win){
		var me = this,btn = win.down('button#save');
		if (form.isValid()) {
			btn.disable();
            form.submit({
            	submitEmptyText:false ,
            	url:cfg.getCtx()+'/b2b/user/save',
                success: function(form, action) {
                   util.success('保存用户成功');
                   me.getUserGrid().getStore().load();
                   win.close();
                },
                failure: function(form, action) {
                	var state = action.result?action.result.statusCode:0,
                		errors = ['保存异常','用户已存在','Email已存在','手机已存在'];
                    util.error(errors[0-parseInt(state)]);
                    btn.enable();
                }
            });
        }
	},
	onEdit:function(btn){
		var rg = this.getUserGrid(),
			win,form,sel,selData,formData={};
		if(sel = util.getSingleModel(rg)){
			win = this.createSaveWindow('修改用户',btn);
			form = win.down('form');
			win.show();
			formData = util.pmModel(sel);
			form.getForm().setValues(formData);
		}
	},
	onDel:function(btn){
		util.delGridModel(this.getUserGrid(),'/b2b/user/del');
	},
	onQuery:function(btn){
	},
	onSwitch:function(btn){
		util.switchGridModel(this.getUserGrid(),'/b2b/user/switch');
	},
	setRole:function(){
		var rgs = this.getRoleGrid().getSelection(),
			ug = this.getUserGrid(),sel,models=[];
		if(rgs.length==0){	
			util.alert('没有选择数据');
			return;
		}
		if(sel = util.getSingleModel(ug)){
			for (var i = 0; i < rgs.length; i++) { 
				models.push(rgs[i].data);
			}
	       	Ext.Ajax.request({
				url:cfg.getCtx()+'/b2b/user/set/role',
				params:{models:Ext.encode(models),userId:sel.get('ID')},
				success:function(){
					util.success("配置成功!");
				},
				failure:function(){
					util.error("配置失败！");
				}
			});
		}
	},
	resetPwd:function(){
		var rg = this.getUserGrid(),
			sel;
		if(sel = util.getSingleModel(rg)){
			Ext.Ajax.request({
				url:cfg.getCtx()+'/b2b/user/resetPWD',
				params:{userId:sel.get('ID')},
				success:function(){
					util.success("重置密码成功，密码：111111");
				}
			});
		}
	}
});