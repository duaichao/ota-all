Ext.define('app.view.b2b.user.agency.totalInfo', {
    extend: 'app.view.b2b.user.totalInfo',
    config:{
	    purview:'agency',
	    isParent:'1',
	    currCompanyId:cfg.getUser().companyId
    },
    initComponent: function() {
    	this.callParent();
    }
});
