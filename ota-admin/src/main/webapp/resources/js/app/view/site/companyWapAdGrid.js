Ext.define('app.view.site.companyWapAdGrid', {
	extend:'Ext.grid.Panel',
	loadMask: true,
	border:true,
	emptyText: '没有找到数据',
	config:{
		parentId:null,
		companyId:null
	},  
    updateParentId:function(parentId){
    	if(parentId){
	    	
    	}
    },
	initComponent: function() {
		var me = this;
		this.store = util.createGridStore(cfg.getCtx()+'/site/company/list/ad?companyId='+me.getCompanyId(),Ext.create('app.model.b2b.ad.model'));
        var columns = Ext.create('app.model.b2b.ad.column');
        
        columns[3] = Ext.apply(columns[3],{
        	text: '类型',
	        width:120,
	        dataIndex: 'TYPE',
	        renderer:function(value,metaData,record,colIndex,store,view){
//	        	var a = ['','焦点图','周边','国内','出境'];
	        	var a = ['','焦点图','主题推荐'];
            	return a[value];  
	        }
        });
        this.columns = columns;
        this.dockedItems = [{
        	xtype:'toolbar',
        	style:'padding-left:5px;',
        	layout: {
                overflowHandler: 'Menu'
            },
        	items:[{
        		text:'上传广告图',
        		handler:function(){
        			var win = Ext.create('Ext.window.Window',{
    		   			title: util.windowTitle('&#xe6a8;','上传广告图',''),
    		   			width:850,
    		   			height:400,
    		   			draggable:false,
    					resizable:false,
    					modal:true,
    		   			bodyStyle:'background:#fff;padding:1px;font-size:16px;color:#bbb;',
    		   			layout:'fit',
    		   			items:[Ext.create('Ext.ux.swfupload.SwfPanel',{
    		   				xtype:'swfpanel',
    		   				upload_url:cfg.getCtx()+'/site/company/upload/ad?wapId='+me.getParentId()+'&companyId='+me.getCompanyId(),
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
//    		   			                ['2','周边'],
//    		   			                ['3','国内'],
//    		   			                ['4','出境']
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
    							
    							me.getStore().load();
    		   				}
    		   			},
    		   			buttons:[{
    		   				xtype:'panel',
    		   				bodyStyle:'background:transparent;',
//    		   				html:'焦点图最佳分辨率：1920 × 450；小广告图最佳分辨率：640 × 360'
    		   				html:'焦点图最佳分辨率：740 × 350；小广告图最佳分辨率：252 × 197'
    		   			}]
    		   		}).show()
        			
        			
        			
        		}
        	},{
        		text:'删除广告图',
        		handler:function(btn){
        			util.delGridModel(btn.up('gridpanel'),'/site/company/del/ad');
        		}
        	}]
        }];
		this.callParent();
	},
	columnLines: true,
	selType:'checkboxmodel'
});