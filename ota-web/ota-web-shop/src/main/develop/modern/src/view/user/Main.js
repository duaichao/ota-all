Ext.define('Weidian.view.user.Main', {
    extend: 'Weidian.view.Base',
    xtype: 'ucenter',
	config: {
		title:'我的',
		userCls:'D_me',
		controller:'user',
		backHide:true,
		expandItems: [{
			ripple:false,
			iconCls:'x-fm fm-home f15',
			handler: 'onHomeButtonTap',
			listeners: {
				scope: 'controller',
				element: 'element'
			}
		},{
			align:'right',
			ripple:false,
			iconCls:'x-fm fm-more f15',
			handler:'moreMenus',
			actions:[{
				iconCls:'x-fm fm-phone fcorange f18',
				text:'联系我们',
				call:'contactUs'
			}]
		}]
	},
	initialize: function(){
    	this.initViews();
    	this.callParent(arguments);
    },
    initViews:function(){
    	var items = [];
    	items.push(Ext.create('Ext.Container',{
    		scrollable:false,
    		padding:10,
    		userCls:'D_me_info_bg',
            layout: {
                type: 'hbox',
                align: 'middle'
            },
    		items:[{
    			xtype:'image',
    			cls: 'br50',
    			height: 60,
                width: 60,
                src:cfg.user.FACE?Ext.imgDomain+cfg.user.FACE:'resources/images/noface.gif',
                //src: 'resources/images/noface.gif',
                listeners:{
                	error:function(img){img.setSrc('resources/images/noface.gif');},
                	tap:function(){
                		Ext.Msg.alert('提示', '浏览器暂不支持头像上传、信息修改，可到PC网站    <a href="javascript:void(0);" class="fcblue">'+Ext.shopInfo.PDOMAIN+'  </a>   进行设置', Ext.emptyFn);
                	}
                }
    		},{
    			flex:1,
    			xtype:'container',
    			padding:'0 10',
    			html:'<div class="fcwhite f14">'+(cfg.user.CHINA_NAME||cfg.user.USER_NAME)+'</div>'
    		}]
    	}));
    	items.push(Ext.create('Ext.List',{
    		itemId:'menulist',
    		cls:'D_menu',
    		onItemDisclosure:true,
    		itemCls:'D_menu_item',
    		itemTpl:'{text}',
    		data:[{
    			text:'我的订单',
    			action:'order'
    		}/*,{
    			text:'个人信息',
    			action:'editinfo'
    		}*/,{
    			text:'修改密码',
    			action:'editpassword'
    		},{
    			text:'常用游客',
    			action:'myvisitors'
    		},{
    			text:'我的收藏',
    			action:'myfavorite'
    		},{
    			text:'清除缓存',
    			action:'clearcache'
    		}]
    	}));
    	items.push({
    		xtype:'container',
    		padding:'0 10',
    		items:[{
        		xtype:'button',
        		ui:'red',
        		height:45,
        		margin:'10 0 0 0',
    			width: '100%',
        		text:'退出登录'
        	}]
    	});
    	this.setItems(items);
    	this.down('button').on('tap',this.logout,this);
    	this.down('list#menulist').on('itemtap',this.onMenuTap,this);
    },
    logout:function(){
    	util.request(cfg.url.logout);
    	localStorage.removeItem('loggedIn');
    	cfg.user = null;
    	util.goHome('notip');
    },
    onMenuTap:function(l,i,t,r){
    	if(r.get('action')=='order'){
    		this.getController().redirectTo('me/order/list');
    	}
    	if(r.get('action')=='editpassword'){
    		this.getController().redirectTo('editpassword');
    	}
    	if(r.get('action')=='editinfo'){
    		this.getController().redirectTo('editinfo');
    	}
    	if(r.get('action')=='myvisitors'){
    		this.getController().redirectTo('visitor/contacts/list');
    	}
    	if(r.get('action')=='myfavorite'){
    		this.getController().redirectTo('me/favorite/list');
    	}
    	if(r.get('action')=='clearcache'){
    		localStorage.clear();
    		this.getController().redirectTo('home');
    	}
    	
    },
    contactUs:function(){
    	cfg.view.initPluginPlusDialog(null,'contcat');
    }
});