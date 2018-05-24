Ext.define('app.store.HeadNavigation', {
    extend: 'Ext.data.TreeStore',
    alias: 'store.headnavigation',
    constructor: function(config) {
        var me = this;
        me.callParent([Ext.apply({
            root: {
            	glyph:'xe60e@iconfont',
		        text: '系统导航',
		        id: 'head_nav_home',
		        expanded: true,
		        children: me.getNavItems()
		    }
        }, config)]);
    },
    getNavItems:function(){
    	var arr = Ext.decode(cfg.getUser().modules);
    	arr= Ext.Array.insert(arr,0,[{
    		id:'home',
    		leaf:true,
    		parentId:'head_nav_home',
    		glyph:'xe600@iconfont',
    		text:'我的首页'
    	}]);
    	return arr;
    }
});
