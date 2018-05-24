Ext.define('Weidian.view.order.route.RouteController', {
    extend: 'Ext.app.ViewController',

    alias: 'controller.orderroute',
    
    
    
    gotoOrderVisitor : function(){
    	//ajax提交数据 跳转
    	var view = this.getView(),
    		sParams = view.getSubmitParams();
    	view.setSubmitParams({totalAmount:(sParams.manCount*sParams.manPrice)+(sParams.childCount*sParams.childPrice)+(sParams.singleRoomCount*sParams.singleRoomPrice)});
    	if(!sParams.startDate){
    		Ext.toast('请选择出团日期',cfg.toastTimeout);
    		return;
    	}
    	
    	this.redirectTo('order/visitor/'+sParams.id);
    },
    gotoPayCenter : function(btn){
    	btn.disable();
    	//ajax提交数据 跳转
    	var view = this.getView(),sParams = view.getSubmitParams(),
    		visitors = [],visitorStore = view.visitorList.getStore(),
    		contactName = view.down('textfield#contactName').getValue(),
    		contactPhone = view.down('numberfield#contactPhone').getValue(),
    		visitorFlag = true;
    	
    	
    	if(!contactName||!contactPhone){
    		Ext.toast('请填写联系人',cfg.toastTimeout);
    		btn.enable();
    		return;
    	}
    	sParams.contactName = contactName;
    	sParams.contactPhone = contactPhone;
    	visitorStore.each(function (record, index, length) {
    		if(cfg.priceAttrs&&cfg.priceAttrs.length>0){
    			Ext.Array.each(cfg.priceAttrs,function(o,i){
    				if(!record.get('ATTR_ID')){
    					if(o.TITLE==record.get('ATTR_NAME')){
    						record.set('ATTR_ID',o.ID);
        					return false;
        				}
    				}
    				
    			});
        	}
    		if(record.get('NAME')&&record.get('ATTR_ID')&&record.get('CARD_NO')){
    			visitors.push(record.data);
    		}else{
    			visitorFlag = false;
    		}
    	});
    	if(!visitorFlag){
    		Ext.toast('游客信息不完整',cfg.toastTimeout);
    		btn.enable();
    		return;
    	}
    	sParams.visitors = Ext.encode(visitors);
    	
    	//console.log(sParams);
    	
    	util.request(cfg.url.order.save,sParams,function(rs){
    		btn.enable();
    		if(rs.success){
    			this.redirectTo('pay/center/'+rs.message);
    		}else{
    			Ext.toast('订单提交失败！',cfg.toastTimeout);
    		}
    	},this);
    }
});