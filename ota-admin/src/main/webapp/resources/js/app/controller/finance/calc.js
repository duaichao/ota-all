Ext.define('app.controller.finance.calc', {
	extend: 'app.controller.common.BaseController',
	views:['app.view.finance.calcGrid','Ext.ux.form.field.Month','app.view.common.CompanyCombo'],
	config: {
		control: {
			'button[itemId=history]':{
				click:'calcHistory'
			},
			'button[itemId=calc]':{
				click:'calcOrder'
			},
			'button[itemId=tbtn]':{
				click:'cancelOrder'
			},
			'grid[itemId=basegrid]':{
				selectionchange: function(view, records) {
	            },
	            cellclick:function( view, td, cellIndex, record, tr, rowIndex, e, eOpts ){
					var rec = record,getUrl='';
					if(cellIndex==2){
						if(e.target.tagName=='A'){
							Ext.factory({
								orderRecord:rec
							},'app.view.order.detail').show();
						}
						if(e.target.tagName=='I'&&e.target.className.indexOf('orange-color')!=-1){
							Ext.factory({
								orderRecord:rec
							},'app.view.order.status').show();
	                	}
					}
					if(cellIndex==4){
						if(e.target.tagName=='A'&&e.target.className.indexOf('title')!=-1){
							if(parseInt(rec.get('PRODUCE_TYPE'))== 2||parseInt(rec.get('PRODUCE_TYPE'))== 3){
								Ext.require('app.view.resource.route.pub.detail',function(){
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
									rd.setRouteId(rec.get('PRODUCE_ID'));
								});
							}
						}
					}
					if(cellIndex==5){
						if(e.target.tagName=='I'&&e.target.className.indexOf('iconfont')!=-1){
							Ext.factory({
								orderRecord:rec
							},'app.view.order.recharge').show();
						}
					}
					if(cellIndex==13){
						
					}
				}
			}
		},
		refs: {
			calcGrid : 'grid[itemId=basegrid]'
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
		        	ut = cfg.getUser().userType;
		        for(var i=0;i<items.length;i++){
		        	ubtn.push(items[i]);
		        }
		        
		        
		        
		        ubtn.push('->');
		        ubtn.push({
	        	 	xtype:'tagfield',
	        	 	itemId:'morder',
					value:'',
					labelAlign:'right',
					fieldLabel:'对账状态',
					typeAhead:false,
					editable:false,  
					inputType:'hidden',
					width:320,
					//typeAhead: true,
		            //triggerAction: 'all',
		            store: [
		                //['','全部'],
		                ['0','待对账'],
		                ['-1','非对账'],
		                ['1','已对账'],
		                ['2','已打款']
		            ]
		        });
		        ubtn.push({
		        	xtype:'combo',
					forceSelection: true,
					editable:false,
		            valueField: 'value',
		            displayField: 'text',
		            store:util.createComboStore([{
		            	text:'创建日期(用于对账)',
		            	value:1
		            },{
		            	text:'出团日期(用于查询)',
		            	value:2
		            }]),
		            width:150,
		            value:1,
		            queryMode: 'local',
		            typeAhead: true
	    		});
		        ubtn.push({
	    			width:105,
	    			xtype:'datefield',
	    			format:'Y-m-d',
	    			endDateField: 'endxddt',
			        itemId:'startxddt',
			        showToday:false,
			        editable:false,
			        vtype: 'daterange',
			        emptyText:'开始日期'
		        });
		        ubtn.push({
			        emptyText:'截止日期',
	    			width:105,
	    			editable:false,
	    			xtype:'datefield',
	    			format:'Y-m-d',
	    			itemId:'endxddt',
	    			showToday:false,
		            startDateField: 'startxddt',
		            vtype: 'daterange'
		        });
		        var combo = Ext.create('Ext.form.field.ComboBox',{
				    minChars: 1,
				    itemId:'companycombo',
				    queryParam: 'query',
				    displayField: 'COMPANY',
				    //pageSize:20,
				    focusOnToFront:false,
				    forceSelection:true,
				    queryMode: 'remote',
				    triggerAction: 'all',
				    valueField: 'ID',
				    emptyText:'选择一个供应商',
				    listConfig:{
				    	minWidth:360,
				    	itemTpl:[
							 '<tpl for=".">',
							 	'<div class="x-boundlist-item-line">',
					            '<h3>{COMPANY}</h3>',
					            '<span>{BRAND_NAME}</span>',
					            '</div>',
					        '</tpl>'
						]
				    },
				    listeners : {
						beforequery: function(qe){
							delete qe.combo.lastQuery;
						},
						change:function( combo, newValue, oldValue, eOpts ){
							if(newValue){
								var b = combo.nextSibling().nextSibling();
								me.queryData(b);
							}
						}
				    },
				    store:Ext.create('Ext.data.Store',{
					    model:Ext.create('app.model.Company'),
					    //pageSize:20,
					    autoLoad: false, 
					    proxy: {
					        type: 'ajax',
					        noCache:true,
					        url: cfg.getCtx()+'/site/company/order/supply',
					        reader: {
					            type: 'json',
					            rootProperty: 'data',
					            totalProperty:'totalSize'
					        }
					   	}
				    })
		        });
		        combo.getStore().on('beforeload',function(store){
		    		var endTime = combo.previousSibling(),
		    			beginTime = endTime.previousSibling(),
		    			timeType = beginTime.previousSibling(),
		    			orderType = timeType.previousSibling();
		    		store.getProxy().setExtraParams({
	        			beginTime:Ext.Date.format(beginTime.getValue(),'Y-m-d'),
	        			endTime:Ext.Date.format(endTime.getValue(),'Y-m-d'),
	        			timeType:timeType.getValue(),
	        			orderType:orderType.getValue(),
	        			companyType:1
	        		});
		    	});
		        ubtn.push(combo);
		        //旅行社
		        var rcombo = Ext.create('Ext.form.field.ComboBox',{
				    minChars: 1,
				    itemId:'companyRetailcombo',
				    queryParam: 'query',
				    displayField: 'COMPANY',
				    //pageSize:20,
				    focusOnToFront:false,
				    forceSelection:true,
				    queryMode: 'remote',
				    triggerAction: 'all',
				    valueField: 'ID',
				    emptyText:'选择一个旅行社',
				    listConfig:{
				    	minWidth:360,
				    	itemTpl:[
							 '<tpl for=".">',
							 	'<div class="x-boundlist-item-line">',
							 	'<h3>{COMPANY}</h3>',
					            '<span>{SHORT_NAME}</span>',
					            '</div>',
					        '</tpl>'
						]
				    },
				    listeners : {
						beforequery: function(qe){
							delete qe.combo.lastQuery;
						} 
				    },
				    store:Ext.create('Ext.data.Store',{
					    model:Ext.create('app.model.Company'),
					    //pageSize:20,
					    autoLoad: false, 
					    proxy: {
					        type: 'ajax',
					        noCache:true,
					        url: cfg.getCtx()+'/site/company/order/supply',
					        reader: {
					            type: 'json',
					            rootProperty: 'data',
					            totalProperty:'totalSize'
					        }
					   	}
				    })
		        });
		        rcombo.getStore().on('beforeload',function(store){
		        	var supplyId = rcombo.previousSibling(),
		        		endTime = supplyId.previousSibling(),
		    			beginTime = endTime.previousSibling(),
		    			timeType = beginTime.previousSibling(),
		    			orderType = timeType.previousSibling();
		    		store.getProxy().setExtraParams({
	        			beginTime:Ext.Date.format(beginTime.getValue(),'Y-m-d'),
	        			endTime:Ext.Date.format(endTime.getValue(),'Y-m-d'),
	        			supplyId:supplyId.getValue(),
	        			timeType:timeType.getValue(),
	        			orderType:orderType.getValue(),
	        			companyType:2
	        		});
		    	});
		        ubtn.push(rcombo);
		        
		        
		        
		        
		        
		        ubtn.push({
		        	text:'查询',
		        	handler:function(b){
		        		me.queryData(b);
		        	}
		        });
		        ubtn.push({
		        	text:'导出订单',
		        	handler:function(b){
		        		var orderType,beginTime,endTime,supplyId,agencyId,timeType;
		        		agencyId = b.previousSibling().previousSibling();
		        		supplyId = agencyId.previousSibling();
		        		endTime = supplyId.previousSibling();
		        		beginTime = endTime.previousSibling();
		        		timeType = beginTime.previousSibling();
		    			orderType = timeType.previousSibling();
		        		
		        		agencyId = agencyId.getValue()||'';
		        		supplyId = supplyId.getValue()||'';
		        		endTime = endTime.getValue()||'';
		        		beginTime = beginTime.getValue()||'';
		        		timeType = timeType.getValue()||'';
		        		orderType = orderType.getValue()||'';
		        		
		        		if(beginTime==''||endTime==''){
		        			util.alert('必须选择一个账期/或出团日期范围');
		        			return;
		        		}
		        		beginTime = Ext.Date.format(beginTime,'Y-m-d');
		        		endTime = Ext.Date.format(endTime,'Y-m-d');
		        		util.downloadAttachment(cfg.getCtx()+'/finance/calc/export?supplyId='+supplyId+'&agencyId='+agencyId+'&orderType='+orderType+'&beginTime='+beginTime+'&endTime='+endTime+'&timeType='+timeType);
		        	}
		        });
		        util.createGridTbar(g,ubtn);
		        g.addDocked({
		        	hidden:true,
		        	itemId:'lockedTool',
		        	xtype:'toolbar',
		        	style:'padding:0px;padding-bottom:5px;',
		        	dock:'top',
		        	items:['->',{
			        	xtype:'container',
						itemId:'ttip'
			        },{
						itemId:'tbtn',
						glyph:'xe6b7@iconfont',
						text:'取消'
			        }]
		        });
		    }
		});
		},500);
	},
	queryData:function(b){
		var orderType,beginTime,endTime,supplyId,agencyId,timeType,g,me=this;
		g = this.getCalcGrid();
		agencyId = b.previousSibling();
		supplyId = agencyId.previousSibling();
		endTime = supplyId.previousSibling();
		beginTime = endTime.previousSibling();
		timeType = beginTime.previousSibling();
		orderType = timeType.previousSibling();
		
		agencyId = agencyId.getValue()||'';
		supplyId = supplyId.getValue()||'';
		endTime = endTime.getValue()||'';
		beginTime = beginTime.getValue()||'';
		timeType = timeType.getValue()||'';
		orderType = orderType.getValue()||'';
		
		if(beginTime==''||endTime==''){
			util.alert('必须选择一个账期或出团日期范围');
			return;
		}
		beginTime = Ext.Date.format(beginTime,'Y-m-d');
		endTime = Ext.Date.format(endTime,'Y-m-d');
		Ext.Ajax.request({
			url:cfg.getCtx()+'/finance/calc/syn',
			params:{beginTime:beginTime,endTime:endTime,supplyId:supplyId,agencyId:agencyId,orderType:orderType,timeType:timeType},
			success:function(response, opts){
				var obj = Ext.decode(response.responseText),
					errors = ['对账查询异常','对账查询异常','其他用户正在对账 『'+obj.message+'』'],
					state = obj?obj.statusCode:0;
				var tbar = g.down('toolbar#lockedTool');
				
				//如果不选供应商 不显示锁定条
				if(supplyId){
					tbar.show();
				}
				if(!obj.success){
					tbar.down('container#ttip').update('<div class="red-color f14"><i class="iconfont f20 red-color">&#xe6ae;</i> 其他用户正在对账 『'+obj.message+'』</div>');
					tbar.down('button#tbtn').hide();
					return;
				}else{
					tbar.down('container#ttip').update('<div class="green-color f14"><i class="iconfont f20 green-color">&#xe6b0;</i> 已锁定供应商 『'+beginTime+'-'+endTime+'』 账单，如需解锁请点击</div>');
					tbar.down('button#tbtn').show();
					me.getCalcGrid().getStore().getProxy().setExtraParams({
	        			beginTime:beginTime,
	        			endTime:endTime,
	        			supplyId:supplyId,
	        			agencyId:agencyId,
	        			timeType:timeType,
	        			orderType:orderType
	        		});
	        		/*if(orderType=='2'){
	        			var filter = new Ext.util.Filter({
	        				id:'orderFilter',
						    filterFn: function(rs) {
						        var ds = rs.get('ACCOUNT_STATUS') || '';
					        	if((parseInt(rs.get('STATUS'))==2||parseInt(rs.get('STATUS'))==5)&&ds==''){
						    		return true;
						    	}else{
						    		return false;
						    	}
						    }
						});
	        			me.getCalcGrid().getStore().addFilter(filter);
	        		}else{
	        			me.getCalcGrid().getStore().removeFilter('orderFilter');
	        		}*/
	        		me.getCalcGrid().getStore().load();
				}
			}
		});
	},
	calcOrder :function(btn){
		var tools = btn.up('toolbar'),
			orderType = tools.down('combo#morder'),
			timeType = orderType.nextSibling();
			beginTime = timeType.nextSibling(),
			endTime = beginTime.nextSibling(),
			supplyId = endTime.nextSibling(),
			agencyId = supplyId.nextSibling();
			
			var supplyName = supplyId.getRawValue()||'';
			agencyId = agencyId.getValue()||'';
			supplyId = supplyId.getValue()||'';
			endTime = endTime.getValue()||'';
			beginTime = beginTime.getValue()||'';
			orderType = orderType.getValue()||'';
			timeType = timeType.getValue()||'';
		if(timeType=='2'){
			util.alert('出团日期只能查询核对数据，请选择创建日期');
			return;
		}
		if(beginTime==''||endTime==''||supplyId==''){
			util.alert('必须选择一个供应商、账期');
			return;
		}
		beginTime = Ext.Date.format(beginTime,'Y-m-d');
		endTime = Ext.Date.format(endTime,'Y-m-d');
   		if(this.getCalcGrid().getSelection().length==0){
   			util.alert('没有待对账数据');
   			return;
   		}
   		var departIds = this.getCalcGrid().getSelectDepartIds().join(',');
		var me=this,win = Ext.create('Ext.window.Window',{
			title:util.windowTitle(util.glyphToFont(btn.glyph),'对账确认 ',''),
				width:700,
				height:360,
				modal:true,
				draggable:false,
				resizable:false,
				maximizable:false,
				bodyPadding:'2',
	   			layout:'fit',
	   			dockedItems:[{
	   				bodyPadding:'5',
	   				itemId:'tipDocked',
	   				bodyStyle:'background:transparent;',
	   				cls:'low',
	   				tpl:[
	   				     '<div style="font-size:14px;color:{color};">',
	   				     '<i class="iconfont f20" style="color:{color}">&#xe6b0;</i> {supplyName}『 {beginTime} 到 {endTime} 』{msg}',
	   				     '</div>'
	   				],
		        	data:{color:'#53a93f;',supplyName:supplyName,beginTime:beginTime,endTime:endTime,msg:''}
	   			}],
	   			items:[{
	   				autoScroll:true,
	   				items:[{
	   					xtype:'dataview',
		   				border:false,
				        trackOver:false,
				        itemSelector: '.data-view-item',
				        store :Ext.create('Ext.data.Store', {
				        	model:Ext.create('Ext.data.Model',{
				        		fields: [
				        			'COMPANY','AMOUNT','BANK_NO',
				        			'BANK_NAME','OPEN_ADDRESS',
				        			'OPER_NAME','SITE_RELA_ID'
				        		]
				        	}),
				        	autoLoad: true, 
				        	listeners:{
				        		beforeload:function(s){
				        			s.getProxy().setExtraParams({departIds:departIds,beginTime:beginTime,endTime:endTime,supplyId:supplyId,agencyId:agencyId,orderType:orderType});
				        		},
				        		load:function(s,r){
				        			if(r.length==0){
				        				win.down('button#goPayBtn').hide();
				        				var tip = win.down('panel#tipDocked'),
				        					tipData = tip.getData();
				        				tipData.msg = '的待对账订单数为零';
				        				tipData.color = '#dd4b39';
				        				tip.setData(tipData);
				        			}
				        		}
				        	},
						    proxy: {
						        type: 'ajax',
						        noCache:true,
						        url: cfg.getCtx()+'/finance/ready/calc',
						        reader: {
						            type: 'json',
						            rootProperty: 'data'
						        }
						   	}
				        }),
				        tpl: [
				            //'<tpl if="this.len(data)">',
				        	'<tpl for=".">',
				        	'<div class="p-detail" style="margin-bottom:5px;width:686px;">',
			        			'<table>',
			        				'<tr>',
					    				'<td style="background:#f1f1f1;font-size:14px;" colspan="5">',
					    					'<i class="iconfont f18 blue-color">&#xe627;</i> 卖家',
					    				'</td>',
					    			'</tr>',
			        				'<tpl for="supply">',
			        				'<tr>',
			        					'<td width="200">{COMPANY}</td>',
			        					'<td width="80">{AMOUNT}</td>',
			        					'<td width="140">{BANK_NAME}</td>',
			        					'<td width="80">{OPER_NAME}</td>',
			        					'<td>{BANK_NO}</td>',
			        				'</tr>',
			        				'</tpl>',
			        			'</table>',
				        	'</div>',
				        	'<div class="p-detail" style="margin-bottom:5px;width:686px;">',
				        		'<table>',
				        			'<tr>',
					    				'<td style="background:#f1f1f1;font-size:14px;" colspan="5">',
					    					'<i class="iconfont f18 green-color">&#xe610;</i> 买家',
					    				'</td>',
					    			'</tr>',
			        				'<tpl for="retail">',
			        				'<tr>',
			        					'<td width="200">{COMPANY}</td>',
			        					'<td width="80">{AMOUNT}</td>',
			        					'<td width="140">{BANK_NAME}</td>',
			        					'<td width="80">{OPER_NAME}</td>',
			        					'<td>{BANK_NO}</td>',
			        				'</tr>',
			        				'</tpl>',
			        			'</table>',
				        	'</div>',
				        	'<div class="p-detail" style="width:686px;">',
				        		'<table>',
				        			'<tr>',
					    				'<td style="background:#f1f1f1;font-size:14px;" colspan="5">',
					    					'<i class="iconfont f18 money-color">&#xe662;</i> 平台',
					    				'</td>',
					    			'</tr>',
			        				'<tpl for="site">',
			        				'<tr>',
			        					'<td width="200">{COMPANY}</td>',
			        					'<td width="80">{AMOUNT}</td>',
			        					'<td width="140">{BANK_NAME}</td>',
			        					'<td width="80">{OPER_NAME}</td>',
			        					'<td>{BANK_NO}</td>',
			        				'</tr>',
			        				'</tpl>',
			        			'</table>',
				        	'</div>',
				        	'</tpl>',
				        	//'<tpl else>',
				        	
				        	//'</tpl>',
				        	{
				            	len:function(v){
				            		return v.length>0;
				            	}
				            }
				        ]
	   				}]
	   			}],
				buttons:[{
					text:'去打款',
					itemId:'goPayBtn',
					cls:'orange',
					handler:function(){
						var departIds = me.getCalcGrid().getSelectDepartIds().join(',');
						Ext.Ajax.request({
							url:cfg.getCtx()+'/finance/confirm/calc',
							params:{departIds:departIds,beginTime:beginTime,endTime:endTime,supplyId:supplyId,agencyId:agencyId,orderType:orderType},
							success:function(response, opts){
								var obj = Ext.decode(response.responseText),
									errors = ['对账查询异常','查询的条件缺失','请求超时'],
									state = obj?obj.statusCode:0;
								if(!obj.success){
									util.error(errors[0-parseInt(state)]);
								}else{
									util.redirectTo('finance/pay','?orderType='+orderType+'&beginTime='+beginTime+'&endTime='+endTime+'&supplyId='+supplyId+'&agencyId='+agencyId);
								}
							}
						});
					}
				},{
					text:'关闭',
					cls:'disable',
					handler:function(){win.close();}
				}]
		}).show();
	},
	cancelOrder :function(b){
		var me = this;
		Ext.Ajax.request({
			url:cfg.getCtx()+'/finance/del/syn',
			success:function(response, opts){
				var obj = Ext.decode(response.responseText),
					errors = ['解锁异常'],
					state = obj?obj.statusCode:0;
				var tbar = b.up('toolbar');
				if(obj.success){
					document.location.reload();
				}else{
					util.error(errors[0-parseInt(state)]);
				}
			}
		});
	},
	calcHistory :function(btn){
		var win = Ext.create('Ext.window.Window',{
			title:util.windowTitle(util.glyphToFont(btn.glyph),'查询历史对账',''),
			width:800,
			height:400,
			modal:true,
			draggable:false,
			resizable:false,
			maximizable:false,
			bodyPadding:'2',
   			layout:'border',
   			items:[{
   				itemId:'condit',
   				region:'west',
   				width:180,
   				bodyPadding:10,
   				buttons:[{
   					xtype:'button',
   					cls:'blue',
   					text:'查询',
   					handler:function(btn){
   						var cd = btn.up('panel#condit'),
   							dv = win.down('dataview'),
   							beginTime = cd.down('datefield#startxddt'),
   							endTime = cd.down('datefield#endxddt');
   						beginTime = beginTime.getValue()||'';
   						endTime = endTime.getValue()||'';
   						if(beginTime==''||endTime==''){
   							util.alert('请选择对账日期');
   							return;
   						}
   						var vs = {beginTime:Ext.Date.format(beginTime,'Y-m-d'),endTime:Ext.Date.format(endTime,'Y-m-d')};
						Ext.applyIf(vs,dv.getStore().getProxy().getExtraParams());
						dv.getStore().getProxy().setExtraParams(vs);
						dv.getStore().load();
   					}
   				}],
   				items:[/*{
   					bbar:['->',{
	   					xtype:'segmentedbutton',
	   					items:[{
	   						text:'上半月',
	   						pressed: true,
	   						handler:function(){
	   							var dv = win.down('dataview'),
	   								vs = {beginType:1};
	   							Ext.applyIf(vs,dv.getStore().getProxy().getExtraParams());
	   							dv.getStore().getProxy().setExtraParams(vs);
					       		dv.getStore().load();
	   						}
	   					},{
	   						text:'下半月',
	   						handler:function(){
	   							var dv = win.down('dataview'),
	   								vs = {beginType:2};
	   							Ext.applyIf(vs,dv.getStore().getProxy().getExtraParams());
	   							dv.getStore().getProxy().setExtraParams(vs);
					       		dv.getStore().load();
	   						}
	   					}]
	   				}],
	   				items:[{
	   					xtype:'monthpicker',
	   					showButtons:false,
	   					value:new Date(),
	   					listeners:{
	   						yearclick :function(mp, value, eOpts){
	   							var dv = win.down('dataview'),
	   								vs = {beginMonth:value[1]+'-'+(value[0]+1>9?(value[0]+1):'0'+(value[0]+1))};
	   							Ext.applyIf(vs,dv.getStore().getProxy().getExtraParams());
	   							dv.getStore().getProxy().setExtraParams(vs);
					       		dv.getStore().load();
	   						},
	   						monthclick :function(mp, value, eOpts){
	   							var dv = win.down('dataview'),
	   								vs = {beginMonth:value[1]+'-'+(value[0]+1>9?(value[0]+1):'0'+(value[0]+1))};
	   							Ext.applyIf(vs,dv.getStore().getProxy().getExtraParams());
	   							dv.getStore().getProxy().setExtraParams(vs);
					       		dv.getStore().load();
	   						}
	   					}
	   				}]
   				},*/{
   					width:155,
	    			xtype:'datefield',
	    			format:'Y-m-d',
	    			endDateField: 'endxddt',
			        itemId:'startxddt',
			        showToday:false,
			        vtype: 'daterange',
			        emptyText:'开始日期'
   				},{
   					emptyText:'截止日期',
	    			width:155,
	    			xtype:'datefield',
	    			format:'Y-m-d',
	    			itemId:'endxddt',
	    			showToday:false,
		            startDateField: 'startxddt',
		            vtype: 'daterange'
   				}]
   			},{
   				border:true,
   				region:'center',
   				bodyPadding:5,
   				autoScroll:true,
   				items:[{
   					xtype:'dataview',
	   				border:false,
	   				cls:'data-view',
			        trackOver:false,
			        itemSelector: '.data-view-item',
			        store :Ext.create('Ext.data.Store', {
			        	model:Ext.create('Ext.data.Model',{
			        		fields: [
			        			'COMPANY_NAME','ACCOUNT_STATUS'
			        		]
			        	}),
			        	autoLoad: true, 
			        	listeners:{
			        		beforeload:function(s){
	   							var vs = s.getProxy().getExtraParams();
	   							Ext.applyIf(vs,{beginMonth:Ext.Date.format( new Date(), 'Y-m' ),beginType:1});
	   							s.getProxy().setExtraParams(vs);
			        		}
			        	},
					    proxy: {
					        type: 'ajax',
					        noCache:true,
					        url: cfg.getCtx()+'/finance/history',
					        reader: {
					            type: 'json',
					            rootProperty: 'data'
					        }
					   	}
			        }),
			        tpl: [
			        	'<tpl for=".">',
			        	'<div class="data-view-item block">',
			        	'<div class="f16 blod ht30 blue-color">{COMPANY_NAME}</div>',
			        	'{ACCOUNT_STATUS:this.format()}',
			        	'</div>',
			        	'</tpl>',
				       	{
				        	format:function(value){
				        		var s = ['未对账','全部对完','部分对完'],
				        			cs = ['disable-color','green-color','orange-color'];
				        		return [
				        			'<div class="ht20 '+cs[parseInt(value)]+'">'+s[parseInt(value)]+'</div>'
				        		].join('');  
				        	}
				       	}]
   				}]
   			}]
		}).show();
	}
});