Ext.define('app.model.b2b.sms.column', {
	constructor:function(){
		return [
		Ext.create('Ext.grid.RowNumberer',{width:35}),
		{
	        text: '手机号',
	        flex:1,
	        dataIndex: 'MOBILE',
	        renderer:function(v,metaData,record,colIndex,store,view){
            	metaData.tdAttr = 'data-qtip="'+v+'"';
	        	return [
	        		'<div class="f12 ht20" style="width:480px;white-space:normal;word-wrap: break-word; break-word: break-all;display:inline-block;">'+v+'</div>'
	        	].join('');
	        }
	    },{
	    	text: '使用条数',
			width:80,
			dataIndex: 'CNT',
			renderer:function(v,metaData,r){
				return '<div class="ht20 f14">'+(v||'1')+'</div>';
			}
	    },{
	    	text: '公司名称',
			width:200,
			dataIndex: 'COMPANY',
			renderer:function(v,metaData,r){
				return '<div class="ht20 f14">'+(v||'')+'</div>';
			}
	    },{
	        text: '短信内容',
	        width:400,
	        dataIndex: 'CONTENT',
	        renderer:function(v,metaData,r){
	        	metaData.tdAttr = 'data-qtip="'+v+'"';
	        	return [
	        		'<div class="f12 ht20" style="width:380px;white-space:normal;display:inline-block;color:#666;">'+v+'</div>'
	        	].join('');
	        }
	    },{
	        text: '类型',
	        width:80,
	        dataIndex: 'TYPE',
	        renderer:function(value,metaData,record,colIndex,store,view){
	        	var str = '<span class="blue-color">系统自动</span>';
	        	if(value==4){
	        		str = '<span class="green-color">手动群发</span>';
	        	}
	        	return str;
	        }
	    },{
	        text: '状态',
	        width:80,
	        dataIndex: 'STATUS',
	        renderer:function(value,metaData,record,colIndex,store,view){
	        	var str = '<span class="green-color">成功</span>';
	        	if(value<0){
	        		str = '<span class="red-color">失败</span>';
	        	}
	        	if(value==-4){
	        		str = '<span class="red-color">格式错误</span>';
	        	}
	        	if(value==-4){
	        		str = '<span class="yellow-color">余额不足</span>';
	        	}
	        	var res = ['B2B','B2C'];
	        	return str+'<div class="ht20">'+res[parseInt(record.get('RESOURCES'))]+'</div>';
	        }
	    },{
	        text: '发送时间',
	        width:120,
	        dataIndex: 'CREATE_TIME'
	    }];
	}
});