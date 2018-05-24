Ext.define('app.view.common.BaseView', {
    extend: 'Ext.container.Viewport',
    layout: 'border',
    requires: [
        'app.view.common.BaseGrid'
    ],
    initComponent: function() {
    	var conditView = util.getModuleAutoCondit(currViewInPath);
    	this.items = [];
    	if(conditView){
    		this.items.push(conditView);
    	}
    	this.items.push({
    		region:'center',
    		xtype:'basegrid',
    		itemId:'basegrid'
    	});
    	this.callParent();
    }
});
