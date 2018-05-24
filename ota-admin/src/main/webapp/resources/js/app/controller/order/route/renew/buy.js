Ext.define('app.controller.order.route.renew.buy', {
	extend: 'app.controller.common.BaseController',
	views:['Ext.ux.grid.column.RadioColumn','app.view.order.route.renew.visitorGrid'],
	config: {
		control: {
			'button[itemId=saveBtn]':{
				click:'saveOrder'
			},'button[itemId=backBtn]':{
				click:function(){
					history.go(-1);
				}
			}
		},
		refs: {
			visitorGrid: 'renewvisitorgrid',
			priceGrid:'rprice'
        }
	},
	saveOrder:function(btn){
		var me = this,
		vg = this.getVisitorGrid(),
		f = btn.up('form[itemId=subForm]'),
		form = f.getForm(),
		vgd = vg.getStore().getData(),
		vgc = vg.getStore().getCount(),
		editOrderId = f.down('hidden#orderIdHiddenField').getValue(),
		priceGrid = this.getPriceGrid(),
		priceParams = priceGrid.hiddenParams;
		
		var conno = f.down('combo#connoItem');
		if(cfg.getUser().isContractMust=='1'){
		if(!editOrderId&&conno.getRawValue()==''){
			util.alert('请选择合同号');
			return;
		}
		}
		var visitors=[],isContact=false,isOk=true,contact={};
		for (var i = 0; i < vgc; i++) {
			if(vgd.items[i].data.CONTACT==1){
				isContact = true;
				contact = vgd.items[i].data;
			}
			if(vgd.items[i].data.NAME==''||vgd.items[i].data.CARD_NO==''){
				isOk = false;
			} 
			visitors.push(vgd.items[i].data);
		}
		if(!isContact){
			util.alert('请选择一个游客联系人');
			return;
		}
		if(contact.MOBILE==''){
			util.alert('请填写游客联系人手机');
			return;
		}
		if(!isOk){
			util.alert('请填写游客姓名和证件号码');
			return;
		}
		if(Ext.getCmp('singleRoomField').getValue()>visitors.length){
			util.alert('单房差数量超出游客数量');
			return;
		}
		if(priceParams.length==0){
			util.alert('请填写价格');
			return;
		}
		if (form.isValid()) {
			btn.disable();
			f.mask('正在保存数据...');
            form.submit({
            	submitEmptyText:false ,
            	params:{
            		detail:Ext.encode(routeDetail),
            		visitors:Ext.encode(visitors),
            		prices:Ext.encode(priceParams)
            	},
            	url:cfg.getCtx()+'/order/route/renew/save',
                success: function(form, action) {
                	var orderId = action.result.message||'',
                		type = 2;
            		var win = Ext.create('Ext.window.Window',{
               			title: util.windowTitle('','信息提示',''),
               			width:360,
               			height:180,
               			draggable:false,
            			resizable:false,
            			closable:false,
            			modal:true,
               			bodyStyle:'background:#fff;padding:10px;',
               			layout:'fit',
               			items:[{
               				html:'<h3 class="alert-box"> 系统提示：基本信息保存成功后，请提交审核，正在跳转到补单列表页面</h3>'
               			}],
               			listeners:{
               				close:function(){
               					util.redirectTo('order/route/renew');
               				}
               			},
               			buttons:[{
               				text:'(10s)后自动跳转',
               				cls:'disable',
               				listeners:{
               					afterrender:function(btn){
               						var i = 10;
               						var inter = setInterval(function(){
               							i--;
               							btn.setText('('+i+'s)后自动跳转');
               							if(i==0){
               								win.close();
               								clearInterval(inter);
               								inter = null;
               							}
               						},1000);
               					}
               				},
               				handler:function(){
               					win.close();
               				}
               			}]
               		}).show();
            		return;
                },
                failure: function(form, action) {
                	f.unmask();
                	var state = action.result?action.result.statusCode:0,
                		errors = ['保存异常','订单编号生成失败','信息填写不完整'];
                    util.error(errors[0-parseInt(state)]);
                    btn.enable();
                }
            });
		}
	}
});