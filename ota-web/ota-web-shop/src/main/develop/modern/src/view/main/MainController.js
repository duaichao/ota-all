Ext.define('Weidian.view.main.MainController', {
    extend: 'Ext.app.ViewController',
    alias: 'controller.main',
	control: {
		'main':{
			activeItemchange :'onActiveItemchange'
		}
    },
    routes: {
    	':module': {
        	allowInactive:true,
            action: 'handleRoute',
            before: 'beforeHandleRoute'
        },
        ':module/:page': {
        	allowInactive:true,
            action: 'handleRoute',
            before: 'beforeHandleRoute'
        },
        ':module/:page/:id': {
        	allowInactive:true,
            action: 'handleRoute',
            before: 'beforeHandleRoute'
        }
    },
    beforeHandleRoute:function(){
    	var action,module,page,id,hashUrl,xtype,
	    	main = cfg.view||Ext.Viewport.down('main'),	
	    	oldItem = main.getActiveItem();
		if(arguments.length==2){
			module = arguments[0];
			page = '';
			id = '';
			hashUrl = module;
			action = arguments[1];
		}
		if(arguments.length==3){
			module = arguments[0];
			page = arguments[1];
			id = '';
			hashUrl = module+'/'+page;
			action = arguments[2];
		}
		if(arguments.length==4){
			module = arguments[0];
			page = arguments[1];
			id = arguments[2];
			hashUrl = module+'/'+page+'/'+id;
			action = arguments[3];
		}
		xtype = module+page;
		//模拟自动登录
		var loggedIn = localStorage.getItem('loggedIn');
    	if(loggedIn){
    		cfg.user = Ext.decode(loggedIn);
    		util.request(cfg.url.login,{
    			loginType:'0',
    			userId:cfg.user.ID,
    			userName:cfg.user.MOBILE,
    			passWord:cfg.user.PASSWORD
    		},function(user){
    			action.resume();
    		});
    	}else{
    		if(cfg.authLoginModules[xtype]){
	    		oldItem.setSubmitParams({redirectUrl:hashUrl});
	    		this.redirectTo('login');
				action.stop();
    		}else{
    			action.resume();
    		}
    	}
    },
    handleRoute: function () {
		var module,page,id,hashUrl;
		if(arguments.length==1){
			module = arguments[0];
			page = '';
			id = '';
			hashUrl = module;
		}
		if(arguments.length==2){
			module = arguments[0];
			page = arguments[1];
			id = '';
			hashUrl =  module+'/'+page;
		}
		if(arguments.length==3){
			module = arguments[0];
			page = arguments[1];
			id = arguments[2];
			hashUrl = module+'/'+page+'/'+id;
		}
		var me = this,
			xtype = module+page,
			main = cfg.view||Ext.Viewport.down('main'),
			item = main.child('component[routeId=' + xtype + ']');
		
		
		var oldItem = main.getActiveItem(),
			sParams = oldItem?(oldItem.getSubmitParams()||{}):{},
			newItem = {};
	    	//if(oldItem&&oldItem.xtype==xtype){return;}
	    	sParams._dc = Ext.ticks();//防止缓存
	    	if(id){
        		if(id.length==32||id.indexOf('_')!=-1){
        			if(module=='route'){
        				if(page=='category'){
        					var _ps = id.split('_');
        					if(_ps.length>1){
        						id = _ps[0];
        						sParams.cityName=decodeURI(_ps[1]);
        					}
        					sParams.categoryId = id;
        					
        					Ext.Array.each(Ext.shopInfo.categorys,function(o,i){
        			    		if(o.PID==id){
        			    			sParams.categoryTitle = o.CATEGORY;
        			    			return false;
        			    		}
        			    	});
        				}
        				if(page=='detail'){
        					sParams.id = id;
        				}
        			}
        			if(module=='pay'){
        				if(page=='center'){
        					sParams.orderId = id;
        				}
        			}
        			if(module=='order'){
        				if(page=='detail'){
        					sParams.id = id;
        				}
        			}
    	        }else{
    	        	console.log(id+'====================复杂跳转参数');
    	        }
        	}
		
		if (!item) {
			cfg.isRedirect = true;
	    	newItem.xtype = xtype;
	    	newItem.routeId = xtype;
	    	newItem.hashUrl = hashUrl;
	    	newItem.backHashUrl = oldItem?oldItem.getHashUrl():'';
	    	newItem.submitParams = sParams;
            item = main.push(newItem);
            //console.log(xtype+'+++++++++++++++++++++++++');
		}else{
			//isRedirect 控制redirectTo 仅跳转1次
			//console.log(cfg.isRedirect);
			if(cfg.isRedirect){
				cfg.isRedirect = false;
				return;
			}
			cfg.isRedirect = true;
			//console.log(xtype+'------------------------=');
			//if(item.xtype=='routedetail'){item.setSubmitParams({id:id})};
			item.setSubmitParams(sParams);
    		main.setActiveItem(item);
		}
		
    },
	onActiveItemchange:function(view , value , oldValue , eOpts){
		//切换card 触发新card的业务处理
		if(value.activeCallBack){
			value.activeCallBack.call(value,view,oldValue);
		}
		//切换card 关闭旧card弹出层
		var floatViews = Ext.Viewport.getItems().items;
		for(var i=0;i<floatViews.length;i++){
			if(floatViews[i].isFloated()&&!floatViews[i].isHidden()){
				floatViews[i].hide();
			}
		}
		var nbar = view.getNavigationBar(),
			bbtn = nbar.getBackButton(),
			expandTitle = (value.getExpandTitle) ? value.getExpandTitle() : value.config.expandTitle,
			backHide = value.getBackHide ? value.getBackHide() : value.config.backHide,
			title = (value.getTitle) ? value.getTitle() : value.config.title;
		nbar.resetExpandItems(value,view);
		
		if(typeof(backHide)!="undefined"){ 
			bbtn[!backHide ? 'show' : 'hide']();
		}

		if(expandTitle){
			setTimeout(function(){nbar.setTitle(expandTitle)},100);
		}
		
		if(title){
			setTimeout(function(){nbar.setTitle(title)},100);
		}
		
	},
    onCityButtonTap: function(btn) {
	   if(!Ext.Viewport.down('startCity')){
			var startCity = Ext.factory({
				releTarget:btn,
				store: Ext.create('Weidian.store.main.StartCity')
				
			},'Weidian.view.StartCity');
			Ext.Viewport.add(startCity);
			startCity.showBy(btn);
		}
    },
    onCategoryButtonTap:function(btn){
    	if(!Ext.Viewport.down('panel#filterPanel')){
    		var me = this,currView = cfg.view.getActiveItem(),
    			sParams = currView.getSubmitParams(),
    			cityName = sParams.cityName || '',hashCity = '';
			var chooiseCategory = Ext.factory({
		        cls:'start-city-view',
		        padding:'0',
				itemCls:'start-city-item',
				itemTpl:new Ext.XTemplate(
		        		'{CATEGORY}'
		        ),
				data:Ext.shopInfo.categorys,
				listeners:{
					select:function(dv,r){
						var dyParams = {categoryId:r.get('PID')};
						if(!Ext.isEmpty(cityName)){
							dyParams.cityName = cityName;
							hashCity = '_'+encodeURI(cityName);
							cfg.view.getNavigationBar().setTitle(r.get('CATEGORY')+'<p class="f12 fthin">'+cityName+'</p>');
						}else{
							cfg.view.getNavigationBar().setTitle(r.get('CATEGORY'));
						}
						cfg.view.getActiveItem().down('routelist').setDyParams(dyParams);
						window.location.hash = 'route/category/'+r.get('PID')+hashCity;
						
						if(util.getAllPlatformsInfo().isWeixinApp){
							var imgUrl = 'resources/images/noface.gif';
				       		if(!Ext.isEmpty(Ext.shopInfo.LOGO)){
				       			imgUrl = Ext.imgDomain+util.pathImage(Ext.shopInfo.LOGO,'320X320');
				       		}
				   			var config={
				   				title : Ext.shopInfo.TITLE+'('+r.get('CATEGORY')+')',
				   	            desc: Ext.shopInfo.ABOUT,
				   	            link:  window.location.href,
				   	            imgUrl: imgUrl
				   			};
				   			util.shareFromWeixinInit(config);
						}
						Ext.Viewport.down('panel#filterPanel').hide();
					}
				}
				
			},'Ext.dataview.DataView');
			//var search = Ext.create('Ext.field.Search');
			var filterPanel = Ext.create('Ext.Panel',{
				itemId:'filterPanel',
				width: '100%',
				padding:'10',
		        modal: true,
				hideOnMaskTap: true,
				cls:'D_dialog',
				floated: true,
				items:[chooiseCategory,{
					docked:'bottom',
					xtype:'toolbar',
					padding:'0',
					items:[{
						xtype:'label',
						cls:'f14 fthin fcgray',
						hidden:Ext.isEmpty(cityName),
						html:'<span>关键字：</span>'+cityName
					},'->',{
						text:'热门目的地',
						cls:'f14',
						handler:function(){
							me.redirectTo('route/city/search');
						}
					}]
				}],
				listeners:{
					hide:function(g){
		         		Ext.Viewport.remove(g);
		         	}
				}
			});
			Ext.Viewport.add(filterPanel);
			filterPanel.showBy(btn);
		}
    },
    onUserButtonTap: function(){
		this.redirectTo('ucenter');
	},
	onHomeButtonTap: function(){
		this.redirectTo('home');
	},
	onSearch:function(){
		this.redirectTo('route/city/search');
	},
    onAddVisitorBtnTap:function(){
    	cfg.view.getActiveItem().setSubmitParams({visitor:null});
    	this.redirectTo('visitor/save/orderid');
    },
    onSearchFinish:function(sf){
    	Ext.toast(sf.getValue(),cfg.toastTimeout);
    },
    saveVisitor:function(){
    	var view = cfg.view.getActiveItem(),
    		form = view.down('formpanel'),
			datas = form.getValues(),
			id = form.getViewModel().get('ID');
    	if(cfg.priceAttrs&&cfg.priceAttrs.length>0){
			Ext.Array.each(cfg.priceAttrs,function(o,i){
				if(o.TITLE==datas.ATTR_NAME){
					datas.ATTR_ID = o.ID;
					return false;
				}
			});
    	}
		var error = this.errorVisitor(datas);
		if(error){
			Ext.toast(error,cfg.toastTimeout);
		}else{
			datas.SEX = this.getSex(datas.CARD_NO)=='0'?'男':'女';
			datas.SEX1 = this.getSex(datas.CARD_NO)=='0'?'1':'2';
			
			util.request(cfg.url.order.visitor.save,{
				id:id,
				visitor:Ext.encode(datas)
			},function(rs){
				var vl = cfg.view.pop();
				window.location.hash='visitor/contacts/list';
				vl.down('visitorlist').getStore().loadPage(1);
				//vl.visitorList.getStore().insert(0,Ext.create('Weidian.model.order.visitor.Model',datas));
			});
		}
		
    },
    trim:function(s){
    	return s.replace(/(^\s*)|(\s*$)/g, "");
    },
    getSex:function(t){
    	t = t+'';
    	return t = this.trim(t.replace(/ /g, "")),
        15 == t.length ? t.substring(14, 15) % 2 == 0 ? 0 : 1 : 18 == t.length ? t.substring(14, 17) % 2 == 0 ? 0 : 1 : null;
    },
    getBirth15:function(t){
    	t = t +'';
    	var e = t.substring(6, 8)
	        , r = t.substring(8, 10)
	        , a = t.substring(10, 12)
	        , n = new Date(e,parseFloat(r) - 1,parseFloat(a))
	        , i = ""
	        , g = "";
	      return i = n.getMonth() + 1 < 10 ? "0" + (n.getMonth() + 1) : n.getMonth() + 1,
	      g = n.getDate() < 10 ? "0" + n.getDate() : n.getDate(),
	      n.getYear() != parseFloat(e) || n.getMonth() != parseFloat(r) - 1 || n.getDate() != parseFloat(a) ? {
	          isValidityBrith: !1,
	          date: n.getFullYear() + "-" + i + "-" + g
	      } : {
	          isValidityBrith: !0,
	          date: n.getFullYear() + "-" + i + "-" + g
	      }
    },
    getBirth18:function(t){
    	t = t +'';
    	var e = t.substring(6, 10)
	        , r = t.substring(10, 12)
	        , a = t.substring(12, 14)
	        , n = new Date(e,parseFloat(r) - 1,parseFloat(a))
	        , i = ""
	        , g = "";
	      return i = n.getMonth() + 1 < 10 ? "0" + (n.getMonth() + 1) : n.getMonth() + 1,
	      g = n.getDate() < 10 ? "0" + n.getDate() : n.getDate(),
	      n.getFullYear() != parseFloat(e) || n.getMonth() != parseFloat(r) - 1 || n.getDate() != parseFloat(a) ? {
	          isValidityBrith: !1,
	          date: n.getFullYear() + "-" + i + "-" + g
	      } : {
	          isValidityBrith: !0,
	          date: n.getFullYear() + "-" + i + "-" + g
	      }
    },
    cardChar:function(t){
    	var e = 0,
    		l = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1], 
        	o = [1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2];
        "x" == t[17].toLowerCase() && (t[17] = 10);
        for (var r = 0; 17 > r; r++)
            e += l[r] * t[r];
        return valCodePosition = e % 11,t[17] == o[valCodePosition] ? !0 : !1
    },
    isIdCard:function(t){
    	t = t +'';
    	if (t = this.trim(t.replace(/ /g, "")),15 == t.length)
            return this.getBirth15(t);
        if (18 == t.length) {
            var r = t.split("");
            return this.getBirth18(t) && this.cardChar(r) ? !0 : !1;
        }
        return !1
    },
    errorVisitor:function(d){
    	var msg = false,
    		errors = {
	    		NAME:'请输入游客姓名',
	    		ATTR_NAME:'请选择游客类型',
	    		MOBILE:'请输入正确的手机号码',
	    		CARD_NO:'请输入正确的证件号码'
    		};
    	for(var i in d){
    		if(!d[i]){
    			msg = errors[i];
    			continue;
    		}else if(i=='MOBILE'){
    			var reg = {
					mobile: /^1[34578]\d{9}$/,
					tel: /^\d{5,11}$/
    			};
    			if(!reg.mobile.test(d[i])&&!reg.tel.test(d[i])){
    				msg = errors[i];
    			}
    			continue;
    		}else if(i=='CARD_NO'){
    			if(!this.isIdCard(d[i])){
    				msg = errors[i];
    			}
    			continue;
    		}
    	}
    	return msg;
    },
    moreMenus:function(btn){
    	var items,
    		view = cfg.view.getActiveItem();
    	
    	if(Ext.isArray(btn.actions)){
    		items = btn.actions;
    	}else{
    		items = btn.actions();
    	}
    	items.handler = function(){item};
    	Ext.Array.each(items,function(item,i){
    		if(item.call){
    			item.handler = function(){
    				Ext.Viewport.removeMenu('bottom');
    				Ext.Viewport.remove(Ext.Viewport.down('menu#moreMenus'));
    				view[item.call].call(view,item);
    			};
    		}
    	});
    	Ext.Viewport.setMenu({
			zIndex:3,
			itemId:'moreMenus',
			userCls:'D_menu_float',
			listeners:{
				hide:function(m){
					Ext.Viewport.removeMenu('bottom');
					Ext.Viewport.remove(m);
				}
			},
			items: items
		}, {
            side: 'bottom',
            cover: true
        });
		Ext.Viewport.showMenu('bottom');
    }
});
