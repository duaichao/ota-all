Ext.define('app.view.common.CityCombo', {
    extend: 'Ext.form.field.ComboBox',
    xtype:'citycombo',
    minChars: 1,
    queryParam: 'query',
    displayField: 'TEXT',
    pageSize:20,
    forceSelection:true,
    multiSelect:false,
	autoSelect:false,
    queryMode: 'remote',
    triggerAction: 'all',
    valueField: 'ID',
    emptyText:'简拼/全拼/汉字',
    listConfig:{
    	minWidth:360,
    	itemTpl:[
			 '<tpl for=".">',
	            '<li class="city-item">{TEXT}/{PTEXT}<span>{JIANPIN}</span></li>',
	        '</tpl>'
		]
    },
    initComponent: function() {
    	var me = this;
    	this.store = Ext.create('app.store.City');
    	this.callParent();
    }
});	
