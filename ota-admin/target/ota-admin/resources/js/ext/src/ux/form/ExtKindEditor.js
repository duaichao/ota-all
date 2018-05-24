Ext.define('Ext.ux.form.ExtKindEditor', {
    extend: 'Ext.form.field.TextArea',
    alias: 'widget.extkindeditor',//xtype名称
    border:false,
    liquidLayout: false,
    tools:[
		'source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'code', 'cut', 'copy', 'paste',
		'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
		'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
		'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
		'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
		'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 'multiimage',
		'flash', 'media', 'insertfile', 'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
		'anchor', 'link', 'unlink'
	],
	offsetWidth:0,
	defaultValue:'',
	offsetHeight:0,
    initComponent: function () {
        var me = this;
        this.html = "<textarea id='" + this.getId() + "-input' name='" + this.name + "'></textarea>";
        this.callParent(arguments);
        this.on("boxready", function (t,w,h) {
	        w = w || t.getWidth();
	        h = h || t.getHeight();
            this.inputEL = Ext.get(this.getId() + "-input");
            this.editor = KindEditor.create('#' + this.getId() + '-inputEl', {
                height: h-18-me.offsetHeight,//有底边高度，需要减去
                width: w-me.offsetWidth,//宽度需要减去label的宽度
                basePath: cfg.getJsPath()+'/kindeditor/',
                uploadJson: cfg.getCtx()+'/kindeditor/upload_json.jsp',//路径自己改一下
                fileManagerJson: cfg.getCtx()+'/kindeditor/file_manager_json.jsp',//路径自己改一下
                resizeType: 0,
                wellFormatMode: true,
                newlineTag: 'br',
                allowFileManager: true,
                allowPreviewEmoticons: true,
                allowImageUpload: true,
                items: me.tools,
		        afterBlur:function(){ 
		            this.sync(); 
		        }           
            });
           	this.setValue(this.defaultValue);
        });
        this.on("resize", function (t, w, h) {
            this.editor.resize(w - this.offsetWidth, h-18-this.offsetHeight);
        });
    },
    setValue: function (value) {
        if (this.editor) {
            this.editor.html(value);
        }
    },
    reset: function () {
        if (this.editor) {
            this.editor.html('');
        }
    },
    setRawValue: function (value) {
        if (this.editor) {
            this.editor.text(value);
        }
    },
    getValue: function () {
        if (this.editor) {
            return this.editor.html();
        } else {
            return ''
        }
    },
    getRawValue: function () {
        if (this.editor) {
            return this.editor.html();
        } else {
            return ''
        }
    }
});
