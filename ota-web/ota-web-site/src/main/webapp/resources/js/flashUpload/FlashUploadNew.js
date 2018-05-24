/**
 * @namespace KuBaoUploader Object
 * @class
 * @author Gantian && WangWei
 * @Date 20101105 
 * @version 2.0 
 */

var KuBaoUploader = {
    list : [],  //uploaded imgs collections
    UI : {},    // UI class
    AsToJs : {}, // the js function for flash callback
    IsFirst:true, // is first init
    
    //return uploaded images path join with ";"
    GetImgPaths : function (){
        return this.list.join(";");
        }
};

/*
 * check navigator support flash
*/
KuBaoUploader.IsSupportFlash = function() {
    var isF = false;

    try {
        var version = new ActiveXObject("ShockwaveFlash.ShockwaveFlash").GetVariable("$version");
        isF = true;
    } catch (e) { };

    if (!isF) {
        if (navigator.plugins) {
            if (navigator.plugins["Shockwave Flash"]) {
                isF = true;
            } else {
                isF = false;
            }
        } else {
            isF = false;
        }
    }

    return isF;
};

/*
 * init FlashControl
*/
KuBaoUploader.UI.Init= function (){
    jQuery("#dvFlashUploadContainer").css("display","");
    ZeroClipboard.nextId=1;
    if(KuBaoUploader.IsFirst)
    {
        if(editImgList !=null && editImgList.length > 0)
        {      
            FillEditImg(editImgList)
        }
        BindFlashToButton();
    }
    
    KuBaoUploader.IsFirst=false;
}

/*
 * remove image
*/
KuBaoUploader.UI.removeImg= function (id,boxId){
   var imgno=jQuery("#"+boxId).attr("imgno");
   jQuery("#"+boxId+" .initial").html(" <p class='num'>"+imgno+"</p><p>求头像</p>").attr("class",'initial');
   jQuery("#"+boxId+" input").val('上传头像').attr("class",'up_btn_1');
}

/*
 * uploading image
*/
KuBaoUploader.UI.progressController= function (data,boxId){
   if(jQuery("#"+boxId+" .initial img").length>0 && jQuery("#"+boxId+" .initial img").attr("src")!="")
   {
        KuBaoUploader.ArrRemove(jQuery("#"+boxId+" .initial img").attr("src")); 
   }
   jQuery("#"+boxId+" .initial").html("<p class='waiting'><span></span></p><p class='loading'>上传中...</p>").attr("class",'initial');
   jQuery("#"+boxId+" input").attr("class",'up_btn_1');					   
}

/*
 * finish upload image
*/
KuBaoUploader.UI.completeController= function (data,boxId){
   jQuery("#"+boxId+" .initial").html("<p class='del'><a href='javascript:void 0' >删除</a></p> <p class='pic'><img src='"+data.url+"' alt='' width='86px' height='86px' /></p>").attr('class','initial right');
   jQuery("#"+boxId+" a").click(function(){
         KuBaoUploader.ArrRemove(data.url);   
		 KuBaoUploader.UI.removeImg(data.id,boxId);
   });
   jQuery("#"+boxId+" input").val("重新上传").attr("class",'up_btn_1');
}

/*
 * upload image error
*/
KuBaoUploader.UI.errorController= function (data,boxId){
   if(jQuery("#"+boxId+" .initial img").length>0 && jQuery("#"+boxId+" .initial img").attr("src")!="")
   {
        KuBaoUploader.ArrRemove(jQuery("#"+boxId+" .initial img").attr("src")); 
   }
    var imgno=jQuery("#"+boxId).attr("imgno");
	jQuery("#"+boxId+" .initial").html("<p class='false'>"+data.msg+"</p><p class='num'>"+imgno+"</p><p class='noimg'>求头像</p>").attr('class','initial wrong');
	jQuery("#"+boxId+" input").val("重新上传").attr("class",'up_btn_1');
	
	//setTimeout( function(){ KuBaoUploader.UI.removeImg(data,boxId);},2000);
}


