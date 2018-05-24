Ext.define('app.view.produce.route.contactWin', {
	extend:'Ext.window.Window',
	xtype:'visitorContactWindow',
	config:{
		layout:'fit',
		modal:true,
		draggable:false,
		resizable:false,
		width:500,
		height:300,
		orderId:'',
		viewShow:'add',
		saveClose:false,
		listeners:{
			beforeclose:function(win){
				if(this.getViewShow=='add'){
					if(!win.getSaveClose()){
						Ext.Msg.show({
						    title:'<h3 class="f16" style="color:#fff;">消息提示</h3>',
						    message: '<div class="f14" style="line-height:20px;">数据可能未保存，直接关闭数据会丢失，<br>是否关闭？</div>',
						    buttons: Ext.Msg.YESNO,
						    icon: Ext.Msg.QUESTION,
						    fn: function(btn) {
						        if (btn === 'yes') {
						        	win.setSaveClose(true);
						        	win.close();
						        }
						    }
						});
						return false;
					}
				}
			}
		}
	},
	initComponent: function() {
		var me = this;
		var contactGrid = Ext.create('app.view.produce.route.contactGrid',{
			margin:10,
			viewShow:this.getViewShow(),
			orderId:this.getOrderId()
		});
		this.title = util.windowTitle('&#xe69e;','游客联系人','');
		this.items=[contactGrid];
		if(this.getViewShow()=='add'){
			this.buttons = [{
				text:'保存',
				handler:function(){
					me.saveContacts();
				}
			}];
		}
		this.callParent();
	},
	saveContacts:function(){
		var me =this,
			contactGrid = this.down('visitorContactGrid'),
			vgd = contactGrid.getStore().getData(),
			vgc = contactGrid.getStore().getCount();
		var contacts=[];
		for (var i = 0; i < vgc; i++) {
			contacts.push(vgd.items[i].data);
		}
		Ext.Ajax.request({
			url:cfg.getCtx()+'/order/route/save/contact?orderId='+this.getOrderId(),
			params:{
				models:Ext.encode(contacts)
			},
			success:function(response, opts){
				util.success('保存成功');
				me.setSaveClose(true);
				me.close();
			}
		});
	},
	updateProId:function(proId){
		if(proId){
			//var contactGrid = Ext.create('app.view.resource.contactGrid',{
			//	proId:porId
			//});
			//this.add(contactGrid);
		}
	}
});