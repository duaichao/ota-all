Ext.define('app.model.b2b.user.model', {
    extend: 'Ext.data.Model',
    fields: [
        'ID','CHINA_NAME', 'BIRTHDAY', 'USER_NAME', 'EMAIL', 'MOBILE', 'FACE', 'SCORE', 'USER_LEVEL',
        {
        	name: 'IS_USE', 
        	type: 'int'
	    },
	    {
        	name: 'USER_TYPE', 
        	type: 'string',
	        convert: function(value){
	        	if(Ext.String.startsWith(value,'01')){
	        		return '管理员';
	        	}
	        	if(Ext.String.startsWith(value,'02')){
	        		return '供应商';
	        	}
	        	if(Ext.String.startsWith(value,'03')){
	        		return '旅行社';
	        	}
	        	if(Ext.String.startsWith(value,'0101')){
	        		return '管理员子账号';
	        	}
	        	if(Ext.String.startsWith(value,'0201')){
	        		return '供应商子账号';
	        	}
	        	if(Ext.String.startsWith(value,'0301')){
	        		return '旅行社子账号';
	        	}
	        }
	    },
        {name: 'SEX', type: 'int'},
        'LOGIN_IP',
        {name: 'LOGIN_TIME', mapping: 'LOGIN_TIME', convert: function(value){
        	return util.timeAgoInWords(value);
        }},
        {name: 'CREATE_TIME', mapping: 'CREATE_TIME', convert: function(value){
        	return util.timeAgoInWords(value);
        }},
        {name: 'UPDATE_TIME', mapping: 'UPDATE_TIME', convert: function(value){
        	return util.timeAgoInWords(value);
        }},'CREATE_USER','UPDATE_USER','CITY_ID','CITY_NAME','ID_CARD','PHONE','QQ1','QQ2','COMPANY_ID','DEPART_ID','DEPART_MANAGER','FAX','TEXT',
        'COMPANY','PARENT_COMPANY','CITY_NAME','CHILD_ROLE_NAME','CHILD_ROLE_ID'
    ]
});