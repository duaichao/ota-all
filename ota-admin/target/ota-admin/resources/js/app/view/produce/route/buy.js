/*var nation = [["汉族","汉族"],["蒙古族","蒙古族"],["回族","回族"],["壮族","壮族"],["维吾尔族","维吾尔族"],["藏族","藏族"],["苗族"," 苗族"],["彝族"," 彝族"],["布依族"," 布依族"],["朝鲜族","朝鲜族"],["满族","满族"],["侗族"," 侗族"],["瑶族"," 瑶族"],["白族"," 白族"],["土家族","土家族"],["哈尼族","哈尼族"],["哈萨克族","哈萨克族"],["傣族"," 傣族"],["黎族"," 黎族"],["僳僳族","僳僳族"],["佤族","佤族"],["畲族","畲族"],["拉祜族","拉祜族"],["水族","水族"],["东乡族","东乡族"],["纳西族","纳西族"],["景颇族","景颇族"],["柯尔克孜族","柯尔克孜族"],["土族","土族"],["达斡尔族","达斡尔族"],["仫佬族","仫佬族"],["仡佬族","仡佬族"],["羌族","羌族"],["锡伯族","锡伯族"],["布朗族","布朗族"],["撒拉族","撒拉族"],["毛南族","毛南族"],["阿昌族","阿昌族"],["普米族","普米族"],["塔吉克族","塔吉克族"],["怒族","怒族"],["乌孜别克族","乌孜别克族"],["俄罗斯族","俄罗斯族"],["鄂温克族","鄂温克族"],["德昂族","德昂族"],["保安族","保安族"],["裕固族","裕固族"],["京族","京族"],["基诺族","基诺族"],["高山族","高山族"],["塔塔尔族","塔塔尔族"],["独龙族","独龙族"],["鄂伦春族","鄂伦春族"],["赫哲族","赫哲族"],["门巴族","门巴族"],["珞巴族","珞巴族"]];*/
var totalHtml = [
	'<ul>',
	'<li class="total">',
	'<label class="title">总计<span style="font-size:10px;color:#E64A19;padding-left:2px;">(不含其他)</span></label>',
	'<label class="th">',
	util.moneyFormat(0,'f20 deep-orange-color'),
	'</label>',
	'<label class="wm">',
	util.moneyFormat(0,'f20 deep-orange-color'),
	'</label>',
	'</li>'
];
if(prices&&prices.length>0){
	for(var i=0;i<prices.length;i++){
		var price = prices[i];
		if(price.TYPE_NAME!='同行'){
			totalHtml.push([
				'<li id="'+price.ATTR_ID+'" price="'+price.WAIMAI+'" thprice="'+price.TONGHANG+'">',
				'<label class="title">'+price.ATTR_NAME+'</label>',
				'<label class="th">',
				'<b>0 ×</b>'+util.moneyFormat(price.TONGHANG,'f12 cor666'),
				'</label>',
				'<label class="wm">',
				'<b>0 ×</b>'+util.moneyFormat(price.WAIMAI,'f12 cor666'),
				'</label>',
				'</li>'
			].join(''));
		}
	}
}

//单房差
//if(routeDetail&&(routeDetail.RETAIL_SINGLE_ROOM>0||routeDetail.INTER_SINGLE_ROOM>0)){
	totalHtml.push([
		'<li id="singleRoomLi" price="'+routeDetail.RETAIL_SINGLE_ROOM+'" thprice="'+routeDetail.INTER_SINGLE_ROOM+'">',
		'<label class="title">单房差</label>',
		'<label class="th">',
		'<b>0 ×</b>'+util.moneyFormat(routeDetail.INTER_SINGLE_ROOM,'f12 cor666'),
		'</label>',
		'<label class="wm">',
		'<b>0 ×</b>'+util.moneyFormat(routeDetail.RETAIL_SINGLE_ROOM,'f12 cor666'),
		'</label>',
		'</li>'
	].join(''));
