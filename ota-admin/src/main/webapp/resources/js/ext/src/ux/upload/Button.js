Ext.define('Ext.ux.upload.Button', {
    extend: 'Ext.button.Button',
    alias: 'widget.uploadbutton',
    requires: ['Ext.ux.upload.Basic','Ext.ux.upload.plugin.Window'],
    disabled: true,
    autoInit:true,
    constructor: function(config)
    {
        var me = this;
        config = config || {};
        Ext.applyIf(config.uploader, {
            browse_button: config.id || Ext.id(me)
        });
        me.callParent([config]);
    },
    
    initComponent: function()
    {
        var me = this,
            e;
        me.uploader = me.createUploader();
        if(me.uploader.drop_element && (e = Ext.getCmp(me.uploader.drop_element)))
        {
            e.addListener('afterrender', function()
                {
                       me.uploader.initialize();
                },
                {
                    single: true,
                    scope: me
                });
        }
        else
        {
            me.listeners = {
                afterrender: {
                    fn: function()
                    {
                    	if(me.autoInit){
                        	me.uploader.initialize();
                        }
                    },
                    single: true,
                    scope: me
                }
            };
        }
        
        me.relayEvents(me.uploader, ['beforestart',
                'uploadready',
                'uploadstarted',
                'uploadcomplete',
                'uploaderror',
                'filesadded',
                'beforeupload',
                'fileuploaded',
                'updateprogress',
                'uploadprogress',
                'storeempty']);
        me.callParent();
    },
    
    /**
     * @private
     */
    createUploader: function()
    {
    	
        return Ext.create('Ext.ux.upload.Basic', this, Ext.applyIf({
            listeners: {}
        }, this.config));
    }
});