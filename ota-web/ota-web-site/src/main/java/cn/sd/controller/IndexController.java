package cn.sd.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
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
import cn.sd.core.util.IP;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.util.WebUtils;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.entity.produce.CompanyDetail;
import cn.sd.service.common.ICommonService;
import cn.sd.service.route.IRouteService;

@RequestMapping("/")
@Controller
public class IndexController extends ExtSupportAction {
	private static Log log = LogFactory.getLog(IndexController.class);
	
	@Resource
	private ICommonService commonService;
	@Resource
	private IRouteService routeService;
	
	@ResponseBody
	@RequestMapping("/index/routes")
	public ListRange routes(HttpServletRequest request, HttpServletResponse response, MapRange mr, String PID, String ID, String ORDER_BY){
		ListRange result = new ListRange();
		CompanyDetail company = (CompanyDetail)request.getSession().getAttribute("company");
		String company_id = (String)company.getID();
		
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		
		mr.pm.clear();
		mr.pm.put("WAP_ID", company.getWAP_ID());
		mr.pm.put("start", 1);
		mr.pm.put("end", 10);
		mr.pm.put("ORDER", "HOT");
		mr.pm.put("ORDER_STATUS", "2");
		mr.pm.put("BUY_COMPANY_ID", company_id);
		mr.pm.put("AGENCY_ID", company_id);
		mr.pm.put("COMPANY_ID", company_id);
		mr.pm.put("IS_ALONE", "0");
		if(CommonUtils.checkString(company.getIS_ALONE()) && company.getIS_ALONE().toString().equals("1")){
			mr.pm.remove("IS_ALONE");
		}
		
		String s_start_city = (String)request.getSession().getAttribute("s_start_city");
		if(!CommonUtils.checkString(s_start_city)){
			List<String> begin_citys = (List<String>)request.getSession().getAttribute("begin_citys");
			if(CommonUtils.checkList(begin_citys) && begin_citys.size() > 0){
				String areaInfo =  "";
				if(CommonUtils.checkString(WebUtils.getIpAddress(request))){
					areaInfo = Arrays.toString(IP.find(WebUtils.getIpAddress(request)));
				}
				for (String city : begin_citys) {
					if(areaInfo.indexOf(city) != -1){
						request.getSession().setAttribute("s_start_city", city);
						break;
					}
				}
				s_start_city = (String)request.getSession().getAttribute("s_start_city");
				if(!CommonUtils.checkString(s_start_city)){
					for (String city : begin_citys) {
						s_start_city = city;
						request.getSession().setAttribute("s_start_city", city);
						break;
					}
				}
			}
		}
		
		mr.pm.put("BEGIN_CITY", s_start_city);
		
		Map<String, Object> routeCategory = new HashMap<String, Object>();

		/**
			36666E8E0C9DEF38E050007F0100469B	主题推荐
			36666E8E0C9EEF38E050007F0100469B	国内游
			36666E8E0C9FEF38E050007F0100469B	出境游
			36666E8E0CA0EF38E050007F0100469B	海岛度假 改成  主题旅游
			36666E8E0CA1EF38E050007F0100469B	周边游
			36666E8E0CA2EF38E050007F0100469B	自由行
			36666E8E0CA3EF38E050007F0100469B	包机邮轮
		 */
		
		mr.pm.put("end", 1000);
		mr.pm.put("IS_RECOMMEND", 1);
		mr.pm.put("index_order", "hot");
		mr.pm.put("PID", PID);
		mr.pm.put("end", 10000);
		List<Map<String, Object>> hotRoutes = this.routeService.searchRoute(mr.pm);
		routeCategory.put("hotRoutes", hotRoutes);
		
		mr.pm.put("index_order", "top");
		mr.pm.put("end", 6);
		mr.pm.put("IS_TOP", 1);
		List<Map<String, Object>> toproutes = this.routeService.searchRoute(mr.pm);
		routeCategory.put("topRoutes", toproutes);
		routeCategory.put("PID", PID);
		routeCategory.put("ID", ID);
		routeCategory.put("ORDER_BY", ORDER_BY);
		
		data.add(routeCategory);
		result.setData(data);
		result.setSuccess(true);
		return result;
	}
	
	@RequestMapping("")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response, MapRange mr, String appName){

		/**
		 * 1.广告图(焦点,国内,周边,出境)
		 * 2.热门线路
		 * 3.国内游
		 * 4.周边游
		 * 5.出境游
		 * 6.推荐顾问
		 */
		CompanyDetail company = (CompanyDetail)request.getSession().getAttribute("company");
		String company_id = (String)company.getID();
		mr.pm.clear();
		mr.pm.put("COMPANY_ID", company_id);
		List<Map<String, Object>> ADATTR = this.commonService.serachADATTR(mr.pm);
		request.setAttribute("ADATTR", ADATTR);
		
		String s_start_city = request.getParameter("s_start_city");
		if(!CommonUtils.checkString(s_start_city)){
			s_start_city = (String)request.getSession().getAttribute("s_start_city");
			if(!CommonUtils.checkString(s_start_city)){
				List<String> begin_citys = (List<String>)request.getSession().getAttribute("begin_citys");
				if(CommonUtils.checkList(begin_citys) && begin_citys.size() > 0){
					
					String areaInfo =  "";
					if(CommonUtils.checkString(WebUtils.getIpAddress(request))){
						areaInfo = Arrays.toString(IP.find(WebUtils.getIpAddress(request)));
					}
					for (String city : begin_citys) {
						if(areaInfo.indexOf(city) != -1){
							request.getSession().setAttribute("s_start_city", city);
							break;
						}
					}
					s_start_city = (String)request.getSession().getAttribute("s_start_city");
					if(!CommonUtils.checkString(s_start_city)){
						for (String city : begin_citys) {
							s_start_city = city;
							request.getSession().setAttribute("s_start_city", city);
							break;
						}
					}
				}
			}
		}else{
			try {
				s_start_city = new String(s_start_city.getBytes("ISO-8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			request.getSession().setAttribute("s_start_city", s_start_city);
		}
		
		List<Map<String, Object>> categorys = (List<Map<String, Object>>)request.getSession().getAttribute("categorys");
		List<Map<String, Object>> routeCategorys = new ArrayList<Map<String, Object>>();
		int i = 0;
		for (Map<String, Object> category : categorys) {
			
			Map<String, Object> routeCategory = new HashMap<String, Object>();
			
			for(Iterator keyItr = category.keySet().iterator();keyItr.hasNext();) {    
                Object key = keyItr.next();  
                routeCategory.put(key.toString(), category.get(key));
            }  
			routeCategorys.add(i, routeCategory);
			i++;
		}
		
		request.setAttribute("routeCategorys", routeCategorys);
		
		String USER_AGENT = (String)request.getSession().getAttribute("USER_AGENT");
		if(CommonUtils.checkString(USER_AGENT) && USER_AGENT.equals("mobile")){
			return new ModelAndView("/mindex");
		}
		return new ModelAndView("/index");
	}
}
