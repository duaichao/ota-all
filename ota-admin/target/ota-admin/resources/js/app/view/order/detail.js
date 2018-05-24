function showContacts(proId){
	Ext.create('app.view.resource.contactWin',{
		proId:proId,
		viewShow:'view'
	}).show();
}
Ext.define('app.view.order.detail', {
	extend:'Ext.Window',
	width:'85%',
	height:'85%',
	config:{
		orderRecord:null,
		modal:true,
		draggable:false,
		resizable:false,
		layout:'fit',
		autoScroll:true,
		tpl:new Ext.XTemplate(
           		'<div class="p-detail" style="padding:10px;">',
           		'<div style="border: 1px solid #e1e1e1;border-bottom:none;padding:15px;background: #f1f1f1;;padding-left:10px;margin-top:10px;"><i class="color-mark"></i><span class="header-name">订单详情</span></div>',
           		'<table width="668" border="0">',
           			'<tr>',
           				'<td class="name">产品名称</td>',
           				'<td colspan="3">{PRODUCE_NAME}</td>',
           				'<td class="name">合同号</td>',
           				'<td>{CON_NO}</td>',
           			'</tr>',
           			'<tr>',
           				'<td class="name">订单编号</td>',
           				'<td><a>{NO}</a></td>',
           				'<td class="name">出发日期</td>',
           				'<td>{START_DATE}</td>',
           				'<td class="name">人数</td>',
           				'<td>成人{MAN_COUNT}人，儿童{CHILD_COUNT}人，共{[this.getPersonCount(values.MAN_COUNT,values.CHILD_COUNT)]}人</td>',
           				
           			'</tr>',
           			'<tr>',
           				'<td class="name">集合地点</td>',
           				'<td colspan="3">{MUSTER_PLACE}</td>',
           				'<td class="name">集合时间</td>',
           				'<td>{MUSTER_TIME}</td>',
           				
           			'</tr>',
           			'<tr>',
       				'<td class="name">出团备注</td>',
       				'<td colspan="5">{REMARK}</td>',
       				'</tr>',
       			'</tr>',
           			'<tr>',
           				'<td colspan="6" style="background:#f8f8f8;font-size:14px;"><i class="iconfont f16 blue-color">&#xe627;</i> 卖家</td>',
           			'</tr>',
           			'<tr>',
           				'<td colspan="5">{COMPANY_NAME}</td>',
           				'<td><a href="javascript:;" onclick="showContacts(\'{PRODUCE_ID}\')">联系卖家</a></td>',
           			'</tr>',
           			'<tr>',
           			'<td colspan="6" style="background:#f8f8f8;font-size:14px;"><i class="iconfont f16 money-color">&#xe610;</i> 买家</td>',
           			'</tr>',
           			'<tr>',
           				'<td colspan="2">{BUY_COMPANY}</td>',
           				'<td class="name">联系人</td>',
           				'<td>{BUY_USER_NAME}</td>',
           				'<td class="name">联系电话</td>',
           				'<td>{BUY_PHONE}</td>',
           			'</tr>',
           			'<tr>',
           				'<td colspan="6" style="background:#f8f8f8;font-size:14px;"><i class="iconfont f16 green-color">&#xe664;</i>  游客</td>',
           			'</tr>',
           			'<tr>',
           				'<td colspan="2"></td>',
           				'<td class="name">联系人</td>',
           				'<td>{VISITOR_CONCAT}</td>',
           				'<td class="name">联系电话</td>',
           				'<td>{VISITOR_MOBILE}</td>',
           			'</tr>',
           		'</table>',
           		
           		'<tpl if="this.hasData(traffics)">',
           		'<div style="border: 1px solid #e1e1e1;border-bottom:none;padding:15px;background: #f1f1f1;;padding-left:10px;margin-top:10px;"><i class="color-mark"></i><span class="header-name">大交通</span></div>',
           		'<table width="100%" border="0" >',
	       			'<tr>',
	       				'<td>序号</td>',
	       				'<td>交通日期</td>',
	       				'<td>交通名称</td>',
	       			'</tr>',
	       			'<tpl for="traffics">',
	       			'<tr>',
	       				'<td>{#}</td>',
	       				'<td>{EXCA}</td>',
	       				'<td>{PRODUCE_NAME}</td>',
	       			'</tr>',
	       			'</tpl>',
	       		'</table>',
	       		'</tpl>',
	       		'<div style="border: 1px solid #e1e1e1;border-bottom:none;padding:15px;background: #f1f1f1;;padding-left:10px;margin-top:10px;"><i class="color-mark"></i><span class="header-name">游客</span></div>',
           		'<table width="100%" border="0" >',
           			'<tr>',
           				'<td>姓名</td>',
           				'<td>性别</td>',
           				'<td>游客类型</td>',
           				'<td>手机</td>',
           				'<td>证件类型</td>',
           				'<td>证件号码</td>',
           			'</tr>',
           			'<tpl for="visitors">',
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
           		
           		'<tpl if="this.hasData(otherFee)">',
           		'<div style="height:30px;line-height:30px;background:#BBDEFB;font-size:14px;margin-top:5px;padding-left:5px;color:#1976D2;">其他附加费</div>',
           		'<table width="100%" border="0">',
	       			'<tr>',
	       				'<td>附加费名称</td>',
	       				'<td>单价</td>',
	       				'<td>人数</td>',
	       			'</tr>',
	       			'<tpl for="otherFee">',
	       			'<tr>',
	       				'<td>{TITLE}</td>',
	       				'<td>{PRICE}</td>',
	       				'<td>{NUM}</td>',
	       			'</tr>',
	       			'</tpl>',
	       		'</table>',
           		'</tpl>',
           		
		    /*'<div style="line-height:25px;position:absolute;width:150px;right:5px;top:40px;border:3px solid #FF5722;background:#fff3e0;padding:10px;">',
		    '外卖：{[util.moneyFormat(values.SALE_AMOUNT,\'f16\')]}<br>',
		    '同行：{[util.moneyFormat(values.INTER_AMOUNT,\'f14\',true)]}<br>',
		    
		    '其他：{[util.moneyFormat(values.OTHER_AMOUNT,\'f14\')]}',
		    '</div>',*/
		    '</div>',{
           			getPersonCount:function(a,b){
           				return a+b;
           			},
               		hasData:function(d){
               			return d&&d.length>0;
               		}
           		}
		),
		dockedItems:[{
			itemId:'tool',
			xtype:'container',
			dock:'bottom',
			html:'<div style="height:40px;background:#fff3e0;border-bottom:3px solid #FF5722;"></div>'
		}]
	},
	updateOrderRecord: function(orderRecord) {
    	var me = this;
    	if(orderRecord){
    		this.setTitle(util.windowTitle('','订单详情-'+orderRecord.get('NO'),''));
    		Ext.Ajax.request({
        		url:cfg.getCtx()+'/order/route/traffics?orderId='+orderRecord.get('ID'),
        		success:function(response, opts){
        			var obj = Ext.decode(response.responseText);
        			orderRecord.set('traffics',obj.data);
        			if(parseInt(orderRecord.get('OTHER_AMOUNT'))>0){
	        			Ext.Ajax.request({
	                		url:cfg.getCtx()+'/order/route/other/fee/logs?orderId='+orderRecord.get('ID'),
	                		success:function(response, opts){
	                			var obj = Ext.decode(response.responseText);
	                			orderRecord.set('otherFee',obj.data);
	                			me.loadVisitor(orderRecord);
	                		}
	                	});
        			}else{
        				me.loadVisitor(orderRecord);
        			}
        		}
        	});
    		
    	}
    },
    loadVisitor:function(orderRecord){
    	var me = this;
    	Ext.Ajax.request({
    		url:cfg.getCtx()+'/order/visitor/listOrderVisitor?orderId='+orderRecord.get('ID'),
    		success:function(response, opts){
    			var obj = Ext.decode(response.responseText);
    			orderRecord.set('visitors',obj.data);
    			var html = ['<div style="padding:20px;background:#f5f5f5; border-top: 1px solid #e1e1e1!important;-webkit-box-shadow: 0 5px 10px -5px #e1e1e1;box-shadow: 0 5px 10px -5px #e1e1e1;">'];
    			html.push('外卖：'+util.moneyFormat(orderRecord.data.SALE_AMOUNT,'f20'));
    			html.push('  &nbsp;&nbsp;&nbsp;&nbsp;  同行：'+util.moneyFormat(orderRecord.data.INTER_AMOUNT,'blue-color f18',true));
    			html.push('  &nbsp;&nbsp;&nbsp;&nbsp;  其他：'+util.moneyFormat(orderRecord.data.OTHER_AMOUNT,'f18'));
    			html.push('</div>');
    			me.down('container#tool').update(html.join(''));
    			me.setData(orderRecord.data);
    		}
    	});
    }
});