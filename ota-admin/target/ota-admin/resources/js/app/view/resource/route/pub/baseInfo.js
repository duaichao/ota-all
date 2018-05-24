Ext.define('app.view.resource.route.pub.baseInfo', {
	extend: 'Ext.form.Panel',
	reader: {
        type: 'json',
        model:Ext.create('app.model.resource.route.model')
    },
	requires:['app.view.common.CityCombo'],
	xtype:'routebaseinfo',
	defaultType:'fieldcontainer',
	fieldDefaults: {
        labelWidth: 70,
        labelAlign:'right'
    },
    autoScroll:true,
    bodyPadding: 15,
    config:{
    	routeId:''
    },
    initComponent :function(){
    	var me = this;
    	var beginCityModel = Ext.create('Ext.data.Model',{
    		fields:['CITY_ID','CITY_NAME']
    	});
    	var beginCityStore = Ext.create('Ext.data.Store',{
    		model:beginCityModel,
		    autoLoad: true, 
		    proxy: {
		        type: 'ajax',
		        noCache:true,
		        url: cfg.getCtx()+'/site/company/list/start/city?companyId='+cfg.getUser().companyId+'&companyType='+cfg.getUser().companyType,
		        reader: {
		            type: 'json',
		            rootProperty: 'data',
		            totalProperty:'totalSize'
		        }
		   	}
    	})
    	
    	this.items = [{
    		xtype:'hidden',
    		name:'pm[ID]'
    	},{
			fieldLabel:'线路名称',
			layout: 'hbox',
			items:[{
				xtype:'combo',
				allowBlank:false,
				editable:false,
				emptyText:'类型',
				name:'pm[TYPE]',
				hiddenName:'pm[TYPE]',
				width:90,
				typeAhead: true,
	            triggerAction: 'all',
	            store: [
	                ['1','周边游'],
	                ['2','国内游'],
	                ['3','出境游']
	            ]
			},{
				xtype: 'splitter'
			},{
				xtype:'textfield',
				allowBlank:false,
				name:'pm[TITLE]',
				flex:1
			}]
		},/*{
			fieldLabel:'出发地',
			layout: 'hbox',
			items:[{
				width:150,
				xtype:'citycombo',
				listeners:{
					beforequery :function(queryPlan, eOpts){
						var combo = queryPlan.combo,
							store = combo.getStore();
						
						 // store.getProxy().setExtraParams({cityId:cfg.getUser().cityId}); 
						 
					},
					select :function(c, r, e){
						c.reset();
						var cg = c.nextSibling().nextSibling();
						if(cg.down('container#'+r.get('ID'))){
							util.alert('出发地重复');
							return;
						}
						cg.add({
							xtype:'container',
							margin:'0 2 0 0',
							itemId:r.get('ID'),
							items:[{
								xtype:'hidden',
								name:'BEGIN_CITY',
								value: r.get('ID')+','+r.get('TEXT')
							},{
								tooltip:r.get('TEXT'),
								cls:'tag-split-btn remove',
								xtype:'splitbutton',
								text:r.get('TEXT'),
								listeners:{
									arrowclick :function(sp){
										var zj = sp.up();
										zj.up('container[itemId=beginCityItems]').remove(zj,true);
									}
								}
							}]
						});
					}
				}
			},{
				xtype: 'splitter'
			},{
				flex:1,
				itemId:'beginCityItems',
				xtype:'container',
				layout:'hbox'
			}]
		}*/{
			fieldLabel:'出发地',
			layout: 'hbox',
			items:[{
				width:150,
				xtype:'combo',
				displayField: 'CITY_NAME',
			    forceSelection:true,
			    multiSelect:false,
				autoSelect:false,
			    queryMode: 'remote',
			    triggerAction: 'all',
			    valueField: 'CITY_ID',
			    emptyText:'选择出发城市',
			    store:beginCityStore,
			    listConfig:{
			    	emptyText:'<div style="padding-top:20px;padding-left:10px;">请检查站配置是否设置出发城市</div>',
			    	minWidth:360,
			    	minHeight:50,
			    	itemTpl:[
						 '<tpl for=".">',
				            '<li class="city-item">{CITY_NAME}</li>',
				        '</tpl>'
					]
			    },
				listeners:{
					select :function(c, r, e){
						c.reset();
						var cg = c.nextSibling().nextSibling();
						if(cg.down('container#'+r.get('CITY_ID'))){
							util.alert('出发地重复');
							return;
						}
						cg.add({
							xtype:'container',
							margin:'0 2 0 0',
							itemId:r.get('CITY_ID'),
							items:[{
								xtype:'hidden',
								name:'BEGIN_CITY',
								value: r.get('CITY_ID')+','+r.get('CITY_NAME')
							},{
								tooltip:r.get('CITY_NAME'),
								cls:'tag-split-btn remove',
								xtype:'splitbutton',
								text:r.get('CITY_NAME'),
								listeners:{
									arrowclick :function(sp){
										var zj = sp.up();
										zj.up('container[itemId=beginCityItems]').remove(zj,true);
									}
								}
							}]
						});
					}
				}
			},{
				xtype: 'splitter'
			},{
				flex:1,
				itemId:'beginCityItems',
				xtype:'container',
				layout:'hbox'
			}]
		},{
			layout: 'hbox',
			fieldLabel:'目的地',
			items:[{
				width:150,
				xtype:'citycombo',
				listeners:{
					select :function(c, r, e){
						c.reset();
						var cg = c.nextSibling().nextSibling();
						if(cg.down('panel#'+r.get('ID'))){
							util.alert('目的地重复');
							return;
						}
						cg.add({
							itemId:r.get('ID'),
							margin:'0 2 0 0',
							items:[{
								xtype:'hidden',
								name:'END_CITY',
								value: r.get('ID')+','+r.get('TEXT')
							},{
								tooltip:r.get('TEXT'),
								cls:'tag-split-btn remove',
								xtype:'splitbutton',
								text:r.get('TEXT'),
								listeners:{
									arrowclick :function(sp){
										var zj = sp.up();
										zj.up('container[itemId=endCityItems]').remove(zj,true);
									}
								}
							}]
						});
					}
				}
			},{
				xtype: 'splitter'
			},{
				flex:1,
				itemId:'endCityItems',
				xtype:'container',
				layout:'hbox'
			}]
		},{
			xtype:'checkboxgroup',
			fieldLabel:'主题属性',
			layout:'auto',
			defaults:{cls:'inline_checkbox'},
			items: [
                {boxLabel: '沙漠', inputValue:'沙漠', name: 'TAG_NAME_ZT'},
                {boxLabel: '美食', inputValue:'美食', name: 'TAG_NAME_ZT'},
                {boxLabel: '宗教', inputValue:'宗教', name: 'TAG_NAME_ZT'},
                {boxLabel: '游学', inputValue:'游学', name: 'TAG_NAME_ZT'},
                {boxLabel: '探险', inputValue:'探险', name: 'TAG_NAME_ZT'},
                {boxLabel: '海岛度假', inputValue:'海岛度假', name: 'TAG_NAME_ZT'},
                {boxLabel: '夏令营', inputValue:'夏令营', name: 'TAG_NAME_ZT'},
                {boxLabel: '漂流', inputValue:'漂流', name: 'TAG_NAME_ZT'},
                {boxLabel: '邮轮', inputValue:'邮轮', name: 'TAG_NAME_ZT'},
                {boxLabel: '夕阳红', inputValue:'夕阳红', name: 'TAG_NAME_ZT'},
                {boxLabel: '温泉', inputValue:'温泉', name: 'TAG_NAME_ZT'},
                {boxLabel: '古镇', inputValue:'古镇', name: 'TAG_NAME_ZT'},
                {boxLabel: '滑雪', inputValue:'滑雪', name: 'TAG_NAME_ZT'},
                {boxLabel: '红色', inputValue:'红色', name: 'TAG_NAME_ZT'},
                {boxLabel: '专列', inputValue:'专列', name: 'TAG_NAME_ZT'},
                {boxLabel: '门票', inputValue:'门票', name: 'TAG_NAME_ZT'},
                {boxLabel: '度假', inputValue:'度假', name: 'TAG_NAME_ZT'},
                {boxLabel: '亲子', inputValue:'亲子', name: 'TAG_NAME_ZT'},
                {boxLabel: '蜜月', inputValue:'蜜月', name: 'TAG_NAME_ZT'},
                {boxLabel: '青年会', inputValue:'青年会', name: 'TAG_NAME_ZT'}
            ]
		},{
			xtype:'radiogroup',
			fieldLabel:'线路属性',
			layout:'auto',
			defaults:{cls:'inline_checkbox'},
			items: [
                {boxLabel: '品质', inputValue:'品质', name: 'pm[ATTR]'},
                {boxLabel: '豪华', inputValue:'豪华', name: 'pm[ATTR]'},
                {boxLabel: '自由行', inputValue:'自由行', name: 'pm[ATTR]'},
                {boxLabel: '特价', inputValue:'特价', name: 'pm[ATTR]'},
                {boxLabel: '包机', inputValue:'包机', name: 'pm[ATTR]'},
                {boxLabel: '纯玩', inputValue:'纯玩', name: 'pm[ATTR]'},
                {boxLabel: '自驾游', inputValue:'自驾游', name: 'pm[ATTR]'}
            ]
		},{
			fieldLabel:'价格方式',
			labelStyle:'position:relative;',
			xtype:'fieldcontainer',
			itemId:'fullPriceCombo',
			layout:{
				type:'hbox'
			},
			anchor:'100%',
			items:[{
				xtype:'combo',
				itemId:'fullPriceComboCmp',
				allowBlank:false,
				editable:false,
				emptyText:'选择价格发布方式',
				name:'pm[IS_FULL_PRICE]',
				hiddenName:'pm[IS_FULL_PRICE]',
				width:240,
				typeAhead: true,
	            triggerAction: 'all',
	            store: [
	                ['0','地接、交通价格独立发布'],
	                ['1','地接、交通价格打包发布']
	            ],
	            listeners:{
	            	change:function(combo, newValue, oldValue, eOpts){
	            		if(newValue=='1'){
	            			combo.nextSibling().show();
	            			combo.nextSibling().nextSibling().show();
	            		}else{
	            			combo.nextSibling().hide();
	            			combo.nextSibling().nextSibling().hide();
	            		}
	            	}
	            }
			},{
		    	emptyText:'提前报名天数',
		    	itemId:'fullPriceDayCnt',
		    	hidden:true,
		    	maxValue:40,
		    	minValue:0,
		    	margin:'0 10 0 10',
		    	width:120,
		    	name: 'pm[TRAFFIC_END_DATE]',
		    	xtype:'numberfield'
		    },{
		    	emptyText:'提前报名时间',
		    	itemId:'fullPriceDayTime',
		    	width:220,
		    	hidden:true,
		    	name: 'pm[TRAFFIC_END_TIME]',
		    	xtype:'timefield',
		    	format:'H:i',
		    	minValue: '8:30',
    			maxValue: '18:30',
    			increment: 30
		    }]
		},{
			fieldLabel:'销售须知',anchor:'100%',
			name:'pm[NOTICE]',
			allowBlank:false,
			maxLength:200,
			height:80,
			xtype:'textarea'
		},{
			fieldLabel:'产品推荐',
			name:'pm[FEATURE]',
			allowBlank:false,
			maxLength:200,
			anchor:'100%',
			height:80,
			xtype:'textarea'
		},{
			fieldLabel:'产品特色',
			name:'pm[FEATURE1]',
			allowBlank:false,
			anchor:'-66',
			xtype: 'extkindeditor'
		}];
		this.callParent();
		
		
    },
    updateRouteId:function(routeId){
    	var me = this;
    	if(routeId){
    		me.down('fieldcontainer#fullPriceCombo').down('combo').setReadOnly(true);
			top.Ext.getBody().mask('加载基本信息...');
	    	me.getForm().load({
	            url: cfg.getCtx()+'/resource/route/info?routeId='+routeId,
	            success :function(form,action){
	            	top.Ext.getBody().unmask();
	            	form.reset();
	            	me.down('extkindeditor').setValue(action.result.FEATURE1);
	            	me.addCitys(action.result.data.begin,'begin');
					me.addCitys(action.result.data.end,'end');
					var result = action.result;
					if(result.data.FEATURE){
						result.data.FEATURE = result.data.FEATURE.replace(/<br>/g,"\r\n");
					}
					if(result.data.NOTICE){
						result.data.NOTICE = result.data.NOTICE.replace(/<br>/g,"\r\n");
					}
	            	var formData = util.pmModel(result);
	            	formData.TAG_NAME_ZT = action.result.data.zt;
	            	
					form.setValues(formData);
					me.down('extkindeditor').setValue(action.result.FEATURE1);
	            }
	        });
    	}
    	return routeId;
    },
    addCitys : function(datas,type){
    	var ct=this.down('container[itemId='+type+'CityItems]');
    	ct.removeAll(true);
    	for(var i=0;i<datas.length;i++){
    		ct.add(this.cityTpl(datas[i],type));
    	}
    },
    cityTpl :function(r,type){
    	return {
    		itemId:r.CITY_ID,
			margin:'0 2 0 0',
			items:[{
				xtype:'hidden',
				name:(type.toUpperCase())+'_CITY',
				value: r.CITY_ID+','+r.CITY_NAME
			},{
				tooltip:r.CITY_NAME,
				cls:'tag-split-btn remove',
				xtype:'splitbutton',
				text:r.CITY_NAME,
				listeners:{
					arrowclick :function(sp){
						var zj = sp.up('panel');
						zj.up('container[itemId='+type+'CityItems]').remove(zj,true);
					}
				}
			}]
		};
    }
});