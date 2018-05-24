Ext.define('Weidian.view.product.route.DetailModel', {
    extend: 'Ext.app.ViewModel',
    alias: 'viewmodel.routedetailmodel',

    formulas: {
		tese:{
			get:function(get){
				 return '<h3>特色</h3><div>'+util.fmWordText(get('FEATURE'))+'</div><h3>推荐</h3><div>'+util.fmWordText(get('NOTICE'))+'</div>';
    		}
		},
		types:{
			get:function(get){
				var arr = ['','周边','国内','出境'];	
    			return arr[get('TYPE')];
			}
		},
		favoriteCls:{
			get:function(get){
    			return Ext.isEmpty(get('COLLECT_ID'))?'x-fm fm-collect':'x-fm fm-collected fcred1';
			}
		},
		favoriteTxt:{
			get:function(get){
    			return Ext.isEmpty(get('COLLECT_ID'))?'收藏产品':'取消收藏';
			}
		}
	},
    data: {
    	TITLE: '加载中...',
    	NO:'',
    	TYPE:'跟团游',
    	SUM_PRICE:'0.00',
    	startCity:[],
    	calendar:[],
    	"FEATURE":"",
    	"NOTICE":""
    	
    }
});
