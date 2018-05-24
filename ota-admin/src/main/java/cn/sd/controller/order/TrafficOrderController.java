package cn.sd.controller.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.DateUtil;
import cn.sd.core.util.ListRange;
import cn.sd.core.util.MapRange;
import cn.sd.core.util.UploadUtil;
import cn.sd.core.web.BaseController;
import cn.sd.service.account.IAccountService;
import cn.sd.service.finance.IFinanceService;
import cn.sd.service.order.IOrderService;
import cn.sd.service.order.IVisitorOrderService;
import cn.sd.service.site.ICompanyService;

@RestController
@RequestMapping("/order/traffic")
public class TrafficOrderController extends BaseController{

	private static Log log = LogFactory.getLog(TrafficOrderController.class);
	
	@Resource
	private IOrderService orderService;
	@Resource
	private IVisitorOrderService visitorOrderService;
	@Resource
	private ICompanyService companyService;
	@Resource
	private IFinanceService financeService;
	@Resource
	private IAccountService accountService;
	
	@RequestMapping("/list")
	public ListRange list(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderTrade, 
			String SALER, String BUYER, String CREATE_BEGIN_TIME, String CREATE_END_TIME, String PAY_BEGIN_TIME, String PAY_END_TIME, 
			String query, String BEGIN_CITY, String END_CITY, String STATUS, String ACCOUNT_STATUS, String orderStatus){
		Map<String, Object> params = new HashMap<String, Object>();
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			int start = toInt(request.getParameter("start"));
			int limit = toInt(request.getParameter("limit"));
			
			mr.pm.put("start", start+1);
			mr.pm.put("end", start+limit);
			
			String USER_ID = (String)user.get("ID"),
			COMPANY_USER_ID = (String)user.get("COMPANY_USER_ID"),
			COMPANY_ID = (String)user.get("COMPANY_ID"),
			COMPANY_TYPE = (String)user.get("COMPANY_TYPE"),
			USER_NAME = (String)user.get("USER_NAME");
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
					if(CommonUtils.checkString(orderTrade) && orderTrade.equals("2")){
						if(USER_ID.equals(COMPANY_USER_ID)){
							mr.pm.put("COMPANY_ID", COMPANY_ID);
						}else{
							
							mr.pm.put("SALE_DEPART_ID", user.get("DEPART_ID"));
							
							//部门经理看部门员工的所有订单
//							String IS_MANAGER = (String)user.get("IS_MANAGER");
//							if(CommonUtils.checkString(IS_MANAGER) && IS_MANAGER.equals("1")){
//								mr.pm.put("SALE_DEPART_ID", user.get("DEPART_ID"));
//							}else{
//								mr.pm.put("PUB_USER_ID", USER_ID);
//							}
							
						}
					}else{
						if(USER_ID.equals(COMPANY_USER_ID)){
							mr.pm.put("CREATE_COMPANY_ID", COMPANY_ID);
						}else{
							
							//部门经理看部门员工的所有订单
							String IS_MANAGER = (String)user.get("IS_MANAGER");
							if(CommonUtils.checkString(IS_MANAGER) && IS_MANAGER.equals("1")){
								mr.pm.put("SALE_DEPART_ID", user.get("DEPART_ID"));
							}else{
								mr.pm.put("CREATE_USER_ID", USER_ID);
							}
							
						}
					}
					
				}else{
					if(USER_ID.equals(COMPANY_USER_ID)){
						//公司管理员看公司所有订单
						mr.pm.put("CREATE_COMPANY_ID", COMPANY_ID);
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
			if(CommonUtils.checkString(BEGIN_CITY)){
				BEGIN_CITY = new String(BEGIN_CITY.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("BEGIN_CITY", BEGIN_CITY);
			}
			if(CommonUtils.checkString(END_CITY)){
				END_CITY = new String(END_CITY.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("END_CITY", END_CITY);
			}
			if(CommonUtils.checkString(SALER)){
				SALER = new String(SALER.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("SALER", SALER);
			}
			if(CommonUtils.checkString(BUYER)){
				BUYER = new String(BUYER.getBytes("ISO-8859-1"), "UTF-8");
				mr.pm.put("BUYER", BUYER);
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
			if(CommonUtils.checkString(orderStatus) && (orderStatus.equals("1")||orderStatus.equals("3")||orderStatus.equals("4"))){
				mr.pm.put("STATUSES", orderStatus);
			}else if(CommonUtils.checkString(orderStatus) && orderStatus.equals("refund")){
				mr.pm.put("STATUSES", "3,4");
			}
			mr.pm.put("PRODUCE_TYPE", 1);
			
			List<Map<String, Object>> data = this.orderService.listService(mr.pm);
			int totalSize = this.orderService.countService(mr.pm);
			json.setData(data);
			json.setTotalSize(totalSize);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询交通订单信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询交通订单信息异常");
		}
		return json;
	}
	
	@RequestMapping("/save")
	public ListRange save(HttpServletRequest request, HttpServletResponse response, MapRange mr, String visitors, String detail){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			Map<String,Object> site = (Map<String,Object>)request.getSession().getAttribute("S_SITE_SESSION_KEY");
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
//			mr.pm.put("SITE_RELA_ID", site.get("ID"));
			mr.pm.put("PRODUCE_TYPE", 1);
			mr.pm.put("detail", detail);
			mr.pm.put("visitors", visitors);
			
			mr.pm.put("RETAIL_SINGLE_ROOM", 0);
			mr.pm.put("INTER_SINGLE_ROOM", 0);
			mr.pm.put("SINGLE_ROOM_CNT", 0);
			
			/**
			 * 对账方式
			 */
			mr.pm.put("ACCOUNT_WAY", user.get("ACCOUNT_WAY"));
			
			Map<String, Object> result = null;
			if(!CommonUtils.checkString(ORDER_ID)){
				ORDER_ID = CommonUtils.uuid();
				mr.pm.put("ID", ORDER_ID);
				result = this.orderService.saveService(mr.pm);
			}else{
				result = this.orderService.updateService(mr.pm);
			}
			json.setMessage(ORDER_ID);
			json.setSuccess((Boolean)result.get("success"));
			json.setStatusCode(CommonUtils.checkString((String)result.get("code"))?(String)result.get("code"):"");
			
			this.orderService.updateAccountStatus(mr.pm, ORDER_ID);
			
		} catch (Exception e) {
			log.error("保存订单信息异常", e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("保存订单信息异常");
		}
		return json;
	}
	
	
	@RequestMapping("/check/pay/pwd")
	public ListRange checkPayPwd(HttpServletRequest request, HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			String PAY_PWD = String.valueOf(user.get("PAY_PWD"));
			String _PAY_PWD = CommonUtils.MD5((String)mr.pm.get("PAY_PWD"));
			int PAY_ERROR_CNT = Integer.parseInt(String.valueOf(user.get("PAY_ERROR_CNT")));
			if(PAY_ERROR_CNT >= 6){
				json.setStatusCode("0");
				json.setSuccess(false);
				return json;
			}else if(!PAY_PWD.equals(_PAY_PWD)){
				PAY_ERROR_CNT = PAY_ERROR_CNT + 1;
				mr.pm.clear();
				mr.pm.put("ID", user.get("ID"));
				mr.pm.put("PAY_ERROR_CNT", PAY_ERROR_CNT);
				this.companyService.updatePayErrorCntService(mr.pm);
				user.put("PAY_ERROR_CNT", String.valueOf(PAY_ERROR_CNT));
				json.setStatusCode(String.valueOf(6-PAY_ERROR_CNT));
				json.setSuccess(false);
				return json;
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("验证支付密码异常", e);
			json.setSuccess(false);
			if(CommonUtils.checkString(e) && e.toString().indexOf("-3") != -1){
				json.setStatusCode("-3");
			}else{
				json.setStatusCode("0");
			}
			json.setMessage("验证支付密码异常");
		}
		return json;
	}
	
	@RequestMapping("/pay")
	public ListRange pay(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderId, String type, String platfrom, String IS_EARNEST){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			/**
			 * 用户账户
			 */
			String is_use = this.accountService.accountStateService((String)user.get("DEPART_ID"));
			if(is_use.equals("启用")){
				mr.pm.put("ORDER_ID", orderId);
				mr.pm.put("CREATE_USER", (String)user.get("USER_NAME"));
				mr.pm.put("CREATE_USER_ID", (String)user.get("ID"));
				mr.pm.put("DEPART_ID", (String)user.get("DEPART_ID"));
				mr.pm.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
				mr.pm.put("type", type);
				mr.pm.put("PLATFROM", platfrom);
				mr.pm.put("isEarnest", IS_EARNEST);
				mr.pm.put("PARENT_COMPANY_ID", (String)user.get("PID"));
				mr.pm.put("PARENT_COMPANY", (String)user.get("PARENT_COMPANY"));
				Map<String, Object> result = this.orderService.payService(mr.pm);
				json.setSuccess((Boolean)result.get("success"));
				json.setStatusCode(CommonUtils.checkString((String)result.get("code"))?(String)result.get("code"):"");
			}else{
				json.setSuccess(false);
				json.setStatusCode("-4");
			}
		} catch (Exception e) {
			log.error("订单支付异常", e);
			json.setSuccess(false);
			if(CommonUtils.checkString(e) && e.toString().indexOf("-3") != -1){
				json.setStatusCode("-3");
			}else{
				json.setStatusCode("0");
			}
			json.setMessage("订单支付异常");
		}
		return json;
	}
	
	@RequestMapping("/online/pay")
	public ListRange onlinePay(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderId, String type, String platfrom){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("ORDER_ID", orderId);
			mr.pm.put("CREATE_USER", (String)user.get("USER_NAME"));
			mr.pm.put("CREATE_USER_ID", (String)user.get("ID"));
			mr.pm.put("DEPART_ID", (String)user.get("DEPART_ID"));
			mr.pm.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
			mr.pm.put("type", type);
			Map<String, Object> result = this.orderService.onlineSeat(mr.pm);
			json.setSuccess((Boolean)result.get("success"));
			json.setStatusCode(CommonUtils.checkString((String)result.get("code"))?(String)result.get("code"):"");
		} catch (Exception e) {
			log.error("订单在线支付异常", e);
			json.setSuccess(false);
			if(CommonUtils.checkString(e) && e.toString().indexOf("-3") != -1){
				json.setStatusCode("-3");
			}else{
				json.setStatusCode("0");
			}
			json.setMessage("订单支付异常");
		}
		return json;
	}
	
	@RequestMapping("/detail")
	public Map<String, Object> detail(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderId){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			mr.pm.put("start", 1);
			mr.pm.put("end", 10);
			if(CommonUtils.checkString(orderId)){
				List<Map<String, Object>> data = this.orderService.listService(mr.pm);	
				if(CommonUtils.checkList(data) && data.size() == 1){
					result = data.get(0);
				}
			}
		} catch (Exception e) {
			log.error("查询交通订单信息异常",e);
		}
		return result;
	}
	
	@RequestMapping("/step")
	public ListRange step(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderId){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("ORDER_ID", orderId);
			List<Map<String, Object>> data = this.orderService.listOrderStepService(mr.pm);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询状态跟踪信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询状态跟踪信息异常");
		}
		return json;
	}
	
