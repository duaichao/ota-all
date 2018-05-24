Ext.define('app.view.b2b.user.openedCompanyGrid', {
    extend: 'Ext.grid.Panel',
    xtype:'openedCompanyGrid',
    config:{
    	columnLines: true,
    	selType:'rowmodel',
    	features: [{
            ftype: 'summary',
            dock: 'top'
        }],
    	params:null
    	
    },
    initComponent: function() {
    	this.store = Ext.create('Ext.data.Store', {
            autoLoad: true,
            model:Ext.create('Ext.data.Model',{
            	fields: [
            	        'CITY_ID','CITY_NAME','SUM_COMPANY_CNT','SUPPLY_COMPANY_CNT','GROUP_COMPANY_CNT','SMALL_COMPANY_CNT','GW_COMPANY_CNT'
            	]
            }),
            proxy: {
		         type: 'ajax',
		         noCache:true,
		         url: cfg.getCtx()+'/stat/sale/opened/company',
		         reader: {
		             rootProperty: 'data',
            		 totalProperty: 'totalSize'
		         }
		     }
        });
    	this.store.getProxy().setExtraParams(this.getParams());
    	this.columns=[Ext.create('Ext.grid.RowNumberer',{width:25}),{
    		text: '城市（站）',
	        width:150,
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
    		text: '开户数量',
    		width:80,
    		summaryType: 'sum',
            summaryFormatter: 'fmNumber',
	        dataIndex: 'SUM_COMPANY_CNT',
	        renderer:function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+v+'</div>'
		        ].join('');
	        }
    	},{
    		text: '旅行社',
	        width:80,
	        summaryType: 'sum',
            summaryFormatter: 'fmNumber',
	        dataIndex: 'GROUP_COMPANY_CNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+v+'</div>'
		        ].join('');
	        }
    	},{
    		text: '供应商',
    		width:80,
    		summaryType: 'sum',
            summaryFormatter: 'fmNumber',
	        dataIndex: 'SUPPLY_COMPANY_CNT',
	        renderer:function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+v+'</div>'
		        ].join('');
	        }
    	},{
    		text: '小社',
	        width:80,
	        summaryType: 'sum',
            summaryFormatter: 'fmNumber',
	        dataIndex: 'SMALL_COMPANY_CNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+v+'</a></div>'
		        ].join('');
	        }
    	},{
    		text: '旅游顾问',
	        width:80,
	        summaryType: 'sum',
            summaryFormatter: 'fmNumber',
	        dataIndex: 'GW_COMPANY_CNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+v+'</div>'
		        ].join('');
	        }
    	},{
    		flex:1,
    		sortable: false,
            menuDisabled: true
    	},{
    		xtype: 'actionglyphcolumn',
            width: 40,
            align:'center',
            sortable: false,
            menuDisabled: true,
            items: [{
            	iconCls:'iconfont blue-color',
                tooltip: '详情',
                scope: this,
                text:'&#xe635;',
                handler: function(grid, rowIndex, colIndex){
                	var record = grid.getStore().getAt(rowIndex),
                		ps = grid.getStore().getProxy().getExtraParams()||{};
                	ps.ccityId = record.get('CITY_ID');
                	grid.getSelectionModel().select(rowIndex);
                	var detail = Ext.create('app.view.b2b.user.openedCompanyDetail',{
					});
					var win = Ext.create('Ext.window.Window',{
						title:util.windowTitle('&#xe635;','开户详情',''),
						width:900,
						height:400,
						modal:true,
						draggable:false,
						resizable:false,
			   			layout:'fit',
						items:[detail]
					}).show();
					detail.setParams(ps);
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
        	items:['->',{
        		width:100,
        		emptyText:'开始日期',
              	endDateField: 'openenddt',
	            itemId:'openstartdt',
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
              	itemId:'openenddt',
	            startDateField: 'openstartdt',
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