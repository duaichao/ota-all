Ext.define('app.model.b2b.power.model', {
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'string'
    },{
        name: 'moduleId',
        type: 'string'
    },{
        name: 'text',
        type: 'string'
    },{
        name: 'handler',
        type: 'string'
    },{
        name: 'glyph',
        type: 'string'
    },{
        name: 'isUse',
        type: 'int'
    },{
        name: 'iconCls',
        type: 'string'
    },{
        name: 'itemId',
        type: 'string'
    },{
        name: 'xtype',
        type: 'string'
    }]
});