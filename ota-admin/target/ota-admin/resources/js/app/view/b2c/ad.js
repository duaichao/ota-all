Ext.define('app.view.b2c.ad', {
	extend:'Ext.grid.Panel',
	config:{
		loadMask: true,
		emptyText: '没有找到数据',
		itemId:'basegrid'
	},
	initComponent: function() {
		var me = this;
		this.store = util.createGridStore(cfg.getCtx()+'/site/company/list/ad',Ext.create('app.model.b2b.ad.model'));
        var columns = Ext.create('app.model.b2b.ad.column');
        
        columns[3] = Ext.apply(columns[3],{
        	text: '类型',
	        width:120,
	        dataIndex: 'TYPE',
	        renderer:function(value,metaData,record,colIndex,store,view){
	        	var a = ['','焦点图','主题推荐'];
            	return a[value];  
	        }
        });
        this.columns = columns;
        this.dockedItems = [{
        	xtype:'toolbar',
        	style:'padding-left:5px;',
            overflowHandler: 'menu',
        	items:[{
        		disabled:true,
        		text:'正在初始化...'
        	}]
        }];
		this.callParent();
	},
	columnLines: true,
	selType:'checkboxmodel'
});