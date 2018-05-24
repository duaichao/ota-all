Ext.define('app.view.b2b.user.home', {
    extend: 'Ext.Panel',
    border:false,
    style:'background:transparent;',
    bodyStyle:'background:transparent;',
    config:{
    	layout: 'fit'
    },
    initComponent: function() {
    	this.callParent();
    	var me = this,
    		pv = cfg.getUser().userType,pruviews={
    			'-1':'admin',
    			'01':'site',
    			'02':'supply',
    			'03':'agency'
    		};
    	if(pv.length>2){pv = pv.substring(0,2);}
    	//数据统计
    	var totalInfo = Ext.create('app.view.b2b.user.totalInfo',{
    		style:'background:transparent;',
    	    bodyStyle:'background:transparent;',
    		params:{
    			cityId:'',
    			pruviews:pruviews[pv]
    		}
    	});
    	//待办提醒
    	var waitDo = Ext.create('app.view.b2b.user.'+pruviews[pv]+'.waitDo',{
    		itemId:'waitDoTools',
    		dock:'top',
    		style:'background: none 0px 0px repeat scroll rgba(251, 180, 76, 0.2);box-shadow: 1px 1px 0px rgbargba(250,235,204, 0.9);border:none;',
    		params:{cityId:''}
    	});
    	//参数定义
		var openCompany,startCityRouteStat,endCityRouteStat,startCityOrderStat,endCityOrderStat,goDateStat,routeSaleStat,routeTabs,refundFinish,retailStat;
		
    	if(citys.length>0){
			var menus = [{
				record:{CITY_ID:'all'},
				glyph:'xe6a3@iconfont',
				text:'全部'
			}];
			Ext.Array.each(citys,function(o,i){
				menus.push({
					record:o,
					glyph:'xe6a3@iconfont',
					text:o.SUB_AREA
				});
			});
			waitDo.add({
	    		cls:'plain none blue',
	    		height:40,
	    		menu:Ext.create('Ext.menu.Menu', {
	    			listeners:{
	    				click:function(menu,item){
	    					menu.up().setText('<i class="iconfont white"  style="font-size:18px;top:1px;">&#xe68d;</i> '+item.text);
	    					waitDo.setParams(Ext.applyIf({cityId:item.record.CITY_ID},waitDo.getParams()||{}));
	    					totalInfo.setParams(Ext.applyIf({cityId:item.record.CITY_ID},totalInfo.getParams()||{}));
	    					openCompany.setParams({cityId:item.record.CITY_ID});
	    					startCityRouteStat.setParams({cityId:item.record.CITY_ID,type:0});
	    					endCityRouteStat.setParams({cityId:item.record.CITY_ID,type:1});
	    					startCityOrderStat.setParams({cityId:item.record.CITY_ID,type:0});
	    					endCityOrderStat.setParams({cityId:item.record.CITY_ID,type:1});
	    				}
	    			},
	    		    items: menus
	    		}),
	    		xtype:'button',
	    		text:'<i class="iconfont white-color"  style="font-size:18px;top:1px;">&#xe68d;</i> 全部'
	    	});
		}
    	var dockedTools = Ext.create('Ext.toolbar.Toolbar',{
    			margin:'5 0 0 0',
	    		height:35,
	    		cls:'docked-tools',
	    		border:false,
	    		padding:0,
	    		dock:'top',
	    		xtype:'toolbar',
	    		style:'background:transparent;',
	    		items:[{
	    			xtype:'button',
	    			cls:'plain lightgray blue',
	    			style:'margin-left:10px',
	    			text:'全部',
	    			handler:function(btn){me.queryTotalInfo.call(me,totalInfo,btn,'');}
	    		},{
	    			xtype:'button',
	    			cls:'plain lightgray',
	    			text:'周边',
	    			handler:function(btn){me.queryTotalInfo.call(me,totalInfo,btn,1);}
	    		},{
	    			cls:'plain lightgray',
	    			xtype:'button',
	    			text:'国内',
	    			handler:function(btn){me.queryTotalInfo.call(me,totalInfo,btn,2);}
	    		},{
	    			cls:'plain lightgray',
	    			xtype:'button',
	    			text:'出境',
	    			handler:function(btn){me.queryTotalInfo.call(me,totalInfo,btn,3);}
	    		}]
		});
    	//整合所有模块 为Accordion
    	var accordionView = me.add({
    		layout: {
    	        type: 'accordion',
    	        titleCollapse: true,
    	        animate: true,
    	        activeOnTop: true
    	    },
    		flex:1,
    		style:'background:transparent;',
    	    bodyStyle:'background:transparent;padding:0px;',
    		border:true,
    		defaults:{
    		    overflowY :'auto',
    		    style:'border:1px solid #e1e1e1;'
    		},
    		items:[{
    			title:'<span class="blue-color f14"><i class="iconfont f18" style="top:1px;">&#xe6ba;</i> 销售统计</span>',
    			//tools:[],
    			bodyStyle: 'padding-top:0px;background:transparent;',
    			style:'border:none;background:transparent;',
    			dockedItems :[dockedTools],
    			items:[totalInfo]
    		}]
    	});
    	
    	/**旅行社begin**/
    	if(pv=='03'){
    		//出团统计
    		goDateStat = Ext.create('app.view.b2b.user.supply.orderGrid',{
    			viewConfig: {
		    	    preserveScrollOnRefresh: true
		    	}
    		});
    		routeTabs = Ext.create('app.view.b2b.user.agency.produceList',{
    			tabBar:{
    				defaults:{height:45},
    				style:'padding-top:5px;'
    			}
    			
    		});
    		refundFinish =  Ext.create('app.view.b2b.user.agency.refundOrderGrid',{
    		});
    		
    		
    		//只有总公司才能看到门市统计
    		if(cfg.getUser().pid=='-1'){
    			retailStat = Ext.create('app.view.b2b.user.agency.retailStat',{
    				viewConfig: {
    		    	    preserveScrollOnRefresh: true
    		    	}
        		});
    		}
    		Ext.Array.insert(companys, 0,[{
    			text:'全部',
    			COMPANY:'全部',
    			ID:''
    		}]);
    		var departBtn = waitDo.add({
    			cls:'plain none blue',
				xtype:'button',
				border:false,
				height:42,
				//scale:'medium',
				text:'全部<br><span class="f12 white">所有部门</span>',
	    		menu:companys
    		});
    		var ckgs = departBtn.getMenu().query('checkboxgroup');
    		departBtn.getMenu().on('click',function(menu, item){
    			totalInfo.setParams(Ext.applyIf({companyId:item.ID,departIds:''},totalInfo.getParams()||{}));
    			waitDo.setParams(Ext.applyIf({companyId:item.ID,departIds:''},waitDo.getParams()||{}));
    			goDateStat.setParams({companyId:item.ID,departIds:''});
    			if(retailStat){
					retailStat.setParams({companyId:item.ID,departIds:''});
				}
    			
    			departBtn.companyId = item.ID;
    			departBtn.setText(item.COMPANY+'<br><span class="f12 white">所有部门</span>');
    			Ext.Array.each(ckgs,function(ckg1,i){
					ckg1.reset();
				});
    		});
    		Ext.Array.each(ckgs,function(ckg,i){
	    			ckg.on('change',function(cg, newValue, oldValue, eOpts){
	    				
	    				var cks = cg.getChecked(),selectedText=[],showText='全部<br><span class="f12 white">所有部门</span>';
	    				var values = '',len=0;
	    				if(cks.length==0){
	    					
	    					values = '';
	    				}else{
	    					values = newValue.depart;
							Ext.Array.each(cks,function(ck,i){
								selectedText.push(cks[i].TEXT);
							});
							
	    					if(Ext.isArray(values)){
	    						len=newValue.depart.length;
	    					}else if(values){
	    						len=1;
	    						values = [values];
	    					}else{}
	    					showText=cg.up().up().text+'<br><span class="f12 white">'+selectedText.join('、')+'</span>';
	    					values = Ext.encode(values)||'';
	    				}
    					
						
						departBtn.setText(showText);
						departBtn.companyId=cg.up().up().ID;
						departBtn.departId = values;
						
						
						totalInfo.setParams(Ext.applyIf({companyId:cg.up().up().ID,departIds:values},totalInfo.getParams()||{}));
						
    					waitDo.setParams({companyId:cg.up().up().ID,departIds:values});
    					goDateStat.setParams({companyId:cg.up().up().ID,departIds:values});
    					if(retailStat){
    						retailStat.setParams({companyId:cg.up().up().ID,departIds:values});
    					}
    					
    					
    					Ext.Array.each(ckgs,function(ckg1,i){
    						if(ckg1.id!=cg.id){
    							ckg1.reset();
    						}
    					});
    					
	    			});
    		});
    		var dateTypeField = waitDo.add({
    			itemId:'dateTypeField',
	        	xtype:'combo',
				forceSelection: true,
				editable:false,
	            valueField: 'value',
	            displayField: 'text',
	            store:util.createComboStore([{
	            	text:'创建日期',
	            	value:1
	            },{
	            	text:'出团日期',
	            	value:2
	            }]),
	            width:100,
	            value:1,
	            queryMode: 'local',
	            typeAhead: true
    		});
    		var startDateField = waitDo.add({
        		width:120,
        		emptyText:'开始',
              	endDateField: 'enddt',
	            itemId:'startdt',
	            margin:'0 2 0 0',
	            showToday:false,
	            editable:false,
	            vtype: 'daterange',
              	format:'Y-m-d',
              	xtype:'datefield'
        	});
        	var endDateField = waitDo.add({
              	width:120,
              	emptyText:'结束',
              	margin:'0 0 0 0',
              	format:'Y-m-d',
              	editable:false,
              	showToday:false,
              	itemId:'enddt',
	            startDateField: 'startdt',
	            vtype: 'daterange',
              	xtype:'datefield'
        	});
        	waitDo.add({
        		cls:'groupChartView',
              	width:30,
              	margin:'0 2 0 2',
              	glyph:'xe61c@iconfont',
              	tooltip:'查询',
        		handler:function(btn){
        			var gs = [goDateStat],
        				endDate = btn.previousSibling(),
        				startDate = endDate.previousSibling(),
        				dateType = startDate.previousSibling();
        			if(retailStat){gs.push(retailStat);}
        			startDate = Ext.Date.format(startDate.getValue(),'Y-m-d');
        			endDate = Ext.Date.format(endDate.getValue(),'Y-m-d');
        			dateType = dateType.getValue();
        			var ep = {startDate:startDate,endDate:endDate,dateType:dateType};
        			for(var i=0;i<gs.length;i++){
        				var g = gs[i];
        				Ext.applyIf(ep,g.getStore().getProxy().getExtraParams());
        				if(ep.ctype)delete ep.ctype;
        				g.getStore().getProxy().setExtraParams(ep);
            			g.getStore().load();
        			}
        			//代办
        			var oldParams = waitDo.getParams();
        			Ext.applyIf(ep,oldParams);
        			waitDo.setParams(ep);
        			//累计
        			totalInfo.setParams(Ext.applyIf(ep,totalInfo.getParams()||{}));
        		}
        	});
        	waitDo.add({
        		margin:'0 10 0 0',
              	glyph:'xe63d@iconfont',
              	width:30,
              	tooltip:'重置日期',
              	handler:function(b){
              		var endDate = b.previousSibling().previousSibling(),
              			startDate = endDate.previousSibling();
              		if(!endDate.getValue()&&!startDate.getValue()){return;}
              		endDate.reset();
              		startDate.reset();
              		var gs = [goDateStat];
              		if(retailStat){gs.push(retailStat);}
              		var ep = {startDate:'',endDate:'',dateType:1};
              		for(var i=0;i<gs.length;i++){
        				var g = gs[i];
        				Ext.applyIf(ep,g.getStore().getProxy().getExtraParams());
        				if(ep.ctype)delete ep.ctype;
        				g.getStore().getProxy().setExtraParams(ep);
            			g.getStore().load();
        			}
              		//代办
        			var oldParams = waitDo.getParams();
        			Ext.applyIf(ep,oldParams);
        			waitDo.setParams(ep);
        			//累计
        			totalInfo.setParams(Ext.applyIf(ep,totalInfo.getParams()||{}));
              	}
        	});
        	goDateStat.setTitle('<span class="f14 blue-color"><i class="iconfont f18" style="top:1px;">&#xe6b9;</i> 最近出团</span>');
        	accordionView.add(goDateStat);
        	if(cfg.getUser().pid=='-1'){
        		retailStat.setTitle('<span class="f14 blue-color"><i class="iconfont f18" style="top:1px;">&#xe6be;</i> 门市统计</span>');
	        	accordionView.add(retailStat);
        	}
        	routeTabs.setTitle('<span class="f14 blue-color"><i class="iconfont f18" style="top:1px;">&#xe6bc;</i> 线路分类</span>');
        	accordionView.add(routeTabs);
			dockedTools.add('->');
        	dockedTools.add({
        		text:'<i class="iconfont white-color" style="font-size:14px;top:1px;">&#xe6ab;</i> 退款到账订单',
        		cls:'float plain deeporange',
        		height:30,
        		handler:function(){
					var refundOrderGrid = Ext.create('app.view.b2b.user.agency.refundOrderGrid',{
						region:'west',
    					width:340,
    					border:true,
    					margin:'0 5 0 0',
    					listeners:{
    						rowclick:function(view, record, tr, rowIndex){
    							view.up().nextSibling().loadOrderData(record.get('ID'));
    						}
    					}
					});
					var win = Ext.create('Ext.window.Window',{
        				width:900,
        				height:400,
        				bodyPadding:5,
        				modal:true,
        				draggable:false,
        				resizable:false,
        				maximizable:false,
        	   			layout:'border',
        				title:util.windowTitle('&#xe615;','退款到账订单'),
        				items:[refundOrderGrid,{
        					region:'center',
        					autoScroll:true,
        					xclass:'app.view.common.OrderFundLog'
        				}]
        			});
        			win.show();
        			refundOrderGrid.setParams({
						companyId:departBtn.companyId,
						departIds:departBtn.departId,
						startDate:startDateField.getValue(),
						endDate:endDateField.getValue()
					});
				}
        	});
        	dockedTools.add({
        		xtype:'button',
        		height:30,
				cls:'float plain green',
				hidden:!util.getDirectModuleId('b2c/visitor'),
				text:'<i class="iconfont white-color" style="font-size:14px;top:1px;">&#xe6ab;</i> 游客',
				handler:function(){
					util.redirectTo('b2c/visitor');
				}
        	});
    	}
    	/**旅行社end**/
    	
    	/**供应商begin**/
    	if(pv=='02'){
    		//出团统计
    		goDateStat = Ext.create('app.view.b2b.user.supply.orderGrid',{
    			viewConfig: {
		    	    preserveScrollOnRefresh: true
		    	}
    		});
    		//线路
    		routeSaleStat = Ext.create('app.view.b2b.user.supply.routeGrid',{
    			viewConfig: {
		    	    preserveScrollOnRefresh: true
		    	}
    		});
    		//订单
			//出发城市
			startCityOrderStat = Ext.create('app.view.b2b.user.orderStatGrid',{
				viewConfig: {
		    	    preserveScrollOnRefresh: true
		    	},
				params:{cityId:'',type:0,pruview:'supply'}
			});
			//目的地
			endCityOrderStat = Ext.create('app.view.b2b.user.orderStatGrid',{
				viewConfig: {
		    	    preserveScrollOnRefresh: true
		    	},
				params:{cityId:'',type:1,pruview:'supply'}
			});
			
			//var departs = cfg.getUser().
    		var departBtn = waitDo.add({
    			cls:'plain none blue',
				xtype:'button',
				border:false,
				height:40,
	    		text:'<i class="iconfont white-color" style="font-size:18px;top:1px;padding:0 2px;">&#xe6a0;</i> 所有部门',
	    		menu:[{
	    			xtype:'checkboxgroup',
	    			padding:5,
	    			columns: 1,
	    			listeners:{
	    				change:function(cg, newValue, oldValue, eOpts ){
	    					var cks = cg.getChecked(),selectedText=[],showText='所有部门';
	    					Ext.Array.each(cks,function(ck,i){
	    						selectedText.push(cks[i].TEXT);
	    					});
	    					var values = newValue.depart,len=0;
	    					if(Ext.isArray(values)){
	    						len=newValue.depart.length;
	    					}else if(values){
	    						len=1;
	    						values = [values];
	    					}else{}
	    					
	    					if(len!=departs.length){
	    						if(len==0){showText='所有部门';}else{
	    							showText=selectedText.join('、');
	    						}
	    					}
	    					departBtn.setText('<i class="iconfont white-color" style="font-size:18px;top:1px;padding:0 2px;">&#xe6a0;</i> '+showText);
	    					
	    					values = Ext.encode(values)||'';
	    					
	    					totalInfo.setParams(Ext.applyIf({departIds:values},totalInfo.getParams()||{}));
	    					
	    					waitDo.setParams({departIds:values});
	    					
	    					startCityOrderStat.setParams({departIds:values,type:0,pruview:'supply'});
	    					endCityOrderStat.setParams({departIds:values,type:1,pruview:'supply'});
	    					goDateStat.setParams({departIds:values});
	    					routeSaleStat.setParams({departIds:values});
	    				}
	    			},
	    			items: departs
	    		}]
    		});
    		waitDo.add({
    			itemId:'dateTypeField',
	        	xtype:'combo',
				forceSelection: true,
				editable:false,
	            valueField: 'value',
	            displayField: 'text',
	            store:util.createComboStore([{
	            	text:'创建日期',
	            	value:1
	            },{
	            	text:'出团日期',
	            	value:2
	            }]),
	            width:100,
	            value:1,
	            queryMode: 'local',
	            typeAhead: true
    		});
    		waitDo.add({
        		width:120,
        		emptyText:'开始',
              	endDateField: 'enddt',
	            itemId:'startdt',
	            margin:'0 2 0 0',
	            showToday:false,
	            editable:false,
	            vtype: 'daterange',
              	format:'Y-m-d',
              	xtype:'datefield'
        	});
        	waitDo.add({
              	width:120,
              	emptyText:'结束',
              	margin:'0 0 0 0',
              	format:'Y-m-d',
              	editable:false,
              	showToday:false,
              	itemId:'enddt',
	            startDateField: 'startdt',
	            vtype: 'daterange',
              	xtype:'datefield'
        	});
        	waitDo.add({
        		cls:'groupChartView',
              	width:30,
              	margin:'0 2 0 2',
              	glyph:'xe61c@iconfont',
              	tooltip:'查询',
        		handler:function(btn){
        			var gs = [goDateStat,routeSaleStat,startCityOrderStat,endCityOrderStat],
	        			endDate = btn.previousSibling(),
	    				startDate = endDate.previousSibling(),
	    				dateType = startDate.previousSibling();
        			startDate = Ext.Date.format(startDate.getValue(),'Y-m-d');
        			endDate = Ext.Date.format(endDate.getValue(),'Y-m-d');
        			dateType = dateType.getValue();
        			var ep = {startDate:startDate,endDate:endDate,dateType:dateType};
        			for(var i=0;i<gs.length;i++){
        				var g = gs[i];
        				Ext.applyIf(ep,g.getStore().getProxy().getExtraParams());
        				g.getStore().getProxy().setExtraParams(ep);
            			g.getStore().load();
        			}
        			//代办
        			var oldParams = waitDo.getParams();
        			Ext.applyIf(ep,oldParams);
        			waitDo.setParams(ep);
        			//累计
        			totalInfo.setParams(Ext.applyIf(ep,totalInfo.getParams()||{}));
        		}
        	});
        	waitDo.add({
        		margin:'0 10 0 0',
              	glyph:'xe63d@iconfont',
              	width:30,
              	tooltip:'重置日期',
              	handler:function(b){
              		var endDate = b.previousSibling().previousSibling(),
              			startDate = endDate.previousSibling();
              		if(!endDate.getValue()&&!startDate.getValue()){return;}
              		endDate.reset();
              		startDate.reset();
              		var gs = [goDateStat,routeSaleStat,startCityOrderStat,endCityOrderStat];
              		var ep = {startDate:'',endDate:'',dateType:1};
              		for(var i=0;i<gs.length;i++){
        				var g = gs[i];
        				Ext.applyIf(ep,g.getStore().getProxy().getExtraParams());
        				g.getStore().getProxy().setExtraParams(ep);
            			g.getStore().load();
        			}
              		//代办
        			var oldParams = waitDo.getParams();
        			Ext.applyIf(ep,oldParams);
        			waitDo.setParams(ep);
        			//累计
        			totalInfo.setParams(Ext.applyIf(ep,totalInfo.getParams()||{}));
              	}
        	});
        	
        	goDateStat.setTitle('<span class="f14 blue-color"><i class="iconfont f18" style="top:1px;">&#xe6b9;</i> 最近出团</span>');
        	accordionView.add(goDateStat);
        	routeSaleStat.setTitle('<span class="f14 blue-color"><i class="iconfont f18" style="top:1px;">&#xe6bd;</i> 线路统计</span>');
        	accordionView.add(routeSaleStat);
        	startCityOrderStat.setTitle('<span class="f14 blue-color"><i class="iconfont f18" style="top:1px;">&#xe6b8;</i> 出发地订单</span>');
        	accordionView.add(startCityOrderStat);
        	endCityOrderStat.setTitle('<span class="f14 blue-color"><i class="iconfont f18" style="top:1px;">&#xe632;</i> 目的地订单</span>');
        	accordionView.add(endCityOrderStat);
        	
    	}
    	/**供应商end**/
    	/**站长、admin begin**/
    	if(pv=='01'||pv=='-1'){
    		//最近开户
    		openCompany = Ext.create('app.view.b2b.user.openedCompanyGrid',{
    			viewConfig: {
		    	    preserveScrollOnRefresh: true
		    	},
    			params:{cityId:''}
        	})
        	//线路统计 
			//出发城市
			startCityRouteStat = Ext.create('app.view.b2b.user.routeStatGrid',{
				viewConfig: {
		    	    preserveScrollOnRefresh: true
		    	},
				params:{cityId:'',type:0}
	    	});
			//目的地
			endCityRouteStat = Ext.create('app.view.b2b.user.routeStatGrid',{
				viewConfig: {
		    	    preserveScrollOnRefresh: true
		    	},
				params:{cityId:'',type:1}
	    	});
			//订单
			//出发城市
			startCityOrderStat = Ext.create('app.view.b2b.user.orderStatGrid',{
				viewConfig: {
		    	    preserveScrollOnRefresh: true
		    	},
				params:{cityId:'',type:0}
			});
			//目的地
			endCityOrderStat = Ext.create('app.view.b2b.user.orderStatGrid',{
				viewConfig: {
		    	    preserveScrollOnRefresh: true
		    	},
				params:{cityId:'',type:1}
			});
			waitDo.add({
    			itemId:'dateTypeField',
	        	xtype:'combo',
				forceSelection: true,
				editable:false,
	            valueField: 'value',
	            displayField: 'text',
	            store:util.createComboStore([{
	            	text:'创建日期',
	            	value:1
	            },{
	            	text:'出团日期',
	            	value:2
	            }]),
	            width:100,
	            value:1,
	            queryMode: 'local',
	            typeAhead: true
    		});
        	waitDo.add({
        		width:120,
        		margin:'0 2 0 0',
        		emptyText:'开始',
              	endDateField: 'enddt',
	            itemId:'startdt',
	            showToday:false,
	            editable:false,
	            vtype: 'daterange',
              	format:'Y-m-d',
              	xtype:'datefield'
        	});
        	waitDo.add({
              	width:120,
              	emptyText:'结束',
              	margin:'0 0 0 0',
              	format:'Y-m-d',
              	editable:false,
              	showToday:false,
              	itemId:'enddt',
	            startDateField: 'startdt',
	            vtype: 'daterange',
              	xtype:'datefield'
        	});
        	waitDo.add({
        		cls:'groupChartView',
              	width:30,
              	margin:'0 2 0 2',
              	glyph:'xe61c@iconfont',
              	tooltip:'查询',
        		handler:function(btn){
        			var gs = [openCompany,startCityRouteStat,endCityRouteStat,startCityOrderStat,endCityOrderStat],
        				endDate = btn.previousSibling(),
        				startDate = endDate.previousSibling(),
        				dateType = startDate.previousSibling();
        			startDate = Ext.Date.format(startDate.getValue(),'Y-m-d');
        			endDate = Ext.Date.format(endDate.getValue(),'Y-m-d');
        			dateType = dateType.getValue();
        			var ep = {startDate:startDate,endDate:endDate,dateType:dateType};
        			for(var i=0;i<gs.length;i++){
        				var g = gs[i];
        				Ext.applyIf(ep,g.getStore().getProxy().getExtraParams());
        				g.getStore().getProxy().setExtraParams(ep);
            			g.getStore().load();
        			}
        			//代办
        			var oldParams = waitDo.getParams();
        			Ext.applyIf(ep,oldParams);
        			waitDo.setParams(ep);
        			//累计
        			totalInfo.setParams(Ext.applyIf(ep,totalInfo.getParams()||{}));
        		}
        	});
        	waitDo.add({
        		margin:'0 10 0 0',
              	glyph:'xe63d@iconfont',
              	width:30,
              	tooltip:'重置日期',
              	handler:function(b){
              		var endDate = b.previousSibling().previousSibling(),
              			startDate = endDate.previousSibling();
              		if(!endDate.getValue()&&!startDate.getValue()){return;}
              		endDate.reset();
              		startDate.reset();
              		var gs = [openCompany,startCityRouteStat,endCityRouteStat,startCityOrderStat,endCityOrderStat];
              		var ep = {startDate:'',endDate:'',dateType:1};
              		for(var i=0;i<gs.length;i++){
        				var g = gs[i];
        				Ext.applyIf(ep,g.getStore().getProxy().getExtraParams());
        				g.getStore().getProxy().setExtraParams(ep);
            			g.getStore().load();
        			}
              		//代办
        			var oldParams = waitDo.getParams();
        			Ext.applyIf(ep,oldParams);
        			waitDo.setParams(ep);
        			//累计
        			totalInfo.setParams(Ext.applyIf(ep,totalInfo.getParams()||{}));
              	}
        	});
        	
        	openCompany.setTitle('<span class="f14 blue-color"><i class="iconfont f18" style="top:1px;">&#xe6bc;</i> 最近开户</span>');
        	accordionView.add(openCompany);
        	startCityRouteStat.setTitle('<span class="f14 blue-color"><i class="iconfont f18" style="top:1px;">&#xe6bb;</i> 产品  - 出发地</span>');
        	accordionView.add(startCityRouteStat);
        	endCityRouteStat.setTitle('<span class="f14 blue-color"><i class="iconfont f18" style="top:1px;">&#xe6bb;</i> 产品  - 目的地</span>');
        	accordionView.add(endCityRouteStat);
        	startCityOrderStat.setTitle('<span class="f14 blue-color"><i class="iconfont f18" style="top:1px;">&#xe6b8;</i> 订单  - 出发地</span>');
        	accordionView.add(startCityOrderStat);
        	endCityOrderStat.setTitle('<span class="f14 blue-color"><i class="iconfont f18" style="top:1px;">&#xe632;</i> 订单  - 目的地</span>');
        	accordionView.add(endCityOrderStat);
        	
        	
    		/*me.add({
    			style:'border-right:1px solid #d1d1d1',
    			dockedItems:[{
    				dock:'top',
    				height:35,
    				style:'border-bottom:2px solid #427fed!important;',
    				html:'<span class="f14 white head"><i class="iconfont">&#xe693;</i> 最近开户'
    			}],
    			layout:{
    		    	type:'hbox',
    		        pack: 'start',
    		        align: 'stretch'
    		    },
    			items:[openCompany]
    		});
    		//线路统计
    		me.add({
    	    	style:'border-bottom:1px solid #d1d1d1;border-right:1px solid #d1d1d1',
    	    	dockedItems:[{
    				dock:'top',
    				height:35,
    				style:'border-bottom:2px solid #427fed!important;',
    				html:'<span class="f14 white head"><i class="iconfont" style="top:3px">&#xe691;</i> 产品统计'
    			}],
    			layout:{
    		    	type:'hbox',
    		        pack: 'start',
    		        align: 'stretch'
    		    },
    			items:[startCityRouteStat,endCityRouteStat]
    		});
    		//订单统计
    		me.add({
    			dockedItems:[{
    				dock:'top',
    				height:35,
    				style:'border-bottom:2px solid #427fed!important;',
    				html:'<span class="f14 white head"><i class="iconfont" style="top:3px">&#xe690;</i> 订单统计'
    			}],
    	    	style:'border-bottom:1px solid #d1d1d1;border-right:1px solid #d1d1d1;',
    			items:[startCityOrderStat,endCityOrderStat]
    		});*/
    	}
    	/**站长、admin end**/
    	
    	
    	
    	me.addDocked(waitDo);
    },
    queryTotalInfo:function(tf,btn,type){
    	Ext.Array.each(btn.up().query('button'),function(b,i){
    		b.removeCls('blue');
    	});
    	btn.addCls('blue');
    	tf.setParams(Ext.applyIf({routeType:type},tf.getParams()||{}));
    }
});
