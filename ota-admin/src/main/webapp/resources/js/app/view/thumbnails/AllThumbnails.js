Ext.define('app.view.thumbnails.AllThumbnails', {
    extend: 'Ext.view.View',
    xtype: 'allthumbnails',
    cls: 'allthumbnails',
    region: 'center',
    store: 'Thumbnails',
    itemSelector: '.thumbnail-item',
    initComponent: function() {
        this.tpl = new Ext.XTemplate(
            '<tpl for=".">' ,
            	'<tpl if="parentId==\'home\'">',
                	'</div><div class="thumbnail-warp">',
            	'</tpl>',
                '<div class="thumbnail-item thumbnail-mini" >' ,
                    '<div class="thumbnail-icon-wrap">' ,
                        '<img src="'+cfg.getCtx()+'/resources/images/icons/menus/{iconCls}_48.png" style="height:32px;"/>' ,
                        //'<i class="iconfont white f20">&#{glyph};</i>',
                        //'<span class="x-btn-badgeCls mini"></span>',
                    '</div>' ,
                    '<div class="thumbnail-text ccc">{text}</div>' ,
                '</div>' ,
                //'<tpl if="parentId==\'nav_home\'">',
                	//'<div class="thumbnail-include">&nbsp;',
                	//'</div>',
            	//'</tpl>',
            '</tpl>'
		);
        
        this.callParent();
    }
});
 
