Ext.define('app.model.finance.calc.model', {
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
    	'CON_NO','CON_ATTR',
    	'IS_ALONE ',
		'SOURCES',
		'ATTR_URL',
		'SHOW_GROUP',
		'LIST_TYPE',
		'ACCOUNT_COMPANY_ID','PLATFORM_ACCOUNT','COMPANY_PAY_NO','ACCOUNT_DETAIL_NO',
    	{name: 'CON_STEP', type: 'string',convert: function(value){
        	return value+'';
        }},
    	{name: 'PAY_TIME', mapping: 'PAY_TIME', convert: function(value){
        	return util.timeAgoInWords(value);
        }},
    	{name: 'CREATE_TIME', mapping: 'CREATE_TIME', convert: function(value){
        	return util.timeAgoInWords(value);
        }},
    	{name: 'LOST_TIME', mapping: 'LOST_TIME', convert: function(value){
        	return util.timeAgoInWords(value);
        }},
        'TEXT','SALE_AMOUNT','INTER_AMOUNT','REFUND_AMOUNT','MARKETING_AMOUNT','ACTUAL_AMOUNT'
        
    ]
});