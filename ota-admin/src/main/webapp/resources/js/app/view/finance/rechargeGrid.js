Ext.define('app.view.finance.rechargeGrid', {
	extend:'Ext.grid.Panel',
	xtype:'rechargegrid',
	loadMask: true,
	multiColumnSort: true,
	viewConfig:{
		stripeRows:false
	},
	features: [{
        ftype : 'groupingsummary',
        groupHeaderTpl :[
		    '<span>{[this.format(values)]}</span>',
		    {
		        format: function(o) {
	        		var n = o.name,
	        			o1 = n.split('**'),
	        			url = o1[0],
	        			msg = o1[1],
	        			no = o1[2];
	        		if(url=='无'){
	        			return msg+'：共'+o.rows.length+'笔充值，还未上传付款凭证';
	        		}
	        		return no+' '+msg+'：共'+o.rows.length+'笔充值，<a href="javascript:;" target="_blank" onclick="util.downloadAttachment(\''+cfg.getPicCtx()+'/'+url+'\')">下载付款凭证</a>';
		        }
		    }
		],
        hideGroupedHeader : false,
        id:'restaurantGrouping',
        enableGroupingMenu : false
    }, {
        ftype: 'summary',
        dock: 'bottom'
    }],
	initComponent: function() {
		var me = this;
		this.store = Ext.create('Ext.data.Store', {
            pageSize: 50,
            groupField:'ALONE_ATTR',
            autoLoad: false,
            model:Ext.create('Ext.data.Model',{
            	fields: [
            		'MONEY','ALONE_ATTR','NOTE','ALONE_STATUS','USER_NAME',
            		'COMPANY','TEXT','OPET_TIME'
            	]
            }),
            proxy: {
		         type: 'ajax',
		         noCache:true,
		         url: cfg.getCtx()+'/finance/alone/recharge',
		         reader: {
		             rootProperty: 'data',
            		 totalProperty: 'totalSize'
		         }
		     }
        });
		
		
        //this.bbar = util.createGridBbar(this.store);
        this.columns = [Ext.create('Ext.grid.RowNumberer',{width:25}),{
        	text: '充值公司',
	        groupable: false,
	        flex:1,
	        dataIndex: 'COMPANY'
        },{
        	text: '充值人',
        	width:80,
        	dataIndex: 'USER_NAME',
        	renderer: function(v,c,r){
        		//『'+r.get('TEXT')+'』 '
	        	return [
	        		'<div class="ht20">'+v+'</div>'
	        	].join('');
	        },
	        summaryType: 'count',
	        summaryRenderer: function(v){
            	var str='';
            	if(Ext.isObject(v)){
            		str = '<div class="disable-color" style="text-align:right;">总计:</div>';
            	}else{
            		str = '<div class="disable-color" style="text-align:right;">共'+v+'笔充值，合计:</div>';
            	}
            	return str;
            }
        },{
        	text: '充值金额',
	        summaryType: 'sum',
            summaryFormatter: 'fmMoney',
	        width:90,
	        dataIndex: 'MONEY',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20">'+util.moneyFormat(v,'f18 green-color')+'</div>'
	        	].join('');
	        }
        },{
        	text: '充值时间',
        	width:130,
        	dataIndex: 'OPET_TIME',
        	renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20">'+v+'</div>'
	        	].join('');
	        }
        },{
        	text: '状态',
	        width:55,
	        groupable: false,
	        dataIndex: 'ALONE_STATUS',
	        renderer: function(v,c,r){
	        	v = parseInt(v) ||'';
	        	if(v==1){
	        		var h =[],attr = r.get('ATTR_URL') || '';
	        		h.push('<div class="ht20 green-color">已打款 </div>');
	        		/*if(attr!=''){
	        			h.push('<a href="'+cfg.getPicCtx()+'/'+attr+'" target="_blank"><i class="iconfont" data-qtip="下载付款凭证" style="color:#888;">&#xe6f4;</i></a>');
	        		}*/
	        		return h.join('');
	        	}
	        	if(v==0){
	        		return '<div class="ht20">待打款</div>';
	        	}
	        }
        }];
        
        
    	
        this.callParent();
	},
	columnLines: true
});