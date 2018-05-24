Ext.define('app.model.b2b.ad.model', {
    extend: 'Ext.data.Model',
    fields: [
        'ID','MAIN_AREA', 'SUB_AREA', 'TITLE', 'author',
        {name: 'IMGCNT', type: 'int'},
        {name: 'MYCREATETIME', mapping: 'MYCREATETIME', type: 'date', dateFormat: 'timestamp'},
        {name: 'MYUPDATETIME', mapping: 'MYUPDATETIME', type: 'date', dateFormat: 'timestamp'}
    ]
});