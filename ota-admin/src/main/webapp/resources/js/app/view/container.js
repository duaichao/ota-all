Ext.define('app.view.container', {
    extend: 'Ext.panel.Panel',
    xtype: 'appContainer',
    id: 'app-container',
    header: {
        hidden: true
    },
    layout: {
        type: 'fit'
    },
    autoScroll:false,
    initComponent: function() {
        this.callParent();
    }
});