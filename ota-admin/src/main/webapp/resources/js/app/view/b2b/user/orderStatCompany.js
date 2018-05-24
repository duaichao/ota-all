Ext.define('app.view.b2b.user.orderStatCompany', {
    extend: 'Ext.grid.Panel',
    xtype:'orderStatCompany',
    config:{
    	columnLines: true,
    	selType:'rowmodel',
    	isSupply:0,
    	params:null
    },
    initComponent: function() {
    	this.store = Ext.create('Ext.data.Store', {
            autoLoad: false,
            pageSize:20,
            model:Ext.create('Ext.data.Model',{
            	fields: [
            	         'SALE_AMOUNT','INTER_AMOUNT','PROFIT_AMOUNT','CNT','PERSON_COUNT','MAN_COUNT','CHILD_COUNT','PRODUCE_CNT','COMPANY_NAME'
            	]
            }),
            proxy: {
		         type: 'ajax',
		         noCache:true,
		         url: cfg.getCtx()+'/stat/sale/order/company',
		         reader: {
		             rootProperty: 'data',
            		 totalProperty: 'totalSize'
		         }
		     }
        });
    	this.bbar = util.createGridBbar(this.store);
    	var isSupply = this.getIsSupply()==1;
    	
    	this.columns=[{
    		text: isSupply?'供应商':'旅行社',
	        width:240,
	        dataIndex: 'COMPANY_NAME',
	        renderer:function(v,c,r){
	        	var h = [
 	        		'<div class="ht20 f14">'+v+'</div>'
 	        	];
 	        	return h.join('');
	        }
    	},{
    		text: '订单数',
    		width:80,
	        dataIndex: 'CNT',
	        renderer:function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+v+'</div>'
		        ].join('');
	        }
    	},{
    		text: '外卖金额',
	        width:150,
	        hidden:isSupply,
	        dataIndex: 'SALE_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+v+'</div>'
		        ].join('');
	        }
    	},{
    		text: '结算金额',
    		width:150,
	        dataIndex: 'INTER_AMOUNT',
	        renderer:function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+v+'</div>'
		        ].join('');
	        }
    	},{
    		text: '旅行社利润',
	        width:150,
	        hidden:isSupply,
	        dataIndex: 'PROFIT_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+v+'</a></div>'
		        ].join('');
	        }
    	},{
    		text: '人数',
	        width:80,
	        dataIndex: 'PERSON_COUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+v+'</a></div>'
		        ].join('');
	        }
    	},{
    		text: '成人',
	        width:80,
	        dataIndex: 'MAN_COUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+v+'</a></div>'
		        ].join('');
	        }
    	},{
    		text: '儿童',
	        width:80,
	        dataIndex: 'CHILD_COUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+v+'</a></div>'
		        ].join('');
	        }
    	},{
    		text: '产品数',
	        width:80,
	        dataIndex: 'PRODUCE_CNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+v+'</a></div>'
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