Ext.define('app.view.resource.route.pub.day', {
	extend: 'Ext.panel.Panel',
	requires:['Ext.ux.DataView.ViewSortable','Ext.ux.form.ExtKindEditor','app.view.common.ScenicCombo'],
	xtype:'routeday',
	defaultType:'panel',
    layout: 'border',
    config:{
    	routeId:''
    },
    initComponent: function() {
		var me = this,dayDetail = me.getDayDetail();
		this.items = [{
	    	region:'west',
	    	width:270,
	    	layout:{
	    		type:'vbox',
	    		align: 'stretch'
	    	},
	    	itemId:'dayView',
	    	cls:'day-view',
	    	autoScroll:true,
	    	bodyStyle:'background:#f3f8fc;',
	    	listeners:{
	    		resize:function(p, width, height, oldWidth, oldHeight, eOpts){
	    			p.el.down('#line').setHeight(height);
	    		}
	    	},
	    	html:[
	    		'<div id="line" style="position:absolute;background:#eaeaea;top:15px;left:6px;width:2px;">',
	    		'</div>'
	    	].join('')
	    },{
	    	//border:true,
	    	region:'center',
	    	xtype:'form',
	    	reader: {
	            type: 'json',
	            model:Ext.create('app.model.resource.route.day')
	        },
	    	layout:'fit',
	    	fieldDefaults: {
		        labelWidth: 60,
		        labelAlign:'right'
		    },
	    	itemId:'dayDetailView',
	    	dockedItems: [{
			    xtype: 'toolbar',
			    style:'background: none 0px 0px repeat scroll rgba(251, 180, 76, 0.2);border-bottom: 1px solid rgba(251, 180, 76, 0.5)!important;',
			    dock: 'top',
			    items: [{
			    	xtype:'container',
					html:'<span class="yellow-color f14" style="padding-left:10px;"><i class="iconfont f20">&#xe6ae;</i>  填写完日程请先点击保存，避免数据丢失，所有日程填写完成后，点击下一步</span>'
			    },'->',{
			    	xtype:'label',
			    	itemId:'dayno',
			    	cls:'iconfont dtitle',
			    	html:''
			    },{
			    	itemId:'saveDayButton',
			    	disabled:true,
			    	cls:'blue',
			   		text:'保存',
			   		handler:function(btn){
			   			var form = btn.up('form[itemId=dayDetailView]').getForm();
			   			if (form.isValid()) {
			   				btn.disable();
				            form.submit({
				            	submitEmptyText : false ,
				            	url:cfg.getCtx()+'/resource/route/updateRouteDay',
				                success: function(form, action) {
				                   util.success('保存日程成功');
				                   btn.enable();
				                },
				                failure: function(form, action) {
				                	var state = action.result?action.result.statusCode:0,
				                		errors = ['保存日程异常'];
				                    util.error(errors[0-parseInt(state)]);
				                    btn.enable();
				                }
				            });
				        }
			   		}	
			    }]
			}],
    		items:[{
    			margin:'10 0 0 0',
    			layout: {
			        type: 'vbox',
			        align: 'stretch'
			    },
			    autoScroll:true,
			    hidden:true,
			    items:dayDetail
    		}]
	    }];
	    this.callParent();
	},
	updateRouteId:function(routeId){
		if(routeId){
			this.loadInitDays();
		}
		return routeId;
	},
	/**
     * 加载目的地 并初始化日程数据
     * @private
     */
	loadInitDays :function(){
		var me = this;
		Ext.Ajax.request({
			url:cfg.getCtx()+'/resource/route/listRouteCitys?routeId='+me.getRouteId(),
			success:function(response, opts){
				var obj = Ext.decode(response.responseText),
					data = obj.data,
					dayView = me.down('[itemId=dayView]');
				dayView.removeAll(true);
				for(var i=0;i<data.length;i++){
					var city = data[i],
						m = city.menu,
						defaults = me.getAddDayDefault(city.days);
					Ext.apply(city,{margin:'0 0 0 10',maxWidth:85,tooltip :city.text});
					if(m){
						dayView.add({
						    xtype: 'toolbar',
						    height:45,
						    cls:'first-tool',
						    dock: 'top',
						    items: city
						});
					}else{
						defaults.items[0].plugins = [{
							ptype:'viewsortable',
							listeners:{
								aftersort:function(plugin, sr, r,index){
									me.sortDay.apply(me,[r,sr]);
								}
							}
						}];
						dayView.add(Ext.apply(defaults,{
					    	tbar:[city,'->',{
					    		xtype:'textfield',
					    		width:35,
					    		value:city.days.length,
					    		inputAttrTpl: " data-qtip='停留天数' ",
					    		readOnly:true
					    	},{
					    		tooltip:'加一天',
					    		releId:city.ID,
				        		glyph:'xe631@iconfont',
				        		handler:function(btn){
				        			me.addDay(btn);
				        			return false;
				        		}
					    	},{
					    		tooltip:'复制一天',
				        		glyph:'xe65a@iconfont',
				        		releCityId:city.CITY_ID,
				        		releId:city.ID,
				        		releDayCount:city.days.length,
				        		handler:function(btn){
				        			me.copyDay(btn);
				        			return false;
				        		}
					    	}]
				    	}));
					}
				}
				//默认选择第一个
				dayView.down('dataview').getSelectionModel().select(0);
			}
		});
	},
	/**
     * 动态添加一天
     * @private
     */
	addDay :function(btn){
		btn.disable();
		var me = this,
			p = btn.up('panel'),
			bdv = p.previousSibling().down('dataview'),//前一个dataview
			dv = p.down('dataview'),
			st = dv.getStore(),
			no = st.getCount()+1;
		if(bdv){
			var bdvs = bdv.getStore(),
				bdvscnt = bdvs.getCount();
			if(bdvscnt==0){
				util.alert('请按照顺序先填写上一站的行程');
				return;
			}else{
				var dp = p.up('[itemId=dayView]'),
		        	dvs = dp.query('dataview'),
		        	idx = Ext.Array.indexOf(dvs,dv),
		        	begin = 0;
		        for(var i=0;i<dvs.length;i++){
		        	if(i<=idx){
		        		begin+=dvs[i].getStore().getCount();
		        	}
		        }
				no = begin+1;
			}
		}
		
		//动态加一天
		Ext.Ajax.request({
			url:cfg.getCtx()+'/resource/route/saveRouteDay',
			params:{
				'pm[ROUTE_ID]':me.getRouteId(),
				'pm[NO]':no,
				'pm[ROUTE_CITY_ID]':btn.releId
			},
			success:function(response, opts){
				btn.enable();
				var obj = Ext.decode(response.responseText),
					errors = ['数据添加失败'],
					state = obj?obj.statusCode:0;
				if(!obj.success){
					util.error(errors[0-parseInt(state)]);
				}else{
					me.loadInitDays.call(me);
				}
			}
		});
		
	},
	/**
     * 动态删除一天
     * @private
     */
	removeDay :function(view,record){
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
   				'请核实日程安排信息，避免误删！删除后填写的日程安排信息不可恢复', 
   				'</h3>'
   				].join('')
   			}],
   			buttons:[{
   				text:'确定',
   				handler:function(){
   					win.close();
   					Ext.Ajax.request({
						url:cfg.getCtx()+'/resource/route/delRouteDay',
						params:{
							routeId:me.getRouteId(),
							no:record.get('NO'),
							dayId:record.get('ID')
						},
						success:function(response, opts){
							var obj = Ext.decode(response.responseText),
								errors = ['数据删除失败'],
								state = obj?obj.statusCode:0;
							if(!obj.success){
								util.error(errors[0-parseInt(state)]);
							}else{
								me.loadInitDays.call(me);
							}
						}
					});
   				}
   			}]
   		}).show();
	},
	/**
     * 动态排序一天
     * @private
     */
	sortDay :function(r,record){
		var me = this;
		Ext.Ajax.request({
			url:cfg.getCtx()+'/resource/route/moveRouteDay',
			params:{
				routeId:me.getRouteId(),
				routeCityId:r.get('ROUTE_CITY_ID'),
				dayId:record.get('ID'),
				targetId:r.get('ID')
			},
			success:function(response, opts){
				var obj = Ext.decode(response.responseText),
					errors = ['数据排序失败'],
					state = obj?obj.statusCode:0;
				if(!obj.success){
					util.error(errors[0-parseInt(state)]);
					me.loadInitDays.call(me);
				}else{
					me.loadInitDays.call(me);
				}
			}
		});
	},
	/**
     * 按目的地格式化日程的view对象模板
     * @private
     */
	getAddDayDefault :function(data){
		var me = this;
		return {
			xtype:'panel',
    		bodyStyle:'background:#f3f8fc;',
    		items:[{
    			border:false,
		    	cls: 'data-view',
		        itemSelector: '.data-view-item',
		        xtype: 'dataview',
		        overItemCls: 'hover',  
		        trackOver:true,
		        store :Ext.create('Ext.data.Store', {
		        	model:Ext.create('Ext.data.Model',{
		        		fields: ['ROUTE_CITY_ID','ID','NO']
		        	}),
		        	data:data
		        }),
		        tpl: [
		        	'<tpl for="."><div class="data-view-item">',
		        	'<span>{[this.covertNo(values.NO)]}</span>',
		        	'<i class="iconfont hover del" data-qtip="删除">&#xe62c;</i>',
		        	'<i class="iconfont select">&#xe618;</i>',
		        	'</div></tpl>',{
		        		covertNo:function(no){
		        			return parseInt(no)<10?'0'+no:no;
		        		}
		        	}
		        ],
		        listeners:{
		        	select :function(model, selected, eOpts){
		        		var p = this.up().up('[itemId=dayView]'),
		        			dvs = p.query('dataview');
		        		for(var i=0;i<dvs.length;i++){
		        			if(dvs[i].id!=this.id){
		        				dvs[i].getSelectionModel().deselectAll();
		        			}
		        		}
		        		var dayDetailView = me.down('form[itemId=dayDetailView]');
		        		dayDetailView.down('panel').show();
		        		dayDetailView.down('hidden[itemId=dayid]').setValue(selected.get('ID'));
		        		dayDetailView.down('hidden[itemId=dayno]').setValue(selected.get('NO'));
		        		dayDetailView.down('button[itemId=saveDayButton]').enable();
		        		dayDetailView.down('label[itemId=dayno]').update(parseInt(selected.get('NO'))>9?selected.get('NO'):'0'+selected.get('NO'));
		        		top.Ext.getBody().mask('加载日程明细...');
		        		dayDetailView.getForm().load({
				            url: cfg.getCtx()+'/resource/route/routeDayDetail?dayId='+selected.get('ID'),
				            //waitMsg: 'Loading...',
				            success :function(form,action){
				            	top.Ext.getBody().unmask();
				            	var formData = util.pmModel(action.result);
				            	form.reset();
				            	//设置目的地ID 景点查询关联
				            	dayDetailView.down('hidden[itemId=daycityid]').setValue(selected.get('CITY_ID'));
								form.setValues(formData);
				            	var dts = action.result.data.routeDayDetails;
				            	me.removeAllPlan();
								me.addPlan(dts);
				            }
				        });
		        		
		        	},
		        	itemclick:function(view, record, item, index, e, eOpts){
		        		if(e.target.tagName=='I'&&e.target.className.indexOf('del')!=-1){
		        			me.removeDay.apply(me,[view,record]);
		        		}
		        	}
		        }
    		}]
		};
	},
	/**
     * 日程详情的对象模板
     * @private
     */
	getDayDetail :function(){
		var me = this;
		return [{
			xtype:'hidden',
	    	name:'pm[ROUTE_ID]',
			value:me.getRouteId()
		},{
	    	xtype:'hidden',
	    	name:'pm[ID]',
	    	itemId:'dayid'
	    },{
	    	xtype:'hidden',
	    	name:'pm[NO]',
	    	itemId:'dayno'
	    },{
	    	xtype:'hidden',
	    	itemId:'daycityid'
	    },{
			xtype:'fieldcontainer',
			fieldLabel:'出发地',
			layout: 'hbox',
			items:[{
				xtype:'textfield',
				emptyText:'城市/景点',
				name:'pm[BEGIN_CITY]',
				width:150
			},{
				xtype: 'splitter'
			},{
				xtype:'combo',
				value:'飞机',
				editable:false,
				name:'pm[TOOL]',
				hiddenName:'pm[TOOL]',
				width:75,
				typeAhead: true,
	            triggerAction: 'all',
	            store: cfg.getTravelTools()
			},{
				xtype: 'splitter'
			},{
				xtype:'textfield',
				emptyText:'目的地-城市/景点',
				name:'pm[END_CITY]',
				width:150
			},{
				xtype: 'splitter'
			},{
				xtype:'combo',
				value:'',
				editable:false,
				name:'pm[TOOL1]',
				hiddenName:'pm[TOOL1]',
				width:75,
				typeAhead: true,
	            triggerAction: 'all',
	            store: cfg.getTravelTools()
			},{
				xtype: 'splitter'
			},{
				xtype:'textfield',
				emptyText:'目的地-城市/景点',
				name:'pm[END_CITY1]',
				width:150
			},{
				xtype: 'splitter'
			},{
				xtype:'textfield',
				emptyText:'自定义标题，优先显示',
				name:'pm[TITLE]',
				flex:1
			}]
		},{
			xtype:'textfield',
			name:'pm[TODAY_TIPS]',
			fieldLabel:'当日提示',
			maxLength:200,
    		cls:'text-cls',
    		listeners:{
				validitychange:function(nf,is){
					if(!is){
						var v = nf.getValue();
						nf.setValue(v.substring(0,200));
					}
				}
			}
		},{
			xtype:'textfield',
			name:'pm[HOTEL_TIPS]',
			fieldLabel:'住宿提示',
			maxLength:200,
    		cls:'text-cls',
    		listeners:{
				validitychange:function(nf,is){
					if(!is){
						var v = nf.getValue();
						nf.setValue(v.substring(0,200));
					}
				}
			}
		},{
			xtype:'textfield',
			name:'pm[PAY_TIPS]',
			fieldLabel:'自费提示',
			maxLength:200,
    		cls:'text-cls',
    		listeners:{
				validitychange:function(nf,is){
					if(!is){
						var v = nf.getValue();
						nf.setValue(v.substring(0,200));
					}
				}
			}
		},{
			xtype:'fieldcontainer',
			fieldLabel:'三餐/早餐',
			layout: 'hbox',
			items:[{
				xtype:'textfield',
				emptyText:'不填默认为自理',
				name:'pm[BREAKFAST]',
				flex:1,
				maxLength:100,
	    		cls:'text-cls',
	    		listeners:{
					validitychange:function(nf,is){
						if(!is){
							var v = nf.getValue();
							nf.setValue(v.substring(0,100));
						}
					}
				}
			},{
				xtype: 'splitter'
			},{
				xtype: 'label',
				margin:'5 0 0 0',
				html:'午餐:'
			},{
				xtype: 'splitter'
			},{
				xtype:'textfield',
				emptyText:'不填默认为自理',
				name:'pm[LUNCH]',
				flex:1,
				maxLength:100,
	    		cls:'text-cls',
	    		listeners:{
					validitychange:function(nf,is){
						if(!is){
							var v = nf.getValue();
							nf.setValue(v.substring(0,100));
						}
					}
				}
			},{
				xtype: 'splitter'
			},{
				xtype: 'label',
				margin:'5 0 0 0',
				html:'晚餐:'
			},{
				xtype: 'splitter'
			},{
				xtype:'textfield',
				emptyText:'不填默认为自理',
				name:'pm[DINNER]',
				flex:1,
				maxLength:100,
	    		cls:'text-cls',
	    		listeners:{
					validitychange:function(nf,is){
						if(!is){
							var v = nf.getValue();
							nf.setValue(v.substring(0,100));
						}
					}
				}
			}]
		},{
			xtype:'panel',
			itemId:'plan',
			flex:1,
			border:true,
			layout:'border',
			items:[{
		    	region:'west',
		    	cls:'tc',
		    	dockedItems: {
			        dock: 'top',
			        style:'padding:1px;',
			        xtype: 'toolbar',
			        items:[{
			        	xtype:'fieldcontainer',
						margin:'6 0 6 24',			        	
			        	items:[{
			        		width:70,
			        		xtype:'button',
			        		ui:'default-toolbar',
			        		glyph:'xe62b@iconfont',
			        		text:'加安排',
			        		handler:function(){
			        			me.addPlan();
			        		}
			        	},{
			        		flex:1,
			        		xtype:'label'
			        	}]
			        }]
			    },
		    	width:120,
		    	itemId:'title',
	        	overflowY:'auto'
		    },{
		    	xtype:'container',
		    	itemId:'content',
		    	region:'center',
		    	layout: {
		    		type:'card',
		    		deferredRender:false
		    	}
		    }]
		}];
	},
	addPlan :function(data){
		var planContainer = this.down('form[itemId=dayDetailView]').down('panel[itemId=plan]'),
			titleContainer = planContainer.down('container[itemId=title]'),
			contentContainer = planContainer.down('container[itemId=content]'),
			no = titleContainer.items.length;
		var l = contentContainer.getLayout();
		if(data&&data.length>0){
			for(var i=0;i<data.length;i++){
				no = titleContainer.items.length;
				titleContainer.add(this.titleTpl(no,data[i]));
				contentContainer.add(this.contentTpl(no,data[i]));
			    l.setActiveItem(no);
			}
			l.setActiveItem(0);
		}else{
			titleContainer.add(this.titleTpl(no));
			contentContainer.add(this.contentTpl(no));
		}
	},
	removeAllPlan :function(){
		var planContainer = this.down('form[itemId=dayDetailView]').down('panel[itemId=plan]'),
			titleContainer = planContainer.down('container[itemId=title]'),
			contentContainer = planContainer.down('container[itemId=content]');
		titleContainer.removeAll(true);
		contentContainer.removeAll(true);
	},
	removePlan :function(no){
		var planContainer = this.down('form[itemId=dayDetailView]').down('panel[itemId=plan]'),
			titleContainer = planContainer.down('container[itemId=title]'),
			contentContainer = planContainer.down('container[itemId=content]'),
			titleItem = titleContainer.items.get(no),
			contentItem = contentContainer.items.get(no);
		//titleContainer.remove(titleContainer.items.get(no),true);
		//contentContainer.remove(contentContainer.items.get(no),true);
		titleItem.hide();
		contentItem.hide();
		
		titleItem.down('combo').disable();
		contentItem.getDockedItems('toolbar[dock="top"]')[0].removeAll(true);
		contentItem.down('extkindeditor').disable();
	},
	titleTpl :function(no,record){
		var me = this,record = record ||{};
		return {
        	xtype:'fieldcontainer',
        	layout:'hbox',
        	no:no,
        	listeners:{
        		afterrender:function(fc){
        			var el = fc.el,
        				planContainer = me.down('form[itemId=dayDetailView]').down('panel[itemId=plan]'),
						content = planContainer.down('container[itemId=content]');
        			fc.el.on('click',function(e, t, eOpts){
        				var m = Ext.get(this),
        					ms = m.parent().query('.time',false);
        					cs = m.parent('.tc').next().query('.cnt',false);
        					for(var i=0;i<ms.length;i++){ms[i].removeCls('select');}
        					m.addCls('select');
        					var l = content.getLayout();
			        		l.setActiveItem(fc.no);
        				if(t.tagName=='I'&&t.className.indexOf('del')!=-1){
		        			me.removePlan(fc.no);
		        		}
        			});
        			fc.el.hover(function(){
        				Ext.get(this).addCls('hover');
        			},function(){
        				Ext.get(this).removeCls('hover');
        			})
        		}
        	},
       		cls:'time'+(no==0?' select':''),
        	width:118,
        	items:[{
	       		xtype:'label',
	       		width:20,
	       		cls:'del',
	       		margin:'6 0 0 2',
	       		html:(no==0?'':'<i data-qtip="删除" class="iconfont money-color del">&#xe62c;</i>')
	       	},{
	       		xtype:'combo',
	       		width:70,
	       		allowBlank:false,
	       		value:record.TITLE,
	       		name:'lp['+no+'].TITLE',
	       		hiddenName:'lp['+no+'].TITLE',
				listConfig:{maxHeight:100},
				emptyText:'时间',
				typeAhead: true,
				mode: 'local',
				forceSelection : true,  
				selectOnFocus : true,
	            triggerAction: 'all',
	            store: [
	            	'全天','上午','下午','07:00','08:00',
	            	'09:00','10:00','11:00','12:00',
	            	'13:00','14:00','15:00','16:00',
	            	'17:00','18:00','19:00','20:00',
	            	'21:00','22:00','23:00','00:00'
	            ]
	       	}]
        };
	},
	contentTpl :function(no,record){
		var me = this,record = record || {};
		return {
			cls:'cnt',
    		bodyPadding:'1 1 1 0',
    		layout:'fit',
    		no:no,
	    	dockedItems: {
		        dock: 'top',
		        itemId:'scenics',
		        style:'padding-left:0px;',
		        xtype: 'toolbar',
		        items:[{
		        	xtype:'sceniccombo',
					emptyText:'时间景点-简拼/全拼/汉字',
					pageSize:5,
					no:no,
					listeners:{
						beforequery :function(queryPlan, eOpts){
							var combo = queryPlan.combo,
								store = combo.getStore(),
								cityId = me.down('form[itemId=dayDetailView]').down('hidden[itemId=daycityid]').getValue();
							store.getProxy().setExtraParams({PID:cityId});
						},
						afterrender:function(combo){
							if(record.scenics){
								me.removeAllScenic(combo);
								for(var r,i=0;i<record.scenics.length;i++){
									r = record.scenics[i];
									me.addScenic(combo,r.SCENIC_ID,r.SCENIC_NAME);
								}
							}
							/*if(no>0){
								var ke = combo.up('toolbar').up('panel').down('extkindeditor');
								setTimeout(function(){
									ke.fireEvent('boxready',ke);
								},500);
							}*/
						},
						select:function(combo,r){
							combo.reset();
							me.addScenic(combo,r.get('SCENIC_ID'),r.get('SCENIC_TEXT'));
						}
					}
		        }]
		    },
		    items:[{
		    	offsetWidth:2,
		    	offsetHeight:-28,
	    		tools:[
	           		'source','forecolor', 'hilitecolor', 'bold','italic', 'underline', 
	           		'strikethrough', 'lineheight','wordpaste',
	           		'justifyleft', 'justifycenter', 'justifyright',
					'justifyfull', 'insertorderedlist', 'insertunorderedlist', 
					'indent', 'outdent','image', 'multiimage','fullscreen'
				],
				name:'lp['+no+'].CONTENT',
				defaultValue:record.CONTENT,
				xtype: 'extkindeditor'
		    }]
    	};
	},
	addScenic :function(combo,sid,sname){
		var me = this,no = combo.no,ct = combo.up('toolbar[itemId=scenics]');
		if(ct.down('fieldcontainer[itemId='+sid+']')){
			return;
		}
		ct.add({
			xtype:'fieldcontainer',
			itemId:sid,
			margin:'0 2 0 0',
			items:[{
				xtype:'hidden',
				name:'lp['+no+'].SCENIC',
				value: sid+'-'+sname
			},{
				flex:1,
				tooltip:sname,
				cls:'tag-split-btn remove',
				xtype:'splitbutton',
				text:sname,
				listeners:{
					arrowclick :function(sp){
						var zj = sp.up('fieldcontainer');
						zj.up('toolbar[itemId=scenics]').remove(zj,true);
					}
				}
			}]
		});
	},
	removeAllScenic :function(combo){
		var me = this,no = combo.no,ct = combo.up('toolbar[itemId=scenics]');
		for(var i=1;i<ct.items.length;i++){
			ct.remove(ct.items.get(i),true);
		}
	},
	copyDay:function(btn){
		var me = this;
			p = btn.up('panel'),
			bdv = p.previousSibling().down('dataview'),//前一个dataview
			dv = p.down('dataview'),
			st = dv.getStore(),
			no = st.getCount()+1;
		if(bdv){
			var bdvs = bdv.getStore(),
				bdvscnt = bdvs.getCount();
			if(bdvscnt==0){
				util.alert('请按照顺序先填写上一站的行程');
				return;
			}else{
				var dp = p.up('[itemId=dayView]'),
		        	dvs = dp.query('dataview'),
		        	idx = Ext.Array.indexOf(dvs,dv),
		        	begin = 0;
		        for(var i=0;i<dvs.length;i++){
		        	if(i<=idx){
		        		begin+=dvs[i].getStore().getCount();
		        	}
		        }
				no = begin+1;
			}
		}
		var store = Ext.create('Ext.data.Store', {
            //pageSize: 50,
            groupField:'ROUTE_TITLE',
            autoLoad: true,
            model:Ext.create('Ext.data.Model',{
            	fields: [
            		'CITY_NAME','CITY_ID','ROUTE_ID','ROUTE_TITLE',
            		'NO','ID','TITLE','BEGIN_CITY','END_CITY','END_CITY1','TOOL','TOOL1'
            	]
            }),
            proxy: {
		         type: 'ajax',
		         noCache:true,
		         url: cfg.getCtx()+'/resource/route/end/city/list?cityId='+btn.releCityId,
		         reader: {
		             rootProperty: 'data',
            		 totalProperty: 'totalSize'
		         }
		     }
        });
		var grid = Ext.create('Ext.grid.Panel',{
			loadMask: true,
			store:store,
			viewConfig:{
				stripeRows:false
			},
			features: [{
		        groupHeaderTpl: '<span class="blue-color f14">{name}</span>',
		        ftype: 'groupingsummary'
		    }],
		    columnLines: true,
		    selModel :{mode:'SINGLE'},
			selType:'checkboxmodel',
		    columns:[/*Ext.create('Ext.grid.RowNumberer',{width:25}),*/{
		    	text: '日程编号',
		        width:100,
		        align:'center',
		        dataIndex: 'NO',
		        renderer: function(v,c,r){
		        	return [
		        		'<div class="ht20">第'+v+'天</div>'
		        	].join('');
		        }
		    },{
		    	text: '出发地',
		    	flex:1,
		    	dataIndex: 'BEGIN_CITY',
		    	renderer: function(v,c,r){
		        	return [
		        		'<div class="ht20">'+(v||'')+'</div>'
		        	].join('');
		        }
		    },{
		    	text: '交通',
		    	flex:1,
		    	dataIndex: 'TOOL',
		    	renderer: function(v,c,r){
		        	return [
		        		'<div class="ht20">'+(v||'')+'</div>'
		        	].join('');
		        }
		    },{
		    	text: '目的地',
		    	flex:1,
		    	dataIndex: 'END_CITY',
		    	renderer: function(v,c,r){
		        	return [
		        		'<div class="ht20">'+(v||'')+'</div>'
		        	].join('');
		        }
		    },{
		    	text: '交通',
		    	flex:1,
		    	dataIndex: 'TOOL1',
		    	renderer: function(v,c,r){
		        	return [
		        		'<div class="ht20">'+(v||'')+'</div>'
		        	].join('');
		        }
		    },{
		    	text: '目的地',
		    	flex:1,
		    	dataIndex: 'END_CITY1',
		    	renderer: function(v,c,r){
		        	return [
		        		'<div class="ht20">'+(v||'')+'</div>'
		        	].join('');
		        }
		    }]
		});
		var win = Ext.create('Ext.window.Window',{
			title:util.windowTitle('&#xe70b;','复制一天',''),
			width:800,
	    	height:450,
			modal:true,
			draggable:false,
			resizable:false,
			layout:'fit',
			items:[grid],
			buttons:[{
				text:'确定',
				handler:function(b){
					var grid = win.down('gridpanel'),sel;
					if(sel = util.getSingleModel(grid)){
						Ext.Ajax.request({
							url:cfg.getCtx()+'/resource/route/copy/day?dayId='+sel.get('ID')+'&dayCount='+no+'&routeId='+me.getRouteId()+'&routeCityId='+btn.releId,
							success:function(response){
								util.success('复制成功！');
								me.loadInitDays.call(me);
								win.close();
							}
						});
					}
				}
			},{
				text:'取消',
				cls:'disable',
				handler:function(){
					win.close();
				}
			}]
		}).show();
	}
});