Ext.define('app.view.b2b.user.routeStatSupplys', {
    extend: 'Ext.grid.Panel',
    xtype:'routeStatSupplys',
    config:{
    	columnLines: true,
    	selType:'rowmodel',
    	params:null
    },
    initComponent: function() {
    	this.store = Ext.create('Ext.data.Store', {
            autoLoad: false,
            model:Ext.create('Ext.data.Model',{
            	fields: [
            	        'SUPPLY_ID','SUPPLY_NAME','PHONE','PRODUCT_CNT'
            	]
            }),
            proxy: {
		         type: 'ajax',
		         noCache:true,
		         url: cfg.getCtx()+'/stat/sale/route/supply',
		         reader: {
		             rootProperty: 'data',
            		 totalProperty: 'totalSize'
		         }
		     }
        });
    	this.columns=[{
    		text: '供应商',
	        flex:1,
	        dataIndex: 'SUPPLY_NAME',
	        renderer:function(v,c,r){
	        	var h = [
 	        		'<div class="ht20 f14">'+v+'</div>'
 	        	];
 	        	return h.join('');
	        }
    	},{
    		text: '联系方式',
    		width:120,
	        dataIndex: 'PHONE',
	        renderer:function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+v+'</div>'
		        ].join('');
	        }
    	},{
    		text: '产品数',
    		align:'center',
	        width:60,
	        dataIndex: 'PRODUCT_CNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<a class="ht20 f14 blue-color hand">'+v+'</a>'
		        ].join('');
	        }
    	}];
    	
    	this.callParent();
    },
    applyParams:function(params){
    	if(params){
	    	this.store.getProxy().setExtraParams(params);
	    	this.store.load();
    	}
    	return params;
    }
});