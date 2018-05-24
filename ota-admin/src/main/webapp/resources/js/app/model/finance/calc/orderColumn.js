Ext.define('app.model.finance.calc.orderColumn', {
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
	        width:420,
	        dataIndex: 'PRODUCE_NAME',
	        renderer: function(v,c,r){
	        	var h = [];
	        	
	        	h.push('<a class="f14 ht20 title" href="javascript:;" data-qtip="'+v+'">'+Ext.util.Format.ellipsis(v,25)+'</a>');
	        	
        		return h.join('');
	        }
	    },{
	        text: '游客人数',
	        width:80,
	        dataIndex: 'MAN_COUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20">'+util.format.fmNumber(v+r.get('CHILD_COUNT'))+'</div>'
	        	].join('');
	        }
	    },{
	        text: '旅行社外卖金额',
	        width:120,
	        dataIndex: 'SALE_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20" style="text-align:left;">'+util.moneyFormat(v,'f18 disable-color')+'</div>'
	        	].join('');
	        }
	    },{
	        text: '旅行社实收金额',
	        width:120,
	        dataIndex: 'ACTUAL_SALE_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20" style="text-align:left;">'+util.moneyFormat(r.get('ACTUAL_SALE_AMOUNT'),'f18 green-color')+'</div>'
	        	].join('');
	        }
	    },{
	        text: '供应商同行金额',
	        width:120,
	        dataIndex: 'INTER_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20" style="text-align:left;">'+util.moneyFormat(v,'f18 disable-color')+'</div>'
	        	].join('');
	        }
	    },{
	        text: '供应商实收金额',
	        width:120,
	        dataIndex: 'ACTUAL_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20" style="text-align:left;">'+util.moneyFormat(r.get('ACTUAL_AMOUNT'),'f18 green-color')+'</div>'
	        	].join('');
	        }
	    },{
	        text: '退款金额',
	        width:120,
	        dataIndex: 'REFUND_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20">'+util.moneyFormat(v,'f18 red-color')+'</div>'
	        	].join('');
	        }
	    },{
	        text: '营销费',
	        width:100,
	        dataIndex: 'MARKETING_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20">'+util.moneyFormat(v,'f18 red-color')+'</div>'
	        	].join('');
	        }
	    },{
	        text: '订单类型',
	        width:70,
	        dataIndex: 'PRODUCE_TYPE',
	        renderer: function(v,c,r){
	        	var h = [];
	        	h.push('<div class="ht20">');
	        	
	        	if(parseInt(v)==1){
	        		h.push('交通订单');
	        	}
	        	if(parseInt(v)==2){
	        		h.push('跟团订单');
	        	}
	        	if(parseInt(v)==3){
	        		h.push('地接订单');
	        	}
	        	h.push('</div>');
	        	return h.join('');
	        }
	    },{
	        text: '支付类型',
	        width:70,
	        dataIndex: 'PAY_TYPE',
	        renderer: function(v,c,r){
	        	var h = [];
	        	h.push('<div class="ht20">');
	        	
	        	if(parseInt(v)==0){
	        		h.push('部门余额');
	        	}
	        	if(parseInt(v)==1){
	        		h.push('支付宝');
	        	}
	        	if(parseInt(v)==2){
	        		h.push('财付通');
	        	}
	        	if(parseInt(v)==3){
	        		h.push('网银');
	        	}
	        	h.push('</div>');
	        	return h.join('');
	        }
	    },{
	        text: '支付平台',
	        groupable: false,
	        align:'center',
	        width:80,
	        dataIndex: 'PLATFORM_PAY',
	        renderer: function(v,c,r){
	        	var h = [];
	        	var t=r.get('PLATFORM_PAY');
	        	h.push('<div class="ht20 disable-color">');
	        	
	        	if(parseInt(t)==0){
	        		h.push('未支付');
	        	}
	        	if(parseInt(t)==1){
	        		h.push('B平台支付');
	        	}
	        	if(parseInt(t)==2){
	        		h.push('C平台支付');
	        	}
	        	h.push('</div>');
	        	 

	        	return h.join('');
	        }
	    },
		{
	        text: '订单状态',
	        width:80,
	        dataIndex: 'STATUS',
	        renderer:function(v,c,r){
	        	var txt = ['待付款','付款中','已付款','待退款','退款中','已退款','手动取消','自动取消'],ov = v,
	        		remark = [],tipIcon='';
	        	return [
	        		'<div class="ht20">'+txt[ov]+'</div>',
	        	].join('');
	        }
	    },
	    {
	        text: '对账状态',
	        width:80,
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