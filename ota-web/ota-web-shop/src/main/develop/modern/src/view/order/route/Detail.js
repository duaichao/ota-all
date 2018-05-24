Ext.define('Weidian.view.order.route.Detail', {
    extend: 'Weidian.view.Base',
    xtype:'orderdetail',
	config:{
		title:'订单详情',
		userCls:'D_order_detail',
		controller:'user',
		viewModel: {
			data:{
				START_CITY:'西安',
				STATUS:null
			}
		},
		backHide:false,
		expandItems: [{
			itemId:'moreOperate',
			align:'right',
			ripple:false,
			iconCls:'x-fm fm-more f15',
			handler:'moreMenus',
			actions:[{
				iconCls:'x-fm fm-finish fcorange',
				text:'去付款',
				call:'goPayOrder'
			},{
				iconCls:'x-fm fm-close',
				text:'取消订单',
				call:'cancelOrder'
			}]
		}]
	},
    initialize: function(){
    	this.initViews();
    	this.callParent();
    },
    initViews:function(){
    	var me=this,items = [],
    		vm = this.getViewModel();
    	items.push({
    		xtype:'panel',
    		//title:'详情',
			userCls:'block bbor tbor',
			bind:{
				html:[
					'<div class="no clf bbor">订单号：{NO}<span style="float:right;" class="fcgreen">{STATUS_TITLE}</span></div>',
					'<div class="title fcblue f16">{PRODUCE_NAME}</div>',
					'<div class="item">',
						'<span>订单金额：</span><span class="fcorange1 f18"><span class="f12">￥</span>{SALE_AMOUNT}</span>',
					'</div>',
					'<div class="item">',
						'<span>出发城市：</span>{START_CITY}',
					'</div>',
					'<div class="item">',
						'<span>出发日期：</span>{STARTDATE}',
					'</div>',
					'<div class="item">',
						'<span>归来日期：</span>{BACKDATE}',
					'</div>',
					'<div class="item">',
						'<span>出游人数：</span>{MAN_COUNT}成人，{CHILD_COUNT}儿童 ',
					'</div>'
				].join('')
			}
		});
    	items.push({
    		xtype:'panel',
    		title:'大交通',
    		userCls:'block bbor tbor',
    		tpl:[
    		    '<tpl for=".">',
				'<div class="item">',
				'<span>{ORDER_BY:this.getText()}：</span>{EXCA}  &nbsp;&nbsp;{BEGIN_CITY_NAME} - {END_CITY_NAME} ({PRODUCE_NAME})',
				'</div>',
				'</tpl>',
				{
    		    	getText:function(order){
    		    		if(order==1){
    		    			return '去';
    		    		}
    		    		if(order>1){
    		    			return '返';
    		    		}
    		    	}
				}
    		],
			bind:{
				data:'{traffics}'
			}
		});
    	items.push({
    		xtype:'panel',
    		title:'单房差',
    		userCls:'block bbor tbor',
			bind:{
				html:[
					'<div class="item">',
						'<span>价格：</span><span class="fcorange1 f18"><span class="f12">￥</span>{RETAIL_SINGLE_ROOM}</span>',
					'</div>',
					'<div class="item">',
						'<span>个数：</span>{SINGLE_ROOM_CNT}',
					'</div>'
				].join('')
			}
		});
    	items.push({
    		xtype:'panel',
    		title:'游客信息',
    		userCls:'block bbor tbor',
			items:[{
				xtype:'container',
				tpl:[
				    '<div class="no">联系人信息</div>',
					'<div class="item">',
						'<span>姓名：</span>{CHINA_NAME}',
					'</div>',
					'<div class="item">',
						'<span>手机：</span>{MOBILE}',
					'</div>',
					'<div class="no tbor" style="padding-top:10px;margin-top:10px;">出游人信息</div>'
				],
				bind:{
					data:'{contact}'
				}
			},{
				xtype:'list',
				userCls:'D_visitor_list',
				itemCls:'D_visitor_list_item',
				itemTpl:[
						'<p class="item">',
							'<span>{NAME} </span>',
							'<span class="f12 fcgray">{[this.attrSimple(values.ATTR_NAME)]}</span>',
						'</p>',
						'<tpl if="!Ext.isEmpty(values.CARD_NO)">',
						'<p class="f12 fcgray">',
							'{CARD_TYPE}：{CARD_NO}',
						'</p>',
						'</tpl>',
						{
							attrSimple:function(v){
								var str = {
										'成人':'成人',
										'不占床位/不含门票':'儿童',
										'不占床位/含门票':'儿童 ',
										'占床位/不含门票':'儿童 ',
										'占床位/含门票':'儿童',
										'婴儿':'婴儿'
								};
								return str[v];
							}
						}
				],
				bind:{
					data:'{visitors}'
				}
			}]
		});
    	this.setItems(items);
    },
    updateSubmitParams:function(params){
    	if(params&&params.id){
    		var me=this,orderId = params.id;
    		util.request(cfg.url.order.detail,{orderId:orderId},function(data){
    			data.contact = data.contacts[0];
		        me.getViewModel().setData(data);
		        if(data.STATUS!=0){
		        	//me.down('toolbar#tools').hide();
		        	cfg.view.getNavigationBar().down('button#moreOperate').disable();
		        }else{
		        	//me.down('toolbar#tools').show();
		        	cfg.view.getNavigationBar().down('button#moreOperate').enable();
		        }
	        });
    	}
    },
    goPayOrder:function(){
    	var vm = this.getViewModel();
    	this.setSubmitParams({
			orderId:vm.get('ID'),
			manCount:vm.get('MAN_COUNT'),
			childCount:vm.get('CHILD_COUNT'),
			title:vm.get('PRODUCE_NAME'),
			startDate:vm.get('STARTDATE'),
			totalAmount:vm.get('SALE_AMOUNT')
		});
		this.getController().redirectTo('pay/center/'+vm.get('ID'));
    },
    cancelOrder:function(){
    	var vm = this.getViewModel();
    	util.request(cfg.url.order.cancel,{orderId:vm.get('ID')},function(){
			this.getController().redirectTo('me/order/list');
		},this);
    }
});