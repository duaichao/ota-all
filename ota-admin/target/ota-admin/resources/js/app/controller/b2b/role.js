var moduleId = '';
Ext.define('app.controller.b2b.role', {
	extend: 'app.controller.common.BaseController',
	config: {
		control: {
			'gridpanel[itemId=basegrid]': {
	             itemclick:'onGridItemClick'
	        },
	        'treepanel[itemId=moduletree]': {
	             itemclick:'onModuleItemClick',
	             checkchange:'onModuleTreeCheckChange'
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
	         'toolbar button[itemId=switch]': {
	             click: 'onSwitch'
	         },'toolbar button[itemId=setmodule]': {
	             click: 'setModule'
	         },'toolbar button[itemId=setpower]': {
	             click: 'setPower'
	         }
		},
		refs: {
			roleGrid:'gridpanel[itemId=basegrid]',
            moduleTree: 'treepanel[itemId=moduletree]',
            powerTree:'treepanel[itemId=powertree]',
            moduleToolbar:'toolbar[itemId=moduletool]',
            powerToolbar:'toolbar[itemId=powertool]'
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
			        	rbtn = [],
			        	mbtn = [],
			        	pbtn = [];
			        
			        
			        for(var i=0;i<items.length;i++){
			        	if(items[i].itemId=='setpower'){
			        		pbtn.push(items[i]);
			        	}else if(items[i].itemId=='setmodule'){
			        		mbtn.push(items[i]);
			        	}else{
			        		rbtn.push(items[i]);
			        	}
			        }
			        me.getPowerToolbar().removeAll();
			        me.getModuleToolbar().removeAll();
			        me.getPowerToolbar().add(pbtn);
			        me.getModuleToolbar().add(mbtn);
			        util.createGridTbar(g,rbtn);
			    }
			});
		},500);
	},
	onGridItemClick:function(g,record, item, index, e, eOpts ){
		moduleId = '';
		var me = this,
			mt = this.getModuleTree();
		mt.getRootNode().expand(true,function(){
			me.setModuleSelections(mt,'module');
		});
		
		/*var pg = me.getPowerTree(),
			pgs = pg.getStore();
		pgs.load({
			url:cfg.getCtx()+'/b2b/power/list?moduleId=',
			callback:function(){
				me.setModuleSelections(pg,'power');
			}
		});*/
		
	},
	setModuleSelections:function(tree,type){
		var me = this;
		Ext.Ajax.request({
			url:cfg.getCtx()+'/b2b/'+type+'/role',
			params:{roleId:me.getRoleGrid().getSelection()[0].get('ID')},
			success:function(response){
				if(response.responseText!=''){
					var ids = Ext.decode(response.responseText),
						root = tree.getRootNode();
					root.cascadeBy(function () {
						this.set('checked', ids.indexOf(this.get('id')) > -1);
					});
				}
			}
		});
	},
	onModuleItemClick:function( treePanel, record, item, index, e, eOpts ){
		var me = this,
			mid=record.get('id'),
			pg = this.getPowerTree(),
			pgs = pg.getStore();
			moduleId = mid;
		pgs.load({
			url:cfg.getCtx()+'/b2b/power/list?moduleId='+mid,
			callback:function(){
				me.setModuleSelections(pg,'power');
			}
		});
	},
	onModuleTreeCheckChange:function(node, checked){
		this.childHasChecked(node,checked);
		var parentNode = node.parentNode;
		if(parentNode != null) {   
			this.parentCheck(parentNode,checked);   
		} 
	},
	parentCheck:function(node ,checked){
		var childNodes = node.childNodes,isChildCheck = false;
		for (var i = 0; i < childNodes.length; i++) {
			if (childNodes[i].get('checked')) {
				isChildCheck = true;
				break;
			}
		};
		node.set('checked',isChildCheck);
		var parentNode = node.parentNode;
		if (parentNode != null ) {
			this.parentCheck(parentNode,checked);
		}
	},
	childHasChecked:function(node, checked){
		node.cascadeBy(function (child) {
			child.set("checked",checked)
		});
	},
	roleTypes:[
	//{text:'站长',value:'1'},
	{text:'供应商',value:'2'},{text:'旅行社',value:'3'}],
	createSaveWindow:function(title,btn){
		var store = util.createComboStore(this.roleTypes),
			me = this,win = Ext.create('Ext.window.Window',{
			title:util.windowTitle(util.glyphToFont(btn.glyph),title,''),
			width:400,
			height:250,
			modal:true,
			draggable:false,
			resizable:false,
   			layout:'fit',
			items:[{
				xtype:'form',
				bodyPadding: '10 5 5 5',
				bodyStyle:'background:#fff;',
    			scrollable:false,
    			fieldDefaults: {
			        labelAlign: 'right',
			        labelWidth: 120,
			        msgTarget: 'side'
			    },
			    defaults:{
			    	anchor:'70%'
			    },
			    items: [{
			    	xtype:'hidden',
			    	name:'pm[ID]'
			    },{
					fieldLabel:'角色名称',
				    name : 'pm[ROLE_NAME]',
				    xtype: 'textfield',
				    allowBlank: false
				},{
					margin:'10 0 0 0',
					xtype:'combo',
					name:'pm[ROLE_TYPE]',
					hiddenName:'pm[ROLE_TYPE]',
					fieldLabel:'角色类型',
					forceSelection: true,
					allowBlank: false,
		            valueField: 'value',
		            displayField: 'text',
		            store:store,
		            minChars: 0,
		            queryMode: 'local',
		            typeAhead: true
				},{
					fieldLabel:'状态',
					xtype:'radiogroup',
					margin:'5 0 0 0',
		        	anchor: '70%',
		        	items: [
		                {boxLabel: '启用', name: 'pm[IS_USE]', inputValue: 0, checked: true},
		                {boxLabel: '禁用', name: 'pm[IS_USE]', inputValue: 1}
		            ]
				}],
				buttons: [{
			        text: '保存',
			        itemId:'save',
			        disabled: true,
			        formBind: true,
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
		this.createSaveWindow('添加角色',btn).show();
	},
	onSave:function(form,win){
		var me = this,btn = win.down('button#save');
		if (form.isValid()) {
			btn.disable();
            form.submit({
            	submitEmptyText:false ,
            	url:cfg.getCtx()+'/b2b/role/save',
                success: function(form, action) {
                   util.success('保存角色成功');
                   win.close();
                   me.getRoleGrid().getStore().load();
                },
                failure: function(form, action) {
                	var state = action.result?action.result.statusCode:0,
                		errors = ['保存异常','角色已存在'];
                    util.error(errors[0-parseInt(state)]);
                    btn.enable();
                }
            });
        }
	},
	onEdit:function(btn){
		var rg = this.getRoleGrid(),
			win,form,sel,selData,formData={};
		if(sel = util.getSingleModel(rg)){
			win = this.createSaveWindow('修改角色',btn);
			form = win.down('form');
			win.show();
			formData = util.pmModel(sel);
			form.getForm().setValues(formData);
		}
	},
	onDel:function(btn){
		util.delGridModel(this.getRoleGrid(),'/b2b/role/del');
	},
	onSwitch:function(btn){
		util.switchGridModel(this.getRoleGrid(),'/b2b/role/switch');
	},
	setBase:function(tree,url,moduleTree){
		var tms = tree.getChecked(),
			rg = this.getRoleGrid(),sel,models=[],
			moduleSel,moduleId;
			if(moduleTree){
				moduleSel = moduleTree.getSelection()[0];
				moduleId = moduleSel.get('id');
			}
//		if(tms.length==0 && url == '/b2b/role/set/module'){	
//			util.alert('没有选择数据');
//			return;
//		}
		if(sel = util.getSingleModel(rg)){
			for (var i = 0; i < tms.length; i++) { 
				models.push(tms[i].data);
			}
//			var moduleId = tms.length == 0 ? '' : tms[0].get('moduleId');
	       	Ext.Ajax.request({
				url:cfg.getCtx()+url,
				params:{models:Ext.encode(models),roleId:sel.get('ID'),moduleId: moduleId},
				success:function(){
					util.success("配置成功!");
				},
				failure:function(){
					util.error("配置失败！");
				}
			});
		}
	},
	setModule:function(btn){
		this.setBase(this.getModuleTree(),'/b2b/role/set/module');
	},
	setPower:function(btn){
		this.setBase(this.getPowerTree(),'/b2b/role/set/power');
	}
});