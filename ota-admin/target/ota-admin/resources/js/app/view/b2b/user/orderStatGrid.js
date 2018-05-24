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
		title:util.windowTitle('&#xe635;',isSupply==1?'供应商':'旅行社',''),
		width:800,
		height:400,
		modal:true,
		draggable:false,
		resizable:false,
			layout:'fit',
		items:[detail]
	}).show();
	detail.setParams(ps);
}
Ext.define('app.view.b2b.user.orderStatGrid', {
    extend: 'Ext.grid.Panel',
    xtype:'orderStatGrid',
    config:{
    	columnLines: true,
    	selType:'rowmodel',
    	features: [{
            ftype: 'summary',
            style:'border-top:1px solid #d1d1d1;',
            dock: 'top'
        }],
    	params:null,
    	viewConfig: {
    	    preserveScrollOnRefresh: true
    	}
    },
    initComponent: function() {
    	var me = this;
    	this.store = Ext.create('Ext.data.Store', {
            autoLoad: true,
            model:Ext.create('Ext.data.Model',{
            	fields: [
            	        'CITY_ID','CITY_NAME','SALE_AMOUNT','INTER_AMOUNT','PROFIT_AMOUNT','CNT','PERSON_COUNT','MAN_COUNT','CHILD_COUNT','PRODUCE_CNT'
            	]
            }),
            proxy: {
		         type: 'ajax',
		         noCache:true,
		         url: cfg.getCtx()+'/stat/sale/order',
		         reader: {
		             rootProperty: 'data',
            		 totalProperty: 'totalSize'
		         }
		     }
        });
    	this.store.getProxy().setExtraParams(this.getParams());
    	this.columns=[Ext.create('Ext.grid.RowNumberer',{width:25}),{
    		text: this.getParams().type==0?'出发城市':'目的地',
	        flex:1,
	        dataIndex: 'CITY_NAME',
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
	        dataIndex: 'INTER_AMOUNT',
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
            text:'旅行社',
            align:'center',
            sortable: false,
            summaryRenderer: function(v){
            	var ps = me.getStore().getProxy().getExtraParams();
            	var psStr = Ext.encode(ps).replace(/"/g,'_');
            	return '<i class="iconfont blue-color hand" style="top:8px;" onclick="showOrderStatDetail(0,\''+psStr+'\')">&#xe635;</i>';
            },
            menuDisabled: true,
            items: [{
            	iconCls:'iconfont blue-color l20',
                tooltip: '旅行社详情',
                scope: this,
                text:'&#xe635;',
                handler: function(grid, rowIndex, colIndex){
                	var record = grid.getStore().getAt(rowIndex),
                		ps = grid.getStore().getProxy().getExtraParams();
                	ps.ccityId = record.get('CITY_ID');
                	grid.getSelectionModel().select(rowIndex);
                	showOrderStatDetail(0,ps);
                }
            }]
    	},{
    		hidden:(this.getParams().pruview=='supply'),
    		xtype: 'actionglyphcolumn',
            width: 60,
            text:'供应商',
            align:'center',
            sortable: false,
            menuDisabled: true,
            summaryRenderer: function(v){
            	var ps = me.getStore().getProxy().getExtraParams();
            	var psStr = Ext.encode(ps).replace(/"/g,'_');
            	return '<i class="iconfont blue-color hand" style="top:8px;" onclick="showOrderStatDetail(1,\''+psStr+'\')">&#xe635;</i>';
            },
            items: [{
            	iconCls:'iconfont blue-color l20',
                tooltip: '供应商详情',
                scope: this,
                text:'&#xe635;',
                handler: function(grid, rowIndex, colIndex){
                	var record = grid.getStore().getAt(rowIndex),
            		ps = grid.getStore().getProxy().getExtraParams();
                	ps.ccityId = record.get('CITY_ID');
                	grid.getSelectionModel().select(rowIndex);
                	showOrderStatDetail(1,ps);
                }
            }]
    	}];
    	/*this.dockedItems = [{
        	xtype:'toolbar',
        	style:'padding:2px 0 2px 1px;',
        	height:32,
        	layout: {
                overflowHandler: 'Menu'
            },
        	items:[,'->',{
        		width:100,
        		emptyText:'开始日期',
              	endDateField: 'orderenddt'+this.getParams().type,
	            itemId:'orderstartdt'+this.getParams().type,
	            showToday:false,
	            editable:false,
	            vtype: 'daterange',
              	format:'Y-m-d',
              	xtype:'datefield'
        	},{
              	width:100,
              	emptyText:'结束日期',
              	margin:'0 0 0 0',
              	format:'Y-m-d',
              	editable:false,
              	showToday:false,
              	itemId:'orderenddt'+this.getParams().type,
	            startDateField: 'orderstartdt'+this.getParams().type,
	            vtype: 'daterange',
              	xtype:'datefield'
        	},{
              	cls:'groupChartView',
              	width:30,
              	margin:'0 2 0 2',
              	glyph:'xe61c@iconfont',
              	tooltip:'查询',
        		handler:function(btn){
        			var g = btn.up('toolbar').up('gridpanel'),
        				endDate = btn.previousSibling(),
        				startDate = endDate.previousSibling();
        			startDate = Ext.Date.format(startDate.getValue(),'Y-m-d');
        			endDate = Ext.Date.format(endDate.getValue(),'Y-m-d');
        			var ep = {startDate:startDate,endDate:endDate};
					Ext.applyIf(ep,g.getStore().getProxy().getExtraParams());
	    			g.getStore().getProxy().setExtraParams(ep);
        			g.getStore().load();
        		}
        	},{
              	margin:'0 10 0 0',
              	glyph:'xe63d@iconfont',
              	width:30,
              	tooltip:'重置日期',
              	handler:function(b){
              		b.previousSibling().previousSibling().reset();
              		b.previousSibling().previousSibling().previousSibling().reset();
              		var g = b.up('toolbar').up('gridpanel');
              		g.getStore().getProxy().setExtraParam('startDate', '');
              		g.getStore().getProxy().setExtraParam('endDate', '');
        			g.getStore().load();
              	}
        	}]
        }];*/
    	
    	
    	this.callParent();
    },
    applyParams:function(params){
    	if(params&&(params.cityId||params.departIds)){
    		Ext.applyIf(params,this.store.getProxy().getExtraParams());
	    	this.store.getProxy().setExtraParams(params);
	    	this.store.load();
    	}
    	return params;
    }
});