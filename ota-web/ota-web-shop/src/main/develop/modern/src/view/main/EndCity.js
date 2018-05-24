Ext.define("Weidian.view.main.EndCity", {
	extend: 'Weidian.view.Base',
	alternateClassName: 'routecity',
    xtype:'routecity',
	config:{
		controller: 'main',
		backHide:false,
		title:'目的地城市'
		/*title:{
			margin:'0 0 0 8',
			xtype: 'searchfield',
			placeHolder: '产品/编号/目的地',
			autoCapitalize: false,
			clearIcon: false,
			listeners:{
				keyup:'onSearchFinish'
			}
		},
		expandItems: [{
			ripple:false,
			userCls:'arrow-icon',
			text:'西安',
			iconAlign:'right',
			iconCls:'x-fa fa-angle-down',
			ui:'white',
			handler: 'onCityButtonTap',
			listeners: {
				scope: 'controller',
				element: 'element'
			}
		},{
			align:'right',
			ripple:false,
			margin:'0 0 0 8',
			iconCls:'x-fm fm-home f15',
			handler: 'onHomeButtonTap',
			listeners: {
				scope: 'controller',
				element: 'element'
			}
		}]*/
	},
    initialize: function(){
    	this.initViews();
    	this.callParent();
    },
    initViews:function(){
    	var items = [];
    	var tabs = [];
    	Ext.Array.each(Ext.shopInfo.categorys,function(cg,i){
    		tabs.push({categoryId:cg.ID,categoryPid:cg.PID,title:cg.CATEGORY});
    	});
    	var dv,tbs;
    	items.push(
			tbs = Ext.create('Ext.tab.Bar',{
				userCls:'left-tab',
				layout:'vbox',
				docked:'left',
				activeTab:0,
				defaults:{ui: 'gray',minHeight:45,maxHeight:45,height:45},
				items:tabs
			})
    	);
    	
    	items.push(
			dv = Ext.create('Ext.dataview.DataView',{
				cls:'start-city-view',
				itemCls:'start-city-item big',
				itemTpl:new Ext.XTemplate(
						'{CITY_NAME}'
				)
			})
    	);
    	this.setItems(items);
    	
    	var store = util.createStore(cfg.url.route.city.list,false),
    		currTab = tbs.getActiveTab();
    	store.getProxy().setExtraParams({
    		categoryId:currTab.categoryId
    	});
    	dv.setStore(store);
    	store.load();
    	
    	tbs.on('activeTabchange',this.onTabChange,this);
    	dv.on('itemtap',this.onCityTagTap,this);
    },
    onTabChange:function(tabs,newTab){
    	var store = this.down('dataview').getStore();
    	store.getProxy().setExtraParams({
    		categoryId:newTab.categoryId
    	});
    	store.load();
    },
    onCityTagTap:function(dv,i,t,r){
    	var currTab = this.down('tabbar').getActiveTab();
    	this.setSubmitParams({
    		category:{CATEGORY:currTab.getTitle()},
    		cityName:r.get('CITY_NAME')
    	});
		this.getController().redirectTo('route/category/'+currTab.categoryPid+'_'+encodeURI(r.get('CITY_NAME')));
    }
});