function roleChildHasChecked(node, checked){
	node.cascadeBy(function (child) {
		child.set("checked",checked)
	});
}
function roleParentCheck(node ,checked){
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
		roleParentCheck(parentNode,checked);
	}
}
function setModuleSelections(grid,tree,type){
	var me = this;
	Ext.Ajax.request({
		url:cfg.getCtx()+'/b2b/'+type+'/role',
		params:{roleId:grid.getSelection()[0].get('ID')},
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
}
Ext.define('app.view.site.companyRole', {
    extend: 'Ext.panel.Panel',
    xtype:'companyRole',
    layout: 'border',
    items: [{
    	region:'west',
    	style:'border-right:1px solid #d1d1d1;',
	    selType:'checkboxmodel',
    	xtype:'gridpanel',
    	itemId:'basegrid',
    	listeners:{
    		itemclick:function(g,record, item, index, e, eOpts ){
    			var mt = this.up().down('treepanel#rolemoduletree');
    			mt.getRootNode().expand(true,function(){
    				setModuleSelections(g,mt,'module');
    			});
    		}
    	},
    	store:util.createGridStore(cfg.getCtx()+'/site/company/list/role',Ext.create('app.model.site.company.roleModel')),
    	columns:Ext.create('app.model.site.company.roleColumn'),
    	dockedItems:[{
    		xtype:'toolbar',
        	items:[{
        		glyph:'xe62b@iconfont',
        		text:'添加',
        		handler:function(b){
        			var store = util.createComboStore([{text:'公司',value:'0'},{text:'员工',value:'1'}]),
	        			win = Ext.create('Ext.window.Window',{
	        			title:util.windowTitle(util.glyphToFont(b.glyph),'添加',''),
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
	        			        disabled: true,
	        			        formBind: true,
	        			        handler:function(btn){
	        			        	var form = btn.up('form'),
	        			        		w = form.up('window');
	        			        	if (form.isValid()) {
	        			        		btn.disable();
	        			                form.submit({
	        			                	submitEmptyText:false ,
	        			                	url:cfg.getCtx()+'/site/company/save/role',
	        			                    success: function(form, action) {
	        			                       util.success('保存角色成功');
	        			                       w.close();
	        			                       b.up('gridpanel#basegrid').getStore().load();
	        			                    },
	        			                    failure: function(form, action) {
	        			                    	var state = action.result?action.result.statusCode:0,
	        			                    		errors = ['保存异常','角色已存在'];
	        			                        util.error(errors[0-parseInt(state)]);
	        			                        btn.enable();
	        			                    }
	        			                });
	        			            }
	        			        }
	        			    }]
	        			}]
	        		}).show();
        		}
        	},{
        		glyph:'xe62d@iconfont',
        		text:'修改',
        		handler:function(b){
        			var rg = b.up('gridpanel#basegrid'),
	        			win,form,sel,selData,formData={};
	        		if(sel = util.getSingleModel(rg)){
	        			win = Ext.create('Ext.window.Window',{
		        			title:util.windowTitle(util.glyphToFont(b.glyph),'修改',''),
		        			width:400,
		        			height:200,
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
		        			        disabled: true,
		        			        formBind: true,
		        			        handler:function(btn){
		        			        	var form = btn.up('form'),
		        			        		w = form.up('window');
		        			        	if (form.isValid()) {
		        			        		btn.disable();
		        			                form.submit({
		        			                	submitEmptyText:false ,
		        			                	url:cfg.getCtx()+'/site/company/save/role',
		        			                    success: function(form, action) {
		        			                       util.success('保存角色成功');
		        			                       w.close();
		        			                       b.up('gridpanel#basegrid').getStore().load();
		        			                    },
		        			                    failure: function(form, action) {
		        			                    	var state = action.result?action.result.statusCode:0,
		        			                    		errors = ['保存异常','角色已存在'];
		        			                        util.error(errors[0-parseInt(state)]);
		        			                        btn.enable();
		        			                    }
		        			                });
		        			            }
		        			        }
		        			    }]
		        			}]
		        		}).show();
	        			form = win.down('form');
	        			win.show();
	        			formData = util.pmModel(sel);
	        			form.getForm().setValues(formData);
	        		}
        		}
        	},{
        		text:'同步',
        		hidden:cfg.getUser().companyType!='2',
        		glyph:'xe659@iconfont',
        		handler:function(btn){
        			var rg = btn.up('gridpanel#basegrid'),sel;
	        		if(sel = util.getSingleModel(rg)){
	        			if(sel.get('ROLE_TYPE')!='1'){
	        				util.alert('只能同步员工角色');
	        				return;
	        			}
	        			btn.disable();
	        			Ext.Ajax.request({
							url:cfg.getCtx()+'/b2b/role/sync',
							params:{
								'roleId':sel.get('ID')
							},
							success:function(response, opts){
								var obj = Ext.decode(response.responseText);
								btn.enable();
								if(obj.success){
									util.success('角色同步成功');
									btn.up('gridpanel#basegrid').getStore().load();
								}else{
									var state = obj.statusCode,
			                		errors = ['角色同步异常'];
			                    	util.error(errors[0-parseInt(state)]);
								}
							},
							failure: function(resp,opts) { 
								btn.enable();
								util.error('角色同步异常');
							}
						});
	        		}
        		}
        	}]
    	}],
    	width:350,
    	minWidth:300
    },{
    	style:'border-right:1px solid #d1d1d1;',
    	region:'center',
    	itemId:'rolemoduletree',
    	xtype:'treepanel',
    	reserveScrollbar: true,
	    useArrows: true,
	    rootVisible: false,
	    multiSelect: true,
	    singleExpand: false,
	    loadMask: true,
	    animate: false,
	    dockedItems:[{
	    	itemId:'moduletool',
    		xtype:'toolbar',
        	items:[{
        		glyph:'xe624@iconfont',
        		text:'配置模块',
        		handler:function(bb){
        			var tree = bb.up().up('treepanel#rolemoduletree'),
        				tms = tree.getChecked(),
        				rg = tree.up().down('gridpanel#basegrid'),sel,models=[];
        			if(sel = util.getSingleModel(rg)){
        				for (var i = 0; i < tms.length; i++) { 
        					models.push(tms[i].data);
        				}
        			   	Ext.Ajax.request({
        					url:cfg.getCtx()+'/b2b/role/set/module',
        					params:{models:Ext.encode(models),roleId:sel.get('ID')},
        					success:function(){
        						util.success("配置成功!");
        					},
        					failure:function(){
        						util.error("配置失败！");
        					}
        				});
        			}
        		}
        	}]
    	}],
    	store:new Ext.data.TreeStore({
    		autoLoad:false,
            model: Ext.create('app.model.b2b.module.model'),
            proxy: {
                type: 'ajax',
                url: cfg.getCtx()+'/b2b/module/listRoleOfModule'
            },
            folderSort: false
        }),
        columns:[{
			xtype: 'treecolumn',
	        text: '模块名称',
	        padding:'1 0 1 0',
	        flex:1,
	        sortable:false,
	        dataIndex: 'text'
	    }],
        root: {  
	        id : -1,
	        text:'系统模块',  
	        expanded: false  
	    },
	    listeners:{
	    	itemclick:function(treePanel, record, item, index, e, eOpts){
	    		var mid=record.get('id'),
	    			grid = this.up().down('gridpanel#basegrid'),
					pg = this.up().down('treepanel#rolepowertree'),
					pgs = pg.getStore();
				pgs.load({
					url:cfg.getCtx()+'/b2b/power/list/user?moduleId='+mid,
					callback:function(){
						setModuleSelections(grid,pg,'power');
					}
				});
	    	},
	    	checkchange:function(node,checked){
	    		roleChildHasChecked(node,checked);
	    		var parentNode = node.parentNode;
	    		if(parentNode != null) {   
	    			roleParentCheck(parentNode,checked);   
	    		} 
	    	}
	    }
    },{
    	region:'east',
    	xtype:'treepanel',
    	reserveScrollbar: true,
	    useArrows: true,
	    rootVisible: false,
	    multiSelect: true,
	    singleExpand: false,
	    loadMask: true,
	    animate: false,
	    dockedItems:[{
	    	itemId:'powertool',
    		xtype:'toolbar',
        	items:[{
        		glyph:'xe687@iconfont',
        		text:'配置按钮',
        		handler:function(bb){
        			var tree = bb.up().up('treepanel#rolepowertree'),
        				tms = tree.getChecked(),
        				rg = tree.up().down('gridpanel#basegrid'),sel,models=[];
        			if(sel = util.getSingleModel(rg)){
        				for (var i = 0; i < tms.length; i++) { 
        					models.push(tms[i].data);
        				}
        			   	Ext.Ajax.request({
        					url:cfg.getCtx()+'/b2b/role/set/power',
        					params:{models:Ext.encode(models),roleId:sel.get('ID'),moduleId: tms[0].get('moduleId')},
        					success:function(){
        						util.success("配置成功!");
        					},
        					failure:function(){
        						util.error("配置失败！");
        					}
        				});
        			}
        		}
        	}]
    	}],
    	store:new Ext.data.TreeStore({
    		autoLoad:false,
            model: Ext.create('app.model.b2b.power.model'),
            proxy: {
                type: 'ajax',
                url: cfg.getCtx()+'/b2b/power/listRoleOfPower'
            },
            folderSort: false
        }),
        columns:[{
			xtype: 'treecolumn',
	        text: '按钮名称',
	        padding:'1 0 1 0',
	        flex:1,
	        sortable:false,
	        dataIndex: 'text'
	    }],
        root: {  
	        id : -1,
	        text:'按钮',  
	        expanded: true  
	    },  
    	itemId:'rolepowertree',
    	width:300,
    	minWidth:300
    }]
});

