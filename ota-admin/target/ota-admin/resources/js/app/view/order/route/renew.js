Ext.define('app.view.order.route.renew', {
	extend:'Ext.grid.Panel',
	xtype:'routeRenewGrid',
	itemId:'basegrid',
	loadMask: true,
	initComponent: function() {
		var me = this;
		this.store = util.createGridStore(cfg.getCtx()+cfg.getModuleListUrl()[currViewInPath],Ext.create('app.model.order.route.model'));
		this.bbar = util.createGridBbar(this.store);
		this.columns = Ext.create('app.model.order.route.column');
		this.columns.splice(this.columns.length-2,1,{
			text:'订单状态',
	        width:120,
	        dataIndex: 'RENEW_STATUS',
	        renderer:function(v,c,r){
	        	var color = '',txt = '';
	        	var status = ['待付款','付款中','已付款','待退款','退款中','已退款','手动取消订单','系统自动取消'],
	    			ov = r.get('STATUS');
	    		txt = status[ov];//=='2'?'已付款':'审核通过';
	    		color = '#4CAF50';
	    		percent = 100;
	        	if(ov==6||ov==7){
	        	}else{
	        		if(v==2){
		        		if(ov==0){
		        			color = '#FFC107';
		        			percent=30;
		        		}
		        		if(ov==1){percent=50;}
		        		if(ov==3){percent=30;}
		        		if(ov==4){percent=50;}
		        		
		        		
		        	}
		        	if(v==3){
		        		txt = '审核不通过';
		        		color = '#FF5722';
		        		percent = 100;
		        	}
		        	if(v==0){
		        		txt = '待提交';
		        		color = '#CDDC39';
		        		percent = 30;
		        	}
		        	if(v==1){
		        		txt = '待审核';
		        		color = '#607D8B';
		        		percent = 50;
		        	}
	        	}
	        	var c2 = percent==100?'#fff':'#9E9E9E'; 
	        	
	        	if(ov==5||ov==3||ov==4){
        			color='#dd4b39';
        		}
        		if(ov==6||ov==7){
        			color='#9E9E9E';
	        	}
	        	var payIcon = '',
		        	payType = parseInt(r.get('PAY_TYPE')),
		        	payTypes=['&#xe66a;','&#xe66e;','&#xe66d;','&#xe66b;'],
		        	payTexts=['部门余额支付','支付宝支付','财付通支付','网银支付'],
		        	payCls=['#F44336','#00a0e9;','#ff8500;','#009688'];
		        payIcon = '<i class="iconfont" style="color:'+payCls[payType]+'" data-qtip="'+payTexts[payType]+'">'+payTypes[payType]+'</i> ';	
		        
		        if(!ov||ov=='0'){
		        	payIcon = '';
		        }
	        	return Ext.String  
                .format(  
                 '<div>'  
                       + '<div style="float:left;border:1px solid {2};height:20px;width:100%;">'  
                       + '<div style="float:left;text-align:center;line-height:16px;width:100%;color:{3};">{0}%</div>'  
                       + '<div style="background: {2};width:{1}%;height:18px;"></div>'  
                       + '</div></div>', percent, percent, color,c2)+'<div class="ht20" style="clear:both;margin-top:3px;">'+payIcon+txt+'</div>';
	        }
		});
		this.dockedItems = [{
    		xtype:'toolbar',
    		itemId:'gridtool',
        	items:[{
        		disabled:true,
        		text:'正在初始化...'
        	}]
    	}];
		this.callParent();
	},
	columnLines: true,
	selType:'checkboxmodel'
});