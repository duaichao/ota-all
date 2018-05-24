Ext.define('app.view.produce.traffic', {
    extend: 'Ext.container.Viewport',
    layout: 'border',
    items: [{
    	region:'west',
    	width:240,
    	minWidth:240,
    	maxWidth:240,
    	margin:'10',
    	bodyStyle:'background:rgba(255,255,255,.5);',
    	bodyPadding:'10',
    	header:false,
    	border:false,
    	xtype:'form',
    	itemId:'queryForm',
    	fieldDefaults: {
	        labelAlign: 'right',
	        labelWidth: 50,
	        msgTarget: 'side'
	    },
    	items:[{
    		margin:'5 0 10 55',
	    	hideLabel:true,
	    	width:150,
	    	xtype:'radiogroup',
        	items: [
                {boxLabel: '单程', name: 'isSingle', inputValue: '0', checked: true},
                {boxLabel: '往返', name: 'isSingle', inputValue: '1'}
            ]
	    },{
    		xtype: 'fieldcontainer',
    		hideLabel:true,
	        layout: {
		        type: 'hbox',
		        pack: 'start',
		        align: 'stretch'
		    },
		    items:[{
		    	xtype: 'fieldcontainer',
		    	flex:1,
		    	layout: {
			        type: 'vbox',
			        pack: 'start',
			        align: 'stretch'
			    },
			    items:[{
		    		xtype:'container',
		    		layout:{
		    			type:'hbox'
		    		},
		    		items:[{
		    			fieldLabel:'出发地',
			    		name:'startCity',
			    		hiddenName:'startCity',
			    		valueField: 'TEXT',
			    		xtype:'citycombo',
			    		flex:1
		    		},{
		    			xtype:'button',
		    			margin:'0 0 0 10',
		    			ui:'default-toolbar',
	                    glyph:'xe64a@iconfont',
	                    handler:function(btn){
				    		var start = btn.previousSibling(),
				    			end = btn.up('container').nextSibling().down('citycombo'),
				    			startOldValue = start.getRawValue();
				    		start.setRawValue(end.getRawValue());
				    		end.setRawValue(startOldValue);
				    	}
		    		}]
		    	},{
		    		xtype:'container',
		    		layout:{
		    			type:'hbox'
		    		},
		    		items:[{
		    			fieldLabel:'目的地',
			    		name:'endCity',
			    		hiddenName:'endCity',
			    		valueField: 'TEXT',
			    		xtype:'citycombo',
			    		flex:1
		    		},{
		    			xtype:'button',
		    			margin:'0 0 0 10',
		    			ui:'default-toolbar',
	                    glyph:'xe63d@iconfont',
	                    handler:function(btn){
	                    	var end = btn.previousSibling(),
				    			start = btn.up('container').previousSibling().down('citycombo');
				    		start.reset();
				    		end.reset();
				    	}
		    		}]
		    	}]
		    }]
    	},{
    		margin:'10 0 0 0',
    		minDate:new Date(),
    		xtype:'datepicker',
    		hideLabel:true,
    		showToday:false
    	}],
    	buttons:[{
    		itemId:'queryBtn',
    		text:'查询'
    	}]/*,
    	collapseMode:'mini',
    	collapsible:true,
    	split: true,
    	collapsed:false*/
    	
    },{
    	region:'center',
    	margin:'10 10 10 0',
    	itemId:'basegrid',
    	xtype:'protrafficgrid'
    }]
});

