Ext.define('app.view.b2c.category', {
    extend: 'Ext.container.Viewport',
    layout: 'border',
    items: [{
    	region:'west',
	    selType:'checkboxmodel',
	    selModel:{
	    	mode:'MULTI'
	    },
	    itemId:'categoryGrid',
    	xtype:'gridpanel',
    	store:util.createGridStore(cfg.getCtx()+'/b2c/category/list',Ext.create('app.model.b2c.category.model')),
    	columns:[{
	        text: '排序',
	        width:60,
	        padding:'1 0 1 0',
	        dataIndex: 'ORDER_BY',
	        
	    },{
	        text: '分类名称',
	        flex:1,
	        padding:'1 0 1 0',
	        dataIndex: 'CATEGORY',
	        renderer: function(v,metaData,r,colIndex,store,view){
	        	return '<div class="">'+v+'</div>';
	        }
	    }],
	    dockedItems:[{
	    	itemId:'categoryTool',
    		xtype:'toolbar'
    	}],
    	width:240
    },{
    	region:'center',
    	border:false,
    	style:'border-left:1px solid #c1c1c1!important;',
    	itemId:'basegrid',
    	xtype:'companyWapTJGrid'
    }]
});

