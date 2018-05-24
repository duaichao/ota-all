Ext.define('app.model.b2b.notice.column', {
	requires:['Ext.ux.grid.ActionGlyphColumn'],
	constructor:function(){
		return [
		Ext.create('Ext.grid.RowNumberer',{width:35}),
		{
	        text: '标题',
	        flex:1,
	        dataIndex: 'TITLE',
	        renderer:function(v,c,r){
	        	if(r.get('IS_TOP')=='1'){
	        		return '<i class="iconfont red-color" data-qtip="置顶">&#xe644;</i> '+v;
	        	}else{
	        		return v;
	        	}
	        }
	    },{
	        text: '城市',
	        width:120,
	        dataIndex: 'SUB_AREA'
	    },{
	        text: '类型',
	        width:50,
	        dataIndex: 'TYPE',
	        renderer:function(v,c,r){
	        	if(v==0){
	        		return '<span class="blue-color">B2B</span>';
	        	}
	        	if(v==1){
	        		return '<span class="green-color">B2C</span>';
	        	}
	        }
	    },{
	        text: '创建人',
	        width:100,
	        dataIndex: 'CREATE_USER'
	    },
	    {
	        text: '创建时间',
	        width:120,
	        dataIndex: 'CREATE_TIME'
	    },{
	        text: '更新人',
	        width:100,
	        dataIndex: 'UPDATE_USER'},{
	        text: '更新时间',
	        width:120,
	        dataIndex: 'UPDATE_TIME'
	    },{
	    	xtype: 'actionglyphcolumn',
            width: 30,
            sortable: false,
            menuDisabled: true,
            items: [{
            	iconCls:'iconfont',
            	text:'&#xe635;',
                tooltip: '详情',
                scope: this,
                handler: function(grid, rowIndex, colIndex){
	                var sel = grid.getStore().getAt(rowIndex),
	                	tpl = new Ext.XTemplate(
						    '<div class="p-detail">',
						    '<h3>{TITLE}</h3>',
						    '<div class="p-time">{CREATE_TIME}</div>',
						    '<div class="p-content">',
						    '{CONTENT}',
						    '</div>',
						    '</div>'
						);
	                Ext.Ajax.request({
						url:cfg.getCtx()+'/b2b/notice/detail',
						params:{ID:sel.get('ID')},
						success:function(response){
							var data = Ext.decode(response.responseText).data,
								d = data[0];
							Ext.create('Ext.window.Window',{
								title:util.windowTitle('&#xe733;',''+sel.get('TITLE'),''),
								width:800,
								height:400,
								modal:true,
								maximizable:true,
								draggable:false,
								resizable:false,
					   			layout:'fit',
					   			bodyPadding:'5',
					   			items:[{
					   				autoScroll:true,
					   				tpl:tpl,
					   				data:d
					   			}]
							}).show();
						}
					});
                }
            }]
	    }];
	}
});