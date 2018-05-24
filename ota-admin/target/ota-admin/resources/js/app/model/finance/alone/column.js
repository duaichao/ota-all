Ext.define('app.model.finance.alone.column', {
	constructor:function(){
		return [
		Ext.create('Ext.grid.RowNumberer',{width:25}),
		{
	        text: '订单编号',
	        groupable: false,
	        width:130,
	        dataIndex: 'NO',
	        renderer: function(v,c,r){
	        	var i = parseInt(r.get('SOURCES')),
	        		sr = ['B2B','B2C'],
	        		srz = ['B','C'],
	        		src = ['#427fed','#ff6600'],
	        		//sri = ['&#xe6a4;','&#xe6a5;'],
	        		qt = "<p>订单来源："+sr[i]+"</p>";
	        	var h = ['<div class="ht20">'];
	        	//h.push('<i class="iconfont orange-color f12" data-qtip="'+qt+'">'+sri[rs]+'</i>');
	        	h.push('<span style="border-radius:2px;font-size:12px;color:#fff;background:'+src[i]+';padding:2px 5px;" data-qtip="'+qt+'">'+srz[i]+'</span> ');
        		h.push('<a href="javascript:;">'+v+'</a>');
        		h.push('</div>');
	        	return h.join('');
	        }
	    },{
	        text: '产品内容',
	        groupable: false,
	        flex:1,
	        summaryType: 'count',
	        summaryRenderer: function(v){
            	var str='';
            	if(Ext.isObject(v)){
            		str = '<div class="disable-color" style="text-align:right;">总计:</div>';
            	}else{
            		str = '<div class="disable-color" style="text-align:right;">共'+v+'笔订单，合计:</div>';
            	}
            	return str;
            },
	        dataIndex: 'PRODUCE_NAME',
	        renderer: function(v,c,r){
	        	var h = [];
	        	
	        	h.push('<a class="f14 ht20 title" href="javascript:;" data-qtip="'+v+'">'+Ext.util.Format.ellipsis(v,25)+'</a>');
	        	
        		return h.join('');
	        }
	    },{
	        text: '结算金额',
	        groupable: false,
	        summaryType: 'sum',
            summaryFormatter: 'fmMoney',
	        width:100,
	        dataIndex: 'INTER_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20">'+util.moneyFormat(v,'f18 green-color')+'</div>'
	        	].join('');
	        }
	    },
	    {
	        text: '状态',
	        width:70,
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
	        		return '<div class="ht20">待打款 </div>';
	        	}
	        }
	    }];
	}
});