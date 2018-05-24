Ext.define('app.view.produce.routeGrid', {
	extend:'Ext.grid.Panel',
	xtype:'proroutegrid',
	loadMask: true,
	constructor: function(config) {
		/*config = Ext.apply({
            plugins: [{
		        ptype: 'rowexpander',
		        pluginId:'expander'
		    }]
        }, config);*/
        this.callParent([config]);
    },
    listeners:{
        viewready: function () {
            
        }
    },
	initComponent: function() {
		var me = this;
		this.store = util.createGridStore(cfg.getCtx()+cfg.getModuleListUrl()[currViewInPath],Ext.create('app.model.resource.route.model'));
		var po = {routeType:''};
		if(dynamicParamsSupplyName!=''){
			po.supplys = dynamicParamsSupplyName;
		}
        this.store.getProxy().setExtraParams(po);
        this.bbar = util.createGridBbar(this.store);
        this.columns = Ext.create('app.model.resource.route.column');
        
        this.columns.splice(this.columns.length-1,1,{
        	xtype: 'widgetcolumn',
        	width:110,
        	text:'',
        	sortable: false,
            menuDisabled: true,
        	widget: {
        		xtype:'container',
        		layout:'column',
        		items:[{
        			text:'预订',
	                cls:cfg.getUser().departId==''?'disable':'red',
	                disabled:(cfg.getUser().departId==''),
	                width:90,
	                xtype: 'button',
	                handler: function(btn) {
	                	var rec = btn.up('container').getWidgetRecord();
	                	if(me.columns[5].hidden){
	                    	me.routePrice(rec);
	                    }else{
	                    	//预订地接
	                    	document.location.href=cfg.getCtx()+'/produce/route/buy?routeId='+rec.get('ID');
	                    }
	                }
        		},{
        			xtype:'button',
        			margin:'3 0 0 0',
        			width:90,
        			ui:'default-toolbar',
        			glyph:'xe67a@iconfont',
        			text:'联系卖家',
        			handler:function(btn){
        				var rec = btn.up('container').getWidgetRecord();
        				Ext.create('app.view.resource.contactWin',{
        					proId:rec.get('ID'),
        					viewShow:'view'
        				}).show();
        				/*Ext.create('Ext.window.Window',{
        					title:util.windowTitle('&#xe6fa;','联系卖家',''),
        					width:300,
        					height:180,
        					modal:true,
        					bodyPadding:10,
							draggable:false,
							resizable:false,
							maximizable:false,
				   			layout:'fit',
        					items:[{
        						tpl:[
        							'<h3 class="f16" style="width:270px;white-space:normal;margin-bottom:5px;line-height:20px;">{COMPANY_NAME}</h3>',
        							'<div class="ht16">联系：{PORDUCE_CONCAT}</div>',
        							'<div class="ht16">手机：{PRODUCE_MOBILE}</div>',
        							'<div class="ht16">电话：{PHONE}</div>'
        						],
        						data:rec.data
        					}]
        				}).show();*/
        			}
        		}]
            }
        });
        this.columns.splice(this.columns.length-2,1,{
        	text:'最近日期/价格',
        	width:120,
	        dataIndex: 'SUM_PRICE',
	        renderer:function(v,c,r){
	        	var re = [],isFullPrice = r.get('IS_FULL_PRICE')||'0',isEarnest = r.get('IS_EARNEST')||'0';
	        	
	        	re.push('<div style="position:absolute;top:10px;right:0px;padding-left:6px;width:30px;border-left:1px dashed #999;">');
	        	
	        	if(r.get('RQ')){
	        		re.push('<i class="iconfont pp disable-color f20" style="margin-bottom:8px;display:block;" data-qtip=\'同行价：'+util.moneyFormat(r.get('SUM_INTER_PRICE'),'f16 light-orange-color')+'\'>&#xe65c;</i>');
	        		if(isFullPrice=='1'){
	        			re.push('<i class="iconfont pp f20" data-qtip="打包价详情" style="font-weight:bold;display:block;color:#427fed;cursor:pointer;margin-bottom:8px;">&#xe658;</i>');
	        		}else{
	        			re.push('<i class="iconfont pp f20" data-qtip="综合报价详情" style="display:block;color:#427fed;cursor:pointer;margin-bottom:8px;">&#xe65f;</i>');
	        		}
	        	}else{
	        		re.push('<i class="iconfont pp disable-color f20" style="margin-bottom:8px;display:block;" data-qtip=\''+util.moneyFormat(r.get('ROUTE_INTER_PRICE'),'f14 light-blue-color')+'\'>&#xe65c;</i>');
	        	}
	        	if(isFullPrice=='0'){
	        		re.push('<i class="iconfont dd f20" data-qtip="地接报价详情" style="display:block;color:#427fed;cursor:pointer;margin-bottom:8px;">&#xe65d;</i>');
	        	}
	        	re.push('<i class="iconfont dfc disable-color f20" data-qtip="单房差：<br>外卖：<span class=\'f16 light-orange-color\'><dfn>￥</dfn>'+r.get('RETAIL_SINGLE_ROOM')+'</span><br>同行：<span class=\'f14 light-blue-color\'><dfn>￥</dfn>'+r.get('INTER_SINGLE_ROOM')+'</span>" style="display:block;">&#xe65e;</i>');
	        	re.push('</div>');
	        	
	        	if(r.get('RQ')){
	        		re.push('<div class="ht20 f14" style="margin-bottom:5px;">'+(r.get('RQ')||'')+'</div>');
	        		re.push('<div class="ht20">'+util.moneyFormat(v)+'</div>');
	        	}else{
	        		re.push('<div class="ht20">'+util.moneyFormat(r.get('ROUTE_PRICE'))+'</div>');
	        		
	        	}
	        	if(isEarnest=='1'){
	        		var earnestInter = util.moneyFormat(r.get('EARNEST_INTER'),'f14 light-blue-color'),
	        			earnestRetail = util.moneyFormat(r.get('EARNEST_RETAIL'),'f14 light-orange-color'),
	        			earnestType = r.get('EARNEST_TYPE');
	        		
	        		re.push('<div style="padding-top:5px;" data-qtip=\'此产品支持定金预付，最低出团费用同行价/人：'+earnestInter+'，外卖价/人:'+earnestRetail+'\'>');
	        		re.push(' <span style="border-radius:2px;font-size:12px;color:#fff;background:#9C27B0;padding:2px 4px;">定</span>');
	        		if(earnestType=='1'){
	        			re.push(' <span style="border-radius:2px;font-size:12px;color:#fff;background:#00796b;padding:2px 4px;" data-qtip=\'代收款定金预付订单，需要供应商确认\' >代</span>');
	        		}
	        		re.push('</div>');
	        	}
	        	return re.join('');
	        }
        });
        this.columns.splice(this.columns.length-2,0,{
        	text:'价格',
        	width:120,
        	hidden:true,
	        dataIndex: 'ROUTE_PRICE',
	        renderer:function(v,c,r){
	        	var re = [],isEarnest = r.get('IS_EARNEST')||'0';
	        	re.push('<div style="position:absolute;top:10px;right:0px;padding-left:6px;width:30px;border-left:1px dashed #999;">');
	        	re.push('<i class="iconfont pp disable-color f16" style="margin-bottom:4px;display:block;" data-qtip=\'同行价：'+util.moneyFormat(r.get('ROUTE_INTER_PRICE'),'f14 light-blue-color')+'\'>&#xe65c;</i>');
	        	re.push('<i class="iconfont dd" data-qtip="地接报价详情" style="display:block;color:#999;">&#xe65d;</i>');
	        	re.push('<i class="iconfont dfc" data-qtip="外卖：<span class=\'f16 light-orange-color\'><dfn>￥</dfn>'+r.get('RETAIL_SINGLE_ROOM')+'</span><br>同行：<span class=\'f14 light-blue-color\'><dfn>￥</dfn>'+r.get('INTER_SINGLE_ROOM')+'</span>" style="display:block;color:#999;margin-top:8px;">&#xe65e;</i>');
	        	re.push('</div>');
	        	re.push('<div class="ht20">'+util.moneyFormat(v)+'</div>');
	        	//re.push('<div class="ht20">'+util.moneyFormat(r.get('ROUTE_INTER_PRICE'),'f14',true)+'</div>');
	        	if(isEarnest=='1'){
	        		var earnestInter = util.moneyFormat(r.get('EARNEST_INTER'),'f14 light-blue-color'),
	        			earnestRetail = util.moneyFormat(r.get('EARNEST_RETAIL'),'f14 light-orange-color'),
	        			earnestType = r.get('EARNEST_TYPE');
	        		
	        		re.push('<div style="padding-top:5px;" data-qtip=\'此产品支持定金预付，最低出团费用同行价/人：'+earnestInter+'，外卖价/人:'+earnestRetail+'\'>');
	        		re.push(' <span style="border-radius:2px;font-size:12px;color:#fff;background:#9C27B0;padding:2px 4px;">定</span>');
	        		if(earnestType=='1'){
	        			re.push(' <span style="border-radius:2px;font-size:12px;color:#fff;background:#00796b;padding:2px 4px;" data-qtip=\'代收款定金预付订单，需要供应商确认\' >代</span>');
	        		}
	        		re.push('</div>');
	        	}
	        	return re.join('');
	        }
        });
        this.dockedItems = [{
    		xtype:'toolbar',
    		itemId:'gridtool',
        	items:[{
        		disabled:true,
        		text:'正在初始化...'
        	}]
    	}];
    	/*this.store.on('load',function(s){
	        view = me.view;
	        var rowIdx = 0;
	        s.each(function(rec) {
	            me.getPlugin('expander').toggleRow(rowIdx++, rec);
	        });
    	});*/
        this.callParent();
        
        
        
        
        
        
	},
	columnLines: true,
	selType:'checkboxmodel',
	
	
	
	
	
	
	routePrice :function(rec){
		var routeId = rec.get('ID'),me = this,win,isFullPrice = rec.get('IS_FULL_PRICE')||'0',isEarnest = rec.get('IS_EARNEST')||'0';
		Ext.require('Ext.ux.form.DatePickerEvent',function(){
			win = Ext.create('Ext.window.Window',{
				title:util.windowTitle('&#xe621;','选择日期，参团交通选择',''),
				width:1000,
				height:470,
				modal:true,
				draggable:false,
				resizable:false,
	   			layout:'hbox',
	   			items:[{
	   				margin:'10 5 10 10',
	   				cls:'eventdatepicker selector',
	   				xtype:'eventdatepicker',
					disabledDatesText:'无发团日期',
					width:377,
					height:377,
					style:'border:none;',
					format:'Ymd',
					showToday:false,
					listeners:{
						monthviewchange:function(a,b,c){
							var nextDay = Ext.Date.format(b,'Y-m')+'-01';
							me.loadDateForSeat(nextDay,routeId,win,isFullPrice);
						},
						select :function(p,date,e){
							me.loadPriceForDate(Ext.util.Format.date(date, 'Ymd'),routeId,win,isFullPrice);
						}
					}
	   			},{
	   				flex:1,
	   				margin:'10 10 10 5',
	   				itemId:'pricePlan',
	   				xtype:'panel',
	   				layout:{
	   					type: 'vbox',
   						pack: 'start',
        				align: 'stretch'
	   				},
	   				items:[{
	   					xtype: 'radiogroup',
	   					defaults:{cls:'check'},
	   					items:[{
	   						boxLabel:'<span class="f14 bold cktxt">请选择出团日期</span>'
	   					}]
	   				},{
	   					margin:'5 0 0 0',
	   					xtype:'rprice',
	   					myLoadId:'-',
						myLoadUrl:cfg.getCtx()+'/resource/route/list/plan/price?routeId='+routeId
	   				},{
	   					border:false,
				    	cls: 'data-view',
				        itemSelector: '.data-view-item',
				        xtype: 'dataview',
				        //overItemCls: 'hover',  
				        //trackOver:true,
				        store :Ext.create('Ext.data.Store', {
				        	model:Ext.create('Ext.data.Model',{
				        		fields: ['TRAFFIC_NAME','DATE','STAY_CNT','SEAT']
				        	}),
				        	autoLoad: false, 
						    proxy: {
						        type: 'ajax',
						        noCache:true,
						        url: cfg.getCtx()+'/resource/route/list/route/traffic/info',
						        reader: {
						            type: 'json',
						            rootProperty: 'data',
						            totalProperty:'totalSize'
						        }
						   	}
				        }),
				        tpl: [
				        	'<tpl for="."><div class="data-view-item block" style="margin:1px 40px 5px 0px;width:180px;padding:2px 0px 8px 8px;border:1px dashed #d1d1d1;">',
				        	'<h3 style="font-size:14px;padding:2px 0px;line-height:22px;color:#427fed">{TRAFFIC_NAME}</h3>',
				        	'<div class="ht18">',
				        	'出发日期：<span class="f14">{[this.fd(values.DATE)]}</span>',
				        	'</div>',
				        	'<div class="ht18">',
				        	'停留天数：<span class="f14">{STAY_CNT}</span>',
				        	'</div>',
				        	'<div class="ht18">',
				        	'剩余座位：<span class="f14">{SEAT}</span>',
				        	'</div>',
				        	//'<div class="step"><i class="iconfont">&#xe69e;</i></div>',
				        	'</div></tpl>',{
				        		fd :function(v){
									return Ext.Date.format(Ext.Date.parse(v, "Ymd"), "Y/m/d");
				        		}
				        	}
				        ]
	   				}]
	   			}],
   				buttons:[{
   					xtype:'label',
   					itemId:'goDateLabel'
   				},'->',{
   					hidden:(parseInt(rec.get('IS_OTHER_FEE'))==0),
   					xtype:'button',
   					style:'background:#E64A19;color:#fff;padding:1px;font-size:14px;',
   					text:'<i class="iconfont" style="font-size:18px;">&#xe62a;</i> 其他费用',
   					handler:function(){
   						Ext.Ajax.request({
							url:cfg.getCtx()+'/resource/route/others?routeId='+routeId+'&type=5',
							success:function(response, opts){
								var action = Ext.decode(response.responseText);
								Ext.create('Ext.Window',{
									title:util.windowTitle('&#xe62a;','其他费用',''),
									width:600,
									height:300,
									modal:true,
									draggable:false,
									resizable:false,
									layout:'fit',
									bodyPadding:10,
									items:[{
										autoScroll:true,
										tpl:[
										     '<tpl for=".">',
										     '<h3 style="font-weight:bold;font-size:14px;padding:5px;10px;">{TITLE}</h3>',
										     '<p style="line-height:18px;padding:5px 10px 20px 10px;">{CONTENT}</p>',
										     '</tpl>'
										],
										data:action.data
									}]
								}).show();
							}
						});
   					}
   				},{
   					text:'下一步',
   					handler:function(btn){
   						var goDate = win.down('eventdatepicker').getValue(),
   							plan = win.down('[itemId=pricePlan]').down('radio[name=planName][checked=true]'),
   							planId = '';
   						if(plan){
   							planId = plan.getGroupValue()
   						}
   						if(planId==''){
   							util.alert('请选择交通方案');
   							return;
   						}
   						btn.disable();
   						document.location.href=cfg.getCtx()+'/produce/route/buy?routeId='+routeId+'&goDate='+Ext.Date.format(goDate, "Ymd")+'&planId='+planId+'&planName='+plan.planName;
   					}
   				}]
	   		}).show();
			if(isEarnest=='1'){
				var earnestInter = util.moneyFormat(rec.get('EARNEST_INTER')),
    				earnestRetail = util.moneyFormat(rec.get('EARNEST_RETAIL'));
				win.down('panel#pricePlan').addDocked({
					dock:'bottom',
					html:'<div style="border-top:2px solid #9C27B0;padding:10px;background:#E1BEE7;">此产品支持定金预付，最低出团费用同行价/人：'+earnestInter+'，外卖价/人:'+earnestRetail+'</div>'
				});
			}
			
	   		var firstDay = Ext.Date.format(Ext.Date.getFirstDateOfMonth(new Date()),'Y-m-d');
			me.loadDateForSeat(firstDay,routeId,win,isFullPrice);
		});
	},
	loadPriceForDate :function(date,routeId,win,isFullPrice){
		var pp = win.down('panel[itemId=pricePlan]').down('radiogroup'),
			prp = pp.nextSibling(),
			pdp = prp.nextSibling();
		//win.down('label#goDateLabel').update('出团日期：<span class="f14 orange-color bold">'+Ext.Date.format(Ext.Date.parse(date, "Ymd"), "Y/m/d")+'</span>');
		Ext.Ajax.request({
		    url: cfg.getCtx()+'/resource/route/list/plan?routeId='+routeId+'&startDate='+date,
		    success: function(response, opts) {
		    	 var obj = Ext.decode(response.responseText),
		        	data = obj.data;
		        pp.removeAll(true);
		    	if(data.length>0){
					for(var i=0;i<data.length;i++){
						var title = data[i].PLAN_TITLE;
						if(isFullPrice=='1'){
							title = title.substring(0,title.lastIndexOf('-'));
						}
						pp.add({
							boxLabel: '<span class="f14 bold cktxt">'+title+'</span>',  inputValue: data[i].PLANID,
							planName:data[i].PLAN_TITLE,
							name:'planName',
							checked:i==0,
							listeners:{
								change :function(rd){
									var planId = rd.getGroupValue();
									prp.getStore().load({params:{planId:planId,startDate:date}});
									pdp.getStore().load({params:{routeId:routeId,planId:planId,startDate:date}});
								}
							}
						});
					}
					prp.getStore().load({params:{planId:data[0].PLANID,startDate:date}});
					pdp.getStore().load({params:{routeId:routeId,planId:data[0].PLANID,startDate:date}});
				}else{
					pp.add({
						xtype:'label',
						html:'更换其他日期试试'
					});
				}
		    }
		});	
	},
	loadDateForSeat:function(date,routeId,win){	
		win.mask('数据加载中...');
		Ext.Ajax.request({
		    url: cfg.getCtx()+'/resource/route/calendar?routeId='+routeId+'&startDate='+date,
		    success: function(response, opts) {
		    	win.unmask();
		        var obj = Ext.decode(response.responseText),
		        	data = obj.data,
		        	count = parseInt(obj.totalSize),
		        	enableDates = [],
		        	enableRuleIds = {},
		        	enableEvents = {},
		        	re = '(?:^(?!';
		        if(count>0){
		        	if(data.length>0){
				        for(var i=0;i<data.length;i++){
				        	enableDates.push(data[i].RQ);
				        	enableRuleIds[data[i].RQ] = data[i].ID;
				        	var fmRQ = Ext.Date.parse(data[i].RQ, "Ymd");
				        	enableEvents[parseInt(Ext.Date.format(fmRQ,'d'))] = [
				        		(data[i].ACTUAL_INTER_PRICE||0),//同行价
				        		(data[i].ACTUAL_PRICE||0),//外卖价
				        		(data[i].ACTUAL_SEAT||'无')//座位
				        	].join('-');
				        }
				        re+=enableDates.join('|');
			        }else{
			        	re += '11';
			        }
			        re+='))';
			        win.down('eventdatepicker').setEventTexts(enableEvents);
			        win.down('eventdatepicker').setDisabledDates(new RegExp(re));
			        win.down('eventdatepicker').dataMap = enableRuleIds;
		        }else{
		        	//win.close();
		        	//util.alert('交通没有发布报价');
		        }
		    }
		});	
	}
});