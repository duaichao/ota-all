Ext.define('app.view.b2b.role', {
    extend: 'Ext.container.Viewport',
    layout: 'border',
    items: [{
    	region:'west',
    	style:'border-right:1px solid #c1c1c1!important;',
	    selType:'checkboxmodel',
    	xtype:'gridpanel',
    	itemId:'basegrid',
    	store:util.createGridStore(cfg.getCtx()+cfg.getModuleListUrl()[currViewInPath],Ext.create('app.model.'+currViewInPath+'.model')),
    	columns:Ext.create('app.model.'+currViewInPath+'.column'),
    	dockedItems:[{
    		xtype:'toolbar',
        	items:[{
        		disabled:true,
        		text:'正在初始化...'
        	}]
    	}],
    	width:500,
    	minWidth:300
    },{
    	style:'border-right:1px solid #c1c1c1!important;',
    	region:'center',
    	itemId:'moduletree',
    	xtype:'treepanel',
    	reserveScrollbar: true,
	    useArrows: true,
	    rootVisible: false,
	    multiSelect: true,
	    singleExpand: false,
	    loadMask: true,
	    animate: false,
	    dockedItems:[{
	    	itemId:'moduletool',
    		xtype:'toolbar',
        	items:[{
        		disabled:true,
        		text:'正在初始化...'
        	}]
    	}],
    	store:new Ext.data.TreeStore({
    		autoLoad:false,
            model: Ext.create('app.model.b2b.module.model'),
            proxy: {
                type: 'ajax',
                url: cfg.getCtx()+'/b2b/module/list'
            },
            folderSort: false
        }),
        columns:[{
			xtype: 'treecolumn',
	        text: '模块名称',
	        padding:'1 0 1 0',
	        flex:1,
	        sortable:false,
	        dataIndex: 'text'
	    }],
        root: {  
	        id : -1,
	        text:'系统模块',  
	        expanded: false  
	    }
    },{
    	region:'east',
    	xtype:'treepanel',
    	reserveScrollbar: true,
	    useArrows: true,
	    rootVisible: false,
	    multiSelect: true,
	    singleExpand: false,
	    loadMask: true,
	    animate: false,
	    dockedItems:[{
	    	itemId:'powertool',
    		xtype:'toolbar',
        	items:[{
        		disabled:true,
        		text:'正在初始化...'
        	}]
    	}],
    	store:new Ext.data.TreeStore({
            autoLoad:false,
            model: Ext.create('app.model.b2b.power.model'),
            proxy: {
                type: 'ajax',
                url: cfg.getCtx()+'/b2b/power/list'
            },
            folderSort: false
        }),
        columns:[{
			xtype: 'treecolumn',
	        text: '按钮名称',
	        padding:'1 0 1 0',
	        flex:1,
	        sortable:false,
	        dataIndex: 'text'
	    }],
        root: {  
	        id : -1,
	        text:'按钮',  
	        expanded: true  
	    },  
    	itemId:'powertree',
    	width:300,
    	minWidth:300
    }]
});

