Ext.define('app.view.site.departGrid', {
	extend:'Ext.grid.Panel',
	xtype:'departgrid',
	loadMask: true,
	emptyText: '没有找到数据',
	initComponent: function() {
		this.store = util.createGridStore(cfg.getCtx()+'/site/depart/list',Ext.create('app.model.site.company.depart'),'0');
        var columns = Ext.create('app.model.site.company.departColumn');
        this.columns = columns;
        this.dockedItems = [{
	    	xtype:'toolbar',
	    	layout: {
                overflowHandler: 'scroller'
            },
	    	itemId:'departtool',
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