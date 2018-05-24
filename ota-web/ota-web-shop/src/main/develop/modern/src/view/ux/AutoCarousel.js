Ext.define('Weidian.view.ux.AutoCarousel', {
	extend : 'Ext.Carousel',
    alias : 'autocarousel',
    xtype: 'autocarousel',
    config : {
        data : null,
        start : false,
        duration : 5000,
        url : null,
        timer : null,
        //是否自动循环
        cycle : true
    },
    
    
    initialize: function() {
        this.callParent();

        this.element.on({
            scope      : this,
            touchstart : 'onPress',
            touchend   : 'onRelease'
        });
    },
    
    updateUrl : function(newurl){
        if(newurl){
            //event to load data , you can use ajax to complete with url
            this.fireEvent('urlChange',this,newurl);
        }
    },
    
    updateData : function(newdata){
        if(newdata){
            this.add(newdata);
        }else{
            this.removeAll(true,true);
        }
    },
    
    updateCycle : function(cycle){
        if(cycle){
            this.setStart(true);
        }
    },
    
    updateStart : function(newstart){
        var me = this,
            timer = me.getTimer(),
            duration = me.getDuration();
        if(newstart){
            clearInterval(timer);
            timer = setInterval(function(){
                me.next();
            },duration);
            me.setTimer(timer);
        }else{
            clearInterval(timer);
        }
    },
    
    onPress : function(){
        this.setStart(false);
    },
    
    onRelease : function(){
        this.setStart(true);
    },
    
    next: function() {
        this.setOffset(0);
        if(!this.innerItems){this.setStart(false);return;}
        if (this.activeIndex === this.innerItems.length-1) {
            this.setActiveItem(0);
            return this;
        }
        this.animationDirection = -1;
        this.setOffsetAnimated(-this.itemLength);
        return this;
    }
});