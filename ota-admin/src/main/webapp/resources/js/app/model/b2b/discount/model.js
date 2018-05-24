Ext.define('app.model.b2b.discount.model', {
    extend: 'Ext.data.Model',
    fields: [
        'ID','TITLE', {
        	name: 'IS_USE', 
        	type: 'int'
        	
        }, {
        	name: 'IS_REFUND', 
        	type: 'int'
        } , 'BEGIN_TIME','END_TIME',
        'SITE_RELE_ID', {
        	name: 'PRO_TYPE', 
        	type: 'int'
        },'REMARK','CREATE_USER',
        'CREATE_TIME','CREATE_USER_ID','CITY_NAME'
    ]
});