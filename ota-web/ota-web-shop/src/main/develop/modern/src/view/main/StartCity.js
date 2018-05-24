Ext.define("Weidian.view.StartCity", {
	extend: 'Ext.dataview.DataView',
	alternateClassName: 'startcity',
    xtype:'startcity',
	controller: 'main',
	config:{
		//centered: true,
		releTarget:null,
		floated: true,
        width: '100%',
		height:110,
        modal: true,
		hideOnMaskTap: true,
        cls:'start-city-view D_dialog',
		itemCls:'start-city-item',
		itemTpl:new Ext.XTemplate(
        		'{CITY_NAME}'
        ),
		store: 'startcity',
		
		listeners:{
			select:function(dv,r){
				dv.getReleTarget().setText(r.get('CITY_NAME'));
				dv.setReleTarget(null);
				dv.hide();
			},
			hide:function(g){
         		Ext.Viewport.remove(g);
         	}
		}
	}
});