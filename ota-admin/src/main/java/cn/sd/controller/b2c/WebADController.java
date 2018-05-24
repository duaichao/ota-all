package cn.sd.controller.b2c;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.sd.core.web.ExtSupportAction;
import cn.sd.service.site.ICompanyService;

@RestController
@RequestMapping("/b2c/ad")
public class WebADController extends ExtSupportAction {
	
	private static Log log = LogFactory.getLog(WebADController.class);
	
	@Resource
	private ICompanyService companyService;
	
	@RequestMapping("")
	public ModelAndView traffic(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("b2c/ad");
	}
	
}
