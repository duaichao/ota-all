Ext.define('app.view.finance.calc', {
    extend: 'Ext.panel.Panel',
    layout:'fit',
    items: [{
    	itemId:'basegrid',
    	xtype:'calcgrid'
    }]
});