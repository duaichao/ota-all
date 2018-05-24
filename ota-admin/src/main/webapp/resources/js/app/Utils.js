Ext.define('app.Utils', {
    alternateClassName: 'util',
    statics:{
    	headNavGlyphMap:function(glyph){
    		if(glyph){
    			glyph = glyph.split('@')[0];
    		}
    		var map = {
    				'xe600':'&#xe7a0;',
    				'xe606':'&#xe80b;',
    				'xe609':'&#xe89d;',
    				'xe60a':'&#xe6b5;',
    				'xe604':'&#xe7e1;',
    				'xe60b':'&#xe6d5;',
    				'xe6a4':'&#xe79d;',
    				'xe607':'&#xe898;',
    				'xe602':'&#xe6b3;',
    				'xe603':'&#xe6b4;'
    		};
    		return map[glyph];
    	},
    	format:Ext.util.Format,
		formatBank:function(bankno){
			bankno = bankno ||'';
			bankno = bankno.replace(/[ ]/g,"");
			var num = /^\d*$/;  //全数字
			if (!num.exec(bankno)) {
				bankno = bankno.replace(/\s/g,'').replace(/(\d{4})(?=\d)/g,"$1 ");
				return '<span style=\'color:#f40;font-size:14px;font-weight:bold;\' qclass="no-tip" qtip="<span class=\'alt-tip-bg\'>注意：可能是虚假卡号</span>"><img style="cursor:pointer;" src="'+Ext.ctx+'/images/icons/default/exclamation-shield.png" />'+bankno+'</span>';
			}else{
				bankno = bankno.replace(/\s/g,'').replace(/(\d{4})(?=\d)/g,"$1 ");
				return '<span style=\'font-size:14px;font-weight:bold;\'>'+bankno+'</span>';
			}
		},
		getBirthDay:function(iIdNo){
			var tmpStr = "";
			var idDate = "";
			var tmpInt = 0;
			var strReturn = "";


			if ((iIdNo.length != 15) && (iIdNo.length != 18)) {
				strReturn = "证件错误";
				return strReturn;
			}

			if (iIdNo.length == 15) {
				tmpStr = iIdNo.substring(6, 12);
				tmpStr = "19" + tmpStr;
				tmpStr = tmpStr.substring(0, 4) + "-" + tmpStr.substring(4, 6) + "-" + tmpStr.substring(6)
				return tmpStr;
			}else {
				tmpStr = iIdNo.substring(6, 14);
				tmpStr = tmpStr.substring(0, 4) + "-" + tmpStr.substring(4, 6) + "-" + tmpStr.substring(6)
				return tmpStr;
			}
		},
		formatPhone:function(str){
			str = str ||'';
			var te = '',pe;
			str = util.format.trim(str);
			if(str.length==11){
				return Utils.formatMobile(str);
			}
			if(/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/.test(str)){
				if(str.indexOf('-')!=-1){
					te = str.split('-')[0]+'-';
					str = str.split('-')[1];
					pe = te+str.substring(0,4)+" "+str.substring(4,8);
				}
			}else{
				pe = str;
			}
			return '<span style=\'font-size:14px;font-weight:bold;\'>'+pe+'</span>';
		},
		formatMobile:function(str){
			str = str || '';
			str = util.format.trim(str);
			if(/^(?:13\d|15[012789]|18\d)-?\d{5}(\d{3}|\*{3})$/.test(str)){
				str = str.replace(/(^\s*)|(\s*$)/g, "");
				return '<span style=\'font-size:14px;font-weight:bold;\'>'+str.substring(0,3)+" "+str.substring(3,7)+" "+str.substring(7,11)+'</span>';
			}else{
				return '<span style=\'font-size:14px;font-weight:bold;\'>'+str+'</span>';
			}
		},
		getFileExt :function(url){
			//不带点
			url = url || '';
			return url.substring(url.lastIndexOf('.') + 1);
			//return url.split('.').pop().toLowerCase();
		},
		imageLoader:function(src,callback,error){     
		    var img = new Image();  
		    callback = callback || Ext.emptyFn;
		    error = error || Ext.emptyFn;
		    if(Ext.browser.is.IE){  
		        img.onreadystatechange =function(){  
		            if(img.readystate=="complete"||img.readystate=="loaded"){  
		            	callback(img);  
		            } 
		        }         
		    }else{  
		        img.onload=function(){  
		            if(img.complete==true){  
		            	callback(img);  
		            }
		        }         
		    }     
		    //如果因为网络或图片的原因发生异常，则显示该图片  
		    img.onerror=error;  
		    img.src=src;  
		},
		pathImage :function(url,size){
			size = size || '500X280';
			url = url || '';
			var ext = util.getFileExt(url);
			if(url.indexOf(size)!=-1){
				return url;
			}
			return url.replace('500X280','').replace('125X70','').replace('.'+ext,'-'+size+'.'+ext);
		},
		glyphToFont :function(glyph){
			return '&#'+glyph.split('@')[0]+';';
		},
		getFont:function(code,fc,tit){
			return '<i class="iconfont '+(fc||'f12')+'" data-qtip="'+(tit||'')+'">&#'+code+';</i>';
		},
    	moneyFormat :function(value,fs,mask,fix){
    		var h = [];
    		if(mask){
    			h.push('<a class="money-body">');
    		}
    		h.push('<span class="money-color '+(fs||'f16')+'">');
    		h.push('<dfn>￥</dfn>');
    		if(fix){
    			value = util.format.number(value||0,'0.00');
    		}
    		h.push(value);
    		h.push('</span>');
    		
    		if(mask){
    			h.push('</a>');
    		}
    		return h.join('');
    	},
    	formatMoney:function(value,toggle,fs){
    		if(toggle){
    			toggle = value>0?'green-color':'money-color';
    		}
    		return '<span class="'+toggle+' '+(fs||'f16')+'"><dfn>￥</dfn>'+util.format.number(value||0,'0.00')+'</span>'
    	},
    	cookie:Ext.state.Manager.getProvider(),
    	delay:function(callback,delay){
    		new Ext.util.DelayedTask(callback).delay(delay||3000);
    	},
    	//时间字符串转换
    	timeAgoInWords:function(dateStr){
    		dateStr = dateStr || '';
			var date = Ext.Date.parse(dateStr, "Y-m-d H:i:s", true);
			if(!date)return dateStr;
			var	now = new Date(),
				mo = Ext.Date.diff( date, now, 'mo' ),
				d = Ext.Date.diff( date, now, 'd' ),
	            h = Ext.Date.diff( date, now, 'h' ),
	            mi = Ext.Date.diff( date, now, 'mi' ),
	            s = Ext.Date.diff( date, now, 's' ),
	            str;
			if(mo>0){return mo+'月前';}
			if(d>0){return d+'天前';}
			if(h>0){return h+'小时前';}
			if(mi>0){return mi+'分钟前';}
			if(s>0){return s+'秒前';}
			
			return '<span data-qtip="'+dateStr+'">'+Ext.Date.format(date, 'Y/m/d')+'</span>';
    	},
    	_msgV1:function(type,format){
    		var defaultConfig = {
    			position: 'tc',
				cls: '',
				minWidth:160,
				//minHeight:100,
				paddingX:0,
				paddingY:3,
				iconCls: 'f16 icon-help-circled',
				closable: false,
				resizable:false,
				autoCloseDelay: 4000,
				slideBackDuration: 500,
				slideInAnimation: 'bounceOut',
				slideBackAnimation: 'easeIn',
    			title: '小提示',
				html: format||''
    		};
    		top.Ext.require('Ext.ux.Notification',function(){
		    	top.Ext.create('notification',Ext.apply(defaultConfig,{
	        		cls:type
	        	})).show();
		    });
        	
        },
        _msg:function(type,format){
        	if(Ext.get('notification')){
        		return;
        	}
    		var dh = Ext.DomHelper,
    			notification = {
				    tag:'div',
				    id:'notification',
				    cls: 'ex-popoup-hint '+type,
				    children: [    
				        {tag: 's'},
	    				{tag: 'span', html: format||''} 
				    ]
				};
    		var nf = dh.append(Ext.getBody(),notification,true);
    			nf.center();
    		//Ext.getBody().mask();
    		util.delay(function(){
    			//Ext.getBody().unmask();
    			if(Ext.get('notification')){
    				Ext.get('notification').destroy();
    			}
    		},3000);
        },
    	alert:function(format){
    		format = Ext.String.format.apply(String, Array.prototype.slice.call(arguments, 0));
    		//console.log(self.location==top.location);
    		if(self.location==top.location){
        		util._msg('exph-war',format);
        	}else{
        		top.util._msg('exph-war',format);
        	}
        },
        success:function(title,format){
        	format = Ext.String.format.apply(String, Array.prototype.slice.call(arguments, 0));
        	if(self.location==top.location){
        		util._msg('exph-suc',format);
        	}else{
        		top.util._msg('exph-suc',format);
        	}
        },
        error:function(title,format){
        	format = Ext.String.format.apply(String, Array.prototype.slice.call(arguments, 0));
        	if(self.location==top.location){
        		util._msg('exph-err',format);
        	}else{
        		top.util._msg('exph-err',format);
        	}
        },
        windowTitle:function(icon,title,cls){
        	return [
        		//'<span style="font-size:16px;color:#fff">',
        		'<i class="iconfont '+cls+'" style="font-size:18px;">'+icon+'</i> ',
        		title,
        		//'</span>'
        	].join('');
        },
	    pluploadPreviewImage :function(file,callback){
	    	if(!file || !/image\//.test(file.type)) return; //确保文件是图片
			if(file.type=='image/gif'){//gif使用FileReader进行预览,因为mOxie.Image只支持jpg和png
				var fr = new mOxie.FileReader();
				fr.onload = function(){
					callback(fr.result);
					fr.destroy();
					fr = null;
				}
				fr.readAsDataURL(file.getSource());
			}else{
				var preloader = new mOxie.Image();
				preloader.onload = function() {
					preloader.downsize( 300, 300 );//先压缩一下要预览的图片,宽300，高300
					var imgsrc = preloader.type=='image/jpeg' ? preloader.getAsDataURL('image/jpeg',80) : preloader.getAsDataURL(); //得到图片src,实质为一个base64编码的数据
					callback && callback(imgsrc); //callback传入的参数为预览图片的url
					preloader.destroy();
					preloader = null;
				};
				preloader.load( file.getSource() );
			}	
	    },
        uploadAttachment:function(title,url,entityId,callback){
        	entityId = entityId || '';
        	if(entityId!=''){
        		url+='?entityId='+entityId
        	}
        	var win;
        	top.Ext.require('Ext.ux.swfupload.SwfPanel',function(){
        		win =　top.Ext.create('Ext.window.Window',{
		   			title: util.windowTitle('&#xe641;',title||'上传附件',''),
		   			id:'swfUploadWindow',
		   			width:'85%',
		   			height:'85%',
		   			draggable:false,
					resizable:false,
					modal:true,
		   			bodyStyle:'background:#fff;padding:10px;font-size:16px;color:#bbb;',
		   			layout:'fit',
		   			items:[{
		   				xtype:'swfpanel',
		   				upload_url:url
		   			}],
		   			listeners:{
		   				beforeclose:function(){
		   					var sp = this.down('swfpanel'),ds = sp.store;
							for(var i=0;i<ds.getCount();i++){
								var record =ds.getAt(i);
								var file_id = record.get('id');
								sp.swfupload.cancelUpload(file_id,false);			
							}
							ds.removeAll();
							sp.swfupload.uploadStopped = false;
							
							callback();
		   				}
		   			},
		   			buttons:[{
		   				xtype:'panel',
		   				bodyStyle:'background:transparent;',
		   				html:'图片最佳分辨率：1920 × 450'
		   			}]
		   		}).show();
        	});
        },
        uploadSingleAttachment :function(title,callback,tips){
        	tips = tips ||'';
        	var win =　top.Ext.create('Ext.window.Window',{
		   			title: util.windowTitle('&#xe641;',title||'上传附件',''),
		   			width:450,
		   			height:tips==''?120:180,
		   			draggable:false,
					resizable:false,
					modal:true,
					layout: {
				        type: 'vbox',
				        pack: 'start',
				        align: 'stretch'
				    },
		   			items:[{
						xtype:'container',
						hidden:tips=='',
						html:'<span class="yellow-color"><i class="iconfont f20">&#xe6ae;</i> '+tips+'</span>',
						cls:'low'
					},{
						flex:1,
		   				xtype:'form',
						fileUpload : true,
						labelWidth:75,
						labelAlign:'right',
						bodyPadding:'10',
						border:false,
						autoScroll:true,
						items:[{
							xtype: 'fileuploadfield',
							name:'file',
							allowBlank:false,
							hideLabel:true,
							anchor:'100%',
				            emptyText: '请选择要导入的文件',
				            listeners:{
				            	change : callback
				            },
				            buttonConfig: {
				                text:'',
								ui:'default-toolbar',
				                glyph: 'xe648@iconfont'
				            }
						}]
		   			}]
		   		}).show();
        },
        downloadAttachment : function(url,callback){
        	top.Ext.require('Ext.ux.IFrame',function(){
        		top.Ext.create('Ext.window.Window',{
		   			title:util.windowTitle('&#xe65b;','导出/下载附件',''),
		   			width:300,
		   			height:120,
		   			draggable:false,
					resizable:false,
					modal:true,
		   			bodyStyle:'background:#fff;padding:10px;font-size:16px;color:#bbb;',
		   			layout:'fit',
		   			items:[{
						border:false,
						html:'<div style="font-size:16px;line-height:24px;">&nbsp;&nbsp;&nbsp;&nbsp;下载附件完成后请关闭窗口,如果没有自动下载，点击<a style="font-size:16px;" href="'+url+'" target="_blank">重新下载</a></div>'
					},top.Ext.create('Ext.ux.IFrame',{
						src:(url||'about:blank'),
						noMask:true,
						id:'downloadAttachmentFrame',
						frameName:'downloadAttachmentFrame',
						style:'width:0px;height:0px;'
					})]
		   		}).show();
        	});
        },
        showAndDownAttachment:function(urlObjs){
        	urlObjs = Ext.decode('['+urlObjs+']');
        	var attrHtml = [];
        	for(var i=0;i<urlObjs.length;i++){
        		attrHtml.push('<div class="tip-self-item">');
        		attrHtml.push('<span>'+urlObjs[i].title+'</span>');
        		attrHtml.push('<a href="'+urlObjs[i].url+'" target="_blank">下载</a>');
        		attrHtml.push('<img src=\''+cfg.getPicCtx()+'/'+urlObjs[i].url+'\'>');
        		attrHtml.push('</div>');
        	}
        	top.Ext.create('Ext.window.Window',{
	   			title:util.windowTitle('','下载附件','icon-download-1'),
	   			width:500,
	   			height:200,
	   			draggable:false,
				resizable:false,
				modal:true,
	   			bodyStyle:'background:#fff;padding:10px;font-size:16px;color:#bbb;',
	   			layout:'fit',
	   			items:[{
					border:false,
					html:attrHtml.join('')
				}]
	   		}).show();
        },
        classcodes :[],
		loadJscss:function(_files,succes){
			var FileArray=[];
	        if(typeof _files==="object"){
	            FileArray=_files;
	        }else{
	            /*如果文件列表是字符串，则用,切分成数组*/
	            if(typeof _files==="string"){
	                FileArray=_files.split(",");
	            }
	        }
	        if(FileArray!=null && FileArray.length>0){
	            var LoadedCount=0;
	            for(var i=0;i< FileArray.length;i++){
	                loadFile(FileArray[i],function(){
	                    LoadedCount++;
	                    if(LoadedCount==FileArray.length){
	                        succes();
	                    }
	                })
	            }
	        }
	        /*加载JS文件,url:文件路径,success:加载成功回调函数*/
	        function loadFile(url, success) {
	            if (!FileIsExt(util.classcodes,url)) {
	                var ThisType=GetFileType(url);
	                var fileObj=null;
	                if(ThisType==".js"){
	                    fileObj=document.createElement('script');
	                    fileObj.src = url;
	                }else if(ThisType==".css"){
	                    fileObj=document.createElement('link');
	                    fileObj.href = url;
	                    fileObj.type = "text/css";
	                    fileObj.rel="stylesheet";
	                }else if(ThisType==".less"){
	                    fileObj=document.createElement('link');
	                    fileObj.href = url;
	                    fileObj.type = "text/css";
	                    fileObj.rel="stylesheet/less";
	                }
	                success = success || function(){};
	                fileObj.onload = fileObj.onreadystatechange = function() {
	                    if (!this.readyState || 'loaded' === this.readyState || 'complete' === this.readyState) {
	                        success();
	                        util.classcodes.push(url)
	                    }
	                }
	                document.getElementsByTagName('head')[0].appendChild(fileObj);
	            }else{
	                success();
	            }
	        }
	        /*获取文件类型,后缀名，小写*/
	        function GetFileType(url){
	            if(url!=null && url.length>0){
	                return url.substr(url.lastIndexOf(".")).toLowerCase();
	            }
	            return "";
	        }
	        /*文件是否已加载*/
	        function FileIsExt(FileArray,_url){
	            if(FileArray!=null && FileArray.length>0){
	                var len =FileArray.length;
	                for (var i = 0; i < len; i++) {
	                    if (FileArray[i] ==_url) {
	                       return true;
	                    }
	                }
	            }
	            return false;
	        }
		},
        getPowerUrl:function(url){
        	var moduleId = util.getModuleId();
        	if(url){
        		moduleId = util.getParentModuleId(url).id;
        	}
        	
        	return cfg.getCtx()+'/b2b/power/list?moduleId='+moduleId+'&roleId='+cfg.getUser().roleId+'&userId='+cfg.getUser().id;
        },
        getModuleId:function(){
        	return top.location.hash.replace('#','');
        },
        getModuleName:function(){
			var paths = location.pathname,
				arr = paths.split('/');
			if(arr.length<=1)return '';
			return arr[arr.length-1];
		},
		getModuleAutoView : function(currPath){
			var cfgAutoView = cfg.getModuleAutoView();
			if(cfgAutoView[currPath]){
				return 'app.view.'+currPath;
			}
			return 'app.view.common.BaseView';
		},
		getModuleAutoCondit:function(currPath){
			var cfgAutoCd = cfg.getModuleAutoCondit();
			if(cfgAutoCd[currPath]){
				return Ext.create('app.view.'+currPath+'.condit');
			}
			return false;
		},
        createGridStore:function(url,model,auto,pageSize){
        	auto = auto||'1';
        	pageSize = pageSize||cfg.getPageSize();
        	return Ext.create('Ext.data.Store', {
	            pageSize: pageSize,
	            autoLoad: auto=='1',
	            model:model,
	            proxy: {
			         type: 'ajax',
			         noCache:true,
			         url: url,
			         reader: {
			             rootProperty: 'data',
	            		 totalProperty: 'totalSize'
			         }
			     }
	        });
        },
        createGridTbar:function(grid,actions,dock){
        	dock = dock||'top';
        	//公用右键按钮
        	//复制
        	var menus = [{
        		text:'复制',
        		id:'grid-copy-botton',
        		glyph:'xe884@fontello'
        	},{
        		text:'刷新',
        		itemId:'refresh',
        		glyph:'xefe5@fontello'
        	}];
        	var contextMenu = Ext.create('Ext.menu.Menu', {
		        items: menus//actions//Array.prototype.slice.call(actions, 1)
		    }),tbar = grid.getDockedItems('toolbar[dock="'+dock+'"]')[0];
		    /*grid.on('cellcontextmenu',function( g, td, cellIndex, record, tr, rowIndex, e, eOpts ){
		    	e.stopEvent();
                contextMenu.showAt(e.getXY());
                var clip = new ZeroClipboard(Ext.getDom('grid-copy-botton-itemEl'));
        		clip.setText(Ext.get(td).first().getHtml());
                return false;
		    });*/
		    if(tbar.items.length>0){
        		tbar.removeAll();
        	}
			tbar.add(actions);
			return tbar;
        },
        addCommonContextMenu :function(grid){
        	//公用右键按钮
        	//复制
        	var menus = [{
        		text:'复制',
        		id:'grid-copy-botton',
        		glyph:'xe884@fontello'
        	},{
        		text:'刷新',
        		itemId:'refresh',
        		glyph:'xefe5@fontello'
        	}];
        	var contextMenu = Ext.create('Ext.menu.Menu', {
		        items: menus
		    });
		    grid.on('cellcontextmenu',function( g, td, cellIndex, record, tr, rowIndex, e, eOpts ){
		    	e.stopEvent();
                contextMenu.showAt(e.getXY());
                var clip = new ZeroClipboard(Ext.getDom('grid-copy-botton-itemEl'));
        		clip.setText(Ext.get(td).first().getHtml());
                return false;
		    });
        },
        createGridBbar:function(store){
        	return Ext.create('Ext.toolbar.Paging', {
		        store: store,
		        displayInfo: true,
		        displayMsg:'共{2}条',
		        plugins : [{
		        	displayText:'单页',
					ptype: 'pagingtoolbarresizer', 
					options : [ 20, 50 ,100, 200 ,300 ,500]
				}]
			});
        },
        getSingleModel:function(grid){
        	var model = grid.getSelection();
        	if(model.length==0){
        		util.alert('请选择一条数据');
        		return false;
        	}
			if(model.length>1){
				util.alert('不可多选，只能选择一条数据');
				return false;
			}else{
				return model[0];
			}
        },
        getMultiModel:function(grid){
        	var model = grid.getSelection();
        	if(model.length==0){
        		util.alert('可多选，至少选择一条数据');
        		return false;
        	}else{
        		return model;
        	}
        },
        delGridModel:function(grid,url,params){
        	var sels,models=[];
        	if(sels = util.getMultiModel(grid)){
        		for (var i = 0; i < sels.length; i++) { 
					models.push(sels[i].data);
				}
				var win = Ext.create('Ext.window.Window',{
		   			title: util.windowTitle('','信息提示',''),
		   			width:300,
		   			height:150,
		   			draggable:false,
					resizable:false,
					closable:false,
					modal:true,
		   			bodyStyle:'background:#fff;padding:10px;',
		   			layout:'fit',
		   			items:[{
		   				html:'<h3 class="alert-box"> 确定删除当前选中数据？</h3>'
		   			}],
		   			buttons:[{
		   				text:'确定',
		   				handler:function(){
		   					Ext.Ajax.request({
								url:cfg.getCtx()+url,
								params:{models:Ext.encode(models)},
								success:function(response, opts){
									win.close();
									var obj = Ext.decode(response.responseText),
										errors = ['数据删除失败','该项目下有子项，请先删除子项目'],
										state = obj?obj.statusCode:0;
									if(!obj.success){
										util.error(errors[0-parseInt(state)]);
									}else{
										util.success("数据删除成功!");
										if(params){
											Ext.applyIf(params,{start:0,limit:cfg.getPageSize()});
										}else{
											params = {start:0,limit:cfg.getPageSize()};
										}
										grid.getStore().load({params:params});
									}
								}
							});
		   				}
		   			}]
		   		}).show();
			}
        },
        switchGridModel:function(grid,url,params,msg){
        	var sels,models=[];
        	msg = msg ||'';
        	if(sels = util.getMultiModel(grid)){
        		for (var i = 0; i < sels.length; i++) { 
					models.push(sels[i].data);
				}
	        	Ext.Ajax.request({
					url:cfg.getCtx()+url,
					params:{models:Ext.encode(models)},
					success:function(response, opts){
						var obj = Ext.decode(response.responseText),
							errors = ['数据更新失败',msg==''?'请检查子项目是否填写完整':msg],
							state = obj?obj.statusCode:0;
						if(!obj.success){
							util.error(errors[0-parseInt(state)]);
						}else{
							util.success("数据更新成功!");
							if(params){
								grid.getStore().load({params:params});
							}else{
								grid.getStore().load();
							}
						}
					},
					failure:function(){
						util.error("数据更新失败！");
					}
				});
			}
        },
        auditGridModel:function(grid,url){
        	var sels,models=[],isAudit=false;
		    if(sels = util.getMultiModel(grid)){
		    	for (var i = 0; i < sels.length; i++) { 
					models.push(sels[i].data);
					var status = sels[i].data.AUDIT_STATUS || 0;
					if(status!=0){
						isAudit = true;
					}
				}
		    	if(isAudit){
		    		util.alert('已审核的数据不能重复审核');
		    		return;
		    	}
	        	Ext.MessageBox.show({
		            title: util.windowTitle('&#xe678;','数据审核',''),
		            msg: '<div style="padding:10px;font-size:14px;" class="red-color"><i class="iconfont f18 red-color">&#xe6ae;</i> 请仔细检查核对信息，确认无误后点击通过</div>',
		            buttons: Ext.MessageBox.YES,
		            buttonAlign:'right',
		            buttonText:{ 
		                yes: "通过"/*, 
		                no: "不通过" */
		            },
		            scope: this,
		            fn: function(btn,text){
		            
		            	if(btn=='cancel')
		            	return;
		            
		        		
			        	Ext.Ajax.request({
							url:cfg.getCtx()+url,
							params:{models:Ext.encode(models),AUDIT_STATUS:(btn=='yes'?1:2)},
							success:function(){
								util.success("数据审核成功!");
								grid.getStore().load();
							},
							failure:function(){
								util.error("数据审核失败！");
							}
						});
		            }
		        });
	        }
        },
        pmModel:function(model){
        	var modelData = model.data,
        		newModelData = {};
        	for(var i in modelData){
				newModelData['pm['+i+']'] = modelData[i];
			}
        	return newModelData;
        },
        createComboStore:function(data){
        	Ext.define('comboModel', {  
		        extend: 'Ext.data.Model',  
		        fields: [  
		            { type: 'string', name: 'value' },  
		            { type: 'string', name: 'text' } 
		        ]  
		    });
        	return Ext.create('Ext.data.Store', {  
		        model: 'comboModel',  
		        data: data  
		    });  
        },
        createEmptyWindow :function(title,iconCls,width,height,items,buttons){
        	var glyph = iconCls.indexOf('&')!=-1?iconCls:'';
        	iconCls = glyph==''?iconCls:'';
        	return Ext.create('Ext.window.Window',{
				title:util.windowTitle(glyph,title,iconCls),
				width:width||450,
				height:height||205,
				modal:true,
				draggable:false,
				resizable:false,
				maximizable:false,
	   			layout:'fit',
				items:[{
					xtype:'form',
	    			scrollable:true,
					fieldDefaults: {
				        labelAlign: 'right',
				        labelWidth: 70,
				        msgTarget: 'qtip'
				    },
					bodyPadding: '15 5 5 5',
					items:items||[],
					buttons:buttons
				}]
			});
        },
        /*
       	 * 重载mask
       	 */
        overrideModal:function(){
			Ext.define('app.ExtZIndexManager',{
			    override:'Ext.ZIndexManager',
			    _showModalMask:function(comp){
			        this.callParent(arguments);
			        this.mask.removeCls(this.customMaskCls);
			        this.customMaskCls = 'modal-mask-'+comp.ui;
			        this.mask.addCls(this.customMaskCls);
			    }
			});
        },
        getParentModuleId:function(url){
        	var modules = top.cfg.getUser().modules;
        	if(modules!=''){
        		modules = Ext.decode(modules);
        		for(var i=0;i<modules.length;i++){
        			if(modules[i].url==url){return modules[i];}
        		}
        	}
        },
        getDirectModuleId :function(url){
        	var modules = top.cfg.getUser().modules;
        	if(modules!=''){
        		modules = Ext.decode(modules);
        		for(var i=0;i<modules.length;i++){
        			var cd = modules[i].children;
        			for(var j=0;j<cd.length;j++){
        				if(cd[j].url==url){return cd[j];}
        			}
        		}
        	}
        },
        //url===== 如：b2c/setting
        redirectTo :function(url,param){
        	param = param || '';
        	var rts,route,ctr,module;
        	if(self.location==top.location){
        		rts = Ext.app.route.Router;
        		module = util.getDirectModuleId(url);
        	}else{
        		rts = top.Ext.app.route.Router;
        		module = top.util.getDirectModuleId(url);
        	}
			route = rts.routes[0];
			ctr = route.controller;
			ctr.redirectParams = param;
			ctr.redirectTo(module.id,true);
			
        },
        beforePrint : function(title ,tplHtml){
        	var htmlMarkup = [
                '<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">',
                '<html class="' + Ext.baseCSSPrefix + 'ux-grid-printer">',
                  '<head>',
                    '<meta content="text/html; charset=UTF-8" http-equiv="Content-Type" />',
                    '<link href="' + cfg.getCssPath() + '/print.css" rel="stylesheet" type="text/css" />',
                    '<link href="' + cfg.getCssPath() + '/global.css" rel="stylesheet" type="text/css" />',
                    '<title>' + title + '</title>',
                  '</head>',
                  '<body class="' + Ext.baseCSSPrefix + 'ux-grid-printer-body">',
                  tplHtml,
                  '</body>',
                '</html>'
             ];
        	return htmlMarkup.join('');
        },
        afterPrint :function(win,html){
            win.document.open();
            win.document.write(html);
            win.document.close();
            setTimeout(function(){win.print();},100);
        },
        print:function(title,tplHtml,autoPrint,autoClose){
        	var html = util.beforePrint(title,tplHtml);
        	var win = window.open();
            win.document.open();
            win.document.write(html);
            win.document.close();
            if (autoPrint){
                setTimeout(function(){win.print();},100);
            }
        },
        accountWindow :function(sel){
        	Ext.create('Ext.window.Window',{
        		title : util.windowTitle('&#xe62f;',sel.data.TEXT,''),
        		width : '85%',
				height: '85%',
				modal:true,
				draggable:false,
				resizable:false,
				maximizable:false,
	   			layout:'fit',
	   			items:[Ext.create('Ext.ux.IFrame',{
					src:cfg.getCtx()+'/account/index?paramId='+sel.data.ID,
					id:'user-account-iframe',
					frameName:'user-account-iframe',
					style:'width:100%;height:100%;'
				})]
        	}).show();
        },
        showOrderViewWindow:function(paramsStr){
        	Ext.create('app.view.common.OrderViewWindow',{
        		paramsStr:paramsStr
        	}).show();
        },
        showSupplyViewWindow:function(paramsStr){
        	Ext.create('app.view.common.SupplyViewWindow',{
        		paramsStr:paramsStr
        	}).show();
        },
        init:function(){
        	Ext.override(Ext.ToolTip, {  
        	    //maxWidth:0,
        	    dismissDelay:15000,
        	    baseCls:'x-gray-tips'
        	});  
        	Ext.override(Ext.grid.column.Column,{
        		resizable:false
        	});
        	if (Ext.util && Ext.util.Format) {  
        		Ext.apply(Ext.util.Format, { 
        			fmEllipsis: function(value, length) {
        	            if (value && value.length >= length) {
        	            	var len = (length-3)/2,
        	            		bl = (length-3)%2;
        	            	return value.substring(0, 4)+'...'+value.substring(value.length-(length-7), value.length);
        	            }
        	            return value;
        	        },
        			fmMoney :function(v){
        				return util.moneyFormat(v,'f18 disable-color');
        			},
        			fmOrangeMoney :function(v){
        				return util.moneyFormat(v,'f18 orange-color');
        			},
        			fmBlueMoney :function(v){
        				return util.moneyFormat(v,'f18 blue-color');
        			},
        			fmRedMoney :function(v){
        				return util.moneyFormat(v,'f18 red-color');
        			},
        			fmGreenMoney :function(v){
        				return util.moneyFormat(v,'f18 green-color');
        			},
        			fmNumber :function(v){
        				return '<span class="f18 disable-color">'+v+'</span>';
        			},
        			fmBlueNumber :function(v){
        				return '<span class="f18 blue-color">'+v+'</span>';
        			},
        			fmGreenNumber :function(v){
        				return '<span class="f18 green-color">'+v+'</span>';
        			},
        			fmRenNumber :function(v){
        				return '<span class="f18 red-color">'+v+'</span>';
        			},
        			isEmptyObj:function(v){
        				for(var n in v){return false;}
        				return true;
        			}, 
        			// 百分比  
		            percentRenderer : function(v, rd, model) {  
		                //v = v * 100;  
		                var v1 = v > 100 ? 100 : v;  
		                v1 = v1 < 0 ? 0 : v1;  
		                var v2 = parseInt(v1 * 2.55).toString(16);  
		                if (v2.length == 1)  
		                    v2 = '0' + v2;  
		                return Ext.String  
		                        .format(  
		                                '<div>'  
		                                        + '<div style="float:left;border:1px solid #008000;height:15px;width:100%;">'  
		                                        + '<div style="float:left;text-align:center;width:100%;color:blue;">{0}%</div>'  
		                                        + '<div style="background: #FAB2{2};width:{1}%;height:13px;"></div>'  
		                                        + '</div></div>', v, v1, v2); 
		        	}                           
        		});
        	}
        	Ext.define('MyApp.overrides.tree.Column', {
        	    override: 'Ext.tree.Column',

        	    /* Changed img tag to div - for IconFonts to work */
        	    cellTpl: [
					'<tpl for="lines">',
					'<img src="{parent.blankUrl}" class="{parent.childCls} {parent.elbowCls}-img ',
					'{parent.elbowCls}-<tpl if=".">line<tpl else>empty</tpl>" role="presentation"/>',
					'</tpl>',
					'<img src="{blankUrl}" class="{childCls} {elbowCls}-img {elbowCls}',
					'<tpl if="isLast">-end</tpl><tpl if="expandable">-plus {expanderCls}</tpl>" role="presentation"/>',
					'<tpl if="checked !== null">',
					'<input type="button" {ariaCellCheckboxAttr}',
					    ' class="{childCls} {checkboxCls}<tpl if="checked"> {checkboxCls}-checked</tpl>"/>',
					'</tpl>',
					
					'<tpl if="icon">',
						'<img src="{blankUrl}" role="presentation" class="{childCls} {baseIconCls} ',
						'{baseIconCls}-<tpl if="leaf">leaf<tpl else>parent</tpl> {iconCls}"',
						'<tpl if="icon">style="background-image:url({icon})"</tpl>/>',
					'<tpl else>',
						'<span class="{childCls} {baseIconCls} ',
						'{baseIconCls}-<tpl if="leaf">leaf<tpl else>parent</tpl> {iconCls}"',
						' style="background:none;font-size:15px;color:#bbb;font-family:{[this.getGlyphFont(values.record)]}">',
						'{[this.getGlyphValue(values.record)]}</span>',
					'</tpl>',      
					'<tpl if="href">',
					'<a href="{href}" role="link" target="{hrefTarget}" class="{textCls} {childCls}">{value}</a>',
					'<tpl else>',
					'<span class="{textCls} {childCls}">{value}</span>',
					'</tpl>',{
						getGlyphFont:function(r){
							if(r.get('glyph')){
								var v = r.get('glyph').split('@');
								return v[1];
							}else{
								return 'iconfont';
							}
						},
						getGlyphValue:function(r){
							if(r.get('glyph')){
								var v = r.get('glyph').split('@');
								return '&#'+v[0]+';';
							}else{
								return '&#xe62b;';
							}
						}
					}     
        	    ]
        	});
        	
        	//解决gridpanel toolbar 隐藏的按钮点击事件
        	Ext.define('MyApp.layout.container.boxOverflow.Menu', {
        	    override: 'Ext.layout.container.boxOverflow.Menu',

        	    createMenuConfig: function(component, hideOnClick) {
        	    	var config = Ext.apply({}, component.initialConfig),
	                    group  = component.toggleGroup;
	
	                Ext.copyTo(config, component, [
	                    'iconCls', 'icon', 'itemId', 'disabled', 'handler', 'scope', 'menu', 'tabIndex'
	                ]);
	
	                Ext.applyIf(config, {
	                    text: component.overflowText || component.text,
	                    hideOnClick: hideOnClick,
	                    destroyMenu: false,
	                    listeners: {}
	                });
	
	                // Clone must have same value, and must sync original's value on change
	                if (component.isFormField) {
	                    config.value = component.getValue();
	
	                    // Sync the original component's value when the clone changes value.
	                    // This intentionally overwrites any developer-configured change listener on the clone.
	                    // That's because we monitor the clone's change event, and sync the
	                    // original field by calling setValue, so the original field's change
	                    // event will still fire.
	                    config.listeners.change = function(c, newVal, oldVal) {                            
	                        component.setValue(newVal);
	                    };
	                }
	
	                // ToggleButtons become CheckItems
	                else if (group || component.enableToggle) {
	                    Ext.apply(config, {
	                        hideOnClick: false,
	                        group: group,
	                        checked: component.pressed,
	                        handler: function (item, e) {
	                            component.onClick(e);
	                        }
	                    });
	                }
	
	                // Buttons may have their text or icon changed - this must be propagated to the clone in the overflow menu
	                if (component.isButton && !component.changeListenersAdded) {
	                    component.on({
	                        textchange: this.onButtonAttrChange,
	                        iconchange: this.onButtonAttrChange,
	                        toggle: this.onButtonToggle
	                    });
	                    component.changeListenersAdded = true;
	                    
	                    //实现方法
	                    config.listeners = {
		                		click:function(){
		                			component.fireEvent('click');
		                		}
		                }
	                }
	
	                // Typically margins are used to separate items in a toolbar
	                // but don't really make a lot of sense in a menu, so we strip
	                // them out here.
	                delete config.margin;
	                delete config.ownerCt;
	                delete config.xtype;
	                delete config.id;
	                delete config.itemId;
	                return config;
        	    }
        	});
        	
        	//filefield 文件类型过滤
        	Ext.define('MyApp.FileFieldAttributes', {
        	    override: 'Ext.form.field.File',

        	    /**
        	     * @cfg {Object} fileInputAttributes
        	     * Extra attributes to add to the file input element.
        	     */

        	    onRender: function() {
        	        var me = this,
        	            attr = me.fileInputAttributes,
        	            fileInputEl, name;

        	        me.callParent();
        	        fileInputEl = me.getTrigger('filebutton').component.fileInputEl.dom;
        	        for (name in attr) {
        	            fileInputEl.setAttribute(name, attr[name]);
        	        }
        	    }
        	});
        	
        	
        	
        	Ext.define('MyApp.form.field.ComboBox', {
                 override: 'Ext.form.field.ComboBox',
                 onBindStore: function(store, initial) {
                     this.callParent(arguments);
                     // Deselect on container click is not required if paging toolbar exists
                     this.pickerSelectionModel.deselectOnContainerClick = false;
                    
                 },
                  /**
                  * if the fromComponent owner is picker then do not collapse boundlist.
                  */
                 onFocusLeave: function(e) {
                     var me = this;
                     if (e.fromComponent.activeOwner && e.fromComponent.activeOwner.id == this.picker.id) {
                         return;
                     }
                     me.collapse();
                     me.callParent([
                     e]);
                 }
        	});
        }
    }
});