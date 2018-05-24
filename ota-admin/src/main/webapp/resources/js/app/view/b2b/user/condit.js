Ext.define('app.view.b2b.user.condit', {
    extend: 'app.view.common.BaseCondit',
    hidden:true,
    initComponent: function() {
    	this.items = [{
    		columnWidth: 0.3,
    		items:[{
				fieldLabel: '查询'
			}]
    	}];
    	this.callParent();
    }
});