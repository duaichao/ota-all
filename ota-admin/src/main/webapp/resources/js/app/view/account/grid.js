Ext.define('app.view.account.grid',{ 
	extend:'Ext.grid.Panel',
	xtype:'accountgrid',
	loadMask: true,
	emptyText: '没有找到数据',
	initComponent: function() {
		this.store = util.createGridStore(cfg.getCtx()+cfg.getModuleListUrl()['account.list']+'?DEPART_ID='+entityId,Ext.create('app.model.account.model'),'1',15);
		this.columns = Ext.create('app.model.account.column');
		this.bbar = util.createGridBbar(this.store);
		this.dockedItems = [{
	    	xtype:'toolbar',
	    	layout: {
                overflowHandler: 'scroller'
            },
	    	itemId:'gridtool',
	    	items:[{
	    		disabled:true,
	    		text:'正在初始化...'
	    	}]
	    }];
		this.callParent();
	},
	columnLines: true,
	selModel :{selType : 'checkboxmodel',mode :'SINGLE'}
});