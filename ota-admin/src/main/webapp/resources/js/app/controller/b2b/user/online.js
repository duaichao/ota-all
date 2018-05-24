Ext.define('app.controller.b2b.user.online', {
	extend: 'app.controller.common.BaseController',
	config: {
		control: {
			'toolbar button[itemId=downline]': {
	             click: 'onDownLine'
	         }
		},
		refs: {
            basegrid: 'basegrid'
        }
	},
	onDownLine:function(){
		var me = this,gd = this.getBasegrid(),
			sel;
		if(sel = util.getSingleModel(gd)){
			Ext.Ajax.request({
			    url: cfg.getCtx()+'/b2b/user/downline?userId='+sel.get('ID'),
			    success: function(response, opts) {
			        gd.getStore().load();
			    }
			});
		}
	}
});