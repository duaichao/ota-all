Ext.define('app.view.order.route.renew.visitorGrid', {
	extend:'Ext.grid.Panel',
	xtype:'renewvisitorgrid',
	loadMask: true,
	border:true,
	emptyText: '请添加游客信息',
	initComponent: function() {
		var me = this;
		this.store = Ext.create('Ext.data.Store', {
	        model: Ext.create('app.model.order.visitor.model')
	    });
        this.columns = [
        	Ext.create('Ext.grid.RowNumberer',{width:35}),{
        		header: '姓名',
	            dataIndex: 'NAME',
	            width:120,
	            editor: {
	                allowBlank: false
	            }
        	},{
	            header: '性别',
	            dataIndex: 'SEX',
	            width: 70,
	            renderer : function(value) {
	            	if(value=='1'){
	            		return '男';
	            	}else if(value=='2'){
	            		return '女';
	            	}else{
	            		return '';
	            	}
	            },
	            editor: new Ext.form.field.ComboBox({
                    typeAhead: true,
                    editable:false,
                    triggerAction: 'all',
                    store: [
                        ['1','男'],
                        ['2','女']
                    ]
                })
        	},{
        		header: '游客类型',
	            dataIndex: 'ATTR_NAME',
	            width: 160,
	            editor: new Ext.form.field.ComboBox({
                    typeAhead: true,
                    allowBlank: false,
                    editable:false,
                    triggerAction: 'all',
                    store: [
                        ['成人','成人'],
                        /*['不占床位/不含门票','不占床位/不含门票'],
                        ['不占床位/含门票','不占床位/含门票'],
                        ['占床位/不含门票','占床位/不含门票'],
                        ['占床位/含门票','占床位/含门票'],
                        ['婴儿','婴儿']*/
                        ['儿童','儿童']
                    ]
                })
        	},{
        		header: '手机',
	            dataIndex: 'MOBILE',
	            width:150,
	            editor: {
	                
	            }
        	}/*,{
        		header: '邮箱',
	            dataIndex: 'EMAIL',
	            width: 180,
	            editor: {
	                vtype: 'email'
	            }
        	}*/,{
        		header: '证件类型',
	            dataIndex: 'CARD_TYPE',
	            width: 120,
	            editor: new Ext.form.field.ComboBox({
                    typeAhead: true,
                    allowBlank: false,
                    editable:false,
                    triggerAction: 'all',
                    store: [
                        ['身份证','身份证'],
                        ['驾驶证','驾驶证'],
                        ['护照','护照'],
                        ['军官证','军官证']
                    ]
                })
        	},{
        		header: '证件号码',
	            dataIndex: 'CARD_NO',
	            flex:1,
	            editor: {
	            	allowBlank: false
	            }
        	},{
        		header:'联系人',
        		xtype: 'radiocolumn',
                dataIndex: 'CONTACT',
                width: 60,
                align:'center',
                sortable: false,
	            menuDisabled: true
        	}
        
        ];
        Ext.grid.RowEditor.prototype.saveBtnText = "保存";
        Ext.grid.RowEditor.prototype.cancelBtnText = '取消';
        var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
	        clicksToMoveEditor: 1,
	        clicksToEdit:2,
	        pluginId:'rowedit',
	        errorSummary:false,
	        autoCancel: false,
	        listeners:{
	        	canceledit:function( editor, context, eOpts){
	        		var r = context.record;
	        		if(r.get('NAME')==''||r.get('MOBILE')==''||r.get('CARD_NO')==''){
	        			var sm = me.getSelectionModel();
		                rowEditing.cancelEdit();
		                //me.store.remove(sm.getSelection());
		                if (me.store.getCount() > 0) {
		                    sm.select(0);
		                }
	        		}else{
	        			me.calcPrice();
	        		}
	        	},
	        	edit:function( editor, context, eOpts ){
	        		context.record.commit();
	        		me.calcPrice();
	        	}
	        }
	    });
        this.dockedItems = [{
        	docked:'top',
    		xtype:'toolbar',
    		itemId:'gridtool',
    		defaults:{scale: 'medium'},
        	items:[{
        		tooltip:'添加游客',
        		glyph:'xe699@iconfont',
        		handler:function(btn){
        			rowEditing.cancelEdit();
        			var win = Ext.create('Ext.Window',{
    					title:util.windowTitle('','请输入人数',''),
    					width:240,
    					height:130,
    					modal:true,
    					draggable:false,
    					resizable:false,
    					maximizable:false,
    		   			layout:'form',
    		   			items:[{
    		   				xtype:'numberfield',
    		   				minValue:1,
    		   				value:1
    		   			}],
    		   			buttons:[{
    		   				text:'确定',
    		   				handler:function(){
    		   					var nb = win.down('numberfield').getValue();
    		   					rowEditing.cancelEdit();
    		   					for(var i=0;i<nb;i++){
    		   						var r = Ext.create('app.model.order.visitor.model', {
    		   		                    ID: '',
    		   		                    NAME: '',
    		   		                    MOBILE: '',
    		   		                    SEX: '1',
    		   		                    CARD_TYPE: '身份证',
    		   		                    CARD_NO: '',
    		   		                    EMAIL: '',
    		   		                    ATTR_NAME:'成人',
    		   		                    CONTACT:0
    		   		                });
    		   		
    		   		                me.store.add(r);
    		   					}
    		   					//rowEditing.startEdit(0, 0);
    		   					me.calcPrice();
    		   					win.close();
    		   				}
    		   			}]
        			}).show();
	                
        		}
        	},'-',{
        		tooltip:'删除游客',
        		glyph:'xe698@iconfont',
        		handler:function(){
        			var sm = me.getSelectionModel();
	                rowEditing.cancelEdit();
	                me.store.remove(sm.getSelection());
	                if (me.store.getCount() > 0) {
	                    sm.select(0);
	                }
	                me.calcPrice();
        		}
        	},'-',{
        		tooltip:'批量导入游客',
        		glyph:'xe69a@iconfont',
        		handler:function(){
        			var sm = me.getSelectionModel(),
        				sel = sm.getSelection();
	                rowEditing.cancelEdit();
	                //me.store.remove(sel);
	                if (me.store.getCount() > 0) {
	                    sm.select(0);
	                }
        		
        			util.uploadSingleAttachment('批量导入游客',function(upload,value) {
						var v = value,
							f = upload.up('form'),
							form = f.getForm(),
							win = f.up('window');
						var fix = /\.xls/i;
		                if (!fix.test(v)) {
		                	util.alert('文件格式不正确');
		                	return;
		                }else{
		                	win.body.mask('游客数据导入中...');
		                	if(form.isValid()){
			                	form.submit({
			                		submitEmptyText:false ,
									url:cfg.getCtx()+'/order/visitor/importVisitor',
									method:'POST',
									success:function(res,req){
										var msg = req.result.message||'';
										if(msg!=''){
											util.alert('导入数据存在异常！参照内容：'+msg);
										}else{
											util.success('导入数据成功！');
										}
										win.body.unmask();
										win.close();
										//me.store.loadData(req.result.data,true);
										var datas = req.result.data;
										Ext.Array.each(datas,function(o,i){
											o.CONTACT=0;
											me.store.add(Ext.create('app.model.order.visitor.model',o));
										});
										me.calcPrice();
									},
									failure:function(){
										util.error('导入数据失败，格式不正确！请严格按照模板格式填写！');
										win.body.unmask();
										win.close();
									}
								});
							}
		                }
					})
        		}
        	},'-',{
        		tooltip:'下载导入模板',
        		glyph:'xe69d@iconfont',
        		href:cfg.getCtx()+'/files/route_visitor_template.xls'
        	},'-',{
        		tooltip:'导入订单游客',
        		glyph:'xe69b@iconfont',
        		handler:function(){
        			var win = util.createEmptyWindow('输入订单编号','&#xe60b;',260,130,[{
        				xtype:'textfield',
        				name:'orderNo',
        				anchor:'100%',
		   				emptyText:'订单编号'
        			}],[{
        				text:'确定',
		   				handler:function(btn){
		   					var f = btn.up('form'),
				        		form = f.getForm(),
				        		win = f.up('window');
				        	if (form.isValid()) {
					            form.submit({
					            	submitEmptyText:false ,
					            	url:cfg.getCtx()+'/order/visitor/listOrderVisitor',
					                success: function(form, action) {
					                	var msg = action.result.message||'';
										if(msg!=''){
											util.alert('导入游客存在异常！参照内容：'+msg);
										}else{
											util.success('导入游客成功！');
										}
										var datas = action.result.data;
										Ext.Array.each(datas,function(o,i){
											o.CONTACT=0;
											me.store.add(Ext.create('app.model.order.visitor.model',o));
										});
										me.calcPrice();
										win.close();
					                },
					                failure: function(form, action) {
					                	var state = action.result?action.result.statusCode:0,
					                		errors = ['导入游客失败'];
					                    util.error(errors[0-parseInt(state)]);
					                    win.close();
					                }
					            });
					        }
		   				}
        			}]).show();
        		}
        	},/*,'-',{
        		tooltip:'查询常用游客',
        		glyph:'xe60e@iconfont'
        	},'<span class="orange-color"><i class="iconfont f18">&#xe6ae;</i> 修改游客请双击单元行</span>'*/,'->',
        	{
        		itemId:'connoItem',
        		xtype:'combo',
    			fieldLabel:'合同号',
    			emptyText:'请选择合同号',
    			hidden:orderId!='',
    			width:200,
    			name:'pm[CON_NO]',
    			hiddenName:'pm[CON_NO]',
			    queryMode: 'remote',
			    triggerAction: 'all',
			    focusOnToFront:true,
			    forceSelection:true,
			    displayField: 'NO',
			    valueField: 'NO',
			    minChars:1,
			    pageSize:20,
			    listConfig:{
			    	minWidth:360
			    },
			    store:Ext.create('Ext.data.Store',{
			    	pageSize:20,
			    	proxy: {
			    		type: 'ajax',
				        noCache:true,
			            url: cfg.getCtx()+'/site/company/list/contract?status=0',
			            reader: {
			            	rootProperty: 'data',
		            		totalProperty: 'totalSize'
			            }
			        },
			    	model:Ext.create('Ext.data.Model',{
			        	fields: ['ID','NO']
			        })
			    })
        	},
        	{
    			xtype:'combo',
    			fieldLabel:'集合信息',
    			width:226,
			    displayField: 'MUSTER_PLACE',
			    focusOnToFront:true,
			    forceSelection:true,
			    editable:false,
			    queryMode: 'remote',
			    triggerAction: 'all',
			    valueField: 'MUSTER_PLACE',
			    emptyText:'选择集合地点',
			    hidden:planId=='',
    			value:(od?od.MUSTER_PLACE:''),
			    listConfig:{
			    	minWidth:360,
			    	itemTpl:[
						 '<tpl for=".">',
				            '<li class="city-item">{MUSTER_PLACE}<span>{MUSTER_TIME}</span></li>',
				        '</tpl>'
					]
			    },
			    store:Ext.create('Ext.data.Store',{
			    	model:Ext.create('Ext.data.Model',{
			        	fields: ['MUSTER_PLACE','MUSTER_TIME']
			        }),
			        data:musters||[]
			    }),listeners:{
			    	select:function( combo, records, eOpts ){
			    		combo.nextSibling().setValue(records.get('MUSTER_TIME'));
			    		combo.nextSibling().nextSibling().setValue(combo.getValue());
			    	}
			    }
    		},{
    			xtype:'textfield',
    			name:'pm[MUSTER_TIME]',
    			readOnly:true,
    			width:70,
    			hidden:planId=='',
    			itemId:'musterTimeItem',
    			emptyText:'集合时间',
    			value:(od?od.MUSTER_TIME:'')
    		},{
    			xtype:'hidden',
    			name:'pm[MUSTER_PLACE]',
    			value:(od?od.MUSTER_PLACE:'')
    		}]
    	}];
    	this.plugins = rowEditing;
        this.callParent();
        
        
        if(orderId!=''){
        	//me.mask('读取中...');
        	Ext.Ajax.request({
				url:cfg.getCtx()+'/order/visitor/listOrderVisitor?orderId='+orderId,
				success:function(response, opts){
					//me.unmask();
					var obj = Ext.decode(response.responseText);
					me.store.setData(obj.data);
					setTimeout(function(){me.calcPrice();},500);
				}
			});
        }else{
        	setTimeout(function(){
            	var r = Ext.create('app.model.order.visitor.model', {
                    ID: '',
                    NAME: '',
                    MOBILE: '',
                    SEX: '1',
                    CARD_TYPE: '身份证',
                    CARD_NO: '',
                    EMAIL: '',
                    ATTR_NAME:'成人',
                    CONTACT:0
                });

                me.store.insert(0, r);
                rowEditing.startEdit(0, 0);
            	
            },500);
        }
        
        
	},
	columnLines: true,
	calcPrice:function(){
		var mds = this.getStore().getData(),cnt = {'成人':0,'不占床位/不含门票':0,'不占床位/含门票':0,'占床位/不含门票':0,'占床位/含门票':0,'婴儿':0,'儿童':0};
		this.getStore().filterBy(function(record) { 
            cnt[record.get('ATTR_NAME')]++;
            return true;
        });
		var hiddenPanel = this.up('form').down('panel[itemId=rightPanel]');
		hiddenPanel.down('hidden#manCount').setValue(cnt['成人']);
		var childCount = 0;
		for(var i in cnt){
			if(i!='成人'){
				childCount+=cnt[i];
			}
		}
		hiddenPanel.down('hidden#childCount').setValue(childCount);
	}
});