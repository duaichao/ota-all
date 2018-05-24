package cn.sd.service.order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.DateUtil;
import cn.sd.core.util.sms.SMSSender;
import cn.sd.dao.common.ICommonDao;
import cn.sd.dao.order.IOrderDao;
import cn.sd.dao.route.IRouteDao;
import cn.sd.dao.traffic.ITrafficDao;
import cn.sd.dao.user.IUserDao;
import cn.sd.dao.visitor.IVisitorDao;
import cn.sd.rmi.ServiceProxyByCFactory;
import cn.sd.service.common.ICommonService;
import cn.sd.service.route.IRouteService;

@Service("orderService")
public class OrderService implements IOrderService{

	@Resource
	private IOrderDao orderDao;
	@Resource
	private ITrafficDao trafficDao;
	@Resource
	private IVisitorDao visitorDao;
	@Resource
	private IRouteDao routeDao;
	@Resource
	private IRouteService routeService;
	@Resource
	private ICommonDao commonDao;
	@Resource
	private IUserDao userDao;
	@Resource
	private ICommonService commonService;
	
	public void editOrderAccountService(Map<String, Object> params)throws Exception{
		
		Date CREATE_TIME = DateUtil.parseDate((String)params.get("CREATE_TIME"));
		String YYYYMM = DateUtil.parseDate(CREATE_TIME, "yyyy-MM");
		Date d = DateUtil.parseDate(YYYYMM+"-15");
		if(CREATE_TIME.compareTo(d) > 0){
			this.dateInterval(params, YYYYMM, "2");
		}else{
			this.dateInterval(params, YYYYMM, "1");
		}
		
		List<Map<String, Object>> data = this.orderDao.searchOrderAccount(params);
		if(CommonUtils.checkList(data)){
			params.put("ACCOUNT_ID", data.get(0).get("ID"));
			this.updateAccountStatusService(params);
		}
	}
	
	public void updateAccountStatusService(Map<String, Object> params) throws Exception{
		List<Map<String, Object>> data = this.orderDao.searchOrderAccountPayFinish(params);
		if(CommonUtils.checkList(data)){
			params.put("ACCOUNT_STATUS", "1");
			this.orderDao.updateOrderAccountByIDS(params);
		}
	}
	
	public Map<String, Object> dateInterval(Map<String, Object> params, String beginMonth, String beginType){
		if(beginType.equals("1")){
			params.put("CREATE_BEGIN_TIME", beginMonth+"-01");
			params.put("CREATE_END_TIME", beginMonth+"-15");	
		}else{
			String end_date = DateUtil.formatString(DateUtil.getLastDayOfMonty(DateUtil.parseDate(beginMonth+"-16")), "yyyy-MM-dd");
			params.put("CREATE_BEGIN_TIME", beginMonth+"-16");
			params.put("CREATE_END_TIME", end_date);
		}
		return params;
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void minishopOrderCancel(Map<String, Object> params) throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("success", true);
		result.put("code", "1");
		
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("ORDER_ID", (String)params.get("ORDER_ID"));
		p.put("STATUS", 6);
		this.orderDao.orderCancel(p);
		
		p.clear();
		p.put("ID", CommonUtils.uuid());
		p.put("ORDER_ID", (String)params.get("ORDER_ID"));
		p.put("TITLE", "取消订单");//-----------------------------
		p.put("STEP_USER", (String)params.get("CREATE_USER"));
		p.put("STEP_USER_ID", (String)params.get("CREATE_USER_ID"));
		p.put("REMARK", params.get("REMARK"));//-----------------------------;
		this.orderDao.saveOrderStep(p);
		
	}
	
	public void orderCancel(Map<String, Object> params) throws Exception{
		this.orderDao.orderCancel(params);
	}
	
