Ext.define('app.controller.stat.sale.parent', {
	extend: 'app.controller.common.BaseController',
	config: {
		control: {
			'#totalHtml':{
				afterrender:'initInfoData'
			},
			'#barChartContainer':{
				afterrender:'initBarChart'
			},
			'#pieDataChange':{
				change:'onPieDataChange'
			},
			'button#queryBtn':{
				click:'onToolbarWeiDuClick'
			},
			'segmentedbutton#toggleView':{
				toggle:'onViewToggleBtnClick'
			},
			'segmentedbutton#groupBtn':{
				toggle:'onGridGroupButtonChange'
			},
			'segmentedbutton#toggleCompanyTypeView':{
				toggle:'onCompanyTypeChange'
			},
			'gridpanel':{
	            cellclick:'onGridCellClick'
			}
		}
	},
	initInfoData:function(p){
		var activeTab = p.up('#frameContainer').getActiveTab();
		var defaultParams = {
				PARAMS_COMPANY_TYPE:currVisitType,
				PARAMS_COMPANY_ID:currVisitCompanyId,
				cityId:activeTab.getCityId()
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
	initBarChart:function(p){
		this.loadBarChart(p,{
			PARAMS_COMPANY_TYPE:'2'//初始化为旅行社
		});
	},
	loadBarChart:function(p,params){
		var activeTab = p.up('#frameContainer').getActiveTab(),
			companyTypeBtns = activeTab.down('#toggleCompanyTypeView').down('[pressed=true]');
		params = params || {};
		Ext.applyIf(params,{
			TYPE:1,//目前不做对比分析 对比分析类型为2 对比分析START END必须有值 ，目前只有 维度为日 才有区间
			VIEW:1,//视图类型 图表 or grid
			WEIDU:3,
			START:'',
			END:'',
			cityId:activeTab.getCityId(),
			PARAMS_COMPANY_TYPE:companyTypeBtns.value,
			PARAMS_COMPANY_ID:currVisitCompanyId
		});
		this.chartParams = params;
		chartUtil.request(cfg.getCtx()+'/stat/sale/parent/company',params,function(result){
			var data = result.data;
			if(data.length>0){
				this.createBarChart(p,data[0],params);
			}
		},this);
	},
	createBarChart:function(p,map,oldParams){
		var option = {
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
		var me = this;
		return chartUtil.initChart(p,option,['销售额','结算额','利润'],function(clickData){
			var param = {
					PARAMS_COMPANY_TYPE:oldParams.PARAMS_COMPANY_TYPE,
					PARAMS_COMPANY_ID:oldParams.PARAMS_COMPANY_ID,
					WEIDU:oldParams.WEIDU,
					queryDate:clickData.name,
					cityId:oldParams.cityId
			};
			chartUtil.request(cfg.getCtx()+'/stat/sale/parent/company',param,function(result){
				var data = result.data;
				if(data.length>0){
					var pieCt = p.nextSibling('#pieChartContainer');
					pieCt.down('#pieDataChange').reset();
					pieCt.sourceDatas = data[0];
					me.initPieChart(pieCt,data[0],'CNT');
				}
			},this);
		});
	},
	initPieChart:function(p,map,key){
		var obj = {'CNT':'订单数','SALE_AMOUNT':'销售额','INTER_AMOUNT':'结算额','TRUE_BUY_AMOUNT':'利润'};
		var option = {
				tooltip : {
			        trigger: 'item',
			        formatter: "{a} <br/>{b} : {c} ({d}%)"
			    },
				//legend: {x : 'left',y:'top',selectedMode:true,textStyle:{fontFamily:"微软雅黑"},data:map['LEGEND']},
				series : [{
	              name:obj[key]+'比例',
	              type:'pie',
	              data:map[key]
	          }]
		};
		return chartUtil.initPieChart(p,option);
	},
	onPieDataChange:function(c, newValue, oldValue){
		var source = c.up('#pieChartContainer').sourceDatas;
		if(source){
			this.initPieChart(c.up('#pieChartContainer'), source,newValue);
		}else{
			c.reset();
			util.alert('请点击左侧柱状图');
		}
	},
	onToolbarWeiDuClick:function(btn){
		var toolbar = btn.up('toolbar'),
			end = btn.previousSibling().previousSibling(),
			start = end.previousSibling(),
			weidu = start.previousSibling();
		var activeTab = toolbar.up('#frameContainer').getActiveTab();
		if(weidu.getValue()==4){
			if(!start.getValue()||!end.getValue()){
				util.alert('选择一个日期区间');
				return;
			}
		}
		this.loadBarChart(activeTab.down('#barChartContainer'),{
			WEIDU:weidu.getValue(),
			START:Ext.Date.format(start.getValue(),'Y-m-d'),
			END:Ext.Date.format(end.getValue(),'Y-m-d'),
			VIEW:1,
			TYPE:1
		});
	},
	onViewToggleBtnClick:function(sb,btn,pressed){
		var tbar = sb.up('toolbar'),
			activeTab = tbar.up('#frameContainer').getActiveTab(),
			dataContainer = activeTab.down('#dataContainer'),
			groupRadio = tbar.down('segmentedbutton#groupBtn'),
			condits = tbar.query('[cls=groupChartView]'),
			dateCondits = tbar.query('[action=groupChartDateView]'),
			l = dataContainer.getLayout(),
			companyTypeBtns = activeTab.down('#toggleCompanyTypeView').down('[pressed=true]');
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
			this.loadBarChart(activeTab.down('#barChartContainer'));
			l.setActiveItem(0);
		}else{
			groupRadio.show();
			Ext.Array.each(condits,function(c){c.hide();});
			Ext.Array.each(dateCondits,function(c){c.hide();});
			l.setActiveItem(1);
			var g = dataContainer.down('gridpanel'),
				st = g.getStore();
			st.getProxy().setExtraParams(Ext.applyIf({
				VIEW:2,
				PARAMS_COMPANY_TYPE:companyTypeBtns.value,
				PARAMS_COMPANY_ID:currVisitCompanyId
			},this.chartParams));
			this.reBuildGridColums(tbar);
			st.load();
		}
	},
	onGridGroupButtonChange:function(sb, button, isPressed){
		var s = sb.getValue(),
			tbar = sb.up('toolbar'),
			activeTab = tbar.up('#frameContainer').getActiveTab(),
			dataContainer = activeTab.down('#dataContainer'),
			g = dataContainer.down('gridpanel'),
			st = g.getStore();
		if(s==0){
			st.setGroupField('WEIDU');
			g.columns[0].show();
			g.columns[1].hide();
		}else{
			st.setGroupField('COMPANY');
			g.columns[1].show();
			g.columns[0].hide();
		}
	   	st.load();
	},
	onCompanyTypeChange:function(sb, button, isPressed){
		var activeTab = sb.up('#frameContainer').getActiveTab();
		var tbar = sb.up('toolbar'),viewSB = tbar.down('#toggleView');
		if(viewSB.getValue()==1){
			this.loadBarChart(activeTab.down('#barChartContainer'));
		}else{
			var g = activeTab.down('gridpanel'),
				st = g.getStore();
			st.getProxy().setExtraParams(Ext.applyIf({
				VIEW:2,
				PARAMS_COMPANY_TYPE:sb.getValue(),
				PARAMS_COMPANY_ID:currVisitCompanyId
			},this.chartParams));
			this.reBuildGridColums(tbar);
			st.load();
		}
	},
	reBuildGridColums:function(tbar){
		//动态列显示
		var companyType = tbar.down('segmentedbutton#toggleCompanyTypeView');
		var activeTab = tbar.up('#frameContainer').getActiveTab();
		var g = activeTab.down('gridpanel');
		if(companyType.getValue()==1){
			//供应商
			g.columns[4].hide();
			g.columns[6].hide();
			g.columns[7].show();
			g.columns[8].show();
			g.columns[9].show();
		}else{
			g.columns[4].show();
			g.columns[6].show();
			g.columns[7].hide();
			g.columns[8].hide();
			g.columns[9].hide();
		}
	},
	onGridCellClick:function(view, td, cellIndex, record, tr, rowIndex, e, eOpts ){
		var frame = view.up('#frameContainer'),
			activeTab = frame.getActiveTab(),
			companyType = activeTab.down('segmentedbutton#toggleCompanyTypeView');
		var rec = record,getUrl='';
		if(cellIndex==0){
			if(e.target.tagName=='A'){
				var isParent ='2';
				if(cfg.getUser().companyType==0){
					//站长看总公司
					isParent = '3';
					if(currVisitCompanyName!=''){
						//站长看总公司的子公司
						if(currVisitCompanyId==rec.get('COMPANY_ID')){
							isParent = '1';
						}else{
							isParent = '2';
						}
					}
				}else{
					if(cfg.getUser().companyId==rec.get('COMPANY_ID')){
						isParent = '1';
					}else{
						isParent = '2';
					}
				}
				getUrl = cfg.getCtx()+'/stat/sale?isParent='+isParent+'&PARAMS_COMPANY_ID='+rec.get('COMPANY_ID')+'&PARAMS_COMPANY_TYPE='+companyType.getValue()+'&PARAMS_COMPANY_NAME='+rec.get('COMPANY');
				var newTab = frame.down('#'+rec.get('COMPANY_ID'));
				if(newTab){
					frame.setActiveTab(newTab);
				}else{
					newTab = frame.add(Ext.create('Ext.ux.IFrame',{
						src:getUrl,
						itemId:rec.get('COMPANY_ID'),
						title:rec.get('COMPANY'),
						closable:true,
						style:'width:100%;height:100%;'
					}));
					frame.setActiveTab(newTab);
				}
			}
		}
	}
});