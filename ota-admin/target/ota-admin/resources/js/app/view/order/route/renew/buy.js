Ext.define('app.view.order.route.renew.buy', {
    extend: 'Ext.panel.Panel',
    layout: 'fit',
    requires:['app.view.resource.route.pub.detail','app.view.resource.route.pub.price'],
    initComponent:function (){
    	var me = this;
    	this.items = [{
	    	autoScroll:true,
	    	border:false,
	    	buttons:[{
	    		xtype:'panel',
	    		width:800,
	    		margin:'0 0 0 20',
	    		bodyStyle:'background:transparent;',
	    		html:[
	    			'<ol class="ui-step ui-step-3">',
	    				'<li class="ui-step-start ui-step-done">',
					        '<div class="ui-step-line">-</div>',
					        '<div class="ui-step-icon">',
					            '<i class="iconfont">&#xe69f;</i>',
					            '<i class="ui-step-number">1</i>',
					            '<span class="ui-step-text">选择产品/日期</span>',
					        '</div>',
					    '</li>',
					    '<li class="ui-step-active ui-step-done">',
					        '<div class="ui-step-line">-</div>',
					        '<div class="ui-step-icon">',
					            '<i class="iconfont">&#xe69f;</i>',
					            '<i class="ui-step-number">2</i>',
					            '<span class="ui-step-text">填写游客/价格</span>',
					        '</div>',
					    '</li>',
					    '<li class="ui-step-active ui-step-end">',
				        '<div class="ui-step-line">-</div>    ',
				        '<div class="ui-step-icon">',
				            '<i class="iconfont">&#xe69f;</i>',
				            '<i class="iconfont ui-step-number">&#xe6a0;</i>',
				            '<span class="ui-step-text">保存</span>',
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
	    		text:'保存'
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
	    		border:false,
	    		layout:{
		    		type: 'vbox',
			        pack: 'start',
			        align: 'stretch'
		    	},
		    	items:[{
		    		xtype:'panel',
		    		height:84,
		    		layout:{
	   					type: 'hbox',
	  					pack: 'start',
	       				align: 'stretch'
	   				},
		    		items:[{
		    			layout:'fit',
		    			flex:1,
		    			bodyStyle:'padding:5px 10px 10px 10px;',
		    			dockedItems:[{
			    			xtype:'toolbar',
			    			items:['<span class="blue-color f16">'+routeDetail.TITLE+'</span>','->',{
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
		    				//style:'border-top:2px solid #427fed;',
		    				items:[]
		    			}]
		    		},{
		    			width:180,
		    			style:'border-left:1px solid #c1c1c1!important;',
		    			bodyStyle:'padding:10px;',
		    			items:[{
		    				fieldLabel:'选择出团日期',
		    				width:150,
		    				showToday:false,
		    				name:'pm[RQ]',
		    				allowBlank:false,
		    				value:(od?od.START_DATE:''),
		    				minValue:Ext.Date.add(new Date(), Ext.Date.DAY, parseInt(cfg.getUser().oldDelayDay)),
		    				maxValue:new Date(),
		    				format:'Y-m-d',
		    				labelStyle: 'font-weight:bold',
		    				labelAlign: 'top',
		    				xtype:'datefield'
		    			}]
		    		}]
		    	},{
		    		flex:1,
		    		border:false,
		    		style:'border-top:1px solid #c1c1c1!important;border-bottom:1px solid #c1c1c1!important;',
		    		xtype:'renewvisitorgrid'
		    		//border:true,
		    		//title:'<span class="f16"><i class="iconfont f18">&#xe60b;</i> 游客信息</span>',
		    	}]
	    	},{
	    		style:'border:2px solid #ff7001!important;',
	    		itemId:'rightPanel',
	    		width:370,
	    		overflowY:'auto',
	    		bodyStyle:'background-color: #FFF9C4;padding:5px;',
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
	    		}]
	    	}]
    	}];
    	this.callParent();
    	//添加总计
	    //this.addTotalHtml();
	    //价格
    	var priceGrid = Ext.create('app.view.resource.route.pub.price',{
			//routeId:record.get('ID'),
	    	myLoadUrl:(od?cfg.getCtx()+'/order/route/renew/price/type?orderId='+od.ID:'')
    	});
	    this.down('panel#rightPanel').add(priceGrid);
	    
    	//单房差
	    this.addSingleRoomItem();
	    //其他费用
	    if(others&&others.length>0){
	    	this.addOtherFeeItem();
	    }
	    //默认打开选产品
	    if(!hasRoute){
	    	this.selectProduct();
	    }
    },
    selectProduct:function(btn){
    	var store = util.createGridStore(cfg.getCtx()+'/order/route/renew/routes',Ext.create('app.model.resource.route.model'));
    	var bbar = util.createGridBbar(store);
    	var grid = Ext.create('Ext.grid.Panel',{
    		bbar:bbar,
    		columns:[{
            	text: '线路名称',
            	dataIndex:'TITLE',
            	flex:1,
    	        renderer: function(v,metaData,r,ri,c,store,view){
    	        	setTimeout(function() {
    	                var row = view.getRow(r);
    	                if(row){
    	                //Capturing the el (I need the div to do the trick)
    	                var el = Ext.fly(Ext.fly(row).query('.x-grid-cell')[c]).down('div');
    	                var cellWidth = el.getWidth();
    	                var wraps = el.query('.d-wrap',false);
    	                Ext.each(wraps,function(w,i){
    	                	w.setWidth(cellWidth-20);
    	                });
    	                }
    	                //All other codes to build the progress bar
    	            }, 50);
    	        	var h = [];
    	        	h.push('<a class="f16 title d-wrap" href="javascript:;" style="display:inline-block;line-height:24px;padding:5px 0 10px 0;">');
            		h.push(v);
            		h.push('</a>');
            		h.push('<div class="ht16" style="color:#999;">编号: <span>'+r.get('NO')+'</span></div>');
            		h.push('<div class="d-wrap product-tag">');
            		
            		if(r.get('TYPE')){
            			var routArrs = ['','周边','国内','出境'];
            			h.push('<span class="flash">'+routArrs[r.get('TYPE')]+'游</span>');
            		}
            		if(r.get('DAY_COUNT')){
            			h.push('<span class="flash flash-gray">'+r.get('DAY_COUNT')+'日游</span>');
            		}
            		h.push('</div>');
            		
            		h.push('<div style="padding:10px;line-height:20px;">');
            		//h.push('<span class="d-wrap-block-title" style="background:#9E9E9E;">产品推荐</span>');
            		h.push('<span>'+r.get('FEATURE')+'</span>');
        			h.push('</div>');
            		return h.join('');
    	        }
            },{
            	width:150,
            	text:'主题属性',
            	dataIndex:'THEMES',
            	renderer:function(v,c,r){
            		var arr = ['','周边','国内','出境'],
		        		cls = ['d-tag-teal','d-tag-green','d-tag-blue','d-tag-red'],vstr='',dc = r.get('DAY_COUNT') || '',attr = r.get('ATTR') || '';
	        		v = v || '';
	        		if(v!=''){
	        		var vs = v.split(',');
	        		Ext.each(vs,function(s,i){
	        			if(i%2==0){
	        				vstr+='<p style="line-height:25px;">';
	        			}
	        			vstr+='<span class="d-tag-radius">'+s+'</span>';
	        			if(i%2!=0){
	        				vstr+='</p>';
	        			}
	        		});
	        		}
	        		var h=[
	        		      '<div class="ht30">' /*<a class="d-tag '+cls[r.get('TYPE')]+' d-tag-noarrow">'+arr[r.get('TYPE')]+'游</a>'*/
	        		];
	        		dc = dc==''?'':dc+'日';
			        if(attr!=''){
			        	h.push('<span class="flash" style="background:'+cfg.getRouteAttrTagColor()[attr]+';padding:2px 5px;color:#fff;">'+attr+'</span>');
			        }
			       /* if(dc){
			        	h.push('<span style="background:#9e9e9e; color:#fff;margin-left:5px;padding:2px 4px;border-radius: 2px;">'+dc+'</span>');
			        }*/
			        h.push('</div>');
			        h.push('<div style="margin-top:5px;">'+vstr+'</div>');
		        	return h.join('');
    	        }
            },{
            	width:150,
            	text:'出发/目的地',
            	dataIndex: 'BEGIN_CITY',
            	renderer:function(v,c,r){
            		var end = r.get('END_CITY') ||'',startStr='',endStr='';
            		v = v || '';
            		if(v!=''){
                		var vs = v.split(',');
                		Ext.each(vs,function(s,i){
                			startStr+='<span class="d-tag-radius"  style="float:left;margin-bottom:2px;">'+s+'</span>';
                		});
                	}
            		if(end!=''){
                		var vs = end.split(',');
                		Ext.each(vs,function(s,i){
                			endStr+='<span class="d-tag-radius" style="float:left;margin-bottom:2px;">'+s+'</span>';
                		});
                	}
    	        	return [
    	        		'<div class="ht20">'+startStr+'</div>',
    	        		'<div style="clear:both;line-height:22px;padding-left:15px;"><i class="iconfont" style="color:#ccc;">&#xe64a;</i></div>',
    	        		'<div>'+endStr+'</div>'
    	        	].join('');
    	        }
            },{
            	xtype: 'widgetcolumn',
            	width:100,
            	text:'',
            	sortable: false,
                menuDisabled: true,
            	widget: {
            		xtype:'container',
            		layout:'column',
            		items:[{
            			text:'选择',
    	                cls:cfg.getUser().departId==''?'disable':'red',
    	                disabled:(cfg.getUser().departId==''),
    	                width:83,
    	                xtype: 'button',
    	                handler: function(b) {
    	                	var rec = b.up('container').getWidgetRecord();
    	                	document.location.href=cfg.getCtx()+'/order/route/renew/buy?orderId=&routeId='+rec.get('ID');
    	                }
            		},{
            			xtype:'button',
            			margin:'3 0 0 0',
            			ui:'default-toolbar',
            			glyph:'xe67a@iconfont',
            			text:'联系卖家',
            			handler:function(btn){
            				var rec = btn.up('container').getWidgetRecord();
            				Ext.create('app.view.resource.contactWin',{
            					proId:rec.get('ID'),
            					viewShow:'view'
            				}).show();
            			}
            		}]
                }
            }],
    		dockedItems: [{
        		xtype:'toolbar',
            	items:['->',{
            		emptyText:'产品关键字搜索',
                	xtype:'searchfield',
                	store:store,
                	width:240
        		}]
        	}],
        	store:store,
    		loadMask: true,
    		columnLines: true,
    		selType:'rowmodel'
    	});
    	var win = Ext.create('Ext.window.Window',{
   			title: util.windowTitle('&#xe630;','选择补单产品',''),
   			width:'85%',
   			height:'85%',
   			draggable:false,
			resizable:false,
			closable:false,
			modal:true,
   			bodyStyle:'background:#fff;',
   			layout:'fit',
   			items:[grid]
   		}).show();
    },
    //单房差
    addSingleRoomItem:function(){
    	var me = this,
    	p=this.down('panel#totalPanel');
    	
    	var items=[{
			xtype:'container',
			padding:'5 0 5 10',
			style:'positon:relative;',
			html:[
			      '<span style="font-size:14px;color:#666;"> 单房差</span>'
			      //'<span class="money-color" style="text-align:right;font-size:20px;display:inline-block;width:100px;position:absolute;top:5px;right:30px;">',
			      //'<dfn>￥</dfn>0.00',
			      //'</span>'
			      ].join('')
		}];
		items.push(Ext.create('Ext.toolbar.Toolbar',{
			items:[{
				xtype:'numberfield',
				value:(od?od.SINGLE_ROOM_CNT:0),
				id:'singleRoomField',
				minValue:0,
				name:'pm[SINGLE_ROOM_CNT]',
				editable:false,
				width:60,
				listeners:{
					change:function(nf,newValue,oldValue){
					}
				}
			},'个，同行/外卖价：'+util.moneyFormat(routeDetail.INTER_SINGLE_ROOM,'f14')+'/'+util.moneyFormat(routeDetail.RETAIL_SINGLE_ROOM,'f14')]
		}));
		this.down('panel#rightPanel').add({
			itemId:'singleRoomPanel',
			margin:'5 0 0 0',
			width:'100%',
			style:'border:1px solid #d1d1d1!important;',
			items:items
		});
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
			padding:'5 0 5 10',
			style:'positon:relative;',
			html:[
			      '<span style="font-size:14px;color:#666;">',
			      '其他费用</span>'
			      //'<span class="money-color" style="text-align:right;font-size:20px;display:inline-block;width:100px;position:absolute;top:5px;right:30px;">',
			      //'<dfn>￥</dfn>0.00',
			      ////((od&&parseFloat(od.OTHER_AMOUNT)>0)?util.format.number(od.OTHER_AMOUNT||0,'0.00'):'0.00')
			      //'</span>'
			      ].join('')
		}];
		for(var i=0;i<others.length;i++){
			items.push(Ext.create('Ext.toolbar.Toolbar',{
				layout: 'hbox',
				items:[{
					tooltip:others[i].CONTENT,
					ui:'plain',
					style:'background:transparent;border:none;padding:0px;margin:0px;',
					text:'<i class="iconfont" style="color:#E64A19;font-size:13px;top:0px;">&#xe62a;</i> '
				},'<span style="font-size:12px;">'+others[i].TITLE+'</span>',{
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
				},'<span style="font-size:12px;color:#999;margin-left:-7px;">人  </span><span style="font-size:16px;">×</span> <dfn style="color:#999;margin-right:-7px;;">￥</dfn>',{
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
    calcOtherFee:function(otherPanel){
		var hids = otherPanel.query('hidden#itemAmountHidden'),amount=0;
		Ext.Array.each(hids,function(o,i){
			amount+=parseFloat(o.getValue());
		});
		otherPanel.down('container').getEl().down('.money-color').setHtml('<dfn>￥</dfn>'+util.format.number(amount||0,'0.00'));
		otherPanel.down('hidden#otherAmountField').setValue(amount);
	},
    addTotalHtml:function(){
    	this.down('panel#rightPanel').add({
			itemId:'totalPanel',
			width:370,
			cls:'checkout',
			bodyStyle:'padding:10px;background:transparent;overflow:auto;overflow-x:hidden;!important',
			html:[
			  	'<ul>',
				'<li class="total">',
				'<label class="title">总计 <span style="font-size:12px;color:#f80;">(不含其他)</span></label>',
				'<label class="th">',
				util.moneyFormat(0,'f20'),
				'</label>',
				'<label class="wm">',
				util.moneyFormat(0,'f20'),
				'</label>',
				'</li>',
			].join('')
		});
    }
});