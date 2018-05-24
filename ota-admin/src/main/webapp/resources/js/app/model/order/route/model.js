Ext.define('app.model.order.route.model', {
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
    	'IS_ALONE','OLD_SALE_AMOUNT','AUDIT_INTER_FLOAT',
		'SOURCES','IS_REFUND','OTHER_SUPPLY_AUDIT','OTHER_AMOUNT','RENEW_STATUS','RENEW_STATUS','PLATFORM_PAY','START_CONFIRM',
		'EARNEST_TYPE','EARNEST_DAY_COUNT','IS_EARNEST','EARNEST_INTER','EARNEST_INTER_AMOUNT','EARNEST_RETAIL_AMOUNT','EARNEST_RETAIL',
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
    ]
});