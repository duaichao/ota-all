Ext.define('app.view.stat.sale', {
    extend: 'Ext.panel.Panel',
    config:{
    	layout: {
	    	type:'vbox',
	        pack: 'start',
	        align: 'stretch'
	    }
    },
    initComponent: function(){
    	this.callParent();
    	var defaultParams = {
    		PARAMS_COMPANY_TYPE:currVisitType,
			PARAMS_COMPANY_ID:currVisitCompanyId,
			isParent:currVisitIsParent
        },
        defaultParamsStr = '&'+Ext.Object.toQueryString(defaultParams);
    	
    	var hideAmountStr = '';
		if(currVisitType=='1'){
    		hideAmountStr = 'style="display:none;"';
    	}
        var me = this,types = ['周边','国内','出境'],typeKeys = ['ZB_','GN_','CJ_'],baseHtml = [
 			'<div class="item">',
			'<li class="title"><label><b>{0}：</b></label></li>',
			'<li>订单：<a href="javascript:;" class="nb" onclick="util.showOrderViewWindow(\'{2}\')">{{1}CNT}</a></li>',
			'<li>人数：<span class="nb">{{1}PERSON_COUNT}</span></li>',
			'<li>成人：<span class="nb">{{1}MAN_COUNT}</span></li>',
			'<li>小孩：<span class="nb">{{1}CHILD_COUNT}</span></li>',
			'<li>'+(currVisitType=='1'?'旅行社':'供应商')+'：<a href="javascript:;" class="nb" onclick="util.showSupplyViewWindow(\'{2}\')">{{1}'+(currVisitType=='1'?'BUY_COMPANY_CNT':'SALE_COMPANY_CNT')+'}</a></li>',
			'<li class="amount"  '+hideAmountStr+'>销售：<span class="money-color f14"><dfn>￥</dfn>{{1}SALE_AMOUNT}</span></li>',
			'<li class="amount">结算：<span class="money-color f14"><dfn>￥</dfn>{{1}INTER_AMOUNT}</span></li>',
			'</div>',
			'<div class="clear"></div>'
        ].join(''),html = [
			'<div class="item">',
			'<li class="title"><label>{0}：</label></li>',
			'<li>订单：<a href="javascript:;" class="nb" onclick="util.showOrderViewWindow(\'{2}\')">{{1}ORDER_CNT}</a></li>',
			'<li>成人：<span class="nb">{{1}MAN_CNT}</span></li>',
			'<li>小孩：<span class="nb">{{1}CHILD_CNT}</span></li>',
			'<li>'+(currVisitType=='1'?'旅行社':'供应商')+'：<a href="javascript:;" class="nb" onclick="util.showSupplyViewWindow(\'{2}\')">{{1}'+(currVisitType=='1'?'BUY_COMPANY_CNT':'SALE_COMPANY_CNT')+'}</a></li>',
			'<li class="amount"  '+hideAmountStr+'>销售：<span class="money-color f14"><dfn>￥</dfn>{{1}SALE_AMOUNT}</span></li>',
			'<li class="amount">结算：<span class="money-color f14"><dfn>￥</dfn>{{1}INTER_AMOUNT}</span></li>',
			'</div>',
			'<div class="clear"></div>'
        ].join(''),
        jrTpl = [],
        zjTpl = [];
        jrTpl.push(Ext.String.format(baseHtml,'今日','','type=today'+defaultParamsStr));
        var now = Ext.Date.format(new Date(),'Y-m-d'),
        	tomorrow = Ext.Date.format(Ext.Date.add(new Date(), Ext.Date.DAY, 1),'Y-m-d');
        jrTpl.push([
			'<div class="item">',
			'<li class="title">&nbsp;</li>',
			'<li class="amount">今日出团：<a href="javascript:;" class="nb" onclick="util.showOrderViewWindow(\'type=today&orderStatuses=2&date='+now+'\')">{TODAY_OUT_CNT}</a></li>',
			'<li class="amount">明日出团：<a href="javascript:;" class="nb" onclick="util.showOrderViewWindow(\'type=today&orderStatuses=2&date='+tomorrow+'\')">{TOMORROW_OUT_CNT}</a></li>',
			'</div>',
			'<div class="clear"></div>'
        ].join(''));
        zjTpl.push(Ext.String.format(baseHtml,'累计','','type=total'+defaultParamsStr));
        
        zjTpl.push([
        			'<div class="item">',
        			'<li class="title">&nbsp;</li>',
        			'<li class="amount ">上述订单包含交通</li>',
        			'</div>',
        			'<div class="clear"></div>'
                ].join(''));
        Ext.Array.each(types,function(s,idx){
        	jrTpl.push(Ext.String.format(html,s,typeKeys[idx],'type=today&routeType='+(idx+1)+defaultParamsStr));
        	zjTpl.push(Ext.String.format(html,s,typeKeys[idx],'type=total&routeType='+(idx+1)+defaultParamsStr));
        });
        
        
        
        //基础信息
        me.add({
			margin:'1',
			border:true,
			itemId:'totalHtml',
			height:120,
			layout: {
				type:'hbox',
				pack: 'start',
				align: 'stretch'
			},
			defaults:{
				bodyPadding:5,
				cls:'total',
				flex:1
			},
			items:[{
				itemId:'today',
				tpl:jrTpl,
				data:{}
			},{
				style:'border-left:1px solid #c3ced5!important;',
				itemId:'total',
      		  	tpl:zjTpl,
      		  	data:{}
			}]
        });
        
        
        var store = Ext.create('Ext.data.Store', {
            groupField:'WEIDU',
            autoLoad: false,
            model:Ext.create('Ext.data.Model',{
            	fields: ['CNT','SUCCESS_CNT','WEIDU','SALE_AMOUNT','INTER_AMOUNT','TRUE_BUY_AMOUNT','TRUE_SALE_AMOUNT',
            	         'SALE_COMPANY','BUY_COMPANY','REFUND_AMOUNT','MARKETING_AMOUNT']
            }),
            proxy: {
		         type: 'ajax',
		         noCache:true,
		         url: cfg.getCtx()+'/stat/sale/company',
		         reader: {
		             rootProperty: 'data',
            		 totalProperty: 'totalSize'
		         }
		     }
        });
        me.add({
        	dockedItems:[{
    			xtype:'toolbar',
	    		style:'background:#f7f7f7;border-bottom:1px solid #ddd!important;',
            	items:[{
            		itemId:'condit',
            		cls:'groupChartView',
	                fieldLabel: '统计视角',
	                labelAlign:'right',
	                labelWidth:60,
            		xtype:'combo',
					forceSelection: true,
					editable:false,
		            valueField: 'value',
		            displayField: 'text',
		            store:util.createComboStore([{
		            	text:'年',
		            	value:1
		            },{
		            	text:'季度',
		            	value:2
		            },{
		            	text:'月',
		            	value:3
		            },{
		            	text:'日',
		            	value:4
		            }]),
		            width:125,
		            minChars: 0,
		            value:3,
		            queryMode: 'local',
		            typeAhead: true,
		            listeners:{
		            	change:function(c, newValue, oldValue, eOpts ){
							if(newValue==4){
								c.nextSibling().show();
								c.nextSibling().nextSibling().show();
								c.nextSibling().nextSibling().nextSibling().show();
							}else{
								c.nextSibling().hide();
								c.nextSibling().nextSibling().hide();
								c.nextSibling().nextSibling().nextSibling().hide();
							}
		            	}
		            }
            	},{
            		action:'groupChartDateView',
            		hidden:true,
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
                	action:'groupChartDateView',
                	width:100,
                	emptyText:'结束日期',
                	hidden:true,
                	margin:'0 0 0 0',
                	format:'Y-m-d',
                	editable:false,
                	showToday:false,
                	itemId:'enddt',
		            startDateField: 'startdt',
		            vtype: 'daterange',
                	xtype:'datefield'
                },{
                	action:'groupChartDateView',
                	hidden:true,
                	margin:'0 0 0 1',
                	glyph:'xe694@iconfont',
                	handler:function(b){
                		b.previousSibling().reset();
                		b.previousSibling().previousSibling().reset();
                	}
                },{
                	cls:'groupChartView',
                	itemId:'queryBtn',
                	margin:'0 0 0 10',
                	text:'查询'
                },{
            		xtype: 'segmentedbutton',
            		itemId:'groupBtn',
            		hidden:true,
                    allowMultiple: false,
                    items: [{
                        text: '按统计视角分组',
                        pressed: true
                    },{
                        text: '按'+(currVisitType=='1'?'旅行社':'供应商')+'分组'
                    }]
	    		},'->',{
	    			xtype: 'segmentedbutton',
	    			itemId:'toggleView',
                    allowMultiple: false,
                    items: [{
                        text: '图表视图',
                        pressed: true
                    },{
                        text: '数据视图'
                    }]
	    		}]
    		}],
    		margin:'0 1 1 1',
	    	flex:1,
	    	border:true,
	    	itemId:'dataContainer',
	    	layout:'card',
	    	items:[{
	    		layout: {
		    		type:'hbox',
		    		pack: 'start',
		    		align: 'stretch'
		    	},
		    	defaults:{
		    		flex:1
		    	},
		    	items:[{
			    	itemId:'moneyChartContainer'
	    		},{
	    			style:'border-left:1px solid #c3ced5!important;',
			    	itemId:'countChartContainer'
	    		}]
	    	},{
	    		xtype:'gridpanel',
				store:store,
				features: [{
			        ftype : 'groupingsummary',
			        groupHeaderTpl :[
	         		    '<span>{[this.format(values)]}</span>',
	         		    {
	         		        format: function(o) {
	         		        	if(o.groupField=='WEIDU'){
	         		        		return '<span>'+o.name+' 共'+o.rows.length+'家'+(currVisitType=='1'?'旅行社':'供应商')+'</span>';
	         		        	}else{
	         		        		return '<span>'+o.name+' 共'+o.rows.length+'条数据</span>';
	         		        	}
	         		        }
	         		    }
	         		],
			        hideGroupedHeader : false,
			        id:'restaurantGrouping',
			        enableGroupingMenu : false
			    }, {
			        ftype: 'summary',
			        dock: 'bottom'
			    }],
				columns:[{
					text: currVisitType=='1'?'旅行社':'供应商',
			        groupable: false,
			        flex:1,
			        summaryType: 'count',
			        summaryRenderer: function(v){
		            	var str='';
		            	if(Ext.isObject(v)){
		            		str = '<div class="disable-color" style="text-align:right;">总计:</div>';
		            	}else{
		            		str = '<div class="disable-color" style="text-align:right;">共'+v+'家'+(currVisitType=='1'?'旅行社':'供应商')+'，合计:</div>';
		            	}
		            	return str;
		            },
			        dataIndex: currVisitType=='1'?'BUY_COMPANY':'SALE_COMPANY',
			        renderer: function(v,c,r){
			        	var h = [];
			        	
			        	h.push('<a class="f14 ht20 title" href="javascript:;" data-qtip="'+v+'">'+Ext.util.Format.ellipsis(v,25)+'</a>');
			        	
		        		return h.join('');
			        }
				},{
					text: '统计条件',
			        groupable: false,
			        hidden:true,
			        flex:1,
			        summaryType: 'count',
			        summaryRenderer: function(v){
		            	var str='';
		            	if(Ext.isObject(v)){
		            		str = '<div class="disable-color" style="text-align:right;">总计:</div>';
		            	}else{
		            		str = '<div class="disable-color" style="text-align:right;">共'+v+'条数据，合计:</div>';
		            	}
		            	return str;
		            },
			        dataIndex: 'WEIDU',
			        renderer: function(v,c,r){
			        	var h = [];
			        	
			        	h.push('<a class="f14 ht20 title" href="javascript:;" data-qtip="'+v+'">'+Ext.util.Format.ellipsis(v,25)+'</a>');
			        	
		        		return h.join('');
			        }
				},{
					text: '订单总数',
			        groupable: false,
			        summaryType: 'sum',
		            summaryFormatter: 'fmNumber',
			        width:80,
			        dataIndex: 'CNT',
			        renderer: function(v,c,r){
			        	return [
			        		'<div class="ht20 f18 disable-color">'+v+'</div>'
			        	].join('');
			        }
				},{
					text: '成功订单',
			        groupable: false,
			        summaryType: 'sum',
		            summaryFormatter: 'fmGreenNumber',
			        width:80,
			        dataIndex: 'SUCCESS_CNT',
			        renderer: function(v,c,r){
			        	return [
			        		'<div class="ht20 f18 green-color">'+v+'</div>'
			        	].join('');
			        }
				},{
					text: '销售金额',
			        groupable: false,
			        hidden:currVisitType=='1',
			        summaryType: 'sum',
		            summaryFormatter: 'fmBlueMoney',
			        width:120,
			        dataIndex: 'SALE_AMOUNT',
			        renderer: function(v,c,r){
			        	return [
			        		'<div class="ht20">'+util.moneyFormat(v,'f18 blue-color')+'</div>'
			        	].join('');
			        }
				},{
					text: '结算金额',
			        groupable: false,
			        summaryType: 'sum',
		            summaryFormatter: 'fmBlueMoney',
			        width:120,
			        dataIndex: 'INTER_AMOUNT',
			        renderer: function(v,c,r){
			        	return [
			        		'<div class="ht20">'+util.moneyFormat(v,'f18 blue-color')+'</div>'
			        	].join('');
			        }
				},{
					text: '销售利润',
					hidden:currVisitType=='1',
			        groupable: false,
			        summaryType: 'sum',
		            summaryFormatter: 'fmGreenMoney',
			        width:120,
			        dataIndex: 'TRUE_BUY_AMOUNT',
			        renderer: function(v,c,r){
			        	return [
			        		'<div class="ht20">'+util.moneyFormat(v,'f18 orange-color')+'</div>'
			        	].join('');
			        }
				},{
					text: '结算利润',
					hidden:currVisitType!='1',
			        groupable: false,
			        summaryType: 'sum',
		            summaryFormatter: 'fmGreenMoney',
			        width:100,
			        dataIndex: 'TRUE_SALE_AMOUNT',
			        renderer: function(v,c,r){
			        	return [
			        		'<div class="ht20">'+util.moneyFormat(v,'f18 green-color')+'</div>'
			        	].join('');
			        }
				},{
					text: '营销费',
					hidden:currVisitType!='1',
			        groupable: false,
			        summaryType: 'sum',
		            summaryFormatter: 'fmOrangeMoney',
			        width:100,
			        dataIndex: 'MARKETING_AMOUNT',
			        renderer: function(v,c,r){
			        	return [
			        		'<div class="ht20">'+util.moneyFormat(v,'f18 orange-color')+'</div>'
			        	].join('');
			        }
				},{
					text: '退款',
					hidden:currVisitType!='1',
			        groupable: false,
			        summaryType: 'sum',
		            summaryFormatter: 'fmRedMoney',
			        width:100,
			        dataIndex: 'REFUND_AMOUNT',
			        renderer: function(v,c,r){
			        	return [
			        		'<div class="ht20">'+util.moneyFormat(v,'f18 red-color')+'</div>'
			        	].join('');
			        }
				}]
	    	}]
        });
    }
});

