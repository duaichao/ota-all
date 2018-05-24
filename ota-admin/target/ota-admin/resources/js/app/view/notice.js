function showDetail(id,title){
	var tpl = new Ext.XTemplate(
	    '<div class="p-detail">',
	    '<h3>{TITLE}</h3>',
	    '<div class="p-time">{CREATE_TIME}</div>',
	    '<div class="p-content">',
	    '{CONTENT}',
	    '</div>',
	    '</div>'
	);
	Ext.Ajax.request({
		url:cfg.getCtx()+'/b2b/notice/detail',
		params:{ID:id},
		success:function(response){
			var data = Ext.decode(response.responseText).data,
				d = data[0];
			Ext.create('Ext.window.Window',{
				title:util.windowTitle('&#xe616;',title),
				width:800,
				height:400,
				modal:true,
				maximizable:true,
				draggable:false,
				resizable:false,
	   			layout:'fit',
	   			bodyPadding:'5',
	   			items:[{
	   				autoScroll:true,
	   				tpl:tpl,
	   				data:d
	   			}]
			}).show();
		}
	});
}
Ext.define('app.view.notice', {
	extend: 'Ext.Container',
    xtype: 'appNotice',
    border:false,
    bodyStyle:'background:transparent;color:#fff;overflow:hidden;',
    tpl:[
         '<div class="notice">',
         '<ul>',
         '<tpl for="data">',
    	 '<li><a href="javascript:;" id="{ID}" onclick="showDetail(\'{ID}\',\'{TITLE}\')" class="word">{TITLE}</a><li>',
    	 '</tpl>',
    	 '</ul>',
    	 '</div>'
    ],
    initComponent: function() {
    	this.callParent();
    	var me = this;
    	Ext.Ajax.request({
			url:cfg.getCtx()+'/b2b/notice/list?start=0&limit=10&type=0',
			success:function(response){
				var data = Ext.decode(response.responseText);
				me.setData(data);
				
				
				var box=me.getEl().down("div.notice").dom,can=true;
				box.innerHTML+=box.innerHTML;
				box.onmouseover=function(){can=false};
				box.onmouseout=function(){can=true};
				new function (){
					var stop=box.scrollTop%23==0&&!can;
					if(!stop)box.scrollTop==parseInt(box.scrollHeight/2)?box.scrollTop=0:box.scrollTop++;
					setTimeout(arguments.callee,box.scrollTop%23?10:5000);
				};
			}
    	});
    }
});