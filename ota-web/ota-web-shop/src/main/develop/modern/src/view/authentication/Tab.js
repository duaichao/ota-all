Ext.define("Weidian.view.authentication.Tab", {
    extend: 'Ext.tab.Bar',
    alternateClassName: 'typetab',
    xtype:'typetab',
    config: {
    	activeTab:0,
    	docked:'top',
		userCls:'category-tab',
		defaults: {
			ui:'gray'
		},
		listeners:{
			activetabchange:function(t,n,o){
				var view = t.up('authbase');
				view.getViewModel().set('hide',t.indexOf(n)==1);
				//view.fireEvent('tabchange', view,t,n);
				if(view.down('image#captcha')){
					view.down('image#captcha').setSrc('https://passport.tuniu.com/ajax/captcha/m/1483499100868.0684?dc=' + new Date().getTime());
				}
			}
		}
    }
});