Ext.define('app.view.site.areachild', {
    extend: 'Ext.panel.Panel',
    xtype: 'areachild',
    autoScroll: true,
    bodyPadding: '10',
    layout: {
        type: 'vbox',
        pack: 'start',
        align: 'stretch'
    },
    defaults:{
    	border:true,
    	layout:'hbox',
    	bodyStyle:'background:transparent;'
    },
    initComponent: function() {
    	this.items=[{
    		itemId:'citypanel',
    		flex:1,
    		items:[{
    			dockedItems:{
    				itemId:'citytools',
    				style:'background:rgba(225,225,225,.5)!important;',
    				padding:'8 10',
	    			xtype:'toolbar',
	    			items:[{
	    				xtype:'label',
	    				cls:'f14',
	    				text:'目的地'
	    			},'->',{
	    				itemId:'addCityLabel',
	    				glyph:'xe631@iconfont',
	    				text:''
	    			}]
	    		},
    			flex:1,
    			itemId:'cityitems',
    			bodyPadding:'10',
    			layout:'column',
    	    	bodyStyle:'background:transparent;'
    		}]
    	},{
    		itemId:'scenicpanel',
    		flex:1,
    		margin:'10 0 0 0',
    		dockedItems:{
    			style:'background:rgba(225,225,225,.5)!important;',
    			itemId:'scenictool',
    			xtype:'toolbar',
    			items:[{
    				xtype:'label',
    				cls:'f14',
    				text:'景点'
    			},'->',{
    				glyph:'xe631@iconfont',
    				text:''
    			}]
    		}
    	}];
    	/*this.dockedItems = {
	    	xtype:'toolbar',
	    	items:[Ext.create('app.view.common.CityCombo',{
				width:200
	    	}),{text:'添加'}]
	    };*/
        this.callParent();
    }
});