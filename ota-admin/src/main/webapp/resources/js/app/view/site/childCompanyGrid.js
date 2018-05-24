Ext.define('app.view.site.childCompanyGrid', {
	extend:'Ext.grid.Panel',
	xtype:'childcompanygrid',
	loadMask: true,
	emptyText: '没有找到数据',
	initComponent: function() {
		this.store = util.createGridStore(cfg.getCtx()+'/site/company/listCompanyChild',Ext.create('app.model.site.company.model'),'1',20);
        this.bbar = util.createGridBbar(this.store);
        var columns = Ext.create('app.model.site.company.column');
        columns[2] = Ext.apply(columns[2],{
        	text: '公司名称',
	        flex:1,
	        dataIndex: 'COMPANY',
	        renderer: function(value,c,r){
	        	var p = [];
	        	if(r.get('FUNDS_LOG_CNT')){
	        		p.push('<i data-qtip=\'余额：'+r.get('AMOUNT')+'\' class="iconfont orange-color hand f18 act">&#xe633;</i> ');
	        	}
	        	if(r.get('PID')=='-1'){
	        		p.push('<i data-qtip="总公司" class="iconfont blue-color hand f18">&#xe61a;</i> ');
	        		p.push('<span class="blue-color f14 bold">'+value+'</span>');
	        	}else{
	        		p.push(value);
	        	}
	        	return p.join('');
	        }
        });
        /*columns.splice(2,0,{
	        text: '角色',
	        width:100,
	        dataIndex: 'ROLE_NAME',
	        renderer: function(v,c,r){
	        	if(v){
	        		return v;
	        	}else{
	        		return r.get('CHILD_ROLE_NAME');
	        	}
	        }
	    });*/
 		columns.splice(4,0,{
	        text: '总公司',
	        flex:1,
	        dataIndex: 'PARENT_COMPANY'
	    },{
	    	text: '所属城市',
	        width:80,
	        dataIndex: 'CITY_NAME'
	    },{
	    	text: '类型',
	        width:80,
	        dataIndex: 'TYPE',
	        renderer: function(value){
	        	if(value==''){
	        		return '';
	        	}
	        	if(value==0){
	        		return '平台管理';
	        	}
	        	if(value==1){
	        		return '供应商';
	        	}
	        	if(value==2){
	        		return '旅行社';
	        	}
	        	if(value==3){
	        		return '门市';
	        	}
	        	if(value==4){
	        		return '分公司';
	        	}
	        	if(value==5){
	        		return '同行';
	        	}
	        	if(value==6){
	        		return '商务中心';
	        	}
	        	if(value==7){
	        		return '子公司';
	        	}
	        }
	    });
        this.columns = columns;
        this.dockedItems = [{
	    	xtype:'toolbar',
	    	itemId:'companytool',
	    	items:[{
	    		disabled:true,
	    		text:'正在初始化...'
	    	}]
	    }];
        this.callParent();
	},
	viewConfig:{
		enableTextSelection:true
	},
	columnLines: true,
	selModel :{selType : 'checkboxmodel',mode :'SINGLE'}
});