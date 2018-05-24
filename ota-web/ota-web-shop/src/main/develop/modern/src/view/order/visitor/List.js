Ext.define('Weidian.view.order.visitor.List', {
    extend: 'Ext.List',
    xtype: 'visitorlist',
    config:{
    	userCls:'D_visitor_list',
    	onItemDisclosure:true,
    	mode:'MULTI',
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
								'儿童':'儿童'/*,
								'不占床位/不含门票':'儿童',
								'不占床位/含门票':'儿童 ',
								'占床位/不含门票':'儿童 ',
								'占床位/含门票':'儿童',
								'婴儿':'婴儿'*/
						};
						return str[v];
					}
				}
		],
		itemCls:'D_visitor_list_item bbor check',
		deferEmptyText:false,
		emptyText:'请添加游客',
		dynamicParams:null,
		autoLoad:true,
		scrollable:'y',
		store:null,
		scrollToTopOnRefresh:false,
		plugins: [{
	        xclass: 'Weidian.view.ux.dataview.ListSwipeAction',
	        buttons:[{
	        	xtype:'button',
	    		userCls:'simple',
	    		ui:'gray-button',
	        	text:'修改',
	            color: 'white',
	            handler:function(rs,list,btn){
	            	btn.disable();
	            	var topView = list.up('visitorcontacts');
	            	topView.setSubmitParams({visitor:rs.data});
	            	topView.getController().redirectTo('visitor/save/orderid');
	            	btn.enable();
	            }
	        },{
	        	xtype:'button',
	        	userCls:'simple',
	    		ui:'red',
	        	text:'删除',
	        	color: 'white',
	        	closeOnHandler:true,
	        	handler:function(res,list,btn){
	        		btn.disable();
	            	util.request(cfg.url.order.visitor.del,{
	            		id:res.get('ID')
	            	},function(rs){
	            		btn.enable();
	            		if(rs.success){
	            			list.getStore().loadPage(1);
	            		}else{
	            			Ext.toast('删除游客失败！',cfg.toastTimeout);
	            		}
	            	},this);
	            }
	        }]
	    },{
			xclass: 'Ext.plugin.ListPaging'
		}]
	},
	initialize: function(){
		var dparams = this.getDynamicParams()||{},
			store = util.createStore(dparams.url||cfg.url.order.visitor.list,this.getAutoLoad(),200);
		store.setModel(Ext.create('Weidian.model.order.visitor.Model'));
		if(dparams){
			store.getProxy().setExtraParams(dparams);
		}
		this.setStore(store);
		this.callParent();
	}
});
    
    