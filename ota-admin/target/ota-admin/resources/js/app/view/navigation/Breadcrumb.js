Ext.define('app.view.navigation.Breadcrumb', {
    extend: 'Ext.toolbar.Toolbar',
    id: 'navigation-breadcrumb',
    xtype: 'navigation-breadcrumb',
    config: {
        selection: null
    },
    initComponent: function() {
    	var store = Ext.StoreMgr.get('navigation'),
    		node = this.getSelection()||'root',
    		nodeId = location.hash.replace('#','');
    	if(nodeId!='home'){
    		node = store.getNodeById(nodeId);
    	}
    	//目前不做首页 先这样强制跳转 做的时候再考虑
    	//node = nodeId.replace('home','')==''?'root':node;
    	
    	/*if(node=='home'){
        	var ctr = Ext.app.route.Router.routes[0].controller;	
        	ctr.redirectTo('home');
        }*/
        this.items = [{
        	id:'btn-user-home',
        	glyph:'xe600@iconfont',
        	text:'首页'
        },'-',{
        	id:'btn-navs',
        	glyph:'xe60e@iconfont',
        	text:'系统导航',
        	hidden:node!=null,
        	handler:function(btn){
        		var bcb = btn.nextSibling();
        		bcb.setSelection('root');
        		btn.hide();
        	}
        },{
        	cls:'my-breadcrumb',
            xtype: 'breadcrumb',
            reference: 'toolbar',
            selection: node,
            flex: 1,
            store: store
        }];

        this.callParent();

        this._breadcrumbBar = this.items.getAt(3);
        
    },

    updateSelection: function(node) {
        if (this.rendered) {
            this._breadcrumbBar.setSelection(node);
        }
    }
});
