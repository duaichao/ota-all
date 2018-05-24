Ext.define('app.controller.produce.payfinish', {
	extend: 'app.controller.common.BaseController',
	config: {
		control: {
			'button[itemId=backOrderList]':{
				click:function(btn){
					if(proType=='1'){
						util.redirectTo('order/traffic');
					}
					if(proType=='2'||proType=='3'){
						util.redirectTo('order/route');
					}
					if(proType=='4'){
						util.redirectTo('order/route/renew');
					}
				}
			}
		},
		refs: {
        }
	}
});