Ext.define('app.model.finance.pay.model', {
    extend: 'Ext.data.Model',
    fields: [
    	'ID','DETAIL_NO','NO','ACCOUNT_ID','BANK_ID','BANK_NAME','BANK_TITLE','BANK_ADDRESS',
    	'COMPANY_ID','SITE_RELA_ID','ACCOUNT_AMOUNT','ATTR_URL','ATTR_USER','ATTR_USER_ID',
    	'ATTR_TIME','ENTITY_TYPE','TYPE','COMPANY_TYPE','ACCOUNT_USER','ACCOUNT_USER_ID','ACCOUNT_TIME','PAY_USER',
    	'PAY_USER_ID','PAY_TIME','COMPANY',
    	{name: 'BANK_NO', mapping: 'BANK_NO', convert: function(val){
    		val = val ||'';
        	return '<span class="f18 blue-color">'+val.replace(/\s/g,'').replace(/(\d{4})(?=\d)/g,"$1 ")+'</span>';  
        }},
    	{name: 'ACCOUNT_TIME', mapping: 'ACCOUNT_TIME', convert: function(value){
        	return util.timeAgoInWords(value);
        }}
        
    ]
});