Ext.define('app.view.site.area', {
    extend: 'Ext.container.Viewport',
    layout: 'border',
    items: [{
    	region:'west',
    	width:240,
    	dockedItems: [{
	        xtype: 'toolbar',
	        itemId:'toggletool',
	        dock: 'top',
	        items: [{
	        	flex:1,
	        	xtype:'segmentedbutton',
	        	value: '1',
	        	items: [{
	        		value: '1',
	        		glyph:'xe67c@iconfont',
                    text: ' 周边'
                }, {
                	value: '2',
                	glyph:'xe669@iconfont',
                    text: ' 国内'
                }, {
                	value: '3',
                	glyph:'xe666@iconfont',
                    text: ' 出境'
                }]
	        }]
	    },{
	    	xtype:'toolbar',
	    	itemId:'savetool',
	    	items:['->',{
	    		itemId:'add',
	    		tooltip:'添加区域',
	    		glyph:'xe62b@iconfont',
	    		text:''
	    	},{
	    		itemId:'edit',
	    		tooltip:'修改区域',
	    		glyph:'xe62d@iconfont',
	    		text:''
	    	},{
	    		itemId:'del',
	    		tooltip:'删除区域',
	    		glyph:'xe62c@iconfont',
	    		text:''
	    	}]
	    }],
	    hideHeaders:true,
	    selType:'checkboxmodel',
    	xtype:'gridpanel',
    	itemId:'basegrid',
    	columnLine:true,
		selModel :{selType : 'checkboxmodel',mode :'SINGLE'},
    	store:util.createGridStore(cfg.getCtx()+cfg.getModuleListUrl()[currViewInPath],Ext.create('app.model.'+currViewInPath+'.model')),
    	columns:Ext.create('app.model.'+currViewInPath+'.column')
    },{
    	style:'border-left:1px solid #c1c1c1!important',
    	region:'center',
    	xtype:'tabpanel',
    	bodyStyle:'background:transparent!important;',
    	margin:'0 ',
    	plain:true,
    	tabBar:{
			defaults:{height:45},
			style:'padding:5px 10px 0 10px;background:transparent!important;'
		},
    	tabPosition:'top',
    	tabRotation:0
    }]
});

