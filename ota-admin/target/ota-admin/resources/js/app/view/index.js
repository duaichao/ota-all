Ext.define('app.view.index', {
    extend: 'Ext.container.Viewport',
    layout: 'border',
    //stateful: true,//构造时访问Ext.state.Manager 
    //stateId: 'app-viewport',//管理器获取组件需要的id
    items: [{
        region: 'north',
        xtype: 'appHeader'
    }, {
        region: 'center',
        style:'border-bottom:2px solid rgba(42,42,42,1);border-top:4px solid rgba(42,42,42,1);',
        xtype: 'appContainer',
        dockedItems: [{
        	height:38,
        	style:'box-shadow: 1px 1px 0px rgba(153,153,153, 0.2);background:transparent!important;',
            layout:'hbox',
            items:[{
            	style:'background: none repeat scroll 0 0 rgba(0, 0, 0, 0.02);padding:2px 6px!important;',
            	xtype: 'navigation-breadcrumb',
                reference: 'breadcrumb>',
                flex:1
            }/*,{
            	xtype:'button',
            	cls:' plain',
            	width:80,
            	height:38,
        		href:userPruviews[upv],
        		disabled:upv=='-1',
        		text:'<i class="iconfont green-color f15" style="top:1px;">&#xe6a4;</i> <span class="green-color f14">帮助</span>'
            }*/]
        }]
    },{
    	region:'south',
    	height: 26,
    	border:false,
    	xtype: 'appFooter'
    },{
    	region:'west',
    	hidden:true,
    	width:78,
    	header: {
	        hidden: true
	    },
        style:'border-bottom:2px solid #427fed;border-top:2px solid #427fed;',
        bodyStyle:'background:rgba(68,68,68,.9);',
        collapsible: true,  
        collapseMode:'mini',
        hideCollapseTool:true,
        /*split: {
            size: 8,
            style:'border-bottom:2px solid #000;border-top:2px solid #000;background:rgba(68,68,68,.9);',
        },*/
        layout:'fit',
        items:[{
        	xtype:'navthumbnails',
        	store:'navigation'
        }]
    }],
    initComponent: function() {
    	this.callParent();
    	//util.startIntro();
    }
});
