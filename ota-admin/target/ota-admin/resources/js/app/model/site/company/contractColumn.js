Ext.define('app.model.site.company.contractColumn', {
	requires:['Ext.ux.grid.ActionGlyphColumn'],
	constructor:function(p){
		var store = p.store;
		return [
		        Ext.create('Ext.grid.RowNumberer',{width:35}),
		        {
			        text: '合同号',
			        width:150,
			        dataIndex: 'NO',
			        items    : {
			        	emptyText:'筛选合同号',
			            xtype: 'textfield',
			            margin: 2,
			            width:145,
			            enableKeyEvents: true,
			            listeners: {
			                keyup: function(filterField){
			        	    	var params = store.getProxy().getExtraParams();
			        	    	store.getProxy().setExtraParams(Ext.applyIf({
			        	    		conNo:filterField.value
			        	    	},params));
			        	    	store.load();
			                },
			                buffer: 500
			            }
			        }
			    },{
			        text: '状态',
			        width:90,
			        dataIndex: 'STATUS',
			        items    : {
			        	xtype:'combo',
						name:'type',
						editable:false,
						hiddenName:'type',
						width:85,
						margin: 2,
						value:'',
						typeAhead: true,
			            triggerAction: 'all',
			            store: [
			            	['','全部'],
			                ['0','未使用'],
			                ['1','已使用'],
			                ['2','已核销'],
			                ['3','未废除'],
			                ['4','已废除']
			            ],
			            listeners: {
			            	change:function( combo, newValue, oldValue){
			            		if(newValue!=''){
			            			Ext.create('Ext.window.Window',{
			            				title:util.windowTitle('','选择时间范围',''),
			            				width:300,
			            		    	height:175,
			            				modal:true,
			            				draggable:false,
			            				resizable:false,
			            				layout:'fit',
			            				closable :false,
			            				items:[{
			            					xtype:'fieldcontainer',
			            					layout:'hbox',
			            					padding:10,
			            					items:[{
			        			    			name:'startTime',
			        			    			flex:1,
			        			    			emptyText:'开始时间',
			        			    			xtype:'datefield',
			        			    			format:'Y-m-d',
			        			    			endDateField: 'endxddt',
			        					        itemId:'startxddt',
			        					        showToday:false,
			        					        vtype: 'daterange'
			        			    		}, {
			        			                xtype: 'splitter'
			        			            },{
			        			    			hideLabel:true,
			        			    			flex:1,
			        			    			emptyText:'结束时间',
			        			    			xtype:'datefield',
			        			    			name:'endTime',
			        			    			format:'Y-m-d',
			        			    			itemId:'endxddt',
			        			    			showToday:false,
			        				            startDateField: 'startxddt',
			        				            vtype: 'daterange'
			        			    		}]
			            				}],
			            				buttons:[{
			            					text:'取消',
			            					cls:'disable',
			            					handler:function(btn){
			            						combo.reset();
			            						btn.up('window').close();
			            					}
			            				},{
			            					text:'查询',
			            					handler:function(btn){
			            						var window = btn.up('window'),
			            							start = window.down('datefield#startxddt'),
			            							end = window.down('datefield#endxddt');
			            						//if(start.getValue()&&end.getValue()){
			            							window.close();
			            							var params = store.getProxy().getExtraParams();
				    			        	    	store.getProxy().setExtraParams(Ext.applyIf({
				    			        	    		type:newValue,
				    			        	    		startTime:Ext.Date.format(start.getValue(),'Y-m-d'),
				    			        	    		endTime:Ext.Date.format(end.getValue(),'Y-m-d')
				    			        	    	},params));
				    			        	    	store.load();
			            						//}
			            					}
			            				}]
			            			}).show();
			            		}else{
			            			var params = store.getProxy().getExtraParams();
			            			store.getProxy().setExtraParams(Ext.applyIf({
    			        	    		type:'',
    			        	    		startTime:'',
    			        	    		endTime:''
    			        	    	},params));
    			        	    	store.load();
			            		}
			            	}
			            }
			        },
			        renderer: function(value){
			        	var str = ['<span class="green-color">未使用</span>','<span style="color:#9e9e9e;">已使用</span>','已核销'];
			        	return str[value];
			        }
			    },{
			    	text: '是否废除',
			        width:70,
			        align:'center',
			        dataIndex: 'IS_CANCEL',
			        renderer: function(value){
			        	return value=='1'?'是':'否';
			        }
			    },{
			    	text: '分配人/部门/时间',
			        width:240,
			        dataIndex: 'CREATE_CHINA_NAME',
			        renderer: function(v,c,r){
			        	v = v || r.get('CREATE_USER');
			        	if(v){
			        		return (v||'')+'/'+(r.get('CREATE_DEPART_NAME')||'')+'/'+(r.get('CREATE_TIME')||'');
			        	}else{
			        		return '';
			        	}
			        }
			    },{
			    	text: '使用人/部门/时间',
			        width:240,
			        dataIndex: 'USE_CHINA_NAME',
			        renderer: function(v,c,r){
			        	v = v || r.get('USE_USER');
			        	if(v){
			        		return (v||'')+'/'+(r.get('USE_DEPART_NAME')||'')+'/'+(r.get('USE_TIME')||'');
			        	}else{
			        		return '';
			        	}
			        }
			    },{
			    	text: '订单编号',
			        width:150,
			        dataIndex: 'ORDER_NO',
			        items    : {
			        	emptyText:'筛选订单号',
			            xtype: 'textfield',
			            margin: 2,
			            width:145,
			            enableKeyEvents: true,
			            listeners: {
			                keyup: function(filterField){
			        	    	var params = store.getProxy().getExtraParams();
			        	    	store.getProxy().setExtraParams(Ext.applyIf({
			        	    		orderNo:filterField.value
			        	    	},params));
			        	    	store.load();
			                },
			                buffer: 500
			            }
			        }
			    },{
			    	text: '废除人/部门/时间',
			        width:240,
			        dataIndex: 'CANCEL_CHINA_NAME',
			        renderer: function(v,c,r){
			        	v = v || r.get('CANCEL_USER');
			        	if(v){
			        		return (v||'')+'/'+(r.get('CANCEL_DEPART_NAME')||'')+'/'+(r.get('CANCEL_TIME')||'');
			        	}else{
			        		return '';
			        	}
			        }
			    },{
			    	text: '核销人/公司/部门/时间',
			        width:300,
			        dataIndex: 'BALANCE_CHINA_NAME',
			        renderer: function(v,c,r){
			        	v = v || r.get('BALANCE_USER');
			        	if(v){
			        		return (v||'')+'/'+(r.get('BALANCE_COMPANY')||'')+'/'+(r.get('BALANCE_DEPART_NAME')||'')+'/'+(r.get('BALANCE_TIME')||'');
				        }else{
			        		return '';
			        	}
			        }
			    }
		];
	}
});