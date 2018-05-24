package cn.sd.controller;

import java.io.Serializable;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import cn.sd.core.filter.city.UserCity;
import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.DateUtil;
import cn.sd.core.util.FileUtil;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.LoadProperties;
import cn.sd.core.util.MapRange;
import cn.sd.core.util.RSAUtil;
import cn.sd.core.util.UploadUtil;
import cn.sd.core.util.WebUtils;
import cn.sd.core.util.encode.MD5;
import cn.sd.core.util.sms.SMSSender;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.entity.b2b.ModuleEntity;
import cn.sd.rmi.ServiceProxyFactory;
import cn.sd.security.SecurityContextHolder;
import cn.sd.service.b2b.IADService;
import cn.sd.service.b2b.IModuleService;
import cn.sd.service.b2b.IRoleService;
import cn.sd.service.b2b.ISMSService;
import cn.sd.service.b2b.IUserService;
import cn.sd.service.site.ICompanyService;
import cn.sd.service.site.IDepartService;
import cn.sd.service.site.ISettingService;
import cn.sd.web.Constants;

@Controller
@RequestMapping("/")
public class IndexController extends ExtSupportAction {
	private static Log log = LogFactory.getLog(IndexController.class);
	
	@Resource
	private IUserService userService;
	@Resource
	private IModuleService moduleService;
	@Resource
	private IRoleService roleService;
	@Resource 
	private IDepartService departService;
	@Resource
	private ISettingService settingService;
	
	@Resource
	private IADService adService;
	@Resource
	private ICompanyService companyService;
	
	@Resource
	private ISMSService smsService;
	
