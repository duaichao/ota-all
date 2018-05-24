Ext.define('app.model.b2b.sms.rechargeColumn', {
	constructor:function(){
		return [
		Ext.create('Ext.grid.RowNumberer',{width:35}),
		{
	        text: '编号',
	        width:150,
	        dataIndex: 'NO',
	        renderer:function(v,metaData,r,colIndex,store,view){
	        	return '<div class="ht20"><a href="javascript:;">'+v+'</a></div>';
	        }
	    },{
	        text: '条数',
	        width:80,
	        dataIndex: 'COUNT',
	        renderer:function(v,metaData,r){
	        	return '<div class="ht20 f14">'+v+'</div>';
	        }
	    },{
	        text: '金额',
	        width:120,
	        dataIndex: 'AMOUNT',
	        renderer:function(v,metaData,record,colIndex,store,view){
	        	return '<div class="ht20">'+util.moneyFormat(v,'f16')+'</div>';
	        }
	    },{
	        text: '创建时间',
	        width:150,
	        dataIndex: 'CREATE_TIME',
	        renderer:function(v,metaData,record,colIndex,store,view){
	        	return '<div class="ht20">'+(v||'')+'</div>';
	        }
	    },{
	        text: '状态',
	        width:90,
	        dataIndex: 'PAY_STATUS',
	        renderer:function(value,metaData,r,colIndex,store,view){
	        	var txt = ['付款中','已付款','掉单'],
	        		color = ['yellow-color','green-color','red-color'];
	        	var payIcon = '',
		        	payType = parseInt(r.get('PAY_TYPE')),
		        	payTypes=['&#xe66a;','&#xe66e;','&#xe66d;','&#xe66b;'],
		        	payTexts=['部门余额支付','支付宝支付','财付通支付','网银支付'],
		        	payCls=['#F44336','#00a0e9;','#ff8500;','#009688'];
		        payIcon = '<i class="iconfont f18" style="color:'+payCls[payType]+'" data-qtip="'+payTexts[payType]+'">'+payTypes[payType]+'</i> ';
	        	return '<div class="ht20 '+color[value]+'">'+payIcon+txt[value]+'</div>';
	        }
	    },{
	        text: '付款时间',
	        width:150,
	        dataIndex: 'PAY_TIME',
	        renderer:function(v,metaData,record,colIndex,store,view){
	        	return '<div class="ht20">'+(v||'')+'</div>';
	        }
	    }];
	}
});