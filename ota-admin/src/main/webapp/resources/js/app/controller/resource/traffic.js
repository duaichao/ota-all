Ext.define('app.controller.resource.traffic', {
	extend: 'app.controller.common.BaseController',
	views:['Ext.ux.form.DatePickerEvent','common.CityCombo','resource.trafficGrid','common.PriceGrid','app.vtype.CommonVtype','app.view.resource.trafficPrice'],
	stores:['City'],
	config: {
		control: {
			'trafficgrid':{
				cellclick:function( view, td, cellIndex, record, tr, rowIndex, e, eOpts ){
					if(cellIndex==7){
						if(e.target.tagName=='A'&&e.target.className.indexOf('zy')!=-1)
	                	var win = Ext.create('Ext.window.Window',{
							title:util.windowTitle('&#xe635;','占用详情',''),
							width:800,
							bodyPadding:5,
							height:300,
							modal:true,
							draggable:false,
							resizable:false,
				   			layout:'fit',
							items:[{
								xtype:'dataview',
								itemSelector: '.data-view-item',
								store :Ext.create('Ext.data.Store', {
						        	model:Ext.create('Ext.data.Model',{
						        		fields: ['ROUTE_TITLE','ROUTE_NO','PLAN_NAME']
						        	}),
						        	autoLoad: true, 
								    proxy: {
								        type: 'ajax',
								        noCache:true,
								        url: cfg.getCtx()+'/resource/traffic/route/use?trafficId='+record.get('ID'),
								        reader: {
								            type: 'json',
								            rootProperty: 'data',
								            totalProperty:'totalSize'
								        }
								   	}
						        }),
						        tpl: [
						        	'<div class="p-detail"><table width="100%">',
						        	'<tr>',
						        		'<td>线路编号</td>',
							        	'<td>线路名称</td>',
							        	'<td>方案名称</td>',
						        	'</tr>',
						        	'<tpl for=".">',
						        	'<tr>',
						        	'<td>{ROUTE_NO}</td>',
						        	'<td>{ROUTE_TITLE}</td>',
						        	'<td>{PLAN_NAME}</td>',
						        	'</tr></tpl></table></div>'
						        ]
							}],
							buttons:[{
								text:'关闭',
								cls:'disable',
								handler:function(){
									win.close();
								}
							}]
						}).show();
					}
				},
				rowclick:'onBaseGridRowClick',
				selectionchange: function(view, records) {
					var tg = this.getTrafficGrid(),
						btn = tg.down('button[itemId=editTraffic]'),
						btn1 = tg.down('button[itemId=delTraffic]'),
						btn2 = tg.down('button[itemId=pubTraffic]'),
						btn4 = tg.down('button[itemId=editSeatTraffic]'),
						btn5 = tg.down('button[itemId=stopTraffic]'),
						isPub = parseInt(records[0].get('IS_PUB')||0);
						if(btn){
	                		btn.setDisabled(isPub==1);
	                	}
	                	if(btn1){
	                		btn1.setDisabled(isPub==1);
	                	}
	                	if(btn2){
	                		btn2.setDisabled(isPub==1);
	                	}
	                	if(btn4){
	                		btn4.setDisabled(isPub!=1);
	                	}
	                	if(btn5){
	                		btn5.setDisabled(isPub!=1);
	                	}
	            }
			},
			'toolbar button[itemId=addTraffic]': {
	             click: 'addTraffic'
	        },'toolbar button[itemId=editTraffic]': {
	             click: 'editTraffic'
	        },'toolbar button[itemId=delTraffic]': {
	             click: 'delTraffic'
	        },'toolbar button[itemId=pubTraffic]': {
	             click: 'pubTraffic'
	        },'toolbar button[itemId=stopTraffic]': {
	             click: 'stopTraffic'
	        },'toolbar button[itemId=priceTraffic]': {
	             click: 'priceTraffic'
	        }/*,'toolbar button[itemId=saleTraffic]': {
	             click: 'saleTraffic'
	        }*/,'toolbar button[itemId=editSeatTraffic]': {
	             click: 'editSeatTraffic'
	        },'toolbar button[itemId=addMuster]': {
	             click: 'addMuster'
	        },'toolbar button[itemId=editMuster]': {
	             click: 'editMuster'
	        },'toolbar button[itemId=delMuster]': {
	             click: 'delMuster'
	        }
		},
		refs: {
			trafficGrid: 'trafficgrid',
			musterGrid:'gridpanel[itemId=mustergrid]',
			musterToolbar:'toolbar[itemId=mustertool]',
        }
	},
	onBaseGridRender:function(g){
		var me = this;
		setTimeout(function(){
		Ext.Ajax.request({
		    url: util.getPowerUrl(),
		    success: function(response, opts) {
		        var obj = Ext.decode(response.responseText),
		        	items = obj.children,
		        	ubtn = [],
		        	rbtn = [];
		        for(var i=0;i<items.length;i++){
		        	
		        	//如果当前用户没有部门id 隐藏设计交通修改交通
		        	if((items[i].itemId == 'addTraffic'||items[i].itemId == 'editTraffic')&&cfg.getUser().departId==''){
		        		Ext.applyIf(items[i],{hidden:true});
		        	}
		        	
		        	
		        	if(items[i].itemId.indexOf('Muster')!=-1){
		        		rbtn.push(items[i]);
		        	}
		        	if(items[i].itemId.indexOf('Traffic')!=-1){
		        		/*if(items[i].itemId == 'saleTraffic'&&cfg.getUser().saleTraffic!='1'){
		        			Ext.applyIf(items[i],{hidden:true});
		        		}*/
		        		ubtn.push(items[i]);
		        	}
		        	
		        }
		        ubtn.push('->');
		        ubtn.push({
		        	emptyText:'出发/目的城市/交通名称',
		        	xtype:'searchfield',
		        	store:g.getStore(),
		        	width:200
		        });
		        me.getMusterToolbar().removeAll();
		        me.getMusterToolbar().add(rbtn);
		        util.createGridTbar(g,ubtn);
		    }
		});
		},500);
	},
	onBaseGridRowClick:function(grid, record, tr, rowIndex, e, eOpts){
		var rg = grid,
			sel,
			ct = this.getMusterGrid();
			ct.getStore().load({params:{
				'trafficId':record.get('ID')
			}});
	},
	createSaveWindow :function(title,icon){
	    var hideEdit = false;
	    if(title.indexOf('修改')!=-1){
	    	hideEdit = true;
	    }
	    var types=[],tools = cfg.getTravelTools();
	    for(var i=0;i<tools.length;i++){
	    	types.push({value:i,text:tools[i]});
	    }
	    var typeStore = util.createComboStore(types),
	    	me= this,win = Ext.create('Ext.window.Window',{
			title:util.windowTitle(util.glyphToFont(icon),title,''),
			width:550,
			height:350,
			modal:true,
			draggable:false,
			resizable:false,
   			layout:'fit',
			items:[{
				xtype:'form',
    			scrollable:false,
				fieldDefaults: {
			        labelAlign: 'right',
			        labelWidth: 80,
			        msgTarget: 'side'
			    },
				bodyPadding: '10 5 5 5',
			    items: [{
			    	xtype:'hidden',
			    	name:'pm[ID]'
			    },{
			    	xtype:'fieldcontainer',
			    	layout:'hbox',
			    	items:[{
			    		fieldLabel:'出发/目的地',
			    		allowBlank: false,
				    	valueField: 'TEXT',
				    	width:250,
				    	itemId:'sc',
				    	xtype:'citycombo',
				    	listeners:{
				    		change:function( combo, newValue, oldValue,eOpts ){
				    			if(newValue){
				    				combo.nextSibling().setValue(newValue);
				    			}
				    		}
				    	}
			    	},{
			    		xtype:'hidden',
			    		name:'pm[START_CITY]'
			    	},{
			    		xtype: 'splitter'
			    	},{
			    		xtype:'textfield',
			    		flex:1,
			    		name:'pm[BEGIN_STATION]',
			    		emptyText:'始发站'
			    	},{
			    		xtype: 'splitter'
			    	},{
			    		itemId:'ec',
			    		allowBlank: false,
			    		width:130,
				    	valueField: 'TEXT',
				    	xtype:'citycombo',
				    	listeners:{
				    		change:function( combo, newValue, oldValue,eOpts ){
				    			if(newValue){
				    				combo.nextSibling().setValue(newValue);
				    			}
				    		}
				    	}
			    	},{
			    		xtype:'hidden',
			    		name:'pm[END_CITY]'
			    	},{
			    		xtype: 'splitter'
			    	},{
			    		xtype:'textfield',
			    		flex:1,
			    		name:'pm[END_STATION]',
			    		emptyText:'到站点'
			    	}]
			    },{
			    	fieldLabel:'名称',
			    	maxLength:120,
			    	name:'pm[TITLE]',
			    	xtype:'textfield',
			    	allowBlank: false,
			        anchor:'100%'
			    },{
			    	fieldLabel:'工具',
			    	value:1,
			    	width:220,
			    	xtype:'combo',
					name:'pm[TYPE]',
					hiddenName:'pm[TYPE]',
					forceSelection: true,
		            valueField: 'value',
		            displayField: 'text',
		            store:typeStore,
		            minChars: 0,
		            queryMode: 'local',
		            typeAhead: true
			    },{
			    	fieldLabel:'提前报名天数',
			    	maxValue:40,
			    	minValue:0,
			    	value:0,
			    	width:220,
			    	name: 'pm[END_DATE]',
			    	xtype:'numberfield',
			    	allowBlank: false
			    },{
			    	fieldLabel:'提前报名时间',
			    	width:220,
			    	value:'8:30',
			    	name: 'pm[END_TIME]',
			    	xtype:'timefield',
			    	format:'H:i',
			    	minValue: '8:30',
        			maxValue: '18:30',
        			increment: 30,
			    	allowBlank: false
			    },{
			    	fieldLabel:'类型',
			    	xtype:'radiogroup',
		        	width:240,
		        	
		        	items: [
		                {boxLabel: '单程', name: 'pm[IS_SINGLE]', inputValue: 0, checked: true},
		                {boxLabel: '往返', name: 'pm[IS_SINGLE]', inputValue: 1}
		            ]
			    },{
			    	xtype:'textarea',
			    	name: 'pm[ROTE]',
			    	fieldLabel:'备注',
			        anchor:'100%'
			    }],
				buttons: [{
			        text: '保存',
			        itemId:'save',
			        disabled: true,
			        formBind: true,
			        handler:function(btn){
			        	var form = btn.up('form'),
			        		win = form.up('window');
			        	me.saveTraffic(form.getForm(),win);
			        }
			    }]
			}]
		});
		return win;
	},
	addTraffic:function(btn){
		this.createSaveWindow('添加出行交通',btn.glyph).show();
	},
	saveTraffic:function(form,win){
		var me = this,btn=win.down('button#save');
		if (form.isValid()) {
			btn.disable();
            form.submit({
            	submitEmptyText:false ,
            	url:cfg.getCtx()+'/resource/traffic/save',
                success: function(form, action) {
                   util.success('保存出行交通成功');
                   me.getTrafficGrid().getStore().load();
                   win.close();
                },
                failure: function(form, action) {
                	var state = action.result?action.result.statusCode:0,
                		errors = ['保存异常','出行交通名称已存在'];
                    util.error(errors[0-parseInt(state)]);
                    btn.enable();
                }
            });
        }
	},
	editTraffic:function(btn){
		var rg = this.getTrafficGrid(),
			win,form,sel,selData,formData={};
		if(sel = util.getSingleModel(rg)){
			win = this.createSaveWindow('修改出行交通',btn.glyph);
			form = win.down('form');
			win.show();
			formData = util.pmModel(sel);
			form.getForm().setValues(formData);
			form.down('citycombo[itemId=sc]').setRawValue(sel.get('START_CITY'));
			form.down('citycombo[itemId=ec]').setRawValue(sel.get('END_CITY'));
		}
	},
	delTraffic:function(btn){
		util.delGridModel(this.getTrafficGrid(),'/resource/traffic/del');
	},
	pubTraffic:function(btn){
		//util.switchGridModel(this.getTrafficGrid(),'/resource/traffic/pub',null,'请检查是否填写交通价格或集合地点');
		var rg = this.getTrafficGrid(),
			win,sel;
		if(sel = util.getSingleModel(rg)){
			win = util.createEmptyWindow('发布交通','&#xe6ac;',300,190,[{
				xtype: 'radiogroup',
	            fieldLabel: '类型',
	            labelWidth:75,
	            columns: 1,
	            items: [
	                {boxLabel: '线路交通', name: 'pm[IS_SALE]', inputValue: 0, checked: true},
	                {boxLabel: '单卖交通', name: 'pm[IS_SALE]', inputValue: 1, hidden:cfg.getUser().saleTraffic!='1',
	                	listeners:{
	                		change :function(ra, newValue, oldValue, eOpts ){
	                			ra.up('radiogroup').nextSibling().setVisible(newValue);
	                		}
	                	}
	                }
	            ]
			},{
				labelWidth:75,
				hidden:true,
				width:150,
				minValue:1,
				margin:'0 0 0 5',
				name:'pm[MIN_BUY]',
				fieldLabel: '最小购票数',
				xtype:'numberfield'
			},{
				name:'pm[ID]',
				value:sel.get('ID'),
				xtype:'hidden'
			}],[{
   				text:'发布',
   				handler:function(bt){
			       	var form = win.down('form'),
						f = form.getForm();
					if(f.isValid()){
						var models = [sel.data]
						f.submit({
							params:{models:Ext.encode(models)},
							submitEmptyText:false ,
							url:cfg.getCtx()+'/resource/traffic/pub',
			                success: function(form, action) {
			                	rg.getStore().load();
			                	util.success('发布交通成功!');
			                	win.close();
			                },
			                failure: function(form, action) {
			                	var state = action.result?action.result.statusCode:0,
			                		errors = ['发布交通异常','请检查是否填写交通价格'];
			                    util.error(errors[0-parseInt(state)]);
			                    win.close();
			                }
						});
					}
   				}
   			},{
   				text:'取消',
   				cls:'disable',
   				handler:function(){win.close();}
   			}]).show();
		}
		
	},
	stopTraffic :function(btn){
		var rg = this.getTrafficGrid(),
			sel;
		if(sel = util.getSingleModel(rg)){
			Ext.Ajax.request({
				url:cfg.getCtx()+'/resource/traffic/unpub?trafficId='+sel.get('ID')+'&isSale='+sel.get('IS_SALE'),
				success:function(response, opts){
					var action = Ext.decode(response.responseText);
					if(action.success){
						util.success('交通停售成功!');
					}else{
						var state = action.statusCode||0,
	                		errors = ['交通停售异常','交通被组团线路占用'];
	                    util.error(errors[0-parseInt(state)]);
					}
					rg.getStore().load();
				}
			});
		}
	},
	saleWindow:function(title,icon){
		var me=this,win = Ext.create('Ext.window.Window',{
			title:util.windowTitle('',title,icon),
			width:300,
			height:210,
			modal:true,
			draggable:false,
			resizable:false,
   			layout:'fit',
			items:[{
				xtype:'form',
    			scrollable:false,
				fieldDefaults: {
			        labelAlign: 'right',
			        labelWidth: 80,
			        msgTarget: 'side'
			    },
				bodyPadding: '15 5 5 5',
			    items: [{
			    	xtype:'hidden',
			    	name:'pm[ID]'
			    },{
			    	xtype:'hidden',
			    	name:'pm[IS_SALE]',
			    	value:1
			    },{
			    	fieldLabel:'最少购票数',
			    	name:'pm[MIN_BUY]',
			    	width:180,
			    	xtype:'numberfield',
			    	value:1,
			    	margin:'0 0 0 5',
			    	minValue: '1',
			    	allowBlank: false
			    },{
			    	fieldLabel:'单卖差价',
			    	name:'pm[SALE_AGIO]',
			    	width:180,
			    	xtype:'numberfield',
			    	value:0,
			    	margin:'10 0 0 5',
			    	allowBlank: false
			    },{
			    	fieldLabel:'销售状态',
			    	margin:'10 0 0 0',
			    	xtype:'radiogroup',
		        	width:200,
		        	items: [
		                {boxLabel: '在售', name: 'pm[SALE_STATUS]', inputValue: 0, checked: true},
		                {boxLabel: '停售', name: 'pm[SALE_STATUS]', inputValue: 1}
		            ]
			    }],
				buttons: [{
			        text: '保存',
			        disabled: true,
			        formBind: true,
			        itemId:'save',
			        handler:function(btn){
			        	var form = btn.up('form'),
			        		win = form.up('window');
			        	me.saveSale(form.getForm(),win);
			        }
			    }]
			}]
		});
		return win;
	},
	saleTraffic:function(btn){
		var rg = this.getTrafficGrid(),
			win,sel;
		if(sel = util.getSingleModel(rg)){
			var sc = sel.get('START_CITY')||'',
				ec = sel.get('END_CITY')||'';
			if(sc==''||ec==''){
				util.alert('单卖团队票需要填写出发/目的地');
				return;
			}
			win = this.saleWindow('单卖团队票',btn.iconCls);
			form = win.down('form');
			win.show();
			formData = util.pmModel(sel);
			form.getForm().setValues(formData);
		}
	},
	saveSale:function(form,win){
		var me = this,btn=win.down('button#save');
		if (form.isValid()) {
			btn.disable();
            form.submit({
            	submitEmptyText:false ,
            	url:cfg.getCtx()+'/resource/traffic/sale',
                success: function(f, action) {
                   util.success('保存数据成功');
                   me.getTrafficGrid().getStore().load();
                   win.close();
                },
                failure: function(form, action) {
                	var state = action.result?action.result.statusCode:0,
                		errors = ['保存异常'];
                    util.error(errors[0-parseInt(state)]);
                    win.close();
                }
            });
        }
	},
	priceTraffic:function(btn){
		var tg = this.getTrafficGrid(),sels = tg.getSelection(),sel;
		if(sels.length==1){
			sel = sels[0];
			var me = this,win = Ext.create('Ext.window.Window',{
				title:util.windowTitle(util.glyphToFont(btn.glyph),'更新价格',''),
				width:700,
				height:450,
				modal:true,
				draggable:false,
				resizable:false,
	   			layout:'fit',
				items:[{
					trafficId:sel.get('ID'),
					trafficPub :sel.get('IS_PUB'),
					trafficSale :sel.get('IS_SALE'),
					xtype:'trcprice'
				}]
			}).show();
			win.down('trcprice').down('pricegrid').hiddenParams = [];
		}else{
			util.alert('请选择一条出行交通');
			return false;
		}
	},
	editSeatTraffic:function(btn){
		var me = this,tg = this.getTrafficGrid(),sels = tg.getSelection(),sel;
		if(sels.length==1){
			sel = sels[0];
			var me = this,win = Ext.create('Ext.window.Window',{
				title:util.windowTitle(util.glyphToFont(btn.glyph),'更新座位',''),
				width:747,
				height:460,
				modal:true,
				draggable:false,
				resizable:false,
	   			layout:'fit',
				items:[{
					xtype:'form',
					bodyPadding: '10',
					fieldDefaults: {
				       labelAlign: 'right',
				       labelWidth: 55,
				       msgTarget: 'side'
					},
					layout: {
				        type: 'hbox',
				        pack: 'start',
				        align: 'stretch'
				    },
					items:[{
						xtype:'hidden',
						name:'pm[TRAFFIC_ID]',
						value:sel.get('ID')
					},{
						xtype:'eventdatepicker',
						disabledDatesText:'无发团日期',
						format:'Ymd',
						showToday:false,
						width:350,
						height:200,
						listeners:{
							monthviewchange:function(a,b,c){
								var nextDay = Ext.Date.format(b,'Y-m')+'-01';
								me.loadDateForSeat(nextDay,sel.get('ID'),win);
							},
							select:function( dp, date, eOpts ){
				        		var d = util.format.date(date,'Ymd'),
				        			dm = dp.dataMap,
				        			tr = dp.nextSibling().down('textarea'),
				        			hid = dp.nextSibling().down('hidden'),
				        			values = tr.getValue(),
				        			hidValues = hid.getValue(),
				        			arrs = values==''?[]:values.split('-'),
				        			hidArrs = hidValues==''?[]:hidValues.split('-'),
				        			idx = Ext.Array.indexOf(arrs,d);
				        		if(idx!=-1){
				        			arrs.splice(idx,1);
				        			hidArrs.splice(idx,1);
				        		}else{
				        			arrs.push(d);
				        			hidArrs.push(dm[d]);
				        		}
				        		tr.setValue(arrs.join('-'));
				        		hid.setValue(hidArrs.join('-'));
				        	}
						}
					},{
						margin:'0 0 0 10',
						layout: {
					        type: 'vbox'
					    },
						flex:1,
						items:[{
							xtype:'hidden',
							name:'pm[SEAT_RULE]'
						},{
							xtype:'textarea',
							emptyText:'选择多个日期，批量更改座位',
							name:'pm[SEAT_DATE]',
							flex:1,
							width:231,
							readOnly:true,
							height:260
						},{
							xtype:'numberfield',
			    			itemId:'seatField',
			    			minValue:0,
			    			value:0,
			    			cls:'editDisable',
			    			fieldLabel:'座位数',
			    			width:150,
			    			margin:'10 0 0 0',
			    			allowBlank:true,
			    			name:'pm[SEAT_COUNT]'
						}]
					}]
				}],
				buttons:[{
					text:'保存',
					handler:function(btn){
						var win = btn.up('window'),
							form = win.down('form'),
							f = form.getForm();
						f.submit({
			            	submitEmptyText:false,
			            	url:cfg.getCtx()+'/resource/traffic/edit/seat',
			                success: function(form, action) {
			                   util.success('保存座位成功');
			                   win.close();
			                },
			                failure: function(form, action) {
			                	var state = action.result?action.result.statusCode:0,
			                		message = action.result?action.result.message:'',
			                		errors = ['保存异常',message+'座位数不能小于可售座位','数据填写不完整'];
			                    util.error(errors[0-parseInt(state)]);
			                }
			            });
					}
				}]
			}).show();
			var firstDay = Ext.Date.format(Ext.Date.getFirstDateOfMonth(new Date()),'Y-m-d');
			//me.loadDateForSeat(firstDay,sel.get('ID'),win);
		}else{
			util.alert('请选择一条出行交通');
			return false;
		}
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
		        	win.close();
		        	util.alert('没有可维护的发团日期');
		        }
		    }
		});	
	},
	saveMusterWindow :function(title,icon){
		var tg = this.getTrafficGrid(),sels = tg.getSelection(),sel;
		if(sels.length==1){
			sel = sels[0];
			var me = this,
				win = Ext.create('Ext.window.Window',{
				title:util.windowTitle(util.glyphToFont(icon),title,''),
				width:360,
				height:230,
				modal:true,
				draggable:false,
				resizable:false,
	   			layout:'fit',
				items:[{
					xtype:'form',
	    			scrollable:false,
					fieldDefaults: {
				        labelAlign: 'right',
				        labelWidth: 60,
				        msgTarget: 'side'
				    },
					bodyPadding: '15 5 5 5',
				    items: [{
				    	xtype:'hidden',
				    	itemId:'trafficId',
				    	value:sel.get('ID'),
				    	name:'pm[TRAFFIC_ID]'
				    },{
				    	xtype:'hidden',
				    	name:'pm[ID]'
				    },{
				    	fieldLabel:'集合地点',
				    	emptyText:'没有可选项，请填写',
						forceSelection: false,
						name : 'pm[MUSTER_PLACE]',
						hiddenName:'pm[MUSTER_PLACE]',
						anchor:'90%',
				    	allowBlank: false,
					    xtype: 'combo',
					    displayField: 'MUSTER_PLACE',
	      				valueField: 'MUSTER_PLACE',
					    store:Ext.create('Ext.data.Store',{
					    	pageSize:100,
					    	model:Ext.create('Ext.data.Model',{
					    		fields:['MUSTER_TIME','MUSTER_PLACE']
					    	}),
					    	proxy: {
						    	autoLoad:true,
						        type: 'ajax',
						        noCache:true,
						        url: cfg.getCtx()+'/resource/traffic/company/muster',
						        reader: {
						            type: 'json',
						            rootProperty: 'data',
						            totalProperty:'totalSize'
						        }
						   	}
					    })
				    },{
				    	fieldLabel:'集合时间',
				    	margin:'10 0 0 0',
				    	width:180,
				    	value:'8:30',
				    	name: 'pm[MUSTER_TIME]',
				    	xtype:'timefield',
				    	format:'H:i',
				    	editable:false,
				    	minValue: '00:00',
	        			maxValue: '24:00',
	        			increment: 30,
				    	allowBlank: false
				    }],
					buttons: [{
				        text: '保存',
				        disabled: true,
				        formBind: true,
				        itemId:'save',
				        handler:function(btn){
				        	var form = btn.up('form'),
				        		win = form.up('window');
				        	me.saveMuster(form.getForm(),win);
				        }
				    }]
				}]
			});
			return win;
		}else{
			util.alert('请选择一条出行交通');
			return false;
		}
	},
	addMuster:function(btn){
		var win = this.saveMusterWindow('添加集合地点',btn.glyph);
		if(win){win.show();}
	},
	editMuster:function(btn){
		var rg = this.getMusterGrid(),
			win,form,sel,selData,formData={};
		if(sel = util.getSingleModel(rg)){
			win = this.saveMusterWindow('修改集合地点',btn.glyph);
			form = win.down('form');
			win.show();
			formData = util.pmModel(sel);
			form.getForm().setValues(formData);
		}
	},
	saveMuster:function(form,win){
		var me = this,btn=win.down('button#save');
		if (form.isValid()) {
			btn.disable();
            form.submit({
            	submitEmptyText:false ,
            	url:cfg.getCtx()+'/resource/traffic/muster/save',
                success: function(f, action) {
                   util.success('保存集合地点成功');
                   var trafficId = win.down('hidden[itemId=trafficId]').getValue();
                   me.getMusterGrid().getStore().load({params:{
						'trafficId':trafficId
				   }});
                   win.close();
                },
                failure: function(form, action) {
                	var state = action.result?action.result.statusCode:0,
                		errors = ['保存异常','集合地点已存在'];
                    util.error(errors[0-parseInt(state)]);
                    win.close();
                }
            });
        }
	},
	delMuster:function(btn){
		var tg = this.getTrafficGrid(),
			sel = tg.getSelection()[0],
			mg = this.getMusterGrid();
		if(mg.getStore().getCount()==1){
			util.alert('集合地点不能为空');
			return;
		}
		util.delGridModel(mg,'/resource/traffic/muster/del',{trafficId:sel.get('ID')});
	}
});