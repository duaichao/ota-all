Ext.define('Weidian.view.authentication.EditPassword', {
    extend: 'Weidian.view.authentication.AuthBase',
    xtype: 'editpassword',
	config: {
		title:'修改密码',
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
    				placeHolder: '密码，6-16位数字或字母',
    				name : 'passWord',
    				clearIcon: false
    			},{
    				margin:'10 0 0 0',
    				height:45,
    				xtype: 'button',
    				width: '100%',
    				text: '确定',
    				iconAlign: 'right',
    				iconCls: 'x-fm fm-finish',
    				ui: 'action',
    				handler: 'onEditPasswordButtonTap',
    				disabled:true,
    				bind:{
    					disabled:'{!password.value}'
    				}
    			}]
    		}]
    	});
    	this.setItems(items);
    }
});
