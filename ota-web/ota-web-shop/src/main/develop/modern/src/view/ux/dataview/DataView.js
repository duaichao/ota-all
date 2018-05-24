Ext.define('Weidian.view.ux.dataview.DataView', {
    extend: 'Ext.dataview.DataView',
	alternateClassName: 'ExpandDataView',
    xtype: 'ExpandDataView',
	//override: 'Ext.dataview.DataView',
    onItemTap: function(container, target, index, e) {
        var me = this,
            store = me.getStore(),
            record = store && store.getAt(index);
		
		this.$rippleWrap = target.insertFirst({cls: 'md-ripple-wrap'});
		this.$rippleWrap.setStyle('z-index', '2');
        this.$ripple = this.$rippleWrap.insertFirst({cls: 'md-ripple'});
		var color = window.getComputedStyle(target.dom).color,
			offset = target.getXY(),
			elementWidth = target.getWidth(),
			elementHeight = target.getHeight(),
			rippleDiameter = elementWidth > elementHeight ? elementWidth : elementHeight,
			pos = e.getXY(),
			posX = pos[0] - offset[0] - (rippleDiameter / 2),
			posY = pos[1] - offset[1] - (rippleDiameter / 2);
		
		this.$ripple.setStyle('backgroundColor', color);
		this.$ripple.toggleCls('md-ripple-effect', true);
		this.$ripple.setWidth(rippleDiameter);
		this.$ripple.setHeight(rippleDiameter);
		this.$ripple.setTop(posY);
		this.$ripple.setLeft(posX);
		this.$rippleWrap.show();

		if (this.$rippleAnimationListener) {
			this.$rippleAnimationListener.destroy();
		}

		this.$rippleAnimationListener = this.$ripple.on('animationend', this.onRippleEnd, this, {
			single: true,
			destroyable: true
		});


        me.fireEvent('itemtap', me, index, target, record, e);
    },

    onItemTapHold: function(container, target, index, e) {
        var me = this,
            store = me.getStore(),
            record = store && store.getAt(index);

        me.fireEvent('itemtaphold', me, index, target, record, e);
    },
	
	onRippleEnd: function () {
        if (this.$ripple) {
            this.$ripple.toggleCls('md-ripple-effect', false);
            this.$rippleWrap.hide();
        }
    },


    removeRippleEffect: function () {
        if (this.$rippleAnimationListener) {
            this.$rippleAnimationListener.destroy();
        }
        this.onRippleEnd();
    },

    destroyRipple: function () {
        this.removeRippleEffect();
        if (this.$rippleWrap) {
            this.$rippleWrap.destroy();
        }
    }

    
});
