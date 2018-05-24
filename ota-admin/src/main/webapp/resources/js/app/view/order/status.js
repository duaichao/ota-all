Ext.define('app.view.order.status', {
	extend:'Ext.Window',
	width:820,
	height:400,
	config:{
		orderRecord:null,
		modal:true,
		draggable:false,
		resizable:false,
		layout:'fit',
		bodyPadding:'10',
		autoScroll:true,
		tpl:new Ext.XTemplate(
				'<div class="p-detail">',
	   			'<table width="100%" border="0">',
	    			'<tr style="background:#f5f5f5;">',
	    				'<td>状态</td>',
	    				'<td>操作人</td>',
	    				'<td>操作人电话</td>',
	    				'<td>操作时间</td>',
	    				'<td>备注</td>',
	    			'</tr>',
	    			'<tpl for=".">',
	    			'<tr>',
	    				'<td>{TITLE}</td>',
	    				'<td>{CHINA_NAME}</td>',
	    				'<td>{MOBILE}</td>',
	    				'<td>{STEP_TIME}</td>',
	    				'<td>{REMARK}</td>',
	    			'</tr>',
	    			'</tpl>',
	    		'</table>',
	   		'</div>'
		)
	},
	updateOrderRecord: function(orderRecord) {
    	var me = this;
    	if(orderRecord){
    		this.setTitle(util.windowTitle('','<span style="height:18px;display:inlie-block;line-height:18px;font-size:18px;">'+orderRecord.get('NO')+'</span><span class="f14" style="line-height:18px;margin-left:10px;height:18px;display:inline-block;">状态跟踪</span>',''));
    		Ext.Ajax.request({
        		url:cfg.getCtx()+'/order/traffic/step?orderId='+orderRecord.get('ID'),
        		success:function(response, opts){
        			var obj = Ext.decode(response.responseText);
        			me.setData(obj.data);
        		}
        	});
    	}
	}
});