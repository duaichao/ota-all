Ext.application({
    name: 'app',
    namespace: 'app',
	appFolder:cfg.getJsPath()+'/app',
    controllers: 'index',
    init: function() {
    	var	cktheme = util.cookie.get('cookie-theme')||'',
    		usetheme = cktheme.length==6?'#'+cktheme:'url('+(cktheme||cfg.getImgPath()+'/wallpapers/repeat/blue.png')+')',
    		isRepeat = 'no-repeat',
    		cover = 'cover';
    	
    	if(cktheme.indexOf('/repeat/')!=-1){
    		isRepeat = 'repeat';
    		cover = 'auto';
    	}
    	/*Ext.getBody().setStyle({
			'background':usetheme,
			'background-position': 'center 0',
			'background-repeat': 'no-repeat',
			'background-attachment': 'fixed;',
			'background-size': 'cover',
			'-webkit-background-size': 'cover',
			'-o-background-size': 'cover'
		});
    	*/
    	Ext.getBody().setStyle({
    		'background':'rgba(38, 41, 46, 0.8)'
    	});
    	var el = Ext.getBody().appendChild({
    		tag:'div',
    		id:'background_cover'
    	},false);
    	el.setStyle({
    	    'position':' absolute',
        	'z-index':' -1',
    		'width':'100%',
    		'height':'100%',
			'background-image':usetheme,
			'background-position': 'center 0',
			'background-repeat': isRepeat,
			'background-attachment': 'fixed',
			'background-size': cover,
			'-webkit-background-size': cover,
			'-o-background-size': cover,
        	'-webkit-filter': 'blur(0px)',
           	'-moz-filter': 'blur(0px)',
            '-ms-filter': 'blur(0px)',    
        	'filter': 'progid:DXImageTransform.Microsoft.Blur(PixelRadius=0, MakeShadow=false)'
		});
    	//创建头部菜单
		Ext.create('app.store.HeadNavigation', {
            storeId: 'headnavigation'
        });
		//创建系统菜单
		Ext.create('app.store.Navigation', {
            storeId: 'navigation'
        });
        Ext.create({
        	plain:true,
        	renderTo:Ext.getBody(),
        	id:'btn-guide',
        	width:35,
        	height:35,
        	cls:'plain btn-menus',
        	text:'<i class="iconfont f20" style="position:relative;top:0px;">&#xe60e;</i>',
        	xtype:'button',
        	listeners:{
        		'click':function(b,event){
        			//if(!this.menu.isHidden())
        				//this.menu.setX(10, true);
        			var x = 5,
        				y = event.getY()-450,
        				win;
        			if(!Ext.fly('navsWindow')){
	        			win = Ext.create('Ext.window.Window',{
	        				id:'navsWindow',
	        				cls:'navsWindow',
	        				header:false,
	        				width:700,
	        				closeAction:'hide',
	        				height:450,
	        				draggable:false,
	        				autoScroll:true,
	        				resizable:false,
	        				shadow:true,
	        				animateTarget:b.el,
	        				modal:true,
	        				bodyStyle:'background:rgba(0,0,0,.7);',
	        				items:[{
	        					store:Ext.StoreMgr.get('navigation'),
	                    		xtype:'allthumbnails'
	        				}],
	        				listeners:{
	        					show:function(){
	        						//b.setText('<i class="iconfont f20" style="position:relative;top:2px;">&#xe60e;</i>');
	        					},
	        					hide:function(){
	        						//b.setText('<i class="iconfont f20" style="position:relative;top:0px;">&#xe7a9;</i>');
	        					}
	        				}
	        			}).showAt(x,y);
	        			win.mon(Ext.getBody(), 'click', function(el, e){
	        	            win.hide();
	        	        }, win, { delegate: '.x-mask' });
        			}else{
        				win = Ext.getCmp('navsWindow');
        				if(win.isHidden()){
        					win.show();
        				}else{
        					win.hide();
        				}
        			}
        		
        		}
        	}
        });
        // Set the default route to start the application.
        this.setDefaultToken('home');
        
        /*
        var _websocket;
        if ('WebSocket' in window) {
            _websocket = new WebSocket("ws://localhost/webSocketServer");
        } else if ('MozWebSocket' in window) {
            _websocket = new MozWebSocket("ws://localhost/webSocketServer");
        } else {
            _websocket = new SockJS("http://localhost/sockjs/webSocketServer");
        }
        _websocket.onopen = function(evt) {
			console.log("Connected to WebSocket server.");
		};
		_websocket.onclose = function(evt) {
			console.log("Disconnected");
		};
        _websocket.onmessage = function (evnt) {console.log(evnt);
            Ext.getCmp("msgcount").setBadgeText(evnt.data);
        };
        _websocket.onerror = function(evt) {
			console.log('Error occured: ' + evt);
		};
        */
    },
    autoCreateViewport:'app.view.index'
});