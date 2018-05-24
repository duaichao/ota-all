Ext.define('Ext.ux.button.HoverButton',{
	extend: "Ext.toolbar.TextItem",
	alias : 'widget.hoverbutton',
	xtype:'hoverbutton',
	componentCls: "hover-menu-button",
	initComponent: function() {
		this.text = this.text+'<div class="hover-menu-view"></div>';
        this.callParent(arguments);
	},
    onRender: function() {
        this.callParent(arguments);
        this.menuEl = this.getEl().down('div.hover-menu-view');
        this.menu.render(this.menuEl);
        this.getEl().on({
        	 click: function() {
                 this.fireEvent("click")
             },
            scope: this
        });
        this.getEl().hover(function(){
        	var btnX = this.getEl().getX();
        	this.addCls('over');
        	this.menuEl.show();
        	var offsetX = btnX+this.menuEl.getWidth()-Ext.getBody().getWidth();
        	if(offsetX>0){
        		this.menuEl.alignTo(this.getEl(),"bl", [0-offsetX-5, 0]);
        	}
        },function(){
        	this.menuEl.hide();
        	this.removeCls('over');
        },this);
    }
});