Ext.define('Weidian.Util', {
    alternateClassName: 'util',
    statics: {
    	getDateAndWeek:function(date){
    		return Ext.Date.format(date,'Y/m/d')+' '+(Ext.Date.dayNames[parseInt(Ext.Date.format(date,'w'))]);
    	},
    	getShortDayName:function(date) {
    		var day = date.getDay();
            return Ext.Date.dayNames[day].substring(2, 3);
        },
    	getRandomColor:function () {
    	    var letters = '0123456789ABCDEF'.split('');
    	    var color = '#';
    	    for (var i = 0; i < 6; i++ ) {
    	        color += letters[Math.floor(Math.random() * 16)];
    	    }
    	    return color;
    	},
		timeAgoInWords: function (value) {
			
			value = value || '';
			var date = Ext.Date.parse(value, "Y-m-d H:i:s", true);
			if(!date)return value;
			var now = Date.now();
			var seconds = Math.floor((now - date) / 1000),
				minutes = Math.floor(seconds / 60),
				hours = Math.floor(minutes / 60),
				days = Math.floor(hours / 24),
				weeks = Math.floor(days / 7),
				months = Math.floor(days / 30),
				years = Math.floor(days / 365),
				ret;

			months %= 12;
			weeks %= 52;
			days %= 365;
			hours %= 24;
			minutes %= 60;
			seconds %= 60;

			if (years) {
				ret = util.part(years, 'Year');
				ret += util.part(months, 'Month', ' ');
			} else if (months) {
				ret = util.part(months, 'Month');
				ret += util.part(days, 'Day', ' ');
			} else if (weeks) {
				ret = util.part(weeks, 'Week');
				ret += util.part(days, 'Day', ' ');
			} else if (days) {
				ret = util.part(days, 'Day');
				ret += util.part(hours, 'Hour', ' ');
			} else if (hours) {
				ret = util.part(hours, 'Hour');
			} else if (minutes) {
				ret = util.part(minutes, ' Minute');
			} else {
				ret = util.part(seconds, 'Second');
			}

			return ret;
		},
		part: function (value, type, gap) {
			var ret = value ? (gap || '') + value + ' ' + type : '';
			if (value > 1) {
				ret += 's';
			}
			return ret;
		},
    	request :function(url,params,callback,scope){
    		if(url.indexOf('http')==-1){url = Ext.domain+url;}
    		Ext.Ajax.request({
                url: url,
                params:params,
                success: function (result) {
                    result = Ext.decode(result.responseText);
                    if(result.data){
	                    if (!result.success) {
	                    	if(result.message=='appout'){
	                    		Ext.toast('登陆失效，请重新登陆',cfg.toastTimeout);
	                    		window.location.hash = 'login';
	                    		localStorage.removeItem('loggedIn');
	                        	cfg.user = null;
	                    	}else{
	                    		util.goHome('notip');
	                    	}
	                    } else {
	                    	if(callback)callback.call(scope,result);
	                    }
                    }else{
                    	if(callback)callback.call(scope,result);
                    }
                }
            });
    	},
    	goHome:function(msg){
    		if(msg!='notip'){Ext.toast(msg||'无效的请求',cfg.toastTimeout);}
    		Ext.Viewport.down('main').reset();
    		window.location.hash = 'home';
    	},
        log:function(message){
        	 if (Ext.os.is('iPhone'))
                 console.log(message);
             else
                 console.info(message);
        },
		getUser:function(){
			var localUser = localStorage.getItem("localUser");
			return Ext.decode(localUser);
		},
		getDeviceSize:function(){
			var viewWidth = Ext.Element.getViewportWidth(),
				viewHeight = Ext.Element.getViewportHeight(),
				deviceWidth = Math.min(viewWidth, 400) - 40,
				deviceHeight = Math.min(viewHeight/2,400)-40;

			return {width:deviceWidth,height:deviceHeight};
		},
		fmWordText:function(html,nobr){
			html = html ||'';
			if(html=='')return html;
			
			html = html.replace(/\n/g,'<br>').replace(/\n\t/g, '<br>').replace(/&nbsp;/g,'');
			
			if(nobr){
				html = html.replace(/<br>/g,'').replace(/<br\/>/g, '').replace(/&nbsp;/g,'');
			}
			html = html.replace(/<(img[^>]*) width=([^ |>]*)([^>]*)/gi,"<$1$3");
			html = html.replace(/<(img[^>]*) height=([^ |>]*)([^>]*)/gi,"<$1$3");
			
			html = html.replace(/<\/?SPAN[^>]*>/gi, "" );// Remove all SPAN tags

			html = html.replace(/<(\w[^>]*) class=([^ |>]*)([^>]*)/gi, "<$1$3") ; // Remove Class attributes

			html = html.replace(/<(\w[^>]*) style="([^"]*)"([^>]*)/gi, "<$1$3") ; // Remove Style attributes

			html = html.replace(/<(\w[^>]*) lang=([^ |>]*)([^>]*)/gi, "<$1$3") ;// Remove Lang attributes

			html = html.replace(/<\\?\?xml[^>]*>/gi, "") ;// Remove XML elements and declarations

			html = html.replace(/<\/?\w+:[^>]*>/gi, "") ;// Remove Tags with XML namespace declarations: <o:p></o:p>

			html = html.replace(/ /, " " );// Replace the


			var re = new RegExp("(<P)([^>]*>.*?)(<\/P>)","gi") ; // Different because of a IE 5.0 error

			html = html.replace( re, "<div$2</div>" ) ;
			
			
			
			html = html.replace(/src/gi,"data-src");
			html = html.replace(/<img/gi,'<img class="lazy_flash"');
			return html;
		},
	    createStore:function(url,autoLoad,pageSize){
	    	var fields = null;
	    	url = Ext.domain+url;
	    	pageSize = pageSize||cfg.pageSize;
	    	return Ext.create('Ext.data.Store', {
	            pageSize: pageSize,
	            autoLoad: autoLoad,
	            proxy: {
			         type: 'ajax',
			         noCache:true,
			         url: url,
			         reader: {
			             rootProperty: 'data',
	            		 totalProperty: 'totalSize'
			         }
			     }
	        });
	    },
	    loader:function(msg){
	    	var loader = Ext.Viewport.down('container#loader');
	    	if(loader){
	    		loader.show();
	    		return;
	    	}else{
	    		loader = Ext.create('Ext.Container', {
	       		 	itemId:'loader',
	                centered : true,
	                cls:'D_loader',
	                modal: {xtype:'mask',transparent:true},
	                hideOnMaskTap : false,
	                styleHtmlContent:true,
	                html:''
	            });
	            Ext.Viewport.add(loader);
	            loader.show();
	    	}
	    },
	    hideLoader:function(){
	    	var loader = Ext.Viewport.down('container#loader');
	    	if(loader){
	    		loader.hide();
	    	}
	    },
	    pathImage :function(url,size){
			size = size || '500X280';
			url = url || '';
			var ext = util.getFileExt(url);
			if(url.indexOf(size)!=-1){
				return url;
			}
			return url.replace('-500X280','').replace('.'+ext,'-'+size+'.'+ext);
		},
		getFileExt :function(url){
			//不带点
			url = url || '';
			return url.substring(url.lastIndexOf('.') + 1);
		},
	    
	    /**
	     * =======================================================
	     * 客户端浏览器判断工具begin
	     */
		share:function(config){
			Ext.Viewport.setMenu({
				zIndex:3,
				itemId:'shareMenus',
				userCls:'D_menu_float',
				listeners:{
					hide:function(m){
						Ext.Viewport.removeMenu('bottom');
						Ext.Viewport.remove(m);
					}
				},
				items: [{
	                text: '微信好友',
	                iconCls: 'x-fm fm-weixin fcweixin',
	                handler: function(){config.platform = 'WechatFriends';util.doShare(config);}
	            }, {
	                text: '微信朋友圈',
	                iconCls: 'x-fm fm-pengyouquan fcpengyouquan',
	                handler: function(){config.platform = 'WechatTimeline';util.doShare(config);}
	            }, {
	                text: '新浪微博 ',
	                hidden:util.getAllPlatformsInfo().isWeixinApp,
	                iconCls: 'x-fm fm-xinlang fcxinlang',
	                handler: function(){config.platform = 'SinaWeibo';util.doShare(config);}
	            }]
			}, {
	            side: 'bottom',
	            cover: true
	        });
			Ext.Viewport.showMenu('bottom');
		},
		doShare:function(config){
			Ext.Viewport.removeMenu('bottom');
			Ext.Viewport.remove(Ext.Viewport.down('menu#shareMenus'));
			var t = util.getAllPlatformsInfo();
	     	if(t.isUCBrowser){
	     		util.shareFromUC(config);
	     	}else if(t.isWeixinApp){
	     		util.shareFromWeixin(config);
	     	}else{
	     		util.sendShare();
	     	}
		},
		shareFromUC:function(config){
			var n = {
		            sweibo: ["SinaWeibo", "新浪微博"],
		            friend: ["WechatFriends", "微信好友"],
		            fsircle: ["WechatTimeline", "微信朋友圈"]
		        }, i = {
	                iweibo: "kSinaWeibo",
	                ifriend: "kWeixin",
	                ifcircle: "kWeixinFriend",
	                asweibo: "SinaWeibo",
	                afriend: "WechatFriends",
	                afcircle: "WechatTimeline"
	            }, o = config.link.indexOf("?") >= 0 ? "&" : "?"
	    		, a = {
    	            qqfriend: "qqfriend",
    	            qqweichat: "qqweichat",
    	            ucfriend: "ucfriend",
    	            ucweichat: "ucweichat"
    	        }, s = config.platform
	    		, r = config.link
	    		, c = 500;
	    	"undefined" != typeof ucweb ? setTimeout(function() {
	            ucweb.startRequest("shell.page_share", [config.title, config.desc, config.link, config.platform, "", "我们正在看【" + config.title + "】，一起来看吧", e.imgUrl])
	        }, c) : "undefined" != typeof ucbrowser && (s == n.sweibo[0] ? s = i.iweibo : s == n.friend[0] ? (s = i.ifriend,
	        r += o + "from=" + a.ucfriend) : s == n.fsircle[0] && (s = i.ifcircle,
	        r += o + "from=" + a.ucweichat),
	        setTimeout(function() {
	        	r = r.split('?')[0];
	            ucbrowser.web_share(config.title, config.desc, r, s, "", "", config.imgUrl)
	        }, c))
		},
		shareFormWeixinLoadSign:function(config,s){
			
			console.log(s);
			s = s || config.link+'/';
			util.request(cfg.url.getWeixinSign,{
				url:s
			},function(rs){
				wx.config({
                      debug: false,
                      appId: rs.appId,
                      timestamp: rs.timestamp,
                      nonceStr: rs.nonceStr,
                      signature: rs.signature,
                      jsApiList: ["onMenuShareAppMessage", "onMenuShareTimeline"]
				});
				wx.ready(function(){
					console.log('ready..............load');
					wx.onMenuShareAppMessage(config);
					wx.onMenuShareTimeline(config);
					wx.error(function(e) {console.log(e);});
				});
				localStorage.setItem('weixinSign',Ext.encode(rs));
				localStorage.setItem('weixinSignUrl',s);
			});
		},
		shareFromWeixinInit:function(config){
			var s = window.location.href||config.link,
				weixinSign = localStorage.getItem("weixinSign"),
				weixinSignUrl = localStorage.getItem("weixinSignUrl"),
				now = new Date();
			s = s.substr(0,s.indexOf('#'));
			console.log(s+'================='+weixinSignUrl);
			if( weixinSign && s == weixinSignUrl && weixinSignUrl!=''){
				console.log('（url相同）已缓存localStorage');
				weixinSign = Ext.decode(weixinSign);
				var lastDate = new Date(parseInt(weixinSign.expires_in));
				console.log(Ext.Date.format(now,'Y-m-d H:i:s')+'==========='+Ext.Date.format(lastDate,'Y-m-d H:i:s')+'================='+Ext.Date.diff(lastDate,now,Ext.Date.HOUR));
				if(Ext.Date.diff(lastDate,now,Ext.Date.HOUR)>0){
					console.log('超过2小时');
					util.shareFormWeixinLoadSign(config,s);
				}else{
					wx.config({
	                      debug: false,
	                      appId: weixinSign.appId,
	                      timestamp: weixinSign.timestamp,
	                      nonceStr: weixinSign.nonceStr,
	                      signature: weixinSign.signature,
	                      jsApiList: ["onMenuShareAppMessage", "onMenuShareTimeline"]
					});
					wx.ready(function(){
						console.log('ready..............');
						wx.onMenuShareAppMessage(config);
						wx.onMenuShareTimeline(config);
						wx.error(function(e) {console.log(e);});
					});
				}
			}else{
				console.log('（url不相同）第一次刷新页面，未缓存localStorage');
				util.shareFormWeixinLoadSign(config,s);
			}
		},
		shareFromWeixin:function(config){
            config.cancel = function(e) {
            	Ext.toast('已取消分享',2000);
            };
			//注册分享好友
			if(config.platform=='WechatFriends'){
				config.success = function(e) {
	                Ext.toast('已分享至好友',2000);
	            };
				wx.onMenuShareAppMessage(config);
			}
			//注册分享朋友圈
			if(config.platform=='WechatTimeline'){
				config.success = function(e) {
	                Ext.toast('已分享至朋友圈',2000);
	            };
				wx.onMenuShareTimeline(config);
			}
			var tips = Ext.create('Ext.Container',{
	    		floated:true,
	    		modal:true,
	    		userCls:'share-dialog weixin',
	    		hideOnMaskTap:true,
	    		listeners:{
	    			hide:function(g){
						Ext.Viewport.remove(g);
					}
	    		},
	    		height:80,
	    		width:'100%',
	    		layout:'fit',
	    		items:[{
	    			xtype:'image',
	        		src:'resources/images/sharetofriend.png',
	        		listeners:{
	        			tap:function(){tips.hide();}
	        		}
	    		}]
	    	});
	    	Ext.Viewport.add(tips);
	    	tips.show();
		},
	    sendShare:function(){
	    	var tips = Ext.create('Ext.ActionSheet',{
	    		userCls:'share-dialog safari',
	    		hideOnMaskTap:true,
	    		listeners:{
	    			hide:function(g){
						Ext.Viewport.remove(g);
					}
	    		},
	    		height:90,
	    		layout:'fit',
	    		items:[{
	    			xtype:'image',
	        		src:'resources/images/share_safari_1.png',
	        		listeners:{
	        			tap:function(){tips.hide();}
	        		}
	    		}]
	    	});
	    	Ext.Viewport.add(tips);
	    	tips.show();
	    },
	    getUaIDs:function(){
	    	return {
	            weibo: "weibo",
	            sinanews: "sinanews",
	            qqBrowser: "mqqbrowser/",
	            ucBrowser: "ucbrowser/",
	            baidu: "baidu",
	            weixin: "micromessenger",
	            qq: "qq",
	            iphone: "iphone",
	            ipod: "ipod",
	            safari: /(Safari)\/(.*?)(?:\s{1}|$)/i,
	            other: ["crios/", "sogou", "browser", "opera"]
	        };
	    },
	    includeStr: function(e, t) {
	    	e = e || 'chrome';
	        return -1 != e.indexOf(t)
	    },
	    checkApp: function(e) {
	        var t = util.includeStr(navigator.appVersion.toLowerCase(), e);
	        return t ? !0 : !1
	    },
	    isWeiboApp: function() {
	        return util.checkApp(util.getUaIDs().weibo)
	    },
	    isSinanews: function() {
	        return util.checkApp(util.getUaIDs().sinanews)
	    },
	    isQQBrowser: function() {
	        return util.checkApp(util.getUaIDs().qqBrowser)
	    },
	    isSafari: function() {
	        var t = util.isBaiduBrowser() || util.isQQBrowser() || util.isUCBrowser() || util.isSinanews() || util.isWeiboApp() || util.isWeixinApp()
	          , n = !1;
	        for (var i in util.getUaIDs().other) {
	            var o = util.getUaIDs().other[i];
	            !n && (n = util.checkApp(o))
	        }
	        return !util.isAndroid() && navigator.appVersion.toLowerCase().match(util.getUaIDs().safari) && !t && !n
	    },
	    isUCBrowser: function() {
	        return util.checkApp(util.getUaIDs().ucBrowser)
	    },
	    isBaiduBrowser: function() {
	        return util.checkApp(util.getUaIDs().baidu)
	    },
	    isWeixinApp: function() {
	        return util.checkApp(util.getUaIDs().weixin)
	    },
	    isQQApp: function() {
	        return util.checkApp(util.getUaIDs().qq) && util.checkApp("nettype")
	    },
	    isAndroid: function() {
	        var e = util.getPlatform();
	        return e == 'android' ? !0 : !1
	    },
	    isIOS: function() {
	        var e = util.getPlatform();
	        return e == 'ios' ? !0 : !1
	    },
	    getPlatform: function() {
	        var e = util.includeStr(navigator.appVersion.toLowerCase(), util.getUaIDs().iphone)
	          , t = util.includeStr(navigator.appVersion.toLowerCase(), util.getUaIDs().ipod);
	        return e || t ? 'ios' : 'android'
	    },
	    getAllPlatformsInfo:function(){
	    	return {
	            isWeiboApp: util.isWeiboApp(),
	            isSinanews: util.isSinanews(),
	            isBaiduBrowser: util.isBaiduBrowser(),
	            isWeixinApp: util.isWeixinApp(),
	            isQQApp: util.isQQApp() && !util.isWeixinApp(),
	            isQQBrowser: util.isQQBrowser() && !util.isQQApp() && !util.isWeixinApp(),
	            isUCBrowser: util.isUCBrowser(),
	            isSafari: util.isSafari()
	        }
	    },
	    /**
	     * 客户端浏览器判断工具end
	     * =======================================================
	     */
	    init:function(){
	    	//开始加载
	    	Ext.Ajax.on('beforerequest',function (connection, options) {
	    		options.params = options.params||{};
	    		Ext.applyIf(options.params,cfg.defaultParams);
	    		if(!options.params.noLoader){
	    			util.loader();
	    		}
	    	});
	    	//加载成功
	    	Ext.Ajax.on('requestcomplete',function (conn, response, options, eOpts) {
	    		options.params = options.params||{};
	    		if(!options.params.noLoader){
	    			util.hideLoader();
	    		}
	    		var result = Ext.decode(response.responseText);
                if(result.data){
                    if (!result.success) {
                    	if(result.message=='appout'){
                    		Ext.toast('登陆失效，请重新登陆',cfg.toastTimeout);
                    		window.location.hash = 'login';
                    		localStorage.removeItem('loggedIn');
                        	cfg.user = null;
                    	}
                    }
                }
	    	});
	    	//加载失败
	    	Ext.Ajax.on('requestexception',function (connection, options) {
	    		util.hideLoader();
	    	});
	    	//
	    	Ext.override(Ext.Toast, {  
			    listeners:{
			    	show:function(toast){
			    		var timeout = 1000 * (5 + (toast.html && toast.html.split(/[\\pP‘’“”]/g).length || 0));
			    		toast.setTimeout(timeout);
			    	}
			    }
			}); 
	    }
    }
});