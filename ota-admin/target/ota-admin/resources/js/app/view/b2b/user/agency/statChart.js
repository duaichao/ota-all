Ext.define('app.view.b2b.user.agency.statChart', {
    extend: 'Ext.Panel',
    config:{
    	layout: {
    		type:'hbox',
    		pack: 'start',
    		align: 'stretch'
    	},
    	border:true,
    	defaults:{
    		flex:1
    	}
    },
    initComponent: function() {
    	this.dockedItems = [{
        	xtype:'toolbar',
        	style:'padding-left:10px;background:#e2eff8;',
        	layout: {
                overflowHandler: 'Menu'
            },
            items:['<span class="bold f14 blue-color"><i class="iconfont f18">&#xe693;</i> 销售统计</span>','->',{
            	text:'销售详情',
        		handler:function(){
        			if(util.getDirectModuleId('stat/sale')){
        				util.redirectTo('stat/sale');
        			}
        		}
            },{
        		xtype:'button',
        		text:'退款到账订单',
        		handler:function(){
        			var win = Ext.create('Ext.window.Window',{
        				width:900,
        				height:400,
        				bodyPadding:5,
        				modal:true,
        				draggable:false,
        				resizable:false,
        				maximizable:false,
        	   			layout:'border',
        				title:util.windowTitle('&#xe679;','退款到账订单'),
        				items:[{
        					region:'west',
        					width:340,
        					border:true,
        					margin:'0 5 0 0',
        					xclass:'app.view.b2b.user.agency.refundOrderGrid',
        					listeners:{
        						rowclick:function(view, record, tr, rowIndex){
        							view.up().nextSibling().loadOrderData(record.get('ID'));
        						}
        					}
        				},{
        					region:'center',
        					autoScroll:true,
        					xclass:'app.view.common.OrderFundLog'
        				}]
        			});
        			win.show();
        		}
        	}]
    	}];
    	this.items = [{
	    	itemId:'barChartContainer',
	    	style:'border-right:1px solid #c3ced5!important;',
		},{
	    	itemId:'lineChartContainer'
		}];
    	this.callParent();
    	var params = params || {
			TYPE:1,//目前不做对比分析 对比分析类型为2 对比分析START END必须有值 ，目前只有 维度为日 才有区间
			VIEW:1,//视图类型 图表 or grid
			WEIDU:3,
			START:'',
			END:'',
			PARAMS_COMPANY_TYPE:'2',
			PARAMS_COMPANY_ID:cfg.getUser().companyId
		};
		var me = this;
    	chartUtil.request(cfg.getCtx()+'/stat/sale/company',params,function(result){
			var data = result.data;
			if(data.length>0){
				var moneyChart = me.initMoneyCharts(me.down('#barChartContainer'),data[0]);
				var countChart = me.initCountCharts(me.down('#lineChartContainer'),data[0]);
			}
		},this);
    },
	initMoneyCharts:function(p,map){
		var option = {
			title:chartUtil.initTitle('销售金额','80'),
			dataZoom:false,
    	    xAxis : [
    	        {
    	            type : 'category',
    	            splitLine : {show : false},
    	            data : map['WEIDU']
    	        }
    	    ],
    	    grid: {x:80,y:50,x2:5,y2:30},
    	    yAxis : [{
                type : 'value',
                position: 'left',
                axisLabel : {
                    formatter: '￥{value}'
                }
            }],
            series:[{
                name:'销售额',
                type:'bar',
                data:map['SALE_AMOUNT'],
                itemStyle:{
                	normal:{
                		color:'#62a9dd'
                	}
                },
            },{
                name:'结算额',
                type:'bar',
                data:map['INTER_AMOUNT'],
                itemStyle:{
                	normal:{
                		color:'#4daf7c'
                	}
                }
            },{
                name:'利润',
                type:'bar',
                itemStyle:{
                	normal:{
                		color:'#e6603b'
                	}
                },
                data:map['TRUE_BUY_AMOUNT']
            }]
		};
		return chartUtil.initChart(p,option,['销售额','结算额','利润']);
	},
	initCountCharts:function(p,map){
		var option = {
				dataZoom:false,
				grid: {x:4,y:50,x2:80,y2:30},
				title:chartUtil.initTitle('订单数量'),
	    	    xAxis : [
	    	        {
	    	            type : 'category',
	    	            splitLine : {show : false},
	    	            data : map['WEIDU']
	    	        }
	    	    ],
	    	    yAxis : [{
	                type : 'value',
	                position: 'right',
	                axisLabel : {
	                    formatter: '{value}笔'
	                }
	            }],
	            series:[{
	                name:'成功订单',
	                type:'line',
	                itemStyle:{
	                	normal:{
	                		color:'#62a9dd'
	                	}
	                },
	                data:map['SUCCESS_CNT']
	            },
	            {
	                name:'失败订单',
	                type:'line',
	                itemStyle:{
	                	normal:{
	                		color:'#e6603b'
	                	}
	                },
	                data:map['OTHER_CNT']
	            }]
			};
			return chartUtil.initChart(p,option,['失败订单','成功订单']);
	}
});
