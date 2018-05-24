Ext.define('app.controller.b2b.user.home', {
	extend: 'app.controller.common.BaseController',
	requires:['Ext.ux.button.HoverButton','Ext.ux.grid.ActionGlyphColumn'],
	config: {
		control: {
			'toolbar#waitDoTools':{
				afterrender:function(){
					
				}
			},
			'openedCompanyGrid':{
				cellclick:function( view, td, cellIndex, record, tr, rowIndex, e, eOpts ){
					if(cellIndex==5){
						
					}
				}
			},
			'routeStatGrid':{
				cellclick:function( view, td, cellIndex, record, tr, rowIndex, e, eOpts ){
					if(cellIndex==3||cellIndex==4||cellIndex==5){
						var ps = view.getStore().getProxy().getExtraParams();
						var supplys = Ext.create('app.view.b2b.user.routeStatSupplys',{
							flex:1
						});
						var routes = Ext.create('app.view.b2b.user.routeStatSupplysRoute',{
							region:'west',
							width:475
						});
						var win = Ext.create('Ext.window.Window',{
							title:util.windowTitle('&#xe627;','供应商',''),
							width:900,
							height:400,
							modal:true,
							draggable:false,
							resizable:false,
				   			layout:{
				   				type:'hbox',
			    		        pack: 'start',
			    		        align: 'stretch'
				   			},
							items:[supplys,routes]
						}).show();
						ps.rtype = cellIndex-2;
						ps.ccityId = record.get('CITY_ID');
						supplys.setParams(ps);
						
						supplys.on('selectionchange',function(v, records){
							var r = records[0],mps = ps;
							mps.companyId = r.get('SUPPLY_ID');
							routes.setParams(mps);
						});
					}
				}
			}
		}
	}
});