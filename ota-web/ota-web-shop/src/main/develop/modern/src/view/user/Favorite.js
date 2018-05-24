Ext.define('Weidian.view.user.Favorite', {
    extend: 'Weidian.view.Base',
    xtype: 'mefavorite',
	config: {
		title:'我的收藏',
		showTopPoint:true,
		userCls:'D_me_favorite',
		controller:'user',
		backHide:false,
		layout:'fit'
		/*expandItems: [{
			align:'right',
			ripple:false,
			iconCls:'x-fm fm-more f15',
			handler:'moreMenus',
			actions:[]
		}]*/
	},
	activeCallBack:function(main,old){
		this.setBackHashUrl('ucenter');
	},
	initialize: function(){
    	this.initViews();
    	this.callParent(arguments);
    },
    initViews:function(){
    	var items = [];
    	var routeStore = util.createStore(cfg.url.favorite.route.list,false,6);
		routeStore.setModel(Ext.create('Weidian.model.product.Route'));
		/*routeStore.getProxy().setExtraParams({
			
		});*/
    	this.routeList = Ext.create('Weidian.view.product.route.List',{
    		controller:'route',
			infinite : false,
		 	variableHeights: true,
		 	//itemHeight:255,
		 	scrollToTopOnRefresh:false,
			userCls: 'route-list-view shadow',
			store:routeStore
	   	});
    	routeStore.loadPage(1);
    	items.push(this.routeList);
    	this.setItems(items);
    }
})