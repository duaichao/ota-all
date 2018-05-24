Ext.define('app.model.b2b.module.column', {
	constructor:function(){
		return [
		//Ext.create('Ext.grid.RowNumberer',{width:35}),
		{
			xtype: 'treecolumn',
	        text: '名称',
	        flex:1,
	        sortable:false,
	        dataIndex: 'text'
	    },{
	        text: '链接',
	        width:200,
	        sortable:false,
	        dataIndex: 'url'
	    },{
	        text: '图标',
	        width:150,
	        sortable:false,
	        dataIndex: 'iconCls'
	    },{
	        text: 'glyph',
	        width:100,
	        sortable:false,
	        dataIndex: 'glyph'
	    },{
            header: '状态',
            sortable:false,
            dataIndex: 'isUse',
            width: 55,
	        renderer: function(value){
	        	return value==1?'<span style="color:red">禁用</span>':'启用';
	        }
        }];
	}
});