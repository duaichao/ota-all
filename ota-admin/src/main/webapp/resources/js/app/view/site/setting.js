Ext.define('app.view.site.setting', {
    extend: 'Ext.container.Viewport',
    layout: 'border',
    items: [{
    	margin:'10',
    	border:true,
    	region:'north',
    	height:200,
    	itemId:'basegrid',
    	xtype:'settingcompanygrid'
    	
    },{
    	region:'west',
    	width:140,
    	margin:'0 10 10 10',
    	itemId:'cityview',
    	//autoScroll:true,
    	cls: 'binding-selection-view',
        itemSelector: '.customer',
        xtype: 'dataview',
        overItemCls: 'hover',  
        trackOver:true,
        store:util.createGridStore(cfg.getCtx()+'/site/company/ownerSites',Ext.create('app.model.site.company.city')),
        tpl: [
            '<div style="background:#427fed;height:34px;color:#fff;line-height:34px;padding-left:5px;font-weight:bold;font-size:14px;">站内城市</div>',
            '<div style="overflow:auto;height:90%;padding-bottom:5px;">',
        	'<tpl for="."><div class="customer" cityId="{CITY_ID}" cityName="{SUB_AREA}">{SUB_AREA}',
        	'<tpl if="IS_USE == 1">',
        	'<i class="icon-eye-off" data-qtip="已禁用"></i>',
        	'</tpl>',
        	'</div></tpl></div>']
    },{
    	region:'center',
    	xtype:'tabpanel',
    	tabPosition:'top',
    	cls:'tabTTools',
    	tabRotation:0,
    	bodyStyle:'background:transparent!important;',
    	margin:'0 10 10 0',
    	plain:true,
    	tabBar:{
			defaults:{height:45},
			style:'padding:5px 10px 0 10px;background:transparent!important;'
		},
    	tbar:[{
    		itemId:'initBtn',
	    	disabled:true,
	    	text:'正在初始化...'
	    }],
    	items:[{
    		title:'旅行社',
    		xtype:'settingchildcompanygrid',
    		itemId:'retialgrid'
    	},{
    		title:'供应商',
    		xtype:'settingchildcompanygrid',
    		itemId:'supplygrid'
    	}]
    }]
});

