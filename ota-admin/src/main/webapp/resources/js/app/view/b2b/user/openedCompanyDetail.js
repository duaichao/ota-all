Ext.define('app.view.b2b.user.openedCompanyDetail', {
    extend: 'Ext.grid.Panel',
    xtype:'openedCompanyDetail',
    config:{
    	columnLines: true,
    	selType:'rowmodel',
    	params:null
    },
    initComponent: function() {
    	this.store = Ext.create('Ext.data.Store', {
            autoLoad: false,
            model:Ext.create('Ext.data.Model',{
            	fields: [
            	        'TYPE','COMPANY','PHONE','CREATE_TIME','AUDIT_STATUS','CREATE_USER','CREATE_DEPART'
            	]
            }),
            proxy: {
		         type: 'ajax',
		         noCache:true,
		         url: cfg.getCtx()+'/stat/sale/opened/company/detail',
		         reader: {
		             rootProperty: 'data',
            		 totalProperty: 'totalSize'
		         }
		     }
        });
    	this.columns=[Ext.create('Ext.grid.RowNumberer',{width:25}),{
    		text: '类型',
    		width:70,
	        dataIndex: 'TYPE',
	        renderer:function(value,c,r){
	        	var str='';
	        	if(value==0){
	        		str= '平台管理';
	        	}
	        	if(value==1){
	        		str= '供应商';
	        	}
	        	if(value==2){
	        		str= '旅行社';
	        	}
	        	if(value==3){
	        		str= '门市';
	        	}
	        	if(value==4){
	        		str= '分公司';
	        	}
	        	if(value==5){
	        		str= '门市部';
	        	}
	        	if(value==6){
	        		str= '旅游顾问';
	        	}
	        	if(value==7){
	        		str= '子公司';
	        	}
	        	return [
	        		'<div class="ht20 f14">'+str+'</div>'
		        ].join('');
	        }
    	},{
    		text: '公司名称',
	        flex:1,
	        dataIndex: 'COMPANY',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20 f14 blue-color">'+v+'</div>'
		        ].join('');
	        }
    	},{
    		text: '联系电话',
    		width:90,
	        dataIndex: 'PHONE',
	        renderer:function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+(v||'')+'</div>'
		        ].join('');
	        }
    	},{
    		text: '开户时间',
	        width:160,
	        dataIndex: 'CREATE_TIME',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+v+'</a></div>'
		        ].join('');
	        }
    	},{
    		text: '审核状态',
	        width:70,
	        dataIndex: 'AUDIT_STATUS',
	        renderer: function(v,c,r){
	        	var str = '';
	        	v = v ||'0';
	        	if(v=='0'){
	        		str= '待审核';
	        	}
	        	if(v=='1'){
	        		str= '<span class="green-color">通过</span>';
	        	}
	        	if(v=='2'){
	        		str= '<span class="red-color">不通过</span>';
	        	}
	        	return [
	        		'<div class="ht20 f14">'+str+'</div>'
		        ].join('');
	        }
    	},{
    		text: '开户人',
	        width:120,
	        dataIndex: 'CREATE_USER',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+(v||'')+'</div>'
		        ].join('');
	        }
    	},{
    		text: '开户人部门',
	        width:90,
	        dataIndex: 'CREATE_DEPART',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20 f14">'+(v||'')+'</div>'
		        ].join('');
	        }
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