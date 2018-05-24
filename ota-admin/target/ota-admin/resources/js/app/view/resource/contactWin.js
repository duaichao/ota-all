Ext.define('app.view.resource.contactWin', {
	extend:'Ext.window.Window',
	xtype:'contactWindow',
	config:{
		layout:'fit',
		modal:true,
		draggable:false,
		resizable:false,
		width:700,
		height:360,
		proId:'',
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
		var contactGrid = Ext.create('app.view.resource.contactGrid',{
			viewShow:this.getViewShow(),
			margin:'10',
			proId:this.getProId()
		});
		this.title = util.windowTitle('&#xe627;','产品联系人','');
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
			contactGrid = this.down('contactGrid'),
			vgd = contactGrid.getStore().getData(),
			vgc = contactGrid.getStore().getCount();
		var contacts=[],isOk=true,isContact=false;
		for (var i = 0; i < vgc; i++) {
			if(vgd.items[i].data.IS_MESSAGE==1){
				isContact = true;
			} 
			contacts.push(vgd.items[i].data);
		}
		if(!isContact){
			util.alert('请选择一个短信联系人');
			return;
		}
		Ext.Ajax.request({
			url:cfg.getCtx()+'/resource/route/save/pro/contact?produceId='+this.getProId(),
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