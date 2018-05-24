Ext.define('app.view.common.BaseCondit', {
    extend: 'Ext.form.Panel',
    layout: {
       	type:'vbox'
	},
    region:'north',
    border:false,
    bodyPadding:5,
    bodyStyle:'background:#dfeaf2;',
    itemId:'baseCondit',
    cls:'condit-form',
    style:'border-top:none;',
    hidden:true,
    fieldDefaults: {
        labelAlign: 'right',
        labelWidth: 60,
        msgTarget: 'side'
    },
    defaults: {
        layout: {
        	type:'hbox'
        },
        xtype: 'container',
        defaultType: 'textfield'
    },initComponent: function() {
    	var me = this;
    	this.dockedItems = [{
        	xtype:'toolbar',
        	style:'background:#dfeaf2;',
        	dock:'right',
        	items:[{
	    		itemId:'conditQueryBtn',
	    		glyph:'xe61c@iconfont',
	    		text:'查询',
	    		minWidth:45
	    	},{
	    		itemId:'conditResetBtn',
	    		text:'重置',
	    		glyph:'xe63d@iconfont',
	    		cls:'disable',
	    		minWidth:45
	    	}]
        }];
    	this.callParent();
    }
});