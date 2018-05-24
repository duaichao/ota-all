Ext.define('app.store.Navigation', {
    extend: 'Ext.data.TreeStore',
    alias: 'store.navigation',
    /*proxy: {
        type: 'ajax',
        url: cfg.getCtx()+'/b2b/module/list'
    },
    root: {
        text: '<i class="icon-location-5 f14"></i>',
        id: 'nav_home',
        expanded: true
    },*/
    constructor: function(config) {
        var me = this;
        me.callParent([Ext.apply({
            root: {
            	glyph:'xe60e@iconfont',
		        text: '系统导航',
		        id: 'home',
		        expanded: true,
		        children: Ext.decode(cfg.getUser().modules)////me.getNavItems()
		    }
        }, config)]);
    },
    addIconClasses: function (items) {return items;
        for (var item, i = items.length; i-- > 0; ) {
            item = items[i];

            if (!('iconCls' in item)) {
                item.iconCls = 'icon-' + item.id;
            }

            if (!('glyph' in item)) {
                // sets the font-family
                item.glyph = '32@Sencha-Examples';
            }

            if (item.children) {
                this.addIconClasses(item.children);
            }
        }

        return items;
    },
    getNavItems: function() {
        return this.addIconClasses([
            {
                text: '产品',
                id: 'nav_produce',
                expanded: true,
                children: [
                	{ id: 'nav_produce_route', text: '旅游线路', leaf: true },
                    { id: 'nav_produce_traffic', text: '往返交通', leaf: true },
                    { id: 'nav_produce_hotel', text: '入住酒店', leaf: true },
                    { id: 'nav_produce_scenic', href:'produce/scenic',text: '景点门票', leaf: true },
                    { id: 'nav_produce_insurance', text: '旅行保险', leaf: true }
                ]
            },
            {
                text: '订单',
                id: 'nav_order',
                expanded: true,
                children: [
                    { id: 'nav_order_route', text: '线路订单', leaf: true },
                    { id: 'nav_order_hotel', text: '酒店订单', leaf: true },
                    { id: 'nav_order_traffic', text: '交通订单', leaf: true },
                    { id: 'nav_order_scenic', text: '景点门票', leaf: true },
                    { id: 'nav_order_insurance', text: '保险订单', leaf: true }
                ]
            },
            {
                text: '咨询',
                id: 'nav_consult',
                expanded: true,
                children: [
                    { id: 'nav_consult_route', text: '线路咨询', leaf: true },
                    { id: 'nav_consult_hotel', text: '酒店咨询', leaf: true },
                    { id: 'nav_consult_traffic', text: '交通咨询', leaf: true },
                    { id: 'nav_consult_scenic', text: '景点咨询', leaf: true },
                    { id: 'nav_consult_insurance', text: '保险咨询', leaf: true }
                ]
            },
            {
                text: '统计',
                id: 'nav_stat',
                expanded: true,
                children: [
                    { id: 'nav_stat_retail', text: '分销商统计', leaf: true },
                    { id: 'nav_stat_supply', text: '供应商统计', leaf: true },
                    { id: 'nav_stat_site', text: '站群统计', leaf: true },
                    { id: 'nav_stat_all', text: '系统统计', leaf: true }
                ]
            },
            {
                text: '财务',
                id: 'nav_finance',
                expanded: true,
                children: [
                    { id: 'nav_finance_deposit', text: '账号提现', leaf: true },
                    { id: 'nav_finance_report', text: '财务报表', leaf: true }
                ]
            },
            {
                text: '站群管理',
                id: 'nav_site',
                expanded: true,
                children: [
                    { id: 'nav_site_setting', text: '站群配置', leaf: true },
                    { id: 'nav_site_supplier', text: '供应商', leaf: true },
                    { id: 'nav_site_retail', text: '分销商', leaf: true }
                ]
            },
            {
			    id: 'nav_system_admin',
			    text: 'B2B系统',
			    expanded: true,
			    children: [{
			    	id:'nav_system_user',
			    	href:'b2b/user',
			    	text: '用户',
			        leaf: true
			    },{
			    	id:'nav_system_role',
			    	href:'b2b/role',
			    	text: '角色',
			        leaf: true
			    },{
			    	id:'nav_system_module',
			    	href:'b2b/module',
			    	text: '模块',
			        leaf: true
			    },{
			    	id:'nav_system_power',
			    	href:'b2b/power',
			    	text: '权限',
			        leaf: true
			    },{
			        id: 'nav_system_log',
			        text: '系统日志',
			        leaf: true
			    },
			    {
			        id: 'nav_system_notice',
			        text: '公告',
			        leaf: true
			    },
			    {
			        id: 'nav_system_admin_ad',
			        href:'b2b/ad',
			        text: '广告图',
			        leaf: true
			    },
			    {
			        id: 'nav_system_sms',
			        text: '短信',
			        leaf: true
			    },
			    {
			        id: 'nav_system_tag',
			        text: '标签',
			        leaf: true
			    },
			    {
			        id: 'nav_system_advise',
			        text: '投诉建议',
			        leaf: true
			    },
			    {
			        id: 'nav_system_visitor',
			        text: '游客',
			        leaf: true
			    }]
			},
			{
			    id: 'nav_system_web',
			    text: 'B2C系统',
			    expanded: true,
			    children: [{
			        id: 'nav_system_news',
			        text: '新闻动态',
			        leaf: true
			    },
			    {
			        id: 'nav_system_web_ad',
			        text: '广告图',
			        leaf: true
			    },
			    {
			        id: 'nav_system_member',
			        text: '注册会员',
			        leaf: true
			    }]
			},{
				id:'nav_',
				text:'资源',
				expanded: true,
			    children: [{
			        id: 'nav_system_news',
			        text: '交通',
			        leaf: true
			    },
			    {
			        id: 'nav_system_web_ad',
			        text: '酒店',
			        leaf: true
			    },
			    {
			        id: 'nav_system_member',
			        text: '景点',
			        leaf: true
			    }]
			}
        ]);
    }
});
