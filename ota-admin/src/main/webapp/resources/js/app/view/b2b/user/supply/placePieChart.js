Ext.define('app.view.b2b.user.supply.placePieChart', {
    extend: 'Ext.Panel',
    config:{
    },
    initComponent: function(p) {
    	var me = this;
    	this.loadChartDatas();
		this.dockedItems = [{
        	xtype:'toolbar',
        	style:'padding-left:1px;background:#e2eff8;',
        	layout: {
                overflowHandler: 'Menu'
            },
        	items:['<span class="bold f14 blue-color"><i class="iconfont f18">&#xe6a1;</i> 目的地分析</span>','->',{
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
        			me.loadChartDatas({startDate:startDate,endDate:endDate});
        		}
        	},{
              	margin:'0 10 0 0',
              	glyph:'xe63d@iconfont',
              	width:30,
              	tooltip:'重置日期',
              	handler:function(b){
              		b.previousSibling().previousSibling().reset();
              		b.previousSibling().previousSibling().previousSibling().reset();
              		
              	}
        	}]
        }];
    	this.callParent();
    },loadChartDatas:function(params){
    	var me = this;
    	params = params || {};
    	chartUtil.request(cfg.getCtx()+'/b2b/user/end/city/stat',params,function(result){
			var data = result.data;
			if(data.length>0){
				me.initPieChart(data[0],'CNT');
			}
		},this);
    },
    initPieChart:function(map){
    	var option = {
				tooltip : {
			        trigger: 'item',
			        formatter: "{a} <br/>{b} : {c} ({d}%)"
			    },
				series : [{
	              name:'目的地人数占比',
	              type:'pie',
	              data:map['PERSON_COUNT']
	          }]
		};
		chartUtil.initPieChart(this,option);
    }
});