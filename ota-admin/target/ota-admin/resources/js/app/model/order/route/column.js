Ext.define('app.model.order.route.column', {
	constructor:function(){
		return [
		Ext.create('Ext.grid.RowNumberer',{width:25}),
		{
	        text: '订单编号/合同号',
	        width:140,
	        dataIndex: 'NO',
	        renderer: function(v,metaData,r,ri,c,store,view){
	        	var h = [
	        		'<div class="ht20"><a href="javascript:;">'+v+'</a></div>'
	        	];
	        	if(r.get('IS_LOST')=='1'){
	        		h.push('<div class="money-color" style="line-height:22px;">'+util.getFont('xe670','f17 money-color')+' 掉单</div>');
	        	}
	        	h.push('<div class="ht20 f999">'+(r.get('CON_NO')||'未填写合同'));
	        	h.push('</div>');
	        	//定金
	        	var earnestType = r.get('EARNEST_TYPE'),
	        		lastDate = Ext.Date.parse(r.get('START_DATE'), "Y-m-d"),
	        		dayCount = r.get('EARNEST_DAY_COUNT'),lastDateStr='';
	        	
	        	if(earnestType=='0'){
	        		if(lastDate){
	        			lastDate = Ext.Date.add(lastDate, Ext.Date.DAY, 0-dayCount);
	        		}
	        		lastDateStr ='，余额截止支付日期：<span class="f14 light-blue-color">'+Ext.Date.format(lastDate,'Y/m/d')+'</span>';
	        	}
	        	if(earnestType=='1'){
	        		if(lastDate){
	        			lastDate = Ext.Date.add(lastDate, Ext.Date.DAY, dayCount);
	        		}
	        		lastDateStr ='，代收款截止确认日期：<span class="f14 light-blue-color">'+Ext.Date.format(lastDate,'Y/m/d')+'</span>';
	        	}
	        	if(r.get('IS_EARNEST')!='0'){
	        		/*setTimeout(function() {
		                var row = view.getRow(r);
		                if(row){
		                	var el = Ext.fly(Ext.fly(row).query('.x-grid-cell')[c]).down('div');
		                		el.setStyle('background','#E1BEE7');
		                }
		            }, 50);*/
	        		var djStr ='';
	        		if(r.get('IS_EARNEST')=='1'){
	        			djStr ='#FFA000';
	        			lastDateStr = '已付定金：'+util.moneyFormat(r.get('EARNEST_INTER_AMOUNT'),'f14 light-orange-color')+lastDateStr;
	        		}
	        		if(r.get('IS_EARNEST')=='4'){
	        			djStr ='#FFA000';
	        			lastDateStr="定金付款中";
	        		}
	        		if(r.get('IS_EARNEST')=='2'){
	        			djStr ='#1976D2';
	        			lastDateStr="定金已确认";
	        		}
	        		if(r.get('IS_EARNEST')=='3'){
	        			djStr ='#388E3C';
	        			lastDateStr="定金已付清";
	        		}
	        		if(r.get('IS_EARNEST')=='5'){
	        			djStr ='#dd4b39';
	        			lastDateStr="系统自动退团，请申请退款退取定金";
	        		}
	        		if(r.get('IS_EARNEST')=='6'){
	        			djStr ='#dd4b39';
	        			lastDateStr="定金待退款，等待供应商审核";
	        		}
	        		if(r.get('IS_EARNEST')=='7'){
	        			djStr ='#dd4b39';
	        			lastDateStr="定金退款中";
	        		}
	        		if(r.get('IS_EARNEST')=='8'){
	        			djStr ='#dd4b39';
	        			lastDateStr="定金退款完成";
	        		}
	        		if(r.get('IS_EARNEST')=='9'){
	        			djStr ='#666666';
	        			lastDateStr="系统自动取消";
	        		}
	        		if(r.get('IS_EARNEST')=='10'){
	        			djStr ='#dd4b39';
	        			lastDateStr="系统自动定金退款完成";
	        		}
	        		if(earnestType=='1'){
	        			h.push(' <div style="border-radius:2px;font-size:12px;color:#fff;background:#00796b;padding:2px 4px;position:absolute;right:28px;top:27px;" data-qtip=\'代收款定金预付订单，需要供应商确认\' >代</div>');
	        		}
	        		h.push(' <div style="border-radius:2px;font-size:12px;color:#fff;background:'+djStr+';padding:2px 4px;position:absolute;right:5px;top:27px;" data-qtip=\''+lastDateStr+'\' >定</div>');
	        	}
	        	return h.join('');
	        }
	    },
	    {
	        text: '产品内容',
	        flex:1,
	        dataIndex: 'PRODUCE_NAME',
	        renderer: function(v,metaData,r,ri,c,store,view){
	        	setTimeout(function() {
	                var row = view.getRow(r);
	                if(row){
	                //Capturing the el (I need the div to do the trick)
	                var el = Ext.fly(Ext.fly(row).query('.x-grid-cell')[c]).down('div');
	                var cellWidth = el.getWidth();
	                var wraps = el.query('.d-wrap',false);
	                Ext.each(wraps,function(w,i){
	                	w.setWidth(cellWidth-50);
	                });
	                }
	                //All other codes to build the progress bar
	            }, 50);
	        	var h = [];
	        	/*h.push('<div style="position:absolute;top:5px;right:0px;padding-left:6px;width:30px;border-left:1px dashed #999;">');
	        	h.push('<i class="iconfont info" data-qtip="点击查看线路详情" style="color:#427fed;cursor:pointer;display:block;">&#xe635;</i>');
	        	if(parseInt(r.get('PRODUCE_TYPE'))==2){
	        		h.push('<i class="iconfont f14" data-qtip="跟团订单" style="margin-top:8px;color:#999;display:block;">&#xe668;</i>');
	        	}
	        	if(parseInt(r.get('PRODUCE_TYPE'))==3){
	        		h.push('<i class="iconfont f14" data-qtip="地接订单" style="margin-top:8px;color:#999;display:block;">&#xe667;</i>');
	        	}
	        	h.push('</div>');*/
	        	h.push('<a  class="f14 d-wrap title" href="javascript:;"  style="line-height:20px;display:inline-block;padding:0 0 5px 0;">'+v+'</a>');
	        	
	        	//标签
        		h.push('<div class="product-tag">');
        		if(r.get('PRODUCE_TYPE')=='2'){
	        		h.push('<span class="flash"><i class="iconfont f12" style="top:0px;" data-qtip="跟团订单">&#xe668;</i> 跟团订单</span>');
	        	}
        		if(r.get('PRODUCE_TYPE')=='3'){
	        		h.push('<span class="flash"><i class="iconfont f12" style="top:0px;" data-qtip="地接订单">&#xe667;</i> 地接订单</span>');
	        	}
        		if(r.get('IS_ALONE')==0){
        			//var dl = ['非独立','独立'];
	        		//h.push('<span class="flash flash-warn" style="padding:2px 5px;margin-right:3px;">'+dl[r.get('IS_ALONE')]+'</span>');
        			h.push('<span class="flash flash-warn">非独立</span>');
	        	}
        		
        		
        		h.push('</div>');
	        	if(r.get('DISCOUNT_TITLE')&&r.get('STATUS')=='2'){
	        		h.push('<div class="ht20" style="color:#E91E63">');
	        		h.push('<span style="background:#E91E63;padding:1px 2px;color:#fff;">参加活动</span> ');
	        		h.push(r.get('DISCOUNT_TITLE'));
					if(r.get('DISCOUNT_TOTAL_AMOUNT')>0){
						h.push('，立减'+r.get('DISCOUNT_TOTAL_AMOUNT')+'元');
					}else{
						h.push('，订单金额过小无法免减');
					}
	        		h.push('</div>');
	        	}
	        	var cstep = r.get('CON_STEP')||'',
	        		supplyConStr = '';
	        	if(cstep==''){
	        		supplyConStr = '供应商未上传合同';
	        	}
	        	if(cstep=='0'){
	        		supplyConStr = '合同已上传';
	        	}
	        	if(cstep=='1'){
	        		supplyConStr = '待确认合同';
	        	}
	        	if(cstep=='2'){
	        		supplyConStr = '合同已确认 <a target="_blank" data-qtip="下载合同" class="iconfont gray-color" href = "'+cfg.getPicCtx()+'/'+r.get('CON_ATTR')+'">&#xe648;</a>';
	        	}
	        	if(supplyConStr!=''){
	        		h.push('<div class="ht20 f999">'+supplyConStr+'</div>');
	        	}
	        	
        		return h.join('');
	        }
	    },{
	    	text: '出发日期',
	        width:100,
	        dataIndex: 'START_DATE',
	        renderer: function(v,metaData,r,ri,c,store,view){
	        	
	        	setTimeout(function() {
	                var row = view.getRow(r);
	                if(row){
	                	var el = Ext.fly(Ext.fly(row).query('.x-grid-cell')[c]);
	                	if(r.get('START_CONFIRM')=='1'){
	                		el.setStyle('background','#C8E6C9');
	                	}
	                }
	            }, 50);
	        	
	        	
	        	v = v||'';
	        	if(v==''){
	        		return v;
	        	}
	        	v = v.replace(/-/g,'/');
	        	var weeks = ["日", "一", "二", "三", "四", "五", "六"],
	        		de = new Date(v).getDay(); 
	        	var s = '';
	        	if(r.get('START_CONFIRM')=='1'){
            		s=' <i class="iconfont f14 green-color" style="top:0px;" data-qtip="已确认出团">&#xe692;</i>';
            	}
	        	return [
	        		'<div class="f14" style="padding:10px 0;">'+v+'</div>',
	        		'<div class="ht20">星期'+weeks[de]+s+'</div>'
	        	].join('');
	        	
	        }
	    },{
	    	text: '人数',
	        width:80,
	        dataIndex: 'MAN_COUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div style="margin:10px 0;font-weight:bold;font-size:14px;">'+v+'</div>',
	        		'<div style="font-weight:bold;font-size:14px;">'+r.get('CHILD_COUNT')+' <i class="iconfont disable-color f18" data-qtip="儿童">&#xe66c;</i></div>'
	        	].join('');
	        }
	    },
	    {
	        text: '订单金额',
	        width:120,
	        dataIndex: 'SALE_AMOUNT',
	        renderer: function(v,c,r){
	        	var re = [],f=r.get('SALE_FLOAT')||'0',t=r.get('AUDIT_INTER_FLOAT')||'0',wmIcon='',thIcon = '',cls='';
	        	if(f!='0'){
	        		if(parseInt(r.get('SALE_FLOAT'))>0){
	        			cls=' d-txt-down';
	        		}
	        		wmIcon = ' <i class="iconfont'+cls+'" style="position:absolute;color:#999;right:0px;top:15px;" data-qtip=\'议价前总金额：'+util.moneyFormat(r.get('OLD_SALE_AMOUNT'),'f14 light-orange-color')+'，议价金额：'+util.moneyFormat(r.get('SALE_FLOAT'),'f14 light-orange-color')+'\'>&#xe63c;</i>';
	        	}
	        	re.push('<div style="margin:10px 0;">'+util.moneyFormat(v,'f20 bd400')+wmIcon+'</div>');
	        	if(t=='1'){
	        		thIcon = ' <div class="timer" style="position:absolute;top:47px;right:0px;" data-qtip=\'待审核，议价金额：'+util.moneyFormat(r.get('INTER_FLOAT_TEMP'),'f14 light-orange-color')+'\'></div>';
	        	}
	        	if(t=='2'){
	        		if(parseInt(r.get('INTER_FLOAT_TEMP'))>0){
	        			cls=' d-txt-down';
	        		}
	        		thIcon = ' <i class="iconfont'+cls+'" style="position:absolute;color:#259b24;top:47px;right:0px;" data-qtip=\'审核通过，议价前总金额：'+util.moneyFormat(r.get('OLD_INTER_AMOUNT'),'f14 light-orange-color')+'，议价金额：'+util.moneyFormat(r.get('INTER_FLOAT_TEMP'),'f14 light-orange-color')+'\'>&#xe63c;</i>';
	        	}
	        	if(t=='3'){
	        		if(parseInt(r.get('INTER_FLOAT_TEMP'))>0){
	        			cls=' d-txt-down';
	        		}
	        		thIcon = ' <i class="iconfont'+cls+'" style="position:absolute;color:#e51c23;top:47px;right:0px;" data-qtip=\'审核不通过，议价金额：'+util.moneyFormat(r.get('INTER_FLOAT_TEMP'),'f14 light-orange-color')+'\'>&#xe63c;</i>';
	        	}
	        	re.push('<div style="padding-right:15px;"><a style="width:100%;display:inline-block;" class="money-body">'+util.moneyFormat(r.get('INTER_AMOUNT'),'f18 bd400 disable-color')+'</a>'+thIcon+'</div>');
	        	
	        	
	        	return re.join('');
	        }
	    },
	    {
	        text: '下单/付款',
	        width:100,
	        dataIndex: 'CREATE_TIME',
	        renderer: function(v,c,r){
	        	
	        	var payKey = parseInt(r.get('PLATFORM_PAY')||'0'),
	        		sourceKey = parseInt(r.get('SOURCES')||'0'),
	        		platKey = parseInt(r.get('PLAT_FROM')||'0');
	        	
	        	
	        	
	        	var map = ['B2B','B2C','微店'],
	        		mapCls = ['blue-color','orange-color','green-color'];
	        	
	        	
	        	
	        	
	        	return [
	        		'<div style="margin:10px 0;"><span class="'+mapCls[platKey]+'">'+map[platKey]+'</span> '+v+'</div>',
	        		'<div style=""><span class="'+(platKey>1?mapCls[platKey]:mapCls[payKey-1<0?0:payKey-1])+'">'+((platKey>1?map[platKey]:map[payKey-1<0?0:payKey-1]))+'</span> '+(r.get('PAY_TIME')||'<span style="color:#9e9e9e">未付款</span>')+'</div>'
	        	].join('');
	        }
	    }/*,
	    {
	        //text: '来源/独立',
	        text: '独立',
	        align:'center',
	        width:55,
	        dataIndex: 'SOURCES',
	        renderer: function(v,c,r){
	        	var sr = ['B2B','B2C'],
	        		srs = ['blue-color','orange-color'],
	        		dl = ['非独立','独立'],
	        		dls = ['','red-color'];
	        	return [
	        		//'<div class="ht20 '+srs[v]+'">'+sr[v]+'</div>',
	        		'<div class="ht40 '+dls[r.get('IS_ALONE')]+'">'+dl[r.get('IS_ALONE')]+'</div>'
	        	].join('');
	        }
	    }*/,
		{
	        text: '订单状态',
	        width:120,
	        dataIndex: 'STATUS',
	        renderer:function(v,c,r){
	        	var txt = ['待付款','付款中','已付款','待退款','退款中','已退款','手动取消订单','系统自动取消'],ov = v,
	        		remark = [],tipIcon='';
	        	
	        	
	        	
	        	if(v==2||v==5||v==6||v==7){
	        		v = 100;
	        	}
	        	if(v==0||ov==3){
	        		v = 30;
	        	}
	        	if(v==1||v==4){
	        		v = 50;
	        	}
	        	if(v==8){
	        		v = 10;
	        	}
	        	var v1 = v > 100 ? 100 : v,
	        		v2 = v==100?'#4CAF50':'#FFC107',
	        		v3 = v==100?'#fff':'#666';  
	        	if(ov==5||ov==3||ov==4||ov==8){
	        		v2='#dd4b39';
	        		//tipIcon = ' <i class="iconfont orange-color" data-qtip="请查阅“状态跟踪”">&#xe716;</i>';
	        	}
	        	if(ov==0){
	        		if(r.get('OTHER_SUPPLY_AUDIT')==0){
	        			tipIcon = ' <i class="iconfont f18" style="color:#FFC107;" data-qtip="请及时付款以免座位不足">&#xe6ae;</i>';
		        	}
	        		if(r.get('OTHER_SUPPLY_AUDIT')==1){
		        		v2='#607D8B';
		        		txt[0] = '待审核';
		        		tipIcon = ' <i class="iconfont f14" style="color:#607D8B;top:0px;" data-qtip="订单含其他附加费(<dfn>￥</dfn>'+r.get('OTHER_AMOUNT')+')，请等待供应商审核">&#xe678;</i>';
		        	}
		        	if(r.get('OTHER_SUPPLY_AUDIT')==2){
		        		v2='#FFC107';
		        		tipIcon = [' <i class="iconfont f18" style="color:#FFC107;" data-qtip="请及时付款以免座位不足">&#xe6ae;</i> ',
		        		           '<i class="iconfont f14" style="color:#4CAF50;top:0px;" data-qtip="订单含其他附加费(<dfn>￥</dfn>'+r.get('OTHER_AMOUNT')+')，供应商审核已通过<br><b>原因：</b>'+r.get('OTHER_SUPPLY_CONTENT')+'">&#xe678;</i>'].join('');
		        	}
		        	if(r.get('OTHER_SUPPLY_AUDIT')==3){
		        		v2='#dd4b39';
		        		txt[0] = '审核不通过';
		        		tipIcon = [
		        		   ' <i class="iconfont f14" style="color:#dd4b39;top:0px;" data-qtip="订单含其他附加费(<dfn>￥</dfn>'+r.get('OTHER_AMOUNT')+')，供应商审核不通过<br><b>原因：</b>'+r.get('OTHER_SUPPLY_CONTENT')+'">&#xe678;</i>'].join('');
		        	}
		        	if(r.get('IS_EARNEST')=='1'){
	        			//
		        		v=50;
	        			v1=50;
	        			v2='#7B1FA2';
	        			v3='#ccc';
	        			txt[0] = '定金已付款';
	        			tipIcon = ' <i class="iconfont f18" style="color:#FFC107;" data-qtip="请及时支付尾款">&#xe6ae;</i> ';
	        		}
	        		if(r.get('IS_EARNEST')=='4'){
	        			//
	        			v2='#7B1FA2';
	        			txt[0] = '定金付款中';
	        		}
	        		if(r.get('IS_EARNEST')=='5'){
	        			//
	        			v=10;
	        			v1=10;
	        			v2='#dd4b39';
	        			txt[0] = '系统自动退团';
	        			tipIcon='';
	        		}
	        		if(r.get('IS_EARNEST')=='6'){
	        			//
	        			v=30;
	        			v1=30;
	        			v2='#dd4b39';
	        			txt[0] = '定金待退款';
	        			tipIcon='';
	        		}
	        		if(r.get('IS_EARNEST')=='7'){
	        			//
	        			v=50;
	        			v1=50;
	        			v2='#dd4b39';
	        			txt[0] = '定金退款中';
	        			tipIcon='';
	        		}
	        		if(r.get('IS_EARNEST')=='8'){
	        			//
	        			v=100;
	        			v1=100;
	        			v3='#fff';
	        			v2='#dd4b39';
	        			txt[0] = '定金已退款';
	        			tipIcon='';
	        		}
	        	}
	        	if(ov==1){
	        		tipIcon = ' <i class="iconfont red-color f18" data-qtip="6小时内未付款，系统将自动取消订单">&#xe6ae;</i>';
	        	}
	        	if(ov==2){
	        		if(r.get('IS_EARNEST')=='6'){
	        			//
	        			v=30;
	        			v1=30;
	        			v2='#dd4b39';
	        			v3='#666';
	        			txt[2] = '定金待退款';
	        			tipIcon='';
	        		}
	        		if(r.get('IS_EARNEST')=='7'){
	        			//
	        			v=50;
	        			v1=50;
	        			v2='#dd4b39';
	        			v3='#666';
	        			txt[2] = '定金退款中';
	        			tipIcon='';
	        		}
	        		if(r.get('IS_EARNEST')=='8'){
	        			//
	        			v=100;
	        			v1=100;
	        			v3='#fff';
	        			v2='#dd4b39';
	        			txt[2] = '定金已退款';
	        			tipIcon='';
	        		}
	        	}
	        	if(ov==6||ov==7){
	        		v2='#666';
	        	}
		        v1 = v1 < 0 ? 0 : v1;  
		        
		        var dzArr = ['','/对账中','/已对账'], dzStr = dzArr[r.get('ACCOUNT_STATUS')];
		        
		        
		        
		        var payIcon = '',
		        	payType = parseInt(r.get('PAY_TYPE')),
		        	payTypes=['&#xe66a;','&#xe66e;','&#xe66d;','&#xe66b;'],
		        	payTexts=['部门余额支付','支付宝支付','财付通支付','网银支付'],
		        	payCls=['#F44336','#00a0e9;','#ff8500;','#009688'];
		        payIcon = '<i class="iconfont f18" style="color:'+payCls[payType]+'" data-qtip="'+payTexts[payType]+'">'+payTypes[payType]+'</i> ';
		        if(!ov||ov=='0'){
		        	payIcon = '';
		        }
	            return Ext.String  
	                    .format(  
	                     '<div style="margin:10px 0;">'  
	                           + '<div style="float:left;border:1px solid {2};height:20px;width:100%;">'  
	                           + '<div style="float:left;text-align:center;line-height:16px;width:100%;color:{3};">{0}%</div>'  
	                           + '<div style="background: {2};width:{1}%;height:18px;"></div>'  
	                           + '</div></div>', v, v1, v2,v3)+'<div style="line-height:22px;clear:both;margin-top:3px;">'+payIcon+txt[ov]+dzStr+tipIcon+'</div>'; 
	        }
	    },{
	    	text: '更多操作',
	    	sortable:false,
	    	menuDisabled: true,
	    	xtype: 'widgetcolumn',
	    	width: 90,
            widget: {
            	margin:'10 0 0 0',
                xtype: 'splitbutton',
                text:'...',
                menu: [{
                	itemId:'orderDetailMenu',
                	glyph:'xe635@iconfont',
                    text:'订单详情',
                    handler:function(m){
						Ext.factory({
							orderRecord:m.up('splitbutton').getWidgetRecord()
						},'app.view.order.detail').show();
                    }
                },{
                	glyph:'xe610@iconfont',
                    text:'游客详情',
                    handler:function(m){
                    	Ext.factory({
							orderRecord:m.up('splitbutton').getWidgetRecord()
						},'app.view.order.visitor').show();
	               	}
                },{
                	glyph:'xe665@iconfont',
                    text:'状态跟踪',
                    handler:function(m){
                    	Ext.factory({
							orderRecord:m.up('splitbutton').getWidgetRecord()
						},'app.view.order.status').show();
	               	}
                },{
                	glyph:'xe61e@iconfont',
                    text:'资金流水',
                    handler:function(m){
                    	Ext.factory({
							orderRecord:m.up('splitbutton').getWidgetRecord()
						},'app.view.order.recharge').show();
	               	}
                },{
                	glyph:'xe70d@iconfont',
                    text:'出团确认单',
                    listeners:{
                    	afterrender:function(m){
                    		var r = m.up('splitbutton').getWidgetRecord(),
                    			status = parseInt(r.get('STATUS'));
                    		if(status==2||status==3||status==4||status==5){m.show();}else{
                    			m.hide();
                    		}
                    	}
                    },
                    handler:function(m){
                    	var r = m.up('splitbutton').getWidgetRecord();
                    	util.downloadAttachment(cfg.getCtx()+'/order/route/export/start/confirm?orderId='+r.get('ID'));
	               	}
                }]
            }
	    }];
	}
});