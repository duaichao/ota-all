Ext.define('app.view.finance.queryGrid', {
	extend:'Ext.grid.Panel',
	xtype:'querygrid',
	loadMask: true,
	multiColumnSort: true,
	viewConfig:{
		stripeRows:false
	},
	features: [{
        ftype : 'groupingsummary',
        groupHeaderTpl :[
		    '<span>{[this.format(values)]}</span>',
		    {
		        format: function(o) {
		        	if(o.groupField=='SHOW_GROUP'){
		        		return '<span>'+o.name+'共'+o.rows.length+'笔订单</span>';
		        	}else{
		        		var n = o.name,
		        			o1 = n.split('**'),
		        			url = o1[0],
		        			msg = o1[1];
		        		if(url=='无'){
		        			return msg+'：共'+o.rows.length+'笔订单，还未上传付款凭证';
		        		}
		        		return msg+'：共'+o.rows.length+'笔订单，<a href="javascript:;" target="_blank" onclick="util.downloadAttachment(\''+cfg.getPicCtx()+'/'+url+'\')">下载付款凭证</a>';
		        	}
		        }
		    }
		],
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
            model:Ext.create('app.model.finance.calc.model'),
            proxy: {
		         type: 'ajax',
		         noCache:true,
		         url: cfg.getCtx()+'/finance/account/info',
		         reader: {
		             rootProperty: 'data',
            		 totalProperty: 'totalSize'
		         }
		     }
        });
		
		
        //this.bbar = util.createGridBbar(this.store);
        this.columns = Ext.create('app.model.finance.calc.queryColumn');
        
        this.dockedItems = [{
    		xtype:'toolbar',
    		itemId:'gridtool',
        	items:[{
        		disabled:true,
        		text:'正在初始化...'
        	}]
    	}];
        this.callParent();
	},
	columnLines: true,
	selType:'checkboxmodel'
});