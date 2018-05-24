Ext.define('app.view.produce.traffic.detailGrid', {
	extend:'Ext.grid.Panel',
	xtype:'trafficdetailgrid',
	loadMask: true,
	border:true,
	emptyText: '没有找到数据',
	viewConfig:{
 		autoScroll:false
 	},
	initComponent: function() {
		var me = this;
		this.store = util.createGridStore(cfg.getCtx()+'/produce/traffic/list?ruleId='+ruleId+'&startDate='+queryDate,Ext.create('app.model.resource.traffic.model'));
        this.columns = [{
        	text: '出发',
        	align:'center',
        	width:100,
        	dataIndex: 'START_CITY',
        	renderer: function(v,metaData,r,colIndex,store,view){
        		return '<div class="f16 pt5" style="line-height:30px;">'+v+'</div>';
        	}
        },{
        	text: '',
        	width:85,
        	align:'center',
        	sortable: false,
            menuDisabled: true,
        	dataIndex: 'IS_SINGLE',
        	renderer:function(v,m,r){
        		var tools = cfg.getTravelTools(),
	        		type = parseInt(r.get('TYPE')),
	        		toolsIcon = cfg.getTravelToolsIcon();
        		return [
	        		'<div class="ht20"><i class="iconfont f75 bbb-color">&#xe69f;</i></div>',
	        		'<div class="bbb-color ht20" data-qtip="'+tools[type]+'"><i class="iconfont f18">'+toolsIcon[type]+'</i> '+(parseInt(v)==0?'单程':'往返')+'</div>'
	        	].join(''); 
        	}
        },{
        	text: '到达',
        	align:'center',
        	width:100,
        	dataIndex: 'END_CITY',
        	renderer: function(v,metaData,r,colIndex,store,view){
        		return '<div class="f16 pt5" style="line-height:30px;">'+v+'</div>';
        	}
        },{
        	text: '产品名称',
	        flex:1,
	        dataIndex: 'TITLE',
	        renderer: function(v,metaData,r,colIndex,store,view){
	        	return [
	        		'<div class="blue-color f14 ht20" data-qtip="'+v+'">'+v+'</div>',
	        		'<div>'+r.get('CHINA_NAME')+'/'+util.formatMobile(r.get('MOBILE'))+'</div>'
	        	].join('');
	        }
        },{
        	text:'价格',
        	width:120,
        	dataIndex: 'ACTUAL_PRICE',
        	renderer: function(v,metaData,r,colIndex,store,view){
        		return [
	        		'<div class="ht40">'+util.moneyFormat(v,'f20')+'</div>',
	        	].join(''); 
        	}
        },{
        	text: '提前报名',
        	width:120,
        	dataIndex: 'END_DATE',
        	renderer: function(v,metaData,r,colIndex,store,view){
	        	return [
	        		'<div class="yellow-color f14 ht20">提前'+v+'天</div>',
	        		'<div>'+r.get('END_TIME')+'</div>'
	        	].join('');
	        }
        },{
        	text: '座位',
	        width:100,
	        dataIndex: 'ACTUAL_SEAT',
	        renderer: function(v,metaData,r,colIndex,store,view){
	        	return [
	        		'<div class="green-color f14 ht20">'+v+'</div>',
	        		'<div>最少购票'+r.get('MIN_BUY')+'</div>'
	        	].join('');
	        }
        }];
        this.callParent();
	},
	columnLines: true
});