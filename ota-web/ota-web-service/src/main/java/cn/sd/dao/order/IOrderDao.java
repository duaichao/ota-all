package cn.sd.dao.order;

import java.util.List;
import java.util.Map;

import cn.sd.core.dao.IBaseDao;

public interface IOrderDao extends IBaseDao {
	
	public void updateOrderAccountByIDS(Map<String, Object> params);
	public List<Map<String, Object>> searchOrderAccountPayFinish(Map<String, Object> params);
	public List<Map<String, Object>> searchOrderAccount(Map<String, Object> params);
	public void saveOrderContact(Map<String, Object> params);
	public List<Map<String, Object>> listOrderContact(Map<String, Object> params);
	
	public void saveOrderPrice(Map<String, Object> params);
	public void saveOrderPriceMain(Map<String, Object> params);
	public void saveOrderTraffic(Map<String, Object> params);
	public void saveOrderTrafficDetail(Map<String, Object> params);
	public void saveOrder(Map<String, Object> params);
	public void saveOrderStep(Map<String, Object> params);
	
	public List<Map<String, Object>> searchOrderBase(Map<String, Object> params);
	public int countOrderBase(Map<String, Object> params);
	
	public void orderLost(Map<String, Object> params);
	
	public void reSeatByEntityIdDao(Map<String, Object> params) throws Exception;
	
	public void updateOrderStatus(Map<String, Object> params);
	public void saveOrderFundsDao(Map<String, Object> params);
	public List<Map<String, Object>> listOrderPriceDao(Map<String, Object> params);
	public List<Map<String, Object>> listVisitorGroupByTrafficAttrDao(Map<String, Object> params);
	
	public List<Map<String, Object>> listDiscountOrderDao(Map<String, Object> params);
	public List<Map<String, Object>> listDiscountProductDao(Map<String, Object> params);
	public void updateDiscountOrderDao(Map<String, Object> params);
	public void delDiscountOrderDao(Map<String, Object> params);
	
	public void orderCancel(Map<String, Object> params);
	
	public List<Map<String, Object>> searchOrderRouteTraffic(Map<String, Object> params);
}
