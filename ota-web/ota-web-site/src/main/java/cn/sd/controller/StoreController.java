package cn.sd.controller;

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
import org.springframework.web.servlet.ModelAndView;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.entity.produce.CompanyDetail;
import cn.sd.service.route.IRouteService;
import cn.sd.service.user.IUserService;

@Controller
@RequestMapping("/store")
public class StoreController extends ExtSupportAction {
	private static Log log = LogFactory.getLog(StoreController.class);
	
	@Resource
	private IUserService userService;
	@Resource
	private IRouteService routeService;
	
	@RequestMapping("/list")
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		CompanyDetail company  = (CompanyDetail)request.getSession().getAttribute("company");
		String company_id = (String)company.getID();
		mr.pm.put("COMPANY_ID", company_id);
		List<Map<String, Object>> counselors = this.userService.searchCompanyCounselor(mr.pm);
		request.setAttribute("stores", counselors);
		return new ModelAndView("/store/list");
	}
	
	@RequestMapping("/detail")
	public ModelAndView detail(HttpServletRequest request, HttpServletResponse response, MapRange mr, String storeId){
		CompanyDetail company  = (CompanyDetail)request.getSession().getAttribute("company");
		String company_id = company.getID();
		mr.pm.put("COMPANY_ID", company_id);
		mr.pm.put("STORE_ID", storeId);
		List<Map<String, Object>> counselors = this.userService.searchCompanyCounselor(mr.pm);
		request.setAttribute("counselors", counselors);
		request.setAttribute("store", counselors.get(0));
		
		mr.pm.clear();
		mr.pm.put("start", 1);
		mr.pm.put("end", 10);
		mr.pm.put("ORDER", "HOT");
		mr.pm.put("ORDER_STATUS", "2");
		mr.pm.put("BUY_COMPANY_ID", company_id);
		mr.pm.put("AGENCY_ID", company_id);
		mr.pm.put("NOTCOMPANYID", company_id);
		mr.pm.put("COMPANY_ID", company_id);
		mr.pm.put("IS_ALONE", "0");
		if(CommonUtils.checkString(company.getIS_ALONE()) && company.getIS_ALONE().equals("1")){
			mr.pm.remove("IS_ALONE");
		}
		
		String s_start_city = (String)request.getSession().getAttribute("s_start_city");
		if(!CommonUtils.checkString(s_start_city)){
			List<String> begin_citys = (List<String>)request.getSession().getAttribute("begin_citys");
			for (String c : begin_citys) {
				mr.pm.put("BEGIN_CITY", c);
				break;
			}
		}else{
			mr.pm.put("BEGIN_CITY", s_start_city);
		}
		mr.pm.put("WAP_ID", company.getWAP_ID());
		List<Map<String, Object>> hotRoute = this.routeService.searchRoute(mr.pm);
		request.setAttribute("hotRoute", hotRoute);
		return new ModelAndView("/store/detail");
	}
	
	@ResponseBody
	@RequestMapping("/counselors")
	public ListRange counselors(HttpServletRequest request, HttpServletResponse response, MapRange mr, String storeId){
		ListRange result = new ListRange();
		CompanyDetail company  = (CompanyDetail)request.getSession().getAttribute("company");
		String company_id = company.getID();
		mr.pm.put("COMPANY_ID", company_id);
		mr.pm.put("STORE_ID", storeId);
		List<Map<String, Object>> counselors = this.userService.searchCompanyCounselor(mr.pm);
		result.setData(counselors);
		result.setSuccess(true);
		return result;
	}
}
