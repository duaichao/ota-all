Ext.define('app.view.b2b.user.supply.routeGrid', {
    extend: 'Ext.grid.Panel',
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
    	this.store = Ext.create('Ext.data.Store', {
            autoLoad: true,
            model:Ext.create('Ext.data.Model',{
            	fields: [
            	        'PRODUCE_NAME','PERSON_COUNT','TRUE_SALE_AMOUNT','IS_PUB','BUY_COMPANY_CNT'
            	]
            }),
            proxy: {
		         type: 'ajax',
		         noCache:true,
		         url: cfg.getCtx()+'/b2b/user/route/stat',
		         reader: {
		             rootProperty: 'data',
            		 totalProperty: 'totalSize'
		         }
		     }
        });
    	this.columns=[Ext.create('Ext.grid.RowNumberer',{width:25}),{
    		text: '线路名称',
	        flex:1,
	        dataIndex: 'PRODUCE_NAME',
	        summaryRenderer: function(v){
            	return '<div class="disable-color f14" style="text-align:right;">总计:</div>';
            },
	        renderer:function(v,c,r){
	        	var h = [
 	        		'<div class="ht20"><a href="javascript:;" class="f14">'+v+'</a></div>'
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
    		text: '游客人数',
    		width:80,
    		summaryType: 'sum',
            summaryFormatter: 'fmNumber',
	        dataIndex: 'PERSON_COUNT',
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
	        dataIndex: 'BUY_COMPANY_CNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+v+'</div>'
		        ].join('');
	        }
    	},{
    		text: '结算金额',
	        width:150,
	        summaryType: 'sum',
            summaryFormatter: 'fmMoney',
	        dataIndex: 'TRUE_SALE_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20">'+util.moneyFormat(v,'f14 cor333')+'</div>'
		        ].join('');
	        }
    	},{
    		text: '状态',
	        width:70,
	        dataIndex: 'IS_PUB',
	        renderer: function(v,c,r){
	        	var s = ['待发布','已发布','已停售'],
	        		cor = ['disable-color','green-color','red-color'];
	        	return [
	        		'<div class="ht20 '+cor[v]+'">'+s[v]+'</div>'
		        ].join('');
	        }
    	}];
    	/*
    	this.dockedItems = [{
        	xtype:'toolbar',
        	style:'padding-left:1px;background:#e2eff8;',
        	layout: {
                overflowHandler: 'Menu'
            },
        	items:['<span class="bold f14 blue-color"><i class="iconfont f18">&#xe691;</i> 线路统计</span>','->',{
        		width:100,
        		emptyText:'开始日期',
              	endDateField: 'enddt',
	            itemId:'startdt',
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
              	itemId:'enddt',
	            startDateField: 'startdt',
	            vtype: 'daterange',
              	xtype:'datefield'
        	},{
              	cls:'groupChartView',
              	itemId:'queryBtn',
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
        	},'-',{
        		text:'详情',
        		handler:function(){
        			util.redirectTo('resource/route');
        		}
        	}]
        }];*/
    	
    	
    	this.callParent();
    },
    applyParams:function(params){
    	if(params&&params.departIds){
    		Ext.applyIf(params,this.store.getProxy().getExtraParams());
	    	this.store.getProxy().setExtraParams(params);
	    	this.store.load();
    	}
    	return params;
    }
});