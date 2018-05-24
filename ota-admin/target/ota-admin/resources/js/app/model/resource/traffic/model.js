Ext.define('app.model.resource.traffic.model', {
    extend: 'Ext.data.Model',
    fields: [
        'ID','TRAFFIC_ID','TITLE', 'END_DATE', 'END_TIME', 'IS_SALE', 'SALE_STATUS', 'MIN_BUY', 'TYPE', 'IS_SINGLE','CITY_ID','CITY_NAME','IS_PUB','MIN_DATE','MIN_RULE_ID',
        'START_CITY','END_CITY','BEGIN_STATION','END_STATION','ROUTE_TRAFFIC_CNT',
        {name: 'ROTE', mapping: 'ROTE', convert: function(v){
        	v = v ||'';
        	v = v.replace(/\r\n/g,"<br>");
        	if(v.indexOf('<br>')!=-1){
        		v = v.replace(/\n/g,'');
        	}else{
        		v = v.replace(/\n/g,'<br>');
        	}
        	
        	return v;
        }},
        {name: 'MIN_PRICE', convert: function(value){
        	return util.format.number(value,'0.00');
        }},
        {name: 'CREATE_TIME', mapping: 'CREATE_TIME', convert: function(value){
        	return util.timeAgoInWords(value);
        }},
        {name: 'UPDATE_TIME', mapping: 'UPDATE_TIME', convert: function(value){
        	return util.timeAgoInWords(value);
        }},
        {name: 'PUB_TIME', mapping: 'PUB_TIME', convert: function(value){
        	return util.timeAgoInWords(value);
        }},'PUB_USER','CREATE_USER','UPDATE_USER','CREATE_USER_ID','CHINA_NAME','MOBILE','COMPANY'
    ]
});