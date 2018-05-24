Ext.define('app.view.b2b.ad.condit', {
    extend: 'app.view.common.BaseCondit',
    initComponent: function() {
    	this.items = [{
    		columnWidth: 0.3,
    		items:[{
				fieldLabel: '查询'
			}]
    	},{
    		columnWidth: 0.4,
    		items:[{
				fieldLabel: '查询'
			}]
    	},{
    		columnWidth: 0.3,
    		items:[{
				fieldLabel: '查询'
			}]
    	}];
    	this.callParent();
    }
});