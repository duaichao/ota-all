Ext.define('app.view.finance.pay', {
    extend: 'Ext.panel.Panel',
    layout:'fit',
    items: [{
    	itemId:'basegrid',
    	xtype:'paygrid'
    }]
});