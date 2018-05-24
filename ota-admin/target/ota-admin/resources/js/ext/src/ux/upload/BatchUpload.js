Ext.define('Ext.ux.upload.BatchUpload', {
    extend: 'Ext.ux.upload.Button',
    alias: 'widget.batchupload',
    xtype:'batchupload',
    disabled: true,
    autoInit:true,
    multipart_params:{},
    faceUrl:'',
    removeUrl:'',
    visitUrl:'',
    url:'',
    constructor: function(config){
    	var me = this;
    	config.uploader = {
    		url:config.url,
    		faceUrl:config.faceUrl,
    		visitUrl :config.visitUrl,
    		removeUrl:config.removeUrl,
    		multipart_params:config.multipart_params,
			autoStart: false,
			uploadType:'multi-preview',
			multi_selection:true,
			unique_names:true//生成唯一文件名
		};
		/*config.plugins = [{
            ptype: 'ux.upload.window',
            title: util.windowTitle('','批量上传图片','icon-up-small'),
            width: 700,
            height: 350
		}];	*/
		this.callParent([config]);
    },
    initComponent: function(){
    	var me = this;
    	me.on({
            uploadcomplete: {
                fn: function(uploader, success, failed)
                {
                	util.success('上传文件成功');
                	me.up('toolbar').down('button[itemId=start]').setDisabled(true);
                },
                scope: me
            },
			uploaderror: {
				fn: function(uploader, data)
                {
                    var err = [];
					Ext.each(data,function(d){
						if(d.code){
							err.push(d.file.msg);
						}
					});
					if(err.length>1)
						util.alert(err.join('<br>'));
                },
                scope: me
			}
		});
        this.callParent();
    }
});