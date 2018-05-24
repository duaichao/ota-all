Ext.define('Weidian.view.product.route.Detail', {
    extend: 'Weidian.view.Base',
    xtype:'routedetail',
	requires: [
       'Ext.field.Select'
    ],
	config:{
		showHashId:true,
		cls:'detail-container D_label',
		submitParams:{},
		controller:'route',
		title:'线路详情',
		viewModel: 'routedetailmodel',
		header:false,
		showTopPoint:true,
		topCls:'histicky',
		defaults:{
			margin:'0 0 10 0'
		},
		expandItems: [{
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
			}/*,{
				iconCls:'x-fm fm-collect fcred f18',
				text:'收藏产品',
				call:'favoriteProduct'
			},{
				iconCls:'x-fm fm-share fcgreen f18',
				text:'分享产品',
				call:'shareProduct'
			}*/]
		}]
	},
	activeCallBack:function(main,old){
		if(old.xtype=='mefavorite'){
			this.setBackHashUrl('me/favorite/list');
		}
		//加载详情
		var sParams = this.getSubmitParams(),
			routeId = sParams.id;
		util.request(cfg.url.route.detail,{routeId:routeId},function(data){
			if(!data.FACES){
				var face = data.FACE?Ext.imgDomain+data.FACE:'resources/images/noimage.jpg';
				data.FACES = face;
			}
			var me = this;
			this.getViewModel().setData(data);
			
			this.down('ExpandDataView').getStore().loadData(data.calendar,false);
			this.down('routedays').getStore().loadData(data.citys,false);
			this.setSubmitParams({endCity:data.END_CITY});
			//图片延迟加载
			setTimeout(function(){me.fireEvent('onDetailLoaded',me);},500);
		    /**
		     * 分享设置begin
		     */
		    document.title = data.TITLE;
		    document.getElementsByTagName('meta')['description'].setAttribute('content',data.FEATURE);
		    //微信
		    if(util.getAllPlatformsInfo().isWeixinApp){
		    	var thumbnails = Ext.get('thumbnails'),imgSrc = 'resources/images/noimage.jpg';
	    	    if(face.indexOf('noimage')==-1){
	        		imgSrc = Ext.imgDomain+util.pathImage(face,'640X360');
        		}
	        	if(thumbnails){
	        		thumbnails.down('img').dom.src = imgSrc;
	        	}
	        	util.shareFromWeixinInit({
    	    		title : data.TITLE,
    	            desc: data.FEATURE,
    	            link:  window.location.href,
    	            imgUrl: face
    		   });
		    }
		    /**
		     * 分享设置end
		     */
		},this);
	},
    initialize: function(){
    	this.initViews();
    	this.callParent();
    },
    initViews:function(){
    	var me=this,items = [];
    	items.push({
    		xtype:'container',
    		height:'180',
			margin:'0',
			layout:'fit',
    		items:[{
    			xtype:'image',
                listeners:{
                	error:function(img){img.setSrc('resources/images/noimage.jpg');}
                },
                bind:{
                	src:'{FACES}',
                	html:'{types}游 | 编号：{NO}'
                }
    		}]
    	});
    	items.push({
    		xtype:'container',
			userCls:'basic_info',
			padding:'10',
			bind:{
				html:[
					'<div class="base">',
					'<div class="title">{TITLE}</div>',
					'<div id="priceInfo" class="priceInfo">',
					'<div class="price ordinary">',
					'<span class="promoCommon">价格&nbsp;&nbsp;￥<strong>{SUM_PRICE}</strong> 起       </span>',
					'</div>',
					'</div>',
					'</div> ',
					'</div>'
				].join('')
			}
		});
    	items.push({
			xtype:'selectfield',
			userCls:'choose-start-city',
			name: 'BEGIN_CITY',
			label: '出发地',
			value:'0',
			bind:{
				options:'{startCity}'
			},
			listeners:{
				change:function(sf, newValue){
					me.setSubmitParams({beginCity:sf.getValue()});
				}
			}
		});
    	items.push({
			xtype:'label',
			userCls:'label',
			html:'出发日期',
			padding:'10 0 0 10',
			margin:'0'
		});
    	items.push({
			xtype:'ExpandDataView',
			scrollable:false,
			userCls:'choose-calendar',
			itemCls:'date-item',
			emptyText:'最近一月无团期',
			itemTpl: [
			     '<div class="date" date="{RQ}">{RQ:this.getWeek()}</div><div class="price">¥ {ACTUAL_PRICE}</div> ',{
			    	 getWeek:function(date){
			    		 date = Ext.Date.parse(date, "Ymd");
			    		 return Ext.Date.format(date,'m-d')+' 周'+Ext.Date.dayNames[date.getDay()].substring(2, 3);
			    	 }
			     }
			],
			store:{
				fields:['RQ','ACTUAL_PRICE']
			},
			html:'<a href="javascript:;" class="x-fa fa-angle-right" action="calendar"></a>',
			listeners: {
				click: {
		            element: 'element',
		            scope: 'controller',
		            fn: 'onDateViewClick'
		        },
		        itemtap : {
		            scope: 'controller',
		            fn: 'onDateItemSelected'
		        }
			}
		});
    	items.push({
			xtype:'tabbar',
			activeTab:0,
			itemId:'itemtabs',
			margin:'0',
			items:[{
				ui: 'gray',
				action:'days',
				title:'X日行程',
				bind:{
					title:'{DAY_COUNT}日行程'
				}
			},{
				ui: 'gray',
				action:'featrue',
				title:'特色推荐'
			},{
				ui: 'gray',
				action:'fee',
				title:'费用说明'
			},{
				ui: 'gray',
				action:'notice',
				title:'预订须知'
			}],
			userCls: 'category-tab'
		});
    	items.push({
			xtype:'routedays',
			tplWriteMode:'overwrite',
			reference:'A_days',
			store:{
				fields:['NO','TITLE','BEGIN_CITY','TOOL','TOOL1','END_CITY',
				        'END_CITY1','BREAKFAST','LUNCH','DINNER','TODAY_TIPS',
				        'HOTEL_TIPS','PAY_TIPS',{
					details:['TITLE','CONTENT']
				}]
			}
		});
    	items.push({
    		xtype:'label',
    		reference:'A_featrue',
    		userCls:'label big',
    		html:'特色推荐',
    		padding:'10 0 0 10',
    		margin:'0'
		});
    	items.push({
    		xtype:'routeitem',
    		userCls:'route-item',
    		bind:{
    			html:'{tese}'
    		}
		});
    	items.push({
    		xtype:'label',
    		reference:'A_fee',
    		userCls:'label big',
    		html:'费用包含',
    		padding:'10 0 0 10',
    		margin:'0'
		});
    	items.push({
    		xtype:'routeitem',
    		userCls:'route-item',
    		bind:{
    			data:'{include}'
    		}
		});
    	items.push({
    		xtype:'label',
    		userCls:'label big',
    		html:'费用不包含',
    		padding:'10 0 0 10',
    		margin:'0'
		});
    	items.push({
    		xtype:'routeitem',
    		userCls:'route-item',
    		bind:{
    			data:'{noclude}'
    		}
		});
    	items.push({
        	xtype:'label',
        	reference:'A_notice',
    		userCls:'label big',
    		html:'出行须知',
    		padding:'10 0 0 10',
    		margin:'0'
		});
    	items.push({
    		xtype:'routeitem',
    		userCls:'route-item',
    		bind:{
    			data:'{notice}'
    		}
		});
    	items.push({
        	xtype:'label',
        	userCls:'label big',
    		html:'温馨提醒',
    		padding:'10 0 0 10',
    		margin:'0'
		});
    	items.push({
    		xtype:'routeitem',
    		userCls:'route-item',
    		bind:{
    			data:'{tips}'
    		}
		});
    	items.push(
			{
				xtype:'toolbar',
				margin:'0',
				userCls:'bottom-tools',
				docked:'bottom',
				defaults:{height:50},
				items:[/*{
					xtype: 'component',
					html:'<a  href="tel:'+Ext.shopInfo.PHONE1+'" class="f14 fthin" style="display: block;text-align:center;padding:5px 5px 0 5px;"><span class="f24 x-fm fm-phone"></span><br>联系卖家</a>'
				},{
					ui:'gray',
					iconAlign:'top',
					text:'联系我们',
					iconCls:'x-fm fm-phone',
					handler:function(){
						cfg.view.initPluginPlusDialog(null,'contcat');
					}
				},*/{
					ui:'gray',
					iconAlign:'top',
					itemId:'favorite',
					text:'收藏产品',
					iconCls:'x-fm fm-collect',
					bind:{
						text:'{favoriteTxt}',
						iconCls:'{favoriteCls}'
					}
				},{
					ui:'gray',
					itemId:'share',
					iconAlign:'top',
					text:'分享产品',
					iconCls:'x-fm fm-share'
				},{xtype: 'spacer'},{
					align:'right',
					ui:'pay-orange',
					width:90,
					text:'下一步',
					handler:'gotoOrderCalendar'
				}]
			}
    	); 
    	this.setItems(items);
    	this.initTabScroller();
    	this.down('button#share').on('tap',this.shareProduct,this);
    	this.down('button#favorite').on('tap',this.favoriteProduct,this);
    },
    initTabScroller:function(){
    	var me = this,tbr = this.down('tabbar'),scroller=this.getScrollable(),y=0;
	    setTimeout(function(){
	    	y=tbr.element.getY()-65;
	    	scroller.on('scroll', function() {	
				if(scroller.getPosition().y>y){
					if(!tbr.isDocked()){
						tbr.setDocked('top');
						y = y-50;
					}
					return;
				}else{
					if(tbr.isDocked()){
						tbr.setDocked(null);
						y=y+50;
					}
					return;
				}
			});
	    },100);
    },
    contactUs:function(){
    	cfg.view.initPluginPlusDialog(null,'contcat');
    },
    shareProduct:function(){
    	var model = this.getViewModel(),
			face = model.get('FACE');
    	util.share({
    		title : model.get('TITLE'),
            desc: model.get('FEATURE'),
            link:  window.location.href,
            imgUrl: face?Ext.imgDomain+face:''
		});
    },
    favoriteProduct:function(btn){
    	if(!cfg.user){
    		Ext.toast('请先登录',cfg.toastTimeout);
    		this.getController().redirectTo('login');
    		return;
    	}
    	var sParams = this.getSubmitParams(),
    		routeId = sParams.id,
    		isDel = btn.getIconCls().indexOf('fm-collected')!=-1;
    	util.request(cfg.url.favorite.route.add,{noLoader:true,routeId:routeId,isDel:(isDel?'1':'')},function(data){
    		var cls = isDel?'x-fm fm-collect':'x-fm fm-collected fcred1',
    			txt = isDel?'收藏产品':'取消收藏';
    		if(btn.getIconCls().indexOf('f18')!=-1){
    			btn.setIconCls(cls+' fcred1 f18');
    		}else{
    			btn.setIconCls(cls);
    		}
    		btn.setText(txt);
    	},this);
    },
    userCenter:function(){
    	this.getController().redirectTo('ucenter');
    }
});
