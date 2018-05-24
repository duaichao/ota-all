Ext.define('app.view.produce.traffic.visitorGrid', {
	extend:'Ext.grid.Panel',
	xtype:'trafficvisitorgrid',
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
	            width:100,
	            editor: {
	                allowBlank: false
	            }
        	},{
	            header: '性别',
	            dataIndex: 'SEX',
	            width: 55,
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
	            width: 120,
	            editor: new Ext.form.field.ComboBox({
                    typeAhead: true,
                    allowBlank: false,
                    editable:false,
                    triggerAction: 'all',
                    store: [
                        ['成人','成人'],
                        ['儿童','儿童']/*,
                        ['婴儿','婴儿']*/
                    ]
                })
        	},{
        		header: '手机',
	            dataIndex: 'MOBILE',
	            width:150,
	            editor: {
	                allowBlank: false
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
	        errorSummary:false,
	        autoCancel: false,
	        listeners:{
	        	canceledit:function( editor, context, eOpts){
	        		var r = context.record;
	        		if(r.get('NAME')==''||r.get('MOBILE')==''||r.get('CARD_NO')==''){
	        			var sm = me.getSelectionModel();
		                rowEditing.cancelEdit();
		                me.store.remove(sm.getSelection());
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
	    })
        this.dockedItems = [{
    		xtype:'toolbar',
    		itemId:'gridtool',
    		defaults:{scale: 'medium'},
        	items:[{
        		tooltip:'添加游客',
        		glyph:'xe609@iconfont',
        		handler:function(){
        			rowEditing.cancelEdit();
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
        		}
        	},'-',{
        		tooltip:'删除游客',
        		glyph:'xe608@iconfont',
        		handler:function(){
        			var sm = me.getSelectionModel();
	                rowEditing.cancelEdit();
	                me.store.remove(sm.getSelection());
	                if (me.store.getCount() > 0) {
	                    sm.select(0);
	                }
	                me.calcPrice()
        		}
        	},'-',{
        		tooltip:'批量导入游客',
        		glyph:'xe60a@iconfont',
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
										me.store.loadData(req.result.data,true);
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
        	},'-','<span class="yellow-color"><i class="iconfont f18">&#xe6ae;</i> 修改游客请双击单元行</span>','->',{
        		tooltip:'下载导入模板',
        		glyph:'xe60d@iconfont',
        		href:cfg.getCtx()+'/files/visitor_template.xls'
        	}/*,'-',{
        		tooltip:'查询常用游客',
        		glyph:'xe60e@iconfont'
        	}*/]
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
					me.calcPrice();
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
		var mds = this.getStore().getData(),cnt = {'成人':0,'儿童':0,'婴儿':0};
		this.getStore().filterBy(function(record) { 
            cnt[record.get('ATTR_NAME')]++;
            return true;
        });
		var totalPanel = this.up('form').down('panel[itemId=rightPanel]').down('panel[itemId=totalPanel]'),
			hiddenPanel = this.up('form').down('panel[itemId=rightPanel]').down('fieldcontainer'),
			el = totalPanel.getEl().down('ul'),
			total = el.down('li.total'),
			lis = el.query('li:not(.total)',false),
			zj = 0,
			thzj = 0;
		for(var i=0;i<lis.length;i++){
			var li = lis[i],attrName = cfg.getPriceAttrs()[li.dom.id],
				wmLabel = li.down('label.wm'),
				thLabel = li.down('label.th'),
				pre = li.getAttribute('price'),
				thpre = li.getAttribute('thprice'),
				hj = cnt[attrName]*parseFloat(pre),
				thhj = cnt[attrName]*parseFloat(thpre);
			zj += hj;
			thzj += thhj;
			wmLabel.setHtml('<b>'+cnt[attrName]+' ×</b>'+util.moneyFormat(pre,'f12 disable-color'));
			thLabel.setHtml('<b>'+cnt[attrName]+' ×</b>'+util.moneyFormat(thpre,'f12 disable-color'));
		}
		hiddenPanel.down('hidden[itemId=saleAmount]').setValue(zj);
		hiddenPanel.down('hidden[itemId=interAmount]').setValue(thzj);
		hiddenPanel.down('hidden[itemId=manCount]').setValue(cnt['成人']);
		hiddenPanel.down('hidden[itemId=childCount]').setValue(cnt['儿童']+cnt['婴儿']);
		total.down('label.wm').setHtml(util.moneyFormat(zj,'f20'));
		total.down('label.th').setHtml(util.moneyFormat(thzj,'f20'));
		
	}
});