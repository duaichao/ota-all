Ext.define('Weidian.view.profile.Social', {
    extend: 'Weidian.view.Base',
    xtype: 'profilesocial',
    
    
    config:{
    	cls:'user-profile',
    	scrollable:false,
        layout: {
            type: 'vbox',
            align: 'middle'
        }
    },
    
    initialize: function(){
    	this.initViews();
    	this.callParent();
    },
    initViews:function(){
    	var items = [];
    	
    	this.setItems(items);
    }
    
});
