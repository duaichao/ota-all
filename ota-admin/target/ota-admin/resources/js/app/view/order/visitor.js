Ext.define('app.view.order.visitor', {
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
		tpl:''
	},
	updateOrderRecord: function(orderRecord) {
    	var me = this,tpl=new Ext.XTemplate(
				'<div class="p-detail">',
	   			'<table width="100%" border="0" >',
	    			'<tr style="background:#f5f5f5;">',
	    				'<td>姓名</td>',
	    				'<td>性别</td>',
	    				'<td>游客类型</td>',
	    				'<td>手机</td>',
	    				'<td>证件类型</td>',
	    				'<td>证件号码</td>',
	    			'</tr>',
	    			'<tpl for=".">',
	    			'<tr>',
	    				'<td>{NAME}</td>',
	    				'<td>{[values.SEX == 1 ? "男" : "女"]}</td>',
	    				'<td>{ATTR_NAME}</td>',
	    				'<td>{MOBILE}</td>',
	    				'<td>{CARD_TYPE}</td>',
	    				'<td>{CARD_NO}</td>',
	    			'</tr>',
	    			'</tpl>',
	    		'</table>',
	   		'</div>'
	    );
    	if(orderRecord){
    		this.setTitle(util.windowTitle('','<span style="height:18px;display:inlie-block;line-height:18px;font-size:18px;">'+orderRecord.get('NO')+'</span><span class="f14" style="line-height:18px;margin-left:10px;height:18px;display:inline-block;">游客详情</span>',''));
    		this.setTpl(tpl);
    		Ext.Ajax.request({
        		url:cfg.getCtx()+'/order/visitor/listOrderVisitor?orderId='+orderRecord.get('ID'),
        		success:function(response, opts){
        			var obj = Ext.decode(response.responseText);
        			me.setData(obj.data);
        			
        			me.addDocked({
        				xtype:'toolbar',
        				style:'padding-right:10px;',
        				items:['->',{
            				glyph:'xe69a@iconfont',
            				text:'打印',
            				handler:function(){
            					var html = tpl.apply(obj.data);
            					util.print('打印游客',html,true,false);
            				}
            			}]
        			},'top');
        			
        		}
        	});
    	}
	}
});