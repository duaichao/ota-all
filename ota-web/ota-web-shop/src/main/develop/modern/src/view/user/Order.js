Ext.define('Weidian.view.user.Order', {
    extend: 'Weidian.view.Base',
    xtype: 'meorder',
	config: {
		title:'我的订单',
		userCls:'D_me_order',
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
	initialize: function(){
    	this.initViews();
    	this.callParent(arguments);
    },
    initViews:function(){
    	var items = [];
    	/*items.push(Ext.create('Ext.tab.Bar',{
			itemId:'categoryTab',
			activeTab:0,
			items:[{
				ui: 'gray',
				title:'待付款'
			},{
				ui: 'gray',
				title:'全部订单'
			}],
			userCls: 'shadow category-tab'
		}));*/
    	this.orderList = Ext.create('Weidian.view.order.route.List',{
	   		 itemCls:'D_order_list_item'
	   	});
    	items.push(this.orderList);
    	this.setItems(items);
    }
})