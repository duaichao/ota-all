Ext.define('app.controller.order.route', {
	extend: 'app.controller.common.BaseController',
	views:['app.view.order.refundGrid'],
	config: {
		control: {
			'button[itemId=cancelOrder]':{
				click:'cancelOrder'
			},
			'button[itemId=delOrder]':{
				click:'delOrder'
			},
			'button[itemId=editOrder]':{
				click:function(){
					var me=this,sel,grid=this.getOrderGrid();
					if(sel = util.getSingleModel(grid)){
						document.location.href=cfg.getCtx()+"/produce/route/buy?&routeId="+sel.get('PRODUCE_ID')+"&orderId="+sel.get('ID')+'&planId='+sel.get('TRAFFIC_ID')+'&planName='+sel.get('PLAN_NAME')+'&goDate='+(sel.get('START_DATE').replace(/-/g,''));
					}
				}
			},
			'button[itemId=payment]':{
				click:function(){
					var me=this,sel,grid=this.getOrderGrid();
					if(sel = util.getSingleModel(grid)){
						if(sel.get('EARNEST_TYPE')=='1'){
							util.alert('代收款的定金预付订单需要供应商确认');
							return;
						}
						document.location.href=cfg.getCtx()+"/produce/paycenter?orderId="+sel.get('ID')+'&type='+sel.get('PRODUCE_TYPE');
					}
				}
			},
			'button#visitorManager menuitem':{
				click:function(mi){
					var me = this;
					if(mi.itemId=='visitorContact'){
						me.visitorContact.call(me,mi);
					}
					if(mi.itemId=='visitorEdit'){
						me.editVisitor.call(me,mi);
					}
				}
			},
			'button[itemId=visitorCon] menuitem':{
				click:function(mi){
					var me = this;
					if(mi.itemId=='contractXz'){
						me.visitorConAdd.call(me,['1']);
					}
					if(mi.itemId=='contractCz'){
						me.visitorConAdd.call(me,['2']);
					}
					if(mi.itemId=='contractCx'){
						me.visitorConWin.call(me);
					}
				}
			},
			'button[itemId=supplyCon] menuitem':{
				click:function(mi){
					var me = this;
					if(mi.itemId=='supplyContractSc'){
						me.uploadConAttr.call(me);
					}
					if(mi.itemId=='supplyContractXz'){
						me.downloadConAttr.call(me);
					}
					if(mi.itemId=='supplyContractQr'){
						me.confirmConAttr.call(me);
					}
				}
			},
			'button[itemId=saleFloat]':{
				click:'saleFloat'
			},
			'button[itemId=interFloat]':{
				click:'interFloat'
			},
			'button[itemId=auditInterFloat]':{
				click:'auditInterFloat'
			},
			'button[itemId=printVisitor]':{
				click:function(btn){
					var me=this,win;
					win = util.createEmptyWindow('打印名单',util.glyphToFont(btn.glyph),500,240,[{
						fieldLabel:'产品名称',
						allowBlank:false,
						xtype:'combo',
						margin:'0 10 10 0',
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
						            '<li  style="white-space:normal;padding:5px;">{TITLE}</li>',
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
						if(sel.get('IS_REFUND')!='1'){
							util.alert('订单参加的活动不允许退款');
							return;
						}
						
						
						win = util.createEmptyWindow('申请退款',util.glyphToFont(btn.glyph),550,350,[{
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
						},{
							xtype:'container',
							html:'<span class="yellow-color"><i class="iconfont f20">&#xe6ae;</i> 参加活动的订单，只退还实际交易金额！</span>',
							cls:'low'
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
					var me=this,sel,grid=this.getOrderGrid(),win,discountMsg='';
					
					if(sel = util.getSingleModel(grid)){
						
						if(sel.get('DISCOUNT_TITLE')){
							discountMsg='<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;参加活动'+sel.get('DISCOUNT_TITLE')+'，可退款总额  <= '+(parseFloat(sel.get('INTER_AMOUNT'))-parseFloat(sel.get('DISCOUNT_TOTAL_AMOUNT')))+'元';
						}
						
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
							record:sel,
							orderId:sel.get('ID')
						}],['<span class="red-color"><i class="iconfont f18">&#xe6ae;</i> 如不是全额退款，牵扯出票等损失费用，请填写备注！'+discountMsg+'</span>','->',{
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
						                		errors = ['退款申请受理异常','','退款金额超出，是否已减优惠'];
						                    util.error(errors[0-parseInt(state)]);
						                    btn.enable();
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
					var me=this,sel,grid=this.getOrderGrid(),win,discountMsg='';
					if(sel = util.getSingleModel(grid)){

						if(sel.get('DISCOUNT_TITLE')){
							discountMsg='<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;参加活动'+sel.get('DISCOUNT_TITLE')+'，可退款总额  <= '+(parseFloat(sel.get('INTER_AMOUNT'))-parseFloat(sel.get('DISCOUNT_TOTAL_AMOUNT')))+'元';
						}
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
							xtype:'hidden',
							name:'pm[REFUND_TYPE]',//判别是交通还是线路
							value:2
						},{
							xtype:'refundgrid',
							orderId:sel.get('ID')
						}],['<span class="red-color"><i class="iconfont f18">&#xe6ae;</i> 如不是全额退款，牵扯出票等损失费用，请填写备注！'+discountMsg+'</span>','->',{
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
						                		errors = ['退款异常','','退款金额超出，是否已减优惠'];
						                    util.error(errors[0-parseInt(state)]);
						                    btn.enable();
						                }
						            });
						        }
					        }
					    }]).show();
					}
				}
			},
			'button#otherFeeAudit':{
				click:function(btn){
					var me=this,sel,grid=this.getOrderGrid(),win;
					if(sel = util.getSingleModel(grid)){
						win = Ext.create('Ext.window.Window',{
							title:util.windowTitle(util.glyphToFont(btn.glyph),'附加费审核',''),
							width:500,
							height:240,
							modal:true,
							draggable:false,
							resizable:false,
							maximizable:false,
				   			layout:'fit',
							items:[{
								xtype:'form',
								autoScroll:true,
								fieldDefaults: {
							        labelAlign: 'right',
							        labelWidth: 60,
							        msgTarget: 'qtip'
							    },
								bodyPadding: 5,
								items:[{
									xtype:'container',
									itemId:'otherLogs',
									style:'padding:5px 5px 10px 5px;',
									tpl:[
									     '<div style="line-height:20px;">',
										     '<tpl for=".">',
										     '<li><b>{TITLE}：',
										     '</b>{[this.cv(values.PRICE)]}，共 <span class="f18">{NUM}</span> 人',
										     ' <i data-qtip="{CONTENT}" class="iconfont f18" style="cursor:pointer;color:#4CAF50;">&#xe79d;</i>',
										     '</li>',
										     '</tpl>',
									     '</div>',
									     {
									    	 cv:function(price){
									    		 return util.moneyFormat(price,'f18');
									    	 }
									     }
									]
								},{
									xtype:'hidden',
									name:'pm[ORDER_ID]',
									value:sel.get('ID')
								},{
							    	name:'pm[OTHER_SUPPLY_CONTENT]',
						    		emptyText:'输入附加费审核备注，最多200个字',
						    		xtype:'textarea',
						    		anchor:'100%',
						    		height:70,
						    		maxLength:200,
						    		cls:'text-cls',
						    		listeners:{
										validitychange:function(nf,is){
											if(!is){
												var v = nf.getValue();
												nf.setValue(v.substring(0,200));
											}
										}
									},
							    	allowBlank: false
								}],
								buttons:['其他附加费：'+util.moneyFormat(sel.get('OTHER_AMOUNT'),'f20'),'->',{
							        text: '同意',
							        disabled: true,
							        formBind: true,
							        handler:function(btn){
							        	me.auditOtherFee(btn,grid);
							        }
							    },{
							    	text:'不同意',
							    	cls:'red',
							    	disabled: true,
							        formBind: true,
							    	handler:function(btn){
							    		me.auditOtherFee(btn,grid);
							    	}
							    },{
							    	text:'取消',
							    	cls:'disable',
							    	handler:function(btn){
							        	win.close();
							        }
							    }]
							}]
						}).show();
						//加载附加费详情
						Ext.Ajax.request({
							url:cfg.getCtx()+'/order/route/other/fee/logs?orderId='+sel.get('ID'),
							success:function(response, opts){
								var obj = Ext.decode(response.responseText),
									data = obj.data;
								win.down('container#otherLogs').setData(data);
							}
						});
					}
				}
			},
			'button#startConfirm':{
				click:function(btn){
					var me=this,sel,grid=this.getOrderGrid(),win;
					if(sel = util.getSingleModel(grid)){
						if(sel.get('START_CONFIRM')=='1'){
							util.alert('已确认出团的数据不能重复确认');
							return;
						}
						var models = [sel.data];
						win = Ext.create('Ext.window.Window',{
							title: util.windowTitle(util.glyphToFont(btn.glyph),'出团确认',''),
							bodyPadding:5,
							width:320,
							height:180,
							modal:true,
							draggable:false,
							resizable:false,
							maximizable:false,
				   			layout:'fit',
				   			items:[{
				   				html:'<div style="padding:10px;line-height:20px;font-size:14px;">请仔细检查核对信息，确认无误后点击“确认”</div>'
				   			}],
				   			buttons:[{
				   				text:'确认',
				   				handler:function(){
				   					Ext.Ajax.request({
										url:cfg.getCtx()+'/order/route/start/confirm',
										params:{models:Ext.encode(models)},
										success:function(){
											util.success("数据操作成功!");
											win.close();
											grid.getStore().load();
										},
										failure:function(){
											util.error("数据操作失败！");
										}
									});
				   				}
				   			}]
						}).show();
					}
				}
			},
			'button#earnestAudit':{
				click:'earnestAudit'
			},
			'basegrid':{
				selectionchange: function(view, records) {
					var	yesBtnIds = [],
						record = records[0],
						orderStatus = parseInt(record.get('STATUS')),
						accountStatus = parseInt(record.get('ACCOUNT_STATUS')),
						otherFeeStatus = parseInt(record.get('OTHER_SUPPLY_AUDIT')),
						auditFloatStatus = parseInt(record.get('AUDIT_INTER_FLOAT'));
					//初始化
					this.btnToggle([],true);
					//待付款
					if(orderStatus==0){
						//其他费用待审核
						if(otherFeeStatus==1){
							yesBtnIds = ['otherFeeAudit'];
						}else if(otherFeeStatus==3){
							yesBtnIds = ['editOrder','cancelOrder','saleFloat','interFloat','auditInterFloat'];
						}else{
							yesBtnIds = ['payment','editOrder','cancelOrder','saleFloat','interFloat','auditInterFloat','earnestAudit'];
						}
						//同行议价待审核
						if(auditFloatStatus==1){
							Ext.Array.remove(yesBtnIds,'interFloat');
						}
						//定金
						if(record.get('IS_EARNEST')=='1'){
							yesBtnIds = ['refund'];
							//支付尾款
							if(record.get('EARNEST_TYPE')=='0'){
								yesBtnIds = ['payment'];
							}
							//余款确认
							if(record.get('EARNEST_TYPE')=='1'){
								yesBtnIds = ['earnestAudit'];
							}
						}
						if(record.get('IS_EARNEST')=='2'){
							yesBtnIds = ['refund'];
		        		}
						if(record.get('IS_EARNEST')=='4'){
							yesBtnIds = ['cancelOrder','delOrder'];
						}
						
						if(record.get('IS_EARNEST')=='5'){
							yesBtnIds = ['refund'];
						}
						if(record.get('IS_EARNEST')=='6'){
							yesBtnIds = ['auditRefund'];
						}
						if(record.get('IS_EARNEST')=='7'){
							yesBtnIds = ['finishRefund'];
						}
						if(record.get('IS_EARNEST')=='8'){
							yesBtnIds = [];
						}
					}else if(orderStatus==1){//付款中
						yesBtnIds = ['cancelOrder'];
					}else if(orderStatus==2){//已付款
						//如果是对账状态且是已付款 不能申请退款
						if(accountStatus==0){
							yesBtnIds = ['refund'];
						}
						if(record.get('IS_EARNEST')=='5'){
							yesBtnIds = ['refund'];
						}
						if(record.get('IS_EARNEST')=='6'){
							yesBtnIds = ['auditRefund'];
						}
						if(record.get('IS_EARNEST')=='7'){
							yesBtnIds = ['finishRefund'];
						}
						if(record.get('IS_EARNEST')=='8'){
							yesBtnIds = [];
						}
						yesBtnIds.push('startConfirm');
					}else if(orderStatus==3){//待退款
						yesBtnIds = ['auditRefund','startConfirm'];
					}else if(orderStatus==4){//退款中
						yesBtnIds = ['finishRefund','startConfirm'];
					}else if(orderStatus==5){//已退款
						yesBtnIds = ['startConfirm'];
					}else if(orderStatus==6){//手动取消
						yesBtnIds = ['delOrder'];
					}else if(orderStatus==7){//自动取消
						yesBtnIds = ['delOrder'];
					}
					this.btnToggle(yesBtnIds,false);
	            },
	            cellclick:function( view, td, cellIndex, record, tr, rowIndex, e, eOpts ){
					var rec = record,getUrl='';
					if(cellIndex==2){
						if(e.target.tagName=='A'){
							Ext.factory({
								orderRecord:rec
							},'app.view.order.detail').show();
						}
					}
					if(cellIndex==3){
						if(e.target.tagName=='A'&&e.target.className.indexOf('title')!=-1){
							Ext.require('app.view.resource.route.pub.detail',function(){
								var win = Ext.create('Ext.window.Window',{
									title:util.windowTitle('&#xe635;','线路详情',''),
									width:'90%',
									height:'85%',
									modal:true,
									draggable:false,
									resizable:false,
									maximizable:false,
						   			layout:'fit',
						   			items:[{
						   				xtype:'routedetail'
						   			}]
								});
								win.show();
								var rd = win.down('routedetail');
								rd.setRouteId(rec.get('PRODUCE_ID'));
							});
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
					if(cellIndex==8||cellIndex==9){
						Ext.factory({
							orderRecord:rec
						},'app.view.order.status').show();
					}
				}
			}
		},
		refs: {
			orderGrid: 'basegrid',
			orderTbar:'basegrid toolbar[dock=top]',
			comboQuery:'combo[itemId=orderTrade]',
        }
	},
	btnToggle:function(btnIds,disabled){
		var tbar = this.getOrderTbar(),
			btns = tbar.query('button'),
			initNoBtnIds = ['visitorManager','printVisitor','visitorCon','supplyCon','moreQuery'];
		Ext.each(btns,function(btn,index){
			if(Ext.Array.contains(initNoBtnIds,btn.getItemId())){
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
	onBaseGridRender:function(g){
		var me = this;
		//外部跳转 动态传参数
		if(dynamicParamsOrderStatus!=''){
			g.getStore().getProxy().setExtraParams({orderStatus:dynamicParamsOrderStatus});
	        g.getStore().load();
		}
		setTimeout(function(){
		Ext.Ajax.request({
		    url: util.getPowerUrl(),
		    success: function(response, opts) {
		        var obj = Ext.decode(response.responseText),
		        	items = obj.children,
		        	ubtn = [];
		        var cmenu = [],smenu = [],vmenu=[];
		        for(var i=0;i<items.length;i++){
		        	delete items[i].checked;
		        	if(Ext.String.startsWith( items[i].itemId, 'contract')){
		        		cmenu.push(items[i]);
		        	}else if (Ext.String.startsWith( items[i].itemId, 'supply')){
		        		smenu.push(items[i]);
		        	}else if (Ext.String.startsWith( items[i].itemId, 'visitor')){
		        		vmenu.push(items[i]);
		        	}else{
		        		ubtn.push(items[i]);
		        	}
		        	
		        }
	        	if(vmenu.length>0){
		        	ubtn.push({
		        		glyph:'xe69c@iconfont',
		        		itemId:'visitorManager',
		        		text:'游客管理',
		        		menu:vmenu
		        	});
		        }
		        if(cmenu.length>0){
		        	ubtn.push({
		        		glyph:'xe634@iconfont',
		        		itemId:'visitorCon',
		        		text:'游客合同',
		        		menu:cmenu
		        	});
		        }
		        if(smenu.length>0){
		        	ubtn.push({
		        		glyph:'xe634@iconfont',
		        		itemId:'supplyCon',
		        		text:'供应商合同',
		        		menu:smenu
		        	});
		        }
		        ubtn.push('->');
		        //是否显示 买入卖出 只有供应商 USER_TPE 02 开始
		        /*if(cfg.getUser().userType.indexOf('02')==0){
			        ubtn.push({
			        	itemId:'orderTrade',
			        	xtype:'combo',
			        	editable:false,
						forceSelection: true,
			            valueField: 'value',
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
			            value:'1',
			            queryMode: 'local',
			            typeAhead: true,
			            listeners:{
			            	change:function(c, newValue, oldValue, eOpts ){
						       g.getStore().getProxy().setExtraParams({orderTrade:newValue});
						        g.getStore().load();
			            	}
			            }
			        });
		        }*/
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
		        var bbar = g.getDockedItems()[g.getDockedItems().length-1];
		        bbar.add('-');
		        bbar.add({
		        	xtype:'checkbox',
		        	boxLabel:'<span class="orange-color">查看取消订单</span>',
		        	inputValue:'1',
		        	cls:'bbarck',
		        	listeners:{
		        		change:function(c,nv,ov){
		        			//nv = nv?'1':'0';
		        			g.getStore().getProxy().setExtraParams({IS_CANCEL:nv});
		        			g.getStore().load();
		        		}
		        	}
		        });
		    }
		});
		},500);
	},
	cancelOrder:function(btn){
		var me=this,win,sel,grid=this.getOrderGrid();
		if(sel = util.getSingleModel(grid)){
			win = util.createEmptyWindow('取消订单',util.glyphToFont(btn.glyph),450,250,[{
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
	},
	delOrder:function(){
		util.delGridModel(this.getOrderGrid(),'/order/route/del');
	},
	visitorConWin:function(btn){
		var store = util.createGridStore(cfg.getCtx()+'/site/company/list/contract?companyId='+cfg.getUser().companyId,Ext.create('app.model.site.company.contractModel'));
		var bbar =  util.createGridBbar(store);
		var columns = Ext.create('app.model.site.company.contractColumn',{store:store});
		Ext.create('Ext.window.Window',{
			title:util.windowTitle('&#xe61c;','合同查询',''),
			width:850,
	    	height:450,
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
				columns:columns
			}]
		}).show();
	},
	visitorConAdd :function(type){
		var me=this,win,sel,grid=this.getOrderGrid();
		if(sel = util.getSingleModel(grid)){
			var cno = sel.get('CON_NO') ||'';
			if(type=='1'){
				if(cno!=''){
					util.alert('合同号已录入，修改请点重置合同');
					return;
				}
				this.visitorConWindow(type,grid,sel);
			}else{
				if(cno==''){
					util.alert('请先选择合同号');
					return;
				}
				Ext.Ajax.request({
					url:cfg.getCtx()+'/order/route/contract/status',
					params:{no:cno},
					success:function(response, opts){
						var obj = Ext.decode(response.responseText);
						if(obj.success){
							if(obj.message=='2'){
								util.alert('已核销的合同不能重置');
							}else{
								me.visitorConWindow(type,grid,sel);
							}
							
						}else{
							util.error("检测合同是否核销失败！");
						}
					},
					failure:function(){
						util.error("检测合同是否核销失败！");
					}
				});
			}
		}
	},
	visitorConWindow:function(type,grid,sel){
		var win = util.createEmptyWindow((type=='1'?'选择合同':'重置合同'),(type=='1'?'&#xe6b1;':'&#xe63d;'),360,(type=='1'?180:260),[{
			xtype:'container',
			hidden:type=='1',
			html:'<span class="yellow-color"><i class="iconfont f20">&#xe6ae;</i> 废除的合同号不可再使用！</span>',
			cls:'low'
		},{
			margin:'10 0 0 0',
			layout:'anchor',
			items:[{
				name:'pm[TYPE]',
				xtype:'hidden',
				value:type
			},{
				name:'pm[ORDER_ID]',
				xtype:'hidden',
				value:sel.get('ID')
			},{
				fieldLabel:'合同号',
				labelWidth:50,
				anchor:'90%',
				allowBlank:false,
				xtype:'combo',
    			emptyText:'请选择合同号',
    			name:'pm[CON_NO]',
    			hiddenName:'pm[CON_NO]',
			    queryMode: 'remote',
			    triggerAction: 'all',
			    focusOnToFront:true,
			    forceSelection:true,
			    displayField: 'NO',
			    valueField: 'NO',
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
			            url: cfg.getCtx()+'/site/company/list/contract?status=0',
			            reader: {
			            	rootProperty: 'data',
		            		totalProperty: 'totalSize'
			            }
			        },
			    	model:Ext.create('Ext.data.Model',{
			        	fields: ['ID','NO']
			        })
			    })
			},{
				hidden:type=='1',
				xtype:'checkbox',
				anchor:'100%',
				name:'pm[IS_CANCEL]',
				margin:'0 0 0 55',
				inputValue:1,
				boxLabel:'废除原合同('+sel.get('CON_NO')+')'
				
			}]
		}],[{
			text:'确定',
			disabled: type=='1'?true:false,
			formBind: true,
			handler:function(){
				var form = win.down('form'),
					f = form.getForm();
				if(f.isValid()){
					if(type!='1'){
						if(form.down('combo').getRawValue()==sel.get('CON_NO')){
							util.alert('请选择新的合同号');
							return;
						}
					}
					
					f.submit({
						submitEmptyText:false ,
						url:cfg.getCtx()+'/order/route/save/contract',
		                success: function(form, action) {
		        			util.success('录入合同号成功！');
							win.close();
							grid.getStore().load();
		                },
		                failure: function(form, action) {
		                	var state = action.result?action.result.statusCode:0,
		                		errors = ['录入合同号异常','当前合同号已存在'];
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
	uploadConAttr :function(){
		var me=this,win,sel,grid=this.getOrderGrid();
		if(sel = util.getSingleModel(grid)){
			if(sel.get('CON_STEP')!='undefined'){
				util.alert('合同已上传，不能重复操作');
				return;
			}
			util.uploadSingleAttachment('上传合同附件',function(upload,value){
				var v = value,
					f = upload.up('form'),
					form = f.getForm(),
					win = f.up('window');
				var fix = /\.xls|.doc|.docx|.trf/i;
	               if (!fix.test(v)) {
	               	util.alert('文件格式不正确');
	               	return;
	               }else{
	               	win.body.mask('合同附件上传中...');
	               	if(form.isValid()){
	                	form.submit({
	                		submitEmptyText:false ,
							url:cfg.getCtx()+'/order/traffic/contract/order/save?orderId='+sel.get('ID'),
							method:'POST',
							success:function(res,req){
								var msg = req.result.message||'';
								if(msg!=''){
									util.alert('合同附件上传异常！参照内容：'+msg);
								}else{
									util.success('合同附件上传成功！');
								}
								win.body.unmask();
								win.close();
								grid.getStore().load();
							},
							failure:function(){
								util.error('合同附件上传异常！');
								win.body.unmask();
								win.close();
							}
						});
					}
	             }
			},'合同附件只能上传一次，请确认无误后再上传');
		}
	},
	downloadConAttr :function(){
		var me=this,win,sel,grid=this.getOrderGrid();
		if(sel = util.getSingleModel(grid)){
			if(sel.get('CON_STEP')=='0'){
				Ext.Ajax.request({
					url:cfg.getCtx()+'/order/traffic/contract/order/update?orderId='+sel.get('ID')+'&step=1',
					success:function(response, opts){
						util.downloadAttachment(cfg.getPicCtx()+'/'+sel.get('CON_ATTR'));
						util.success('请等待供应商确认合同！');
						grid.getStore().load();
					}
				});
			}else{
				util.alert('合同申请已提交，不能重复操作');
			}
		}
	},
	confirmConAttr :function(){
		var me=this,win,sel,grid=this.getOrderGrid();
		if(sel = util.getSingleModel(grid)){
			if(sel.get('CON_STEP')=='1'){
			Ext.Ajax.request({
				url:cfg.getCtx()+'/order/traffic/contract/order/update?orderId='+sel.get('ID')+'&step=2',
				success:function(response, opts){
					util.success('合同已确认！');
					grid.getStore().load();
				}
			});
			}else{
				if(sel.get('CON_STEP')=='0'){
				util.alert('合同未申请');
				}else{
				util.alert('合同已确认，不能重复操作');
				}
			}
		}
	},
	auditOtherFee:function(btn,grid){
		var f = btn.up('form'),
			form = f.getForm(),
			win = f.up('window');
		if (form.isValid()) {
			btn.disable();
	        form.submit({
	        	submitEmptyText:false ,
	        	url:cfg.getCtx()+'/order/route/other/fee/audit?auditStatus='+(btn.getText()=='同意'?'2':'3'),
	            success: function(form, action) {
	               util.success('其他附加费审核受理成功');
	               grid.getStore().load();
	               win.close();
	            },
	            failure: function(form, action) {
	            	var state = action.result?action.result.statusCode:0,
	            		errors = ['其他附加费审核受理异常','',''];
	                util.error(errors[0-parseInt(state)]);
	                win.close();
	            }
	        });
	    }
	},
	floatWindow:function(glyph,type,orderId,money,float,url){
		var txt = '外卖',paramName='SALE_FLOAT';
		if(type==1){
			txt='同行';
			paramName='INTER_FLOAT';
		}
		var grid=this.getOrderGrid(),
		win = util.createEmptyWindow(txt+'议价',util.glyphToFont(glyph),500,300,[{
			xtype:'hidden',
			name:'pm[ID]',
			value:orderId
		},{
			xtype:'displayfield',
	    	fieldLabel:txt+'金额',
			value:util.moneyFormat(money,'f20')
		},{
			xtype:'numberfield',
	    	fieldLabel:'议价金额',
	    	width:150,
			value:0,
			name:'pm['+paramName+']',
			enableKeyEvents:true,
			allowBlank: false,
			listeners:{
				change:function(t,n){
					var a = parseFloat(money),
						b = parseFloat(n||0);
					win.down('displayfield#result').setValue(util.format.fmGreenMoney(a+b));
				}
			}
		},{
	    	name:'pm[REMARK]',
    		emptyText:'输入议价备注',
    		xtype:'textarea',
    		anchor:'100%',
    		height:60,
    		margin:'0 10 0 0',
    		cls:'text-cls',
	    	allowBlank: false,
	    	fieldLabel:'备注'
		}],[{
			xtype:'displayfield',
	    	fieldLabel:'议价后',
	    	itemId:'result',
			value:'<span style="height:25px;display:inline-block;line-height:25px;">'+util.format.fmGreenMoney(money,'f20')+'</span>'
		},'->',{
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
		            	url:cfg.getCtx()+url,
		                success: function(form, action) {
		                   util.success('订单'+txt+'金额议价成功');
		                   grid.getStore().load();
		                   win.close();
		                },
		                failure: function(form, action) {
		                	var state = action.result?action.result.statusCode:0,
		                		errors = ['订单'+txt+'金额议价异常'];
		                    util.error(errors[0-parseInt(state)]);
		                    win.close();
		                }
		            });
		        }
	        }
	    }]).show();
	},
	saleFloat:function(btn){
		var me=this,sel,grid=this.getOrderGrid();
		if(sel = util.getSingleModel(grid)){
			this.floatWindow(btn.glyph,0,sel.get('ID'),sel.get('SALE_AMOUNT'),sel.get('SALE_FLOAT'),'/order/route/sale/float');
		}
	}
	,
	interFloat:function(btn){
		var me=this,sel,grid=this.getOrderGrid();
		if(sel = util.getSingleModel(grid)){
			this.floatWindow(btn.glyph,1,sel.get('ID'),sel.get('INTER_AMOUNT'),sel.get('INTER_FLOAT'),'/order/route/inter/float');
		}
	},
	auditInterFloat:function(btn){
		var me=this,sel,grid=this.getOrderGrid(),win;
		if(sel = util.getSingleModel(grid)){
			win = Ext.create('Ext.window.Window',{
				title:util.windowTitle(util.glyphToFont(btn.glyph),'议价审核',''),
				width:400,
				height:250,
				modal:true,
				draggable:false,
				resizable:false,
				maximizable:false,
	   			layout:'fit',
				items:[{
					xtype:'form',
					autoScroll:true,
					fieldDefaults: {
				        labelAlign: 'right',
				        labelWidth: 60,
				        msgTarget: 'qtip'
				    },
					bodyPadding: 5,
					items:[{
						xtype:'hidden',
						name:'pm[ID]',
						value:sel.get('ID')
					},{
						xtype:'displayfield',
				    	fieldLabel:'同行金额',
						value:util.moneyFormat(sel.get('INTER_AMOUNT'),'f18')
					},{
						xtype:'displayfield',
				    	fieldLabel:'议价金额',
						value:util.moneyFormat(sel.get('INTER_FLOAT_TEMP'),'f18')
					},{
				    	name:'pm[REMARK]',
			    		emptyText:'输入审核备注，最多200个字',
			    		xtype:'textarea',
			    		anchor:'100%',
			    		height:70,
			    		maxLength:200,
			    		cls:'text-cls',
			    		listeners:{
							validitychange:function(nf,is){
								if(!is){
									var v = nf.getValue();
									nf.setValue(v.substring(0,200));
								}
							}
						},
				    	allowBlank: false
					}],
					buttons:['议价后：'+util.format.fmGreenMoney((parseFloat(sel.get('INTER_AMOUNT'))+parseFloat(sel.get('INTER_FLOAT_TEMP'))),'f20'),'->',{
				        text: '同意',
				        disabled: true,
				        formBind: true,
				        handler:function(btn){
				        	me.auditInterFloatSubmit(btn,grid);
				        }
				    },{
				    	text:'不同意',
				    	cls:'red',
				    	disabled: true,
				        formBind: true,
				    	handler:function(btn){
				    		me.auditInterFloatSubmit(btn,grid);
				    	}
				    },{
				    	text:'取消',
				    	cls:'disable',
				    	handler:function(btn){
				        	win.close();
				        }
				    }]
				}]
			}).show();
		}
	},
	auditInterFloatSubmit:function(btn,grid){
		var f = btn.up('form'),
			form = f.getForm(),
			win = f.up('window');
		if (form.isValid()) {
			btn.disable();
	        form.submit({
	        	submitEmptyText:false ,
	        	url:cfg.getCtx()+'/order/route/audit/inter/float?auditStatus='+(btn.getText()=='同意'?'2':'3'),
	            success: function(form, action) {
	               util.success('同行议价审核受理成功');
	               grid.getStore().load();
	               win.close();
	            },
	            failure: function(form, action) {
	            	var state = action.result?action.result.statusCode:0,
	            		errors = ['同行议价审核受理异常','',''];
	                util.error(errors[0-parseInt(state)]);
	                win.close();
	            }
	        });
	    }
	},
	earnestAudit:function(b){
		var me=this,sel,grid=this.getOrderGrid(),win;
		if(sel = util.getSingleModel(grid)){
			if(sel.get('IS_EARNEST')=='1'&&sel.get('EARNEST_TYPE')=='1'){
				var str = '定金已付：'+util.moneyFormat(sel.get('EARNEST_INTER_AMOUNT'),'f18')+'，未付：'+util.moneyFormat(sel.get('INTER_AMOUNT')-sel.get('EARNEST_INTER_AMOUNT'),'f18');
				var earnestType = sel.get('EARNEST_TYPE'),
					lastDate = Ext.Date.parse(sel.get('START_DATE'), "Y-m-d"),
					dayCount = sel.get('EARNEST_DAY_COUNT'),lastDateStr='';
				
				if(earnestType=='0'){
					if(lastDate){
						lastDate = Ext.Date.add(lastDate, Ext.Date.DAY, 0-dayCount);
					}
					lastDateStr ='，余额截止支付日期：<span class="f14 blue-color">'+Ext.Date.format(lastDate,'Y/m/d')+'</span>';
				}
				if(earnestType=='1'){
					if(lastDate){
						lastDate = Ext.Date.add(lastDate, Ext.Date.DAY, dayCount);
					}
					lastDateStr ='，代收款截止确认日期：<span class="f14 blue-color">'+Ext.Date.format(lastDate,'Y/m/d')+'</span>';
				}
				str = str+lastDateStr;
				win = Ext.create('Ext.window.Window',{
					title:util.windowTitle(util.glyphToFont(b.glyph),'余款确认',''),
					width:500,
					height:210,
					modal:true,
					draggable:false,
					resizable:false,
					maximizable:false,
		   			layout:'fit',
					items:[{
						xtype:'form',
						fieldDefaults: {
					        labelAlign: 'right',
					        labelWidth: 60,
					        msgTarget: 'qtip'
					    },
						bodyPadding: 5,
						items:[{
							xtype:'hidden',
							name:'pm[ID]',
							value:sel.get('ID')
						},{
							xtype:'displayfield',
							value:str
						},{
					    	name:'pm[CONTENT]',
				    		emptyText:'输入备注，最多200个字',
				    		xtype:'textarea',
				    		anchor:'100%',
				    		height:70,
				    		maxLength:200,
				    		cls:'text-cls',
				    		listeners:{
								validitychange:function(nf,is){
									if(!is){
										var v = nf.getValue();
										nf.setValue(v.substring(0,200));
									}
								}
							},
					    	allowBlank: false
						}],
						buttons:[{
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
						            	url:cfg.getCtx()+'/order/route/comfirm/instead/pay',
						                success: function(form, action) {
						                   util.success('审核余款成功');
						                   grid.getStore().load();
						                   win.close();
						                },
						                failure: function(form, action) {
						                	var state = action.result?action.result.statusCode:0,
						                		errors = ['审核余款异常'];
						                    util.error(errors[0-parseInt(state)]);
						                    win.close();
						                }
						            });
						        }
					        }
					    }]
					}]
				}).show();
			}else{
				util.alert('不是代收款定金预付订单或已确认过');
			}
		}
	},
	visitorContact:function(btn){
		var me=this,sel,grid=this.getOrderGrid();
		if(sel = util.getSingleModel(grid)){
			Ext.create('app.view.produce.route.contactWin',{
				viewShow:cfg.getUser().userType=='03'?'add':'show',
				orderId:sel.get('ID')
			}).show();
		}
	},
	editVisitor:function(btn){
		var me=this,sel,grid=this.getOrderGrid();
		if(sel = util.getSingleModel(grid)){
			Ext.grid.RowEditor.prototype.saveBtnText = "确定";
	        Ext.grid.RowEditor.prototype.cancelBtnText = '取消';
	        var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
		        clicksToMoveEditor: 1,
		        clicksToEdit:2,
		        pluginId:'rowedit',
		        errorSummary:false,
		        autoCancel: false,
		        listeners:{
		        	canceledit:function( editor, context, eOpts){
		        		var r = context.record;
		        		if(r.get('NAME')==''||r.get('MOBILE')==''||r.get('CARD_NO')==''){
		        			var sm = visitorGrid.getSelectionModel();
			                rowEditing.cancelEdit();
			                //me.store.remove(sm.getSelection());
			                if (visitorStore.getCount() > 0) {
			                    sm.select(0);
			                }
		        		}
		        	},
		        	edit:function( editor, context, eOpts ){
		        		context.record.commit();
		        	}
		        }
		    });
			var visitorStore = Ext.create('Ext.data.Store', {
		        model: Ext.create('app.model.order.visitor.model')
		    });
			var visitorGrid = Ext.create('Ext.grid.Panel',{
		    	border:true,
		    	margin:'10',
		    	store:visitorStore,
		    	plugins:[rowEditing],
		    	columns:[
	 	        	Ext.create('Ext.grid.RowNumberer',{width:35}),{
		        		header: '姓名',
			            dataIndex: 'NAME',
			            width:100,
			            editor: {
			                allowBlank: false
			            }
		        	},{
			            header: '性别',
			            dataIndex: 'SEX',
			            width: 70,
			            renderer : function(value) {
			            	if(value=='1'){
			            		return '男';
			            	}else if(value=='2'){
			            		return '女';
			            	}else{
			            		return '';
			            	}
			            },
			            editor: new Ext.form.field.ComboBox({
		                    typeAhead: true,
		                    editable:false,
		                    triggerAction: 'all',
		                    store: [
		                        ['1','男'],
		                        ['2','女']
		                    ]
		                })
		        	},{
		        		header: '游客类型',
			            dataIndex: 'ATTR_NAME',
			            width: 90
		        	},{
		        		header: '手机',
			            dataIndex: 'MOBILE',
			            width:150,
			            editor: {
			                
			            }
		        	}/*,{
		        		header: '邮箱',
			            dataIndex: 'EMAIL',
			            width: 180,
			            editor: {
			                vtype: 'email'
			            }
		        	}*/,{
		        		header: '证件类型',
			            dataIndex: 'CARD_TYPE',
			            width: 120,
			            editor: new Ext.form.field.ComboBox({
		                    typeAhead: true,
		                    allowBlank: false,
		                    editable:false,
		                    triggerAction: 'all',
		                    store: [
		                        ['身份证','身份证'],
		                        ['驾驶证','驾驶证'],
		                        ['护照','护照'],
		                        ['军官证','军官证']
		                    ]
		                })
		        	},{
		        		header: '证件号码',
			            dataIndex: 'CARD_NO',
			            flex:1,
			            editor: {
			            	allowBlank: false
			            }
		        	}
		        
		        ]
			});
			
			var window = Ext.create('Ext.Window',{
				layout:'fit',
				title:util.windowTitle(util.glyphToFont(btn.config.glyph),btn.config.text,''),
				width:800,
				height:420,
				maximized:false,
				modal:true,
				draggable:false,
				resizable:false,
				layout:'fit',
				items:[visitorGrid],
				buttons:[{
					text:'保存',
					cls:'blue',
					handler:function(){
						var visitors = [],
						vgd = visitorStore.getData(),
						vgc = visitorStore.getCount();
						for (var i = 0; i < vgc; i++) {
							visitors.push(vgd.items[i].data);
						}
						Ext.Ajax.request({
							params:{
								visitors:Ext.encode(visitors)
							},
							url:cfg.getCtx()+'/order/route/edit/visitor',
							success:function(response, opts){
								var obj = Ext.decode(response.responseText);
								if(obj.success){
									util.success('修改游客成功！');
									window.close();
								}else{
									util.error('修改游客失败！');
								}
							}
						});
					}
				}]
			});
			window.show();
			
			
			Ext.Ajax.request({
				url:cfg.getCtx()+'/order/visitor/listOrderVisitor?orderId='+sel.get('ID'),
				success:function(response, opts){
					var obj = Ext.decode(response.responseText);
					visitorStore.setData(obj.data);
				}
			});
		}
		
	}
});