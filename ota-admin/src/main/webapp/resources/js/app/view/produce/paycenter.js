
var whatPay,
	earnestInter,
	earnestRetail,
	earnestType,
	startDate,
	lastDate,
	lastDateStr,
	earnestThAmount,
	earnestWmAmount,
	canPayEarnest = false,
	canPayTips='',
	nowDate,
	payLastStr;
//补单 付全款 非定金产品
if(proType==4||route.IS_EARNEST=='0'){
	whatPay = 1;
	canPayTips = '，产品不支持定金支付';
	if(proType==4){
		canPayTips = '，补单不支持定金支付';
	}
}else{
	//定金计算
	earnestInter = util.moneyFormat(route.EARNEST_INTER,'f14 light-blue-color');
	earnestRetail = util.moneyFormat(route.EARNEST_RETAIL,'f14 light-orange-color');
	earnestType = route.EARNEST_TYPE;
	startDate = Ext.Date.parse(order.START_DATE, "Y-m-d");
	lastDate = startDate;
	lastDateStr = '';
	earnestThAmount = util.moneyFormat((order.MAN_COUNT+order.CHILD_COUNT)*route.EARNEST_INTER,'f18');
	earnestWmAmount = util.moneyFormat((order.MAN_COUNT+order.CHILD_COUNT)*route.EARNEST_RETAIL,'f18');
	nowDate = Ext.Date.parse(Ext.Date.format(new Date(),'Y/m/d'), "Y/m/d");
	if(earnestType=='0'){
		if(lastDate){
			lastDate = Ext.Date.add(startDate, Ext.Date.DAY, 0-route.EARNEST_DAY_COUNT);
		}
		canPayEarnest = Ext.Date.diff(nowDate,lastDate, Ext.Date.DAY);
		lastDateStr ='，余额截止支付日期：<span class="f14 blue-color">'+Ext.Date.format(lastDate,'Y/m/d')+'</span>';
	}
	if(earnestType=='1'){
		if(lastDate){
			lastDate = Ext.Date.add(startDate, Ext.Date.DAY, route.EARNEST_DAY_COUNT);
		}
		canPayEarnest = Ext.Date.diff(nowDate,lastDate, Ext.Date.DAY);
		lastDateStr ='，代收款截止确认日期：<span class="f14 blue-color">'+Ext.Date.format(lastDate,'Y/m/d')+'</span>';
	}
	
	
	
	//付定金
	whatPay = 2;
	if(canPayEarnest<0){
		canPayTips='，已过期'+lastDateStr;
	}else{
		canPayTips=lastDateStr;
	}
	
	//付尾款
	if(order.IS_EARNEST=='1'){
		canPayEarnest =false;
		canPayTips='';
		whatPay = 3;
		earnestThAmount = util.moneyFormat(order.INTER_AMOUNT-((order.MAN_COUNT+order.CHILD_COUNT)*route.EARNEST_INTER),'f18');
		payLastStr = ''+(parseFloat(order.OTHER_AMOUNT)>0?'<span style="font-weight:normal;color:#f60;" data-qtip="其他附加费：'+order.OTHER_AMOUNT+'">(含其他附加费)</span>':'');
		payLastStr +='<span class="orange-color" style="padding-left:10px;">'+(discountInfo!=''?discountInfo.replace(/-/g,' &nbsp;&nbsp;'):discountInfo)+'</span>';
	}
}


