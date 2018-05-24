Ext.define('app.model.order.traffic.column', {
	constructor:function(){
		return [
		Ext.create('Ext.grid.RowNumberer',{width:25}),
		{
	        text: '订单编号',
	        width:120,
	        dataIndex: 'NO',
	        renderer: function(v,c,r){
	        	var h = [
	        		'<div class="ht20"><a href="javascript:;" class="xq">'+v+'</a></div>'
	        	];
	        	if(r.get('IS_LOST')=='1'){
	        		h.push('<div class="ht20"><a href="javascript:;" class="money-color dd">'+util.getFont('xe670','f17 money-color')+' 掉单</div>');
	        	}
	        	return h.join('');
	        }
	    },
	    {
	        text: '产品内容',
	        flex:1,
	        dataIndex: 'PRODUCE_NAME',
	        renderer: function(value){
	        	return [
	        		'<div class=" f14 ht20" data-qtip="'+value+'">'+value+'</div>',
	        		'<div class="ht20"></div>'
	        	].join('');
	        }
	    },{
	    	text: '出发日期',
	        width:100,
	        dataIndex: 'START_DATE',
	        renderer: function(v,c,r){
	        	v = v||'';
	        	if(v==''){
	        		return v;
	        	}
	        	v = v.replace(/-/g,'/');
	        	var weeks = ["日", "一", "二", "三", "四", "五", "六"],
	        		de = new Date(v).getDay(); 
	        	return [
	        		'<div class="ht20">'+v+'</div>',
	        		'<div class="ht20">星期'+weeks[de]+'</div>'
	        	].join('');
	        	
	        }
	    },{
	    	text: '人数',
	        width:50,
	        dataIndex: 'MAN_COUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20">'+v+'</div>',
	        		'<div class="ht20">'+r.get('CHILD_COUNT')+' <i class="iconfont disable-color f18" data-qtip="儿童">&#xe66c;</i></div>'
	        	].join('');
	        }
	    },
	    {
	        text: '订单金额',
	        width:100,
	        dataIndex: 'SALE_AMOUNT',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20">'+util.moneyFormat(v)+'</div>',
	        		'<div class="ht20">'+util.moneyFormat(r.get('INTER_AMOUNT'),'f14 disable-color',true)+'</div>'
	        	].join('');
	        }
	    },
	    {
	        text: '下单/付款',
	        width:70,
	        dataIndex: 'CREATE_TIME',
	        renderer: function(v,c,r){
	        	return [
	        		'<div class="ht20">'+v+'</div>',
	        		'<div class="ht20">'+r.get('PAY_TIME')+'</div>'
	        	].join('');
	        }
	    },
	    {
	        text: '来源/独立',
	        width:70,
	        dataIndex: 'SOURCES',
	        renderer: function(v,c,r){
	        	var sr = ['B2B','B2C'],
	        		srs = ['blue-color','orange-color'],
	        		dl = ['非独立','独立'],
	        		dls = ['','red-color'];
	        	return [
	        		'<div class="ht20 '+srs[v]+'">'+sr[v]+'</div>',
	        		'<div class="ht20 '+dls[r.get('IS_ALONE')]+'">'+dl[r.get('IS_ALONE')]+'</div>'
	        	].join('');
	        }
	    },
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
	        	var v1 = v > 100 ? 100 : v,
	        		v2 = v==100?'#53a93f':'#FFB90F',
	        		v3 = v==100?'#fff':'#888';  
	        	if(ov==5||ov==3||ov==4){
	        		v2='#dd4b39';
	        		//tipIcon = ' <i class="iconfont orange-color" data-qtip="请查阅“状态跟踪”">&#xe716;</i>';
	        	}
	        	if(ov==0){
	        		tipIcon = ' <i class="iconfont yellow-color f18" data-qtip="请及时付款以免座位不足">&#xe6ae;</i>';
	        	}
	        	if(ov==1){
	        		tipIcon = ' <i class="iconfont red-color f18" data-qtip="6小时内未付款，系统将自动取消订单">&#xe6ae;</i>';
	        	}
	        	if(ov==6||ov==7){
	        		v2='#f1f1f1';
	        		v3='#888';
	        	}
	        	var dzArr = ['','/对账中','/已对账'], dzStr = dzArr[r.get('ACCOUNT_STATUS')];
		        v1 = v1 < 0 ? 0 : v1;  
	            return Ext.String  
	                    .format(  
	                     '<div>'  
	                           + '<div style="float:left;border:1px solid {2};height:20px;width:100%;">'  
	                           + '<div style="float:left;text-align:center;line-height:16px;width:100%;color:{3};">{0}%</div>'  
	                           + '<div style="background: {2};width:{1}%;height:18px;"></div>'  
	                           + '</div></div>', v, v1, v2,v3)+'<div class="ht20" style="clear:both;margin-top:3px;">'+txt[ov]+dzStr+tipIcon+'</div>'; 
	        }
	    },{
	    	text: '更多操作',
	    	sortable:false,
	    	menuDisabled: true,
	    	xtype: 'widgetcolumn',
	    	width: 90,
            widget: {
            	margin:'3 0 0 0',
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
                }]
            }
	    }];
	}
});