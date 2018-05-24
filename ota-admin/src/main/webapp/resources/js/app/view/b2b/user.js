Ext.define('app.view.b2b.user', {
    extend: 'Ext.container.Viewport',
    layout: 'border',
    items: [{
    	region:'east',
	    selType:'checkboxmodel',
	    selModel:{
	    	mode:'SINGLE'
	    },
    	xtype:'gridpanel',
    	itemId:'rolegrid',
    	store:util.createGridStore(cfg.getCtx()+'/b2b/role/list?isUse=0',Ext.create('app.model.b2b.role.model')),
    	columns:[{
	        text: '角色',
	        flex:1,
	        padding:'1 0 1 0',
	        dataIndex: 'ROLE_NAME'
	    }],
	    dockedItems:[{
	    	itemId:'roletool',
    		xtype:'toolbar'
    	}],
    	width:200
    },{
    	region:'center',
    	itemId:'basegrid',
    	border:false,
    	style:'border-right:1px solid #c1c1c1!important;',
    	xtype:'usergrid'
    },{
    	region:'south',
    	itemId:'condit',
    	borderWidth:2,
    	height:200,
    	collapseMode:'mini',
    	collapsed:true,
    	hidden:true
    }]
});

