package cn.sd.service.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.DateUtil;
import cn.sd.core.util.sms.SMSSender;
import cn.sd.dao.b2b.IUserDao;
import cn.sd.dao.finance.IFinanceDao;
import cn.sd.dao.order.IOrderDao;
import cn.sd.dao.order.IVisitorOrderDao;
import cn.sd.dao.resource.ITrafficDao;
import cn.sd.dao.site.ICompanyDao;
import cn.sd.entity.RouteEntity;
import cn.sd.rmi.ServiceProxyFactory;
import cn.sd.service.b2b.IDiscountService;
import cn.sd.service.b2b.ISMSService;
import cn.sd.service.finance.IFinanceService;
import cn.sd.service.resource.IRouteService;

@Repository
@Service("trafficOrderService")

public class OrderService implements IOrderService {

	@Resource
	private IOrderDao orderDao;
	@Resource
	private IVisitorOrderDao visitorOrderDao;
	@Resource
	private ITrafficDao trafficDao;
	@Resource
	private ICompanyDao companyDao;
	@Resource
	private IUserDao userDao;
	@Resource
	private IRouteService routeService;
	
	@Resource
	private IFinanceDao financeDao;
	@Resource
	private IFinanceService financeService;
	@Resource
	private IDiscountService discountService;
	
	@Resource
	private ISMSService smsService;
	
	public void delOrderService(Map<String, Object> params){
		this.orderDao.delOrderDao(params);
	}
	
