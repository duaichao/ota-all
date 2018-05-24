Ext.define('app.store.Scenic', {
    extend: 'Ext.data.Store',
    xtype:'citystore',
    model:'app.model.City',
    pageSize:10,
    autoLoad: true, 
    proxy: {
        type: 'ajax',
        noCache:true,
        url: cfg.getCtx()+'/base/listScenic',
        reader: {
            type: 'json',
            rootProperty: 'data',
            totalProperty:'totalSize'
        }
   	}
});