Ext.define('app.view.produce.paycenter', {
    extend: 'Ext.container.Viewport',
    layout: 'border',
    items: [{
    	height:90,
    	region:'north',
    	style:'border-bottom: 1px solid #d1d1d1;',
    	bodyStyle:'background:#fff',
    	layout:{
			type: 'hbox',
		    pack: 'start',
		    align: 'stretch'
		},
		items:[{
			
			width:180,
			bodyPadding:'10',
			bodyStyle:'background:transparent;',
			html:[
				'<div class="ht20">订单金额：</div>',
				'<div class="ht40">'+util.moneyFormat(order.SALE_AMOUNT,'f24')+'</div>'
			].join('')
		},{
			flex:1,
			autoScroll:false,
			bodyPadding:'8 8 0 5',
			bodyStyle:'background:transparent;',
			tpl:[
				'<h3 class="blue-color f16 ht20">{PRODUCE_NAME}</h3>',
				'<div class="ht20" style="margin-top:10px;"><pre class=" f14">人数：{MAN_COUNT}成人,{CHILD_COUNT}儿童',
				'		出发日期：{START_DATE}',
				'		<tpl if="this.hasMusterPlace(values.MUSTER_PLACE)">集合地点：{MUSTER_PLACE}</tpl>',
				'		<tpl if="this.hasMusterTime(values.MUSTER_TIME)">集合时间：{MUSTER_TIME}</tpl></pre></div>',
				{
					hasMusterPlace:function(m){
						return m;
					},
					hasMusterTime:function(m){
						return m;
					}
				}
			],
			data:order,
			dockedItems: [{
		        xtype: 'toolbar',
		        style:'padding:0px;padding-bottom:5px;background:transparent;',
		        dock: 'bottom',
		        items: ['出团备注：'+(order.REMARK||'无'),'->',{
		        	glyph:'xe62d@iconfont',
		        	text:'修改备注',
		        	handler:function(){
		        		var win = Ext.create('Ext.window.Window',{
        					title:util.windowTitle('&#xe62d;','修改出团备注',''),
        					width:360,
        					height:180,
        					modal:true,
        					bodyPadding:10,
							draggable:false,
							resizable:false,
							maximizable:false,
				   			layout:'fit',
        					items:[{
        						xtype:'form',
        						items:[{
        							xtype:'hiddenfield',
        							name:'pm[ID]',
        							value:order.ID
        						},{
        							anchor:'100%',
        							allowBlank:false,
        							name:'pm[REMARK]',
        							xtype:'textarea',
        							value:order.REMARK,
        							hideLabel:true
        						}]
        					}],
        					buttons:[{
        						text:'保存',
        						handler:function(btn){
        							var f = win.down('form'),
        							form = f.getForm();
        							if (form.isValid()) {
        								btn.disable();
        					            form.submit({
        					            	submitEmptyText:false ,
        					            	url:cfg.getCtx()+'/order/route/save/remark',
        					                success: function(form, action) {
        					                	document.location.reload();
        					                }
        					            });
        					        }
        						}
        					}]
        				}).show();
		        	}
		        },'-',{
		            text: '游客名单',
		            glyph:'xe636@iconfont',
		            handler:function(btn){
		            	var tpl = cfg.getTpls().visitor.single,
							win = Ext.create('Ext.window.Window',{
							title:util.windowTitle('&#xe60b;','游客名单',''),
							width:800,
							height:400,
							modal:true,
							draggable:false,
							resizable:false,
							layout:'fit',
							bodyPadding:'5',
							tbar:[{
								xtype:'panel',
								bodyStyle:'background:transparent;',
								html:'<pre>联系人：'+order.VISITOR_CONCAT+'	电话：'+order.VISITOR_MOBILE+'</pre>'
								
							},'->',{
								iconCls:'icon-print',
								hidden:true,
								itemId:'printBtn',
								text:'打印'
							}],
							items:[{
								autoScroll:true,
								itemId:'detailPanel',
								tpl:tpl
							}]
						}).show();
						win.mask('读取中...');
						Ext.Ajax.request({
							url:cfg.getCtx()+'/order/visitor/listOrderVisitor?orderId='+order.ID,
							success:function(response, opts){
								win.unmask();
								var obj = Ext.decode(response.responseText);
								win.down('panel[itemId=detailPanel]').setData(obj.data);
								var pb = win.down('button[itemId=printBtn]');
								pb.show();
								pb.on('click',function(){
									util.print('打印游客',tpl.apply(obj.data),true,false);
								});
							}
						});
		            }
		        },'-',{
		        	xtype:'button',
        			glyph:'xe67a@iconfont',
        			text:'联系卖家',
        			handler:function(btn){
        				Ext.create('app.view.resource.contactWin',{
        					proId:order.PRODUCE_ID,
        					viewShow:'view'
        				}).show();
        			}
		        }]
		    }]
		}]
    },{
    	region:'center',
    	autoScroll:true,
    	xtype:'form',
    	itemId:'payForm',
    	border:false,
	    layout:{
			type: 'vbox',
		    pack: 'start',
		    align: 'stretch'
		},
		dockedItems:[{
			xtype:'toolbar',
			style:'padding:5px 0 5px 5px;',
			items:[{
		    	xtype:'radiogroup',
		    	height:40,
		    	defaults:{name:'IS_EARNEST'},
		    	listeners:{
		    		afterrender:function(rg){
		    			if(whatPay==2&&canPayEarnest>=0){
		    				rg.setValue({
			    				IS_EARNEST : 1
			                });
		    			}else{
		    				rg.setValue({
			    				IS_EARNEST : 0
			                });
		    			}
		    			
		    		},
		    		change:function(rg, newValue, oldValue, eOpts ){
		    			var bodyP = rg.up('#payForm'),payBtn = bodyP.down('button#payBtn'),href=payBtn.getHref();
		    			if(newValue.IS_EARNEST==0&&whatPay!=3){
		    				bodyP.down('#allPayBody').show();
		    				bodyP.down('#partPayBody').hide();
		    			}else{
		    				bodyP.down('#allPayBody').hide();
		    				bodyP.down('#partPayBody').show();
		    			}
		    			if(href){
		    				var sub = href.split('?')[1],
		    					params = Ext.Object.fromQueryString(sub);
		    				//付尾款
		    				if(order.IS_EARNEST=='1'){
		    					params.IS_EARNEST=-1;
							}else{
							//付定金
								params.IS_EARNEST = newValue.IS_EARNEST;
							}
		    				payBtn.setHref(cfg.getCtx()+'/order/online/pay?'+Ext.Object.toQueryString(params));
		    			}
		    		}
		    	},
		    	items:[{
		    		inputValue:1,
		    		disabled:(!canPayEarnest||canPayEarnest<0),
		    		boxLabel:'预付定金 '+canPayTips
		    	},{
		    		style:'margin-left:20px;',
		    		boxLabel:whatPay==3?'尾款支付':'全款支付',
		    		inputValue:0
		    	}]
			},'->','<span style="background:#DD2C00;color:#fff;padding:5px 8px;"><i class="iconfont">&#xe673;</i> 请及时付款以免座位不足！ </span>']
		}],
	    items:[{
	    	style:'border-top:4px solid #ff6600;',
	    	bodyStyle:'background:#fffbdd;',
	    	bodyPadding:'15 10 15 10',
	    	itemId:'partPayBody',
	    	html:[
	    	      '<h3 class="bold" style="margin-top:0px;">订单编号：'+order.NO+'，'+((order.CON_NO==''||order.CON_NO==null)?'':'合同号：'+order.CON_NO+'，')+'您还需要支付：'+earnestThAmount,
	    	      ' <i class="iconfont orange-color" style="font-size:15px;top:0px;" data-qtip=\'『 最低出团费用同行价/人：'+earnestInter+'，外卖价/人:'+earnestRetail+' 』\'>&#xe671;</i> ',
	    	      payLastStr,
	    	      //lastDateStr,
	    	      '</h3>'
	    	      ].join('')
	    },{	
	    	style:'border-top:4px solid #ff6600;',
	    	bodyStyle:'background:#fffbdd;',
	    	bodyPadding:'15 10 15 10',
	    	itemId:'allPayBody',
	    	html:[
	    	      '<h3 class="bold" style="margin-top:0px;">订单编号：'+order.NO+'，'+((order.CON_NO==''||order.CON_NO==null)?'':'合同号：'+order.CON_NO+'，')+'您还需要支付：'+util.moneyFormat(order.INTER_AMOUNT,'f18'),
	    	      ''+(parseFloat(order.OTHER_AMOUNT)>0?'<span style="font-weight:normal;color:#f60;" data-qtip="其他附加费：'+order.OTHER_AMOUNT+'">(含其他附加费)</span>':''),
	    	      '<span class="orange-color" style="padding-left:10px;">'+(discountInfo!=''?discountInfo.replace(/-/g,' &nbsp;&nbsp;'):discountInfo)+'</span>',
	    	      '</h3>'
	    	      ].join('')
	    },{
	    	flex:1,
	    	margin:1,
	    	plain: true,
	    	border:true,
	    	xtype:'tabpanel',
	    	deferredRender:false,
	    	activeTab:(order.IS_EARNEST=='1'&&order.PAY_TYPE!='0'?1:0),
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
	    		title:'余额支付',
	    		itemId:'balanceTab',
	    		hidden:(order.IS_EARNEST=='1'&&order.PAY_TYPE!='0'),
	    		data:[{
	    			name:util.moneyFormat(yue,'f16'),
	    			value:'account',
	    			checked:(order.IS_EARNEST=='1'&&order.PAY_TYPE=='0'),
	    			color:'#f60'
	    		}]
	    	},{
	    		title:'支付平台',
	    		hidden:(order.IS_EARNEST=='1'&&order.PAY_TYPE=='0'),
	    		data:[{
	    			name:'支付宝',
	    			value:'1',
	    			checked:(order.IS_EARNEST=='1'&&order.PAY_TYPE=='1'),
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
	    		hidden:(order.IS_EARNEST=='1'&&order.PAY_TYPE=='0'),
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
	    	}*/]
	    }],
	    buttons:[{
    		xtype:'panel',
    		width:800,
    		margin:'0 0 0 20',
    		bodyStyle:'background:transparent;',
    		html:[
    			'<ol class="ui-step ui-step-4">',
    				'<li class="ui-step-start ui-step-done">',
				        '<div class="ui-step-line">-</div>',
				        '<div class="ui-step-icon">',
				            '<i class="iconfont">&#xe69f;</i>',
				            '<i class="ui-step-number">1</i>',
				            '<span class="ui-step-text">选择产品</span>',
				        '</div>',
				    '</li>',
				    '<li class="ui-step-done">',
				        '<div class="ui-step-line">-</div>',
				        '<div class="ui-step-icon">',
				            '<i class="iconfont">&#xe69f;</i>',
				            '<i class="ui-step-number">2</i>',
				            '<span class="ui-step-text">填写与核对</span>',
				        '</div>',
				    '</li>',
				    '<li class="ui-step-active">',
				        '<div class="ui-step-line">-</div>',
				        '<div class="ui-step-icon">',
				            '<i class="iconfont">&#xe69f;</i>',
				            '<i class="ui-step-number">3</i>',
				            '<span class="ui-step-text">支付</span>',
				        '</div>',
				    '</li>',
				    '<li class="ui-step-end">',
				        '<div class="ui-step-line">-</div>    ',
				        '<div class="ui-step-icon">',
				            '<i class="iconfont">&#xe69f;</i>',
				            '<i class="iconfont ui-step-number">&#xe6a0;</i>',
				            '<span class="ui-step-text">成功</span>',
				        '</div>',
				    '</li>',
    			'</ol>'
    		].join('')
    	},'->',{
    		itemId:'payBtn',
    		cls:'orange',
    		text:'付款'
    	}]
    }]
});

