Ext.define('app.view.site.companyWapTJGrid', {
	extend:'Ext.grid.Panel',
	xtype:'companyWapTJGrid',
	loadMask: true,
	border:true,
	emptyText: '没有找到数据',
	config:{
		categoryId:null,
		//companyId:null,
		url:null
	},
    updateCategoryId:function(categoryId){
    	if(categoryId){
	    	this.getStore().getProxy().setUrl(cfg.getCtx()+'/site/company/list/web/recommend?isRecommend=1&categoryId='+categoryId);
	    	this.getStore().load();
    	}
    },
	initComponent: function() {
		var me = this;
		this.store = util.createGridStore(me.getUrl()||cfg.getCtx()+'/site/company/list/web/recommend?isRecommend=1&categoryId='+me.getCategoryId(),Ext.create('Ext.data.Model',{
			fields: [
			         'ID','RECOMMEND_ID','NO','THEMES','ROUTE_WAP_PRICE', 'TITLE','ROUTE_ID',
			         'RECOMMEND_TYPE','ORDER_BY','COMPANY_ID','USER_ID','CREATE_TIME',
			         'DAY_COUNT','ATTR','FACE'
			]
		}));
		
        var columns = [{
			text: '线路图片',
        	dataIndex:'FACE',
        	width:145,
	        renderer: function(v,metaData,r,colIndex,store,view){
	        	v = cfg.getPicCtx()+'/'+v;//util.pathImage(v,'125X70');
	        	var /*arr = ['','周边','国内','出境'],
	        		cls = ['#427fed','#53a93f','#427fed','#dd4b39'],*/
	        	 	h = [
		        		'<img src="'+v+'" width="125px" height="70px"/>'
		        	],dc = r.get('DAY_COUNT') || '',attr = r.get('ATTR') || '';
		        dc = dc==''?'':dc+'日';
		        if(attr!=''){
		        	h.push('<span style="position:absolute;top:5px;left:10px;background:'+cfg.getRouteAttrTagColor()[attr]+';color:#fff;padding:2px 4px;">'+attr+dc+'</span>');
		        }
		        h.push('<div class="ht16" style="color:#666;">编号: <a href="javascript:;">'+r.get('NO')+'</a></div>');
	        	return h.join('');
	        }
		},{
        	text: '线路名称',
        	dataIndex:'TITLE',
        	flex:1,
        	renderer: function(v,metaData,r,ri,c,store,view){
        		setTimeout(function() {
	                var row = view.getRow(r);
	                if(row){
	                //Capturing the el (I need the div to do the trick)
	                var el = Ext.fly(Ext.fly(row).query('.x-grid-cell')[c]).down('div');
	                var cellWidth = el.getWidth();
	                var wraps = el.query('.d-wrap',false);
	                Ext.each(wraps,function(w,i){
	                	w.setWidth(cellWidth-50);
	                });
	                }
	                //All other codes to build the progress bar
	            }, 50);
        		var s = r.get('IS_TOP')=='1'?'<i class="iconfont orange-color" data-qtip="精彩推荐" style="display:block;margin-top:8px;">&#xe605;</i> ':'';
        		var h = [];
        		h.push('<div style="position:absolute;top:10px;right:0px;padding-left:6px;width:30px;border-left:1px dashed #999;">');
	        	h.push('<i class="iconfont info" data-qtip="点击查看线路详情" style="color:#427fed;cursor:pointer;display:block;">&#xe635;</i>');
	        	h.push(s);
	        	h.push('</div>');
	        	h.push('<a class="f14 d-wrap" href="javascript:;" style="line-height:20px;">'+v+'</a>');
        		h.push('<div class="ht20"><span class="money-color f16"><dfn>￥</dfn>'+util.format.number(r.get('ROUTE_WAP_PRICE')||0,'0.00')+'</span></div>');
        		return h.join('');
        	}
        },{
        	width:150,
        	text:'属性/主题',
        	dataIndex:'THEMES',
        	renderer:function(v,c,r){
        		var arr = ['','周边','国内','出境'],
	        		cls = ['d-tag-teal','d-tag-green','d-tag-blue','d-tag-red'],vstr='';
        		v = v || '';
        		if(v!=''){
        		var vs = v.split(',');
        		Ext.each(vs,function(s,i){
        			if(i%2==0){
        				vstr+='<p class="ht22">';
        			}
        			vstr+='<span class="d-tag-radius">'+s+'</span>';
        			if(i%2!=0){
        				vstr+='</p>';
        			}
        		});
        		}
	        	return [
	        		'<div class="ht25 f14" ><a class="d-tag '+cls[r.get('TYPE')]+' d-tag-noarrow">'+arr[r.get('TYPE')]+'游</a></div>',
	        		'<div style="margin-top:5px;">'+vstr+'</div>'
	        	].join('');
	        }
        },{
        	text:'最近日期/价格',
        	width:120,
	        dataIndex: 'SUM_PRICE',
	        renderer:function(v,c,r){
	        	var re = [],isFullPrice = r.get('IS_FULL_PRICE')||'0';
	        	
	        	/*re.push('<div style="position:absolute;top:10px;right:0px;padding-left:6px;width:30px;border-left:1px dashed #999;">');
	        	
	        	if(r.get('RQ')){
	        		re.push('<i class="iconfont pp disable-color f20" style="margin-bottom:8px;display:block;" data-qtip=\'同行价：'+util.moneyFormat(r.get('SUM_INTER_PRICE'),'f16')+'\'>&#xe65c;</i>');
	        		if(isFullPrice=='1'){
	        			re.push('<i class="iconfont pp f20" data-qtip="打包价详情" style="font-weight:bold;display:block;color:#427fed;cursor:pointer;margin-bottom:8px;">&#xe658;</i>');
	        		}else{
	        			re.push('<i class="iconfont pp f20" data-qtip="综合报价详情" style="display:block;color:#427fed;cursor:pointer;margin-bottom:8px;">&#xe65f;</i>');
	        		}
	        	}else{
	        		re.push('<i class="iconfont pp disable-color f20" style="margin-bottom:8px;display:block;" data-qtip=\''+util.moneyFormat(r.get('ROUTE_INTER_PRICE'),'f14 blue-color')+'\'>&#xe65c;</i>');
	        	}
	        	if(isFullPrice=='0'){
	        		re.push('<i class="iconfont dd f20" data-qtip="地接报价详情" style="display:block;color:#427fed;cursor:pointer;margin-bottom:8px;">&#xe65d;</i>');
	        	}
	        	re.push('<i class="iconfont dfc disable-color f20" data-qtip="单房差：<br>外卖：<span class=\'money-color f16\'><dfn>￥</dfn>'+r.get('RETAIL_SINGLE_ROOM')+'</span><br>同行：<span class=\'money-color f14\'><dfn>￥</dfn>'+r.get('INTER_SINGLE_ROOM')+'</span>" style="display:block;">&#xe65e;</i>');
	        	re.push('</div>');
	        	*/
	        	if(r.get('RQ')){
	        		re.push('<div class="ht20 f14" style="margin-bottom:5px;">'+(r.get('RQ')||'')+'</div>');
	        		re.push('<div class="ht20">'+util.moneyFormat(v)+'</div>');
	        	}else{
	        		re.push('<div class="ht20">'+util.moneyFormat(r.get('ROUTE_PRICE'))+'</div>');
	        		
	        	}
	        	return re.join('');
	        }
        },{
        	text:'手动排序',
        	width:80,
        	hidden:me.getUrl()!=null,
        	dataIndex:'ORDER_BY',
        	editor :{
        		value:0,
        		minValue:0,
        		maxValue:999,
        		xtype:'numberfield'
        	}
        },{
        	text:'推荐排序',
        	width:80,
        	hidden:me.getUrl()!=null,
        	dataIndex:'TOP_ORDER_BY',
        	editor :{
        		value:0,
        		minValue:0,
        		maxValue:999,
        		xtype:'numberfield'
        	}
        },{
        	xtype: 'widgetcolumn',
        	width:100,
        	hidden:me.getUrl()==null,
        	text:'',
        	sortable: false,
            menuDisabled: true,
            widget:{
        		xtype:'container',
        		layout:'column',
        		items:[{
        			xtype:'button',
            		text:'选择',
                    cls:'red',
                    width:83,
                    myurl:me.getUrl(),
                    style:'margin-top:5px;',
                    handler: function(b) {
                    	b.disable();
                    	var sel = b.up('container').getWidgetRecord();
                    	if(b.myurl.indexOf('?')!=-1){
                    		b.myurl = b.myurl.split('?')[1];
                    	}
                    	var o = Ext.Object.fromQueryString(b.myurl);
                    	Ext.Ajax.request({
    						url:cfg.getCtx()+'/site/company/save/web/recommend?categoryId='+o.categoryId,
    						params:{models:Ext.encode([sel.data])},
    						success:function(response, opts){
    							var obj = Ext.decode(response.responseText),
    								errors = ['数据推荐失败'],
    								state = obj?obj.statusCode:0;
    							if(!obj.success){
    								util.error(errors[0-parseInt(state)]);
    							}else{
    								util.success("数据推荐成功!");
    								me.getStore().load();
    							}
    						}
    					});
                    }
        		},{
        			xtype:'button',
        			margin:'3 0 0 0',
        			ui:'default-toolbar',
        			glyph:'xe67a@iconfont',
        			text:'联系卖家',
        			handler:function(btn){
        				var rec = btn.up('container').getWidgetRecord();
        				Ext.create('app.view.resource.contactWin',{
        					proId:rec.get('ID'),
        					viewShow:'view'
        				}).show();
        			}
        		}]
            }
        }];
        this.columns = columns;
        if(!me.getUrl()){
        	this.plugins=[new Ext.grid.plugin.CellEditing({
                clicksToEdit: 1,
                listeners:{
                	edit:function(a,b){
                		b.record.commit();
                		Ext.Ajax.request({
							url:cfg.getCtx()+'/site/company/upadte/web/recommend/order?field='+b.field+'&id='+b.record.get('RECOMMEND_ID')+'&orderBy='+b.value,
							success:function(response, opts){
								me.getStore().load();
							}
						});
                	}
                }
            })];
	        this.dockedItems = [{
	        	xtype:'toolbar',
	        	style:'padding-left:5px;',
	        	layout: {
	                overflowHandler: 'Menu'
	            }/*,
	        	items:[{
	        		text:'线路推荐',
	        		xtype:'splitbutton',
	        		menu: new Ext.menu.Menu({
	        	        items: [{
	        	        	text:'主题推荐',
	        	        	value:1
	        	        },{
	        	        	text: '首页推荐',
	        	        	value:2
	        	        },{
	        	        	text:'国内游推荐',
	        	        	value:3
	        	        },{
	        	        	text:'出境游推荐',
	        	        	value:4
	        	        },{
	        	        	text:'海岛度假推荐',
	        	        	value:5
	        	        },{
	        	        	text:'周边游推荐',
	        	        	value:6
	        	        },{
	        	        	text:'自由行推荐',
	        	        	value:7
	        	        },{
	        	        	text:'包机邮轮推荐',
	        	        	value:8
	        	        }],
	        	        listeners:{
	        	        	click:function(menu,item){
	        	        		var win = Ext.create('Ext.window.Window',{
	            		   			title: util.windowTitle('',item.text,''),
	            		   			width:850,
	            		   			height:400,
	            		   			draggable:false,
	            					resizable:false,
	            					modal:true,
	            		   			bodyStyle:'background:#fff;padding:1px;font-size:16px;color:#bbb;',
	            		   			layout:'fit',
	            		   			items:[Ext.create('app.view.site.companyWapTJGrid',{
	            		   				companyId:me.getCompanyId(),
	            		   				//parentId:me.getParentId(),
	            		   				url:cfg.getCtx()+'/site/company/list/web/recommend?recommendType='+item.value+'&companyId='+me.getCompanyId()
	            		   			})],
	            		   			buttons:[{
	            		   				text:'确认',
	            		   				handler:function(btn){
	            		   					var g = btn.up('window').down('gridpanel');
	            		   					var sels,models=[];
	            		   		        	if(sels = util.getMultiModel(g)){
	            		   		        		for (var i = 0; i < sels.length; i++) { 
	            		   							models.push(sels[i].data);
	            		   						}
	            		   		        	}
	            		   		        	Ext.Ajax.request({
	            								url:cfg.getCtx()+'/site/company/save/web/recommend?recommendType='+item.value+'&companyId='+me.getCompanyId()+'&parentId='+me.getParentId(),
	            								params:{models:Ext.encode(models)},
	            								success:function(response, opts){
	            									win.close();
	            									var obj = Ext.decode(response.responseText),
	            										errors = ['数据推荐失败'],
	            										state = obj?obj.statusCode:0;
	            									if(!obj.success){
	            										util.error(errors[0-parseInt(state)]);
	            									}else{
	            										util.success("数据推荐成功!");
	            										me.getStore().load();
	            									}
	            								}
	            							});
	            		   					
	            		   				}
	            		   			}]
	        	        		}).show();
	        	        	}
	        	        }
	        	    })
	        	},{
	        		text:'删除',
	        		handler:function(btn){
	        			util.delGridModel(btn.up('gridpanel'),'/site/company/del/web/recommend');
	        		}
	        	}]*/
	        }];
        }else{
        	//this.bbar = util.createGridBbar(this.store);
        }
        this.bbar = util.createGridBbar(this.store);
		this.callParent();
	},
	columnLines: true,
	selType:'checkboxmodel',
	selModel:{mode:'MULTI'}
});