/**
 * @class Ext.ux.button.BadgeButton
 * @extends Ext.button.Button
 * 
 * @author Tomasz Jagusz
 * based on: http://blogs.walkingtree.in/2012/07/16/badge-text-in-extjs-4-1/
 * based on: https://github.com/vondervick/ext/tree/master/badge_button
 */


Ext.define('Ext.ux.button.BadgeButton', {
    extend: 'Ext.button.Button',
    alias: 'widget.badgebutton',

	
    badgeText: null,
	renderTpl:'<span id="{id}-btnWrap" data-ref="btnWrap" role="presentation" unselectable="on" style="{btnWrapStyle}" ' +
                'class="{btnWrapCls} {btnWrapCls}-{ui} {splitCls}{childElCls}">' +
            '<span id="{id}-btnEl" data-ref="btnEl" role="presentation" unselectable="on" style="{btnElStyle}" ' +
                    'class="{btnCls} {btnCls}-{ui} {textCls} {noTextCls} {hasIconCls} ' +
                    '{iconAlignCls} {textAlignCls} {btnElAutoHeightCls}{childElCls}">' +
                '<tpl if="iconBeforeText">{[values.$comp.renderIcon(values)]}</tpl>' +
                '<span id="{id}-btnInnerEl" data-ref="btnInnerEl" unselectable="on" ' +
                    'class="{innerCls} {innerCls}-{ui}{childElCls}">{text}</span>' +
                    '<tpl if="badgeText"><span id="{id}-btnBadge" data-ref="btnBadge" class="{baseCls}-badgeCls" reference="badgeElement" unselectable="on">{badgeText}</span></tpl>'+
                '<tpl if="!iconBeforeText">{[values.$comp.renderIcon(values)]}</tpl>' +
            '</span>' +
        '</span>' +
        '{[values.$comp.getAfterMarkup ? values.$comp.getAfterMarkup(values) : ""]}' +
        // if "closable" (tab) add a close element icon
        '<tpl if="closable">' +
            '<span id="{id}-closeEl" data-ref="closeEl" class="{baseCls}-close-btn">' +
                '<tpl if="closeText">' +
                    ' {closeText}' +
                '</tpl>' +
            '</span>' +
        '</tpl>',



    childEls: ['btnBadge'],


    initComponent: function() {
        this.callParent();
    },


    getTemplateArgs: function() {
        var me = this,
            btnCls = me._btnCls,
            baseIconCls = me._baseIconCls,
            iconAlign = me.getIconAlign(),
            glyph = me.glyph,
            glyphFontFamily = Ext._glyphFontFamily,
            text = me.text,
            hasIcon = me._hasIcon(),
            hasIconCls = me._hasIconCls,
            glyphParts;

        if (typeof glyph === 'string') {
            glyphParts = glyph.split('@');
            glyph = glyphParts[0];
            glyphFontFamily = glyphParts[1];
        }

        return {
            innerCls: me._innerCls,
            splitCls: me.getArrowVisible() ? me.getSplitCls() : '',
            iconUrl: me.icon,
            iconCls: me.iconCls,
            glyph: glyph,
            glyphCls: glyph ? me._glyphCls : '',
            glyphFontFamily: glyphFontFamily,
            text: text || '&#160;',
            badgeText: me.badgeText || undefined,
            closeText: me.closeText,
            textCls: text ? me._textCls : '',
            noTextCls: text ? '' : me._noTextCls,
            hasIconCls: hasIcon ? hasIconCls : '',
            btnWrapCls: me._btnWrapCls,
            btnWrapStyle: me.width ? 'table-layout:fixed;' : '',
            btnElStyle: me.height ? 'height:auto;' : '',
            btnCls: btnCls,
            baseIconCls: baseIconCls,
            iconBeforeText: iconAlign === 'left' || iconAlign === 'top',
            iconAlignCls: hasIcon ? (hasIconCls + '-' + iconAlign) : '',
            textAlignCls: btnCls + '-' + me.getTextAlign()
        };
    },




    /**
     * Sets this Button's Badge text
     * @param {String} text The button badge text
     * @return {Ext.ux.container.BadgeButton} this
     */
    setBadgeText: function(text) {
        text = text || '';
        var me = this,
            oldBadgeText = me.badgeText || '';


        if (text != oldBadgeText) {


            if (Ext.isEmpty(text)) {
                me.btnBadge.addCls('hide-badge');
            } else {
                me.btnBadge.removeCls('hide-badge');
            }


            me.badgeText = text;
            if (me.rendered) {
                me.btnBadge.update(text || '');
                //me.setComponentCls();
                if (Ext.isStrict && Ext.isIE8) {
                    // weird repaint issue causes it to not resize
                    me.el.repaint();
                }
                //me.updateLayout();
            }
            me.fireEvent('badgetextchange', me, oldBadgeText, text);
        }
        return me;
    }
});  