Ext.define('app.view.b2b.user.agency.waitDo', {
	extend: 'app.view.b2b.user.waitDo',
    initComponent: function() {
    	this.callParent();
    	var me = this;
    	var html = [
			//'<div class="item">',
			'<span class="f14" style="padding-right:10px;padding-left:3px;color:#FAA000;"><i class="iconfont f18" style="top:2px;">&#xe68c;</i> 待办提醒</span>'
		];
    	var waitpay1 = '',waitpay2='',paying1='',paying2='',refund1='',refund2=''; 
    	var rcd = waitDoData.routeOrderCnt,
    		tcd = waitDoData.trafficOrderCnt;
    	if(util.getDirectModuleId('order/route')){
    		waitpay1 = '<a href="javascript:;" class="f14" onclick="util.redirectTo(\'order/route\',\'?dynamicParamsOrderStatus=0\')">{routeOrderCnt.WAIT_PAY_CNT}</a>';
    		paying1 = '<a href="javascript:;" class="f14" onclick="util.redirectTo(\'order/route\',\'?dynamicParamsOrderStatus=4\')">{routeOrderCnt.WAIT_REFUNDING_CNT}</a>';
    		refund1 = '<a href="javascript:;" class="f14" onclick="util.redirectTo(\'order/route\',\'?dynamicParamsOrderStatus=3\')">{routeOrderCnt.WAIT_REFUND_CNT}</a>';
    	}else{
    		waitpay1 = '<span class="f14">{routeOrderCnt.WAIT_PAY_CNT}</span>';
    		paying1 = '<span class="f14">{routeOrderCnt.WAIT_REFUNDING_CNT}</span>';
    		refund1 = '<span class="f14">{routeOrderCnt.WAIT_REFUND_CNT}</span>';
    	}
    	if(util.getDirectModuleId('order/traffic')){
    		waitpay2 = '<a href="javascript:;" class="f14" onclick="util.redirectTo(\'order/traffic\',\'?dynamicParamsOrderStatus=0&dynamicParamsOrderTrade=2\')">{routeOrderCnt.WAIT_PAY_CNT}</a>';
    		paying2 = '<a href="javascript:;" class="f14" onclick="util.redirectTo(\'order/traffic\',\'?dynamicParamsOrderStatus=4&dynamicParamsOrderTrade=2\')">{routeOrderCnt.WAIT_REFUNDING_CNT}</a>';
    		refund2 = '<a href="javascript:;" class="f14" onclick="util.redirectTo(\'order/traffic\',\'?dynamicParamsOrderStatus=3&dynamicParamsOrderTrade=2\')">{routeOrderCnt.WAIT_REFUND_CNT}</a>';
    	}else{
    		waitpay2 = '<span class="f14">{routeOrderCnt.WAIT_PAY_CNT}</span>';
    		paying2 = '<span class="f14">{routeOrderCnt.WAIT_REFUNDING_CNT}</span>';
    		refund2 = '<span class="f14">{routeOrderCnt.WAIT_REFUND_CNT}</span>';
    	}
    	html.push('待付款： '+waitpay1+' 个订单；待退款： '+refund1+' 个订单；退款中： '+paying1+' 个订单');
    	//html.push('待付款：交通订单 '+waitpay2+' 个、线路订单 '+waitpay1+' 个；待退款：交通订单 '+refund2+' 个、线路订单 '+refund1+' 个；退款中：交通订单 '+paying2+' 个、线路订单 '+paying1+' 个');
    	//html.push('</div>');
    	
    	me.add({
    		xtype:'container',
    		itemId:'waitDoPanel',
    		tpl:html,
    		data:[]
    	});
    	me.add('->');
    	if(util.getParentModuleId('site/company')){
    		Ext.Ajax.request({
    			url:cfg.getCtx()+'/site/depart/list?COMPANY_ID='+cfg.getUser().companyId+'&start=0&limit=20',
    			success:function(response, opts){
    				var obj = Ext.decode(response.responseText),
    					data = obj.data;
    				var menus = [];
    				Ext.Array.each(data,function(o,i){
    					menus.push({
    						record:{data:o},
    						glyph:'xe615@iconfont',
    						text:o.TEXT
    					});
    				});
    				if(menus.length>0){
	    				me.insert(2,{
	    					margin:'0 1 0 0',
	    		    		menu:Ext.create('Ext.menu.Menu', {
	    		    			listeners:{
	    		    				click:function(menu,item){
	    		    					util.accountWindow(item.record);
	    		    				}
	    		    			},
	    		    		    items: menus
	    		    		}),
	    		    		cls:'plain none deeporange',
	    		    		height:35,
	    		    		xtype:'button',
	    		    		text:'<i class="iconfont white-color" style="font-size:16px;top:1px;">&#xe633;</i> 我的余额'
	    		    	});
    				}
    			}
    		});
	    	
    	}
    }
});
