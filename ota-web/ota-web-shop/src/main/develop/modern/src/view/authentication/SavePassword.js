Ext.define('Weidian.view.authentication.SavePassword', {
    extend: 'Weidian.view.authentication.AuthBase',
    xtype: 'savepassword',
	config: {
		title:'重置密码',
		showMainPoint:false,
		backHide:false,
		controller:'auth',
		viewModel:true
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
    				minWidth:300,
    				reference:'password',
    				xtype: 'passwordfield',
    				revealable: true,
    				placeHolder: '请输入新密码',
    				name : 'password',
    				clearIcon: false
    			},{
    				minWidth:300,
    				reference:'passwordagin',
    				xtype: 'passwordfield',
    				revealable: true,
    				placeHolder: '请再次输入密码',
    				name : 'passwordagin',
    				clearIcon: false
    			},{
    				margin:'10 0 0 0',
    				height:45,
    				xtype: 'button',
    				width: '100%',
    				text: '重置密码',
    				iconAlign: 'right',
    				iconCls: 'x-fm fm-finish',
    				ui: 'action',
    				handler: 'onResetPwdButtonTap',
    				listeners: {
    					scope: 'controller',
    					element: 'element'
    				},
    				disabled:true,
    				bind:{
    					disabled:'{!password.value}'
    				}
    			}]
    		}]
    	});
    }
});
