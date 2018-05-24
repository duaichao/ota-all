Ext.define('Weidian.model.product.Route', {
    extend: 'Weidian.model.Base',
    config:{
	    fields: [
	        'ID','NO','DAY_COUNT', 'TITLE', 'TYPE','BEGIN_CITY','END_CITY','ATTR','THEMES','ROUTE_TRAFFIC_CNT','BASE_PRICE_CNT',
	        'ROUTE_PRICE','ROUTE_INTER_PRICE','SUM_INTER_PRICE','SUM_PRICE','RQ',
	        {name:'lazyloaded',type:'boolean',defaultValue:false},
	        {name:'id',type:'string',mapping:'ID'},
	        {name:'FACE',
	        	mapping:'FACE',convert:function(v){
	        		return Ext.imgDomain+v;
	        	}
	        },
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
	        }}
	    ]
    }
});