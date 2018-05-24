Ext.define('Ext.ux.upload.FaceUpload', {
    extend: 'Ext.ux.upload.Button',
    alias: 'widget.faceupload',
    disabled: true,
    constructor: function(config){
    	var me = this;
    	config.uploader = {
    		url:config.url,
			autoStart: true,
			uploadType:'single-preview',
			multi_selection:false,
			unique_names:true
		};
		this.callParent([config]);
    },
    initComponent: function(){
    	var me = this;
    	me.on({
            filesadded: {
                fn: function(uploader, files)
                {
                    var me = this,upbtn=Ext.getCmp(me.id),hvbtn=Ext.getCmp('btn-user');
					hvbtn.showMenu();
					for(var i = 0, len = files.length; i<len; i++){
						var file_name = files[i].name; //文件名
						!function(i){
							util.pluploadPreviewImage(files[i],function(imgsrc){
								upbtn.el.down('img').dom.src=imgsrc;
								hvbtn.setIcon(imgsrc);
							})
					    }(i);
					}
					return true;
                },
                scope: me
            },
            uploadcomplete: {
                fn: function(uploader, success, failed)
                {
                	util.success('上传文件成功');
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