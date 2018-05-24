Ext.define('app.view.site.settingCompanyGrid', {
	extend:'Ext.grid.Panel',
	xtype:'settingcompanygrid',
	loadMask: true,
	emptyText: '没有找到数据',
	initComponent: function() {
		this.store = util.createGridStore(cfg.getCtx()+'/site/company/list',Ext.create('app.model.site.company.model'));
        var columns = Ext.create('app.model.site.company.column');
 		columns.splice(columns.length-1,0,{
	        text: '管理城市',
	        width:240,
	        dataIndex: 'CITY',
	        renderer:function(value,metaData,record,colIndex,store,view){
	        	metaData.tdAttr = 'data-qtip="' + value + '"';  
            	return value;  
	        }
	    });
        this.columns = columns;
        this.dockedItems = [{
	    	xtype:'toolbar',
	    	style:'background:transparent;',
	    	itemId:'firsttool',
	    	items:[{
	    		disabled:true,
	    		text:'正在初始化...'
	    	}]
	    }];
        this.callParent();
	},
	viewConfig:{
		enableTextSelection:true
	},
	columnLines: true,
	selModel :{selType : 'checkboxmodel',mode :'SINGLE'}
});