/**
 * return result after DFS upload
 */
KuBaoUploader.AsToJs.CallBackPath = function(callbackpath,index){
    switch(callbackpath)
    {
        case "wrongtype":
            var data={code:'1',msg:'格式错误'};
	        KuBaoUploader.UI.errorController(data,"dvFlashUpload"+index);
	        break;
        case "error":
            var data={code:'2',msg:'上传失败'};
	        KuBaoUploader.UI.errorController(data,"dvFlashUpload"+index);
	        break;
        case "bigger":
            var data={code:'3',msg:'图片过大'};
	        KuBaoUploader.UI.errorController(data,"dvFlashUpload"+index);
	        break;
        default:
        	var imgType = callbackpath.split("|")[0],
        		imgPath = picUrl+'/'+callbackpath.split("|")[1];
            KuBaoUploader.list.push(imgPath);
            
            //hdImgPathsForFlashId 控件外存放图片路径的隐藏控件Id
            if( typeof(hdImgPathsForFlashId)=="string" && (typeof document.getElementById(hdImgPathsForFlashId) != "undefined")){
                document.getElementById(hdImgPathsForFlashId).value= KuBaoUploader.GetImgPaths();
            }
            //hdUrlsPathId  上传控件自身存放图片路径的隐藏控件Id
            document.getElementById(hdUrlsPathId).value=KuBaoUploader.GetImgPaths();
            var data={id:index,name:'',url:imgPath};
            KuBaoUploader.UI.completeController(data,"dvFlashUpload"+index);
            break;
    }
};

/**
 * show the UI when choose wrong image
 */
KuBaoUploader.AsToJs.SelectError = function(result,index){
   //显示对应错误信息  0: 没选择  -1：图片太大 
   switch(result)
   {
     case -1:
        var data={code:'3',msg:'图片过大'};
	    KuBaoUploader.UI.errorController(data,"dvFlashUpload"+index);
        break;
   }  
};

/**
 * alert error message
 */
KuBaoUploader.AsToJs.Error = function(message){
    alert("sys:"+message);
};

/**
 * show the Uploading UI 
 */
KuBaoUploader.AsToJs.Uploading = function(index){
    var data={pecent:'',speed:'',allBytes:'',bytes:''};  
	KuBaoUploader.UI.progressController(data,"dvFlashUpload"+index); 
};

KuBaoUploader.ArrRemove = function(obj)
{
    for ( var i=0 ; i < KuBaoUploader.list.length ; ++i )
    {
        var isIncludImg=false;
        isIncludImg= GetImgName(KuBaoUploader.list[i]) == GetImgName(obj) ;
        if ( isIncludImg ){
            if ( i > KuBaoUploader.list.length/2 ){
                for ( var j=i ; j < KuBaoUploader.list.length-1 ; ++j )
                {
                    KuBaoUploader.list[j] = KuBaoUploader.list[j+1];
                }
                KuBaoUploader.list.pop();
            }
            else{
                for ( var j=i ; j > 0 ; --j )
                {
                    KuBaoUploader.list[j] = KuBaoUploader.list[j-1];
                }
                KuBaoUploader.list.shift();
            }    
            break;
        }
    }
    document.getElementById(hdUrlsPathId).value=KuBaoUploader.GetImgPaths();
    if(typeof(hdImgPathsForFlashId)=="string" && (typeof document.getElementById(hdImgPathsForFlashId) != "undefined")){
        document.getElementById(hdImgPathsForFlashId).value= KuBaoUploader.GetImgPaths();
    }
};

