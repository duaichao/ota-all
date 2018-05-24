Ext.define('app.controller.order.traffic', {
	extend: 'app.controller.common.BaseController',
	views:['app.view.order.refundGrid'],
	config: {
		control: {
			'button[itemId=cancelOrder]':{
				click:'cancelOrder'
			},
			'button[itemId=editOrder]':{
				click:function(){
					var me=this,sel,grid=this.getOrderGrid();
					if(sel = util.getSingleModel(grid)){
						document.location.href=cfg.getCtx()+"/produce/traffic/buy?orderId="+sel.get('ID')+'&ruleId='+sel.get('RULE_ID')+'&queryDate='+(sel.get('START_DATE').replace(/-/g,''))+'&trafficId='+sel.get('TRAFFIC_ID');
					}
				}
			},
			'button[itemId=payment]':{
				click:function(){
					var me=this,sel,grid=this.getOrderGrid();
					if(sel = util.getSingleModel(grid)){
						document.location.href=cfg.getCtx()+"/produce/paycenter?orderId="+sel.get('ID')+'&type=1';
					}
				}
			},
			'button[itemId=printVisitor]':{
				click:function(btn){
					var me=this,win;
					win = util.createEmptyWindow('打印名单',util.glyphToFont(btn.glyph),500,240,[{
						fieldLabel:'产品名称',
						allowBlank:false,
						xtype:'combo',
						anchor:'100%',
						minChars: 1,
					    queryParam: 'query',
					    displayField: 'TITLE',
					    pageSize:50,
					    focusOnToFront:true,
					    forceSelection:true,
					    queryMode: 'remote',
					    triggerAction: 'all',
					    valueField: 'ID',
					    name:'trafficId',
					    hiddenName:'trafficId',
					    emptyText:'',
					    store:Ext.create('Ext.data.Store',{
					    	autoLoad:true,
						    proxy: {
						        type: 'ajax',
						        noCache:true,
						        model:Ext.create('Ext.data.Model',{
						        	fields: ['ID','TITLE']
						        }),
						        url: cfg.getCtx()+'/resource/traffic/list',
						        reader: {
						            type: 'json',
						            rootProperty: 'data',
						            totalProperty:'totalSize'
						        }
						   	}
					    }),
					    listConfig:{
					    	minWidth:360,
					    	itemTpl:[
								 '<tpl for=".">',
						            '<li class="city-item">{TITLE}</li>',
						        '</tpl>'
							]
					    }
					},{
						fieldLabel:'出发日期',
						name:'startDate',
						allowBlank:false,
						showToday:false,
						xtype:'datefield',
						format:'Y-m-d'
					}],[{
						text:'打印',
						handler:function(){
							var form = win.down('form'),
								f = form.getForm();
							if(f.isValid()){
								var wind = window.open();
								f.submit({
									submitEmptyText:false ,
									url:cfg.getCtx()+'/order/traffic/trafficVisitors',
					                success: function(form, action) {
					                   var d = action.result,
					                   	   tpl = cfg.getTpls().visitor.batch,
					                   	   html = tpl.apply(d);
					        			var hm = util.beforePrint('打印名单',html);
					        			util.afterPrint(wind,hm);
					                },
					                failure: function(form, action) {
					                	var state = action.result?action.result.statusCode:0,
					                		errors = ['查询结果异常','当前查询还没有订单'];
					                    util.error(errors[0-parseInt(state)]);
					                }
								});
							}
						}
					}]).show();
				}
			},
			'button[itemId=refund]':{
				click:function(btn){
					var me=this,sel,grid=this.getOrderGrid(),win;
					if(sel = util.getSingleModel(grid)){
						win = util.createEmptyWindow('申请退款',util.glyphToFont(btn.glyph),450,265,[{
							bodyStyle:'background:transparent',
							xtype:'panel',
							margin:'0 0 10 0',
							html:[
				    			'<ol class="ui-step ui-step-3">',
				    				'<li class="ui-step-start ui-step-active">',
								        '<div class="ui-step-line">-</div>',
								        '<div class="ui-step-icon">',
								            '<i class="iconfont">&#xe69f;</i>',
								            '<i class="ui-step-number">1</i>',
								            '<span class="ui-step-text">申请退款</span>',
								        '</div>',
								    '</li>',
								    '<li class="">',
								        '<div class="ui-step-line">-</div>',
								        '<div class="ui-step-icon">',
								            '<i class="iconfont">&#xe69f;</i>',
								            '<i class="ui-step-number">2</i>',
								            '<span class="ui-step-text">退款审核</span>',
								        '</div>',
								    '</li>',
								    '<li class="ui-step-end">',
								        '<div class="ui-step-line">-</div>',
								        '<div class="ui-step-icon">',
								            '<i class="iconfont">&#xe69f;</i>',
								            '<i class="iconfont ui-step-number">&#xe6a0;</i>',
								            '<span class="ui-step-text">完成</span>',
								        '</div>',
								    '</li>',
				    			'</ol>'
				    		].join('')
						},{
							xtype:'hidden',
							name:'pm[ORDER_ID]',
							value:sel.get('ID')
						},{
							xtype:'hidden',
							name:'pm[TYPE]',
							value:1
						},{
					    	name:'pm[REMARK]',
				    		emptyText:'输入退款原因，已付款的订单供应商已出票，平台不保证全额退款，请慎重操作！如果长时间没有完成，请联系产品供应商',
				    		xtype:'textarea',
				    		anchor:'100%',
				    		height:100,
				    		cls:'text-cls',
					    	allowBlank: false
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
						            	url:cfg.getCtx()+'/order/traffic/refund',
						                success: function(form, action) {
						                   util.success('您的申请已提交成功，等待供应商审核');
						                   grid.getStore().load();
						                   win.close();
						                },
						                failure: function(form, action) {
						                	var state = action.result?action.result.statusCode:0,
						                		errors = ['您的申请已提交异常'];
						                    util.error(errors[0-parseInt(state)]);
						                    win.close();
						                }
						            });
						        }
					        }
					    }]).show();
					}
				}
			},
			'button[itemId=auditRefund]':{
				click:function(btn){
					var me=this,sel,grid=this.getOrderGrid(),win;
					if(sel = util.getSingleModel(grid)){
						win = util.createEmptyWindow('退款审核',util.glyphToFont(btn.glyph),800,400,[{
							bodyStyle:'background:transparent',
							xtype:'panel',
							margin:'0 0 10 0',
							html:[
				    			'<ol class="ui-step ui-step-3">',
				    				'<li class="ui-step-start ui-step-done">',
								        '<div class="ui-step-line">-</div>',
								        '<div class="ui-step-icon">',
								            '<i class="iconfont">&#xe69f;</i>',
								            '<i class="ui-step-number">1</i>',
								            '<span class="ui-step-text">申请退款</span>',
								        '</div>',
								    '</li>',
								    '<li class="ui-step-active">',
								        '<div class="ui-step-line">-</div>',
								        '<div class="ui-step-icon">',
								            '<i class="iconfont">&#xe69f;</i>',
								            '<i class="ui-step-number">2</i>',
								            '<span class="ui-step-text">退款审核</span>',
								        '</div>',
								    '</li>',
								    '<li class="ui-step-end">',
								        '<div class="ui-step-line">-</div>',
								        '<div class="ui-step-icon">',
								            '<i class="iconfont">&#xe69f;</i>',
								            '<i class="iconfont ui-step-number">&#xe6a0;</i>',
								            '<span class="ui-step-text">完成</span>',
								        '</div>',
								    '</li>',
				    			'</ol>'
				    		].join('')
						},{
							xtype:'hidden',
							name:'pm[ORDER_ID]',
							value:sel.get('ID')
						},{
							xtype:'hidden',
							name:'pm[TYPE]',
							value:2
						},{
							xtype:'refundgrid',
							orderId:sel.get('ID')
						}],['<span class="yellow-color"><i class="iconfont f18">&#xe6ae;</i> 如不是全额退款，牵扯出票等损失费用，请填写备注！</span>','->',{
					        text: '确定',
					        disabled: true,
					        formBind: true,
					        handler:function(btn){
					        	var f = btn.up('form'),
					        		form = f.getForm(),
					        		win = f.up('window'),
					        		rgds = win.down('refundgrid').getStore(),
					        		rgdd = rgds.getData(),
					        		rgdc = rgds.getCount();
					        	var bills=[];
								for (var i = 0; i < rgdc; i++) {
									bills.push(rgdd.items[i].data);
								}
					        	if (form.isValid()) {
					        		btn.disable();
						            form.submit({
						            	params:{
						            		bills:Ext.encode(bills)
						            	},
						            	submitEmptyText:false ,
						            	url:cfg.getCtx()+'/order/traffic/refund',
						                success: function(form, action) {
						                   util.success('退款申请受理成功');
						                   grid.getStore().load();
						                   win.close();
						                },
						                failure: function(form, action) {
						                	var state = action.result?action.result.statusCode:0,
						                		errors = ['退款申请受理异常'];
						                    util.error(errors[0-parseInt(state)]);
						                    win.close();
						                }
						            });
						        }
					        }
					    }]).show();
					}
				}
			},
			'button[itemId=finishRefund]':{
				click:function(btn){
					var me=this,sel,grid=this.getOrderGrid(),win;
					if(sel = util.getSingleModel(grid)){
						win = util.createEmptyWindow('退款完成',util.glyphToFont(btn.glyph),800,400,[{
							bodyStyle:'background:transparent',
							xtype:'panel',
							margin:'0 0 10 0',
							html:[
				    			'<ol class="ui-step ui-step-3">',
				    				'<li class="ui-step-start ui-step-done">',
								        '<div class="ui-step-line">-</div>',
								        '<div class="ui-step-icon">',
								            '<i class="iconfont">&#xe69f;</i>',
								            '<i class="ui-step-number">1</i>',
								            '<span class="ui-step-text">申请退款</span>',
								        '</div>',
								    '</li>',
								    '<li class="ui-step-done">',
								        '<div class="ui-step-line">-</div>',
								        '<div class="ui-step-icon">',
								            '<i class="iconfont">&#xe69f;</i>',
								            '<i class="ui-step-number">2</i>',
								            '<span class="ui-step-text">退款审核</span>',
								        '</div>',
								    '</li>',
								    '<li class="ui-step-active ui-step-end">',
								        '<div class="ui-step-line">-</div>',
								        '<div class="ui-step-icon">',
								            '<i class="iconfont">&#xe69f;</i>',
								            '<i class="iconfont ui-step-number">&#xe6a0;</i>',
								            '<span class="ui-step-text">完成</span>',
								        '</div>',
								    '</li>',
				    			'</ol>'
				    		].join('')
						},{
							xtype:'hidden',
							name:'pm[ORDER_ID]',
							value:sel.get('ID')
						},{
							xtype:'hidden',
							name:'pm[TYPE]',
							value:3
						},{
							xtype:'refundgrid',
							orderId:sel.get('ID')
						}],['<span class="yellow-color"><i class="iconfont f18">&#xe6ae;</i> 如不是全额退款，牵扯出票等损失费用，请填写备注！</span>','->',{
					        text: '确定',
					        disabled: true,
					        formBind: true,
					        handler:function(btn){
					        	var f = btn.up('form'),
					        		form = f.getForm(),
					        		win = f.up('window'),
					        		rgds = win.down('refundgrid').getStore(),
					        		rgdd = rgds.getData(),
					        		rgdc = rgds.getCount();
					        	var bills=[];
								for (var i = 0; i < rgdc; i++) {
									bills.push(rgdd.items[i].data);
								}
					        	if (form.isValid()) {
					        		btn.disable();
						            form.submit({
						            	params:{
						            		bills:Ext.encode(bills)
						            	},
						            	submitEmptyText:false ,
						            	url:cfg.getCtx()+'/order/traffic/refund',
						                success: function(form, action) {
						                   util.success('退款完成');
						                   grid.getStore().load();
						                   win.close();
						                },
						                failure: function(form, action) {
						                	var state = action.result?action.result.statusCode:0,
						                		errors = ['退款异常'];
						                    util.error(errors[0-parseInt(state)]);
						                    win.close();
						                }
						            });
						        }
					        }
					    }]).show();
					}
				}
			},
			'basegrid':{
				selectionchange: function(view, records) {
					var btn = this.getCancelBtn(),
						payBtn = this.getPayBtn(),
						wmtjBtn = this.getWmtjBtn(),
						thtjBtn = this.getThtjBtn(),
						yxftjBtn = this.getYxftjBtn(),
						finishRefundBtn = this.getFinishRefundBtn(),
						auditRefundBtn = this.getAuditRefundBtn(),
						refundBtn = this.getRefundBtn(),
						editBtn = this.getEditBtn(),
						combo = this.getComboQuery()
						;
					var g1 = [btn,payBtn,editBtn],
						g2 = [wmtjBtn,thtjBtn,yxftjBtn,refundBtn],
						g3=[auditRefundBtn],
						g4=[finishRefundBtn],
						status = records[0].get('STATUS'),
						dzStatus = records[0].get('ACCOUNT_STATUS');
					this.btnToggle(g1,status!='0');
					this.btnToggle(g2,status!='2');
					this.btnToggle(g3,status!='3');
					this.btnToggle(g4,status!='4');
					
					//如果是对账状态 不能申请退款
					this.btnToggle([refundBtn],dzStatus!=0);
					
					if(combo){
					if(combo.getValue()==1){
						this.btnToggle(g3,true);
					}else{
						this.btnToggle([refundBtn],true);
					}
					}
					
	            },
	            cellclick:function( view, td, cellIndex, record, tr, rowIndex, e, eOpts ){
					var rec = record,getUrl='';
					var grid = this.getOrderGrid();
					if(cellIndex==2){
						if(e.target.tagName=='A'&&e.target.className.indexOf('xq')!=-1){
							Ext.factory({
								orderRecord:rec
							},'app.view.order.detail').show();
						}
						if(e.target.tagName=='A'&&e.target.className.indexOf('dd')!=-1){
							var win = Ext.create('Ext.window.Window',{
								title:util.windowTitle('&#xe66a;','掉单处理',''),
								width:360,
								height:150,
								modal:true,
								draggable:false,
								resizable:false,
								maximizable:false,
					   			layout:'fit',
					   			html:[
					   				'<div style="padding:20px;font-size:18px;color:#f40;">',
					   				'<i class="iconfont f24 orange-color">&#xe700;</i> ',
					   				'系统超时或异常，订单已掉单',
					   				'</div>'
					   			].join(''),
								buttons:[{
									text:'手动处理',
									handler:function(){
										Ext.Ajax.request({
											url:cfg.getCtx()+'/order/traffic/edit/lost?orderId='+rec.get('ID'),
											success:function(response, opts){
												win.close();
												var obj = Ext.decode(response.responseText),
													errors = ['手动处理掉单失败'],
													state = obj?obj.statusCode:0;
												if(!obj.success){
													util.error(errors[0-parseInt(state)]);
												}else{
													util.success('手动处理掉单成功！');
													grid.getStore().load();
												}
												
												
											}
										});
									}
								},{
									text:'关闭',
									cls:'disable',
									handler:function(){
										win.close();
									}
								}]
							}).show();
						}
					}
					if(cellIndex==5){
						Ext.factory({
							orderRecord:rec
						},'app.view.order.visitor').show();
					}
					if(cellIndex==6){
						Ext.factory({
							orderRecord:rec
						},'app.view.order.recharge').show();
					}
					if(cellIndex==7||cellIndex==9){
						Ext.factory({
							orderRecord:rec
						},'app.view.order.status').show();
					}
				}
			}
		},
		refs: {
			cancelBtn:'button[itemId=cancelOrder]',
			editBtn:'button[itemId=editOrder]',
			finishRefundBtn:'button[itemId=finishRefund]',
			auditRefundBtn:'button[itemId=auditRefund]',
			refundBtn:'button[itemId=refund]',
			payBtn:'button[itemId=payment]',
			wmtjBtn:'button[itemId=adjustment]',
			thtjBtn:'button[itemId=interAdjustment]',
			yxftjBtn:'button[itemId=otherAdjustment]',
			orderGrid: 'basegrid',
			comboQuery:'combo[itemId=orderTrade]',
        }
	},
	btnToggle:function(btns,v){
		for(var i=0;i<btns.length;i++){
			if(btns[i]){
				btns[i].setDisabled(v);
			}
		}
	},
	onBaseGridRender:function(g){
		var me = this;
		//外部跳转 动态传参数
		if(dynamicParamsOrderTrade!=''){
			g.getStore().getProxy().setExtraParams({orderTrade:dynamicParamsOrderTrade,orderStatus:dynamicParamsOrderStatus});
	        g.getStore().load();
		}
		setTimeout(function(){
		Ext.Ajax.request({
		    url: util.getPowerUrl(),
		    success: function(response, opts) {
		        var obj = Ext.decode(response.responseText),
		        	items = obj.children,
		        	ubtn = [];
		        for(var i=0;i<items.length;i++){
		        	ubtn.push(items[i]);
		        }
		        ubtn.push('->');
		        //是否显示 买入卖出 只有供应商 USER_TPE 02 开始
		        if(cfg.getUser().userType.indexOf('02')==0){
			        ubtn.push({
			        	itemId:'orderTrade',
			        	xtype:'combo',
						forceSelection: true,
			            valueField: 'value',
			            editable:false,
			            displayField: 'text',
			            store:util.createComboStore([{
			            	text:'买入',
			            	value:1
			            },{
			            	text:'卖出',
			            	value:2
			            }]),
			            width:60,
			            minChars: 0,
			            value:dynamicParamsOrderTrade||'1',
			            queryMode: 'local',
			            typeAhead: true,
			            listeners:{
			            	change:function(c, newValue, oldValue, eOpts ){
						        g.getStore().getProxy().setExtraParams({orderTrade:newValue});
						        g.getStore().load();
			            	}
			            }
			        });
		        }
		        ubtn.push({
		        	itemId:'accountType',
		        	xtype:'combo',
					forceSelection: true,
					editable:false,
		            valueField: 'value',
		            displayField: 'text',
		            store:util.createComboStore([{
		            	text:'全部',
		            	value:''
		            },{
		            	text:'对账中',
		            	value:1
		            },{
		            	text:'已对账',
		            	value:2
		            },{
		            	text:'未对账',
		            	value:0
		            }]),
		            width:75,
		            minChars: 0,
		            value:'',
		            queryMode: 'local',
		            typeAhead: true,
		            listeners:{
		            	change:function(c, newValue, oldValue, eOpts ){
					       g.getStore().getProxy().setExtraParams({ACCOUNT_STATUS:newValue});
					       g.getStore().load();
		            	}
		            }
		        });
		        ubtn.push({
		        	emptyText:'产品名/订单号',
		        	xtype:'searchfield',
		        	store:g.getStore()
		        });
		        ubtn.push({
	        		text:'筛选',
		        	glyph:'xe66f@iconfont',
		        	tooltip:'更多筛选',
		        	itemId:'moreQuery'
		        });
		        /*
		        ubtn.push({
		        	text:'<i class="iconfont" data-qtip="打印">&#xe69a;</i>',
		        	itemId:'printResult'
		        });*/
		        util.createGridTbar(g,ubtn);
		    }
		});
		},500);
	},
	cancelOrder:function(btn){
		var me=this,win,sel,grid=this.getOrderGrid();
		if(sel = util.getSingleModel(grid)){
			win = util.createEmptyWindow('取消订单',util.glyphToFont(btn.glyph),450,205,[{
				    	name:'pm[REMARK]',
			    		emptyText:'输入取消订单原因',
			    		xtype:'textarea',
			    		anchor:'100%',
			    		height:100,
			    		cls:'text-cls',
				    	allowBlank: false
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
			            	url:cfg.getCtx()+'/order/traffic/cancelOrder?orderId='+sel.get('ID'),
			                success: function(form, action) {
			                   util.success('订单取消成功');
			                   grid.getStore().load();
			                   win.close();
			                },
			                failure: function(form, action) {
			                	var state = action.result?action.result.statusCode:0,
			                		errors = ['订单取消异常'];
			                    util.error(errors[0-parseInt(state)]);
			                    win.close();
			                }
			            });
			        }
		        }
		    }]).show();
		}
	}
});