Ext.define('app.view.b2b.user.routeStatSupplysRoute', {
    extend: 'Ext.grid.Panel',
    xtype:'routeStatSupplysRoute',
    config:{
    	columnLines: true,
    	selType:'rowmodel',
    	style:{
    		borderLeft:'1px solid #d1d1d1;'
    	},
    	params:null
    },
    initComponent: function() {
    	this.store = Ext.create('Ext.data.Store', {
            autoLoad: false,
            pageSize:20,
            model:Ext.create('Ext.data.Model',{
            	fields: [
            	        'ID','TITLE'
            	]
            }),
            proxy: {
		         type: 'ajax',
		         noCache:true,
		         url: cfg.getCtx()+'/stat/sale/route/supply/create',
		         reader: {
		             rootProperty: 'data',
            		 totalProperty: 'totalSize'
		         }
		     }
        });
    	this.bbar = util.createGridBbar(this.store);
    	this.columns=[{
    		text: '产品名称',
	        flex:1,
	        dataIndex: 'TITLE',
	        renderer:function(v,c,r){
	        	var h = [
 	        		'<div class="ht20 f14">'+v+'</div>'
 	        	];
 	        	return h.join('');
	        }
    	},{
    		xtype: 'actionglyphcolumn',
            width: 30,
            align:'center',
            sortable: false,
            menuDisabled: true,
            items: [{
            	iconCls:'iconfont blue-color',
                tooltip: '详情',
                scope: this,
                text:'&#xe635;',
                handler: function(grid, rowIndex, colIndex){
                	var record = grid.getStore().getAt(rowIndex);
                	grid.getSelectionModel().select(rowIndex);
                	var detail = Ext.create('app.view.resource.route.pub.detail',{});
                	var win = Ext.create('Ext.window.Window',{
            			title:util.windowTitle('&#xe635;','线路详情',''),
            			bodyPadding:5,
            			modal:true,
            			draggable:false,
            			resizable:false,
            			maximizable:false,
               			layout:'fit',
               			items:[detail]
            		});
            		win.show().maximize();
            		detail.setRouteId(record.get('ID'));
                }
            }]
    	}];
    	
    	this.callParent();
    },
    applyParams:function(params){
    	if(params){
	    	this.store.getProxy().setExtraParams(params);
	    	this.store.load();
    	}
    	return params;
    }
});