Ext.define('app.model.resource.traffic.column', {
	constructor:function(){
		return [
		Ext.create('Ext.grid.RowNumberer',{width:35}),{
        	text: '出发',
        	align:'center',
        	width:100,
        	dataIndex: 'START_CITY',
        	renderer: function(v,metaData,r,colIndex,store,view){
        		return [
        			'<div class="f14" style="line-height:20px;">'+v+'</div>',
        			'<div class="f12 disable-color" style="line-height:20px;">'+(r.get('BEGIN_STATION')||'')+'</div>',
        		].join('');
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
	        		'<div class="ht20"><i class="iconfont f75 bbb-color">&#xe655;</i></div>',
	        		'<div class="bbb-color ht20" data-qtip="'+tools[type]+'"><i class="iconfont f20">'+toolsIcon[type]+'</i> '+(parseInt(v)==0?'单程':'往返')+'</div>'
	        	].join(''); 
        	}
        },{
        	text: '到达',
        	align:'center',
        	width:100,
        	dataIndex: 'END_CITY',
        	renderer: function(v,metaData,r,colIndex,store,view){
        		return [
        			'<div class="f14" style="line-height:20px;">'+v+'</div>',
        			'<div class="f12 disable-color" style="line-height:20px;">'+(r.get('END_STATION')||'')+'</div>',
        		].join('');
        	}
        },{
        	text: '交通名称',
        	dataIndex:'TITLE',
        	flex:1,
	        renderer: function(v,metaData,r,colIndex,store,view){
	        	return [
	        		'<a class="f14 ht16" href="javascript:;" style="width:420px;white-space:normal;display:inline-block;">'+v+'</a>',
	        		'<div class="ht16" style="margin-top:3px;" data-qtip="'+(r.get('ROTE')||'无备注')+'">'+(r.get('ROTE')||'无备注')+'</div>'
	        	].join('');
	        }
        },{
	        text: '提前报名天数',
	        width:120,
	        dataIndex: 'END_DATE',
	        renderer:function(v,c,r){
	        	var h = [];
	        	h.push('<div class="h20" >提前'+v+'天</div>');
	        	h.push('<div class="h20 pt5" >'+r.get('END_TIME')+'</div>');
	        	return h.join('');
	        }
	    }/*,{
	        text: '最近日期',
	        width:120,
	        dataIndex: 'MIN_DATE',
	        renderer: function(value,metaData,record,colIndex,store,view){
	        	return value;
	        }
	    },{
	        text: '销售状态',
	        width:80,
	        dataIndex: 'IS_SALE',
	        renderer: function(value,metaData,record,colIndex,store,view){
	        	var h = [];
	        	if(value=='1'){
	        		if(record.get('SALE_STATUS')=='0'){
	        			h.push('<span class="green-color">在售</span>');
	        		}else{
	        			h.push('<span class="red-color">停售</span>');
	        		}
	        		h.push('<div class="h40 pt5" data-qtip="最小购票数'+record.get('MIN_BUY')+'">≥ '+record.get('MIN_BUY')+'</div>');
	        	}else{
	        		h.push('<span class="disable-color">不可售</span>');
	        	}
	        	return h.join('');
	        }
	    }*/,{
	        text: '发布状态',
	        width:100,
	        dataIndex: 'IS_PUB',
	        renderer: function(value,metaData,record,colIndex,store,view){
	        	var h = [];
	        	var rc = record.get('ROUTE_TRAFFIC_CNT')||'0';
	        	if(value=='1'){
	        		h.push('<div class="blue-color">已发布</div>');
	        	}else if(value=='2'){
	        		h.push('<div class="red-color">已停售</div>');
	        	}else{
	        		h.push('<div class="gray-color">未发布</div>');
	        	}
	        	h.push('<div class="ht30">');
	        	if(record.get('IS_SALE')=='1'){
	        		h.push('<span class="orange-color">最小购票数'+record.get('MIN_BUY')+'</span>');
	        	}
	        	if(parseInt(rc)>0){
	        		h.push('<a href="#" click="javascript:;" class="green-color zy">线路占用数'+rc+'</a>');
	        	}
	        	h.push('</div>');
	        	return h.join('');
	        }
	    }];
	}
});