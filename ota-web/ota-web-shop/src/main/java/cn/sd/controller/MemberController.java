package cn.sd.controller;

import cn.sd.core.config.ConfigLoader;
import cn.sd.core.util.*;
import cn.sd.core.util.captcha.CaptchaServiceSingleton;
import cn.sd.core.util.sms.SMSSender;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.service.user.IUserService;
import cn.sd.service.visitor.IVisitorService;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/member")
@Controller
public class MemberController extends ExtSupportAction {
	
	private static Log log = LogFactory.getLog(MemberController.class);
	
	@Resource
	private IUserService userService;
	@Resource
	private IVisitorService visitorService;
	
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
	@RequestMapping("/login")
	public Map<String, Object> login(HttpServletRequest request, HttpServletResponse response, MapRange mr, String userName, String passWord, String loginType,
			String phoneCode, String phoneNumber, String userId){
		Map<String, Object> webUser = new HashMap<String, Object>();
		webUser.put("message", "用户名或密码错误");
		if(CommonUtils.checkString(loginType)){
			if(loginType.equals("0")){
				if(CommonUtils.checkString(userName) && CommonUtils.checkString(passWord)){
					try {
						if(!CommonUtils.checkString(userId)){

							RSAPrivateKey privateKey = (RSAPrivateKey)request.getSession().getAttribute("privateKey");
							passWord = RSAUtil.decryptByPrivateKey(passWord, privateKey);


						}
						mr.pm.put("PASSWORD", CommonUtils.MD5(passWord));
					} catch (Exception e) {
						return webUser;
					}
					mr.pm.put("MOBILE", userName);
					List<Map<String, Object>> webUsers = this.userService.searchWebUser(mr.pm);
					if(CommonUtils.checkList(webUsers) && webUsers.size() == 1){
						webUser = webUsers.get(0);
						webUser.remove("PASSWORD");
						request.getSession().setAttribute("webUser", webUser);
					}
				}	
			}else if(loginType.equals("1")){
				if(CommonUtils.checkString(phoneCode) && CommonUtils.checkString(phoneNumber)){
					String code = (String)request.getSession().getAttribute(phoneNumber);
					if(CommonUtils.checkString(code) && code.equals(phoneCode)){
						mr.pm.put("MOBILE", phoneNumber);
						List<Map<String, Object>> webUsers = this.userService.searchWebUser(mr.pm);
						
						/**
						 * 手机号码是否注册
						 */
						if(!CommonUtils.checkList(webUsers)){
							/**
							 * 注册手机号码为新用户
							 */
							try {
								mr.pm.put("ID", CommonUtils.uuid());
								mr.pm.put("PASSWORD", CommonUtils.MD5("111111"));
								mr.pm.put("USER_NAME", "LXWD"+DateUtil.getCurrDateTimeString());
								this.userService.saveWebUser(mr.pm);
								mr.pm.clear();
								
								mr.pm.put("MOBILE", phoneNumber);
								webUsers = this.userService.searchWebUser(mr.pm);
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
						if(CommonUtils.checkList(webUsers) && webUsers.size() == 1){
							webUser = webUsers.get(0);
							webUser.remove("PASSWORD");
							request.getSession().setAttribute("webUser", webUser);
						}
					}
				}
			}
		}
		return webUser;
	}
	
	@ResponseBody
	@RequestMapping("/reset/password")
	public ListRange resetPasswrod(HttpServletRequest request, HttpServletResponse response, MapRange mr, String phoneCode, String phoneNumber, String passWord){
		ListRange json = new ListRange();
		json.setSuccess(false);
		if(CommonUtils.checkString(phoneCode) && CommonUtils.checkString(phoneNumber) && CommonUtils.checkString(passWord)){
			String code = (String)request.getSession().getAttribute(phoneNumber);
			if(CommonUtils.checkString(code) && code.equals(phoneCode)){
				try {
					mr.pm.clear();
					mr.pm.put("MOBILE", phoneNumber);
					mr.pm.put("PASSWORD", CommonUtils.MD5(passWord));
					this.userService.updateUserPwdByMobile(mr.pm);
					json.setSuccess(true);
				} catch (Exception e) {
					json.setSuccess(false);
				}
			}
		}else{
			json.setMessage("信息填写不完全,请检查!");
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/edit/password")
	public ListRange editPasswrod(HttpServletRequest request, HttpServletResponse response, MapRange mr, String passWord){
		Map<String, Object> webUser = (Map<String, Object>)request.getSession().getAttribute("webUser");
		ListRange json = new ListRange();
		json.setSuccess(false);
		if(CommonUtils.checkString(passWord)){
			try {
				mr.pm.clear();
				mr.pm.put("ID", webUser.get("ID"));
				mr.pm.put("PASSWORD", CommonUtils.MD5(passWord));
				this.userService.updateUserPwdByID(mr.pm);
				json.setSuccess(true);
			} catch (Exception e) {
				json.setSuccess(false);
			}
		}else{
			json.setMessage("信息填写不完全,请检查!");
		}
		return json;
	}
	
	
	@ResponseBody
	@RequestMapping("/register")
	public ListRange register(HttpServletRequest request, HttpServletResponse response, MapRange mr, String phoneCode, String phoneNumber, String passWord){
		ListRange json = new ListRange();
		json.setSuccess(false);
		if(CommonUtils.checkString(phoneCode) && CommonUtils.checkString(phoneNumber) && CommonUtils.checkString(passWord)){
			String code = (String)request.getSession().getAttribute(phoneNumber);
			if(CommonUtils.checkString(code) && code.equals(phoneCode)){
				
				mr.pm.put("MOBILE", phoneNumber);
				List<Map<String, Object>> webUsers = this.userService.searchWebUser(mr.pm);
				if(CommonUtils.checkList(webUsers) || webUsers.size() >= 1){
					json.setSuccess(false);
					json.setMessage("手机号码已注册!");
					return json;
				}
				
				try {
					mr.pm.put("ID", CommonUtils.uuid());
					mr.pm.put("MOBILE", phoneNumber);
					mr.pm.put("PASSWORD", CommonUtils.MD5(passWord));
					mr.pm.put("USER_NAME", "LXWD"+DateUtil.getCurrDateTimeString());
					this.userService.saveWebUser(mr.pm);
					json.setSuccess(true);
				} catch (Exception e) {
					json.setSuccess(false);
				}
			}
		}else{
			json.setMessage("注册信息填写不全,请检查!");
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/phone/exist")
	public ListRange phoneExist(HttpServletRequest request, HttpServletResponse response, MapRange mr, String phoneNumber){
		ListRange json = new ListRange();
		json.setSuccess(true);
		try {
			mr.pm.put("MOBILE", phoneNumber);
			List<Map<String, Object>> webUsers = this.userService.searchWebUser(mr.pm);
			if(!CommonUtils.checkList(webUsers) || webUsers.size() != 1){
				json.setSuccess(false);
				return json;
			}
		} catch (Exception e) {
			json.setSuccess(false);
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/logout")
	public ListRange logout(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		request.getSession().removeAttribute("webUser");
		ListRange json = new ListRange();
		json.setSuccess(true);
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/phone/code")
	public ListRange sendLoginCode(HttpServletRequest request, HttpServletResponse response, MapRange mr, String phoneNumber, String imageCode){
		ListRange json = new ListRange();
		json.setSuccess(true);
		mr.pm.clear();
		imageCode = imageCode.toLowerCase();
		if(CommonUtils.checkString(imageCode)){
			boolean isResponseCorrect = false;
			String captchaId = request.getSession().getId();
			isResponseCorrect = CaptchaServiceSingleton.getInstance().validateResponseForID(captchaId, imageCode);
			if(!isResponseCorrect) {
				json.setSuccess(false);
				return json;
			}
		}
		if(CommonUtils.checkString(phoneNumber)){
			String code = CommonUtils.randomInt(6);
			request.getSession().setAttribute(phoneNumber, code);
			String tip = ConfigLoader.config.getStringConfigDetail("phone_tip");
			tip = tip.replace("code", code);
			try {
				double c = 50.00;
				double cnt = Math.ceil(Double.parseDouble(String.valueOf(tip.length()))/c);
				SMSSender.sendSMS(phoneNumber, tip, "", "", "10", "", "1", "1", "", "", cnt);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping("/imageCaptcha")
	public void imageCaptcha(HttpServletRequest request, HttpServletResponse response, MapRange mr){

		byte[] captchaChallengeAsJpeg = null;
	    ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		try {
		String captchaId = request.getSession().getId();
		BufferedImage challenge = CaptchaServiceSingleton.getInstance().getImageChallengeForID(captchaId, request.getLocale());
		JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(jpegOutputStream);
		jpegEncoder.encode(challenge);
		} catch (Exception e) {
			return;
		}
		try {
			captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
		    response.setHeader("Cache-Control", "no-store");
		    response.setHeader("Pragma", "no-cache");
		    response.setDateHeader("Expires", 0);
		    response.setContentType("image/jpeg");
		    ServletOutputStream responseOutputStream = response.getOutputStream();
		    responseOutputStream.write(captchaChallengeAsJpeg);
		    responseOutputStream.flush();
		    responseOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@ResponseBody
	@RequestMapping("/visitors")
	public ListRange visitors(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		Map<String, Object> webUser = (Map<String, Object>)request.getSession().getAttribute("webUser");
		ListRange json = new ListRange();
		try {
			mr.pm.clear();
			
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			
			mr.pm.put("start", start+1);
			mr.pm.put("end", start+limit);
			
			mr.pm.put("CREATE_USER_ID", webUser.get("ID"));
			int totalSize = this.visitorService.countWebVisitor(mr.pm);
			List<Map<String, Object>> data = this.visitorService.searchWebVisitor(mr.pm);
			json.setTotalSize(totalSize);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询常用游客异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询常用游客异常");
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping("visitor/save")
	public ListRange saveVisitor(HttpServletRequest request,HttpServletResponse response, MapRange mr, String visitor, String id){
		Map<String, Object> webUser = (Map<String, Object>)request.getSession().getAttribute("webUser");
		ListRange json = new ListRange();
		try {
			JSONObject info = JSONObject.fromObject(visitor);
			
			mr.pm.put("NAME", info.get("NAME"));
			mr.pm.put("MOBILE", info.get("MOBILE"));
			mr.pm.put("SEX", info.get("SEX1"));
			mr.pm.put("CARD_TYPE", info.get("CARD_TYPE"));
			mr.pm.put("CARD_NO", info.get("CARD_NO"));
			mr.pm.put("ATTR_ID", info.get("ATTR_ID"));
			mr.pm.put("ATTR_NAME", info.get("ATTR_NAME"));
			mr.pm.put("CREATE_USER_ID", webUser.get("ID"));
			
			if(CommonUtils.checkString(id)){
				mr.pm.put("ID", id);
				this.visitorService.updateWebVisitor(mr.pm);
			}else{
				mr.pm.put("ID", CommonUtils.uuid());
				this.visitorService.saveWebVisitor(mr.pm);
			}
			
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("添加/修改常用游客异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("添加/修改常用游客异常");
		}
		return json;
	}
	
	@ResponseBody
	@RequestMapping("visitor/del")
	public ListRange delVisitor(HttpServletRequest request,HttpServletResponse response, MapRange mr, String id){
		Map<String, Object> webUser = (Map<String, Object>)request.getSession().getAttribute("webUser");
		ListRange json = new ListRange();
		try {
			mr.pm.clear();
			mr.pm.put("ID", id);
			this.visitorService.delWebVisitor(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("删除常用游客异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("删除常用游客异常");
		}
		return json;
	}
	
}
