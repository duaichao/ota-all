Ext.define('Weidian.view.authentication.AuthController', {
    extend: 'Ext.app.ViewController',

    alias: 'controller.auth',
	//选择国家地区code
    onSelectCode:function(sf){
		cfg.view.push(Ext.factory({routeId:'countrycode',store:Ext.create('Weidian.store.CountryCode')},'Weidian.view.authentication.CountryCode'));
	},
	onCodeItemSelected:function(dv,r){
		var registerView = cfg.view.pop();
		registerView.down('textfield#selectCodeTF').setValue(r.get('value'));
	},
	onFinistRegisitButtonTap:function(btn){
		btn.disable();
		var view = this.getView(),
			form = view.down('formpanel'),
			formDatas = form.getValues();
		//验证注册信息
		util.request(cfg.url.register,formDatas,function(rs){
			btn.enable();
			if(rs.success){
				Ext.toast('注册成功!',cfg.toastTimeout);
				this.redirectTo('login');
			}else{
				Ext.toast(rs.message,cfg.toastTimeout);
			}
		},this);
	},
	onResetPwdButtonTap:function(btn){
		btn.disable();
		var view = this.getView(),
			form = view.down('formpanel'),
			formDatas = form.getValues();
		//验证注册信息
		util.request(cfg.url.resetPwd,formDatas,function(rs){
			btn.enable();
			if(rs.success){
				Ext.toast('密码重置成功!',cfg.toastTimeout);
				util.request(cfg.url.logout);
		    	localStorage.removeItem('loggedIn');
		    	cfg.user = null;
		    	this.redirectTo('login');
			}else{
				Ext.toast('信息填写不完整',cfg.toastTimeout);
			}
		},this);
	},
	onEditPasswordButtonTap:function(btn){
		btn.disable();
		var view = this.getView(),
			form = view.down('formpanel'),
			formDatas = form.getValues();
		//验证注册信息
		util.request(cfg.url.editPwd,formDatas,function(rs){
			btn.enable();
			if(rs.success){
				Ext.toast('密码修改成功，请登录!',cfg.toastTimeout);
				util.request(cfg.url.logout);
		    	localStorage.removeItem('loggedIn');
		    	cfg.user = null;
		    	this.redirectTo('login');
			}else{
				Ext.toast('密码修改失败!',cfg.toastTimeout);
			}
		},this);
	}
});
