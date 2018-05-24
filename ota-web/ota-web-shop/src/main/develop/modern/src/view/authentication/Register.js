Ext.define('Weidian.view.authentication.Register', {
    extend: 'Weidian.view.authentication.AuthBase',
    xtype: 'register',
	config: {
		title:'注册',
		showMainPoint:false,
		controller:'auth',
		viewModel:true,
		backHide:false/*,
		expandItems:[{
			align:'right',
			iconCls:'x-fm fm-home',
			handler: 'onHomeButtonTap',
			listeners: {
				scope: 'controller',
				element: 'element'
			}
		}]*/
	},
	initialize: function(){
		this.initViews();
    	this.callParent();
    },
    initViews:function(){
    	var items = [];
    	items.push({
    		xtype:'formpanel',
            padding:20,

            items: [{
                xtype:'container',
                defaults: {
                    margin:'0 0 10 0'
                },
    			items: [{
    				required: true,
    				minWidth: 300,
    				xtype: 'numberfield',
    				name:'phoneNumber',
    				reference:'phone',
    				placeHolder: '手机号码',
    				ui: 'light'
    			},{
    				layout: 'hbox',
    				minWidth:300,
    				items: [{
    					reference:'imgcode',
    					name:'imageCode',
    					xtype: 'textfield',
    					placeHolder: '图形验证码',
    					ui: 'light',
    					flex:1,
    					margin:'0 10 0 0'
    				},{
    					width:100,
    					height:30,
    					itemId:'imageCode',
    					xtype:'image',
    					src:Ext.domain+'/member/imageCaptcha',
    					listeners:{
    						tap:function(){
    							this.setSrc(Ext.domain+'/member/imageCaptcha?_dc='+Ext.ticks());
    						}
    					}
    				}]
    			},{
    				layout: 'hbox',
    				items: [{
    					reference:'msgcode',
    					xtype: 'textfield',
    					required: true,
    					name:'phoneCode',
    					placeHolder: '短信验证码',
    					ui: 'light',
    					flex:1,
    					margin:'0 10 0 0'
    				},{
    					width:100,
    					ui:'get-code',
    					itemId:'phoneCodeBtn',
    					xtype:'button',
    					text:'获取验证码',//60秒后重新发送 提示 动态口令已发送 15分钟内有效
    					disabled:true,
    					bind:{
    						disabled:'{!phone.value&&!imgcode.value}'
    					}
    				}]
    			},{
    				minWidth:300,
    				required: true,
    				reference:'password',
    				xtype: 'passwordfield',
    				revealable: true,
    				placeHolder: '密码，6-16位数字或字母',
    				name : 'passWord',
    				clearIcon: false
    			},{
    				xtype: 'button',
    				width: '100%',
    				height:45,
    				itemId:'next',
    				margin:'10 0 0 0',
    				text: '注册完成',
    				iconCls: 'x-fa fa-angle-right',
    				iconAlign: 'right',
    				ui: 'action',
    				handler: 'onFinistRegisitButtonTap',
    				listeners: {
    					scope: 'controller',
    					element: 'element'
    				},
    				disabled:true,
    				bind:{
    					disabled:'{!phone.value&&!msgcode.value&&!password.value}'
    				}
    			}]
    		}]
    	});
    	this.setItems(items);
    	this.down('button#phoneCodeBtn').on('tap',this.getPhoneCode,this);
    },
    /**
     * 发送短信
     * @param btn
     */
    getPhoneCode:function(btn){
    	btn.disable();
    	var form = this.down('formpanel'),
    		submitDatas = form.getValues();
    	if(!submitDatas.phoneNumber||submitDatas.phoneNumber==''){
    		Ext.toast('未填写手机号码',cfg.toastTimeout);
    		btn.enable();
    	}else{
    		util.request(cfg.url.getPhoneCode,submitDatas,function(result){
    			if(result.success){
    				var t = 60,inter;
    				btn.setText(t+'秒后重发');
            		inter = setInterval(function(){
            			if(t==1){
            				clearInterval(inter);
            				inter = null;
            				btn.enable();
            				btn.setText('获取验证码');
            				return;
            			}
            			t--;
            			btn.setText(t+'秒后重发');
            		},1000);
    			}else{
    				Ext.toast('验证码错误',cfg.toastTimeout);
    				btn.enable();
    			}
    			this.down('image#imageCode').setSrc(Ext.domain+'/member/imageCaptcha?_dc='+Ext.ticks());
    		},this);
    	}
    }
});
