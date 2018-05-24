Ext.define('app.view.common.OrderViewWindow', {
    extend: 'Ext.Window',
    config:{
    	paramsStr:null,
    	title:util.windowTitle('&#xe687;','订单详情',''),
    	width:800,
    	height:450,
		modal:true,
		draggable:false,
		resizable:false,
		layout:'fit',
    },
    initComponent: function(){
    	var store = Ext.create('Ext.data.Store', {
            pageSize: 20,
            autoLoad: true,
            model:Ext.create('app.model.finance.calc.model'),
            proxy: {
		         type: 'ajax',
		         noCache:true,
		         url: cfg.getCtx()+'/stat/order',
		         reader: {
		             rootProperty: 'data',
            		 totalProperty: 'totalSize'
		         }
		     }
        });
    	store.getProxy().setExtraParams(Ext.Object.fromQueryString(this.getParamsStr()));
    	var grid = Ext.create('Ext.grid.Panel',{
    		store:store,
    		columns : Ext.create('app.model.finance.calc.orderColumn'),
    		bbar:[Ext.create('Ext.toolbar.Paging', {
    	        displayInfo: true,
    	        store:store,
    	        plugins : [{
    	        	displayText:'每页条数',
    				ptype: 'pagingtoolbarresizer', 
    				options : [ 20, 50 ,100]
    			}]
    		})]
    	});
    	grid.columns[grid.columns.length-1].hide(); 
        this.callParent();
        this.add(grid);
    }
});
