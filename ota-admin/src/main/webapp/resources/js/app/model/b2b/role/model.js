Ext.define('app.model.b2b.role.model', {
    extend: 'Ext.data.Model',
    fields: [
        'ID','ROLE_NAME','ROLE_TYPE',
        {
        	name: 'IS_USE', 
        	type: 'int'
	    }
    ]
});