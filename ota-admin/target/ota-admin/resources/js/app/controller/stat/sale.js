Ext.define('app.controller.stat.sale', {
	extend: 'app.controller.common.BaseController',
	config: {
		control: {
			'#totalHtml':{
				afterrender:'initInfoData'
			},
			'panel#dataContainer':{
				afterrender:'loadCharts'
			},
			'button#queryBtn':{
				click:'onQueryWeiDuClick'
			},
			'segmentedbutton#groupBtn':{
				toggle:'onGroupButtonChange'
			},
			'segmentedbutton#toggleView':{
				toggle:'onToggleBtnClick'
			}
		}
	},
	initInfoData:function(p){
		var defaultParams = {
				PARAMS_COMPANY_TYPE:currVisitType,
				PARAMS_COMPANY_ID:currVisitCompanyId,
				isParent:currVisitIsParent
		};
		Ext.Ajax.request({
			url:cfg.getCtx()+'/stat/sale/today',
			params:defaultParams,
			success:function(response, opts){
				var obj = Ext.decode(response.responseText);
				p.down('#today').setData(obj);
			}
		});
    	Ext.Ajax.request({
			url:cfg.getCtx()+'/stat/sale/total',
			params:defaultParams,
			success:function(response, opts){
				var obj = Ext.decode(response.responseText);
				p.down('#total').setData(obj);
			}
		});
	},
	onToggleBtnClick:function(sb,btn,pressed){
		var tbar = sb.up('toolbar'),
			ct = tbar.up('#dataContainer'),
			groupRadio = tbar.down('segmentedbutton#groupBtn'),
			condits = tbar.query('[cls=groupChartView]'),
			dateCondits = tbar.query('[action=groupChartDateView]'),
			l = ct.getLayout();
		if(btn.getText()=='图表视图'){
			groupRadio.hide();
			var weidu = 3;
			Ext.Array.each(condits,function(c){
				c.show();
				if(c.itemId=='condit'){weidu=c.getValue();}
			});
			if(weidu==4){
				Ext.Array.each(dateCondits,function(c){c.show();});
			}
			l.setActiveItem(0);
		}else{
			groupRadio.show();
			Ext.Array.each(condits,function(c){c.hide();});
			Ext.Array.each(dateCondits,function(c){c.hide();});
			l.setActiveItem(1);
			var g = ct.down('gridpanel'),
				st = g.getStore();
			st.getProxy().setExtraParams(Ext.applyIf({
				VIEW:2,
				PARAMS_COMPANY_TYPE:currVisitType,
				PARAMS_COMPANY_ID:currVisitCompanyId
			},this.chartParams));
			
			st.load();
		}
	},
	initCharts:function(p){
		this.loadCharts(p);
	},
	onGroupButtonChange:function(sb, button, isPressed){
		var s = sb.getValue(),
			tbar = sb.up('toolbar'),
			ct = tbar.up('#dataContainer'),
			g = ct.down('gridpanel'),
			st = g.getStore();
		if(s==0){
			st.setGroupField('WEIDU');
			g.columns[0].show();
			g.columns[1].hide();
		}else{
			st.setGroupField(currVisitType=='1'?'BUY_COMPANY':'SALE_COMPANY');
			g.columns[1].show();
			g.columns[0].hide();
		}
	   	st.load();
	},
	onQueryWeiDuClick:function(btn){
		var end = btn.previousSibling().previousSibling(),
			start = end.previousSibling(),
			weidu = start.previousSibling();
		
		if(weidu.getValue()==4){
			if(!start.getValue()||!end.getValue()){
				util.alert('选择一个日期区间');
				return;
			}
		}
		this.loadCharts(btn.up('toolbar').up('#dataContainer'),{
			WEIDU:weidu.getValue(),
			START:Ext.Date.format(start.getValue(),'Y-m-d'),
			END:Ext.Date.format(end.getValue(),'Y-m-d'),
			VIEW:1,
			TYPE:1,
			PARAMS_COMPANY_TYPE:currVisitType,
			PARAMS_COMPANY_ID:currVisitCompanyId
		});
	},
	loadCharts:function(p,params){
		params = params || {
			TYPE:1,//目前不做对比分析 对比分析类型为2 对比分析START END必须有值 ，目前只有 维度为日 才有区间
			VIEW:1,//视图类型 图表 or grid
			WEIDU:3,
			START:'',
			END:'',
			PARAMS_COMPANY_TYPE:currVisitType,
			PARAMS_COMPANY_ID:currVisitCompanyId
		};
		this.chartParams = params;
		chartUtil.request(cfg.getCtx()+'/stat/sale/company',params,function(result){
			var data = result.data;
			if(data.length>0){
				var moneyChart = this.initMoneyCharts(p.down('#moneyChartContainer'),data[0]);
				var countChart = this.initCountCharts(p.down('#countChartContainer'),data[0]);
			}
		},this);
	},
	initMoneyCharts:function(p,map){
		var option = {
			title:chartUtil.initTitle('销售金额','80'),
    	    xAxis : [
    	        {
    	            type : 'category',
    	            splitLine : {show : false},
    	            data : map['WEIDU']
    	        }
    	    ],
    	    grid: {x:80,y:50,x2:5,y2:60},
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