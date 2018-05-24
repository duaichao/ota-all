Ext.define('app.view.b2b.user.agency.supplyView', {
    extend: 'Ext.panel.Panel',
    config:{
	    cls:'py-container',
	    padding:10
    },
    layout:'fit',
    initComponent: function() {
    	this.dockedItems = [{
    		xtype:'toolbar',
    		items:[{
    			xtype:'segmentedbutton',
        		items:[{
        			text:'全部',
        			value:'',
        			pressed:true
        		},{
        			text:'国内',
        			value:'1'
        		},{
        			text:'出境',
        			value:'2'
        		}],
        		listeners:{
        			toggle:function( sg, button, isPressed, eOpts ){
        				if(isPressed){
        					var st = sg.up().up().down('dataview').getStore();
        					st.getProxy().setExtraParam('lineType',sg.getValue());
        					st.load();
        				}
        			}
        		}
    		}]
    	}];
    	var store = Ext.create('Ext.data.Store', {
            autoLoad: true,
            model:Ext.create('Ext.data.Model',{
            	fields: [
            	        'BRAND_NAME','CNT','BRAND_JP','BRAND_PY'
            	]
            }),
            proxy: {
		         type: 'ajax',
		         noCache:true,
		         url: cfg.getCtx()+'/produce/route/list/company',
		         reader: {
		             rootProperty: 'data',
            		 totalProperty: 'totalSize'
		         }
		     }
        });
    	var tpl =  new Ext.XTemplate(
    		'<tpl for=".">',
    		'<li class="py-crad" style="width:220px;padding:10px;">',
    		'<div class="py-crad-body">',
    		'<a href="javascript:;" class="py-card-title" onclick="util.redirectTo(\'produce/route\',\'?dynamicParamsSupplyName={BRAND_NAME}\')">{BRAND_NAME}</a>',
    		'</div>',
    		'<div class="py-crad-tool">',
    		'<span class="phone"></span>',
			'<div class="py-card-tool-right" data-qtip="产品数量">',
				'<span class="cnt">',
				'<span class="">在售产品:</span> ',
				'<span class="f14 bold blue-color">{CNT}</span>',
				'</span>',
			'</div>',
			'</div>',
			'</li>',
			'</tpl>'
    	);
    	this.items = [{
    		style:'border-top:1px solid #ddd;padding-top:10px;',
    		xtype:'dataview',
    		store:store,
    		tpl:tpl,
    		itemSelector:'li.py-crad'
    	}];
    	this.callParent();
    }
});
