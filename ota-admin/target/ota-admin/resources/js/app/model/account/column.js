Ext.define('app.model.account.column', {
	constructor:function(){
		return [
		Ext.create('Ext.grid.RowNumberer',{width:35}),
		{
	        text: '描述',
	        width:300,
	        dataIndex: 'DES',
	        renderer:function(v,c,r){
	        	c.tdAttr = 'data-qtip="<div class=\'f14 ht20\'>操作人：『'+(r.get('TEXT')||'')+'』 '+r.get('USER_NAME')+'</div>'+(r.get('NOTE')||'')+'"';
	        	var h = [],attr = r.get('CERTIFY_ATTR')||'';
	        	if(attr!=''){
	        		h.push('<a target="_blank" data-qtip="下载凭证" class="iconfont gray-color" href = "'+cfg.getPicCtx()+'/'+r.get('CERTIFY_ATTR')+'">&#xe6f4;</a> ');
	        	}
	        	h.push(v);
	        	return h.join('');
	        }
	    },
	    {
	        text: '金额',
	        width:100,
	        dataIndex: 'MONEY'
	    },
		{
	        text: '余额',
	        width:100,
	        dataIndex: 'YEMONEY'
	    },
	    {
	        text: '时间',
	        width:200,
	        dataIndex: 'OP_TIME_SS'
	    },{
	    	text: '备注',
	    	width:400,
	    	dataIndex:'NOTE'
	    }];
	}
});