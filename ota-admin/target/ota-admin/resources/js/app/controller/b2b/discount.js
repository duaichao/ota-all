Ext.define('app.controller.b2b.discount', {
	extend: 'app.controller.common.BaseController',
	requires:['Ext.ux.DateTimeField'],
	config: {
		control: {
			'toolbar button[itemId=add]':{
	        	afterrender:'onBtnRender',
	        	click:'onBtnClick'
	        },
	        'toolbar button[itemId=edit]':{
	        	click:'onEdit'
	        },
	        'toolbar button[itemId=switch]':{
	        	click:'onSwitch'
	        },
	        'toolbar button[itemId=add] menu':{
	        	click:'onBtnMenuClick'
	        },
	        'gridpanel[itemId=basegrid]': {
	             itemclick:'onGridItemClick'
	        },
	        'toolbar button[itemId=addChild]':{
	        	click:'onChildAdd'
	        },
	        'toolbar button[itemId=editChild]':{
	        	click:'onChildEdit'
	        },
	        'toolbar button[itemId=switchChild]':{
	        	click:'onChildSwitch'
	        }
		},
		refs: {
           discountGrid: 'gridpanel[itemId=basegrid]',
           ruleGrid:'gridpanel[itemId=rulegrid]',
           ruleToolbar:'toolbar[itemId=ruletool]'
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
		        	rbtn = [];
		        Ext.Array.each(items,function(item,i){
		        	if(items[i].itemId.indexOf('Child')!=-1){
		        		rbtn.push(items[i]);
		        	}else{
		        		ubtn.push(items[i]);
		        	}
		        });
		        ubtn.push('->');
		        ubtn.push({
					emptyText:'搜索关键字',
		        	xtype:'searchfield',
		        	store:g.getStore()
				});
				me.getRuleToolbar().removeAll();
		        me.getRuleToolbar().add(rbtn);
		        util.createGridTbar(g,ubtn);
		    }
		});
		},500);
	},
	onBtnRender:function(btn){
		Ext.Ajax.request({
		    url: cfg.getCtx()+'/site/company/ownerSites',
		    success: function(response, opts) {
		        var obj = Ext.decode(response.responseText),
		        	items = obj.data,
		        	menus = [];
		        for(var i=0;i<items.length;i++){
		        	menus.push({
		        		orgId:items[i].ID,
		        		itemId:items[i].CITY_ID,
		        		pinyin:items[i].PINYIN,
		        		text:items[i].SUB_AREA
		        	});
		        }
		        btn.setMenu(menus);
		    }
		});
	},
	onBtnClick:function(btn){
		util.alert('点击按钮箭头选择一个城市');
	},
	onBtnMenuClick:function(m,item){
		var me = this;
		this.createDisWindow(item.orgId);
	},
	createDisWindow:function(orgId){
		var me = this;
		var beginTime = Ext.widget("datetimefield", {
			fieldLabel:'开始时间',
			width:260,
			allowBlank: false,
			name:'pm[BEGIN_TIME]',
            format: 'Y/m/d H:i:s'
        });
		var endTime = Ext.widget("datetimefield", {
			fieldLabel:'结束时间',
			width:260,
			allowBlank: false,
			name:'pm[END_TIME]',
            format: 'Y/m/d H:i:s'
        });
		var proType = Ext.create('Ext.form.field.ComboBox',{
			fieldLabel:'产品类型',
			width:150,
			name:'pm[PRO_TYPE]',
			forceSelection: true,
			editable:false,
            valueField: 'value',
            displayField: 'text',
            store:util.createComboStore([{
            	text:'交通',
            	value:1
            },{
            	text:'线路',
            	value:2
            }]),
            minChars: 0,
            value:2,
            itemId:'proType',
            queryMode: 'local',
            typeAhead: true
		});
		var win = Ext.create('Ext.window.Window',{
			title:util.windowTitle('&#xe62b;','优惠折扣',''),
			width:550,
			height:400,
			modal:true,
			draggable:false,
			resizable:false,
   			layout:'fit',
			items:[{
				xtype:'form',
    			scrollable:false,
				fieldDefaults: {
			        labelAlign: 'right',
			        labelWidth: 60,
			        msgTarget: 'side'
			    },
				bodyPadding: 5,
				bodyStyle:'background:#fff;',
				items:[{xtype:'hidden',name:'pm[ID]'},{xtype:'hidden',name:'pm[SITE_RELE_ID]',value:orgId},{
					fieldLabel:'优惠名称',
					allowBlank: false,
					anchor:'100%',
		        	name : 'pm[TITLE]',
					xtype: 'textfield'
				},beginTime,endTime,proType,{
					anchor:'75%',
					xtype:'radiogroup',
					itemId:'isRefund',
					fieldLabel:'可否退款',
					items:[
						{ boxLabel: '此优惠订单可以退款', name: 'pm[IS_REFUND]', inputValue: 1 },
			            { boxLabel: '此优惠订单不能退款', name: 'pm[IS_REFUND]', inputValue: 0, checked: true}
					]
				},{
					xtype:'textarea',
					anchor:'100%',
					fieldLabel:'备注',
					name:'pm[REMARK]'
				}],
				buttons: [{
			        text: '保存',
			        itemId:'save',
			        disabled: true,
			        formBind: true,
			        handler:function(btn){
			        	var form = btn.up('form'),
			        		win = form.up('window');
			        	me.onSaveDiscount(form.getForm(),win);
			        }
			    }]
			}]
		});
		win.show();
		return win;
	},
	onSaveDiscount:function(form,win){
		var me = this,btn = win.down('button#save');
		if (form.isValid()) {
			btn.disable();
            form.submit({
            	submitEmptyText:false ,
            	url:cfg.getCtx()+'/b2b/discount/save',
                success: function(form, action) {
                   util.success('保存优惠成功');
                   me.getDiscountGrid().getStore().load();
                   win.close();
                },
                failure: function(form, action) {
                	var state = action.result?action.result.statusCode:0,
                		errors = ['保存异常','该优惠名称存在'];
                    util.error(errors[0-parseInt(state)]);
                    btn.enable();
                }
            });
        }
	},
	onEdit:function(btn){
		var rg = this.getDiscountGrid(),
			win,form,sel,selData,formData={};
		if(sel = util.getSingleModel(rg)){
			win = this.createDisWindow();
			form = win.down('form');
			formData = util.pmModel(sel);
			form.getForm().setValues(formData);
		}
	},
	onSwitch:function(btn){
		util.switchGridModel(this.getDiscountGrid(),'/b2b/discount/switch');
	},
	onGridItemClick:function(g,record, item, index, e){
		var rg = this.getRuleGrid();
		rg.getStore().getProxy().setExtraParams({discountId:record.get('ID')});
		rg.getStore().load();
	},
	onChildAdd:function(btn){
		var g = this.getDiscountGrid(),sel;
		if(sel = util.getSingleModel(g)){
			this.createRuleWin(sel.get('ID'));
		}
	},
	createRuleWin:function(dcId){
		var me = this;
		var win =  Ext.create('Ext.window.Window',{
			title:util.windowTitle('&#xe89d;','优惠套餐',''),
			width:500,
			height:250,
			modal:true,
			draggable:false,
			resizable:false,
   			layout:'fit',
			items:[{
				xtype:'form',
    			scrollable:false,
				fieldDefaults: {
			        labelAlign: 'right',
			        labelWidth: 60,
			        msgTarget: 'side'
			    },
				bodyPadding: 5,
				bodyStyle:'background:#fff;',
				items:[{xtype:'hidden',name:'pm[ID]'},{xtype:'hidden',name:'pm[DISCOUNT_ID]',value:dcId},{
					anchor:'100%',
					xtype:'radiogroup',
					fieldLabel:'平台类型',
					items:[
						{ boxLabel: 'B2B', name: 'pm[PLATFROM]', inputValue: 1 , checked: true},
			            { boxLabel: 'APP', name: 'pm[PLATFROM]', inputValue: 2}
					]
				},{
					anchor:'100%',
					xtype:'radiogroup',
					fieldLabel:'支付类型',
					items:[
						{ boxLabel: '在线支付', name: 'pm[PAY_WAY]', inputValue: 1 , checked: true},
			            { boxLabel: '余额支付', name: 'pm[PAY_WAY]', inputValue: 2}
					]
				},{
					anchor:'100%',
					xtype:'radiogroup',
					fieldLabel:'套餐类型',
					items:[
						{ boxLabel: '金额百分比(%)', name: 'pm[RULE_TYPE]', inputValue: 2 , checked: true},
			            { boxLabel: '固定金额(元)', name: 'pm[RULE_TYPE]', inputValue: 1},
			            { boxLabel: '单人优惠(元)', name: 'pm[RULE_TYPE]', inputValue: 3}
					]
				},{
					width:150,
					xtype:'textfield',
					allowBlank: false,
					name: 'pm[MONEY]',
					fieldLabel:'套餐值'
				}],
				buttons: [{
			        text: '保存',
			        itemId:'save',
			        disabled: true,
			        formBind: true,
			        handler:function(btn){
			        	var form = btn.up('form'),
			        		win = form.up('window');
			        	me.onSaveRule(form.getForm(),win);
			        }
			    }]
			}]
		});
		win.show();
		return win;
	},
	onSaveRule:function(form,win){
		var me = this,btn = win.down('button#save');
		if (form.isValid()) {
			btn.disable();
            form.submit({
            	submitEmptyText:false ,
            	url:cfg.getCtx()+'/b2b/discount/save/rule',
                success: function(form, action) {
                   util.success('保存优惠规则成功');
                   me.getRuleGrid().getStore().load();
                   win.close();
                },
                failure: function(form, action) {
                	var state = action.result?action.result.statusCode:0,
                		errors = ['保存异常','规则已存在'];
                    util.error(errors[0-parseInt(state)]);
                    btn.enable();
                }
            });
        }
	},
	onChildEdit:function(btn){
		var rg = this.getRuleGrid(),
			win,form,sel,selData,formData={};
		if(sel = util.getSingleModel(rg)){
			win = this.createRuleWin();
			form = win.down('form');
			formData = util.pmModel(sel);
			form.getForm().setValues(formData);
		}
	},
	onChildSwitch:function(btn){
		util.switchGridModel(this.getRuleGrid(),'/b2b/discount/switch/rule');
	}
});