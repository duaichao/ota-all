Ext.define('app.view.b2b.user.supply.statGrid', {
    extend: 'Ext.grid.Panel',
    config:{
    	columnLines: true,
    	selType:'rowmodel'
    },
    initComponent: function() {
    	this.store = Ext.create('Ext.data.Store', {
            autoLoad: true,
            model:Ext.create('Ext.data.Model',{
            	fields: [
            	        'SUCCESS_CNT','REFUND_CNT','CANCEL_CNT','CNT','BUY_COMPANY'
            	]
            }),
            proxy: {
		         type: 'ajax',
		         noCache:true,
		         url: cfg.getCtx()+'/b2b/user/order/num',
		         reader: {
		             rootProperty: 'data',
            		 totalProperty: 'totalSize'
		         }
		     }
        });
    	this.columns=[Ext.create('Ext.grid.RowNumberer',{width:25}),{
    		text: '旅行社',
	        flex:1,
	        dataIndex: 'BUY_COMPANY',
	        renderer:function(v,c,r){
	        	var h = [
 	        		'<div class="ht20"><a href="javascript:;">'+v+'</a></div>'
 	        	];
 	        	return h.join('');
	        }
    	},{
    		text: '总数',
    		width:80,
	        dataIndex: 'CNT',
	        renderer:function(v,c,r){
	        	return [
	        		'<div class="ht20 f18 blue-color">'+v+'</div>'
		        ].join('');
	        }
    	},{
    		text: '成功数',
    		width:80,
	        dataIndex: 'SUCCESS_CNT',
	        renderer:function(v,c,r){
	        	return [
	        		'<div class="ht20 f18 green-color">'+v+'</div>'
		        ].join('');
	        }
    	},{
    		text: '退款数',
	        width:80,
	        dataIndex: 'REFUND_CNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20 f18 blue-color">'+v+'</div>'
		        ].join('');
	        }
    	},{
    		text: '取消数',
	        width:80,
	        dataIndex: 'CANCEL_CNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20 f18 disable-color">'+v+'</div>'
		        ].join('');
	        }
    	}];
    	this.dockedItems = [{
        	xtype:'toolbar',
        	style:'padding-left:1px;background:#e2eff8;',
        	layout: {
                overflowHandler: 'Menu'
            },
        	items:['<span class="bold f14 blue-color"><i class="iconfont f18">&#xe693;</i> 订单统计</span>','->',{
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
        			util.redirectTo('stat/sale');
        		}
        	}]
        }];
    	
    	
    	this.callParent();
    }
});