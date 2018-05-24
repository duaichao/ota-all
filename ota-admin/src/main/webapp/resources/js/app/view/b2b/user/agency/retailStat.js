function showOrderStatDetail(isSupply,ps){
	ps = ps ||{};
	if(Ext.isString(ps)){
		ps = ps.replace(/_/g,'"');
		ps = Ext.decode(ps);
	}
	ps.ctype = isSupply;
	var detail = Ext.create('app.view.b2b.user.orderStatCompany',{
		isSupply:isSupply
	});
	var win = Ext.create('Ext.window.Window',{
		title:util.windowTitle('&#xe635;','供应商',''),
		width:800,
		height:400,
		modal:true,
		draggable:false,
		resizable:false,
			layout:'fit',
		items:[detail]
	}).show();
	detail.getStore().getProxy().setUrl(cfg.getCtx()+'/stat/sale/order/retail');
	detail.setParams(ps);
}
Ext.define('app.view.b2b.user.agency.retailStat', {
    extend: 'Ext.grid.Panel',
    xtype:'retailStat',
    config:{
    	columnLines: true,
    	selType:'rowmodel',
    	features: [{
            ftype: 'summary',
            style:'border-top:1px solid #d1d1d1;',
            dock: 'top'
        }],
    	params:null
    },
    initComponent: function() {
    	var me = this;
    	this.store = Ext.create('Ext.data.Store', {
            autoLoad: true,
            model:Ext.create('Ext.data.Model',{
            	fields: [
            	        'BUY_COMPANY_ID','COMPANY_NAME','CITY_ID','CITY_NAME','SALE_AMOUNT','INTER_AMOUNT','PROFIT_AMOUNT','CNT','PERSON_COUNT','MAN_COUNT','CHILD_COUNT','PRODUCE_CNT','ACTUAL_AMOUNT'
            	]
            }),
            proxy: {
		         type: 'ajax',
		         noCache:true,
		         url: cfg.getCtx()+'/stat/sale/order/retail',
		         reader: {
		             rootProperty: 'data',
            		 totalProperty: 'totalSize'
		         }
		     }
        });
    	this.store.getProxy().setExtraParams(this.getParams());
    	this.columns=[Ext.create('Ext.grid.RowNumberer',{width:25}),{
    		text: '门市',
	        flex:1,
	        dataIndex: 'COMPANY_NAME',
	        summaryRenderer: function(v){
            	return '<div class="disable-color f14" style="text-align:right;">总计:</div>';
            },
	        renderer:function(v,c,r){
	        	var h = [
 	        		'<div class="ht20 f14">'+v+'</div>'
 	        	];
 	        	return h.join('');
	        }
    	},{
    		text: '订单数',
    		width:80,
    		summaryType: 'sum',
            summaryFormatter: 'fmNumber',
	        dataIndex: 'CNT',
	        renderer:function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+v+'</div>'
		        ].join('');
	        }
    	},{
    		text: '外卖金额',
	        width:150,
	        summaryType: 'sum',
            summaryFormatter: 'fmMoney',
	        dataIndex: 'SALE_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+util.moneyFormat(v,'f14 cor333')+'</div>'
		        ].join('');
	        }
    	},{
    		text: '结算金额',
    		width:150,
    		summaryType: 'sum',
            summaryFormatter: 'fmMoney',
	        dataIndex: 'ACTUAL_AMOUNT',
	        renderer:function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+util.moneyFormat(v,'f14 cor333')+'</div>'
		        ].join('');
	        }
    	},{
    		text: '旅行社利润',
	        width:150,
	        summaryType: 'sum',
            summaryFormatter: 'fmMoney',
	        dataIndex: 'PROFIT_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+util.moneyFormat(v,'f14 cor333')+'</a></div>'
		        ].join('');
	        }
    	},{
    		text: '人数',
	        width:80,
	        summaryType: 'sum',
            summaryFormatter: 'fmNumber',
	        dataIndex: 'PERSON_COUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+v+'</a></div>'
		        ].join('');
	        }
    	},{
    		text: '成人',
	        width:80,
	        summaryType: 'sum',
            summaryFormatter: 'fmNumber',
	        dataIndex: 'MAN_COUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+v+'</a></div>'
		        ].join('');
	        }
    	},{
    		text: '儿童',
	        width:80,
	        summaryType: 'sum',
            summaryFormatter: 'fmNumber',
	        dataIndex: 'CHILD_COUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+v+'</a></div>'
		        ].join('');
	        }
    	},{
    		text: '产品数',
	        width:80,
	        summaryType: 'sum',
            summaryFormatter: 'fmNumber',
	        dataIndex: 'PRODUCE_CNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+v+'</a></div>'
		        ].join('');
	        }
    	},{
    		xtype: 'actionglyphcolumn',
            width: 60,
            text:'供应商',
            align:'center',
            sortable: false,
            menuDisabled: true,
            /*summaryRenderer: function(v){
            	var ps = me.getStore().getProxy().getExtraParams();
            	var psStr = Ext.encode(ps).replace(/"/g,'_');
            	return '<i class="iconfont blue-color hand" style="top:8px;" onclick="showOrderStatDetail(1,\''+psStr+'\')">&#xe635;</i>';
            },*/
            items: [{
            	iconCls:'iconfont blue-color l20',
                tooltip: '供应商详情',
                scope: this,
                text:'&#xe635;',
                handler: function(grid, rowIndex, colIndex){
                	var record = grid.getStore().getAt(rowIndex),
            		ps = grid.getStore().getProxy().getExtraParams()||{};
                	ps.companyId = record.get('BUY_COMPANY_ID');
                	showOrderStatDetail(1,ps);
                	grid.getSelectionModel().select(rowIndex);
                }
            }]
    	}];
    	
    	this.callParent();
    },
    applyParams:function(params){
    	if(params){
    		Ext.applyIf(params,this.store.getProxy().getExtraParams());
    		if(params.ctype)delete params.ctype;
	    	this.store.getProxy().setExtraParams(params);
	    	this.store.load();
    	}
    	return params;
    }
});