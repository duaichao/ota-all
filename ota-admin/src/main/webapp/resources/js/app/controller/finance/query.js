Ext.define('app.controller.finance.query', {
	extend: 'app.controller.common.BaseController',
	views:['app.view.finance.queryGrid','Ext.ux.form.field.Month'],
	config: {
		control: {
			'grid[itemId=basegrid]':{
				selectionchange: function(view, records) {
	            },
	            cellclick:function( view, td, cellIndex, record, tr, rowIndex, e, eOpts ){
					var rec = record,getUrl='';
					if(cellIndex==2){
						if(e.target.tagName=='A'){
							Ext.factory({
								orderRecord:rec
							},'app.view.order.detail').show();
						}
					}
					if(cellIndex==3){
						if(e.target.tagName=='A'&&e.target.className.indexOf('title')!=-1){
							if(parseInt(rec.get('PRODUCE_TYPE'))== 2||parseInt(rec.get('PRODUCE_TYPE'))== 3){
								Ext.require('app.view.resource.route.pub.detail',function(){
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
									rd.setRouteId(rec.get('PRODUCE_ID'));
								});
							}
						}
					}
					if(cellIndex==5){
						if(e.target.tagName=='I'&&e.target.className.indexOf('iconfont')!=-1){
							Ext.factory({
								orderRecord:rec
							},'app.view.order.recharge').show();
						}
					}
					if(cellIndex==11){
						if(e.target.tagName=='I'&&e.target.className.indexOf('iconfont')!=-1){
							Ext.factory({
								orderRecord:rec
							},'app.view.order.status').show();
	                	}
					}
				}
			}
		},
		refs: {
			queryGrid : 'grid[itemId=basegrid]'
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
		        	ubtn = [];
		        for(var i=0;i<items.length;i++){
		        	ubtn.push(items[i]);
		        }
		        ubtn.push('->');
		        
		        
		        ubtn.push({
					xtype:'segmentedbutton',
					fieldLabel:'分组',
					listeners:{
						toggle:function(sb, button, isPressed, eOpts){
							var s = sb.getValue(),
								st = g.getStore(),
								groupCity = false;
							if(s==0){
								st.setGroupField('SHOW_GROUP');
							}
							if(s==1){
								st.setGroupField('ATTR_URL');
							}
							if(s==2){
								st.setGroupField('GROUP_PRODUCE_TITLE');
							}
							if(s==3){
								st.setGroupField('GROUP_END_CITY');
								groupCity = true;
							}
							if(groupCity){
								st.load({
									params:{
										groupCity:1
									}
								});
							}else{
								st.load();
							}
						}
					},
					items: [{
                        text: '按公司',
                        pressed: true
                    }, {
                        text: '按付款凭证'
                    },{
                        text: '按产品'
                    },{
                        text: '按目的地'
                    }]
		        });
		        ubtn.push({
	    			width:105,
	    			xtype:'datefield',
	    			format:'Y-m-d',
	    			endDateField: 'endxddt',
			        itemId:'startxddt',
			        showToday:false,
			        vtype: 'daterange',
			        emptyText:'开始日期'
		        });
		        ubtn.push({
			        emptyText:'截止日期',
	    			width:105,
	    			xtype:'datefield',
	    			format:'Y-m-d',
	    			itemId:'endxddt',
	    			showToday:false,
		            startDateField: 'startxddt',
		            vtype: 'daterange'
		        });
		        ubtn.push({
	        	 	xtype:'combo',
	        	 	emptyText:'付款方',
					editable:false,
					width:75,
					typeAhead: true,
		            triggerAction: 'all',
		            store: [
		            	['','全部'],
		                ['0','平台'],
		                ['1','旅行社']
		            ]
		        });
		        ubtn.push({
	        	 	xtype:'combo',
	        	 	emptyText:'对账状态',
					editable:false,
					width:85,
					typeAhead: true,
		            triggerAction: 'all',
		            store: [
		            	['','全部'],
		                ['0','待对账'],
		                ['-1','非对账'],
		                ['1','已对账'],
		                ['2','已打款']
		            ]
		        });ubtn.push({
	        	 	xtype:'textfield',
	        	 	emptyText:'订单编号',
					width:120
		        });
		        ubtn.push({
		        	text:'查询',
		        	handler:function(b){
		        		var orderNo,
		        			calcStatus,
		        			payWho,
		        			beginTime,endTime;
		        		orderNo = b.previousSibling();
		        		calcStatus = orderNo.previousSibling();
		        		payWho = calcStatus.previousSibling();
		        		endTime = payWho.previousSibling();
		        		beginTime = endTime.previousSibling();
		        		
		        		
		        		
		        		orderNo = orderNo.getValue()||'';
		        		calcStatus = calcStatus.getValue()||'';
		        		payWho = payWho.getValue()||'';
		        		endTime = endTime.getValue()||'';
		        		beginTime = beginTime.getValue()||'';
		        		
		        		if(beginTime==''||endTime==''){
		        			util.alert('必须选择一个账期区间');
		        			return;
		        		}
		        		beginTime = Ext.Date.format(beginTime,'Y-m-d');
		        		endTime = Ext.Date.format(endTime,'Y-m-d');
		        		me.getQueryGrid().getStore().getProxy().setExtraParams({
		        			orderNo:orderNo,
		        			calcStatus:calcStatus,
		        			payWho:payWho,
		        			beginTime:beginTime,
		        			endTime:endTime
		        		});
		        		me.getQueryGrid().getStore().load();
		        		
		        		
		        		
		        	}
		        });
		        ubtn.push({
		        	text:'导出订单',
		        	handler:function(b){
		        		var orderNo,
		        			calcStatus,
		        			payWho,
		        			beginTime,endTime;
		        		orderNo = b.previousSibling().previousSibling();
		        		calcStatus = orderNo.previousSibling();
		        		payWho = calcStatus.previousSibling();
		        		endTime = payWho.previousSibling();
		        		beginTime = endTime.previousSibling();
		        		
		        		
		        		
		        		orderNo = orderNo.getValue()||'';
		        		calcStatus = calcStatus.getValue()||'';
		        		payWho = payWho.getValue()||'';
		        		endTime = endTime.getValue()||'';
		        		beginTime = beginTime.getValue()||'';
		        		
		        		if(beginTime==''||endTime==''){
		        			util.alert('必须选择一个账期区间');
		        			return;
		        		}
		        		beginTime = Ext.Date.format(beginTime,'Y-m-d');
		        		endTime = Ext.Date.format(endTime,'Y-m-d');
		        		util.downloadAttachment(cfg.getCtx()+'/finance/calc/supply/order/export?orderNo='+orderNo+'&calcStatus='+calcStatus+'&payWho='+payWho+'&beginTime='+beginTime+'&endTime='+endTime);
		        	}
		        });
		        util.createGridTbar(g,ubtn);
		    }
		});
		},500);
	}
});