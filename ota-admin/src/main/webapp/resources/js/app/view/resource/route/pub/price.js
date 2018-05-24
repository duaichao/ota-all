Ext.define('app.view.resource.route.pub.price', {
	extend:'Ext.grid.Panel',
	xtype:'rprice',
	cls:'pricegrid',
	loadMask: true,
	border:true,
	trackMouseOver: false,
	hiddenParams:[],
	myLoadId:'',//显示价格
	myLoadUrl:'',
	initComponent: function() {
		this.columns = [{text:'',width:60,sortable: false,menuDisabled: true,dataIndex: 'TITLE' }];
		if(this.myLoadUrl==''){
			this.myLoadUrl = cfg.getCtx()+'/resource/route/price/type?routeId='+this.routeId;
		}else{
			this.myLoadUrl = this.myLoadUrl+this.myLoadId.replace('-','');
		}
		this.store = util.createGridStore(this.myLoadUrl,Ext.create('Ext.data.Model',{
			fields: ['ID','TITLE']
		}));
        this.callParent();
        var me = this;
        Ext.Ajax.request({
			url:cfg.getCtx()+'/resource/route/price/attr',
			success:function(response, opts){
				var data = Ext.decode(response.responseText).data;
				for(var i=0;i<data.length;i++){
					
					var column;
					if(data[i].childs){
						column = {
							sortable: false,
						    menuDisabled: true, 
						    dataIndex:data[i].CON_NAME,
							text: data[i].TITLE,
							columns:[]
						};
						var childs = data[i].childs;
						for(var j=0;j<childs.length;j++){
							//发布价格使用
							column.columns.push({
								text: childs[j].TITLE,  
								xtype:'widgetcolumn',
						        flex:1,
						        sortable: false,
						        menuDisabled: true,  
						        align:'center',
						        dataIndex:childs[j].CON_NAME,
						        attrObj:childs[j],
						        renderer:function(value,metaData,record,colIndex,store,view){
						        	metaData.tdAttr='typeobj=\''+Ext.encode(record.data)+'\'';
						        },
						        widget: {
						        	margin:'0',
						        	xtype: 'numberfield',
						        	readOnly:me.myLoadId!='',
					                cls:'price',
					                flex:1,
					                value:0,
					                listeners:{
					                	change:function(nf,newValue,oldValue){
					                		if(me.myLoadId!=''){return;}
					                		setTimeout(function(){
						                		var md = nf.up('widgetcolumn'),col = nf.el.up('.x-textfield-default-cell'),
						                			model = {},old = {},idx,
						                			typeObj = Ext.decode(col.getAttribute('typeobj')),
						                			attrObj = md.attrObj;
						                		old['ATTR_ID'] = model['ATTR_ID'] = attrObj.ID;
						                		old['ATTR_NAME'] = model['ATTR_NAME'] = attrObj.TITLE;
						                		old['TYPE_ID'] = model['TYPE_ID'] = typeObj.ID;
						                		old['TYPE_NAME'] = model['TYPE_NAME'] = typeObj.TITLE;
						                		model['PRICE'] = newValue;
						                		old['PRICE'] = oldValue;
						                		model = Ext.encode(model);
						                		old = Ext.encode(old);
						                		idx = Ext.Array.indexOf(me.hiddenParams,old);
						                		if(idx!=-1){
						                			me.hiddenParams.splice(idx,1);
						                		}
						                		me.hiddenParams.push(model);
						                		//console.log(me.hiddenParams+'=====');
					                		},100);
					                	}
					                }
					            }
							});
						}
					}else{
						column = Ext.create('Ext.grid.column.Widget', {  
		        			text: data[i].TITLE,  
					        flex:1,
					        sortable: false,
					        menuDisabled: true,  
					        align:'center',
					        dataIndex:data[i].CON_NAME,
					        attrObj:data[i],
					        renderer:function(value,metaData,record,colIndex,store,view){
					        	metaData.tdAttr='typeobj=\''+Ext.encode(record.data)+'\'';
					        },
					        widget: {
					        	xtype: 'numberfield',
				                cls:'price',
				                margin:'0',
				                readOnly:me.myLoadId!='',
				                flex:1,
				                value:0,
				                listeners:{
				                	change:function(nf,newValue,oldValue){
				                		if(me.myLoadId!=''){return;}
				                		setTimeout(function(){
					                		var md = nf.up('widgetcolumn'),col = nf.el.up('.x-textfield-default-cell'),
					                			model = {},old = {},idx,
					                			typeObj = Ext.decode(col.getAttribute('typeobj')),
					                			attrObj = md.attrObj;
					                		old['ATTR_ID'] = model['ATTR_ID'] = attrObj.ID;
					                		old['ATTR_NAME'] = model['ATTR_NAME'] = attrObj.TITLE;
					                		old['TYPE_ID'] = model['TYPE_ID'] = typeObj.ID;
					                		old['TYPE_NAME'] = model['TYPE_NAME'] = typeObj.TITLE;
					                		model['PRICE'] = newValue;
					                		old['PRICE'] = oldValue;
					                		model = Ext.encode(model);
					                		old = Ext.encode(old);
					                		idx = Ext.Array.indexOf(me.hiddenParams,old);
					                		if(idx!=-1){
					                			me.hiddenParams.splice(idx,1);
					                		}
					                		me.hiddenParams.push(model);
				                		},100);
				                	}
				                }
				            }  
					    });  
				    }
				    me.headerCt.add(column); 
			    } 
			    me.getView().refresh();  
			}
		});
	},
	columnLines: true
});