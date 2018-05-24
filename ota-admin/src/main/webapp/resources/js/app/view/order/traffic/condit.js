Ext.define('app.view.order.traffic.condit', {
    extend: 'app.view.common.BaseCondit',
    requires: [
	    'app.view.common.CityCombo'
    ],
    initComponent: function() {
    	this.items = [{
    		items:[{
	    		fieldLabel:'出发地',
	    		name:'BEGIN_CITY',
	    		width:180,
	    		hiddenName:'BEGIN_CITY',
	    		valueField: 'TEXT',
	    		xtype:'citycombo'
    		},{
    			fieldLabel: '卖家',
    			name:'SALER',
    			width:180
    		},{
    			fieldLabel: '买家',
    			name:'BUYER',
    			width:180
    		},{
    			fieldLabel: '下单时间',
    			name:'CREATE_BEGIN_TIME',
    			width:165,
    			xtype:'datefield',
    			format:'Y-m-d',
    			endDateField: 'endxddt',
		        itemId:'startxddt',
		        showToday:false,
		        vtype: 'daterange'
    		},{
	            xtype: 'splitter'
	        },{
    			hideLabel:true,
    			width:105,
    			xtype:'datefield',
    			name:'CREATE_END_TIME',
    			format:'Y-m-d',
    			itemId:'endxddt',
    			showToday:false,
	            startDateField: 'startxddt',
	            vtype: 'daterange'
    		},{
    			fieldLabel: '付款时间',
    			name:'PAY_BEGIN_TIME',
    			width:165,
    			xtype:'datefield',
    			format:'Y-m-d',
    			endDateField: 'enddt',
		        itemId:'startdt',
		        showToday:false,
		        vtype: 'daterange'
    		},{
	            xtype: 'splitter'
	        },{
    			hideLabel:true,
    			name:'PAY_END_TIME',
    			width:105,
    			xtype:'datefield',
    			format:'Y-m-d',
    			itemId:'enddt',
    			showToday:false,
	            startDateField: 'startdt',
	            vtype: 'daterange'
    		}]
    	},{
    		margin:'5 0 0 0',
    		items:[{
	    		fieldLabel:'目的地',
	    		name:'END_CITY',
	    		width:180,
	    		hiddenName:'END_CITY',
	    		valueField: 'TEXT',
	    		xtype:'citycombo'
    		},{
	        	fieldLabel:'状态',
    			xtype:'combo',
				name:'STATUS',
				editable:false,
				hiddenName:'STATUS',
				width:180,
				value:'',
				typeAhead: true,
	            triggerAction: 'all',
	            store: [
	            	['','请选择'],
	                ['0','待付款'],
	                ['1','付款中'],
	                ['2','已付款'],
	                ['3','待退款'],
	                ['4','退款中'],
	                ['5','已退款'],
	                ['6','手动取消订单'],
	                ['7','系统自动取消']
	            ]
    		}]
    	}];
    	this.callParent();
    }
});