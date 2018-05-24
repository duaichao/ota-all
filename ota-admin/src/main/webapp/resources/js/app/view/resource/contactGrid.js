Ext.define('app.view.resource.contactGrid', {
	extend:'Ext.grid.Panel',
	xtype:'contactGrid',
	requires:['Ext.ux.grid.column.RadioColumn'],
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
		proId:''
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
	            flex:1,
	            editor: {
	                allowBlank: false
	            }
        	},{
        		header: '电话',
	            dataIndex: 'PHONE',
	            width:160,
	            editor: {
	                allowBlank: false
	            }
        	},{
        		header: '手机',
	            dataIndex: 'MOBILE',
	            width:160,
	            editor: {
	                allowBlank: false
	            }
        	},{
        		header:'短信',
        		xtype: 'radiocolumn',
                dataIndex: 'IS_MESSAGE',
                width: 60,
                disabled:this.getViewShow()!='add',
                align:'center',
                sortable: false,
	            menuDisabled: true
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
    	        		if(r.get('CHINA_NAME')==''||r.get('MOBILE')==''||r.get('PHONE')==''){
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
	    		defaults:{scale: 'medium'},
	        	items:[{
	        		tooltip:'添加联系人',
	        		glyph:'xe62b@iconfont',
	        		handler:function(btn){
	        			rowEditing.cancelEdit();
	        			var r = Ext.create('app.model.resource.Contact', {
		                    ID: '',
		                    PRODUCE_ID:'',
		                    CHINA_NAME: '',
		                    MOBILE: '',
		                    PHONE: '',
		                    TYPE: '0',
		                    IS_MESSAGE:0
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
        
        
        if(this.getProId()!=''){
        	Ext.Ajax.request({
				url:cfg.getCtx()+'/resource/route/list/pro/contact?produceId='+this.getProId(),
				success:function(response, opts){
					var obj = Ext.decode(response.responseText);
					me.store.setData(obj.data);
				}
			});
        }else{
        	setTimeout(function(){
            	var r = Ext.create('app.model.resource.Contact', {
                    ID: '',
                    PRODUCE_ID:'',
                    CHINA_NAME: '',
                    MOBILE: '',
                    PHONE: '',
                    TYPE: '0',
                    IS_MESSAGE:0
                });
                me.store.insert(0, r);
                rowEditing.startEdit(0, 0);
            },500);
        }
        
        
	}
});