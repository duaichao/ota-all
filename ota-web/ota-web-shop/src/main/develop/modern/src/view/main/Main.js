Ext.define('Weidian.view.main.Main', {
	extend: 'Weidian.view.ux.navigation.View',
	xtype: 'main',
	
	config:{
		controller: 'main',
	    //viewModel: 'main',
		defaultBackButtonText: '',
		navigationBar:{
			ui:'topnav'
		},
		fullscreen:true
	},
	
    initialize: function() {
    	cfg.point.phone = this.initPluginPhonePoint();
		cfg.point.top = this.initPluginTopPoint();
		/*cfg.toastBar = Ext.Viewport.setMenu({
			top:56,
			html:'提示信息'
		}, {
            side: 'top',
            cover: false
        });*/
		this.add(Ext.create('Weidian.view.main.Home',{routeId:'home'}));
	    this.callParent();
	},
	fireBbarDisplay:function(view){
		if(!view.isInnerItem()){return;}
		var me = this,topPoint = cfg.point.top,
			mainPoint = cfg.point.phone,
			isShowPhone = (view.getShowMainPoint) ? view.getShowMainPoint() : false,
			isShowTop = (view.getShowTopPoint) ? view.getShowTopPoint() : false,
			topCls = (view.getTopCls) ? view.getTopCls() : 'sticky',scroller;
		mainPoint[isShowPhone?'show':'hide']();
		topPoint.setCls(null);
		topPoint[isShowTop?'show':'hide']();
		//me.getNavigationBar().removeCls('hide');
		if(isShowTop){
			var lazyImages,list = view.down('routelist');
			if(list){
				list.getStore().on('load',function(s,rs){
					lazyImages = list.element.query('img.lazy_flash',false);
					var loaded;
					if(lazyImages.length>0){
						if(s.currentPage>1){loaded = lazyImages.splice(0,s.getPageSize()*(s.currentPage-1));}
						if(loaded){me.lazyImageRefresh(loaded);}
						me.lazyImageLoad(lazyImages);
					}
				});
			}
			if(view.xtype=='routedetail'){
				view.on('onDetailLoaded',function(){
					lazyImages = this.element.query('img.lazy_flash',false);
					if(lazyImages.length>0){me.lazyImageLoad(lazyImages);}
				});
			}
			
			scroller = view.getScrollable()?view.getScrollable():list.getScrollable();
			if(scroller){
				scroller.on('scroll', function() {
					if(lazyImages&&lazyImages.length>0){
						me.lazyImageLoad(lazyImages);
					}else{
						var el = list?list.element:view.element;
						lazyImages = el.query('img.lazy_flash',false);
						me.lazyImageLoad(lazyImages);
					}
					if(scroller.getPosition().y>=100){
						topPoint.addCls(topCls);
						//me.getNavigationBar().addCls('hide');
					}else{
						topPoint.removeCls(topCls);
						//me.getNavigationBar().removeCls('hide');
					}
				});
			}
		}
		//
		var nbarSize = (view.getNbarSize) ? view.getNbarSize() : false;
		if(nbarSize){
			this.getNavigationBar().setHeight(nbarSize);
		}else{
			this.getNavigationBar().setHeight('auto');
		}
	},
	lazyImageRefresh:function(imgEls){
		Ext.Array.each(imgEls,function(imgEl,i){
			imgEl.set({"src": imgEl.getAttribute("data-src")});
			imgEl.addCls(['loaded','already']);
		});
	},
	lazyImageLoad:function(imgEls){
		var me = this,windowHeight = Ext.Viewport.getWindowHeight();
		Ext.Array.each(imgEls,function(imgEl,i){
			if(imgEl.getY()<windowHeight&&!imgEl.hasCls('loaded')){
		         me.lazyImageShow(imgEl);   
			}
		});
	},
	lazyImageShow:function(imgEl){
		imgEl.set({"src": imgEl.getAttribute("data-src")});
		imgEl.dom.onload = function(){
			imgEl.applyStyles("opacity: 1;");
		};
		imgEl.dom.onerror = function(){
			this.src='resources/images/noimage.jpg';
		}
        imgEl.addCls('loaded');
	},
	initPluginPhonePoint:function(){
		var me = this;
		return Ext.Viewport.add({
			xtype: 'button',
			itemId:'mainPoint',
			iconCls:'x-fm fm-account',
			ui: 'callme round',
			//zIndex:2,
			floated:true,
			width: 50,
			height: 50,
			bottom: 10,
			left: 10,
			handler: function(btn){
				/*var dialog = Ext.Viewport.down('panel#contcatDialog');
				if(!dialog){
					me.initPluginPlusDialog(btn,'contcat');
				}else{
					if(btn.getIconCls().indexOf('md-icon')!=-1){dialog.hide();}else{
						dialog.show();
					}
				}*/
				me.getController().redirectTo('ucenter');
			}
		});
	},
	initPluginTopPoint:function(){
		var topPoint = Ext.Viewport.add({
			xtype: 'button',
			itemId:'topPoint',
			userCls:'back-to-top',
			iconCls:'md-icon md-icon-arrow-upward',
			ui:'gotop round',
			zIndex:2,
			floated:true,
			width: 50,
			height: 50
		});
		
		topPoint.on('tap',function(){
			var currView = Ext.Viewport.down('main').getActiveItem(),scroller=currView.getScrollable()?currView.getScrollable():currView.down('routelist').getScrollable();;
			if(scroller){
				scroller.scrollTo(0,0,true);
			}
		});
		
		return topPoint;
	},
	initPluginPlusDialog:function(btn,type){
		var html = ['<img src="'+Ext.imgDomain+'{'+type+'}" onerror="javascript:this.src=\'resources/images/qrcode.jpg\';" height="220">'],title='扫描二维码';
		if(type=='WX_IMG'){
			title = '微信联系客服';
		}
		if(type=='CODE'){
			title = '扫一扫分享微店';
		}
		if(type=='contcat'){
			html =  [
			    '<div class="dialog-user-code">',
			    '<div class="title"><span>微店二维码</span><span>微信二维码</span></div>',
			    '<img style="margin-right:10px;" src="'+Ext.imgDomain+'{CODE}" onerror="javascript:this.src=\'resources/images/qrcode.jpg\';" height="80">',
			    '<img style="margin-left:10px;" src="'+Ext.imgDomain+'{WX_IMG}" onerror="javascript:this.src=\'resources/images/qrcode.jpg\';" height="80">',
			    '</div>',
				'<div class="dialog-user-item">',
				'<div class="dialog-user-image"><img src="'+Ext.imgDomain+'{LOGO}" onerror="javascript:this.src=\'resources/images/noface.gif\';" class="circular" width="50" height="50"></div>',
				'<div class="dialog-user-content">',
				'<div class="dialog-user-title">{MANAGER}</div>',
				'<div class="dialog-user-info">{TITLE}</div>',
				'</div>',
				'</div>',
				'<a class="chat-dialog-btn chat-dialog-highlight" href="tel:{PHONE1}"><span class="x-fm fm-phone f20"></span> <span>{PHONE1}</span></a>',
				'<a class="chat-dialog-btn" href="mqqwpa://im/chat?chat_type=wpa&uin={QQ1}&version=1&src_type=web&web_src=136ly.com"><span class="x-fm fm-qqchat f20"></span> <span>{QQ1}</span></a>'
			];
			title='联系我们';
		}
		var dialog = Ext.Viewport.add({
			xtype: 'panel',
			itemId:'contcatDialog',
			floated: true,
			modal: true,
			userCls:'dialog D_dialog',
			hideOnMaskTap: true,
			showAnimation: {
				type: 'popIn',
				duration: cfg.anim.duration,
				easing: 'ease-out'
			},
			hideAnimation: {
				type: 'popOut',
				duration: cfg.anim.duration,
				easing: 'ease-out'
			},
			tools:[{
				style:{color:'#9e9e9e'},
				type:'close',
				handler:function(){dialog.hide();}
			}],
			centered: true,
			width: util.getDeviceSize().width,//Ext.filterPlatform('ie10') ? '100%' : (Ext.os.deviceType == 'Phone') ? 260 : 400,
            //maxHeight: util.getDeviceSize().height,//Ext.filterPlatform('ie10') ? '30%' : (Ext.os.deviceType == 'Phone') ? 220 : 400,
			styleHtmlContent: true,
			tpl: html,
			data:Ext.shopInfo,
			header: {
				title: title
			},
			listeners:{
				show:function(g){
					if(type=='contcat'&&btn){btn.setIconCls('md-icon md-icon-close');}
				},
				hide:function(g){
					if(type=='contcat'&&btn){btn.setIconCls('x-fm fm-phone');}
					Ext.Viewport.remove(g);
				}
			}
		});
		dialog.show();
	}
});