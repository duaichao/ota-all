Ext.define('app.model.resource.route.model', {
    extend: 'Ext.data.Model',
    fields: [
        'ID','NO','FACE', 'TITLE', 'TYPE', 'COMPANY_ID', 'COMPANY_NAME', 'BRAND_NAME',
        'PRODUCE_CONCAT','PRODUCE_MOBILE',
        'CREATE_USER','CREATE_USER_ID',
        'UPDATE_USER','CITY_ID','CITY_NAME',
        'IS_DEL','IS_PUB','SOURCE_URL','IS_SINGLE_PUB','SINGLE_REMARK',
        'IS_GRANT','IS_SHARE','UPDATE_USER_ID',
        'BEGIN_CITY','END_CITY','ATTR','THEMES','ROUTE_TRAFFIC_CNT','BASE_PRICE_CNT',
        'ROUTE_PRICE','ROUTE_INTER_PRICE','SUM_PRICE','SUM_INTER_PRICE','QQ1','QQ2','RQ','ROUTE_WAP_PRICE','IS_OTHER_FEE','IS_EARNEST','EARNEST_INTER',
        'EARNEST_RETAIL','EARNEST_DAY_COUNT','EARNEST_TYPE','WEB_RECOMMEND_CNT',
        {name: 'NOTICE', mapping: 'NOTICE', convert: function(v){
        	v = v ||'';
        	v = v.replace(/\r\n/g,"<br>");
        	if(v.indexOf('<br>')!=-1){
        		v = v.replace(/\n/g,'');
        	}else{
        		v = v.replace(/\n/g,'<br>');
        	}
        	
        	return v;
        }},
        {name: 'FEATURE', mapping: 'FEATURE', convert: function(v){
        	v = v ||'';
        	v = v.replace(/\r\n/g,"<br>");
        	if(v.indexOf('<br>')!=-1){
        		v = v.replace(/\n/g,'');
        	}else{
        		v = v.replace(/\n/g,'<br>');
        	}
        	return v;
        }},
        {name: 'CREATE_TIME', mapping: 'CREATE_TIME', convert: function(value){
        	return util.timeAgoInWords(value);
        }},
        {name: 'UPDATE_TIME', mapping: 'UPDATE_TIME', convert: function(value){
        	return util.timeAgoInWords(value);
        }}
    ]
});