	private static LoadProperties prop=new LoadProperties("/properties/sms.properties");
	
	
	@ResponseBody
	@RequestMapping("/ttt")
	public ListRange ttt(HttpServletRequest request, HttpServletResponse response){
		ListRange json = new ListRange();
		ServiceProxyFactory.getProxy().printLoginInfo();
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/login")
	public Map<String,Object> login(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		//解密前台密码

		RSAPrivateKey privateKey = (RSAPrivateKey)request.getSession().getAttribute("privateKey");
		String password = "";


		try {
			password = RSAUtil.decryptByPrivateKey(toString(mr.pm.get("USER_PWD")), privateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		mr.pm.put("USER_PWD", MD5.toMD5(MD5.toMD5(password)));
		mr.pm.put("IP", WebUtils.getIpAddress(request));
		Map<String,Object> user = userService.detailUserService(mr.pm);
		
		if(user!=null){
			if(request.getParameter("appName") != null){
				user.put("appName", "1");
			}
			List<ModuleEntity> data = null;
			Map<String,Object> p = new HashMap<String,Object>();
			String roleId = "all";
			//所属公司的管理员状态，禁用不能登录
			String COMPANY_ID = (String)user.get("COMPANY_ID");
			p.put("COMPANY_ID", COMPANY_ID);
			p.put("IS_USE", "0");
			p.put("AUDIT_STATUS", "1");
			List<Map<String, Object>> d = this.userService.listCompanyManagerService(p);
			if(!CommonUtils.checkList(d) && !((String)user.get("USER_NAME")).equals("admin")){
				user = new HashMap<String,Object>();
				user.put("info", "用户名、密码不正确或用户已被禁用");
				return user;
			}
			p.clear();
			//所属部门的管理员状态，禁用不能登录
			String DEPART_ID = (String)user.get("DEPART_ID");
			if(CommonUtils.checkString(DEPART_ID)){
				p.put("ID", DEPART_ID);
				p.put("IS_USE", "1");
				p.put("start", 1);
				p.put("end", 2);
				List<Map<String, Object>> _d = this.departService.listService(p);
				if(CommonUtils.checkList(_d)){
					user = new HashMap<String,Object>();
					user.put("info", "用户名、密码不正确或用户已被禁用");
					return user;
				}
				p.clear();
			}
			
			if(!mr.pm.get("USER_NAME").equals("admin")){
				List<String> userRole = roleService.userRoleService(user.get("ID").toString());
				//目前设计 角色单选 userRole 为1条数据
				//后期扩展 循环userRole
				if(CommonUtils.checkList(userRole)){
					roleId = userRole.get(0);
					p.put("ROLE_ID", roleId);
					p.put("PID", "-1");
					data = moduleService.userRoleModuleService(p);
				}else{
					p.put("USER_ID", user.get("ID"));
					p.put("PID", "-1");
					p.put("COMPANY_USER_ID", user.get("COMPANY_USER_ID"));
					String companyType = (String)user.get("COMPANY_TYPE");
					if("0,1,2,4,5,6".indexOf(companyType) == -1){
						p.put("companyType", companyType);
					}
					data = moduleService.userModuleService(p);
					roleId = "";
				}
			}else{
				//admin 除禁用所有模块
				p.put("IS_USE", "0");
				p.put("PID", "-1");
				data = moduleService.listService(p);
			}
			p.clear();
			user.put("ROLE_ID", roleId);
			user.put("MODULES", this.getJsonArray(data));
			HttpSession session = request.getSession();
			String PID = (String)user.get("PID");
			if(CommonUtils.checkString(PID) && PID.equals("-1")){
				p.clear();
				p.put("PID", user.get("COMPANY_ID"));
				int child_company_cnt = this.companyService.countCompanyService(p);
				if(child_company_cnt > 0){
					user.put("HAS_CHILD", String.valueOf(child_company_cnt));
				}
				p.clear();
			}
			session.setAttribute(Constants.SESSION_USER_KEY, user);
			//websocket使用
			session.setAttribute(Constants.SESSION_USERNAME, user.get("USER_NAME"));
			//admin和站长没有城市ID
			if(CommonUtils.checkString((String)user.get("CITY_ID"))){
				p.put("CITY_ID", user.get("CITY_ID"));
				session.setAttribute(Constants.SESSION_SITE_KEY, companyService.listSiteManagerService(p).get(0));
			}else{
				if(!"admin".equals((String)user.get("USER_NAME"))){
					p.clear();
					p.put("USER_ID", (String)user.get("ID"));
					List<Map<String, Object>> sites = this.companyService.listSiteManagerService(p);
					if(CommonUtils.checkList(sites)){
						session.setAttribute(Constants.SESSION_SITE_KEY, sites.get(0));
					}
				}
			}
			p.clear();
			SecurityContextHolder.getContext().loginUser(user, session.getId(), true, response, request);
			user.put("success", true);
//			mr.pm.put("ID", user.get("ID"));
			
			user.put("LOGIN_TIME", DateUtil.getNowDateTimeString());
			
//			/**
//			 * 查询是否开通了网店,公司微店只能是公司管理员开通,个人微店只能是公司员工开通,保存微店信息到session中的user
//			 */
//			String ID = (String)user.get("ID");
//			p.clear();
//			p.put("TYPE", 1);
//			p.put("ENTITY_ID", ID);
//			p.put("start", 1);
//			p.put("end", 10000);
//			List<WapSettingEntity> mobileWapSettings = this.companyService.listWapSettingService(p);
//			if(CommonUtils.checkString(mobileWapSettings) && mobileWapSettings.size() == 1){
//				user.put("MOBILE_SETTING_ID", mobileWapSettings.get(0).getID());
//				user.put("MOBILE_SETTING_IS_USE", mobileWapSettings.get(0).getID());
//			}
//			
//			/**
//			 * 公司网店信息
//			 */
//			p.clear();
//			p.put("start", 1);
//			p.put("end", 10000);
//			p.put("TYPE", 0);
//			p.put("COMPANY_ID", user.get("COMPANY_ID"));
//			List<WapSettingEntity> wapSettings = this.companyService.listWapSettingService(p);
//			if(CommonUtils.checkString(wapSettings) && wapSettings.size() == 1){
//				user.put("WAP_SETTING_ID", wapSettings.get(0).getID());
//			}
			
		}else{
			user = new HashMap<String,Object>();
			user.put("info", "用户名、密码不正确或用户已被禁用");
			return user;
		}
		this.userService.saveLoginService(request, "login--tomcat1", "");
		
		/**
		 * 
		 */
//		if(ServiceProxyFactory.getFlag() == 1){
			/**
			 * s.put(uid, r.getSession());
			 * 浏览器多个用户登录,session不创建,只保存引用
			 */
			Enumeration<String> ans = request.getSession().getAttributeNames();
			Map<String, Object> m = new HashMap<String, Object>();
			for(Enumeration<String> e = ans; e.hasMoreElements();){
				String an = (String)e.nextElement();
				if (request.getSession().getAttribute(an) instanceof Serializable) {
					m.put(an, request.getSession().getAttribute(an));
				}
			}
			
			Cookie cookie = WebUtils.getCookie(request, ServiceProxyFactory.SSO_TOKEN_NAME);
			Map<String, Object> _cookie = new HashMap<String, Object>();
			_cookie.put("cv", cookie.getValue());
			_cookie.put("cn", cookie.getName());
			_cookie.put("cc", cookie.getClass().hashCode());
			
			ServiceProxyFactory.getProxy().saveLoginInfo((String)user.get("ID"), (String)user.get("USER_NAME"), request.getSession().getId(), m, _cookie);
			
//		}
		return user;
	}
	@ResponseBody
	@RequestMapping("/rsa")
	public ListRange rsa(HttpServletRequest request, HttpServletResponse response){
		ListRange json = new ListRange();
		json.setSuccess(true);
		try {


			HashMap<String, Object> map = RSAUtil.getKeys();
			//生成公钥和私钥
			RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
			RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");
			//私钥保存在session中，用于解密
			request.getSession().setAttribute("privateKey", privateKey);
			//公钥信息保存在页面，用于加密
			String publicKeyExponent = publicKey.getPublicExponent().toString(16);
			String publicKeyModulus = publicKey.getModulus().toString(16);



	        json.setMessage(publicKeyModulus+"="+publicKeyExponent);
		} catch (Exception e) {
			log.error("解密异常", e);
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/applogout")
	public ListRange applogout(HttpServletRequest request, HttpServletResponse response){
		ListRange json = new ListRange();
		json.setData(new ArrayList());
		json.setSuccess(false);
		json.setMessage("appout"); 
		return json;
	}
	
	@RequestMapping("/logout")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response){
		this.userService.saveLoginService(request, "logout--tomcat1", "");
		this.initLoginPage(request, response);
		HttpSession session = request.getSession();
		String userid = null;
		String user_name = "";
		if(session.getAttribute("S_USER_SESSION_KEY")!=null){
			userid = (String)((Map<String, Object>)session.getAttribute("S_USER_SESSION_KEY")).get("ID");
			user_name = (String)((Map<String, Object>)session.getAttribute("S_USER_SESSION_KEY")).get("USER_NAME");
		}
		
		if(ServiceProxyFactory.getFlag() == 1){
			try {
				ServiceProxyFactory.getProxy().removeLoginInfobyUserId(userid);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("service服务中断 logout");
			}
		}
		
		request.getSession().removeAttribute("S_USER_SESSION_KEY");
		request.getSession().removeAttribute("S_SITE_SESSION_KEY");
		SecurityContextHolder.getContext().logoutUserBySession(request.getSession().getId());
		try {



			HashMap<String, Object> map = RSAUtil.getKeys();
			//生成公钥和私钥
			RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
			RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");
			//私钥保存在session中，用于解密
			request.getSession().setAttribute("privateKey", privateKey);
			//公钥信息保存在页面，用于加密
			String publicKeyExponent = publicKey.getPublicExponent().toString(16);
			String publicKeyModulus = publicKey.getModulus().toString(16);
			request.setAttribute("e", publicKeyExponent);
			request.setAttribute("m", publicKeyModulus);



		} catch (Exception e) {
			e.printStackTrace();
			log.error("解密异常", e);
		}  
		
//		Cookie cookie = WebUtils.getCookie(request, ServiceProxyFactory.SSO_TOKEN_NAME);
//		System.out.println(cookie+"-cookie--logout--flag"+ServiceProxyFactory.getFlag());
//		if(cookie != null && ServiceProxyFactory.getFlag() == 1){
//			try {
//				ServiceProxyFactory.getProxy().removeLoginInfo(cookie.getValue());
//			} catch (Exception e) {
//				e.printStackTrace();
//				log.error("service服务中断 logout");
//			}
//		}
		return new ModelAndView("/login");
	}
	@RequestMapping("/invalid")
	public ModelAndView invalid(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("/invalid");
	}
	
	@ResponseBody
	@RequestMapping("/userKicked")
	public ListRange userKicked(HttpServletRequest request, HttpServletResponse response, String userId, String sessionId) throws InterruptedException{
		
		ListRange json = new ListRange();
		json.setMessage("yes");
		json.setSuccess(true);
//		if(ServiceProxyFactory.getFlag() == 1){
//			
//			try {
//
//				Cookie cookie = WebUtils.getCookie(request, ServiceProxyFactory.SSO_TOKEN_NAME);
//				Map<String, Object>	result = ServiceProxyFactory.getProxy().existCookie(cookie.getValue(), request.getSession().getId());
//				boolean exist = (Boolean)result.get("exist");
//				if(!exist){
//					this.userService.saveLoginService(request, "userKicked", "331");
//					json.setMessage("no");
//					return json;
//				}
//				
//			} catch (Exception e) {
//				log.error("service服务中断");
//				this.userService.saveLoginService(request, "userKicked", "331");
//				json.setMessage("no");
//				return json;
//			}
//			
//		}else{
//			boolean userKicked = SecurityContextHolder.getContext().userKicked(userId,sessionId);
//			if(userKicked){
//				this.userService.saveLoginService(request, "userKicked", "331");
//				json.setMessage("no");
//				return json;
//			}
//		}
		
//		boolean userKicked = SecurityContextHolder.getContext().userKicked(userId,sessionId);
//		if(userKicked){
//			this.userService.saveLoginService(request, "userKicked", "331");
//			json.setMessage("no");
//			return json;
//		}
		
		
		return json;
	}
	
	/**
	 * 切换城市
	 * @param request
	 * @param response
	 * @param cityId
	 * @return
	 * @throws InterruptedException
	 */
	@RequestMapping("/changeCity")
	public String changeCity(HttpServletRequest request, HttpServletResponse response, String cityId) throws InterruptedException{
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("CITY_ID", cityId);
		parameters.put("IS_USE", "0");
		
		List<Map<String, Object>> citys = this.companyService.listSiteManagerService(parameters);
		if(CommonUtils.checkList(citys) && citys.size() == 1){
			Map<String, Object> city = citys.get(0);
			WebUtils.saveCookieValue(response, "XSX-SITE-KEY", ((String)city.get("SUB_AREA")).replace("市", ""), 365*24*60*60);
		}
		return "redirect:/logout";
	}
	
	private void initLoginPage(HttpServletRequest request, HttpServletResponse response){
		
		Map<String, Object> params = new HashMap<String, Object>(); 
		
		//已开通的城市
		List<Map<String, Object>> citys = this.companyService.listSiteManagerService(params);
		request.setAttribute("citys", citys);
		
		UserCity userCity = this.settingService.initCityManager(response, request);
		request.setAttribute("userCity", userCity);
		
		//广告图
		params.put("start", 1);
		params.put("end", 100);
		params.put("CITY_ID", userCity.getLocalCityID());
		List<Map<String, Object>> ads = adService.listService(params);
		request.setAttribute("ads", ads);
		
		params.clear();
		String cityId = userCity.getLocalCityID();
		params.put("CITY_ID", cityId);
		List<Map<String, Object>> siteAttr = this.settingService.listSiteAttrService(params);
		if(CommonUtils.checkList(siteAttr)){
			request.setAttribute("siteAttr", siteAttr.get(0));
		}
		
	}
	
	@ResponseBody
	@RequestMapping("/companys")
	private ListRange companys(HttpServletRequest request, HttpServletResponse response){
		ListRange json = new ListRange();
		Map<String, Object> params = new HashMap<String, Object>(); 
		
		UserCity userCity = this.settingService.initCityManager(response, request);
		int start = toInt(request.getParameter("start"));
		int limit = toInt(request.getParameter("limit"));
		params.put("start", start+1);
		params.put("end", start+limit);
		params.put("CITY_ID", userCity.getLocalCityID());
		params.put("IS_SHOW", "1");
		params.put("AUDIT_STATUS", 1);
		params.put("IS_USE", 0);
		
		//推荐供应商
		List<Map<String, Object>> companys = companyService.listService(params);
		int totalSize = this.companyService.countService(params);
		json.setData(companys);
		json.setTotalSize(totalSize);
		json.setSuccess(true);
		
		return json;
	}
	
	@RequestMapping("/regist")
	public ModelAndView regist(HttpServletRequest request, HttpServletResponse response){
		
		Map<String, Object> params = new HashMap<String, Object>(); 
		
		//已开通的城市
		List<Map<String, Object>> citys = this.companyService.listSiteManagerService(params);
		request.setAttribute("citys", citys);
		
		UserCity userCity = this.settingService.initCityManager(response, request);
		request.setAttribute("userCity", userCity);
		
		params.clear();
		String cityId = userCity.getLocalCityID();
		params.put("CITY_ID", cityId);
		List<Map<String, Object>> siteAttr = this.settingService.listSiteAttrService(params);
		if(CommonUtils.checkList(siteAttr)){
			request.setAttribute("siteAttr", siteAttr.get(0));
		}
		
		return new ModelAndView("/regist");
	}
	
	@ResponseBody
	@RequestMapping("/join")
	public ListRange join(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		//旅行社注册
		
		ListRange json = new ListRange();
		try {
			
			String LICENSE_ATTR = "";
			String LOGO_URL = "";
			String BUSINESS_URL = "";
			/**
			 * 上传文件类型是否正确，-4：图片类型错误,-5：未上传许可证附件，-6：未上传形象图，-7：未上传营业执照
			 */
			try{
				//许可证附件
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				MultipartFile LICENSE_ATTR_FILE = multipartRequest.getFile("LICENSE_ATTR");
				mr.pm.get("LICENSE_ATTR");
				if(LICENSE_ATTR_FILE != null){
					String file_name = LICENSE_ATTR_FILE.getOriginalFilename();
					if(CommonUtils.checkString(file_name)){
						if (FileUtil.checkFilesuffix(file_name,"jpg,gif,png")){
							log.error("图片类型错误");
							json.setSuccess(false);
							json.setStatusCode("-4");
							json.setMessage("图片类型错误");
							return json;
						}else{
							String[] paths = UploadUtil.pathAdmin("admin");
							String file_suffix = file_name.substring(file_name.indexOf("."), file_name.length());
							file_name = CommonUtils.uuid()+file_suffix;
							LICENSE_ATTR = UploadUtil.uploadByte(LICENSE_ATTR_FILE.getBytes(), paths[0], paths[1], file_name).replace("\\", "/");
						}
					}
				}
				
				//形象图
				MultipartFile LOGO_URL_FILE = multipartRequest.getFile("LOGO_URL");
				if(LOGO_URL_FILE != null){
					String file_name = LOGO_URL_FILE.getOriginalFilename();
					if(CommonUtils.checkString(file_name)){
						if (FileUtil.checkFilesuffix(file_name,"jpg,gif,png")){
							log.error("图片类型错误");
							json.setSuccess(false);
							json.setStatusCode("-4");
							json.setMessage("图片类型错误");
							return json;
						}else{
							String[] paths = UploadUtil.pathAdmin("admin");
							String file_suffix = file_name.substring(file_name.indexOf("."), file_name.length());
							file_name = CommonUtils.uuid()+file_suffix;
							LOGO_URL = UploadUtil.uploadByte(LOGO_URL_FILE.getBytes(), paths[0], paths[1], file_name).replace("\\", "/");
						}	
					}
					
				}
				
				//营业执照
				MultipartFile BUSINESS_URL_FILE = multipartRequest.getFile("BUSINESS_URL");
				if(BUSINESS_URL_FILE != null){
					String file_name = BUSINESS_URL_FILE.getOriginalFilename();
					if(CommonUtils.checkString(file_name)){
						if (FileUtil.checkFilesuffix(file_name,"jpg,gif,png")){
							log.error("图片类型错误");
							json.setSuccess(false);
							json.setStatusCode("-4");
							json.setMessage("图片类型错误");
							return json;
						}else{
							String[] paths = UploadUtil.pathAdmin("admin");
							String file_suffix = file_name.substring(file_name.indexOf("."), file_name.length());
							file_name = CommonUtils.uuid()+file_suffix;
							BUSINESS_URL = UploadUtil.uploadByte(BUSINESS_URL_FILE.getBytes(), paths[0], paths[1], file_name).replace("\\", "/");
						}
					}
				}
			}catch(Exception e){
				log.error("上传文件异常",e);
				Map<String,Object> err = new HashMap<String,Object>();
				err.put("code", "101");
				err.put("message", "上传文件异常");
			}
			
			mr.pm.put("LICENSE_ATTR", LICENSE_ATTR);
			mr.pm.put("LOGO_URL", LOGO_URL);
			mr.pm.put("BUSINESS_URL", BUSINESS_URL);
			String statusCode = "0";
//			String CITY_ID = (String)mr.pm.get("CITY_ID");
//			if(!CommonUtils.checkString(CITY_ID)){
//				mr.pm.put("CITY_ID", (String)user.get("CITY_ID"));
//			}
			String PID = (String)mr.pm.get("PID");
			if(!CommonUtils.checkString(PID)){
				mr.pm.put("PID", "-1");
			}
			//只注册旅行社,用户类型和角色类型默认
			mr.pm.put("USER_TYPE", "03");
			mr.pm.put("ROLE_ID", "1");
			//前台申请的用户状态默认都是禁用,前台申请的公司默认都是等待审核
			mr.pm.put("IS_USE", "1");
			mr.pm.put("AUDIT_STATUS", "0");
			mr.pm.put("aginPassword", mr.pm.get("USER_PWD"));
			//前台申请的公司类型由管理员手动修改，TYPE默认为空值
			mr.pm.put("TYPE", "2");
			mr.pm.put("IS_SHOW", "0");
			statusCode = this.companyService.saveSiteManagerService(mr.pm);
			
			if("0".equals(statusCode)){
				
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("CITY_ID", mr.pm.get("CITY_ID"));
				List<Map<String, Object>> users = this.userService.siteServiceService(params);
				if(CommonUtils.checkList(users)){
					String MOBILE = (String)users.get(0).get("MOBILE");
					String content = mr.pm.get("COMPANY")+"于"+DateUtil.getNowDateTimeString()+"在线注册，请尽快审核，联系电话："+mr.pm.get("PHONE");
					double c = 50.00;
					double cnt = Math.ceil(Double.parseDouble(String.valueOf(content.length()))/c);
					SMSSender.sendSMS(MOBILE, content, "system", "", "6", "", "2", "0", "", "", cnt);
				}
				
				json.setSuccess(true);
			}else{
				json.setSuccess(false);
				json.setStatusCode(String.valueOf(statusCode));
			}
		} catch (Exception e) {
			log.error("添加异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("添加异常");
		}
		return json;
	}
	@RequestMapping("/forget")
	public ModelAndView forget(HttpServletRequest request, HttpServletResponse response){
		UserCity userCity = this.settingService.initCityManager(response, request);
		request.setAttribute("userCity", userCity);
		Map<String, Object> params = new HashMap<String, Object>();
		params.clear();
		String cityId = userCity.getLocalCityID();
		params.put("CITY_ID", cityId);
		List<Map<String, Object>> siteAttr = this.settingService.listSiteAttrService(params);
		if(CommonUtils.checkList(siteAttr)){
			request.setAttribute("siteAttr", siteAttr.get(0));
		}
		return new ModelAndView("/forget");
	}
	
	@ResponseBody
	@RequestMapping("/findPwd")
	public ListRange findPwd(HttpServletRequest request, HttpServletResponse response, String mobile, String passWord, String code){
		ListRange json = new ListRange();
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			String _code = (String)request.getSession().getAttribute("code");
			if(_code.equals(code)){
				params.put("MOBILE", mobile);
				request.getSession().removeAttribute("code");
				Map<String,Object> user = this.userService.detailUserService(params);
				if(CommonUtils.checkMap(user)){
					params.put("ID", user.get("ID"));
					params.put("USER_PWD", MD5.toMD5(MD5.toMD5(passWord)));
					params.put("UPDATE_USER", (String)user.get("ID"));
					this.userService.editService(params);
					json.setSuccess(true);
				}else{
					json.setSuccess(false);
					json.setStatusCode("-2");//手机号码不存在
				}
			}else{
				json.setSuccess(false);
				json.setStatusCode("-1");//验证码填写错误
			}
		} catch (Exception e) {
			log.error("配置权限异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/sendCode")
	public ListRange sendCode(HttpServletRequest request, HttpServletResponse response, String mobile, String type){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		Map<String,Object> site = (Map<String,Object>)request.getSession().getAttribute("S_SITE_SESSION_KEY");
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			
			//手机号码不存在，不发送验证码
			if(CommonUtils.checkString(type) && type.equals("1")){
				params.put("MOBILE", mobile);
				user = this.userService.detailUserService(params);
				if(!CommonUtils.checkMap(user)){
					json.setSuccess(false);
					json.setStatusCode("-2");//手机号码不存在
					return json;
				}
			}
			
			//手机号码已存在，不发送验证码
			if(CommonUtils.checkString(type) && type.equals("2")){
				params.put("MOBILE", mobile);
				int size = this.userService.countService(params);
				if(size > 0){
					json.setSuccess(false);
					json.setStatusCode("-3");
					return json;
				}
			}
			
			String USER_NAME = "", SITE_ID = "";
			if(CommonUtils.checkMap(user)){
				USER_NAME = (String)user.get("USER_NAME");
			}
			if(!CommonUtils.checkMap(site)){
				params.put("USER_ID", (String)user.get("ID"));
				List<Map<String, Object>> sites = this.companyService.listSiteManagerService(params);
				if(CommonUtils.checkList(sites)){
					site = sites.get(0);
					SITE_ID = (String)site.get("ID");
				}	
			}
			//发送短信
			String code = CommonUtils.randomInt(6);
			String content = prop.getPropName("dynamicVerificationCode").replace("code", code);
			boolean b = this.smsService.voild(mobile, content, type);
			if(b){
				double c = 50.00;
				double cnt = Math.ceil(Double.parseDouble(String.valueOf(content.length()))/c);
				int status = SMSSender.sendSMS(mobile, content, USER_NAME, "", type, SITE_ID,"1", "0", "", "", cnt);
				if(status >= 0){
					request.getSession().setAttribute("code", code);
					json.setSuccess(true);
				}else{
					json.setSuccess(false);
					json.setStatusCode("-1");//短信发送失败
				}
			}else{
				json.setSuccess(false);
				json.setStatusCode("-4");//短信验证码超过5次，请于一小时后重新发送
			}
		} catch (Exception e) {
			log.error("验证码发送失败",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/threadInfo")
	public ModelAndView threadInfo(HttpServletRequest request, HttpServletResponse response){
		ModelAndView mv = new ModelAndView("/threadInfo");	
		if(ServiceProxyFactory.getFlag()==0){
			mv.addObject("service_type","本地");
			mv.addObject("service_status", "");
		}else{
			try{
				mv.addObject("service_type", "远程");
				ServiceProxyFactory.test();
				mv.addObject("service_status", "已连接");
			}catch(Exception ex){
				mv.addObject("service_status", "失败");
			}
		}
		
		return mv;
	}
	
	@ResponseBody
	@RequestMapping("/serviceTest")
	public ListRange serviceTest(HttpServletRequest request, HttpServletResponse response){
		ListRange json = new ListRange();
		try{
			ServiceProxyFactory.test();
			json.setMessage("成功");
		}catch(Exception ex){
			json.setMessage("失败");
		}
		json.setSuccess(true);
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/accountThreadClear")
	public ListRange accountThreadClear(HttpServletRequest request, HttpServletResponse response){
		ListRange json = new ListRange();
		try{
			ServiceProxyFactory.getProxy().accountThreadClear();
			json.setMessage(ServiceProxyFactory.getProxy().lockContainerInfo());
			json.setSuccess(true);
		}catch(Exception ex){
			ex.printStackTrace();
			json.setMessage("远程未连接");
			json.setSuccess(false);
		}
		
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/fwsieo")
	public ListRange fwsieo(HttpServletRequest request, HttpServletResponse response){
		ListRange json = new ListRange();
		json.setSuccess(true);
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/fwsiec")
	public ListRange fwsiec(HttpServletRequest request, HttpServletResponse response){
		
		ListRange json = new ListRange();
		json.setSuccess(true);
		return json;
	}
	
	
	@RequestMapping("/baidu/map")
	public ModelAndView main(HttpServletRequest request,HttpServletResponse response, MapRange mr, String companyId){
		mr.pm.put("ID", companyId);
		mr.pm.put("start", 1);
		mr.pm.put("end", 1);
		String longitude = "";
		String latitude = "";
		List<Map<String, Object>> data = this.companyService.listCompanyService(mr.pm);
		if(CommonUtils.checkList(data)){
			longitude = (String)data.get(0).get("LONGITUDE");
			latitude = (String)data.get(0).get("LATITUDE");
		}
		request.setAttribute("longitude", CommonUtils.checkString(longitude)?longitude:"");
		request.setAttribute("latitude", CommonUtils.checkString(latitude)?latitude:"");
		return new ModelAndView("/baidu/map");
	}
	
	@ResponseBody
	@RequestMapping("/appcheckin")
	public ListRange appCheckIn(HttpServletRequest request, HttpServletResponse response){
	
		ListRange json = new ListRange();
		if(request.getParameter("appName") == null){
			json.setSuccess(false);
			return json;
		}
		/**
		 * 1 判断服务器是否存在用户
		 * 2 如果不存在，返回-手机自动登录
		 * 3 如果存在，判断是否是手机端登录
		 * 4 如果不是，返回-登录
		 * 5 如果是，返回-手机自动登录
		 * */
//		Map<String, Object> obj = ServiceProxyFactory.getProxy().getUser(request.getParameter("id"));
		Map<String, Object> obj = (Map<String, Object>)request.getSession().getAttribute(Constants.SESSION_USER_KEY);
		
		if(obj==null){
//			json.setMessage("autoLogin");
			json.setSuccess(true);
		}else{
			if(obj.get("appName")==null){
				json.setSuccess(false);
			}else{
				json.setSuccess(true);
			}
		}
		
		try {


			HashMap<String, Object> map = RSAUtil.getKeys();
			//生成公钥和私钥
			RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
			RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");
			//私钥保存在session中，用于解密
			request.getSession().setAttribute("privateKey", privateKey);
			//公钥信息保存在页面，用于加密
			String publicKeyExponent = publicKey.getPublicExponent().toString(16);
			String publicKeyModulus = publicKey.getModulus().toString(16);



			json.setMessage(publicKeyModulus+"="+publicKeyExponent);

		} catch (Exception e) {
			log.error("解密异常", e);
		} 
		
		return json;
	}
	
	
}
