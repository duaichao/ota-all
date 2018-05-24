/**
 * Barebones iframe implementation. For serious iframe work, see the
 * ManagedIFrame extension
 * (http://www.sencha.com/forum/showthread.php?71961).
 */
function appr(goal, current, time){
  if(current < goal){
    return current + time;
  }else{
    return goal;
  }
}  
function point(x, y){
  this.x = x;
  this.y = y;
}
Ext.define('Ext.ux.IFrame', {
    extend: 'Ext.Component',

    alias: 'widget.uxiframe',

    loadMask: 'Loading...',

    src: 'about:blank',

	style: '',
	noMask:false,
    renderTpl: [
        '<iframe src="{src}" style="{style}" id="{id}-iframeEl" data-ref="iframeEl" name="{frameName}" width="100%" height="100%" frameborder="0"></iframe>'
    ],
    childEls: ['iframeEl'],

    initComponent: function () {
        this.callParent();
        this.frameName = this.frameName || this.id + '-frame';
    },

    initEvents : function() {
        var me = this;
        me.callParent();
        if(!this.noMask){
        	var box = this.el.appendChild({
        		id:'toppageloader',
        		tag:'div',
        		cls:'pjax-loader-bar'
        	},true);
        	var crp = new point(0.0, 0);
        	var p = new point(1.0, 100);

        	var itr = setInterval(function(){

        	  crp.x = appr(p.x, crp.x, 0.015);
        	  crp.y = appr(p.y, crp.y, 1.5);
        	  box.style.opacity = crp.x;
        	  box.style.width = crp.y+"%";
        	  if(crp.y==100){
        		  clearInterval(itr);
        		  itr = null;
        	  }
        	}, 1000/120);
        	/*this.el.appendChild({
        		id:'toppageloader',tag:'div',cls:'wrap go',
        		children:[{
        			tag:'div',cls:'loader bar',
        			children:[
        			    {tag:'div',cls:'blue'},
        			    {tag:'div',cls:'yellow'},
        			    {tag:'div',cls:'green'},
        			    {tag:'div',cls:'red'}
        			]
        		}]
        	});*/
			//this.el.mask('<i></i>页面载入中');
		}
        me.iframeEl.on('load', me.onLoad, me);
    },

    initRenderData: function() {
        return Ext.apply(this.callParent(), {
            src: this.src,
            style: this.style,
            frameName: this.frameName
        });
    },

    getBody: function() {
        var doc = this.getDoc();
        return doc.body || doc.documentElement;
    },

    getDoc: function() {
        try {
            return this.getWin().document;
        } catch (ex) {
            return null;
        }
    },

    getWin: function() {
        var me = this,
            name = me.frameName,
            win = Ext.isIE
                ? me.iframeEl.dom.contentWindow
                : window.frames[name];
        return win;
    },

    getFrame: function() {
        var me = this;
        return me.iframeEl.dom;
    },

    beforeDestroy: function () {
        this.cleanupListeners(true);
        this.callParent();
    },
    
    cleanupListeners: function(destroying){
        var doc, prop;

        if (this.rendered) {
            try {
                doc = this.getDoc();
                if (doc) {
                    Ext.get(doc).un(this._docListeners);
                    if (destroying) {
                        for (prop in doc) {
                            if (doc.hasOwnProperty && doc.hasOwnProperty(prop)) {
                                delete doc[prop];
                            }
                        }
                    }
                }
            } catch(e) { }
        }
    },

    onLoad: function() {
        var me = this,
            doc = me.getDoc(),
            fn = me.onRelayedEvent;
		
        if (doc) {
            try {
                // These events need to be relayed from the inner document (where they stop
                // bubbling) up to the outer document. This has to be done at the DOM level so
                // the event reaches listeners on elements like the document body. The effected
                // mechanisms that depend on this bubbling behavior are listed to the right
                // of the event.
                Ext.get(doc).on(
                    me._docListeners = {
                        mousedown: fn, // menu dismisal (MenuManager) and Window onMouseDown (toFront)
                        mousemove: fn, // window resize drag detection
                        mouseup: fn,   // window resize termination
                        click: fn,     // not sure, but just to be safe
                        dblclick: fn,  // not sure again
                        scope: me
                    }
                );
            } catch(e) {
                // cannot do this xss
            }

            // We need to be sure we remove all our events from the iframe on unload or we're going to LEAK!
            Ext.get(this.getWin()).on('beforeunload', me.cleanupListeners, me);

            //this.el.unmask();
            setTimeout(function(){if(me.el.down('#toppageloader'))me.el.down('#toppageloader').destroy();},1000);
            
            this.fireEvent('load', this);

        } else if (me.src) {

        	//this.el.unmask();
        	setTimeout(function(){if(me.el.down('#toppageloader'))me.el.down('#toppageloader').destroy();},1000);
            this.fireEvent('error', this);
        }


    },

    onRelayedEvent: function (event) {
        // relay event from the iframe's document to the document that owns the iframe...

        var iframeEl = this.iframeEl,

            // Get the left-based iframe position
            iframeXY = iframeEl.getTrueXY(),
            originalEventXY = event.getXY(),

            // Get the left-based XY position.
            // This is because the consumer of the injected event will
            // perform its own RTL normalization.
            eventXY = event.getTrueXY();

        // the event from the inner document has XY relative to that document's origin,
        // so adjust it to use the origin of the iframe in the outer document:
        event.xy = [iframeXY[0] + eventXY[0], iframeXY[1] + eventXY[1]];

        //event.injectEvent(iframeEl); // blame the iframe for the event...

        event.xy = originalEventXY; // restore the original XY (just for safety)
    },

    load: function (src) {
        var me = this,
            text = me.loadMask,
            frame = me.getFrame();

        if (me.fireEvent('beforeload', me, src) !== false) {
            if (text && me.el) {
                //me.el.mask(text);
            }

            frame.src = me.src = (src || me.src);
        }
    }
});
