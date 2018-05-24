Ext.define('app.view.b2b.user.site.statChart', {
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
    	},
    	cityId:null
    },
    initComponent: function() {
    	var me = this;
    	this.dockedItems = [{
        	xtype:'toolbar',
        	style:'padding-left:10px;background:#e2eff8;',
        	layout: {
                overflowHandler: 'Menu'
            },
            items:['<span class="bold f14 blue-color"><i class="iconfont f18">&#xe693;</i> 销售统计</span>','->',{
	    			xtype: 'segmentedbutton',
	                allowMultiple: false,
	                listeners:{
	                	toggle:function(sb, button, isPressed){
	                		me.loadBarData(sb.getValue(),me.getCityId());
	                	}
	                },
	                items: [{
	                    text: '旅行社',
	                    value:2,
	                    pressed: true
	                },{
	                    text: '供应商',
	                    value:1
	                }]
	    		},{
            	text:'销售详情',
        		handler:function(){
        			if(util.getDirectModuleId('stat/sale')){
        				util.redirectTo('stat/sale');
        			}
        		}
            }]
            
            
    	}];
    	this.items = [{
	    	itemId:'barChartContainer',
	    	style:'border-right:1px solid #c3ced5!important;'
		},{
	    	itemId:'pieChartContainer',
	    	dockedItems:[{
    			xtype:'toolbar',
    			dock:'bottom',
	    		items:['->',{
	    			fieldLabel: '数据切换',
	                labelAlign:'right',
	                labelWidth:60,
	                itemId:'pieDataChange',
	    			listeners:{
	    				change:function(c, newValue, oldValue){
    						var source = c.up('#pieChartContainer').sourceDatas;
    						if(source){
    							me.initPieChart(c.up('#pieChartContainer'), source,newValue);
    						}else{
    							c.reset();
    							util.alert('请点击左侧柱状图');
    						}
	    				}
	    			},
            		xtype:'combo',
					forceSelection: true,
					editable:false,
		            valueField: 'value',
		            displayField: 'text',
		            store:util.createComboStore([{
		            	text:'订单数',
		            	value:'CNT'
		            },{
		            	text:'销售额',
		            	value:'SALE_AMOUNT'
		            },{
		            	text:'结算额',
		            	value:'INTER_AMOUNT'
		            },{
		            	text:'利润',
		            	value:'TRUE_BUY_AMOUNT'
		            }]),
		            width:140,
		            minChars: 0,
		            value:'CNT',
		            queryMode: 'local',
		            typeAhead: true,
	    		}]
	    	}]
		}];
    	this.callParent();
    	
    },
    applyCityId:function(cityId){
    	var me = this;
    	if(cityId){
    		me.loadBarData('2',cityId);
    	}
    	return cityId;
    },
    loadBarData:function(pctype,cityId){
    	var params = params || {
			TYPE:1,//目前不做对比分析 对比分析类型为2 对比分析START END必须有值 ，目前只有 维度为日 才有区间
			VIEW:1,//视图类型 图表 or grid
			WEIDU:3,
			START:'',
			END:'',
			PARAMS_COMPANY_TYPE:pctype||'2',
			PARAMS_COMPANY_ID:cfg.getUser().companyId,
			cityId:cityId
		};
		var me = this;
		this.chartParams = params;
		chartUtil.request(cfg.getCtx()+'/stat/sale/parent/company',params,function(result){
			var data = result.data;
			if(data.length>0){
				//setTimeout(function(){
					me.createBarChart(me.down('#barChartContainer'),data[0],params);
				//},500)
				
			}
		},this);
    },
    createBarChart:function(p,map,oldParams){
		var option = {
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
	}
});
