Ext.define('app.view.thumbnails.Thumbnails', {
    extend: 'Ext.view.View',
    xtype: 'thumbnails',
    cls: 'thumbnails',
    reference: 'contentView',
    region: 'center',
    store: 'Thumbnails',
    itemSelector: '.thumbnail-item',

    initComponent: function() {
        this.tpl =
            '<tpl for=".">' +
                '<div class="thumbnail-item">' +
                    '<div class="thumbnail-icon-wrap">' +
                        '<img src="'+cfg.getCtx()+'/resources/images/icons/menus/{iconCls}_64.png" style="height:64px;"/>' +
                        //'<span class="x-btn-badgeCls big">20</span>'+
                    '</div>' +
                    '<div class="thumbnail-text">{text}</div>' +
                '</div>' +
            '</tpl>';
        
        this.callParent();
    }
});
