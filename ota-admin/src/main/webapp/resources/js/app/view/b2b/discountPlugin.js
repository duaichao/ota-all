Ext.define('app.view.b2b.discountPlugin', {
    extend: 'Ext.window.Window',
    config:{
    	layout:'fit',
    	title:util.windowTitle('&#xe623;','参加优惠活动',''),
		width:500,
		height:220,
		modal:true,
		draggable:false,
		resizable:false,
    	proId:'',
    	proType:'',
    	cityId:'',
    	discountId:''
    },
    initComponent: function() {
    	var me = this;
    	var combo = Ext.create('Ext.form.field.ComboBox',{
    		value:this.getDiscountId(),
		    fieldLabel:'优惠名称',
		    itemId:'discountCombo',
		    queryParam: 'query',
		    displayField: 'TITLE',
		    focusOnToFront:false,
		    forceSelection:true,
		    queryMode: 'remote',
		    triggerAction: 'all',
		    valueField: 'ID',
		    width:450,
		    name:'pm[DISCOUNT_ID]',
		    hiddenName:'pm[DISCOUNT_ID]',
		    editable:false,
		    emptyText:'选择一个优惠活动',
		    listConfig:{
		    	minWidth:360,
		    	itemTpl:[
					 '<tpl for=".">',
			            '<li class="city-item">{TITLE}</li>',
			        '</tpl>'
				]
		    },
		    listeners : {
			    beforequery: function(qe){
			    	delete qe.combo.lastQuery;
			    },
			    change: function( combo, newValue, oldValue, eOpts ){
			    	me.loadRule(combo);
			    }
		    },
		    store:Ext.create('Ext.data.Store',{
			    model:Ext.create('app.model.b2b.discount.model'),
			    autoLoad: true, 
			    proxy: {
			        type: 'ajax',
			        noCache:true,
			        url: cfg.getCtx()+'/b2b/discount/list?cityId='+this.getCityId()+'&proType='+this.getProType()+'&isUse=0',
			        reader: {
			            type: 'json',
			            rootProperty: 'data',
			            totalProperty:'totalSize'
			        }
			   	}
		    })
        });
    	combo.getStore().on('load',function(){
    		me.loadRule(combo);
    	});
    	this.items = [{
			xtype:'form',
			scrollable:false,
			fieldDefaults: {
		        labelAlign: 'right',
		        labelWidth: 80,
		        msgTarget: 'side'
		    },
			bodyPadding: '10 5 5 5',
			bodyStyle:'background:#fff;',
			items:[{xtype:'hidden',name:'pm[PRO_ID]',value:this.getProId()},
			       {xtype:'hidden',name:'pm[PRO_TYPE]',value:this.getProType()},combo,{
				fieldLabel:'优惠套餐',
				xtype:'checkboxgroup',
				hidden:true,
				vertical: true,
				columns: 2
			}],
			buttons: [{
				itemId:'save',
		        text: '保存',
		        disabled: true,
		        formBind: true,
		        handler:function(btn){
		        	var form = btn.up('form'),
		        		win = form.up('window');
		        	me.onSaveDiscountAction(form.getForm(),win);
		        }
		    }]
		}];
    	this.callParent();
    },
    loadRule:function(combo){
    	if(!combo.getValue())return;
    	Ext.Ajax.request({
			url:cfg.getCtx()+'/b2b/discount/rule?proId='+this.getProId()+'&discountId='+combo.getValue()+'&isUse=0',
			success:function(response, opts){
				var obj = Ext.decode(response.responseText),
					data = obj.data;
				var cg = combo.nextSibling();
				cg.show();
				cg.removeAll();
				var pt = ['','B2B','APP'],
					pay = ['','在线支付','余额支付'],
					unit = ['','元','%','元/人'];
				Ext.Array.each(data,function(d,i){
					var str = [];
					str.push(pt[d.PLATFROM]);
					str.push(pay[d.PAY_WAY]);
					str.push(' 优惠：'+d.MONEY+unit[d.RULE_TYPE]);
					cg.add({
						boxLabel:str.join(''),
						name: 'DISCOUNT_RULE_ID',
						checked:d.CHECKED,
						inputValue:d.ID
					});
				});
				
			}
		});
    },
    onSaveDiscountAction:function(form,win){
    	var me = this,btn=win.down('button#save');
		if (form.isValid()) {
			btn.disable();
            form.submit({
            	submitEmptyText:false ,
            	url:cfg.getCtx()+'/b2b/discount/save/product',
                success: function(form, action) {
                   util.success('设置优惠成功');
                   win.close();
                },
                failure: function(form, action) {
                	var state = action.result?action.result.statusCode:0,
                		errors = ['设置异常'];
                    util.error(errors[0-parseInt(state)]);
                    win.close();
                }
            });
        }
    }
});

