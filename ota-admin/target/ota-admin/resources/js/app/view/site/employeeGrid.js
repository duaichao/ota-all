Ext.define('app.view.site.employeeGrid', {
	extend:'Ext.grid.Panel',
	xtype:'employeegrid',
	loadMask: true,
	emptyText: '没有找到数据',
	initComponent: function() {
		this.store = util.createGridStore(cfg.getCtx()+'/b2b/user/list?departType=1',Ext.create('app.model.b2b.user.model'),'0');
        this.bbar = util.createGridBbar(this.store);
        var columns = Ext.create('app.model.site.company.employeeColumn');
        columns.splice(2,0,{
	        text: '角色',
	        tdCls:'pt-4',
	        width:100,
	        dataIndex: 'CHILD_ROLE_NAME',
	        renderer: function(v,c,r){
	        	return v;
	        }
	    });
        this.columns = columns;
        this.dockedItems = [{
	    	xtype:'toolbar',
	    	itemId:'employeetool',
	    	overflowHandler: 'menu',
	    	items:[{
	    		disabled:true,
	    		text:'正在初始化...'
	    	}]
	    }];
        this.callParent();
	},
	columnLines: true,
	viewConfig:{
		enableTextSelection:true
	},
	selModel :{selType : 'checkboxmodel',mode :'SINGLE'}
});