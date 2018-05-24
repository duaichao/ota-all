Ext.define('app.view.resource.traffic', {
    extend: 'Ext.container.Viewport',
    layout: 'border',
    items: [{
    	region:'east',
    	margin:'0 1 1 0',
	    selType:'checkboxmodel',
	    selModel:{
	    	mode:'SINGLE'
	    },
    	xtype:'gridpanel',
    	itemId:'mustergrid',
    	store:util.createGridStore(cfg.getCtx()+'/resource/traffic/muster',Ext.create('app.model.resource.traffic.muster')),
    	columns:[{
	        text: '集合时间',
	        flex:1,
	        padding:'1 0 1 0',
	        dataIndex: 'MUSTER_TIME'
	    },{
	        text: '集合地点',
	        width:140,
	        padding:'1 0 1 0',
	        dataIndex: 'MUSTER_PLACE',
	        renderer: function(v,metaData,r,colIndex,store,view){
	        	return '<div style="width:130px;white-space:normal;display:inline-block;">'+v+'</div>';
	        }
	    }],
	    dockedItems:[{
	    	itemId:'mustertool',
    		xtype:'toolbar'
    	}],
    	width:240
    },{
    	region:'center',
    	style:'border-right:1px solid #d1d1d1!important',
    	margin:'0 1 1 1',
    	itemId:'basegrid',
    	xtype:'trafficgrid'
    }]
});

