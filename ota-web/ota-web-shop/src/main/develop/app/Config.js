Ext.define('Weidian.Config', {
    alternateClassName: 'cfg',
    statics: {
    	isRedirect:false,
    	isAdmin:false,//店主true 游客false
    	tmpParams : {},
    	view:null,//主窗口mainView
    	point:{
    		phone:null,
    		top:null
    	},
    	rsa:null,
    	user:null,
    	defaultParams :{
    		noLoader:false,
    		appName:'minishop',
    		ostype:0,
    		version:1
    	},
    	pageSize:10,
		anim:{
    		duration:300
    	},
    	//toastBar:null,
    	toastTimeout:1000,
    	weixinAlready:false,
		url:{
			getWeixinSign:'/get/weixin/sign',
			index:'/index',
			auth:'/authentication',
			isExistPhone:'/menber/phone/exist',
			register:'/member/register',
			resetPwd:'/member/reset/password',
			editPwd:'/member/edit/password',
			getPhoneCode:'/member/phone/code',
			getRsa:'/member/rsa',
			login:'/member/login',
			logout:'/member/logout',
			favorite:{
				route:{
					add:'/resource/route/favorite',
					list:'/resource/route/favorite/list'
				}
			},
			route:{
				list:'/resource/route/list',
				detail:'/resource/route/detail',
				calendar:'/resource/route/calendar',
				price:{
					plan:'/resource/route/plan',
					attr:'/resource/route/attr',
					detail:'/resource/route/price'
				},
				city:{
					list:'/resource/route/city/list'
				}
			},
			order:{
				list:'/order/list',
				detail:'/order/detail',
				save:'/order/save',
				pay:'/order/pay',
				cancel:'/order/cancel',
				visitor:{
					save:'/member/visitor/save',
					del : '/member/visitor/del',
					list : '/member/visitors'//常用游客列表
				}
			}
		},
		priceAttrs:{
			'成人':'0FA5123749CF8C87E050007F0100BCAD',
			'儿童':'0FA5123749D08C87E050007F0100BCAD'
		},
		authLoginModules:{
			'ucenter':true,
			'editpassword':true,
			'ordervisitor':true,
			'ordercalendar':true,
			'visitorcontacts':true,
			'visitorsave':true,
			'paycenter':true,
			'mefavorite':true,
			'meorder':true
		}
    }
});