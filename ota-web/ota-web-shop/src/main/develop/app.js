/*
 * This file is generated and updated by Sencha Cmd. You can edit this file as
 * needed for your application, but these edits will have to be merged by
 * Sencha Cmd when upgrading.
 */
Ext.application({
    extend: 'Weidian.Application',
    name: 'Weidian',
    viewport: {
        autoMaximize: !Ext.browser.is.Standalone && Ext.os.is.iOS
    },
    requires: [
        'Weidian.*'
    ],
    launch: function() {
    	cfg.view = Ext.Viewport.down('main');
    	util.init();
    	//微信jssdk init
    	if(util.getAllPlatformsInfo().isWeixinApp){
    		var imgUrl = 'resources/images/noface.gif';
    		if(!Ext.isEmpty(Ext.shopInfo.LOGO)){
    			imgUrl = Ext.imgDomain+util.pathImage(Ext.shopInfo.LOGO,'320X320');
    		}
			var config={
				title : Ext.shopInfo.TITLE,
	            desc: Ext.shopInfo.ABOUT,
	            link:  Ext.domain,
	            imgUrl: imgUrl
			};
			setTimeout(function(){util.shareFromWeixinInit(config);},500);
    	}
    }
});
