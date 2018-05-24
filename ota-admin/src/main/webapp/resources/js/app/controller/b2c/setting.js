Ext.define('app.controller.b2c.setting', {
	extend: 'app.controller.common.BaseController',
	views:['app.view.resource.route.pub.detail','app.view.b2c.selectRoutes','Ext.ux.form.ExtKindEditor'],
	config: {
		control: {
			'toolbar button#recommend':{
				click:'recommend'
			},
			'toolbar button#audit':{
				click:'audit'
			},
			'toolbar button#isuse':{
				click:'isuse'
			},
			'toolbar button#info':{
				click:'info'
			}
		},
		refs: {
			settingGrid: 'gridpanel[itemId=basegrid]'
        }
	},
	info:function(btn){
		var rg = this.getSettingGrid(),
			window,form,sel,selData,formData={},pv=cfg.getUser().userType;
		if(sel = util.getSingleModel(rg)){
			window = Ext.create('Ext.window.Window',{
				title:util.windowTitle(util.glyphToFont(btn.glyph),btn.getText(),''),
				width:'90%',
				height:'90%',
				modal:true,
				draggable:false,
				resizable:false,
				maximizable:false,
	   			layout:'fit',
				items:[{
					xtype:'form',
	    			scrollable:'y',
					fieldDefaults: {
				        labelAlign: 'right',
				        labelWidth: 90,
				        msgTarget: 'qtip'
				    },
					bodyPadding: 5,
					items:[{
						xtype:'hiddenfield',
						name:'pm[ID]'
					},{
						xtype:'textfield',
						anchor:'100%',
						fieldLabel:'网站名称',
						name:'pm[TITLE]',
						allowBlank:false
					}/*,{
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
					}*/,{
						xtype:'filefield',
						anchor:'100%',
						fieldLabel:'LOGO',
						name:'LOGO',
						buttonConfig: {
			                text:'',
			                disabled:(pv=='01'),
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
			                disabled:(pv=='01'),
							ui:'default-toolbar',
			                glyph: 'xe648@iconfont'
			            }
					}/*,{
						xtype:'filefield',
						anchor:'100%',
						fieldLabel:'二维码',
						name:'CODE',
						buttonConfig: {
			                text:'',
			                disabled:(pv=='01'),
							ui:'default-toolbar',
			                glyph: 'xe648@iconfont'
			            }
					}*/,{
						xtype:'filefield',
						anchor:'100%',
						fieldLabel:'微信公众号',
						name:'WX_IMG',
						buttonConfig: {
			                text:'',
			                disabled:(pv=='01'),
							ui:'default-toolbar',
			                glyph: 'xe648@iconfont'
			            }
					}/*,{
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
					}*/,{
						xtype: 'fieldcontainer',
						fieldLabel:'联系客服',
				        labelWidth: 90,
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
				    	anchor:'-67',
				    	height:300,
				    	fieldLabel:'关于我们',
						name:'ABOUT'
					}],
					buttons:[{
						text:'保存',
						handler:function(bbb){
							var form = window.down('form'),
								f = form.getForm();
							if(f.isValid()){
								bbb.disable();
								f.submit({
									submitEmptyText:false ,
									url:cfg.getCtx()+'/site/company/save/wap',
					                success: function(form, action) {
					                	util.success('网站设置成功');
					                	rg.getStore().load();
					                	window.close();
					                },
					                failure: function(form, action) {
					                	bbb.enable();
					                	var state = action.result?action.result.statusCode:0,
					                		errors = ['网站设置异常'];
					                    util.error(errors[0-parseInt(state)]);
					                }
								});
							}
						}
					}]
				}]
			});
			window.show();
			form = window.down('form');
			form.mask();
			Ext.Ajax.request({
				url:cfg.getCtx()+'/site/company/detail/wap',
				success:function(response){
					form.unmask();
					var data = Ext.decode(response.responseText).data;
					
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
	isuse:function(btn){
		util.switchGridModel(this.getSettingGrid(),'/b2c/minishop/isuse');
	},
	audit:function(btn){
		var me = this,rg = this.getSettingGrid(),
			window,form,sel;
		if(sel = util.getSingleModel(rg)){
			var shStore = util.createGridStore(cfg.getCtx()+'/b2c/route/list',Ext.create('Ext.data.Model',{
				fields: [
				         'ID','COMPANY_ID','NO','THEMES','ROUTE_WAP_PRICE', 'TITLE','ROUTE_ID',
				         'ORDER_BY','COMPANY_ID','USER_ID','CREATE_TIME',
				         'DAY_COUNT','ATTR','FACE'
					]
				}));
			var shGrid = Ext.create('Ext.grid.Panel',{
				store:shStore,
				bbar:util.createGridBbar(shStore),
				selType:'checkboxmodel',
				selModel:{mode:'MULTI'},
				dockedItems:[{
					xtype:'toolbar',
		        	style:'padding-left:5px;',
		            overflowHandler: 'menu',
		        	items:[{
		        		glyph:'xe678@iconfont',
		        		text:'线路审核',
		        		handler:function(){
		        			var win = Ext.create('Ext.window.Window',{
		        	   			title: util.windowTitle(util.glyphToFont(btn.glyph),'审核线路展示到前台',''),
		        	   			width:'80%',
		        	   			height:'80%',
		        	   			draggable:false,
		        				resizable:false,
		        				modal:true,
		        	   			bodyStyle:'background:#fff;padding:1px;font-size:16px;color:#bbb;',
		        	   			layout:'fit',
		        	   			items:[{
		        	   				storeUrl:cfg.getCtx()+'/b2c/route/select',
		        	   				saveUrl:cfg.getCtx()+'/b2c/route/save',
		        	   				xtype:'selectRoutes'
		        	   			}],
		        	   			listeners:{
		        	   				close:function(){
		        	   					shStore.load();
		        	   				}
		        	   			}
		        			});
		        			win.show();
		        		}
		        	},{
		        		glyph:'xe62c@iconfont',
		        		text:'删除',
		        		handler:function(){
		        			util.delGridModel(shGrid,'/b2c/route/del');
		        		}
		        	},'->',{
						emptyText:'搜索关键字',
			        	xtype:'searchfield',
			        	store:shStore
					}]
				}],
				columnLines: true,
				columns:[{
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
		        }]
			});
			shGrid.on('cellclick',this.detailWindow,this);
			window = Ext.create('Ext.window.Window',{
				title:util.windowTitle(util.glyphToFont(btn.glyph),btn.getText(),''),
				width:'90%',
				height:'90%',
				maximized:false,
				modal:true,
				draggable:false,
				resizable:false,
				layout: 'fit',
				items:[shGrid]
			});
			window.show();
		}
	},
	recommend:function(btn){
		var me = this,rg = this.getSettingGrid(),
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
		    	dockedItems:[{
		    		xtype:'toolbar',
		    		items:[{
		    			text:'排序',
		    			glyph:'xe64a@iconfont',
		    			handler:function(btn){
		    				var sel;
		    				if(sel = util.getSingleModel(lxGrid)){
		    					var win = Ext.create('Ext.window.Window',{
		    			   			title: util.windowTitle(util.glyphToFont(btn.glyph),sel.get('CATEGORY'),''),
		    			   			width:360,
		    			   			height:150,
		    			   			draggable:false,
		    						resizable:false,
		    						modal:true,
		    			   			bodyStyle:'background:#fff;font-size:16px;color:#bbb;',
		    			   			layout: {
		    					        type: 'vbox',
		    					        pack: 'start',
		    					        align: 'stretch'
		    					    },
		    			   			items:[{
		    							flex:1,
		    							fieldDefaults: {
		    						        labelAlign: 'right',
		    						        labelWidth: 100
		    						    },
		    							xtype:'form',
		    							bodyPadding:'16 0 0 0',
		    			   				items: [{
		    			   					xtype:'hiddenfield',
		    			   					name:'pm[ID]',
		    			   					value:sel.get('ID')
		    			   				},{
		    						        xtype: 'numberfield',
		    						        name: 'pm[ORDER_BY]',
		    						        anchor:'50%',
		    						        allowBlank:false,
		    						        fieldLabel: '排序序号',
		    						        minValue:0,
		    						        value:sel.get('ORDER_BY')||0
		    						    }],
		    						    buttons:[{
		    						    	text:'保存',
		    						    	handler:function(b){
		    						    		var f = b.up('form'),bf = f.getForm();
		    						    		if(bf.isValid()){
		    						    			bf.submit({
		    						    				submitEmptyText:false ,
		    			   								url:cfg.getCtx()+'/b2c/category/sort',
		    							                success: function(form, action) {
		    							                	lxGrid.getStore().load();
		    							                    util.success('排序设置成功');
		    							                    win.close();
		    							                },
		    							                failure: function(form, action) {
		    							                    util.error('排序设置失败');
		    							                    win.close();
		    							                }
		    			   							});
		    						    		}
		    						    	}
		    						    }]
		    			   			}]
		    			   		}).show();
		    				}
		    			}
		    		}]
		    	}],
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
		    			   			width:'80%',
		    			   			height:'80%',
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
		    		},{
		    			text:'精彩推荐',
		    			glyph:'xe605@iconfont',
		    			handler:function(_btn){
		    				//var _sel = util.getSingleModel(lxGrid);
		    				util.switchGridModel(tjGrid,'/b2c/category/top');
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
		        },{
		        	text:'推荐排序',
		        	width:80,
		        	dataIndex:'TOP_ORDER_BY',
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
				width:'90%',
				height:'90%',
				maximized:false,
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
	}
});