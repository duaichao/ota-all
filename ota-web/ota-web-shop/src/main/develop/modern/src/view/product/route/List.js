Ext.define('Weidian.view.product.route.List', {
    extend: 'Ext.dataview.List',
    xtype:'routelist', 
	config:{
		binded:false,
		cls:'route-list-view',
		dyParams:null,
		itemTpl:[
	 		'<div class="item-top">',
	 			'<div class="product-image">', 
	 			'<img class="lazy_flash" data-src="{FACE:this.pathImg()}"  />',
	 			'</div>',
	 			'<div class="product-extra">',
	 				/*'<span>{THEMES}</span>',
	 				'<span class="separator">|</span>',*/
	 				'<span>{ATTR} | {NO}</span>',
	 			'</div>',
	 			/*'<tpl if="this.isNull(COLLECT_ID)">',
	 			'<span class="favorite x-fm fm-collect f24"></span>',
	 			'<tpl else>',
	 			'<span class="favorite x-fm fm-collected f24"></span>',
	 			'</tpl>',*/
	 			'<span class="share x-fm fm-share f24"></span>',
	 			'<div class="product-price price-schema-1">',
	 				'<div class="product-price-now ">￥<em>{SUM_PRICE}</em>起</div>',
	 				'<tpl if="this.isNull(values.fee)">',
	 				'<div class="product-promotion">已省￥{fee}</div>',
	 				'</tpl>',
	 			'</div>',
	 		'</div>',
	 		'<div class="item-bottom">',
	 			'<div class="product-title">{TITLE}</div>',
	 			'<div class="product-describe">{FEATURE}</div>',
	 		'</div><div class="D_spacer"></div>',{
	 			pathImg:function(v){
	 				if(v){
	 					return Ext.imgDomain+v;
	 				}else{
	 					return 'resources/images/noimage.jpg';
	 				}			
	 			},
	 			isNull:function(v){
	 				v = v ||'0';
	 				if(v!='0'){
	 					return true;
	 				}
	 				return false;
	 			}
	 		}
	 	],
	 	itemCls:'product-item',
	 	emptyText: '<p class="no-datas">暂无线路</p>',
	 	infinite : true,
	 	//striped:true,
	 	variableHeights: true,
		scrollToTopOnRefresh:false,
		plugins: [{
			xclass: 'Ext.plugin.ListPaging'
		}]
	},
	initialize: function(){
		if(!this.getStore()){
			var routeStore = util.createStore(cfg.url.route.list,false,6);
			routeStore.setModel(Ext.create('Weidian.model.product.Route'));
			routeStore.getProxy().setExtraParams(this.getDyParams());
			this.setStore(routeStore);
			routeStore.loadPage(1);
		}
		this.on('itemtap',this.onRouteItemTap,this);
		this.callParent();
	},
	updateDyParams:function(dyParams){
		var store = this.getStore();
		if(dyParams&&store){
			store.getProxy().setExtraParams(dyParams);
			store.loadPage(1);
		}
	},
	onRouteItemTap:function(dv,index,target,r,e){
		if(Ext.fly(e.getTarget('.share'))){
			var face = r.get('FACE');
			util.share({
				title : r.get('TITLE'),
	            desc: r.get('FEATURE'),
	            link:  Ext.domain+'#route/detail/'+r.get('ID'),
	            imgUrl: face?Ext.imgDomain+face:''
			});
		}else{
			this.getController().redirectTo('route/detail/'+r.get('ID'));
		}
	}
});
