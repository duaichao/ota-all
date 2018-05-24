Ext.define('app.model.site.area.model', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'ID',
        type: 'string'
    },{
        name: 'TYPE',
        type: 'string'
    },{
        name: 'TEXT',
        type: 'string'
    },{
        name: 'GLYPH',
        type: 'string'
    },{
        name: 'IS_USE',
        type: 'int'
    },{
        name: 'ICONCLS',
        type: 'string'
    },{
        name: 'ORDER_BY',
        type: 'string'
    }]
});