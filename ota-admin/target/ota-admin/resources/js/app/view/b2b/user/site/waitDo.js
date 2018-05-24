Ext.define('app.view.b2b.user.site.waitDo', {
    extend: 'app.view.b2b.user.waitDo',
    initComponent: function() {
    	var me = this;
    	this.callParent();
    	var html = [
			'<div class="item">',
			'<span class="f14" style="padding-right:10px;padding-left:3px;color:#FAA000;"><i class="iconfont f18" style="top:2px;">&#xe68c;</i> 待办提醒</span>'
		];
    	
    	html.push('退款审核：');
    	/*html.push('交通订单');
    	html.push('<a href="javascript:;" class="f14" onclick="util.redirectTo(\'order/traffic\',\'?dynamicParamsOrderStatus=4&dynamicParamsOrderTrade=2\')"> ');
    	html.push(waitDoData.trafficOrderTotalSize);
    	html.push(' </a>个  、');*/
    	
    	
    	html.push('线路订单');
    	html.push('<a href="javascript:;" class="f14" onclick="util.redirectTo(\'order/route\',\'?dynamicParamsOrderStatus=4\')"> ');
    	html.push('{routeOrderTotalSize}');
    	html.push(' </a>个；');
    	
    	
    	
    	html.push('入驻审核：供应商');
    	html.push('<a href="javascript:;" class="f14" onclick="util.redirectTo(\'site/setting\')"> ');
    	html.push('{supplySize}');
    	html.push(' </a>家、');
    	
    	
    	html.push('旅行社');
    	html.push('<a href="javascript:;" class="f14" onclick="util.redirectTo(\'site/setting\')"> ');
    	html.push('{size}');
    	html.push(' </a>家');
    	
    	
    	
    	
    	
    	html.push('</div>');
    	me.add({
    		xtype:'container',
    		itemId:'waitDoPanel',
    		tpl:html,
    		data:[]
    	});
    	me.add('->');
		if(citys.length>0){
			me.add({
				cls:'plain none deeporange',
				xtype:'button',
				height:35,
				margin:'0 1 0 0',
				itemId:'rechangeButton',
	    		text:'<i class="iconfont white-color" style="font-size:18px;top:1px;">&#xe68b;</i> 累计充值',
	    		cityId:'',
	    		handler:function(btn){
	    			var win = Ext.create('Ext.window.Window',{
        				width:800,
        				height:400,
        				modal:true,
        				draggable:false,
        				resizable:false,
        				maximizable:false,
        	   			layout:'fit',
        				title:util.windowTitle('&#xe61e;','历史充值'),
        				items:[{
        					xclass:'app.view.b2b.user.site.rechargeGrid',
        					cityId:btn.cityId
        				}]
        			});
        			win.show();
	    		}
			});
		}
    },
    updateParams:function(params){
    	var me = this;
		Ext.Ajax.request({
			params:params,
			url:cfg.getCtx()+'/b2b/user/site/info',
			success:function(response, opts){
				var obj = Ext.decode(response.responseText);
				me.down('#rechangeButton').setText('<i class="iconfont white-color"  style="font-size:18px;top:1px;">&#xe68b;</i> 累计充值:'+obj.money);
				me.down('#rechangeButton').cityId = params.cityId;
			}
		});
    }
});
