Ext.define('Weidian.view.authentication.Login', {
    extend: 'Weidian.view.authentication.AuthBase',
    xtype: 'login',
	config: {
		userCls: 'auth-login',
		controller:'auth',
		title:'登录',
		/*expandItems:[{
			xtype: 'segmentedbutton',
			value:0,
			align:'right',
			allowDepress: false,
			items: [{
				text: '账号'
			},
			{
				text: '短信'
			}],listeners:{change:function(sg,n){
				sg.up('navigationview').getActiveItem().getViewModel().set('hide',n==1);
			}}
		}],*/
		showMainPoint:false,
		backHide:false,
		viewModel: {
	        type: 'default',
	        data: {
	            hide:false
	        }
	    }
	},
    initialize: function(){
    	this.getRsa();
    	this.initViews();
    	this.callParent();
    },
    initViews:function(){
    	var items = [{
    		xtype:'typetab',
    		items:[{
    			title:'密码登录'
    		},{
    			title:'动态密码登录'
    		}]
    	},{
    		xtype:'formpanel',
            padding:20,

            items: [{
                xtype:'container',
                defaults: {
                    margin:'0 0 10 0'
                },
    			items: [{
    				xtype: 'textfield',
    				minWidth: 300,
    				placeHolder: '手机号码',
    				ui: 'light',
    				name:'userName',
    				bind: {
    					hidden: '{hide}'
    				}
    			},{
    				xtype: 'passwordfield',
    				placeHolder: '密码',
    				name:'passWord',
    				ui: 'light',
    				bind: {
    					hidden: '{hide}'
    				}
    			},{
    				xtype: 'numberfield',
    				minWidth: 300,
    				placeHolder: '手机号码',
    				name:'phoneNumber',
    				ui: 'light',
    				bind: {
    					hidden: '{!hide}'
    				}
    			},{
    				layout: 'hbox',
    				padding:'0 0 12 0',
    				items: [{
    					xtype: 'textfield',
    					name:'phoneCode',
    					placeHolder: '短信验证码',
    					ui: 'light',
    					flex:1,
    					margin:'0 10 0 0'
    				},{
    					width:100,
    					ui:'get-code',
    					xtype:'button',
    					itemId:'phoneCodeBtn',
    					text:'获取验证码'
    				}],
    				bind: {
    					hidden: '{!hide}'
    				}
    			},{
    				layout: 'hbox',
    				
    				items: [{
    					xtype: 'checkboxfield',
    					checked:true
    				},{
    					html: '记住我'
    					
    				},{
    					style: 'textAlign:right',
    					flex:'1',
    					html: '<a href="#passwordreset">忘记密码？</a>'
    				}]
    			},{
    				margin:'10 0 10 0',
    				itemId:'loginBtn',
    				xtype: 'button',
    				text: '登录',
    				height:45,
    				width: '100%',
    				iconAlign: 'right',
    				iconCls: 'x-fa fa-angle-right',
    				ui: 'action'
    			},{
    				xtype: 'button',
    				height:45,
    				itemId:'registBtn',
    				width: '100%',
    				text: '注册',
    				ui: 'soft-green',
    				iconAlign: 'right',
    				iconCls: 'x-fm fm-plus'
    			}]
    		}]
    	}];
    	this.setItems(items);
    	this.down('button#phoneCodeBtn').on('tap',this.getPhoneCode,this);
    	this.down('button#loginBtn').on('tap',this.login,this);
    	this.down('button#registBtn').on('tap',this.regist,this);
    },
    /**
     * 获取登录密码 key
     * @param 
     */
    getRsa:function(){
    	var me = this;
    	if(!cfg.rsa){
    		util.request(cfg.url.getRsa,{noLoader:true},function(keyResult){
    			var em = keyResult.message.split('=');
    			if(em.length==2){
    				cfg.rsa = {};
    				cfg.rsa.m = em[0];
    				cfg.rsa.e = em[1];
    			}
    		});
    	}
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
    		util.request(cfg.url.getPhoneCode,submitDatas);
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
    		
    	}
    },
    /**
     * 验证登录
     * @param btn
     */
    login:function(btn){
    	btn.disable();
    	var me = this,loginType = this.down('typetab').getActiveTab().getTitle(),
    		form = this.down('formpanel'),
    		submitDatas = form.getValues(),
    		passWordKey,oldPassWord = submitDatas.passWord;
    	if(loginType=='密码登录'){
    		setMaxDigits(130);
    		
    		if(!submitDatas.userName||submitDatas.userName==''||!submitDatas.passWord||submitDatas.passWord==''){
    			Ext.toast('未填写手机或密码',cfg.toastTimeout);
    			btn.enable();
    			return;
    		}
    		passWordKey = encryptedString(new RSAKeyPair(cfg.rsa.e,"",cfg.rsa.m), encodeURIComponent(submitDatas.passWord));
    		submitDatas.passWord = passWordKey;
    		submitDatas.loginType = 0;
    	}else{
    		submitDatas.loginType = 1;
    	}
    	submitDatas.noLoader = false;
    	//登录
    	util.request(cfg.url.login,submitDatas,function(user){
    		if(user.message){
    			btn.enable();
    			Ext.toast(user.message,cfg.toastTimeout);
    		}else{
    			btn.enable();
    			user.PASSWORD = oldPassWord;
    			localStorage.setItem('loggedIn',Ext.encode(user));
    			cfg.user = user;
    			//哪儿过来的回哪儿去
    			if(this.getSubmitParams().redirectUrl){
    				this.getController().redirectTo(this.getSubmitParams().redirectUrl);
    			}else{
    				this.getController().redirectTo('ucenter');
    			}
    			var me = this;
    			setTimeout(function(){cfg.view.remove(me);},500);
    		}
    	},this);
    },
    regist:function(btn){
    	this.getController().redirectTo('register');
    }
});
