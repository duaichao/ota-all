Ext.define('Weidian.view.order.visitor.Main', {
	extend: 'Weidian.view.Base',
	xtype:'visitorcontacts',
	config:{
		controller:'user',
		title:'常用游客',
		expandItems: [{
			itemId:'addVisitor',
			align:'right',
			ripple:false,
			margin:'0 0 0 8',
			iconCls:'x-fa fa-user-plus f12',
			handler: 'onAddVisitorBtnTap'
		}],
		viewModel:{
			type:'default',
			data:{
				isReadOnly:true,//isReadOnly来源 false订单 ,true会员
				selectManCount:0,
				selectChildCount:0,
				manCount:0,
				childCount:0
			}
		}
	},
	activeCallBack:function(main,old){
		var sParams = this.getSubmitParams();
		if(sParams&&sParams.id){
			this.getViewModel().set(Ext.applyIf(sParams,this.getViewModel().getData()));
		}
		if(old&&old.xtype=='ordervisitor'){
			this.getViewModel().set('isReadOnly',false);
			var list = this.down('visitorlist'),
				datas = old.visitorList.getData(),
				sels,i,j;
			list.getStore().on('load',function(s,rs){
				sels = [];
				for(j=0;j<rs.length;j++){
					for(i=0;i<datas.length;i++){
						if(datas[i].data.ID){
							if(datas[i].data.ID==rs[j].data.ID){
								sels.push(rs[j]);
							}
						}
					}
				}
				list.select(sels);
			});
		}else{
			this.getViewModel().set('isReadOnly',true);
		}
	},
    initialize: function(){
    	this.initViews();
    	this.callParent();
    },
    initViews:function(){
    	
    	var items = [],sParams = this.getSubmitParams(),
    		isReadOnly = Ext.isEmpty(sParams.id);
    	items.push({
    		xtype:'visitorlist',
    		userCls:'D_visitor_list single',
   		 	mode:isReadOnly?'SINGLE':'MULTI',
			 bind:{
				 onItemDisclosure:'{!isReadOnly}'
			 },
	   		 autoLoad:true
    	});
    	
    	items.push({
    		bind:{
    			hidden:'{isReadOnly}'
    		},
    		xtype:'toolbar',
			userCls:'mini-tools tbor',
			docked:'bottom',
			items:[{
				xtype:'label',
				padding:'5 0 0 10',
				userCls:'f14 fthin',
				bind:{
					html:'已选中：成人<b>{selectManCount}</b>/<b>{manCount}</b>人，儿童<b>{selectChildCount}</b>/<b>{childCount}</b>人'
				}
			},{xtype:'spacer'},{
	    		xtype:'button',
	    		height:42,
	    		width:75,
	    		userCls:'simple',
	    		ui:'pay-orange',
	        	text:'选完了',
	        	handler: 'chooiseVisitorsFinish',
				scope:this
    		}]
    	});
    	this.setItems(items);
    	this.down('visitorlist').on('select',this.onVisitorItemToggleSelected,this,{
    		args:[1]
    	});
    	this.down('visitorlist').on('deselect',this.onVisitorItemToggleSelected,this,{
    		args:[-1]
    	});
    },
    onVisitorItemToggleSelected:function(i,list,r,o){
    	var viewModel = this.getViewModel(),
			smc = viewModel.get('selectManCount'),
			scc = viewModel.get('selectChildCount'),
			mc = viewModel.get('manCount'),
			cc = viewModel.get('childCount');
    	
    	if(viewModel.get('isReadOnly'))return;
    	
    	if(r.get('ATTR_NAME')=='成人'){
    		smc=smc+i<0?0:smc+i;
    		if(i>0&&smc>mc){
    			Ext.toast('成人最多可选'+mc+'人',cfg.toastTimeout);
    			list.deselect(r);
    			smc--;
    		}
    	}else{
    		scc=scc+i<0?0:scc+i;
    		if(i>0&&scc>cc){
    			Ext.toast('儿童最多可选'+cc+'人',cfg.toastTimeout);
    			list.deselect(r);
    			scc--;
    		}
    	}
    	viewModel.set('selectManCount',smc);
    	viewModel.set('selectChildCount',scc);
    },
    chooiseVisitorsFinish:function(){
    	var datas = this.down('visitorlist').getSelections(),
    		prevView = cfg.view.pop(),vm = prevView.getViewModel(),
    		man=vm.get('manCount'),child=vm.get('childCount');

    	window.location.hash = this.getBackHashUrl();
    	for(var i=datas.length;i<(man+child);i++){
    		var visitorModel = Ext.create('Weidian.model.order.visitor.Model',{
    			NAME:'游客'+(i+1)
    		});
    		datas.push(visitorModel);
    	}
    	prevView.visitorList.getStore().clearData();
    	prevView.visitorList.setData(datas);
    }
});