var isBrowser = function(vision) {
            if (typeof vision == 'undefiend')
                return false;
            vision = vision.toUpperCase();
            var isIE = !!window.ActiveXObject;
            var isIE6 = isIE && !window.XMLHttpRequest;
            var isIE8 = isIE && !!document.documentMode;
            var isIE7 = isIE && !isIE6 && !isIE8;
            if (isIE) {
                if (isIE6) {
                    return vision === 'IE6';
                } else if (isIE8) {
                    return vision === 'IE8';
                } else if (isIE7) {
                    return vision === 'IE7';
                }
            }
            else {
                return vision === 'FF';
            }
        }

function GetImgName(imgsrc)
{
    if(imgsrc==null || imgsrc.length==0)
        return "";
    
    var index=imgsrc.lastIndexOf('/');
    return imgsrc.substring(index+1);
}


/*------------------ZeroClipboard js --------------------*/
var ZeroClipboard = {
	
	version: "1.0.7",
	clients: {}, // registered upload clients on page, indexed by id
	moviePath: ctx+'/resources/js/flashUpload/KuBaoUpload.swf', // URL to movie
	nextId: 1, // ID of next movie
	posturl:"", //upload url
	filtertype:"", 
	
	$: function(thingy) {
		// simple DOM lookup utility function
		if (typeof(thingy) == 'string') thingy = document.getElementById(thingy);
		if (!thingy.addClass) {
			// extend element with a few useful methods
			thingy.hide = function() { this.style.display = 'none'; };
			thingy.show = function() { this.style.display = ''; };
			thingy.addClass = function(name) { this.removeClass(name); this.className += ' ' + name; };
			thingy.removeClass = function(name) {
				var classes = this.className.split(/\s+/);
				var idx = -1;
				for (var k = 0; k < classes.length; k++) {
					if (classes[k] == name) { idx = k; k = classes.length; }
				}
				if (idx > -1) {
					classes.splice( idx, 1 );
					this.className = classes.join(' ');
				}
				return this;
			};
			thingy.hasClass = function(name) {
				return !!this.className.match( new RegExp("\\s*" + name + "\\s*") );
			};
		}
		return thingy;
	},
	
	setMoviePath: function(path) {
		// set path to ZeroClipboard.swf
		this.moviePath = path;
	},
	
	dispatch: function(id, eventName, args) {
		// receive event from flash movie, send to client		
		var client = this.clients[id];
		if (client) {
			client.receiveEvent(eventName, args);
		}
	},
	
	register: function(id, client) {
		// register new client to receive events
		this.clients[id] = client;
	},
	
	getDOMObjectPosition: function(obj, stopObj) {
		// get absolute coordinates for dom element
		var info = {
			left: 0, 
			top: 0, 
			width: obj.width ? obj.width : obj.offsetWidth, 
			height: obj.height ? obj.height : obj.offsetHeight
		};

		while (obj && (obj != stopObj)) {
			info.left += obj.offsetLeft;
			info.top += obj.offsetTop;
			obj = obj.offsetParent;
		}

		return info;
	},
	
	Client: function(elem) {
		// constructor for new simple upload client
		this.handlers = {};
		
		// unique ID
		this.id = ZeroClipboard.nextId++;
		this.movieId = 'ZeroClipboardMovie_' + this.id;
		
		// register client with singleton to receive flash events
		ZeroClipboard.register(this.id, this);
		
		// create movie
		if (elem) this.glue(elem);
	}
};

