Ext.define('app.model.finance.pay.column', {
	constructor:function(){
		return [
		Ext.create('Ext.grid.RowNumberer',{width:25}),
		{
	        text: '账单编号',
	        groupable: false,
	        width:150,
	        /*summaryType: 'count',
            summaryRenderer: function(v,c,r,e){
            	var str='';
            	if(Ext.isObject(v)){
            		var t = 0;
            		for(var i in v){
            			t += parseInt(v[i]);
            		}
            		str = '<span class="disable-color">共'+t+'笔账单</span>';
            	}else{
            		str = '<a href="javascript:;">共'+v+'笔账单'+no+' <i class="iconfont orange-color" data-qtip="点击查看“订单详情”">&#xe716;</i></a>'
            	}
            	return str;
            },*/
	        dataIndex: 'DETAIL_NO',
	        renderer: function(v,c,r){
	        	var h = [
	        		'<div class="ht20">'
	        	];
	        	h.push(v);
	        	var attr = r.get('ATTR_USER') || '';
	        	if(attr!=''){
	        		h.push('<a data-qtip="上传人：'+r.get('ATTR_USER')+'<br>'+r.get('ATTR_TIME')+'" href="'+cfg.getPicCtx()+'/'+r.get('ATTR_URL')+'" target="_blank"> <i class="iconfont disable-color">&#xe6f4;</i></a>');
	        	}
	        	h.push('</div>');
	        	return h.join('');
	        }
	    },{
	        text: '收款方',
	        groupable: false,
	        width:300,
	        dataIndex: 'COMPANY',
	        renderer: function(v,c,r){
	        	var h = [
	        		'<div class="ht20">'+v+'</div>'
	        	];
	        	return h.join('');
	        }
	    },{
	        text: '公司类型',
	        groupable: false,
	        width:70,
	        summaryType: 'count',
            summaryRenderer: function(v){
            	var str='';
            	if(Ext.isObject(v)){
            		str = '<div class="disable-color">总计:</div>';
            	}else{
            		str = '<div class="disable-color">合计:</div>';
            	}
            	return str;
            },
	        dataIndex: 'COMPANY_TYPE',
	        renderer: function(v,c,r){
	        	var h = [];
	        	h.push('<div class="ht20">');
	        	if(parseInt(v)==0){
	        		h.push('平台');
	        	}
	        	if(parseInt(v)==1){
	        		h.push('供应商');
	        	}
	        	if(parseInt(v)==2){
	        		h.push('旅行社');
	        	}
	        	h.push('</div>');
	        	return h.join('');
	        }
	    },{
	        text: '对账金额',
	        groupable: false,
	        summaryType: 'sum',
            summaryFormatter: 'fmMoney',
	        width:150,
	        dataIndex: 'ACCOUNT_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20">'+util.moneyFormat(v,'f18')+'</div>'
	        	].join('');
	        }
	    },
		{
	        text: '银行名称',
	        width:200,
	        groupable: false,
	        dataIndex: 'BANK_TITLE',
	        renderer:function(v,c,r){
	        	v = v ||'';
	        	return [
	        		'<div class="ht20">'+v+'</div>',
	        	].join('');
	        }
	    },
	    {
	        text: '开户名',
	        width:80,
	        groupable: false,
	        dataIndex: 'BANK_NAME',
	        renderer: function(v,c,r){
	        	v = v ||'';
	        	return [
	        		'<div class="ht20">'+v+'</div>',
	        	].join('');
	        }
	    },
	    {
	        text: '银行卡号',
	        flex:1,
	        groupable: false,
	        dataIndex: 'BANK_NO',
	        renderer: function(v,c,r){
	        	v = v ||'';
	        	return [
	        		'<div class="ht20">'+v+'</div>',
	        	].join('');
	        }
	    },
	    {
	        text: '状态',
	        width:60,
	        groupable: false,
	        dataIndex: 'PAY_USER',
	        renderer: function(v,c,r){
	        	v = v ||'';
	        	if(v!=''){
	        		v = '<span class="green-color" data-qtip="打款人：'+r.get('PAY_USER')+'<br>'+r.get('PAY_TIME')+'">已打款</span>';
	        	}else{
	        		v = '待打款';
	        	}
	        	return [
	        		'<div class="ht20">'+v+'</div>',
	        	].join('');
	        }
	    }];
	}
});