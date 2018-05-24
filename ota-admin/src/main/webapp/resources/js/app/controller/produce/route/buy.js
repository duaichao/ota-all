Ext.define('app.controller.produce.route.buy', {
	extend: 'app.controller.common.BaseController',
	views:['Ext.ux.grid.column.RadioColumn','app.view.produce.route.visitorGrid'],
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
			visitorGrid: 'routevisitorgrid'
        }
	},
	saveOrder:function(btn){
		var me = this,
			vg = this.getVisitorGrid(),
			f = btn.up('form[itemId=subForm]'),
			form = f.getForm(),
			vgd = vg.getStore().getData(),
			vgc = vg.getStore().getCount(),
			editOrderId = f.down('hidden#orderIdHiddenField').getValue();
		
		
		
		
		var muster = f.down('#musterTimeItem'),
			conno = f.down('combo#connoItem');
		if(cfg.getUser().isContractMust=='1'){
			if(!editOrderId&&conno.getRawValue()==''){
				util.alert('请选择合同号');
				return;
			}
		}
		/*if(planId!=''&&muster.getValue()==''){
			util.alert('请选择集合信息');
			return;
		}*/
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
		//单房差 如果游客为一人，单房差必选；否则不必须，选择数量
		/*if(visitors.length==1&&Ext.getCmp('singleRoomField').getValue()==0){
			util.alert('如果游客为一人，单房差必填');
			return;
		}*/
		if(Ext.getCmp('singleRoomField').getValue()>visitors.length){
			util.alert('单房差数量超出游客数量');
			return;
		}
		if (form.isValid()) {
			btn.disable();
			f.mask('正在保存数据...');
            form.submit({
            	submitEmptyText:false ,
            	params:{
            		detail:Ext.encode(routeDetail),
            		visitors:Ext.encode(visitors)
            	},
            	url:cfg.getCtx()+'/order/route/save',
                success: function(form, action) {
                	var orderId = action.result.message||'',
                		type = 2;
                	//其他费用不允许直接付款 需要审核
                	if(orderId=='otherFee'){
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
                   				html:'<h3 class="alert-box"> 含附加费用的订单产品，保存成功后，须等到供应商审核订单完成后才能付款，正在跳转到订单列表页面</h3>'
                   			}],
                   			listeners:{
                   				close:function(){
                   					util.redirectTo('order/route');
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
                	}
                	if(planId==''){type=3;}
                	var discountInfo = f.getValues()['DISCOUNT_INFO'];
                	if(discountInfo){
                		discountInfo = encodeURI(encodeURI(discountInfo));
                	}
                    document.location.href=cfg.getCtx()+"/produce/paycenter?orderId="+orderId+'&type='+type+'&discountInfo='+discountInfo;
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