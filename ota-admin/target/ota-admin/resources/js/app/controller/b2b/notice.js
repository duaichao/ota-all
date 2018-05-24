Ext.define('app.controller.b2b.notice', {
	extend: 'app.controller.common.BaseController',
	views: ['Ext.ux.form.ExtKindEditor'],
	config: {
		control: {
			'gridpanel[itemId=basegrid]':{
				selectionchange: function(view, records) {
					var grid = this.getNoticeGrid(),tbar = grid.down('toolbar');
					for(var i=0;i<tbar.items.length;i++){
						var item = tbar.items.get(i);
						if(item.config.xtype=='button'){
							item.setDisabled('admin'==cfg.getUser().username);
						}
					}
	            },
	            cellclick:function( view, td, cellIndex, record, tr, rowIndex, e, eOpts ){
					var sel = record,getUrl='';
					if(cellIndex==2){
						
						/*var tpl = new Ext.XTemplate(
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
							params:{ID:sel.get('ID')},
							success:function(response){
								var data = Ext.decode(response.responseText).data,
									d = data[0];
								Ext.create('Ext.window.Window',{
									title:util.windowTitle('',''+sel.get('TITLE'),'icon-megaphone-1'),
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
						});*/
					}
				}
			},
			'gridpanel[itemId=basegrid] searchfield':{
	        	afterrender:'onSearchFieldRender'
	        },
	        'toolbar button[itemId=add]': {
	        	 afterrender:'onBtnRender',
	             click: 'onAdd'
	         },
	         'toolbar button[itemId=add] menu':{
	        	click:'onAddMenuClick'
	         },
	         'toolbar button[itemId=edit]': {
	             click: 'onEdit'
	         },
	         'toolbar button[itemId=del]': {
	             click: 'onDel'
	         },
	         'toolbar button[itemId=top]': {
	             click: 'onTop'
	         }
		},
		refs: {
            noticeGrid: 'gridpanel[itemId=basegrid]'
        }
	},
	onBtnRender :function(btn){
		Ext.Ajax.request({
		    url: cfg.getCtx()+'/site/company/ownerSites',
		    success: function(response, opts) {
		        var obj = Ext.decode(response.responseText),
		        	items = obj.data,
		        	menus = [];
		        for(var i=0;i<items.length;i++){
		        	menus.push({
		        		id:items[i].CITY_ID,
		        		text:items[i].SUB_AREA
		        	});
		        }
		        btn.setMenu(menus);
		    }
		});
	},
	onSearchFieldRender:function(s){
		var tbar = s.up('toolbar'),
			types = [
				{text:'全部',value:''},
		        {text:'B2B',value:0},
		        {text:'B2C',value:1}
		        
	        ],store = tbar.up('grid').getStore(),filter;
		tbar.add({
			width:80,
			margin:'0 0 0 5',
			xtype:'combo',
			forceSelection: true,
			emptyText:'类型',
            valueField: 'value',
            displayField: 'text',
            store:util.createComboStore(types),
            minChars: 0,
            value:'',
            queryMode: 'local',
            typeAhead: true,
            listeners:{
            	change:function(c, newValue, oldValue, eOpts ){
			       store.getProxy().setExtraParams({type:newValue});
			       store.load();
            	}
            }
		});
	},
	createSaveWindow:function(title,btn){
		var types = [
		        {text:'B2B',value:0},
		        {text:'B2C',value:1}
		        
	        ],me= this,win = Ext.create('Ext.window.Window',{
			title:util.windowTitle(util.glyphToFont(btn.glyph||'xe62b@iconfont'),title,''),
			width:'85%',
			height:'85%',
			modal:true,
			draggable:false,
			resizable:false,
			maximizable:false,
   			layout:'fit',
			items:[{
				xtype:'form',
    			scrollable:false,
				fieldDefaults: {
			        labelAlign: 'right',
			        labelWidth: 0,
			        msgTarget: 'side'
			    },
				bodyPadding: 10,
				defaults:{hideLabel:true},
			    items:[{
			    	xtype:'hidden',name:'pm[CITY_ID]',
			    	value:btn.id
			    },{
			    	xtype:'hidden',name:'pm[CITY_NAME]',
			    	value:btn.text
			    },{xtype:'hidden',name:'pm[ID]'},{
			    	xtye:'fieldcontainer',
			    	layout: 'hbox',
			    	anchor:'100%',
			    	items:[{
			    		width:80,
						xtype:'combo',
						forceSelection: true,
						emptyText:'类型',
			            valueField: 'value',
			            displayField: 'text',
			            store:util.createComboStore(types),
			            minChars: 0,
			            value:0,
			            name:'pm[TYPE]',
			            hiddenName:'pm[TYPE]',
			            queryMode: 'local',
			            typeAhead: true
			    	},{
			    		margin:'0 0 0 5',
			    		flex:1,
			    		name:'pm[TITLE]',
			    		emptyText:'标题',
			    		xtype:'textfield',
				    	allowBlank: false
			    	}]
			    },{
			    	fieldLabel: '内容',
			    	name: 'CONTENT',
			    	xtype: 'extkindeditor',
			    	anchor:'100% 100%'
			    }],
				buttons: [{
			        text: '保存',
			        itemId:'save',
			        disabled: true,
			        formBind: true,
			        handler:function(btn){
			        	var form = btn.up('form'),
			        		win = form.up('window');
			        	me.onSave(form,win);
			        }
			    }]
			}]
		});
		return win;
	},
	onSave:function(f,win){
		var me = this,form = f.getForm(),ke=f.down('extkindeditor');
		
		var btn = win.down('button#save');
		if (ke.getRawValue()!=''&&form.isValid()) {
			btn.disable();
            form.submit({
            	submitEmptyText:false ,
            	
            	url:cfg.getCtx()+'/b2b/notice/save',
                success: function(form, action) {
                   util.success('保存新闻公告成功');
                   me.getNoticeGrid().getStore().load();
                   win.close();
                },
                failure: function(form, action) {
                	var state = action.result?action.result.statusCode:0,
                		errors = ['保存异常','当前用户没有权限'];
                    util.error(errors[0-parseInt(state)]);
                    win.close();
                }
            });
        }else{
        	util.alert('信息填写不完整');
        }
	},
	onAdd:function(btn){
		util.alert('点击按钮箭头选择一个城市');
	},
	onAddMenuClick :function(btn,item){
		this.createSaveWindow('添加新闻公告',item).show();
	},
	onEdit:function(btn){
		var rg = this.getNoticeGrid(),
			win,form,sel,selData,formData={},basic;
		if(sel = util.getSingleModel(rg)){
			win = this.createSaveWindow('修改新闻公告',btn);
			form = win.down('form');
			basic = form.getForm();
			win.show();
			win.mask('数据加载中...');
			formData = util.pmModel(sel);
			basic.setValues(formData);
			Ext.Ajax.request({
				url:cfg.getCtx()+'/b2b/notice/detail',
				params:{ID:sel.get('ID')},
				success:function(response){
					var data = Ext.decode(response.responseText).data,
						d = data[0];
					form.down('extkindeditor').setValue(d.CONTENT);
					win.unmask();
				}
			});
		}
	},
	onDel:function(){
		util.delGridModel(this.getNoticeGrid(),'/b2b/notice/del');
	},
	onTop:function(btn){
		util.switchGridModel(this.getNoticeGrid(),'/b2b/notice/top');
	}
});