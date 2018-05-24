Ext.define('app.view.finance.query', {
    extend: 'Ext.panel.Panel',
    layout:'fit',
    items: [{
    	itemId:'basegrid',
    	xtype:'querygrid'
    }]
});