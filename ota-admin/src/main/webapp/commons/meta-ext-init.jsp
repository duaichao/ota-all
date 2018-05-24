<script type='text/javascript'>
	var currViewInPath = location.pathname.replace(/\//g,'.');
	currViewInPath = currViewInPath.substring(1,currViewInPath.length);
	var autoView = util.getModuleAutoView(currViewInPath),
		autoController = currViewInPath;
	Ext.application({
		name: 'app',
		appFolder:cfg.getJsPath()+'/app',
		autoCreateViewport:autoView,
	    controllers:autoController
	});
</script>
