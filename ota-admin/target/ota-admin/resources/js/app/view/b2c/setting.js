var pv = cfg.getUser().userType;
Ext.define('app.view.b2c.setting', {
	extend: 'Ext.form.Panel',
	config:{
		autoScroll:true,
		fieldDefaults: {
	        labelAlign: 'right',
	        labelWidth: 100,
	        msgTarget: 'qtip'
	    },
		bodyPadding: 5,
		defaults:{
			readOnly:(pv=='01')
		},
		items:[{
				xtype:'hiddenfield',
				name:'pm[ID]'
			},{
				xtype:'textfield',
				anchor:'100%',
				fieldLabel:'网站名称',
				name:'pm[TITLE]',
				allowBlank:false
			}/*,{
				xtype: 'fieldcontainer',
				fieldLabel:'域名',
		        labelWidth: 100,
		        layout: 'hbox',
		        items: [{
		        	name:'pm[DOMAIN]',
		            xtype: 'textfield',emptyText:'域名',itemId:'domain',
		            flex: 1
		        },{
		            xtype: 'splitter'
		        },{
		            xtype: 'textfield',
		            width:150,
		            itemId:'beianhao',
		            emptyText:'备案号',
					name:'pm[RECORD_NO]'
		        }]
			}*/,{
				xtype:'filefield',
				anchor:'100%',
				fieldLabel:'LOGO',
				name:'LOGO',
				buttonConfig: {
	                text:'',
	                disabled:(pv=='01'),
					ui:'default-toolbar',
	                glyph: 'xe648@iconfont'
	            }
			},{
				xtype:'filefield',
				anchor:'100%',
				fieldLabel:'徽标',
				name:'ICON',
				buttonConfig: {
	                text:'',
	                disabled:(pv=='01'),
					ui:'default-toolbar',
	                glyph: 'xe648@iconfont'
	            }
			},{
				xtype:'filefield',
				anchor:'100%',
				fieldLabel:'二维码',
				name:'CODE',
				buttonConfig: {
	                text:'',
	                disabled:(pv=='01'),
					ui:'default-toolbar',
	                glyph: 'xe648@iconfont'
	            }
			},{
				xtype:'filefield',
				anchor:'100%',
				fieldLabel:'微信二维码',
				name:'WX_IMG',
				buttonConfig: {
	                text:'',
	                disabled:(pv=='01'),
					ui:'default-toolbar',
	                glyph: 'xe648@iconfont'
	            }
			}/*,{
				xtype: 'fieldcontainer',
				fieldLabel:'支付宝',
		        labelWidth: 100,
		        layout: 'hbox',
		        items: [{
		        	itemId:'alipayAccount',
		        	name:'pm[SELLER_EMAIL]',
		            xtype: 'textfield',emptyText:'支付宝账号',
		            flex: 1
		        }, {
		            xtype: 'splitter'
		        }, {
		        	itemId:'alipayIdcard',
		            xtype: 'textfield',
		            flex: 1,
		            emptyText:'合作者身份号',
					name:'pm[PARTNER]'
		        }, {
		            xtype: 'splitter'
		        }, {
		        	itemId:'alipayKey',
		            xtype: 'textfield',
		            flex: 1,emptyText:'加密KEY',
					name:'pm[KEY]'
		        }]
			}*/,{
				xtype: 'fieldcontainer',
				fieldLabel:'联系客服',
		        labelWidth: 100,
		        layout: 'hbox',
		        items: [{
		        	name:'pm[QQ1]',
		            xtype: 'textfield',emptyText:'QQ1',
		            flex: 1
		        },{
		            xtype: 'splitter'
		        },{
		            xtype: 'textfield',
		            flex: 1,
		            emptyText:'QQ2',
					name:'pm[QQ2]'
		        },{
		            xtype: 'splitter'
		        },{
		            xtype: 'textfield',
		            flex: 1,emptyText:'客服电话1',
					name:'pm[PHONE1]'
		        },{
		            xtype: 'splitter'
		        },{
		            xtype: 'textfield',
		            flex: 1,emptyText:'客服电话2',
					name:'pm[PHONE2]'
		        }]
			},{
		    	xtype: 'extkindeditor',
		    	anchor:'-110',
		    	height:300,
		    	fieldLabel:'关于我们',
				name:'ABOUT'
			}],
			buttons:[{
				text:'保存',
				formBind:true,
				disabled:true,
				handler:function(btn){
					var form = btn.up('form'),
						f = form.getForm();
					if(f.isValid()){
						f.submit({
							submitEmptyText:false ,
							url:cfg.getCtx()+'/site/company/save/wap',
			                success: function(form, action) {
			                	util.success('网站设置成功');
			                	util.redirectTo('b2c/setting');
			                },
			                failure: function(form, action) {
			                	var state = action.result?action.result.statusCode:0,
			                		errors = ['网站设置异常'];
			                    util.error(errors[0-parseInt(state)]);
			                }
						});
					}
				}
			}]
	}
});