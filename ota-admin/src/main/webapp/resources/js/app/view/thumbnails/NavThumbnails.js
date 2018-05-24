Ext.define('app.view.thumbnails.NavThumbnails', {
    extend: 'Ext.view.View',
    xtype: 'navthumbnails',
    cls: 'navthumbnails',
    store: 'navigation',
    itemSelector: '.thumbnail-item',
    initComponent: function() {
        this.tpl = new Ext.XTemplate(
            '<tpl for=".">' ,
            	'<tpl if="this.isParent(values.parentId)">',
	            	'<div class="thumbnail-item thumbnail-mini" nodeId="{id}">' ,
	                '<div class="thumbnail-icon-wrap">' ,
	                    '<i class="iconfont" style="font-size:32px;">{[this.covertGlyph(values.glyph)]}</i>',
	                '</div>' ,
	                '<div class="thumbnail-text">{text}</div>' ,
	                '<div class="spacer"></div>',
	                '</div>' ,
	            '</tpl>',
            '</tpl>',{
        		isParent:function(parentId){
        			return parentId=='head_nav_home';
        		},
            	covertGlyph:function(glyph){
            		return util.headNavGlyphMap(glyph);
            		//return '&#'+glyph.split('@')[0]+';';
            	}
            }
		);
        
        this.callParent();
    }
});
 
