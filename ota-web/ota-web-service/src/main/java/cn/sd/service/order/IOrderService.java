package cn.sd.service.order;

import java.util.List;
import java.util.Map;


public interface IOrderService {
	public void editOrderAccountService(Map<String, Object> params)throws Exception;
	public void minishopOrderCancel(Map<String, Object> params) throws Exception;
	
	public void orderCancel(Map<String, Object> params) throws Exception;
	public List<Map<String, Object>> listOrderContact(Map<String, Object> params);
	
	public Map<String, Object> saveOrder(Map<String, Object> params) throws Exception;
	
	public List<Map<String, Object>> searchOrderBase(Map<String, Object> params);
	public int countOrderBase(Map<String, Object> params);
	
	public void orderLost(Map<String, Object> params)throws Exception;
	
	public void saveOrderStep(Map<String, Object> params)throws Exception;
	
	public void reSeatByEntityIdService(Map<String, Object> params)throws Exception;
	
	public int onlinePayCallBackService(Map<String, Object> params);
	public List<Map<String, Object>> listDiscountOrderService(Map<String, Object> params);
	
	public double saveDiscountInfo(String DISCOUNT_ID, String PLATFROM, String PAY_WAY, String ORDER_ID, double INTER_AMOUNT, String PRO_ID, String SITE_COMPANY_ID, String SITE_RELA_ID, Map<String, Object> p, Map<String, Object> order) throws Exception;
	
	public Map<String, Object> onlineSeat(Map<String, Object> params)throws Exception;
	
	public void updateOrderStatus(Map<String, Object> params);
	
	public List<Map<String, Object>> searchOrderRouteTraffic(Map<String, Object> params);
}
