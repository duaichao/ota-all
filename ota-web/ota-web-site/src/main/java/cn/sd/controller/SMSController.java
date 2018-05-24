package cn.sd.controller;

import java.util.Date;
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

import cn.sd.core.config.ConfigLoader;
import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.DateUtil;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.sms.SMSSender;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.service.user.IUserService;

@Controller
@RequestMapping("/sms")
public class SMSController extends ExtSupportAction {
	
	private static Log log = LogFactory.getLog(SMSController.class);
	
	private static Map<String, Object> SMS_LOG = new HashMap<String, Object>();
	
	@Resource
	private IUserService userService;
	
	@ResponseBody
	@RequestMapping("/send")
	public ListRange send(HttpServletRequest request, HttpServletResponse response, String mobile, String type, String oldMobile){
		ListRange json = new ListRange();
		json.setSuccess(true);
		Map<String, Object> p = new HashMap<String, Object>();
		if(CommonUtils.checkString(mobile) && CommonUtils.checkString(type)){
			Date send_time = (Date)SMS_LOG.get(mobile);
			if(send_time != null){
				int i = DateUtil.getNowDateTime().compareTo(DateUtil.getNSecond(send_time, 120));
				if(i < 0){
					//限制时间内，清等待
					json.setStatusCode("-1");
					json.setSuccess(false);
					return json;
				}
			}
		}else{
			//手机号不存在
			json.setStatusCode("-2");
			json.setSuccess(false);
			return json;
		}
		
		//发送短信
		String tip = ConfigLoader.config.getStringConfigDetail(type);
		
		if(!CommonUtils.checkString(type) || !CommonUtils.checkString(tip)){
			json.setStatusCode("-3");
			json.setSuccess(false);
			return json;
		}
		
		if(type.equals("change_phone_tip")){
			Map<String, Object> webUser = (Map<String, Object>)request.getSession().getAttribute("webUser");
			if(!CommonUtils.checkMap(webUser) || !CommonUtils.checkString(webUser.get("MOBILE")) || 
					!CommonUtils.checkString(mobile) || !webUser.get("MOBILE").toString().equals(oldMobile)){
				json.setStatusCode("-5");
				json.setSuccess(false);
				return json;
			}
		}else{
			p.put("MOBILE", mobile);
			List<Map<String, Object>> webUsers = this.userService.searchWebUser(p);
			
			if(type.equals("reg_tip")){
				if(CommonUtils.checkList(webUsers)){
					//用户名已被注册
					json.setStatusCode("-4");
					json.setSuccess(false);
					return json;
				}
			}else if(type.equals("find_pwd_tip")){
				if(!CommonUtils.checkList(webUsers) || webUsers.size() != 1){
					//用户不存在
					json.setStatusCode("-5");
					json.setSuccess(false);
					return json;
				}
			}else{
				json.setStatusCode("-6");
				json.setSuccess(false);
				return json;
			}
		}
		
		String code = CommonUtils.randomInt(6);
		System.out.println(code+"----------------------------code");
		request.getSession().setAttribute(mobile, code);
		tip = tip.replace("code", code);
		try {
			double c = 50.00;
			double cnt = Math.ceil(Double.parseDouble(String.valueOf(tip.length()))/c);
			int send_status = SMSSender.sendSMS(mobile, tip, "", "", "7", "", "1", "1", "", "", cnt);
			if(send_status == 1){
				json.setStatusCode("0");
				json.setSuccess(true);
				SMS_LOG.put(mobile, new Date());
			}else{
				json.setStatusCode("-3");
				json.setSuccess(false);
				return json;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		//清理过期的发送日志
		return json;
	}
	
}
