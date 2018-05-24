Ext.define('app.view.common.CityAllCombo', {
    extend: 'Ext.form.field.ComboBox',
    xtype:'cityallcombo',
    minChars: 1,
    queryParam: 'query',
    displayField: 'TEXT',
    pageSize:50,
    typeAhead: true,
    forceSelection:true,
    selectOnFocus:true, 
    queryMode: 'remote',
    triggerAction: 'all',
    valueField: 'ID',
    
    store:'City',
    
    emptyText:'简拼/全拼/汉字',
	
    initComponent: function() {
    	this.callParent();
    },
    createPicker:function(){
    	console.log(this.onListRefresh);
    	var me=this,
    		picker,
            pickerCfg = Ext.apply({
                xtype: 'boundlist',
                pickerField: me,
                selModel: {
                    mode: me.multiSelect ? 'SIMPLE' : 'SINGLE',
                    enableInitialSelection: false
                },
                floating: true,
                hidden: true,
                store: me.store,
                displayField: me.displayField,
                preserveScrollOnRefresh: true,
                pageSize: me.pageSize,
                tpl: me.tpl
            }, me.listConfig, me.defaultListConfig);

        picker = me.picker = Ext.widget(pickerCfg);
        if (me.pageSize) {
            picker.pagingToolbar.on('beforechange', me.onPageChange, me);
        }

        me.mon(picker, {
            refresh: me.onListRefresh,
            scope: me
        });

        me.mon(picker.getSelectionModel(), {
            beforeselect: me.onBeforeSelect,
            beforedeselect: me.onBeforeDeselect,
            selectionchange: me.onListSelectionChange,
            scope: me
        });

        return picker;
    }
});	
