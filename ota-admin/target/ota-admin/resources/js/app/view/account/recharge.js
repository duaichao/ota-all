Ext.define('app.view.account.recharge',{ 
	extend: 'Ext.container.Viewport',
    layout: 'border',
	items:[{
    	region:'center',
    	autoScroll:true,
    	xtype:'form',
    	itemId:'payForm',
    	border:false,
	    layout:{
			type: 'vbox',
		    pack: 'start',
		    align: 'stretch'
		},
	    items:[{
	    	height:40,
	    	bodyPadding:10,
	    	html:'<h3 class="bold" style="margin-top:0px;">充值编号：'+rechargeNo+'，您需要支付：'+util.moneyFormat(money,'f18')+'</h3>'
	    },{
	    	flex:1,
	    	margin:1,
	    	plain: true,
	    	border:true,
	    	xtype:'tabpanel',
	    	deferredRender:false,
	    	defaults:{
	    		bodyPadding:10,
	    		tpl:[
	    			'<ul class="bank-list">',
	    				'<tpl for=".">',
	    				'<tpl if="checked==true">',
	    				'<li class="bank focus">',
	    					'<input type="radio" value="{value}" name="payment" class="f-radio" checked="true">',
	    				'<tpl else>',
	    				'<li class="bank">',
	    					'<input type="radio" value="{value}" name="payment" class="f-radio">',
	    				'</tpl>',
	    					'<label>',
			                    '<i class="iconfont f20" style="color:{color}"  >{font}</i>',
			                    ' <span> {name}</span>',
			                '</label>',
			                '<i class="iconfont check">&#xe62a;</i>',
	    				'</li>',
	    				'</tpl>',
	    			'</ul>'
	    		]
	    	},
	    	items:[{
	    		title:'支付平台',
	    		data:[{
	    			name:'支付宝',
	    			checked:true,
	    			value:'1',
	    			font:'&#xe625;',
	    			color:'#f60'
	    		}/*,{
	    			name:'财付通',
	    			value:'2',
	    			font:'&#xe614;',
	    			color:'#ff7500'
	    		},{
	    			name:'微信支付',
	    			value:'3',
	    			font:'&#xe613;',
	    			color:'#02bb00'
	    		}*/]
	    	},{
	    		title:'网银支付',
	    		data:[{
	    			name:'招商银行',
	    			value:'CMB',
	    			font:'&#xe621;',
	    			color:'#da150f'
	    		},{
	    			name:'交通银行',
	    			value:'COMM',
	    			font:'&#xe61a;',
	    			color:'#041f9b'
	    		},{
	    			name:'建设银行',
	    			value:'CCB',
	    			font:'&#xe619;',
	    			color:'#0255b9'
	    		},{
	    			name:'工商银行',
	    			value:'ABC',
	    			font:'&#xe617;',
	    			color:'#fe0101'
	    		},{
	    			name:'农业银行',
	    			value:'ABC',
	    			font:'&#xe61c;',
	    			color:'#1e9256'
	    		},{
	    			name:'中国银行',
	    			value:'BOC-DEBIT',
	    			font:'&#xe623;',
	    			color:'#a71e32'
	    		},{
	    			name:'上海浦发银行',
	    			value:'SPDB',
	    			font:'&#xe61e;',
	    			color:'#20699f'
	    		},{
	    			name:'光大银行',
	    			value:'CEB-DEBIT',
	    			//font:'&#xe61e;',
	    			color:'#e5a700'
	    		},{
	    			name:'民生银行',
	    			value:'CMBC',
	    			//font:'&#xe61e;',
	    			color:'#0072bc'
	    		},{
	    			name:'广东发展银行',
	    			value:'GDB',
	    			//font:'&#xe61e;',
	    			color:'#b60012'
	    		},{
	    			name:'兴业银行',
	    			value:'CIB',
	    			font:'&#xe61f;',
	    			color:'#004192'
	    		},{
	    			name:'平安银行',
	    			value:'SPABANK',
	    			font:'&#xe626;',
	    			color:'#ef5107'
	    		},{
	    			name:'北京银行',
	    			value:'BJBANK',
	    			font:'&#xe616;',
	    			color:'#cb030d'
	    		},{
	    			name:'邮政银行',
	    			value:'POSTGC',
	    			font:'&#xe620;',
	    			color:'#018263'
	    		},{
	    			name:'上海银行',
	    			value:'SHBANK',
	    			font:'&#xe627;',
	    			color:'#ffb400'
	    		},{
	    			name:'杭州银行',
	    			value:'HZCBB2C',
	    			//font:'&#xe627;',
	    			color:'#008ed8'
	    		}]
	    	},{
	    		title:'信用卡快捷支付',
	    		disabled:true
	    	}]
	    }],
	    buttons:['->',{
    		itemId:'payBtn',
    		cls:'orange',
    		text:'付款'
    	},{
    		itemId:'backBtn',
    		cls:'disable',
    		text:'返回'
    	}]
    }],
	initComponent: function() {
        this.callParent();
	}
});