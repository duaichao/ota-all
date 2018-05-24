Ext.define('Weidian.view.product.route.RouteController', {
    extend: 'Ext.app.ViewController',

    alias: 'controller.route',
	
	control: {
		'tabbar#itemtabs':{
			activeTabchange:'onTabItemChange'
		}
    },
	onTabItemChange:function(tb,n,o,e){
		var me = this,
			detailView = me.getView(),
        	refs = me.getReferences();
		var target = refs['A_'+n.action].element,
			scroller = detailView.getScrollable();
		
		scroller.scrollTo(0,parseInt(target.getY())+scroller.getPosition().y-(tb.isDocked()?115:65),true);
	},
	onDateViewClick:function(e,t){
		var el = Ext.fly(t);
		if(el.getAttribute('action')=='calendar'){
			this.gotoOrderCalendar();
		}
	},
	onDateItemSelected:function(dv , index , target , record , e ){
		this.gotoOrderCalendar(record.get('RQ'));
	},
	gotoOrderCalendar:function(startDate){
		var currView = cfg.view.getActiveItem(),
			modelData = currView.getViewModel().getData();
		
		if(!Ext.isString(startDate))startDate='';
		currView.setSubmitParams({
			routeId:modelData.ID,
			title:modelData.TITLE,
			singleRoomPrice:modelData.RETAIL_SINGLE_ROOM,
			startDate:startDate
		});
		this.redirectTo('order/calendar/'+modelData.ID);
	}
});
