Ext.define('app.view.b2b.user.waitDo', {
    extend: 'Ext.toolbar.Toolbar',
    config:{
	    params:null
    },
    applyParams:function(params){
    	var me = this;
    	if(params){
    		Ext.Ajax.request({
    			params:params,
    			url:cfg.getCtx()+'/b2b/user/wait/do',
    			success:function(response, opts){
    				var data = Ext.decode(response.responseText);
    				if(me.down('container#waitDoPanel')){
    					me.down('container#waitDoPanel').setData(data);
    				}
    			}
    		});
    	}
    	return params;
    }
});
