Ext.define("Weidian.view.pay.Center", {
	extend: 'Weidian.view.Base',
	alternateClassName: 'paycenter',
    xtype:'paycenter',
	config:{
		controller:'orderroute',
		userCls:'D_pay',
		title:'支付',
		expandItems: [{
			itemId:'moreOperate',
			align:'right',
			ripple:false,
			iconCls:'x-fm fm-more f15',
			handler:'moreMenus',
			actions:[{
				iconCls:'x-fm fm-account-circle fcblue',
				text:'会员中心',
				call:'userCenter'
			},{
				iconCls:'x-fm fm-phone fcorange f18',
				text:'联系我们',
				call:'contactUs'
			},{
				iconCls:'x-fm fm-home',
				text:'返回首页',
				call:'backHome'
			}]
		}]
	},
    initialize: function(){
    	this.callParent();
    	if(this.getInitStatus()){
    		this.initViews();
    	}
    },
    initViews:function(){
    	var items = [];
    	items.push({
    		title:'订单信息',
    		//height:146,
    		userCls:'D_fieldset',
    		xtype:'fieldset',
    		margin:'0 0 10 0',
    		items:[{
    			xtype:'container',
    			userCls:'D_pay_info',
    			tpl:new Ext.XTemplate(
    					'<h3 class="font16 blue-text">{title}</h3>',
    					'<p>',
    					'出发日期：<label>{startDate}</label>',
    					'</p>',
    					'<p>',
    					'游客人数：<label>{manCount}成人,{childCount}儿童</label>',
    					'</p>',
    					'<p>',
    					'订单金额：<span class="fcorange1 f18"><span class="f14">￥</span>{totalAmount}</span>',
    					'</p>'
    			),
    			data:this.getSubmitParams()
    		}]
    	});
    	items.push({
    		instructions:'选择支付方式，您需要支付：<span class="fcorange1 f18"><span class="f14">￥</span>'+(this.getSubmitParams().totalAmount)+'</span>',
    		xtype:'fieldset',
    		userCls:'D_fieldset',
    		layout:'fit',
    		title:'支付方式',
    		items:[{
    			itemId:'payway',
        		xtype:'list',
        		scrollable:false,
        		onItemDisclosure:true,
        		userCls: 'type-data-view',
    			margin:'10 0 0 0',
    			itemCls:'type-data-item pay-item',
        		itemTpl:new Ext.XTemplate(
        				'<svg data-app="{type}" class="svg-icon" aria-hidden="true"><use xlink:href="#{icon}"></use></svg>'
    			),
    			data:[{
    				icon:'icon-zhifubao',
    				type:'alipay',
    				text:'支付宝'
    			}/*,{
    				icon:'icon-weixinzhifu',
    				type:'weixin',
    				text:'微信支付'
    			}*/]
        	}]
    	});
    	this.setItems(items);
    	this.down('list#payway').on('itemtap',this.pay,this);
    },
    pay:function(dv, index , target , record){
    	var me = this;
    	if(record.get('type')=='alipay'){
    		//如果微信中打开
    		if(util.getAllPlatformsInfo().isWeixinApp){
    			Ext.Msg.alert('提示', '抱歉，微信内置浏览器不支持支付宝支付，请在其他浏览器中打开   <a href="javascript:void(0);" class="fcblue">'+Ext.shopInfo.DOMAIN+'  </a>   进入会员我的订单进行支付', Ext.emptyFn);
        		return;
    		}
    		
    		var payWindow = Ext.create('Ext.Panel',{
        		title:record.get('text'),
        		closable:true,
        		padding:10,
        		centered:true,
        		floated:true,
        		width:'100%',
        		height:'100%',
        		layout:'fit',
        		items:[{
        			html:'<iframe name="payIframe" id="payIframe" width="100%" height="'+Ext.Element.getViewportHeight()+'px" frameborder=0 src="'+Ext.domain+cfg.url.order.pay+'?orderId='+this.getSubmitParams().orderId+'&appName=minishop"></iframe>'
        		}],
        		listeners:{
        			destroy :function(){
        				util.goHome('支付成功，请到会员中心查看订单');
        			}
        		}
        	});
        	Ext.Viewport.add(payWindow);
        	payWindow.show();
    	}else if(record.get('type')=='weixin'){
    		Ext.toast('支付接口正在接洽中，请使用支付宝支付',cfg.toastTimeout);
    	}
    	
    },
    userCenter:function(){
    	this.getController().redirectTo('ucenter');
    },
    contactUs:function(){
    	cfg.view.initPluginPlusDialog(null,'contcat');
    },
    backHome:function(){
    	cfg.view.pop();
		window.location.hash = 'home';
    }
});