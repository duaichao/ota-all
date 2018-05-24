Ext.define('app.view.b2b.user.site.companyGrid', {
    extend: 'Ext.grid.Panel',
    config:{
    	columnLines: true,
    	selType:'rowmodel',
    	cityId:null,
    	purview:'',
    	ownerName:''
    },
    applyCityId:function(cityId){
    	var me = this;
    	if(cityId){
    		if(me.getStore()){
    			me.getStore().getProxy().setExtraParams({cityId:cityId});
    			me.getStore().load();
    		}
    	}
    	return cityId;
    },
    initComponent: function() {
    	var me =this;
    	this.store = Ext.create('Ext.data.Store', {
            autoLoad: false,
            model:Ext.create('Ext.data.Model',{
            	fields:['INTER_AMOUNT','CNT','SALE_COMPANY','SALE_AMOUNT','BUY_COMPANY','TRUE_SALE_AMOUNT']
            }),
            proxy: {
		         type: 'ajax',
		         noCache:true,
		         url: cfg.getCtx()+'/b2b/user/site/company/order?companyType='+(this.getPurview()=='supply'?'1':'2'),
		         reader: {
		             rootProperty: 'data',
            		 totalProperty: 'totalSize'
		         }
		     }
        });
    	this.store.getProxy().setExtraParams({cityId:this.getCityId()});
    	
    	this.columns = [Ext.create('Ext.grid.RowNumberer',{width:25}),{
    		text: '公司名称',
	        flex:1,
	        dataIndex: 'SALE_COMPANY',
	        renderer:function(v,c,r){
	        	var h = [
 	        		'<div class="ht20"><a href="javascript:;">'+r.get(this.getPurview()=='supply'?'SALE_COMPANY':'BUY_COMPANY')+'</a></div>'
 	        	];
 	        	return h.join('');
	        }
    	},{
    		text: '订单数',
	        width:80,
	        dataIndex: 'CNT',
	        renderer:function(v,c,r){
	        	var h = [
 	        		'<div class="ht20 f18 disable-color">'+v+'</div>'
 	        	];
 	        	return h.join('');
	        }
    	},{
    		text: '结算总额',
	        width:120,
	        hidden:this.getPurview()=='angncy',
	        dataIndex: 'INTER_AMOUNT',
	        renderer:function(v,c,r){
	        	var h = [
 	        		'<div class="ht20">'+util.moneyFormat(v,'f18 orange-color')+'</div>'
 	        	];
 	        	return h.join('');
	        }
    	},{
    		text: '外卖总额',
	        width:120,
	        hidden:this.getPurview()=='supply',
	        dataIndex: 'SALE_AMOUNT',
	        renderer:function(v,c,r){
	        	var h = [
 	        		'<div class="ht20">'+util.moneyFormat(v,'f18 orange-color')+'</div>'
 	        	];
 	        	return h.join('');
	        }
    	},{
    		text: '总利润',
	        width:120,
	        hidden:this.getPurview()=='supply',
	        dataIndex: 'TRUE_SALE_AMOUNT',
	        renderer:function(v,c,r){
	        	var h = [
 	        		'<div class="ht20">'+util.moneyFormat(v,'f18 green-color')+'</div>'
 	        	];
 	        	return h.join('');
	        }
    	}];
    	this.dockedItems = [{
        	xtype:'toolbar',
        	style:'padding-left:1px;background:#e2eff8;',
        	items:['<span class="bold f14 blue-color"><i class="iconfont f20">'+(this.getOwnerName()=='旅行社'?'&#xe608;':'&#xe628;')+'</i> '+this.getOwnerName()+'</span>'
	        ,'->',{
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
	          	width:30,
              	margin:'0 2 0 2',
              	glyph:'xe61c@iconfont',
              	tooltip:'查询',
	    		handler:function(btn){
	    			var g = btn.up('toolbar').up('gridpanel'),
	    				endDate = btn.previousSibling(),
	    				startDate = endDate.previousSibling();
	    			startDate = Ext.Date.format(startDate.getValue(),'Y-m-d');
	    			endDate = Ext.Date.format(endDate.getValue(),'Y-m-d');
	    			var ep = {startDate:startDate,endDate:endDate};
					Ext.applyIf(ep,g.getStore().getProxy().getExtraParams());
	    			g.getStore().getProxy().setExtraParams(ep);
	    			g.getStore().load();
	    		}
	    	},{
	          	margin:'0 10 0 0',
	          	glyph:'xe63d@iconfont',
              	width:30,
              	tooltip:'重置日期',
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