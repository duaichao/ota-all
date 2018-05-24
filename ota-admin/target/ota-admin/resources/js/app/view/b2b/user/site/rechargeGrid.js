Ext.define('app.view.b2b.user.site.rechargeGrid', {
    extend: 'Ext.grid.Panel',
    config:{
    	columnLines: true,
    	selType:'rowmodel',
    	cityId:null
    },
    initComponent: function() {
    	var me =this;
    	this.store = Ext.create('Ext.data.Store', {
            autoLoad: true,
            model:Ext.create('Ext.data.Model',{
            	fields:['MONEY','TEXT','COMPANY','OPET_TIME']
            }),
            proxy: {
		         type: 'ajax',
		         noCache:true,
		         url: cfg.getCtx()+'/b2b/user/site/account?cityId='+this.getCityId(),
		         reader: {
		             rootProperty: 'data',
            		 totalProperty: 'totalSize'
		         }
		     }
        });
    	this.columns = [Ext.create('Ext.grid.RowNumberer',{width:25}),{
    		text: '旅行社',
	        flex:1,
	        dataIndex: 'COMPANY',
	        renderer:function(v,c,r){
	        	var h = [
 	        		'<div class="ht20"><a href="javascript:;">'+v+'</a></div>'
 	        	];
 	        	return h.join('');
	        }
    	},{
    		text: '部门',
	        width:80,
	        dataIndex: 'TEXT',
	        renderer:function(v,c,r){
	        	var h = [
 	        		'<div class="ht20">'+v+'</div>'
 	        	];
 	        	return h.join('');
	        }
    	},{
    		text: '充值金额',
	        width:120,
	        dataIndex: 'MONEY',
	        renderer:function(v,c,r){
	        	var h = [
 	        		'<div class="ht20">'+util.moneyFormat(v,'f18 orange-color')+'</div>'
 	        	];
 	        	return h.join('');
	        }
    	},{
    		text: '充值时间',
	        width:150,
	        dataIndex: 'OPET_TIME',
	        renderer:function(v,c,r){
	        	var h = [
 	        		'<div class="ht20">'+v+'</div>'
 	        	];
 	        	return h.join('');
	        }
    	}];
    	this.dockedItems = [{
        	xtype:'toolbar',
        	layout: {
                overflowHandler: 'Menu'
            },
        	items:['->',{
        		width:100,
        		emptyText:'开始日期',
              	endDateField: 'enddt',
	            itemId:'startdt',
	            showToday:false,
	            editable:false,
	            vtype: 'daterange',
              	format:'Y-m-d',
              	xtype:'datefield'
        	},{
              	width:100,
              	emptyText:'结束日期',
              	margin:'0 0 0 0',
              	format:'Y-m-d',
              	editable:false,
              	showToday:false,
              	itemId:'enddt',
	            startDateField: 'startdt',
	            vtype: 'daterange',
              	xtype:'datefield'
        	},{
              	cls:'groupChartView',
              	itemId:'queryBtn',
              	margin:'0 2 0 2',
              	text:'查询',
        		handler:function(btn){
        			var g = btn.up('toolbar').up('gridpanel'),
        				endDate = btn.previousSibling(),
        				startDate = endDate.previousSibling();
        			startDate = Ext.Date.format(startDate.getValue(),'Y-m-d');
        			endDate = Ext.Date.format(endDate.getValue(),'Y-m-d');
        			g.getStore().getProxy().setExtraParams({startDate:startDate,endDate:endDate});
        			g.getStore().load();
        		}
        	},{
              	margin:'0 10 0 0',
              	tooltip:'重置查询',
              	glyph:'xe63d@iconfont',
              	handler:function(b){
              		b.previousSibling().previousSibling().reset();
              		b.previousSibling().previousSibling().previousSibling().reset();
              		var g = b.up('toolbar').up('gridpanel');
              		g.getStore().getProxy().setExtraParam('startDate', '');
              		g.getStore().getProxy().setExtraParam('endDate', '');
        			g.getStore().load();
              	}
        	}]
        }];
    	this.callParent();
    }
});