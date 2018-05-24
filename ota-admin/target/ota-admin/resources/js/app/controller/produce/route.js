Ext.define('app.controller.produce.route', {
	extend: 'app.controller.common.BaseController',
	views:['app.view.produce.routeGrid','app.view.resource.route.pub.detail','app.view.resource.route.pub.price'],
	config: {
		control: {
			'toolbar button#setWapPrice':{
				click:'setWapPrice'
			},
			'basegrid':{
	        	selectionchange: function(view, records) {
	            },
	            cellclick:function( view, td, cellIndex, record, tr, rowIndex, e, eOpts ){
					if(cellIndex==3){
						if(e.target.tagName=='A'&&e.target.className.indexOf('title')!=-1)
	                	this.detailWindow(record.get('ID'));
					}
					if(cellIndex==6){
						if(e.target.tagName=='I'&&e.target.className.indexOf('dd')!=-1){
							 var win = Ext.create('Ext.window.Window',{
								title:util.windowTitle('&#xe65d;','地接价',''),
								width:700,
								bodyPadding:5,
								height:220,
								modal:true,
								draggable:false,
								resizable:false,
					   			layout:'fit',
								items:[{
									routeId:record.get('ID'),
									xtype:'rprice'
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
					}
					if(cellIndex==7){
						if(e.target.tagName=='I'&&e.target.className.indexOf('pp')!=-1){
							var isFullPrice = parseInt(record.get('IS_FULL_PRICE')||'0');
							this.routePrice(record.get('ID'),isFullPrice==1);
						}
						if(e.target.tagName=='I'&&e.target.className.indexOf('dd')!=-1){
							 var win = Ext.create('Ext.window.Window',{
								title:util.windowTitle('&#xe65d;','地接价',''),
								width:700,
								bodyPadding:5,
								height:220,
								modal:true,
								draggable:false,
								resizable:false,
					   			layout:'fit',
								items:[{
									routeId:record.get('ID'),
									xtype:'rprice'
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
					}
				}
	        }/*,
	        'splitbutton[itemId=moreQuery] menu':{
	        	click:function(menu,item){
	        		this.moreQuery.call(this,[item.value]);
	        	}
	        }*/
		},
		refs: {
			routeGrid: 'proroutegrid'
        }
	},
	onBaseGridRender:function(g){
		var me = this;
		setTimeout(function(){
		Ext.Ajax.request({
		    url: util.getPowerUrl(),
		    success: function(response, opts) {
		        var obj = Ext.decode(response.responseText),
		        	items = obj.children;
		        items.push('->');
				items.push({
					emptyText:'产品标题/编号/目的地',
		        	width:200,
		        	xtype:'searchfield',
		        	store:g.getStore()
				});
				items.splice(0,0,{
					xtype:'segmentedbutton',
					listeners:{
						toggle:function(sb, button, isPressed, eOpts){
							var v = sb.getValue()=='1'?'1':null,
								vs = {isSinglePub:v};
							if(v==1){
								g.columns[5].show();
								g.columns[6].hide();
								sb.nextSibling().nextSibling().hide();
							}else{
								g.columns[5].hide();
								g.columns[6].show();
								sb.nextSibling().nextSibling().show();
							}
							Ext.applyIf(vs,g.getStore().getProxy().getExtraParams());
							g.getStore().getProxy().setExtraParams(vs);
					       	g.getStore().load();
						}
					},
					items: [{
                        text: '跟团游',
                        pressed: true
                    }, {
                        text: '买地接'
                    }]
		        });
				items.splice(1,0,{
					xtype:'container',
					itemId:'queryText',
					layout:'hbox',
					items:[{
						xtype:'splitbutton',
						text:'已选查询条件，点击<i class="iconfont" >&#xe66f;</i>进行更多筛选',
						cls:'tag-split-btn remove'
					}]
		        });
				items.push({
		        	itemId:'routeType',
		        	xtype:'combo',
					forceSelection: true,
					editable:false,
		            valueField: 'value',
		            displayField: 'text',
		            store:util.createComboStore([{
		            	text:'全部',
		            	value:''
		            },{
		            	text:'周边',
		            	value:1
		            },{
		            	text:'国内',
		            	value:2
		            },{
		            	text:'出境',
		            	value:3
		            }]),
		            width:60,
		            minChars: 0,
		            value:'',
		            queryMode: 'local',
		            typeAhead: true,
		            listeners:{
		            	change:function(c, newValue, oldValue, eOpts ){
							g.getStore().getProxy().setExtraParams(Ext.applyIf({routeType:newValue},g.getStore().getProxy().getExtraParams()));
							g.getStore().load();
		            	}
		            }
		        });
				items.push({
					tooltip:'更多筛选',
		        	glyph:'xe66f@iconfont',
		        	itemId:'moreQuery',
		        	/*xtype:'splitbutton',
		        	menu:Ext.create('Ext.menu.Menu', {
					    minWidth:70,
					    items: [{
			            	text:'周边',
			            	glyph:'xe6ec@iconfont',
			            	value:1
			            },{
			            	text:'国内',
			            	glyph:'xe65c@iconfont',
			            	value:2
			            },{
			            	text:'出境',
			            	glyph:'xe6ed@iconfont',
			            	value:3
			            }]
					}),*/
		        	handler:function(b){
		        		//util.alert('请选择线路类型');
		        		me.moreQuery.call(me,b.previousSibling().getValue());
		        	}
		        });
		        util.createGridTbar(g,items);
		        
		        if(dynamicParamsSupplyName!=''){
					var qt = g.down('container#queryText');
					qt.removeAll();
					qt.add({
						key:'supplys',
						xtype:'splitbutton',
						text:'供应商：'+dynamicParamsSupplyName,
						margin:'0 0 0 4',
						cls:'tag-split-btn remove',
						listeners:{
							arrowclick :function(sb){
								util.redirectTo('produce/route','?dynamicParamsSupplyName=')
							}
						}
					});
				}
		        
		    }
		});
		},500);
	},
	btnToggle :function(btns,v){
		for(var i=0;i<btns.length;i++){
			if(btns[i]){
				btns[i].setDisabled(v);
			}
		}
	},
	detailWindow :function(routeId){
		var win = Ext.create('Ext.window.Window',{
			title:util.windowTitle('&#xe635;','线路详情',''),
			bodyPadding:5,
			modal:true,
			draggable:false,
			resizable:false,
			maximizable:false,
			width:'90%',
			height:'85%',
   			layout:'fit',
   			items:[{
   				xtype:'routedetail'
   			}]
		});
		win.show();
		var rd = win.down('routedetail');
		rd.setRouteId(routeId);
	},
	moreQuery :function(type,isSinglePub){
		type = type==''?'':type;
		var me = this,win = Ext.create('Ext.window.Window',{
			title:util.windowTitle('&#xe66f;','更多筛选',''),
			bodyPadding:5,
			width:'85%',
			height:'85%',
			modal:true,
			draggable:true,
			resizable:false,
			maximizable:false,
   			layout:'fit',
   			items:[{
		    	xtype:'form',
		    	autoScroll:true,
		    	fieldDefaults: {
			        labelWidth: 60,
       				labelAlign:'right'
			    },
		    	items:[{
		    		bodyStyle:'padding-bottom:5px;',
		    		border:true,
		    		/*dockedItems:{
	    				xtype:'toolbar',
	    				items:[{
	    					xtype:'tabbar',
	    					frame:true,
	    					items:[{
	    						text: '周边'
	    					},{
	    						text: '周边'
	    					}]
	    				},'->',{
	    					xtype:'segmentedbutton',
	    					defaults:{
	    						handler:function(b){
	    							me.loadCityTags(win,b.val);
	    							var cg = b.up('toolbar').up('panel').down('panel[itemId=citytags]').down('checkboxgroup[itemId=citys]');
	    							cg.removeAll(true);
	    							cg.add({xtype:'label',html:'请选择区域'});
	    							me.loadSupplyTags(win,b.val);
	    						}
	    					},
	    					items: [{
		                        text: '周边',
		                        val:'1',
		                        pressed : type==1
		                    }, {
		                        text: '国内',
		                        val:'2',
		                        pressed : type==2
		                    }, {
		                        text: '出境',
		                        val:'3',
		                        pressed : type==3
		                    }]
	    				}]
	    			},*/
		    		dockedItems:{
	    				xtype:'tabpanel',
	    				listeners:{
	    					tabchange:function( tabPanel, newCard, oldCard, eOpts ){
	    						me.loadCityTags(win,newCard.val);
    							var cg = tabPanel.up('panel').down('panel[itemId=citytags]').down('checkboxgroup[itemId=citys]');
    							cg.removeAll(true);
    							cg.add({xtype:'label',html:'请选择区域'});
    							me.loadSupplyTags(win,newCard.val);
	    					}
	    				},
	    				frame:true,
	    				activeTab :type,
	    				items:[{
	                        title: '全部',
	                        val:''
	                    },{
	                    	title: '周边',
	                        val:'1'
	                    }, {
	                    	title: '国内',
	                        val:'2'
	                    }, {
	                    	title: '出境',
	                        val:'3'
	                    }]
		    		},
		    		items:[{
		    			itemId:'citytags',
		    			items:[{
		    				xtype:'radiogroup',
		    				margin:'5 0',
							layout:'auto',
							itemId:'areas',
							fieldLabel:'区域',
							defaults:{cls:'inline_checkbox'},
							items:[{xtype:'label',html:'没有查询结果'}]
		    			},{
		    				xtype:'checkboxgroup',margin:'5 0',
		    				itemId:'citys',
							layout:'auto',
							fieldLabel:'目的地',
							defaults:{cls:'inline_checkbox'},
							items:[{xtype:'label',html:'请选择区域'}]
		    			}]
		    		},{
		    			margin:'5 0',
	    				xtype:'checkboxgroup',
	    				itemId:'supplys',
	    				fieldLabel:'供应商',
						layout:'auto',
						defaults:{cls:'inline_checkbox'},
						items:[{xtype:'label',html:'没有查询结果'}]
	    			}]
		    	},{
		    		xtype:'radiogroup',
		    		margin:'5 0',
					fieldLabel:'天数',
					layout:'auto',
					defaults:{cls:'inline_checkbox'},
					items: [
		                {boxLabel: '不限', inputValue:'', name: 'dayCount'},
		                {boxLabel: '1天', inputValue:'1', name: 'dayCount'},
		                {boxLabel: '2天', inputValue:'2', name: 'dayCount'},
		                {boxLabel: '3天', inputValue:'3', name: 'dayCount'},
		                {boxLabel: '4天', inputValue:'4', name: 'dayCount'},
		                {boxLabel: '5天', inputValue:'5', name: 'dayCount'},
		                {boxLabel: '6天', inputValue:'6', name: 'dayCount'},
		                {boxLabel: '6天以上', inputValue:'7', name: 'dayCount'}
		            ]
		    	},{
		    		xtype:'checkboxgroup',
		    		margin:'10 0 5 0',
					fieldLabel:'主题属性',
					layout:'auto',
					itemId:'themes',
					defaults:{cls:'inline_checkbox',name:'themes'},
					items: [
		                {boxLabel: '沙漠', inputValue:'沙漠'},
		                {boxLabel: '美食', inputValue:'美食'},
		                {boxLabel: '宗教', inputValue:'宗教'},
		                {boxLabel: '游学', inputValue:'游学'},
		                {boxLabel: '探险', inputValue:'探险'},
		                {boxLabel: '海岛度假', inputValue:'海岛度假'},
		                {boxLabel: '夏令营', inputValue:'夏令营'},
		                {boxLabel: '漂流', inputValue:'漂流'},
		                {boxLabel: '邮轮', inputValue:'邮轮'},
		                {boxLabel: '夕阳红', inputValue:'夕阳红'},
		                {boxLabel: '温泉', inputValue:'温泉'},
		                {boxLabel: '古镇', inputValue:'古镇'},
		                {boxLabel: '滑雪', inputValue:'滑雪'},
		                {boxLabel: '红色', inputValue:'红色'},
		                {boxLabel: '专列', inputValue:'专列'},
		                {boxLabel: '门票', inputValue:'门票'},
		                {boxLabel: '度假', inputValue:'度假'},
		                {boxLabel: '亲子', inputValue:'亲子'},
		                {boxLabel: '蜜月', inputValue:'蜜月'},
		                {boxLabel: '青年会', inputValue:'青年会'}
		            ]
		    	},{
		    		xtype:'radiogroup',
		    		margin:'5 0',
					fieldLabel:'线路属性',
					layout:'auto',
					defaults:{cls:'inline_checkbox'},
					items: [
					    {boxLabel: '不限', inputValue:'', name: 'attr'},
		                {boxLabel: '品质', inputValue:'品质', name: 'attr'},
		                {boxLabel: '豪华', inputValue:'豪华', name: 'attr'},
		                {boxLabel: '自由行', inputValue:'自由行', name: 'attr'},
		                {boxLabel: '特价', inputValue:'特价', name: 'attr'},
		                {boxLabel: '包机', inputValue:'包机', name: 'attr'},
		                {boxLabel: '纯玩', inputValue:'纯玩', name: 'attr'},
		                {boxLabel: '自驾游', inputValue:'自驾游', name: 'attr'}
		            ]
		    	},{
		    		xtype:'radiogroup',
		    		margin:'5 0',
					fieldLabel:'价格',
					layout:'auto',
					defaults:{cls:'inline_checkbox'},
					items: [
		                {boxLabel: '不限', inputValue:'', name: 'price'},
		                {boxLabel: '500以下', inputValue:'0-500', name: 'price'},
		                {boxLabel: '500-1000', inputValue:'500-1000', name: 'price'},
		                {boxLabel: '1000-2000', inputValue:'1000-2000', name: 'price'},
		                {boxLabel: '2000-4000', inputValue:'2000-4000', name: 'price'},
		                {boxLabel: '4000-6000', inputValue:'4000-6000', name: 'price'},
		                {boxLabel: '6000以上', inputValue:'6000', name: 'price'}
		            ]
		    	},{
		    		xtype:'fieldcontainer',
		    		hideLabel:true,
		    		margin:'5 0',
		    		layout:'hbox',
		    		items:[{
		    			fieldLabel: '出发日期',
		    			name:'BEGIN_DATE',
		    			width:165,
		    			xtype:'datefield',
		    			format:'Y-m-d',
		    			endDateField: 'endxddt',
				        itemId:'startxddt',
				        showToday:false,
				        selectOnFocus:true, 
        				editable:false, 
				        vtype: 'daterange'
		    		},{
			            xtype: 'splitter'
			        },{
		    			hideLabel:true,
		    			width:105,
		    			selectOnFocus:true, 
        				editable:false,
		    			xtype:'datefield',
		    			name:'END_DATE',
		    			format:'Y-m-d',
		    			itemId:'endxddt',
		    			showToday:false,
			            startDateField: 'startxddt',
			            vtype: 'daterange'
		    		}]
		    	},{
		    		fieldLabel: '关键字',
		    		xtype:'textfield',
		    		name:'query',
	    			width:280,
		    	}]
   			}],buttons:[{
		        text: '确定',
		        handler:function(btn){
		        	var f = btn.up('window').down('form'),
		        		g = me.getRouteGrid();
		        	var vs = f.getValues(0,0,0,1);
		        	var ck1 = f.down('checkboxgroup[itemId=themes]').getChecked(),
		        		themes = '',
		        		ck2 = f.down('checkboxgroup[itemId=citys]').getChecked(),
		        		citys = '',
		        		citysTxt = '',
		        		ck3 = f.down('checkboxgroup[itemId=supplys]').getChecked(),
		        		supplys = '';
		        	
		        	for(var i=0;i<ck1.length;i++){
		        		if(i>0){
		        			themes+=',';
		        		}
		        		themes += ck1[i].inputValue;
		        	}
		        	for(var i=0;i<ck2.length;i++){
		        		if(i>0){
		        			citys+=',';
		        			citysTxt += ',';
		        		}
		        		citys += ck2[i].inputValue;
		        		citysTxt += ck2[i].boxLabel;
		        	}
		        	for(var i=0;i<ck3.length;i++){
		        		if(i>0){
		        			supplys+=',';
		        		}
		        		supplys += ck3[i].inputValue;
		        	}
		        	vs.themes = themes;
		        	vs.citys = citys;
		        	vs.supplys = supplys;
		        	if(citysTxt!=''){
		        		vs.citysTxt = citysTxt;
		        	}else{
			        	var area = f.down('radiogroup[itemId=areas]').getChecked();
			        	if(area.length>0)
			        	vs.areaTxt = area[0].boxLabel;
		        	}
		        	
		        	var bd = f.down('datefield[itemId=startxddt]'),ed = f.down('datefield[itemId=endxddt]'),
		        		bdv = bd.getValue()||'',
		        		edv = ed.getValue()||'';
		        	
		        	if(bdv!=''){
		        		vs['BEGIN_DATE'] =  Ext.Date.format(new Date(bdv),'Y-m-d'); 
		        	}
		        	if(edv!=''){
		        		vs['END_DATE'] =  Ext.Date.format(new Date(edv),'Y-m-d'); 
		        	}
		        	
		        	
		        	Ext.applyIf(vs,g.getStore().getProxy().getExtraParams());
		        	g.getStore().getProxy().setExtraParams(vs);
					g.getStore().load();
					btn.up('window').close();
					var qt = g.down('container[itemId=queryText]');
					qt.removeAll(true);
					if(vs.citys==''){delete vs.citysTxt;}else{delete vs.areaTxt;}
					for(var i in vs){
						var text = vs[i];
						if(text&&i=='dayCount'){
							var dcArr = ['','1天','2天','3天','4天','5天','6天','6天以上'];
							text = dcArr[text];
						}
						if(vs[i]&&i!='routeType'&&i!='area'&&i!='citys'&&vs[i]!=''&&i!='isSinglePub'){
							if(i=='supplys'){
								text = '供应商：'+vs[i];
							}
							if(i=='citysTxt'){
								text = '目的地：'+vs[i];
							}
							if(i=='areaTxt'){
								text = '区域：'+vs[i];
							}
							qt.add({
								key:i,
								xtype:'splitbutton',
								text:text,
								margin:'0 0 0 4',
								cls:'tag-split-btn remove',
								listeners:{
									arrowclick :function(sb){
										var vs = g.getStore().getProxy().getExtraParams();
										if(sb.key=='areaTxt'){
											delete vs['area'];
										}
										if(sb.key=='citysTxt'){
											delete vs['area'];
											delete vs['citys'];
										}
										delete vs[sb.key];
							        	g.getStore().getProxy().setExtraParams(vs);
										g.getStore().load();
										sb.up('container[itemId=queryText]').remove(sb,true);
									}
								}
							});
						}
					}
		        }
		    },{
		    	text:'取消',
		    	cls:'disable',
		    	handler:function(btn){
		        	win.close();
		        }
		    	
		    }]
		}).show();
		//加载
		me.loadCityTags(win,type);
		me.loadSupplyTags(win,type);
	},loadCityTags :function(win,type){
		var me = this,g = this.getRouteGrid(),
			pms = g.getStore().getProxy().getExtraParams(),
			pstr = pms['isSinglePub']?'&isSinglePub=1':'';
		Ext.Ajax.request({
			url:cfg.getCtx()+'/produce/route/list/label?type='+type+pstr,
			success:function(response, opts){
				var obj = Ext.decode(response.responseText),
					data = obj.data,
					ctags = win.down('panel[itemId=citytags]'),
					ctagstool = ctags.down('radiogroup');
				ctagstool.removeAll(true);
				if(data.length>0){
					for(var i=0;i<data.length;i++){
						ctagstool.add({
							name:'area',
							inputValue:data[i].ID,
							boxLabel:data[i].TEXT,
							listeners:{
								change :function(b, newValue, oldValue, eOpts){
									Ext.Ajax.request({
										url:cfg.getCtx()+'/produce/route/list/label?type='+type+pstr+'&labelId='+b.getGroupValue(),
										success:function(response, opts){
											var obj = Ext.decode(response.responseText),
												data = obj.data,
												cks = b.up('panel[itemId=citytags]').down('checkboxgroup[itemId=citys]');
											cks.removeAll(true);
											if(data.length>0){
												for(var j=0;j<data.length;j++){
													cks.add({
														boxLabel: data[j].CITY_NAME, inputValue:data[j].CITY_ID, name: 'citys'
													});
												}
											}else{
												cks.add({xtype:'label',html:'没有查询结果'});
											}
										}
									});
								}
							}
						});
					}
				}else{
					ctagstool.add({xtype:'label',html:'没有查询结果'});
				}
			}
		});
	},loadSupplyTags :function(win,type){
		var me = this,g = this.getRouteGrid(),
			pms = g.getStore().getProxy().getExtraParams(),
			pstr = pms['isSinglePub']?'&isSinglePub=1':'';
		Ext.Ajax.request({
			url:cfg.getCtx()+'/produce/route/list/company?type='+type+pstr,
			success:function(response, opts){
				var obj = Ext.decode(response.responseText),
					data = obj.data,
					ctags = win.down('checkboxgroup[itemId=supplys]');
				ctags.removeAll(true);
				if(data.length>0){
					for(var i=0;i<data.length;i++){
						ctags.add({
							name:'supplys',
							inputValue:data[i].BRAND_NAME,
							boxLabel:data[i].BRAND_NAME+'('+data[i].CNT+')'
						});
					}
				}else{
					ctags.add({xtype:'label',html:'没有查询结果'});
				}
			}
		});
	},
	routePrice :function(routeId,isFullPrice){
		var me = this,win;
		Ext.require('Ext.ux.form.DatePickerEvent',function(){
			win = Ext.create('Ext.window.Window',{
				title:util.windowTitle(isFullPrice?'&#xe646;':'&#xe61f;',isFullPrice?'线路打包价':'线路综合报价',''),
				width:1000,
				height:377,
				modal:true,
				draggable:false,
				resizable:false,
				bodyPadding:1,
	   			layout:'border',
	   			items:[{
	   				region:'center',
	   				cls:'eventdatepicker selector',
	   				xtype:'eventdatepicker',
					disabledDatesText:'无发团日期',
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
	   				region:'east',
	   				width:650,
	   				itemId:'pricePlan',
	   				xtype:'tabpanel',
	   				margin:'0 0 0 1',
	   				activeTab: 0,
	   				plain:true,
	   				defaults: {
				        bodyPadding: 0
				    },
	   				items:[{
		                title: '方案名称',
		                bodyPadding:10,
		                html: '选择具体日期查看交通线路报价'
		            }]
	   			}]
	   		}).show();
	   		var firstDay = Ext.Date.format(Ext.Date.getFirstDateOfMonth(new Date()),'Y-m-d');
			me.loadDateForSeat(firstDay,routeId,win,isFullPrice);
		});
	},
	loadPriceForDate :function(date,routeId,win,isFullPrice){
		var pp = win.down('tabpanel[itemId=pricePlan]');
		Ext.Ajax.request({
		    url: cfg.getCtx()+'/resource/route/list/plan?routeId='+routeId+'&startDate='+date,
		    success: function(response, opts) {
		    	 var obj = Ext.decode(response.responseText),
		        	data = obj.data;
		        pp.removeAll(true);
		    	if(data.length>0){
					for(var i=0;i<data.length;i++){
						var title = data[i].PLAN_TITLE;
						if(isFullPrice){
							title = title.substring(0,title.lastIndexOf('-'));
						}
						pp.add({
							title:title,
							id:data[i].PLANID,
							items:[{
								xtype:'rprice',
								myLoadId:data[i].PLANID,
								myLoadUrl:cfg.getCtx()+'/resource/route/list/plan/price?routeId='+routeId+'&startDate='+date+'&planId='
							},{
								border:false,
						    	cls: 'data-view',
						        itemSelector: '.data-view-item',
						        xtype: 'dataview',
						        overItemCls: 'hover',  
						        trackOver:true,
						        store :Ext.create('Ext.data.Store', {
						        	model:Ext.create('Ext.data.Model',{
						        		fields: ['TRAFFIC_NAME','DATE','STAY_CNT','SEAT']
						        	}),
						        	autoLoad: true, 
						        	planId:data[i].PLANID,
						        	routeId:routeId,
						        	startDate:date,
						        	listeners:{
						        		beforeload:function(s){
						        			s.getProxy().setExtraParams({routeId:s.routeId,planId:s.planId,startDate:s.startDate});
						        		}
						        	},
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
						        	'<tpl for="."><div class="data-view-item block" style="margin:5px 40px 5px 0px;width:180px;padding:2px 0px 8px 8px;border:1px dashed #d1d1d1;">',
						        	'<h3 style="font-size:14px;padding:2px 0px;line-height:22px;color:#427fed">{TRAFFIC_NAME}</h3>',
						        	'<div class="ht20">',
						        	'出发日期：<span style="font-size:16px;">{[this.fd(values.DATE)]}</span>',
						        	'</div>',
						        	//'<div style="">',
						        	//'停留天数：<span style="font-size:16px;">{STAY_CNT}</span>',
						        	//'</div>',
						        	'<div class="ht20">',
						        	'剩余座位：<span style="font-size:16px;color:#4CAF50;">{SEAT}</span>',
						        	'</div>',
						        	//'<div class="step"><i class="iconfont">&#xe69e;</i></div>',
						        	'</div></tpl>',{
						        		fd :function(v){
											return Ext.Date.format(Ext.Date.parse(v, "Ymd"), "Y/m/d");
						        		}
						        	}
						        ]
							}]
						});
						
					}
				}else{
					pp.add({
						title:'没有查询结果',
						bodyPadding:10,
						html:'更换其他日期试试'
					});
				}
				pp.setActiveTab(0);
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
	},
	setWapPrice:function(btn){
		var me=this,sel,grid=this.getRouteGrid();
		if(sel = util.getSingleModel(grid)){
			var win = Ext.create('Ext.window.Window',{
	   			title: util.windowTitle(util.glyphToFont(btn.glyph),btn.getText(),''),
	   			width:360,
	   			height:220,
	   			draggable:false,
				resizable:false,
				modal:true,
	   			bodyStyle:'background:#fff;font-size:16px;color:#bbb;',
	   			layout: {
			        type: 'vbox',
			        pack: 'start',
			        align: 'stretch'
			    },
	   			items:[{
					xtype:'container',
					html:[
					      '<div class="yellow-color ht20"><i class="iconfont f17">&#xe6f6;</i> 网站价(前台网站外卖价) = 产品同行价+上浮价格',
					      '<div class="yellow-color" style="padding-left:20px;">上浮价格不填写或填写为0，网站价 = 产品外卖价</div>',
					      '</div>'
					].join(''),
					height:50,
					style:'padding:5px;',
					cls:'low'//good
				},{
					flex:1,
					fieldDefaults: {
				        labelAlign: 'right',
				        labelWidth: 85
				    },
					xtype:'form',
	   				items: [{
	   					margin:'10 0 0 0',
	   					xtype:'container',
	   					height:30,
	   					html:'<span style="margin-left:45px;padding:5px 0">同行价: <span style="margin-left:3px;">'+util.moneyFormat(sel.get('ROUTE_INTER_PRICE'))+'</span></span>'
	   				},{
	   					xtype:'hiddenfield',
	   					name:'pm[PRO_ID]',
	   					value:sel.get('ID')
	   				},{
				        xtype: 'numberfield',
				        name: 'pm[PRICE]',
				        anchor:'50%',
				        allowBlank:false,
				        fieldLabel: '上浮价格',
				        value:sel.get('ROUTE_WAP_PRICE')||0
				    }],
				    buttons:[{
				    	text:'保存',
				    	handler:function(b){
				    		var f = b.up('form'),bf = f.getForm();
				    		if(bf.isValid()){
				    			bf.submit({
				    				submitEmptyText:false ,
	   								url:cfg.getCtx()+'/produce/route/save/wap/price',
					                success: function(form, action) {
					                	grid.getStore().load();
					                   util.success('网站价格设置成功');
					                   win.close();
					                },
					                failure: function(form, action) {
					                    util.error('网站价格设置失败');
					                    win.close();
					                }
	   							});
				    		}
				    	}
				    }]
	   			}]
	   		}).show();
		}
	}
});