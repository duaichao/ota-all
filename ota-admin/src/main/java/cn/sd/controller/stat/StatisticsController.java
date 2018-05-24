package cn.sd.controller.stat;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import cn.sd.core.util.DateUtil;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.web.ExtSupportAction;
import cn.sd.service.site.ICompanyService;
import cn.sd.service.statistics.IStatisticsService;

@RestController
@RequestMapping("/stat")
public class StatisticsController extends ExtSupportAction{

	private static Log log = LogFactory.getLog(StatisticsSaleController.class);
	
	@Resource
	private IStatisticsService statisticsService;
	@Resource
	private ICompanyService companyService;
	
	
	@RequestMapping("/sale")
	public ModelAndView sale(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		String COMPANY_TYPE = request.getParameter("PARAMS_COMPANY_TYPE");
		if(!CommonUtils.checkString(COMPANY_TYPE)){
			COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
		}

		/**
		 * 旅行社分公司,供应商,无分公司的旅行社总公司
		 */
		request.setAttribute("PARAMS_COMPANY_TYPE", COMPANY_TYPE);
		
		String PARAMS_COMPANY_ID = request.getParameter("PARAMS_COMPANY_ID");
		request.setAttribute("PARAMS_COMPANY_ID", PARAMS_COMPANY_ID);
		
		String isParent = request.getParameter("isParent");
		request.setAttribute("isParent", isParent);
		
		String PARAMS_COMPANY_NAME = request.getParameter("PARAMS_COMPANY_NAME");
		if(CommonUtils.checkString(PARAMS_COMPANY_NAME)){
			try {
				PARAMS_COMPANY_NAME = new String(PARAMS_COMPANY_NAME.getBytes("ISO-8859-1"), "UTF-8");
				request.setAttribute("PARAMS_COMPANY_NAME", PARAMS_COMPANY_NAME);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		List<Map<String, Object>> citys = new ArrayList<Map<String, Object>>();
		Map<String,Object> params = new HashMap<String, Object>();
		if(COMPANY_TYPE.equals("0")){
			/**
			 * 站长管理的城市
			 */
			params.put("USER_ID", user.get("ID"));
			
			if("0101".equals((String)user.get("USER_TYPE"))){
				/**
				 * 如果是管理员子账户，那应该继承管理员管理的城市
				 */
				params.clear();
				params.put("ID", user.get("COMPANY_ID"));
				params.put("start", 1);
				params.put("end", 1);
				List<Map<String, Object>> company = this.companyService.listCompanyOfUserService(params);
				if(CommonUtils.checkList(company)){
					params.clear();
					params.put("USER_ID", company.get(0).get("USER_ID"));
				}
				
			}
			
			citys = this.companyService.listSiteManagerService(params);
			
		}
		String[] jsonConfig = {"DEL_TIME"};
		String city = this.getJsonArray(citys, jsonConfig);
		
		request.setAttribute("city", city);
		String str = "";
		if(CommonUtils.checkString(isParent) && ("1".equals(isParent) || "2".equals(isParent))){
			str = "stat/sale";
		}else{
			int child_company_cnt = 0;
			if(CommonUtils.checkString(PARAMS_COMPANY_ID)){
				params.clear();
				params.put("PID", PARAMS_COMPANY_ID);
				child_company_cnt = this.companyService.countCompanyService(params);
			}else{
				child_company_cnt = CommonUtils.checkString(user.get("HAS_CHILD")) ? Integer.parseInt((String)user.get("HAS_CHILD")) : 0;
			}
			str = StatisticsPowerSaleController.firstward(COMPANY_TYPE, user, isParent, child_company_cnt);
		}
		
		return new ModelAndView(str);
	}
	
	/**
	 * 查询统计订单列表
	 * @param params
	 */
	@RequestMapping("/order")
	public ListRange order(HttpServletRequest request,HttpServletResponse response, MapRange mr, String cityId, String type, String date, String routeType, String PARAMS_COMPANY_TYPE, String PARAMS_COMPANY_ID,
			String isParent, String orderStatuses){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			
			mr.pm.clear();
			
			String COMPANY_TYPE = "";
			
			if(CommonUtils.checkString(PARAMS_COMPANY_TYPE)){
				COMPANY_TYPE = PARAMS_COMPANY_TYPE;
			}else{
				COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
			}
			
			request.setAttribute("PARAMS_COMPANY_TYPE", COMPANY_TYPE);
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(user);
			if(CommonUtils.checkString(PARAMS_COMPANY_ID)){
				params.put("COMPANY_ID", PARAMS_COMPANY_ID);
			}
			
			if("today".equals(type)){
				if(!CommonUtils.checkString(date)){
					mr.pm.put("CREATE_TIME", DateUtil.getNowDate());
				}
				StatisticsPowerSaleController.powerLevel(mr, COMPANY_TYPE, cityId, params, "3", (String)user.get("USER_NAME"));
			}else{
				StatisticsPowerSaleController.powerLevel(mr, COMPANY_TYPE, cityId, params, "3", (String)user.get("USER_NAME"));
//				StatisticsPowerSaleController.powerCompany(mr, COMPANY_TYPE, cityId, params, isParent);
			}
			mr.pm.put("ROUTE_TYPE", routeType);
			mr.pm.put("START_DATE", date);
			mr.pm.put("STATUS", orderStatuses);
			
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			mr.pm.put("start", start+1);
			mr.pm.put("end", start+limit);
			
			List<Map<String, Object>> data = this.statisticsService.listOrderService(mr.pm);
			int totalSize = this.statisticsService.countOrderService(mr.pm);
			json.setData(data);
			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询统计订单列表异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询统计订单列表异常");
		}
		return json;
	}
	
	/**
	 * 供应商/旅行社
	 * @param params
	 */
	@RequestMapping("/supply")
	public ListRange supply(HttpServletRequest request,HttpServletResponse response, MapRange mr, String cityId, String type, String routeType, String PARAMS_COMPANY_TYPE, String PARAMS_COMPANY_ID,
			String isParent){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			
			mr.pm.clear();
			
			String COMPANY_TYPE = "";
			
			if(CommonUtils.checkString(PARAMS_COMPANY_TYPE)){
				COMPANY_TYPE = PARAMS_COMPANY_TYPE;
			}else{
				COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
			}
			
			request.setAttribute("PARAMS_COMPANY_TYPE", COMPANY_TYPE);
			
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.putAll(user);
			if(CommonUtils.checkString(PARAMS_COMPANY_ID)){
				params.put("COMPANY_ID", PARAMS_COMPANY_ID);
			}
			
			mr.pm.put("GROUP_TYPE", COMPANY_TYPE.equals("1")?"1":"2");
			
			if("today".equals(type)){
				mr.pm.put("NOW_DATE", DateUtil.getNowDate());
				StatisticsPowerSaleController.powerLevel(mr, COMPANY_TYPE, cityId, params, isParent, (String)user.get("USER_NAME"));
			}else{
				StatisticsPowerSaleController.powerCompany(mr, COMPANY_TYPE, cityId, params, isParent, (String)user.get("USER_NAME"));
			}
			mr.pm.put("ROUTE_TYPE", routeType);
			List<Map<String, Object>> data = this.statisticsService.todaySupplyService(mr.pm);
			
			json.setData(data);
			json.setSuccess(true);
		
		} catch (Exception e) {
			log.error("查询今日供应商异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询今日供应商异常");
		}
		return json;
	}
}
