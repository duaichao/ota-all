Ext.define('app.view.b2b.module', {
    extend: 'Ext.tree.Panel',
    xtype: 'module',
    reserveScrollbar: true,
    useArrows: true,
    rootVisible: false,
    multiSelect: true,
    singleExpand: false,
    loadMask: true,
    animate: false,
    bodyStyle:'background:transparent;',
    style:'background:transparent;',
    viewConfig: {
    	bodyStyle:'background:transparent;',
    	style:'background:transparent;'
    },
    initComponent: function() {
    	Ext.apply(this, {
            store: new Ext.data.TreeStore({
                model: Ext.create('app.model.'+currViewInPath+'.model'),
                proxy: {
                    type: 'ajax',
                    url: cfg.getCtx()+cfg.getModuleListUrl()[currViewInPath]
                },
                folderSort: false
            }),
            columns:Ext.create('app.model.'+currViewInPath+'.column'),
            root: {  
		        id : -1,
		        text:'系统模块',  
		        expanded: false  
		    }  
        });
    	this.callParent();
    }
});