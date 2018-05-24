package cn.sd.controller.user;

import java.io.UnsupportedEncodingException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.util.RSAUtil;
import cn.sd.core.util.UploadUtil;
import cn.sd.core.util.WebUtils;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.core.web.taglib.PageUtil;
import cn.sd.service.order.IOrderService;
import cn.sd.service.user.IUserService;

@RequestMapping("/user")
@Controller
public class UserController extends ExtSupportAction {
	private static Log log = LogFactory.getLog(UserController.class);

	private String autoCookieName = "136LYAUTOLOGIN";
	
	@Resource
	private IUserService userService;
	@Resource
	private IOrderService orderService;
	
	@RequestMapping("sec/center")
	public ModelAndView center(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		return new ModelAndView("/user/center/index");
	}
	
	@RequestMapping("sec/center/main")
	public ModelAndView centerMain(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		Map<String, Object> webUser = (Map<String, Object>)request.getSession().getAttribute("webUser");
		mr.pm.put("WAP_USER_ID", webUser.get("ID"));
		mr.pm.put("STATUS", 0);
		int cnt = this.orderService.countOrderBase(mr.pm);
		request.setAttribute("cnt", cnt);
		return new ModelAndView("/user/center/main");
	}
	
	@ResponseBody
	@RequestMapping("sec/update/pwd")
	public ListRange updatePwd(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			
			String oldPwd = request.getParameter("oldPwd"), newPwd = request.getParameter("newPwd");
			
			Map<String, Object> webUser = (Map<String, Object>)request.getSession().getAttribute("webUser");
			
			if(!CommonUtils.MD5(oldPwd).equals(webUser.get("PASSWORD").toString())){
				json.setSuccess(false);
				json.setStatusCode("-1");
				return json;
			}
			
			if(newPwd.length() < 6 || newPwd.length() > 20){
				json.setSuccess(false);
				json.setStatusCode("-2");
				return json;
			}
			
			String pwd = CommonUtils.MD5(newPwd);
			
			mr.pm.put("ID", webUser.get("ID"));
			mr.pm.put("PASSWORD", pwd);
			
			this.userService.updateWebUserPwd(mr.pm);
			
			webUser.put("PASSWORD", pwd);
			
			json.setSuccess(true);
			json.setStatusCode("0");
			
		} catch (Exception e) {
			log.error("修改密码异常",e);
			json.setSuccess(false);
			json.setStatusCode("-3");
			json.setMessage("修改密码异常");
		}
		return json;
	}
	
	@RequestMapping("sec/update/info")
	public ModelAndView updateInfo(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		try {
			Map<String, Object> webUser = (Map<String, Object>)request.getSession().getAttribute("webUser");
			webUser.put("CHINA_NAME", mr.pm.get("CHINA_NAME"));
			webUser.put("SEX", mr.pm.get("SEX"));
			webUser.put("BIRTHDAY", mr.pm.get("BIRTHDAY"));
			this.userService.updateWebUserInfo(mr.pm);
		} catch (Exception e) {
			log.error("修改用户信息异常",e);
		}
		return new ModelAndView("/user/center/info");
	}
	
	@RequestMapping("sec/order")
	public ModelAndView order(HttpServletRequest request, HttpServletResponse response, MapRange mr, String query, String endDate, String beginDate, String status){
		Map<String, Object> webUser = (Map<String, Object>)request.getSession().getAttribute("webUser");
		mr.pm.put("WAP_USER_ID", webUser.get("ID"));

		if(CommonUtils.checkString(status)){
			if(status.equals("pay")){
				mr.pm.put("STATUS", "2");
			}else if(status.equals("unpay")){
				mr.pm.put("STATUS", "0");
			}else if(status.equals("cancel")){
				mr.pm.put("STATUS", "6,7");
			}
		}
		
		mr.pm.put("BEGIN_DATE", beginDate);
		mr.pm.put("END_DATE", endDate);
		
		if(CommonUtils.checkString(query)){
			try {
				query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("query", query);
				request.setAttribute("query", query);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		int recordCount = this.orderService.countOrderBase(mr.pm);
		
		Map<Object, Object> pageResult = PageUtil.page(recordCount, 10, 10, 10, 2, 10, false, request);
		
		mr.pm.put("start", (Long)pageResult.get("start"));
		mr.pm.put("end", (Long)pageResult.get("end"));
		
		List<Map<String, Object>> orders = this.orderService.searchOrderBase(mr.pm);
		request.setAttribute("orders", orders);
		
		request.setAttribute("query", query);
		request.setAttribute("endDate", endDate);
		request.setAttribute("beginDate", beginDate);
		request.setAttribute("status", status);
		return new ModelAndView("/user/center/order");
	}
	
	@ResponseBody
	@RequestMapping("sec/change/phone")
	public ListRange changePhone(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			
			String oldMobile = request.getParameter("oldMobile"), mobile = request.getParameter("mobile"), 
					code = request.getParameter("code");
			
			Map<String, Object> webUser = (Map<String, Object>)request.getSession().getAttribute("webUser");
			
			if(!CommonUtils.checkString(oldMobile) || !CommonUtils.checkString(mobile) || 
					mobile.equals(oldMobile) || oldMobile.length() != 11 && mobile.length() != 11){
				json.setSuccess(false);
				json.setStatusCode("-1");
				return json;
			}
			
			String _code = (String)request.getSession().getAttribute(mobile);
			if(!code.equals(_code)){
				//验证码错误
				json.setSuccess(false);
				json.setStatusCode("-2");
				return json;
			}
			request.getSession().removeAttribute(mobile);
			
			mr.pm.clear();
			mr.pm.put("ID", webUser.get("ID"));
			mr.pm.put("MOBILE", mobile);
			this.userService.updateWebUserMobile(mr.pm);
			
			webUser.put("MOBILE", mobile);
			
			json.setSuccess(true);
			json.setStatusCode("0");
			
		} catch (Exception e) {
			log.error("修改密码异常",e);
			json.setSuccess(false);
			json.setStatusCode("-3");
			json.setMessage("修改密码异常");
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping("sec/face")
	public void face(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		String retVal = "";
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile Filedata = multipartRequest.getFile("Filedata");
			
			Map<String, Object> webUser = (Map<String, Object>)request.getSession().getAttribute("webUser");
			
			String file_name = Filedata.getOriginalFilename();
			String[] paths = UploadUtil.pathAdmin("wap/user/header");
			String file_suffix = file_name.substring(file_name.indexOf("."), file_name.length());
			file_name = CommonUtils.uuid()+file_suffix;
			String p = UploadUtil.uploadByte(Filedata.getBytes(), paths[0], paths[1], file_name).replace("\\", "/");
			mr.pm.put("ID", webUser.get("ID"));
			mr.pm.put("FACE", p);
			this.userService.updateWebUserFace(mr.pm);
			retVal =  "HEAD_IMG|" + p;
			webUser.put("FACE", p);
			response.setContentType("text/html;charset=UTF-8");
			response.getWriter().write(retVal);
			
		} catch (Exception e) {
			log.error("上传头像异常",e);
			retVal =  "errod";
		}
	}
	
	@RequestMapping("mobile/face")
	public ModelAndView mobileFace(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile Filedata = multipartRequest.getFile("Filedata");
			
			Map<String, Object> webUser = (Map<String, Object>)request.getSession().getAttribute("webUser");
			
			String file_name = Filedata.getOriginalFilename();
			String[] paths = UploadUtil.pathAdmin("wap/user/header");
			String file_suffix = file_name.substring(file_name.indexOf("."), file_name.length());
			file_name = CommonUtils.uuid()+file_suffix;
			String p = UploadUtil.uploadByte(Filedata.getBytes(), paths[0], paths[1], file_name).replace("\\", "/");
			mr.pm.put("ID", webUser.get("ID"));
			mr.pm.put("FACE", p);
			this.userService.updateWebUserFace(mr.pm);
			webUser.put("FACE", p);
			
		} catch (Exception e) {
			log.error("上传头像异常",e);
		}
		return new ModelAndView("redirect:/user/sec/center/main");
	}
	
	@RequestMapping("/logout")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		request.getSession().removeAttribute("webUser");
		WebUtils.removeCookie(request, response, autoCookieName);
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping("/to/login")
	public ModelAndView toLogin(HttpServletRequest request, HttpServletResponse response, MapRange mr){
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
		}  
		return new ModelAndView("/user/login");
	}
	
	@RequestMapping("/to/mini/login")
	public ModelAndView toMiniLogin(HttpServletRequest request, HttpServletResponse response, MapRange mr){
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
		}  
		return new ModelAndView("/user/alert_login");
	}
	
	@ResponseBody
	@RequestMapping("/mini/login")
	public ListRange miniLogin(HttpServletRequest request, HttpServletResponse response, MapRange mr){
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
			request.setAttribute("e", publicKeyExponent);
			request.setAttribute("m", publicKeyModulus);
		} catch (Exception e) {
			log.error("加密错误", e);
		}
		String PASSWORD = (String)request.getParameter("PASSWORD");
		String USER_NAME = (String)request.getParameter("USER_NAME");
		if(CommonUtils.checkString(USER_NAME) && CommonUtils.checkString(PASSWORD)){
			try {



				RSAPrivateKey privateKey = (RSAPrivateKey)request.getSession().getAttribute("privateKey");
				PASSWORD = RSAUtil.decryptByPrivateKey(PASSWORD, privateKey);
				mr.pm.put("PASSWORD", CommonUtils.MD5(PASSWORD));
			} catch (Exception e) {
				json.setSuccess(false);
				return json;
			}
			mr.pm.put("MOBILE", USER_NAME);
			
			/**
			 * 使用手机号码登录,原用户名提交上来的就是手机号码
			 */
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("PASSWORD", CommonUtils.MD5(PASSWORD));
			params.put("MOBILE", mr.pm.get("USER_NAME"));
			List<Map<String, Object>> webUsers = this.userService.searchWebUser(params);
			
			if(!CommonUtils.checkList(webUsers) || webUsers.size() != 1){
				json.setSuccess(false);
				return json;
			}
			Map<String, Object> webUser = webUsers.get(0);
			String uuid = (String)webUser.get("ID");
			if(request.getParameter("autoLogin")!=null){
				WebUtils.saveCookieValue(response, autoCookieName, uuid, 60 * 60 * 24 * 14);
			}else{
				WebUtils.saveCookieValue(response, autoCookieName,"", 60 * 60 * 24 * 14);	
			}
			
			request.getSession().setAttribute("webUser", webUser);
			
		}else{
			json.setSuccess(false);
			return json;
		}		
		return json;
	}
	
	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		
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
			log.error("加密错误", e);
		}
		
		String PASSWORD = (String)mr.pm.get("PASSWORD");
		String USER_NAME = (String)mr.pm.get("USER_NAME");
		if(CommonUtils.checkString(USER_NAME) && CommonUtils.checkString(PASSWORD)){
			try {


				RSAPrivateKey privateKey = (RSAPrivateKey)request.getSession().getAttribute("privateKey");
				PASSWORD = RSAUtil.decryptByPrivateKey(PASSWORD, privateKey);
				mr.pm.put("PASSWORD", CommonUtils.MD5(PASSWORD));
			} catch (Exception e) {
				request.setAttribute("loginError", "loginError");
				return new ModelAndView("/user/login");
			}
			
			/**
			 * 使用手机号码登录,原用户名提交上来的就是手机号码
			 */
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("PASSWORD", CommonUtils.MD5(PASSWORD));
			params.put("MOBILE", mr.pm.get("USER_NAME"));
			List<Map<String, Object>> webUsers = this.userService.searchWebUser(params);
			
			if(!CommonUtils.checkList(webUsers) || webUsers.size() != 1){
				request.setAttribute("loginError", "loginError");
				return new ModelAndView("/user/login");
			}
			Map<String, Object> webUser = webUsers.get(0);
			String uuid = (String)webUser.get("ID");
			if(request.getParameter("autoLogin")!=null){
				WebUtils.saveCookieValue(response, autoCookieName, uuid, 60 * 60 * 24 * 14);
			}else{
				WebUtils.saveCookieValue(response, autoCookieName,"", 60 * 60 * 24 * 14);	
			}
			
			request.getSession().setAttribute("webUser", webUser);
			
		}else{
			request.setAttribute("loginError", "loginError");
			return new ModelAndView("/user/login");
		}		
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping("/to/reg")
	public ModelAndView toReg(HttpServletRequest request, HttpServletResponse response, MapRange mr){
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
		}  
		return new ModelAndView("/user/reg");
	}
	
	@ResponseBody
	@RequestMapping("/mini/reg")
	public ListRange miniReg(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		json.setSuccess(true);
		try {
			json.setSuccess(true);
			String USER_NAME = (String)request.getParameter("USER_NAME"), password = (String)request.getParameter("PASSWORD"), MOBILE = (String)request.getParameter("MOBILE"), code = (String)request.getParameter("CODE");
			if(!CommonUtils.checkString(USER_NAME) || !CommonUtils.checkString(password) || !CommonUtils.checkString(MOBILE) || !CommonUtils.checkString(code) || password.length() < 6 || password.length() > 20){
				json.setStatusCode("-3");
				json.setSuccess(false);
				return json;
			}
			
			/**
			 * 用户名是否重复/电话是否存在
			 */
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("USER_NAME_AND_MOBILE", USER_NAME);
			p.put("T_USER_NAME", USER_NAME);
			p.put("T_MOBILE", MOBILE);
			List<Map<String, Object>> webUsers = this.userService.searchWebUser(p);
			if(CommonUtils.checkList(webUsers)){
				//用户名已被注册
				json.setSuccess(false);
				json.setStatusCode("-1");
				return json;
			}
			String _code = (String)request.getSession().getAttribute(MOBILE);
			if(!code.equals(_code)){
				//验证码错误
				json.setSuccess(false);
				json.setStatusCode("-2");
				return json;
			}
			request.getSession().removeAttribute(MOBILE);
			password = CommonUtils.MD5(password);
			mr.pm.put("ID", CommonUtils.uuid());
			mr.pm.put("PASSWORD", password);
			mr.pm.put("MOBILE", MOBILE);
			mr.pm.put("USER_NAME", USER_NAME);
			this.userService.saveWebUser(mr.pm);
			Map<String, Object> webUser = this.userService.searchWebUser(mr.pm).get(0);
			request.getSession().setAttribute("webUser", webUser);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
	
	@RequestMapping("/reg")
	public ModelAndView reg(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		try {
			String USER_NAME = (String)mr.pm.get("USER_NAME"), password = (String)mr.pm.get("PASSWORD"), MOBILE = (String)mr.pm.get("MOBILE"), code = (String)mr.pm.get("CODE");
			if(!CommonUtils.checkString(USER_NAME) || !CommonUtils.checkString(password) || !CommonUtils.checkString(MOBILE) || !CommonUtils.checkString(code) || password.length() < 6 || password.length() > 20){
				return new ModelAndView("/user/reg");
			}
			
			/**
			 * 用户名是否重复/电话是否存在
			 */
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("USER_NAME_AND_MOBILE", USER_NAME);
			p.put("T_USER_NAME", USER_NAME);
			p.put("T_MOBILE", MOBILE);
			List<Map<String, Object>> webUsers = this.userService.searchWebUser(p);
			if(CommonUtils.checkList(webUsers)){
				//用户名已被注册
				return new ModelAndView("redirect:/user/reg?statuscode=-1");
			}
			String _code = (String)request.getSession().getAttribute(MOBILE);
			if(!code.equals(_code)){
				//验证码错误
				return new ModelAndView("redirect:/user/reg?statuscode=-2");
			}
			request.getSession().removeAttribute(MOBILE);
			password = CommonUtils.MD5(password);
			mr.pm.put("ID", CommonUtils.uuid());
			mr.pm.put("PASSWORD", password);
			this.userService.saveWebUser(mr.pm);
			Map<String, Object> webUser = this.userService.searchWebUser(mr.pm).get(0);
			request.getSession().setAttribute("webUser", webUser);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping("/find/pwd")
	public ModelAndView findPwd(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		String MOBILE = (String)mr.pm.get("MOBILE"), code = (String)mr.pm.get("CODE");
		if(!CommonUtils.checkString(MOBILE) || !CommonUtils.checkString(code)){
			return new ModelAndView("redirect:/user/find_pwd.jsp");
		}
		
		String _code = (String)request.getSession().getAttribute(MOBILE);
		
		if(!code.equals(_code)){
			//验证码错误
			return new ModelAndView("redirect:/user/find_pwd?statuscode=-1");
		}
		mr.pm.put("MOBILE", MOBILE);
		List<Map<String, Object>> webUsers = this.userService.searchWebUser(mr.pm);
		if(!CommonUtils.checkList(webUsers) || webUsers.size() != 1){
			return new ModelAndView("redirect:/user/find_pwd.jsp?statuscode=-2");
		}
		
		return new ModelAndView("redirect:/user/find_pwd_step2.jsp?ID="+webUsers.get(0).get("ID")+"&MOBILE="+MOBILE+"&CODE="+code);
	}
	
	@RequestMapping("/edit/pwd")
	public ModelAndView editPwd(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		try {
			String password = (String)mr.pm.get("PASSWORD"), MOBILE = (String)mr.pm.get("MOBILE"), code = (String)mr.pm.get("CODE"), id = (String)mr.pm.get("ID");
			if(!CommonUtils.checkString(id) ||!CommonUtils.checkString(password) || !CommonUtils.checkString(MOBILE) || !CommonUtils.checkString(code) || password.length() < 6 || password.length() > 20){
				//参数错误
				return new ModelAndView("redirect:/user/find_pwd_step2.jsp?error=0&ID="+id+"&MOBILE="+MOBILE+"&CODE="+code);
			}
			String _code = (String)request.getSession().getAttribute(MOBILE);
			if(!code.equals(_code)){
				//验证码错误
				return new ModelAndView("redirect:/user/find_pwd_step2?error=1&ID="+id+"&MOBILE="+MOBILE+"&CODE="+code);
			}
			
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("ID", id);
			List<Map<String, Object>> webUsers = this.userService.searchWebUser(p);
			if(!CommonUtils.checkList(webUsers) || webUsers.size() != 1){
				//用户不存在
				return new ModelAndView("redirect:/user/find_pwd_step2.jsp?error=2&ID="+id+"&MOBILE="+MOBILE+"&CODE="+code);
			}
			
			mr.pm.put("PASSWORD", CommonUtils.MD5(password));
			this.userService.updateWebUserPwd(mr.pm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/user/find_pwd_step3.jsp");
	}
	
}
