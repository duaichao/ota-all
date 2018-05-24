Ext.define('app.controller.finance.alone', {
	extend: 'app.controller.common.BaseController',
	views:['app.view.finance.rechargeGrid','app.view.finance.aloneGrid','Ext.ux.form.field.Month'],
	config: {
		control: {
			'button[itemId=upload]':{
				click:'uploadAttr'
			},
			'button[itemId=export]':{
				click:'exportOrder'
			},
			'grid[itemId=basegrid]':{
				selectionchange: function(view, records) {
	            },
	            cellclick:function( view, td, cellIndex, record, tr, rowIndex, e, eOpts ){
					var rec = record,getUrl='';
					if(cellIndex==1){
						if(e.target.tagName=='A'){
							Ext.factory({
								orderRecord:rec
							},'app.view.order.detail').show();
						}
					}
					if(cellIndex==2){
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
				}
			}
		},
		refs: {
			aloneGrid : 'grid[itemId=basegrid]',
			rechargeGrid :'grid[itemId=rechargegrid]'
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
	        	 	itemId:'status',
	        	 	emptyText:'状态',
					editable:false,
					width:85,
					value:'0',
					typeAhead: true,
		            triggerAction: 'all',
		            store: [
		                ['0','未打款'],
		                ['1','已打款']
		            ],
		            listeners:{
		            	change:function(c,n,o){
		            		var tbar = c.up('toolbar');
		            		if(tbar.down('button#upload')){
		            		if(n=='0'){
		            			tbar.down('button#upload').show();
		            		}else{
		            			tbar.down('button#upload').hide();
		            		}
		            		}
		            	}
		            }
		        });
		        var parentCombo = Ext.create('Ext.form.field.ComboBox',{
				    minChars: 1,
				    itemId:'parentcompanycombo',
				    queryParam: 'query',
				    displayField: 'COMPANY',
				    pageSize:20,
				    hidden:cfg.getUser().companyType!='0',
				    focusOnToFront:false,
				    forceSelection:true,
				    queryMode: 'remote',
				    triggerAction: 'all',
				    valueField: 'ID',
				    emptyText:'选择一个总公司',
				    listConfig:{
				    	minWidth:360,
				    	itemTpl:[
							 '<tpl for=".">',
					            '<li class="city-item">{COMPANY}<span>{BRAND_NAME}</span></li>',
					        '</tpl>'
						]
				    },
				    listeners : {
				     beforequery: function(qe){
				      delete qe.combo.lastQuery;
				     } 
				    },
				    store:Ext.create('Ext.data.Store',{
					    model:Ext.create('app.model.Company'),
					    pageSize:20,
					    autoLoad: false, 
					    proxy: {
					        type: 'ajax',
					        noCache:true,
					        url: cfg.getCtx()+'/site/company/parent/alone',
					        reader: {
					            type: 'json',
					            rootProperty: 'data',
					            totalProperty:'totalSize'
					        }
					   	}
				    })
		        });
		        parentCombo.getStore().on('beforeload',function(store){
		    		var status = parentCombo.previousSibling(),
		    			endTime = status.previousSibling(),
		    			beginTime = endTime.previousSibling();
		    			
		    		store.getProxy().setExtraParams({
		    			beginTime:Ext.Date.format(beginTime.getValue(),'Y-m-d'),
	        			endTime:Ext.Date.format(endTime.getValue(),'Y-m-d'),
	        			status:status.getValue()
	        		});
		    	});
		        ubtn.push(parentCombo);
		        var combo = Ext.create('Ext.form.field.ComboBox',{
				    minChars: 1,
				    itemId:'companycombo',
				    queryParam: 'query',
				    displayField: 'COMPANY',
				    pageSize:20,
				    focusOnToFront:false,
				    forceSelection:true,
				    queryMode: 'remote',
				    triggerAction: 'all',
				    valueField: 'ID',
				    emptyText:'选择一个公司',
				    listConfig:{
				    	minWidth:360,
				    	itemTpl:[
							 '<tpl for=".">',
					            '<li class="city-item">{COMPANY}<span>{BRAND_NAME}</span></li>',
					        '</tpl>'
						]
				    },
				    listeners : {
				     beforequery: function(qe){
				      delete qe.combo.lastQuery;
				     } 
				    },
				    store:Ext.create('Ext.data.Store',{
					    model:Ext.create('app.model.Company'),
					    pageSize:20,
					    autoLoad: false, 
					    proxy: {
					        type: 'ajax',
					        noCache:true,
					        url: cfg.getCtx()+'/site/company/alone',
					        reader: {
					            type: 'json',
					            rootProperty: 'data',
					            totalProperty:'totalSize'
					        }
					   	}
				    })
		        });
		        combo.getStore().on('beforeload',function(store){
		    		var parent = combo.previousSibling(),
		    			status = parent.previousSibling(),
		    			endTime = status.previousSibling(),
		    			beginTime = endTime.previousSibling();
		    			
		    		store.getProxy().setExtraParams({
		    			beginTime:Ext.Date.format(beginTime.getValue(),'Y-m-d'),
	        			endTime:Ext.Date.format(endTime.getValue(),'Y-m-d'),
	        			status:status.getValue(),
	        			parentCompanyId:parent.getValue()
	        		});
		    	});
		        ubtn.push(combo);
		        ubtn.push({
		        	xtype:'textfield',
		        	emptyText:'对账编号',
		        	width:120
		        });
		        ubtn.push({
		        	text:'查询',
		        	handler:function(b){
		        		var parentCompanyId,status,
		        			beginTime,endTime,companyId,dzno;
		        		
		        		dzno = b.previousSibling();
		        		companyId = dzno.previousSibling();
		        		parentCompanyId = companyId.previousSibling();
		        		status = parentCompanyId.previousSibling();
		        		endTime = status.previousSibling();
		        		beginTime = endTime.previousSibling();
		        		
		        		
		        		dzno = dzno.getValue()||'';
		        		status = status.getValue()||'';
		        		beginTime = beginTime.getValue()||'';
		        		endTime = endTime.getValue()||'';
		        		companyId = companyId.getValue()||'';
		        		parentCompanyId = parentCompanyId.getValue()||'';
		        		/*if(companyId==''){
		        			util.alert('必须选择一个公司');
		        			return;
		        		}*/
		        		if(beginTime==''||endTime==''){
		        			util.alert('必须选择一个账期区间');
		        			return;
		        		}
		        		beginTime = Ext.Date.format(beginTime,'Y-m-d');
		        		endTime = Ext.Date.format(endTime,'Y-m-d');
		        		me.getAloneGrid().getStore().getProxy().setExtraParams({
		        			companyPayNo:dzno,
		        			status:status,
		        			companyId:companyId,
		        			parentCompanyId:parentCompanyId,
		        			beginTime:beginTime,
		        			endTime:endTime
		        		});
		        		me.getAloneGrid().getStore().load();
		        		me.getRechargeGrid().getStore().getProxy().setExtraParams({
		        			companyPayNo:dzno,
		        			status:status,
		        			parentCompanyId:parentCompanyId,
		        			companyId:companyId,
		        			beginTime:beginTime,
		        			endTime:endTime
		        		});
		        		me.getRechargeGrid().getStore().load();
		        		
		        		
		        	}
		        });
		        //util.createGridTbar(g,ubtn);
		        var tbar = g.up('panel').getDockedItems('toolbar[dock="top"]')[0];
		        if(tbar.items.length>0){
	        		tbar.removeAll();
	        	}
				tbar.add(ubtn);
		    }
		});
		},500);
	},
	uploadAttr :function(btn){
		var tbar = btn.up('toolbar'),
			status = tbar.down('combo#status'),
			endTime = status.previousSibling(),
			beginTime = endTime.previousSibling(),
       		parentCompanyId= status.nextSibling(),
       		companyId = parentCompanyId.nextSibling(),
       		g = this.getAloneGrid(),
       		rg = this.getRechargeGrid();
       		
       	status = status.getValue()||'';
       	beginTime = beginTime.getValue()||'';
   		endTime = endTime.getValue()||'';
   		companyId = companyId.getValue()||'';
   		parentCompanyId = parentCompanyId.getValue()||'';
   		
   		beginTime = Ext.Date.format(beginTime,'Y-m-d');
		endTime = Ext.Date.format(endTime,'Y-m-d');
		if(status=='0'){
			util.uploadSingleAttachment('上传凭证',function(upload,value) {
				var v = value,
					f = upload.up('form'),
					form = f.getForm(),
					win = f.up('window');
				var fix = /\.xls|.doc|.docx|.trf/i;
	               if (!fix.test(v)) {
	               	util.alert('文件格式不正确，支持.xls|.doc');
	               	return;
	               }else{
	               	win.body.mask('凭证上传中...');
	               	if(form.isValid()){
	               		btn.disable();
	                	form.submit({
	                		submitEmptyText:false ,
							url:cfg.getCtx()+'/finance/pay/attr',
							method:'POST',
							params: {
						        status:status,
			        			companyId:companyId,
			        			beginTime:beginTime,
			        			endTime:endTime,
			        			parentCompanyId:parentCompanyId
						    },
							success:function(res,req){
								var msg = req.result.message||'';
								if(msg!=''){
									util.alert('凭证上传异常！参照内容：'+msg);
								}else{
									util.success('凭证上传成功！');
								}
								win.body.unmask();
								win.close();
								g.getStore().load();
								rg.getStore().load();
							},
							failure:function(res,req){
								var msg = req.result.message||'';
								util.error(msg);
								win.body.unmask();
								win.close();
							}
						});
					}
	             }
			});
		}
	},
	exportOrder:function(btn){
		var tbar = btn.up('toolbar'),
			status = tbar.down('combo#status'),
			endTime = status.previousSibling(),
			beginTime = endTime.previousSibling(),
	   		parentCompanyId= status.nextSibling(),
	   		companyId = parentCompanyId.nextSibling(),
	   		g = this.getAloneGrid(),
	   		rg = this.getRechargeGrid();
	   		
		   	status = status.getValue()||'';
		   	beginTime = beginTime.getValue()||'';
			endTime = endTime.getValue()||'';
			companyId = companyId.getValue()||'';
			parentCompanyId = parentCompanyId.getValue()||'';
			
			if(beginTime==''||endTime==''){
				util.alert('请选择日期区间');
				return;
			}
			beginTime = Ext.Date.format(beginTime,'Y-m-d');
			endTime = Ext.Date.format(endTime,'Y-m-d');
			util.downloadAttachment(cfg.getCtx()+'/finance/alone/order/export?status='+status+'&beginTime='+beginTime+'&endTime='+endTime+'&parentCompanyId='+parentCompanyId+'&companyId='+companyId);
	}
});