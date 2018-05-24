var cookie = Ext.state.Manager.getProvider(),
	cktheme = cookie.get('cookie-theme')||'',
	themeimg = cktheme.length>6?cktheme:cfg.getImgPath()+'/wallpapers/wood.jpg',
	themecor = cktheme.length==6?cktheme:'000000';
	user = cfg.getUser(),
	face= user.face;
	if(face){
		face = cfg.getPicCtx()+'/'+face;
	}else{
		face = cfg.getImgPath()+'/noface.gif';
	}
var upv = cfg.getUser().userType,userPruviews={
	'-1':false,
	'01':cfg.getCtx()+'/help/help_zqgl.html',
	'02':cfg.getCtx()+'/help/help_gys1.html',
	'03':cfg.getCtx()+'/help/help_zts1.html'
};
if(upv.length>2){upv = upv.substring(0,2);}
Ext.define('app.view.header', {
    extend: 'Ext.Container',
    xtype: 'appHeader',
    cls:'head-container',
    id: 'app-header',
    title: '',
    height: 75,
    layout: {
        type: 'hbox',
        align: 'top'
    },
    initComponent: function() {
        document.title = '136旅游平台';
        //var headnavigation = Ext.create('app.store.HeadNavigation');
        
        var themeMenus = Ext.create('Ext.view.View', {
        	itemId:'themeMenu',
        	cls:'hover-view-container',
			width:652,
			store: {
		        fields: ['src','name'],
		        data: [
		            //{src: '/wallpapers/star.png',name:'星空',value:2},
		            //{src: '/wallpapers/repeat/black.png',name:'深色',value:1},
		            {src: '/wallpapers/repeat/yellow.png',name:'暗黄色',value:1},
		            {src: '/wallpapers/repeat/blue.png',name:'蓝色',value:1},
		            {src: '/wallpapers/repeat/green.png',name:'芥末',value:1},
		            {src: '/wallpapers/repeat/pink.png',name:'美景',value:1},
		            {src: '/wallpapers/repeat/deepblue.png',name:'幽暗黄昏',value:1},
		            {src: '/wallpapers/repeat/deepred.png',name:'桃花心木色',value:1},
		            {src: '/wallpapers/repeat/lightgreen.png',name:'薄荷绿',value:1},
		            {src: '/wallpapers/repeat/flower.png',name:'樱花缤纷',value:1},
		            {src: '/wallpapers/table.png',name:'地板木',value:2},
		            {src: '/wallpapers/stone.png',name:'鹅卵石',value:2},
		            {src: '/wallpapers/sea.png',name:'海洋',value:2},
		            //{src: '/wallpapers/wood.png',name:'木头',value:2},
		            {src: '/wallpapers/leaves.png',name:'秋色',value:2},
		            {src: '/wallpapers/wood1.png',name:'桌子',value:2},
		            {src: '/wallpapers/defaults.png',name:'美景',value:2},
		            {src: '/wallpapers/random.png',name:'随机',value:3},
		        ]
		    },
			tpl: [
	          '<tpl for=".">',
	          		'<div class="theme-item">',
	              	'<img width="120" height="67" src="'+cfg.getImgPath()+'{src}" />',
	              	'<div class="name">{name}</div>',
	              	'</div>',
	          '</tpl>',
	       ],
	       overItemCls:'theme-over',
	       itemSelector: 'div.theme-item'
		});
        var userMenus = Ext.create('Ext.Container', {
			cls:'hover-view-container',
			width:400,
			layout: {
		        type: 'hbox',
		        pack: 'start',
		        align: 'stretch'
		    },
		    padding: 10,
			items:[{
				id:'btn-upload-img',
				xtype:'faceupload',
				url: cfg.getCtx()+'/upload/face',
				plain:true,
				cls:'plain face',
				style:'position:relative;',
				width:120,
				height:120,
				//margin:'10 0 20 0',
				text:[
					'<img src="'+face+'" width="120px" height="120px" onerror="javascript:this.src=\''+cfg.getImgPath()+"/noface.gif"+'\'"/>',
					'<span class="face-warp"><b class="face-bg"></b><span class="face-txt">换头像</span></span>',
				].join('')
			},{
				border:false,
				xtype:'panel',
				flex:1,
				height:120,
				cls:'p-user-info',
				padding:'0 0 0 10',
				dockedItems:[{
					xtype:'toolbar',
					dock:'bottom',
					items:['->',{
						cls:'blue',
						width:80,
						itemId:'btnUserInfo',
						height:40,
						scale:'medium',
						text:'个人中心'
					}]
				}],
				html:[
				      '<h3 style="font-size:15px;line-height:18px;white-space: normal;padding-bottom:5px;">'+user.companyName+'</h3>',
				      user.departName?'<div class="blue-color f14" style="white-space: normal;line-height:18px;">'+user.departName+'/'+user.username+'</div>':'<div class="blue-color f14">'+user.username+'</div>'
					  /*'<div style="padding:10px 0;background:#f1f1f1;text-align:center;">',
					  cfg.getUser().signature==''?'<span class="f999">个人中心可以设置个性签名</span>':
							'<span class="cor888 ht16" id="usersignature" data-qtip="'+cfg.getUser().signature+'">'+Ext.String.ellipsis(cfg.getUser().signature,80)+'</span>',
						'</div>'*/
					].join('')
			}]
		});
        
        this.items = [{
            xtype: 'component',
            width:123,
            height:75,
            margin:'0 0 0 10',
            cls:'logo',
            html:'<img src="'+cfg.getCtx()+'/resources/images/login/logo.png" height="50" alt="136旅游" title="136旅游">'
        },{
        	xtype:'navthumbnails',
        	cls:'navthumbnails head',
        	margin:'5 10 0 10',
        	store:'headnavigation',
        	flex:1,
        	listeners:{
        		afterrender:function(v){
        			v.getSelectionModel().select(0);
        		}
        	}
        },{
        	//style:'background:rgba(255,255,255,.4);',
        	xtype:'container',
        	cls:'head-small-btns',
        	layout:{
        		type:'vbox',
                pack: 'start',
                align: 'stretch'
        	},
        	width:267,
        	padding:'2 0 0 0',
        	bodyStyle:'background:transparent;overflow:visible;',
        	defaults:{
        		flex:1,
        		xtype:'toolbar',
        		padding:'2 0',
        		style:'background:transparent;overflow:visible;',
        		defaults:{width:45}
        	},
        	items:[{
        		items:[{
        			id:'btn-user',
        			width:205,
        			cls:'x-btn',    
        			style:'background: none repeat scroll 0 0 rgba(0, 0, 0, 0.2);border: 1px solid rgba(0, 0, 0, 0.2);border-radius: 30px;box-shadow: 0 1px 0 rgba(255, 255, 255, 0.1);',
        			xtype:'hoverbutton',
        			padding:'2px 0 2px 2px',
        			text:'<span style="background-image:url('+face+');display:inline-block;width:26px;height:26px;line-height:22px;font-size:14px;color:#f8f8f8;" class="d-img-size-auto">   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+util.format.fmEllipsis((user.companyName?user.companyName:user.username),12)+'</span>',
        			listeners:{
        				afterrender:function(b){
        					util.imageLoader(face,Ext.emptyFn,function(){
        						b.el.down('span.d-img-size-auto').setStyle({
        							'background-image':'url('+cfg.getImgPath()+'/noface.gif)'
        						});
        					});
        				}
        			},
        			menu:userMenus
        		},{
        			id:'btn-logout',
        			cls:'red',
        			style:'border-radius: 3px;',
        			tooltip:'退出登录',
        			scale:'medium',
	        		text:'<i class="iconfont white f18" style="left:2px;">&#xe6a7;</i>'
        		}]
        	},{
        		items:[{
        			id:'btn-theme',
        			width:45,
        			padding:'6px 0 9px 5px',
	        		xtype:'hoverbutton',
	        		tooltip:'设置背景',
	        		menu:themeMenus,
	        		cls: 'x-btn x-btn-default-toolbar-medium',
	        		text:' <i class="iconfont f999 f18">&#xe6a8;</i>'
        		},{
        			id:'btn-user-pwd',
        			text:'<i class="iconfont f999 f18">&#xe68f;</i>',
        			scale:'medium',
        			tooltip:'修改密码'
        		},{
        			id:'btn-user-setting',
        			text:'<i class="iconfont f999 f18">&#xe613;</i>',
        			scale:'medium',
        			tooltip:'绑定手机'
        		},{
        			id:'btn-pay-pwd',
		        	disabled:(cfg.getUser().departId==''),
		        	text:'<i class="iconfont f999 f16">&#xe611;</i>',
		        	scale:'medium',
		            tooltip: '支付密码'
        		},{
        			style:'border-radius: 3px;',
        			tooltip:'使用帮助',
        			href:userPruviews[upv],
	        		disabled:upv=='-1',
        			scale:'medium',
	        		text:'<i class="iconfont green-color f16" style="top:1px">&#xe6a4;</i>'
        		}]
        	}/*,{
        		height:8,
            	padding:'0 10 0 0',
	    		xtype:'appNotice'
        	}*/]
        }];
        /*this.style='background:url('+cfg.getImgPath()+'/wallpapers/sky.jpg) no-repeat;';
        var pos = 0,maxPos = 380,go=true,me = this;
		setInterval(function(){
			if(go){
				pos+=1;
			}else{
				pos-=1;
			}
			if(pos>=maxPos){
				go=false;
			}
			if(pos<=66){
				go = true;
			}
			me.setStyle({"background-position-y":(0-pos)+"px"});
		},50);*/
        this.callParent();
    }
});