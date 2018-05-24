Ext.define('Weidian.view.ux.navigation.Bar', {
    extend: 'Ext.navigation.Bar',
    alternateClassName: 'ExpandNavigationBar',
    xtype: 'ExpandNavigationBar',
    config: {
    	tmpExpandItems:null,
		titleAlign: 'center'
    },
    applyInitialItems: function (items) {
        var me = this,
            titleAlign = me.getTitleAlign(),
            defaults = me.getDefaults() || {};

        me.initialItems = items;

        me.leftBox = me.add({
            xtype: 'container',
            style: 'position: relative',
            cls: Ext.baseCSSPrefix + 'titlebar-left',
            layout: {
                type: 'hbox',
                align: 'center'
            },
            listeners: {
                resize: 'refreshTitlePosition',
                scope: me
            }
        });

        me.spacer = me.add({
            xtype: 'component',
            style: 'position: relative',
            cls: Ext.baseCSSPrefix + 'titlebar-center',
            flex: 1,
            listeners: {
                resize: 'refreshTitlePosition',
                scope: me
            }
        });

        me.rightBox = me.add({
            xtype: 'container',
            style: 'position: relative',
            cls: Ext.baseCSSPrefix + 'titlebar-right',
            layout: {
                type: 'hbox',
                align: 'center'
            },
            listeners: {
                resize: 'refreshTitlePosition',
                scope: me
            }
        });

        switch (titleAlign) {
            case 'left':
                me.titleComponent = me.leftBox.add({
                    xtype: 'container',
                    cls: Ext.baseCSSPrefix + 'title-align-left',
                    hidden: defaults.hidden
                });
                me.refreshTitlePosition = Ext.emptyFn;
                break;
            case 'right':
                me.titleComponent = me.rightBox.add({
                    xtype: 'container',
                    cls: Ext.baseCSSPrefix + 'title-align-right',
                    hidden: defaults.hidden
                });
                me.refreshTitlePosition = Ext.emptyFn;
                break;
            default:
                me.titleComponent = me.add({
                    xtype: 'container',
                    hidden: defaults.hidden,
                    centered: true
                });
                break;
        }

        me.doAdd = me.doBoxAdd;
        me.remove = me.doBoxRemove;
        me.doInsert = me.doBoxInsert;
    },
    /**
     * @override Ext.TitleBar
     */
    updateTitle: function(newTitle) {
		if(newTitle=='&nbsp;'||newTitle=='')return;
		this.getItems();
		var me = this;
    	//util.log('debug：======updateTitle newTitle=======');
        //util.log(newTitle);
        //util.log('debug：======updateTitle newTitle=======');
    	this.titleComponent.removeAll(true);
    	this.titleComponent.setHtml('');
    	this.titleComponent.removeCls('x-title');
    	if(Ext.isString(newTitle)){
			this.titleComponent.addCls('x-title');
    		this.titleComponent.setHtml(newTitle);

    	}else{
			newTitle.cls = (newTitle.cls||'')+' title-cmp';
			//newTitle.hidden=true;
			newTitle = this.titleComponent.add(newTitle);
    		//setTimeout(function(){newTitle.show();},2000);
    	}
        if (this.isPainted()) {
            this.refreshTitlePosition();
        }
    },
    /**
     * @override Ext.navigation.Bar
     */
    onViewAdd: function(view, item) {
        var me = this,
            backButtonStack = me.backButtonStack,
            hasPrevious, title,backHide;

        me.endAnimation();

        title = (item.getTitle) ? item.getTitle() : item.config.title;
        backHide = (item.backHide) ? item.getBackHide() : item.config.backHide;
        //me.resetExpandItems(item,view);
        
        
        backButtonStack.push(title || '&nbsp;');
        hasPrevious = backButtonStack.length > 1&&!backHide;

        
        
        me.doChangeView(view, hasPrevious, false);
    },
    /**
     * @private
     */
    onViewRemove: function(view,item) {
        var me = this,
            backButtonStack = me.backButtonStack,
            hasPrevious,backHide;

        me.endAnimation();
        backButtonStack.pop();
        
        backHide = (item.backHide) ? item.getBackHide() : item.config.backHide;
        hasPrevious = backButtonStack.length > 1&&!backHide;
        
        //me.resetExpandItems(item,view);
        
        me.doChangeView(view, hasPrevious, true);
    },
    resetExpandItems:function(item,view){
    	var me = this,tmpExpandItems = me.getTmpExpandItems();
    	var barCls = (item.barCls) ? item.getBarCls() : item.config.barCls;
    	//this.removeCls(['green','materialize-red','blue','teal','deep-orange']);
    	
        if(barCls){
        	this.addCls(barCls);
        }
        if(tmpExpandItems&&tmpExpandItems.length>0){
	    	Ext.Array.each(tmpExpandItems,function(bar,index){
	    		me.remove(bar,true);
	    	});
	    	me.setTmpExpandItems(null);
        }
        var title = (item.getExpandTitle) ? item.getExpandTitle() : item.config.expandTitle,
        	expandItems = (item.getExpandItems) ? item.getExpandItems() : item.config.expandItems;
        if(title&&!Ext.isString(title)){
        	title._currView = item;
        }
        if(expandItems&&expandItems.length>0){
        	var tmpArr = [];
        	Ext.Array.each(expandItems,function(bar,index){
        		bar._currView = item;
        		/**
    			 * 设置handler作用域
    			 * 默认controller 
    			 * view 当前视图作用域
    			 */
        		if(bar.scope&&bar.scope=='view'){
        			bar.scope = item;
        		}
        		tmpArr.push(me.add(bar));
        	});
        	me.setTmpExpandItems(tmpArr);
        	item._tmpExpandItems = tmpArr;
        	item.fireEvent('barInit',view,item,me,tmpArr);
        }
    }
});