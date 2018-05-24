Ext.define('app.model.finance.calc.queryColumn', {
	constructor:function(){
		return [
		Ext.create('Ext.grid.RowNumberer',{width:25}),
		{
	        text: '订单编号',
	        groupable: false,
	        width:120,
	        dataIndex: 'NO',
	        renderer: function(v,c,r){
	        	var h = [
	        		'<div class="ht20"><a href="javascript:;">'+v+'</a></div>'
	        	];
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
            		var c = 0;
            		for(var p in v){  
            			c+=(v[p]||0);
            		}
            		str = '<div class="disable-color" style="text-align:right;">共'+c+'笔订单，总计:</div>';
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
	        text: '人数',
	        groupable: false,
	        summaryType: 'sum',
            summaryFormatter: 'fmNumber',
	        width:60,
	        dataIndex: 'PERSON_COUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20">'+util.format.fmNumber(v||0,'f18')+'</div>'
	        	].join('');
	        }
	    },{
	        text: '销售金额',
	        groupable: false,
	        summaryType: 'sum',
            summaryFormatter: 'fmMoney',
	        width:100,
	        dataIndex: 'SALE_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20">'+util.moneyFormat(v,'f18 disable-color')+' <i class="iconfont orange-color hand f18" data-qtip="点击查看“资金流水”">&#xe6ae;</i></div>'
	        	].join('');
	        }
	    },{
	        text: '结算金额',
	        groupable: false,
	        summaryType: 'sum',
            summaryFormatter: 'fmGreenMoney',
	        width:100,
	        dataIndex: 'INTER_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20">'+util.moneyFormat(v,'f18 green-color')+'</div>'
	        	].join('');
	        }
	    },{
	        text: '退款金额',
	        width:100,
	        groupable: false,
	        summaryType: 'sum',
            summaryFormatter: 'fmMoney',
	        dataIndex: 'REFUND_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20">'+util.moneyFormat(v,'f18 red-color')+'</div>'
	        	].join('');
	        }
	    },{
	        text: '营销费',
	        groupable: false,
	        summaryType: 'sum',
            summaryFormatter: 'fmMoney',
	        width:80,
	        dataIndex: 'MARKETING_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20">'+util.moneyFormat(v,'f18 red-color')+'</div>'
	        	].join('');
	        }
	    },
	    {
	        text: '实收金额',
	        groupable: false,
	        summaryType: 'sum',
            summaryFormatter: 'fmMoney',
	        width:100,
	        dataIndex: 'ACTUAL_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20">'+util.moneyFormat(v,'f18 green-color')+'</div>',
	        	].join('');
	        }
	    },{
	        text: '付款方',
	        groupable: false,
	        width:70,
	        dataIndex: 'ACCOUNT_WAY',
	        renderer: function(v,c,r){
	        	var h = [];
	        	if(v=='1'){
	        		h.push('<div class="ht20 disable-color" style="text-align:left;">旅行社</div>');
	        	}
				if(v=='2'){
					h.push('<div class="ht20 disable-color" style="text-align:left;">集团公司</div>');       		
				}
				if(v=='3'){
					h.push('<div class="ht20 disable-color" style="text-align:left;">平台</div>');
				}
	        	return h.join('');
	        }
	    },
		{
	        text: '订单状态',
	        width:80,
	        groupable: false,
	        dataIndex: 'STATUS',
	        renderer:function(v,c,r){
	        	var txt = ['待付款','付款中','已付款','待退款','退款中','已退款','手动取消','自动取消'],ov = v,
	        		remark = [],tipIcon='';
	        	return [
	        		'<div class="ht20">'+txt[ov]+' <i class="iconfont orange-color hand f18" data-qtip="点击查看“状态跟踪”">&#xe6ae;</i></div>',
	        	].join('');
	        }
	    },
	    {
	        text: '对账状态',
	        width:80,
	        groupable: false,
	        dataIndex: 'ACCOUNT_STATUS',
	        renderer: function(v,c,r){
	        	v = v ||'';
	        	var h = [],
        		status = parseInt(r.get('STATUS'));
	        	if(v==1){
	        		h.push('<div class="ht20 green-color">已对账</div>');
	        	}else if(v==2){
	        		h.push('<div class="ht20 green-color">已打款</div>');
	        	}else{
    				if(status==2||status==5){
        				h.push('<div class="ht20">待对账</div>');
        			}else if(status==6||status==7){
        				h.push('<div class="ht20 disable-color">非对账</div>');
        			}else{
        				h.push('<div class="ht20 disable-color">等待中..</div>');
        			}	
	        	}
	        	
	        	return h.join('');
	        }
	    }];
	}
});