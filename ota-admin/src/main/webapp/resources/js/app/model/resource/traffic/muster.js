Ext.define('app.model.resource.traffic.muster', {
    extend: 'Ext.data.Model',
    fields: [
        'ID','MUSTER_TIME', 'MUSTER_PLACE', 
        {name: 'CREATE_TIME', mapping: 'CREATE_TIME', convert: function(value){
        	return util.timeAgoInWords(value);
        }},
        {name: 'UPDATE_TIME', mapping: 'UPDATE_TIME', convert: function(value){
        	return util.timeAgoInWords(value);
        }},'CREATE_USER','UPDATE_USER','CREATE_USER_ID'
    ]
});