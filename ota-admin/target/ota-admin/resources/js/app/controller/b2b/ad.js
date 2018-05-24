Ext.define('app.controller.b2b.ad', {
	extend: 'app.controller.common.BaseController',
	config: {
		control: {
	        'toolbar button[itemId=b2badUpload]':{
	        	afterrender:'onBtnRender',
	        	click:'onBtnUpload'
	        },
	        'toolbar button[itemId=b2badUpload] menu':{
	        	click:'onBtnMenuClick'
	        },
	        'toolbar button[itemId=del]':{
				click:'onBtnDelete'
	        }
		},
		refs: {
            adgrid: 'gridpanel[itemId=basegrid]'
        }
	},
	onBtnRender:function(btn){
		Ext.Ajax.request({
		    url: cfg.getCtx()+'/site/company/ownerSites',
		    success: function(response, opts) {
		        var obj = Ext.decode(response.responseText),
		        	items = obj.data,
		        	menus = [];
		        for(var i=0;i<items.length;i++){
		        	menus.push({
		        		itemId:items[i].CITY_ID,
		        		pinyin:items[i].PINYIN,
		        		text:items[i].SUB_AREA
		        	});
		        }
		        btn.setMenu(menus);
		    }
		});
	},
	onBtnUpload:function(btn){
		util.alert('点击按钮箭头选择一个城市');
	},
	onBtnMenuClick:function(m,item){
		var me = this;
		util.uploadAttachment('上传广告图',cfg.getCtx()+'/upload/ad/b2b?cityId='+item.itemId+'&cityName='+item.text+'&pinyin='+item.pinyin,'',function(){
			me.getAdgrid().getStore().load();
		});
	},
	onBtnDelete:function(btn){
		util.delGridModel(this.getAdgrid(),'/b2b/ad/del');
	}
});