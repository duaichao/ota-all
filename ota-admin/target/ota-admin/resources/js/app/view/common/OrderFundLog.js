Ext.define('app.view.common.OrderFundLog', {
    extend: 'Ext.form.Panel',
    config:{
    	orderId:null
    },
    initComponent: function() {
    	var me = this;
    	this.tpl = new Ext.XTemplate(
    	   		'<div class="p-detail">',
    	   			'<table width="100%" border="0">',
    	    			'<tr>',
    	    				'<td>类型</td>',
    	    				'<td>金额</td>',
    	    				'<td>操作人</td>',
    	    				'<td>操作时间</td>',
    	    				'<td>备注</td>',
    	    			'</tr>',
    	    			'<tr>',
    	    				'<td  style="background:#f1f1f1;font-size:14px;"><i class="iconfont f18 green-color">&#xe610;</i> 买家</td>',
    	    				'<td style="background:#f1f1f1;" colspan="4" >{[util.formatMoney(values.buyTotal,true,\'f14\')]}</td>',
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
    	    				'<td style="background:#f1f1f1;font-size:14px;"><i class="iconfont f18 blue-color">&#xe627;</i> 卖家</td>',
    	    				'<td style="background:#f1f1f1;"  colspan="4">{[util.formatMoney(values.saleTotal,true,\'f14\')]}</td>',
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
    	    				'<td style="background:#f1f1f1;font-size:14px;"><i class="iconfont f18 money-color">&#xe662;</i> 平台</td>',
    	    				'<td style="background:#f1f1f1;" colspan="4">{[util.formatMoney(values.manageTotal,true,\'f14\')]}</td>',
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
    	    			           '<span class="red-color">优惠活动退款</span>','<span class="red-color">优惠活动退款</span>', '单房差', '单房差'];	
    	    			return arr[v];
    	    		}
    	   		}
    	);
    	this.callParent();
    },
    loadOrderData:function(orderId){
    	var me = this;
    	orderId = orderId || this.getOrderId();
    	Ext.Ajax.request({
    		url:cfg.getCtx()+'/order/traffic/funds?orderId='+orderId,
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
});