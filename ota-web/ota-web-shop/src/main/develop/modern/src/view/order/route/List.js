Ext.define('Weidian.view.order.route.List', {
    extend: 'Ext.dataview.List',
    xtype: 'routeorderlist',
    config:{
    	userCls:'D_order_list',
    	controller:'user',
    	//onItemDisclosure:true,
    	itemTpl:[
    	         '<div class="wrap"><div class="tbar">',
    	         '<span style="float:left;" class="fcgreen f12">{STATUS:this.getOrderStatus}</span><span style="float:right;" class="f12 fcgray">订单号：{NO}</span>',
    	         '<div style="clear:both"></div>',
    	         '</div>',
    	         '<div class="content">',
    	         '<h3>{PRODUCE_NAME}</h3>',
    	         '<div class="info f12">{MAN_COUNT}位成人，{CHILD_COUNT}位儿童       <span style="padding-left:20px;">{START_DATE}出发</span></div>',
    	         '</div>',
    	         '<div class="bbar tbor">',
    	         '<span class="fcorange1 f16"><span class="f12">￥</span>{SALE_AMOUNT}</span>',
    	         '<span class="btn" style="float:right;">查看订单</span>',
    	         '<div style="clear:both"></div>',
    	         '</div></div><div class="D_spacer"></div>',{
    	        	 getOrderStatus:function(status){
    	        		 var txt = ['待付款','付款中','已付款','待退款','退款中','已退款','手动取消订单','系统自动取消'];
    	        		 return txt[status];
    	        	 }
    	         }
    	],
		itemCls:'D_order_list_item', 
		deferEmptyText:false,
		store:null,
	 	emptyText: '<p class="no-datas">暂无订单</p>',
	 	infinite : true,
	 	//striped :true,
	 	variableHeights: true,
		scrollToTopOnRefresh:false,
		plugins: [{
			xclass: 'Ext.plugin.PullRefresh'
		},{
			xclass: 'Ext.plugin.ListPaging'
		}]
	},
	initialize: function(){
		var store = util.createStore(cfg.url.order.list,false,10);
		store.setModel(Ext.create('Weidian.model.order.Model'));
		this.setStore(store);
		store.loadPage(1);
		this.callParent();
		this.on('itemtap',this.onListItemTap,this);
	},
	onListItemTap:function(list , index , target , record , e ){
		//var t = Ext.fly(e.getTarget());
		//if(t&&t.hasCls('btn')){
			this.getController().redirectTo('order/detail/'+record.get('ID'));
		//}
	}
});
    
    