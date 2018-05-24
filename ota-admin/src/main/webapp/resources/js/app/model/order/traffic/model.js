Ext.define('app.model.order.traffic.model', {
    extend: 'Ext.data.Model',
    fields: [
    	'ID','NO','PRODUCE_ID','TRAFFIC_ID','PRODUCE_TYPE','COMPANY_ID','COMPANY_NAME','PORDUCE_CONCAT',
    	'PRODUCE_MOBILE','START_DATE','MUSTER_TIME','MUSTER_PLACE','VISITOR_CONCAT','VISITOR_MOBILE',
    	'MAN_COUNT','CHILD_COUNT','STATUS','SALE_AMOUNT','INTER_AMOUNT','PAY_TYPE','ACCOUNT_STATUS','CREATE_USER',
    	'CREATE_USER_ID','CREATE_COMPANY_ID','CREATE_DEPART_ID','SITE_RELA_ID','IS_LOST',
    	'VISITOR_EMAIL','RULE_ID',
    	'BUY_COMPANY',
    	'BUY_PHONE',
    	'BUY_USER_NAME',
    	'IS_ALONE ',
		'SOURCES',
    	{name: 'PAY_TIME', mapping: 'PAY_TIME', convert: function(value){
        	return util.timeAgoInWords(value);
        }},
    	{name: 'CREATE_TIME', mapping: 'CREATE_TIME', convert: function(value){
        	return util.timeAgoInWords(value);
        }},
    	{name: 'LOST_TIME', mapping: 'LOST_TIME', convert: function(value){
        	return util.timeAgoInWords(value);
        }},
    ]
});