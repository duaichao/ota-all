Ext.define('app.view.produce.trafficGrid', {
	extend:'Ext.grid.Panel',
	xtype:'protrafficgrid',
	loadMask: true,
	emptyText: '没有找到数据，请根据左边查询，默认出发日期为今天',
	constructor: function(config) {
        /*config = Ext.apply({
            plugins: [{
		        ptype: 'rowexpander',
		        pluginId:'expander',
		        rowBodyTpl : new Ext.XTemplate(
		        	'<div class="g-detail">',
		        	'<ul class="left">',
			        	'<li><h3>{TITLE}</h3></li>',
			        	'<li>{ROTE}</li>',
		        	'</ul>',
		        	'<ul class="right">',
		        		'<li>外卖价：{ACTUAL_PRICE:this.formatMoney}</li>',//{ACTUAL_SEAT:this.formatSeat}
			        	'<li style="color:#999;">同行价：{ACTUAL_INTER_PRICE:this.formatTHMoney}</li>',
		        	'</ul>',
		        	'</div>',
		        {
		            formatMoney: function(v){
		                return util.moneyFormat(v);
		            },
		            formatTHMoney: function(v){
		                return util.moneyFormat(v).replace('money-color','money-color disable-color');
		            },
		            formatSeat: function(v){
		            	return '<span class="green-color f16">'+util.format.number(v||0,'0.####')+'</span>'
		            }
		        })
		    }]
        }, config);*/
        this.callParent([config]);
    },
    listeners:{
        viewready: function () {
            
        }
    },
	initComponent: function() {
		var me = this;
		this.store = util.createGridStore(cfg.getCtx()+cfg.getModuleListUrl()[currViewInPath],Ext.create('app.model.resource.traffic.model'));
        this.bbar = util.createGridBbar(this.store);
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
	        		'<div style="position:relative;line-height:16px;"><i class="iconfont" data-qtip="<span class=\'f14\'>'+r.get('COMPANY')+'</span><br>联系：'+r.get('CHINA_NAME')+'<br>电话：'+util.formatMobile(r.get('MOBILE'))+'" style="color:#999;position:absolute;top:2px;right:0px;">&#xe841;</i><a class="f14 ht20" href="javascript:;" style="width:550px;white-space:normal;display:inline-block;">'+v+'</a></div>',
	        		'<div class="ht16" style="margin-top:3px;" data-qtip="'+(r.get('ROTE')||'无备注')+'">'+(r.get('ROTE')||'无备注')+'</div>'
	        	].join('');
	        }
        },{
        	text:'价格',
        	width:100,
        	dataIndex: 'ACTUAL_PRICE',
        	renderer: function(v,metaData,r,colIndex,store,view){
        		return [
	        		'<div class="ht20">'+util.moneyFormat(v,'f18')+'</div>',
	        		'<div class="ht20">'+util.moneyFormat(r.get('ACTUAL_INTER_PRICE'),'f14 disable-color',true)+'</div>'
	        	].join(''); 
        	}
        },{
        	text: '提前报名',
        	width:90,
        	dataIndex: 'END_DATE',
        	renderer: function(v,metaData,r,colIndex,store,view){
	        	return [
	        		'<div class="yellow-color f14 ht20">提前'+v+'天</div>',
	        		'<div>'+r.get('END_TIME')+'</div>'
	        	].join('');
	        }
        }/*,{
        	text: '供应商',
	        flex:1,
	        dataIndex: 'COMPANY',
	        renderer: function(v,metaData,r,colIndex,store,view){
	        	return [
	        		'<div class="blue-color f14 ht20">'+v+'</div>',
	        		'<div>'+r.get('CHINA_NAME')+'/'+util.formatMobile(r.get('MOBILE'))+'</div>'
	        	].join('');
	        }
        }*/,{
        	text: '座位',
	        width:100,
	        dataIndex: 'ACTUAL_SEAT',
	        renderer: function(v,metaData,r,colIndex,store,view){
	        	return [
	        		'<div class="green-color f14 ht20">'+v+'</div>',
	        		'<div>最少购票'+r.get('MIN_BUY')+'张</div>'
	        	].join('');
	        }
        },{
        	xtype: 'widgetcolumn',
        	width:100,
        	text:'',
        	hidden:(cfg.getUser().departId==''),
        	sortable: false,
            menuDisabled: true,
        	widget: {
                text:'预订',
                cls:'red',
                xtype: 'button',
                margin:'3 0 0 0',
                handler: function(btn) {
                	var rec = btn.getWidgetRecord();
                    document.location.href=cfg.getCtx()+'/produce/traffic/buy?ruleId='+rec.get('ID')+'&queryDate='+rec.get('RQ')+'&trafficId='+rec.get('TRAFFIC_ID');
                }
            }
        }];
        this.dockedItems = [{
    		xtype:'toolbar',
    		itemId:'gridtool',
        	items:[{
        		disabled:true,
        		text:'正在初始化...'
        	}]
    	}];
    	/*this.store.on('load',function(s){
	        view = me.view;
	        var rowIdx = 0;
	        s.each(function(rec) {
	            me.getPlugin('expander').toggleRow(rowIdx++, rec);
	        });
    	});*/
        this.callParent();
	},
	columnLines: true,
	selType:'checkboxmodel'
});