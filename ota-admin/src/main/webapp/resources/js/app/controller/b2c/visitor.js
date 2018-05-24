Ext.define('app.controller.b2c.visitor', {
	extend: 'app.controller.common.BaseController',
	config: {
		control: {
			 'toolbar button#export': {
	             click: 'onExport'
	         }
		},
		refs: {
			visitorGrid: 'gridpanel[itemId=basegrid]'
        }
	},
	onBaseGridRender:function(g){
		var me = this;
		setTimeout(function(){
		Ext.Ajax.request({
		    url: util.getPowerUrl(),
		    success: function(response, opts) {
		        var obj = Ext.decode(response.responseText),
		        	items = obj.children,
		        	ubtn = [],
		        	rbtn = [];
		        for(var i=0;i<items.length;i++){
		        	if(items[i].itemId == 'setrole'){
		        		rbtn.push(items[i]);
		        	}else{
		        		ubtn.push(items[i]);
		        	}
		        }
		        ubtn.push('->');
		        var attrMenus = Ext.create('Ext.menu.Menu',{
		        	items:[{
                        text: '全部属性',
                        group: 'attr',
                        value:'',
                        checked: true
                    },{
                        text: '品质',
                        value:'品质',
                        group: 'attr',
                        checked: false
                    },{
                        text: '豪华',
                        value: '豪华',
                        group: 'attr',
                        checked: false
                    },{
                        text: '自由行',
                        value: '自由行',
                        group: 'attr',
                        checked: false
                    },{
                        text: '特价',
                        value: '特价',
                        group: 'attr',
                        checked: false
                    },{
                        text: '包机',
                        value: '包机',
                        group: 'attr',
                        checked: false
                    },{
                        text: '纯玩',
                        value: '纯玩',
                        group: 'attr',
                        checked: false
                    }]
		        });
		        var attrBtn = Ext.create('Ext.button.Button',{
		        	text:'线路属性',
		        	menu:attrMenus
		        });
		        attrMenus.on('click',function(menu, item){
		        	attrBtn.setText(item.text);
		        	var ep = {produceAttr:item.value};
        			Ext.applyIf(ep,g.getStore().getProxy().getExtraParams());
    				g.getStore().getProxy().setExtraParams(ep);
        			g.getStore().load();
	    		});
		        ubtn.push(attrBtn);
		        var ageMenus = Ext.create('Ext.menu.Menu',{
		        	items:[{
                        text: '全部年龄',
                        group: 'age',
                        value:'',
                        checked: true
                    },{
                        text: '0岁-17岁',
                        value:'0-17',
                        group: 'age',
                        checked: false
                    },{
                        text: '18岁-65岁',
                        value:'18-65',
                        group: 'age',
                        checked: false
                    },{
                        text: '65岁以上',
                        value:'65',
                        group: 'age',
                        checked: false
                    }]
		        });
		        var ageBtn = Ext.create('Ext.button.Button',{
		        	text:'年龄段',
		        	menu:ageMenus
		        });
		        ageMenus.on('click',function(menu, item){
		        	ageBtn.setText(item.text);
		        	var ep = {age:item.value};
        			Ext.applyIf(ep,g.getStore().getProxy().getExtraParams());
    				g.getStore().getProxy().setExtraParams(ep);
        			g.getStore().load();
	    		});
		        ubtn.push(ageBtn);
		        
		        var startDateField = Ext.create('Ext.form.field.Date',{
	        		width:100,
	        		emptyText:'开始日期',
	              	endDateField: 'enddt',
		            itemId:'startdt',
		            margin:'0 2 0 0',
		            showToday:false,
		            editable:false,
		            vtype: 'daterange',
	              	format:'Y-m-d'
		        });
	        	var endDateField = Ext.create('Ext.form.field.Date',{
	        		width:100,
	              	emptyText:'结束日期',
	              	margin:'0 0 0 0',
	              	format:'Y-m-d',
	              	editable:false,
	              	showToday:false,
	              	itemId:'enddt',
		            startDateField: 'startdt',
		            vtype: 'daterange'
		        });
		        ubtn.push(startDateField);
		        ubtn.push(endDateField);
		        ubtn.push({
	              	width:30,
	              	margin:'0 2 0 2',
	              	glyph:'xe61c@iconfont',
	              	tooltip:'查询',
	        		handler:function(btn){
	        			var endDate = btn.previousSibling(),
	        				startDate = endDate.previousSibling();
	        			startDate = Ext.Date.format(startDate.getValue(),'Y-m-d');
	        			endDate = Ext.Date.format(endDate.getValue(),'Y-m-d');
	        			var ep = {startDate:startDate,endDate:endDate};
	        			Ext.applyIf(ep,g.getStore().getProxy().getExtraParams());
        				g.getStore().getProxy().setExtraParams(ep);
            			g.getStore().load();
	        		}
	        	});
		        ubtn.push({
		        	margin:'0 10 0 0',
	              	glyph:'xe63d@iconfont',
	              	width:30,
	              	tooltip:'重置日期',
	              	handler:function(b){
	              		var endDate = b.previousSibling().previousSibling(),
	              			startDate = endDate.previousSibling();
	              		if(!endDate.getValue()&&!startDate.getValue()){return;}
	              		endDate.reset();
	              		startDate.reset();
	              		var ep = {startDate:'',endDate:''};
	              		Ext.applyIf(ep,g.getStore().getProxy().getExtraParams());
        				g.getStore().getProxy().setExtraParams(ep);
            			g.getStore().load();
	              		
	              	}
	        	});
		        
		        ubtn.push({
		        	emptyText:'游客姓名',
		        	xtype:'searchfield',
		        	store:g.getStore()
		        });
		        util.createGridTbar(g,ubtn);
		    }
		});
		},500);
	},
	onExport:function(btn){
		var grid = this.getVisitorGrid(),
			params = grid.getStore().getProxy().getExtraParams()||{},
			str = Ext.Object.toQueryString(params);
		util.downloadAttachment(cfg.getCtx()+'/b2c/visitor/export?'+str);
	}
});