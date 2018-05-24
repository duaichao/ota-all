Ext.define('app.model.b2c.visitor.column', {
	constructor:function(){
		return [
				Ext.create('Ext.grid.RowNumberer',{width:35}),
				{
			        text: '姓名',
			        width:80,
			        dataIndex: 'NAME',
			        renderer: function(value){
			        	return  '<div class="ht20">'+value+'</div>';
			        }
			    },{
			    	text: '生日',
			        width:80,
			        dataIndex: 'CARD_NO',
			        renderer: function(value){
			        	return  '<div class="ht20">'+util.getBirthDay(value)+'</div>';
			        }
			    },
			    {
			        text: '性别',
			        width:50,
			        dataIndex: 'SEX',
			        renderer: function(value){
			        	if(value=='1'){
			        		return '<div class="ht20">男</div>';
			        	}else 
			        	if(value=='2'){
			        		return '<div class="ht20">女</div>';
			        	}else{
			        		return '';
			        	}
			        }
			    },{
			    	text: '类型',
			        width:80,
			        dataIndex: 'ATTR_NAME',
			        renderer: function(value){
			        	return '<div class="ht20">'+value+'</div>';
			        }
			    },
			    {
			        text: '手机',
			        width:150,
			        dataIndex: 'MOBILE',
			        renderer: function(value){
			        	return '<div class="ht20">'+util.formatMobile(value)+'</div>';
			        }
			    },
				{
			        text: '证件',
			        width:200,
			        dataIndex: 'CARD_NO',
			        renderer: function(value){
			        	return  '<div class="ht20">'+value+'</div>';
			        }
			    },{
					text: '产品',
			        width:500,
			        dataIndex: 'PRODUCE_NAME',
			        renderer: function(value){
			        	return  '<div class="ht20 blue-color">'+value+'</div>';
			        }
				},{
			        text: '订单编号',
			        width:120,
			        dataIndex: 'ORDER_NO',
			        renderer: function(value){
			        	return  '<div class="ht20 blue-color">'+value+'</div>';
			        }
			    },{
			        text: '来源',
			        width:50,
			        dataIndex: 'PLAT_FROM',
			        renderer: function(value){
			        	var s = ['B2B','B2C','微店'],
			        		c = ['blue-color','orange-color','green-color'];
			        	return  '<div class="ht20 '+c[value||0]+'">'+s[value||0]+'</div>';
			        }
			    },{
			    	text: '出团日期',
			        width:100,
			        dataIndex: 'START_DATE',
			        renderer: function(value){
			        	return  '<div class="ht20">'+value+'</div>';
			        }
			    },{
			    	text: '公司',
			        width:300,
			        dataIndex: 'BUY_COMPANY',
			        renderer: function(value){
			        	return  '<div class="ht20">'+value+'</div>';
			        }
			    },{
			    	text: '员工',
			        width:100,
			        dataIndex: 'USER_NAME',
			        renderer: function(value){
			        	return  '<div class="ht20">'+value+'</div>';
			        }
			    },{
			    	text: '付款时间',
			        width:180,
			        dataIndex: 'PAY_TIME',
			        renderer: function(value){
			        	value = value ||'';
			        	return  '<div class="ht20">'+value+'</div>';
			        }
			    }];
			}
});