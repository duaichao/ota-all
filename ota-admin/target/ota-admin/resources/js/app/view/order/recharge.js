Ext.define('app.view.order.recharge', {
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
	   			'<table width="100%" border="0" >',
	    			'<tr style="background:#f5f5f5;">',
	    				'<td>类型</td>',
	    				'<td>金额</td>',
	    				'<td>操作人</td>',
	    				'<td>操作时间</td>',
	    				'<td>备注</td>',
	    			'</tr>',
	    			'<tr>',
	    				'<td  style="    background: none 0px 0px repeat scroll rgba(251, 180, 76, 0.1);font-size:14px;"><i class="iconfont f14 green-color" style="top:0px;">&#xe610;</i> 买家</td>',
	    				'<td style="    background: none 0px 0px repeat scroll rgba(251, 180, 76, 0.1);" colspan="4" >{[util.moneyFormat(values.buyTotal,\'f14\')]}</td>',
	    			'</tr>',
	    			'<tpl for="buyer">',
	    			'<tr>',
	    				'<td>{[this.typeText(values.TYPE)]}</td>',
	    				'<td>{[util.moneyFormat(values.AMOUNT,\'f12 disable-color\')]}</td>',
	    				'<td>{CREATE_USER}</td>',
	    				'<td>{CREATE_TIME}</td>',
	    				'<td>{REMARKS}</td>',
	    			'</tr>',
	    			'</tpl>',
	    			'<tr>',
	    				'<td style="    background: none 0px 0px repeat scroll rgba(251, 180, 76, 0.1);font-size:14px;"><i class="iconfont f14 blue-color" style="top:0px;">&#xe627;</i> 卖家</td>',
	    				'<td style="    background: none 0px 0px repeat scroll rgba(251, 180, 76, 0.1);"  colspan="4">{[util.moneyFormat(values.saleTotal,\'f14\')]}</td>',
	    			'</tr>',
	    			'<tpl for="saler">',
	    			'<tr>',
	    				'<td>{[this.typeText(values.TYPE)]}</td>',
	    				'<td>{[util.moneyFormat(values.AMOUNT,\'f12 disable-color\')]}</td>',
	    				'<td>{CREATE_USER}</td>',
	    				'<td>{CREATE_TIME}</td>',
	    				'<td>{REMARKS}</td>',
	    			'</tr>',
	    			'</tpl>',
	    			'<tr>',
	    				'<td style="    background: none 0px 0px repeat scroll rgba(251, 180, 76, 0.1);font-size:14px;"><i class="iconfont f14 money-color" style="top:0px;">&#xe662;</i> 平台</td>',
	    				'<td style="    background: none 0px 0px repeat scroll rgba(251, 180, 76, 0.1);" colspan="4">{[util.moneyFormat(values.manageTotal,\'f14\')]}</td>',
	    			'</tr>',
	    			'<tpl for="manager">',
	    			'<tr>',
	    				'<td>{[this.typeText(values.TYPE)]}</td>',
	    				'<td>{[util.moneyFormat(values.AMOUNT,\'f12 disable-color\')]}</td>',
	    				'<td>{CREATE_USER}</td>',
	    				'<td>{CREATE_TIME}</td>',
	    				'<td>{REMARKS}</td>',
	    			'</tr>',
	    			'</tpl>',
	    		'</table>',
	   		'</div>',{
	    		typeText:function(v){
	    			var arr = ['','外卖','同行','外卖调价','同行调价','外卖退款','同行退款','营销费','实收退款','营销费','同行付款',
	    			           '<span class="orange-color">优惠活动</span>','<span class="orange-color">优惠活动</span>',
	    			           '<span class="red-color">优惠活动退款</span>','<span class="red-color">优惠活动退款</span>', '单房差', '单房差', '其他费用', '其他费用'];	
	    			return arr[v];
	    		}
	   		}
		)
	},
	updateOrderRecord: function(orderRecord) {
    	var me = this;
    	if(orderRecord){
    		this.setTitle(util.windowTitle('','<span style="height:18px;display:inlie-block;line-height:18px;font-size:18px;">'+orderRecord.get('NO')+'</span><span class="f14" style="line-height:18px;margin-left:10px;height:18px;display:inline-block;">资金流水</span>',''));
    		Ext.Ajax.request({
        		url:cfg.getCtx()+'/order/traffic/funds?orderId='+orderRecord.get('ID'),
        		success:function(response, opts){
        			var obj = Ext.decode(response.responseText);
        			var data = obj.data,
						o = {
							buyTotal:0,
							buyer:[],
							saleTotal:0,
							saler:[],
							manageTotal:0,
							manager:[]
						};
					for(var i=0;i<data.length;i++){
						if(data[i].ACCOUNT_TYPE==1){
							o.buyer.push(data[i]);
							o.buyTotal+=parseFloat(data[i].AMOUNT);
						}
						if(data[i].ACCOUNT_TYPE==2){
							o.saler.push(data[i]);
							o.saleTotal+=parseFloat(data[i].AMOUNT);
						}
						if(data[i].ACCOUNT_TYPE==3){
							o.manager.push(data[i]);
							o.manageTotal+=parseFloat(data[i].AMOUNT);
						}
					}	
        			me.setData(o);
        		}
        	});
    	}
	}
});