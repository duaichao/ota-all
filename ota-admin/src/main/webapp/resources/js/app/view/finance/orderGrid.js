Ext.define('app.view.finance.orderGrid', {
	extend:'Ext.grid.Panel',
	xtype:'ordregrid',
	loadMask: true,
	accountNo:'',
	initComponent: function() {
		var me = this;
		this.store = Ext.create('Ext.data.Store', {
            pageSize: 50,
            autoLoad: true,
            model:Ext.create('app.model.finance.calc.model'),
            proxy: {
		         type: 'ajax',
		         noCache:true,
		         url: cfg.getCtx()+'/finance/calc/list?accountNo='+me.accountNo,
		         reader: {
		             rootProperty: 'data',
            		 totalProperty: 'totalSize'
		         }
		     }
        });
        this.columns = Ext.create('app.model.finance.calc.orderColumn');
        this.callParent();
	},
	columnLines: true
});