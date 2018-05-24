Ext.define('app.view.order.refundGrid', {
	extend:'Ext.grid.Panel',
	xtype:'refundgrid',
	loadMask: true,
	border:true,
	emptyText: '没有找到数据',
	orderId:'',
	config:{
		record:null
	},
	initComponent: function() {
		var me = this;
		this.store = util.createGridStore(cfg.getCtx()+'/order/traffic/refund/audit?orderId='+me.orderId,Ext.create('Ext.data.Model',{
			fields: [
				'ID',
				'TITLE',
				'ENTITY_TYPE',
				'COMPANY',
				'USER_NAME',
				'IS_SELF',
				'MOBILE',
				'ENTITY_ID',
				'REMARKS',
				'AMOUNT'
			]
		}));
        //this.bbar = util.createGridBbar(this.store);
        this.columns = [{
        	text: '产品名称',
        	flex:1,
        	dataIndex: 'TITLE',
        	renderer: function(v,metaData,r,colIndex,store,view){
        		var h = [
        			'<a style="width:300px;white-space:normal;display:inline-block;" href="javascript:;" class="f14">'+v+'</a>'
        		];
        		/*if(r.get('DISCOUNT_TITLE')){
	        		h.push('<div class="ht20 red-color">');
	        		h.push('参加活动'+r.get('DISCOUNT_TITLE')+'，立减'+r.get('DISCOUNT_TOTAL_AMOUNT')+'元');
	        		h.push('</div>');
	        	}*/
        		return h.join('');
        	}
        },{
        	text: '公司',
	        width:180,
	        dataIndex: 'COMPANY',
	        renderer:function(v,c,r){
        		return [
	        		'<div class="ht16" style="width:165px;white-space:normal;display:inline-block;"><i class="iconfont" data-qtip="联系人<br>'+r.get('USER_NAME')+'<br>'+util.formatMobile(r.get('MOBILE'))+'" style="color:#999;">&#xe841;</i> '+v+'</div>',
	        		//'<div class="ht16">联系：'+r.get('USER_NAME')+'</div>',
	        		//'<div class="ht16">电话：'+util.formatMobile(r.get('MOBILE'))+'</div>'
	        	].join('');
        	}
        },{
        	text: '退款金额',
        	width:105,
        	dataIndex: 'AMOUNT',
        	xtype: 'widgetcolumn',
            widget: {
                xtype: 'numberfield',
                width:100,
                padding:2,
                listeners: {
                    afterrender: function(f){
                        var r = f.getWidgetRecord();
                        	//dam = r.get('DISCOUNT_TOTAL_AMOUNT');
                       // if(dam){
                       // 	dam = parseFloat(dam);
                       // 	f.setMaxValue(parseFloat(r.get('AMOUNT'))-dam);
                       // 	f.setValue(parseFloat(r.get('AMOUNT'))-dam);
                       // }else{
                        	f.setMaxValue(parseFloat(r.get('AMOUNT')));
                       // }
                        
                        if(r.get('IS_SELF')=='0'){
                        	f.disable();
                        }
                    },
                    blur:function(f){
                    	var r = f.getWidgetRecord();
                    	r.set('AMOUNT',f.getValue()||0);
                    	if(f.getValue()==''){f.setValue(0);}
                    }
                }
            }
        },{
        	text: '备注',
        	width:200,
        	dataIndex: 'REMARKS',
        	xtype: 'widgetcolumn',
            widget: {
                xtype: 'textfield',
                padding:2,
                listeners: {
                    afterrender: function(f){
                        var r = f.getWidgetRecord();
                        if(r.get('IS_SELF')=='0'){
                        	f.disable();
                        }
                    },
                    blur:function(f){
                    	var r = f.getWidgetRecord();
                    	
                    	r.set('REMARKS',f.getValue());
                    }
                }
            }
        }];
        this.callParent();
	},
	columnLines: true,
	selType:'rowmodel'
});