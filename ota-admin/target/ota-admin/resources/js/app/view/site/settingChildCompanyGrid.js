Ext.define('app.view.site.settingChildCompanyGrid', {
	extend:'Ext.grid.Panel',
	xtype:'settingchildcompanygrid',
	loadMask: true,
	border:true,
	emptyText: '没有找到数据',
	initComponent: function() {
		this.store = util.createGridStore(cfg.getCtx()+'/site/company/list',Ext.create('app.model.site.company.model'),'0');
        this.bbar = util.createGridBbar(this.store);
        var columns = Ext.create('app.model.site.company.column');
        columns[2] = Ext.apply(columns[2],{
        	text: '公司名称',
	        flex:1,
	        dataIndex: 'COMPANY',
	        renderer: function(value,c,r){
	        	var h = [],cls="";
	        	if(r.get('IS_SHOW')=='1'){
	        		h.push('<i data-qtip="已推荐" class="iconfont orange-color f18">&#xe644;</i> ');
	        	}
	        	if(r.get('SALE_TRAFFIC')=='1'){
	        		h.push('<i data-qtip="已授权单卖交通，营销费：'+(r.get('TRAFFIC_EXPENSE')||0)+'" class="iconfont blue-color f18">&#xe683;</i> ');
	        	}
	        	if(r.get('SALE_ROUTE')=='1'){
	        		h.push('<i data-qtip="已授权单卖地接线路，营销费：'+(r.get('ROUTE_EXPENSE')||0)+'" class="iconfont orange-color f18">&#xe682;</i> ');
	        	}
	        	if(r.get('SHARE_ROUTE')=='1'){
	        		h.push('<i data-qtip="已授权线路共享" class="iconfont green-color f18">&#xe63e;</i> ');
	        	}
	        	if(r.get('IS_ALONE')=='1'){
	        		h.push('<i data-qtip="独立运营" class="iconfont blue-color f18">&#xe685;</i> ');
	        		cls = 'bold f14 blue-color';
	        	}
	        	if(r.get('IS_CONTRACT_MUST')=='1'){
	        		h.push('<i data-qtip="下订单必须选择合同" class="iconfont green-color f18">&#xe686;</i> ');
	        	}
	        	if(r.get('IS_COUNTRY')=='1'||r.get('IS_WORLD')=='1'){
	        		var str = '';
	        		if(r.get('IS_COUNTRY')=='1'){
	        			str += '国内专线';
	        		}
	        		if(str!=''){
	        			str+='/';
	        		}
	        		if(r.get('IS_WORLD')=='1'){
	        			str += '出境专线';
	        		}
	        		h.push('<i data-qtip="'+str+'" class="iconfont blue-grey-color f18">&#xe684;</i> ');
	        	}
	        	return h.join('')+'<span class="'+cls+'">'+value+'</span>';
	        }
        });
        columns.splice(2,0,{
	        text: '角色',
	        width:100,
	        dataIndex: 'ROLE_NAME'
	    });
        columns.splice(columns.length-1,0,{
        	text: '来源',
        	width:80,
        	dataIndex: 'SOURCE',
        	 renderer: function(value){
	        	if(value==0){
	        		return '<span class="blue-color">系统开通</span>';
	        	}
	        	if(value==1){
	        		return '<span class="green-color">前台注册</span>';
	        	}
	        }
        },{
	        text: '审核',
	        width:80,
	        dataIndex: 'AUDIT_STATUS',
	        renderer: function(value,c,r){
	        	if(value==0){
	        		return '待审核';
	        	}
	        	if(value==1){
	        		return '通过 <i class="iconfont orange-color f14" data-qtip="审核：'+r.get("AUDIT_USER")+'<br>时间：'+r.get("AUDIT_TIME")+'">&#xe671;</i>';
	        	}
	        	if(value==2){
	        		return '不通过 <i class="iconfont orange-color f14" data-qtip="审核：'+r.get("AUDIT_USER")+'<br>时间：'+r.get("AUDIT_TIME")+'">&#xe671;</i>';
	        	}
	        }
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
	        		return '门市部';
	        	}
	        	if(value==6){
	        		return '旅游顾问';
	        	}
	        	if(value==7){
	        		return '子公司';
	        	}
	        }
	    });
        this.columns = columns;
        this.callParent();
	},
	viewConfig:{
		enableTextSelection:true
	},
	
	columnLines: true,
	selType:'checkboxmodel'
});