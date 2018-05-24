Ext.define('Weidian.view.authentication.GetCode', {
    extend: 'Weidian.view.authentication.AuthBase',
    xtype: 'getcode',
	config: {
		title:'注册2/3',
		showMainPoint:false,
		backHide:false,
		controller:'auth',
		viewModel:true
	},
	initialize: function(){
    	this.initViews();
    	this.callParent();
    	var me = this;
    	console.log(this.getSubmitParams());
    	if(!this.getSubmitParams().phoneNumber){
			setTimeout(function(){
				me.getController().redirectTo('register');
			},200)
		}
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
    						disabled:'{!imgcode.value}'
    					}
    				}]
    			},{
    				margin:'10 0 0 0',
    				height:45,
    				xtype: 'button',
    				width: '100%',
    				text: '下一步，设置密码',
    				iconAlign: 'right',
    				iconCls: 'x-fa fa-angle-right',
    				ui: 'action',
    				handler: 'onNextPwdButtonTap',
    				listeners: {
    					scope: 'controller',
    					element: 'element'
    				},
    				disabled:true,
    				bind:{
    					disabled:'{!msgcode.value}'
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
    		submitDatas = form.getValues(),
    		sParams = this.getSubmitParams();
    	if(!sParams.phoneNumber||sParams.phoneNumber==''){
    		Ext.toast('未填写手机号码');
    		btn.enable();
    	}else{
    		submitDatas.phoneNumber = sParams.phoneNumber;
    		util.request(cfg.url.getPhoneCode,submitDatas,function(result){
    			if(result.success){
    				var t = 60,inter;
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
    				Ext.toast('验证码错误');
    				btn.enable();
    			}
    			this.down('image#imageCode').setSrc(Ext.domain+'/member/imageCaptcha?_dc='+Ext.ticks());
    		},this);
    		
    		
    	}
    }
});
