package cn.sd.controller.produce;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.sd.core.web.ExtSupportAction;
@RestController
@RequestMapping("/produce")
public class ScenicController extends ExtSupportAction {
	@RequestMapping("/scenic")
	public ModelAndView scenic(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("/produce/scenic");
	}
}
