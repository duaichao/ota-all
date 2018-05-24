Ext.define('app.controller.b2c.shop', {
	extend: 'app.controller.common.BaseController',
	config: {
		control: {
			'toolbar button#recommend':{
				click:'recommend'
			},
			'toolbar button#sort':{
				click:'sort'
			}
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
	recommend:function(btn){
		var me=this,sel,grid=this.getBasegrid();
		if(sel = util.getSingleModel(grid)){
			util.switchGridModel(this.getBasegrid(),'/b2c/shop/recommend');
		}
	},
	sort:function(btn){
		var me=this,sel,grid=this.getBasegrid();
		if(sel = util.getSingleModel(grid)){
			var win = Ext.create('Ext.window.Window',{
	   			title: util.windowTitle(util.glyphToFont(btn.glyph),sel.get('COMPANY'),''),
	   			width:360,
	   			height:150,
	   			draggable:false,
				resizable:false,
				modal:true,
	   			bodyStyle:'background:#fff;font-size:16px;color:#bbb;',
	   			layout: {
			        type: 'vbox',
			        pack: 'start',
			        align: 'stretch'
			    },
	   			items:[{
					flex:1,
					fieldDefaults: {
				        labelAlign: 'right',
				        labelWidth: 100
				    },
					xtype:'form',
					bodyPadding:'16 0 0 0',
	   				items: [{
	   					xtype:'hiddenfield',
	   					name:'pm[ID]',
	   					value:sel.get('ID')
	   				},{
				        xtype: 'numberfield',
				        name: 'pm[ORDER_BY]',
				        anchor:'50%',
				        allowBlank:false,
				        fieldLabel: '排序序号',
				        minValue:0,
				        value:sel.get('ORDER_BY')||0
				    }],
				    buttons:[{
				    	text:'保存',
				    	handler:function(b){
				    		var f = b.up('form'),bf = f.getForm();
				    		if(bf.isValid()){
				    			bf.submit({
				    				submitEmptyText:false ,
	   								url:cfg.getCtx()+'/b2c/shop/sort',
					                success: function(form, action) {
					                	grid.getStore().load();
					                    util.success('排序设置成功');
					                    win.close();
					                },
					                failure: function(form, action) {
					                    util.error('排序设置失败');
					                    win.close();
					                }
	   							});
				    		}
				    	}
				    }]
	   			}]
	   		}).show();
		}
	}
});