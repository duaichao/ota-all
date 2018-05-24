Ext.define('app.view.finance.alone', {
    extend: 'Ext.panel.Panel',
    layout:'border',
    initComponent :function(){
    	this.items = [{
	    	region:'center',
	    	style:'border-right:1px solid #c1c1c1!important;',
	    	itemId:'basegrid',
	    	xtype:'alonegrid'
	    },{
	    	region:'east',
	    	width:550,
	    	itemId:'rechargegrid',
	    	xtype:'rechargegrid'
	    }];
    	this.dockedItems = [{
    		xtype:'toolbar',
    		itemId:'gridtool',
    		style:'border-bottom:1px solid #c1c1c1!important;',
        	items:[{
        		disabled:true,
        		text:'正在初始化...'
        	}]
    	}];
    	this.callParent();
    }
});