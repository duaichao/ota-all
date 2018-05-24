Ext.define('app.view.finance.calcGrid', {
	extend:'Ext.grid.Panel',
	xtype:'calcgrid',
	loadMask: true,
	multiColumnSort: true,
	viewConfig:{
		stripeRows:false
	},
	config:{
		selectDepartIds:[]
	},
	features: [{
        ftype : 'groupingsummary',
        collapsible :false,
        //groupHeaderTpl :'<span>{name} 共{rows.length}笔订单</span>',
        groupHeaderTpl:['<dl style="height:18px;padding:0px;margin:0px;">',
                        '<dd class="x-grid-row-checker x-column-header-text chkGrp"', 
                        'style="float:left;margin-top:2px;">',
                        '</dd>',
                        '<dd style="float:left; padding:3px 0px 0px 3px;">',
                        '<span>{name} 共{rows.length}笔订单</span>',
                        '</dd>',
                        '</dl>'
                        ].join(''),
        hideGroupedHeader : false,
        id:'restaurantGrouping',
        enableGroupingMenu : false
    }, {
        ftype: 'summary',
        dock: 'bottom'
    }],
	initComponent: function() {
		var me = this;
		this.store = Ext.create('Ext.data.Store', {
            pageSize: 50,
            groupField:'TEXT',
            autoLoad: false,
            model:Ext.create('app.model.'+currViewInPath+'.model'),
            proxy: {
		         type: 'ajax',
		         noCache:true,
		         url: cfg.getCtx()+cfg.getModuleListUrl()[currViewInPath],
		         reader: {
		             rootProperty: 'data',
            		 totalProperty: 'totalSize'
		         }
		     }
        });
		
		
        //this.bbar = util.createGridBbar(this.store);
        this.columns = Ext.create('app.model.'+currViewInPath+'.column',{grid:me});
        
        this.dockedItems = [{
    		xtype:'toolbar',
    		itemId:'gridtool',
        	items:[{
        		disabled:true,
        		text:'正在初始化...'
        	}]
    	}];
        this.viewConfig = {  
    		    stripeRows : true,
    		    enableTextSelection : true/*,
    		    listeners:{
    		    	groupcollapse:function( view, node, group, eOpts ){
    		    		if(view.chkGrp[group]){
    		    			//Ext.get(node).addCls('x-grid-row-checked');
    		        	}
    		    	}
    		    }*/
    		};  
        this.callParent();
        this.getView().chkGrp = {};
        this.on('groupclick',function(view, node, group, e, eOpts){
        	var fg = view.getFeature('restaurantGrouping'),dd=e.getTarget('.chkGrp');
        	group = group || 'error';
        	if(dd&&group!='error'){
        		var classes = node.classList;
                var nodeEl = Ext.get(node);
                var addingCheck;
                if(!view.chkGrp[group]) {
                    nodeEl.addCls('x-grid-row-checked');
                    addingCheck = true;
                    view.chkGrp[group]=true;
                }
                else {
                    nodeEl.removeCls('x-grid-row-checked');
                    addingCheck = false;
                    view.chkGrp[group]=false;
                }
                var sm = view.getSelectionModel();
                var ds = sm.store,departId = '';
                sm.setLocked(false);
                var records = ds.queryBy(
                    function(record, id) {
                        if(record.data[ds.groupField] == group) {
                        	departId = record.get('DEPART_ID');
                            if(addingCheck) {
                                sm.select(record, true);
                            }
                            else {
                                sm.deselect(record);
                            }
                        }
                    }, this
                );
                var ds = me.getSelectDepartIds();
                departId = departId || '';
                if(addingCheck) {
                	ds.push(departId);
                }else{
                	Ext.Array.remove( ds, departId );
                }
                me.setSelectDepartIds(ds);
                sm.setLocked(true);
                //fg.expand(group);
        	}
        });
	},
	columnLines: true,
	selModel: Ext.create('Ext.selection.CheckboxModel', { showHeaderCheckbox: false })
});