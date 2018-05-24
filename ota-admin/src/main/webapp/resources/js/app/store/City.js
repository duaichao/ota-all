Ext.define('app.store.City', {
    extend: 'Ext.data.Store',
    xtype:'citystore',
    model:'app.model.City',
    pageSize:20,
    autoLoad: true, 
    proxy: {
        type: 'ajax',
        noCache:true,
        url: cfg.getCtx()+'/base/listCity',
        reader: {
            type: 'json',
            rootProperty: 'data',
            totalProperty:'totalSize'
        }
   	}
});
