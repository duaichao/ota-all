Ext.define('Weidian.view.order.route.Calendar', {
    extend: 'Weidian.view.Base',
    xtype: 'ordercalendar',
    
    
    requires: [
       'Ext.field.Spinner'
    ],
    config:{
    	viewModel:true,
    	cls: 'route-calendar D_label',
    	controller:'orderroute',
    	title:'1/3选择团期、交通',
		expandItems: [{
			itemId:'moreOperate',
			align:'right',
			ripple:false,
			iconCls:'x-fm fm-more f15',
			handler:'moreMenus',
			actions:[{
				iconCls:'x-fm fm-account-circle fcblue',
				text:'会员中心',
				call:'userCenter'
			},{
				iconCls:'x-fm fm-phone fcorange f18',
				text:'联系我们',
				call:'contactUs'
			},{
				iconCls:'x-fm fm-home',
				text:'返回首页',
				call:'backHome'
			}]
		}]
    	/*nbarSize:40,
    	title:Ext.Date.format(new Date(),'Y/m')*/
    },
    userCenter:function(){
    	this.getController().redirectTo('ucenter');
    },
    initialize: function(){
    	this.callParent();
    	if(this.getInitStatus()){
    		this.initViews();
    	}
    },
    initViews:function(){
    	var me=this,items = [];
    	items.push({
    		padding:'0 10',
    		xtype:'toolbar',
    		userCls:'mini-tools bbor bgwhite',
    		items:[{
    			xtype:'label',
    			html:'<span class="f14 fthin fcgray">出团日期：</span>'
    		},{
    			xtype:'label',
    			margin:'0 10 0 10',
    			html:'',
    			itemId:'startDateLabel'
    		},{xtype:'spacer'},{
    			ui:'calendar',
    			itemId:'calendarbtn',
    			iconCls:'x-fa fa-calendar',
    			listeners:{
    				tap:{
    					fn : me.onCalendarChooise,
    					scope : me
    				}
    			}
    		}]
    	});
    	items.push({
    		xtype:'label',
    		userCls:'label larger',
    		html:'交通',
    		padding:'10 0 0 10',
    		margin:'10 0 0 0'
		});
    	this.pricePlanView = Ext.create('Weidian.view.order.route.Plan',{
		});
    	items.push(this.pricePlanView);
    	items.push(Ext.create('Ext.Container',{
    		docked:'bottom',
    		itemId:'btools',
    		userCls:'tbor bbar bgwhite',
    		defaults:{
    			xtype:'toolbar',
    			userCls:'mini-tools',
    			defaults:{
    				padding:'0 10 0 10',
			    	clearIcon:false,
		    		stepValue: 1,
		    		textAlign:'center',
		    		minValue:0
    			}
    		},
    		items:[/*{
    			items:[{
    				userCls:'f14 fthin fcgray', 
    				xtype:'label',
    				html:'成人'
    			},{xtype:'spacer'},{
    				userCls:'f14 fthin fcgray',
    				xtype:'label',
    				html:'<span class="price">￥1980</span>/人'
    			},{
    				name:'manCount',
    				xtype:'spinnerfield',
    				margin:'0 0 0 10',
    				width:120,
    				minValue:1
    			}]
    		},{
    			items:[{
    				userCls:'f14 fthin fcgray',
    				xtype:'label',
    				html:'儿童'
    			},{xtype:'spacer'},{
    				userCls:'f14 fthin fcgray',
    				xtype:'label',
    				html:'<span class="price">￥1980</span>/人'
    			},{
    				name:'childCount',
    				margin:'0 0 0 10',
    				width:120,
    				xtype:'spinnerfield'
    			}]
    		},*/{
    			items:[{
    				userCls:'f14 fthin fcgray',
    				xtype:'label',
    				html:'单房差'
    			},{xtype:'spacer'},{
    				userCls:'f14 fthin fcgray',
    				xtype:'label',
    				html:'<span class="price">￥'+(this.getSubmitParams().singleRoomPrice||0)+'</span>/人'
    			},{
    				name:'singleRoomCount',
    				xtype:'spinnerfield',
    				margin:'0 0 0 10',
    				width:120,
    				minValue:0
    			}]
    			
    		}]
    	}));
    	this.setItems(items);
    	this.loadPriceAttrs();
    	this.initCalendarDialog();
    	this.initEventsAndSubmitParams();
    },
    loadPriceAttrs:function(){
    	//获取价格attr 
    	var me = this;
		util.request(cfg.url.route.price.attr,{},function(result){
			var datas = result.data,
				btools = me.down('container#btools');
			cfg.priceAttrs = datas;
			for(var i=0;i<datas.length;i++){
				btools.add({
					//itemId:datas[i].ID,
					//conName:datas[i].CON_NAME,
					viewModel:{
						data:{price:0}
					},
					itemId:datas[i].CON_NAME,
	    			items:[{
	    				userCls:'f14 fthin fcgray',
	    				xtype:'label',
	    				html:datas[i].TITLE
	    			},{xtype:'spacer'},{
	    				userCls:'f14 fthin fcgray',
	    				xtype:'label',
	    				bind:{
	    					html:'<span class="price">￥{price}</span>/人'
	    				}
	    			},{
	    				margin:'0 0 0 10',
	    				width:120,
	    				name:datas[i].CON_NAME=='CHENGREN'?'manCount':'childCount',
	    				minValue:datas[i].CON_NAME=='CHENGREN'?1:0,
	    				xtype:'spinnerfield'
	    			}]
	    		});
			}
			btools.add({
	    		xtype:'button',
	    		width:'100%',
	    		height:40,
	    		ui:'pay-orange',
	        	text:'下一步，填写订单',
	        	handler: 'gotoOrderVisitor',
				listeners: {
					scope: 'controller',
					element: 'element'
				}
    		});
			/**
	    	 * 人数 单房差 监听
	    	 */
	    	var spinners = btools.query('spinnerfield');
	    	me.setSubmitParams({
	    		'manCount' : 1,
	    		'childCount' : 0,
	    		'singleRoomCount' : 0
	    	});
	    	Ext.Array.each(spinners, function(o, index) {
	    	   o.on('change',function(spi,newValue){
	    		   var par = {};
	    		   par[spi.getName()] = newValue;
	    		   me.setSubmitParams(par);
	    	   })
	    	});
		});
    },
    initEventsAndSubmitParams:function(){
    	var me = this;
    	/**
    	 * 日历价格 出团日期 参数
    	 */
    	this.calendarView.on('periodchange',this.onCalendarPeriodChange,this);
    	this.calendarView.on('selectionchange',this.onCalendarDateSelected,this);
    	/**
    	 * 交通计划 参数 选中后加载价格详情
    	 */
    	this.pricePlanView.on('select',function(list,record){
    		this.setSubmitParams({
    			'planId' : record.get('PLANID'),
    			'planName' : record.get('PLAN_TITLE')
    		});
    		//加载外卖价格
    		util.request(cfg.url.route.price.detail,this.getSubmitParams(),function(result){
    			var datas = result.data,wmPrices;
    			for(var i=0;i<datas.length;i++){
    				if(datas[i].TITLE=='外卖'){
    					wmPrices = datas[i];
    					continue;
    				}
    			}
    			if(wmPrices){
    				for(var j in wmPrices){
    					if(j=='CHENGREN'||j=='ERTONG'){
    						var po = {};
    						if(j=='CHENGREN'){
    							po.manPrice = wmPrices[j];
    						}
    						if(j=='ERTONG'){
    							po.childPrice = wmPrices[j];
    						}
    						me.setSubmitParams(po);
    						me.down('container#btools').down('#'+j).getViewModel().set('price',wmPrices[j]);
    					}
    				}
    				
    			}
    		});
    	},this);
    },
    initCalendarDialog:function(){
    	var me = this,dialog=Ext.Viewport.down('container#calendarDialog');
    	if(dialog){
    		Ext.Viewport.remove(dialog);
    	}
    	this.calendarView = Ext.create('Weidian.view.ux.calendar.Main');
    	this.calendar = Ext.create('Ext.Container', {
    		itemId:'calendarDialog',
    		userCls:'tip-calendar-wrap D_dialog',
    		modal:true,
    		hideOnMaskTap: true,
    		floated:true,
    		width:'100%',
    		height:301,
    		items:[this.calendarView]
	    });
    	//是否已选择日期 Ymd
    	var sd = this.getSubmitParams().startDate;
    	if(sd){
    		this.down('label#startDateLabel').setHtml(Ext.Date.format(Ext.Date.parseDate(sd,'Ymd'),'Y/m/d')); 
    		this.setSubmitParams({'startDate':sd});
    		this.loadPricePlan();
    	}else{
    		setTimeout(function(){me.calendar.showBy(me.down('button#calendarbtn'));},200);
    	}
    	//首次加载日历价格
    	this.loadMonthPrice();
    },
    onCalendarChooise:function(btn){
    	this.calendar.showBy(btn);
    },
    /**
     * 日历翻页
     */
    onCalendarPeriodChange : function(view, minDate, maxDate, direction){
    	var ym = Ext.Date.format(view.currentDate,'Y/m'),
			m = Ext.Date.format(view.currentDate,'Y-m'+'-01');
    	//view.up('main').getNavigationBar().setTitle(ym);
		this.loadMonthPrice(m);
    },
    onCalendarDateSelected : function(view, newDate, previousValue,tdel){
    	var m = Ext.Date.format(newDate,'Ymd'),
			sm = Ext.Date.format(newDate,'Y/m/d');
		//this.setSubmitParams({'goDate':sm}); 套页面的时候重新确定加载Plan参数 废弃startDate 使用goDate
    	this.setSubmitParams({'startDate':m});
		this.loadPricePlan();
		this.down('label#startDateLabel').setHtml(sm); 
		this.calendar.hide();
    },
    loadMonthPrice : function(startDate){
    	var me = this,
    		sParams = this.getSubmitParams(),isFirst=true;
    	startDate = startDate || Ext.Date.format(new Date(),'Y-m'+'-01');
    	util.request(cfg.url.route.calendar,{
    		routeId:sParams.routeId||sParams.id,
    		startDate:startDate
    	},function(result){
    		var datas = result.data,view = me.calendarView;
    		view.element.select('td').each(function(el){
    			var dateStr = el.getAttribute('datetime'),
    				dateNo = el.getAttribute('dateno'),
    				priceStr ='';
    			Ext.Array.each(datas,function(data,i){
    				if(dateStr==data.RQ){
    					priceStr += '<div class="price">￥'+data.ACTUAL_PRICE+'</div>';
    					el.set({
    						th:cfg.isAdmin?data.ACTUAL_INTER_PRICE:data.ACTUAL_PRICE,
    						wm:data.ACTUAL_PRICE
    					});
    					//priceStr += '<div>'++'</div>';
    				}
    			});
    			el.setHtml(dateNo+(priceStr==''?'<div>&nbsp;</div>':priceStr));
    			
    			if(priceStr==''){
    				el.removeCls('able');
    			}else{
    				el.addCls('able');
    			}
    		});
        });
    },
    loadPricePlan : function(){
    	var store = this.pricePlanView.getStore(),sParams = this.getSubmitParams();
    	store.getProxy().setExtraParams({routeId:sParams.routeId||sParams.id,startDate:sParams.startDate});
    	store.load();
    },
    contactUs:function(){
    	cfg.view.initPluginPlusDialog(null,'contcat');
    },
    backHome:function(){
    	cfg.view.pop();
		window.location.hash = 'home';
    }
});
