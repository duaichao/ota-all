Ext.define('app.controller.site.company', {
	extend: 'app.controller.common.BaseController',
	config:{
		refs: {
			companyTool:'toolbar[itemId=companytool]',
			companyView:'#companyPanel dataview',
			moduleTree:'treepanel[itemId=moduletree]',
			powerTree:'treepanel[itemId=powertree]'
        },
		control:{
			'#companyPanel dataview':{
	        	afterrender:'onViewRender'
	        },
			'treepanel[itemId=moduletree]': {
	             itemclick:'onModuleItemClick',
	             checkchange:'onModuleTreeCheckChange'
	        },
	        'toolbar button[itemId=setModule]': {
	             click: 'setModule'
	        },'toolbar button[itemId=setChildPower]': {
	             click: 'setPower'
	        }
		},
        detailData:''
	},
	onViewRender:function(g){
		var me = this;
		//加载当前用户公司详情
		Ext.getBody().mask('数据初始化...');
		Ext.Ajax.request({
			url:cfg.getCtx()+'/site/company/detailCompany',
			success:function(response, opts){
				var data = Ext.decode(response.responseText);
				me.setDetailData(data);
				Ext.getBody().unmask();
			}
		});
		Ext.Ajax.request({
		    url: util.getPowerUrl(),
		    success: function(response, opts) {
		        var obj = Ext.decode(response.responseText),
		        	items = obj.children,
		        	cobtns = [],
		        	embtns = [],
		        	dpbtns = [],
		        	setBtns = [];
		        for(var i=0;i<items.length;i++){
		        	delete items[i].checked;
		        	var itm = items[i];
		        	if(Ext.String.endsWith(itm.itemId,'Child')){
		        		itm.xtype = 'menuitem';
		        		//如果是子公司 不能再添加分公司
		        		if(itm.itemId=='addChild'){
		        			if(cfg.getUser().companyType=='3'||cfg.getUser().companyType=='4'){
		        			}else{
		        				cobtns.push(itm);
		        			}
		        		}else{
		        			cobtns.push(itm);
		        		}
		        	}
		        	if(Ext.String.endsWith(itm.itemId,'Depart')){
		        		itm.text = itm.text.replace('部门','');
		        		dpbtns.push(itm);
		        	}
		        	if(Ext.String.endsWith(itm.itemId,'Employee')||itm.itemId=='setPower'){
		        		embtns.push(itm);
		        	}
		        	if(Ext.String.endsWith(itm.itemId,'Menu')){
		        		setBtns.push(itm);
		        	}
		        }
		        embtns.push({
	        		glyph:'xe645@iconfont',
	        		itemId:'employeePosition',
	        		text:'特殊授权',
	        		menu:setBtns
	        	});
		        //me.getEmployeeTool().removeAll();
		        //me.getDepartTool().removeAll();
		        me.getCompanyTool().removeAll();
		        //me.getEmployeeTool().add('->');
		        //me.getEmployeeTool().add(embtns);
		        //me.getDepartTool().add(dpbtns);
		        
		        
		        //me.getCompanyTool().add(cobtns);
		        me.getCompanyTool().add('->');
		        me.getCompanyTool().add({
					emptyText:'公司名称/登录名',
		        	width:150,
		        	xtype:'searchfield',
		        	store:g.getStore()
				});
		        
		        var m = Ext.create('Ext.menu.Menu', {
			        items: cobtns
				 });
			    g.on('itemcontextmenu',function( g, record, item, index, e, eOpts ){
			    	e.stopEvent();
			    	g.getSelectionModel().select(record);
			    	m.showAt(e.getXY());
	                return false;
			    });
			    m.on('click',function(menu,item,e){
			    	this[item.itemId](item);
			    },me);
			    
			    
			    var dm = Ext.create('Ext.menu.Menu', {
			        items: [{
			        	text:'刷新',
			        	handler:function(){
			        		document.location.reload();
			        	}
			        }]  
		    	});
			    g.on('containercontextmenu',function(g,e){
			    	e.stopEvent();
			    	dm.showAt(e.getXY());
	                return false;
			    });
		    }
		});	
	},
	addChild:function(btn){
		this.managerWindow('创建分公司',btn).show();
	},
	editChild:function(btn){
		var companyView = this.getCompanyView(),
			win,form,sel,selData,formData={};
		if(sel = companyView.getSelectionModel().getSelection()[0]){
			win = this.managerWindow('修改分公司',btn);
			form = win.down('form'),
			fs = form.down('panel[itemId=account]'),
			cs = form.down('panel[itemId=companyfieldset]'),
			tfs = fs.query('textfield:not(tagfield)');
			win.show();
			formData = util.pmModel(sel);
			for(var i=0;i<tfs.length;i++){
				tfs[i].disable().hide();
			}
			fs.hide();
			form.getForm().setValues(formData);
			cs.down('filefield[name=LICENSE_ATTR]').setRawValue(formData['pm[LICENSE_ATTR]']);
			cs.down('filefield[name=LOGO_URL]').setRawValue(formData['pm[LOGO_URL]']);
			cs.down('filefield[name=BUSINESS_URL]').setRawValue(formData['pm[BUSINESS_URL]']);
		}
	},
	managerWindow:function(title,btn){
	    var types,defaultValue = 0,hideCitys=true;
	    if(title.indexOf('分公司')!=-1){
	    	defaultValue = 4;
	    	types =[
		        {text:'门市',value:3},
		        {text:'子公司',value:7}
	        ];
	    }   
	    var saveUserType = cfg.getUser().userType;
	    if(saveUserType==''){
	    	util.alert('用户类型不正确');
	    	return;
	    }
	    //总公司员工创建子公司 应截取前两位
	    saveUserType = saveUserType.substring(0,2);
	    var store = util.createComboStore(types),
	    	me = this,
			pid = me.getDetailData().ID,
			win = Ext.create('Ext.window.Window',{
				title:util.windowTitle(util.glyphToFont(btn.glyph),title),
				width:800,
				height:360,
				modal:true,
				draggable:false,
				resizable:false,
	   			layout:'fit',
				items:[{
					xtype:'form',
					fieldDefaults: {
				        labelAlign: 'right',
				        labelWidth: 60
				    },
					bodyPadding: '0 5 5 5',
					layout: 'border',
					items:[{
						xtype:'panel',
						layout:'anchor',
						region:'center',
						itemId:'companyfieldset',
						title:'公司信息',
						items:[{
							xtype:'hidden',
							name:'pm[USER_TYPE]',
							value:saveUserType
						},{
							xtype:'fieldcontainer',
				        	layout: 'hbox',
							fieldLabel:'名称',
							items:[{
								flex:1,
								xtype:'textfield',
								name : 'pm[COMPANY]',
								allowBlank: false
							},{
								width:100,
								margin:'0 0 0 5',
								xtype:'textfield',
								emptyText:'公司代码',
								name : 'pm[CODE]',
								allowBlank: false
							},{
								width:100,
								margin:'0 0 0 5',
								xtype:'textfield',
								name : 'pm[LEGAL_PERSON]',
								emptyText:'负责人',
								allowBlank: false
							}]
						},{
							xtype:'fieldcontainer',
				        	layout: 'hbox',
							fieldLabel:'地址',
							items:[{
								flex:1,
								xtype:'textfield',
								name : 'pm[ADDRESS]'
							},{
								width:100,
								margin:'0 0 0 5',
								xtype:'combo',
								name:'pm[TYPE]',
								forceSelection: true,
								editable:false,
								hiddenName:'pm[TYPE]',
								emptyText:'公司类型',
								value:defaultValue,
								allowBlank: false,
					            valueField: 'value',
					            displayField: 'text',
					            store:store,
					            minChars: 0,
					            queryMode: 'local',
					            typeAhead: true
							},{
								width:100,
								margin:'0 0 0 5',
								xtype:'textfield',
								name : 'pm[PHONE]',
								emptyText:'座机/手机',
								allowBlank: false
							}]
						},{
							xtype:'fieldcontainer',
				        	layout: 'hbox',
							fieldLabel:'许可证号',
							items:[{
								width:180,
								xtype:'textfield',
								name : 'pm[LICENSE_NO]',
								allowBlank: false
							}, {
					            xtype: 'splitter'
					        },{
								flex:1,
								xtype:'filefield',
								emptyText:'许可证副本/身份证',
								name : 'LICENSE_ATTR',
								buttonConfig: {
					                text:'',
									ui:'default-toolbar',
					                glyph: 'xe648@iconfont'
					            }
							}]
						},{
							xtype:'fieldcontainer',
				        	layout: 'hbox',
							fieldLabel:'品牌名称',
							items:[{
								width:180,
								xtype:'textfield',
								name : 'pm[BRAND_NAME]'
							}, {
					            xtype: 'splitter'
					        },{
								flex:1,
								xtype:'filefield',
								emptyText:'形象图',
								name:'LOGO_URL',
								buttonConfig: {
					                text:'',
									ui:'default-toolbar',
					                glyph: 'xe648@iconfont'
					            }
							}]
						},{
							fieldLabel:'营业执照',
							anchor:'100%',
							xtype:'filefield',
							name:'BUSINESS_URL',
							buttonConfig: {
								text:'',
								ui:'default-toolbar',
				                glyph: 'xe648@iconfont'
				            }
						},{
							fieldLabel:'经营范围',
							anchor:'100%',
							xtype:'textarea',
							name:'pm[BUSINESS]'
						},{xtype:'hidden',name:'pm[PID]',value:pid},{xtype:'hidden',name:'pm[USER_ID]'},{xtype:'hidden',name:'pm[ID]'},{xtype:'hidden',name:'pm[SOURCE]',value:0},{
							xtype:'hidden',
							name:'pm[CITY_ID]',
							value:(cfg.getUser().cityId)
						},{
							xtype:'hidden',
							name:'pm[CITY_NAME]',
							value:(cfg.getUser().cityName)
						}]
					},{
						width:240,
						margin:'0 0 0 5',
						xtype:'panel',
						itemId:'account',
						title:'账号信息',
						layout:'anchor',
						region:'east',
						items:[{
							fieldLabel:'登录名',
							maxLength:20,
							maxLengthText:'最多可填写20个中文字或字母数字',
							xtype:'textfield',
							name : 'pm[USER_NAME]',
							allowBlank: false
						},{
							fieldLabel:'密码',
							name : 'pm[USER_PWD]',
						    xtype: 'textfield',
						    minLength: 6,
						    inputType: 'password',
						    allowBlank: false
						},{
							fieldLabel:'确认密码',
							name : 'pm[aginPassword]',
						    xtype: 'textfield',
						    inputType: 'password',
						    allowBlank: false,
						    validator: function(value) {
					            var password1 = this.previousSibling('[inputType=password]');
					            return (value === password1.getValue()) ? true : '两次密码不一致'
					        }
						},{
							name : 'pm[ROLE_ID]',
							xtype:'hidden',
							value:'1'//分公司手动分配角色 分配总公司的权限
						}/*,{
							fieldLabel:'分配角色',
							hidden:true,
							forceSelection: true,
							name : 'pm[ROLE_ID]',
							hiddenName:'pm[ROLE_ID]',
							width:215,
						    xtype: 'combo',
						    displayField: 'ROLE_NAME',
        					valueField: 'ID',
						    store:Ext.create('Ext.data.Store',{
						    	pageSize:100,
						    	model:Ext.create('Ext.data.Model',{
						    		fields:['ID','ROLE_NAME']
						    	}),
						    	proxy: {
							    	autoLoad:false,
							        type: 'ajax',
							        noCache:true,
							        url: cfg.getCtx()+'/b2b/role/listRole?roleType=3',
							        reader: {
							            type: 'json',
							            rootProperty: 'data',
							            totalProperty:'totalSize'
							        }
							   	}
						    })
						}*/]
					}],
					buttons: [{
						itemId:'save',
				        text: '保存',
				        disabled: true,
				        formBind: true,
				        handler:function(btn){
				        	var form = btn.up('form'),
				        		win = form.up('window');
				        	me.onManagerSave(form.getForm(),win);
	      				}
				    }]
				}]
			});
		return win;
	},
	useChild:function(btn){
		util.switchGridModel(this.getCompanyView(),'/site/company/switch');
	},
	powerChild:function(btn){
		var me = this,
			cg = this.getCompanyView(),
			win,sel,mt;
			if(sel = util.getSingleModel(cg)){
				win = this.powerWindow('配置公司权限',btn);
				win.show();
				mt = this.getModuleTree();
				mt.getRootNode().expand(true,function(){
					me.setSelections(mt,'module',sel.get('USER_ID'));
				});
			}
	},
	powerWindow:function(title,btn){
		var me = this,
			win = Ext.create('Ext.window.Window',{
			title:util.windowTitle(util.glyphToFont(btn.glyph),title,''),
			width:550,
			height:380,
			modal:true,
			draggable:false,
			resizable:false,
   			layout: 'border',
   			items:[{
   				region:'center',
		    	itemId:'moduletree',
		    	xtype:'treepanel',
		    	reserveScrollbar: true,
			    useArrows: true,
			    rootVisible: false,
			    multiSelect: true,
			    singleExpand: false,
			    loadMask: true,
			    animate: false,
			    selModel:{
		    		mode:'SINGLE'
		    	},
			    dockedItems:[{
			    	itemId:'moduletool',
		    		xtype:'toolbar',
		        	items:[{
		        		itemId:'setModule',
		        		glyph:'xe624@iconfont',
		        		text:'配置模块'
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
			    }]
   			},{
   				region:'east',
		    	xtype:'treepanel',
		    	selModel:{
		    		mode:'SINGLE'
		    	},
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
		        		itemId:'setChildPower',
		        		glyph:'xe687@iconfont',
		        		text:'配置按钮'
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
		    	itemId:'powertree',
		    	split: {
			        size: 1,
			        style:'background:#c1c1c1;'
			    },
		    	width:225,
		    	minWidth:225
   			}]
   		});
   		return win;
	},
	setSelections:function(tree,type,userId){
		var me = this;
		Ext.Ajax.request({
			url:cfg.getCtx()+'/b2b/'+type+'/user?userId='+userId,
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
			pgs = pg.getStore(),sel,userId;
			
			
		if(treePanel.up('window').title.indexOf('公司权限')!=-1){
			sel = this.getCompanyView().getSelection()[0],
			userId = sel.get('USER_ID');
		}else{
			sel = this.getEmployeeGrid().getSelection()[0],
			userId = sel.get('ID');
		}
		pgs.load({
			url:cfg.getCtx()+'/b2b/power/listRoleOfPower?MODULE_ID='+mid,
			callback:function(){
				me.setSelections(pg,'power',userId);
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
	}
});