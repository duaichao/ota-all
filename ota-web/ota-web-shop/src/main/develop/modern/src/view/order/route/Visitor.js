Ext.define('Weidian.view.order.route.Visitor', {
    extend: 'Weidian.view.Base',
    xtype: 'ordervisitor',
    config:{
    	controller:'orderroute',
    	cls: 'D_order_visitor D_label',
    	title:'2/3填写订单',
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
    userCenter:function(){
    	this.getController().redirectTo('ucenter');
    },
    initialize: function(){
    	this.callParent();
    	if(this.getInitStatus()){
    		this.initViews();
    	}
    },
    initViews:function(){
    	var me=this,
    		items = [],
    		sParams = this.getSubmitParams();
    	this.setViewModel({
			type:'default',
			data:{
				manPrice:sParams.manPrice,
				manCount:sParams.manCount,
				childPrice:sParams.childPrice,
				childCount:sParams.childCount,
				singleRoomCount:sParams.singleRoomCount,
				singleRoomPrice:sParams.singleRoomPrice,
				totalAmount:sParams.totalAmount
			}
		});
    	items.push({
	   		 xtype: 'fieldset',
	   		 scrollable:false,
	   		 height:128,
	   		 userCls:'D_fieldset',
	   		 padding:'0 10',
             title: '联系人',
             defaults:{
     			//labelAlign: 'placeholder'
            	 labelAlign: 'left',
            	 labelWidth:60,
            	 labelTextAlign:'left'
     		},
     		items:[{
         		xtype:'textfield',
         		itemId:'contactName',
         		placeHolder:'姓名'
     		},{
         		xtype:'numberfield',
         		itemId:'contactPhone',
         		placeHolder:'手机'
     		}/*,{
         		xtype:'emailfield',
         		label:'邮箱'
     		}*/]
    	});
    	this.visitorList = Ext.create('Weidian.view.order.visitor.List',{
    		 itemCls:'D_visitor_list_item bbor',
    		 scrollable:false,
    		 autoLoad:false
    	});
    	this.visitorList.on('itemtap',this.onVisitorItemTap,this);
    	items.push({
    		 layout:'fit',
    		 scrollable:false,
    		 xtype: 'fieldset',
    		 margin:'10 0 10 0',
    		 userCls:'D_fieldset',
    		 bind:{
    			 title: '出游人<span class="title"><b>{manCount}</b>成人，<b>{childCount}</b>儿童</span><a href="#visitor/contacts/'+sParams.id+'" class="btn">常用游客 <span class="x-fa fa-angle-right"></span></a>'
    		 },
             instructions: '因信息不完整、填写不正确造成的额外损失、保险拒赔等问题，我司不承担相应责任，需由客人自行承担',
             items:[this.visitorList]
    	});
    	items.push({
    		xtype: 'checkboxfield',
    		checked:true,
    		userCls:'bgwhite',
    		padding:'10 0',
    		labelWidth:'89%',
    		labelAlign:'right',
            label: '<div class="f14" style="padding-right:10px;">我已阅读并接受<a class="fcblue">预订须知、合同、保险等条款</a></div>'
    	});
    	
    	items.push({
    		xtype:'toolbar',
			userCls:'mini-tools tbor',
			docked:'bottom',
			items:[{
				xtype:'label',
				userCls:'fcgray f14 fthin',
				padding:'0 0 0 10',
				bind:{
					html:'总价：<span class="fcorange1 f18"><span class="f14">￥</span>{totalAmount}</span>'
				}
			},{xtype:'spacer'},{
				xtype:'label',
				html:'<a class="fcblue f14 fthin" style="margin-right:10px;">费用详情</a>',
				listeners:{
					click: this.showAmountDetail,
                    element: 'element',
                    delegate:'.fcblue',
                    scope:this
				}
			},{
	    		xtype:'button',
	    		height:42,
	    		width:90,
	    		ui:'pay-orange',
	        	text:'提交订单',
	        	handler: 'gotoPayCenter',
				listeners: {
					scope: 'controller',
					element: 'element'
				}
    		}]
    	});
    	this.setItems(items);
    	this.initVisitors();
    },
    initVisitors:function(){
    	var visitorDatas = [],
			sParams = this.getSubmitParams();
    	for(var i=0;i<(sParams.manCount+sParams.childCount);i++){
    		var visitorModel = Ext.create('Weidian.model.order.visitor.Model',{
    			NAME:'游客'+(i+1)
    		});
    		visitorDatas.push(visitorModel);
    	}
    	this.visitorList.setData(visitorDatas);
    },
    onVisitorItemTap:function(){
    	this.getController().redirectTo('visitor/contacts/'+this.getSubmitParams().id);
    },
    showAmountDetail:function(a,t){
    	var me = this,dialog = Ext.Viewport.down('panel#amountDialog');
    	if(dialog){
    		dialog.hide();
    		return;
    	}
    	dialog = Ext.Viewport.add({
    		xtype:'panel',
    		itemId:'amountDialog',
    		userCls:'tip-down-wrap D_dialog D_Amount_dialog',
    		modal:true,
    		hideOnMaskTap: true,
    		floated:true,
    		bottom:43,
    		width: '100%',
    		tools:[{
				style:{color:'#9e9e9e'},
				type:'close',
				handler:function(){dialog.hide();}
			}],
			listeners:{
				hide:function(g){
					Ext.Viewport.remove(g);
				}
			},
    		header: {
				title: '费用详情'
			},
    		tpl:[
				'<li>',
				'<span class="title">单房差</span>',
				'<span class="price">',
				'<b>{singleRoomCount} ×</b>￥{singleRoomPrice}',
				'</span>',
				'<span>￥{[values.singleRoomCount*values.singleRoomPrice]}</span>',
				'</li>',
				'<li>',
				'<span class="title">成人</span>',
				'<span class="price">',
				'<b>{manCount} ×</b>￥{manPrice}',
				'</span>',
				'<span>￥{[values.manCount*values.manPrice]}</span>',
				'</li>',
				'<li>',
				'<span class="title">儿童</span>',
				'<span class="price">',
				'<b>{childCount} ×</b>￥{childPrice}',
				'</span>',
				'<span>￥{[values.childCount*values.childPrice]}</span>',
				'</li>',
				'<li class="tbor">',
				'<span class="title">总计</span>',
				'<span class="price">',
				'</span>',
				'<span class="fbold fcorange1">￥{totalAmount}</span>',
				'</li>'
    		],
    		data:this.getViewModel().data
	    });
    	dialog.show();
    },
    contactUs:function(){
    	cfg.view.initPluginPlusDialog(null,'contcat');
    },
    backHome:function(){
    	cfg.view.pop();
		window.location.hash = 'home';
    }
});
    