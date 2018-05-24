Ext.define('app.controller.site.setting', {
	extend: 'app.controller.common.BaseController',
	views:['Ext.ux.form.field.CardNumber','site.settingCompanyGrid','site.settingChildCompanyGrid','common.CityCombo','Ext.ux.form.ItemSelector'],
	stores:['City'],
	config: {
		control: {
			'toolbar[itemId=firsttool] button[itemId=opencity]':{
				click:'openCity'
			},
			'toolbar[itemId=firsttool] button[itemId=ckmanager]':{
				click:'onAddManager'
			},
			'toolbar[itemId=firsttool] button[itemId=edmanager]':{
				click:'onEditManager'
			},
			'toolbar[itemId=firsttool] button[itemId=bankNoManager]':{
				click:'bankManager'
			},
			'tabpanel toolbar button[itemId=bankNoChild]':{
				click:'bankManager'
			},
			'toolbar[itemId=firsttool] button[itemId=isuse]':{
				click:'onEnable'
			},
			'toolbar[itemId=firsttool] button[itemId=style]':{
				afterrender:'onBtnRender',
				click:'onStyle'
			},
			'toolbar[itemId=firsttool] button[itemId=style] menu':{
	        	click:'onBtnMenuClick'
	        },
			'tabpanel toolbar button[itemId=addChild]':{
				click:'onAdd'
			},
			'tabpanel toolbar button[itemId=editChild]':{
				click:'onEdit'
			},
			'tabpanel toolbar button[itemId=auditChild]':{
				click:'onAudit'
			},
			'tabpanel toolbar button[itemId=useChild]':{
				click:'onChildEnable'
			},
			'tabpanel toolbar button[itemId=recommendChild]':{
				click:'recommendChild'
			},
			'tabpanel toolbar button[itemId=cooperationChild]':{
				click:'cooperationChild'
			},
			'tabpanel toolbar button[itemId=grantChild]':{
				afterrender:function(btn){
					var me = this;
					btn.setMenu([{
						glyph:'xe683@iconfont',
		        		text:'单卖交通',
		        		handler:function(){
		        			me.grantChild('traffic');
		        		}
					},{
						glyph:'xe682@iconfont',
						text:'单卖地接',
		        		handler:function(){
		        			me.grantChild('route');
		        		}
					},{
						glyph:'xe63e@iconfont',
						text:'线路共享',
		        		handler:function(){
		        			me.shareChild();
		        		}
					}]);
				}
			},
			'tabpanel toolbar button[itemId=aloneChild]':{
				click:'aloneChild'
			},
			'tabpanel toolbar button[itemId=contractChild]':{
				click:'contractChild'
			},
			'tabpanel toolbar button[itemId=setCityChild]':{
				click:'setCityChild'
			},
			'settingcompanygrid':{
				//afterrender:function(g){util.addCommonContextMenu(g);},
				select:'onSiteGridRowSelect'
			},
			'dataview[itemId=cityview]':{
				afterrender:function(g){
					g.getStore().on('load',function(){
						g.getSelectionModel().select(0);
					});
				},
				selectionchange:'onSelectCityChange'
			},
			'tabpanel':{
				afterrender:'onTabRender',
				tabchange:'onTabChange'
			}
		},
		refs: {
			siteGrid:'settingcompanygrid',
			firstTool:'toolbar[itemId=firsttool]',
			tabPanel:'tabpanel',
			secondTool:'tabpanel toolbar',
			cityView:'dataview[itemId=cityview]',
			styleBtn :'toolbar[itemId=firsttool] button[itemId=style]',
			editBtn:'tabpanel toolbar button[itemId=editChild]'
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
			        	btns = [],
			        	cbtns = [];
		        	for(var i=0;i<items.length;i++){
		        		delete items[i].checked;
			        	if(items[i].itemId.indexOf('Child')!=-1){
			        		if(items[i].itemId.indexOf('grant')!=-1){
			        			Ext.applyIf(items[i],{hidden:true});
			        		}
			        		cbtns.push(items[i]);
			        	}else{
			        		btns.push(items[i]);
			        	}
			        }
			        
			        me.getFirstTool().removeAll();
			        me.getFirstTool().add(btns);
			    }
			});	
		},500);
	},openCity:function(btn){
		var me = this,win = Ext.create('Ext.window.Window',{
			id:'OpenCityWindow',
			title:util.windowTitle(util.glyphToFont(btn.glyph),'城市配置',btn.iconCls),
			autoShow:true,
			y:0,
			width:700,
			height:360,
			modal:true,
			draggable:false,
			resizable:false,
   			layout:'fit',
   			listeners:{
   				show:function(w){
   					me.loadAlreadyOpenCity(w);
   				}
   			},
			items:[{
			    xtype:'panel',
			    layout:'border',
				bodyPadding: 5,
				bodyStyle:'background:#fff;',
    			scrollable:false,
    			fieldDefaults: {
			        labelAlign: 'right',
			        labelWidth: 60,
			        msgTarget: 'side'
			    },
			    defaults:{
			    	anchor:'100%'
			    },
			    items: [{
					itemId:'cityquery',
					title:'选择并开通城市',
	    			region:'east',
	    			width:210,
	    			bodyPadding:'5',
	    			items:[{
	    				width:200,
	    				xtype:'citycombo',
				    	listeners:{
				    		beforequery:function(queryPlan, eOpts){
				    			var combo = queryPlan.combo,
									store = combo.getStore();
								store.getProxy().setExtraParams({type:1});
				    		},
				    		beforeselect:function(combo, record, index, eOpts){
				    			me.onCityTagSelect.apply(me,[combo, record, index, eOpts])
				    		}
				    	}
	    			}],
	    			border:true
				},{
					itemId:'cityitems',
					title:'已开通城市',
					region:'center',
					margin:'0 5 0 0',
					bodyPadding:'5',
					border:true
				}]
			}]
		});
	},
	loadAlreadyOpenCity:function(win){
		var me = this;
		Ext.Ajax.request({
			url:cfg.getCtx()+'/site/setting/list',
			success:function(response, opts){
				var ids = Ext.decode(response.responseText).data,
					cityPanel = win.down('panel').down('[itemId=cityitems]');
					
					
					
				
				cityPanel.removeAll(true);
				for(var i=0;i<ids.length;i++){
					cityPanel.add({
						cls:'tag-split-btn'+(ids[i].IS_USE=='1'?' disable':''),
						margin:'0 5 5 0',
						xtype:'splitbutton',
						text:ids[i].SUB_AREA,
						itemId:ids[i].CITY_ID,
						menu: [{
							isUse:ids[i].IS_USE,
	                        text:ids[i].IS_USE=='1'?'启用':'禁用',
	                        glyph:ids[i].IS_USE=='1'?'xe674@iconfont':'xe677@iconfont',
	                        handler:function(m){
	                        	me.disableCityTag(m.up('splitbutton').itemId,m.up('splitbutton'),m.isUse);
	                        }
	                    }]
					});
				}
			}
		});
	},
	onCityTagSelect:function(combo, record, index, eOpts){
		var me = this,
			cityPanel = combo.up('[itemId=cityquery]').nextSibling('[itemId=cityitems]');
		Ext.Ajax.request({
			url:cfg.getCtx()+'/site/setting/addSite',
			params:{
				'pm[CITY_ID]':record.get('ID'),
				'pm[MAIN_AREA]':record.get('PTEXT'),
				'pm[SUB_AREA]':record.get('TEXT'),
				'pm[PINYIN]':record.get('PINYIN'),
				'pm[IS_USER]':'0'
			},
			success:function(response, opts){
				var obj = Ext.decode(response.responseText);
				if(!obj.success){
					util.error(record.get('TEXT')+'已存在，不能重复选择');
					combo.clearValue();
				}else{
					me.loadAlreadyOpenCity(Ext.getCmp('OpenCityWindow'));
					combo.clearValue();
				}
			}
		});
	},
	disableCityTag:function(entityId,btn,use){
		var me = this;
		Ext.Ajax.request({
			url:cfg.getCtx()+'/site/setting/editSiteUseStatus',
			params:{
				'CITY_ID':entityId,
				'USE_STATUS':(1-parseInt(use))
			},
			success:function(response, opts){
				var d=Ext.decode(response.responseText),
					state = d.statusCode||0,
                	errors = ['操作异常','该城市正在使用中，无法禁用'];
                if(!d.success){
                    util.error(errors[0-parseInt(state)]);
                }
				me.loadAlreadyOpenCity(Ext.getCmp('OpenCityWindow'));
			}
		});
	},
	managerWindow:function(title,ctype,btn){
	    var types,defaultValue = 0,
	    	defaultUserType,
	    	cv = this.getCityView(),
			cvv = Ext.get(cv.getSelectedNodes()[0]),
			cityId='',cityName='',hideCitys=true;
	    if(title.indexOf('站长')!=-1){
	    	types =[{text:'平台管理',value:0}];
	    	defaultValue = 0;
	    	hideCitys=false;//管理城市 旅行社供应商不显示
	    	defaultUserType = '01';
	    }else{
	    	
	    }
	    if(title.indexOf('供应商')!=-1){
	    	types =[{text:'供应商',value:1}];
	    	defaultValue = 1;
	    	cityId = cvv.getAttribute('cityId');
	    	cityName = cvv.getAttribute('cityName');
	    	defaultUserType = '02';
	    } 
	    if(title.indexOf('旅行社')!=-1){
	    	defaultValue = 2;
	    	types =[
		    	{text:'旅行社',value:2},
//		        {text:'门市',value:3},
		        {text:'分公司',value:4},
		        {text:'门市部',value:5},
		        {text:'旅游顾问',value:6}
	        ];
	        cityId = cvv.getAttribute('cityId');
	    	cityName = cvv.getAttribute('cityName');
	    	defaultUserType = '03';
	    }   
	         
	    var store = util.createComboStore(types),
	    	me = this,
			win = Ext.create('Ext.window.Window',{
				title:util.windowTitle(util.glyphToFont(btn.glyph),title),
				width:'85%',
				height:'85%',
				modal:true,
				scrollable:true,
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
							value:defaultUserType
						},{
							xtype:'fieldcontainer',
				        	layout: {
						        type: 'hbox',
						        pack: 'start',
						        align: 'stretch'
						    },
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
								hiddenName:'pm[TYPE]',
								editable:false,
								emptyText:'公司类型',
								value:defaultValue,
								allowBlank: false,
					            valueField: 'value',
					            displayField: 'text',
					            store:store,
					            minChars: 0,
					            queryMode: 'local',
					            typeAhead: true,
					            listeners:{
					            	change:function(combo, newValue,oldValue, eOpts){
					            		var xzfh = combo.up('fieldcontainer').nextSibling('fieldcontainer');
					            		if(combo.getValue()=='6'){
					            			xzfh.setFieldLabel('身份证号');
					            		}else{
					            			xzfh.setFieldLabel('许可证号');
					            		}
					            		/*var accountWay = combo.up('form').down('combo#accountWay');
					            		if(combo.getValue()=='2'){//旅行社 供应商
					            			accountWay.setDisabled(false);
					            		}else{
					            			accountWay.setDisabled(true);
					            		}*/
					            	}
					            }
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
							fieldLabel:'公章图片',
							anchor:'100%',
							xtype:'filefield',
							name:'COMPANY_SIGN',
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
						},{
							xtype:'fieldcontainer',
				        	layout: 'hbox',
							fieldLabel:'公司简称',
							items:[{
								width:180,
								xtype:'textfield',
								name : 'pm[SHORT_NAME]'
							}, {
					            xtype: 'splitter'
					        },{
								flex:1,
								xtype:'filefield',
								emptyText:'Word模版附件',
								name : 'WORD_TPL',
								fileInputAttributes: {
							        accept: '.docx'
							    },
								buttonConfig: {
					                text:'',
									ui:'default-toolbar',
					                glyph: 'xe648@iconfont'
					            }
							}]
						},{
							xtype:'fieldcontainer',
							layout:'hbox',
							fieldLabel:'补单设置',
							items:[{
								width:180,
								fieldLabel:'延迟天数',
								labelWidth:55,
								maxValue:0,
								minValue:-99,
								xtype:'numberfield',
								name : 'pm[OLD_DELAY_DAY]',
								emptyText:'延迟天数'
							},{
								xtype:'splitter'
							},{
								fieldLabel:'审核天数',
								labelWidth:55,
								maxValue:99,
								minValue:0,
								name : 'pm[OLD_AUDIT_DAY]',
								xtype:'numberfield',
								emptyText:'审核天数'
							}]
						},{
							xtype:'combo',
							width:245,
		   					itemId:'accountWay',
							name:'pm[ACCOUNT_WAY]',
							hiddenName:'pm[ACCOUNT_WAY]',
							fieldLabel:'对账方式',
							//hidden:(defaultUserType=='02'||defaultUserType=='01'),
							//disabled:(defaultUserType=='02'||defaultUserType=='01'),
							allowBlank: title.indexOf('旅行社')==-1,
							forceSelection: true,
				            valueField: 'value',
				            displayField: 'text',
				            editable:false,
				            emptyText:'选择对账方式',
				            store:util.createComboStore([{
				            	value:'1',
				            	text:'门市/分公司独立对账'
				            },{
				            	value:'2',
				            	text:'总部对账'
				            },{
				            	value:'3',
				            	text:'平台对账'
				            }]),
				            //minChars: 0,
				            queryMode: 'local',
				            typeAhead: true,
				            listeners:{
				            	change:function(combo, newValue, oldValue, eOpts){
				            		if(oldValue&&!combo.abc){
				            			if(oldValue!=newValue){
				            				var win = Ext.create('Ext.window.Window',{
				        			   			title: util.windowTitle('','信息提示',''),
				        			   			width:360,
				        			   			height:180,
				        			   			draggable:false,
				        						resizable:false,
				        						closable:false,
				        						modal:true,
				        			   			layout:'fit',
			        			   				items:[{
			        			   					bodyStyle:'background:#fff;padding:10px;',
			        				   				html:'<h3 class="f16 ht30 red-color">更换对账方式注意：</h3><div class="ht20 f14">请确认对账数据是否已对完，未对账完成的数据会丢失，是否更换？</div>'
			        				   			}],
			        				   			buttons:[{
			        				   				text:'我要更换',
			        				   				handler:function(){
			        				   					win.close();
			        				   					combo.abc=false;
			        				   				}
			        				   			},{
			        				   				text:'不更换',
			        				   				cls:'disable',
			        				   				handler:function(){
			        				   					win.close();
			        				   					combo.abc=true;
			        				   					combo.setValue(oldValue);
			        				   					combo.abc=false;
			        				   				}
			        				   			}]
				        			   		}).show();
				            			}
				            		}
				            	}
				            }
						},{xtype:'hidden',name:'pm[USER_ID]'},{xtype:'hidden',name:'pm[ID]'},{xtype:'hidden',name:'pm[SOURCE]',value:0},{xtype:'hidden',name:'pm[CITY_ID]',value:cityId},{xtype:'hidden',name:'pm[CITY_NAME]',value:cityName}]
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
						    inputType: 'password',
						    minLength: 6,
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
							fieldLabel:'管理城市',
							forceSelection: true,
							name : 'pm[SITE_IDS]',
							hiddenName:'pm[SITE_IDS]',
							width:215,
							hidden:hideCitys,
						    xtype: 'tagfield',
						    displayField: 'SUB_AREA',
        					valueField: 'ID',
        					listeners:{
        						change:function( combo, newValue, oldValue, eOpts){
								    var res = [], hash = {};
								    for(var i=0, elem; (elem = newValue[i]) != null; i++)  {
								        if (!hash[elem])
								        {
								            res.push(elem);
								            hash[elem] = true;
								        }
								    }
								    combo.setValue(res);
        						}
        					},
						    store:Ext.create('Ext.data.Store',{
						    	model:Ext.create('Ext.data.Model',{
						    		fields:['ID','CITY_ID','MAIN_AREA','SUB_AREA']
						    	}),
						    	proxy: {
							    	autoLoad:false,
							        type: 'ajax',
							        noCache:true,
							        url: cfg.getCtx()+'/site/setting/usableSites',
							        reader: {
							            type: 'json',
							            rootProperty: 'data',
							            totalProperty:'totalSize'
							        }
							   	}
						    })
						},{
							itemId:'roleField',
							fieldLabel:'分配角色',
							forceSelection: true,
							editable:false,
							allowBlank: !hideCitys,
							hidden:!hideCitys,
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
						    	autoLoad:true,
						    	proxy: {
							        type: 'ajax',
							        noCache:true,
							        url: cfg.getCtx()+'/b2b/role/listRole?roleType='+(defaultValue+1),
							        reader: {
							            type: 'json',
							            rootProperty: 'data',
							            totalProperty:'totalSize'
							        }
							   	}
						    })
						}]
					}],
					buttons: [{
				        text: '保存',
				        disabled: true,
				        formBind: true,
				        ctype:ctype,
				        handler:function(btn){
				        	var form = btn.up('form'),
				        		win = form.up('window');
				        	me.onManagerSave(form.getForm(),win,btn);
				        }
				    }]
				}]
			});
		return win;
	},
	onAddManager:function(btn){
		this.managerWindow('创建站长',1,btn).show();
	},
	onEditManager:function(btn){
		var rg = this.getSiteGrid(),
			win,form,sel,selData,formData={};
		if(sel = util.getSingleModel(rg)){
			win = this.managerWindow('修改站长',1,btn);
			form = win.down('form');
		var fs = form.down('panel[itemId=account]'),
			cs = form.down('panel[itemId=companyfieldset]'),tfs;
			if(fs){
				tfs = fs.query('textfield:not(tagfield)');
				for(var i=0;i<tfs.length;i++){
					tfs[i].disable().hide();
				}
			}
			win.show();
			formData = util.pmModel(sel);
			
			form.getForm().setValues(formData);
			if(cs){
				cs.down('filefield[name=LICENSE_ATTR]').setRawValue(formData['pm[LICENSE_ATTR]']);
				cs.down('filefield[name=LOGO_URL]').setRawValue(formData['pm[LOGO_URL]']);
				cs.down('filefield[name=BUSINESS_URL]').setRawValue(formData['pm[BUSINESS_URL]']);
			}
			Ext.Ajax.request({
				url:cfg.getCtx()+'/site/company/ownerSites',
				params:{
					'USER_ID':sel.get('USER_ID')
				},
				success:function(response, opts){
					var obj = Ext.decode(response.responseText),
						data = obj.data,
						arr = [];
					for(var i=0;i<data.length;i++){
						arr.push(data[i].SITE_ID);
					}
					fs.down('tagfield').getStore().getProxy().setExtraParams({type:'edit', 'USER_ID': sel.get('USER_ID')});
					fs.down('tagfield').getStore().load({
						callback:function(){
							fs.down('tagfield').setValue(arr);
						}
					});
					
				}
			});
			
		}
	},
	onManagerSave:function(form,win,btn){
		var me = this,ctype=btn.ctype;
		if (form.isValid()) {
			/*var accountWay = win.down('combo#accountWay'),
				accountValue = accountWay.getValue()||'';
			if(!accountWay.isDisabled()){
				if(accountValue==''){
					util.alert('请选择对账方式');
					return;
				}
			}*/
			btn.disable();
            form.submit({
            	submitEmptyText:false ,
            	url:cfg.getCtx()+'/site/company/save',
                success: function(form, action) {
                   util.success('保存数据成功');
                   if(ctype==1){
						me.getSiteGrid().getStore().load();
						me.getCityView().getStore().load();
                   }else{
                   		me.getTabPanel().getActiveTab().getStore().load();
                   }
                   win.close();
                },
                failure: function(form, action) {
                	var state = action.result?action.result.statusCode:0,
                		errors = ['保存异常','公司信息已存在','用户名已存在','两次输入的密码不相同','图片类型错误','文件格式只支持.docx'];
                    util.error(errors[0-parseInt(state)]);
                    btn.enable();
                }
            });
        }
	},
	onEnable:function(){
		util.switchGridModel(this.getSiteGrid(),'/site/company/switch');
	},
	onStyle:function(btn){
		util.alert('点击按钮箭头选择一个城市');
	},
	onBtnRender:function(btn){
		Ext.Ajax.request({
		    url: cfg.getCtx()+'/site/company/ownerSites',
		    success: function(response, opts) {
		        var obj = Ext.decode(response.responseText),
		        	items = obj.data,
		        	menus = [];
		        for(var i=0;i<items.length;i++){
		        	menus.push({
		        		itemId:items[i].CITY_ID,
		        		pinyin:items[i].PINYIN,
		        		text:items[i].SUB_AREA
		        	});
		        }
		        btn.setMenu(menus);
		    }
		});
	},
	onBtnMenuClick:function(m,item){
		var win = Ext.create('Ext.window.Window',{
			title:util.windowTitle('&#xe632;','站个性化'),
			width:500,
			height:180,
			modal:true,
			draggable:false,
			resizable:false,
   			layout:'fit',
   			listeners:{
   				afterrender:function(win){
   					Ext.Ajax.request({
					    url: '/site/setting/attr?cityId='+item.itemId,
					    success: function(response, opts) {
					        var obj = Ext.decode(response.responseText),
					        	data = obj.data[0];
					        var formData = {};
					        for(var i in data){
								formData['pm['+i+']'] = data[i];
							}
							win.down('form').getForm().setValues(formData);
					    }
					 });
   					
   				}
   			},
			items:[{
				xtype:'form',
				fieldDefaults: {
			        labelAlign: 'right',
			        labelWidth: 60,
			        msgTarget: 'side'
			    },
				bodyPadding: '10 10 0 0',
				items:[{
					xtype:'fieldcontainer',
					layout: 'hbox',
					fieldLabel: '客服电话',
			        items: [{
			        	xtype:'hidden',
			        	name:'pm[ID]'
			        },{
			        	xtype:'hidden',
			        	name:'pm[CITY_ID]',
			        	value:item.itemId
			        },{
			        	xtype:'hidden',
			        	name:'pm[CITY_NAME]',
			        	value:item.text
			        },{
			            xtype: 'textfield',
			            name:'pm[SERVICE_PHONE]',
			            flex: 1
			        }, {
			            xtype: 'splitter'
			        }, {
			            xtype: 'textfield',
			            name:'pm[QQ1]',
			            emptyText:'QQ1',
			            flex: 1
			        }, {
			            xtype: 'splitter'
			        }, {
			            xtype: 'textfield',
			            name:'pm[QQ2]',
			            emptyText:'QQ2',
			            flex: 1
			        }]
				}]
			}],
			buttons:[{
				text:'确定',
				handler:function(btn){
					var f = win.down('form'),
		        		form = f.getForm();
		        	if (form.isValid()) {
		        		btn.disable();
			            form.submit({
			            	submitEmptyText:false ,
			            	url:cfg.getCtx()+'/site/setting/attr/save',
			                success: function(form, action) {
			                   util.success('设置个性化成功');
			                   win.close();
			                },
			                failure: function(form, action) {
			                	var state = action.result?action.result.statusCode:0,
			                		errors = ['设置个性化失败'];
			                    util.error(errors[0-parseInt(state)]);
			                    win.close();
			                }
			            });
			        }
				}
			}]
		}).show();
	},
	onSiteGridRowSelect:function(grid, record, rowIndex,  eOpts){
		var rg = grid,
			sel,
			ct = this.getCityView(),
			me = this;
		if(sel = util.getSingleModel(rg)){
			ct.getStore().load({params:{
				'USER_ID':sel.get('USER_ID')
			}});
		}
	},
	onTabRender:function(tabPanel){
		this.getCityView().getSelectionModel().select(0);
		//初始化按钮
		var me = this,tools = tabPanel.down('toolbar');
		//tools.removeAll();
		setTimeout(function(){
			Ext.Ajax.request({
			    url: util.getPowerUrl(),
			    success: function(response, opts) {
			        var obj = Ext.decode(response.responseText),
			        	items = obj.children,
			        	cbtns = [];
			        for(var i=0;i<items.length;i++){
		        		delete items[i].checked;
		        		delete items[i].enableToggle;
			        	if(items[i].itemId.indexOf('Child')!=-1){
			        		if(items[i].itemId.indexOf('grant')!=-1){
			        			Ext.applyIf(items[i],{hidden:true});
			        		}
			        		cbtns.push(items[i]);
			        	}
			        }
		            cbtns.push({
		            	glyph:'xe60c@iconfont',
		            	text:'设置专线类型',
		            	itemId:'lineType',
		            	hidden:true,
		            	handler:function(btn){
			     			me.setLineType(btn);
			     		}
		            });
		            cbtns.push('->');
		            cbtns.push({
		    			emptyText:'公司名称/登录名',
		            	width:150,
		            	xtype:'searchfield',
		            	store:tabPanel.getActiveTab().getStore()
		    		});
		            tools.add(cbtns);
		            tools.remove(tools.down('button#initBtn'));
			    }
			});
		},500);
	},
	onTabChange:function(tabPanel, newCard, oldCard, eOpts){
		var cv = this.getCityView(),
			cvv = cv.getSelectedNodes(),
			cvv = Ext.get(cv.getSelectedNodes()[0]),
	    	cityId = cvv?cvv.getAttribute('cityId'):'',
	    	type = newCard.title=='旅行社'?2:1;
	    	
	    	
	    var tbar  = tabPanel.down('toolbar'),
	    	search = tbar.down('searchfield');
	    search.onClearClick();
	    tbar.remove(search);
	    var newSearch = Ext.create('Ext.ux.form.SearchField',{
	    	emptyText:'公司名称/登录名',
        	width:150,
        	xtype:'searchfield',
        	store:newCard.getStore()
	    });
	    tbar.add(newSearch);
	    
	    if(cityId==''){
	    	util.alert('请选择一个城市');
	    	return;
	    }
	    var grantBtn = tabPanel.down('toolbar button#grantChild');
	    var aloneBtn = tabPanel.down('toolbar button#aloneChild');
	    var hzshBtn = tabPanel.down('toolbar button#cooperationChild');
	    var lineBtn = tabPanel.down('toolbar button#lineType');
	    if(grantBtn){
		    if(type==2){
		    	grantBtn.hide();
		    	lineBtn.hide();
		    }else{
		    	grantBtn.show();
		    	lineBtn.show();
		    }
	    }
	    if(aloneBtn){
		    if(type==2){
		    	aloneBtn.show();
		    }else{
		    	aloneBtn.hide();
		    }
	    }
	    if(hzshBtn){
		    if(type==2){
		    	hzshBtn.show();
		    }else{
		    	hzshBtn.hide();
		    }
	    }
		newCard.getStore().getProxy().setExtraParams({'CITY_ID': cityId,TYPE:type});
		newCard.getStore().load();
	},
	onSelectCityChange:function(sm, selected, eOpt){
		this.getTabPanel().setActiveTab(0);
		this.getTabPanel().getActiveTab().getStore().getProxy().setExtraParams({'CITY_ID': selected[0].get('CITY_ID'),TYPE:2});
		this.getTabPanel().getActiveTab().getStore().load();
	},
	onAdd:function(btn){
		var tabPanel = this.getTabPanel(),
			ap = tabPanel.getActiveTab(),
			cv = this.getCityView(),
			cvv = cv.getSelectedNodes();
		if(cvv.length==1){
			this.managerWindow('添加'+ap.title,2,btn).show();
		}else{
			util.alert('请先选择一个城市');
		}
		
	},
	onEdit:function(btn){
		var tabPanel = this.getTabPanel(),
			rg = tabPanel.getActiveTab(),
			win,form,sel,selData,formData={};
		if(sel = util.getSingleModel(rg)){
			
			if(sel.get('PID')=='-1'){
				win = this.managerWindow('修改'+rg.title,2,btn);
				form = win.down('form'),
				fs = form.down('panel[itemId=account]'),
				cs = form.down('panel[itemId=companyfieldset]'),
				tfs = fs.query('textfield:not(combo)');
				win.show();
				formData = util.pmModel(sel);
				for(var i=0;i<tfs.length;i++){
					tfs[i].disable().hide();
				}
				form.getForm().setValues(formData);
				cs.down('filefield[name=LICENSE_ATTR]').setRawValue(formData['pm[LICENSE_ATTR]']);
				cs.down('filefield[name=LOGO_URL]').setRawValue(formData['pm[LOGO_URL]']);
				cs.down('filefield[name=BUSINESS_URL]').setRawValue(formData['pm[BUSINESS_URL]']);
				cs.down('filefield[name=COMPANY_SIGN]').setRawValue(formData['pm[COMPANY_SIGN]']);
				cs.down('filefield[name=WORD_TPL]').setRawValue(formData['pm[WORD_TPL]']);
				fs.down('combo[itemId=roleField]').getStore().load();
			}else{
				win = util.createEmptyWindow('修改'+rg.title,util.glyphToFont(btn.glyph),450,200,[{
					xtype:'hidden',
					name:'pm[COMPANY_ID]',
					value:sel.get('ID')
				},{
			    	name:'pm[SHORT_NAME]',
		    		xtype:'textfield',
		    		anchor:'100%',
		    		cls:'text-cls',
		    		fieldLabel:'公司简称',
			    	allowBlank: false
				},{
					xtype:'fieldcontainer',
					layout:'hbox',
					fieldLabel:'补单设置',
					items:[{
						width:170,
						fieldLabel:'延迟天数',
						maxValue:0,
						minValue:-99,
						labelWidth:55,
						xtype:'numberfield',
						name : 'pm[OLD_DELAY_DAY]',
						emptyText:'延迟天数'
					},{
						xtype:'splitter'
					},{
						fieldLabel:'审核天数',
						maxValue:99,
						minValue:0,
						labelWidth:55,
						width:170,
						name : 'pm[OLD_AUDIT_DAY]',
						xtype:'numberfield',
						emptyText:'审核天数'
					}]
				},{
					xtype:'combo',
					width:245,
   					itemId:'accountWay',
					name:'pm[ACCOUNT_WAY]',
					hiddenName:'pm[ACCOUNT_WAY]',
					fieldLabel:'对账方式',
					allowBlank: rg.title.indexOf('旅行社')==-1,
					forceSelection: true,
		            valueField: 'value',
		            displayField: 'text',
		            editable:false,
		            emptyText:'选择对账方式',
		            store:util.createComboStore([{
		            	value:'1',
		            	text:'门市/分公司独立对账'
		            },{
		            	value:'2',
		            	text:'总部对账'
		            },{
		            	value:'3',
		            	text:'平台对账'
		            }]),
		            queryMode: 'local',
		            typeAhead: true,
		            listeners:{
		            	change:function(combo, newValue, oldValue, eOpts){
		            		if(oldValue&&!combo.abc){
		            			if(oldValue!=newValue){
		            				var win = Ext.create('Ext.window.Window',{
		        			   			title: util.windowTitle('','信息提示',''),
		        			   			width:360,
		        			   			height:180,
		        			   			draggable:false,
		        						resizable:false,
		        						closable:false,
		        						modal:true,
		        			   			layout:'fit',
	        			   				items:[{
	        			   					bodyStyle:'background:#fff;padding:10px;',
	        				   				html:'<h3 class="f16 ht30 red-color">更换对账方式注意：</h3><div class="ht20 f14">请确认对账数据是否已对完，未对账完成的数据会丢失，是否更换？</div>'
	        				   			}],
	        				   			buttons:[{
	        				   				text:'我要更换',
	        				   				handler:function(){
	        				   					win.close();
	        				   					combo.abc=false;
	        				   				}
	        				   			},{
	        				   				text:'不更换',
	        				   				cls:'disable',
	        				   				handler:function(){
	        				   					win.close();
	        				   					combo.abc=true;
	        				   					combo.setValue(oldValue);
	        				   					combo.abc=false;
	        				   				}
	        				   			}]
		        			   		}).show();
		            			}
		            		}
		            	}
		            }
				}],[{
			        text: '确定',
			        disabled: true,
			        formBind: true,
			        handler:function(btn){
			        	var f = btn.up('form'),
			        		form = f.getForm(),
			        		win = f.up('window');
			        	if (form.isValid()) {
			        		btn.disable();
				            form.submit({
				            	submitEmptyText:false ,
				            	url:cfg.getCtx()+'/site/company/update/Filiale',
				                success: function(form, action) {
				                   util.success('保存数据成功');
				                   rg.getStore().load();
				                   win.close();
				                },
				                failure: function(form, action) {
				                	var state = action.result?action.result.statusCode:0,
				                		errors = ['保存数据异常'];
				                    util.error(errors[0-parseInt(state)]);
				                    win.close();
				                }
				            });
				        }
			        }
			    }]).show();
				form = win.down('form');
				formData = util.pmModel(sel);
				form.getForm().setValues(formData);
			}
			
			
		}
	},
	onAudit:function(){
		var tabPanel = this.getTabPanel(),
			rg = tabPanel.getActiveTab(),sels,isAudit=true;
		if(sels = util.getMultiModel(rg)){
	    	for (var i = 0; i < sels.length; i++) { 
				var role = sels[i].data.ROLE_ID || '',
					type = sels[i].data.TYPE || '';
				if(role==''||type==''){
					isAudit = false;
				}
			}
		}
		if(isAudit){
			util.auditGridModel(rg,'/site/company/audiCompany');
		}else{
			util.alert('没有选择角色或公司类型');
		}
	},
	onChildEnable:function(){
		var tabPanel = this.getTabPanel(),
			rg = tabPanel.getActiveTab();
		util.switchGridModel(rg,'/site/company/switch');
	},
	recommendChild:function(){
		var tabPanel = this.getTabPanel(),
			rg = tabPanel.getActiveTab();
		util.switchGridModel(rg,'/site/company/recommend');
	},grantChild:function(type){
		var tabPanel = this.getTabPanel(),
			rg = tabPanel.getActiveTab(),
			win,sel;
		if(sel = util.getSingleModel(rg)){
			var title = type=='traffic'?'单卖交通':'单卖地接',
				stype = type=='traffic'?'1':'2',
				glyph = type=='traffic'?'&#xe632;':'&#xe682;';
			win = Ext.create('Ext.window.Window',{
				title:util.windowTitle(glyph,''+title,''),
				width:300,
				height:220,
				modal:true,
				draggable:false,
				resizable:false,
	   			layout:'fit',
				items:[{
					xtype:'form',
					fieldDefaults: {
				        labelAlign: 'right',
				        labelWidth: 80,
				        msgTarget: 'side'
				    },
					bodyPadding: 5,
					bodyStyle:'background:#fff;',
					items:[{
						xtype:'hidden',
						name:'pm[ID]',
						value:sel.get('ID')
					},{
						xtype:'hidden',
						name:'pm[TYPE]',
						value:stype
					},{
						xtype: 'radiogroup',
				        fieldLabel:'授权',
				        margin:'10 0 10 0',
				        width:200,
	                    items: [
				            { boxLabel: '是', name: 'pm[SALE]', inputValue: '1', checked: true},
				            { boxLabel: '否', name: 'pm[SALE]', inputValue: '2'}
				        ]
					},{
						xtype:'numberfield',
						fieldLabel:'营销费(%)',
						minValue:0,
						hideTrigger:true,
						maxValue:100,
						name:'pm[EXPENSE]',
						margin:'0 0 0 3',
						width:180,
						value:(sel.get(type.toUpperCase()+'_EXPENSE')||0),
						allowBlank:false
					}]
				}],
				buttons:[{
					text:'确定',
					handler:function(btn){
						var f = win.down('form'),
			        		form = f.getForm();
			        	if (form.isValid()) {
			        		btn.disable();
				            form.submit({
				            	submitEmptyText:false ,
				            	url:cfg.getCtx()+'/site/company/saleTraffic',
				                success: function(form, action) {
				                   util.success('授权成功');
				                   rg.getStore().load();
				                   win.close();
				                },
				                failure: function(form, action) {
				                	var state = action.result?action.result.statusCode:0,
				                		errors = ['授权异常'];
				                    util.error(errors[0-parseInt(state)]);
				                    win.close();
				                }
				            });
				        }
					}
				}]
			}).show();
		}
	},
	aloneChild :function(){
		var tabPanel = this.getTabPanel(),
			rg = tabPanel.getActiveTab();
		if(rg.title=='旅行社')
		util.switchGridModel(rg,'/site/company/setAlone');
	},
	bankManager :function(btn){
		var grid = this.getTabPanel().getActiveTab(),
			win,sel;
		if(btn.up('#firsttool')){
			grid = this.getSiteGrid();
		}
		if(sel = util.getSingleModel(grid)){
			win = util.createEmptyWindow('设置银行卡号',util.glyphToFont(btn.glyph),500,320,[{
				xtype:'hidden',
				name:'pm[ID]',
				value:sel.get('BANK_ID')
			},{
				xtype:'hidden',
				name:'pm[COMPANY_ID]',
				value:sel.get('ID')
			},{
				xtype:'cardno',
				margin:'5 10 0 0',
				fieldLabel:'银行卡号',
				noType:'bank',
				allowBlank:false,
				vtype:'bank',
				anchor:'100%',
				name:'pm[BANK_NO]',
				value:sel.get('BANK_NO')
			},{
				xtype:'textfield',
				margin:'10 10 0 0',
				fieldLabel:'银行名称',
				allowBlank:false,
				anchor:'100%',
				name:'pm[BANK_NAME]',
				value:sel.get('BANK_NAME')
			},{
				xtype:'textfield',
				margin:'10 10 0 0',
				allowBlank:false,
				fieldLabel:'开户行',
				anchor:'100%',
				name:'pm[OPEN_ADDRESS]',
				value:sel.get('OPEN_ADDRESS')
			},{
				xtype:'textfield',
				margin:'10 10 0 0',
				fieldLabel:'开户姓名',
				allowBlank:false,
				width:200,
				name:'pm[OPER_NAME]',
				value:sel.get('OPER_NAME')
			}],[{
				text:'确定',
				formBind:true,
				disabled:true,
				handler:function(){
					var form = win.down('form'),
						f = form.getForm();
					if(f.isValid()){
						f.submit({
							submitEmptyText:false ,
							url:cfg.getCtx()+'/site/company/bank/save',
			                success: function(form, action) {
			                	grid.getStore().load();
			                	win.close();
			                	util.success('银行信息保存成功');
			                },
			                failure: function(form, action) {
			                	var state = action.result?action.result.statusCode:0,
			                		errors = ['保存结果异常','公司穿越了，没有站关系'];
			                    util.error(errors[0-parseInt(state)]);
			                }
						});
					}
				}
			}]).show();
		}
	},
	cooperationChild:function(btn){
		var grid = this.getTabPanel().getActiveTab(),
		win,sel;
		var cv = this.getCityView(),
			cvv = Ext.get(cv.getSelectedNodes()[0]),
			cityId=cvv.getAttribute('cityId');
		if(sel = util.getSingleModel(grid)){
			
			if(grid.title=='旅行社'&&sel.get('PID')!='-1'){
				util.alert('非法操作');
				return;
			}
			
			var selectorStore = Ext.create('Ext.data.Store',{
	        	model:Ext.create('Ext.data.Model',{
	        		fields: ['COMPANY_ID','COMPANY_NAME','BRAND_NAME']
	        	}),
	        	autoLoad: true, 
	            proxy: {
	                type: 'ajax',
	                noCache:true,
	                url: cfg.getCtx()+'/site/company/cooperation/supply?companyId='+sel.get('ID'),
	                reader: {
	                    type: 'json',
	                    rootProperty: 'data',
	                    totalProperty:'totalSize'
	                }
	           	}
	        });
			win = util.createEmptyWindow(btn.getText(),util.glyphToFont(btn.glyph),800,450,[{
				xtype:'hiddenfield',
				name:'pm[COMPANY_ID]',
				value:sel.get('ID')
			},{
				xtype:'hiddenfield',
				name:'pm[COMPANY_TYPE]',
				value:sel.get('TYPE')
			},{
				name:'pm[SUPPLYS]',
				xtype: 'itemselector',
				anchor:'100% 100%',
	            store: selectorStore,
	            displayField: 'COMPANY_NAME',
	            valueField: 'COMPANY_ID',
	            //value: ['3', '4', '6'],
	            //allowBlank: false,
	            msgTarget: 'side',
	            fromTitle: '商户列表',
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
				handler:function(){
					var form = win.down('form'),
						f = form.getForm();
					if(f.isValid()){
						f.submit({
							submitEmptyText:false ,
							url:cfg.getCtx()+'/site/company/save/business/seller',
			                success: function(form, action) {
			                	win.close();
			                	util.success('合作商户设置成功');
			                },
			                failure: function(form, action) {
			                	var state = action.result?action.result.statusCode:0,
			                		errors = ['合作商户设置异常'];
			                    util.error(errors[0-parseInt(state)]);
			                }
						});
					}
				}
			}]).show();
			/*win.addDocked({
   				xtype:'toolbar',
   				docked:'top',
   				items:[{
   					width:200,
					emptyText:'公司/品牌/城市名称',
		        	xtype:'searchfield',
		        	store:selectorStore
				}]
   			});*/
			win.mask('加载中...');
			Ext.Ajax.request({
				url:cfg.getCtx()+'/site/company/list/business/seller',
				params:{companyId:sel.get('ID')},
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
	},
	shareChild:function(){
		var tabPanel = this.getTabPanel(),
		rg = tabPanel.getActiveTab(),
		win,sel;
		if(sel = util.getSingleModel(rg)){
				win = Ext.create('Ext.window.Window',{
					title:util.windowTitle('&#xe665;','线路共享'),
					width:300,
					height:180,
					modal:true,
					draggable:false,
					resizable:false,
		   			layout:'fit',
					items:[{
						xtype:'form',
						fieldDefaults: {
					        labelAlign: 'right',
					        labelWidth: 80,
					        msgTarget: 'side'
					    },
						bodyPadding: 5,
						bodyStyle:'background:#fff;',
						items:[{
							xtype:'hidden',
							name:'pm[ID]',
							value:sel.get('ID')
						},{
							xtype:'hidden',
							name:'pm[TYPE]',
							value:'3'//线路共享
						},{
							xtype: 'radiogroup',
					        fieldLabel:'授权',
					        margin:'10 0 10 0',
					        width:200,
		                    items: [
					            { boxLabel: '是', name: 'pm[SALE]', inputValue: '1', checked: true},
					            { boxLabel: '否', name: 'pm[SALE]', inputValue: '2'}
					        ]
						}]
					}],
					buttons:[{
						text:'确定',
						handler:function(btn){
							var f = win.down('form'),
				        		form = f.getForm();
				        	if (form.isValid()) {
				        		btn.disable();
					            form.submit({
					            	submitEmptyText:false ,
					            	url:cfg.getCtx()+'/site/company/saleTraffic',
					                success: function(form, action) {
					                   util.success('授权成功');
					                   rg.getStore().load();
					                   win.close();
					                },
					                failure: function(form, action) {
					                	var state = action.result?action.result.statusCode:0,
					                		errors = ['授权异常'];
					                    util.error(errors[0-parseInt(state)]);
					                    win.close();
					                }
					            });
					        }
						}
					}]
				}).show();
		}
		
	},
	setLineType:function(btn){
		var tabPanel = this.getTabPanel(),
		rg = tabPanel.getActiveTab(),
		win,sel;
		
		if(sel = util.getSingleModel(rg)){
				win = Ext.create('Ext.window.Window',{
					title:util.windowTitle(util.glyphToFont(btn.glyph),'设置专线类型'),
					width:300,
					height:190,
					modal:true,
					draggable:false,
					resizable:false,
		   			layout:'fit',
					items:[{
						xtype:'form',
						fieldDefaults: {
					        labelAlign: 'right',
					        labelWidth: 80,
					        msgTarget: 'side'
					    },
					    defaults: {
			                anchor: '100%',
			                hideEmptyLabel: false
			            },
						bodyPadding: 5,
						bodyStyle:'background:#fff;',
						items:[{
							xtype:'hidden',
							name:'pm[ID]',
							value:sel.get('ID')
						},{
							xtype:'checkbox',
			                fieldLabel: '专线类型',
			                boxLabel: '国内',
			                checked:sel.get('IS_COUNTRY')==1,
			                name: 'pm[IS_COUNTRY]',
			                inputValue: '1'
			            }, {
			            	xtype:'checkbox',
			                boxLabel: '出境',
			                checked:sel.get('IS_WORLD')==1,
			                name: 'pm[IS_WORLD]',
			                inputValue: '1'
			            }]
					}],
					buttons:[{
						text:'确定',
						handler:function(btn){
							var f = win.down('form'),
				        		form = f.getForm();
				        	if (form.isValid()) {
				        		btn.disable();
					            form.submit({
					            	submitEmptyText:false ,
					            	url:cfg.getCtx()+'/site/company/edit/route/type',
					                success: function(form, action) {
					                   util.success('设置专线成功');
					                   rg.getStore().load();
					                   win.close();
					                },
					                failure: function(form, action) {
					                	var state = action.result?action.result.statusCode:0,
					                		errors = ['设置专线异常'];
					                    util.error(errors[0-parseInt(state)]);
					                    win.close();
					                }
					            });
					        }
						}
					}]
				}).show();
		}
	},
	contractChild:function(btn){
		var tabPanel = this.getTabPanel(),
			rg = tabPanel.getActiveTab();
		if(rg.title=='旅行社')
		util.switchGridModel(rg,'/site/company/contract/must');
	},
	setCityChild:function(btn){
		var tabPanel = this.getTabPanel(),
			me = this,
			rg = tabPanel.getActiveTab(),win,sel;
		if(sel = util.getSingleModel(rg)){
			if(rg.title=='旅行社'&&sel.get('PID')!='-1'){
				util.alert('非法操作');
				return;
			}
			
			win = util.createEmptyWindow(btn.getText(),util.glyphToFont(btn.glyph),360,300,[{
				padding:10,
				layout:'anchor',
				items:[{
					name:'pm[COMPANY_ID]',
					xtype:'hidden',
					value:sel.get('ID')
				},{
					name:'pm[COMPANY_TYPE]',
					xtype:'hidden',
					value:sel.get('TYPE')
				},{
					fieldLabel:'城市',
					xtype:'citycombo',
					listeners:{
						beforequery :function(queryPlan, eOpts){
							var combo = queryPlan.combo,
								store = combo.getStore();
							/**
							 * store.getProxy().setExtraParams({cityId:cfg.getUser().cityId}); 
							 */
						},
						select :function(c, r, e){
							c.reset();
							var cg = c.nextSibling();
							if(cg.down('container#'+r.get('ID'))){
								util.alert('出发城市重复');
								return;
							}
							cg.add({
								xtype:'container',
								margin:'0 2 2 0',
								itemId:r.get('ID'),
								items:[{
									xtype:'hidden',
									name:'BEGIN_CITY',
									value: r.get('ID')+','+r.get('TEXT')
								},{
									tooltip:r.get('TEXT'),
									cls:'tag-split-btn remove',
									xtype:'splitbutton',
									text:r.get('TEXT'),
									listeners:{
										arrowclick :function(sp){
											var zj = sp.up();
											zj.up('container[itemId=beginCityItems]').remove(zj,true);
										}
									}
								}]
							});
						}
					}
				},{
					fieldLabel:'已选城市',
					itemId:'beginCityItems',
					layout:'column',
					autoScroll:true,
					overflowY:'auto',
					column:2,
					xtype:'fieldcontainer'
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
							url:cfg.getCtx()+'/site/company/save/start/city',
			                success: function(form, action) {
			        			util.success('保存出发城市成功！');
								win.close();
								grid.getStore().load();
			                },
			                failure: function(form, action) {
			                	var state = action.result?action.result.statusCode:0,
			                		errors = ['保存出发城市异常'];
			                    util.error(errors[0-parseInt(state)]);
			                }
						});
					}
				}
			}]).show();
			
			//获取已选
			win.mask();
			Ext.Ajax.request({
				url:cfg.getCtx()+'/site/company/list/start/city?companyId='+sel.get('ID')+'&companyType='+sel.get('TYPE'),
				success:function(response, opts){
					win.unmask();
					var action = Ext.decode(response.responseText);
					if(action&&action.data){
					me.addCitys(win,action.data);
					}
				}
			});
		}
	},
    addCitys : function(win,datas){
    	var ct=win.down('container[itemId=beginCityItems]');
    	ct.removeAll(true);
    	for(var i=0;i<datas.length;i++){
    		ct.add(this.cityTpl(datas[i]));
    	}
    },
    cityTpl :function(r){
    	return {
    		itemId:r.CITY_ID,
			margin:'5 5 0 0',
			items:[{
				xtype:'hidden',
				name:'BEGIN_CITY',
				value: r.CITY_ID+','+r.CITY_NAME
			},{
				tooltip:r.CITY_NAME,
				cls:'tag-split-btn remove',
				xtype:'splitbutton',
				text:r.CITY_NAME,
				listeners:{
					arrowclick :function(sp){
						var zj = sp.up('panel');
						zj.up('container[itemId=beginCityItems]').remove(zj,true);
					}
				}
			}]
		};
    }
});