Ext.define('app.controller.b2c.ad', {
	extend: 'app.controller.common.BaseController',
	views:['Ext.ux.swfupload.SwfPanel'],
	config: {
		control: {
			'toolbar button#upload':{
				click:'upload'
			},
			'toolbar button#delete':{
				click:'del'
			}
		},
		refs: {
			baseGrid:'gridpanel#basegrid',
        }
	},
	onBaseGridRender:function(g){
		var me = this;
		setTimeout(function(){
			Ext.Ajax.request({
			    url: util.getPowerUrl(),
			    success: function(response, opts) {
			        var obj = Ext.decode(response.responseText),
			        	items = obj.children;
			        items.push({
			        	text:'上传广告图',
			        	glyph:'xe641@iconfont',
			        	itemId:'upload'
			        });
			        items.push({
			        	text:'删除广告图',
			        	glyph:'xe677@iconfont',
			        	itemId:'delete'
			        });
			        util.createGridTbar(g,items);
			    }
			});
		},500);
	},
	upload:function(){
		var me = this;
		var win = Ext.create('Ext.window.Window',{
   			title: util.windowTitle('&#xe641;','上传广告图',''),
   			width:'85%',
   			height:'85%',
   			draggable:false,
			resizable:false,
			modal:true,
   			bodyStyle:'background:#fff;padding:10px;font-size:16px;color:#bbb;',
   			layout:'fit',
   			items:[Ext.create('Ext.ux.swfupload.SwfPanel',{
   				xtype:'swfpanel',
   				upload_url:cfg.getCtx()+'/site/company/upload/ad',
   				typeCol:{
   					text: '类型', 
   					width: 70,
   					dataIndex: 'type',
   					editor: {
   						xtype:'combo',
   						allowBlank:false,
   						editable:false,
   						emptyText:'类型',
   						width:75,
   						typeAhead: true,
   			            triggerAction: 'all',
   			            store: [
   			                ['1','焦点图'],
   			                ['2','主题推荐']
   			            ]
   					}
   				}
   			})],
   			listeners:{
   				beforeclose:function(){
   					var sp = this.down('swfpanel'),ds = sp.store;
					for(var i=0;i<ds.getCount();i++){
						var record =ds.getAt(i);
						var file_id = record.get('id');
						sp.swfupload.cancelUpload(file_id,false);			
					}
					ds.removeAll();
					sp.swfupload.uploadStopped = false;
					
					me.getBaseGrid().getStore().load();
   				}
   			},
   			buttons:[{
   				xtype:'panel',
   				bodyStyle:'background:transparent;',
   				html:'焦点图最佳分辨率：740 × 350；小广告图最佳分辨率：252 × 197'
   			}]
   		}).show()
	},
	del:function(){
		var grid = this.getBaseGrid();
		util.delGridModel(grid,'/site/company/del/ad');
	}
});