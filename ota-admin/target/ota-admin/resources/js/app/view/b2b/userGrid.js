Ext.define('app.view.b2b.userGrid', {
	extend:'Ext.grid.Panel',
	xtype:'usergrid',
	loadMask: true,
	border:true,
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
	viewConfig:{
		enableTextSelection:true
	},
	columnLines: true,
	selType:'checkboxmodel'
});