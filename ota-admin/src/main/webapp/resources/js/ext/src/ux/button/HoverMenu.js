Ext.define('Ext.ux.button.HoverMenu',{
	extend: "Ext.view.View",
    alias: "widget.hovermenu",
    xtype:'hovermenu',
    floating:true,
    hidden:true,
    componentCls: "hover-menu",
    itemSelector: "div.item",
    deferEmptyText: false,
    initComponent: function() {
        this.renderTo = Ext.getBody();
        this.callParent()
    }
});