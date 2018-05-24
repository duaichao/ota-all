package cn.sd.controller.produce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.sd.controller.site.AreaController;
import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.DateUtil;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.web.BaseController;
import cn.sd.service.order.IOrderService;

@RestController
@Controller("produceTrafficController")
@RequestMapping("/produce/traffic")
public class TrafficController extends BaseController{

	private static Log log = LogFactory.getLog(AreaController.class);
	
	@Resource
	private cn.sd.service.resource.ITrafficService trafficService;
	@Resource
	private IOrderService orderService;
	
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request, HttpServletResponse response, MapRange mr, String query, String queryDate, String startCity, String endCity, String isSingle, String ruleId, String startDate){
		ListRange json = new ListRange();
		try {
			//当前城市的交通
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			String city_id = (String)user.get("CITY_ID");
			String COMPANY_ID = (String)user.get("COMPANY_ID");
//			/**
//			 * 管理员看所有数据
//			 */
//			if(!"admin".equals((String)user.get("USER_NAME"))){
//				/**
//				 * 站长看管理城市数据
//				 */
//				if("0D8AE92D48203E81E050007F0100E920".equals((String)user.get("ROLE_ID"))){
//					mr.pm.put("USER_ID", (String)user.get("ID"));
//					List<Map<String, Object>> sites = this.companyService.listSiteManagerService(mr.pm);
//					if(CommonUtils.checkList(sites)){
//						StringBuffer cityIds = new StringBuffer();
//						int i = 0;
//						for (Map<String, Object> site : sites) {
//							if(i != 0){
//								cityIds.append(",");
//							}
//							i++;
//							cityIds.append("'"+(String)site.get("CITY_ID")+"'");
//						}
//						mr.pm.put("CITY_ID", cityIds);
//					}else{
//						mr.pm.put("CITY_ID", "");
//					}
//				}else{
//					/**
//					 * 登录人的城市数据
//					 */
//					mr.pm.put("CITY_ID", "'"+city_id+"'");
//				}
//			}
			//得到当前用户名和ID
			if(!CommonUtils.checkString(queryDate) && !CommonUtils.checkString(ruleId)){
				queryDate = DateUtil.getNowDate();
			}
			
			Map<String, Object> params = new HashMap<String, Object>();
			if(CommonUtils.checkString(queryDate)){
				params.put("FIRST_DATE", queryDate.replaceAll("-", ""));
				params.put("LAST_DATE", queryDate.replaceAll("-", ""));
			}
			params.put("RULE_ID", ruleId);
			List<Map<String, Object>> compiles = this.trafficService.listTrafficRuleCompileService(params);
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>(); 
			StringBuffer sql = new StringBuffer();
			if(CommonUtils.checkString(queryDate)){
				mr.pm.put("NOW_DATE", queryDate.replaceAll("-", ""));	
			}
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			mr.pm.put("start", start+1);
			mr.pm.put("end", start+limit);
			
			/**
			 * 订单详情传参
			 */
			if(CommonUtils.checkString(startDate)){
//				startDate = startDate.substring(0, 4)+"-"+startDate.subSequence(4, 6)+"-"+startDate.subSequence(6, 8);
				mr.pm.put("NOW_DATE", startDate);
			}
			
			if(CommonUtils.checkString(query)){
				query = new String(query.getBytes("iso-8859-1"), "UTF-8");
				mr.pm.put("query", query);
			}
			
			//在售、单卖
			mr.pm.put("SALE_STATUS", 0);
			mr.pm.put("SALE_TRAFFIC", 1);
			mr.pm.put("IS_SALE", 1);
			mr.pm.put("IS_PUB", 1);
			//出发城市
			//结束城市
			if(CommonUtils.checkString(startCity)){
				startCity = new String(startCity.getBytes("iso-8859-1"), "UTF-8");
				mr.pm.put("START_CITY", startCity);
			}
			
			if(CommonUtils.checkString(endCity)){
				endCity = new String(endCity.getBytes("iso-8859-1"), "UTF-8");
				mr.pm.put("END_CITY", endCity);
			}
			
			if(!CommonUtils.checkString(isSingle) && !CommonUtils.checkString(ruleId)){
				isSingle  = "0";
			}
			mr.pm.put("IS_SINGLE", isSingle);
			mr.pm.put("SALE_TRAFFIC", "1");
			
			int totalSize = 0;
			if(CommonUtils.checkList(compiles)){
				for (int i = 0; i < compiles.size(); i++) {
					sql.append((String)compiles.get(i).get("RULE_SQL"));
					if((i + 1) != compiles.size()){
						sql.append(" union all ");
					}
				}
				mr.pm.put("sql", sql.toString());
				mr.pm.put("NOT_COMPANY_ID", COMPANY_ID);
				data = this.trafficService.listTrafficRuleDateService(mr.pm);
				
				totalSize = trafficService.countTrafficRuleDateService(mr.pm);
			}
			json.setData(data);
			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询交通信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询交通信息异常");
		}
		return json;
	}
	
	@RequestMapping("/buy")
	public ModelAndView buy(HttpServletRequest request, HttpServletResponse response, MapRange mr, String ruleId, String orderId){
		try {
			mr.pm.put("ENTITY_ID", ruleId);
//			List<Map<String, Object>> basePrices = this.trafficService.listBasePriceService(mr.pm);
//			request.setAttribute("basePrices", this.getJsonArray(basePrices));
			List<Map<String, Object>> basePrices = this.trafficService.listBasePriceByAttrService(mr.pm);
			request.setAttribute("basePrices", this.getJsonArray(basePrices));
			//订单
			if(CommonUtils.checkString(orderId)){
				mr.pm.put("start", 1);
				mr.pm.put("end", 10);
				mr.pm.put("ID", orderId);
				List<Map<String, Object>> data = this.orderService.listService(mr.pm);
				request.setAttribute("detail", JSONObject.fromObject(data.get(0)));
			}
			
		} catch (Exception e) {
			log.error("购买交通信息异常",e);
		}
		return new ModelAndView("/produce/traffic/buy");
	}
}