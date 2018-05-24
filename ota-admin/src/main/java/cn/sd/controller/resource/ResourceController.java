package cn.sd.controller.resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.sd.core.web.BaseController;

@RestController
@RequestMapping("/resource")
public class ResourceController extends BaseController{

	@RequestMapping("/traffic")
	public ModelAndView traffic(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("resource/traffic");
	}
	@RequestMapping("/route")
	public ModelAndView route(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("resource/route");
	}
}