	public List<Map<String, Object>> listOrderContact(Map<String, Object> params){
		return this.orderDao.listOrderContact(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public Map<String, Object> onlineSeat(Map<String, Object> params)throws Exception{
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", true);
		result.put("code", "1");
		
		String ORDER_ID = (String)params.get("ORDER_ID");
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("ID", ORDER_ID);
		p.put("start", 1);
		p.put("end", 1);
		Map<String, Object> order = this.orderDao.searchOrderBase(p).get(0);
		String rq = order.get("START_DATE").toString().replaceAll("-", "");
		String plan_id = (String)order.get("TRAFFIC_ID");
		String order_id = ORDER_ID;
		int sale_seat = Integer.parseInt(String.valueOf(order.get("MAN_COUNT")))+Integer.parseInt(String.valueOf(order.get("CHILD_COUNT")));
//		int seat = ServiceProxyByCFactory.getProxy().usedSeatByRouteOrder(rq,plan_id,order_id,Integer.toString(sale_seat));
		int seat = 0;
		if(seat == 0){
			
		}else{
			result.put("success", false);
			result.put("code", "-2");//占座失败
		}
		
		return result;
	}
	public List<Map<String, Object>> listDiscountOrderService(Map<String, Object> params){
		return this.orderDao.listDiscountOrderDao(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public Map<String, Object> saveOrder(Map<String, Object> params) throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", true);
		
		String COMPANY_ID = (String)((Map<String, Object>)params.get("route")).get("COMPANY_ID");
		
//		String NO = CommonUtils.randomString(10);
		
		String NO = "-1";
		NO = ServiceProxyByCFactory.getProxy().getRouteOrderNoDao(COMPANY_ID);
		if(NO.equals("-1")){
			result.put("success", false);
			result.put("statusCode", "-1");
			return result;
		}
		
		Map<String, Object> route = (Map<String, Object>)params.get("route");
		Map<String, Object> p = new HashMap<String, Object>();
		
		String ORDER_ID = (String)params.get("ID");
		String ROUTE_ID = (String)route.get("ID");
		
		params.put("NO", NO);
		
		params.put("PRODUCE_ID", ROUTE_ID);
		params.put("TRAFFIC_ID", (String)params.get("PLAN_ID"));
		params.put("PORDUCE_CONCAT", (String)route.get("PORDUCE_CONCAT"));
		params.put("PRODUCE_USER_ID", (String)route.get("CREATE_USER_ID"));
		if(CommonUtils.checkString(route.get("PRODUCE_MOBILE"))){
			params.put("PRODUCE_MOBILE", String.valueOf(route.get("PRODUCE_MOBILE")));
		}
		
		params.put("COMPANY_ID", (String)route.get("COMPANY_ID"));
		params.put("COMPANY_NAME", (String)route.get("COMPANY_NAME"));
		
		params.put("STATUS", 0);
		params.put("PAY_TYPE", 0);
		params.put("ACCOUNT_STATUS", 0);
		
		p.clear();
		List<Map<String, Object>> priceAttrs = this.trafficDao.searchPriceAttr(p);
		for (Map<String, Object> priceAttr: priceAttrs) {
			String ID = (String)priceAttr.get("ID");
			if(ID.equals("0FA5123749D08C87E050007F0100BCAD")){
				p.put("PID", "0FA5123749D08C87E050007F0100BCAD");
				List<Map<String, Object>> childs = this.trafficDao.searchPriceAttr(p);
				for (Map<String, Object> map : childs) {
					priceAttrs.add(map);
				}
				break;
			}
		}
		
		
		p.clear();
		p.put("ROUTE_ID", ROUTE_ID);
		p.put("RQ", params.get("START_DATE").toString().replaceAll("-", ""));
		p.put("PLAN_ID", params.get("PLAN_ID"));
		
		List<Map<String, Object>> planPrices = this.routeService.listPlanPricesService(p);
		
		double sale_amount = 0.0, inter_amount = 0.0;
		
		/**
		 * 保存游客
		 * 不同类型的游客数量
		 * 成人
		 * 婴儿
		 * 不占床位/不含门票
		 * 不占床位/含门票
		 * 占床位/不含门票
		 * 占床位/含门票
		 */
	    
		String[] names = (String[])params.get("visitor_name");
		String[] types = (String[])params.get("visitor_type");
		
		String[] sexs = (String[])params.get("visitor_sex");
		String[] mobiles = (String[])params.get("visitor_mobile");
		String[] ids = (String[])params.get("visitor_id");
		String[] id_nos = (String[])params.get("visitor_id_no");
		
		int MAN_COUNT = 0, CHILD_COUNT = 0;
		String wapPrices = (String)params.get("wapPrices");
		for (int i = 0; i < names.length; i++) {
			
			for (Map<String, Object> priceAttr : priceAttrs) {
				for (Map<String, Object> price : planPrices) {
					if(types[i].equals(priceAttr.get("ID"))){
						if(price.get("ID").equals("0FA5123749D28C87E050007F0100BCAD")){
							if(!CommonUtils.checkString(wapPrices)){
								sale_amount +=  Double.parseDouble(String.valueOf(price.get(priceAttr.get("CON_NAME"))));
							}
						}else{
							if(CommonUtils.checkString(wapPrices)){
								sale_amount +=  Double.parseDouble(String.valueOf(price.get(priceAttr.get("CON_NAME")))) + Double.parseDouble(wapPrices);
							}
							inter_amount +=  Double.parseDouble(String.valueOf(price.get(priceAttr.get("CON_NAME"))));
						}	
					}
				}
			}
			
			p.clear();
			
			String VISITOR_ID = CommonUtils.uuid();
			p.put("ID", VISITOR_ID);
			p.put("NAME", names[i]);
			p.put("ATTR_ID", types[i]);
			
			if(types[i].equals("0FA5123749CF8C87E050007F0100BCAD")){
				MAN_COUNT++;
			}else{
				CHILD_COUNT++;
			}
			
			for (Map<String, Object> priceAttr: priceAttrs) {
				String attr_id = (String)priceAttr.get("ID");
				if(types[i].equals(attr_id)){
					p.put("ATTR_NAME", priceAttr.get("TITLE"));
				}
			}
			
			/**
			 * 订单联系人已重建表
			 * 原订单联系人字段废弃
			 */
//			if(CommonUtils.checkString(contact[i])){
//				params.put("VISITOR_CONCAT", names[i]);
//				params.put("VISITOR_MOBILE", mobiles[i]);
//			}
			
			p.put("SEX", sexs[i]);
			p.put("MOBILE", mobiles[i]);
			p.put("CARD_TYPE", ids[i]);
			p.put("CARD_NO", id_nos[i]);
			this.visitorDao.saveVisitor(p);
			
			p.clear();
			p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", ORDER_ID);
			p.put("VISITOR_ID", VISITOR_ID);
			this.visitorDao.saveOrderVisitor(p);
			
		}
		
		/**
		 * 保存订单联系人
		 */
		String[] contact_name = (String[])params.get("contact_name");
		String[] contact_phone = (String[])params.get("contact_phone");
		for (int j = 0; j < contact_name.length; j++) {
			p.clear();
			p.put("ID", CommonUtils.uuid());
			p.put("CHINA_NAME", contact_name[j]);
			p.put("MOBILE", contact_phone[j]);
			p.put("ORDER_ID", ORDER_ID);
			p.put("CREATE_USER_ID", params.get("CREATE_USER_ID"));
			p.put("COMPANY_ID", params.get("CREATE_DEPART_ID"));
			this.orderDao.saveOrderContact(p);
		}
		
		/**
		 * 保存线路基础价格
		 * 保存线路交通基础价格
		 * 保存线路交通汇总价格
		 */
		
		p.clear();
		String PLAN_ID = (String)params.get("PLAN_ID");
		p.put("ENTITY_ID", "'"+ROUTE_ID+"'");
		p.put("ORDER_ID", ORDER_ID);
		p.put("ENTITY_TYPE", "2");
		this.orderDao.saveOrderPrice(p);
		
		if(CommonUtils.checkString(PLAN_ID)){
			p.clear();
			p.put("ROUTE_ID", ROUTE_ID);
			p.put("RQ", params.get("START_DATE").toString().replaceAll("-", ""));
			Map<String, Object> routeCalendars = this.routeDao.listRouteCalendarDao(p).get(0);
			
			String[] RULEIDGROUPS = routeCalendars.get("RULEIDGROUP").toString().split("@");
			StringBuffer _IDS = new StringBuffer();
			for (int i = 0; i < RULEIDGROUPS.length; i++) {
				if(CommonUtils.checkString(RULEIDGROUPS[i])){
					String[] _RULEIDGROUPS = RULEIDGROUPS[i].split("-");
					if(PLAN_ID.equals(_RULEIDGROUPS[0])){
						String[] RULE_IDS = _RULEIDGROUPS[1].toString().split(",");
						for (int j = 0; j < RULE_IDS.length; j++) {
							if(CommonUtils.checkString(RULE_IDS[j])){
								_IDS.append("'"+RULE_IDS[j]+"',");
							}
						}
						break;
					}
				}
			}
			
			p.clear();
			p.put("ENTITY_ID", _IDS.toString().substring(0, _IDS.toString().length()-1));
			p.put("ORDER_ID", ORDER_ID);
			p.put("ENTITY_TYPE", "1");
			this.orderDao.saveOrderPrice(p);
		}
		
		for (Map<String, Object> price : planPrices) {
			for (Map<String, Object> priceAttr : priceAttrs) {
				p.clear();
				p.put("ID", CommonUtils.uuid());
				p.put("ORDER_ID", ORDER_ID);
				p.put("TYPE_ID", price.get("ID"));
				p.put("TYPE_NAME", price.get("TITLE"));
				p.put("ATTR_ID", priceAttr.get("ID"));
				p.put("ATTR_NAME", priceAttr.get("TITLE"));
				p.put("PRICE", price.get(priceAttr.get("CON_NAME")));
				this.orderDao.saveOrderPriceMain(p);
			}
		}

		/**
		 * 保存出行方案
		 */
		if(CommonUtils.checkString(PLAN_ID)){
			p.clear();
			p.put("ID", PLAN_ID);
			List<Map<String, Object>> routeTraffics = this.routeDao.searchRouteTraffic(p);
			Map<String, Object> routeTraffic = routeTraffics.get(0);
			String _PLAN_ID = CommonUtils.uuid();
			routeTraffic.put("ID", _PLAN_ID);
			routeTraffic.put("ORDER_ID", ORDER_ID);
			routeTraffic.put("BEFORE_ID", PLAN_ID);
			this.orderDao.saveOrderTraffic(routeTraffic);
			/**
			 * 保存出行方案详情
			 */
			p.clear();
			p.put("PLAN_ID", PLAN_ID);
			List<Map<String, Object>> routeTrafficDetails = this.routeDao.searchRouteTrafficDetail(p);
			for (Map<String, Object> routeTrafficDetail : routeTrafficDetails) {
				String BEFORE_DETAIL_ID = "";
				BEFORE_DETAIL_ID = (String)routeTrafficDetail.get("ID");

				routeTrafficDetail.put("ID", CommonUtils.uuid());
				routeTrafficDetail.put("PLAN_ID", _PLAN_ID);
				routeTrafficDetail.put("ORDER_ID", ORDER_ID);
				
				routeTrafficDetail.put("BEFORE_DETAIL_ID", BEFORE_DETAIL_ID);
				routeTrafficDetail.put("BEFORE_PLAN_ID", PLAN_ID);
				
				this.orderDao.saveOrderTrafficDetail(routeTrafficDetail);
			}
		}
		
		params.put("MAN_COUNT", MAN_COUNT);
		params.put("CHILD_COUNT", CHILD_COUNT);
		/**
		 * 单房差
		 * 同行总价
		 * 外卖总价
		 * 
		 */
		
		int RETAIL_SINGLE_ROOM = Integer.parseInt(String.valueOf(params.get("RETAIL_SINGLE_ROOM")));
		int INTER_SINGLE_ROOM = Integer.parseInt(String.valueOf(params.get("INTER_SINGLE_ROOM")));
		int SINGLE_ROOM_CNT = Integer.parseInt(String.valueOf(params.get("SINGLE_ROOM_CNT")));
		
		int SUM_RETAIL_SINGLE_ROOM = RETAIL_SINGLE_ROOM * SINGLE_ROOM_CNT;
		int SUM_INTER_SINGLE_ROOM = INTER_SINGLE_ROOM * SINGLE_ROOM_CNT;
		
		params.put("SALE_AMOUNT", sale_amount+SUM_RETAIL_SINGLE_ROOM);
		params.put("INTER_AMOUNT", inter_amount+SUM_INTER_SINGLE_ROOM);
		
		this.orderDao.saveOrder(params);
		
		
		p.clear();
		p.put("ID", CommonUtils.uuid());
		p.put("ORDER_ID", ORDER_ID);
		p.put("TITLE", "提交订单");//-----------------------------
		p.put("STEP_USER", (String)params.get("CREATE_USER"));
		p.put("STEP_USER_ID", (String)params.get("CREATE_USER_ID"));
		p.put("REMARK", "");//-----------------------------;
		this.orderDao.saveOrderStep(p);
		
		/**
		p.clear();
		p.put("PRO_ID", ROUTE_ID);
		p.put("ORDER_ID", ORDER_ID);
		p.put("DISCOUNT_ID", params.get("DISCOUNT_ID"));
		this.discountService.saveDiscountOrderService(p);
		*/
		
		return result;
	}
	
	public List<Map<String, Object>> searchOrderBase(Map<String, Object> params){
		if(params.get("start") == null){
			params.put("start", 1);
			params.put("end", 100);
		}
		return this.orderDao.searchOrderBase(params);
	}
	
	public int countOrderBase(Map<String, Object> params){
		return this.orderDao.countOrderBase(params);
	}
	
	public void orderLost(Map<String, Object> params)throws Exception{
		this.orderDao.orderLost(params);
	}
	
	public void saveOrderStep(Map<String, Object> params)throws Exception{
		this.orderDao.saveOrderStep(params);
	}
	
	public void reSeatByEntityIdService(Map<String, Object> params)throws Exception{
		this.orderDao.reSeatByEntityIdDao(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public int onlinePayCallBackService(Map<String, Object> params){
		try {
			Map<String, Object> p = new HashMap<String, Object>();
			String NO = (String)params.get("NO");
			p.put("NO", NO);
			p.put("start", 1);
			p.put("end", 1);
			Map<String, Object> order = this.orderDao.searchOrderBase(p).get(0);
			String STATUS = String.valueOf(order.get("STATUS"));
			System.out.println("***********STATUS**************"+STATUS);
			if(STATUS.equals("1")){
				String orderId = (String)order.get("ID");
				String SITE_RELA_ID = (String)order.get("SITE_RELA_ID");
				String type = String.valueOf(order.get("PRODUCE_TYPE"));
				
				p.clear();
				p.put("ID", SITE_RELA_ID);
				p.put("start", 1);
				p.put("end", 1);
				List<Map<String, Object>> siteManagers = this.commonDao.searchSiteManager(p);
				Map<String, Object> siteManager = siteManagers.get(0);
				String SITE_COMPANY_ID = (String)siteManager.get("COMPANY_ID");
				params.put("CREATE_USER", (String)order.get("CREATE_USER"));
				params.put("CREATE_USER_ID", (String)order.get("CREATE_USER_ID"));
				params.put("type", type);
				
				/**
				 * 单房差
				 */
				if(type.equals("2") || type.equals("3")){
					System.out.println("--------saveSingleRoom------start----");
					this.saveSingleRoom(p, params, order, (String)order.get("ID"), SITE_RELA_ID);
					System.out.println("---------saveSingleRoom-----end----");
				}
				
				/**
				 * 记账完成后 更新数据库状态
				 * */
				this.balancePayAfter(p,(String)order.get("ID"));
				/**
				 * 记录状态跟踪
				 * */
				this.orderStep(p,params,(String)order.get("ID"));
				/**
				 * 买家 记账
				 * */
				this.sellStepFunds(p,params,order,(String)order.get("ID"),SITE_RELA_ID);
				/**
				 * 卖家 记账
				 * */
				this.buyStepFundsByRouteOrder(p,params,order,(String)order.get("ID"),SITE_RELA_ID);
				/**
				 * 平台 记账
				 * 同时给买家记录一笔负数
				 * */
				this.siteStepFunds(p,params,order,(String)order.get("ID"),SITE_RELA_ID,SITE_COMPANY_ID);
				
				
				p.clear();
				p.put("ORDER_ID", order.get("ID"));
				List<Map<String, Object>> discountOrders = this.orderDao.listDiscountOrderDao(p);
				if(CommonUtils.checkList(discountOrders) && discountOrders.size() == 1){
					double INTER_AMOUNT = Double.parseDouble(String.valueOf(order.get("INTER_AMOUNT")));
					String PRODUCE_ID = (String)order.get("PRODUCE_ID");
					this.saveDiscountInfo((String)discountOrders.get(0).get("DISCOUNT_ID"), (String)params.get("platfrom"), "1", (String)order.get("ID"), INTER_AMOUNT, PRODUCE_ID, SITE_COMPANY_ID, SITE_RELA_ID, params, order);
				}
				
				p.clear();
				p.put("ID", order.get("ID"));
				p.put("start", 1);
				p.put("end", 1);
				this.orderDao.searchOrderBase(p);
				
				String MOBILE = (String)order.get("SALE_PHONE");
				
				//供应商
				String content = "";
				double c = 50.00;
				double cnt = 0.0;
				int status = -1;
				if(CommonUtils.checkString(MOBILE)){
					content = "尊敬的"+order.get("COMPANY_NAME")+"，"+order.get("BUY_COMPANY")+"于"+DateUtil.getNowDateTimeString()+"提交了订单："+order.get("PRODUCE_NAME")+" "+order.get("START_DATE")+"出发";
					if(type.equals("2")){
						String START_DATE = (String)order.get("START_DATE");
						int DAY_COUNT = Integer.parseInt(String.valueOf(order.get("DAY_COUNT")));
						content += "/"+DateUtil.parseDate(DateUtil.getNDay(DateUtil.parseDate(START_DATE), DAY_COUNT), "yyyy-MM-dd")+"返回";
					}
					content += "，游客为"+order.get("MAN_COUNT")+"大"+order.get("CHILD_COUNT")+"小， 联系人："+order.get("BUY_USER_NAME")+" 电话："+order.get("BUY_PHONE")+"。请提前通知游客出团信息，并做好接待服务";
					
					p.clear();
					p.put("COMPANY_ID", (String)order.get("CREATE_COMPANY_ID"));
					cnt = Math.ceil(Double.parseDouble(String.valueOf(content.length()))/c);
					p.put("useCount", cnt);
					status = this.commonService.expenseSMS(p);
					if(status == 0){
						SMSSender.sendSMS(MOBILE, content, (String)order.get("CREATE_USER"), "", "9", (String)order.get("SITE_RELA_ID"), "2", "1", orderId, (String)order.get("CREATE_COMPANY_ID"), cnt);
					}
				}
				
				//查询订单联系人.循环发送
				p.clear();
				p.put("ORDER_ID", order.get("ID"));
				List<Map<String, Object>> contacts = this.orderDao.listOrderContact(p);
				System.out.println("contacts--------"+contacts.size());
				for (int i = 0; i < contacts.size(); i++) {
					//游客
					content = "您于"+DateUtil.getNowDateTimeString()+"提交了订单："+order.get("PRODUCE_NAME")+" "+order.get("START_DATE")+"出发";
					if(type.equals("2")){
						String START_DATE = (String)order.get("START_DATE");
						int DAY_COUNT = Integer.parseInt(String.valueOf(order.get("DAY_COUNT")));
						content += "/"+DateUtil.parseDate(DateUtil.getNDay(DateUtil.parseDate(START_DATE), DAY_COUNT), "yyyy-MM-dd")+"返回";
					}
					content += "，游客为"+order.get("MAN_COUNT")+"大"+order.get("CHILD_COUNT")+"小";
					
					p.clear();
					p.put("COMPANY_ID", (String)order.get("CREATE_COMPANY_ID"));
					cnt = Math.ceil(Double.parseDouble(String.valueOf(content.length()))/c);
					p.put("useCount", cnt);
					status = this.commonService.expenseSMS(p);
					if(status == 0){
						SMSSender.sendSMS((String)contacts.get(i).get("MOBILE"), content, (String)order.get("CREATE_USER"), "", "9", (String)order.get("SITE_RELA_ID"), "2", "1", orderId, (String)order.get("CREATE_COMPANY_ID"), cnt);
					}
				}
				
				if(CommonUtils.checkString(order.get("BUY_PHONE"))){
					content = "尊敬的"+order.get("BUY_COMPANY")+"，游客"+order.get("VISITOR_CONCAT")+"于"+DateUtil.getNowDateTimeString()+"提交了订单："+order.get("PRODUCE_NAME")+" "+order.get("START_DATE")+"出发";
					if(type.equals("2")){
						String START_DATE = (String)order.get("START_DATE");
						int DAY_COUNT = Integer.parseInt(String.valueOf(order.get("DAY_COUNT")));
						content += "/"+DateUtil.parseDate(DateUtil.getNDay(DateUtil.parseDate(START_DATE), DAY_COUNT), "yyyy-MM-dd")+"返回";
					}
					content += "，游客为"+order.get("MAN_COUNT")+"大"+order.get("CHILD_COUNT")+"小， 联系人："+order.get("VISITOR_CONCAT")+" 电话："+order.get("VISITOR_MOBILE")+"。请提前通知游客出团信息";
					p.clear();
					p.put("COMPANY_ID", (String)order.get("CREATE_COMPANY_ID"));
					cnt = Math.ceil(Double.parseDouble(String.valueOf(content.length()))/c);
					p.put("useCount", cnt);
					status = this.commonService.expenseSMS(p);
					if(status == 0){
						SMSSender.sendSMS((String)order.get("BUY_PHONE"), content, (String)order.get("CREATE_USER"), "", "9", (String)order.get("SITE_RELA_ID"), "2", "1", orderId, (String)order.get("CREATE_COMPANY_ID"), cnt);
					}
				}
				
			}else if(STATUS.equals("6") || STATUS.equals("7")){
				
				/**
				 * 保存掉单信息
				 */
				p.clear();
				p.put("ID", order.get("ID"));
				p.put("IS_LOST", 1);
				p.put("LOST_TIME", "YES");
				this.orderDao.orderLost(p);
				
				p.clear();
				p.put("ID", CommonUtils.uuid());
				p.put("ORDER_ID", order.get("ID"));
				p.put("TITLE", "掉单");
				p.put("STEP_USER", order.get("CREATE_USER"));
				p.put("STEP_USER_ID", order.get("CREATE_USER_ID"));
				p.put("REMARK", "");
				this.orderDao.saveOrderStep(p);
				
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}	
		
	}
	
	public void saveSingleRoom(Map<String, Object> p, Map<String, Object>params, Map<String, Object> order, String ORDER_ID, String SITE_RELA_ID) throws Exception{
//		 15:单房差付款(旅行社,负数) 16:单房差(供应商,正数)
		int INTER_SINGLE_ROOM = Integer.parseInt(String.valueOf(order.get("INTER_SINGLE_ROOM")));
		int SINGLE_ROOM_CNT = Integer.parseInt(String.valueOf(order.get("SINGLE_ROOM_CNT")));
		if(SINGLE_ROOM_CNT != 0){
			p.clear();
			p.put("ORDER_ID", ORDER_ID);
			p.put("ID", CommonUtils.uuid());
			p.put("TYPE", 15);
//			p.put("ENTITY_ID", );
//			p.put("ENTITY_TYPE",  );
			p.put("AMOUNT", -(INTER_SINGLE_ROOM * SINGLE_ROOM_CNT));
			if(SINGLE_ROOM_CNT != 0){
				p.put("REMARKS", "单价:"+INTER_SINGLE_ROOM+",数量:"+SINGLE_ROOM_CNT);	
			}
			p.put("ACCOUNT_TYPE", 1);
			p.put("ACCOUNT_COMPANY_ID", (String)order.get("COMPANY_ID"));
			p.put("SITE_RELA_ID", SITE_RELA_ID);
			p.put("CREATE_USER", (String)params.get("CREATE_USER"));
			p.put("CREATE_USER_ID", (String)params.get("CREATE_USER_ID"));
			this.orderDao.saveOrderFundsDao(p);
			
			p.put("ID", CommonUtils.uuid());
			p.put("TYPE", 16);
			p.put("ACCOUNT_TYPE", 2);
			p.put("AMOUNT", INTER_SINGLE_ROOM * SINGLE_ROOM_CNT);
			if(SINGLE_ROOM_CNT != 0){
				p.put("REMARKS", "单价:"+INTER_SINGLE_ROOM+",数量:"+SINGLE_ROOM_CNT);	
			}
			this.orderDao.saveOrderFundsDao(p);
		}
	}
	
	public double saveDiscountInfo(String DISCOUNT_ID, String PLATFROM, String PAY_WAY, String ORDER_ID, double INTER_AMOUNT, String PRO_ID, String SITE_COMPANY_ID, String SITE_RELA_ID, Map<String, Object> p, Map<String, Object> order) throws Exception{
		/**
		 * 保存日志
		 */
		double TOTAL_AMOUNT = 0;
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("PRO_ID", PRO_ID);
		params.put("DISCOUNT_ID", DISCOUNT_ID);
		
		if("b2b".equals(PLATFROM)){
			params.put("PLATFROM", "1");
		}else{
			params.put("PLATFROM", "0");
		}
		
		params.put("PAY_WAY", PAY_WAY);
		
		List<Map<String, Object>> discountProducts = this.orderDao.listDiscountProductDao(params);
		
		if(CommonUtils.checkList(discountProducts) && discountProducts.size() == 1){
			Map<String, Object> discountProduct = discountProducts.get(0);
			String RULE_TYPE = String.valueOf(discountProduct.get("RULE_TYPE"));
			
			double MONEY = Double.parseDouble(String.valueOf(discountProduct.get("MONEY")));
			
			params.put("ORDER_ID", ORDER_ID);
			params.put("RULE_TYPE", discountProduct.get("RULE_TYPE"));
			params.put("DISCOUNT_RULE_ID", discountProduct.get("DISCOUNT_RULE_ID"));
			params.put("MONEY", MONEY);
			
			
			//1:数值 2:百分比
			if("1".equals(RULE_TYPE)){
				TOTAL_AMOUNT = MONEY;
			}else if("2".equals(RULE_TYPE)){
				BigDecimal b = new BigDecimal(INTER_AMOUNT*(MONEY/100));
				TOTAL_AMOUNT = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
			params.put("TOTAL_AMOUNT", TOTAL_AMOUNT);
					
			this.orderDao.updateDiscountOrderDao(params);
			/**
			 * 记账
			 */
			this.saveDiscountAccount(ORDER_ID, TOTAL_AMOUNT, SITE_COMPANY_ID, SITE_RELA_ID, p, order);
		}else{
			params.clear();
			params.put("ORDER_ID", ORDER_ID);
			this.orderDao.delDiscountOrderDao(params);
		}
		return TOTAL_AMOUNT;
	}
	
	private void saveDiscountAccount(String ORDER_ID, double AMOUNT, String SITE_COMPANY_ID, String SITE_RELA_ID, Map<String, Object> params, Map<String, Object> order) throws Exception{
		Map<String, Object> p = new HashMap<String, Object>();
		p.clear();
		p.put("ID", CommonUtils.uuid());
		p.put("ORDER_ID", ORDER_ID);
		p.put("TYPE", 11);//平台
		p.put("AMOUNT", "-"+AMOUNT);//供应商需要支付给平台的营销费
		p.put("REMARKS", "");
		p.put("ACCOUNT_TYPE", 3);
		
		//站长的公司ID
		p.put("ACCOUNT_COMPANY_ID", SITE_COMPANY_ID);
		p.put("SITE_RELA_ID", SITE_RELA_ID);
//		p.put("ENTITY_TYPE", order.get("PRODUCE_TYPE"));
		p.put("CREATE_USER", (String)params.get("CREATE_USER"));
		p.put("CREATE_USER_ID", (String)params.get("CREATE_USER_ID"));
		this.orderDao.saveOrderFundsDao(p);
		
		
		
		p.clear();
		p.put("ID", CommonUtils.uuid());
		p.put("ORDER_ID", ORDER_ID);
		p.put("TYPE", 12);
		p.put("AMOUNT", AMOUNT);
		p.put("REMARKS", "");
		p.put("ACCOUNT_TYPE", 1);
		p.put("ACCOUNT_COMPANY_ID", (String)order.get("CREATE_COMPANY_ID"));
		p.put("SITE_RELA_ID", SITE_RELA_ID);
		p.put("CREATE_USER", (String)params.get("CREATE_USER"));
		p.put("CREATE_USER_ID", (String)params.get("CREATE_USER_ID"));
		this.orderDao.saveOrderFundsDao(p);
	}
	
	/**
	 * 账户完成后
	 * @throws Exception 
	 * */
	private void balancePayAfter(Map<String, Object> p,String ORDER_ID) throws Exception{
		try{
			p.clear();
			p.put("ID", ORDER_ID);
			p.put("STATUS", "2");
			p.put("PLATFORM_PAY", "2");
			this.orderDao.updateOrderStatus(p);
		}catch(Exception ex){
			throw new Exception("账户完成后 更新状态失败"){
				
			};
		}
	}
	
	private void orderStep(Map<String, Object> p,Map<String, Object> params,String ORDER_ID) throws Exception{
		try{
			p.clear();
			p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", ORDER_ID);
			p.put("TITLE", "订单付款");//-----------------------------
			p.put("STEP_USER", (String)params.get("CREATE_USER"));
			p.put("STEP_USER_ID", (String)params.get("CREATE_USER_ID"));
			p.put("REMARK", "");//-----------------------------;
			this.orderDao.saveOrderStep(p);
		}catch(Exception ex){
			throw new Exception("更新状态完成后 记录状态日志失败"){
				
			};
		}
	}
	
	/**
	 * 买家 记账
	 * */
	private void sellStepFunds(Map<String, Object> p,Map<String, Object> params,Map<String, Object> order,String ORDER_ID,String SITE_RELA_ID) throws Exception{
		try{
			p.clear();
			p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", ORDER_ID);
			p.put("TYPE", 1);
			p.put("AMOUNT", order.get("SALE_AMOUNT"));
			p.put("REMARKS", "");
			p.put("ACCOUNT_TYPE", 1);
			p.put("ACCOUNT_COMPANY_ID", (String)order.get("CREATE_COMPANY_ID"));
			p.put("SITE_RELA_ID", SITE_RELA_ID);
			p.put("CREATE_USER", (String)params.get("CREATE_USER"));
			p.put("CREATE_USER_ID", (String)params.get("CREATE_USER_ID"));
			
			this.orderDao.saveOrderFundsDao(p);

			double INTER_SINGLE_ROOM = Double.parseDouble(String.valueOf(order.get("INTER_SINGLE_ROOM")));
			double SINGLE_ROOM_CNT = Double.parseDouble(String.valueOf(order.get("SINGLE_ROOM_CNT")));
			System.out.println("INTER_SINGLE_ROOM--"+INTER_SINGLE_ROOM+"--SINGLE_ROOM_CNT--"+SINGLE_ROOM_CNT+"***********"+"-"+String.valueOf((Double.parseDouble(String.valueOf(order.get("INTER_AMOUNT"))) - (INTER_SINGLE_ROOM * SINGLE_ROOM_CNT))));
			p.clear();
			p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", ORDER_ID);
			p.put("TYPE", 10);
			p.put("AMOUNT", "-"+String.valueOf((Double.parseDouble(String.valueOf(order.get("INTER_AMOUNT"))) - (INTER_SINGLE_ROOM * SINGLE_ROOM_CNT))));
			p.put("REMARKS", "");
			p.put("ACCOUNT_TYPE", 1);
			p.put("ACCOUNT_COMPANY_ID", (String)order.get("CREATE_COMPANY_ID"));
			p.put("SITE_RELA_ID", SITE_RELA_ID);
			p.put("CREATE_USER", (String)params.get("CREATE_USER"));
			p.put("CREATE_USER_ID", (String)params.get("CREATE_USER_ID"));
			this.orderDao.saveOrderFundsDao(p);
		}catch(Exception ex){
			ex.printStackTrace();
			throw new Exception("买家记账失败"){
				
			};
		}
	}
	
	private void buyStepFundsByRouteOrder(Map<String, Object> p,Map<String, Object> params,Map<String, Object> order,String ORDER_ID,String SITE_RELA_ID) throws Exception{
		try{
			p.clear();
			
			Map<String, String> visitor_num = new HashMap<String, String>();
			/**
			 * 不同类型的游客 数量
			 */
			p.put("ORDER_ID", ORDER_ID);
			List<Map<String, Object>> visitors = this.visitorDao.searchVisitorGroupByType(p);
			for (Map<String, Object> visitor : visitors) {
				String ATTR_ID = (String)visitor.get("ATTR_ID");
				String num = String.valueOf(visitor.get("NUM"));
				visitor_num.put(ATTR_ID, num);
			}
			
			/**
			 * 地接价格
			 */
			p.put("ENTITY_ID", (String)order.get("PRODUCE_ID"));
			p.put("ENTITY_TYPE",  "2");
			p.put("TYPE_ID", "0FA5123749D38C87E050007F0100BCAD");
			List<Map<String, Object>> orderPrices = this.orderDao.listOrderPriceDao(p);
			double sum_price = 0.0;
			for (Map<String, Object> orderPrice : orderPrices) {
				double price = Double.parseDouble(String.valueOf(orderPrice.get("PRICE")));
				String ATTR_ID = (String)orderPrice.get("ATTR_ID");
				String num = visitor_num.get(ATTR_ID);
				if(CommonUtils.checkString(num)){
					sum_price = sum_price+(price * Integer.parseInt(num));
				}
			}
			
			p.put("ID", CommonUtils.uuid());
			p.put("TYPE", 2);
			p.put("AMOUNT", sum_price);
			p.put("REMARKS", "");
			p.put("ACCOUNT_TYPE", 2);
			p.put("ACCOUNT_COMPANY_ID", (String)order.get("COMPANY_ID"));
			p.put("SITE_RELA_ID", SITE_RELA_ID);
			p.put("ENTITY_TYPE", "2");
			p.put("CREATE_USER", (String)params.get("CREATE_USER"));
			p.put("CREATE_USER_ID", (String)params.get("CREATE_USER_ID"));
			this.orderDao.saveOrderFundsDao(p);
			
			/**
			 * 
			 * */
			if(CommonUtils.checkString(params.get("type")) && params.get("type").toString().equals("2")){
				p.clear();
				p.put("ORDER_ID", ORDER_ID);
				List<Map<String, Object>> list = this.orderDao.listVisitorGroupByTrafficAttrDao(p);
				for (Map<String, Object> orderPrice : list) {
					p.clear();
					p.put("ORDER_ID", ORDER_ID);
					p.put("ID", CommonUtils.uuid());
					p.put("TYPE", 2);
					p.put("ENTITY_ID", (String)orderPrice.get("ENTITY_ID"));
					p.put("ENTITY_TYPE",  "1");
					p.put("AMOUNT", orderPrice.get("PRICE"));
					p.put("REMARKS", "");
					p.put("ACCOUNT_TYPE", 2);
					p.put("ACCOUNT_COMPANY_ID", (String)order.get("COMPANY_ID"));
					p.put("SITE_RELA_ID", SITE_RELA_ID);
					p.put("CREATE_USER", (String)params.get("CREATE_USER"));
					p.put("CREATE_USER_ID", (String)params.get("CREATE_USER_ID"));
					this.orderDao.saveOrderFundsDao(p);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
			throw new Exception("买家记账失败"){
				
			};
		}
	}
	
	/**
	 * 平台 记账
	 * 同时给买家记录一笔负数
	 * */
	private void siteStepFunds(Map<String, Object> p,Map<String, Object> params,Map<String, Object> order,String ORDER_ID,String SITE_RELA_ID,String SITE_COMPANY_ID) throws Exception{
		try{
			//计算营销费
			p.clear();
			p.put("ID", (String)order.get("COMPANY_ID"));
			System.out.println("***********COMPANY_ID**********"+(String)order.get("COMPANY_ID")+"********************8");
			p.put("start", 1);
			p.put("end", 1);
			Map<String, Object> company = this.userDao.searchCompanyBuyID(p).get(0);
			System.out.println("***********company**********"+company+"********************8");
			/**
			 * 营销费 = 订单总价 * 营销费百分比
			 */
			double EXPENSE = 0;
			String PRODUCE_TYPE = String.valueOf(order.get("PRODUCE_TYPE"));
			
			System.out.println("***********company**********"+company+"********************8");
			
			if(PRODUCE_TYPE.equals("1")){
				Object TRAFFIC_EXPENSE = company.get("TRAFFIC_EXPENSE");
				if(TRAFFIC_EXPENSE != null && !"".equals(TRAFFIC_EXPENSE)){
					EXPENSE = Double.parseDouble(String.valueOf(TRAFFIC_EXPENSE))/100;
				}else{
					EXPENSE = 0;
				}
				
			}else if(PRODUCE_TYPE.equals("2") || PRODUCE_TYPE.equals("3")){
				
				Object ROUTE_EXPENSE = company.get("ROUTE_EXPENSE");
				if(ROUTE_EXPENSE != null && !"".equals(ROUTE_EXPENSE)){
					EXPENSE = Double.parseDouble(String.valueOf(ROUTE_EXPENSE))/100;
				}else{
					EXPENSE = 0;
				}
			}
			double INTER_AMOUNT = Double.parseDouble(String.valueOf(order.get("INTER_AMOUNT")));
			
			
			p.clear();
			p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", ORDER_ID);
			p.put("TYPE", 7);//平台
			p.put("AMOUNT", EXPENSE * INTER_AMOUNT);//供应商需要支付给平台的营销费
			p.put("REMARKS", "");
			p.put("ACCOUNT_TYPE", 3);
			System.out.println("************SITE_COMPANY_ID*************"+SITE_COMPANY_ID+"*******SITE_RELA_ID*********"+SITE_RELA_ID+"*****************");
			//站长的公司ID
			p.put("ACCOUNT_COMPANY_ID", SITE_COMPANY_ID);
			p.put("SITE_RELA_ID", SITE_RELA_ID);
//			p.put("ENTITY_TYPE", order.get("PRODUCE_TYPE"));
			p.put("CREATE_USER", (String)params.get("CREATE_USER"));
			p.put("CREATE_USER_ID", (String)params.get("CREATE_USER_ID"));
			this.orderDao.saveOrderFundsDao(p);
			
			p.clear();
			p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", ORDER_ID);
			p.put("TYPE", 9);//供应商
			p.put("AMOUNT", (0-(EXPENSE * INTER_AMOUNT)));//供应商需要支付给平台的营销费
			p.put("REMARKS", "");
			p.put("ACCOUNT_TYPE", 2);
			p.put("ACCOUNT_COMPANY_ID", (String)order.get("COMPANY_ID"));
			p.put("SITE_RELA_ID", SITE_RELA_ID);
//			p.put("ENTITY_TYPE", order.get("PRODUCE_TYPE"));
			p.put("CREATE_USER", (String)params.get("CREATE_USER"));
			p.put("CREATE_USER_ID", (String)params.get("CREATE_USER_ID"));
			this.orderDao.saveOrderFundsDao(p);
		}catch(Exception ex){
			throw new Exception("营销费记账失败"){
				
			};
		}
	}
	
	public void updateOrderStatus(Map<String, Object> params){
		this.orderDao.updateOrderStatus(params);
	}
	
	public List<Map<String, Object>> searchOrderRouteTraffic(Map<String, Object> params){
		return this.orderDao.searchOrderRouteTraffic(params);
	}
}
