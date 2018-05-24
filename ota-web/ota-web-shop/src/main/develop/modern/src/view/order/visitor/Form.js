Ext.define('Weidian.view.order.visitor.Form', {
	extend: 'Weidian.view.Base',
	xtype:'visitorsave',
	config:{
		controller:'user',
		title:'新增常用游客',
		padding:'0 10',
		expandItems: [{
			align:'right',
			cls:'f14',
			text:'保存',
			handler: 'saveVisitor'
		}]
	},
	initialize: function(){
		/*if(Ext.isEmpty(this.getSubmitParams().routeId)){
    		Ext.toast('无效的请求',cfg.toastTimeout);
    		setTimeout(function(){
    			cfg.view.pop();
    			window.location.hash = 'home';
    		},1000)
    		return;
    	}*/
    	this.initViews();
    	this.callParent();
    },
    initViews:function(){
    	var items = [],
    		formItems = [];
    	/*formItems.push({
			xtype: 'hiddenfield',
			bind:{
				value:'{ID}'
			},
            name: 'ID'
		});
    	formItems.push({
			xtype: 'hiddenfield',
			bind:{value:'{ORDER_ID}'},
            name: 'ORDER_ID'
		});
    	formItems.push({
			xtype: 'hiddenfield',
			bind:{value:'{MEMBER_ID}'},
            name: 'MEMBER_ID'
		});*/
    	formItems.push({
    		required: true,
			xtype: 'textfield',
			label:'姓名',
			placeHolder: '必填',
			bind:{
				value:'{NAME}'
			},
            name: 'NAME'
    	});
    	formItems.push({
    		required: true,
			xtype: 'selectfield',
			label:'游客类型',
			placeHolder: '必填',
			name:'ATTR_NAME',
			bind:{
				value:'{ATTR_NAME}'
			},
			options:[
                {value:'成人', text:'成人'},
                {value:'儿童', text:'儿童'}
				/*{value:'不占床位/不含门票', text:'不占床位/不含门票'},
				{value:'不占床位/含门票', text:'不占床位/含门票'},
				{value:'占床位/不含门票', text:'占床位/不含门票'},
				{value:'占床位/含门票', text:'占床位/含门票'},
				{value:'婴儿', text:'婴儿'}*/
			]
    	});
    	formItems.push({
			label:'手机',
			xtype: 'numberfield',
			placeHolder: '选填',
			name:'MOBILE',
			bind:{
				value:'{MOBILE}'
			}
    	});
    	/*formItems.push({
			label:'邮箱',
			xtype: 'emailfield',
			placeHolder: '选填'
    	});*/
    	formItems.push({
    		required: true,
    		xtype:'selectfield',
			label: '证件类型',
			value:'身份证',
			name:'CARD_TYPE',
			options:[
			    {text: '身份证',value: '身份证'}/*,
			    {text: '护照',value: '1'},
			    {text: '军官证',value: '2'},
			    {text: '其他',value: '3'}*/
			],
			bind:{
				value:'{CARD_TYPE}'
			}
    	});
    	formItems.push({
    		required: true,
			xtype: 'textfield',
			label:'证件号码',
			placeHolder: '必填',
			name:'CARD_NO',
			bind:{
				value:'{CARD_NO}'
			}
    	});
    	var sParams = this.getSubmitParams();
    	this.formView = Ext.create('Ext.form.Panel',{
    		viewModel:{
    			type:'default',
    			data:sParams.visitor||{ID:'',ORDER_ID:'',MEMBER_ID:'',NAME:'',MOBILE:'',CARD_TYPE:'身份证',CARD_NO:'',EMAIL:'',ATTR_ID:'',ATTR_NAME:''}
    		},
    		items:formItems
    	});
    	items.push(this.formView);
    	this.setItems(items);
    }
});