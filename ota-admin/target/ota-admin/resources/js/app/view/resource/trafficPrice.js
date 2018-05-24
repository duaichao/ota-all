Ext.define('app.view.resource.trafficPrice', {
	extend: 'Ext.form.Panel',
	layout: 'card',
	xtype:'trcprice',
	defaults: {
        border:false
    },
	scrollable:false,
	fieldDefaults: {
       labelAlign: 'right',
       labelWidth: 55,
       msgTarget: 'side'
	},
    defaultListenerScope: true,
    config:{
    	trafficId:'',
    	trafficPub:'',
    	trafficSale:'',
    	isFullPrice:0,
    	fullPriceFromList:0
    },
    rule:['全部日期','排除星期X','只发星期X','排除日期X','只发日期X'],
	initComponent: function() {
		var me = this;
		this.tbar = [{
			glyph:'xe62b@iconfont',
			cls:'sel',
			handler: 'showNext',
   			text:'添加'
   		},{
   			glyph:'xe62d@iconfont',
   			cls:'sel',
   			handler:'editPrice',
   			text:'修改'
   		},{
   			glyph:'xe62c@iconfont',
   			cls:'sel',
   			hidden:this.getIsFullPrice()==1,
   			handler:'delPrice',
   			text:'删除'
   		},{
   			glyph:'xe644@iconfont',
   			cls:'sel',
   			hidden:this.getIsFullPrice()==1,
   			handler:'pubPrice',
   			text:'发布'
   		},'->',{
   			xtype: 'segmentedbutton',
            items: [{
            	itemId: 'card-prev',
                text: '&laquo; 所有价格',
                handler: 'showPrevious',
                disabled:true,
                pressed: true
            }, {
            	itemId: 'card-next',
            	handler: 'showNext',
                text: '新价格 &raquo;'
            }]
   		}];
		this.items = [{
			id: 'cardprice-0',
    		xtype:'gridpanel',
    		itemId:'rulegrid',
    		border:true,
    		columnLines: true,
	    	margin:'0 8 10 8',
		    selType:'checkboxmodel',
		    listeners:{
		    	selectionchange: function(view, records) {
					var tools = this.up('trcprice').down('toolbar'),
						isPub = parseInt(records[0].get('IS_PUB')||0);;
					//tools.down('button[text=修改]').setDisabled(isPub==1);
					tools.down('button[text=删除]').setDisabled(isPub==1);
					tools.down('button[text=发布]').setDisabled(isPub==1);
	            }
		    },
	    	store:util.createGridStore(cfg.getCtx()+'/resource/traffic/price/rule?fullPriceFromList='+me.getFullPriceFromList()+'&isFullPrice='+me.getIsFullPrice()+'&trafficId='+this.getTrafficId(),Ext.create('app.model.resource.traffic.rule')),
	    	columns:[Ext.create('Ext.grid.RowNumberer',{width:35}),{
		        text: '开始日期',
		        width:120,
		        padding:'1 0 1 0',
		        dataIndex: 'BEGIN_DATE'
		    },{
		        text: '结束日期',
		        width:120,
		        padding:'1 0 1 0',
		        dataIndex: 'END_DATE'
		    },{
		        text: '发布状态',
		        width:80,
		        dataIndex: 'IS_PUB',
		        renderer: function(value,metaData,record,colIndex,store,view){
		        	metaData.tdAttr = 'data-qtip="已发布的价格不可修改"';
		        	if(value=='1'){
		        		return '<span class="blue-color">已发布</span>'
		        	}else{
		        		return '<span class="disable-color">未发布</span>'
		        	}
		        }
		    },{
		        text: '团期/规则',
		        padding:'1 0 1 0',
		        flex:1,
		        dataIndex: 'TYPE',
		        renderer: function(value,metaData,record,colIndex,store,view){
		        	var de = record.get('TEXT')||'',
		        		arr = de.split('-'),qtip=[];
		        	if(de!=''){
		        	for(var i=0;i<arr.length;i++){
		        		qtip.push('<p>'+arr[i]+'</p>');
		        	}
		        	}
		        	return me.rule[parseInt(value)]+(qtip.length>0?' <i class="green-color icon-info-circled" style="float:right;" data-qtip="'+qtip.join('')+'"></i>':'');
		        }
		    }]
    	},{
    		id: 'cardprice-1',
    		layout:'anchor',
    		defaults:{anchor:'100%'},
    		items:[{
		    	xtype: 'fieldcontainer',
		    	margin: '0 8',
		    	layout: 'hbox',
		        items: [{
		        	xtype:'hidden',
		        	name:'pm[IS_PUB]'
		        },{
		        	xtype:'hidden',
		        	name:'pm[ID]'
		        },{
		        	cls:'editDisable',
		            xtype: 'datefield',
		            name:'pm[BEGIN_DATE]',
		            emptyText:'开始日期',
		            allowBlank:true,
		            format:'Ymd',
		            endDateField: 'enddt',
		            itemId:'startdt',
		            showToday:false,
		            vtype: 'daterange',
		            flex:1,
		            listeners:{
		            	change:function(f,v,o){
		            		var muliPicker = f.up('trcprice').down('datepicker[itemId=muliPicker]')
		            		muliPicker.setMinDate(v);
		            		muliPicker.setValue(v);
		            	}
		            }
		        }, {
		            xtype: 'splitter'
		        }, {
		        	cls:'editDisable',
		            xtype: 'datefield',
		            name:'pm[END_DATE]',
		            emptyText:'结束日期',
		            showToday:false,
		            allowBlank:true,
		            format:'Ymd',
		            itemId:'enddt',
		            startDateField: 'startdt',
		            vtype: 'daterange',
		            flex:1,
		            listeners:{
		            	change:function(f,v,o){
		            		var muliPicker = f.up('trcprice').down('datepicker[itemId=muliPicker]')
		            		muliPicker.setMaxDate(v);
		            	}
		            }
		        }, {
		            xtype: 'splitter'
		        },{
		        	cls:'editDisable',
					hiddenName:'pm[TYPE]',
					name:'pm[TYPE]',
					width:120,
					allowBlank:true,
					xtype:'combobox',
					itemId:'rulerg',
					value:0,
					displayField:'text',
					valueField: 'value',
		            typeAhead: true,
		            editable:false,
		            queryMode: 'local',
		            store: Ext.create('Ext.data.Store', {
					    fields: ['text', 'value'],
					    data : [
					    	{"value":"0", "text":"全部日期"},
					        {"value":"1", "text":"排除星期X"},
					        {"value":"2", "text":"只发星期X"},
					        {"value":"3", "text":"排除日期X"},
					        {"value":"4", "text":"只发日期X"}
					    ]
					}),
		            listeners:{
		            	select:function(f,v){
		            		var begin = f.previousSibling().previousSibling().previousSibling().previousSibling(),
		            			end = f.previousSibling().previousSibling();
		            		if(begin.getRawValue()==''||end.getRawValue()==''){
		            			util.alert('请选择价格日期区间');
		            			f.setValue(0);
		            		}
		            	},
		            	change:function(rg, newValue, oldValue, eOpts ){
			        		var hp = rg.up('fieldcontainer').nextSibling(),t = parseInt(newValue);
			        		switch(t){
			        			case 0:
			        				hp.down('datepicker').hide();
			        				hp.down('checkboxgroup').hide();
			        				hp.down('textarea').hide();
			        				break;
			        			case 1:
			        			case 2:
			        				hp.down('datepicker').hide();
			        				hp.down('checkboxgroup').show();
			        				hp.down('textarea').hide();
			        				break;
			        			case 3:
			        			case 4:
			        				hp.down('datepicker').show();
			        				hp.down('checkboxgroup').hide();
			        				hp.down('textarea').show();
			        				break;
			        		}
			        	}
		            }
		        }]
		    },{
		    	margin:'0 0 0 8',
		    	layout: 'hbox',
		    	items:[{
		    		itemId:'muliPicker',
		    		hidden:true,
		    		width:218,
		    		border:false,
		    		margin:'0 7 0 0',
		    		cls:'eventdatepicker selector small',
	   				xtype:'eventdatepicker',
					disabledDatesText:'',
					format:'Ymd',
					showToday:false,
					listeners:{
						monthviewchange:function(a,b,c){
							/*var nextExt = Ext.Date.format(b,'Ym'),
								tr = a.nextSibling().down('textarea'),
								values = tr.getValue(),
								arrs = values==''?[]:values.split('-');
							if(a.cells){
								var els = a.cells.elements;
								Ext.Array.each(els,function(el,i){
									el = Ext.get(el);
									for(var j=0;j<arrs.length;j++){
										console.log(el.getAttribute('aria-value')+"==="+arrs[j]);
										if(el.getAttribute('aria-value')==arrs[j]){
											el.addCls('my-selected');
										}
									}
								});
							}*/
						},
						select:function( dp, date, e ){
        	        		var d = util.format.date(date,'Ymd'),
        	        			tr = dp.nextSibling().down('textarea'),
        	        			values = tr.getValue(),
        	        			arrs = values==''?[]:values.split('-'),
        	        			idx = Ext.Array.indexOf(arrs,d);
        	        		if(idx!=-1){
        	        			Ext.get(dp.activeCell).removeCls('my-selected');
        	        			arrs.splice(idx,1);
        	        		}else{
        	        			Ext.get(dp.activeCell).addCls('my-selected');
        	        			arrs.push(d);
        	        		}
        	        		tr.setValue(arrs.join('-'));
        	        	}
					}
		    		
		    	},{
		    		flex:1,
		    		layout: {
				        type: 'vbox',
				        pack: 'start',
				        align: 'stretch'
				    },
		    		items:[{
		    			xtype: 'checkboxgroup',
		    			itemId:'weeks',
		    			hidden:true,
		    			margin:'0 0 0 0',
		    			layout:'hbox',
		    			defaults:{width:40},
			    		items: [
			    			{cls:'editDisable',boxLabel: '日', name: 'WEEKS',inputValue:'星期日'},
			                {cls:'editDisable',boxLabel: '一', name: 'WEEKS',inputValue:'星期一'},
			                {cls:'editDisable',boxLabel: '二', name: 'WEEKS',inputValue:'星期二'},
			                {cls:'editDisable',boxLabel: '三', name: 'WEEKS',inputValue:'星期三'},
			                {cls:'editDisable',boxLabel: '四', name: 'WEEKS',inputValue:'星期四'},
			                {cls:'editDisable',boxLabel: '五', name: 'WEEKS',inputValue:'星期五'},
			                {cls:'editDisable',boxLabel: '六', name: 'WEEKS',inputValue:'星期六'}
			            ]
		    		},{
		    			hidden:true,
		    			margin:'0 8 0 0',
		    			cls:'editDisable',
		    			xtype:'textarea',
		    			itemId:'days',
		    			name:'pm[DAYS]',
		    			readOnly:true,
		    			emptyText:'点击左边日历，选择多个日期'
		    		},{
		    			flex:1,
		    			margin:'8 8 0 0',
		    			xtype:'pricegrid'
		    		},{
		    			xtype: 'fieldcontainer',
						layout: 'hbox',
						margin:'5 0 0 0',
						items:[{
			    			xtype:'numberfield',
			    			itemId:'seatField',
			    			minValue:0,
			    			value:0,
			    			cls:'editDisable',
			    			width:150,
			    			fieldLabel:'座位数',
			    			allowBlank:true,
			    			name:'pm[SEAT_COUNT]'
			    		},{
			    			flex:1
			    		}]
		    		}]
		    	}]
		    }],
		    buttons:[{
		    	text:'保存',
		    	disabled: true,
		        formBind: true,
		        handler:function(btn){
		        	var form = btn.up('form'),
		        		win = form.up('window');
		        	me.saveRule(form,win,btn);
		        }
		    }]
    	}];
        this.callParent();
	},
	saveRule:function(form,win,btn){
		var me = this,f = form.getForm(),
			params = form.down('pricegrid').hiddenParams,
			rulegrid = form.down('gridpanel[itemId=rulegrid]'),
			seat = form.down('numberfield[itemId=seatField]').getRawValue();
		if(params.length==0){
			util.alert('请填写价格');
			return;
		}
		if(seat==''||seat.indexOf('.')!=-1){
			util.alert('请填写座位，座位必须为整数');
			return;
		}
		btn.setDisabled(true);
		//console.log(params);
		//return;
		if (f.isValid()) {
            f.submit({
            	submitEmptyText:false,
            	url:cfg.getCtx()+'/resource/traffic/price/rule/save?fullPriceFromList='+me.getFullPriceFromList()+'&isFullPrice='+me.getIsFullPrice()+'&trafficId='+me.getTrafficId()+'&trafficPub='+me.getTrafficPub()+'&isSale='+me.getTrafficSale(),
            	params:{prices:Ext.encode(params)},
                success: function(rf, action) {
                   util.success('保存价格成功');
                   f.reset();
                   btn.setDisabled(false);
                   me.showPrevious();
                   form.down('pricegrid').getStore().load();
                   form.down('pricegrid').hiddenParams = [];
                  
                   rulegrid.getStore().load();
                },
                failure: function(form, action) {
                	var state = action.result?action.result.statusCode:0,
                		errors = ['保存异常','数据填写不完整','日期区间已存在', '日期区间已过期'];
                    util.error(errors[0-parseInt(state)]);
                    btn.setDisabled(false);
                }
            });
        }
	},
	showNext: function () {
		var btns = this.down('toolbar').query('button[cls=sel]');
		for(var i=0;i<btns.length;i++){btns[i].hide();}
        this.doCardNavigation(1);
        this.reset();
        this.down('#startdt').setMaxValue(null);
        this.down('#enddt').setMinValue(null);
       /* var dfs = this.query('[cls=editDisable]');
		for(var i=0;i<dfs.length;i++){
			dfs[i].enable();
		}*/
		//this.down('pricegrid').getStore().load();
    },

    showPrevious: function (btn) {
    	var btns = this.down('toolbar').query('button[cls=sel]');
		for(var i=0;i<btns.length;i++){
			if(this.getIsFullPrice()==1){
				if(i==2||i==3){
					continue;
				}
			}
			btns[i].show();
		}
        this.doCardNavigation(-1);
    },
    doCardNavigation: function (incr) {
        var me = this;
        var l = me.getLayout();
        var i = l.activeItem.id.split('cardprice-')[1];
        var next = parseInt(i, 10) + incr;
        l.setActiveItem(next);

        me.down('#card-prev').setDisabled(next===0);
        me.down('#card-next').setDisabled(next===1);
        this.down('pricegrid').hiddenParams = [];
        this.down('pricegrid').getStore().load();
    },
    editPrice:function(){
    	var rg = this.down('gridpanel[itemId=rulegrid]'),
    		priceGrid = this.down('pricegrid'),
			form = this,sel,formData={},t,de;
		if(sel = util.getSingleModel(rg)){
			this.showNext();
			priceGrid.hiddenParams = [];
			priceGrid.getStore().load({params:{ruleId:sel.get('ID')}/*,callback:function(){console.log('loaded');}*/});
			formData = util.pmModel(sel);
			form.getForm().setValues(formData);
			form.down('#startdt').setRawValue(sel.get('BEGIN_DATE'));
        	form.down('#enddt').setRawValue(sel.get('END_DATE'));
			t = parseInt(sel.get('TYPE'));
			de = sel.get('TEXT');
       		switch(t){
       			case 1:
       			case 2:
       				form.down('checkboxgroup[itemId=weeks]').setValue({
       					WEEKS:de.split('-')
       				});
       				break;
       			case 3:
       			case 4:
       				form.down('textarea[itemId=days]').setValue(de);	
       				break;
       		}
       		
       		/*if(sel.get('IS_PUB')=='1'){
       			var dfs = form.query('[cls=editDisable]');
       			for(var i=0;i<dfs.length;i++){
       				dfs[i].disable();
       			}
       		}*/
		}
    },
    delPrice:function(){
		util.delGridModel(this.down('gridpanel[itemId=rulegrid]'),cfg.getCtx()+'/resource/traffic/price/rule/del');
    },pubPrice:function(){
		util.switchGridModel(this.down('gridpanel[itemId=rulegrid]'),cfg.getCtx()+'/resource/traffic/price/rule/pub');
    }
});