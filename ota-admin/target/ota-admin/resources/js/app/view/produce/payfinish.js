function showContacts(proId){
	Ext.create('app.view.resource.contactWin',{
		proId:proId,
		viewShow:'view'
	}).show();
}
var msgHtml = [
	'<div style="padding:50px;">',
],errors = ['支付异常','订单不存在','占座失败','余额不足', '账户已停用'];
var djStr = order.IS_EARNEST=='1'?'定金':'',lastDateStr='';
//支付尾款或全款支付
if(order.STATUS=='2'){
	djStr='';
}
if(code>0){
	msgHtml.push('<div style="width:600px;margin:0px auto;" class="f18 green-color"><i class="iconfont green-color" style="margin-left:10px;font-size:30px;">&#xe673;</i> 您的订单（'+order.NO+'）已成功支付'+djStr+'！');
}else{
	msgHtml.push('<div style="width:600px;margin:0px auto;" class="f18 red-color"><i class="iconfont red-color" style="margin-left:10px;font-size:30px;">&#xe670;</i> 您的订单（'+order.NO+'）支付失败！');
}
var redirectUrl = '';
if(proType=='1'){
	redirectUrl = 'order/traffic';
}
if(proType=='2'||proType=='3'){
	redirectUrl = 'order/route';
}
if(proType=='4'){
	redirectUrl = 'order/route/renew';
}
var jtJe = util.moneyFormat(order.INTER_AMOUNT,'f18',true);
if(order.IS_EARNEST=='1'){
	jtJe = util.moneyFormat(order.EARNEST_INTER_AMOUNT,'f18',true);
	var earnestType = order.EARNEST_TYPE,
		lastDate = Ext.Date.parse(order.START_DATE, "Y-m-d"),
		dayCount = order.EARNEST_DAY_COUNT;
	
	if(earnestType=='0'){
		if(lastDate){
			lastDate = Ext.Date.add(lastDate, Ext.Date.DAY, 0-dayCount);
		}
		lastDateStr ='，余额截止支付日期：<span class="f14 blue-color">'+Ext.Date.format(lastDate,'Y/m/d')+'</span>';
	}
	if(earnestType=='1'){
		if(lastDate){
			lastDate = Ext.Date.add(lastDate, Ext.Date.DAY, dayCount);
		}
		lastDateStr ='，代收款截止确认日期：<span class="f14 blue-color">'+Ext.Date.format(lastDate,'Y/m/d')+'</span>';
	}
	if(order.STATUS=='2'){
		lastDateStr='';
		jtJe = util.moneyFormat(order.INTER_AMOUNT-order.EARNEST_INTER_AMOUNT,'f18',true);
	}
}
msgHtml.push('<div style="line-height:25px;margin-left:5px;margin-top:20px;padding-top:5px;">');
msgHtml.push('您选择的产品：<a>'+order.PRODUCE_NAME+'</a>&nbsp;&nbsp;&nbsp;&nbsp;人数：'+order.MAN_COUNT+'成人,'+order.CHILD_COUNT+'儿童&nbsp;&nbsp;&nbsp;&nbsp;出发日期：'+order.START_DATE+'&nbsp;&nbsp;&nbsp;&nbsp;'+djStr+'金额：'+jtJe+lastDateStr);
if(discount&&order.STATUS=='2'){
	msgHtml.push('<div class="ht20 red-color">');
	msgHtml.push('参加活动'+discount.TITLE);
	if(discount.TOTAL_AMOUNT>0){
		msgHtml.push('，立减'+discount.TOTAL_AMOUNT+'元');
	}else{
		msgHtml.push('，订单金额过小无法免减');
	}
	msgHtml.push('</div>');
}
msgHtml.push('<div class="ht20">更多操作：<a href="javascript:;" onclick="util.redirectTo(\''+redirectUrl+'\')">订单列表</a></div>');
msgHtml.push('</div>');

msgHtml.push('<div style="line-height:25px;margin-left:5px;margin-top:10px;padding-top:5px;">');
if(code>0){
	msgHtml.push('订单号已发送到供应商联系人手机，请记住订单号，您今后可在登录后查询订单、点评、取消订单。<a href="javascript:;" onclick="showContacts(\''+order.PRODUCE_ID+'\')">联系卖家</a>');
}else{
	msgHtml.push('错误提示：'+errors[0-code]+'');
}
msgHtml.push('</div>');
msgHtml.push('</div>');
msgHtml.push('</div>');
Ext.define('app.view.produce.payfinish', {
    extend: 'Ext.container.Viewport',
    layout: 'border',
    items: [{
    	region:'center',
    	autoScroll:true,
    	style:'border-top: 3px solid '+(code>0?'#C8E6C9':'#FFCCBC'),
    	border:false,
    	items:[{
    		margin:'40 20',
    		bodyStyle:'background:#fff',
    		height:300,
    		html:msgHtml.join('')
    	}],
	    buttons:[{
    		xtype:'panel',
    		width:800,
    		margin:'0 0 0 20',
    		bodyStyle:'background:transparent;',
    		html:[
    			'<ol class="ui-step ui-step-4">',
    				'<li class="ui-step-start ui-step-done">',
				        '<div class="ui-step-line">-</div>',
				        '<div class="ui-step-icon">',
				            '<i class="iconfont">&#xe69f;</i>',
				            '<i class="ui-step-number">1</i>',
				            '<span class="ui-step-text">选择产品</span>',
				        '</div>',
				    '</li>',
				    '<li class="ui-step-done">',
				        '<div class="ui-step-line">-</div>',
				        '<div class="ui-step-icon">',
				            '<i class="iconfont">&#xe69f;</i>',
				            '<i class="ui-step-number">2</i>',
				            '<span class="ui-step-text">填写与核对</span>',
				        '</div>',
				    '</li>',
				    '<li class="ui-step-done">',
				        '<div class="ui-step-line">-</div>',
				        '<div class="ui-step-icon">',
				            '<i class="iconfont">&#xe69f;</i>',
				            '<i class="ui-step-number">3</i>',
				            '<span class="ui-step-text">支付</span>',
				        '</div>',
				    '</li>',
				    '<li class="ui-step-active ui-step-end">',
				        '<div class="ui-step-line">-</div>    ',
				        '<div class="ui-step-icon">',
				            '<i class="iconfont">&#xe69f;</i>',
				            '<i class="iconfont ui-step-number">&#xe6a0;</i>',
				            '<span class="ui-step-text">成功</span>',
				        '</div>',
				    '</li>',
    			'</ol>'
    		].join('')
    	},'->',{
    		text:'查看订单',
    		itemId:'backOrderList'
    	}]
    }]
});

