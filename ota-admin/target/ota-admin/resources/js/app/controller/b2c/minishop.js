Ext.define('app.controller.b2c.minishop', {
	extend: 'app.controller.common.BaseController',
	views:['app.view.resource.route.pub.detail','app.view.b2c.selectRoutes'],
	config: {
		control: {
			'toolbar button#recommend':{
				click:'recommend'
			},
			'toolbar button#add':{
				click:'add'
			},
			'toolbar button#isuse':{
				click:'isuse'
			},
			'toolbar button#info':{
				click:'info'
			},
			'toolbar button#syn':{
				click:'sync'
			},
			'toolbar button#setAccountUser':{
				click:'setAccountUser'
			}
		},
		refs: {
			minishopGrid: 'gridpanel[itemId=basegrid]'
        }
	},
	onBaseGridRender:function(g){
		var me = this;
		setTimeout(function(){
		Ext.Ajax.request({
		    url: util.getPowerUrl(),
		    success: function(response, opts) {
		        var obj = Ext.decode(response.responseText),
		        	items = obj.children;
		        items.push('->');
				items.push({
					emptyText:'搜索关键字',
		        	xtype:'searchfield',
		        	store:g.getStore()
				});
		        util.createGridTbar(g,items);
		    }
		});
		},500);
	},
	recommend:function(btn){
		var me = this,rg = this.getMinishopGrid(),
			window,form,sel;
		if(sel = util.getSingleModel(rg)){
			var lxGrid = Ext.create('Ext.grid.Panel',{
		    	region:'west',
			    selType:'checkboxmodel',
			    selModel:{
			    	mode:'MULTI'
			    },
			    itemId:'categoryGrid',
		    	store:util.createGridStore(cfg.getCtx()+'/b2c/category/list',Ext.create('app.model.b2c.category.model')),
		    	columns:[{
			        text: '排序',
			        width:60,
			        dataIndex: 'ORDER_BY',
			        
			    },{
			        text: '分类名称',
			        flex:1,
			        dataIndex: 'CATEGORY',
			        renderer: function(v,metaData,r,colIndex,store,view){
			        	return '<div class="">'+v+'</div>';
			        }
			    }],
			    dockedItems:[{
			    	itemId:'categoryTool',
		    		xtype:'toolbar',
		    		height:36
		    	}],
		    	width:240,
		    	listeners:{
		    		selectionchange:function(s, selected){
		    			var csel = selected[0];
		    			tjStore.getProxy().setExtraParams({parentId:sel.get('ID'),categoryId:csel.get('ID')});
		    			tjStore.load();
		    		}
		    	}
		    });
			var tjStore = util.createGridStore(cfg.getCtx()+'/site/company/list/web/recommend?isRecommend=1',Ext.create('Ext.data.Model',{
				fields: [
				         'ID','RECOMMEND_ID','NO','THEMES','ROUTE_WAP_PRICE', 'TITLE','ROUTE_ID',
				         'RECOMMEND_TYPE','ORDER_BY','COMPANY_ID','USER_ID','CREATE_TIME',
				         'DAY_COUNT','ATTR','FACE'
				]
			}),'0');
			var tjGrid = Ext.create('Ext.grid.Panel',{
				region:'center',
		    	border:false,
		    	style:'border-left:1px solid #c1c1c1!important;',
		    	dockedItems:[{
		    		xtype:'toolbar',
		    		items:[{
		    			text:'添加',
		    			glyph:'xe631@iconfont',
		    			handler:function(_btn){
		    				var _sel;
		    				if(_sel = util.getSingleModel(lxGrid)){
		    					var supplyStore = util.createGridStore(cfg.getCtx()+'/site/company/list/web/recommend/supply',Ext.create('Ext.data.Model',{
		    						fields: [
		    						         'SALE_COMPANY','SALE_COMPANY_ID','SALE_BRAND_NAME'
		    						]
		    					}));
		    					var _win = Ext.create('Ext.window.Window',{
		    			   			title: util.windowTitle(util.glyphToFont(_btn.glyph),_sel.get('CATEGORY'),''),
		    			   			width:'85%',
		    			   			height:'85%',
		    			   			draggable:false,
		    						resizable:false,
		    						modal:true,
		    			   			bodyStyle:'background:#fff;padding:1px;font-size:16px;color:#bbb;',
		    			   			layout:'fit',
		    			   			items:[{
		    			   				storeUrl:cfg.getCtx()+'/site/company/list/web/recommend?categoryId='+_sel.get('ID')+'&parentId='+sel.get('ID')+'&filter=1',
		    			   				saveUrl:cfg.getCtx()+'/site/company/save/web/recommend?categoryId='+_sel.get('ID')+'&parentId='+sel.get('ID'),
		    			   				xtype:'selectRoutes'
		    			   			}],
		    			   			listeners:{
		    			   				close:function(){
		    			   					tjGrid.getStore().load();
		    			   				}
		    			   			}
		    					});
		    					
		    					_win.show();
		    				}
		    			}
		    		},{
		    			text:'删除',
		    			glyph:'xe62c@iconfont',
		    			handler:function(_btn){
		    				var _sel = util.getSingleModel(lxGrid);
		    				util.delGridModel(tjGrid,'/site/company/del/web/recommend?categoryId='+_sel.get('ID')+'&parentId='+sel.get('ID'));
		    			}
		    		}]
		    	}],
		    	store:tjStore,
		    	plugins:[new Ext.grid.plugin.CellEditing({
	                clicksToEdit: 1,
	                listeners:{
	                	edit:function(a,b){
	                		b.record.commit();
	                		Ext.Ajax.request({
								url:cfg.getCtx()+'/site/company/upadte/web/recommend/order?field='+b.field+'&id='+b.record.get('RECOMMEND_ID')+'&orderBy='+b.value,
								success:function(response, opts){
									tjStore.load();
								}
							});
	                	}
	                }
	            })],
	            columnLines: true,
		    	columns: [{
		    		padding:'1 0 1 0',
					text: '线路图片',
		        	dataIndex:'FACE',
		        	width:145,
			        renderer: function(v,metaData,r,colIndex,store,view){
			        	v = cfg.getPicCtx()+'/'+v;
			        	var 
			        	 	h = [
				        		'<img src="'+v+'" width="125px" height="70px"/>'
				        	],dc = r.get('DAY_COUNT') || '',attr = r.get('ATTR') || '';
				        dc = dc==''?'':dc+'日';
				        if(attr!=''){
				        	h.push('<span class="flash" style="position:absolute;top:5px;left:10px;border-radius:0px;background:'+cfg.getRouteAttrTagColor()[attr]+';color:#fff;padding:2px 5px;">'+attr+dc+'</span>');
				        }
				        h.push('<div class="ht16" style="color:#666;padding-top:5px;">编号: <a href="javascript:;">'+r.get('NO')+'</a></div>');
			        	return h.join('');
			        }
				},{
		        	text: '线路名称',
		        	dataIndex:'TITLE',
		        	flex:1,
		        	renderer: function(v,metaData,r,ri,c,store,view){
		        		setTimeout(function() {
			                var row = view.getRow(r);
			                if(row){
			                //Capturing the el (I need the div to do the trick)
			                var el = Ext.fly(Ext.fly(row).query('.x-grid-cell')[c]).down('div');
			                var cellWidth = el.getWidth();
			                var wraps = el.query('.d-wrap',false);
			                Ext.each(wraps,function(w,i){
			                	w.setWidth(cellWidth-50);
			                });
			                }
			                //All other codes to build the progress bar
			            }, 50);
		        		var h = [];
			        	h.push('<a class="f16 d-wrap title" href="javascript:;" style="display:inline-block;line-height:24px;padding:5px 0 10px 0;">'+v+'</a>');
		        		
			        	//标签
			        	h.push('<div class="product-tag">');
		        		if(r.get('BRAND_NAME')){
		        			h.push('<span class="flash flash-success"><i  class="iconfont f16" style="top:0px;">&#xe6b2;</i> '+r.get('BRAND_NAME')+'</span>');
		        		}
		        		if(r.get('TYPE')){
		        			var routArrs = ['','周边','国内','出境'];
		        			h.push('<span class="flash">'+routArrs[r.get('TYPE')]+'游</span>');
		        		}
		        		if(r.get('IS_TOP')=='1'){
		        			h.push('<span class="flash flash-error"><i  class="iconfont f14" style="top:0px;">&#xe605;</i> 精彩推荐</span>');
		        		}
		        		
		        		
		        		h.push('</div>');
		        		
		        		
			        	h.push('<div class="ht20"><span>网站价：</span><span class="money-color f16"><dfn>￥</dfn>'+util.format.number(r.get('ROUTE_WAP_PRICE')||0,'0.00')+'</span></div>');
		        		return h.join('');
		        	}
		        },{
		        	width:150,
		        	text:'主题属性',
		        	dataIndex:'THEMES',
		        	renderer:function(v,c,r){
		        		v = v || '';
		        		var vstr='';
		        		if(v!=''){
			        		var vs = v.split(',');
			        		Ext.each(vs,function(s,i){
			        			if(i%2==0){
			        				vstr+='<p class="ht22">';
			        			}
			        			vstr+='<span class="d-tag-radius">'+s+'</span>';
			        			if(i%2!=0){
			        				vstr+='</p>';
			        			}
			        		});
		        		}
		 	        	return '<div style="margin-top:5px;">'+vstr+'</div>';
			        }
		        },{
		        	text:'最近日期/价格',
		        	width:120,
			        dataIndex: 'SUM_PRICE',
			        renderer:function(v,c,r){
			        	var re = [],isFullPrice = r.get('IS_FULL_PRICE')||'0';
			        	if(r.get('RQ')){
			        		re.push('<div class="ht20 f14" style="margin-bottom:5px;">'+(r.get('RQ')||'')+'</div>');
			        		re.push('<div class="ht20">'+util.moneyFormat(v)+'</div>');
			        	}else{
			        		re.push('<div class="ht20">'+util.moneyFormat(r.get('ROUTE_PRICE'))+'</div>');
			        		
			        	}
			        	return re.join('');
			        }
		        },{
		        	text:'手动排序',
		        	width:80,
		        	dataIndex:'ORDER_BY',
		        	editor :{
		        		value:0,
		        		minValue:0,
		        		maxValue:999,
		        		xtype:'numberfield'
		        	}
		        }]
			});
			tjGrid.on('cellclick',this.detailWindow,this);
			window = Ext.create('Ext.window.Window',{
				title:util.windowTitle(util.glyphToFont(btn.glyph),btn.getText(),''),
				width:1000,
				height:400,
				maximized:true,
				modal:true,
				draggable:false,
				resizable:false,
				layout: 'border',
				items:[lxGrid,tjGrid]
			});
			window.show();
		}
	},
	detailWindow :function(view, td, cellIndex, record, tr, rowIndex, e, eOpts){
		if(e.target.tagName=='A'&&e.target.className.indexOf('title')!=-1){
			var win = Ext.create('Ext.window.Window',{
				title:util.windowTitle('&#xe635;','线路详情',''),
				bodyPadding:5,
				modal:true,
				draggable:false,
				resizable:false,
				maximizable:false,
	   			layout:'fit',
	   			items:[{
	   				xtype:'routedetail'
	   			}]
			});
			win.show().maximize();
			var rd = win.down('routedetail');
			rd.setRouteId(record.get('ID'));
		}
	},
	isuse:function(btn){
		util.switchGridModel(this.getMinishopGrid(),'/b2c/minishop/isuse');
	},
	add:function(btn){
		var me = this,
			companyStore = util.createGridStore(cfg.getCtx()+'/site/company/listCompanyChild',Ext.create('app.model.site.company.model'),'1',20),
			companyGrid = Ext.create('Ext.grid.Panel',{
			region:'center',
			width:255,
	    	itemId:'companytree',
		    dockedItems:[{
		    	itemId:'powertool',
	    		xtype:'toolbar',
	    		height:36,
	        	items:[{
	        		xtype:'label',
	        		html:'<span class="yellow-color"><i class="iconfont f20">&#xe6ae;</i> 开通公司微店请选择公司管理员</span>'
	        	},'->',{
	        		emptyText:'搜索公司名称',
		        	xtype:'searchfield',
		        	store:companyStore
	        	}]
	    	}],
	    	style:'border-right:1px solid #d1d1d1;',
	    	bbar:util.createGridBbar(companyStore),
	    	store:companyStore,
	        columns:[{
		        text: '公司名称',
		        flex:1,
		        dataIndex: 'COMPANY',
		        renderer: function(value,c,r){
		        	return '<div class="ht20">'+value+'</div>';
		        }
		    }]
		}),
		employeeStore = util.createGridStore(cfg.getCtx()+'/b2b/user/list',Ext.create('app.model.b2b.user.model'),'0',20),
		employeeGrid = Ext.create('Ext.grid.Panel',{
				region:'east',
				dockedItems:[{
			    	itemId:'powertool',
		    		xtype:'toolbar',
		    		height:36,
		        	items:['->',{
		        		emptyText:'搜索员工',
			        	xtype:'searchfield',
			        	store:employeeStore
		        	}]
		    	}],
		    	store:employeeStore,
		    	//bbar:util.createGridBbar(employeeStore),
		        columns:[{
			        text: '员工名称',
			        flex:1,
			        dataIndex: 'USER_NAME',
			        renderer: function(value,c,r){
			        	var _sel = util.getSingleModel(companyGrid);
			        	var s='';
			        	if(value == _sel.get('USER_NAME')){
			        		s = '<i class="iconfont blue-color f18" data-qtip="管理员(公司微店请绑定该账号)">&#xe69c;</i> ';
			        	}
			        	return '<div class="ht20">'+s+value+'</div>';
			        }
			    },{
			    	text: '',
		            width:90,
		            xtype: 'widgetcolumn',
		            //dataIndex: 'WEIDIAN_ID',
		            widget: {
		            	margin:'2 0 0 0',
		                ui:'default-toolbar',
		                xtype: 'button',
		                listeners:{
		                	afterrender:function(btn){
		                		var rec = btn.getWidgetRecord();
		                		if(rec.get('MINISHOP_ID')){
		                			btn.setText('已开通');
		                			btn.disable();
		                		}else{
		                			btn.setText('开通');
		                		}
		                	}
		                },
		                handler: function(btn) {
		                	var _sel = util.getSingleModel(companyGrid);
		                	var rec = btn.getWidgetRecord();
		                	if(rec.get('USER_NAME') == _sel.get('USER_NAME')){
		                		me.openAccountUser(null, {COMPANY_ID:rec.get('COMPANY_ID')},{COMPANY_ID:rec.get('COMPANY_ID'),ID:rec.get('ID')},window);
		                	}else{
			                    Ext.Ajax.request({
									url:cfg.getCtx()+'/b2c/minishop/open',
									params:{companyId:rec.get('COMPANY_ID'),userId:rec.get('ID')},
									success:function(){
										util.success("微店开通成功!");
										window.close();
										me.getMinishopGrid().getStore().load();
									},
									failure:function(){
										util.error("微店开通失败！");
									}
								});
		                	}
		                	
		                	
		                    
		                }
		            }
			    }],
		    	width:300,
		    	minWidth:300
		}),window = Ext.create('Ext.window.Window',{
			title:util.windowTitle(util.glyphToFont(btn.glyph),btn.getText(),''),
			width:'85%',
			height:'85%',
			modal:true,
			draggable:false,
			resizable:false,
			layout: 'border',
			items:[companyGrid,employeeGrid]
		});
		window.show();
		companyGrid.on('itemclick',function(grid, record, item, index, e, eOpts){
			employeeGrid.getStore().getProxy().setExtraParams({COMPANY_ID:record.get('ID'),limit:10000});
			employeeGrid.getStore().load();
		});
	},
	setSelections:function(){
		
	},
	sync:function(btn){
		Ext.MessageBox.confirm(util.windowTitle(util.glyphToFont(btn.glyph),'提示',''), '<div style="font-size:16px;height:40px;line-height:30px;">同步会清空以前数据,确定同步网站所有推荐产品？</div>', function(btn, text){
			if(btn=='yes'){
				Ext.Ajax.request({
					url:cfg.getCtx()+'/b2c/minishop/syn/web/data',
					success:function(response, opts){
						var obj = Ext.decode(response.responseText);
						if(obj.success){
							util.success('同步成功');
							grid.getStore().load();
						}else{
							util.error("同步失败！");
						}
					},
					failure:function(){
						util.error("同步失败！");
					}
				});
			}
		}, this);
	},
	info:function(btn){
		var rg = this.getMinishopGrid(),
			window,form,sel,selData,formData={};
		if(sel = util.getSingleModel(rg)){
			var window = util.createEmptyWindow(btn.getText(),util.glyphToFont(btn.glyph),600,'85%',[{
				xtype:'hiddenfield',
				value:sel.get('ID'),
				name:'pm[ID]'
			},{
				xtype: 'fieldcontainer',
				fieldLabel:'域名',
		        layout: 'hbox',
		        items: [{
		        	readOnly:cfg.getUser().userType!='01',
		        	name:'pm[DOMAIN]',
		            xtype: 'textfield',emptyText:'域名',itemId:'domain',
		            flex: 1,
					allowBlank:false
		        },{
		            xtype: 'splitter'
		        },{
		            xtype: 'textfield',
		            width:150,
		            readOnly:cfg.getUser().userType!='01',
		            itemId:'beianhao',
		            emptyText:'备案号',
					name:'pm[RECORD_NO]',
					allowBlank:false
		        }]
			},{
				xtype:'textfield',
				anchor:'100%',
				fieldLabel:'微店名称',
				name:'pm[TITLE]',
				value:sel.get('TITLE'),
				allowBlank:cfg.getUser().userType=='01'
			},{
				xtype:'textareafield',
				anchor:'100%',
				fieldLabel:'微店描述',
				allowBlank:cfg.getUser().userType=='01', 
				name:'pm[ABOUT]',
				value:sel.get('ABOUT')
			},{
				xtype:'filefield',
				anchor:'100%',
				value:sel.get('LOGO'),
				fieldLabel:'微店头像',
				name:'LOGO',
				allowBlank:cfg.getUser().userType=='01',
				buttonConfig: {
	                text:'',
					ui:'default-toolbar',
	                glyph: 'xe648@iconfont'
	            }
			},{
				xtype:'filefield',
				anchor:'100%',
				value:sel.get('CODE'),
				fieldLabel:'微店二维码',
				allowBlank:cfg.getUser().userType=='01',
				name:'CODE',
				buttonConfig: {
	                text:'',
					ui:'default-toolbar',
	                glyph: 'xe648@iconfont'
	            }
			},{
				xtype:'filefield',
				anchor:'100%',
				value:sel.get('WX_IMG'),
				allowBlank:cfg.getUser().userType=='01',
				fieldLabel:'微信二维码',
				name:'WX_IMG',
				buttonConfig: {
	                text:'',
					ui:'default-toolbar',
	                glyph: 'xe648@iconfont'
	            }
			},{
				xtype:'textfield',
				anchor:'100%',
				fieldLabel:'appId',
				allowBlank:cfg.getUser().userType=='01',
				name:'pm[WX_APP_ID]',
				value:sel.get('WX_APP_ID')
			},{
				xtype:'textfield',
				anchor:'100%',
				fieldLabel:'appSecret',
				allowBlank:cfg.getUser().userType=='01',
				name:'pm[WX_SECRET]',
				value:sel.get('WX_SECRET')
			},{
				xtype:'textfield',
				anchor:'100%',
				fieldLabel:'店主名称',
				allowBlank:cfg.getUser().userType=='01',
				name:'pm[MANAGER]',
				value:sel.get('MANAGER')
			},{
				fieldLabel:'客服QQ',
				anchor:'80%',
				value:sel.get('QQ1'),
				name:'pm[QQ1]',
	            xtype: 'textfield',emptyText:'客服QQ',
	            allowBlank:cfg.getUser().userType=='01',
	            flex: 1
			},{
				fieldLabel:'客服电话',
				anchor:'80%',
				value:sel.get('PHONE1'),
				name:'pm[PHONE1]',
				allowBlank:cfg.getUser().userType=='01',
	            xtype: 'textfield',emptyText:'客服电话',
	            flex: 1
			}],[{
				cls:'orange',
				margin:'0 0 0 10',
				text:'微信接口开通帮助',
				handler:function(){
					Ext.create('Ext.window.Window',{
						title:'微信接口开通帮助',
						width:900,
						height:400,
						autoScroll:true,
						html:'<div><img src="'+cfg.getCtx()+'/resources/images/weixin_1.png"/><br><img src="'+cfg.getCtx()+'/resources/images/weixin_2.png"/></div>'
					}).show();
				}
			},'->',{
				text:'保存',
				handler:function(bbb){
					var form = window.down('form'),
						f = form.getForm();
					if(f.isValid()){
						bbb.disable();
						f.submit({
							submitEmptyText:false ,
							url:cfg.getCtx()+'/b2c/minishop/update',
			                success: function(form, action) {
			                	rg.getStore().load();
			                	window.close();
			                	util.success('保存成功');
			                },
			                failure: function(form, action) {
			                	bbb.enable();
			                	var state = action.result?action.result.statusCode:0,
			                		errors = ['保存结果异常', '域名与存在'];
			                    util.error(errors[0-parseInt(state)]);
			                }
						});
					}else{
						util.alert('信息录入不完整！');
					}
				}
			}]);
			window.show();
			form = window.down('form');
			formData = util.pmModel({data:sel.data});
			form.getForm().setValues(formData);
			form.down('filefield[name=LOGO]').setRawValue(formData['pm[LOGO]']);
			form.down('filefield[name=CODE]').setRawValue(formData['pm[CODE]']);
			form.down('filefield[name=WX_IMG]').setRawValue(formData['pm[WX_IMG]']);
			
		}
	},
	openAccountUser:function(btn,params,callBackParams,callBackWindow){
		var me = this,departStore = util.createGridStore(cfg.getCtx()+'/site/depart/list?COMPANY_ID='+params.COMPANY_ID,Ext.create('app.model.site.company.depart'),'1',20),
		departGrid = Ext.create('Ext.grid.Panel',{
			region:'west',
			width:155,
	    	itemId:'departGrid',
		    dockedItems:[{
		    	itemId:'powertool',
	    		xtype:'toolbar',
	    		height:44,
	        	items:[{
	        		xtype:'label',
	        		html:'<span class="yellow-color"></span>'
	        	}]
	    	}],
	    	style:'border-right:1px solid #d1d1d1;',
	    	store:departStore,
	        columns:[{
		        text: '部门名称',
		        flex:1,
		        dataIndex: 'TEXT',
		        renderer: function(value,c,r){
		        	return '<div class="ht20">'+value+'</div>';
		        }
		    }]
		}),
		employeeStore = util.createGridStore(cfg.getCtx()+'/b2b/user/list',Ext.create('app.model.b2b.user.model'),'0',20),
		employeeGrid = Ext.create('Ext.grid.Panel',{
				region:'center',
				dockedItems:[{
			    	itemId:'powertool',
		    		xtype:'toolbar',
		        	items:[{
		        		text:'设置顾问',
			        	handler:function(_btn){
			        		var rec;
			        		if(rec = util.getSingleModel(employeeGrid)){
			        			if(callBackParams){
				        			Ext.Ajax.request({
										url:cfg.getCtx()+'/b2c/minishop/open',
										params:{companyId:callBackParams.COMPANY_ID,userId:callBackParams.ID,accountUserId:rec.get('ID'),accountUserName:rec.get('USER_NAME')},
										success:function(){
											util.success("微店开通成功!");
											window.close();
											callBackWindow.close();
											me.getMinishopGrid().getStore().load();
										},
										failure:function(){
											window.close();
											callBackWindow.close();
											util.error("微店开通失败！");
										}
									});
				        		}else{
				                    Ext.Ajax.request({
										url:cfg.getCtx()+'/b2c/minishop/set/account/user',
										params:{id:params.ID,userId:rec.get('ID'),userName:rec.get('USER_NAME')},
										success:function(){
											util.success("微店设置顾问成功!");
											window.close();
											me.getMinishopGrid().getStore().load();
										},
										failure:function(){
											util.error("微店设置顾问失败！");
										}
									});
				        		}
			        		}
			        	}
		        	}]
		    	}],
		    	columnLines: true,
		    	selModel :{mode:'SINGLE'},
		    	selType:'checkboxmodel',
		    	store:employeeStore,
		    	//bbar:util.createGridBbar(employeeStore),
		        columns:[{
			        text: '登录名',
			        width:150,
			        dataIndex: 'USER_NAME',
			        renderer: function(value,c,r){
			        	var s='';
			        	if(params.ACCOUNT_USER_NAME&&value == params.ACCOUNT_USER_NAME){
			        		s = '<i class="iconfont blue-color f18" data-qtip="顾问">&#xe638;</i> ';
			        	}
			        	return '<div class="ht20">'+s+value+'</div>';
			        }
			    },{
			        text: '姓名',
			        width:120,
			        dataIndex: 'CHINA_NAME'
			    },{
			        text: '手机',
			        flex:1,
			        dataIndex: 'MOBILE'
			    }],
		    	width:300,
		    	minWidth:300
		}),window = Ext.create('Ext.window.Window',{
			title:util.windowTitle(util.glyphToFont(btn?btn.glyph:'xe638@iconfont'),btn?btn.getText():'设置顾问',''),
			width:800,
			height:380,
			modal:true,
			draggable:false,
			resizable:false,
			layout: 'border',
			items:[departGrid,employeeGrid]
		});
		window.show();
		departGrid.on('itemclick',function(grid, record, item, index, e, eOpts){
			employeeGrid.getStore().getProxy().setExtraParams({DEPART_ID:record.get('ID'),limit:10000});
			employeeGrid.getStore().load();
		});
	},
	setAccountUser:function(btn){
		var me = this,rg = this.getMinishopGrid(),sel;
		if(sel = util.getSingleModel(rg)){
			me.openAccountUser(btn, {ID:sel.get('ID'),COMPANY_ID:sel.get('COMPANY_ID'),ACCOUNT_USER_NAME:sel.get('ACCOUNT_USER_NAME')}, null);
		}
	}
});