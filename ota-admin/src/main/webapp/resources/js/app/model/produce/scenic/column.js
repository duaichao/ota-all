Ext.define('app.model.b2b.ad.column', {
	constructor:function(){
		return [
		Ext.create('Ext.grid.RowNumberer',{width:35}),
		{
	        text: '景点名称',
	        id: 'TITLE',
	        flex: 1,
	        dataIndex: 'TITLE'
	    }, {
	        text: '省/市',
	        id: 'SUB_AREA',
	        width:80,
	        dataIndex: 'SUB_AREA'
	    }];
	}
});