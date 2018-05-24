Ext.define('Weidian.view.order.route.Plan', {
    extend: 'Ext.List',
    xtype: 'routepriceplan',
    config:{
    	userCls:'D_traffic_plan bbor',
    	onItemDisclosure:true,
		itemTpl:'{PLAN_TITLE}',
		itemCls:'D_traffic_plan_item bbor',
		deferEmptyText:'没有数据',
		emptyText:'没有数据',
		submitParams:null,
		store:null
	},
	initialize: function(){
		var store = util.createStore(cfg.url.route.price.plan,false),
			sParams = this.getSubmitParams();
		store.setFields(['ROUTE_ID','PLANID','ID','PLAN_TITLE','NO','TITLE']);
		if(sParams){
			store.getProxy().setExtraParams(sParams);
		}
		this.setStore(store);
		store.on('load',this.onStoreLoaded,this);
		this.callParent();
	},
	onStoreLoaded:function(s,rs){
		if(rs.length>0){this.select(0);}
	}
});
    
    