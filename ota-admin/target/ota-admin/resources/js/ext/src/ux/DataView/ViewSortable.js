/**
/**
 * Plugin (ptype = 'viewsortable') that makes a view "Sortable"
 * via drag/drop.
 */
Ext.define('Ext.ux.DataView.ViewSortable', {

    extend: 'Ext.AbstractPlugin',

    alias: 'plugin.viewsortable',

    mixins: {
        observable: 'Ext.util.Observable'
    },

    config: {

        /** 
         * the event which the plugin should listen on the view,
         *
         * at this moment the plugin initiializes it's drag and drop zones
         */
        eventName: 'render',

        /**
         * the class which will be added to the dragged node, then it enters a
         * valid target
         *
         */
        highlightClass: 'dd-row-highlight-class'
    },

    //logger: log4javascript.getLogger('Utils.plugin.Sortable'),
    logger: {
        debug: function() {return null;}
    },

    /**
     * initialize the plugin
     *
     * @private
     */
    init : function(view) {
        this.mixins.observable.constructor.call(this);

        this.logger.debug('sortable plugin init:', view);
        this.logger.debug('using event:', this.getEventName());

        this.view = view;
        view.on(this.getEventName(), this.initDragDrop, this);
    },

    /**
     * initialize the drag and drop behavior on the view
     *
     * @private
     */
    initDragDrop : function() {

        this.logger.debug('init drag and drop');

        var me = this;
        var v = this.view;

        var dragZone = Ext.create('Ext.dd.DragZone', this.view.getEl(), {
			animRepair:true,
			containerScroll:true,
            //      On receipt of a mousedown event, see if it is within a DataView node.
            //      Return a drag data object if so.
            getDragData: function(e) {

                // Use the DataView's own itemSelector (a mandatory property) to
                // test if the mousedown is within one of the DataView's nodes.
                var sourceEl = e.getTarget(v.itemSelector, 10);

                // If the mousedown is within a DataView node, clone the node to produce
                // a ddel element for use by the drag proxy. Also add application data
                // to the returned data object.
                if (sourceEl) {
                    d = sourceEl.cloneNode(true);
                    d.id = Ext.id();
                    return {
                        ddel: d,
                        sourceEl: sourceEl,
                        repairXY: Ext.fly(sourceEl).getXY(),
                        sourceStore: v.store,
                        draggedRecord: v.getRecord(sourceEl)
                    };
                }
            },

            // Provide coordinates for the proxy to slide back to on failed drag.
            // This is the original XY coordinates of the draggable element captured
            // in the getDragData method.
            getRepairXY: function() {
                return this.dragData.repairXY;
            }
        });

        var dropZone = Ext.create('Ext.dd.DropZone', this.view.getEl(), {

            // If the mouse is over a grid row, return that node. This is
            // provided as the "target" parameter in all "onNodeXXXX" node event handling functions
            getTargetFromEvent: function(e) {
                return e.getTarget(v.itemSelector);
            },

            // On entry into a target node, highlight that node.
            onNodeEnter : function(target, dd, e, data){
                Ext.fly(target).addCls(me.getHighlightClass());
            },

            // On exit from a target node, unhighlight that node.
            onNodeOut : function(target, dd, e, data){
                Ext.fly(target).removeCls(me.getHighlightClass());
            },

            // While over a target node, return the default drop allowed class which
            // places a "tick" icon into the drag proxy.
            onNodeOver : function(target, dd, e, data){
                return Ext.dd.DropZone.prototype.dropAllowed;
            },

            // On node drop we can interrogate the target to find the underlying
            // application object that is the real target of the dragged data.
            // In this case, it is a Record in the GridPanel's Store.
            // We can use the data set up by the DragZone's getDragData method to read
            // any data we decided to attach in the DragZone's getDragData method.
            onNodeDrop : function(target, dd, e, data){
                me.logger.debug('onNodeDrop target', target);
                me.logger.debug('onNodeDrop data', data);
				
                var store = v.getStore();

                var index = v.indexOf(target);
                me.logger.debug('onNodeDrop dropped on', index);
                
                
                //console.log("当前："+(index+1)+"=="+data.draggedRecord.get('NO'));
                
                var r = store.getAt(index);
                data.sourceStore.remove(data.draggedRecord);
                store.insert(index, [data.draggedRecord]);

                me.fireEvent('aftersort', me, data.draggedRecord, r,index);

                // Ext.Msg.alert('Drop gesture', 'Dropped Record id ' + data.draggedRecord.id +
                //     ' on Record id ' + r.id);
                return true;
            }
        });
    }
});