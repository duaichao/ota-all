Ext.define('app.view.b2c.selectRoutes', {
	extend:'Ext.grid.Panel',
	xtype:'selectRoutes',
	loadMask: true,
	border:true,
	emptyText: '没有找到数据',
	config:{
		storeUrl:null,
		saveUrl:null
	},
	initComponent: function() {
		var me = this;
		this.store = util.createGridStore(me.getStoreUrl(),Ext.create('Ext.data.Model',{
			fields: [
			         'ID','RECOMMEND_ID','NO','THEMES','ROUTE_WAP_PRICE', 'TITLE','ROUTE_ID',
			         'RECOMMEND_TYPE','ORDER_BY','COMPANY_ID','USER_ID','CREATE_TIME',
			         'DAY_COUNT','ATTR','FACE'
			]
		}));
		var supplyStore = util.createGridStore(cfg.getCtx()+'/b2c/route/select/supply',Ext.create('Ext.data.Model',{
			fields: [
			         'SALE_COMPANY','SALE_COMPANY_ID','SALE_BRAND_NAME'
			]
		}));
		this.dockedItems = [{
			labelWidth:72,
			labelAlign:'right',
    		xtype:'checkboxgroup',
			fieldLabel:'主题属性',
			layout:'auto',
			defaults:{cls:'inline_checkbox',name:'themes'},
			items: [
                {boxLabel: '沙漠', inputValue:'沙漠'},
                {boxLabel: '美食', inputValue:'美食'},
                {boxLabel: '宗教', inputValue:'宗教'},
                {boxLabel: '游学', inputValue:'游学'},
                {boxLabel: '探险', inputValue:'探险'},
                {boxLabel: '海岛度假', inputValue:'海岛度假'},
                {boxLabel: '夏令营', inputValue:'夏令营'},
                {boxLabel: '漂流', inputValue:'漂流'},
                {boxLabel: '邮轮', inputValue:'邮轮'},
                {boxLabel: '夕阳红', inputValue:'夕阳红'},
                {boxLabel: '温泉', inputValue:'温泉'},
                {boxLabel: '古镇', inputValue:'古镇'},
                {boxLabel: '滑雪', inputValue:'滑雪'},
                {boxLabel: '红色', inputValue:'红色'},
                {boxLabel: '专列', inputValue:'专列'},
                {boxLabel: '门票', inputValue:'门票'},
                {boxLabel: '度假', inputValue:'度假'},
                {boxLabel: '亲子', inputValue:'亲子'},
                {boxLabel: '蜜月', inputValue:'蜜月'},
                {boxLabel: '青年会', inputValue:'青年会'}
            ],
            listeners:{
            	change:function(rg, newValue, oldValue, eOpts ){
            		var themes=[];
            		if(Ext.isArray(newValue.themes)){
            			Ext.Array.each(newValue.themes,function(o,i){
            				themes.push({'value':o});
            			});
            		}else{
            			themes.push({'value':newValue.themes});
            		}
            		me.getStore().getProxy().setExtraParam('routeThemes',Ext.encode(themes));
            		me.getStore().load();
            	}
            }
    	},{
			xtype:'toolbar',
			style:'padding:0 0 0 7px;border:none;',
			items:[{
				labelWidth:65,
				labelAlign:'right',
	    		xtype:'radiogroup',
				fieldLabel:'线路属性',
				layout:'auto',
				defaults:{cls:'inline_checkbox',name:'attrs'},
				items: [
				    {boxLabel: '全部', inputValue:'',checked:true},
	                {boxLabel: '品质', inputValue:'品质'},
	                {boxLabel: '豪华', inputValue:'豪华'},
	                {boxLabel: '自由行', inputValue:'自由行'},
	                {boxLabel: '特价', inputValue:'特价'},
	                {boxLabel: '包机', inputValue:'包机'},
	                {boxLabel: '纯玩', inputValue:'纯玩'}
	            ],
	            listeners:{
	            	change:function(rg, newValue, oldValue, eOpts ){
	            		me.getStore().getProxy().setExtraParam('routeAttrs',newValue);
	            		me.getStore().load();
	            	}
	            }
			}]
		},{
			xtype:'toolbar',
			style:'padding:0 0 10px 7px;border:none;',
			items:[{
				labelWidth:65,
				labelAlign:'right',
				fieldLabel:'供应商',
				xtype:'combo',
				forceSelection: true,
				editable:false,
	            valueField: 'value',
	            displayField: 'text',
	            store:util.createComboStore([{
	            	text:'全部',
	            	value:''
	            },{
	            	text:'国内',
	            	value:1
	            },{
	            	text:'出境',
	            	value:2
	            }]),
	            width:145,
	            minChars: 0,
	            value:'',
	            queryMode: 'local',
	            typeAhead: true,
	            listeners:{
	            	change:function(c, newValue, oldValue, eOpts ){
	            		var c1 = c.nextSibling();
	            		c1.getStore().getProxy().setExtraParam('type',newValue);
	            		c1.getStore().load();
	            	}
	            }
			},{
				xtype:'combo',
				width:240,
				minChars: 1,
			    queryParam: 'supplyName',
			    displayField: 'SALE_COMPANY',
			    pageSize:20,
			    store:supplyStore,
			    forceSelection:true,
			    multiSelect:false,
				autoSelect:false,
			    queryMode: 'remote',
			    triggerAction: 'all',
			    valueField: 'SALE_COMPANY_ID',
			    emptyText:'全部供应商',
			    listConfig:{
			    	minWidth:360,
			    	itemTpl:[
						 '<tpl for=".">',
				            '<li class="city-item">{SALE_COMPANY}<span>{SALE_BRAND_NAME}</span></li>',
				        '</tpl>'
					]
			    },
			    listeners:{
			    	change:function(c, newValue, oldValue, eOpts ){
	            		me.getStore().getProxy().setExtraParam('supplyId',newValue);
	            		me.getStore().load();
	            	}
			    }
			},{
				glyph:'xe63d@iconfont',
				tool:'清空重新选择供应商',
				handler:function(b){
					b.previousSibling().reset();
					b.previousSibling().previousSibling().reset();
					me.getStore().getProxy().setExtraParam('supplyId','');
            		me.getStore().load();
				}
			},'->',{
   				xtype:'combo',
				forceSelection: true,
				editable:false,
	            valueField: 'value',
	            displayField: 'text',
	            store:util.createComboStore([{
	            	text:'全部',
	            	value:''
	            },{
	            	text:'周边游',
	            	value:1
	            },{
	            	text:'国内游',
	            	value:2
	            },{
	            	text:'出境游',
	            	value:3
	            }]),
	            width:75,
	            minChars: 0,
	            value:'',
	            queryMode: 'local',
	            typeAhead: true,
	            listeners:{
	            	change:function(c, newValue, oldValue, eOpts ){
	            		me.getStore().getProxy().setExtraParam('routeType',newValue);
	            		me.getStore().load();
	            	}
	            }
   			},{
		   			emptyText:'产品标题/编号/目的地',
		        	xtype:'searchfield',
		        	store:me.getStore(),
		        	width:200
			}]
		}];
        var columns = [{
			text: '线路图片',
        	dataIndex:'FACE',
        	width:145,
	        renderer: function(v,metaData,r,colIndex,store,view){
	        	v = cfg.getPicCtx()+'/'+v;
	        	var 
	        	 	h = [
		        		'<img src="'+v+'" width="125px" height="70px"/>'
		        	],dc = r.get('DAY_COUNT') || '',attr = r.get('ATTR') || '';
		        dc = dc==''?'':dc+'日';
		        if(attr!=''){
		        	h.push('<span class="flash" style="position:absolute;top:5px;left:10px;border-radius:0px;background:'+cfg.getRouteAttrTagColor()[attr]+';color:#fff;padding:2px 5px;">'+attr+dc+'</span>');
		        }
		        h.push('<div class="ht16" style="color:#666;padding-top:5px;">编号: <a href="javascript:;">'+r.get('NO')+'</a></div>');
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
        		var h = [];
	        	h.push('<a class="f16 d-wrap title" href="javascript:;" style="display:inline-block;line-height:24px;padding:5px 0 10px 0;">'+v+'</a>');
        		
	        	//标签
	        	h.push('<div class="product-tag">');
        		if(r.get('BRAND_NAME')){
        			h.push('<span class="flash flash-success"><i  class="iconfont f16" style="top:0px;">&#xe6b2;</i> '+r.get('BRAND_NAME')+'</span>');
        		}
        		if(r.get('TYPE')){
        			var routArrs = ['','周边','国内','出境'];
        			h.push('<span class="flash">'+routArrs[r.get('TYPE')]+'游</span>');
        		}
        		if(r.get('IS_TOP')=='1'){
        			h.push('<span class="flash flash-error"><i  class="iconfont f14" style="top:0px;">&#xe605;</i> 精彩推荐</span>');
        		}
        		
        		
        		h.push('</div>');
        		
        		
	        	h.push('<div class="ht20"><span>网站价：</span><span class="money-color f16"><dfn>￥</dfn>'+util.format.number(r.get('ROUTE_WAP_PRICE')||0,'0.00')+'</span></div>');
        		return h.join('');
        	}
        },{
        	width:150,
        	text:'主题属性',
        	dataIndex:'THEMES',
        	renderer:function(v,c,r){
        		v = v || '';
        		var vstr='';
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
 	        	return '<div style="margin-top:5px;">'+vstr+'</div>';
	        }
        },{
        	xtype: 'widgetcolumn',
        	width:100,
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
                    myStoreUrl:me.getStoreUrl(),
                    mySaveUrl:me.getSaveUrl(),
                    style:'margin-top:5px;',
                    handler: function(b) {
                    	b.disable();
                    	var sel = b.up('container').getWidgetRecord(),o={};
                    	if(b.myStoreUrl.indexOf('?')!=-1){
                    		b.myStoreUrl = b.myStoreUrl.split('?')[1];
                    		o = Ext.Object.fromQueryString(b.myStoreUrl);
                    	}
                    	Ext.Ajax.request({
    						url:b.mySaveUrl,//+'?categoryId='+o.categoryId+'&parentId='+o.parentId,
    						params:{models:Ext.encode([sel.data])},
    						success:function(response, opts){
    							b.enable();
    							var obj = Ext.decode(response.responseText),
    								errors = ['数据选取失败'],
    								state = obj?obj.statusCode:0;
    							if(!obj.success){
    								util.error(errors[0-parseInt(state)]);
    							}else{
    								util.success("数据选取成功!");
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
        				/*Ext.create('Ext.window.Window',{
        					title:util.windowTitle('&#xe67a;','联系卖家',''),
        					width:300,
        					height:180,
        					modal:true,
        					bodyPadding:10,
							draggable:false,
							resizable:false,
							maximizable:false,
				   			layout:'fit',
        					items:[{
        						tpl:[
        							'<h3 class="f16" style="width:270px;white-space:normal;margin-bottom:5px;line-height:20px;">{COMPANY_NAME}</h3>',
        							'<div class="ht16">联系：{PORDUCE_CONCAT}</div>',
        							'<div class="ht16">手机：{PRODUCE_MOBILE}</div>',
        							'<div class="ht16">电话：{PHONE}</div>'
        						],
        						data:rec.data
        					}]
        				}).show();*/
        			}
        		}]
            }
        }];
        this.columns = columns;
        this.bbar = util.createGridBbar(this.store);
		this.callParent();
		this.on('cellclick',this.detailWindow,this);
	},
	columnLines: true,
	selType:'checkboxmodel',
	selModel:{mode:'MULTI'},
	detailWindow :function(view, td, cellIndex, record, tr, rowIndex, e, eOpts){
		if(e.target.tagName=='A'&&e.target.className.indexOf('title')!=-1){
			var win = Ext.create('Ext.window.Window',{
				title:util.windowTitle('&#xe635;','线路详情',''),
				bodyPadding:5,
				modal:true,
				draggable:false,
				resizable:false,
				maximizable:false,
	   			layout:'fit',
	   			items:[{
	   				xtype:'routedetail'
	   			}]
			});
			win.show().maximize();
			var rd = win.down('routedetail');
			rd.setRouteId(record.get('ID'));
		}
	}
});