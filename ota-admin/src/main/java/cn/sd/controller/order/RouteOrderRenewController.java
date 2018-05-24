package cn.sd.controller.order;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.DateUtil;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.web.BaseController;
import cn.sd.power.PowerFactory;
import cn.sd.service.order.IOrderService;
import cn.sd.service.resource.IRouteService;
import cn.sd.service.resource.ITrafficService;
import cn.sd.service.site.ICompanyService;

@RestController
@RequestMapping("order/route/renew")
public class RouteOrderRenewController extends BaseController{

	private static Log log = LogFactory.getLog(RouteOrderRenewController.class);
	
	@Resource
	private ITrafficService trafficService;
	
	@Resource
	private IOrderService orderService;
	@Resource
	private ICompanyService companyService;
	@Resource
	private cn.sd.service.produce.IRouteService produceRouteService;
	@Resource
	private IRouteService routeService;
	
	
	@RequestMapping("/price/type")
	public ListRange priceType(HttpServletRequest request,HttpServletResponse response, MapRange mr, String orderId){
		ListRange json = new ListRange();
		try {
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			if(CommonUtils.checkString(orderId)){
				mr.pm.put("ORDER_ID", orderId);	
				
				List<Map<String, Object>> priceAttrs = this.trafficService.listPriceAttrService(mr.pm);
				StringBuffer sql = new StringBuffer();
				for (Map<String, Object> priceAttr : priceAttrs) {
					sql.append(" ,sum(decode(c_order_by,"+priceAttr.get("ORDER_BY").toString()+",price,0)) as "+priceAttr.get("CON_NAME").toString()+" ");
				}
				mr.pm.put("sql", sql.toString());
				data = this.trafficService.listRenewPriceTypeService(mr.pm);
			}else{
				Map<String, Object> retail_data = new HashMap<String, Object>();
				retail_data.put("TITLE", "外卖");
				retail_data.put("ID", "0FA5123749D28C87E050007F0100BCAD");
				retail_data.put("CHENGREN", 0);
				retail_data.put("ERTONG", 0);
				data.add(retail_data);
				
				Map<String, Object> inter_data = new HashMap<String, Object>();
				inter_data.put("TITLE", "同行");
				inter_data.put("ID", "0FA5123749D38C87E050007F0100BCAD");
				inter_data.put("CHENGREN", 0);
				inter_data.put("ERTONG", 0);
				data.add(inter_data);
			}
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询价格类型异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询价格类型异常");
		}
		return json;
	}
	
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderTrade, 
			String SALER, String BUYER, String CREATE_BEGIN_TIME, String CREATE_END_TIME, String PAY_BEGIN_TIME, String PAY_END_TIME, 
			String query, String BEGIN_CITY, String END_CITY, String STATUS, String ACCOUNT_STATUS, String orderStatus, String renewStatus,
			String IS_CANCEL){
		Map<String, Object> params = new HashMap<String, Object>();
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			mr.pm.put("start", start+1);
			mr.pm.put("end", start+limit);
			mr.pm.put("RENEW_STATUS", renewStatus);
			String USER_ID = (String)user.get("ID"),
			COMPANY_USER_ID = (String)user.get("COMPANY_USER_ID"),
			COMPANY_ID = (String)user.get("COMPANY_ID"),
			COMPANY_TYPE = (String)user.get("COMPANY_TYPE"),
			USER_NAME = (String)user.get("USER_NAME"),
			COMPANY_PID = (String)user.get("PID");
			if("0".equals(COMPANY_TYPE)){
				params.put("T_USER_ID", USER_ID);
				params.put("T_COMPANY_ID", COMPANY_ID);
				params.put("HAS_CITY", "YES");
				List<Map<String, Object>> siteManagers = this.companyService.listSiteManagerService(params);
				if(CommonUtils.checkList(siteManagers)){
					StringBuffer siteIds = new StringBuffer();
					for (Map<String, Object> siteManager : siteManagers) {
						String id = (String)siteManager.get("ID");
						siteIds.append("'"+id+"',");
					}
					mr.pm.put("SITE_RELA_ID", siteIds.substring(0, siteIds.toString().length()-1));
				}
			}else if(!USER_NAME.equals("admin")){
				/**
				 * 默认0 平台管理公司 1供应商公司2分销商公司3门市部4子公司5同行6商务中心
				 * 公司管理员看公司订单
				 * 	旅行社员工查询自己下的订单
				 * 	供应商员工查询自己发的产品订单
				 */
				if("1".equals(COMPANY_TYPE)){
					if(USER_ID.equals(COMPANY_USER_ID)){
						mr.pm.put("COMPANY_ID", COMPANY_ID);
					}else{
						
						mr.pm.put("SALE_DEPART_ID", user.get("DEPART_ID"));
						
						//部门经理看部门员工的所有订单
//						String IS_MANAGER = (String)user.get("IS_MANAGER");
//						if(CommonUtils.checkString(IS_MANAGER) && IS_MANAGER.equals("1")){
//							mr.pm.put("SALE_DEPART_ID", user.get("DEPART_ID"));
//						}else{
//							mr.pm.put("PUB_USER_ID", USER_ID);
//						}
						
					}
				}else{
					if(USER_ID.equals(COMPANY_USER_ID)){
						if(COMPANY_PID.equals("-1")){
							mr.pm.put("COMPANY_PID", COMPANY_ID);
						}else{
							mr.pm.put("CREATE_COMPANY_ID", COMPANY_ID);
						}
					}else{
						
						//部门经理看部门员工的所有订单
						String IS_MANAGER = (String)user.get("IS_MANAGER");
						if(CommonUtils.checkString(IS_MANAGER) && IS_MANAGER.equals("1")){
							mr.pm.put("BUY_DEPART_ID", user.get("DEPART_ID"));
						}else{
							mr.pm.put("CREATE_USER_ID", USER_ID);
						}
						
					}
				}
			}
			
			mr.pm.put("STATUS", STATUS);
			mr.pm.put("ACCOUNT_STATUS", ACCOUNT_STATUS);
			if(CommonUtils.checkString(query)){
				query = new String(query.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("query", query);
			}

			if(CommonUtils.checkString(CREATE_BEGIN_TIME)){
				mr.pm.put("CREATE_BEGIN_TIME", DateUtil.formatString(CREATE_BEGIN_TIME, "") + " 00:00:00");
			}
			if(CommonUtils.checkString(CREATE_END_TIME)){
				mr.pm.put("CREATE_END_TIME", DateUtil.formatString(CREATE_END_TIME, "") + " 23:59:59");
			}
			if(CommonUtils.checkString(PAY_BEGIN_TIME)){
				mr.pm.put("PAY_BEGIN_TIME", DateUtil.formatString(PAY_BEGIN_TIME, "") + " 00:00:00");
			}
			if(CommonUtils.checkString(PAY_END_TIME)){
				mr.pm.put("PAY_END_TIME", DateUtil.formatString(PAY_END_TIME, "") + " 23:59:59");
			}
			if(CommonUtils.checkString(SALER)){
				SALER = new String(SALER.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("SALER", SALER);
			}
			if(CommonUtils.checkString(BUYER)){
				BUYER = new String(BUYER.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("BUYER", BUYER);
			}
			if(CommonUtils.checkString(orderStatus) && (orderStatus.equals("1")||orderStatus.equals("3")||orderStatus.equals("4"))){
				mr.pm.put("STATUSES", orderStatus);
			}else if(CommonUtils.checkString(orderStatus) && orderStatus.equals("refund")){
				mr.pm.put("STATUSES", "3,4");
			}
			mr.pm.put("PRODUCE_TYPES", "'2', '3'");
			mr.pm.put("IS_RENEW", "1");
			
			if(CommonUtils.checkString(IS_CANCEL) && IS_CANCEL.equals("true")){
				mr.pm.put("IS_CANCEL", IS_CANCEL);
			}
			
			List<Map<String, Object>> data = this.orderService.listRouteOrderService(mr.pm);
			int totalSize = this.orderService.countRouteOrderService(mr.pm);
			json.setData(data);
			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询线路订单信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询线路订单信息异常");
		}
		return json;
	}
	
	@RequestMapping("/routes")
	public ListRange routes(HttpServletRequest request, HttpServletResponse response, MapRange mr, String query){
		ListRange json = new ListRange();
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");

		mr.pm.put("IS_ALONE", "0");
		mr.pm.put("USER_ID", (String)user.get("ID"));
		mr.pm.put("MANAGER_SITE", this.companyService.listSiteManagerService(mr.pm));
		mr.pm = PowerFactory.getPower(request, response, "", "route-list", mr.pm);
		
		try {
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			
			mr.pm.put("start", start+1);
			mr.pm.put("end", start+limit);
			if(CommonUtils.checkString(query)){
				query = new String(query.getBytes("iso-8859-1"), "UTF-8");
				mr.pm.put("query", query);
			}
			
			List<Map<String, Object>> data = this.produceRouteService.listRenewService(mr.pm);
			int totalSize = this.produceRouteService.countRenewService(mr.pm);
			json.setData(data);
			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询线路信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询线路信息异常");
		}
		return json;
	}
	
	@RequestMapping("/save")
	public ListRange save(HttpServletRequest request, HttpServletResponse response, MapRange mr, String visitors, String detail, String prices){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			Map<String,Object> site = (Map<String,Object>)request.getSession().getAttribute("S_SITE_SESSION_KEY");
			Map<String, Object> params = new HashMap<String, Object>();
			String ORDER_ID = (String)mr.pm.get("ID");

			String COMPANY_TYPE = String.valueOf(user.get("COMPANY_TYPE"));
			String IS_ALONE = String.valueOf(user.get("IS_ALONE"));
			if((COMPANY_TYPE.equals("2") || COMPANY_TYPE.equals("3") ||  COMPANY_TYPE.equals("4") ||  COMPANY_TYPE.equals("5") || COMPANY_TYPE.equals("6") || COMPANY_TYPE.equals("7")) && IS_ALONE.equals("1")){
				mr.pm.put("IS_ALONE", "1");
			}else{
				mr.pm.put("IS_ALONE", "0");
			}
			
			mr.pm.put("CREATE_USER", user.get("USER_NAME"));
			mr.pm.put("CREATE_USER_ID", user.get("ID"));
			mr.pm.put("CREATE_COMPANY_ID", user.get("COMPANY_ID"));
			mr.pm.put("CREATE_DEPART_ID", user.get("DEPART_ID"));
			mr.pm.put("SITE_RELA_ID", site.get("ID"));
			
			//补单的线路都是3
			mr.pm.put("PRODUCE_TYPE", 3);
			
			mr.pm.put("detail", detail);
			mr.pm.put("visitors", visitors);
			
			Map<String, Object> result = null;
			/**
			 * 单房差
			 * 单房差数量不能超过游客数量
			 * 一人必须买单房差
			 */
			int single_count = Integer.parseInt((String)mr.pm.get("SINGLE_ROOM_CNT"));
			int person_count = Integer.parseInt((String)mr.pm.get("MAN_COUNT")) + Integer.parseInt((String)mr.pm.get("CHILD_COUNT"));
			JSONObject _detail = JSONObject.fromObject(detail);
			params.put("ID", (String)_detail.get("ID"));
			Map<String, Object> r = this.routeService.routeSingleRoomService(params);
			if(single_count > person_count){
				json.setStatusCode("");//------------------------单房差数量超过游客数量
				json.setSuccess(false);
				return json;
			}
			
			if(person_count == 1){
//				mr.pm.put("SINGLE_ROOM_CNT", 1);
			}
			
			mr.pm.put("RETAIL_SINGLE_ROOM", r.get("RETAIL_SINGLE_ROOM"));
			mr.pm.put("INTER_SINGLE_ROOM", r.get("INTER_SINGLE_ROOM"));
			
			/**
			 * 其他价格
			 */
			mr.pm.put("OTHER_PRICE", request.getParameterValues("OTHER_PRICE"));
			mr.pm.put("OTHER_NUM", request.getParameterValues("OTHER_NUM"));
			mr.pm.put("OTHER_ID", request.getParameterValues("OTHER_ID"));
			mr.pm.put("OTHER_TITLE", request.getParameterValues("OTHER_TITLE"));
			mr.pm.put("OTHER_CONTENT", request.getParameterValues("OTHER_CONTENT"));
			
			/**
			 * 对账方式
			 */
			mr.pm.put("ACCOUNT_WAY", user.get("ACCOUNT_WAY"));
			
			/**
			 * 线路基础单价
			 */
			mr.pm.put("PRICES", prices);
			
			if(!CommonUtils.checkString(ORDER_ID)){
				ORDER_ID = CommonUtils.uuid();
				mr.pm.put("ID", ORDER_ID);
				result = this.orderService.saveRouteOrderRenewService(mr.pm);
			}else{
				result = this.orderService.updateRouteOrderRenewService(mr.pm);
			}
			String otherFee = (String)result.get("otherFee");
			if(CommonUtils.checkString(otherFee)){
				json.setMessage(otherFee);
			}else{
				json.setMessage(ORDER_ID);
			}
			json.setSuccess((Boolean)result.get("success"));
			json.setStatusCode(CommonUtils.checkString(result.get("statusCode"))?String.valueOf(result.get("statusCode")):"");
			if((Boolean)result.get("success")){
				this.orderService.updateAccountStatus(mr.pm, ORDER_ID);
			}
			
		} catch (Exception e) {
			log.error("保存补单信息异常", e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存补单信息异常");
		}
		return json;
	}
	
	@RequestMapping("/submit")
	public ListRange submit(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderId){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			mr.pm.clear();
			mr.pm.put("ID", orderId);
			mr.pm.put("RENEW_STATUS", 1);
			this.orderService.updateRenewStatusService(mr.pm);
			
			mr.pm.clear();
			mr.pm.put("ID", CommonUtils.uuid());
			mr.pm.put("ORDER_ID", orderId);
			mr.pm.put("TITLE", "补单提交审核");//-----------------------------
			mr.pm.put("STEP_USER", (String)user.get("user_name"));
			mr.pm.put("STEP_USER_ID", (String)user.get("ID"));
			mr.pm.put("REMARK", "");//-----------------------------;
			this.orderService.saveOrderStepService(mr.pm);
			
		} catch (Exception e) {
			log.error("提交审核补单信息异常", e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("提交审核补单信息异常");
		}
		return json;
	}
	
	@RequestMapping("/audit")
	public ListRange audit(HttpServletRequest request, HttpServletResponse response, MapRange mr, String renewStatus){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			String orderId = (String)mr.pm.get("ID");
			String remark = (String)mr.pm.get("REMARK");
			//0:带提交,1:待审核,2:通过,3:不通过
			String status_content = "";
			mr.pm.put("RENEW_STATUS", renewStatus);
			if(renewStatus.equals("2")){
				mr.pm.put("OTHER_SUPPLY_AUDIT", 2);
				status_content = "审核通过";
			}else{
				status_content = "审核不通过";
			}
			
			this.orderService.updateRenewStatusService(mr.pm);
			
			mr.pm.put("ID", CommonUtils.uuid());
			mr.pm.put("ORDER_ID", orderId);
			mr.pm.put("TITLE", "补单审核");
			mr.pm.put("STEP_USER", (String)user.get("user_name"));
			mr.pm.put("STEP_USER_ID", (String)user.get("ID"));
			if(CommonUtils.checkString(remark)){
				mr.pm.put("REMARK", status_content+","+(String)mr.pm.get("REMARK"));
			}else{
				mr.pm.put("REMARK", status_content);
			}
			this.orderService.saveOrderStepService(mr.pm);
			
			
		} catch (Exception e) {
			log.error("审核补单信息异常", e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("审核补单信息异常");
		}
		return json;
	}
	
}
