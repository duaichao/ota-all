Ext.define('Weidian.view.product.route.Main', {
    extend: 'Weidian.view.Base',
    xtype:'routecategory',
	config:{
		controller:'route',
		cls: 'list-container',
		title:'...',
		layout:'fit',
		scrollable:false,
		showTopPoint:true,
		expandItems: [{
			align:'right',
			userCls:'arrow-icon bar-text',
			text:'切换',
			ripple:false,
			iconAlign:'right',
			iconCls:'x-fa fa-angle-down',
			ui:'white',
			handler: 'onCategoryButtonTap'
		},{
			align:'right',
			ripple:false,
			iconCls:'x-fm fm-more f15',
			handler:'moreMenus',
			actions:[{
				iconCls:'x-fm fm-account-circle fcblue',
				text:'会员中心',
				call:'userCenter'
			}/*,{
				xtype:'searchfield',
				emptyText:'关键字'
			}*/]
		}]
	},
    userCenter:function(){
    	this.getController().redirectTo('ucenter');
    },
    activeCallBack:function(main,old){
    	var sParams = this.getSubmitParams(),
			categoryTitle = sParams.categoryTitle,
			cityName = sParams.cityName;
		if(!Ext.isEmpty(categoryTitle)){
			if(!Ext.isEmpty(cityName)){
				setTimeout(function(){main.getNavigationBar().setTitle(categoryTitle+'<p class="f12 fthin">'+cityName+'</p>');},500);
			}else{
				setTimeout(function(){main.getNavigationBar().setTitle(categoryTitle);},500);
			}
			
		}
		if(util.getAllPlatformsInfo().isWeixinApp){
			var imgUrl = 'resources/images/noface.gif';
	   		if(!Ext.isEmpty(Ext.shopInfo.LOGO)){
	   			imgUrl = Ext.imgDomain+util.pathImage(Ext.shopInfo.LOGO,'320X320');
	   		}
			var config={
				title : Ext.shopInfo.TITLE+'('+categoryTitle+')',
	            desc: Ext.shopInfo.ABOUT,
	            link:  window.location.href,
	            imgUrl: imgUrl
			};
			util.shareFromWeixinInit(config);
		}
    },
    initialize: function(){
    	this.initViews();
    	this.callParent();
    },
    initViews:function(){
    	var items = [],sParams = this.getSubmitParams();
    	items.push(
			Ext.create('Weidian.view.product.route.List',{
				controller:'route',
				infinite : false,
			 	variableHeights: true,
			 	//itemHeight:255,
			 	scrollToTopOnRefresh:false,
				dyParams:sParams,
				userCls: 'route-list-view shadow'
			})
    	);
    	
    	this.setItems(items);
    },
    filterByCategory:function(r){
    	cfg.view.getNavigationBar().setTitle(r.text);
    	this.down('routelist').setDyParams({categoryId:r.categoryPid});
    }
});
