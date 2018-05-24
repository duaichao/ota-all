Ext.define('app.view.common.CompanyCombo', {
    extend: 'Ext.form.field.ComboBox',
    xtype:'companycombo',
    defaultParam:'1',
    minChars: 1,
    queryParam: 'query',
    displayField: 'COMPANY',
    pageSize:20,
    forceSelection:true,
    multiSelect:false,
    autoSelect:false,
    queryMode: 'remote',
    triggerAction: 'all',
    valueField: 'ID',
    emptyText:'公司名称',
    listConfig:{
    	minWidth:360,
    	itemTpl:[
			 '<tpl for=".">',
	            '<li class="city-item">{COMPANY}<span>{BRAND_NAME}</span></li>',
	        '</tpl>'
		]
    },
    initComponent: function() {
    	var me = this;
    	this.store = Ext.create('app.store.Company');
    	this.store.getProxy().setExtraParams({type:me.defaultParam});
    	this.callParent();
    }
});	
