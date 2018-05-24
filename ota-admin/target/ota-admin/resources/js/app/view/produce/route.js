Ext.define('app.view.produce.route', {
    extend: 'Ext.panel.Panel',
    layout:'fit',
    style:'background:transparent;',
    bodyStyle:'background:transparent;',
    items: [{
    	itemId:'basegrid',
    	xtype:'proroutegrid'
    }]
});

