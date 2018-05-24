function showOrder(no){
	no = no.split(' ')[0];
	Ext.create('Ext.window.Window',{
		title:util.windowTitle('&#xe62b;',no),
		width:800,
		height:400,
		bodyPadding:1,
		modal:true,
		draggable:false,
		resizable:false,
		maximizable:false,
		layout:'fit',
		items:[Ext.create('app.view.finance.orderGrid',{accountNo:no})]
	}).show();
}
Ext.define('app.view.finance.payGrid', {
	extend:'Ext.grid.Panel',
	xtype:'paygrid',
	loadMask: true,
	multiColumnSort: true,
	viewConfig:{
		stripeRows:false
	},
	features: [{
        ftype : 'groupingsummary',
        collapsible :false,
        groupHeaderTpl :'<span class="f14">{name}</span> <a href="javascript:;" class="f14" onclick="showOrder(\'{name}\')">共{rows.length}笔账单 <i class="iconfont orange-color f18" data-qtip="点击查看“订单详情”">&#xe6ae;</i></a>',
        hideGroupedHeader : false,
        id:'restaurantGrouping',
        enableGroupingMenu : false
    }, {
        ftype: 'summary',
        dock: 'bottom'
    }],
	initComponent: function() {
		var me = this;
		this.store = Ext.create('Ext.data.Store', {
            pageSize: 50,
            groupField:'SHOW_GROUP',
            autoLoad: false,
            model:Ext.create('app.model.'+currViewInPath+'.model'),
            proxy: {
		         type: 'ajax',
		         noCache:true,
		         url: cfg.getCtx()+cfg.getModuleListUrl()[currViewInPath],
		         reader: {
		             rootProperty: 'data',
            		 totalProperty: 'totalSize'
		         }
		     }
        });
		
		
        //this.bbar = util.createGridBbar(this.store);
        this.columns = Ext.create('app.model.'+currViewInPath+'.column');
        
        this.dockedItems = [{
    		xtype:'toolbar',
    		itemId:'gridtool',
        	items:[{
        		disabled:true,
        		text:'正在初始化...'
        	}]
    	}];
        this.callParent();
        if(beginTime!=''&&endTime!=''&&supplyId!=''){
        	this.getStore().getProxy().setExtraParams({
        		beginTime:beginTime,
        		endTime:endTime,
        		supplyId:supplyId
       		});
        	this.getStore().load();
        }
        /*this.store.on('load',function(s,rs){
        	var sm = me.getSelectionModel(),ms = []; 
        	sm.setLocked(false);
        	for(var i=0;i<rs.length;i++){
        		var ds = rs[i].get('ACCOUNT_STATUS') || '';
	        	if((parseInt(rs[i].get('STATUS'))==2||parseInt(rs[i].get('STATUS'))==5)&&ds==''){
		    		ms.push(rs[i]);
		    		me.getView().getRow(i).style.backgroundColor = "#C1FFC1";  
		    	}
        	}
        	sm.select(ms);
        	sm.setLocked(true);
        	
        });*/
	},
	columnLines: true,
	selType:'checkboxmodel'
});