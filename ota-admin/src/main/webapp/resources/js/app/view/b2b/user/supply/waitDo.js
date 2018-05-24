Ext.define('app.view.b2b.user.supply.waitDo', {
	extend: 'app.view.b2b.user.waitDo',
    initComponent: function() {
    	this.callParent();
    	var me = this;
    	var html = [
		'<div class="item">',
		'<span class="f14" style="padding-right:10px;padding-left:3px;color:#FAA000;"><i class="iconfont f18" style="top:2px;">&#xe68c;</i> 待办提醒</span>'
		];
    	var s1 = '',s2='',s3='',s4=''; 
    	if(util.getDirectModuleId('order/traffic')){
    		s1 = '<a href="javascript:;" class="f14" onclick="util.redirectTo(\'order/traffic\',\'?dynamicParamsOrderTrade=2\')">{trafficOrderTotalSize}</a>';
    	}else{
    		s1 = '<span class="f14">{trafficOrderTotalSize}</span>';
    	}
    	if(util.getDirectModuleId('order/route')){
    		s2 = '<a href="javascript:;" class="f14" onclick="util.redirectTo(\'order/route\',\'?dynamicParamsOrderStatus=refund\')">{routeOrderTotalSize}</a>';
    	}else{
    		s2 = '<span class="f14">{routeOrderTotalSize}</span>';
    	}
    	if(util.getDirectModuleId('resource/route')){
    		s3 = '<a href="javascript:;" class="f14" onclick="util.redirectTo(\'resource/route\',\'?dynamicParamsTrafficStatus=timeout\')">{routeTotalSize}</a>';
    	}else{
    		s3 = '<span class="f14">{routeTotalSize}</span>';
    	}
    	//if(util.getDirectModuleId('finance/query')){
    		//s4 = '对账：累计对账 '+util.moneyFormat(waitDoData.finishCalcTotal,'f14')+'，待对账 '+util.moneyFormat(waitDoData.unFinishCalcTotal,'f14')+'，对账中 '+util.moneyFormat(waitDoData.waitFinishCalcTotal,'f14');
    	//}
    	//html.push('退款审核：交通订单 '+s1+' 个、线路订单 '+s2+' 个；	线路团期：'+s3+' 条线路已过期；'+s4);
    	html.push('线路：'+s3+' 条线路已过期； 订单（线路）：退款待审核 '+s2+' 个 ');
    	html.push('</div>');
    	
    			
    	me.add({
    		xtype:'container',
    		itemId:'waitDoPanel',
    		tpl:html,
    		data:[]
    	});
		me.add('->');
		if(util.getDirectModuleId('finance/query')){
    		me.add({
    			cls:'plain none deeporange',
    			height:35,
    			style:'margin-right:1px;',
    			text:'<i class="iconfont white-color" style="font-size:18px;top:1px;">&#xe6ab;</i> 对账查询',
    			handler:function(){
    				util.redirectTo('finance/query','')
    			}
    		});
    		
    	}
    }
});
