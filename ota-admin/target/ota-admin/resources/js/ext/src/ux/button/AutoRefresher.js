/**
 * @class Ext.ux.button.AutoRefresher
 * @extends Ext.button.Split
 * @author tz
 * @email atian25@qq.com
 * @date 2011-05-21
 * @version 1.0
 * @forum http://www.sencha.com/forum/showthread.php?134350-Ext.ux.button.AutoRefresher
 */
Ext.define('Ext.ux.button.AutoRefresher', {
    alias: 'widget.autorefresher',
    extend: 'Ext.button.Split',
    iconCls: 'x-tbar-loading',
    btnText: '刷新数据',
    minuteText: '分',
    secondText: '秒',
    
    /**
     * 是否显示自动刷新倒计时
     */
    showCountDown: true,

    /**
     * 刷新时间
     */
    refreshInterval:0,
    
    //@private
    lastInterval:null,
    
    menu:{
        items:[
            {text:'手动刷新',itemId:'btn0',value:0},
	        {text:'每15秒',itemId:'btn15',value:15},
	        {text:'每30秒',itemId:'btn30',value:30},
	        {text:'每1分钟',itemId:'btn60',value:60},
	        {text:'每3分钟',itemId:'btn180',value:180},
	        {text:'每5分钟',itemId:'btn300',value:300}
        ]
    },
    
    initComponent: function() {
        this.addEvents(
            /**
             * @event refresh 刷新事件
             * 达到触发间隔的时候发布.
             * @param {AutoRefresher} refresher
             */
            'refresh'
        );
        this.callParent(arguments)
        
        this.buildTask();
        
        this.on({
            scope:this,
            'afterrender':function(c){
                this.menu.on('click',function(m,item,e){
                    this.reconfigure(item.value)
                },this)
                this.reconfigure(this.refreshInterval)
            },
            'click':function(){
                this.fireEvent('refresh',this)
                this.reconfigure(this.refreshInterval)
            }
        })
    },
    /**
     * 建立计划任务
     * @private
     */
    buildTask: function(){
        this.runner = new Ext.util.TaskRunner()
        this.clockTask = {
            countDown:0,
            interval: 1000,
            scope: this,
            run: function(){
                this.clockTask.countDown--
                if(this.clockTask.countDown<0){
                    this.clockTask.countDown = this.refreshInterval
                }
                this.refreshCountDown(this.clockTask.countDown)
            }
        }
        
        this.autoRefreshTask = {
            interval: this.refreshInterval*1000,
            scope: this,
            run: function(count){
                if(count>1){
                    this.fireEvent('refresh',this)
                }
            }
        }
    },
    
    /**
     * 刷新倒计时显示
     * @param {Number} countDown 剩余时间
     */
    refreshCountDown: function(countDown){
        var minutes = Math.floor(countDown/60)
        var seconds = Math.floor(countDown%60)
        this.setText(this.btnText+'('+(minutes>0?minutes+this.minuteText:'') + seconds + this.secondText+')')
    },
    
    /**
     * 重置计时器
     */
    reconfigure: function(value){
        this.runner.stopAll();
        this.setText(this.btnText);
        if(value>0){
            //需要显示倒计时
            if(this.showCountDown){
                this.refreshInterval = value
                this.clockTask.countDown = value;
                this.runner.start(this.clockTask);
            }
            this.autoRefreshTask.interval = value * 1000
            this.runner.start(this.autoRefreshTask);
        }
    },
    
    /**
     * 暂停/启动计时器
     * @param {Boolean} stop 
     */
    toggleTrigger: function(stop){
        if(stop){
            this.lastInterval = this.refreshInterval
            this.reconfigure(0)
        }else{
            if(this.lastInterval>0){
                this.reconfigure(this.lastInterval)
            }
        }
    },
    
    /**
     * 销毁对象
     */
    onDestroy:function(){
        this.runner.stopAll();
        this.clockTask = null;
        this.autoRefreshTask = null;
        this.runner = null;
        this.callParent(arguments);
    },
    
    author:'tz'
})