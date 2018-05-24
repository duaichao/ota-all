Ext.define('app.controller.account.index', {
	extend: 'app.controller.common.BaseController',
    views:['app.view.account.grid'],
	config: {
        control: {
        	'accountgrid':{
        		afterrender: 'onBaseGridRender'
        	},
        	'panel[itemId=baseinfo]': {
	             afterrender: 'onBaseInfoRender'
	         },
	         'toolbar[itemId=accounttool] > button':{
	         	click : 'whatPay'
	         }
	    },
	    refs: {
	    	accountGrid:'accountgrid',
	    	baseInfo:'panel[itemId=baseinfo]'
	    }
    },
    onBaseGridRender :function(g){
    	var me = this;
		/*setTimeout(function(){
		Ext.Ajax.request({
		    url: util.getPowerUrl(),
		    success: function(response, opts) {
		        var obj = Ext.decode(response.responseText),
		        	items = obj.children,
		        	btns = [];
		        for(var i=0;i<items.length;i++){
		        	if(Ext.String.endsWith(items[i].itemId,'aaa')){
		        		btns.push(items[i]);
		        	}
		        }
		        util.createGridTbar(g,btns);
		    }
		});
		});*/
		var btns = [
		'->',{
			xtype: 'datefield',
			format: 'Y-m-d',
			width:100,
			editable:false,
	        itemId:'startxddt',
	        showToday:false
		},{
			xtype: 'datefield',
			width:100,
			format: 'Y-m-d',
			editable:false,
			itemId:'endxddt',
   			showToday:false
		},{
			glyph:'xe61c@iconfont',
          	tooltip:'查询',
          	width:30,
			handler:function(btn){
				var grid = me.getAccountGrid(),
					start = btn.previousSibling().getValue(),
					end = btn.previousSibling().previousSibling().getValue();
				grid.getStore().getProxy().setExtraParams({
					'OP_TIME_BEGIN':Ext.Date.format(end,'Y-m-d'),
					'OP_TIME_END':Ext.Date.format(start,'Y-m-d')
				});
				grid.getStore().load();
			}
		},{
			glyph:'xe63d@iconfont',
          	width:30,
          	tooltip:'重置日期',
			handler:function(btn){
				var grid = me.getAccountGrid(),
					start = btn.previousSibling().previousSibling(),
					end = btn.previousSibling().previousSibling().previousSibling();
				start.reset();
				end.reset();
				grid.getStore().getProxy().setExtraParams({
					'OP_TIME_BEGIN':'',
					'OP_TIME_END':''
				});
				grid.getStore().load();
			}
		},{
			text:'导出',
			handler:function(btn){
				var startCmp = btn.previousSibling().previousSibling().previousSibling(),
					start = startCmp.getValue(),
					end = startCmp.previousSibling().getValue();
				start = Ext.Date.format(start,'Y-m-d');
				end = Ext.Date.format(end,'Y-m-d');
				util.downloadAttachment(cfg.getCtx()+'/account/export?DEPART_ID='+entityId+'&OP_TIME_BEGIN='+end+'&OP_TIME_END='+start);
			}
		}
		];
		
		util.createGridTbar(g,btns);
    },
    loadBaseInfo :function(g){
    	Ext.Ajax.request({
			url:cfg.getCtx()+'/account/stat?DEPART_ID='+entityId,
			success:function(response, opts){
				var data = Ext.decode(response.responseText);
				g.update(data);
			}
		});
    },
    onBaseInfoRender :function(g){
   		var me = this;
		setTimeout(function(){
		me.loadBaseInfo(g);
		Ext.Ajax.request({
		    url: util.getPowerUrl('site/company'),
		    success: function(response, opts) {
		        var obj = Ext.decode(response.responseText),
		        	items = obj.children,
		        	btns = [];
		        for(var i=0;i<items.length;i++){
		        	if(Ext.String.endsWith(items[i].itemId,'account')){
		        		btns.push(items[i]);
		        	}
		        }
		        
		        util.createGridTbar(g,btns);
		    }
		});
		},500);
	},
	whatPay :function(btn){
		var me = this,grid = me.getAccountGrid(),
			info = me.getBaseInfo();
		if(btn.itemId=='zxczaccount'){
			this.internetPay(btn);
			return;
		}
		if(btn.itemId=='tyaccount'||btn.itemId=='qyaccount'){
			this.toggleOpen(btn);
			return;
		}
		var type = {'czaccount':'1','tkaccount':'2','tzaccount':'3','hkaccount':'4'};
		var win = util.createEmptyWindow(
			btn.text,
			util.glyphToFont(btn.glyph),400,240,[
				{
					xtype:'hidden',
					value:entityId,
					name:'pm[DEPART_ID]'
				},{
					xtype:'hidden',
					value:type[btn.itemId],
					name:'pm[TYPE]'
				},{
		        	fieldLabel:'金额',
		        	xtype:'fieldcontainer',
		        	layout:'hbox',
		        	items:[{
		        		width:80,
		        		minValue:0,
			        	name : 'pm[MONEY]',
						xtype: 'numberfield',
						allowBlank:false
		        	},{
		        		xtype:'label',
		        		itemId:'maxMoney',
		        		style:'padding:5px 10px;',
		        		html:''
		        	}]
			    },{
			    	xtype:"textareafield",
			    	fieldLabel:"备注",
			    	name:'pm[NOTE]',
					rows:3,cols:20,
					anchor:'100%',
					allowBlank:false 
				},{
					fieldLabel:"上传凭证",
					anchor:'100%',
					name:'CERTIFY_ATTR',
					xtype:"filefield",
					buttonConfig: {
		                text:'',
						ui:'default-toolbar',
		                glyph: 'xe648@iconfont'
		            }
				}
			],[{
				text:'确定',
				handler:function(bbb){
					var form = win.down('form'),
						f = form.getForm();
					if(f.isValid()){
						bbb.disable();
						f.submit({
							submitEmptyText:false ,
							url:cfg.getCtx()+'/account/save',
			                success: function(form, action) {
			                	grid.getStore().load();
			                	win.close();
			                	util.success(btn.text+'保存成功');
			                	me.loadBaseInfo(info);
			                },
			                failure: function(form, action) {
			                	bbb.enable();
			                	var state = action.result?action.result.statusCode:0,
			                		errors = ['保存结果异常'];
			                    util.error(btn.text+errors[0-parseInt(state)]);
			                }
						});
					}
				}
			}]
		);
		if(type[btn.itemId]!='2'){
			Ext.Ajax.request({
				url:cfg.getCtx()+'/site/company/get/money?departId='+entityId,
				success:function(response, opts){
					var obj = Ext.decode(response.responseText),
						label = win.down('label#maxMoney'),
						money = label.previousSibling();
					label.setHtml('可用余额：'+util.moneyFormat(obj.message||'','f14 orange-color'));
					if(obj.message){
						money.setMaxValue(parseFloat(obj.message));
					}
					win.show();
				}
			});
		}else{
			win.show();
		}
	},
	internetPay :function(btn){
		var me = this,grid = me.getAccountGrid(),
			info = me.getBaseInfo();
		var win = util.createEmptyWindow(
			btn.text,
			util.glyphToFont(btn.glyph),300,150,[
				{
		        	fieldLabel:'充值金额',
		        	width:180,
		        	margin:'10 0 0 0',
		        	name : 'MONEY',
					xtype: 'numberfield',
					allowBlank:false
			    }
			],[{
				text:'去充值',
				disabled: true,
				formBind: true,
				cls:'orange',
				handler:function(btn){
					btn.disable();
					var money = win.down('numberfield').getValue();
					document.location.href = cfg.getCtx()+'/account/recharge?paramId='+entityId+'&money='+money;
					return;
				}
			}]
		).show();
	},
	toggleOpen :function(btn){
		var me = this;
		var a = "1";
		if(btn.itemId=='tyaccount'){
			a="1";
		}else if(btn.itemId=='qyaccount'){
			a="0";
		}
		Ext.Ajax.request({
			url:cfg.getCtx()+'/account/updateState?DEPART_ID='+entityId+"&STATE="+a,
			success:function(response, opts){
				util.success('保存数据成功');
				me.loadBaseInfo(me.getBaseInfo());
			}
		});
	}
});