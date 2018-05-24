package cn.sd.controller.b2b;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.web.BaseController;
import cn.sd.service.b2b.ISMSService;
@RestController
@RequestMapping("/b2b")
public class B2bController extends BaseController {
	private static Log log = LogFactory.getLog(B2bController.class);
	
	@Resource
	private ISMSService smsService;
	
	@RequestMapping("/user")
	public ModelAndView user(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("/b2b/user");
	}
	@RequestMapping("/role")
	public ModelAndView role(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("/b2b/role");
	}
	@RequestMapping("/module")
	public ModelAndView module(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("/b2b/module");
	}
	@RequestMapping("/power")
	public ModelAndView power(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("/b2b/power");
	}
	@RequestMapping("/ad")
	public ModelAndView ad(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("/b2b/ad");
	}
	@RequestMapping("/sms")
	public ModelAndView sms(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> user = (Map<String, Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("COMPANY_ID", user.get("COMPANY_ID"));
		params.put("start", 1);
		params.put("end", 10);
		List<Map<String, Object>> data = this.smsService.listUserSmsService(params);
		if(CommonUtils.checkString(data) && data.size() == 1){
			Map<String, Object> d = data.get(0);
			request.setAttribute("useCount", d.get("ENABLE_COUNT"));
		}else{
			request.setAttribute("useCount", 0);
		}
		return new ModelAndView("/b2b/sms");
	}
	@RequestMapping("/notice")
	public ModelAndView notice(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("/b2b/notice");
	}
	@RequestMapping("/discount")
	public ModelAndView discount(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("/b2b/discount");
	}

}
