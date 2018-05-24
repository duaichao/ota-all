Ext.define('app.view.produce.route.contactGrid', {
	extend:'Ext.grid.Panel',
	xtype:'visitorContactGrid',
	config:{
		loadMask: true,
		border:true,
		autoScroll:true,
		emptyText: '还没有联系人',
		columnLines: true,
		viewConfig:{
			enableTextSelection:true
		},
		viewShow:'add',
		orderId:''
	},
	initComponent: function() {
		var me = this;
		this.store = Ext.create('Ext.data.Store', {
	        model: Ext.create('app.model.resource.Contact')
	    });
        this.columns = [
        	Ext.create('Ext.grid.RowNumberer',{width:35}),{
        		header: '联系人',
	            dataIndex: 'CHINA_NAME',
	            width:120,
	            editor: {
	                allowBlank: false
	            }
        	}/*,{
        		header: '电话',
	            dataIndex: 'PHONE',
	            width:160,
	            editor: {
	                allowBlank: false
	            }
        	}*/,{
        		header: '手机',
        		flex:1,
	            dataIndex: 'MOBILE',
	            editor: {
	                allowBlank: false
	            }
        	}];
        if(this.getViewShow()=='add'){
        	Ext.grid.RowEditor.prototype.saveBtnText = "确定";
            Ext.grid.RowEditor.prototype.cancelBtnText = '取消';
            var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
    	        clicksToMoveEditor: 1,
    	        clicksToEdit:2,
    	        pluginId:'rowedit',
    	        errorSummary:false,
    	        autoCancel: false,
    	        listeners:{
    	        	canceledit:function( editor, context, eOpts){
    	        		var r = context.record;
    	        		if(r.get('CHINA_NAME')==''||r.get('MOBILE')==''){
    	        			var sm = me.getSelectionModel();
    		                rowEditing.cancelEdit();
    		                me.store.remove(sm.getSelection());
    		                if (me.store.getCount() > 0) {
    		                    sm.select(0);
    		                }
    	        		}else{
    	        		}
    	        	},
    	        	edit:function( editor, context, eOpts ){
    	        		context.record.commit();
    	        	}
    	        }
    	    });
	        this.dockedItems = [{
	        	docked:'top',
	    		xtype:'toolbar',
	    		itemId:'gridtool',
	        	items:[{
	        		tooltip:'添加联系人',
	        		glyph:'xe62b@iconfont',
	        		handler:function(btn){
	        			rowEditing.cancelEdit();
	        			var r = Ext.create('app.model.resource.Contact', {
		                    ID: '',
		                    ORDER_ID:'',
		                    CHINA_NAME: '',
		                    MOBILE: ''
		                });
	        			me.store.insert(0, r);
	        			rowEditing.startEdit(0, 0);
	        		}
	        	},'-',{
	        		tooltip:'删除联系人',
	        		glyph:'xe62c@iconfont',
	        		handler:function(){
	        			var sm = me.getSelectionModel();
		                rowEditing.cancelEdit();
		                me.store.remove(sm.getSelection());
		                //if (me.store.getCount() > 0) {
		                //    sm.select(0);
		                //}
	        		}
	        	}]
	    	}];
        }
    	this.plugins = rowEditing;
        this.callParent();
        
        
        if(this.getOrderId()!=''){
        	Ext.Ajax.request({
				url:cfg.getCtx()+'/order/route/list/contact?orderId='+this.getOrderId(),
				success:function(response, opts){
					var obj = Ext.decode(response.responseText);
					me.store.setData(obj.data);
				}
			});
        }else{
        	setTimeout(function(){
            	var r = Ext.create('app.model.resource.Contact', {
                    ID: '',
                    ORDER_ID:'',
                    CHINA_NAME: '',
                    MOBILE: ''
                });
                me.store.insert(0, r);
                rowEditing.startEdit(0, 0);
            },500);
        }
        
        
	}
});