package cn.sd.dao.order;

import java.util.List;
import java.util.Map;

public interface IOrderDao {
	
	public void delOrderDao(Map<String, Object> params);
	public void saveOrderContactDao(Map<String, Object> params);
	public void delOrderContactDao(Map<String, Object> params);
	public List<Map<String, Object>> listOrderContactDao(Map<String, Object> params);
	
	public void batchSaveAutoRefunEarnestStepDao(Map<String, Object> params);
	public List<Map<String, Object>> listRefundEarnestDao(Map<String, Object> params);
	public void batchSaveSaleAtuoRefunEarnestFundsDao(Map<String, Object> params);
	public void batchSaveBuyAtuoRefunEarnestFundsDao(Map<String, Object> params);
	public void batchUpdateAtuoRefunEarnestStatusDao(Map<String, Object> params);
	
	public void reSeatEarnestDao(Map<String, Object> params);
	
	public void batchResetEarnestTypeDao(Map<String, Object> params);
	public void batchSaveEarnestTypeStepDao(Map<String, Object> params);
	
	public void updateOrderBaseStatusDao(Map<String, Object> params);
	public void updateOrderEarnestStatusByIdDao(Map<String, Object> params);
	public void confirmInsteadPayDao(Map<String, Object> params);
	public void updateOrderEarnestInfoDao(Map<String, Object> params);
	public void updateOrderEarnestStatusDao(Map<String, Object> params);
	
	public void updateOrderBaseRemarkDao(Map<String, Object> params);
	public List<Map<String, Object>> searchOrderPriceDao(Map<String, Object> params);
	public List<Map<String, Object>> searchOrderPriceMainDao(Map<String, Object> params);
	
	public void updateStartConfirmByIdDao(Map<String, Object> params);
	public void saveCancelRenewOrderStepDao();
	public void batchRenewRestContractAgencyDao();
	public void cancelRenewOrderDao();
	
	public void updateRenewStatusDao(Map<String, Object> params);
	public void delOrderPriceDao(Map<String, Object> params);
	public void saveOrderRenewPriceDao(Map<String, Object> params);
	public void restContractAgencyDao(Map<String, Object> params);
	public void restOrderContractAgencyDao(Map<String, Object> params);
	
	public void morePayCheckDao(Map<String, Object> params);
	public void updateOrderInterAuditDao(Map<String, Object> params);
	public void updateOrderInterFloatTempDao(Map<String, Object> params);
	public void updateOrderInterFloatDao(Map<String, Object> params);
	
	public void updateOrderSaleFloatDao(Map<String, Object> params);
	
	public List<Map<String, Object>> listOrderOtherPriceDao(Map<String, Object> params);
	
	public void updateOrderOtherAuditDao(Map<String, Object> params);
	
	public void saveOrderOtherPriceDao(Map<String, Object> params);
	public void delOrderOtherPriceDao(Map<String, Object> params);
	
	public Map<String, Object> detailOrderDao(Map<String, Object> params);
	
	public String getOrderNoDao(String PREFIX) throws Exception;
	public String getPrefix(String company_id);
	
	public void updateOrderConNoDao(Map<String, Object> params);
	
	public void saveDao(Map<String, Object> params)throws Exception;
	public void updateDao(Map<String, Object> params)throws Exception;
	
	public void saveOrderPriceDao(Map<String, Object> params)throws Exception;
	
	public List<Map<String, Object>> listDao(Map<String, Object> params);
	public int countDao(Map<String, Object> params);
	
	public List<Map<String, Object>> listOrderPriceDao(Map<String, Object> params);
	public List<Map<String, Object>> listOrderStepDao(Map<String, Object> params);
	public void saveOrderStepDao(Map<String, Object> params)throws Exception;
	
	public List<Map<String, Object>> listOrderFundsDao(Map<String, Object> params);
	
	public void saveOrderFundsDao(Map<String, Object> params)throws Exception;
	
	public void saveOrderPriceMainDao(Map<String, Object> params)throws Exception;
	public void saveOrderTrafficDao(Map<String, Object> params)throws Exception;
	public void saveOrderTrafficDetailDao(Map<String, Object> params)throws Exception;
	
	public List<Map<String, Object>> listOrderTrafficDao(Map<String, Object> params);
	public List<Map<String, Object>> listOrderTrafficDetailDao(Map<String, Object> params);
	
	public List<Map<String, Object>> listRouteOrderDao(Map<String, Object> params);
	public int countRouteOrderDao(Map<String, Object> params);
	
	public List<Map<String, Object>> listVisitorGroupByTrafficAttrDao(Map<String, Object> params);
	
	public void delOrderTrafficDao(Map<String, Object> params)throws Exception;
	public void delOrderTrafficDetailDao(Map<String, Object> params)throws Exception;
	public void delOrderPriceMainDao(Map<String, Object> params)throws Exception;
	
	public List<Map<String, Object>> listRefundsDao(Map<String, Object> params);

	public void saveContractLogDao(Map<String, Object> params)throws Exception;
	
	public List<Map<String, Object>> listOrderDao(Map<String, Object> params);
	
	public void saveOrderContractDao(Map<String, Object> params)throws Exception;
	public void updateOrderContractDao(Map<String, Object> params)throws Exception;
	 
	public void saveOrderBankDao(Map<String, Object> params)throws Exception;
	public void updateOrderBankDao(Map<String, Object> params)throws Exception;
	
	public void reSeatOnlineDao(String orderId, String remark) throws Exception;
	public void reSeatDao() throws Exception;
	public void reSeatYYDao() throws Exception;
	
	
	public void reSeatByEntityIdDao(Map<String, Object> params) throws Exception;
	
	public List<Map<String, Object>> searchOrderRouteTraffic(Map<String, Object> params);
	
	public Map<String, Object> detailContractAgencyByNoDao(Map<String, Object> params);
	
}