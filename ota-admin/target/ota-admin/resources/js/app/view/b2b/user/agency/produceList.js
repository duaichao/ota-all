Ext.define('app.view.b2b.user.agency.produceList', {
    extend: 'Ext.tab.Panel',
    config:{
    	plain:true
    },
    initComponent: function() {
    	this.defaults = {
			viewConfig: {
	    	    preserveScrollOnRefresh: true
	    	}
    	};
    	this.items = [{
    		title:'<i class="iconfont" style="top:1px;">&#xe67c;</i> <span class="f14">周边游</span>',
    		//glyph:'xe67c@iconfont',
    		xclass:'app.view.b2b.user.agency.produceGrid',
    		where:'zby'
    	},{
    		title:'<i class="iconfont" style="top:1px;">&#xe669;</i> <span class="f14">国内游</span>',
    		//glyph:'xe669@iconfont',
    		xclass:'app.view.b2b.user.agency.produceGrid',
    		where:'gny'
    	},{
    		title:'<i class="iconfont" style="top:1px;">&#xe666;</i> <span class="f14">出境游</span>',
    		//glyph:'xe666@iconfont',
    		xclass:'app.view.b2b.user.agency.produceGrid',
    		where:'cjy'
    	},{
    		title:'<i class="iconfont" style="top:1px;">&#xe680;</i> <span class="f14">热卖产品</span>',
    		//glyph:'xe616@iconfont',
    		xclass:'app.view.b2b.user.agency.produceGrid',
    		where:'hot'
    	},{
    		title:'<i class="iconfont" style="top:1px;">&#xe623;</i> <span class="f14">特惠产品</span>',
    		//glyph:'xe623@iconfont',
    		xclass:'app.view.b2b.user.agency.produceGrid',
    		where:'discount'
    	},{
    		title:'<i class="iconfont" style="top:1px;">&#xe619;</i> <span class="f14">我的足迹</span>',
    		//glyph:'xe619@iconfont',
    		xclass:'app.view.b2b.user.agency.produceGrid',
    		where:'buy'
    	},{
    		title:'<i class="iconfont" style="top:1px;">&#xe67e;</i> <span class="f14">合作商户</span>',
    		//glyph:'xe67e@iconfont',
    		xclass:'app.view.b2b.user.agency.supplyView'
    	}];
    	this.callParent();
    }
});
