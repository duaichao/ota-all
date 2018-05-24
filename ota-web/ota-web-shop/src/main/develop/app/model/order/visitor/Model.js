Ext.define('Weidian.model.order.visitor.Model', {
    extend: 'Ext.data.Model',
    config:{
    	fields: [
    	     'id',
		     {name: 'ID'},
		     {name: 'NAME'},
		     {name: 'MOBILE'},
		     {name: 'SEX'},
		     {name: 'CARD_TYPE'},
		     {name: 'CARD_NO'},
		     {name: 'EMAIL'},
		     {name: 'ATTR_ID'},
		     {name: 'ATTR_NAME'},
		     {name: 'CONTACT',convert:function(v){return v=='1';}}
		 ]
    }
});