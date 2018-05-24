Ext.define('app.controller.order.route.renew', {
	extend: 'app.controller.common.BaseController',
	views:['app.view.order.refundGrid'],
	config: {
		control: {
			'button#add':{
				click:'addRenewOrder'
			},
			'button#edit':{
				click:function(){
					var me=this,sel,grid=this.getOrderGrid();
					if(sel = util.getSingleModel(grid)){
						document.location.href=cfg.getCtx()+"/order/route/renew/buy?&routeId="+sel.get('PRODUCE_ID')+"&orderId="+sel.get('ID');
					}
				}
			},
			'button#submit':{
				click:'submitRenewOrder'
			},
			'button#audit':{
				click:'auditRenewOrder'
			},
			'button#payment':{
				click:function(){
					var me=this,sel,grid=this.getOrderGrid();
					if(sel = util.getSingleModel(grid)){
						document.location.href=cfg.getCtx()+"/produce/paycenter?orderId="+sel.get('ID')+'&type=4';
					}
				}
			},
			'button#refund':{
				click:'refund'
			},
			'button#auditRefund':{
				click:'auditRefund'
			},
			'button#finishRefund':{
				click:'finishRefund'
			},
			'button#cancelOrder':{
				click:'cancelOrder'
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
							title: util.windowTitle('&#xe67d;','出团确认',''),
							bodyPadding:5,
							width:320,
							height:150,
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
			'routeRenewGrid':{
				selectionchange: function(view, records) {
					var	yesBtnIds = [],
						record = records[0],
						orderStatus = parseInt(record.get('STATUS')),
						accountStatus = parseInt(record.get('ACCOUNT_STATUS')),
						renewStatus = parseInt(record.get('RENEW_STATUS')),
						auditFloatStatus = parseInt(record.get('AUDIT_INTER_FLOAT'));
					//初始化
					this.btnToggle([],true);
					//待付款
					if(orderStatus==0){
						if(renewStatus==0||renewStatus==3){
							yesBtnIds = ['edit','submit','cancelOrder'];
						}else if(renewStatus==1){
							yesBtnIds = ['audit'];
						}else{
							yesBtnIds = ['payment','cancelOrder'];
						}
					}else if(orderStatus==1){//付款中
						yesBtnIds = ['cancelOrder'];
					}else if(orderStatus==2){//已付款
						//如果是对账状态且是已付款 不能申请退款
						if(accountStatus!=0){
							yesBtnIds = ['startConfirm'];
						}else{
							yesBtnIds = ['refund','startConfirm'];
						}
					}else if(orderStatus==3){//待退款
						yesBtnIds = ['auditRefund','startConfirm'];
					}else if(orderStatus==4){//退款中
						yesBtnIds = ['finishRefund','startConfirm'];
					}else if(orderStatus==5){//已退款
						yesBtnIds = ['startConfirm'];
					}else if(orderStatus==6){//手动取消
						yesBtnIds = [];
					}else if(orderStatus==7){//自动取消
						yesBtnIds = [];
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
						if(e.target.tagName=='I'&&e.target.className.indexOf('info')!=-1){
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
				}
			}
		},
		refs: {
			orderGrid: 'routeRenewGrid',
			orderTbar:'routeRenewGrid toolbar[dock=top]'
		}
	},
	btnToggle:function(btnIds,disabled){
		var tbar = this.getOrderTbar(),
			btns = tbar.query('button'),
			initNoBtnIds = ['add','printVisitor','visitorCon','supplyCon'];
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
		setTimeout(function(){
			Ext.Ajax.request({
			    url: util.getPowerUrl(),
			    success: function(response, opts) {
			        var obj = Ext.decode(response.responseText),
			        	items = obj.children,
			        	ubtn = [];
			        var cmenu = [],smenu = [];
			        for(var i=0;i<items.length;i++){
			        	delete items[i].checked;
			        	//如果是管理员 不能补单
			        	if(cfg.getUser().departId==''){
			        		if(items[i].itemId=='add'||items[i].itemId=='edit'||items[i].itemId=='payment'||items[i].itemId=='submit'){
			        			items[i].hidden = true;
			        		}
			        	}
			        	if(Ext.String.startsWith( items[i].itemId, 'contract')){
			        		cmenu.push(items[i]);
			        	}else if (Ext.String.startsWith( items[i].itemId, 'supply')){
			        		smenu.push(items[i]);
			        	}else{
			        		ubtn.push(items[i]);
			        	}
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
			        ubtn.push({
			        	xtype:'combo',
						forceSelection: true,
						editable:false,
			            valueField: 'value',
			            labelWidth:65,
			            labelAlign:'right',
			            fieldLabel:'状态',
			            displayField: 'text',
			            store:util.createComboStore([{
			            	text:'全部',
			            	value:''
			            },{
			            	text:'待审核',
			            	value:1
			            },{
			            	text:'已审核',
			            	value:3
			            }]),
			            width:150,
			            minChars: 0,
			            value:cfg.getUser().companyType=='1'?'1':'',
			            queryMode: 'local',
			            typeAhead: true,
			            listeners:{
			            	change:function(c, newValue, oldValue, eOpts ){
						       g.getStore().getProxy().setExtraParams({renewStatus:newValue});
						       g.getStore().load();
			            	}
			            }
			        });
			        if(items.length>0){
			        	util.createGridTbar(g,ubtn);
			        }
			        
			        var bbar = g.getDockedItems()[g.getDockedItems().length-1];
			        bbar.add('-');
			        bbar.add({
			        	xtype:'checkbox',
			        	boxLabel:'<span class="orange-color">查看取消补单</span>',
			        	inputValue:'1',
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
	addRenewOrder:function(btn){
		document.location.href=cfg.getCtx()+'/order/route/renew/buy';
	},
	submitRenewOrder:function(btn){
		var me=this,sel,grid=this.getOrderGrid();
		if(sel = util.getSingleModel(grid)){
			Ext.MessageBox.confirm(util.windowTitle('&#xe635;','提示',''), '<div style="font-size:16px;height:40px;line-height:30px;">此操作不可逆，确定提交补单？</div>', function(btn, text){
				if(btn=='yes'){
					Ext.Ajax.request({
						url:cfg.getCtx()+'/order/route/renew/submit',
						params:{orderId:sel.get('ID')},
						success:function(response, opts){
							var obj = Ext.decode(response.responseText);
							if(obj.success){
								util.success('补单提交成功');
								grid.getStore().load();
							}else{
								util.error("补单提交失败！");
							}
						},
						failure:function(){
							util.error("补单提交失败！");
						}
					});
				}
			}, this);
		}
	},
	auditRenewOrder:function(btn){
		var me=this,sel,grid=this.getOrderGrid(),win;
		if(sel = util.getSingleModel(grid)){
			win = Ext.create('Ext.window.Window',{
				title:util.windowTitle(util.glyphToFont(btn.glyph),'补单审核',''),
				width:400,
				height:175,
				modal:true,
				draggable:false,
				resizable:false,
				maximizable:false,
	   			layout:'fit',
				items:[{
					xtype:'form',
					autoScroll:false,
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
					buttons:['->',{
				        text: '同意',
				        disabled: true,
				        formBind: true,
				        handler:function(btn){
				        	me.auditRenewOrderSubmit(btn,grid);
				        }
				    },{
				    	text:'不同意',
				    	cls:'red',
				    	disabled: true,
				        formBind: true,
				    	handler:function(btn){
				    		me.auditRenewOrderSubmit(btn,grid);
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
	auditRenewOrderSubmit:function(btn,grid){
		var f = btn.up('form'),
			form = f.getForm(),
			win = f.up('window');
		if (form.isValid()) {
			btn.disable();
	        form.submit({
	        	submitEmptyText:false ,
	        	url:cfg.getCtx()+'/order/route/renew/audit?renewStatus='+(btn.getText()=='同意'?'2':'3'),
	            success: function(form, action) {
	               util.success('补单审核受理成功');
	               grid.getStore().load();
	               win.close();
	            },
	            failure: function(form, action) {
	            	var state = action.result?action.result.statusCode:0,
	            		errors = ['补单审核受理异常','',''];
	                util.error(errors[0-parseInt(state)]);
	                win.close();
	            }
	        });
	    }
	},
	refund:function(btn){
		var me=this,sel,grid=this.getOrderGrid(),win;
		if(sel = util.getSingleModel(grid)){
			if(sel.get('IS_REFUND')!='1'){
				util.alert('订单参加的活动不允许退款');
				return;
			}
			
			
			win = util.createEmptyWindow('申请退款',util.glyphToFont(btn.glyph),450,285,[{
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
				xtype:'label',
				html:'<span class="red-color"><i class="iconfont f18">&#xe6ae;</i> 参加活动的订单，只退还实际交易金额！</span>'
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
	},
	auditRefund:function(btn){
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
	},
	finishRefund:function(btn){
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
	},
	visitorConWin:function(btn){
		var store = util.createGridStore(cfg.getCtx()+'/site/company/list/contract?companyId='+cfg.getUser().companyId,Ext.create('app.model.site.company.contractModel'));
		var bbar =  util.createGridBbar(store);
		var columns = Ext.create('app.model.site.company.contractColumn',{store:store});
		Ext.create('Ext.window.Window',{
			title:util.windowTitle('&#xe771;','合同查询',''),
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
		var win = util.createEmptyWindow('选择合同','&#xe6a6;',360,(type=='1'?150:200),[{
			padding:10,
			layout:'anchor',
			items:[{
				xtype:'container',
				hidden:type=='1',
				style:'padding:2px 0 10px 55px;',
				html:'<div class="red-color f14" ><i class="iconfont f20 red-color">&#xe6ae;</i> 废除的合同号不可再使用！</div>'
			},{
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
	}
});