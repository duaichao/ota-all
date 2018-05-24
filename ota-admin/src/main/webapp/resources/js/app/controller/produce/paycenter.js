Ext.define('app.controller.produce.paycenter', {
	extend: 'app.controller.common.BaseController',
	config: {
		control: {
			'button[itemId=payBtn]':{
				click:'payOrder'
			},
			'tabpanel':{
				afterrender:function(p){
					var lis = p.el.query('li',false),
						len = lis.length,
						me = this;
					if(len>0){
						for(var i=0;i<len;i++){
							lis[i].on('click',function(){
								for(var j=0;j<len;j++){
									lis[j].down('input').dom.removeAttribute('checked');
									lis[j].removeCls('focus');
								}
								this.down('input').set({checked:true});
								this.addCls('focus');
								if(this.down('span.money-color')){
									me.getPayBtn().setHref(null);
								}else{
									var rg = me.getPayForm().down('radiogroup'),val='';
									if(rg){
										if(order.IS_EARNEST=='1'){
											val=-1;
										}else{
											val = rg.getValue().IS_EARNEST;
										}
									}
									if(val=='1'){
										//定金金额需大于订单金额的20%
										var djAmount = (order.MAN_COUNT+order.CHILD_COUNT)*route.EARNEST_INTER,
											minAmount = order.INTER_AMOUNT*0.2;//20%
										if(djAmount<minAmount){
											return;
										}
									}
									me.getPayBtn().setHref(cfg.getCtx()+'/order/online/pay?isEarnest='+val+'&orderId='+order.ID+'&bankCode='+this.down('input').dom.value+'&orderNo='+order.NO+'&money='+order.INTER_AMOUNT+'&platfrom=b2b');
									
								}
							});
						}
					}
				}
			}
		},
		refs: {
			payBtn:'button[itemId=payBtn]',
			tabPanel:'tabpanel',
			payForm:'form[itemId=payForm]'
        }
	},
	payOrder:function(btn){
		
		var me = this,
			tb = this.getTabPanel(),
			rds = tb.getEl().query('input[name=payment]:checked');
		
		if(rds.length==0){
			util.alert('请选择支付方式');
			return;
		}
		
		
		
		if(btn.getHref()){
			var win = top.Ext.create('Ext.window.Window',{
				title:util.windowTitle('','支付提醒',''),
				width:400,
	   			height:220,
	   			draggable:false,
				resizable:false,
				closable:false,
				modal:true,
	   			bodyStyle:'background:#fff;padding:10px;',
	   			layout:'fit',
	   			items:[{
	   				html:'<h3 style="font-size:16px;padding:5px 2px;line-height:22px;">请你在新打开的平台支付页面进行支付，支付完成前请不要关闭该窗口！</h3><div style="font-size:14px;padding:5px;line-height:18px;margin-top:5px;">在订单支付完成前请不要关闭此窗口，否则会影响购买。</div>'
	   			}],
	   			listeners:{
	   				close:function(){
	   					if(proType=='1'){
							util.redirectTo('order/traffic');
						}
						if(proType=='2'||proType=='3'){
							util.redirectTo('order/route');
						}
						if(proType=='4'){
							util.redirectTo('order/route/renew');
						}
	   				}
	   			},
	   			buttons:[{
	   				text:'已完成支付',
	   				handler:function(){
	   					win.close();
	   				}
	   			},{
	   				text:'支付遇到问题',
	   				cls:'disable',
	   				handler:function(){
	   					win.close();
	   				}
	   			}]
			}).show();
		}else{
			
			//需要输入支付密码
			var isSetPayPassword = cfg.getUser().isSetPayPassword;
			if(isSetPayPassword!=2){
				util.alert('支付密码未激活，不能余额支付');
				return;
			}
			var validPayPwdWin = util.createEmptyWindow(
					'验证支付密码',
					'&#xe63b;',300,150,[{
							style:'margin-top:10px;',
				        	fieldLabel:'支付密码',
				        	emptyText:'24小时内最多输错6次',
				        	width:240,
			        		minLength:6,
				        	name : 'pm[PAY_PWD]',
							xtype: 'textfield',
							inputType:'password',
							allowBlank:false
					    }
					],[{
						text:'确定',
						handler:function(b){
							var payWinForm = validPayPwdWin.down('form'),
								payWinBasicForm = payWinForm.getForm();
							if(payWinBasicForm.isValid()){
								b.disable();
								payWinBasicForm.submit({
									submitEmptyText:false ,
									url:cfg.getCtx()+'/order/traffic/check/pay/pwd',
					                success: function(ff, a) {
					                	validPayPwdWin.close();
					                	//begin可以支付----------------
					                	var f = me.getPayForm(),
						    				form = f.getForm();
						    			
						    			
						    			var rg =f.down('radiogroup'),val='';
						    			if(rg&&rg.isVisible()){
						    				if(order.IS_EARNEST=='1'){
						    					val=-1;
						    				}else{
						    					val = rg.getValue().IS_EARNEST;
						    				}
						    			}
						    			if(val=='1'){
						    				//定金金额需大于订单金额的20%
						    				var djAmount = (order.MAN_COUNT+order.CHILD_COUNT)*route.EARNEST_INTER,
						    					minAmount = order.INTER_AMOUNT*0.2;//20%
						    				
						    				if(djAmount<minAmount){
						    					util.alert('定金金额需大于订单金额的20%');
						    					return;
						    				}
						    			}
						    			
						    			if (form.isValid()) {
						    				f.mask('付款中...');
						    	            form.submit({
						    	            	submitEmptyText:false ,
						    	            	url:cfg.getCtx()+'/order/traffic/pay?orderId='+order.ID+'&type='+proType+'&platfrom=b2b',
						    	                success: function(form, action) {
						    	                	var orderId = action.result.message||'';
						    	                    document.location.href=cfg.getCtx()+"/produce/payfinish?orderId="+order.ID+"&code=1"+"&type="+proType;
						    					    return;
						    	                },
						    	                failure: function(form, action) {
						    	                	f.unmask();
						    	                	var state = action.result?action.result.statusCode:0,
						    	                		errors = ['支付异常','订单不存在','占座失败','余额不足', '账户已停用'];
						    	                    //跳转到订单列表
						    	                    document.location.href=cfg.getCtx()+"/produce/payfinish?orderId="+order.ID+"&code="+state+"&type="+proType;
						    	                }
						    	            });
						    	        }
						    			//end可以支付----------------
					                	
					                },
					                failure: function(form, action) {
					                	b.enable();
					                	var state = action.result?action.result.statusCode:0;
					                	if(state==0){
					                		util.error('密码错误次数过多，24小时内不能余额支付');
					                		validPayPwdWin.close();
					                		me.getTabPanel().down('#balanceTab').disable();
					                	}else{
					                		util.error('密码错误，今天还能输入错误'+state+'次');
					                	}
					                }
								});
							}
						}
					}]
				).show();
			
			
			
			
			
			
		}
	}
});