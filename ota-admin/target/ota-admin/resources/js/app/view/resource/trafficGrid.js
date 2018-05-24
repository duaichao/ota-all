Ext.define('app.view.resource.trafficGrid', {
	extend:'Ext.grid.Panel',
	xtype:'trafficgrid',
	loadMask: true,
	emptyText: '没有找到数据',
	initComponent: function() {
		this.store = util.createGridStore(cfg.getCtx()+cfg.getModuleListUrl()[currViewInPath],Ext.create('app.model.'+currViewInPath+'.model'));
        this.bbar = util.createGridBbar(this.store);
        var columns = Ext.create('app.model.'+currViewInPath+'.column');
        this.columns = columns;
        this.dockedItems = [{
    		xtype:'toolbar',
        	items:[{
        		disabled:true,
        		text:'正在初始化...'
        	}]
    	}];
        this.callParent();
	},
	columnLines: true,
	selType:'checkboxmodel'
});