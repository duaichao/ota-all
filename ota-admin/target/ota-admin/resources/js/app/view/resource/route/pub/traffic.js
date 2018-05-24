Ext.define('app.view.resource.route.pub.traffic', {
	extend: 'Ext.panel.Panel',
	layout: 'border',
	xtype:'rtraffic',
	routeId:'',
    setRouteId:function(routeId){
    	this.routeId = routeId;
    },
	initComponent: function() {
		var me = this;
		this.items = [{
	    	region:'west',
	    	width:180,
	    	margin:'0 2 0 0',
	    	layout:{
	    		type:'vbox',
	    		align: 'stretch'
	    	},
	    	itemId:'planView',
	    	cls:'day-view',
	    	autoScroll:true,
	    	border:true,
	    	bodyStyle:'background:#f3f8fc;',
	    	dockedItems: [{
			    xtype: 'toolbar',
			    cls:'no-sign',
			    dock: 'top',
			    items: ['->',{
			    	tooltip:'添加方案',
	        		glyph:'xe631@iconfont',
	        		handler:function(btn){
	        			me.addPlan(btn);
	        			return false;
	        		}
			    }]
			}],
	    	items:[{
	    		tpl: [
		        	'<tpl for="."><div class="data-view-item plan">',
		        	'<span>方案{NO}</span>',
		        	'<i class="iconfont hover del" data-qtip="删除">&#xe62c;</i>',
		        	'<i class="iconfont select">&#xe618;</i>',
		        	'</div></tpl>'
		        ],
		        cls: 'data-view nop',
		        itemSelector: '.data-view-item',
		        xtype: 'dataview',
		        overItemCls: 'hover',  
		        trackOver:true,
		        store :Ext.create('Ext.data.Store', {
		        	model:Ext.create('Ext.data.Model',{
		        		fields: ['ROUTE_ID','ID','NO','TITLE']
		        	}),
		        	autoLoad: true, 
		        	listeners:{
		        		beforeload:function(s){
		        			s.getProxy().setExtraParams({routeId:me.routeId});
		        		}
		        	},
				    proxy: {
				        type: 'ajax',
				        noCache:true,
				        url: cfg.getCtx()+'/resource/route/traffic/plan/list',
				        reader: {
				            type: 'json',
				            rootProperty: 'data',
				            totalProperty:'totalSize'
				        }
				   	}
		        }),
		        listeners:{
			        	select :function(model, r, eOpts){
			        		me.down('hidden[itemId=planId]').setValue(r.get('ID'));
			        		me.down('panel[itemId=planDetailView]').down('toolbar').down('textfield[itemId=planName]').setValue(r.get('TITLE'));
			        		me.down('form').show();
			        		me.mask('读取中...');
			        		me.down('panel[itemId=planDetailView]').removeAll(true);
			        		//加载已保存的交通s
			        		Ext.Ajax.request({
								url:cfg.getCtx()+'/resource/route/traffic/detail/list?planId='+r.get('ID'),
								success:function(response, opts){
									me.unmask();
									var action = Ext.decode(response.responseText),
										btn = me.down('form').down('button[itemId=addDetailPlanBtn]'),
										data = action.data;
									for(var i=0;i<data.length;i++){
										me.addDetailPlan(btn,data[i]);
									}
								}
							});
			        		
			        		
			        		
			        		
			        	},
			        	itemclick:function(view, record, item, index, e, eOpts){
			        		if(e.target.tagName=='I'&&e.target.className.indexOf('del')!=-1){
			        			me.removePlan.apply(me,[view,record]);
			        		}
			        	}
			    }
	    	}]
	    },{
	    	margin:'1 0 0 0',
	    	region:'center',
	    	bodyPadding:'0 1 1 1',
	    	xtype:'form',
	    	hidden:true,
	    	layout:'fit',
	    	fieldDefaults: {
		        labelWidth: 60,
		        labelAlign:'right'
		    },
	    	dockedItems: [{
			    xtype: 'toolbar',
			    style:'background:#fff;',
			    ui:'footer',
			    dock: 'bottom',
			    items: ['->',{
			    	itemId:'saveButton',
			   		text:'保存',
			   		handler:function(btn){
			   			var f = btn.up('form'),
			   				form = f.getForm(),
			   				vs = f.down('panel[itemId=planDetailView]');
			   				
			   			if(vs.items.length==0){util.alert('没有配置交通');return;}
			   			if (form.isValid()) {
			   				btn.disable();
				            form.submit({
				            	submitEmptyText:false ,
				            	url:cfg.getCtx()+'/resource/route/traffic/detail/save?routeId='+me.routeId,
				                success: function(form, action) {
				                   util.success('配置交通成功');
				                   btn.enable();
				                   me.down('dataview').getStore().load();
				                },
				                failure: function(form, action) {
				                	var state = action.result?action.result.statusCode:0,
				                		errors = ['配置交通异常', '出发交通编号为1','编号重复'];
				                    util.error(errors[0-parseInt(state)]);
				                    btn.enable();
				                }
				            });
				        }
			   		}	
			    }]
			}],
    		items:[{
    			layout: 'fit',
			    items:[{
					xtype:'hidden',
			    	name:'pm[PLAN_ID]',
					itemId:'planId',
				},{
			    	hiddenLable:true,
			    	itemId:'planDetailView',
			    	xtype:'panel',
			    	border:true,
			    	autoScroll:true,
			    	anchor:'100% 100%',
			    	bodyPadding:'0 10 10 10',
			    	dockedItems: [{
					    xtype: 'toolbar',
					    cls:'no-sign',
					    dock: 'top',
					    items: [{
					    	fieldLabel:'标题',
					    	itemId:'planName',
					    	name:'pm[TITLE]',
					    	allowBlank:false,
					    	labelWidth: 30,
					    	width:250,
					    	xtype:'textfield'
					    },'->',{
					    	tooltip:'添加交通',
					    	itemId:'addDetailPlanBtn',
			        		glyph:'xe62b@iconfont',
			        		handler:function(btn){
			        			me.addDetailPlan(btn);
			        			return false;
			        		}
					    }]
					}],
			    	layout:{
			    		type:'vbox',
			    		align: 'stretch'
			    	}
			    }]
    		}]
	    }];
	    this.callParent();
	    
	    this.on('afterrender',function(){
	    	me.setRouteId(me.up('window').routeId||'')
	    });
	},
	addDetailPlan :function(btn,data){
		var me = this,view　= btn.up('panel[itemId=planDetailView]');
		data = data || {};
		view.add({
			style:'margin:5px 0',
			xtype:'fieldcontainer',
			layout: 'hbox',
			items:[{
				xtype:'hidden',
				name:'ID',
				value:(data.ID||'')
			},{
				xtype:'numberfield',
				name:'ORDER_BY',
				minValue:1,
				allowBlank:false,
				width:70,
				value:data.ORDER_BY?data.ORDER_BY:(view.items.length+1),
				emptyText:'序号'
			},{
				xtype: 'label',
				style:'margin:5px 8px 0px 8px;',
				html:'从'
			},{
				xtype:'hidden',
				name:'BEGIN_CITY_ID',
				value:(data.BEGIN_CITY_ID||'')
			},{
				emptyText:'出发地',
				name:'BEGIN_CITY_NAME',
				hiddenName:'BEGIN_CITY_NAME',
				xtype:'combo',
				width:150,
				queryParam: 'all',
			    displayField: 'CITY_NAME',
			    allowBlank:false,
			    focusOnToFront:true,
			    editable :false,
			    forceSelection:true,
			    queryMode: 'remote',
			    triggerAction: 'all',
			    valueField: 'CITY_NAME',
			    hidValue:(data.BEGIN_CITY_NAME||''),
			    listeners:{
			    	select :function(combo, r, eOpts){
			    		combo.previousSibling().setValue(r.get('CITY_ID'));
			    		var　endC = combo.nextSibling().nextSibling().nextSibling(),
			    			tC = endC.nextSibling().nextSibling();
			    			endC.enable();
			    		 tC.beginCity = r.get('CITY_NAME');
			    		 if(tC.endCity!=''){
			    		 	tC.getStore().load();
			    		 }
			    	},
			    	afterrender:function(c){
			    		c.getStore().on('load',function(){
			    			c.setValue(c.hidValue);
			    			c.nextSibling().nextSibling().nextSibling().nextSibling().nextSibling().beginCity = c.hidValue;
			    		});
			    	}
			    },
			    store:Ext.create('Ext.data.Store',{
			    	autoLoad:true,
				    proxy: {
				        type: 'ajax',
				        model:Ext.create('Ext.data.Model',{
				        	fields: ['CITY_ID','CITY_NAME']
				        }),
				        url: cfg.getCtx()+'/resource/route/list/city?routeId='+me.routeId,
				        reader: {
				            type: 'json',
				            rootProperty: 'data',
				            totalProperty:'totalSize'
				        }
				   	}
			    })
			},{
				xtype: 'label',
				style:'margin:5px 8px 0px 8px;',
				html:'到'
			},{
				xtype:'hidden',
				value:(data.END_CITY_ID||''),
				name:'END_CITY_ID'
			},{
				hidValue:(data.END_CITY_NAME||''),
				emptyText:'目的地',
				xtype:'combo',
				disabled:true,
				name:'END_CITY_NAME',
				hiddenName:'END_CITY_NAME',
				width:150,
				queryParam: 'all',
			    displayField: 'CITY_NAME',
			    allowBlank:false,
			    editable :false,
			    focusOnToFront:true,
			    forceSelection:true,
			    queryMode: 'remote',
			    triggerAction: 'all',
			    valueField: 'CITY_NAME',
			    listeners:{
			    	select :function(combo, r, eOpts){
			    		combo.previousSibling().setValue(r.get('CITY_ID'));
			    		combo.nextSibling().nextSibling().enable();
			    		combo.nextSibling().nextSibling().endCity = r.get('CITY_NAME');
			    		combo.nextSibling().nextSibling().getStore().load();
			    	},
			    	afterrender:function(c){
			    		c.getStore().on('load',function(){
			    			c.setValue(c.hidValue);
			    			c.nextSibling().nextSibling().endCity = c.hidValue;
			    			if(c.hidValue){
			    				c.enable();
			    			}
			    		});
			    	}
			    },
			    store:Ext.create('Ext.data.Store',{
			    	autoLoad:true,
				    proxy: {
				        type: 'ajax',
				        model:Ext.create('Ext.data.Model',{
				        	fields: ['CITY_ID','CITY_NAME']
				        }),
				        url: cfg.getCtx()+'/resource/route/list/city?routeId='+me.routeId,
				        reader: {
				            type: 'json',
				            rootProperty: 'data',
				            totalProperty:'totalSize'
				        }
				   	}
			    })
			},{
				xtype: 'label',
				style:'margin:5px 8px 0px 8px;',
				html:'乘坐'
			},{
				hidValue:(data.TRAFFIC_NAME||''),
				emptyText:'选择交通',editable :false,
				name:'TRAFFIC_NAME',
				hiddenName:'TRAFFIC_NAME',
				xtype:'combo',
				flex:1,
    			queryParam: 'all',
				allowBlank:false,
				disabled:true,
			    displayField: 'TITLE',
			    focusOnToFront:true,
			    forceSelection:true,
			    queryMode: 'remote',
			    triggerAction: 'all',
			    valueField: 'TITLE',
			    beginCity :'',
			    endCity :'',
			    listeners:{
			    	select :function(combo, r, eOpts){
			    		combo.nextSibling().setValue(r.get('ID'));
			    	},
		        	afterrender :function(c){
	        			c.getStore().on('beforeload',function(s){
		        			s.getProxy().setExtraParams({
		        				'code':'1',
		        				'pm[START_CITY]':c.beginCity,
		        				'pm[END_CITY]':c.endCity
		        			});
	        			});
	        			if(c.hidValue){
	        				c.getStore().load();
	        			}
	        			c.getStore().on('load',function(){
			    			c.setValue(c.hidValue);
			    			if(c.hidValue){
			    				c.enable();
			    			}
			    		});
	        		}
	        	},
	        	listConfig:{
			    	minWidth:360,
			    	itemTpl:[
						 '<tpl for=".">',
				            '<li class="city-item">{TITLE}<span>{STATION}</span></li>',
				        '</tpl>'
					]
			    },
			    store:Ext.create('Ext.data.Store',{
			    	autoLoad:false,
				    proxy: {
				    	noCache:true,
				        type: 'ajax',
				        model:Ext.create('Ext.data.Model',{
				        	fields: ['ID','TITLE','STATION']
				        }),
				        url: cfg.getCtx()+'/resource/traffic/list?routeId='+me.routeId+'&isSale=0&isPub=1',
				        reader: {
				            type: 'json',
				            rootProperty: 'data',
				            totalProperty:'totalSize'
				        }
				   	}
			    })
			},{
				name:'TRAFFIC_ID',
				value:(data.TRAFFIC_ID||''),
				xtype:'hidden'
			},{
				xtype:'button',
				ui:'default-toolbar',
				glyph:'xe692@iconfont',
				margin:'0 0 0 8',
				handler:function(b){
					if(b.previousSibling().getValue()==''){
						util.alert('没有选择交通，无法查看交通报价');
						return;
					}
					me.detailTrafficPrice(b,b.previousSibling().getValue());
				}
			},{
				xtype: 'label',
				style:'margin:5px 8px 0px 8px;',
				html:'停留'
			},{
				xtype:'numberfield',
				value:(data.STAY_CNT||'0'),
				name:'STAY_CNT',
				allowBlank:false,
				minValue:0,
				width:70,
			},{
				xtype: 'label',
				style:'margin:5px 8px 0px 3px;',
				html:'天'
			},{
				xtype:'button',
				ui:'default-toolbar',
				tooltip:'删除交通',
				glyph:'xe62c@iconfont',
				handler:function(b){
					view.remove(b.up('fieldcontainer'),true);
				}
			}]
		});
	},
	detailTrafficPrice :function(btn,trafficId){
		var me = this,win;
		Ext.require('Ext.ux.form.DatePickerEvent',function(){
			win = Ext.create('Ext.window.Window',{
				title:util.windowTitle(util.glyphToFont(btn.glyph),'查看交通报价',''),
				width:700,
				height:377,
				modal:true,
				draggable:false,
				resizable:false,
				bodyPadding:1,
	   			layout:'fit',
	   			items:[{
	   				xtype:'eventdatepicker',
					disabledDatesText:'无发团日期',
					format:'Ymd',
					showToday:false,
					width:500,
					listeners:{
						monthviewchange:function(a,b,c){
							var nextDay = Ext.Date.format(b,'Y-m')+'-01';
							me.loadDateForSeat(nextDay,trafficId,win);
						}
					}
	   			}]
	   		}).show();
	   		var firstDay = Ext.Date.format(Ext.Date.getFirstDateOfMonth(new Date()),'Y-m-d');
			me.loadDateForSeat(firstDay,trafficId,win);
		});
	},
	loadDateForSeat:function(date,trafficId,win){	
		win.mask('数据加载中...');
		Ext.Ajax.request({
		    url: cfg.getCtx()+'/resource/traffic/dates?trafficId='+trafficId+'&month='+date,
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
				        		(data[i].ACTUAL_SEAT||0)//座位
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
	},
	addPlan :function(btn){
		var me = this;
		btn.disable();
		//动态加方案
		Ext.Ajax.request({
			url:cfg.getCtx()+'/resource/route/traffic/plan/save',
			params:{
				'routeId':me.routeId
			},
			success:function(response, opts){
				btn.enable();
				var obj = Ext.decode(response.responseText),
					errors = ['数据添加失败'],
					state = obj?obj.statusCode:0;
				if(!obj.success){
					util.error(errors[0-parseInt(state)]);
				}else{
					me.down('dataview').getStore().load();
				}
			}
		});
	},
	removePlan :function(view,record){
		var me = this,
			win = Ext.create('Ext.window.Window',{
   			title: util.windowTitle('','信息提示',''),
   			width:300,
   			height:180,
   			draggable:false,
			resizable:false,
			closable:false,
			modal:true,
   			bodyStyle:'background:#fff;padding:10px;',
   			layout:'fit',
   			items:[{
   				html:[
   				'<h3 class="alert-box">',
   				'请核实交通线路方案信息，避免误删！删除后配置的交通线路方案不可恢复', 
   				'</h3>'
   				].join('')
   			}],
   			buttons:[{
   				text:'确定',
   				handler:function(){
   					win.close();
   					Ext.Ajax.request({
						url:cfg.getCtx()+'/resource/route/traffic/plan/del?planId='+record.get('ID')+'&routeId='+me.routeId,
						success:function(response, opts){
							var obj = Ext.decode(response.responseText),
								errors = ['数据删除失败'],
								state = obj?obj.statusCode:0;
							if(!obj.success){
								util.error(errors[0-parseInt(state)]);
							}else{
								me.down('form').reset();
								me.down('form').hide();
								me.down('dataview').getStore().load();
							}
						}
					});
   				}
   			}]
   		}).show();
	}
});