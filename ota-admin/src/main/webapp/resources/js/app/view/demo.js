Ext.define('app.view.user.demo', {
	extend: 'Ext.container.Viewport',
    layout: 'border',
    items: [{
    	region:'west',
    	hidden:true
    },{
        region: 'center',
        dockedItems:[{
        	xtype:'toolbar',
        	items:[{
        		text:'提示消息',
        		handler:function(){
        			util.alert('早起早睡','一般{0}起床，晚上{1}睡觉','9点','10点');
        			util.error('早起早睡','一般{0}起床，晚上{1}睡觉','9点','10点');
        			util.success('早起早睡','一般{0}起床，晚上{1}睡觉','9点','10点');
        		}
        	},{
        		text:'上传附件',
        		handler:function(){
        			util.uploadAttachment();
        		}
        	},{
        		text:'下载附件',
        		handler:function(){
        			util.downloadAttachment('http://www.163.com');
        		}
        	}]
        }],
        items:[{
        	html:'demo'
        }]
    }]
});