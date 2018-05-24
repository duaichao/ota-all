Ext.define('app.controller.site.area', {
	extend: 'app.controller.common.BaseController',
	views:['common.ScenicCombo','common.CityCombo','site.areachild'],
	stores:['City','Scenic'],
	config: {
		control: {
			'toolbar[itemId=savetool] button[itemId=add]':{
				click:'onAdd'
			},
			'toolbar[itemId=savetool] button[itemId=edit]':{
				click:'onEdit'
			},
			'toolbar[itemId=savetool] button[itemId=del]':{
				click:'onDel'
			},
			'toolbar[itemId=toggletool] segmentedbutton':{
				toggle:'onToggle'
			},
			'toolbar[itemId=citytools] button[itemId=addCityLabel]':{
				click:'addCityLabel'
			},
			'gridpanel[itemId=basegrid]':{
				rowclick:'onRowClick'
			},
			'tabpanel':{
				tabchange:'onTabChange'
			}
		},
		refs: {
			toggleButtons:'toolbar[itemId=toggletool] segmentedbutton',
			areaGrid:'gridpanel[itemId=basegrid]',
			childView:'tabpanel',
			areachild:'areachild'
        }
	},
	addCityLabel:function(btn){
		var me = this,
			activeTab = this.getChildView().getActiveTab(),
			toggleValue = activeTab.labelType;
		var win = Ext.create('Ext.window.Window',{
			title:util.windowTitle('&#xe631;','添加目的地',''),
			width:300,
			y:50,
			height:180,
			modal:true,
			draggable:false,
			resizable:false,
   			layout:'fit',
			items:[{
				xtype:'form',
				bodyPadding: 10,
				bodyStyle:'background:#fff;',
    			scrollable:false,
    			fieldDefaults: {
			        labelAlign: 'right',
			        labelWidth: 60,
			        msgTarget: 'side'
			    },
			    defaults:{
			    	anchor:'100%'
			    },
			    items: [{
			    	name:'pm[ENTITY_ID]',
			    	hiddenName:'pm[ENTITY_ID]',
			    	xtype:'citycombo',
			    	listeners:{
			    		beforequery:function(queryPlan, eOpts){
			    			var combo = queryPlan.combo,
								store = combo.getStore();
							store.getProxy().setExtraParams({type:activeTab.labelType});
			    		},
			    		select:function( combo, records, eOpts ){
			    			combo.nextSibling().setValue(records.get('TEXT'));
			    		}
			    	}
				},{
					xtype:'hidden',
					name:'pm[ENTITY_NAME]'
				},{
					xtype:'hidden',
					name:'pm[LABEL_ID]',
					value:activeTab.getId()
				},{
					xtype:'hidden',
					name:'pm[TYPE]',
					value:'0'
					//value:(toggleValue=='3'?'1':'0') 2016/5/27 修改
				}],
				buttons: [{
			        text: '保存',
			        itemId:'save',
			        disabled: true,
			        formBind: true,
			        handler:function(btn){
			        	var form = btn.up('form'),
			        		win = form.up('window');
			        	me.onSaveCityLabel(form.getForm(),win,activeTab);
			        }
			    }]
			}]
		}).show();
	},
	onSaveCityLabel:function(form,win,activeTab){
		var me = this,btn=win.down('button#save');
		if (form.isValid()) {
			btn.disable();
            form.submit({
            	submitEmptyText:false ,
            	url:cfg.getCtx()+'/site/area/saveCityLabel',
                success: function(form, action) {
                   util.success('保存目的地成功');
                   me.loadCityLabels(activeTab);
                   win.close();
                },
                failure: function(form, action) {
                	var state = action.result?action.result.statusCode:0,
                		msg = action.result?action.result.message:'',
                		errors = ['保存异常',msg+'已存在，不能重复选择'];
                    util.error(errors[0-parseInt(state)]);
                    btn.enable();
                }
            });
        }
	},
	onTabChange:function(tabPanel, newCard, oldCard, eOpts ){
		this.loadCityLabels(newCard);
	},
	loadCityLabels:function(activeTab){
		var me = this,
			itemsPanel = activeTab.down('[itemId=citypanel]').down('[itemId=cityitems]'),
			pid = activeTab.getId(),
			ptype = '0';
			//ptype = activeTab.labelType=='3'?'1':'0'; 2016/5/27 修改
		Ext.Ajax.request({
			url:cfg.getCtx()+'/site/area/listCityLabel',
			params:{
				'labelID':pid,
				'type':ptype
			},
			success:function(response, opts){
				var ids = Ext.decode(response.responseText).data;
				itemsPanel.removeAll(true);
				for(var i=0;i<ids.length;i++){
					itemsPanel.add({
						cls:'tag-split-btn remove',
						margin:'0 5 5 0',
						xtype:'splitbutton',
						text:ids[i].ENTITY_NAME,
						itemId:ids[i].ENTITY_ID,
						arrowHandler :function(m){
                        	me.deleteCityTag(m.itemId,m);
                        }
					});
				}
				itemsPanel.doLayout();
			}
		});
	},
	onBaseGridRender:function(tp){
		tp.getStore().load({params:{'type':''+this.getToggleButtons().getValue()}});
	},
	onRowClick:function(g, record, tr, rowIndex, e, eOpts){
		var tab = this.getChildView(),
			id = record.get('ID'),
			text = record.get('TEXT');
		if(tab.down('#'+id)){
			tab.setActiveTab(id);
		}else{
			tab.add({
				id:id,
				bodyStyle:'background:transparent!important;',
				closable:true,
				labelType:this.getToggleButtons().getValue(),
				title:text,
				xtype:'areachild'
			});
			tab.setActiveTab(id);
		}
	},
	createSaveWindow:function(btn,title){
		var me = this,win = Ext.create('Ext.window.Window',{
			title:util.windowTitle(util.glyphToFont(btn.glyph),title,''),
			width:400,
			height:200,
			modal:true,
			draggable:false,
			resizable:false,
   			layout:'fit',
			items:[{
				xtype:'form',
				bodyPadding: 5,
				bodyStyle:'background:#fff;',
    			scrollable:false,
    			fieldDefaults: {
			        labelAlign: 'right',
			        labelWidth: 60,
			        msgTarget: 'side'
			    },
			    defaults:{
			    	anchor:'100%'
			    },
			    items: [{
			    	xtype:'hidden',
			    	name:'pm[ID]'
			    },{
					fieldLabel:'区域名称',
				    name : 'pm[TEXT]',
				    xtype: 'textfield',
				    allowBlank: false
				},{
					margin:'10 0 0 0',
					fieldLabel:'类型',
					hiddenName:'pm[TYPE]',
					name:'pm[TYPE]',
					anchor:'40%',
					xtype:'combobox',
					value:1,
					displayField:'text',
					valueField: 'value',
		            typeAhead: true,
		            queryMode: 'local',
		            store: Ext.create('Ext.data.Store', {
					    fields: ['text', 'value'],
					    data : [
					        {"value":"1", "text":"周边"},
					        {"value":"2", "text":"国内"},
					        {"value":"3", "text":"出境"}
					    ]
					})
				},{
					margin:'5 0 0 0',
					fieldLabel:'状态',
					xtype:'radiogroup',
		        	anchor: '70%',
		        	items: [
		                {boxLabel: '启用', name: 'pm[IS_USE]', inputValue: 0, checked: true},
		                {boxLabel: '禁用', name: 'pm[IS_USE]', inputValue: 1}
		            ]
				}],
				buttons: [{
			        text: '保存',
			        itemId:'save',
			        disabled: true,
			        formBind: true,
			        handler:function(btn){
			        	var form = btn.up('form'),
			        		win = form.up('window');
			        	me.onSave(form.getForm(),win);
			        }
			    }]
			}]
		});
		return win;
	},
	onAdd:function(btn){
		var win = this.createSaveWindow(btn,'添加区域').show();
		win.down('form').down('combobox').setValue(this.getToggleButtons().getValue());
		
	},
	onSave:function(form,win){
		var me = this,btn=win.down('button#save');
		if (form.isValid()) {
			btn.disable();
            form.submit({
            	submitEmptyText:false ,
            	url:cfg.getCtx()+'/site/area/save',
                success: function(form, action) {
                   util.success('保存区域成功');
                   win.close();
                   me.getAreaGrid().getStore().load({params:{'type':''+me.getToggleButtons().getValue()}});
                },
                failure: function(form, action) {
                	var state = action.result?action.result.statusCode:0,
                		errors = ['保存异常','区域名称已存在'];
                    util.error(errors[0-parseInt(state)]);
                    btn.enable();
                }
            });
        }
	},
	onToggle:function(sb, button, isPressed, eOpts ){
		this.getAreaGrid().getStore().load({params:{'type':''+sb.getValue()}});
	},
	onEdit:function(btn){
		var rg = this.getAreaGrid(),
			win,form,sel,selData,formData={};
		if(sel = util.getSingleModel(rg)){
			win = this.createSaveWindow(btn,'修改区域');
			form = win.down('form');
			win.show();
			formData = util.pmModel(sel);
			form.getForm().setValues(formData);
		}
	},
	onDel:function(btn){
		util.delGridModel(this.getAreaGrid(),'/site/area/del', {'type':''+this.getToggleButtons().getValue()});
	},
	deleteCityTag:function(entityId,btn){
		var me = this,
			activeTab = this.getChildView().getActiveTab(),
			toggleValue = activeTab.labelType;
		Ext.Ajax.request({
			url:cfg.getCtx()+'/site/area/delCityLabel',
			params:{
				'ENTITY_ID':entityId,
				'LABEL_ID':activeTab.getId()
			},
			success:function(response, opts){
				 btn.destroy();
			}
		});
	}
});