	@RequestMapping("/funds")
	public ListRange funds(HttpServletRequest request, HttpServletResponse response, MapRange mr, String orderId){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("ORDER_ID", orderId);
			List<Map<String, Object>> data = this.orderService.listRefundsService(mr.pm);
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("查询资金流水信息异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
			json.setMessage("查询资金流水信息异常");
		}
		return json;
	}
	
	@RequestMapping("/cancelOrder")
	public ListRange cancelOrder(HttpServletRequest request,HttpServletResponse response, MapRange mr, String orderId){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			
			Map<String, Object> params = new HashMap<String, Object>();
			
			params.put("ID", orderId);
			params.put("start", 1);
			params.put("end", 1);
			Map<String, Object> data = this.orderService.listOrderService(params).get(0);
			
			if(!data.get("PAY_TYPE").equals("0")){
				this.orderService.reSeatOnlineService(orderId, (String)mr.pm.get("REMARK"));
			}else{
				params.clear();
				params.put("ORDER_ID", orderId);
				params.put("CREATE_USER", (String)user.get("USER_NAME"));
				params.put("CREATE_USER_ID", (String)user.get("ID"));
				params.put("REMARK", mr.pm.get("REMARK"));
				params.put("CONTRACT_AGENCY_NO", (String)data.get("CON_NO"));
				this.orderService.cancelOrder(params);
				
				params.clear();
				params.put("ID", orderId);
				params.put("start", 1);
				params.put("end", 1);
				data = this.orderService.listOrderService(params).get(0);
				String IS_ALONE = String.valueOf(data.get("IS_ALONE"));
				String SOURCES = String.valueOf(data.get("SOURCES"));
				params.put("COMPANY_ID", (String)data.get("COMPANY_ID"));
				params.put("CREATE_TIME", (String)data.get("CREATE_TIME"));
				if(IS_ALONE.equals("0") && SOURCES.equals("0")){
					params.put("ACCOUNT", "0");
					params.put("ACCOUNT_TYPE", "0");
					params.put("CREATE_COMPANY_ID", data.get("CREATE_COMPANY_ID"));
				}else{
					params.put("ACCOUNT", "-1");
					params.put("ACCOUNT_TYPE", "1");
					params.put("CREATE_COMPANY_ID", data.get("CREATE_COMPANY_ID"));
					params.put("ACCOUNT_COMPANY_ID", data.get("CREATE_COMPANY_ID"));
				}
				this.financeService.editOrderAccountService(params);
				
			}
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("取消订单异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/savefunds")
	public ListRange savefunds(HttpServletRequest request,HttpServletResponse response, MapRange mr, String orderId){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
			mr.pm.put("USER_ID", (String)user.get("ID"));
			mr.pm.put("USER_NAME", (String)user.get("USER_NAME"));
			mr.pm.put("ORDER_ID", orderId);
			this.orderService.saveOrderFundsService(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("保存资金异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/refund")
	public ListRange refund(HttpServletRequest request,HttpServletResponse response, MapRange mr, String orderId, String bills){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("ID", mr.pm.get("ORDER_ID"));
			p.put("start", 1);
			p.put("end", 1);
			List<Map<String, Object>> orders = this.orderService.listOrderService(p);
			Map<String, Object> order = orders.get(0);
			
			/**
			 * 查询订单信息
			 * STATUS = 0 or STATUS = 1 
			 * IS_EARNEST = 5
			 * 只支付了定金的订单,退款只退定金
			 * 修改定金IS_EARNEST = 6 为退款
			 * 
			 */
			
			String STATUS = String.valueOf(order.get("STATUS")), 
					IS_EARNEST = String.valueOf(order.get("IS_EARNEST")), 
					IS_REFUND = String.valueOf(order.get("IS_REFUND"));
			
			if("0".equals(IS_REFUND)){
				json.setSuccess(false);
				json.setStatusCode("-1");//订单不允许退款
				return json;
			}
			mr.pm.put("CREATE_USER_ID", (String)user.get("ID"));
			mr.pm.put("CREATE_USER", (String)user.get("USER_NAME"));
			mr.pm.put("EARNEST_TYPE", String.valueOf(order.get("EARNEST_TYPE")));
			
			if(CommonUtils.checkString(bills)){
				JSONArray jsonarrayBills = JSONArray.fromObject(bills);
				Object[] ObjBills = jsonarrayBills.toArray();
				double REFUND_AMOUNT = 0.0;
				
				for(int i = 0; i < ObjBills.length; i++){
					JSONObject ObjBill = JSONObject.fromObject(ObjBills[i]);
					REFUND_AMOUNT = REFUND_AMOUNT + Double.parseDouble(String.valueOf(ObjBill.get("AMOUNT")));
				}
				double AMOUNT = 0.0;
				
//				STATUS	0待付款 1付款中 2已付款 3待退款 4退款中 5已退款 6手动取消订单 7系统自动取消
//				IS_EARNEST	0:未付定金 1:已付定金,2.定金已确认  3.定金余款已付,4:定金付款中(在线支付使用的状态) 5系统自动退团(定金) 6.定金待退款 7.定金退款中  8.定金已退款
				if((STATUS.equals("2") && (IS_EARNEST.equals("2") || IS_EARNEST.equals("6") || IS_EARNEST.equals("7"))) 
						|| (STATUS.equals("0") && (IS_EARNEST.equals("5") || IS_EARNEST.equals("6") || IS_EARNEST.equals("7")))){
					/**
					 * 定金退款
					 */
					AMOUNT = Double.parseDouble(String.valueOf(order.get("EARNEST_INTER_AMOUNT")));
				}else{
					AMOUNT = Double.parseDouble(String.valueOf(order.get("INTER_AMOUNT"))) - Double.parseDouble(String.valueOf(order.get("DISCOUNT_TOTAL_AMOUNT")));
				}
				
				if(REFUND_AMOUNT > AMOUNT){
					json.setSuccess(false);
					json.setStatusCode("-2");//退款金额大于优惠后的金额
					return json;
				}
			}
			
			mr.pm.put("STATUS", STATUS);
			mr.pm.put("IS_EARNEST", IS_EARNEST);
			
			mr.pm.put("bills", bills);

			this.orderService.refundService(mr.pm);
			
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("启用/禁用异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/refund/audit")
	public ListRange refundAudit(HttpServletRequest request,HttpServletResponse response, MapRange mr, String orderId){
		ListRange json = new ListRange();
		try {
			mr.pm.put("ID", orderId);
			Map<String, Object> order = this.orderService.detailOrderService(mr.pm);
//			STATUS	0待付款 1付款中 2已付款 3待退款 4退款中 5已退款 6手动取消订单 7系统自动取消
//			IS_EARNEST	0:未付定金 1:已付定金,2.定金已确认  3.定金余款已付,4:定金付款中(在线支付使用的状态) 5系统自动退团(定金) 6.定金待退款 7.定金退款中  8.定金已退款
			String STATUS = String.valueOf(order.get("STATUS")), IS_EARNEST = String.valueOf(order.get("IS_EARNEST"));
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			//(代收款定金订单)已尾款付款,定金已确认订单 走定金退款流程
			//定金已付,尾款未付,系统自动退团 走定金退款流程
			if((STATUS.equals("2") && (IS_EARNEST.equals("2") || IS_EARNEST.equals("6") || IS_EARNEST.equals("7"))) 
					|| (STATUS.equals("0") && (IS_EARNEST.equals("5") || IS_EARNEST.equals("6") || IS_EARNEST.equals("7")))){
				Map<String, Object> d = new HashMap<String, Object>();
				d.put("TITLE", "定金");
				d.put("AMOUNT", order.get("EARNEST_INTER_AMOUNT"));
				d.put("ENTITY_TYPE", "3");
				d.put("COMPANY", order.get("SALE_COMPANY_NAME"));
				data.add(d);
				json.setData(data);
				json.setSuccess(true);
				return json;
			}
			
			mr.pm.put("ORDER_ID", orderId);
			mr.pm.put("ACCOUNT_TYPE", 2);
			mr.pm.put("TYPE", 2);
			data = this.orderService.listOrderFundsService(mr.pm);
			/**
			 * 综合价格只显示 交通价
			 */
			int i= 0;
			for (Map<String, Object> d : data) {
//				String IS_FULL_PRICE = String.valueOf(d.get("IS_FULL_PRICE"));
//				String ENTITY_TYPE = String.valueOf(d.get("ENTITY_TYPE"));
//				String IS_RENEW = String.valueOf(d.get("IS_RENEW"));
//				if(IS_FULL_PRICE.equals("1") && ENTITY_TYPE.equals("2") && IS_RENEW.equals("0")){
//					data.remove(d);
//				}
				
				//判断是否是定金订单
				if(!IS_EARNEST.equals("0")){
					if(i == 0){
						d.put("TITLE", "定金");
					}else if(i == 1){
						d.put("TITLE", "尾款");
					}
					i++;
				}
				String AMOUNT = String.valueOf(d.get("AMOUNT"));
				if(AMOUNT.equals("0") ){
					data.remove(d);
				}
			}
//			for (Map<String, Object> d : data) {
//				String entityId = (String)d.get("ENTITY_ID");
//				String entityType = String.valueOf(d.get("ENTITY_TYPE"));
//				mr.pm.clear();
//				mr.pm.put("ID", entityId);
//				mr.pm.put("start", 1);
//				mr.pm.put("end", 1);
//				if(entityType.equals("1")){
//					Map<String, Object> traffic = this.trafficService.listService(mr.pm).get(0);
//					d.put("TITLE", traffic.get("TITLE"));
//					d.put("COMPANY", traffic.get("COMPANY"));
//					d.put("CHINA_NAME", traffic.get("CHINA_NAME"));
//					d.put("USER_NAME", traffic.get("CHINA_NAME"));
//					d.put("MOBILE", traffic.get("MOBILE"));
//				}else if(entityType.equals("2")){
//					Map<String, Object> route = this.routeService.listService(mr.pm).get(0);
//					d.put("TITLE", route.get("TITLE"));
//					d.put("COMPANY", route.get("COMPANY_NAME"));
//					d.put("CHINA_NAME", route.get("CHINA_NAME"));
//					d.put("USER_NAME", route.get("CHINA_NAME"));
//					d.put("MOBILE", route.get("MOBILE"));
//				}
//			}
			/**
			 * 单房差
			 */
			double cnt = Double.parseDouble(String.valueOf(order.get("SINGLE_ROOM_CNT")));
			if(cnt > 0){
				Map<String, Object> m = new HashMap<String, Object>();
				m.putAll(data.get(0));
				m.put("TITLE", "单房差");
				double INTER_SINGLE_ROOM = Double.parseDouble(String.valueOf(order.get("INTER_SINGLE_ROOM")));
				m.put("AMOUNT", (INTER_SINGLE_ROOM * cnt));
				m.put("ENTITY_TYPE", "3");
				data.add(m);
			}
			double OTHER_AMOUNT = Double.parseDouble(String.valueOf(order.get("OTHER_AMOUNT")));
			if(OTHER_AMOUNT != 0){
				Map<String, Object> m = new HashMap<String, Object>();
				m.putAll(data.get(0));
				m.put("TITLE", "其他费用");
				m.put("AMOUNT", OTHER_AMOUNT);
				m.put("ENTITY_TYPE", "3");
				data.add(m);
			}
			json.setData(data);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("退款审核/退款完成异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/trafficVisitors")
	public Map<String, Object> trafficVisitors(HttpServletRequest request, HttpServletResponse response, MapRange mr, String trafficId, String startDate){
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if(!CommonUtils.checkString(trafficId) || !CommonUtils.checkString(startDate)){
				result.put("success", false);
				result.put("statusCode", "-1");
				return result;
			}
			mr.pm.put("PRODUCE_ID", trafficId);
			mr.pm.put("START_DATE", startDate);
			mr.pm.put("start", 1);
			mr.pm.put("end", 1000);
			List<Map<String, Object>> datas = this.visitorOrderService.listOrderVisitorService(mr.pm);
			if(CommonUtils.checkList(datas)){
				String ORDER_ID = "";
				List<Map<String, Object>> orders = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> vistors = new ArrayList<Map<String, Object>>();
				Map<String, Object> order = null;
				for (int i = 0; i < datas.size(); i++) {
					Map<String, Object> data = datas.get(i);
					String _ORDER_ID = (String)data.get("ORDER_ID");
					
					Map<String, Object> visitor = new HashMap<String, Object>();
					visitor.put("ID", data.get("ID"));
					visitor.put("NAME", data.get("NAME"));
					visitor.put("MOBILE", data.get("MOBILE"));
					visitor.put("SEX", data.get("SEX"));
					visitor.put("CARD_TYPE", data.get("CARD_TYPE"));
					visitor.put("CARD_NO", data.get("CARD_NO"));
					visitor.put("NATION", data.get("NATION"));
					visitor.put("TYPE_ID", data.get("TYPE_ID"));
					visitor.put("TYPE_NAME", data.get("TYPE_NAME"));
					vistors.add(visitor);
					
					if(i == 0){
						result.put("PRODUCE_NAME", data.get("PRODUCE_NAME"));
						result.put("START_DATE", startDate);
					}

					if(!ORDER_ID.equals(_ORDER_ID)){
						order = new HashMap<String, Object>();
						order.put("NO", data.get("NO"));
						order.put("START_DATE", data.get("START_DATE"));
						order.put("MAN_COUNT", data.get("MAN_COUNT"));
						order.put("CHILD_COUNT", data.get("CHILD_COUNT"));
						order.put("MUSTER_PLACE", data.get("MUSTER_PLACE"));
						order.put("MUSTER_TIME", data.get("MUSTER_TIME"));
						order.put("COMPANY_NAME", data.get("COMPANY_NAME"));
						order.put("PORDUCE_CONCAT", data.get("PORDUCE_CONCAT"));
						order.put("PRODUCE_MOBILE", data.get("PRODUCE_MOBILE"));
						order.put("COMPANY", data.get("COMPANY"));
						order.put("USER_NAME", data.get("USER_NAME"));
						order.put("PHONE", data.get("PHONE"));
						order.put("BUY_COMPANY", data.get("BUY_COMPANY"));
						order.put("BUY_USER_NAME", data.get("BUY_USER_NAME"));
						order.put("BUY_PHONE", data.get("BUY_PHONE"));
						
						if(i != 0 || data.size() == i){
							order.put("vistors", vistors);
							orders.add(order);
							vistors = new ArrayList<Map<String, Object>>();
						}else if(datas.size() == 1){
							order.put("vistors", vistors);
							orders.add(order);
						}
					}
					
					ORDER_ID = (String)data.get("ORDER_ID");
					
				}
				result.put("orders", orders);
				result.put("success", true);
			}else{
				result.put("success", false);
				result.put("statusCode", "-1");
				return result;
			}
			
		} catch (Exception e) {
			log.error("打印名单错误",e);
		}
		return result;
	}
	
	@RequestMapping("/contract/save")
	public ListRange saveContract(HttpServletRequest request,HttpServletResponse response, MapRange mr){
		ListRange json = new ListRange();
		try {
			Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
			mr.pm.put("start", 1);
			mr.pm.put("end", 1);
			List<Map<String, Object>> orders = this.orderService.listOrderService(mr.pm);
			if(!CommonUtils.checkList(orders)){
				mr.pm.put("CREATE_USER", (String)user.get("USER_NAME"));
				this.orderService.saveContractLogService(mr.pm);
				json.setSuccess(true);
			}else{
				json.setStatusCode("-1");//订单编号重复
				json.setSuccess(false);
			}
		} catch (Exception e) {
			log.error("合同操作异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/contract/order/save")
	public ListRange saveOrderContract(HttpServletRequest request,HttpServletResponse response, MapRange mr, String orderId){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = multipartRequest.getFile("file");
			String path = "";
			if(file != null){
				String file_name = file.getOriginalFilename();
				if(CommonUtils.checkString(file_name)){
					String[] paths = UploadUtil.pathAdmin("contract");
					String file_suffix = file_name.substring(file_name.indexOf("."), file_name.length());
					file_name = CommonUtils.uuid()+file_suffix;
					path = UploadUtil.uploadByte(file.getBytes(), paths[0], paths[1], file_name).replace("\\", "/");
				}
			}
			mr.pm.put("CREATE_USER", user.get("USER_NAME"));
			mr.pm.put("CREATE_USER_ID", user.get("ID"));
			mr.pm.put("CON_ATTR", path);
			mr.pm.put("ORDER_ID", orderId);
			mr.pm.put("ID", CommonUtils.uuid());
			this.orderService.saveOrderContractSerivce(mr.pm);
			json.setSuccess(true);
		} catch (Exception e) {
			log.error("合同操作异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/contract/order/update")
	public ListRange updateOrderContract(HttpServletRequest request,HttpServletResponse response, MapRange mr, String step, String orderId){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			if(step.equals("1")){
				mr.pm.put("APPLY_USER", user.get("USER_NAME"));
				mr.pm.put("APPLY_USER_ID", user.get("ID"));
			}else if(step.equals("2")){
				mr.pm.put("CONFIRM_USER", user.get("USER_NAME"));
				mr.pm.put("CONFIRM_USER_ID", user.get("ID"));
			}
			mr.pm.put("ORDER_ID", orderId);
			mr.pm.put("STEP", step);
			json.setSuccess(true);
			this.orderService.updateOrderContractSerivce(mr.pm);	
		} catch (Exception e) {
			log.error("合同操作异常",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
	@RequestMapping("/edit/lost")
	public ListRange editLost(HttpServletRequest request,HttpServletResponse response, MapRange mr, String orderId){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		ListRange json = new ListRange();
		try {
			mr.pm.clear();
			orderId = request.getParameter("orderId");
			mr.pm.put("ID", orderId);
			int status = this.orderService.editLostService(mr.pm);
			if(status == 0){
				json.setSuccess(true);
			}else{
				json.setStatusCode("-1");//掉单更新失败
				json.setSuccess(false);
			}
		} catch (Exception e) {
			log.error("掉单更新失败",e);
			json.setSuccess(false);
			json.setStatusCode("0");
		}
		return json;
	}
	
}
