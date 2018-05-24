Ext.define('app.model.site.company.depart', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'ID',
        type: 'string'
    },{
        name: 'TEXT',
        type: 'string'
    },{
        name: 'COMPAY_ID',
        type: 'string'
    },{
        name: 'ORDER_BY',
        type: 'int'
    },{
        name: 'IS_USE',
        type: 'string'
    }]
});