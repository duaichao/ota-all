Ext.define('Weidian.Application', {
    extend: 'Ext.app.Application',
    
    name: 'Weidian',

    defaultToken : 'home',

    mainView: 'Weidian.view.main.Main',
    

    stores: [
        //'NavigationTree'
    ],
    constructor:function(){
    	//微店店铺信息
    	if(!Ext.shopInfo){
	    	util.request(cfg.url.index,{},function(data){
	    		Ext.shopInfo = data;
	    	});
    	}else{
    		if(Ext.isString(Ext.shopInfo)){
    			Ext.shopInfo = Ext.decode(Ext.shopInfo);
    		}
    	}
    	this.callParent();
    }
});
