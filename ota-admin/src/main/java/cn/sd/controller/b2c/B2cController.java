package cn.sd.controller.b2c;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.sd.core.web.BaseController;
@RestController
@RequestMapping("/b2c")
public class B2cController extends BaseController {
	
	private static Log log = LogFactory.getLog(B2cController.class);
	
	@RequestMapping("/visitor")
	public ModelAndView visitor(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("/b2c/visitor");
	}
	
	@RequestMapping("/member")
	public ModelAndView member(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("/b2c/member");
	}
	
	@RequestMapping("/minishop")
	public ModelAndView minishop(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("/b2c/minishop");
	}
}
