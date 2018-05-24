Ext.define('Weidian.view.main.Home', {
    extend: 'Weidian.view.Base',
    xtype: 'home',
	config: {
		cls: 'home-container',
		controller:'main',
		routeId:'home',
		showMainPoint:false,
		showTopPoint:true,
		backHide:true,
		expandTitle:{
			margin:'0 0 0 8',
			xtype: 'searchfield',
			placeHolder: '目的地',
			disabled:true,
			name: 'searchfield',
			autoCapitalize: false,
			clearIcon: false,
			readOnly:true,
			listeners:{
				tap :{
					scope: 'controller',
					element:'element',
					fn:'onSearch'
				}
			}
		},
		expandItems: [/*{
			userCls:'arrow-icon',
			text:'西安',
			ripple:false,
			iconAlign:'right',
			iconCls:'x-fa fa-angle-down',
			ui:'white',
			handler: 'onCityButtonTap'
		},*/{
			ripple:false,
			iconCls:'x-fm fm-account f15',
			handler: 'onUserButtonTap'
		},{
			align:'right',
			iconCls:'x-fm fm-share f13',
			handler:function(){
				util.share({
					title: Ext.shopInfo.TITLE,
	                desc: Ext.shopInfo.ABOUT,
	                link:  window.location.href,
	                imgUrl: Ext.shopInfo.LOGO
				});
			}
		}],
		viewModel:true
	},
	activeCallBack:function(main,old){
		//回首页 销毁除首页的所有card
		var its = main.getInnerItems();
		for (i = its.length-1; i >0; i--) {
			//console.log(its[i].xtype);
			main.remove(its[i]);
        }
		//微信分享meta设置
		document.title = Ext.shopInfo.TITLE;
        document.getElementsByTagName('meta')['description'].setAttribute('content',Ext.shopInfo.ABOUT);
        if(util.getAllPlatformsInfo().isWeixinApp){
        	var thumbnails = Ext.get('thumbnails'),imgSrc = 'resources/images/noface.gif';
        	if(!Ext.isEmpty(Ext.shopInfo.LOGO)){
        		imgSrc = Ext.imgDomain+util.pathImage(Ext.shopInfo.LOGO,'320X320');
    		}
        	if(thumbnails){
        		thumbnails.down('img').dom.src = imgSrc;
        	}else{
        		//微信分享图片设置
        	    var dh = Ext.DomHelper,
        	    spec = {
        			   id:'thumbnails',
        			   tag:'div',
        			   style:'margin:0;display:none;',
        			   children:[{
        				   tag:'img',
    	    			   width:300,
    	    			   height:300,
    	    			   src:imgSrc
        			   }]
        	    };
        	    dh.insertFirst(document.body,spec);
        	}
        	if(Ext.isDefined(old)){
	        	util.shareFromWeixinInit({
    	    		title : Ext.shopInfo.TITLE,
    	            desc: Ext.shopInfo.ABOUT,
    	            link:  Ext.domain,
    	            imgUrl: imgSrc
    			});
        	}
        }
	},
    initialize: function(){
    	this.initViews();
    	this.callParent(arguments);
    	var me = this;
    	if(Ext.shopInfo){
    		this.initViewModelData();
    	}else{
    		setTimeout(function(){me.initViewModelData()},1000);
    	}
    },
    initViews:function(){
    	var items = [],category;
    	items.push({
    		xtype:'container',
    		cls:'user-profile',
        	scrollable:false,
            layout: {
                type: 'vbox',
                align: 'middle'
            },
    		items:[{
    			xtype:'image',
        		cls: 'userProfilePic',
                height: 150,
                width: 150,
                alt: 'profile-picture',
                src: 'resources/images/noface.gif',
                bind:{
                	src:Ext.imgDomain+util.pathImage('{LOGO}','320X320')
                },
                listeners:{
                	error:function(img){img.setSrc('resources/images/noface.gif');}
                }
        	},{
        		xtype:'container',
        		cls: 'userProfileName',
                html: 'XXXX, 经理',
                bind:{
                	html:'<h3>{MANAGER}</h3><div class="des">{TITLE}</div>'
                }
        	},{
        		xtype:'container',
        		layout: 'hbox',
                defaults: {
                    xtype: 'button',
                    margin: 5
                },
                margin: '5 0',
                items: [{
                	itemId:'qrcode',
                    ui: 'soft-cyan',
                    bind:{
                    	value:'{CODE}'
                    },
                    iconCls: 'x-fm fm-qrcode'
                },
                {
                	itemId:'wxcode',
                	ui: 'soft-green',
                	bind:{
                    	value:'{WX_IMG}'
                    },
                    iconCls:'x-fm fm-wechat'
                },{
                    //ui: 'soft-red',
                    //iconCls: 'x-fm fm-phone',
    				//text:'<a href="tel:15298521252">152 9852 1252</a>'
    				xtype:'container',
    				flex:1,
    				cls:'owner-phone',
    				styleHtmlContent: true,
    				bind:{
    					html:'<a class="" href="tel:{PHONE1}"><span class="x-fm fm-phone f20"></span> <span>{PHONE1}</span></a>'
    				}
                }]
        	}]
    	});
    	items.push(
    		category = Ext.create('Weidian.view.ux.dataview.DataView',{
    			itemId:'categoryBlock',
				userCls: 'type-data-view',
				scrollable:null,
				margin:'10 0 10 0',
				padding:'10 0 0 10',
				store: {
					fields: ['ID','PID','CATEGORY','ICON','COLOR']
				},
				itemCls:'big-33 small-33 dashboard-item shadow type-data-item',
				itemTpl: ['<div class="wrap" style="background-color:{COLOR}"><div class="x-fm {ICON} fm-coner"><h3>{CATEGORY}</h3></div></div>']
			})
    	);
    	category.on('itemtap',this.onTripTypeTap,this);
    	items.push(
			Ext.create('Ext.tab.Bar',{
				itemId:'categoryTab',
				activeTab:0,
				items:[{
					ui: 'gray',
					title:'热卖'
				},{
					ui: 'gray',
					title:'特惠'
				}],
				userCls: 'shadow category-tab'
			})
    	);
    	items.push(
			Ext.create('Weidian.view.product.route.List',{
				controller:'route',
				infinite : false,
			 	variableHeights: true,
				userCls: 'shadow route-list-view'
			})
    	);
    	
    	this.setItems(items);
    	
    	this.down('button#qrcode').on('tap',this.onQrcodeButtonTap,this);
    	this.down('button#wxcode').on('tap',this.onWxcodeButtonTap,this);
    },
    onQrcodeButtonTap:function(btn){
		cfg.view.initPluginPlusDialog(btn,'CODE');
    	/*cfg.toastBar.setHtml('sdfsdfsdf');
    	Ext.Viewport.showMenu('top');*/
	},
	onWxcodeButtonTap:function(btn){
		cfg.view.initPluginPlusDialog(btn,'WX_IMG');
	},
    onTripTypeTap:function(dv,index,target,r,e){
    	this.setSubmitParams({categoryTitle:r.get('CATEGORY')});
		this.getController().redirectTo('route/category/'+r.get('PID'));
    },
    initViewModelData:function(){
    	var data = Ext.shopInfo;
    	this.getViewModel().setData(data);
    	//this.down('container#profile').getViewModel().setData(data);
    	var categorys = data.categorys,categoryId='',block=[],tabs=[];
    	Ext.Array.each(categorys,function(o,i){
    		if(!o.ICON){
    			o.ui='gray';
    			o.title=o.CATEGORY;
    			categoryId = o.PID;
    			tabs.push(o);
    		}else{
    			block.push(o);
    		}
    	});
    	this.down('dataview#categoryBlock').getStore().setData(block);
    	tabs.push({
			ui: 'gray',
			title:'热卖'
    	});
    	this.down('tabbar#categoryTab').setItems(tabs);
    	this.down('tabbar#categoryTab').setActiveTab(0);
    	this.down('tabbar#categoryTab').on('activeTabchange',function(a,b){
    		if(b.getTitle()=='热卖'){
    			this.down('routelist').setDyParams({hot:1});
    		}else{
    			this.down('routelist').setDyParams({categoryId:categoryId,indexOrder:'hot'});
    		}
    	},this);
    	this.down('routelist').setDyParams({categoryId:categoryId,indexOrder:'hot'});
    }
});
