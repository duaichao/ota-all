Ext.define('app.view.common.ScenicCombo', {
    extend: 'Ext.form.field.ComboBox',
    xtype:'sceniccombo',
    minChars: 1,
    queryParam: 'query',
    displayField: 'SCENIC_TEXT',
    pageSize:10,
    autoLoad:false,
    focusOnToFront:true,
    forceSelection:true,
    queryMode: 'remote',
    triggerAction: 'all',
    valueField: 'ID',
    emptyText:'简拼/全拼/汉字',
    listConfig:{
    	minWidth:800,
    	itemTpl:[
			 '<tpl for=".">',
	            '<li class="city-item">{SCENIC_TEXT}/{TEXTS_SHOW}<span>{JIANPINS_SHOW}</span></li>',
	        '</tpl>'
		]
    },
    initComponent: function() {
    	var me = this;
    	this.store = Ext.create('app.store.Scenic',{pageSize:me.pageSize,autoLoad:me.autoLoad});
    	this.callParent();
    }
});