ZeroClipboard.Client.prototype = {
	
	id: 0, // unique ID for us
	ready: false, // whether movie is ready to receive events or not
	movie: null, // reference to movie object
	clipText: '', // text to copy to clipboard
	handCursorEnabled: true, // whether to show hand cursor, or default pointer cursor
	cssEffects: true, // enable CSS mouse effects on dom container
	handlers: null, // user event handlers
	
	
	glue: function(elem, appendElem, stylesToAdd) {
		// glue to DOM element
		// elem can be ID or actual DOM element object
		this.domElement = ZeroClipboard.$(elem);
		// float just above object, or zIndex 99 if dom element isn't set
		var zIndex = 99;
		if (this.domElement.style.zIndex) {
			zIndex = parseInt(this.domElement.style.zIndex, 10) + 1;
		}
		
		if (typeof(appendElem) == 'string') {
			appendElem = ZeroClipboard.$(appendElem);
		}
		else if (typeof(appendElem) == 'undefined') {
			appendElem = document.getElementsByTagName('body')[0];
		}
		
		// find X/Y position of domElement
		var box = ZeroClipboard.getDOMObjectPosition(this.domElement, appendElem);
		
		// create floating DIV above element
		this.div = document.createElement('div');
		var style = this.div.style;
		style.position = 'absolute';
//		if(isBrowser("IE6") || isBrowser("IE7"))
//		{
//		    style.left = '' + (box.left+10) + 'px';
//		    style.top = '' + (box.top+15) + 'px';
//		}
//		else
//		{
		    style.left = '' + box.left + 'px';
		    style.top = '' + box.top + 'px';
		//}
		style.width = '' + box.width + 'px';
		style.height='20px';
		style.zIndex = zIndex;
		style.overflow="hidden";
		
		if (typeof(stylesToAdd) == 'object') {
			for (addedStyle in stylesToAdd) {
				style[addedStyle] = stylesToAdd[addedStyle];
			}
		}
		
		// style.backgroundColor = '#f00'; // debug
		
		appendElem.appendChild(this.div);
		
		if(isBrowser("IE6") || isBrowser("IE7"))
		    this.div.innerHTML = "<div style='position:relative;top:-40px;'>"+this.getHTML( box.width, box.height )+"</div>";
		else
		    this.div.innerHTML = this.getHTML( box.width, box.height );
	},
	
	getHTML: function(width, height) {
		// return HTML for movie
		var html = '';
		//debugger;
		//if(this.id>5)
		 //   this.id=this.id%5+1;
		var _flashvars = 'id=' + this.id +
		    '&posturl=' + this.posturl + 
			'&filtertype=' + this.filtertype+
			'&width=' + width + 
			'&height=' + height+
			'&maxsize=' + this.maxsize;
		if (navigator.userAgent.match(/MSIE/)) {
			// IE gets an OBJECT tag
			var protocol = location.href.match(/^https/i) ? 'https://' : 'http://';
			html += '<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="'+protocol+'download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,0,0" width="'+width+'" height="'+height+'" id="'+this.movieId+'" align="middle"><param name="allowScriptAccess" value="always" /><param name="allowFullScreen" value="false" /><param name="movie" value="'+ZeroClipboard.moviePath+'" /><param name="loop" value="false" /><param name="menu" value="false" /><param name="quality" value="best" /><param name="bgcolor" value="#ffffff" /><param name="flashvars" value="'+_flashvars+'"/><param name="wmode" value="transparent"/></object>';
		}
		else {
			// all other browsers get an EMBED tag
			html += '<embed id="'+this.movieId+'" src="'+ZeroClipboard.moviePath+'" loop="false" menu="false" quality="best" bgcolor="#ffffff" width="'+width+'" height="'+height+'" name="'+this.movieId+'" align="middle" allowScriptAccess="always" allowFullScreen="false" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" flashvars="'+_flashvars+'" wmode="transparent" />';
		}
		
		return html;
	},
	
	setParms: function(url,allowFile,size) {
	    this.posturl=url;
	    this.filtertype=allowFile;
	    this.maxsize=size;
	},
	
	hide: function() {
		// temporarily hide floater offscreen
		if (this.div) {
			this.div.style.left = '-2000px';
		}
	},
	
	show: function() {
		// show ourselves after a call to hide()
		this.reposition();
	},
	
	destroy: function() {
		// destroy control and floater
		if (this.domElement && this.div) {
			this.hide();
			this.div.innerHTML = '';
			
			var body = document.getElementsByTagName('body')[0];
			try { body.removeChild( this.div ); } catch(e) {;}
			
			this.domElement = null;
			this.div = null;
		}
	},
	
	reposition: function(elem) {
		// reposition our floating div, optionally to new container
		// warning: container CANNOT change size, only position
		if (elem) {
			this.domElement = ZeroClipboard.$(elem);
			if (!this.domElement) this.hide();
		}
		
		if (this.domElement && this.div) {
			var box = ZeroClipboard.getDOMObjectPosition(this.domElement);
			var style = this.div.style;
			style.left = '' + box.left + 'px';
			style.top = '' + box.top + 'px';
		}
	},
	
	setText: function(newText) {
		// set text to be copied to clipboard
		this.clipText = newText;
		if (this.ready) this.movie.setText(newText);
	},
	
	addEventListener: function(eventName, func) {
		// add user event listener for event
		// event types: load, queueStart, fileStart, fileComplete, queueComplete, progress, error, cancel
		eventName = eventName.toString().toLowerCase().replace(/^on/, '');
		if (!this.handlers[eventName]) this.handlers[eventName] = [];
		this.handlers[eventName].push(func);
	},
	
	setHandCursor: function(enabled) {
		// enable hand cursor (true), or default arrow cursor (false)
		this.handCursorEnabled = enabled;
		if (this.ready) this.movie.setHandCursor(enabled);
	},
	
	setCSSEffects: function(enabled) {
		// enable or disable CSS effects on DOM container
		this.cssEffects = !!enabled;
	},
	
	receiveEvent: function(eventName, args) {
		// receive event from flash
		eventName = eventName.toString().toLowerCase().replace(/^on/, '');
				
		// special behavior for certain events
		switch (eventName) {
			case 'load':
				// movie claims it is ready, but in IE this isn't always the case...
				// bug fix: Cannot extend EMBED DOM elements in Firefox, must use traditional function
				this.movie = document.getElementById(this.movieId);
				if (!this.movie) {
					var self = this;
					setTimeout( function() { self.receiveEvent('load', null); }, 1 );
					return;
				}
				
				// firefox on pc needs a "kick" in order to set these in certain cases
				if (!this.ready && navigator.userAgent.match(/Firefox/) && navigator.userAgent.match(/Windows/)) {
					var self = this;
					setTimeout( function() { self.receiveEvent('load', null); }, 100 );
					this.ready = true;
					return;
				}
				
				this.ready = true;
				this.movie.setText( this.clipText );
				this.movie.setHandCursor( this.handCursorEnabled );
				break;
			
			case 'mouseover':
				if (this.domElement && this.cssEffects) {
					this.domElement.addClass('hover');
					if (this.recoverActive) this.domElement.addClass('active');
				}
				break;
			
			case 'mouseout':
				if (this.domElement && this.cssEffects) {
					this.recoverActive = false;
					if (this.domElement.hasClass('active')) {
						this.domElement.removeClass('active');
						this.recoverActive = true;
					}
					this.domElement.removeClass('hover');
				}
				break;
			
			case 'mousedown':
				if (this.domElement && this.cssEffects) {
					this.domElement.addClass('active');
				}
				break;
			
			case 'mouseup':
				if (this.domElement && this.cssEffects) {
					this.domElement.removeClass('active');
					this.recoverActive = false;
				}
				break;
		} // switch eventName
		
		if (this.handlers[eventName]) {
			for (var idx = 0, len = this.handlers[eventName].length; idx < len; idx++) {
				var func = this.handlers[eventName][idx];
			
				if (typeof(func) == 'function') {
					// actual function reference
					func(this, args);
				}
				else if ((typeof(func) == 'object') && (func.length == 2)) {
					// PHP style object + method, i.e. [myObject, 'myMethod']
					func[0][ func[1] ](this, args);
				}
				else if (typeof(func) == 'string') {
					// name of function
					window[func](this, args);
				}
			} // foreach event handler defined
		} // user defined handler for event
	}
	
};


