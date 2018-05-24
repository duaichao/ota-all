Ext.define('app.model.b2b.ad.column', {
	constructor:function(){
		return [
		Ext.create('Ext.grid.RowNumberer',{width:35}),
		{
	        text: '标题',
	        width:300,
	        dataIndex: 'TITLE',
	        renderer:function(value,metaData,record,colIndex,store,view){
	        	Ext.apply(Ext.QuickTips.getQuickTip(),{maxWidth:996});
	        	metaData.tdAttr = 'data-qwidth="996" data-qtip="<div style=\'width:996px;height:400px;background:url('+cfg.getPicCtx()+'/'+record.get('PATH')+') no-repeat center 0;\'></div>"';  
            	return value;  
	        }
	    },{
	        text: '广告链接',
	        flex:1,
	        dataIndex: 'HREF'
	    },{
	        text: '城市',
	        width:180,
	        dataIndex: 'SUB_AREA'
	    }];
	}
});