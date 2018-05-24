Ext.define('app.controller.b2b.ad', {
	extend: 'Ext.app.Controller',
	requires: [
        'Ext.ux.PagingToolbarResizer',
        'Ext.ux.upload.plugin.Window',
        'Ext.ux.upload.BatchUpload'
    ],
    views:[
    	'b2b.AdGrid'
    ],
	config: {
		control: {
			'AdGrid': {
	             afterrender: 'onAdgridRender',
	             itemclick:'onGridItemClick'
	         },
	         'AdGrid checkboxmodel':{
	         	selectionchange:'onSelectionChange'
	         },
	         'AdGrid button[itemId=scenic-upload]':{
	         	click:'onBtnUpload'
	         },
	         'AdGrid button[itemId=scenic-delete]':{
	         	click:'onBtnDelete'
	         }
		},
		refs: {
            adgrid: 'AdGrid'
        }
	},
	onAdgridRender:function(g){
		util.createGridTbar(g,[{
			itemId:'scenic-upload',
			xtype:'batchupload',
			autoInit:false,
			url: cfg.getCtx()+'/upload/image',
			multipart_params:{
				entityName:'scenic'
			},
			iconCls:'icon-upload-cloud',
			text:'上传图片'
		},{
			iconCls:'icon-camera',
			itemId:'scenic-delete',
			text:'删除景点'
		}]);
		
	},
	onBtnUpload:function(btn){
		var model,grid = this.getAdgrid();
		if(model=util.getSingleModel(grid)){
			
		}
	},
	onBtnDelete:function(btn){
		var model,grid = this.getAdgrid();
		if(model=util.getMultiModel(grid)){
			
		}
	},onGridItemClick:function(view, record, item, index, e, eOpts){
		var grid = this.getAdgrid(),
			tbar = grid.getDockedItems('toolbar[dock="top"]')[0],
			upbtn = tbar.getComponent('scenic-upload');
		
		//console.log(upbtn);
		if(util.getSingleModel(grid)){
			//upbtn.uploader.initialize();
		}
	},onSelectionChange:function(sm, selections){
		var grid = this.getAdgrid();
		grid.down('#scenic-upload').setDisabled(selections.length === 0);
		grid.down('#scenic-delete').setDisabled(selections.length === 0);
	}
});