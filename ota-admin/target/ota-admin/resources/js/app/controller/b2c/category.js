Ext.define('app.controller.b2c.category', {
	extend: 'app.controller.common.BaseController',
	views:['app.view.resource.route.pub.detail','app.view.site.companyWapTJGrid'],
	config: {
		control: {
			'toolbar#categoryTool button#sortMain': {
	             click: 'sort'
	        },
	        'companyWapTJGrid toolbar button#add': {
	             click: 'add'
	        },
	        'companyWapTJGrid toolbar button#del': {
	             click: 'del'
	        },
	        'companyWapTJGrid toolbar button#top': {
	             click: 'top'
	        },
	        'gridpanel#categoryGrid':{
	        	selectionchange:'categoryGridSelected'
	        },
	        'companyWapTJGrid':{
	        	cellclick:function( view, td, cellIndex, record, tr, rowIndex, e, eOpts ){
					var rec = record,getUrl='';
					if(cellIndex==2){
						if(e.target.tagName=='I'&&e.target.className.indexOf('info')!=-1)
		                	this.detailWindow(record.get('ID'));
					}
				}
	        }
		},
		refs: {
			categoryGrid:'gridpanel#categoryGrid',
			baseGrid:'companyWapTJGrid',
			categoryTool:'toolbar#categoryTool',
			baseTool:'basegrid toolbar'
		}
	},
	onBaseGridRender:function(g){
		var me = this;
		setTimeout(function(){
		Ext.Ajax.request({
		    url: util.getPowerUrl(),
		    success: function(response, opts) {
		        var obj = Ext.decode(response.responseText),
		        	items = obj.children,citems=[],bitems=[];
		        for(var i=0;i<items.length;i++){
		        	if(items[i].itemId.indexOf('Route')!=-1)continue;
		        	delete items[i].checked;
		        	//如果当前用户没有部门id 隐藏设计线路修改线路
		        	if(items[i].itemId.indexOf('Main')!=-1){
		        		citems.push(items[i]);
		        	}else{
		        		bitems.push(items[i]);
		        	}
		        }
		        setTimeout(function(){
		        	me.getCategoryTool().removeAll();
			        me.getCategoryTool().add(citems);
		        },500);
		        //items.push('->');
		        util.createGridTbar(g,bitems);
		    }
		});
		},500);
	},
	detailWindow :function(routeId){
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
		rd.setRouteId(routeId);
	},
	categoryGridSelected:function(s, selected, eOpts){
		var sel = selected[0],yesBtnIds = [];
		if(sel.get('ORDER_BY')==-1){
		}else{
			yesBtnIds = ['sortMain'];
		}
		this.getBaseGrid().setCategoryId(sel.get('ID'));
		this.btnToggle(yesBtnIds,false);
	},
	btnToggle:function(btnIds,disabled){
		var tbar = this.getCategoryTool(),
			btns = tbar.query('button'),
			initNoBtnIds = [];
		Ext.each(btns,function(btn,index){
			if(initNoBtnIds.length>0&&Ext.Array.contains(initNoBtnIds,btn.getItemId())){
			}else{
				if(btnIds.length==0){
					btn.setDisabled(true);
				}else{
					if(Ext.Array.contains(btnIds,btn.getItemId())){
						btn.setDisabled(disabled);
					}
				}
			}
		});
	},
	sort:function(btn){
		var me=this,sel,grid=this.getCategoryGrid();
		if(sel = util.getSingleModel(grid)){
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
					                	grid.getStore().load();
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
	},
	add:function(btn){
		var me = this,grid = this.getCategoryGrid(),sel;
		if(sel = util.getSingleModel(grid)){
			var supplyStore = util.createGridStore(cfg.getCtx()+'/site/company/list/web/recommend/supply',Ext.create('Ext.data.Model',{
				fields: [
				         'SALE_COMPANY','SALE_COMPANY_ID','SALE_BRAND_NAME'
				]
			}));
			var win = Ext.create('Ext.window.Window',{
	   			title: util.windowTitle(util.glyphToFont(btn.glyph),sel.get('CATEGORY'),''),
	   			width:850,
	   			height:450,
	   			draggable:false,
				resizable:false,
				modal:true,
	   			bodyStyle:'background:#fff;padding:1px;font-size:16px;color:#bbb;',
	   			layout:'fit',
	   			items:[Ext.create('app.view.site.companyWapTJGrid',{
	   				url:cfg.getCtx()+'/site/company/list/web/recommend?categoryId='+sel.get('ID')+'&filter=1',
	   				dockedItems:[{
	   					labelWidth:72,
	   					labelAlign:'right',
			    		xtype:'checkboxgroup',
						fieldLabel:'主题属性',
						layout:'auto',
						defaults:{cls:'inline_checkbox',name:'themes'},
						items: [
			                {boxLabel: '沙漠', inputValue:'沙漠'},
			                {boxLabel: '美食', inputValue:'美食'},
			                {boxLabel: '宗教', inputValue:'宗教'},
			                {boxLabel: '游学', inputValue:'游学'},
			                {boxLabel: '探险', inputValue:'探险'},
			                {boxLabel: '海岛度假', inputValue:'海岛度假'},
			                {boxLabel: '夏令营', inputValue:'夏令营'},
			                {boxLabel: '漂流', inputValue:'漂流'},
			                {boxLabel: '邮轮', inputValue:'邮轮'},
			                {boxLabel: '夕阳红', inputValue:'夕阳红'},
			                {boxLabel: '温泉', inputValue:'温泉'},
			                {boxLabel: '古镇', inputValue:'古镇'},
			                {boxLabel: '滑雪', inputValue:'滑雪'},
			                {boxLabel: '红色', inputValue:'红色'},
			                {boxLabel: '专列', inputValue:'专列'},
			                {boxLabel: '门票', inputValue:'门票'},
			                {boxLabel: '度假', inputValue:'度假'},
			                {boxLabel: '亲子', inputValue:'亲子'},
			                {boxLabel: '蜜月', inputValue:'蜜月'},
			                {boxLabel: '青年会', inputValue:'青年会'}
			            ],
			            listeners:{
			            	change:function(rg, newValue, oldValue, eOpts ){
			            		var g = win.down('companyWapTJGrid'),themes=[];
			            		if(Ext.isArray(newValue.themes)){
			            			Ext.Array.each(newValue.themes,function(o,i){
			            				themes.push({'value':o});
			            			});
			            		}else{
			            			themes.push({'value':newValue.themes});
			            		}
   						        g.getStore().getProxy().setExtraParam('routeThemes',Ext.encode(themes));
   						        g.getStore().load();
			            	}
			            }
			    	},{
	   					xtype:'toolbar',
	   					style:'border:none;',
	   					items:[{
	   						labelWidth:65,
		   					labelAlign:'right',
				    		xtype:'radiogroup',
							fieldLabel:'线路属性',
							layout:'auto',
							defaults:{cls:'inline_checkbox',name:'attrs'},
							items: [
							    {boxLabel: '全部', inputValue:'',checked:true},
				                {boxLabel: '品质', inputValue:'品质'},
				                {boxLabel: '豪华', inputValue:'豪华'},
				                {boxLabel: '自由行', inputValue:'自由行'},
				                {boxLabel: '特价', inputValue:'特价'},
				                {boxLabel: '包机', inputValue:'包机'},
				                {boxLabel: '纯玩', inputValue:'纯玩'}
				            ],
				            listeners:{
				            	change:function(rg, newValue, oldValue, eOpts ){
				            		var g = win.down('companyWapTJGrid');
	   						        g.getStore().getProxy().setExtraParam('routeAttrs',newValue);
	   						        g.getStore().load();
				            	}
				            }
	   					}]
	   				},{
	   					xtype:'toolbar',
	   					itemId:'endTools',
	   					style:'padding:0 0 10px 7px;border:none;',
	   					items:[{
	   						labelWidth:65,
	   						labelAlign:'right',
	   						fieldLabel:'供应商',
	   						xtype:'combo',
	   						forceSelection: true,
	   						editable:false,
	   			            valueField: 'value',
	   			            displayField: 'text',
	   			            store:util.createComboStore([{
	   			            	text:'全部',
	   			            	value:''
	   			            },{
	   			            	text:'国内',
	   			            	value:1
	   			            },{
	   			            	text:'出境',
	   			            	value:2
	   			            }]),
	   			            width:145,
	   			            minChars: 0,
	   			            value:'',
	   			            queryMode: 'local',
	   			            typeAhead: true,
	   			            listeners:{
	   			            	change:function(c, newValue, oldValue, eOpts ){
	   			            		var c1 = c.nextSibling();
	   			            		c1.getStore().getProxy().setExtraParam('type',newValue);
	   			            		c1.getStore().load();
	   			            	}
	   			            }
	   					},{
	   						xtype:'combo',
	   						width:240,
	   						minChars: 1,
	   					    queryParam: 'supplyName',
	   					    displayField: 'SALE_COMPANY',
	   					    pageSize:20,
	   					    store:supplyStore,
	   					    forceSelection:true,
	   					    multiSelect:false,
	   						autoSelect:false,
	   					    queryMode: 'remote',
	   					    triggerAction: 'all',
	   					    valueField: 'SALE_COMPANY_ID',
	   					    emptyText:'全部供应商',
	   					    listConfig:{
	   					    	minWidth:360,
	   					    	itemTpl:[
	   								 '<tpl for=".">',
	   						            '<li class="city-item">{SALE_COMPANY}<span>{SALE_BRAND_NAME}</span></li>',
	   						        '</tpl>'
	   							]
	   					    },
	   					    listeners:{
	   					    	change:function(c, newValue, oldValue, eOpts ){
	   					    		var g = win.down('companyWapTJGrid');
	   			            		g.getStore().getProxy().setExtraParam('supplyId',newValue);
	   			            		g.getStore().load();
	   			            	}
	   					    }
	   					},{
	   						glyph:'xe63d@iconfont',
	   						tool:'清空重新选择供应商',
	   						handler:function(b){
	   							var g = win.down('companyWapTJGrid');
	   							b.previousSibling().reset();
	   							b.previousSibling().previousSibling().reset();
	   							g.getStore().getProxy().setExtraParam('supplyId','');
	   		            		g.getStore().load();
	   						}
	   					},'->',{
	   		   				xtype:'combo',
	   						forceSelection: true,
	   						editable:false,
	   			            valueField: 'value',
	   			            displayField: 'text',
	   			            store:util.createComboStore([{
	   			            	text:'全部',
	   			            	value:''
	   			            },{
	   			            	text:'周边游',
	   			            	value:1
	   			            },{
	   			            	text:'国内游',
	   			            	value:2
	   			            },{
	   			            	text:'出境游',
	   			            	value:3
	   			            }]),
	   			            width:75,
	   			            minChars: 0,
	   			            value:'',
	   			            queryMode: 'local',
	   			            typeAhead: true,
	   			            listeners:{
	   			            	change:function(c, newValue, oldValue, eOpts ){
	   			            		var g = win.down('companyWapTJGrid');
	   						        g.getStore().getProxy().setExtraParam('routeType',newValue);
	   						        g.getStore().load();
	   			            	}
	   			            }
	   		   			}]
	   				}]
	   			})],
	   			listeners:{
	   				close:function(){
	   					me.getBaseGrid().getStore().load();
	   				}
	   			}
			});
			var gd = win.down('companyWapTJGrid');
			gd.down('toolbar#endTools').add({
		   			emptyText:'产品标题/编号/目的地',
		        	xtype:'searchfield',
		        	store:gd.getStore(),
		        	width:200
			});
			win.show();
		}
	},
	del:function(btn){
		var grid = this.getBaseGrid(),sel;
		//if(sel = util.getSingleModel(grid)){
			util.delGridModel(grid,'/site/company/del/web/recommend');
		//}
	},
	top:function(btn){
		var grid = this.getBaseGrid(),sel;
		//if(sel = util.getSingleModel(grid)){
			util.switchGridModel(grid,'/b2c/category/top');
		//}
	}
});