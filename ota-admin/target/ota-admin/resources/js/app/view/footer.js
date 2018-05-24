Ext.define('app.view.footer', {
    extend: 'Ext.Container',
    xtype: 'appFooter',
    id: 'app-footer',
    layout: {
        type: 'hbox',
        align: 'middle'
    },
    initComponent: function() {
    	this.items=[{
    		border:false,
    		padding:'0 5 0 0',
    		bodyStyle:'background:transparent;text-align:center;',
    		flex: 1,
    		html:'<div style="color:#fff;">Copyright © 2012-2017 All Rights Reserved 琼ICP备16003588号 版权所有 </div>'
    	}];
        this.callParent();
    }
});