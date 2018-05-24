Ext.define('app.view.common.PriceGrid', {
	extend:'Ext.grid.Panel',
	xtype:'pricegrid',
	cls:'pricegrid',
	loadMask: true,
	border:true,
	trackMouseOver: false,
	hiddenParams:[],
	initComponent: function() {
		this.columns = [{text:'',width:60,sortable: false,menuDisabled: true,dataIndex: 'TITLE' }];
		this.store = util.createGridStore(cfg.getCtx()+'/resource/traffic/price/type',Ext.create('Ext.data.Model',{
			fields: ['ID','TITLE']
		}));
        this.callParent();
        var me = this;
        Ext.Ajax.request({
			url:cfg.getCtx()+'/resource/traffic/price/attr',
			success:function(response, opts){
				var data = Ext.decode(response.responseText).data;
				for(var i=0;i<data.length;i++){
					var column = Ext.create('Ext.grid.column.Widget', {  
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
				        	margin:'0',
			                cls:'price',
			                flex:1,
			                value:0,
			                listeners:{
			                	change:function(nf,newValue,oldValue){
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
				                		/*console.log(me.hiddenParams+'--------');*/
			                		},100);
			                	}
			                }
			            }  
				    });  
				    me.headerCt.add(column); 
			    } 
			    me.getView().refresh();  
			    
			}
		});
	},
	columnLines: true
});