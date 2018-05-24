Ext.define('Weidian.view.ux.navigation.View', {
    extend: 'Ext.navigation.View',
    alternateClassName: 'ExpandNavigationView',
    xtype: 'ExpandNavigationView',
    requires: ['Weidian.view.ux.navigation.Bar'],
    config: {
    	defaultBackButtonText: ''
    },
    // @private
    applyNavigationBar: function(config) {
        var me = this;
        if (!config) {
            config = {
                hidden: true,
                docked: 'top'
            };
        }

        if (config.title) {
            delete config.title;
            //<debug>
            Ext.Logger.warn("Ext.navigation.View: The 'navigationBar' configuration does not accept a 'title' property. You " +
                "set the title of the navigationBar by giving this navigation view's children a 'title' property.");
            //</debug>
        }

        config.view = this;
        config.useTitleForBackButtonText = this.getUseTitleForBackButtonText();
        
        // Blackberry specific nav setup where title is on the top title bar and the bottom toolbar is used for buttons and BACK
        if (config.splitNavigation) {
            this.$titleContainer = this.add({
                docked: 'top',
                xtype: 'titlebar',
                ui: 'light',
                title: this.$currentTitle || ''
            });

            var containerConfig = (config.splitNavigation === true) ? {} : config.splitNavigation;

            this.$backButtonContainer = this.add({
                xtype: 'toolbar',
                docked: 'bottom',
                hidden: true
            });

            // Any item that is added to the BackButtonContainer should be monitored for visibility
            // this will allow the toolbar to be hidden when no items exist in it.
            this.$backButtonContainer.on ({
                scope: me,
                add: me.onBackButtonContainerAdd,
                remove: me.onBackButtonContainerRemove
            });

            this.$backButton = this.$backButtonContainer.add({
                xtype: 'button',
                text: 'Back',
                hidden: true,
                ui: 'back'
            });

            // Default config items go into the bottom bar
            if(config.items) {
                this.$backButtonContainer.add(config.items);
            }

            // If the user provided items and splitNav items, default items go into the bottom bar, split nav items go into the top
            if(containerConfig.items) {
                this.$titleContainer.add(containerConfig.items);
            }

            this.$backButton.on({
                scope: this,
                tap: this.onBackButtonTap
            });

            config = {
                hidden: true,
                docked: 'top'
            };
        }

        return Ext.factory(config, Weidian.view.ux.navigation.Bar, this.getNavigationBar());
    },
    /**
     * @private
     */
    fireBbarDisplay:Ext.emptyFn,
    onItemAdd: function(item, index) {
        if (item && item.getDocked() && item.config.title === true) {
            this.$titleContainer = item;
        }
        
        this.doItemLayoutAdd(item, index);

        var navigaitonBar = this.getInitialConfig().navigationBar;
        
        if (!this.isItemsInitializing && item.isInnerItem()) {
            this.setActiveItem(item);

            // Update the navigationBar
            if (navigaitonBar) {
                this.getNavigationBar().onViewAdd(this, item, index);
            }

            // Update the custom backButton
            if (this.$backButtonContainer) {
                this.$backButton.show();
            }
        }

        if (item && item.isInnerItem()) {
            // Update the title container title
            this.updateTitleContainerTitle((item.getTitle) ? item.getTitle() : item.config.title);
        }

        if (this.initialized) {
            this.fireEvent('add', this, item, index);
        }
        this.fireBbarDisplay(item);
    },
    push: function(view) {
    	/*var progressBar;
    	if(Ext.Viewport.down('container#progress')){
    		progressBar = Ext.Viewport.down('container#progress');
    	}else{
	    	progressBar = Ext.Viewport.add(Ext.factory({
	    		itemId:'progress',
	    		cls:'progress',
	    		width:'100%',
	    		top:36,
	    		height:3,
	    		centered : true,
	            modal: {xtype:'mask',transparent:true},
	            hideOnMaskTap : false,
	            styleHtmlContent:true,
	            html:'<div class="indeterminate"></div>'
	    	},'Ext.Container'));
    	}
    	progressBar.show();
    	view.on('painted',function(){
    		setTimeout(function(){Ext.Viewport.remove(progressBar);},800);
    	});
    	console.log('========================');
    	console.log(view.getXTypes());
    	console.log(this.getActiveItem().getXTypes());
    	console.log('========================');*/
		//console.log(view.routeId);
        if (this.child('component[routeId=' + view.routeId + ']')) return;
		
        return this.add(view);
    },
	/**
     * @private
     */
    doPop: function() {
        var me = this,
            innerItems = this.getInnerItems();

        //set the new active item to be the new last item of the stack
        me.remove(innerItems[innerItems.length - 1]);

        // Hide the backButton
        if (innerItems.length < 3 && this.$backButton) {
            this.$backButton.hide();
        }

        // Update the title container
        if (this.$titleContainer) {
            //<debug>
            if (!this.$titleContainer.setTitle) {
                Ext.Logger.error([
                    'You have selected to display a title in a component that does not ',
                    'support titles in NavigationView. Please remove the `title` configuration from your ',
                    'NavigationView item, or change it to a component that has a `setTitle` method.'
                ].join(''));
            }
            //</debug>

            var item = innerItems[innerItems.length - 2];
            this.$titleContainer.setTitle((item.getTitle) ? item.getTitle() : item.config.title);
        }
		this.fireBbarDisplay(this.getActiveItem());
        return this.getActiveItem();
    },
    popAndPush: function (view) {
    	if (this.child('component[routeId=' + view.routeId + ']')) return;
    	//如果出现闪屏 请重写逻辑 改为新视图销毁或返回的时候指定跳转
        this.pop();
        return this.add(view);
    },
    doResetActiveItem: function(innerIndex) {
        var me = this,
            innerItems = me.getInnerItems(),
            animation = me.getLayout().getAnimation();

        if (innerIndex > 0) {
            if (animation && animation.isAnimation) {
                animation.setReverse(true);
            }
            me.setActiveItem(innerIndex - 1);
            me.getNavigationBar().onViewRemove(me, innerItems[innerIndex-1], innerIndex);
        }
    },
    onBackButtonTap: function() {
    	var hash = this.getActiveItem().getBackHashUrl();
    	this.pop();
    	window.location.hash = hash||'home';
        this.fireEvent('back', this);
    }
});