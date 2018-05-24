Ext.define('app.model.order.visitor.column', {
	constructor:function(){
		return [
		Ext.create('Ext.grid.RowNumberer',{width:35}),
		{
	        text: '姓名',
	        width:80,
	        dataIndex: 'NAME'
	    },
	    {
	        text: '性别',
	        width:50,
	        dataIndex: 'SEX',
	        renderer: function(value){
	        	if(value=='1'){
	        		return '男';
	        	}else 
	        	if(value=='2'){
	        		return '女';
	        	}else{
	        		return '';
	        	}
	        }
	    },
	    {
	        text: '手机',
	        width:100,
	        dataIndex: 'MOBILE',
	        renderer: function(value){
	        	return util.formatMobile(value);
	        }
	    },
		{
	        text: 'E-mail',
	        width:150,
	        dataIndex: 'EMAIL'
	    },
		{
	        text: '证件',
	        flex:1,
	        dataIndex: 'CARD_NO',
	        renderer: function(value){
	        	return  value;
	        }
	    }];
	}
});