Ext.define('app.model.order.visitor.model', {
    extend: 'Ext.data.Model',
    fields: [
        {name: 'ID'},
        {name: 'NAME'},
        {name: 'MOBILE'},
        {name: 'SEX'},
        {name: 'CARD_TYPE'},
        {name: 'CARD_NO'},
        {name: 'CREATE_TIME'},
        {name: 'EMAIL'},
        {name: 'ATTR_ID'},
        {name: 'ATTR_NAME'},
        {name: 'CONTACT',convert:function(v){return v=='1';}}
    ]
});