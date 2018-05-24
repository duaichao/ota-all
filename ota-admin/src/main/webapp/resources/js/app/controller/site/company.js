Ext.define('app.controller.site.company', {
	extend: 'app.controller.common.BaseController',
	views:['site.childCompanyGrid','site.departGrid','site.employeeGrid','Ext.ux.form.ExtKindEditor','app.view.site.companyRole','app.view.site.companyAccountLog','Ext.ux.form.ItemSelector'],
	config: {
		control: {
			'toolbar[itemId=companytool] button[itemId=addChild]':{
				click:'addChild'
			},
			'toolbar[itemId=companytool] button[itemId=editChild]':{
				click:'editChild'
			},
			'toolbar[itemId=companytool] button[itemId=useChild]':{
				click:'useChild'
			},
			'toolbar[itemId=companytool] button[itemId=powerChild]':{
				click:'powerChild'
			},
			'toolbar[itemId=companytool] button[itemId=resetPwdChild]':{
				click:'resetPwdChild'
			},
			'toolbar[itemId=companytool] button[itemId=setWapChild]':{
				click:'setWapChild'
			},
			'toolbar[itemId=companytool] button[itemId=contractChild]':{
				click:'contractChild'
			},
			'toolbar[itemId=companytool] button[itemId=setMapChild]':{
				click:'setMapChild'
			},
			'toolbar[itemId=companytool] button[itemId=addRoleChild]':{
				click:'addRoleChild'
			},
			'toolbar[itemId=companytool] button[itemId=giveRoleChild]':{
				click:'giveRoleChild'
			},
			'toolbar[itemId=companytool] button[itemId=czChild]':{
				click:'czChild'
			},
			'toolbar[itemId=companytool] button[itemId=detail]':{
				click:'detailCompany'
			},
			'toolbar[itemId=departtool] button[itemId=addDepart]':{
				click:'addDepart'
			},
			'toolbar[itemId=departtool] button[itemId=editDepart]':{
				click:'editDepart'
			},
			'toolbar[itemId=departtool] button[itemId=delDepart]':{
				click:'delDepart'
			},
			'toolbar[itemId=departtool] button[itemId=useDepart]':{
				click:'useDepart'
			},
			'toolbar[itemId=employeetool] button[itemId=addEmployee]':{
				click:'addEmployee'
			},
			'toolbar[itemId=employeetool] button[itemId=editEmployee]':{
				click:'editEmployee'
			},
			'toolbar[itemId=employeetool] button[itemId=delEmployee]':{
				click:'delEmployee'
			},
			'toolbar[itemId=employeetool] button[itemId=useEmployee]':{
				click:'useEmployee'
			},
			'toolbar[itemId=employeetool] button[itemId=setPower]':{
				click:'setUserPower'
			},
			'toolbar[itemId=employeetool] button[itemId=resetPwdEmployee]':{
				click:'resetPwdChild'
			},
			'toolbar[itemId=employeetool] button[itemId=employeePosition] menuitem':{
				click:function(mi){
					var me = this;
					if(mi.itemId=='setManagerMenu'){
						me.setManagerEmployee.call(me);
					}
					if(mi.itemId=='setCounselorMenu'){
						me.setCounselorEmployee.call(me);
					}
					if(mi.itemId=='setServiceMenu'){
						me.setServiceEmployee.call(me);
					}
					if(mi.itemId=='setCaiWuMenu'){
						me.setCaiWuMenu.call(me);
					}
					if(mi.itemId=='payPasswordMenu'){
						me.payPasswordMenu.call(me,mi);
					}
					if(mi.itemId=='setDepartMenu'){
						me.setDepartMenu.call(me,mi);
					}
				}
			},
			'toolbar[itemId=employeetool] button[itemId=giveRoleEmployee]':{
				click:'giveRoleEmployee'
			},
			'childcompanygrid':{
				//afterrender:'initPageBar',
				cellclick:function( view, td, cellIndex, record, tr, rowIndex, e, eOpts ){
					var rec = record,getUrl='';
					if(cellIndex==4){
						if(e.target.tagName=='I'&&e.target.className.indexOf('act')!=-1){
							Ext.factory({
								companyId:rec.get('ID')
							},'app.view.site.companyAccountLog').show();
						}
					}
				},
				selectionchange: function(view, records) {
					var btn = this.getCompanyGrid().down('button[itemId=powerChild]'),
						btn1 = this.getCompanyGrid().down('button[itemId=useChild]'),
						btn2 = this.getCompanyGrid().down('button[itemId=resetPwdChild]'),
						btn3 = this.getCompanyGrid().down('button[itemId=editChild]'),
						btn4 = this.getCompanyGrid().down('button[itemId=giveRoleChild]');
					if(btn){
	                	btn.setDisabled(records[0].get('USER_ID')==cfg.getUser().id);
	                }
	                if(btn1){
	                	btn1.setDisabled(records[0].get('USER_ID')==cfg.getUser().id);
	                }
	                if(btn2){
	                	btn2.setDisabled(records[0].get('USER_ID')==cfg.getUser().id);
	                }
	                if(btn3){
	                	btn3.setDisabled(records[0].get('USER_ID')==cfg.getUser().id);
	                }
	                if(btn4){
	                	btn4.setDisabled(records[0].get('PID')=='-1');
	                }
	                
	                var rg = this.getCompanyGrid(),
						sel,
						ct = this.getDepartGrid();
					if(sel = util.getSingleModel(rg)){
						ct.getStore().load({params:{
							'COMPANY_ID':sel.get('ID')
						}});
					}else{
						ct.getStore().load({params:{
							'COMPANY_ID':'123'
						}});
					}
	            }
			},
			'departgrid':{
				afterrender:function(g){
					var me = this;
					g.getStore().on('load',function(store, records, successful, eOpts){
						if(records.length>0){
							g.getSelectionModel().select(0);
						}else{
							me.getEmployeeGrid().getStore().load({params:{
								'DEPART_ID':'123'
							}});
						}
						
					});
				},
				select:'onDepartGridRowClick'
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
		refs: {
			companyGrid:'childcompanygrid',
			departGrid:'departgrid',
			employeeGrid:'employeegrid',
			companyTool:'toolbar[itemId=companytool]',
			departTool:'toolbar[itemId=departtool]',
			employeeTool:'toolbar[itemId=employeetool]',
			moduleTree:'treepanel[itemId=moduletree]',
			powerTree:'treepanel[itemId=powertree]'
        },
        detailData:''
	},
	initPageBar:function(g){
		//util.addCommonContextMenu(g);
		//var me = this;
		/*g.getStore().on('load',function(store, records, successful, eOpts){
			console.log(records.length);
			if(records.length<20){
				g.down('pagingtoolbar').hide();
			}else{
				g.down('pagingtoolbar').show();
			}
		});*/
	},
	onDepartGridRowClick:function(grid, record, index, eOpts){
		var rg = grid,
			sel,
			ct = this.getEmployeeGrid();
		if(sel = util.getSingleModel(rg)){
			ct.getStore().getProxy().setExtraParams({'DEPART_ID':''+sel.get('ID')});
            ct.getStore().load();
		}else{
			ct.getStore().getProxy().setExtraParams({'DEPART_ID':'123'});
			ct.getStore().load();
		}
	},
	onBaseGridRender:function(g){
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
		setTimeout(function(){
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
			        me.getEmployeeTool().removeAll();
			        me.getDepartTool().removeAll();
			        me.getCompanyTool().removeAll();
			        me.getEmployeeTool().add('->');
			        me.getEmployeeTool().add(embtns);
			        me.getDepartTool().add(dpbtns);
			        me.getCompanyTool().add(cobtns);
			        
			        me.getCompanyTool().add('->');
			        me.getCompanyTool().add({
						emptyText:'公司名称/登录名',
			        	width:150,
			        	xtype:'searchfield',
			        	store:g.getStore()
					});
			        
			        /*me.getCompanyTool().add('->');
			        me.getCompanyTool().add({
			        	itemId:'detail',
			        	text:'公司详情'
			        });*/
			    }
			});	
		},500);
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
				width:'85%',
				height:'60%',
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
	addChild:function(btn){
		this.managerWindow('创建分公司',btn).show();
	},
	editChild:function(btn){
		var rg = this.getCompanyGrid(),
			win,form,sel,selData,formData={};
		if(sel = util.getSingleModel(rg)){
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
			form.getForm().setValues(formData);
			cs.down('filefield[name=LICENSE_ATTR]').setRawValue(formData['pm[LICENSE_ATTR]']);
			cs.down('filefield[name=LOGO_URL]').setRawValue(formData['pm[LOGO_URL]']);
			cs.down('filefield[name=BUSINESS_URL]').setRawValue(formData['pm[BUSINESS_URL]']);
		}
	},
	onManagerSave:function(form,win){
		var me = this,btn=win.down('button#save');
		if (form.isValid()) {
			btn.disable();
            form.submit({
            	submitEmptyText:false ,
            	url:cfg.getCtx()+'/site/company/save?source=company',
                success: function(form, action) {
                   util.success('保存数据成功');
                   me.getCompanyGrid().getStore().load();
                   win.close();
                },
                failure: function(form, action) {
                	var state = action.result?action.result.statusCode:0,
                		errors = ['保存异常','公司信息已存在','用户名已存在','两次输入的密码不相同','图片类型错误'];
                    util.error(errors[0-parseInt(state)]);
                    btn.enable();
                }
            });
        }
	},
	useChild:function(btn){
		util.switchGridModel(this.getCompanyGrid(),'/site/company/switch');
	},
	detailCompany:function(btn){
		var me = this,tpl = new Ext.XTemplate(
		    '<div class="p-detail">',
		    '<img src="'+cfg.getPicCtx()+'/{LOGO_URL}" class="logo"/>',
		    '<ul>',
		    '<li><label>公司名称</label>{COMPANY}</li>',
		    '<li><label>公司法人</label>{LEGAL_PERSON}</li>',
		    '<li><label>许可证号</label>{LICENSE_NO}</li>',
		    '<li><label>品牌名称</label>{BRAND_NAME}</li>',
		    '<li><label>地址</label>{ADDRESS}</li>',
		    '<li><label>座机</label>{PHONE}</li>',
		    '<li><label>附件</label>',
		    '<a href="'+cfg.getPicCtx()+'/{LICENSE_ATTR}" target="_blank">下载许可证号附件</a>',
		    '<a href="'+cfg.getPicCtx()+'/{BUSINESS_URL}" target="_blank">下载营业执照附件</a>',
		    '</li>',
		    '<li><label>经营范围</label>{BUSINESS}</li>',
		    '</ul>',
		    '</div>'
		);
		Ext.create('Ext.window.Window',{
			title:util.windowTitle(util.glyphToFont(btn.glyph),''+me.getDetailData().COMPANY,''),
			width:600,
			height:300,
			modal:true,
			draggable:false,
			resizable:false,
   			layout:'fit',
   			bodyPadding:'5',
   			items:[{
   				tpl:tpl,
   				data:me.getDetailData()
   			}]
		}).show();
	},
	departWindow:function(title,btn){
		var me = this,
			cg = this.getCompanyGrid(),
			sel,win;
		if(sel = util.getSingleModel(cg)){
			win = Ext.create('Ext.window.Window',{
				title:util.windowTitle(util.glyphToFont(btn.glyph),title,''),
				width:400,
				height:210,
				modal:true,
				draggable:false,
				resizable:false,
	   			layout:'fit',
				items:[{
					xtype:'form',
					bodyPadding: '10 0 0 0',
					bodyStyle:'background:#fff;',
	    			scrollable:false,
	    			fieldDefaults: {
				        labelAlign: 'right',
				        labelWidth: 100,
				        msgTarget: 'side'
				    },
				    defaults:{
				    	anchor:'90%'
				    },
				    items: [{
				    	xtype:'hidden',
				    	name:'pm[ID]'
				    },{
				    	xtype:'hidden',
				    	name:'pm[COMPANY_ID]',
				    	value:(sel.get('ID'))
				    },{
						fieldLabel:'部门名称',
					    name : 'pm[TEXT]',
					    xtype: 'textfield',
					    allowBlank: false
					},{
						margin:'5 0 0 0',
						fieldLabel:'状态',
						xtype:'radiogroup',
			        	anchor: '70%',
			        	items: [
			                {boxLabel: '启用', name: 'pm[IS_USE]', inputValue: 0, checked: true},
			                {boxLabel: '禁用', name: 'pm[IS_USE]', inputValue: 1}
			            ]
					}],
					buttons: [{
						itemId:'save',
				        text: '保存',
				        disabled: true,
				        formBind: true,
				        handler:function(btn){
				        	var form = btn.up('form'),
				        		win = form.up('window');
				        	me.saveDepart(form.getForm(),win);
				        }
				    }]
				}]
			});
			return win;
		}
	},
	addDepart:function(btn){
		this.departWindow('添加部门',btn).show();
	},
	editDepart:function(btn){
		var rg = this.getDepartGrid(),
			win,form,sel,selData,formData={};
		if(sel = util.getSingleModel(rg)){
			win = this.departWindow('修改部门',btn);
			form = win.down('form');
			win.show();
			formData = util.pmModel(sel);
			form.getForm().setValues(formData);
		}
	},
	saveDepart:function(form,win){
		var me = this,
			cg = this.getCompanyGrid(),
			sel = cg.getSelection()[0],
			btn=win.down('button#save');
		if (form.isValid()) {
			btn.disable();
            form.submit({
            	submitEmptyText:false ,
            	url:cfg.getCtx()+'/site/depart/save',
                success: function(form, action) {
                   util.success('保存部门成功');
                   win.close();
                   me.getDepartGrid().getStore().load({params:{'COMPANY_ID':''+sel.get('ID')}});
                },
                failure: function(form, action) {
                	var state = action.result?action.result.statusCode:0,
                		errors = ['保存异常','部门名称已存在'];
                    util.error(errors[0-parseInt(state)]);
                    btn.enable();
                }
            });
        }
	},
	delDepart:function(btn){
		var sel = this.getCompanyGrid().getSelection()[0];
		util.delGridModel(this.getDepartGrid(),'/site/depart/del',{'COMPANY_ID':''+sel.get('ID')});
	},
	useDepart:function(btn){
		var sel = this.getCompanyGrid().getSelection()[0];
		util.switchGridModel(this.getDepartGrid(),'/site/depart/switch',{'COMPANY_ID':''+sel.get('ID')});
	},
	employeeWindow:function(title,btn){
	    var defaultTypeValue = '',
	    	hideEdit = false;
	    if(title.indexOf('修改')!=-1){
	    	hideEdit = true;
	    }
	    if(cfg.getUser().userType=='01'){
	    	defaultTypeValue= '0101';
	    }
	    if(cfg.getUser().userType=='02'){
	    	defaultTypeValue= '0201';
	    }
	    if(cfg.getUser().userType=='03'){
	    	defaultTypeValue= '0301';
	    }
	    
		var me = this,
			dg = this.getDepartGrid(),
			sel,win;
		if(sel = util.getSingleModel(dg)){
			win = Ext.create('Ext.window.Window',{
				title:util.windowTitle('',title,btn.icon),
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
				        labelWidth: 90,
				        msgTarget: 'side'
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
							xtype:'hidden',
							name:'pm[DEPART_ID]',
							value:(sel.get('ID'))
						},{
							xtype:'hidden',
							name:'pm[COMPANY_ID]',
							value:(sel.get('COMPANY_ID'))
						},{
							xtype:'hidden',
							name:'pm[CITY_ID]',
							value:(cfg.getUser().cityId)
						},{
							xtype:'hidden',
							name:'pm[CITY_NAME]',
							value:(cfg.getUser().cityName)
						},{
				        	fieldLabel:'登录名',
				        	maxLength:20,
				        	maxLengthText:'最多可填写20个中文字或字母数字',
				        	name : 'pm[USER_NAME]',
							xtype: 'textfield',
							allowBlank: hideEdit
				        },{
				        	fieldLabel: '启用账户',
				        	checked: true,
						    xtype: 'checkbox',
						    hidden:true,
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
							xtype:'hidden',
							name:'pm[USER_TYPE]',
							value:defaultTypeValue
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
				        	allowBlank: false,
				        	width:180,
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
				        	fieldLabel:'Email',
			        		vtype: 'email'
				        }]
				    }],
					buttons: [{
				        text: '保存',
				        disabled: true,
				        formBind: true,
				        handler:function(btn){
				        	var form = btn.up('form'),
				        		win = form.up('window');
				        	me.saveEmployee(form.getForm(),win);
				        }
				    }]
				}]
			});
			return win;
		}
		return null;
	},
	addEmployee:function(btn){
		var win = this.employeeWindow('添加员工',btn);
			if(win)win.show();
	},
	editEmployee:function(btn){
		var eg = this.getEmployeeGrid(),
			win,form,sel,selData,formData={};
		if(sel = util.getSingleModel(eg)){
			win = this.employeeWindow('修改员工',btn);
			form = win.down('form');
			win.show();
			formData = util.pmModel(sel);
			form.getForm().setValues(formData);
		}
	},
	saveEmployee:function(form,win){
		var me = this,
			cg = this.getDepartGrid(),
			sel = cg.getSelection()[0];
		if (form.isValid()) {
            form.submit({
            	submitEmptyText:false ,
            	url:cfg.getCtx()+'/b2b/user/save',
                success: function(form, action) {
                   util.success('保存员工成功');
                   win.close();
                   me.getEmployeeGrid().getStore().getProxy().setExtraParams({'DEPART_ID':''+sel.get('ID')});
                   me.getEmployeeGrid().getStore().load();
                },
                failure: function(form, action) {
                	var state = action.result?action.result.statusCode:0,
                		errors = ['保存异常','用户已存在','Email已存在','手机已存在'];
                    util.error(errors[0-parseInt(state)]);
                    win.close();
                }
            });
        }
	},
	delEmployee:function(btn){
		var sel = this.getDepartGrid().getSelection()[0];
		util.delGridModel(this.getEmployeeGrid(),'/b2b/user/del');
	},
	useEmployee:function(btn){
		var sel = this.getDepartGrid().getSelection()[0];
		util.switchGridModel(this.getEmployeeGrid(),'/b2b/user/switch');
	},
	powerWindow:function(title,btn){
		var me = this,
			win = Ext.create('Ext.window.Window',{
			title:util.windowTitle(util.glyphToFont(btn.glyph),title,''),
			width:650,
			height:420,
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
			sel = this.getCompanyGrid().getSelection()[0],
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
	},
	powerChild:function(btn){
		var me = this,
			cg = this.getCompanyGrid(),
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
	setUserPower:function(btn){
		var me=this,eg = this.getEmployeeGrid(),
			win,sel,mt;
		if(sel = util.getSingleModel(eg)){
			win = this.powerWindow('配置员工权限',btn);
			win.show();
			mt = this.getModuleTree();
			mt.getRootNode().expand(true,function(){
				me.setSelections(mt,'module',sel.get('ID'));
			});
		}
	},
	setBase:function(tree,url,title,moduleTree){
		var tms = tree.getChecked(),
			rg = this.getEmployeeGrid(),sel,models=[],
			moduleSel,moduleId;
		if(moduleTree){
			moduleSel = moduleTree.getSelection()[0];
			moduleId = moduleSel.get('id');
		}
		
		
		
		if(title.indexOf('公司权限')!=-1){
			rg = this.getCompanyGrid();
		}
		/*if(tms.length==0){	
			util.alert('没有选择数据');
			return;
		}*/
		if(sel = util.getSingleModel(rg)){
			var userId = sel.get('ID');
			if(title.indexOf('公司权限')!=-1){
				userId = sel.get('USER_ID');
			}
			for (var i = 0; i < tms.length; i++) { 
				models.push(tms[i].data);
			}
	       	Ext.Ajax.request({
				url:cfg.getCtx()+url,
				params:{models:Ext.encode(models),userId:userId,moduleId:moduleId},
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
		this.setBase(this.getModuleTree(),'/b2b/user/set/module',btn.up('window').title);
	},
	setPower:function(btn){
		this.setBase(this.getPowerTree(),'/b2b/user/set/power',btn.up('window').title,this.getModuleTree());
	},
	resetPwdChild:function(btn){
		var eg = btn.up('gridpanel'),
			sel,userId;
		if(sel = util.getSingleModel(eg)){
			userId = sel.get('ID');
			if(eg.xtype=='childcompanygrid'){
				userId = sel.get('USER_ID');
			}
			Ext.Ajax.request({
				url:cfg.getCtx()+'/b2b/user/resetPWD',
				params:{userId:userId},
				success:function(){
					util.success("重置密码成功，密码：111111");
				}
			});
		}
	},
	setManagerBase:function(eg,url){
		var sel,userId;
		if(sel = util.getSingleModel(eg)){
			userId = sel.get('ID');
			Ext.Ajax.request({
				url:cfg.getCtx()+url,
				params:{userId:userId},
				success:function(response, opts){
					var obj = Ext.decode(response.responseText),
						errors = ['设置失败！','该部门已设置经理','已设置客服','请填写手机号码','已设置财务短信通知'],
						state = obj?obj.statusCode:0;
					if(!obj.success){
						util.error(errors[0-parseInt(state)]);
					}else{
						util.success("设置成功！");
						eg.getStore().load();
					}
				},
				failure:function(){
					util.error("设置失败！");
				}
			});
		}
	},
	setManagerEmployee:function(){
		this.setManagerBase(this.getEmployeeGrid(),'/b2b/user/set/manager');
	},
	setCounselorEmployee:function(){
		this.setManagerBase(this.getEmployeeGrid(),'/b2b/user/set/counselor');
	},
	setServiceEmployee:function(){
		this.setManagerBase(this.getEmployeeGrid(),'/b2b/user/set/contacts');
	},
	setCaiWuMenu:function(){
		this.setManagerBase(this.getEmployeeGrid(),'/b2b/user/set/finance');
	},
	
	
	setWapChild:function(btn){
		var eg = btn.up('gridpanel'),
			sel,
			pv = cfg.getUser().userType;
		if(pv.length>2){pv = pv.substring(0,2);}
		if(sel = util.getSingleModel(eg)){
			var win = Ext.create('Ext.window.Window',{
				title:util.windowTitle(util.glyphToFont(btn.glyph),btn.getText()),
				width:'85%',
				height:'85%',
				modal:true,
				draggable:false,
				resizable:false,
				maximizable:false,
				maximized:false,
	   			layout:'fit',
	   			items:[{
	   				xtype:'form',
	    			scrollable:true,
					fieldDefaults: {
				        labelAlign: 'right',
				        labelWidth: 100,
				        msgTarget: 'qtip'
				    },
					bodyPadding: 5,
					defaults:{
						//readOnly:(pv=='01')
					},
	   				items:[{
	   					xtype:'hiddenfield',
	   					name:'pm[ID]'
	   				},{
	   					xtype:'hiddenfield',
	   					value:sel.get('ID'),
	   					name:'pm[COMPANY_ID]'
	   				},{
	   					xtype:'textfield',
	   					anchor:'100%',
	   					fieldLabel:'网站名称',
	   					allowBlank:false,
	   					name:'pm[TITLE]'
	   				},{
	   					xtype: 'fieldcontainer',
	   					fieldLabel:'域名',
		   		        labelWidth: 100,
		   		        layout: 'hbox',
		   		        items: [{
		   		        	name:'pm[DOMAIN]',
		   		            xtype: 'textfield',emptyText:'域名',itemId:'domain',
		   		            flex: 1
		   		        },{
		   		            xtype: 'splitter'
		   		        },{
		   		            xtype: 'textfield',
		   		            width:150,
		   		            itemId:'beianhao',
		   		            emptyText:'备案号',
		   					name:'pm[RECORD_NO]'
		   		        }]
	   				},{
	   					xtype:'filefield',
	   					anchor:'100%',
	   					fieldLabel:'LOGO',
	   					name:'LOGO',
						buttonConfig: {
			                text:'',
			                //disabled:(pv=='01'),
							ui:'default-toolbar',
			                glyph: 'xe648@iconfont'
			            }
	   				},{
	   					xtype:'filefield',
	   					anchor:'100%',
	   					fieldLabel:'徽标',
	   					name:'ICON',
						buttonConfig: {
			                text:'',
			                //disabled:(pv=='01'),
							ui:'default-toolbar',
			                glyph: 'xe648@iconfont'
			            }
	   				},{
	   					xtype:'filefield',
	   					anchor:'100%',
	   					fieldLabel:'二维码',
	   					name:'CODE',
						buttonConfig: {
			                text:'',
			                //disabled:(pv=='01'),
							ui:'default-toolbar',
			                glyph: 'xe648@iconfont'
			            }
	   				},{
	   					xtype:'filefield',
	   					anchor:'100%',
	   					fieldLabel:'微信二维码',
	   					name:'WX_IMG',
						buttonConfig: {
			                text:'',
			                //disabled:(pv=='01'),
							ui:'default-toolbar',
			                glyph: 'xe648@iconfont'
			            }
	   				},{
	   					xtype: 'fieldcontainer',
	   					fieldLabel:'支付宝',
		   		        labelWidth: 100,
		   		        layout: 'hbox',
		   		        items: [{
		   		        	itemId:'alipayAccount',
		   		        	name:'pm[SELLER_EMAIL]',
		   		            xtype: 'textfield',emptyText:'支付宝账号',
		   		            flex: 1
		   		        }, {
		   		            xtype: 'splitter'
		   		        }, {
		   		        	itemId:'alipayIdcard',
		   		            xtype: 'textfield',
		   		            flex: 1,
		   		            emptyText:'合作者身份号',
		   					name:'pm[PARTNER]'
		   		        }, {
		   		            xtype: 'splitter'
		   		        }, {
		   		        	itemId:'alipayKey',
		   		            xtype: 'textfield',
		   		            flex: 1,emptyText:'加密KEY',
		   					name:'pm[KEY]'
		   		        }]
	   				},{
	   					xtype: 'fieldcontainer',
	   					fieldLabel:'联系客服',
		   		        labelWidth: 100,
		   		        layout: 'hbox',
		   		        items: [{
		   		        	name:'pm[QQ1]',
		   		            xtype: 'textfield',emptyText:'QQ1',
		   		            flex: 1
		   		        },{
		   		            xtype: 'splitter'
		   		        },{
		   		            xtype: 'textfield',
		   		            flex: 1,
		   		            emptyText:'QQ2',
		   					name:'pm[QQ2]'
		   		        },{
		   		            xtype: 'splitter'
		   		        },{
		   		            xtype: 'textfield',
		   		            flex: 1,emptyText:'客服电话1',
		   					name:'pm[PHONE1]'
		   		        },{
		   		            xtype: 'splitter'
		   		        },{
		   		            xtype: 'textfield',
		   		            flex: 1,emptyText:'客服电话2',
		   					name:'pm[PHONE2]'
		   		        }]
	   				},{
	   			    	xtype: 'extkindeditor',
	   			    	anchor:'-110',
	   			    	height:300,
	   			    	fieldLabel:'关于我们',
	   					name:'ABOUT'
	   				}],
	   				buttons:[{
	   					text:'保存',
	   					formBind:true,
	   					disabled:true,
	   					handler:function(){
	   						var form = win.down('form'),
	   							f = form.getForm();
	   						if(f.isValid()){
	   							f.submit({
	   								submitEmptyText:false ,
	   								url:cfg.getCtx()+'/site/company/save/wap',
	   				                success: function(form, action) {
	   				                	win.close();
	   				                	util.success('网站设置成功');
	   				                },
	   				                failure: function(form, action) {
	   				                	var state = action.result?action.result.statusCode:0,
	   				                		errors = ['网站设置异常'];
	   				                    util.error(errors[0-parseInt(state)]);
	   				                }
	   							});
	   						}
	   					}
	   				}]
	   			}]
			}).show();
			
			win.mask('加载中...');
			Ext.Ajax.request({
				url:cfg.getCtx()+'/site/company/detail/wap',
				params:{companyId:sel.get('ID')},
				success:function(response){
					win.unmask();
					var form = win.down('form');
					var data = Ext.decode(response.responseText).data;
					
					form.down('textfield#domain').setReadOnly(pv!='01');
					form.down('textfield#beianhao').setReadOnly(pv!='01');
					
					//form.down('combo#platformAccount').setReadOnly(pv!='01');
					form.down('textfield#alipayAccount').setReadOnly(pv!='01');
					form.down('textfield#alipayIdcard').setReadOnly(pv!='01');
					form.down('textfield#alipayKey').setReadOnly(pv!='01');
					
					if(data.length>0){
						var d = data[0];
						var formData = util.pmModel({data:d});
						form.getForm().setValues(formData);
						form.down('extkindeditor').setValue(d.ABOUT);
						form.down('filefield[name=LOGO]').setRawValue(formData['pm[LOGO]']);
						form.down('filefield[name=ICON]').setRawValue(formData['pm[ICON]']);
						form.down('filefield[name=CODE]').setRawValue(formData['pm[CODE]']);
						form.down('filefield[name=WX_IMG]').setRawValue(formData['pm[WX_IMG]']);
					}
					
				}
			});
		}
	},
	setMapChild:function(btn){
		var eg = btn.up('gridpanel'),
		sel;
		if(sel = util.getSingleModel(eg)){
			Ext.create('Ext.window.Window',{
				title:util.windowTitle(util.glyphToFont(btn.glyph),btn.getText(),''),
				width:800,
		    	height:450,
				modal:true,
				draggable:false,
				resizable:false,
				layout:'fit',
				items:[Ext.create('Ext.ux.IFrame',{
					style:'width:100%;height:100%;',
					src:cfg.getCtx()+'/baidu/map?companyId='+sel.get('ID')
				})]
			}).show();
		}
	},
	contractChild:function(btn){
		var me = this,eg = btn.up('gridpanel'),
		sel;
		if(sel = util.getSingleModel(eg)){
			var store = util.createGridStore(cfg.getCtx()+'/site/company/list/contract?companyId='+sel.get('ID'),Ext.create('app.model.site.company.contractModel'));
			var bbar =  util.createGridBbar(store);
			var columns = Ext.create('app.model.site.company.contractColumn',{store:store});
			Ext.create('Ext.window.Window',{
				title:util.windowTitle(util.glyphToFont(btn.glyph),btn.getText(),''),
				width:'85%',
		    	height:'85%',
				modal:true,
				draggable:false,
				resizable:false,
				layout:'fit',
				items:[{
					xtype:'grid',
					bbar:bbar,
					columnLines: true,
					selType:'checkboxmodel',
					viewConfig:{
						stripeRows : true, // 奇偶行不同底色  
					    enableTextSelection : true  
					},
					store:store,
					columns:columns,
					dockedItems : [{
			        	xtype:'toolbar',
			        	style:'padding-left:5px;',
			        	layout: {
			                overflowHandler: 'Menu'
			            },
			        	items:[{
			        		xtype:'container',
			        		html:'<div class="green-color f14"> 已选公司 『 '+sel.get('COMPANY')+' 』 </div>'
			        	}/*,'-',{
			        		emptyText:'合同号/订单号',
				        	xtype:'searchfield',
				        	store:store
			        	},{
			    			xtype:'combo',
							name:'type',
							editable:false,
							hiddenName:'type',
							width:70,
							value:'',
							typeAhead: true,
				            triggerAction: 'all',
				            store: [
				            	['','状态'],
				                ['0','未使用'],
				                ['1','已使用'],
				                ['2','已核销'],
				                ['3','未废除'],
				                ['4','已废除']
				            ]
			    		},{
			    			name:'startTime',
			    			width:95,
			    			emptyText:'开始',
			    			xtype:'datefield',
			    			format:'Y-m-d',
			    			endDateField: 'endxddt',
					        itemId:'startxddt',
					        showToday:false,
					        vtype: 'daterange'
			    		},'-',{
			    			hideLabel:true,
			    			width:95,
			    			emptyText:'结束',
			    			xtype:'datefield',
			    			name:'endTime',
			    			format:'Y-m-d',
			    			itemId:'endxddt',
			    			showToday:false,
				            startDateField: 'startxddt',
				            vtype: 'daterange'
			    		}*/,'->',{
			        		text:'分配合同',
			        		handler:function(){
			        			me.assignContract(sel.get('ID'),this);
			        		}
			        	},{
			        		text:'删除合同',
			        		handler:function(){
			        			var myGrid = this.up('grid'),
			        				mySels = myGrid.getSelection(),
			        				isGo = true;
			        			Ext.Array.each(mySels,function(s,idx){
			        				if(s.get('STATUS')!='0'){
			        					isGo = false;
			        					return ;
			        				}
			        			});
			        			if(isGo){
			        				util.switchGridModel(this.up('grid'),'/site/company/del/contract',null,'未使用、未废除的合同才能删除');
			        			}else{
			        				util.alert('未使用的合同才能删除');
			        			}
			        		}
			        	},{
			        		text:'回收合同',
			        		hidden:true
			        	},{
			        		text:'核销合同',
			        		handler:function(){
			        			var myGrid = this.up('grid'),
			        				mySels = myGrid.getSelection(),
			        				isGo = true;
			        			Ext.Array.each(mySels,function(s,idx){
			        				if(s.get('STATUS')!='1'){
			        					isGo = false;
			        					return ;
			        				}
			        			});
			        			if(isGo){
			        				util.switchGridModel(myGrid,'/site/company/balance/contract',null,'已使用的合同才能核销');
			        			}else{
			        				util.alert('已使用的合同才能核销');
			        			}
			        		}
			        	}]
			        }]
				}]
			}).show();
		}
	},
	assignContract:function(useCompanyId,bbtn){
		Ext.create('Ext.window.Window',{
			title:util.windowTitle('','分配合同号',''),
			width:400,
	    	height:210,
			modal:true,
			draggable:false,
			resizable:false,
			dockedItems:[{
				xtype:'container',
				docked:'top',
				padding:'10 0 10 20',
				html:'<div class="red-color f14" ><i class="iconfont f20 red-color">&#xe6ae;</i> 每次最多可分配100个合同号</div>'
			}],
			layout:'fit',
			items:[{
				xtype:'form',
				buttons:[{
					text:'保存',
					disabled: true,
			        formBind: true,
			        handler:function(btn){
			        	var formPanel = btn.up('form'),
			        		form = formPanel.getForm(),
			        		win = formPanel.up('window');
			        	if (form.isValid()) {
			        		btn.disable();
			                form.submit({
			                	submitEmptyText:false ,
			                	url:cfg.getCtx()+'/site/company/save/contract?companyId='+useCompanyId,
			                    success: function(form, action) {
			                       util.success('保存数据成功');
			                       bbtn.up('gridpanel').getStore().load();
			                       win.close();
			                    },
			                    failure: function(form, action) {
			                    	var state = action.result?action.result.statusCode:0,
			                    		errors = ['保存异常','最多100条','合同号已存在','开始编号不能为空'];
			                        util.error(errors[0-parseInt(state)]);
			                        btn.enable();
			                    }
			                });
			            }
      				}
				}],
				items:[{
					padding:10,
					labelWidth:60,
					fieldLabel:'合同前缀',
					labelAlign:'right',
					xtype:'fieldcontainer',
					layout:'hbox',
					items:[{
						flex:1,
						name:'no',
						xtype:'textfield'
					}, {
			            xtype: 'splitter'
			        },{
						text:'从',
						xtype:'label',
						margin:'11 0 0 0'
					}, {
			            xtype: 'splitter'
			        },{
						width:60,
						minValue:0,
						//maxValue:100,
						value:0,
						name:'start',
						itemId:'beginNumber',
						xtype:'numberfield',
						allowBlank: false,
						enableKeyEvents:true,
						listeners:{
							keyup:function(nf){
								var ev = nf.up('fieldcontainer').down('numberfield#endNumber');
								ev.setMinValue(nf.getValue());
								ev.setMaxValue(99+nf.getValue());
							}
						}
					}, {
			            xtype: 'splitter'
			        },{
						text:'到',
						xtype:'label',
						margin:'11 0 0 0'
					}, {
			            xtype: 'splitter'
			        },{
						width:60,
						name:'end',
						minValue:1,
						value:99,
						xtype:'numberfield',
						itemId:'endNumber',
						allowBlank: false
					}]
				}]
			}]
		}).show();
	},
	addRoleChild:function(btn){
		Ext.create('Ext.window.Window',{
			title:util.windowTitle(util.glyphToFont(btn.glyph),'创建角色',''),
			width:'85%',
	    	height:'85%',
			modal:true,
			draggable:false,
			resizable:false,
			layout:'fit',
			items:[{
				xtype:'companyRole'
			}]
		}).show();
	},
	giveRoleBase:function(btn,grid,sel,type){
		var hasRole = sel.get('CHILD_ROLE_ID')||'',
			companyGrid = grid.up().down('childcompanygrid'),
			companyId = companyGrid.getSelection()[0].get('ID');
		var win = util.createEmptyWindow('分配角色',util.glyphToFont(btn.glyph),360,(hasRole==''?190:240),[{
			layout:'anchor',
			items:[{
				xtype:'container',
				hidden:hasRole=='',
				style:'padding:2px 0 10px 55px;',
				html:'<div class="blue-color f14" >当前角色：'+sel.get('CHILD_ROLE_NAME')+'</div>'
			},{
				name:'pm[USER_ID]',
				xtype:'hidden',
				value:sel.get(type==0?'USER_ID':'ID')
			},{
				fieldLabel:'角色',
				labelWidth:50,
				anchor:'90%',
				allowBlank:false,
				xtype:'combo',
    			emptyText:'请选择角色',
    			name:'pm[ROLE_ID]',
    			hiddenName:'pm[ROLE_ID]',
			    queryMode: 'remote',
			    triggerAction: 'all',
			    focusOnToFront:true,
			    forceSelection:true,
			    displayField: 'ROLE_NAME',
			    valueField: 'ID',
			    minChars:1,
			    pageSize:20,
			    listConfig:{
			    	minWidth:360
			    },
			    store:Ext.create('Ext.data.Store',{
			    	pageSize:20,
			    	autoLoad:true,
			    	proxy: {
			    		type: 'ajax',
				        noCache:true,
			            url: cfg.getCtx()+'/site/company/list/role?roleType='+type+'&companyId='+companyId,
			            reader: {
			            	rootProperty: 'data',
		            		totalProperty: 'totalSize'
			            }
			        },
			    	model:Ext.create('Ext.data.Model',{
			        	fields: ['ID','ROLE_NAME']
			        })
			    })
			}]
		}],[{
			text:'确定',
			formBind: true,
			handler:function(){
				var form = win.down('form'),
					f = form.getForm();
				if(f.isValid()){
					f.submit({
						submitEmptyText:false ,
						url:cfg.getCtx()+'/site/company/give/role',
		                success: function(form, action) {
		        			util.success('分配角色成功！');
							win.close();
							grid.getStore().load();
		                },
		                failure: function(form, action) {
		                	var state = action.result?action.result.statusCode:0,
		                		errors = ['分配角色异常'];
		                    util.error(errors[0-parseInt(state)]);
		                }
					});
				}
			}
		},{
			text:'取消',
			cls:'disable',
			handler:function(){
				win.close();
			}
		}]).show();
	},
	giveRoleChild:function(btn){
		var me = this,eg = btn.up('gridpanel'),
		sel;
		if(sel = util.getSingleModel(eg)){
			me.giveRoleBase(btn,eg,sel,0);
		}
	},
	giveRoleEmployee:function(btn){
		var me = this,eg = btn.up('gridpanel'),
		sel;
		if(sel = util.getSingleModel(eg)){
			me.giveRoleBase(btn,eg,sel,1);
		}
	},
	czChild:function(btn){
		var me = this,eg = btn.up('gridpanel'),
		sel;
		if(sel = util.getSingleModel(eg)){
			var win = util.createEmptyWindow(
				'充值',
				util.glyphToFont(btn.glyph),400,240,[
					{
						xtype:'hidden',
						value:sel.get('ID'),
						name:'pm[COMPANY_ID]'
					},{
			        	fieldLabel:'金额',
			        	width:180,
			        	minValue:0,
			        	name : 'pm[MONEY]',
						xtype: 'numberfield',
						allowBlank:false
				    },{
				    	xtype:"textareafield",
				    	fieldLabel:"备注",
				    	name:'pm[NOTE]',
						rows:3,cols:20,
						anchor:'90%',
						allowBlank:false 
					}
				],[{
					text:'确定',
					handler:function(b){
						b.disable();
						var form = win.down('form'),
							f = form.getForm();
						if(f.isValid()){
							f.submit({
								submitEmptyText:false ,
								url:cfg.getCtx()+'/site/company/update/money',
				                success: function(form, action) {
				                	eg.getStore().load();
				                	win.close();
				                	util.success('充值成功');
				                },
				                failure: function(form, action) {
				                	b.enable();
				                	var state = action.result?action.result.statusCode:0,
				                		errors = ['充值异常'];
				                    util.error(errors[0-parseInt(state)]);
				                }
							});
						}
					}
				}]
			).show();
		}
	},
	payPasswordMenu:function(btn){
		var me = this,eg = this.getEmployeeGrid(),
		sel;
		if(sel = util.getSingleModel(eg)){
			var win = util.createEmptyWindow(
				'设置支付密码',
				util.glyphToFont(btn.glyph),360,150,[
					{
						xtype:'hidden',
						value:sel.get('ID'),
						name:'pm[ID]'
					},{
						xtype: 'fieldcontainer',
						layout: 'hbox',
						style:'margin-top:10px;',
			        	fieldLabel:'密码',
			        	items: [{
			        		width:180,
			        		minLength:6,
				        	name : 'pm[PAY_PWD]',
							xtype: 'textfield',
							allowBlank:false
			            },{
			            	xtype: 'splitter'
			            },{
			                xtype: 'button',
			                ui:'default-toolbar',
			                glyph:'xe60f@iconfont',
			                tooltip:'随机密码',
			                handler:function(b){
			                	var chars = "abcdefghijkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789"; 
			                	var randomChars = ""; 
		                	    for(var x=0; x<6; x++) { 
		                	        var i = Math.floor(Math.random() * chars.length); 
		                	        randomChars += chars.charAt(i); 
		                	    }
		                	    b.previousSibling().previousSibling().setValue(randomChars);
			                }
			            }],
			        	anchor:'100%'
				    }
				],[{
					text:'确定',
					handler:function(b){
						
						var form = win.down('form'),
							f = form.getForm();
						if(f.isValid()){b.disable();
							f.submit({
								submitEmptyText:false ,
								url:cfg.getCtx()+'/site/company/init/pay/pwd',
				                success: function(form, action) {
				                	eg.getStore().load();
				                	win.close();
				                	util.success('支付密码设置成功');
				                },
				                failure: function(form, action) {
				                	b.enable();
				                	var state = action.result?action.result.statusCode:0,
				                		errors = ['支付密码设置异常'];
				                    util.error(btn.text+errors[0-parseInt(state)]);
				                }
							});
						}
					}
				}]
			).show();
		}
	},
	setDepartMenu:function(m){
		var me = this,eg = this.getEmployeeGrid(),
		sel,win;
		if(sel = util.getSingleModel(eg)){
			var selectorStore = Ext.create('Ext.data.Store',{
	        	model:Ext.create('Ext.data.Model',{
	        		fields: ['COMPANY_ID','TEXT','ID']
	        	}),
	        	autoLoad: true, 
	            proxy: {
	                type: 'ajax',
	                noCache:true,
	                url: cfg.getCtx()+'/site/depart/list?COMPANY_ID='+sel.get('COMPANY_ID'),
	                reader: {
	                    type: 'json',
	                    rootProperty: 'data',
	                    totalProperty:'totalSize'
	                }
	           	}
	        });
			win = util.createEmptyWindow('部门查看权限',util.glyphToFont(m.glyph),800,450,[{
				xtype:'hiddenfield',
				name:'pm[COMPANY_ID]',
				value:sel.get('COMPANY_ID')
			},{
				xtype:'hiddenfield',
				name:'pm[USER_ID]',
				value:sel.get('ID')
			},{
				name:'pm[DEPART_ID]',
				xtype: 'itemselector',
				anchor:'100% 100%',
	            store: selectorStore,
	            displayField: 'TEXT',
	            valueField: 'ID',
	            //value: ['3', '4', '6'],
	            //allowBlank: false,
	            msgTarget: 'side',
	            fromTitle: '部门列表',
	            toTitle: '已选列表',
	            buttons: ['add', 'remove'],
	            buttonsText: {
	                add: "添加",
	                remove: "移除"
	            },
			}],[{
				text:'确定',
				formBind:true,
				disabled:true,
				handler:function(btn){
					var form = win.down('form'),
						f = form.getForm();
					if(f.isValid()){
						btn.disable();
						f.submit({
							submitEmptyText:false ,
							url:cfg.getCtx()+'/site/company/save/choose/depart',
			                success: function(form, action) {
			                	win.close();
			                	util.success('部门权限设置成功');
			                },
			                failure: function(form, action) {
			                	var state = action.result?action.result.statusCode:0,
			                		errors = ['部门权限设置异常'];
			                    util.error(errors[0-parseInt(state)]);
			                    win.close();
			                }
						});
					}
				}
			},{
				text:'取消',
				cls:'disable',
				handler:function(){win.close();}
			}]).show();
			win.mask('加载中...');
			Ext.Ajax.request({
				url:cfg.getCtx()+'/site/company/choose/depart',
				params:{userId:sel.get('ID')},
				success:function(response){
					win.unmask();
					var form = win.down('form');
					var data = Ext.decode(response.responseText).data;
					if(data.length>0){
						form.down('itemselector').setValue(data);
					}
					
				}
			});
		}
	}
});