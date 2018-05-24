Ext.define('app.controller.b2c.route', {
	extend: 'app.controller.common.BaseController',
	views:['Ext.ux.IFrame','app.view.resource.route.pub.detail','app.view.b2c.selectRoutes'],
	config: {
		control: {
			'tabpanel grid#basegrid':{
				afterrender: 'onBaseGridRender',
				cellclick:function( view, td, cellIndex, record, tr, rowIndex, e, eOpts ){
					var rec = record,getUrl='';
					if(cellIndex==2){
						if(e.target.tagName=='I'&&e.target.className.indexOf('info')!=-1)
		                	this.detailWindow(record.get('ID'));
					}
				}
			},
			'tabpanel grid#basegrid toolbar button#auditRoute':{
				click:'auditRoute'
			},
			'tabpanel grid#basegrid toolbar button#delRoute':{
				click:'delRoute'
			},
	        'selectRoutes':{
	        	cellclick:function( view, td, cellIndex, record, tr, rowIndex, e, eOpts ){
					var rec = record,getUrl='';
					if(cellIndex==2){
						if(e.target.tagName=='I'&&e.target.className.indexOf('info')!=-1)
		                	this.detailWindow(record.get('ID'));
					}
				}
	        }
			
		},
		refs: {
			baseGrid:'tabpanel grid#basegrid'
		}
	},
	onBaseGridRender:function(g){
		var me = this;
		setTimeout(function(){
		Ext.Ajax.request({
		    url: util.getPowerUrl(),
		    success: function(response, opts) {
		        var obj = Ext.decode(response.responseText),
		        	oitems = obj.children,items=[];
		        for(var i=0;i<oitems.length;i++){
		        	if(oitems[i].itemId.indexOf('Route')!=-1){
		        		items.push(oitems[i]);
		        	}
		        }
		        items.push('->');
				items.push({
					emptyText:'搜索关键字',
		        	xtype:'searchfield',
		        	store:g.getStore()
				});
		        util.createGridTbar(g,items);
		    }
		});
		},500);
	},
	auditRoute:function(btn){
		var me = this;
		var win = Ext.create('Ext.window.Window',{
   			title: util.windowTitle(util.glyphToFont(btn.glyph),'审核线路展示到前台',''),
   			width:850,
   			height:450,
   			draggable:false,
			resizable:false,
			modal:true,
   			bodyStyle:'background:#fff;padding:1px;font-size:16px;color:#bbb;',
   			layout:'fit',
   			items:[{
   				storeUrl:cfg.getCtx()+'/b2c/route/select',
   				saveUrl:cfg.getCtx()+'/b2c/route/save',
   				xtype:'selectRoutes'
   			}],
   			listeners:{
   				close:function(){
   					me.getBaseGrid().getStore().load();
   				}
   			}
		});
		win.show();
		
	},
	delRoute:function(btn){
		var grid = this.getBaseGrid(),sel;
		util.delGridModel(grid,'/b2c/route/del');
	},
	detailWindow :function(routeId){
		var win = Ext.create('Ext.window.Window',{
			title:util.windowTitle('&#xe635;','线路详情',''),
			bodyPadding:5,
			modal:true,
			draggable:false,
			resizable:false,
			maximizable:false,
   			layout:'fit',
   			items:[{
   				xtype:'routedetail'
   			}]
		});
		win.show().maximize();
		var rd = win.down('routedetail');
		rd.setRouteId(routeId);
	}
});