function showOrder(no){
	no = no.split(' ')[0];
	Ext.create('Ext.window.Window',{
		title:util.windowTitle('&#xe62b;',no),
		width:800,
		height:400,
		bodyPadding:1,
		modal:true,
		draggable:false,
		resizable:false,
		maximizable:false,
		layout:'fit',
		items:[Ext.create('app.view.finance.orderGrid',{accountNo:no})]
	}).show();
}
Ext.define('app.view.b2b.user.agency.refundOrderGrid', {
    extend: 'Ext.grid.Panel',
    config:{
    	columnLines: true,
    	selType:'rowmodel',
    	params:null
    	
    },
    initComponent: function() {
    	this.store = Ext.create('Ext.data.Store', {
            autoLoad: true,
            groupField:'SHOW_GROUP',
            model:Ext.create('Ext.data.Model',{
            	fields: [
            	        'NO','ID','REFUND_AMOUNT','SHOW_GROUP'
            	]
            }),
            proxy: {
		         type: 'ajax',
		         noCache:true,
		         url: cfg.getCtx()+'/b2b/user/refund/order',
		         reader: {
		             rootProperty: 'data',
            		 totalProperty: 'totalSize'
		         }
		     }
        });
    	this.columns=[Ext.create('Ext.grid.RowNumberer',{width:25}),{
    		text: '订单编号',
	        flex:1,
	        dataIndex: 'NO',
	        renderer:function(v,c,r){
	        	var h = [
 	        		'<div class="ht20"><a href="javascript:;">'+v+'</a></div>'
 	        	];
 	        	return h.join('');
	        }
    	},{
    		text: '退款金额',
	        width:100,
	        dataIndex: 'REFUND_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20">'+util.moneyFormat(v,'f14 orange-color')+'</div>'
		        ].join('');
	        }
    	}];
    	
    	this.callParent();
    },
    applyParams:function(params){
    	if(params){
    		Ext.applyIf(params,this.store.getProxy().getExtraParams());
	    	this.store.getProxy().setExtraParams(params);
	    	this.store.load();
    	}
    	return params;
    }
});