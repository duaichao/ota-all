Ext.define('app.model.site.company.departColumn', {
	constructor:function(){
		return [
		{
	        text: '部门名称',
	        flex:1,
	        dataIndex: 'TEXT'
	    },{
	        text: '状态',
	        width:60,
	        dataIndex: 'IS_USE',
	        renderer: function(value){
	        	return value==1?'<span style="color:red">禁用</span>':'启用';
	        }
	    },{
	    	xtype: 'actionglyphcolumn',
            width: 28,
            align:'center',
            sortable: false,
            menuDisabled: true,
            items: [{
            	iconCls:'iconfont orange-color f18',
            	text:'&#xe62f;',
                tooltip: '部门钱包',
                scope: this,
                handler: function(grid, rowIndex, colIndex){
                	var sel = grid.getStore().getAt(rowIndex);
                	util.accountWindow(sel);
                }
            }]
	    }];
	}
});