//}
totalHtml.push('</ul>');
Ext.define('app.view.produce.route.buy', {
    extend: 'Ext.panel.Panel',
    layout: 'fit',
    requires:['app.view.resource.route.pub.price','app.view.resource.route.pub.detail'],
    initComponent:function (){
    	var me = this;
    	var isFullPrice = routeDetail.IS_FULL_PRICE||'0',isEarnest = routeDetail.IS_EARNEST||'0',earnestStr = '';
    	var earnestInter = util.moneyFormat(routeDetail.EARNEST_INTER),
			earnestRetail = util.moneyFormat(routeDetail.EARNEST_RETAIL);
    	if(isEarnest=='1'){
    		earnestStr = '<div style="color:#9C27B0;">此产品支持定金预付，最低出团费用同行价/人：'+earnestInter+'，外卖价/人:'+earnestRetail+'</div>';
    	}
    	this.items = [{
	    	autoScroll:true,
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
					    '<li class="ui-step-active">',
					        '<div class="ui-step-line">-</div>',
					        '<div class="ui-step-icon">',
					            '<i class="iconfont">&#xe69f;</i>',
					            '<i class="ui-step-number">2</i>',
					            '<span class="ui-step-text">填写与核对</span>',
					        '</div>',
					    '</li>',
					    '<li class="">',
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
	    		cls:'disable',
	    		itemId:'backBtn',
	    		text:'返回'
	    	},{
	    		itemId:'saveBtn',
	    		disabled: true,
				formBind: true,
				//href:cfg.getCtx()+'/produce/payfinish?orderId=a06c0b3d51744be38ea0ce362385516f',
	    		text:'保存，下一步'
	    	}],
	    	itemId:'subForm',
	    	xtype:'form',
	   		fieldDefaults: {
		        labelAlign: 'right',
		        labelWidth: 60,
		        msgTarget: 'side'
		    },
	    	layout:{
	    		type: 'hbox',
		        pack: 'start',
		        align: 'stretch'
	    	},
	    	items:[{
	    		flex:1,
	    		style:'border: 1px solid #c3ced5; border-bottom:none;',
	    		margin:'5 0 5 5',
	    		layout:{
		    		type: 'vbox',
			        pack: 'start',
			        align: 'stretch'
		    	},
		    	items:[{
		    		xtype:'panel',
		    		height:100,
		    		layout:{
	   					type: 'hbox',
	  					pack: 'start',
	       				align: 'stretch'
	   				},
		    		items:[{
		    			layout:{
		   					type: 'vbox',
		  					pack: 'start',
		       				align: 'stretch'
		   				},
		    			flex:1,
		    			bodyStyle:'padding:0px 10px 0px 10px;',
		    			dockedItems:[{
		    				height:55,
			    			xtype:'toolbar',
			    			items:['<span class="blue-color f16">'+routeDetail.TITLE+'</span>','->',{
			    				//text:'<i class="iconfont blue-color">&#xe635;</i>',
			    				glyph:'xe635@iconfont',
			    				ui:'default-toolbar',
			    				tooltip:'线路详情',
			    				handler:function(){
			    					me.detailWindow(routeDetail.ID);
			    				}
			    			},{
		    					xtype:'button',
		    					ui:'default-toolbar',
		            			glyph:'xe67a@iconfont',
		            			tooltip:'联系卖家',
		            			handler:function(btn){
		            				Ext.create('app.view.resource.contactWin',{
		            					proId:routeDetail.ID,
		            					viewShow:'view'
		            				}).show();
		            			}
		    				}]
			    		}],
		    			items:[{
		    				margin:'4 0 0 0',
				    		xtype:'textfield',
				    		emptyText:'填写出团备注',
				    		name:'pm[REMARK]'
				    	}]
		    		},{
		    			width:240,
		    			bodyStyle:'padding:10px;',
		    			style:'border-left:1px solid #d1d1d1!important;',
		    			bbar:planId!=''?[{
		    				xtype:'hidden',
		    				name:'pm[RQ]',
		    				value:goDate
		    			},{
			    			xtype:'label',
			    			html:'<i class="iconfont disable-color" style="padding-left:10px;">&#xe65f;</i> <span class="f14 blue-color">'+Ext.Date.format(Ext.Date.parse(goDate, "Ymd"), "Y/m/d")+'</span>'
			    		},'->',{
			    			//text:'更换',
			    			tooltip:'更换其他日期',
			    			glyph:'xe62d@iconfont',
			    			handler:function(){
			    				me.routePrice(routeId);
			    			}
			    		}]:[],
			    		items:[planId!=''?{
			    			hideLabel:true,
			    			labelWidth:0,
			    			margin:'0 0 0 8',
			    			xtype:'radio',
			    			checked:true,
			    			name:'pm[PLAN_ID]',
			    			boxLabel:'<span class="f14 blue-color">'+(isFullPrice=='1'?planName.substring(0,planName.lastIndexOf('-')):planName)+'</span>',
			    			inputValue:planId
			    		}:{
			    			fieldLabel:'出团日期',
			    			margin:'5 0 0 55',
			    			width:180,
			    			minValue:new Date(),
			    			allowBlank:false,
			    			xtype:'datefield',
			    			format:'Y-m-d',
			    			name:'pm[RQ]'
			    		}]
		    		}]
		    	},{
		    		flex:1,
		    		border:false,
		    		style:'border-top:1px solid #c1c1c1!important;border-bottom:1px solid #c1c1c1!important;',
		    		xtype:'routevisitorgrid'
		    		//border:true,
		    		//title:'<span class="f16"><i class="iconfont f18">&#xe60b;</i> 游客信息</span>',
		    	}]
	    	},{
	    		style:'border:2px solid #ff6600;',
	    		itemId:'rightPanel',
	    		margin:'5',
	    		width:370,
	    		overflowY:'auto',
	    		bodyStyle:'background-color: #fff;padding:5px;',
	    		/*layout:{
	    			type: 'vbox',
			        pack: 'start',
			        align: 'stretch'
	    		},*/
	    		items:[{
	    			xtype:'hidden',
	    			itemId:'orderIdHiddenField',
	    			name:'pm[ID]',
	    			value:(od?od.ID:'')
	    		},{
	    			xtype:'hidden',
	    			itemId:'saleAmount',
	    			name:'pm[SALE_AMOUNT]',
	    			value:(od?od.SALE_AMOUNT:'')
	    		},{
	    			xtype:'hidden',
	    			itemId:'interAmount',
	    			name:'pm[INTER_AMOUNT]',
	    			value:(od?od.INTER_AMOUNT:'')
	    		},{
	    			xtype:'hidden',
	    			itemId:'manCount',
	    			name:'pm[MAN_COUNT]',
	    			value:(od?od.MAN_COUNT:'')
	    		},{
	    			xtype:'hidden',
	    			itemId:'childCount',
	    			name:'pm[CHILD_COUNT]',
	    			value:(od?od.CHILD_COUNT:'')
	    		},{
	    			xtype:'hidden',
	    			itemId:'wmBasePrice',
	    			name:'WMBP'
	    		},{
	    			xtype:'hidden',
	    			itemId:'thBasePrice',
	    			name:'THBP'
	    		},{
	    			xtype:'hidden',
	    			itemId:'discountHidden',
	    			name:'pm[DISCOUNT_ID]',
	    			value:(od?od.DISCOUNT_ID:'')
	    		},{
	    			xtype:'hidden',
	    			itemId:'discountInfoHidden',
	    			name:'DISCOUNT_INFO'
	    		},{
	    			xtype:'hidden',
	    			itemId:'singleRoomHiddenField',
	    			name:'pm[SINGLE_ROOM_CNT]',
	    			value:0
	    		},{
		    		//flex:1,
		    		itemId:'totalPanel',
		    		cls:'checkout',
		    		bodyStyle:'padding:0px;background:transparent;overflow:auto;overflow-x:hidden;!important',
		    		html:totalHtml.join('')
		    	}]
	    	}]
	    }];
	    this.callParent();
	    //单房差
	    //if(routeDetail&&(routeDetail.RETAIL_SINGLE_ROOM>0||routeDetail.INTER_SINGLE_ROOM>0)){
	    	this.addSingleRoomItem();
	    //}
	    //优惠打折
	    if(discount&&discount.length>0&&planId!=''){
	    	this.addDiscountItem();
	    }
	    //其他费用
	    if(others&&others.length>0){
	    	this.addOtherFeeItem();
	    }
    },
    //单房差
    addSingleRoomItem:function(){
    	var me = this,
    	p=this.down('panel#totalPanel');
		var singleRoomPanel = Ext.create('Ext.toolbar.Toolbar',{
			margin:'5 0 0 0',
			width:'100%',
			style:'border:1px solid #d1d1d1!important;',
			items:['<span class="f14" style="color:#666;">单房差</span>',{
				xtype:'numberfield',
				value:(od?od.SINGLE_ROOM_CNT:0),
				id:'singleRoomField',
				minValue:0,
				editable:false,
				width:60,
				listeners:{
					change:function(nf,newValue,oldValue){
						var el = p.getEl().down('ul'),
							lis = el.query('li',false);
						
						oldValue = oldValue || 0;
						var baseP = p.up('panel#rightPanel'),
							wmzjHidden = baseP.down('hidden#saleAmount'),
							thzjHidden = baseP.down('hidden#interAmount'),
							wmzj = wmzjHidden.getValue()||0,
							thzj = thzjHidden.getValue()||0,
							wmLabel = el.down('li#singleRoomLi').down('label.wm'),
							thLabel = el.down('li#singleRoomLi').down('label.th');
						
						baseP.down('hidden#singleRoomHiddenField').setValue(newValue);
						
						wmzj = parseFloat(wmzj)-parseFloat(oldValue)*parseFloat(routeDetail.RETAIL_SINGLE_ROOM);
						wmzj = wmzj+parseFloat(newValue)*parseFloat(routeDetail.RETAIL_SINGLE_ROOM);
						thzj = parseFloat(thzj)-parseFloat(oldValue)*parseFloat(routeDetail.INTER_SINGLE_ROOM);
						thzj = thzj+parseFloat(newValue)*parseFloat(routeDetail.INTER_SINGLE_ROOM);
						wmzjHidden.setValue(wmzj);
						thzjHidden.setValue(thzj);
						el.down('li.total').down('label.wm').setHtml(util.moneyFormat(wmzj,'f20'));
						el.down('li.total').down('label.th').setHtml(util.moneyFormat(thzj,'f20'));
						wmLabel.setHtml('<b>'+newValue+' ×</b>'+util.moneyFormat(routeDetail.RETAIL_SINGLE_ROOM,'f12 disable-color'));
						thLabel.setHtml('<b>'+newValue+' ×</b>'+util.moneyFormat(routeDetail.INTER_SINGLE_ROOM,'f12 disable-color'));
					}
				}
			},'个','->',{
				ui:'default-toolbar',
				xtype:'button',
				tooltip:'显示/隐藏同行价',
				text:'<i class="iconfont" style="color:#E64A19;font-size:20px;">&#xe6a9;</i>',
				handler:function(eye){
					var el = p.getEl().down('ul'),
						lis = el.query('li',false);
					if(eye.hasCls('icon-eye-off')){
						eye.removeCls('icon-eye-off');
						eye.setText('<i class="iconfont" style="color:#E64A19;font-size:20px;">&#xe6a9;</i>');
						Ext.Array.each(lis, function(obj, index, countriesItSelf) {
						    obj.down('label.th').setStyle({visibility:'hidden'});
						});
					}else{
						eye.addCls('icon-eye-off');
						eye.setText('<i class="iconfont" style="color:#E64A19;font-size:20px;">&#xe6aa;</i>');
						Ext.Array.each(lis, function(obj, index, countriesItSelf) {
						    obj.down('label.th').setStyle({visibility:'visible'});
						});
					}
				}
			}]
		});
		this.down('panel#rightPanel').add(singleRoomPanel);
    },
    //优惠打折
    addDiscountItem:function(){
		var me=this,store = Ext.create('Ext.data.Store',{
		    model:Ext.create('app.model.b2b.discount.model'),
		    autoLoad: true, 
		    proxy: {
		        type: 'ajax',
		        noCache:true,
		        url: cfg.getCtx()+'/b2b/discount/product?proId='+routeId,
		        reader: {
		            type: 'json',
		            rootProperty: 'data',
		            totalProperty:'totalSize'
		        }
		   	}
	    });
		var combo = Ext.create('Ext.form.field.ComboBox',{
		    itemId:'discountCombo',
		    displayField: 'TITLE',
		    focusOnToFront:false,
		    forceSelection:true,
		    queryMode: 'remote',
		    triggerAction: 'all',
		    valueField: 'ID',
		    editable:false,
		    flex:1,
		    emptyText:'选择一个优惠活动',
		    listConfig:{
		    	minWidth:280,
		    	itemTpl:[
					 '<tpl for=".">',
			            '<li class="city-item">{TITLE}</li>',
			        '</tpl>'
				]
		    },
		    listeners : {
		    	afterrender:function(c){
		    		c.getStore().on('load',function(){
	    				c.setValue(od?od.DISCOUNT_ID:'');
    				});
		    	},
			    beforequery: function(qe){
			    	delete qe.combo.lastQuery;
			    },
			    change: function( combo, newValue, oldValue, eOpts ){
			    	me.loadRule(combo);
			    }
		    },
		    store:store
        });
		var discountPanel = Ext.create('Ext.Panel',{
			width:'100%',
			style:'border:1px solid #d1d1d1!important;',
			border:false,
			bodyPadding:7,
			header:{
				html:'<span style="position:absolute;top:2px;left:0px;font-size:14px;color:#666;">优惠活动</span>',
				style:'background:#fff;line-height:30px;',
				height:30
			},
			
			margin:'5 0 0 0',
			//style:'position:absolute;bottom:0px;left:0px;',
			items:[{
				xtype:'panel',
				itemId:'warp',
				layout:'hbox',
				bodyStyle:'background:transparent;',
				items:[combo,{
					ui:'default-toolbar',
					xtype:'button',
					margin:'0 0 0 10',
					text:'<i class="iconfont" style="color:#E64A19;font-size:18px;">&#xe63d;</i>',
					handler:function(b){
						b.previousSibling().reset();
						b.up('#warp').nextSibling().update('');
					}
				}]
			},{
				bodyStyle:'background:transparent;',
				itemId:'taocan'
			}]
		});
		this.down('panel#rightPanel').add(discountPanel);
    },
    //其他费用
    addOtherFeeItem:function(){
    	var me = this;
    	var items=[{
			xtype:'hidden',
			itemId:'otherAmountField',
			name:'OTHER_AMOUNT',
			value:0
		},{
			xtype:'container',
			padding:'10 0 10 10',
			style:'positon:relative;',
			html:[
			      '<span style="font-size:14px;color:#666;">',
			      '其他费用</span>',
			      '<span class="deep-orange-color" style="text-align:right;font-size:20px;display:inline-block;width:200px;position:absolute;top:10px;right:10px;">',
			      '<dfn>￥</dfn>0.00',
			      //((od&&parseFloat(od.OTHER_AMOUNT)>0)?util.format.number(od.OTHER_AMOUNT||0,'0.00'):'0.00')
			      '</span>'].join('')
		}];
		for(var i=0;i<others.length;i++){
			items.push(Ext.create('Ext.toolbar.Toolbar',{
				style:'border-top:1px dotted #d3d3d3!important;',
				padding:'5 0 5 10',
				layout: 'hbox',
				items:[{
					tooltip:others[i].CONTENT,
					ui:'plain',
					style:'background:transparent;border:none;padding:0px;margin:0px;',
					text:'<i class="iconfont" style="color:#E64A19;font-size:13px;top:0px;">&#xe62a;</i> '
				},'<span style="font-size:12px;">'+others[i].TITLE+'</span>','->',{
					xtype:'numberfield',
					name:'OTHER_NUM',
					width:55,
					value:0,
					listeners:{
						change:function(nf,newValue,oldValue){
							var otherPrice = nf.nextSibling().nextSibling().getValue();
							nf.up().down('hidden#itemAmountHidden').setValue(newValue*otherPrice);
							me.calcOtherFee(nf.up('panel#othersPanel'));
							
						}
					}
				},'<span style="font-size:12px;color:#999;margin-left:-7px;">人  </span><span style="font-size:16px;">×</span> <dfn style="color:#999;margin-right:-7px;">￥</dfn>',{
					xtype:'numberfield',
					name:'OTHER_PRICE',
					width:65,
					value:0,
					listeners:{
						change:function(nf,newValue,oldValue){
							var otherNum = nf.previousSibling().previousSibling().getValue();
							nf.up().down('hidden#itemAmountHidden').setValue(newValue*otherNum);
							me.calcOtherFee(nf.up('panel#othersPanel'));
						}
					}
				},{
					xtype:'hidden',
					itemId:'itemAmountHidden',
					value:0
				},{
					xtype:'hidden',
					name:'OTHER_ID',
					value:others[i].ID
				},{
					xtype:'hidden',
					name:'OTHER_TITLE',
					value:others[i].TITLE
				},{
					xtype:'hidden',
					name:'OTHER_CONTENT',
					value:others[i].CONTENT
				}]
			}));
		}
		this.down('panel#rightPanel').add({
			itemId:'othersPanel',
			margin:'5 0 0 0',
			width:'100%',
			style:'border:1px solid #d1d1d1!important;',
			items:items
		});
    },
    loadRule:function(combo){
    	if(!combo.getValue())return;
    	var me = this;
    	this.down('hiddenfield#discountHidden').setValue(combo.getValue());
    	Ext.Ajax.request({
			url:cfg.getCtx()+'/b2b/discount/rule?valid=yes&proId='+routeId+'&discountId='+combo.getValue()+'&isUse=0',
			success:function(response, opts){
				var obj = Ext.decode(response.responseText),
					data = obj.data;
				var cg = combo.up('#warp').nextSibling();
				var html=[],paramStr='',pt = ['','B2B','APP'],
					pay = ['','在线支付','余额支付'],
					unit = ['','元','%','元/人'];
				Ext.Array.each(data,function(d,i){
					var str = [];
					str.push(pt[d.PLATFROM]);
					str.push(pay[d.PAY_WAY]);
					str.push(' 优惠：'+d.MONEY+unit[d.RULE_TYPE]);
					html.push('<div class="ht20 discount" style="color:#E64A19;">'+str.join('')+'</div>');
					if(i>0){paramStr+='-';}
					paramStr+=str.join('');
				});
				cg.update(html.join(''));
				me.down('hiddenfield#discountInfoHidden').setValue(paramStr);
			}
		});
    },
    
    
    
    routePrice :function(routeId){
		var me = this,win;
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
	   				width:377,
					height:377,
					margin:'10 5 10 10',
					style:'border:none;',
	   				cls:'eventdatepicker selector',
	   				xtype:'eventdatepicker',
					disabledDatesText:'无发团日期',
					format:'Ymd',
					showToday:false,
					listeners:{
						monthviewchange:function(a,b,c){
							var nextDay = Ext.Date.format(b,'Y-m')+'-01';
							me.loadDateForSeat(nextDay,routeId,win);
						},
						select :function(p,date,e){
							me.loadPriceForDate(Ext.util.Format.date(date, 'Ymd'),routeId,win);
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
   					text:'确定',
   					handler:function(){
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
   						
   						document.location.href=cfg.getCtx()+'/produce/route/buy?orderId='+orderId+'&routeId='+routeId+'&goDate='+Ext.Date.format(goDate, "Ymd")+'&planId='+planId+'&planName='+planName;
   					}
   				}]
	   		}).show();
	   		var firstDay = Ext.Date.format(Ext.Date.getFirstDateOfMonth(new Date()),'Y-m-d');
			me.loadDateForSeat(firstDay,routeId,win);
		});
	},
	loadPriceForDate :function(date,routeId,win){
		var pp = win.down('panel[itemId=pricePlan]').down('radiogroup'),
			prp = pp.nextSibling(),
			pdp = prp.nextSibling();
		//win.down('label#goDateLabel').update('出团日期：<span class="f14 blue-color bold">'+Ext.Date.format(Ext.Date.parse(date, "Ymd"), "Y/m/d")+'</span>');
		Ext.Ajax.request({
		    url: cfg.getCtx()+'/resource/route/list/plan?routeId='+routeId+'&startDate='+date,
		    success: function(response, opts) {
		    	 var obj = Ext.decode(response.responseText),
		        	data = obj.data;
		        pp.removeAll(true);
		    	if(data.length>0){
					for(var i=0;i<data.length;i++){
						var isFullPrice = routeDetail.IS_FULL_PRICE||'0';
						var title = data[i].PLAN_TITLE;
						if(isFullPrice=='1'){
							title = title.substring(0,title.lastIndexOf('-'));
						}
						pp.add({
							boxLabel: '<span class="f14 bold cktxt">'+title+'</span>',  inputValue: data[i].PLANID,
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
	},
	detailWindow :function(routeId){
		var win = Ext.create('Ext.window.Window',{
			title:util.windowTitle('&#xe635;','线路详情',''),
			bodyPadding:5,
			modal:true,
			draggable:false,
			resizable:false,
			maximizable:false,
   			layout:'fit',
   			items:[{
   				xtype:'routedetail'
   			}]
		});
		win.show().maximize();
		var rd = win.down('routedetail');
		rd.setRouteId(routeId);
	},
	calcOtherFee:function(otherPanel){
		var hids = otherPanel.query('hidden#itemAmountHidden'),amount=0;
		Ext.Array.each(hids,function(o,i){
			amount+=parseFloat(o.getValue());
		});
		otherPanel.down('container').getEl().down('.money-color').setHtml('<dfn>￥</dfn>'+util.format.number(amount||0,'0.00'));
		otherPanel.down('hidden#otherAmountField').setValue(amount);
	}
});

