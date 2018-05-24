Ext.define('app.model.b2b.sms.model', {
    extend: 'Ext.data.Model',
    fields: [
        'ID','MOBILE', 'CONTENT', 'STATUS','CREATE_USER','TYPE','SMS_TYPE','RESOURCES','COMPANY','CNT',
        {name: 'CREATE_TIME', mapping: 'CREATE_TIME', convert: function(value){
        	return util.timeAgoInWords(value);
        }
        }
    ]
});