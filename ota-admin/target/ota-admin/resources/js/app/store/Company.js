Ext.define('app.store.Company', {
    extend: 'Ext.data.Store',
    xtype:'companystore',
    model:'app.model.Company',
    pageSize:20,
    autoLoad: true, 
    proxy: {
        type: 'ajax',
        noCache:true,
        url: cfg.getCtx()+'/site/company/combo',
        reader: {
            type: 'json',
            rootProperty: 'data',
            totalProperty:'totalSize'
        }
   	}
});
