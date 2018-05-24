Ext.define('app.model.b2c.visitor.model', {
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
        {name: 'ATTR_NAME'},'PLAT_FROM','USER_NAME','ORDER_NO','PRODUCE_NAME','BIRTHDAY','BUY_COMPANY','START_DATE','PAY_TIME','ATTR_NAME',
        {name: 'CONTACT',convert:function(v){return v=='1';}}
    ]
});