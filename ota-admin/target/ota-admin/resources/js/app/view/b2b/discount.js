Ext.define('app.view.b2b.discount', {
    extend: 'Ext.container.Viewport',
    layout: 'border',
    items: [{
    	region:'east',
	    selType:'checkboxmodel',
	    selModel:{
	    	mode:'SINGLE'
	    },
    	xtype:'gridpanel',
    	itemId:'rulegrid',
    	store:util.createGridStore(cfg.getCtx()+'/b2b/discount/rule',Ext.create('app.model.b2b.discount.rulemodel')),
    	columns:[{
	    	flex:1,
	    	text: '套餐值',
	        dataIndex: 'MONEY'
	    	
	    },{
	        text: '套餐类型',
	        flex:1,
	        dataIndex: 'RULE_TYPE',
	        renderer:function(v,c,r){
	        	if(v==1){
	        		return '元';
	        	}else if(v==2){
	        		return '%';
	        	}else if(v==3){
	        		return '元/人';
	        	}
	        }
	    },{
	        text: '平台类型',
	        flex:1,
	        dataIndex: 'PLATFROM',
	        renderer:function(v,c,r){
	        	var s = ['','B2B','APP'];
        		return s[v];
	        }
	    },{
	        text: '支付类型',
	        flex:1,
	        dataIndex: 'PAY_WAY',
	        renderer:function(v,c,r){
	        	return v==1?'在线支付':'余额支付';
	        }
	    },{
	        text: '是否禁用',
	        width:65,
	        dataIndex: 'IS_USE',
	        renderer:function(v,c,r){
	        	return v==1?'已禁用':'已启用';
	        }
	    }],
	    dockedItems:[{
	    	itemId:'ruletool',
    		xtype:'toolbar'
    	}],
    	width:400
    },{
    	region:'center',
    	style:'border-right:1px solid #c1c1c1!important;',
    	xtype:'gridpanel',
    	dockedItems : [{
    		xtype:'toolbar',
        	items:[{
        		disabled:true,
        		text:'正在初始化...'
        	}]
    	}],
    	itemId:'basegrid',
    	selType:'checkboxmodel',
	    selModel:{
	    	mode:'SINGLE'
	    },
    	store:util.createGridStore(cfg.getCtx()+'/b2b/discount/list',Ext.create('app.model.b2b.discount.model')),
    	columns:[{
	        text: '活动名称',
	        flex:1,
	        dataIndex: 'TITLE'
	    },{
	        text: '城市',
	        width:85,
	        dataIndex: 'CITY_NAME'
	    },{
	        text: '产品类型',
	        width:65,
	        dataIndex: 'PRO_TYPE',
	        renderer:function(v,c,r){
	        	var s = ['','交通','线路','地接'];
        		return s[v];
	        }
	    },{
	        text: '开始时间',
	        width:150,
	        dataIndex: 'BEGIN_TIME'
	    },{
	        text: '结束时间',
	        width:150,
	        dataIndex: 'END_TIME'
	    },{
	        text: '是否禁用',
	        width:65,
	        dataIndex: 'IS_USE',
	        renderer:function(v,c,r){
	        	return v==1?'已禁用':'已启用';
	        }
	    },{
	        text: '可否退款',
	        width:65,
	        dataIndex: 'IS_REFUND',
	        renderer:function(v,c,r){
	        	return v==1?'可以退款':'不可退款';
	        }
	    }/*,{
	    	flex:1,
	    	text: '备注',
	        dataIndex: 'REMARK'
	    	
	    }*/]
    }]
});

