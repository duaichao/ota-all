var newCurrViewInPath = currViewInPath;
if(currViewInPath.indexOf('online')!=-1){
	newCurrViewInPath = 'b2b.user';
}
Ext.define('app.view.common.BaseGrid', {
	extend:'Ext.grid.Panel',
	xtype:'basegrid',
	loadMask: true,
	emptyText: '没有找到数据',
	initComponent: function() {
		this.store = util.createGridStore(cfg.getCtx()+cfg.getModuleListUrl()[currViewInPath],Ext.create('app.model.'+newCurrViewInPath+'.model'));
        this.bbar = util.createGridBbar(this.store);
        this.columns = Ext.create('app.model.'+newCurrViewInPath+'.column');
        this.dockedItems = [{
        	xtype:'toolbar',
        	style:'padding-left:5px;',
            overflowHandler: 'menu',
        	items:[{
        		disabled:true,
        		text:'正在初始化...'
        	}]
        }];
        this.viewConfig = {  
        	style:'background:transparent;',
		    //stripeRows : true, // 奇偶行不同底色  
		    enableTextSelection : true
		    // 加入允许拖动功能  
		   /* plugins : [{  
		        ptype : 'gridviewdragdrop',  
		        ddGroup : 'DD_grid_Global', // 拖动分组必须设置，这个分组名称为:DD_grid_Global  
		        enableDrop : false  // 设为false，不允许在本grid中拖动  
		        }]  */
		  
		};  
        this.callParent();
	},
	columnLines: true,
	selModel :{mode:'SINGLE'},
	selType:'checkboxmodel'
});