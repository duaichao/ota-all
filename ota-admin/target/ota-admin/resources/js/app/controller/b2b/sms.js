Ext.define('app.controller.b2b.sms', {
	extend: 'app.controller.common.BaseController',
	config: {
		control: {
			'gridpanel[itemId=basegrid] searchfield':{
	        	afterrender:'onSearchFieldRender'
	        },
	        'toolbar button[itemId=batchSend]': {
	             click: 'batchSend'
	        },
	        'toolbar button[itemId=recharge]': {
	             click: 'recharge'
	        },
	        'toolbar button[itemId=rechargeLog]': {
	             click: 'rechargeLog'
	        }
		},
		refs: {
            smsgrid: 'gridpanel[itemId=basegrid]'
        }
	},
	onSearchFieldRender:function(s){
		var tbar = s.up('toolbar'),
			types = [
		        {text:'全部',value:0},
		        {text:'系统自动',value:1},
		        {text:'手动群发',value:4}
	        ],store = tbar.up('grid').getStore(),filter;
		tbar.add({
			width:80,
			margin:'0 0 0 5',
			xtype:'combo',
			editable:false,
			forceSelection: true,
			emptyText:'短信类型',
			value:0,
            valueField: 'value',
            displayField: 'text',
            store:util.createComboStore(types),
            minChars: 0,
            queryMode: 'local',
            typeAhead: true,
            listeners:{
            	change:function(c, newValue, oldValue, eOpts ){
			       store.getProxy().setExtraParams({type:newValue});
			       store.load();
            	}
            }
		});
		
		tbar.insert(2,{
			glyph:'xe634@iconfont',
			text:'充值记录',
			itemId:'rechargeLog'
		});
		tbar.insert(0,{
			xtype:'container',
	    	padding:'5 10',
	    	cls:'good',
	    	html:'<div class="green-color"><i class="iconfont f18 ">&#xe712;</i> 剩余条数：<span class="f14 green-color">'+useCount+'</span> 条</div>'
		});
	},
	batchSend:function(btn){
		var me=this,win = Ext.create('Ext.window.Window',{
			title:util.windowTitle(util.glyphToFont(btn.glyph),'群发短信',''),
			width:'60%',
			height:340,
			modal:true,
			draggable:false,
			resizable:false,
			maximizable:false,
   			layout:'fit',
			items:[{
				xtype:'form',
    			scrollable:false,
				fieldDefaults: {
			        labelAlign: 'right',
			        labelWidth: 0,
			        msgTarget: 'side'
			    },
				bodyPadding: 5,
				defaults:{hideLabel:true},
			    items:[{
			    	name:'pm[MOBILES]',
		    		emptyText:'输入手机号码，多个手机号码请用英文逗号(,)隔开',
		    		xtype:'textarea',
		    		anchor:'100%',
		    		height:143,
		    		cls:'text-cls',
			    	allowBlank: false
			    },{
			    	emptyText: '短信内容,最多70个字符',
			    	name: 'pm[CONTENT]',
			    	xtype: 'textarea',
			    	maxLength:70,
			    	height:60,
			    	cls:'text-cls',
			    	anchor:'100%',
			    	allowBlank: false
			    }],
				buttons: [{
			        text: '发送',
			        itemId:'save',
			        disabled: true,
			        formBind: true,
			        handler:function(btn){
			        	var form = btn.up('form'),
			        		win = form.up('window');
			        	me.onSendSave(form,win);
			        }
			    }]
			}]
		}).show();
	},onSendSave:function(form,win){
		var me = this,form = form.getForm(),
		btn = win.down('button#save');
		if (form.isValid()) {
			btn.disable();
            form.submit({
            	submitEmptyText:false ,
            	url:cfg.getCtx()+'/b2b/sms/batchSend',
                success: function(form, action) {
                   util.success('群发短信成功');
                   me.getSmsgrid().getStore().load();
                   win.close();
                },
                failure: function(form, action) {
                	var state = action.result?action.result.statusCode:0,
                		errors = ['发送异常','短信内容存在非法字符','短信条数余额不足','短信条数余额不足', '短信验证码超过次数显示或内容重复，请于联系管理员'];
                    util.error(errors[0-parseInt(state)]);
                    btn.enable();
                }
            });
        }
	},
	recharge:function(btn){
		var pv = cfg.getUser().userType,
			pvStr = '';
		if(pv=='02'){//供应商
			pvStr = '<div class=\'ht20 white\'>附加费用  > 订单审核提醒：旅行社（预计1条）</div>';
		}
		if(pv=='03'){//旅行社
			pvStr = '<div class=\'ht20 white\'>B2B > 订单提醒： 供应商、旅行社、旅行社总公司（预计3条）</div><div class=\'ht20 white\'>B2C > 订单提醒： 供应商、旅行社、游客（预计3条）</div>';
		}
		var me=this,win = Ext.create('Ext.window.Window',{
			title:util.windowTitle(util.glyphToFont(btn.glyph),'短信充值',''),
			width:740,
			height:450,
			modal:true,
			draggable:false,
			resizable:false,
			maximizable:false,
   			layout:'fit',
			items:[{
				xtype:'form',
    			scrollable:false,
				fieldDefaults: {
			        labelAlign: 'right',
			        labelWidth: 90,
			        msgTarget: 'side'
			    },
			    items:[{
			    	xtype:'fieldcontainer',
			    	anchor:'100%',
			    	margin:'10 0 0 0',
			    	layout: 'hbox',
			    	fieldLabel:'选择充值套餐',
			    	items:[{
			    		width:90,
				    	xtype:'combo',
				    	value:100,
						forceSelection: true,
						editable:false,
			            valueField: 'value',
			            displayField: 'text',
			            store:util.createComboStore([{
			            	text:'100条',
			            	value:100
			            },{
			            	text:'200条',
			            	value:200
			            },{
			            	text:'500条',
			            	value:500
			            },{
			            	text:'1000条',
			            	value:1000
			            },{
			            	text:'5000条',
			            	value:5000
			            }]),
			            
			            queryMode: 'local',
			            typeAhead: true,
			            listeners:{
			            	change:function( combo, newValue, oldValue, eOpts ){
								if(newValue){
									var b = combo.nextSibling();
									b.setValue(newValue*0.1);
									var form = combo.up(),
									payBtn = form.up().down('button#payBtn'),
									payMoney = form.down('textfield#payMoney'),
									href = payBtn.getHref(),
									params = Ext.Object.fromQueryString(href.split('?')[1]);
									
									params.money = newValue*0.1;
									
									payBtn.setHref(cfg.getCtx()+'/order/sms/pay?'+Ext.Object.toQueryString(params));
								}
							}
			            }
				    },{
				    	width:50,
				    	readOnly:true,
				    	itemId:'payMoney',
				    	margin:'0 0 0 10',
				    	xtype:'textfield',
				    	value:10
				    },{
				    	flex:1,
				    	margin:'0 0 0 10',
				    	xtype:'container',
				    	html:'<span class="" style="position:relative;top:10px;">元，短信资费0.1元/条</span>'
				    }]
			    },{
			    	plain: true,
			    	xtype:'tabpanel',
			    	deferredRender:false,
			    	activeTab:0,
			    	tabBar:{
	    				defaults:{height:45},
	    				style:'padding:5px 10px 0 10px;'
	    			},
			    	defaults:{
			    		bodyPadding:10,
			    		tpl:[
			    			'<ul class="bank-list">',
			    				'<tpl for=".">',
			    				'<tpl if="checked==true">',
			    				'<li class="bank focus">',
			    					'<input type="radio" value="{value}" name="payment" class="f-radio" checked="true">',
			    				'<tpl else>',
			    				'<li class="bank">',
			    					'<input type="radio" value="{value}" name="payment" class="f-radio">',
			    				'</tpl>',
					                    //'<i class="iconfont f24" style="color:{color}"  >{font}</i>',
					                    '<svg class="icon f24" aria-hidden="true"><use xlink:href="#{font}"></use></svg>',
					                    ' <span class="f14"> {name}</span>',
					                '<i class="iconfont check">&#xe618;</i>',
			    				'</li>',
			    				'</tpl>',
			    			'</ul>'
			    		]
			    	},
			    	items:[{
			    		title:'支付平台',
			    		data:[{
			    			name:'支付宝',
			    			value:'1',
			    			checked:true,
			    			font:'icon-zhifubao'
			    		}/*,{
			    			name:'财付通',
			    			value:'2',
			    			font:'&#xe614;',
			    			color:'#ff7500'
			    		},{
			    			name:'微信支付',
			    			value:'3',
			    			font:'icon-weixinzhifu'
			    		}*/]
			    	},{
			    		title:'网银支付',
			    		data:[{
			    			name:'招商银行',
			    			value:'CMB',
			    			font:'icon-zhaoshangyinhang'
			    		},{
			    			name:'交通银行',
			    			value:'COMM',
			    			font:'icon-jiaotongyinhang'
			    		},{
			    			name:'建设银行',
			    			value:'CCB',
			    			font:'icon-jiansheyinhang'
			    		},{
			    			name:'工商银行',
			    			value:'ICBCB2C',
			    			font:'icon-gongshangyinhang'
			    		},{
			    			name:'农业银行',
			    			value:'ABC',
			    			font:'icon-nongyeyinxing1'
			    		},{
			    			name:'中国银行',
			    			value:'BOC-DEBIT',
			    			font:'icon-zhongguoyinhang'
			    		},{
			    			name:'上海浦发银行',
			    			value:'SPDB',
			    			font:'icon-pufayinhang'
			    		},{
			    			name:'光大银行',
			    			value:'CEB-DEBIT',
			    			font:'icon-guangdayinhang'
			    		},{
			    			name:'民生银行',
			    			value:'CMBC',
			    			font:'icon-minshengyinhang'
			    		},{
			    			name:'广东发展银行',
			    			value:'GDB',
			    			font:'icon-guangfayinhang'
			    		},{
			    			name:'兴业银行',
			    			value:'CIB',
			    			font:'icon-xingyeyinhang'
			    		},{
			    			name:'平安银行',
			    			value:'SPABANK',
			    			font:'icon-pinganyinhang'
			    		},{
			    			name:'北京银行',
			    			value:'BJBANK',
			    			font:'icon-beijingyinhang'
			    		},{
			    			name:'邮政银行',
			    			value:'POSTGC',
			    			font:'icon-youzhengyinhang'
			    		},{
			    			name:'上海银行',
			    			value:'SHBANK',
			    			font:'icon-shanghaiyinhang'
			    		},{
			    			name:'杭州银行',
			    			value:'HZCBB2C',
			    			font:'icon-hangzhouyinhang'
			    		}]
			    	}/*,{
			    		title:'信用卡快捷支付',
			    		disabled:true
			    	}*/],
			    	listeners:{
						afterrender:function(p){
							var lis = p.el.query('li',false),
								len = lis.length,
								me = this,
								form = me.up(),
								payBtn = form.up().down('button#payBtn');
							//默认支付宝 bankCode=1 条数100条10元
							payBtn.setHref(cfg.getCtx()+'/order/sms/pay?bankCode=1&money=10');
							if(len>0){
								for(var i=0;i<len;i++){
									lis[i].on('click',function(){
										for(var j=0;j<len;j++){
											lis[j].down('input').dom.removeAttribute('checked');
											lis[j].removeCls('focus');
										}
										this.down('input').set({checked:true});
										this.addCls('focus');
										
										
										var href = payBtn.getHref(),
											params = Ext.Object.fromQueryString(href.split('?')[1]);
										
										params.bankCode = this.down('input').dom.value;
										
										payBtn.setHref(cfg.getCtx()+'/order/sms/pay?'+Ext.Object.toQueryString(params));
									});
								}
							}
						}
			    	}
			    }],
				buttons: [{
					xtype:'container',
			    	padding:'10 15',
			    	margin:'0 0 10 0',
			    	anchor:'100%',
			    	html:'<span class="orange-color" data-qtip="'+pvStr+'"><i class="iconfont f20">&#xe79d;</i> 由于公司名称字符数不同，单条短信最大字符数=50，超出按多条计算</span>'
				},'->',{
			        text: '付款',
			        cls:'orange',
			        itemId:'payBtn',
			        disabled: true,
			        formBind: true,
			        handler:function(btn){
			        	var win = top.Ext.create('Ext.window.Window',{
							title:util.windowTitle('','支付提醒',''),
							width:400,
				   			height:220,
				   			draggable:false,
							resizable:false,
							closable:false,
							modal:true,
				   			bodyStyle:'background:#fff;padding:10px;',
				   			layout:'fit',
				   			items:[{
				   				html:'<h3 style="font-size:16px;padding:5px 2px;line-height:22px;">请你在新打开的平台支付页面进行支付，支付完成前请不要关闭该窗口！</h3><div style="font-size:14px;padding:5px;line-height:18px;margin-top:5px;">在支付完成前请不要关闭此窗口，否则会影响购买。</div>'
				   			}],
				   			listeners:{
				   				close:function(){
				   					util.redirectTo('b2b/sms');
				   				}
				   			},
				   			buttons:[{
				   				text:'已完成支付',
				   				handler:function(){
				   					win.close();
				   				}
				   			},{
				   				text:'支付遇到问题',
				   				cls:'disable',
				   				handler:function(){
				   					win.close();
				   				}
				   			}]
						}).show();
			        }
			    }]
			}]
		}).show();
	},
	rechargeLog:function(btn){
		var store = util.createGridStore(cfg.getCtx()+'/order/sms/pay/record',Ext.create('app.model.b2b.sms.rechargeModel'));
		var bbar = util.createGridBbar(store);
		var columns = Ext.create('app.model.b2b.sms.rechargeColumn');
		
        
		var dockedItems = [{
        	xtype:'toolbar',
        	style:'padding-left:5px;',
            overflowHandler: 'menu',
        	items:[{
				xtype:'container',
		    	padding:'5 10',
		    	html:'<span class="red-color"><i class="iconfont f20">&#xe6ae;</i> 30分钟未完成支付，系统将自动取消</span>'
			},'->',{
            	xtype:'combo',
    			forceSelection: true,
    			editable:false,
                valueField: 'value',
                displayField: 'text',
                store:util.createComboStore([{
                	text:'创建日期',
                	value:1
                },{
                	text:'付款日期',
                	value:2
                }]),
                width:100,
                value:1,
                queryMode: 'local',
                typeAhead: true
    		},{
    			width:105,
    			editable:false,
    			xtype:'datefield',
    			format:'Y-m-d',
    			endDateField: 'endxddt',
    	        itemId:'startxddt',
    	        showToday:false,
    	        vtype: 'daterange',
    	        emptyText:'开始日期'
            },{
    	        emptyText:'截止日期',
    			width:105,
    			editable:false,
    			xtype:'datefield',
    			format:'Y-m-d',
    			itemId:'endxddt',
    			showToday:false,
                startDateField: 'startxddt',
                vtype: 'daterange'
            },{
            	text:'查询',
            	handler:function(b){
            		var beginTime,endTime,timeType;
            		endTime = b.previousSibling();
            		beginTime = endTime.previousSibling();
            		timeType = beginTime.previousSibling();
            		
            		endTime = endTime.getValue()||'';
            		beginTime = beginTime.getValue()||'';
            		timeType = timeType.getValue()||'';
            		
            		if(beginTime==''||endTime==''){
            			util.alert('必须选择一个日期范围');
            			return;
            		}
            		beginTime = Ext.Date.format(beginTime,'Y-m-d');
            		endTime = Ext.Date.format(endTime,'Y-m-d');
            		store.getProxy().setExtraParams({
	        			beginTime:beginTime,
	        			endTime:endTime,
	        			timeType:timeType
	        		});
            		store.load();
            	}
            }]
        }];
		Ext.create('Ext.window.Window',{
			title:util.windowTitle(util.glyphToFont(btn.glyph),'短信充值记录',''),
			width:800,
			height:400,
			modal:true,
			draggable:false,
			resizable:false,
			maximizable:false,
   			layout:'fit',
   			items:[{
   				xtype:'grid',
   				loadMask: true,
   				emptyText: '没有数据',
   				viewConfig:{
   					stripeRows : true, // 奇偶行不同底色  
   				    enableTextSelection : false
   				},
   				columnLines: true,
   				//selModel :{mode:'SINGLE'},
   				//selType:'checkboxmodel'
   				store:store,
   				bbar:bbar,
   				dockedItems:dockedItems,
   				columns:columns
   			}]
		}).show();
	}
});