Ext.define('Weidian.view.authentication.AuthBase', {
    extend: 'Weidian.view.Base',
	xtype:'authbase',
	
	
    config:{
    	baseCls: 'auth-locked',
    	layout: {
            type: 'vbox',
            align: 'center',
            pack: 'center'
        }
    }
});