	public void autoRefunEarnest()throws Exception{
		
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void saveOrderContactService(Map<String, Object> params){
		this.orderDao.delOrderContactDao(params);
		
		String visitors = (String)params.get("models");
	    JSONArray jarray = JSONArray.fromObject(visitors);
		Object[] objArray = jarray.toArray();
		
		String[] keyNames = {"ORDER_ID", "CHINA_NAME", "MOBILE"};
		List<Map<String, Object>> datas = this.jsonArrayToList(objArray, keyNames);
		for (Map<String, Object> data : datas) {
			data.put("ID", CommonUtils.uuid());
			data.put("ORDER_ID", params.get("ORDER_ID"));
			data.put("COMPANY_ID", params.get("COMPANY_ID"));
			data.put("CREATE_USER_ID", params.get("CREATE_USER_ID"));
			this.orderDao.saveOrderContactDao(data);
		}
	}
	public List<Map<String, Object>> listOrderContactService(Map<String, Object> params){
		return this.orderDao.listOrderContactDao(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void reSeatEarnestService() throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		this.orderDao.reSeatEarnestDao(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void batchResetEarnestTypeService(Map<String, Object> params)throws Exception{
		this.orderDao.batchSaveEarnestTypeStepDao(params);
		this.orderDao.batchResetEarnestTypeDao(params);
	}
	
	public void confirmInsteadPayService(Map<String, Object> params)throws Exception{
		
		Map<String, Object> order = this.orderDao.detailOrderDao(params);
		params.put("ID", order.get("ID"));
		params.put("OLD_INTER_AMOUNT", order.get("INTER_AMOUNT"));
		params.put("OLD_RETAIL_AMOUNT", order.get("SALE_AMOUNT"));
		params.put("INTER_AMOUNT", order.get("EARNEST_INTER_AMOUNT"));
		params.put("SALE_AMOUNT", order.get("EARNEST_RETAIL_AMOUNT"));
		params.put("STATUS", 2); //--------------------------------------支付完成
		params.put("IS_EARNEST", 2); //--------------------------------------确认支付
		this.orderDao.confirmInsteadPayDao(params);
		
		Map<String, Object> p = new HashMap<String, Object>();
		p.clear();
		p.put("ID", CommonUtils.uuid());
		p.put("ORDER_ID", order.get("ID"));
		p.put("TITLE", "余款确认");
		p.put("STEP_USER", (String)params.get("CREATE_USER"));
		p.put("STEP_USER_ID", (String)params.get("CREATE_USER_ID"));
		p.put("REMARK", params.get("CONTENT"));
		this.orderDao.saveOrderStepDao(p);
		
	}
	
	public void updateOrderEarnestInfoService(Map<String, Object> params)throws Exception{
		this.orderDao.updateOrderEarnestInfoDao(params);
	}
	public void updateOrderEarnestStatusService(Map<String, Object> params)throws Exception{
		this.orderDao.updateOrderEarnestStatusDao(params);
	}
	
	public void updateOrderBaseRemarkService(Map<String, Object> params)throws Exception{
		this.orderDao.updateOrderBaseRemarkDao(params);
	}
	
	public List<Map<String, Object>> searchOrderPriceService(Map<String, Object> params){
		return this.orderDao.searchOrderPriceDao(params);
	}
	public List<Map<String, Object>> searchOrderPriceMainService(Map<String, Object> params){
		return this.orderDao.searchOrderPriceMainDao(params);
	}
	
	public void updateStartConfirmByIdService(Map<String, Object> params)throws Exception{
		this.orderDao.updateStartConfirmByIdDao(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void cancelRenewOrderService()throws Exception{
		this.orderDao.saveCancelRenewOrderStepDao();
		this.orderDao.batchRenewRestContractAgencyDao();
		this.orderDao.cancelRenewOrderDao();
	}
	
	public void updateRenewStatusService(Map<String, Object> params)throws Exception{
		this.orderDao.updateRenewStatusDao(params);
	}
	
	public void morePayCheckService(Map<String, Object> params)throws Exception{
		this.orderDao.morePayCheckDao(params);
	}
	
	public void updateOrderInterFloatTempService(Map<String, Object> params)throws Exception{
		
		this.orderDao.updateOrderInterFloatTempDao(params);
		/**
		 * 保存议价日志
		 */
		Map<String, Object> p = new HashMap<String, Object>();
		p.clear();
		p.put("ID", CommonUtils.uuid());
		p.put("ORDER_ID", params.get("ID"));
		p.put("TITLE", "同行议价申请");
		p.put("STEP_USER", (String)params.get("CREATE_USER"));
		p.put("STEP_USER_ID", (String)params.get("CREATE_USER_ID"));
		p.put("REMARK", params.get("CONTENT"));
		this.orderDao.saveOrderStepDao(p);
	}
	
	public void updateOrderInterFloatService(Map<String, Object> params)throws Exception{
		
		this.orderDao.updateOrderInterAuditDao(params);

		if(params.get("auditStatus").equals("2")){
			this.orderDao.updateOrderInterFloatDao(params);
		}
		
		/**
		 * 保存议价日志
		 */
		Map<String, Object> p = new HashMap<String, Object>();
		p.clear();
		p.put("ID", CommonUtils.uuid());
		p.put("ORDER_ID", params.get("ID"));
		p.put("TITLE", "同行议价审核");
		p.put("STEP_USER", (String)params.get("CREATE_USER"));
		p.put("STEP_USER_ID", (String)params.get("CREATE_USER_ID"));
		p.put("REMARK", params.get("CONTENT"));
		this.orderDao.saveOrderStepDao(p);
	}
	
	public void updateOrderSaleFloatService(Map<String, Object> params)throws Exception{
		/**
		 * 修改订单外卖总金额
		 * 修改订单外卖议价金额
		 */
		this.orderDao.updateOrderSaleFloatDao(params);
		/**
		 * 保存议价日志
		 */
		Map<String, Object> p = new HashMap<String, Object>();
		p.clear();
		p.put("ID", CommonUtils.uuid());
		p.put("ORDER_ID", params.get("ID"));
		p.put("TITLE", "外卖议价");
		p.put("STEP_USER", (String)params.get("CREATE_USER"));
		p.put("STEP_USER_ID", (String)params.get("CREATE_USER_ID"));
		p.put("REMARK", params.get("CONTENT"));
		this.orderDao.saveOrderStepDao(p);
	}
	
	public List<Map<String, Object>> listOrderOtherPriceService(Map<String, Object> params){
		return this.orderDao.listOrderOtherPriceDao(params);
	}
	
	public void updateOrderOtherAuditService(Map<String, Object> params)throws Exception{
		this.orderDao.updateOrderOtherAuditDao(params);
	}
	
	public void saveOrderOtherPriceService(Map<String, Object> params)throws Exception{
		this.orderDao.saveOrderOtherPriceDao(params);
	}
	
	public void delOrderOtherPriceService(Map<String, Object> params)throws Exception{
		this.orderDao.delOrderOtherPriceDao(params);
	}
	
	public Map<String, Object> detailOrderService(Map<String, Object> params){
		return this.orderDao.detailOrderDao(params);
	}
	
	public Map<String, Object> detailContractAgencyByNoService(Map<String, Object> params){
		return this.orderDao.detailContractAgencyByNoDao(params);
	}
	
	public Map<String, Object> saveContractService(Map<String, Object> params)throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", true);
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("ID", params.get("ORDER_ID"));
		p.put("start", 1);
		p.put("end", 10);
		List<Map<String, Object>> data = this.orderDao.listOrderDao(p);
		if(!CommonUtils.checkList(data) || data.size() != 1){
			result.put("code", "-1");//订单不存在
			result.put("success", false);
			return result;
		}
		Map<String, Object> d = data.get(0);
		String _con_no = (String)d.get("CON_NO");
		
		/**
		 * 查询订单信息
		 * 		没有合同
		 * 			直接保存合同号,记录日志
		 * 			使用合同	记录日志
		 * 
		 * 		已有合同 
		 * 			合同是否已核销,已核销的合同不能重置
		 * 		
		 * 			是否废除
		 * 				废除,废除合同	记录日志
		 * 				不废除,	重置合同	记录日志
		 * 			使用合同	记录日志
		 * 			修改订单的合同号	记录日志
		 * 废除=1
		 */
		if(CommonUtils.checkString(_con_no)){
			String CONTRACT_STATUS = String.valueOf(d.get("CONTRACT_STATUS"));
			if(CONTRACT_STATUS.equals("3")){
				result.put("code", "-2");//已核销不能重置合同
				result.put("success", false);
				return result;
			}else{
				String is_cancel = (String)params.get("IS_CANCEL");
				if(CommonUtils.checkString(is_cancel) && is_cancel.equals("1")){
					
					p.clear();
					p.put("NO", _con_no);
					p.put("CANCEL_COMPANY_ID", params.get("CREATE_COMPANY_ID"));
					p.put("CANCEL_USER", params.get("CREATE_USER"));
					p.put("CANCEL_USER_ID", params.get("CREATE_USER_ID"));
					this.companyDao.cancelContractAgencyDao(p);
					
					p.clear();
					p.put("ORDER_ID", params.get("ORDER_ID"));
					p.put("CON_NO", _con_no);
					p.put("CREATE_USER", params.get("CREATE_USER"));
					p.put("CREATE_USER_ID", params.get("CREATE_USER_ID"));
					p.put("TYPE", "6");
					this.companyDao.saveOrderContractLogDao(p);
					
				}else{
					
					p.clear();
					p.put("NO", _con_no);
					this.companyDao.resetContractAgencyDao(p);
					
					p.clear();
					p.put("ORDER_ID", params.get("ORDER_ID"));
					p.put("CON_NO", _con_no);
					p.put("CREATE_USER", params.get("CREATE_USER"));
					p.put("CREATE_USER_ID", params.get("CREATE_USER_ID"));
					p.put("TYPE", "5");
					this.companyDao.saveOrderContractLogDao(p);
					
				}
				
				p.clear();
				p.put("NO", params.get("CON_NO"));
				p.put("STATUS", 1);
				p.put("ORDER_NO", d.get("NO"));
				p.put("ORDER_ID", params.get("ORDER_ID"));
				p.put("USE_COMPANY_ID", params.get("CREATE_COMPANY_ID"));
				p.put("USE_USER", params.get("CREATE_USER"));
				p.put("USE_USER_ID", params.get("CREATE_USER_ID"));
				this.companyDao.useContractAgencyDao(p);
				
				p.clear();
				p.put("ORDER_ID", params.get("ORDER_ID"));
				p.put("CON_NO", params.get("CON_NO"));
				p.put("CREATE_USER", params.get("CREATE_USER"));
				p.put("CREATE_USER_ID", params.get("CREATE_USER_ID"));
				p.put("TYPE", "3");
				this.companyDao.saveOrderContractLogDao(p);
				
				p.clear();
				p.put("ID", params.get("ORDER_ID"));
				p.put("CON_NO", params.get("CON_NO"));
				this.orderDao.updateOrderConNoDao(p);
			}
		}else{
			
			p.clear();
			p.put("NO", params.get("CON_NO"));
			p.put("STATUS", 1);
			p.put("ORDER_NO", d.get("NO"));
			p.put("ORDER_ID", params.get("ORDER_ID"));
			p.put("USE_COMPANY_ID", params.get("CREATE_COMPANY_ID"));
			p.put("USE_USER", params.get("CREATE_USER"));
			p.put("USE_USER_ID", params.get("CREATE_USER_ID"));
			this.companyDao.useContractAgencyDao(p);

			p.clear();
			p.put("ORDER_ID", params.get("ORDER_ID"));
			p.put("CON_NO", params.get("CON_NO"));
			p.put("CREATE_USER", params.get("CREATE_USER"));
			p.put("CREATE_USER_ID", params.get("CREATE_USER_ID"));
			p.put("TYPE", "3");
			this.companyDao.saveOrderContractLogDao(p);
			
			p.clear();
			p.put("ID", params.get("ORDER_ID"));
			p.put("CON_NO", params.get("CON_NO"));
			this.orderDao.updateOrderConNoDao(p);
		}
		
		
		return result;
	}
	
	public Map<String, Object> validator(Map<String, Object> params)throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		/**
		 * 订单编号
		 */
		String COMPANY_ID = (String)params.get("COMPANY_ID");
		String NO = "-1";
		if(params.get("PRODUCE_TYPE").toString().equals("1")){
			NO = ServiceProxyFactory.getProxy().getTrafficOrderNoDao(COMPANY_ID);
		}else if(params.get("PRODUCE_TYPE").toString().equals("2") || params.get("PRODUCE_TYPE").toString().equals("3")){
			NO = ServiceProxyFactory.getProxy().getRouteOrderNoDao(COMPANY_ID);
		}
		
		if(NO.equals("-1")){
			result.put("success", false);
			result.put("statusCode", "-1");
			return result;
		}
		
		/**
		 * 游客信息
		 */
		boolean hasContact = false;
		List<Map<String, Object>> datas = (List<Map<String, Object>>)params.get("datas");
		for (Map<String, Object> data : datas) {
	    	String NAME = (String)data.get("NAME"), ATTR_NAME = (String)data.get("ATTR_NAME"), CONTACT = (String)data.get("CONTACT");
			if(!CommonUtils.checkString(NAME) || !CommonUtils.checkString(ATTR_NAME)){
				result.put("success", false);
				result.put("statusCode", "-2");
				return result;
			}
			if(CommonUtils.checkString(CONTACT) && CONTACT.equals("true")){
				hasContact = true;
			}
		}
		
		if(!hasContact){
			result.put("success", false);
			result.put("statusCode", "-3");
			return result;
		}
		result.put("success", true);
		result.put("statusCode", "1");
		result.put("NO", NO);
		return result;
	}
	
	private void saveOrderPrice(List<Map<String, Object>> basePrices, String ORDER_ID, String IS_MAIN, String ENTITY_TYPE, String ENTITY_ID, Map<String, String> VISITOR_NUM, String PLAN_ID)throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		for (Map<String, Object> basePrice : basePrices) {
			params.clear();
			params.put("ID", CommonUtils.uuid());
			params.put("ORDER_ID", ORDER_ID);
			params.put("TYPE_ID", basePrice.get("TYPE_ID"));
			params.put("TYPE_NAME", basePrice.get("TYPE_NAME"));
			params.put("ATTR_ID", basePrice.get("ATTR_ID"));
			String ATTR_NAME = (String)basePrice.get("ATTR_NAME");
			params.put("ATTR_NAME", ATTR_NAME);
			String PRICE = String.valueOf(basePrice.get("PRICE"));
			params.put("PRICE", PRICE);
			params.put("ENTITY_ID", basePrice.get("ENTITY_ID"));
			params.put("ENTITY_TYPE", ENTITY_TYPE);
			
			//单价
			this.orderDao.saveOrderPriceDao(params);
		}
	}
	@Transactional(rollbackFor={Exception.class})
	public Map<String, Object> saveRouteOrderRenewService(Map<String, Object> params)throws Exception{
		JSONObject detail = JSONObject.fromObject((String)params.get("detail"));
		Map<String, Object> p = new HashMap<String, Object>();
		params.put("COMPANY_ID", detail.get("COMPANY_ID"));
		params.put("ROUTE_ID", detail.get("ID"));
		
		String visitors = (String)params.get("visitors");
	    JSONArray jarray = JSONArray.fromObject(visitors);
		Object[] objArray = jarray.toArray();
		
		String[] keyNames = {"NAME", "ATTR_NAME", "SEX", "MOBILE", "CARD_TYPE", "CARD_NO", "CONTACT"};
		Map<String, String>  VISITOR_NUM = new HashMap<String, String>();
	    List<Map<String, Object>> datas = this.jsonArrayToList(objArray, keyNames);
	    params.put("datas", datas);
	    
		Map<String, Object> result = this.validator(params);
		boolean success = (Boolean)result.get("success");
		if(!success){
			return result;
		}
		
		String ORDER_ID = (String)params.get("ID");
		int MAN_COUNT = Integer.parseInt((String)params.get("MAN_COUNT")), CHILD_COUNT = Integer.parseInt((String)params.get("CHILD_COUNT"));
		
		params.put("NO", result.get("NO"));
		
		String RQ = params.get("RQ").toString().replaceAll("-", "");
		String _RQ = DateUtil.parseDate(DateUtil.subDate(RQ));
		
		params.put("PRODUCE_ID", (String)detail.get("ID"));
//		params.put("TRAFFIC_ID", (String)params.get("PLAN_ID"));
		if(!detail.get("PORDUCE_CONCAT").equals("null")){
			params.put("PORDUCE_CONCAT", detail.get("PORDUCE_CONCAT"));
		}else{
			params.put("PORDUCE_CONCAT", "");
		}
		
		params.put("PRODUCE_USER_ID", (String)detail.get("CREATE_USER_ID"));
		if(CommonUtils.checkString(detail.get("PRODUCE_MOBILE"))){
			params.put("PRODUCE_MOBILE", String.valueOf(detail.get("PRODUCE_MOBILE")));
		}
		params.put("START_DATE", _RQ);
		
		params.put("COMPANY_ID", (String)detail.get("COMPANY_ID"));
		params.put("COMPANY_NAME", (String)detail.get("COMPANY_NAME"));
		
		params.put("STATUS", 0);
		params.put("RENEW_STATUS", 0);
		params.put("PAY_TYPE", 0);
		params.put("ACCOUNT_STATUS", 0);
		
		p.clear();
		int n = 0;
		List<Map<String, Object>> attr = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> priceAttrs = this.trafficDao.listPriceAttrDao(p);
		for (int i = 0; i < priceAttrs.size(); i++) {
			String ID = (String)priceAttrs.get(i).get("ID");
			if(ID.equals("0FA5123749D08C87E050007F0100BCAD")){
				p.put("PID", "0FA5123749D08C87E050007F0100BCAD");
				List<Map<String, Object>> childs = this.trafficDao.listPriceAttrDao(p);
				for (Map<String, Object> map : childs) {
					attr.add(n, map);
					n++;
				}
			}
			attr.add(n, priceAttrs.get(i));
			n++;
		}
		
	    
	    /**
		 * 保存线路基础价格
		 * 保存线路交通基础价格
		 * 保存线路交通汇总价格
		 * 补单的订单没有交通,不保存交通的基础价格
		 * 
		 * 0FA5123749D28C87E050007F0100BCAD	外卖
		 * 0FA5123749D38C87E050007F0100BCAD	同行
		 */
	    
	    JSONArray PRICES = JSONArray.fromObject(params.get("PRICES"));
	    String ROUTE_ID = (String)detail.get("ID");
	    int statusCode = this.routeService.validation(params);
	    
		if(statusCode == 0){
			//保存价格库
			for (Object PRICE : PRICES) {
				JSONObject _PRICE = JSONObject.fromObject((String)PRICE);
				String TYPE_ID = (String)_PRICE.get("TYPE_ID");
				p.put("ID", CommonUtils.uuid());
				p.put("ORDER_ID", ORDER_ID);
				p.put("ENTITY_ID", ROUTE_ID);
				p.put("ENTITY_TYPE", "2");
				p.put("TYPE_ID", TYPE_ID);
				p.put("TYPE_NAME", _PRICE.get("TYPE_NAME"));
				p.put("ATTR_ID", (String)_PRICE.get("ATTR_ID"));
				p.put("ATTR_NAME", _PRICE.get("ATTR_NAME"));
				if(CommonUtils.checkString(_PRICE.get("PRICE"))){
					p.put("PRICE", _PRICE.get("PRICE"));
				}else{
					p.put("PRICE", 0);
				}
				this.orderDao.saveOrderRenewPriceDao(p);
				p.put("ID", CommonUtils.uuid());
				this.orderDao.saveOrderPriceMainDao(p);
				
			}
		}else{
			result.put("success", false);
			result.put("statusCode", statusCode);
			return result;
		}
		
		/**
		 * 保存游客
		 * 不同类型的游客数量
		 * 成人
		 * 婴儿
		 * 不占床位/不含门票
		 * 不占床位/含门票
		 * 占床位/不含门票
		 * 占床位/含门票
		 * 
		 * 根据游客类型计算 外卖和同行总价
		 */
	    
		double SALE_AMOUNT = 0.0;
		double INTER_AMOUNT = 0.0;
		
	    for (Map<String, Object> data : datas) {
			
			String NAME = (String)data.get("NAME"), ATTR_NAME = (String)data.get("ATTR_NAME"), 
			SEX = (String)data.get("SEX"), MOBILE = (String)data.get("MOBILE"), 
			CARD_TYPE = (String)data.get("CARD_TYPE"), CARD_NO = (String)data.get("CARD_NO"), CONTACT = (String)data.get("CONTACT");
			
			p.clear();
			String VISITOR_ID = CommonUtils.uuid();
			p.put("ID", VISITOR_ID);
			p.put("NAME", NAME);
			p.put("ATTR_NAME", ATTR_NAME);
			for (Map<String, Object> priceAttr : attr) {
				String TITLE = (String)priceAttr.get("TITLE");
				if(ATTR_NAME.equals(TITLE)){
					p.put("ATTR_ID", (String)priceAttr.get("ID"));
					break;
				}
			}
			String num = VISITOR_NUM.get(ATTR_NAME);
			if(CommonUtils.checkString(num)){
				int _num = Integer.parseInt(num)+1;
				VISITOR_NUM.put(ATTR_NAME, String.valueOf(_num));
			}else{
				VISITOR_NUM.put(ATTR_NAME, "1");
			}
			
			if("true".equals(CONTACT)){
				params.put("VISITOR_CONCAT", NAME);
				params.put("VISITOR_MOBILE", MOBILE);
			}
			
			p.put("SEX", SEX);
			p.put("MOBILE", MOBILE);
			p.put("CARD_TYPE", CARD_TYPE);
			p.put("CARD_NO", CARD_NO);
			this.visitorOrderDao.saveVisitorDao(p);
			
			String ATTR_ID = (String)p.get("ATTR_ID");
			for (Object PRICE : PRICES) {
				JSONObject _PRICE = JSONObject.fromObject((String)PRICE);
				String _ATTR_NAME = (String)_PRICE.get("ATTR_NAME");
				String _ATTR_ID = (String)_PRICE.get("ATTR_ID");
				String TYPE_ID = (String)_PRICE.get("TYPE_ID");
				double price = 0.0;
				if(CommonUtils.checkString(_PRICE.get("PRICE"))){
					price =  Double.parseDouble(String.valueOf(_PRICE.get("PRICE")));
				}
//				0FA5123749D28C87E050007F0100BCAD	外卖
//				0FA5123749D38C87E050007F0100BCAD	同行
				if(ATTR_ID.equals(_ATTR_ID) && TYPE_ID.equals("0FA5123749D28C87E050007F0100BCAD")){
					SALE_AMOUNT += price;
				}else if(ATTR_ID.equals(_ATTR_ID) && TYPE_ID.equals("0FA5123749D38C87E050007F0100BCAD")){
					INTER_AMOUNT += price;
				}
				
			}
			p.clear();
			p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", ORDER_ID);
			p.put("VISITOR_ID", VISITOR_ID);
			this.visitorOrderDao.saveOrderVisitorDao(p);
			
		}

	    /**
	     * 单房差价格
	     */
	    double SINGLE_ROOM_CNT = Double.parseDouble(String.valueOf(params.get("SINGLE_ROOM_CNT")));
	    
	    if(SINGLE_ROOM_CNT > 0){
	    	double RETAIL_SINGLE_ROOM = Double.parseDouble(String.valueOf(params.get("RETAIL_SINGLE_ROOM")));
	    	SALE_AMOUNT += (SINGLE_ROOM_CNT * RETAIL_SINGLE_ROOM);
	    	
	    	double INTER_SINGLE_ROOM = Double.parseDouble(String.valueOf(params.get("INTER_SINGLE_ROOM")));
	    	INTER_AMOUNT += (SINGLE_ROOM_CNT * INTER_SINGLE_ROOM);
	    	
	    }
	    
	    params.put("SALE_AMOUNT", SALE_AMOUNT);
	    params.put("INTER_AMOUNT", INTER_AMOUNT);
		/**
		 * 保存合同日志
		 * 修改合同表订单编号和合同使用状态
		 */
		p.clear();
		p.put("ORDER_ID", ORDER_ID);
		p.put("CON_NO", params.get("CON_NO"));
		p.put("CREATE_USER", params.get("CREATE_USER"));
		p.put("CREATE_USER_ID", params.get("CREATE_USER_ID"));
		p.put("TYPE", "3");
		this.companyDao.saveOrderContractLogDao(p);
		
		p.clear();
		p.put("NO", params.get("CON_NO"));
		p.put("STATUS", 1);
		p.put("ORDER_NO", params.get("NO"));
		p.put("ORDER_ID", ORDER_ID);
		p.put("USE_COMPANY_ID", params.get("CREATE_COMPANY_ID"));
		p.put("USE_USER", params.get("CREATE_USER"));
		p.put("USE_USER_ID", params.get("CREATE_USER_ID"));
		this.companyDao.useContractAgencyDao(p);
		
//		params.put("RULE_ID", RULE_ID);//------------------------------------------
		params.put("MAN_COUNT", MAN_COUNT);
		params.put("CHILD_COUNT", CHILD_COUNT);
		params.put("START_CITY", detail.get("BEGIN_CITY"));
		params.put("END_CITY", detail.get("END_CITY"));
		
		/**
		 * 其他价格
		 */
		String[] OTHER_PRICES = (String[])params.get("OTHER_PRICE");
		String[] OTHER_NUMS = (String[])params.get("OTHER_NUM");
		String[] OTHER_IDS = (String[])params.get("OTHER_ID");
		String[] OTHER_TITLES = (String[])params.get("OTHER_TITLE");
		String[] OTHER_CONTENTS = (String[])params.get("OTHER_CONTENT");
		double OTHER_PRICE = 0.0, OTHER_NUM = 0.0, OTHER_AMOUNT = 0.0;
		if(OTHER_IDS != null && OTHER_IDS.length > 0){
			for (int i = 0; i < OTHER_IDS.length; i++) {
				
				//非空判断
				if(!CommonUtils.checkString(OTHER_PRICES[i])){
					OTHER_PRICE = 0.0;
				}else{
					OTHER_PRICE = Double.parseDouble(OTHER_PRICES[i]);
				}
				
				if(!CommonUtils.checkString(OTHER_NUMS[i])){
					OTHER_NUM = 0.0;
				}else{
					OTHER_NUM = Double.parseDouble(OTHER_NUMS[i]);
				}
				
				OTHER_AMOUNT += (OTHER_PRICE * OTHER_NUM);
				
				if(OTHER_PRICE != 0 || OTHER_NUM != 0){
					p.clear();
					p.put("ID", CommonUtils.uuid());
					p.put("ORDER_ID", ORDER_ID);
					p.put("ROUTE_ID", ROUTE_ID);
					p.put("OTHER_ID", OTHER_IDS[i]);
					p.put("TITLE", OTHER_TITLES[i]);
					p.put("CONTENT", OTHER_CONTENTS[i]);
					p.put("PRICE", OTHER_PRICES[i]);
					p.put("NUM", OTHER_NUMS[i]);
					this.orderDao.saveOrderOtherPriceDao(p);
				}
					
			}
		}
		
		if(OTHER_AMOUNT != 0){
			params.put("OTHER_SUPPLY_AUDIT", 1);
			params.put("OTHER_AMOUNT", OTHER_AMOUNT);
			
			params.put("SALE_AMOUNT", SALE_AMOUNT+OTHER_AMOUNT);
			params.put("INTER_AMOUNT", INTER_AMOUNT+OTHER_AMOUNT);
			result.put("otherFee", "otherFee");
		}else{
			params.put("OTHER_SUPPLY_AUDIT", 0);
			params.put("OTHER_AMOUNT", 0);
		}
		params.put("IS_RENEW", 1);
		
		this.orderDao.saveDao(params);
		
		p.clear();
		p.put("ID", CommonUtils.uuid());
		p.put("ORDER_ID", ORDER_ID);
		p.put("TITLE", "提交订单");//-----------------------------
		p.put("STEP_USER", (String)params.get("CREATE_USER"));
		p.put("STEP_USER_ID", (String)params.get("CREATE_USER_ID"));
		p.put("REMARK", "");//-----------------------------;
		this.orderDao.saveOrderStepDao(p);
		
		p.clear();
		p.put("PRO_ID", ROUTE_ID);
		p.put("ORDER_ID", ORDER_ID);
		p.put("DISCOUNT_ID", params.get("DISCOUNT_ID"));
		this.discountService.saveDiscountOrderService(p);
		return result;
	}
	@Transactional(rollbackFor={Exception.class})
	public Map<String, Object> updateRouteOrderRenewService(Map<String, Object> params)throws Exception{
		JSONObject detail = JSONObject.fromObject((String)params.get("detail"));
		Map<String, Object> p = new HashMap<String, Object>();
		params.put("COMPANY_ID", detail.get("COMPANY_ID"));
		
		String ROUTE_ID = (String)detail.get("ID");
		params.put("ROUTE_ID", ROUTE_ID);
		String visitors = (String)params.get("visitors");
	    JSONArray jarray = JSONArray.fromObject(visitors);
		Object[] objArray = jarray.toArray();
		
		String[] keyNames = {"NAME", "ATTR_NAME", "SEX", "MOBILE", "CARD_TYPE", "CARD_NO", "CONTACT"};
		Map<String, String>  VISITOR_NUM = new HashMap<String, String>();
	    List<Map<String, Object>> datas = this.jsonArrayToList(objArray, keyNames);
	    params.put("datas", datas);
	    
		Map<String, Object> result = this.validator(params);
		boolean success = (Boolean)result.get("success");
		if(!success){
			return result;
		}
		
		String ORDER_ID = (String)params.get("ID");
		int MAN_COUNT = Integer.parseInt((String)params.get("MAN_COUNT")), CHILD_COUNT = Integer.parseInt((String)params.get("CHILD_COUNT"));
		
		String COMPANY_ID = (String)detail.get("COMPANY_ID");
		String NO = ServiceProxyFactory.getProxy().getTrafficOrderNoDao(COMPANY_ID);
		params.put("NO", NO);
		
		String RQ = (String)params.get("RQ");
//		String _RQ = DateUtil.parseDate(DateUtil.subDate(RQ));
		
		params.put("PRODUCE_ID", (String)detail.get("ID"));
		
		if(!detail.get("PORDUCE_CONCAT").equals("null")){
			params.put("PORDUCE_CONCAT", detail.get("PORDUCE_CONCAT"));
		}else{
			params.put("PORDUCE_CONCAT", "");
		}
		
		params.put("PRODUCE_MOBILE", (String)detail.get("PRODUCE_MOBILE"));
		params.put("START_DATE", RQ);
		
		params.put("COMPANY_ID", (String)detail.get("COMPANY_ID"));
		params.put("COMPANY_NAME", (String)detail.get("COMPANY_NAME"));
		
		params.put("STATUS", 0);
		params.put("PAY_TYPE", 0);
		params.put("ACCOUNT_STATUS", 0);
		
		p.clear();
		int n = 0;
		List<Map<String, Object>> attr = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> priceAttrs = this.trafficDao.listPriceAttrDao(p);
		for (int i = 0; i < priceAttrs.size(); i++) {
			String ID = (String)priceAttrs.get(i).get("ID");
			if(ID.equals("0FA5123749D08C87E050007F0100BCAD")){
				p.put("PID", "0FA5123749D08C87E050007F0100BCAD");
				List<Map<String, Object>> childs = this.trafficDao.listPriceAttrDao(p);
				for (Map<String, Object> map : childs) {
					attr.add(n, map);
					n++;
				}
			}
			attr.add(n, priceAttrs.get(i));
			n++;
		}
		
		
		/**
		 * 删除订单与游客关系
		 */
		p.clear();
		p.put("ORDER_ID", ORDER_ID);
		this.visitorOrderDao.delOrderVisitorByOrderIDDao(p);
		
		/**
		 * 保存游客
		 * 不同类型的游客数量
		 * 成人
		 * 婴儿
		 * 不占床位/不含门票
		 * 不占床位/含门票
		 * 占床位/不含门票
		 * 占床位/含门票
		 * 
		 * 根据游客类型计算 外卖和同行总价
		 */
	    
		double SALE_AMOUNT = 0.0;
		double INTER_AMOUNT = 0.0;
		
	    for (Map<String, Object> data : datas) {
			
			String NAME = (String)data.get("NAME"), ATTR_NAME = (String)data.get("ATTR_NAME"), 
			SEX = (String)data.get("SEX"), MOBILE = (String)data.get("MOBILE"), 
			CARD_TYPE = (String)data.get("CARD_TYPE"), CARD_NO = (String)data.get("CARD_NO"), CONTACT = (String)data.get("CONTACT");
			
			p.clear();
			String VISITOR_ID = CommonUtils.uuid();
			p.put("ID", VISITOR_ID);
			p.put("NAME", NAME);
			p.put("ATTR_NAME", ATTR_NAME);
			for (Map<String, Object> priceAttr : attr) {
				String TITLE = (String)priceAttr.get("TITLE");
				if(ATTR_NAME.equals(TITLE)){
					p.put("ATTR_ID", (String)priceAttr.get("ID"));
					break;
				}
			}
			String num = VISITOR_NUM.get(ATTR_NAME);
			if(CommonUtils.checkString(num)){
				int _num = Integer.parseInt(num)+1;
				VISITOR_NUM.put(ATTR_NAME, String.valueOf(_num));
			}else{
				VISITOR_NUM.put(ATTR_NAME, "1");
			}
			
			if("true".equals(CONTACT)){
				params.put("VISITOR_CONCAT", NAME);
				params.put("VISITOR_MOBILE", MOBILE);
			}
			
			p.put("SEX", SEX);
			p.put("MOBILE", MOBILE);
			p.put("CARD_TYPE", CARD_TYPE);
			p.put("CARD_NO", CARD_NO);
			this.visitorOrderDao.saveVisitorDao(p);
			
			JSONArray PRICES = JSONArray.fromObject(params.get("PRICES"));
		    
		    int statusCode = this.routeService.validation(params);
			    
			String ATTR_ID = (String)p.get("ATTR_ID");
			for (Object PRICE : PRICES) {
				JSONObject _PRICE = JSONObject.fromObject((String)PRICE);
				String _ATTR_ID = (String)_PRICE.get("ATTR_ID");
				String TYPE_ID = (String)_PRICE.get("TYPE_ID");
				double price = 0.0;
				if(CommonUtils.checkString(_PRICE.get("PRICE"))){
					price =  Double.parseDouble(String.valueOf(_PRICE.get("PRICE")));
				}
//				0FA5123749D28C87E050007F0100BCAD	外卖
//				0FA5123749D38C87E050007F0100BCAD	同行
				if(ATTR_ID.equals(_ATTR_ID) && TYPE_ID.equals("0FA5123749D28C87E050007F0100BCAD")){
					SALE_AMOUNT += price;
				}else if(ATTR_ID.equals(_ATTR_ID) && TYPE_ID.equals("0FA5123749D38C87E050007F0100BCAD")){
					INTER_AMOUNT += price;
				}
				
			}
			p.clear();
			p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", ORDER_ID);
			p.put("VISITOR_ID", VISITOR_ID);
			this.visitorOrderDao.saveOrderVisitorDao(p);
			
		}

	    /**
	     * 单房差价格
	     */
	    double SINGLE_ROOM_CNT = Double.parseDouble(String.valueOf(params.get("SINGLE_ROOM_CNT")));
	    
	    if(SINGLE_ROOM_CNT > 0){
	    	double RETAIL_SINGLE_ROOM = Double.parseDouble(String.valueOf(params.get("RETAIL_SINGLE_ROOM")));
	    	SALE_AMOUNT += SINGLE_ROOM_CNT * RETAIL_SINGLE_ROOM;
	    	
	    	double INTER_SINGLE_ROOM = Double.parseDouble(String.valueOf(params.get("INTER_SINGLE_ROOM")));
	    	INTER_AMOUNT += SINGLE_ROOM_CNT * INTER_SINGLE_ROOM;
	    	
	    }
	    
	    
	    /**
		 * 保存线路基础价格
		 * 保存线路交通基础价格
		 * 保存线路交通汇总价格
		 * 补单的订单没有交通,不保存交通的基础价格
		 * 
		 * 0FA5123749D28C87E050007F0100BCAD	外卖
		 * 0FA5123749D38C87E050007F0100BCAD	同行
		 */
	    
	    JSONArray PRICES = JSONArray.fromObject(params.get("PRICES"));
	    int statusCode = this.routeService.validation(params);
	    
		if(statusCode == 0){
			//保存价格库
			p.clear();
			p.put("ORDER_ID", ORDER_ID);
			this.orderDao.delOrderPriceDao(p);
			for (Object PRICE : PRICES) {
				JSONObject _PRICE = JSONObject.fromObject((String)PRICE);
				String TYPE_ID = (String)_PRICE.get("TYPE_ID");
				p.put("ID", CommonUtils.uuid());
				p.put("ORDER_ID", ORDER_ID);
				p.put("ENTITY_ID", ROUTE_ID);
				p.put("ENTITY_TYPE", "2");
				p.put("TYPE_ID", TYPE_ID);
				p.put("TYPE_NAME", _PRICE.get("TYPE_NAME"));
				p.put("ATTR_ID", (String)_PRICE.get("ATTR_ID"));
				p.put("ATTR_NAME", _PRICE.get("ATTR_NAME"));
				if(CommonUtils.checkString(_PRICE.get("PRICE"))){
					p.put("PRICE", _PRICE.get("PRICE"));
				}else{
					p.put("PRICE", 0);
				}
				this.orderDao.saveOrderRenewPriceDao(p);
				p.put("ID", CommonUtils.uuid());
				this.orderDao.saveOrderPriceMainDao(p);
				
			}
		}else{
			result.put("success", false);
			result.put("statusCode", statusCode);
			return result;
		}
		
		params.put("MAN_COUNT", MAN_COUNT);
		params.put("CHILD_COUNT", CHILD_COUNT);
		
		
		/**
		 * 删除原其他价格 
		 */
		p.clear();
		p.put("ORDER_ID", ORDER_ID);
		this.orderDao.delOrderOtherPriceDao(p);
		
		/**
		 * 保存其他价格
		 */
		String[] OTHER_PRICES = (String[])params.get("OTHER_PRICE");
		String[] OTHER_NUMS = (String[])params.get("OTHER_NUM");
		String[] OTHER_IDS = (String[])params.get("OTHER_ID");
		String[] OTHER_TITLES = (String[])params.get("OTHER_TITLE");
		String[] OTHER_CONTENTS = (String[])params.get("OTHER_CONTENT");
		double OTHER_PRICE = 0.0, OTHER_NUM = 0.0, OTHER_AMOUNT = 0.0;
		if(OTHER_IDS != null && OTHER_IDS.length > 0){
			for (int i = 0; i < OTHER_IDS.length; i++) {
				
				//非空判断
				if(!CommonUtils.checkString(OTHER_PRICES[i])){
					OTHER_PRICE = 0.0;
				}else{
					OTHER_PRICE = Double.parseDouble(OTHER_PRICES[i]);
				}
				
				if(!CommonUtils.checkString(OTHER_NUMS[i])){
					OTHER_NUM = 0.0;
				}else{
					OTHER_NUM = Double.parseDouble(OTHER_NUMS[i]);
				}
				
				OTHER_AMOUNT += (OTHER_PRICE * OTHER_NUM);
				
				if(OTHER_PRICE != 0 || OTHER_NUM != 0){
					p.clear();
					p.put("ID", CommonUtils.uuid());
					p.put("ORDER_ID", ORDER_ID);
					p.put("ROUTE_ID", ROUTE_ID);
					p.put("OTHER_ID", OTHER_IDS[i]);
					p.put("TITLE", OTHER_TITLES[i]);
					p.put("CONTENT", OTHER_CONTENTS[i]);
					p.put("PRICE", OTHER_PRICES[i]);
					p.put("NUM", OTHER_NUMS[i]);
					this.orderDao.saveOrderOtherPriceDao(p);
				}
					
			}
		}
		
		if(OTHER_AMOUNT != 0){
			params.put("OTHER_SUPPLY_AUDIT", 1);
			params.put("OTHER_AMOUNT", OTHER_AMOUNT);
			
			params.put("SALE_AMOUNT", SALE_AMOUNT+OTHER_AMOUNT);
			params.put("INTER_AMOUNT", INTER_AMOUNT+OTHER_AMOUNT);
			
			result.put("otherFee", "otherFee");
			
		}else{
			params.put("OTHER_SUPPLY_AUDIT", 0);
			params.put("OTHER_AMOUNT", 0);
			
			params.put("SALE_AMOUNT", SALE_AMOUNT);
			params.put("INTER_AMOUNT", INTER_AMOUNT);
			
		}
		
		this.orderDao.updateDao(params);
		
		p.clear();
		p.put("ID", CommonUtils.uuid());
		p.put("ORDER_ID", ORDER_ID);
		p.put("TITLE", "提交订单");//-----------------------------
		p.put("STEP_USER", (String)params.get("CREATE_USER"));
		p.put("STEP_USER_ID", (String)params.get("CREATE_USER_ID"));
		p.put("REMARK", "");//-----------------------------;
		this.orderDao.saveOrderStepDao(p);
		
		/**
		 * 删除优惠关系
		 * 重新保存优惠关系
		 */
		
		
		p.clear();
		p.put("ORDER_ID", ORDER_ID);
		this.discountService.delDiscountOrderService(p);
		
		p.put("DISCOUNT_ID", params.get("DISCOUNT_ID"));
		this.discountService.saveDiscountOrderService(p);
		
		return result;
	}
	
	/**
	 * 保存线路订单
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor={Exception.class})
	public Map<String, Object> saveRouteOrderService(Map<String, Object> params)throws Exception{
		JSONObject detail = JSONObject.fromObject((String)params.get("detail"));
		Map<String, Object> p = new HashMap<String, Object>();
		params.put("COMPANY_ID", detail.get("COMPANY_ID"));
		
		String visitors = (String)params.get("visitors");
	    JSONArray jarray = JSONArray.fromObject(visitors);
		Object[] objArray = jarray.toArray();
		
		String[] keyNames = {"NAME", "ATTR_NAME", "SEX", "MOBILE", "CARD_TYPE", "CARD_NO", "CONTACT"};
		Map<String, String>  VISITOR_NUM = new HashMap<String, String>();
	    List<Map<String, Object>> datas = this.jsonArrayToList(objArray, keyNames);
	    params.put("datas", datas);
	    
		Map<String, Object> result = this.validator(params);
		boolean success = (Boolean)result.get("success");
		if(!success){
			return result;
		}
		
		String ORDER_ID = (String)params.get("ID");
		int MAN_COUNT = Integer.parseInt((String)params.get("MAN_COUNT")), CHILD_COUNT = Integer.parseInt((String)params.get("CHILD_COUNT"));
		
		params.put("NO", result.get("NO"));
		
		String RQ = params.get("RQ").toString().replaceAll("-", "");
		String _RQ = DateUtil.parseDate(DateUtil.subDate(RQ));
		
		params.put("PRODUCE_ID", (String)detail.get("ID"));
		params.put("TRAFFIC_ID", (String)params.get("PLAN_ID"));
		if(!detail.get("PORDUCE_CONCAT").equals("null")){
			params.put("PORDUCE_CONCAT", detail.get("PORDUCE_CONCAT"));
		}else{
			params.put("PORDUCE_CONCAT", "");
		}
		
		params.put("PRODUCE_USER_ID", (String)detail.get("CREATE_USER_ID"));
		if(CommonUtils.checkString(detail.get("PRODUCE_MOBILE"))){
			params.put("PRODUCE_MOBILE", String.valueOf(detail.get("PRODUCE_MOBILE")));
		}
		params.put("START_DATE", _RQ);
		
		params.put("COMPANY_ID", (String)detail.get("COMPANY_ID"));
		params.put("COMPANY_NAME", (String)detail.get("COMPANY_NAME"));
		
		params.put("STATUS", 0);
		params.put("PAY_TYPE", 0);
		params.put("ACCOUNT_STATUS", 0);
		
		p.clear();
		int n = 0;
		List<Map<String, Object>> attr = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> priceAttrs = this.trafficDao.listPriceAttrDao(p);
		for (int i = 0; i < priceAttrs.size(); i++) {
			String ID = (String)priceAttrs.get(i).get("ID");
			if(ID.equals("0FA5123749D08C87E050007F0100BCAD")){
				p.put("PID", "0FA5123749D08C87E050007F0100BCAD");
				List<Map<String, Object>> childs = this.trafficDao.listPriceAttrDao(p);
				for (Map<String, Object> map : childs) {
					attr.add(n, map);
					n++;
				}
			}
			attr.add(n, priceAttrs.get(i));
			n++;
		}
		
		
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
	    
	    for (Map<String, Object> data : datas) {
			
			String NAME = (String)data.get("NAME"), ATTR_NAME = (String)data.get("ATTR_NAME"), 
			SEX = (String)data.get("SEX"), MOBILE = (String)data.get("MOBILE"), 
			CARD_TYPE = (String)data.get("CARD_TYPE"), CARD_NO = (String)data.get("CARD_NO"), CONTACT = (String)data.get("CONTACT");
			
			p.clear();
			String VISITOR_ID = CommonUtils.uuid();
			p.put("ID", VISITOR_ID);
			p.put("NAME", NAME);
			p.put("ATTR_NAME", ATTR_NAME);
			for (Map<String, Object> priceAttr : attr) {
				String TITLE = (String)priceAttr.get("TITLE");
				if(ATTR_NAME.equals(TITLE)){
					p.put("ATTR_ID", (String)priceAttr.get("ID"));
					break;
				}
			}
			String num = VISITOR_NUM.get(ATTR_NAME);
			if(CommonUtils.checkString(num)){
				int _num = Integer.parseInt(num)+1;
				VISITOR_NUM.put(ATTR_NAME, String.valueOf(_num));
			}else{
				VISITOR_NUM.put(ATTR_NAME, "1");
			}
			
			if("true".equals(CONTACT)){
				params.put("VISITOR_CONCAT", NAME);
				params.put("VISITOR_MOBILE", MOBILE);
			}
			
			p.put("SEX", SEX);
			p.put("MOBILE", MOBILE);
			p.put("CARD_TYPE", CARD_TYPE);
			p.put("CARD_NO", CARD_NO);
			this.visitorOrderDao.saveVisitorDao(p);
			
			p.clear();
			p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", ORDER_ID);
			p.put("VISITOR_ID", VISITOR_ID);
			this.visitorOrderDao.saveOrderVisitorDao(p);
			
		}
	    
	    /**
		 * 保存线路基础价格
		 * 保存线路交通基础价格
		 * 保存线路交通汇总价格
		 */
		
		String ROUTE_ID = (String)detail.get("ID");
		String PLAN_ID = (String)params.get("PLAN_ID");
		p.put("ENTITY_ID", "'"+ROUTE_ID+"'");
		p.put("ORDER_ID", ORDER_ID);
		p.put("ENTITY_TYPE", "2");
		this.orderDao.saveOrderPriceDao(p);
		
		if(CommonUtils.checkString(PLAN_ID)){
			p.clear();
			p.put("ROUTE_ID", ROUTE_ID);
			p.put("RQ", RQ);
			Map<String, Object> routeCalendars = this.routeService.listRouteCalendarService(p).get(0);
			
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
			this.orderDao.saveOrderPriceDao(p);
		}
		
		JSONArray WMBP = (JSONArray)params.get("WMBP");
		
//		0FA5123749D28C87E050007F0100BCAD	外卖
//		0FA5123749D38C87E050007F0100BCAD	同行
		
		for (int i = 0; i < WMBP.size(); i++) {
			p.clear();
			String[] _WMBP = WMBP.get(i).toString().split("-");
			p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", ORDER_ID);
			p.put("TYPE_ID", "0FA5123749D28C87E050007F0100BCAD");
			p.put("TYPE_NAME", "外卖");
			p.put("ATTR_ID", _WMBP[0]);
			p.put("ATTR_NAME", _WMBP[1]);
			p.put("PRICE", _WMBP[2]);
			this.orderDao.saveOrderPriceMainDao(p);
		}
		
		JSONArray THBP = (JSONArray)params.get("THBP");
		for (int i = 0; i < THBP.size(); i++) {
			p.clear();
			String[] _THBP = THBP.get(i).toString().split("-");
			p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", ORDER_ID);
			p.put("TYPE_ID", "0FA5123749D28C87E050007F0100BCAD");
			p.put("TYPE_NAME", "同行");
			p.put("ATTR_ID", _THBP[0]);
			p.put("ATTR_NAME", _THBP[1]);
			p.put("PRICE", _THBP[2]);
			this.orderDao.saveOrderPriceMainDao(p);
		}
		/**
		 * 保存出行方案
		 */
		if(CommonUtils.checkString(PLAN_ID)){
			p.clear();
			p.put("ID", PLAN_ID);
			List<Map<String, Object>> routeTraffics = this.routeService.listRouteTrafficService(p);
			Map<String, Object> routeTraffic = routeTraffics.get(0);
			String _PLAN_ID = CommonUtils.uuid();
			routeTraffic.put("ID", _PLAN_ID);
			routeTraffic.put("ORDER_ID", ORDER_ID);
			routeTraffic.put("BEFORE_ID", PLAN_ID);
			this.orderDao.saveOrderTrafficDao(routeTraffic);
			/**
			 * 保存出行方案详情
			 */
			p.clear();
			p.put("PLAN_ID", PLAN_ID);
			List<Map<String, Object>> routeTrafficDetails = this.routeService.listRouteTrafficDetailService(p);
			for (Map<String, Object> routeTrafficDetail : routeTrafficDetails) {
				String BEFORE_DETAIL_ID = "";
				BEFORE_DETAIL_ID = (String)routeTrafficDetail.get("ID");

				routeTrafficDetail.put("ID", CommonUtils.uuid());
				routeTrafficDetail.put("PLAN_ID", _PLAN_ID);
				routeTrafficDetail.put("ORDER_ID", ORDER_ID);
				
				routeTrafficDetail.put("BEFORE_DETAIL_ID", BEFORE_DETAIL_ID);
				routeTrafficDetail.put("BEFORE_PLAN_ID", PLAN_ID);
				
				this.orderDao.saveOrderTrafficDetailDao(routeTrafficDetail);
			}
		}
		
		/**
		 * 保存合同日志
		 * 修改合同表订单编号和合同使用状态
		 */
		p.clear();
		p.put("ORDER_ID", ORDER_ID);
		p.put("CON_NO", params.get("CON_NO"));
		p.put("CREATE_USER", params.get("CREATE_USER"));
		p.put("CREATE_USER_ID", params.get("CREATE_USER_ID"));
		p.put("TYPE", "3");
		this.companyDao.saveOrderContractLogDao(p);
		
		p.clear();
		p.put("NO", params.get("CON_NO"));
		p.put("STATUS", 1);
		p.put("ORDER_NO", params.get("NO"));
		p.put("ORDER_ID", ORDER_ID);
		p.put("USE_COMPANY_ID", params.get("CREATE_COMPANY_ID"));
		p.put("USE_USER", params.get("CREATE_USER"));
		p.put("USE_USER_ID", params.get("CREATE_USER_ID"));
		this.companyDao.useContractAgencyDao(p);
		
//		params.put("RULE_ID", RULE_ID);//------------------------------------------
		params.put("MAN_COUNT", MAN_COUNT);
		params.put("CHILD_COUNT", CHILD_COUNT);
		params.put("START_CITY", detail.get("BEGIN_CITY"));
		params.put("END_CITY", detail.get("END_CITY"));
		
		/**
		 * 其他价格
		 */
		String[] OTHER_PRICES = (String[])params.get("OTHER_PRICE");
		String[] OTHER_NUMS = (String[])params.get("OTHER_NUM");
		String[] OTHER_IDS = (String[])params.get("OTHER_ID");
		String[] OTHER_TITLES = (String[])params.get("OTHER_TITLE");
		String[] OTHER_CONTENTS = (String[])params.get("OTHER_CONTENT");
		double OTHER_PRICE = 0.0, OTHER_NUM = 0.0, OTHER_AMOUNT = 0.0;
		if(OTHER_IDS != null && OTHER_IDS.length > 0){
			for (int i = 0; i < OTHER_IDS.length; i++) {
				
				//非空判断
				if(!CommonUtils.checkString(OTHER_PRICES[i])){
					OTHER_PRICE = 0.0;
				}else{
					OTHER_PRICE = Double.parseDouble(OTHER_PRICES[i]);
				}
				
				if(!CommonUtils.checkString(OTHER_NUMS[i])){
					OTHER_NUM = 0.0;
				}else{
					OTHER_NUM = Double.parseDouble(OTHER_NUMS[i]);
				}
				
				OTHER_AMOUNT += (OTHER_PRICE * OTHER_NUM);
				
				if(OTHER_PRICE != 0 || OTHER_NUM != 0){
					p.clear();
					p.put("ID", CommonUtils.uuid());
					p.put("ORDER_ID", ORDER_ID);
					p.put("ROUTE_ID", ROUTE_ID);
					p.put("OTHER_ID", OTHER_IDS[i]);
					p.put("TITLE", OTHER_TITLES[i]);
					p.put("CONTENT", OTHER_CONTENTS[i]);
					p.put("PRICE", OTHER_PRICES[i]);
					p.put("NUM", OTHER_NUMS[i]);
					this.orderDao.saveOrderOtherPriceDao(p);
				}
					
			}
		}
		
		if(OTHER_AMOUNT != 0){
			params.put("OTHER_SUPPLY_AUDIT", 1);
			params.put("OTHER_AMOUNT", OTHER_AMOUNT);
			double SALE_AMOUNT = Double.parseDouble(String.valueOf(params.get("SALE_AMOUNT")));
			double INTER_AMOUNT = Double.parseDouble(String.valueOf(params.get("INTER_AMOUNT")));
			params.put("SALE_AMOUNT", SALE_AMOUNT+OTHER_AMOUNT);
			params.put("INTER_AMOUNT", INTER_AMOUNT+OTHER_AMOUNT);
			result.put("otherFee", "otherFee");
		}else{
			params.put("OTHER_SUPPLY_AUDIT", 0);
			params.put("OTHER_AMOUNT", 0);
		}
		
		this.orderDao.saveDao(params);
		
		p.clear();
		p.put("ID", CommonUtils.uuid());
		p.put("ORDER_ID", ORDER_ID);
		p.put("TITLE", "提交订单");//-----------------------------
		p.put("STEP_USER", (String)params.get("CREATE_USER"));
		p.put("STEP_USER_ID", (String)params.get("CREATE_USER_ID"));
		p.put("REMARK", "");//-----------------------------;
		this.orderDao.saveOrderStepDao(p);
		
		p.clear();
		p.put("PRO_ID", ROUTE_ID);
		p.put("ORDER_ID", ORDER_ID);
		p.put("DISCOUNT_ID", params.get("DISCOUNT_ID"));
		this.discountService.saveDiscountOrderService(p);
		return result;
	}
	
	/**
	 * 修改线路订单
	 * @return
	 * @throws Exception
	 */
	@Transactional(rollbackFor={Exception.class})
	public Map<String, Object> updateRouteOrderService(Map<String, Object> params)throws Exception{
		JSONObject detail = JSONObject.fromObject((String)params.get("detail"));
		Map<String, Object> p = new HashMap<String, Object>();
		params.put("COMPANY_ID", detail.get("COMPANY_ID"));
		
		String visitors = (String)params.get("visitors");
	    JSONArray jarray = JSONArray.fromObject(visitors);
		Object[] objArray = jarray.toArray();
		
		String[] keyNames = {"NAME", "ATTR_NAME", "SEX", "MOBILE", "CARD_TYPE", "CARD_NO", "CONTACT"};
		Map<String, String>  VISITOR_NUM = new HashMap<String, String>();
	    List<Map<String, Object>> datas = this.jsonArrayToList(objArray, keyNames);
	    params.put("datas", datas);
	    
		Map<String, Object> result = this.validator(params);
		boolean success = (Boolean)result.get("success");
		if(!success){
			return result;
		}
		
		String ORDER_ID = (String)params.get("ID");
		int MAN_COUNT = Integer.parseInt((String)params.get("MAN_COUNT")), CHILD_COUNT = Integer.parseInt((String)params.get("CHILD_COUNT"));
		
		String COMPANY_ID = (String)detail.get("COMPANY_ID");
		String NO = ServiceProxyFactory.getProxy().getTrafficOrderNoDao(COMPANY_ID);
		params.put("NO", NO);
		
		String RQ = (String)params.get("RQ");
		String _RQ = DateUtil.parseDate(DateUtil.subDate(RQ));
		
		params.put("PRODUCE_ID", (String)detail.get("ID"));
		params.put("TRAFFIC_ID", (String)params.get("PLAN_ID"));
		params.put("PORDUCE_CONCAT", (String)detail.get("PORDUCE_CONCAT"));
		if(CommonUtils.checkString(String.valueOf(detail.get("PRODUCE_MOBILE")))){
			params.put("PRODUCE_MOBILE", (String)detail.get("PRODUCE_MOBILE"));
		}
		params.put("START_DATE", _RQ);
		
		params.put("COMPANY_ID", (String)detail.get("COMPANY_ID"));
		params.put("COMPANY_NAME", (String)detail.get("COMPANY_NAME"));
		
		params.put("STATUS", 0);
		params.put("PAY_TYPE", 0);
		params.put("ACCOUNT_STATUS", 0);
		
		p.clear();
		int n = 0;
		List<Map<String, Object>> attr = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> priceAttrs = this.trafficDao.listPriceAttrDao(p);
		for (int i = 0; i < priceAttrs.size(); i++) {
			String ID = (String)priceAttrs.get(i).get("ID");
			if(ID.equals("0FA5123749D08C87E050007F0100BCAD")){
				p.put("PID", "0FA5123749D08C87E050007F0100BCAD");
				List<Map<String, Object>> childs = this.trafficDao.listPriceAttrDao(p);
				for (Map<String, Object> map : childs) {
					attr.add(n, map);
					n++;
				}
			}
			attr.add(n, priceAttrs.get(i));
			n++;
		}
		
		
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
	    
		/**
		 * 删除订单与游客关系
		 */
		p.clear();
		p.put("ORDER_ID", ORDER_ID);
		this.visitorOrderDao.delOrderVisitorByOrderIDDao(p);
		
	    for (Map<String, Object> data : datas) {
			
			String NAME = (String)data.get("NAME"), ATTR_NAME = (String)data.get("ATTR_NAME"), 
			SEX = (String)data.get("SEX"), MOBILE = (String)data.get("MOBILE"), 
			CARD_TYPE = (String)data.get("CARD_TYPE"), CARD_NO = (String)data.get("CARD_NO"), CONTACT = (String)data.get("CONTACT");
			
			p.clear();
			String VISITOR_ID = CommonUtils.uuid();
			p.put("ID", VISITOR_ID);
			p.put("NAME", NAME);
			p.put("ATTR_NAME", ATTR_NAME);
			for (Map<String, Object> priceAttr : attr) {
				String TITLE = (String)priceAttr.get("TITLE");
				if(ATTR_NAME.equals(TITLE)){
					p.put("ATTR_ID", (String)priceAttr.get("ID"));
					break;
				}
			}
			String num = VISITOR_NUM.get(ATTR_NAME);
			if(CommonUtils.checkString(num)){
				int _num = Integer.parseInt(num)+1;
				VISITOR_NUM.put(ATTR_NAME, String.valueOf(_num));
			}else{
				VISITOR_NUM.put(ATTR_NAME, "1");
			}
			
			if("true".equals(CONTACT)){
				params.put("VISITOR_CONCAT", NAME);
				params.put("VISITOR_MOBILE", MOBILE);
			}
			
			p.put("SEX", SEX);
			p.put("MOBILE", MOBILE);
			p.put("CARD_TYPE", CARD_TYPE);
			p.put("CARD_NO", CARD_NO);
			this.visitorOrderDao.saveVisitorDao(p);
			
			p.clear();
			p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", ORDER_ID);
			p.put("VISITOR_ID", VISITOR_ID);
			this.visitorOrderDao.saveOrderVisitorDao(p);
			
		}
	    
	    /**
	     * 删除原保存的基础价格
	     * 
		 * 保存线路基础价格
		 * 保存线路交通基础价格
		 * 保存线路交通汇总价格
		 */
		
	    p.clear();
		p.put("ORDER_ID", ORDER_ID);
		this.orderDao.delOrderPriceDao(p);
		
		String ROUTE_ID = (String)detail.get("ID");
		String PLAN_ID = (String)params.get("PLAN_ID");
		p.clear();
		p.put("ENTITY_ID", "'"+ROUTE_ID+"'");
		p.put("ORDER_ID", ORDER_ID);
		p.put("ENTITY_TYPE", "2");
		this.orderDao.saveOrderPriceDao(p);
		
		if(CommonUtils.checkString(PLAN_ID)){
			p.clear();
			p.put("ROUTE_ID", ROUTE_ID);
			p.put("RQ", RQ);
			Map<String, Object> routeCalendars = this.routeService.listRouteCalendarService(p).get(0);
			
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
			this.orderDao.saveOrderPriceDao(p);
		}
		
		JSONArray WMBP = (JSONArray)params.get("WMBP");
		
//		0FA5123749D28C87E050007F0100BCAD	外卖
//		0FA5123749D38C87E050007F0100BCAD	同行
		p.clear();
		p.put("ORDER_ID", ORDER_ID);
		this.orderDao.delOrderPriceMainDao(p);
		for (int i = 0; i < WMBP.size(); i++) {
			p.clear();
			String[] _WMBP = WMBP.get(i).toString().split("-");
			p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", ORDER_ID);
			p.put("TYPE_ID", "0FA5123749D28C87E050007F0100BCAD");
			p.put("TYPE_NAME", "外卖");
			p.put("ATTR_ID", _WMBP[0]);
			p.put("ATTR_NAME", _WMBP[1]);
			p.put("PRICE", _WMBP[2]);
			this.orderDao.saveOrderPriceMainDao(p);
		}
		
		JSONArray THBP = (JSONArray)params.get("THBP");
		for (int i = 0; i < THBP.size(); i++) {
			p.clear();
			String[] _THBP = THBP.get(i).toString().split("-");
			p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", ORDER_ID);
			p.put("TYPE_ID", "0FA5123749D28C87E050007F0100BCAD");
			p.put("TYPE_NAME", "同行");
			p.put("ATTR_ID", _THBP[0]);
			p.put("ATTR_NAME", _THBP[1]);
			p.put("PRICE", _THBP[2]);
			this.orderDao.saveOrderPriceMainDao(p);
		}
		
		//删除出行方案
		if(CommonUtils.checkString(PLAN_ID)){
			
			p.clear();
			p.put("ORDER_ID", ORDER_ID);
			this.orderDao.delOrderTrafficDao(p);
			/**
			 * 保存出行方案
			 */
			p.clear();
			p.put("ID", PLAN_ID);
			List<Map<String, Object>> routeTraffics = this.routeService.listRouteTrafficService(p);
			Map<String, Object> routeTraffic = routeTraffics.get(0);
			routeTraffic.put("ID", CommonUtils.uuid());
			routeTraffic.put("ORDER_ID", ORDER_ID);
			this.orderDao.saveOrderTrafficDao(routeTraffic);
			
			p.clear();
			p.put("ORDER_ID", ORDER_ID);
			this.orderDao.delOrderTrafficDetailDao(p);
			/**
			 * 保存出行方案详情
			 */
			p.clear();
			p.put("PLAN_ID", PLAN_ID);
			List<Map<String, Object>> routeTrafficDetails = this.routeService.listRouteTrafficDetailService(p);
			for (Map<String, Object> routeTrafficDetail : routeTrafficDetails) {
				routeTrafficDetail.put("ID", CommonUtils.uuid());
				routeTrafficDetail.put("ORDER_ID", ORDER_ID);
				this.orderDao.saveOrderTrafficDetailDao(routeTrafficDetail);
			}
			
		}
		
//		params.put("RULE_ID", RULE_ID);//------------------------------------------
		params.put("MAN_COUNT", MAN_COUNT);
		params.put("CHILD_COUNT", CHILD_COUNT);
		
		
		/**
		 * 删除原其他价格 
		 */
		p.clear();
		p.put("ORDER_ID", ORDER_ID);
		this.orderDao.delOrderOtherPriceDao(p);
		
		/**
		 * 保存其他价格
		 */
		String[] OTHER_PRICES = (String[])params.get("OTHER_PRICE");
		String[] OTHER_NUMS = (String[])params.get("OTHER_NUM");
		String[] OTHER_IDS = (String[])params.get("OTHER_ID");
		String[] OTHER_TITLES = (String[])params.get("OTHER_TITLE");
		String[] OTHER_CONTENTS = (String[])params.get("OTHER_CONTENT");
		double OTHER_PRICE = 0.0, OTHER_NUM = 0.0, OTHER_AMOUNT = 0.0;
		if(OTHER_IDS != null && OTHER_IDS.length > 0){
			for (int i = 0; i < OTHER_IDS.length; i++) {
				
				//非空判断
				if(!CommonUtils.checkString(OTHER_PRICES[i])){
					OTHER_PRICE = 0.0;
				}else{
					OTHER_PRICE = Double.parseDouble(OTHER_PRICES[i]);
				}
				
				if(!CommonUtils.checkString(OTHER_NUMS[i])){
					OTHER_NUM = 0.0;
				}else{
					OTHER_NUM = Double.parseDouble(OTHER_NUMS[i]);
				}
				
				OTHER_AMOUNT += (OTHER_PRICE * OTHER_NUM);
				
				if(OTHER_PRICE != 0 || OTHER_NUM != 0){
					p.clear();
					p.put("ID", CommonUtils.uuid());
					p.put("ORDER_ID", ORDER_ID);
					p.put("ROUTE_ID", ROUTE_ID);
					p.put("OTHER_ID", OTHER_IDS[i]);
					p.put("TITLE", OTHER_TITLES[i]);
					p.put("CONTENT", OTHER_CONTENTS[i]);
					p.put("PRICE", OTHER_PRICES[i]);
					p.put("NUM", OTHER_NUMS[i]);
					this.orderDao.saveOrderOtherPriceDao(p);
				}
					
			}
		}
		
		if(OTHER_AMOUNT != 0){
			params.put("OTHER_SUPPLY_AUDIT", 1);
			params.put("OTHER_AMOUNT", OTHER_AMOUNT);
			
			double SALE_AMOUNT = Double.parseDouble(String.valueOf(params.get("SALE_AMOUNT")));
			double INTER_AMOUNT = Double.parseDouble(String.valueOf(params.get("INTER_AMOUNT")));
			params.put("SALE_AMOUNT", SALE_AMOUNT+OTHER_AMOUNT);
			params.put("INTER_AMOUNT", INTER_AMOUNT+OTHER_AMOUNT);
			
			result.put("otherFee", "otherFee");
			
		}else{
			params.put("OTHER_SUPPLY_AUDIT", 0);
			params.put("OTHER_AMOUNT", 0);
		}
		
		this.orderDao.updateDao(params);
		
		p.clear();
		p.put("ID", CommonUtils.uuid());
		p.put("ORDER_ID", ORDER_ID);
		p.put("TITLE", "提交订单");//-----------------------------
		p.put("STEP_USER", (String)params.get("CREATE_USER"));
		p.put("STEP_USER_ID", (String)params.get("CREATE_USER_ID"));
		p.put("REMARK", "");//-----------------------------;
		this.orderDao.saveOrderStepDao(p);
		
		/**
		 * 删除优惠关系
		 * 重新保存优惠关系
		 */
		
		
		p.clear();
		p.put("ORDER_ID", ORDER_ID);
		this.discountService.delDiscountOrderService(p);
		
		p.put("DISCOUNT_ID", params.get("DISCOUNT_ID"));
		this.discountService.saveDiscountOrderService(p);
		
		return result;
	}
	
	/**
	 * 保存订单
	 */
	@Transactional(rollbackFor={Exception.class})
	public Map<String, Object> saveService(Map<String, Object> params)throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", true);
		result.put("code", "1");
		Map<String, Object> p = new HashMap<String, Object>();
		String ORDER_ID = (String)params.get("ID");
		int MAN_COUNT = Integer.parseInt((String)params.get("MAN_COUNT")), CHILD_COUNT = Integer.parseInt((String)params.get("CHILD_COUNT"));
		
		JSONObject detail = JSONObject.fromObject((String)params.get("detail"));
		
		String COMPANY_ID = (String)detail.get("COMPANY_ID");
		String NO = ServiceProxyFactory.getProxy().getTrafficOrderNoDao(COMPANY_ID);
		if(NO.equals("-1")){
			result.put("success", false);
			result.put("code", "-1");//供应商没有设置前缀(订单编号生成失败)
			return result;
		}
		
		/**
		 * 验证游客信息填写是否完整
		 */
		String visitors = (String)params.get("visitors");
	    JSONArray jarray = JSONArray.fromObject(visitors);
		Object[] objArray = jarray.toArray();
		
		String[] keyNames = {"NAME", "ATTR_NAME", "SEX", "MOBILE", "CARD_TYPE", "CARD_NO", "CONTACT"};
	    List<Map<String, Object>> datas = this.jsonArrayToList(objArray, keyNames);
		    
	    for (Map<String, Object> data : datas) {
	    	String NAME = (String)data.get("NAME"), ATTR_NAME = (String)data.get("ATTR_NAME");
			if(!CommonUtils.checkString(NAME) || !CommonUtils.checkString(ATTR_NAME)){
				result.put("success", false);
				result.put("code", "-2");//游客信息填写不完整
				return result;
			}
		}
	    
		params.put("NO", NO);
		
		String RQ = (String)detail.get("RQ");
		RQ = DateUtil.parseDate(DateUtil.subDate(RQ));
		
		/**
		 * 卖家的site_rela_id
		 */
		String CITY_ID = (String)detail.get("CITY_ID");
		p.put("CITY_ID", CITY_ID);
		Map<String, Object> site = this.companyDao.listSiteManagerDao(p).get(0);
		
		params.put("SITE_RELA_ID", site.get("ID"));
		params.put("PRODUCE_ID", (String)detail.get("TRAFFIC_ID"));
		params.put("TRAFFIC_ID", (String)detail.get("TRAFFIC_ID"));
		params.put("PORDUCE_CONCAT", (String)detail.get("USER_NAME"));
		params.put("PRODUCE_MOBILE", (String)detail.get("MOBILE"));
		params.put("PRODUCE_USER_ID", (String)detail.get("CREATE_USER_ID"));
		params.put("START_DATE", RQ);
		
		params.put("COMPANY_ID", (String)detail.get("COMPANY_ID"));
		params.put("COMPANY_NAME", (String)detail.get("COMPANY"));
		
		params.put("STATUS", 0);
		params.put("PAY_TYPE", 0);
		params.put("ACCOUNT_STATUS", 0);
		
		/**
		 * 单价
		 */
		String RULE_ID = (String)detail.get("ID");
		p.put("ENTITY_ID", "'"+RULE_ID+"'");
		p.put("ORDER_ID", ORDER_ID);
		p.put("ENTITY_TYPE", "1");
		this.orderDao.saveOrderPriceDao(p);
		
		p.clear();
		int n = 0;
		List<Map<String, Object>> attr = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> priceAttrs = this.trafficDao.listPriceAttrDao(p);
		for (int i = 0; i < priceAttrs.size(); i++) {
			String ID = (String)priceAttrs.get(i).get("ID");
			if(ID.equals("0FA5123749D08C87E050007F0100BCAD")){
				p.put("PID", "0FA5123749D08C87E050007F0100BCAD");
				List<Map<String, Object>> childs = this.trafficDao.listPriceAttrDao(p);
				for (Map<String, Object> map : childs) {
					attr.add(n, map);
					n++;
				}
			}
			attr.add(n, priceAttrs.get(i));
			n++;
		}
		
		/**
		 * 保存游客
		 */
	    for (Map<String, Object> data : datas) {
			
			String NAME = (String)data.get("NAME"), ATTR_NAME = (String)data.get("ATTR_NAME"), 
			SEX = (String)data.get("SEX"), MOBILE = (String)data.get("MOBILE"), 
			CARD_TYPE = (String)data.get("CARD_TYPE"), CARD_NO = (String)data.get("CARD_NO"), 
			CONTACT = (String)data.get("CONTACT");
			
			p.clear();
			String VISITOR_ID = CommonUtils.uuid();
			p.put("ID", VISITOR_ID);
			p.put("NAME", NAME);
			p.put("ATTR_NAME", ATTR_NAME);
			for (Map<String, Object> priceAttr : attr) {
				String TITLE = (String)priceAttr.get("TITLE");
				if(ATTR_NAME.equals(TITLE)){
					p.put("ATTR_ID", (String)priceAttr.get("ID"));
					break;
				}
			}
			
			if("true".equals(CONTACT)){
				params.put("VISITOR_CONCAT", NAME);
				params.put("VISITOR_MOBILE", MOBILE);
			}
			
			p.put("SEX", SEX);
			p.put("MOBILE", MOBILE);
			p.put("CARD_TYPE", CARD_TYPE);
			p.put("CARD_NO", CARD_NO);
			this.visitorOrderDao.saveVisitorDao(p);
			
			p.clear();
			p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", ORDER_ID);
			p.put("VISITOR_ID", VISITOR_ID);
			this.visitorOrderDao.saveOrderVisitorDao(p);
			
		}
		params.put("RULE_ID", RULE_ID);
		params.put("MAN_COUNT", MAN_COUNT);
		params.put("CHILD_COUNT", CHILD_COUNT);
		params.put("START_CITY", detail.get("START_CITY"));
		params.put("END_CITY", detail.get("END_CITY"));
		
		params.put("OTHER_SUPPLY_AUDIT", 0);
		params.put("OTHER_AMOUNT", 0);
		
		this.orderDao.saveDao(params);
		
		p.clear();
		p.put("ID", CommonUtils.uuid());
		p.put("ORDER_ID", ORDER_ID);
		p.put("TITLE", "提交订单");//-----------------------------
		p.put("STEP_USER", (String)params.get("CREATE_USER"));
		p.put("STEP_USER_ID", (String)params.get("CREATE_USER_ID"));
		p.put("REMARK", "");//-----------------------------;
		this.orderDao.saveOrderStepDao(p);
		return result;
	}
	
	@Transactional(rollbackFor={Exception.class})
	public Map<String, Object> onlineSeat(Map<String, Object> params)throws Exception{
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", true);
		result.put("code", "1");
		
		String type = (String)params.get("type");
		String ORDER_ID = (String)params.get("ORDER_ID");
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("ID", ORDER_ID);
		p.put("start", 1);
		p.put("end", 1);
		Map<String, Object> order = this.orderDao.listOrderDao(p).get(0);
		String RULE_ID = (String)order.get("RULE_ID");
		int seat = 0;
		if(type.equals("2")){
			String rq = order.get("START_DATE").toString().replaceAll("-", "");
			String plan_id = (String)order.get("TRAFFIC_ID");
			String order_id = ORDER_ID;
			int sale_seat = Integer.parseInt(String.valueOf(order.get("MAN_COUNT")))+Integer.parseInt(String.valueOf(order.get("CHILD_COUNT")));
			seat = ServiceProxyFactory.getProxy().usedSeatByRouteOrder(rq,plan_id,order_id,Integer.toString(sale_seat));
		}else if(type.equals("3")){
			
		}else{
			p.clear();
			p.put("RULE_ID", RULE_ID);
			p.put("SALE_DATE", order.get("START_DATE").toString().replaceAll("-", ""));
			p.put("SALE_SEAT", Integer.parseInt(String.valueOf(order.get("MAN_COUNT")))+Integer.parseInt(String.valueOf(order.get("CHILD_COUNT"))));
			p.put("ENTITY_ID", ORDER_ID);
			p.put("TRAFFIC_ID", (String)order.get("TRAFFIC_ID"));
			p.put("TYPE", order.get("PRODUCE_TYPE"));
			seat = ServiceProxyFactory.getProxy().usedSeat(p);
		}
		
		if(seat == 0){
			
		}else{
			result.put("success", false);
			result.put("code", "-2");//占座失败
		}
		
		return result;
	}
	
	@Transactional(rollbackFor={Exception.class})
	public int editLostService(Map<String, Object> params)throws Exception{
		try {
			Map<String, Object> p = new HashMap<String, Object>();
			String ID = (String)params.get("ID");
			p.put("ID", ID);
			p.put("start", 1);
			p.put("end", 1);
			Map<String, Object> order = this.orderDao.listOrderDao(p).get(0);
			String IS_LOST = String.valueOf(order.get("IS_LOST"));
//			System.out.println("***********STATUS**************"+STATUS);
			if(IS_LOST.equals("1")){
				String SITE_RELA_ID = (String)order.get("SITE_RELA_ID");
				String type = String.valueOf(order.get("PRODUCE_TYPE"));
				
				p.clear();
				p.put("ID", SITE_RELA_ID);
				p.put("start", 1);
				p.put("end", 1);
				List<Map<String, Object>> siteManagers = this.companyDao.listSiteManagerDao(p);
				Map<String, Object> siteManager = siteManagers.get(0);
				String SITE_COMPANY_ID = (String)siteManager.get("COMPANY_ID");
				params.put("CREATE_USER", (String)order.get("CREATE_USER"));
				params.put("CREATE_USER_ID", (String)order.get("CREATE_USER_ID"));
				params.put("type", String.valueOf(order.get("PRODUCE_TYPE")));
				/**
				 * 记账完成后 更新数据库状态
				 * */
//				System.out.println("***********111111111**************");
				this.balancePayAfter(p,(String)order.get("ID"));
				/**
				 * 记录状态跟踪
				 * */
//				System.out.println("***********22222222**************");
				this.orderStep(p,params,(String)order.get("ID"), "订单付款");
				/**
				 * 买家 记账
				 * */
//				System.out.println("***********333333**************");
				this.sellStepFunds(p,params,order,(String)order.get("ID"),SITE_RELA_ID);
				/**
				 * 卖家 记账
				 * */
//				System.out.println("***********444444**************");
				if(CommonUtils.checkString(type) && (type.equals("2") || type.equals("3"))){
//					System.out.println("***********55555**************");
					this.buyStepFundsByRouteOrder(p,params,order,(String)order.get("ID"),SITE_RELA_ID);
				}else{
//					System.out.println("***********66666**************");
					//交通
					this.buyStepFundsByTrafficOrder(p,params,order,(String)order.get("ID"),SITE_RELA_ID);
				}
//				System.out.println("***********77777**************");
				/**
				 * 平台 记账
				 * 同时给买家记录一笔负数
				 * */
				this.siteStepFunds(p,params,order,(String)order.get("ID"),SITE_RELA_ID,SITE_COMPANY_ID);
				
				p.clear();
				p.put("ID", order.get("ID"));
				p.put("IS_LOST", "0");
				p.put("LOST_TIME_NULL", "YES");
				this.orderDao.updateDao(p);
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}	
	}
	
	@Transactional(rollbackFor={Exception.class})
	public int onlinePayCallBackService(Map<String, Object> params){
		try {
			Map<String, Object> p = new HashMap<String, Object>();
			String NO = (String)params.get("NO");
			p.put("NO", NO);
			p.put("start", 1);
			p.put("end", 1);
			Map<String, Object> order = this.orderDao.listOrderDao(p).get(0);
			String STATUS = String.valueOf(order.get("STATUS"));
			String ID = String.valueOf(order.get("ID"));
			String CREATE_COMPANY_ID = String.valueOf(order.get("CREATE_COMPANY_ID"));
			String IS_EARNEST = String.valueOf(order.get("IS_EARNEST"));
//			System.out.println("***********STATUS**************"+STATUS);
			if(STATUS.equals("1") || IS_EARNEST.equals("4")){
				
				/**
				 * 定金支付
				 * 更新定金支付状态为已支付
				 */
				if(IS_EARNEST.equals("4")){
					p.put("IS_EARNEST", 1);
					p.put("PLATFORM_PAY", 1);
					this.orderDao.updateOrderEarnestStatusDao(p);
				}
				
				String SITE_RELA_ID = (String)order.get("SITE_RELA_ID");
				String type = String.valueOf(order.get("PRODUCE_TYPE"));
				
				p.clear();
				p.put("ID", SITE_RELA_ID);
				p.put("start", 1);
				p.put("end", 1);
				List<Map<String, Object>> siteManagers = this.companyDao.listSiteManagerDao(p);
				Map<String, Object> siteManager = siteManagers.get(0);
				String SITE_COMPANY_ID = (String)siteManager.get("COMPANY_ID");
				params.put("CREATE_USER", (String)order.get("CREATE_USER"));
				params.put("CREATE_USER_ID", (String)order.get("CREATE_USER_ID"));
				params.put("type", type);
				
				
				String stepTitle = "定金付款";
				/**
				 * 定金支付的订单不更新订单状态
				 */
				if(IS_EARNEST.equals("4")){

				}else{
					/**
					 * 记账完成后 更新数据库状态
					 */
//					System.out.println("***********111111111**************");
					this.balancePayAfter(p,(String)order.get("ID"));
					stepTitle = "订单付款";					
				}
				
				/**
				 * 记录状态跟踪
				 * */
//				System.out.println("***********22222222**************");
				this.orderStep(p,params,(String)order.get("ID"), stepTitle);
				/**
				 * 买家 记账
				 * */
//				System.out.println("***********333333**************");
				this.sellStepFunds(p,params,order,(String)order.get("ID"),SITE_RELA_ID);
				/**
				 * 卖家 记账
				 * */
//				System.out.println("***********444444**************");
				if(CommonUtils.checkString(type) && (type.equals("2") || type.equals("3"))){
//					System.out.println("***********55555**************");
					this.buyStepFundsByRouteOrder(p,params,order,(String)order.get("ID"),SITE_RELA_ID);
				}else{
//					System.out.println("***********66666**************");
					//交通
					this.buyStepFundsByTrafficOrder(p,params,order,(String)order.get("ID"),SITE_RELA_ID);
				}
//				System.out.println("***********77777**************");
				/**
				 * 平台 记账
				 * 同时给买家记录一笔负数
				 * */
				this.siteStepFunds(p,params,order,(String)order.get("ID"),SITE_RELA_ID,SITE_COMPANY_ID);
				
				/**
				 * 定金支付不记录优惠
				 */
				if(CommonUtils.checkString(order.get("isEarnest")) && order.get("isEarnest").equals("1")){

				}else{
					p.clear();
					p.put("ORDER_ID", order.get("ID"));
					List<Map<String, Object>> discountOrders = this.discountService.listDiscountOrderService(p);
					if(CommonUtils.checkList(discountOrders) && discountOrders.size() == 1){
						double INTER_AMOUNT = Double.parseDouble(String.valueOf(order.get("INTER_AMOUNT")));
						String PRODUCE_ID = (String)order.get("PRODUCE_ID");
						this.saveDiscountInfo((String)discountOrders.get(0).get("DISCOUNT_ID"), (String)params.get("platfrom"), "1", (String)order.get("ID"), INTER_AMOUNT, PRODUCE_ID, SITE_COMPANY_ID, SITE_RELA_ID, params, order);
					}
				}
				
				
				p.clear();
				p.put("IS_MESSAGE", 1);
				p.put("PRODUCE_ID", order.get("PRODUCE_ID"));
				List<Map<String, Object>> proContacts = this.routeService.listProContactService(p);
				
				if(CommonUtils.checkList(proContacts) && proContacts.size() == 1 && CommonUtils.checkString((String)proContacts.get(0).get("MOBILE"))){
					String content = "尊敬的"+order.get("COMPANY_NAME")+"，"+order.get("BUY_COMPANY")+"于"+DateUtil.getNowDateTimeString()+"提交了订单："+order.get("PRODUCE_NAME")+" "+order.get("START_DATE")+"出发";
					if(type.equals("2")){
						String START_DATE = (String)order.get("START_DATE");
						int DAY_COUNT = Integer.parseInt(String.valueOf(order.get("DAY_COUNT")));
						content += "/"+DateUtil.parseDate(DateUtil.getNDay(DateUtil.parseDate(START_DATE), DAY_COUNT), "yyyy-MM-dd")+"返回";
					}
					content += "，游客为"+order.get("MAN_COUNT")+"大"+order.get("CHILD_COUNT")+"小， 联系人："+order.get("CHINA_NAME")+" 电话："+order.get("BUY_MOBILE")+"。请提前通知游客出团信息，并做好接待服务";
					p.clear();
					p.put("COMPANY_ID", (String)order.get("CREATE_COMPANY_ID"));
					double c = 50.00;
					double cnt = Math.ceil(Double.parseDouble(String.valueOf(content.length()))/c);
					p.put("useCount", cnt);
					int status = this.smsService.expenseSMS(p);
					if(status == 0){
						SMSSender.sendSMS((String)proContacts.get(0).get("MOBILE"), content, (String)order.get("CREATE_USER"), "", "3", (String)order.get("SITE_ID"), "2", "0", ID, CREATE_COMPANY_ID, cnt);
					}
				}
				
				p.clear();
				p.put("COMPANY_ID", order.get("CREATE_COMPANY_ID"));
				p.put("IS_FINANCE", "1");
				p.put("start", 1);
				p.put("end", 1);
				List<Map<String, Object>> data = this.userDao.listDao(p);
				System.out.println("给下单人公司的财务短信通知人发送短信-------"+data.size());
				if(CommonUtils.checkList(data) && data.size() == 1){
					Map<String, Object> user = data.get(0);
					String MOBILE = (String)user.get("MOBILE");
					/**
					 * 给下单人公司的财务短信通知人发送短信
					 */
					if(CommonUtils.checkString(MOBILE)){
						String content = "尊敬的"+order.get("BUY_COMPANY")+"，于"+DateUtil.getNowDateTimeString()+"提交了订单："+order.get("PRODUCE_NAME")+" "+order.get("START_DATE")+"出发";
						if(type.equals("2")){
							String START_DATE = (String)order.get("START_DATE");
							int DAY_COUNT = Integer.parseInt(String.valueOf(order.get("DAY_COUNT")));
							content += "/"+DateUtil.parseDate(DateUtil.getNDay(DateUtil.parseDate(START_DATE), DAY_COUNT), "yyyy-MM-dd")+"返回";
						}
						content += "，游客为"+order.get("MAN_COUNT")+"大"+order.get("CHILD_COUNT")+"小， 联系人："+order.get("CHINA_NAME")+" 电话："+order.get("BUY_MOBILE")+"。请提前通知游客出团信息，并做好接待服务";
						p.clear();
						p.put("COMPANY_ID", (String)order.get("CREATE_COMPANY_ID"));
						double c = 50.00;
						double cnt = Math.ceil(Double.parseDouble(String.valueOf(content.length()))/c);
						p.put("useCount", cnt);
						int status = this.smsService.expenseSMS(p);
						if(status == 0){
							SMSSender.sendSMS(MOBILE, content, (String)order.get("CREATE_USER"), "", "3", (String)order.get("SITE_ID"), "2", "0", ID, CREATE_COMPANY_ID, cnt);
						}
						
					}
					
					/**
					 * 给下单人总公司的财务短信通知人发送短信
					 */
					
					String PARENT_COMPANY_ID = (String)user.get("PARENT_COMPANY_ID");
					System.out.println("给下单人总公司的财务短信通知人发送短信-------"+PARENT_COMPANY_ID);
					if(CommonUtils.checkString(PARENT_COMPANY_ID) && !PARENT_COMPANY_ID.equals("-1")){
						String PARENT_COMPANY = (String)user.get("PARENT_COMPANY");
						p.put("COMPANY_ID", PARENT_COMPANY_ID);
						p.put("IS_FINANCE", "1");
						p.put("start", 1);
						p.put("end", 1);
						List<Map<String, Object>> _data = this.userDao.listDao(p);
						if(CommonUtils.checkList(_data) && _data.size() == 1){
							MOBILE = (String)_data.get(0).get("MOBILE");
							if(CommonUtils.checkString(MOBILE)){
								String content = "尊敬的"+PARENT_COMPANY+"，"+order.get("BUY_COMPANY")+"于"+DateUtil.getNowDateTimeString()+"提交的订单："+order.get("PRODUCE_NAME")+" "+order.get("START_DATE")+"出发";
								if(type.equals("2")){
									String START_DATE = (String)order.get("START_DATE");
									int DAY_COUNT = Integer.parseInt(String.valueOf(order.get("DAY_COUNT")));
									content += "/"+DateUtil.parseDate(DateUtil.getNDay(DateUtil.parseDate(START_DATE), DAY_COUNT), "yyyy-MM-dd")+"返回";
								}
								content += "，游客为"+order.get("MAN_COUNT")+"大"+order.get("CHILD_COUNT")+"小， 联系人："+order.get("CHINA_NAME")+" 电话："+order.get("BUY_MOBILE")+"。请提前通知游客出团信息，并做好接待服务";
								p.clear();
								p.put("COMPANY_ID", (String)order.get("CREATE_COMPANY_ID"));
								double c = 50.00;
								double cnt = Math.ceil(Double.parseDouble(String.valueOf(content.length()))/c);
								p.put("useCount", cnt);
								int status = this.smsService.expenseSMS(p);
								if(status == 0){
									SMSSender.sendSMS(MOBILE, content, (String)order.get("CREATE_USER"), "", "3", (String)order.get("SITE_ID"), "2", "0", ID, CREATE_COMPANY_ID, cnt);
								}
								
							}
						}
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
				this.orderDao.updateDao(p);
				
				p.clear();
				p.put("ID", CommonUtils.uuid());
				p.put("ORDER_ID", order.get("ID"));
				p.put("TITLE", "掉单");
				p.put("STEP_USER", order.get("CREATE_USER"));
				p.put("STEP_USER_ID", order.get("CREATE_USER_ID"));
				p.put("REMARK", "");
				this.orderDao.saveOrderStepDao(p);
				
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}	
		
	}
	
	@Transactional(rollbackFor={Exception.class})
	public Map<String, Object> payService(Map<String, Object> params)throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", true);
		result.put("code", "1");
		Map<String, Object> p = new HashMap<String, Object>();
		String ORDER_ID = (String)params.get("ORDER_ID");
		p.put("ID", ORDER_ID);
		p.put("start", 1);
		p.put("end", 10);
		List<Map<String, Object>> orders = null;
		
		String type = (String)params.get("type");
		//type=4是补单的状态 前台用
		if(CommonUtils.checkString(type) && (type.equals("2") || type.equals("3") || type.equals("4"))){
			if(type.equals("3")){
				p.put("TYPE", type);
			}
			orders = this.orderDao.listRouteOrderDao(p);
		}else{
			orders = this.orderDao.listDao(p);
		}
		
		if(CommonUtils.checkList(orders) && orders.size() == 1){
			Map<String, Object> order = orders.get(0);
			String IS_EARNEST = String.valueOf(order.get("IS_EARNEST"));
			String SITE_RELA_ID = (String)order.get("SITE_RELA_ID");
			p.clear();
			p.put("ID", SITE_RELA_ID);
			p.put("start", 1);
			p.put("end", 1);
			List<Map<String, Object>> siteManagers = this.companyDao.listSiteManagerDao(p);
			Map<String, Object> siteManager = siteManagers.get(0);
			String SITE_COMPANY_ID = (String)siteManager.get("COMPANY_ID");
			String RULE_ID = (String)order.get("RULE_ID");
			
			String isEarnest = (String)params.get("isEarnest");
			
			/**
			 * 扣座位
			 * 定金已付的订单不需要扣座位
			 */
			int seat = 0;
			if(isEarnest.equals("0")){
				p.clear();
				if(type.equals("2")){
					String rq = order.get("START_DATE").toString().replaceAll("-", "");
					String plan_id = (String)order.get("TRAFFIC_ID");
					String order_id = ORDER_ID;
					int sale_seat = Integer.parseInt(String.valueOf(order.get("MAN_COUNT")))+Integer.parseInt(String.valueOf(order.get("CHILD_COUNT")));
					seat = ServiceProxyFactory.getProxy().usedSeatByRouteOrder(rq,plan_id,order_id,Integer.toString(sale_seat));
				}else if(type.equals("3") || type.equals("4")){
					//type=4是补单的状态 前台用
				}else{
					p.put("RULE_ID", RULE_ID);
					p.put("SALE_DATE", order.get("START_DATE").toString().replaceAll("-", ""));
					p.put("SALE_SEAT", Integer.parseInt(String.valueOf(order.get("MAN_COUNT")))+Integer.parseInt(String.valueOf(order.get("CHILD_COUNT"))));
					p.put("ENTITY_ID", ORDER_ID);
					p.put("TRAFFIC_ID", (String)order.get("TRAFFIC_ID"));
					p.put("TYPE", order.get("PRODUCE_TYPE"));
					seat = ServiceProxyFactory.getProxy().usedSeat(p);
				}
			}
			
			/**
			 * 占座成功或 已支付定金需要支付尾款的订单
			 */
			if(seat == 0 || IS_EARNEST.equals("1")){
				
				/**
				 * 保存优惠日志
				 * 优惠规则ID
				 * 平台 B2B APP
				 * 支付类型 在线 余额
				 * 支付定金的时候不需要计算优惠
				 */
				p.clear();
				p.put("ORDER_ID", ORDER_ID);
				List<Map<String, Object>> discountOrders = this.discountService.listDiscountOrderService(p);
				if(CommonUtils.checkList(discountOrders) && discountOrders.size() == 1 && (!CommonUtils.checkString(isEarnest) || !isEarnest.equals("1") || IS_EARNEST.equals("1"))){
					double INTER_AMOUNT = Double.parseDouble(String.valueOf(order.get("INTER_AMOUNT")));
					String PRODUCE_ID = (String)order.get("PRODUCE_ID");
					double AMOUNT = this.saveDiscountInfo((String)discountOrders.get(0).get("DISCOUNT_ID"), (String)params.get("PLATFROM"), "2", ORDER_ID, INTER_AMOUNT, PRODUCE_ID, SITE_COMPANY_ID, SITE_RELA_ID, params, order);
					order.put("DISCOUNT_TOTAL_AMOUNT", AMOUNT);
				}
				
				/**
				 * 定金支付
				 */
				double EARNEST_INTER_AMOUNT = 0.0;
				double EARNEST_RETAIL_AMOUNT = 0.0;
				if(CommonUtils.checkString(isEarnest) && isEarnest.equals("1") && IS_EARNEST.equals("0")){
					/**
					 * 修改订单的定金信息
					 */
					String PRODUCE_ID = (String)order.get("PRODUCE_ID");
					p.clear();
					p.put("ID", PRODUCE_ID);
					RouteEntity routeEntity = this.routeService.routeDetailService(p);
					if(routeEntity != null && CommonUtils.checkString(routeEntity.getIS_EARNEST()) && routeEntity.getIS_EARNEST().equals("1")){
							double PERSON_COUNT = Double.parseDouble(String.valueOf(order.get("MAN_COUNT"))) + Double.parseDouble(String.valueOf(order.get("CHILD_COUNT")));
							EARNEST_INTER_AMOUNT = Double.parseDouble(routeEntity.getEARNEST_INTER()) * PERSON_COUNT;
							EARNEST_RETAIL_AMOUNT = Double.parseDouble(routeEntity.getEARNEST_RETAIL()) * PERSON_COUNT;
							
							String status = this.payEarnest(p, params, order, EARNEST_INTER_AMOUNT);
							if(!status.equals("1")){
								throw new Exception("-3"){
									
								};
							}
							
							p.clear();
							p.put("ID", order.get("ID"));
							p.put("EARNEST_INTER", routeEntity.getEARNEST_INTER());
							p.put("EARNEST_RETAIL", routeEntity.getEARNEST_RETAIL());
							p.put("EARNEST_INTER_AMOUNT", EARNEST_INTER_AMOUNT);
							p.put("EARNEST_RETAIL_AMOUNT", EARNEST_RETAIL_AMOUNT);
							p.put("EARNEST_DAY_COUNT", routeEntity.getEARNEST_DAY_COUNT());
							p.put("EARNEST_TYPE", routeEntity.getEARNEST_TYPE());
							p.put("IS_EARNEST", 1);
							p.put("PLATFORM_PAY", 1);
							this.orderDao.updateOrderEarnestInfoDao(p);
							p.remove("PLATFORM_PAY");
							
							/**
							 * 事务问题
							 * 买/卖家记账方法需要
							 */
							order.put("isEarnest", isEarnest);
							order.put("EARNEST_INTER_AMOUNT", EARNEST_INTER_AMOUNT);
							order.put("EARNEST_RETAIL_AMOUNT", EARNEST_RETAIL_AMOUNT);
							
					}else{
						result.put("success", false);
						result.put("code", "-4");//线路不支持定金付款
					}
				}else{
					/**
					 *  DEPART_ID
					 *  MONEY
					 *  USER_ID
					 *  NOTE
					 *  ORDER_NO
					 *  PRODUCE_TYPE 产品类型 1线路2交通3酒店4景点5保险
					 *  第一步：扣款
					 */
					String status = this.balancePay(p,params,order);
					if(!status.equals("1")){
						throw new Exception("-3"){
							
						};
	//					result.put("success", false);
	//					result.put("code", "-3");//占座失败
	//					return result;
					}
				}
				
				/**
				 * 定金支付 未支付单房差和其他费用, 资金流水不用记录
				 * 不更新订单状态
				 * 不记录状态跟踪
				 * 
				 */
				String stepTitle = "定金付款";
				if(CommonUtils.checkString(isEarnest) && isEarnest.equals("1") && IS_EARNEST.equals("0")){
					
				}else{
					if(IS_EARNEST.equals("0")){
						/**
						 * 单房差
						 * 其他费用
						 */
						if(type.equals("2") || type.equals("3") || type.equals("4")){
							this.saveSingleRoom(p, params, order, ORDER_ID, SITE_RELA_ID);
							this.saveOtherPrice(p, params, order, ORDER_ID, SITE_RELA_ID);
						}
					}
					
					/**
					 * 记账完成后 更新数据库状态
					 * */
					this.balancePayAfter(p,ORDER_ID);
					if(IS_EARNEST.equals("1")){
						stepTitle = "订单尾款";
					}else{
						stepTitle = "订单付款";
					}
					
				}
				
				/**
				 * 记录状态跟踪
				 * */
				this.orderStep(p,params,ORDER_ID, stepTitle);
				
				/**
				 * 卖家 记账
				 * */
				this.sellStepFunds(p,params,order,ORDER_ID,SITE_RELA_ID);
				/**
				 * 买家 记账
				 * */
				if(CommonUtils.checkString(type) && (type.equals("2") || type.equals("3") || type.equals("4"))){
					this.buyStepFundsByRouteOrder(p,params,order,ORDER_ID,SITE_RELA_ID);
				}else{
					//交通
					this.buyStepFundsByTrafficOrder(p,params,order,ORDER_ID,SITE_RELA_ID);
				}
				
				/**
				 * 平台 记账
				 * 同时给买家记录一笔负数
				 * */
				this.siteStepFunds(p,params,order,ORDER_ID,SITE_RELA_ID,SITE_COMPANY_ID);
				
				String CREATE_COMPANY_ID = String.valueOf(order.get("CREATE_COMPANY_ID"));
				
				p.clear();
				p.put("IS_MESSAGE", 1);
				p.put("PRODUCE_ID", order.get("PRODUCE_ID"));
				List<Map<String, Object>> proContacts = this.routeService.listProContactService(p);
				
				if(CommonUtils.checkList(proContacts) && proContacts.size() == 1 && CommonUtils.checkString((String)proContacts.get(0).get("MOBILE"))){
					String content = "尊敬的"+order.get("COMPANY_NAME")+"，"+order.get("BUY_COMPANY")+"于"+DateUtil.getNowDateTimeString()+"提交了订单："+order.get("PRODUCE_NAME")+" "+order.get("START_DATE")+"出发";
					if(type.equals("2")){
						String START_DATE = (String)order.get("START_DATE");
						int DAY_COUNT = Integer.parseInt(String.valueOf(order.get("DAY_COUNT")));
						content += "/"+DateUtil.parseDate(DateUtil.getNDay(DateUtil.parseDate(START_DATE), DAY_COUNT), "yyyy-MM-dd")+"返回";
					}
					content += "，游客为"+order.get("MAN_COUNT")+"大"+order.get("CHILD_COUNT")+"小， 联系人："+order.get("BUY_USER_NAME")+" 电话："+order.get("BUY_PHONE")+"。请提前通知游客出团信息，并做好接待服务";
					
					p.clear();
					p.put("COMPANY_ID", (String)order.get("CREATE_COMPANY_ID"));
					double c = 50.00;
					double cnt = Math.ceil(Double.parseDouble(String.valueOf(content.length()))/c);
					p.put("useCount", cnt);
					int status = this.smsService.expenseSMS(p);
					if(status == 0){
						SMSSender.sendSMS((String)proContacts.get(0).get("MOBILE"), content, (String)order.get("CREATE_USER"), "", "3", (String)order.get("SITE_ID"), "2", "0", (String)order.get("ID"), CREATE_COMPANY_ID, cnt);
					}
					
				}
				
				p.clear();
				p.put("COMPANY_ID", order.get("CREATE_COMPANY_ID"));
				p.put("IS_FINANCE", "1");
				p.put("start", 1);
				p.put("end", 1);
				List<Map<String, Object>> data = this.userDao.listDao(p);
				if(CommonUtils.checkList(data) && data.size() == 1){
					String MOBILE = (String)data.get(0).get("MOBILE");
					/**
					 * 给下单人公司的财务短信通知人发送短信
					 */
					if(CommonUtils.checkString(MOBILE)){
						String content = "尊敬的"+order.get("BUY_COMPANY")+"，于"+DateUtil.getNowDateTimeString()+"提交的订单："+order.get("PRODUCE_NAME")+" "+order.get("START_DATE")+"出发";
						if(type.equals("2")){
							String START_DATE = (String)order.get("START_DATE");
							int DAY_COUNT = Integer.parseInt(String.valueOf(order.get("DAY_COUNT")));
							content += "/"+DateUtil.parseDate(DateUtil.getNDay(DateUtil.parseDate(START_DATE), DAY_COUNT), "yyyy-MM-dd")+"返回";
						}
						content += "，游客为"+order.get("MAN_COUNT")+"大"+order.get("CHILD_COUNT")+"小， 联系人："+order.get("BUY_USER_NAME")+" 电话："+order.get("BUY_PHONE")+"。请提前通知游客出团信息，并做好接待服务";
						
						p.clear();
						p.put("COMPANY_ID", (String)order.get("CREATE_COMPANY_ID"));
						double c = 50.00;
						double cnt = Math.ceil(Double.parseDouble(String.valueOf(content.length()))/c);
						p.put("useCount", cnt);
						int status = this.smsService.expenseSMS(p);
						if(status == 0){
							SMSSender.sendSMS(MOBILE, content, (String)order.get("CREATE_USER"), "", "3", (String)order.get("SITE_ID"), "2", "0", (String)order.get("ID"), CREATE_COMPANY_ID, cnt);
						}
					}
				}
				
				/**
				 * 给总公司的财务发送短信通知
				 */
				String PARENT_COMPANY_ID = (String)params.get("PARENT_COMPANY_ID");
				if(CommonUtils.checkString(PARENT_COMPANY_ID) && !PARENT_COMPANY_ID.equals("-1")){
					String PARENT_COMPANY = (String)params.get("PARENT_COMPANY");
					p.put("COMPANY_ID", PARENT_COMPANY_ID);
					p.put("IS_FINANCE", "1");
					p.put("start", 1);
					p.put("end", 1);
					List<Map<String, Object>> _data = this.userDao.listDao(p);
					if(CommonUtils.checkList(_data) && _data.size() == 1){
						String MOBILE = (String)_data.get(0).get("MOBILE");
						if(CommonUtils.checkString(MOBILE)){
							String content = "尊敬的"+PARENT_COMPANY+"，"+order.get("BUY_COMPANY")+"于"+DateUtil.getNowDateTimeString()+"提交的订单："+order.get("PRODUCE_NAME")+" "+order.get("START_DATE")+"出发";
							if(type.equals("2")){
								String START_DATE = (String)order.get("START_DATE");
								int DAY_COUNT = Integer.parseInt(String.valueOf(order.get("DAY_COUNT")));
								content += "/"+DateUtil.parseDate(DateUtil.getNDay(DateUtil.parseDate(START_DATE), DAY_COUNT), "yyyy-MM-dd")+"返回";
							}
							content += "，游客为"+order.get("MAN_COUNT")+"大"+order.get("CHILD_COUNT")+"小， 联系人："+order.get("BUY_USER_NAME")+" 电话："+order.get("BUY_PHONE")+"。请提前通知游客出团信息，并做好接待服务";
							
							p.clear();
							p.put("COMPANY_ID", (String)order.get("CREATE_COMPANY_ID"));
							double c = 50.00;
							double cnt = Math.ceil(Double.parseDouble(String.valueOf(content.length()))/c);
							p.put("useCount", cnt);
							int status = this.smsService.expenseSMS(p);
							if(status == 0){
								SMSSender.sendSMS(MOBILE, content, (String)order.get("CREATE_USER"), "", "3", (String)order.get("SITE_ID"), "2", "0", (String)order.get("ID"), CREATE_COMPANY_ID, cnt);
							}
							
						}
					}
				}
				
			}else{
				result.put("success", false);
				result.put("code", "-2");//占座失败
			}
		}else{
			result.put("success", false);
			result.put("code", "-1");//订单不存在
		}
		return result;
	}
	
	public void saveOtherPrice(Map<String, Object> p, Map<String, Object>params, Map<String, Object> order, String ORDER_ID, String SITE_RELA_ID) throws Exception{
//		 15:单房差付款(旅行社,负数) 16:单房差(供应商,正数)
		double OTHER_AMOUNT = Double.parseDouble(String.valueOf(order.get("OTHER_AMOUNT")));
		if(OTHER_AMOUNT != 0){
			p.clear();
			p.put("ORDER_ID", ORDER_ID);
			p.put("ID", CommonUtils.uuid());
			p.put("TYPE", 17);
			p.put("AMOUNT", -OTHER_AMOUNT);
			p.put("ACCOUNT_TYPE", 1);
			p.put("ACCOUNT_COMPANY_ID", (String)order.get("COMPANY_ID"));
			p.put("SITE_RELA_ID", SITE_RELA_ID);
			p.put("CREATE_USER", (String)params.get("CREATE_USER"));
			p.put("CREATE_USER_ID", (String)params.get("CREATE_USER_ID"));
			this.orderDao.saveOrderFundsDao(p);
			
			p.put("ID", CommonUtils.uuid());
			p.put("TYPE", 18);
			p.put("ACCOUNT_TYPE", 2);
			p.put("AMOUNT", OTHER_AMOUNT);
			this.orderDao.saveOrderFundsDao(p);
		}
	}
	public void saveSingleRoom(Map<String, Object> p, Map<String, Object>params, Map<String, Object> order, String ORDER_ID, String SITE_RELA_ID) throws Exception{
//		 15:单房差付款(旅行社,负数) 16:单房差(供应商,正数)
		double INTER_SINGLE_ROOM = Double.parseDouble(String.valueOf(order.get("INTER_SINGLE_ROOM")));
		double SINGLE_ROOM_CNT = Double.parseDouble(String.valueOf(order.get("SINGLE_ROOM_CNT")));
		if(SINGLE_ROOM_CNT != 0){
			p.clear();
			p.put("ORDER_ID", ORDER_ID);
			p.put("ID", CommonUtils.uuid());
			p.put("TYPE", 15);
//			p.put("ENTITY_ID", );
//			p.put("ENTITY_TYPE",  );
			p.put("AMOUNT", -(INTER_SINGLE_ROOM * SINGLE_ROOM_CNT));
			if(SINGLE_ROOM_CNT != 0){
				p.put("REMARKS", "单价:"+INTER_SINGLE_ROOM+",数量:"+order.get("SINGLE_ROOM_CNT"));	
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
				p.put("REMARKS", "单价:"+INTER_SINGLE_ROOM+",数量:"+order.get("SINGLE_ROOM_CNT"));	
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
		params.put("IS_USE", "0");
		params.put("PAY_WAY", PAY_WAY);
		
		List<Map<String, Object>> discountProducts = this.discountService.listDiscountProductService(params);
		
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
			else if("3".equals(RULE_TYPE)){
				double PERSON_COUNT = Double.parseDouble(String.valueOf(order.get("MAN_COUNT"))) + Double.parseDouble(String.valueOf(order.get("CHILD_COUNT")));
				BigDecimal b = new BigDecimal(PERSON_COUNT*MONEY);
				TOTAL_AMOUNT = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
			params.put("TOTAL_AMOUNT", TOTAL_AMOUNT);
					
			this.discountService.updateDiscountOrderService(params);
			/**
			 * 记账
			 */
			this.saveDiscountAccount(ORDER_ID, TOTAL_AMOUNT, SITE_COMPANY_ID, SITE_RELA_ID, p, order);
		}else{
			params.clear();
			params.put("ORDER_ID", ORDER_ID);
			this.discountService.delDiscountOrderService(params);
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
	
	private List<Map<String, Object>> jsonArrayToList(Object[] objArray, String[] keyNames){
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
	    for(int i = 0; i < objArray.length; i++){
	    	JSONObject jobject = JSONObject.fromObject(objArray[i]);
	    	Map<String, Object> data = new HashMap<String, Object>();
	    	for (int j = 0; j < keyNames.length; j++) {
	    		boolean b = jobject.has(keyNames[j]);
	    		if(b){
	    			data.put(keyNames[j], jobject.getString(keyNames[j]));
	    		}
			}
	    	datas.add(data);
		}
		return datas;
	}
	
	@Transactional(rollbackFor={Exception.class})
	public Map<String, Object> updateService(Map<String, Object> params)throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", true);
		result.put("code", "1");
		Map<String, Object> p = new HashMap<String, Object>();
		String ORDER_ID = (String)params.get("ID");
		/**
		 * 联系人信息
		 * 集合信息
		 * 游客信息
		 */
		
		
		p.clear();
		int n = 0;
		List<Map<String, Object>> attr = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> priceAttrs = this.trafficDao.listPriceAttrDao(p);
		for (int i = 0; i < priceAttrs.size(); i++) {
			String ID = (String)priceAttrs.get(i).get("ID");
			if(ID.equals("0FA5123749D08C87E050007F0100BCAD")){
				p.put("PID", "0FA5123749D08C87E050007F0100BCAD");
				List<Map<String, Object>> childs = this.trafficDao.listPriceAttrDao(p);
				for (Map<String, Object> map : childs) {
					attr.add(n, map);
					n++;
				}
			}
			attr.add(n, priceAttrs.get(i));
			n++;
		}
		
		String visitors = (String)params.get("visitors");
	    JSONArray jarray = JSONArray.fromObject(visitors);
	    Object[] objArray = jarray.toArray();
	    String[] keyNames = {"NAME", "ATTR_NAME", "SEX", "MOBILE", "CARD_TYPE", "CARD_NO", "CONTACT"};
	    List<Map<String, Object>> datas = this.jsonArrayToList(objArray, keyNames);
	    
	    /**
		 * 验证游客信息填写是否完整
		 */
	    for (Map<String, Object> data : datas) {
	    	String NAME = (String)data.get("NAME"), ATTR_NAME = (String)data.get("ATTR_NAME");
			if(!CommonUtils.checkString(NAME) || !CommonUtils.checkString(ATTR_NAME)){
				result.put("success", false);
				result.put("code", "-2");//游客信息填写不完整
				return result;
			}
		}
		
		/**
		 * 删除订单与游客关系
		 */
		p.clear();
		p.put("ORDER_ID", ORDER_ID);
		this.visitorOrderDao.delOrderVisitorByOrderIDDao(p);
		
		for (Map<String, Object> data : datas) {
			
			String NAME = (String)data.get("NAME"), ATTR_NAME = (String)data.get("ATTR_NAME"), 
			SEX = (String)data.get("SEX"), MOBILE = (String)data.get("MOBILE"), 
			CARD_TYPE = (String)data.get("CARD_TYPE"), CARD_NO = (String)data.get("CARD_NO"),
			CONTACT = (String)data.get("CONTACT");
			
			p.clear();
			String VISITOR_ID = CommonUtils.uuid();
			p.put("ID", VISITOR_ID);
			p.put("NAME", NAME);
			p.put("ATTR_NAME", ATTR_NAME);
			for (Map<String, Object> priceAttr : attr) {
				String TITLE = (String)priceAttr.get("TITLE");
				if(ATTR_NAME.equals(TITLE)){
					p.put("ATTR_ID", (String)priceAttr.get("ID"));
					break;
				}
			}
			
			if("true".equals(CONTACT)){
				params.put("VISITOR_CONCAT", NAME);
				params.put("VISITOR_MOBILE", MOBILE);
			}
			
			p.put("SEX", SEX);
			p.put("MOBILE", MOBILE);
			p.put("CARD_TYPE", CARD_TYPE);
			p.put("CARD_NO", CARD_NO);
			this.visitorOrderDao.saveVisitorDao(p);
			
			p.clear();
			p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", ORDER_ID);
			p.put("VISITOR_ID", VISITOR_ID);
			this.visitorOrderDao.saveOrderVisitorDao(p);
			
		}
		
		p.put("ID", ORDER_ID);
		
		p.put("VISITOR_CONCAT", (String)params.get("VISITOR_CONCAT"));
		p.put("VISITOR_MOBILE",(String)params.get("VISITOR_MOBILE"));
		
		p.put("MUSTER_TIME", (String)params.get("MUSTER_TIME"));
		p.put("MUSTER_PLACE", (String)params.get("MUSTER_PLACE"));
		
		p.put("MAN_COUNT", (String)params.get("MAN_COUNT"));
		p.put("CHILD_COUNT", (String)params.get("CHILD_COUNT"));
		
		p.put("SALE_AMOUNT", (String)params.get("SALE_AMOUNT"));
		p.put("INTER_AMOUNT", (String)params.get("INTER_AMOUNT"));
		
		this.orderDao.updateDao(p);
		
		p.clear();
		p.put("ID", CommonUtils.uuid());
		p.put("ORDER_ID", ORDER_ID);
		p.put("TITLE", "修改订单");//-----------------------------
		p.put("STEP_USER", (String)params.get("CREATE_USER"));
		p.put("STEP_USER_ID", (String)params.get("CREATE_USER_ID"));
		p.put("REMARK", "");//-----------------------------;
		this.orderDao.saveOrderStepDao(p);
		
		return result;
	}
	
	public List<Map<String, Object>> listService(Map<String, Object> params){
		return this.orderDao.listDao(params);
	}
	
	public int countService(Map<String, Object> params){
		return this.orderDao.countDao(params);
	}
	
	public List<Map<String, Object>> listOrderPriceService(Map<String, Object> params){
		return this.orderDao.listOrderPriceDao(params);
	}
	
	public List<Map<String, Object>> listOrderStepService(Map<String, Object> params){
		return this.orderDao.listOrderStepDao(params);
	}
	
	public List<Map<String, Object>> listOrderFundsService(Map<String, Object> params){
		return this.orderDao.listOrderFundsDao(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public Map<String, Object> cancelOrder(Map<String, Object> params)throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("success", true);
		result.put("code", "1");
		
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("ID", (String)params.get("ORDER_ID"));
		p.put("STATUS", 6);
		this.orderDao.updateDao(p);
		
		p.clear();
		p.put("ID", CommonUtils.uuid());
		p.put("ORDER_ID", (String)params.get("ORDER_ID"));
		p.put("TITLE", "取消订单");//-----------------------------
		p.put("STEP_USER", (String)params.get("CREATE_USER"));
		p.put("STEP_USER_ID", (String)params.get("CREATE_USER_ID"));
		p.put("REMARK", params.get("REMARK"));//-----------------------------;
		this.orderDao.saveOrderStepDao(p);
		
		/**
		 * 解除订单与合同的关系
		 * 重置合同信息
		 */
		p.clear();
		p.put("ORDER_ID", (String)params.get("ORDER_ID"));
		this.orderDao.restOrderContractAgencyDao(p);
		
		p.clear();
		p.put("CONTRACT_AGENCY_NO", (String)params.get("CONTRACT_AGENCY_NO"));
		this.orderDao.restContractAgencyDao(p);
		return result;
	}
	
	@Transactional(rollbackFor={Exception.class})
	public Map<String, Object> saveOrderFundsService(Map<String, Object> params)throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("success", true);
		result.put("code", "1");
		String orderId = (String)params.get("orderId");
		
		Map<String, Object> p = new HashMap<String, Object>();
		
//		p.put("ID", orderId);
//		p.put("start", 1);
//		p.put("end", 10);
//		List<Map<String, Object>> orders = null;
//		String ENTITY_TYPE = String.valueOf(params.get("ENTITY_TYPE"));
//		if(ENTITY_TYPE.equals("2")){
//			orders = this.orderDao.listRouteOrderDao(p);
//		}else{
//			orders = this.orderDao.listDao(p);
//		}
//		if(!CommonUtils.checkList(orders) || orders.size() != 1){
////			result.put("success", false);
////			result.put("code", "-1");//订单不存在
////			return result;
//			throw new Exception("订单不存在"){};
//		}
		
		Map<String, Object> order = (Map<String, Object>)params.get("order");
		
		//默认0 1外卖2同行3外卖调价4同行调价5外卖退款6同行退款7营销费(平台)8旅行社实收供应商退款9营销费(供应商)10同行付款, 11:平台优惠, 12:分销商优惠  13:优惠活动退款(供应商)14:优惠活动退款(平台) 15:单房差付款(旅行社,负数) 16:单房差(供应商,正数)  17:其他费用(旅行社,负数) 18:其他费用(供应商,正数)
		//营销费记两笔第1笔供应商负数 第2笔记平台实收正数  平台营销费 实收 退款 各记录一笔
		//1: 1(+), 3, 5(-), 8(+), 10(-), 12(+)
		//2: 2(+), 4, 6(-), 9(-)
		//3: 7(+), 11(-)
		
		//1,2,7是在订单付款的时候保存的，不在此处添加
		
		//同行时 需要记录6和8  6的时候需要保存8
		//营销费时 需要记录7和9 7的时候需要保存9
		
		p.clear();
		double AMOUNT = Double.parseDouble(String.valueOf(params.get("AMOUNT")));
		
		String TYPE = String.valueOf(params.get("TYPE")), ACCOUNT_TYPE = "", ACCOUNT_COMPANY_ID = "";
		
		if(CommonUtils.checkString(TYPE)){
			
			if("3,5".indexOf(TYPE) != -1){
				ACCOUNT_TYPE = "1";
				ACCOUNT_COMPANY_ID = (String)params.get("COMPANY_ID");
			}else if("4,6".indexOf(TYPE) != -1){
				ACCOUNT_TYPE = "2";
				ACCOUNT_COMPANY_ID = (String)order.get("COMPANY_ID");
			}
			
			if("5,6".indexOf(TYPE) != -1){
				//负数
				AMOUNT = (AMOUNT > 0) ? (0 - AMOUNT) : AMOUNT;
			}
		}
		
		if(ACCOUNT_TYPE.equals("")){
			throw new Exception("数据不完整"){};
//			result.put("success", false);
//			result.put("code", "-2");//数据不完整
//			return result;
		}
		
		p.put("ID", CommonUtils.uuid());
		p.put("ORDER_ID", orderId);
		p.put("TYPE", TYPE);
		p.put("AMOUNT", AMOUNT);
		p.put("REMARKS", params.get("REMARKS"));
		p.put("ACCOUNT_TYPE", ACCOUNT_TYPE);
		p.put("ACCOUNT_COMPANY_ID", ACCOUNT_COMPANY_ID);
		p.put("ENTITY_TYPE", params.get("ENTITY_TYPE"));
		p.put("CREATE_USER_ID", (String)params.get("USER_ID"));
		p.put("CREATE_USER", (String)params.get("USER_NAME"));
		p.put("SITE_RELA_ID", (String)params.get("SITE_RELA_ID"));
		p.put("ENTITY_ID", params.get("ENTITY_ID"));
		p.put("PID", params.get("PID"));
		this.orderDao.saveOrderFundsDao(p);
		
		if("6".equals(TYPE)){
			
			p.clear();
			p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", orderId);
			p.put("TYPE", 8);
			p.put("AMOUNT", (0 - AMOUNT));
			p.put("REMARKS", params.get("REMARKS"));
			p.put("ACCOUNT_TYPE", 1);
			p.put("ACCOUNT_COMPANY_ID", (String)order.get("CREATE_COMPANY_ID"));
			p.put("ENTITY_TYPE", params.get("ENTITY_TYPE"));
			p.put("CREATE_USER_ID", (String)params.get("USER_ID"));
			p.put("CREATE_USER", (String)params.get("USER_NAME"));
			p.put("SITE_RELA_ID", (String)params.get("SITE_RELA_ID"));
			p.put("ENTITY_ID", params.get("ENTITY_ID"));
			p.put("PID", params.get("PID"));
			
			this.orderDao.saveOrderFundsDao(p);
			
			/**
			 * 查询付款人部门ID
			 */
			p.clear();
			p.put("ORDER_ID", orderId);
			p.put("TYPE", 1);
			Map<String, Object> fund = this.orderDao.listOrderFundsDao(p).get(0);
			String CREATE_USER_ID = (String)fund.get("CREATE_USER_ID");
			p.clear();
			p.put("ID", CREATE_USER_ID);
			Map<String, Object> _user = this.userDao.detailUserDao(p);
			/**
			 * 订单退款 
			 * DEPART_ID
			   MONEY
			   USER_ID
			   NOTE 订单退款+订单编号
			   ORDER_NO
			   PRODUCE_TYPE 产品类型 1线路2交通3酒店4景点5保险
			   return : "-1" 失败
			 * */
			String PAY_TYPE = String.valueOf(order.get("PAY_TYPE"));
			if(PAY_TYPE.equals("0")){
				p.clear();
				p.put("DEPART_ID", _user.get("DEPART_ID"));
				p.put("MONEY", (0 - AMOUNT));
				p.put("USER_ID", (String)params.get("USER_ID"));
				p.put("NOTE", "订单退款 "+(String)order.get("NO"));
				p.put("ORDER_NO", (String)order.get("NO"));
				String  PRODUCE_TYPE = String.valueOf(order.get("PRODUCE_TYPE"));
				if(PRODUCE_TYPE.equals("3")){
					PRODUCE_TYPE = "2";
				}
				p.put("PRODUCE_TYPE", PRODUCE_TYPE);
				String status = ServiceProxyFactory.getProxy().orderRefundService(p);
				if(status.equals("-1")){
					throw new Exception("退款失败");
				}
			}
		}
		return result;
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void autoRefundEarnestService(Map<String, Object> params)throws Exception{
		Map<String, Object> p = new HashMap<String, Object>();
		List<Map<String, Object>> orders = this.orderDao.listRefundEarnestDao(params);
		for (Map<String, Object> order : orders) {
			/**
			 * 查询付款人部门ID
			 */
			double EARNEST_INTER_AMOUNT = Double.parseDouble(String.valueOf(order.get("EARNEST_INTER_AMOUNT")));  
			p.clear();
			String CREATE_USER_ID = (String)order.get("CREATE_USER_ID");
			p.put("ID", CREATE_USER_ID);
			Map<String, Object> _user = this.userDao.detailUserDao(p);
			/**
			 * 订单退款 
			 * DEPART_ID
			   MONEY
			   USER_ID
			   NOTE 订单退款+订单编号
			   ORDER_NO
			   PRODUCE_TYPE 产品类型 1线路2交通3酒店4景点5保险
			   return : "-1" 失败
			 * */
			String PAY_TYPE = String.valueOf(order.get("PAY_TYPE"));
			if(PAY_TYPE.equals("0")){
				p.clear();
				p.put("DEPART_ID", _user.get("DEPART_ID"));
				p.put("MONEY", EARNEST_INTER_AMOUNT);
				p.put("OPER", "-");
				p.put("USER_ID", (String)_user.get("ID"));
				p.put("NOTE", "订单退款 "+(String)order.get("NO"));
				p.put("ORDER_NO", (String)order.get("NO"));
				String  PRODUCE_TYPE = String.valueOf(order.get("PRODUCE_TYPE"));
				if(PRODUCE_TYPE.equals("3")){
					PRODUCE_TYPE = "2";
				}
				p.put("PRODUCE_TYPE", PRODUCE_TYPE);
				String status = ServiceProxyFactory.getProxy().orderRefundService(p);
				if(status.equals("-1")){
					throw new Exception("退款失败");
				}
			}
		}
		
		this.orderDao.batchSaveSaleAtuoRefunEarnestFundsDao(params);
		this.orderDao.batchSaveBuyAtuoRefunEarnestFundsDao(params);
		this.orderDao.batchUpdateAtuoRefunEarnestStatusDao(params);//---OK
		this.orderDao.batchSaveAutoRefunEarnestStepDao(params);
		
	}
	
	@Transactional(rollbackFor={Exception.class})
	public Map<String, Object> refundService(Map<String, Object> params)throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> p = new HashMap<String, Object>();
		result.put("success", true);
		result.put("code", "1");

		String bills = (String)params.get("bills");
	    JSONArray jsonarrayBills = JSONArray.fromObject(bills);
		Object[] ObjBills = jsonarrayBills.toArray();

		String TYPE = (String)params.get("TYPE");
		String ORDER_ID = (String)params.get("ORDER_ID");
		
		p.put("ID", ORDER_ID);
		//0待付款 1付款中 2已付款 3待退款 4退款中 5已退款 6手动取消订单 7系统自动取消
		if(!CommonUtils.checkString(TYPE)){
			result.put("success", false);
			result.put("code", "-1");//数据不完整
		}
		String TITLE = "";
		String STATUS = String.valueOf(params.get("STATUS")), 
				IS_EARNEST = String.valueOf(params.get("IS_EARNEST"));
		if((STATUS.equals("2") && (IS_EARNEST.equals("2") || IS_EARNEST.equals("6") || IS_EARNEST.equals("7"))) 
				|| (STATUS.equals("0") && (IS_EARNEST.equals("5") || IS_EARNEST.equals("6") || IS_EARNEST.equals("7")))){
			TITLE = "申请定金退款";
			if(TYPE.equals("1")){
				p.put("IS_EARNEST", 6);
			}else if(TYPE.equals("2")){
				p.put("IS_EARNEST", 7);
				TITLE = "定金退款中";
			}else if(TYPE.equals("3")){
				//定金退款完成的订单状态需要修改为已退款状态,对账需要
				p.put("STATUS", 5);
				p.put("IS_EARNEST", 8);
				TITLE = "定金已退款";
			}
		}else{
			TITLE = "申请退款";
			if(TYPE.equals("1")){
				p.put("STATUS", 3);
			}else if(TYPE.equals("2")){
				p.put("STATUS", 4);
				TITLE = "退款中";
			}else if(TYPE.equals("3")){
				p.put("STATUS", 5);
				TITLE = "已退款";
			}
		}
		
		
		
		if((STATUS.equals("2") && (IS_EARNEST.equals("2") || IS_EARNEST.equals("6") || IS_EARNEST.equals("7"))) 
				|| (STATUS.equals("0") && (IS_EARNEST.equals("5") || IS_EARNEST.equals("6") || IS_EARNEST.equals("7")))){
			this.orderDao.updateOrderEarnestStatusByIdDao(p);
		}else{
			this.orderDao.updateDao(p);
		}
		
		if(!TYPE.equals("1")){
			double REFUND_AMOUNT = 0.0;
			String REMARKS = "";
			for(int i = 0; i < ObjBills.length; i++){
		    	JSONObject ObjBill = JSONObject.fromObject(ObjBills[i]);
		    	REFUND_AMOUNT = REFUND_AMOUNT + Double.parseDouble(String.valueOf(ObjBill.get("AMOUNT")));
		    	if(CommonUtils.checkString((String)ObjBill.get("REMARKS"))){
		    		REMARKS = REMARKS + "；" + (String)ObjBill.get("REMARKS");
		    	}
			}
			p.clear();
	    	p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", ORDER_ID);
			p.put("TITLE", TITLE);//-----------------------------
			p.put("STEP_USER", (String)params.get("CREATE_USER"));
			p.put("STEP_USER_ID", (String)params.get("CREATE_USER_ID"));
			
			p.put("REMARK", "退款金额："+REFUND_AMOUNT+REMARKS);//
			this.orderDao.saveOrderStepDao(p);
			
		}else{
			p.clear();
	    	p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", ORDER_ID);
			p.put("TITLE", TITLE);//-----------------------------
			p.put("STEP_USER", (String)params.get("CREATE_USER"));
			p.put("STEP_USER_ID", (String)params.get("CREATE_USER_ID"));
			p.put("REMARK", params.get("REMARK"));//
			this.orderDao.saveOrderStepDao(p);
		}
		
		
		p.put("ID", ORDER_ID);
		p.put("start", 1);
		p.put("end", 10);
		List<Map<String, Object>> orders = null;
		if(CommonUtils.checkString(params.get("REFUND_TYPE")) && (params.get("REFUND_TYPE").toString().equals("2") || params.get("REFUND_TYPE").toString().equals("3"))){
			if(params.get("REFUND_TYPE").toString().equals("3")){
				p.put("TYPE", "3");
			}
			orders = this.orderDao.listRouteOrderDao(p);
		}else{
			orders = this.orderDao.listDao(p);
		}
		
		if(TYPE.equals("3")){
			String ACCOUNT_COMPANY_ID = "";
			Map<String, Object> order = orders.get(0);	
			for(int i = 0; i < ObjBills.length; i++){
		    	JSONObject ObjBill = JSONObject.fromObject(ObjBills[i]);
		    	
				p.clear();
				p.put("orderId", ORDER_ID);
				p.put("PID", ObjBill.get("ID"));
				
				p.put("AMOUNT", ObjBill.get("AMOUNT"));
				p.put("REMARKS", ObjBill.get("REMARKS"));
				
				p.put("TYPE", 6);
				
				p.put("ACCOUNT_TYPE", ObjBill.get("ACCOUNT_TYPE"));
				p.put("ACCOUNT_COMPANY_ID", ObjBill.get("ACCOUNT_COMPANY_ID"));
				p.put("SITE_RELA_ID", ObjBill.get("SITE_RELA_ID"));
				p.put("ENTITY_TYPE", ObjBill.get("ENTITY_TYPE"));
				p.put("ENTITY_ID", ObjBill.get("ENTITY_ID"));
				
				p.put("COMPANY_ID", ObjBill.get("ACCOUNT_COMPANY_ID"));
				p.put("USER_ID", (String)params.get("CREATE_USER_ID"));
				p.put("USER_NAME", (String)params.get("CREATE_USER"));
				p.put("order", order);
				this.saveOrderFundsService(p);
				ACCOUNT_COMPANY_ID = (String)ObjBill.get("ACCOUNT_COMPANY_ID");
				
			}
			
			/**
			 * 如果订单状态为0,未付款状态,定金退款完成后,订单状态修改为-已退款.对账需要 
			 */
			if(STATUS.equals("0")){
				p.clear();
				p.put("ID", ORDER_ID);
				p.put("STATUS", 5);
				this.orderDao.updateOrderBaseStatusDao(p);
			}
			
			/**
			 * 定金不退优惠活动
			 */
			if((STATUS.equals("2") && (IS_EARNEST.equals("2") || IS_EARNEST.equals("6") || IS_EARNEST.equals("7"))) 
					|| (STATUS.equals("0") && (IS_EARNEST.equals("5") || IS_EARNEST.equals("6") || IS_EARNEST.equals("7")))){
				
			}else{
				this.refundsDiscount(ORDER_ID, params, orders.get(0), ACCOUNT_COMPANY_ID);
			}
		}
		
		return result;
	}
	
	public void refundsDiscount(String orderId, Map<String, Object> params, Map<String, Object> order, String ACCOUNT_COMPANY_ID) throws Exception{
		Map<String, Object> p = new HashMap<String, Object>();
		p.clear();
		p.put("ORDER_ID", orderId);
		p.put("TYPE", "12");
		List<Map<String, Object>> orderFundses = this.orderDao.listOrderFundsDao(p);
		/**
		 * 优惠活动退款
		 */
		if(CommonUtils.checkList(orderFundses)){
			Map<String, Object> orderFunds = orderFundses.get(0);
			String DISCOUNT_AMOUNT = String.valueOf(orderFunds.get("AMOUNT"));
			
			
			p.clear();
			p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", orderId);
			p.put("TYPE", 14);//平台
			p.put("AMOUNT", DISCOUNT_AMOUNT);//供应商需要支付给平台的营销费
			p.put("REMARKS", "");
			p.put("ACCOUNT_TYPE", 3);
			
			//站长的公司ID
			p.put("ACCOUNT_COMPANY_ID", ACCOUNT_COMPANY_ID);
			p.put("SITE_RELA_ID", (String)params.get("SITE_RELA_ID"));
//			p.put("ENTITY_TYPE", order.get("PRODUCE_TYPE"));
			p.put("CREATE_USER", (String)params.get("CREATE_USER"));
			p.put("CREATE_USER_ID", (String)params.get("CREATE_USER_ID"));
			this.orderDao.saveOrderFundsDao(p);
			
			
			p.clear();
			p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", orderId);
			p.put("TYPE", 13);
			p.put("AMOUNT", "-"+DISCOUNT_AMOUNT);
			p.put("REMARKS", "");
			p.put("ACCOUNT_TYPE", 2);
			p.put("ACCOUNT_COMPANY_ID", (String)order.get("CREATE_COMPANY_ID"));
			p.put("SITE_RELA_ID", (String)params.get("SITE_RELA_ID"));
			p.put("CREATE_USER", (String)params.get("CREATE_USER"));
			p.put("CREATE_USER_ID", (String)params.get("CREATE_USER_ID"));
			this.orderDao.saveOrderFundsDao(p);
			
		}
	}
	
	public List<Map<String, Object>> listOrderTrafficService(Map<String, Object> params){
		return this.orderDao.listOrderTrafficDao(params);
	}
	
	public List<Map<String, Object>> listOrderTrafficDetailService(Map<String, Object> params){
		return this.orderDao.listOrderTrafficDetailDao(params);
	}
	
	public List<Map<String, Object>> listRouteOrderService(Map<String, Object> params){
		return this.orderDao.listRouteOrderDao(params);
	}
	
	public int countRouteOrderService(Map<String, Object> params){
		return this.orderDao.countRouteOrderDao(params);
	}
	
	
	/**
	 * 支付定金
	 */
	private String payEarnest(Map<String, Object> p,Map<String, Object> params,Map<String, Object> order, double EARNEST_AMOUNT) throws Exception{
		p.clear();
		p.put("DEPART_ID", (String)params.get("DEPART_ID"));
		p.put("MONEY", EARNEST_AMOUNT);//同行价格-------------------------------------
		p.put("USER_ID", (String)order.get("CREATE_USER_ID"));
		p.put("NOTE", "定金支付成功");//备注----------------------------------------
		p.put("ORDER_NO", (String)order.get("NO"));
		String PRODUCE_TYPE = String.valueOf(order.get("PRODUCE_TYPE"));
		if(PRODUCE_TYPE.equals("3")){
			PRODUCE_TYPE = "2";
		}
		p.put("PRODUCE_TYPE", PRODUCE_TYPE);
		String status = ServiceProxyFactory.getProxy().balancePayService(p);
		return status;
	}
	
	/**
	 * 订单流程--需要重构
	 * */
	/**
	 * 账户
	 * @throws Exception 
	 * */
	private String balancePay(Map<String, Object> p,Map<String, Object> params,Map<String, Object> order) throws Exception{
		/**
		 *  DEPART_ID
		 *  MONEY
		 *  USER_ID
		 *  NOTE
		 *  ORDER_NO
		 *  PRODUCE_TYPE 产品类型 1线路2交通3酒店4景点5保险
		 */
		double INTER_AMOUNT = 0.0;
		if(order.get("DISCOUNT_TOTAL_AMOUNT") != null){
			INTER_AMOUNT = Double.parseDouble(String.valueOf(order.get("INTER_AMOUNT"))) - Double.parseDouble(String.valueOf(order.get("DISCOUNT_TOTAL_AMOUNT")));
		}else{
			INTER_AMOUNT = Double.parseDouble(String.valueOf(order.get("INTER_AMOUNT")));
		}
		
		/**
		 * 默认减去定金金额
		 * 定金订单 (订单尾款 = 总金额 - 定金金额)
		 * 全款订单 (订单尾款 = 总金额 - 定金金额[此时的定金金额为0])
		 */
		
		if(order.get("EARNEST_INTER_AMOUNT") != null){
			INTER_AMOUNT = INTER_AMOUNT - Double.parseDouble(String.valueOf(order.get("EARNEST_INTER_AMOUNT")));
		}
		
		p.clear();
		p.put("DEPART_ID", (String)params.get("DEPART_ID"));
		p.put("MONEY", INTER_AMOUNT);//同行价格-------------------------------------
		p.put("USER_ID", (String)order.get("CREATE_USER_ID"));
		p.put("NOTE", "");//备注----------------------------------------
		p.put("ORDER_NO", (String)order.get("NO"));
		String PRODUCE_TYPE = String.valueOf(order.get("PRODUCE_TYPE"));
		if(PRODUCE_TYPE.equals("3")){
			PRODUCE_TYPE = "2";
		}
		p.put("PRODUCE_TYPE", PRODUCE_TYPE);

		String status = ServiceProxyFactory.getProxy().balancePayService(p);
		if(!status.equals("1")){
//			throw new Exception("余额不足"){
//				
//			};
		}
		
		return status;
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
			p.put("PLATFORM_PAY", "1");
			this.orderDao.updateDao(p);
		}catch(Exception ex){
			throw new Exception("账户完成后 更新状态失败"){
				
			};
		}
		
	}
	
	private void orderStep(Map<String, Object> p,Map<String, Object> params,String ORDER_ID, String title) throws Exception{
		try{
			p.clear();
			p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", ORDER_ID);
			p.put("TITLE", title);//-----------------------------
			p.put("STEP_USER", (String)params.get("CREATE_USER"));
			p.put("STEP_USER_ID", (String)params.get("CREATE_USER_ID"));
			p.put("REMARK", "");//-----------------------------;
			this.orderDao.saveOrderStepDao(p);
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
			String isEarnest = (String)order.get("isEarnest");
			String IS_EARNEST = String.valueOf(order.get("IS_EARNEST"));
			if(IS_EARNEST.equals("1")){
				/**
				 * 订单尾款 外卖
				 * 订单总金额 - 订单定金总金额
				 */
				double amount = Double.parseDouble(String.valueOf(order.get("SALE_AMOUNT"))) - Double.parseDouble(String.valueOf(order.get("EARNEST_RETAIL_AMOUNT")));
				p.put("AMOUNT", amount);
			}else if((CommonUtils.checkString(isEarnest) && isEarnest.equals("1") && IS_EARNEST.equals("0")) || IS_EARNEST.equals("4")){
				p.put("AMOUNT", order.get("EARNEST_RETAIL_AMOUNT"));	
			}else{
				p.put("AMOUNT", order.get("SALE_AMOUNT"));
			}
			p.put("REMARKS", "");
			p.put("ACCOUNT_TYPE", 1);
			p.put("ACCOUNT_COMPANY_ID", (String)order.get("CREATE_COMPANY_ID"));
			p.put("SITE_RELA_ID", SITE_RELA_ID);
			p.put("CREATE_USER", (String)params.get("CREATE_USER"));
			p.put("CREATE_USER_ID", (String)params.get("CREATE_USER_ID"));
			
			this.orderDao.saveOrderFundsDao(p);

			double INTER_SINGLE_ROOM = Double.parseDouble(String.valueOf(order.get("INTER_SINGLE_ROOM")));
			double SINGLE_ROOM_CNT = Double.parseDouble(String.valueOf(order.get("SINGLE_ROOM_CNT")));
			double OTHER_AMOUNT = Double.parseDouble(String.valueOf(order.get("OTHER_AMOUNT")));
			double INTER_AMOUNT = Double.parseDouble(String.valueOf(order.get("INTER_AMOUNT")));
			p.clear();
			p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", ORDER_ID);
			p.put("TYPE", 10);
			if(IS_EARNEST.equals("1")){
				double amount = Double.parseDouble(String.valueOf(order.get("INTER_AMOUNT"))) - Double.parseDouble(String.valueOf(order.get("EARNEST_INTER_AMOUNT")));
				p.put("AMOUNT", "-"+amount);
			}else if((CommonUtils.checkString(isEarnest) && isEarnest.equals("1") && IS_EARNEST.equals("0")) || IS_EARNEST.equals("4")){
				p.put("AMOUNT", "-"+order.get("EARNEST_INTER_AMOUNT"));
			}else{
				p.put("AMOUNT", "-"+(INTER_AMOUNT - (INTER_SINGLE_ROOM * SINGLE_ROOM_CNT) - OTHER_AMOUNT));
			}
			p.put("REMARKS", "");
			p.put("ACCOUNT_TYPE", 1);
			p.put("ACCOUNT_COMPANY_ID", (String)order.get("CREATE_COMPANY_ID"));
			p.put("SITE_RELA_ID", SITE_RELA_ID);
			p.put("CREATE_USER", (String)params.get("CREATE_USER"));
			p.put("CREATE_USER_ID", (String)params.get("CREATE_USER_ID"));
			this.orderDao.saveOrderFundsDao(p);
		}catch(Exception ex){
			throw new Exception("卖家记账失败"){
				
			};
		}
	}
	
	/**
	 * 卖家 记账
	 * */
	private void buyStepFundsByTrafficOrder(Map<String, Object> p,Map<String, Object> params,Map<String, Object> order,String ORDER_ID,String SITE_RELA_ID) throws Exception{
		try{
			p.clear();
			p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", ORDER_ID);
			p.put("TYPE", 2);
			p.put("AMOUNT", order.get("INTER_AMOUNT"));
			p.put("REMARKS", "");
			p.put("ENTITY_ID", (String)order.get("PRODUCE_ID"));
			p.put("ENTITY_TYPE",  order.get("PRODUCE_TYPE"));
			p.put("ACCOUNT_TYPE", 2);
			p.put("ACCOUNT_COMPANY_ID", (String)order.get("COMPANY_ID"));
			p.put("SITE_RELA_ID", SITE_RELA_ID);
			
			p.put("CREATE_USER", (String)params.get("CREATE_USER"));
			p.put("CREATE_USER_ID", (String)params.get("CREATE_USER_ID"));
			this.orderDao.saveOrderFundsDao(p);
		}catch(Exception ex){
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
			List<Map<String, Object>> visitors = this.visitorOrderDao.listVisitorGroupByTypeDao(p);
			for (Map<String, Object> visitor : visitors) {
				String ATTR_ID = (String)visitor.get("ATTR_ID");
				String num = String.valueOf(visitor.get("NUM"));
				visitor_num.put(ATTR_ID, num);
			}
			
			/**
			 * 地接价格
			 * 
			 * 
			 * a.RETAIL_SINGLE_ROOM,
				a.INTER_SINGLE_ROOM,
				a.SINGLE_ROOM_CNT,
			 */
			double INTER_SINGLE_ROOM = Double.parseDouble(String.valueOf(order.get("INTER_SINGLE_ROOM")));
			double SINGLE_ROOM_CNT = Double.parseDouble(String.valueOf(order.get("SINGLE_ROOM_CNT")));
			
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
			double INTER_FLOAT = Double.parseDouble(String.valueOf(order.get("INTER_FLOAT")));
			p.put("ID", CommonUtils.uuid());
			p.put("TYPE", 2);
			
			String isEarnest = (String)order.get("isEarnest");
			String IS_EARNEST = String.valueOf(order.get("IS_EARNEST"));
			if(IS_EARNEST.equals("1")){
				double amount = Double.parseDouble(String.valueOf(order.get("INTER_AMOUNT"))) - Double.parseDouble(String.valueOf(order.get("EARNEST_INTER_AMOUNT")));
				p.put("AMOUNT", amount);
			}else if((CommonUtils.checkString(isEarnest) && isEarnest.equals("1") && IS_EARNEST.equals("0")) || IS_EARNEST.equals("4")){
				p.put("AMOUNT", order.get("EARNEST_INTER_AMOUNT"));
			}else{
				p.put("AMOUNT", sum_price+INTER_FLOAT);
			}
			
			
			p.put("REMARKS", "");
			p.put("ACCOUNT_TYPE", 2);
			p.put("ACCOUNT_COMPANY_ID", (String)order.get("COMPANY_ID"));
			p.put("SITE_RELA_ID", SITE_RELA_ID);
			p.put("ENTITY_TYPE", "2");
			p.put("CREATE_USER", (String)params.get("CREATE_USER"));
			p.put("CREATE_USER_ID", (String)params.get("CREATE_USER_ID"));
			this.orderDao.saveOrderFundsDao(p);
			
			/**
			 * 支付定金尾款 
			 * 定金支付 
			 * 不记录交通价格 
			 */
			if(IS_EARNEST.equals("1")){
				
			}else if((CommonUtils.checkString(isEarnest) && isEarnest.equals("1") && IS_EARNEST.equals("0")) || IS_EARNEST.equals("4")){
				
			}else{
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
			p.put("start", 1);
			p.put("end", 1);
			Map<String, Object> company = this.companyDao.listDao(p).get(0);
			
			/**
			 * 营销费 = 订单总价 * 营销费百分比
			 */
			double EXPENSE = 0;
			String PRODUCE_TYPE = String.valueOf(order.get("PRODUCE_TYPE"));
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
			double EARNEST_INTER_AMOUNT = Double.parseDouble(String.valueOf(order.get("EARNEST_INTER_AMOUNT")));
			
			p.clear();
			p.put("ID", CommonUtils.uuid());
			p.put("ORDER_ID", ORDER_ID);
			p.put("TYPE", 7);//平台
			String isEarnest = (String)order.get("isEarnest");
			String IS_EARNEST = String.valueOf(order.get("IS_EARNEST"));
			if(IS_EARNEST.equals("1")){
				double amount = EXPENSE * (INTER_AMOUNT - EARNEST_INTER_AMOUNT);
				p.put("AMOUNT", amount);//供应商需要支付给平台的营销费
			}else if((CommonUtils.checkString(isEarnest) && isEarnest.equals("1") && IS_EARNEST.equals("0")) || IS_EARNEST.equals("4")){
				p.put("AMOUNT", EXPENSE * Double.parseDouble(String.valueOf(order.get("EARNEST_INTER_AMOUNT"))));//供应商需要支付给平台的营销费
			}else{
				p.put("AMOUNT", EXPENSE * INTER_AMOUNT);//供应商需要支付给平台的营销费
			}
			
			p.put("REMARKS", "");
			p.put("ACCOUNT_TYPE", 3);
			
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
			if(IS_EARNEST.equals("1")){
				double amount = EXPENSE * (INTER_AMOUNT - EARNEST_INTER_AMOUNT);
				p.put("AMOUNT", 0-amount);//供应商需要支付给平台的营销费
			}else if((CommonUtils.checkString(isEarnest) && isEarnest.equals("1") && IS_EARNEST.equals("0")) || IS_EARNEST.equals("4")){
				p.put("AMOUNT", 0-(EXPENSE * Double.parseDouble(String.valueOf(order.get("EARNEST_INTER_AMOUNT")))));//供应商需要支付给平台的营销费
			}else{
				p.put("AMOUNT", (0-(EXPENSE * INTER_AMOUNT)));//供应商需要支付给平台的营销费
			}
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
	
	public List<Map<String, Object>> listRefundsService(Map<String, Object> params){
		return this.orderDao.listRefundsDao(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void saveContractLogService(Map<String, Object> params)throws Exception{
		Map<String, Object> _params = new HashMap<String, Object>();
		
		_params.put("ID", params.get("ORDER_ID"));
		_params.put("CON_NO", params.get("CON_NO"));
		this.orderDao.updateDao(_params);
		
		params.put("ID", CommonUtils.uuid());
		this.orderDao.saveContractLogDao(params);
	
	}
	
	public List<Map<String, Object>> listOrderService(Map<String, Object> params){
		return this.orderDao.listOrderDao(params);
	}
	
	public void saveOrderContractSerivce(Map<String, Object> params)throws Exception{
		this.orderDao.saveOrderContractDao(params);
	}
	
	public void updateOrderContractSerivce(Map<String, Object> params)throws Exception{
		this.orderDao.updateOrderContractDao(params);
	}
	
	public void saveOrderBankService(Map<String, Object> params)throws Exception{
		this.orderDao.saveOrderBankDao(params);
	}
	public void updateOrderBankService(Map<String, Object> params)throws Exception{
		this.orderDao.updateOrderBankDao(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void reSeatOnlineService(String orderId, String remark) throws Exception{
		this.orderDao.reSeatOnlineDao(orderId, remark);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void reSeatService() throws Exception{
		this.orderDao.reSeatDao();
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void reSeatYYService() throws Exception{
		this.orderDao.reSeatYYDao();
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void reSeatByEntityIdService(Map<String, Object> params) throws Exception{
		this.orderDao.reSeatByEntityIdDao(params);
	}
	
	public void updateOrderBaseService(Map<String, Object> params)throws Exception{
		this.orderDao.updateDao(params);
	}
	
	public void saveOrderStepService(Map<String, Object> params)throws Exception{
		this.orderDao.saveOrderStepDao(params);
	}
	
	public void updateAccountStatus(Map<String, Object> params, String ORDER_ID)throws Exception{
	
		params.clear();
		params.put("ID", ORDER_ID);
		params.put("start", 1);
		params.put("end", 1);
		Map<String, Object> data = this.listOrderService(params).get(0);
		
		Date CREATE_TIME = DateUtil.parseDate((String)data.get("CREATE_TIME"));
		String YYYYMM = DateUtil.parseDate(CREATE_TIME, "yyyy-MM");
		Date d = DateUtil.parseDate(YYYYMM+"-15");
		if(CREATE_TIME.compareTo(d) > 0){
			this.financeService.dateInterval(params, YYYYMM, "2");
		}else{
			this.financeService.dateInterval(params, YYYYMM, "1");
		}
		params.put("COMPANY_ID", data.get("COMPANY_ID"));
		
		List<Map<String, Object>> orderAccounts = null;
		if(data.get("IS_ALONE").toString().equals("0") && data.get("SOURCES").toString().equals("0")){
			params.put("ACCOUNT_TYPE", "0");
			orderAccounts = this.financeDao.listOrderAccountDao(params);
		}else{
			params.put("ACCOUNT_TYPE", "1");
			params.put("ACCOUNT_COMPANY_ID", data.get("CREATE_COMPANY_ID"));
			orderAccounts = this.financeDao.listOrderAccountDao(params);
		}
		
		if(CommonUtils.checkList(orderAccounts)){
			Map<String, Object> orderAccount = orderAccounts.get(0);
			String ACCOUNT_STATUS = String.valueOf(orderAccount.get("ACCOUNT_STATUS"));
			if(!ACCOUNT_STATUS.equals("2")){
				params.clear();
				params.put("ID", orderAccount.get("ID"));
				params.put("ACCOUNT_STATUS", "2");
				this.financeDao.updateOrderAccountDao(params);
			}
		}
		
	}
	
	public List<Map<String, Object>> searchOrderRouteTraffic(Map<String, Object> params){
		return this.orderDao.searchOrderRouteTraffic(params);
	}
}
