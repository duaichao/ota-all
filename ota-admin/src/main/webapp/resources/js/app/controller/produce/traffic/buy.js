Ext.define('app.controller.produce.traffic.buy', {
	extend: 'app.controller.common.BaseController',
	views:['Ext.ux.grid.column.RadioColumn','app.view.produce.traffic.detailGrid','app.view.produce.traffic.visitorGrid'],
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
			trafficGrid: 'trafficdetailgrid',
			visitorGrid: 'trafficvisitorgrid',
			subForm:'form[itemId=subForm]'
        }
	},
	saveOrder:function(btn){
		var me = this,
			tg = this.getTrafficGrid(),
			vg = this.getVisitorGrid(),
			f = this.getSubForm(),
			form = f.getForm(),
			tgds = tg.getStore().getData(),
			tgd = tgds.items[0],
			vgd = vg.getStore().getData(),
			vgc = vg.getStore().getCount();
		if(parseInt(tgd.get('MIN_BUY'))>vgc){
			util.alert('游客人数('+vgc+')小于交通最少购票数('+tgd.get('MIN_BUY')+')');
			return;
		}
		var visitors=[],isContact=false;
		for (var i = 0; i < vgc; i++) { 
			if(vgd.items[i].data.CONTACT==1){
				isContact = true;
			} 
			visitors.push(vgd.items[i].data);
		}
		
		if(!isContact){
			util.alert('请选择一个游客联系人');
			return;
		}
		if (form.isValid()) {
			btn.disable();
			f.mask('正在保存数据...');
            form.submit({
            	submitEmptyText:false ,
            	params:{
            		detail:Ext.encode(tgd.data),
            		visitors:Ext.encode(visitors)
            	},
            	url:cfg.getCtx()+'/order/traffic/save',
                success: function(form, action) {
                	var orderId = action.result.message||'';
                    document.location.href=cfg.getCtx()+"/produce/paycenter?orderId="+orderId+'&type=1';
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