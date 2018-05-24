Ext.define('app.controller.account.recharge', {
	extend: 'Ext.app.Controller',
	config: {
        control: {
        	'button[itemId=backBtn]':{
				click:'backAccount'
			},
        	'button[itemId=payBtn]':{
				click:'payOrder'
			},
        	'tabpanel':{
				afterrender:function(p){
					var lis = p.el.query('li',false),
						len = lis.length,
						me = this;
					me.getPayBtn().setHref(cfg.getCtx()+'/account/onlinePay?orderId='+rechargeId+'&bankCode='+lis[0].down('input').dom.value+'&orderNo='+rechargeNo);
					if(len>0){
						for(var i=0;i<len;i++){
							lis[i].on('click',function(){
								for(var j=0;j<len;j++){
									lis[j].down('input').dom.removeAttribute('checked');
									lis[j].removeCls('focus');
								}
								this.down('input').set({checked:true});
								this.addCls('focus');
								me.getPayBtn().setHref(cfg.getCtx()+'/account/onlinePay?orderId='+rechargeId+'&bankCode='+this.down('input').dom.value+'&orderNo='+rechargeNo);
							});
						}
					}
				}
			}
	    },
	    refs: {
	    	payBtn:'button[itemId=payBtn]',
	    	backBtn:'button[itemId=backBtn]',
			tabPanel:'tabpanel',
			payForm:'form[itemId=payForm]'
	    }
    },
    payOrder :function(btn){
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
	   				html:'<h3 style="font-size:16px;padding:5px 2px;line-height:22px;">请你在新打开的平台支付页面进行支付，支付完成前请不要关闭该窗口！</h3><div style="font-size:14px;padding:5px;line-height:18px;margin-top:5px;">在交易完成前请不要关闭此窗口，否则会影响支付。</div>'
	   			}],
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
			util.alert('请选择您的支付方式');
		}
    },
    backAccount :function(btn){
    	document.location.href = cfg.getCtx()+'/account/index?paramId='+entityId;
    }
});