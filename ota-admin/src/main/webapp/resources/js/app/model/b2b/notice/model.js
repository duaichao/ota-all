Ext.define('app.model.b2b.notice.model', {
    extend: 'Ext.data.Model',
    fields: [
        'ID','SITE_ID','TITLE','IS_TOP','CONTENT', 'TYPE','CREATE_USER','SUB_AREA',
        {name: 'CREATE_TIME', mapping: 'CREATE_TIME', convert: function(value){
        	return util.timeAgoInWords(value);
        }
        },'UPDATE_USER',
        {name: 'UPDATE_TIME', mapping: 'UPDATE_TIME', convert: function(value){
        	return util.timeAgoInWords(value);
        }
        }
    ]
});