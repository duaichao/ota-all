Ext.define('app.view.b2b.user.routeStatGrid', {
    extend: 'Ext.grid.Panel',
    xtype:'routeStatGrid',
    config:{
    	columnLines: true,
    	selType:'rowmodel',
    	/*features: [{
            ftype: 'summary',
            dock: 'top'
        }],*/
    	params:null
    },
    initComponent: function() {
    	this.store = Ext.create('Ext.data.Store', {
            autoLoad: true,
            model:Ext.create('Ext.data.Model',{
            	fields: [
            	        'CITY_ID','CITY_NAME','PRODUCE_CNT','ZB_SUPPLY_CNT','GN_SUPPLY_CNT','CJ_SUPPLY_CNT','ZB_PRODUCE_CNT','GN_PRODUCE_CNT','CJ_PRODUCE_CNT'
            	]
            }),
            proxy: {
		         type: 'ajax',
		         noCache:true,
		         url: cfg.getCtx()+'/stat/sale/route',
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
    		text: '产品数量',
    		width:80,
    		summaryType: 'sum',
            summaryFormatter: 'fmNumber',
	        dataIndex: 'PRODUCE_CNT',
	        renderer:function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+v+'</div>'
		        ].join('');
	        }
    	},{
    		text: '周边/供应商',
	        width:100,
	        summaryType: 'sum',
            summaryFormatter: 'fmSplitNumber',
	        dataIndex: 'ZB_SUPPLY_CNT',
	        renderer: function(v,c,r){
	        	return [
	        	    '<a class="ht20 f14 blue-color hand">'+(r.get('ZB_PRODUCE_CNT')||'0')+'</a>',
	        	    ' / ',
	        		'<a class="ht20 f14 blue-color hand">'+v+'</a>'
		        ].join('');
	        }
    	},{
    		text: '国内/供应商',
    		width:100,
    		summaryType: 'sum',
            summaryFormatter: 'fmSplitNumber',
	        dataIndex: 'GN_SUPPLY_CNT',
	        renderer:function(v,c,r){
	        	return [
	        	    '<a class="ht20 f14 blue-color hand">'+(r.get('GN_PRODUCE_CNT')|'0')+'</a>',
	        	    ' / ',
	        		'<a class="ht20 f14 blue-color hand">'+v+'</a>'
		        ].join('');
	        }
    	},{
    		text: '出境/供应商',
	        width:100,
	        summaryType: 'sum',
            summaryFormatter: 'fmSplitNumber',
	        dataIndex: 'CJ_SUPPLY_CNT',
	        renderer: function(v,c,r){
	        	return [
	        	    '<a class="ht20 f14 blue-color hand">'+(r.get('CJ_PRODUCE_CNT')||'0')+'</a>',
	        	    ' / ',
	        		'<a class="ht20 f14 blue-color hand">'+v+'</a>'
		        ].join('');
	        }
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
              	endDateField: 'routeenddt'+this.getParams().type,
	            itemId:'routestartdt'+this.getParams().type,
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
              	itemId:'routeenddt'+this.getParams().type,
	            startDateField: 'routestartdt'+this.getParams().type,
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
    	if(params&&params.cityId){
    		Ext.applyIf(params,this.store.getProxy().getExtraParams());
	    	this.store.getProxy().setExtraParams(params);
	    	this.store.load();
    	}
    	return params;
    }
});