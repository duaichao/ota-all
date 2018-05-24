Ext.define('Weidian.view.Base', {
    extend: 'Ext.Container',
    
    config:{
    	hashUrl:'',
    	backHashUrl : '',
    	scrollable : 'y',
    	/**
		 * 联系卖家
		 */
    	showMainPoint : false,
    	/**
		 * 回到顶部
		 */
		showTopPoint : false,
		/**
		 * 退回按钮
		 */
		backHide : false,
		/**
		 *navigationBar 高度设置
		 */
		nbarSize : null,
    	/**
    	 * 提交数据
    	 */
    	submitParams:{},
    	/**
    	 *  停止子页面initialize initView 
    	 *  false停止 true运行
    	 */
    	initStatus:true
    },
    /**
     * 无效请求过滤
     */
    initialize:function(){
    	//订单流程页面
    	if(this.getController()&&this.getController().type=='orderroute'){
    		var sParams = this.getSubmitParams();
    		if(this.xtype=='paycenter'){console.log(sParams.orderId);
    			if(Ext.isEmpty(sParams.orderId)){
    				this.setInitStatus(false);
	        		setTimeout(function(){
	        			cfg.view.pop();
	        			window.location.hash = 'home';
	        		},1000)
    			}
    		}else{
	    		if(Ext.isEmpty(sParams.routeId)){
	        		//if(sParams.id){
	        		//如果url包含id参数  加载线路详情 设置submitParams必须值 routeId、singleRoomPrice	
	        		//}else{
	        			//停止
	        			this.setInitStatus(false);
		    			Ext.toast('请求超时，返回首页',cfg.toastTimeout);
		        		setTimeout(function(){
		        			cfg.view.pop();
		        			window.location.hash = 'home';
		        		},1000)
	        		//}
	        	}
    		}
    	}
    	this.callParent();
    },
    applySubmitParams:function(params){
    	if(params){
    		var newParams;
    		if(params=='reset'){
    			newParams = {};
    		}else{
    			newParams = Ext.apply(this.getSubmitParams()||{},params);
    		}
    		//console.log('applySubmitParams.................'+this.xtype);
    		//console.log(newParams);
    		//console.log('applySubmitParams.................'+this.xtype);
    		return newParams;
    	}
    },
    loadResult:function(url,params,callback){
    	util.request(url,params,function(data){
    		if(this.getViewModel()){
    			this.getViewModel().setData(data);
    		}
    		callback.call(this,data);
	    },this);
    }
});
