Ext.define('app.model.b2b.module.model', {
    extend: 'Ext.data.TreeModel',
    fields: [{
        name: 'id',
        type: 'string'
    },{
        name: 'parentId',
        type: 'string'
    },{
        name: 'text',
        type: 'string'
    },{
        name: 'href',
        type: 'string'
    },{
        name: 'leaf',
        type: 'boolean',
        convert: function(value){
        	return value=='1';
        }
    },{
        name: 'expanded',
        type: 'boolean'
    },{
        name: 'glyph',
        type: 'string'
    },{
        name: 'isUse',
        type: 'int'
    },{
        name: 'iconCls',
        type: 'string'
    }]
});