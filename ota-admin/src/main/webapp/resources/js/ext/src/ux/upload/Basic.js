/**
 * @class Ext.ux.upload.Basic
 * @extends Ext.util.Observable
 * 
 * @author Harald Hanek (c) 2011-2012
 * @license http://harrydeluxe.mit-license.org
 */
Ext.define('Ext.ux.upload.Basic', {
    extend: 'Ext.util.Observable',
    autoStart: true,
    autoRemoveUploaded: true,
    uploadType:'multi-grid',//single-preview multi-preview multi-grid  
    
    statusQueuedText: '等待上传',
    statusUploadingText: '上传中 ({0}%)',
    statusFailedText: '失败',
    statusDoneText: '完成',
    statusInvalidSizeText: '文件超出大小限制',
    statusInvalidExtensionText: '无效的文件类型',
    

    configs: {
        uploader: {
            runtimes: '',
            url: '',
            browse_button: null,
            container: null,
            resize: '',
            flash_swf_url: cfg.getExtPath()+'/src/ux/upload/plupload/Moxie.swf',
            silverlight_xap_url: cfg.getExtPath()+'/src/ux/upload/plupload/Moxie.xap',
            filters: {
				mime_types : [
				  { title : "Image files", extensions : "jpg,gif,png" }
				],
				max_file_size : '1mb', 
				prevent_duplicates : true 
			},
            //chunk_size: '1mb', // @see http://www.plupload.com/punbb/viewtopic.php?id=1259
            chunk_size: null,
            unique_names: true,
            multipart: true,
            multipart_params: {},
            multi_selection: true,
            drop_element: null,
            required_features: null
        }
    },
    
    constructor: function(owner, config)
    {
        var me = this;
        me.owner = owner;
        me.success = [];
        me.failed = [];
        Ext.apply(me, config.listeners);
        me.uploaderConfig = Ext.apply(me, config.uploader, me.configs.uploader);
        if(me.uploadType=='multi-preview'){
	        var win = me.owner.up('window');
	        Ext.define('Ext.ux.upload.Model', {
	            extend: 'Ext.data.Model',
	            fields: ['id',
	                    'loaded',
	                    'name',
	                    'size',
	                    'src',
	                    'percent',
	                    'status',
	                    'msg','face']
	        });
	        me.store = Ext.create('Ext.data.Store', {
	            model: 'Ext.ux.upload.Model',
	            autoLoad: true, 
	            listeners: {
	                load: me.onStoreLoad,
	                remove: me.onStoreRemove,
	                update: me.onStoreUpdate,
	                scope: me
	            }
	        });
	        if(me.visitUrl!=''){
	        	me.store.setProxy({
	        		type: 'ajax',
			        noCache:true,
			        url: me.visitUrl,
			        reader: {
			            type: 'json',
			            rootProperty: 'data',
			            totalProperty:'totalSize'
			        }
	        	})
	        }
	        win.add({
	        	xtype:'dataview',
	        	store:me.store,
	        	overItemCls: 'hover', 
	        	tpl: [
	                '<tpl for=".">',
	                    '<div class="dataview-image-item">',
	                        '<img src="{[this.path(values.src)]}" width="125px" height="70px"  onerror="javascript:this.src=\''+cfg.getImgPath()+"/noimage.jpg"+'\'"/>',
	                        //'<span class="name">{[this.fm(values.name)]}</span>',
	                        '<span class="status {[this.fmc(values.status)]}">{[this.fm(values.status,values.face)]}</span>',
	                        '<span class="percent" style="width:{percent}%"></span>',
	                        '<i class="iconfont f20 green-color">{[this.cvx(values.status,values.face)]}</i>',
	                        '<i class="iconfont red-color bold f18">&#xe6b6;</i>',
	                    '</div>',
	                '</tpl>',{
	                	path:function(v){
	                		if(v&&v.indexOf('xian')!=-1){
	                			//v = util.pathImage(v,'500X280');
	                			//数据库存储500X280 如需要使用其他尺寸 使用替换
	                			return cfg.getPicCtx()+'/'+v;
	                		}else{
	                			return v;
	                		}
	                	},
	                	fmc:function(v){
	                		if(v==4){
	                			return 'suc';
	                		}else{
	                			return 'wait';
	                		}
	                	},
	                	fm:function(v,f){
	                		if(v==4){
	                			if(f==1){
	                				return '<span style="color:#53a93f">封面</span>';
	                			}else{
	                				return '设为封面';
	                			}
	                		}else{
	                			return '未上传';//v+'kb';
	                		}
	                	},
	                	cvx:function(v,f){
	                		var s = '';
	                		if(v==4&&f==1){
	                			s = '&#xe6ad;'
	                		}  
	                		return s;
	                	}
	                }
	            ],
	            itemSelector: 'div.dataview-image-item',
	            listeners:{
	            	itemclick :function(view, record, item, index, e, eOpts){
	            		if(e.target.tagName=='I'&&e.target.className.indexOf('red-color')!=-1){
	            			if(record.get('status')==4&&me.removeUrl!=''){
	            				Ext.Ajax.request({
									url:me.removeUrl+'?ID='+record.get('id'),
									success:function(response, opts){
										me.removeFile.apply(me,[record.get('id')]);
									}
								});
	            			}else{
	            				me.removeFile.apply(me,[record.get('id')]);
	            			}
		        			
		        		}
		        		if(e.target.tagName="SPAN"&&e.target.className.indexOf('suc')!=-1){
		        			if(record.get('status')==4&&me.faceUrl!=''){
	            				Ext.Ajax.request({
									url:me.faceUrl+'&ID='+record.get('id'),
									success:function(response, opts){
										me.store.load();
									}
								});
	            			}
		        		}
	            	}
	            }
	        });
	        me.actions = {
	        	start:{
	        		itemId:'start',
		        	text: config.uploadButtonText || '开始上传',
					disabled: true,
					iconCls: config.uploadButtonCls,
					handler: me.start,
					scope: me
		        }
	        };
        }
        
        
        me.callParent();
    },
    
    /**
     * @private
     */
    initialize: function()
    {
        var me = this;
        if(!me.initialized)
        {
            me.initialized = true;
            me.initializeUploader();
        }
    },
    
    /**
     * Destroys this object.
     */
    destroy: function()
    {
        this.clearListeners();
    },
    
    setUploadPath: function(path)
    {
        this.uploadpath = path;
    },
    
    removeAll: function()
    {
        this.store.data.each(function(record)
        {
            this.removeFile(record.get('id'));
        }, this);
    },
    
    removeUploaded: function()
    {
    	if(this.uploadType=='multi-preview'){
	        this.store.each(function(record)
	        {
	            if(record && record.get('status') == 5)
	            {
	                this.removeFile(record.get('id'));
	            }
	        }, this);
        }
    },
    
    removeFile: function(id)
    {
        var me = this,
            file = me.uploader.getFile(id);
        
        if(file)
            me.uploader.removeFile(file);
        else
            me.store.remove(me.store.getById(id));
    },
    
    cancel: function()
    {
        var me = this;
        me.uploader.stop();
        me.actions.start.setDisabled(me.store.data.length == 0);
    },
    
    start: function()
    {
        var me = this;
        me.fireEvent('beforestart', me);
        if(me.multipart_params)
        {
            me.uploader.settings.multipart_params = me.multipart_params;
        }
        me.uploader.start();
    },
    
    initializeUploader: function()
    {
        var me = this;

        if (!me.uploaderConfig.runtimes) {
            var runtimes = ['html5'];
            
            me.uploaderConfig.flash_swf_url && runtimes.push('flash');
            me.uploaderConfig.silverlight_xap_url && runtimes.push('silverlight');

            runtimes.push('html4');

            me.uploaderConfig.runtimes = runtimes.join(',');
        }
        //console.log(me.uploaderConfig);
        me.uploader = Ext.create('plupload.Uploader', me.uploaderConfig);
        //console.log(me.uploader);
        Ext.each(['Init',
                'ChunkUploaded',
                'FilesAdded',
                'FilesRemoved',
                'FileUploaded',
                'PostInit',
                'QueueChanged',
                'Refresh',
                'StateChanged',
                'BeforeUpload',
                'UploadFile',
                'UploadProgress',
                'Error'], function(v){
                    me.uploader.bind(v, eval("me._" + v), me);
                }, me);
        
        me.uploader.init();
        if(me.uploadType=='multi-preview'){ 
        	me.owner.up('toolbar').add(me.actions.start);
        }
    },
    
    updateProgress: function()
    {
        var me = this,
            t = me.uploader.total,
            speed = Ext.util.Format.fileSize(t.bytesPerSec),
            total = me.store.data.length,
            failed = me.failed.length,
            success = me.success.length,
            sent = failed + success,
            queued = total - success - failed,
            percent = t.percent;
        
        me.fireEvent('updateprogress', me, total, percent, sent, success, failed, queued, speed);
    },
    
    updateStore: function(v)
    {
        var me = this,
            data = me.store.getById(v.id);
        
        if(!v.msg)
        {
            v.msg = '';
        }
        if(data)
        {
            data.data = v;
            data.commit();
        }
        else
        {
            me.store.loadData([v], true);
        }
    },
    
    onStoreLoad: function(store, record, operation)
    {
        this.updateProgress();
    },
    
    onStoreRemove: function(store, records, index, isMove, eOpts)
    {
        var me = this,record=records[0];
        if(!store.data.length)
        {
            me.owner.up('toolbar').down('button[itemId=start]').setDisabled(true);
            //me.actions.removeUploaded.setDisabled(true);
            //me.actions.removeAll.setDisabled(true);
            me.uploader.total.reset();
            me.fireEvent('storeempty', me);
        }
        var id = record.get('id');
        Ext.each(me.success, function(v)
        {
            if(v && v.id == id)
                Ext.Array.remove(me.success, v);
        }, me);
        
        Ext.each(me.failed, function(v)
        {
            if(v && v.id == id)
                Ext.Array.remove(me.failed, v);
        }, me);
        me.updateProgress();
    },
    
    onStoreUpdate: function(store, record, operation)
    {
        record.data = this.fileMsg(record.data);
        this.updateProgress();
    },
    
    fileMsg: function(file)
    {
        var me = this;
        if(file.status && file.server_error != 1)
        {
            switch(file.status)
            {
                case 1:
                    file.msg = me.statusQueuedText;
                    break;
                case 2:
                    file.msg = Ext.String.format(me.statusUploadingText, file.percent);
                    break;
                case 4:
                    file.msg = file.msg || me.statusFailedText;
                    break;
                case 5:
                    file.msg = me.statusDoneText;
                    break;
            }
        }
        return file;
    },
    
    /**
     * Plupload EVENTS
     */
    _Init: function(uploader, data)
    {
        this.runtime = data.runtime;
        this.owner.enable(true); // button aktiv schalten
        this.fireEvent('uploadready', this);
    },
    
    // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    _BeforeUpload: function(uploader, file)
    {
        this.fireEvent('beforeupload', this, uploader, file);
    },
    
    _ChunkUploaded: function()
    {
    },
    
    _FilesAdded: function(uploader, files)
    {
        var me = this;
        
        
        if(me.uploaderConfig.multi_selection != true) 
        {
        	
            if(me.uploadType=='multi-preview'&&me.store.data.length == 1)
                return false;
            
            files = [files[0]];
            uploader.files = [files[0]];  
        }   
        if(me.uploadType=='multi-preview'){ 
	        //me.actions.removeUploaded.setDisabled(false);
	        //me.actions.removeAll.setDisabled(false);
	        me.owner.up('toolbar').down('button[itemId=start]').setDisabled(uploader.state == 2);
	        Ext.each(files, function(v)
	        {
	        	util.pluploadPreviewImage(v,function(src){
		        		v.src = src;
		        		me.updateStore(v);
		        	});
	        	
	        }, me);
        }
        

        if(me.fireEvent('filesadded', me, files) !== false)
        {
            if(me.autoStart && uploader.state != 2)
                Ext.defer(function()
                {
                    me.start();
                }, 300);
        }
    },
    
    _FilesRemoved: function(uploader, files)
    {
    	if(this.uploadType=='multi-preview'){ 
	        Ext.each(files, function(file)
	        {
	            this.store.remove(this.store.getById(file.id));
	        }, this);
        }
    },
    
    _FileUploaded: function(uploader, file, status)
    {
        var me = this,
            response = Ext.JSON.decode(status.response);
        if(response.result == 'success')
        {
            file.server_error = 0;
            me.success.push(file);
            me.fireEvent('fileuploaded', me, file, response);
        }
        else
        {
            if(response.message)
            {
                file.msg = '<span style="color: red">' + response.message + '</span>';
            }
            file.server_error = 1;
            me.failed.push(file);
            me.fireEvent('uploaderror', me, Ext.apply(status, {
                file: file
            }));
        }
        if(me.uploadType=='multi-preview'){
        	this.updateStore(file);
        }
    },
    
    _PostInit: function(uploader)
    {
    },
    
    _QueueChanged: function(uploader)
    {
    },
    
    _Refresh: function(uploader)
    {
    	if(this.uploadType=='multi-preview'){
	        Ext.each(uploader.files, function(v)
	        {
	            this.updateStore(v);
	        }, this);
        }
    },
    
    _StateChanged: function(uploader)
    {
        if(uploader.state == 2)
        {
            this.fireEvent('uploadstarted', this);
            if(this.uploadType=='multi-preview'){
	            //this.actions.cancel.setDisabled(false);
	            //this.actions.start.setDisabled(true);
	            this.owner.up('toolbar').down('button[itemId=start]').setDisabled(true);
            }
        }
        else
        {
            this.fireEvent('uploadcomplete', this, this.success, this.failed);
            if(this.autoRemoveUploaded)
                this.removeUploaded();
            
            if(this.uploadType=='multi-preview'){
	            //this.actions.cancel.setDisabled(true);
	            //this.actions.start.setDisabled(this.store.data.length == 0);
	            //this.owner.up('toolbar').down('button[itemId=start]').setDisabled(this.store.data.length == 0);
	            this.owner.up('toolbar').down('button[itemId=start]').setDisabled(true);
	            this.store.load();
            }
        }
    },
    
    _UploadFile: function(uploader, file)
    {
    },
    
    _UploadProgress: function(uploader, file)
    {
        var me = this,
            name = file.name,
            size = file.size,
            percent = file.percent; 
    
        me.fireEvent('uploadprogress', me, file, name, size, percent);

        if(file.server_error)
            file.status = 4;
        
        if(me.uploadType=='multi-preview'){
        	me.updateStore(file);
        }
    },
    
    _Error: function(uploader, data)
    {
        if(data.file)
        {
            data.file.status = 4;
            if(data.code == -600)
            {
                data.file.msg = Ext.String.format('{0}，文件大小不能超过{1}', data.message,uploader.getOption().filters.max_file_size);
            }
            else if(data.code == -700)
            {
                data.file.msg = Ext.String.format('{0}', data.message);
            }
            else
            {
                data.file.msg = Ext.String.format('{2} ({0}: {1})', data.code, data.details,
                        data.message);
            }
            this.failed.push(data.file);
            if(this.uploadType=='multi-preview'){
            	//this.updateStore(data.file);
            	util.alert(data.file.msg);
            }
        }
        this.fireEvent('uploaderror', this, data);
    }
});