Ext.define("Weidian.view.Bar", {
    extend: 'Ext.tab.Bar',
    alternateClassName: 'mainbar',
    xtype:'mainbar',
    activeTab:0,
	docked:'top',
	items:[{
		title: '首页',
		iconCls: 'x-fm fm-home',
		
		action: 'home'
	},{
		title: '目的地',
		iconCls: 'x-fm fm-earth'
	},{
		title: '我的',
		iconCls: 'x-fm fm-account'
	},{
		title: '二维码',
		iconCls: 'x-fm fm-qrcode'
	}]
});