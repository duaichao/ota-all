Ext.define('app.model.finance.calc.column', {
	constructor:function(p){
		var grid = p.grid;
		return [
		Ext.create('Ext.grid.RowNumberer',{width:25}),
		{
	        text: '订单编号/状态',
	        groupable: false,
	        width:120,
	        dataIndex: 'NO',
	        editor: {
	            xtype: 'textfield'
	        },
	        items    : {
	            xtype: 'textfield',
	            flex : 1,
	            margin: 2,
	            width:115,
	            enableKeyEvents: true,
	            listeners: {
	                keyup: function(filterField){
		        	    var store = grid.getStore();
	        	    	var params = store.getProxy().getExtraParams();
	        	    	store.getProxy().setExtraParams(Ext.applyIf({
	        	    		no:filterField.value
	        	    	},params));
	        	    	store.load();
	                },
	                buffer: 500
	            }
	        },
	        renderer: function(v,c,r){
	        	var h = [
	        		'<div class="ht20"><a href="javascript:;">'+v+'</a></div>'
	        	],t=r.get('PRODUCE_TYPE');
	        	var txt = ['待付款','付款中','已付款','待退款','退款中','已退款','手动取消','自动取消'],
	        		cor = ['yellow-color','yellow-color','green-color','red-color','red-color','red-color','gray-color','gray-color'],
		        	ov = r.get('STATUS'),
	        		remark = [],tipIcon='';
	        	h.push('<div class="ht20 '+cor[ov]+'">');
	        	if(parseInt(t)==1){
	        		h.push('<i class="iconfont f18" data-qtip="交通订单">&#xe683;</i> ');
	        	}
	        	if(parseInt(t)==2){
	        		h.push('<i class="iconfont f18" data-qtip="跟团订单">&#xe685;</i> ');
	        	}
	        	if(parseInt(t)==3){
	        		h.push('<i class="iconfont f18" data-qtip="地接订单">&#xe694;</i> ');
	        	}
	        	
	        	
	        	h.push(txt[ov]+' <i class="iconfont orange-color hand f18" data-qtip="点击查看“状态跟踪”">&#xe6ae;</i>');
	        	h.push('</div>');
	        	return h.join('');
	        }
	    },{
	        text: '对账编号/状态',
	        groupable: false,
	        width:120,
	        dataIndex: 'ACCOUNT_DETAIL_NO',
	        editor: {
	            xtype: 'textfield'
	        },
	        items    : {
	            xtype: 'textfield',
	            flex : 1,
	            margin: 2,
	            width:115,
	            enableKeyEvents: true,
	            listeners: {
	                keyup: function(filterField){
	                	var store = grid.getStore();
	        	    	var params = store.getProxy().getExtraParams();
	        	    	store.getProxy().setExtraParams(Ext.applyIf({
	        	    		no:filterField.value
	        	    	},params));
	        	    	store.load();
	                },
	                buffer: 500
	            }
	        },
	        renderer: function(b,c,r){
	        	b = b ||'<span class="disable-color">无</span>';
	        	var h = [
	        		'<div class="ht20"><a href="javascript:;">'+b+'</a></div>'
	        	];
	        	
	        	
	        	var v = parseInt(r.get('ACCOUNT_STATUS') ||'0'),
	        		status = parseInt(r.get('STATUS')),
	        		way = parseInt(r.get('ACCOUNT_WAY'));
	        	
	        	if(v==1){
	        		h.push('<div class="ht20 green-color">已对账</div>');
	        	}else if(v==2){
	        		h.push('<div class="ht20 green-color">已打款</div>');
	        	}else if(cfg.getUser().companyType==0){
	        		if(way==3){
	        			if(status==2||status==5){
	        				h.push('<div class="ht20">待对账</div>');
	        			}else if(status==6||status==7){
	        				h.push('<div class="ht20 disable-color">非对账</div>');
	        			}else{
	        				h.push('<div class="ht20 disable-color">等待中..</div>');
	        			}
	        		}else{
	        			h.push('<div class="ht20 disable-color">非对账</div>');
	        		}
	        	}else{
	        		if(cfg.getUser().pid=='-1'){
	        			if(way == 2 && r.get('BUY_COMPANY_PID_ID') == cfg.getUser().companyId){
	        				if(status==2||status==5){
		        				h.push('<div class="ht20">待对账</div>');
		        			}else if(status==6||status==7){
		        				h.push('<div class="ht20 disable-color">非对账</div>');
		        			}else{
		        				h.push('<div class="ht20 disable-color">等待中..</div>');
		        			}	
	        			}else if(way == 1 && r.get('BUY_COMPANY_ID') == cfg.getUser().companyId){
	        				if(status==2||status==5){
		        				h.push('<div class="ht20">待对账</div>');
		        			}else if(status==6||status==7){
		        				h.push('<div class="ht20 disable-color">非对账</div>');
		        			}else{
		        				h.push('<div class="ht20 disable-color">等待中..</div>');
		        			}	
	        			}else{
	        				h.push('<div class="ht20 disable-color">非对账</div>');
	        			}
	        		}else{
	        			if(way == 1 && r.get('BUY_COMPANY_ID') == cfg.getUser().companyId){
	        				if(status==2||status==5){
		        				h.push('<div class="ht20">待对账</div>');
		        			}else if(status==6||status==7){
		        				h.push('<div class="ht20 disable-color">非对账</div>');
		        			}else{
		        				h.push('<div class="ht20 disable-color">等待中..</div>');
		        			}	
	        			}else{
	        				h.push('<div class="ht20 disable-color">非对账</div>');
	        			}
	        		}
	        	}
	        	return h.join('');
	        }
	    },{
	        text: '产品内容',
	        groupable: false,
	        width:520,
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
	        	
	        	h.push('<a class="f14 ht20 title"  style="width:500px;white-space:normal;display:inline-block;" href="javascript:;">'+v+'</a>');
	        	
        		return h.join('');
	        }
	    },{
	        text: '销售金额',
	        hidden:true,
	        groupable: false,
	        summaryType: 'sum',
            summaryFormatter: 'fmOrangeMoney',
	        width:120,
	        dataIndex: 'SALE_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20">'+util.moneyFormat(v,'f18 disable-color')+' <i class="iconfont orange-color" data-qtip="点击查看“资金流水”">&#xe716;</i></div>'
	        	].join('');
	        }
	    },{
	        text: '游客人数',
	        groupable: false,
	        summaryType: 'sum',
            summaryFormatter: 'fmNumber',
	        width:80,
	        dataIndex: 'PERSON_COUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht40">'+util.format.fmNumber(v)+'</div>'
	        	].join('');
	        }
	    },{
	        text: '旅行社外卖金额',
	        groupable: false,
	        summaryType: 'sum',
            summaryFormatter: 'fmOrangeMoney',
	        width:120,
	        dataIndex: 'SALE_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht40" style="text-align:left;">'+util.moneyFormat(v,'f18 disable-color')+'</div>'
	        	].join('');
	        }
	    },{
	        text: '旅行社实收金额',
	        groupable: false,
	        summaryType: 'sum',
            summaryFormatter: 'fmOrangeMoney',
	        width:120,
	        dataIndex: 'ACTUAL_SALE_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht40" style="text-align:left;">'+util.moneyFormat(r.get('ACTUAL_SALE_AMOUNT'),'f18 green-color')+'</div>'
	        	].join('');
	        }
	    },{
	        text: '供应商同行金额',
	        groupable: false,
	        summaryType: 'sum',
            summaryFormatter: 'fmOrangeMoney',
	        width:120,
	        dataIndex: 'INTER_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht40" style="text-align:left;">'+util.moneyFormat(v,'f18 disable-color')+'</div>'
	        	].join('');
	        }
	    },{
	        text: '供应商实收金额',
	        groupable: false,
	        summaryType: 'sum',
            summaryFormatter: 'fmOrangeMoney',
	        width:120,
	        dataIndex: 'ACTUAL_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht40" style="text-align:left;">'+util.moneyFormat(r.get('ACTUAL_AMOUNT'),'f18 green-color')+'</div>'
	        	].join('');
	        }
	    },{
	        text: '退款金额',
	        width:120,
	        groupable: false,
	        summaryType: 'sum',
            summaryFormatter: 'fmOrangeMoney',
	        dataIndex: 'REFUND_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht40">'+util.moneyFormat(v,'f18 red-color')+'</div>'
	        	].join('');
	        }
	    },{
	        text: '营销费',
	        hidden:true,
	        groupable: false,
	        summaryType: 'sum',
            summaryFormatter: 'fmMoney',
	        width:80,
	        dataIndex: 'MARKETING_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht40">'+util.moneyFormat(v,'f18 red-color')+'</div>'
	        	].join('');
	        }
	    }/*,{
	        text: '订单类型',
	        groupable: false,
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
	    }*/,{
	        text: '支付<br>类型/平台',
	        groupable: false,
	        align:'center',
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
	    },/*{
	        text: '付款方',
	        align:'center',
	        groupable: false,
	        width:70,
	        dataIndex: 'SOURCES',
	        renderer: function(v,c,r){
	        	
	        	var h = [],s='平台';
	        	h.push('<div class="ht20">');
	        	h.push(s);
	        	h.push('</div>');
	        	
	        	v = v ||'';
	        	var status = parseInt(r.get('STATUS'));
	        	if(cfg.getUser().companyType==0){
	        		if(((r.get('IS_ALONE') == 0 && ((r.get('SOURCES') == 1 && r.get('PAY_TYPE') != 0 && r.get('PLATFORM_ACCOUNT') == 1) || (r.get('SOURCES') == 0))) || (r.get('IS_ALONE') == 1 && r.get('SOURCES') == 1 && r.get('PAY_TYPE') != 0 && r.get('PLATFORM_ACCOUNT') == 1))){
	        			return '<div class="ht20">平台</div>';
	        		}else{
	        			return '<div class="ht20">旅行社</div>';
	        		}
	        	}else{
	        		if(r.get('IS_ALONE')==1){
	        			if((r.get('IS_ALONE') == 1 && ((r.get('SOURCES') == 1 && r.get('PAY_TYPE') != 0 && r.get('PLATFORM_ACCOUNT') == 0) || (r.get('SOURCES') == 0)))){
	        				return '<div class="ht20">旅行社</div>';
	        			}else{
	        				return '<div class="ht20">平台</div>';
	        			}
	        		}else{
	        			if((r.get('IS_ALONE') == 0 && r.get('SOURCES') == 1 && r.get('PAY_TYPE') != 0 && r.get('PLATFORM_ACCOUNT') == 0)){
	        				return '<div class="ht20">旅行社</div>';
	        			}else{
	        				return '<div class="ht20">平台</div>';
	        			}
	        		}
	        	}
	        	
	        }
	    },*/{
	    	text: '旅行社/来源/付款方',
	    	align:'center',
	        groupable: false,
	        width:180,
	        dataIndex: 'BUY_COMPANY',
	        renderer: function(t,c,r){
	        	var h = [],s='';
	        	h.push('<div class="ht20 f14" style="text-align:left;color:#455A64;">'+t+'</div>');
	        	
	        	var t1 = parseInt(r.get('SOURCES')||'0'),
	        		sr = ['B2B','B2C'],
	        		srs = ['blue-color','orange-color'];
	        	
	        	v = r.get('ACCOUNT_WAY') ||'';
	        	/*var status = parseInt(r.get('STATUS'));
	        	if(cfg.getUser().companyType==0){
	        		if(((r.get('IS_ALONE') == 0 && ((r.get('SOURCES') == 1 && r.get('PAY_TYPE') != 0 && r.get('PLATFORM_ACCOUNT') == 1) || (r.get('SOURCES') == 0))) || (r.get('IS_ALONE') == 1 && r.get('SOURCES') == 1 && r.get('PAY_TYPE') != 0 && r.get('PLATFORM_ACCOUNT') == 1))){
	        			h.push('<div class="ht20 disable-color" style="text-align:left;">平台</div>');
	        		}else{
	        			h.push('<div class="ht20 disable-color" style="text-align:left;">旅行社</div>');
	        		}
	        	}else{
	        		if(r.get('IS_ALONE')==1){
	        			if((r.get('IS_ALONE') == 1 && ((r.get('SOURCES') == 1 && r.get('PAY_TYPE') != 0 && r.get('PLATFORM_ACCOUNT') == 0) || (r.get('SOURCES') == 0)))){
	        				h.push('<div class="ht20 disable-color" style="text-align:left;">旅行社</div>');
	        			}else{
	        				h.push('<div class="ht20 disable-color" style="text-align:left;">平台</div>');
	        			}
	        		}else{
	        			if((r.get('IS_ALONE') == 0 && r.get('SOURCES') == 1 && r.get('PAY_TYPE') != 0 && r.get('PLATFORM_ACCOUNT') == 0)){
	        				h.push('<div class="ht20 disable-color" style="text-align:left;">旅行社</div>');
	        			}else{
	        				h.push('<div class="ht20 disable-color" style="text-align:left;">平台</div>');
	        			}
	        		}
	        	}*/
	        	h.push('<div class="ht20 disable-color" style="text-align:left;">');
	        	h.push('<span class="'+srs[t1]+'">'+sr[t1]+'</span> ');
	        	if(v=='1'){
	        		h.push('旅行社');
	        	}
				if(v=='2'){
					h.push('集团公司');       		
				}
				if(v=='3'){
					h.push('平台');
				}
				h.push('</div>');
	        	return h.join('');
	        }
	    }/*,
		{
	        text: '订单状态',
	        width:80,
	        groupable: false,
	        dataIndex: 'STATUS',
	        renderer:function(v,c,r){
	        	var txt = ['待付款','付款中','已付款','待退款','退款中','已退款','手动取消','自动取消'],ov = v,
	        		remark = [],tipIcon='';
	        	return [
	        		'<div class="ht20">'+txt[ov]+' <i class="iconfont orange-color" data-qtip="点击查看“状态跟踪”">&#xe716;</i></div>',
	        	].join('');
	        }
	    }*//*,
	    {
	        text: '对账状态',
	        width:80,
	        groupable: false,
	        dataIndex: 'ACCOUNT_STATUS',
	        renderer: function(v,c,r){
	        	v = v ||'';
	        	var status = parseInt(r.get('STATUS'));
	        	if(cfg.getUser().companyType==0){
	        		if(((r.get('IS_ALONE') == 0 && ((r.get('SOURCES') == 1 && r.get('PAY_TYPE') != 0 && r.get('PLATFORM_ACCOUNT') == 1) || (r.get('SOURCES') == 0))) || (r.get('IS_ALONE') == 1 && r.get('SOURCES') == 1 && r.get('PAY_TYPE') != 0 && r.get('PLATFORM_ACCOUNT') == 1))){
	        			if(status==2||status==5){
	        				if(r.get('ACCOUNT_STATUS')==1){
	        					return '<div class="ht20 green-color">对账完成</div>';
	        				}else if(r.get('ACCOUNT_STATUS')==2){
	        					return '<div class="ht20 green-color">打款完成</div>';
	        				}else{
	        					return '<div class="ht20">待对账</div>';
	        				}
	        			}else if(status==6||status==7){
	        				return '<div class="ht20 disable-color">非对账</div>';
	        			}else{
	        				return '<div class="ht20 disable-color">等待中..</div>';
	        			}

	        		}else{
	        			return '<div class="ht20 disable-color">非对账</div>';
	        		}
	        	}else{
	        		if(r.get('IS_ALONE')==1){
	        			if((r.get('IS_ALONE') == 1 && ((r.get('SOURCES') == 1 && r.get('PAY_TYPE') != 0 && r.get('PLATFORM_ACCOUNT') == 0) || (r.get('SOURCES') == 0)))){
	        				if(status==2||status==5){
		        				if(r.get('ACCOUNT_STATUS')==1){
		        					return '<div class="ht20 green-color">对账完成</div>';
		        				}else if(r.get('ACCOUNT_STATUS')==2){
		        					return '<div class="ht20 green-color">打款完成</div>';
		        				}else{
		        					return '<div class="ht20">待对账</div>';
		        				}
		        			}else if(status==6||status==7){
		        				return '<div class="ht20 disable-color">非对账</div>';
		        			}else{
		        				return '<div class="ht20 disable-color">等待中..</div>';
		        			}
	        			}else{
	        				return '<div class="ht20 disable-color">非对账</div>';
	        			}
	        		}else{
	        			if((r.get('IS_ALONE') == 0 && r.get('SOURCES') == 1 && r.get('PAY_TYPE') != 0 && r.get('PLATFORM_ACCOUNT') == 0)){
	        				if(status==2||status==5){
		        				if(r.get('ACCOUNT_STATUS')==1){
		        					return '<div class="ht20 green-color">对账完成</div>';
		        				}else if(r.get('ACCOUNT_STATUS')==2){
		        					return '<div class="ht20 green-color">打款完成</div>';
		        				}else{
		        					return '<div class="ht20">待对账</div>';
		        				}
		        			}else if(status==6||status==7){
		        				return '<div class="ht20 disable-color">非对账</div>';
		        			}else{
		        				return '<div class="ht20 disable-color">等待中..</div>';
		        			}
	        			}else{
	        				return '<div class="ht20 disable-color">非对账</div>';
	        			}
	        		}
	        	}
	        	
	        }
	    }*/];
	}
});