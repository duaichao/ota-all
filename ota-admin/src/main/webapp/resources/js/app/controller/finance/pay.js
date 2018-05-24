Ext.define('app.controller.finance.pay', {
	extend: 'app.controller.common.BaseController',
	views:['app.view.finance.payGrid','Ext.ux.form.field.Month','app.view.common.CompanyCombo'],
	config: {
		control: {
			'button[itemId=finish]':{
				click:'finishBill'
			},
			'button[itemId=upload]':{
				click:'uploadAttr'
			},
			'grid[itemId=basegrid]':{
				beforeselect :function(sm, record, index, eOpts ){
					if(record.get('ACCOUNT_COMPANY_ID')!=cfg.getUser().companyId){
						return false;
					}else{
						return true;
					}
				}
			}
		},
		refs: {
			payGrid : 'grid[itemId=basegrid]'
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
		        	ut = cfg.getUser().userType;
		        for(var i=0;i<items.length;i++){
		        	ubtn.push(items[i]);
		        }
		        ubtn.push('->');
		        var bt = beginTime==''?'':Ext.Date.parse(beginTime, "Y-m-d"),
		        	et = endTime==''?'':Ext.Date.parse(endTime, "Y-m-d");
		        ubtn.push({
	    			width:105,
	    			xtype:'datefield',
	    			format:'Y-m-d',
	    			endDateField: 'endxddt',
			        itemId:'startxddt',
			        showToday:false,
			        vtype: 'daterange',
			        value:bt,
			        emptyText:'开始日期'
		        });
		        ubtn.push({
			        emptyText:'截止日期',
	    			width:105,
	    			xtype:'datefield',
	    			format:'Y-m-d',
	    			itemId:'endxddt',
	    			value:et,
	    			showToday:false,
		            startDateField: 'startxddt',
		            vtype: 'daterange'
		        });
		        var combo = Ext.create('Ext.form.field.ComboBox',{
		        	value:supplyId,
				    minChars: 1,
				    itemId:'companycombo',
				    queryParam: 'query',
				    displayField: 'COMPANY',
				    //pageSize:20,
				    focusOnToFront:false,
				    forceSelection:true,
				    queryMode: 'remote',
				    triggerAction: 'all',
				    valueField: 'COMPANY_ID',
				    emptyText:'选择一个供应商',
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
					    //pageSize:20,
					    autoLoad: true, 
					    proxy: {
					        type: 'ajax',
					        noCache:true,
					        url: cfg.getCtx()+'/finance/pay/supply',
					        reader: {
					            type: 'json',
					            rootProperty: 'data',
					            totalProperty:'totalSize'
					        }
					   	}
				    })
		        });
		        combo.getStore().on('beforeload',function(store){
		    		var endTime = combo.previousSibling(),
		    			beginTime = endTime.previousSibling();
		    		store.getProxy().setExtraParams({
	        			beginTime:Ext.Date.format(beginTime.getValue(),'Y-m-d'),
	        			endTime:Ext.Date.format(endTime.getValue(),'Y-m-d'),
	        			companyType:1
	        		});
		    	});
		        ubtn.push(combo);
		        
		        ubtn.push({
		        	text:'查询',
		        	handler:function(b){
		        		var orderType,beginTime,endTime,supplyId;
		        		supplyId = b.previousSibling();
		        		endTime = supplyId.previousSibling();
		        		beginTime = endTime.previousSibling();
		        		
		        		supplyId = supplyId.getValue()||'';
		        		endTime = endTime.getValue()||'';
		        		beginTime = beginTime.getValue()||'';
		        		if(beginTime==''||endTime==''||supplyId==''){
		        			util.alert('必须选择一个供应商、账期');
		        			return;
		        		}
		        		beginTime = Ext.Date.format(beginTime,'Y-m-d');
		        		endTime = Ext.Date.format(endTime,'Y-m-d');
		        		me.getPayGrid().getStore().getProxy().setExtraParams({
		        			beginTime:beginTime,
		        			endTime:endTime,
		        			supplyId:supplyId
		        		});
		        		me.getPayGrid().getStore().load();
		        	}
		        });
		        util.createGridTbar(g,ubtn);
		    }
		});
		},500);
	},
	finishBill :function(btn){
		var tools = btn.up('toolbar'),
			beginTime = tools.down('datefield#startxddt'),
			endTime = beginTime.nextSibling(),
			supplyId = endTime.nextSibling();
			
			var supplyName = supplyId.getRawValue()||'';
			supplyId = supplyId.getValue()||'';
			endTime = endTime.getValue()||'';
			beginTime = beginTime.getValue()||'';
			beginTime = Ext.Date.format(beginTime,'Y-m-d');
    		endTime = Ext.Date.format(endTime,'Y-m-d');
		var g = this.getPayGrid(),sels,models=[];
		if(sels = util.getMultiModel(g)){
			for (var i = 0; i < sels.length; i++) { 
				models.push(sels[i].data);
			}
			var win = Ext.create('Ext.window.Window',{
	   			title: util.windowTitle('','信息提示',''),
	   			width:300,
	   			height:170,
	   			draggable:false,
				resizable:false,
				closable:false,
				modal:true,
	   			bodyStyle:'background:#fff;padding:10px;',
	   			layout:'fit',
	   			items:[{
	   				html:'<h3 class="alert-box"> 是否确定已经线下打款完成？此操作可以多选</h3>'
	   			}],
	   			buttons:[{
	   				text:'确定',
	   				handler:function(){
	   					Ext.Ajax.request({
							url:cfg.getCtx()+'/finance/pay/finish',
							params:{models:Ext.encode(models),beginTime:beginTime,endTime:endTime,supplyId:supplyId},
							success:function(response, opts){
								win.close();
								var obj = Ext.decode(response.responseText),
									errors = ['操作失败',''],
									state = obj?obj.statusCode:0;
								if(!obj.success){
									util.error(errors[0-parseInt(state)]);
								}else{
									g.getStore().load();
								}
							}
						});
	   				}
	   			}]
	   		}).show();
   		}
	},
	uploadAttr :function(btn){
		var me=this,sel,grid=this.getPayGrid();
		if(sel = util.getSingleModel(grid)){
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
							url:cfg.getCtx()+'/finance/pay/upload?detailId='+sel.get('ID'),
							method:'POST',
							success:function(res,req){
								var msg = req.result.message||'';
								if(msg!=''){
									util.alert('凭证上传异常！参照内容：'+msg);
								}else{
									util.success('凭证上传成功！');
								}
								win.body.unmask();
								win.close();
								grid.getStore().load();
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
	}
	
});