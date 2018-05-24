Ext.define('app.view.b2b.user.admin.waitDo', {
    extend: 'app.view.b2b.user.waitDo',
    initComponent: function() {
    	var me = this;
    	this.callParent();
    	me.add({xtype:'tbfill'});
    }
});
