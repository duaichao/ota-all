package cn.sd.service.order;

import java.util.List;
import java.util.Map;


public interface IOrderService {
	
	public void delOrderService(Map<String, Object> params);
	
	public void saveOrderContactService(Map<String, Object> params);
	public List<Map<String, Object>> listOrderContactService(Map<String, Object> params);
	
	public void autoRefundEarnestService(Map<String, Object> params)throws Exception;
	public void autoRefunEarnest()throws Exception;
	
	public void reSeatEarnestService()throws Exception;
	
	public void batchResetEarnestTypeService(Map<String, Object> params)throws Exception;
	public void confirmInsteadPayService(Map<String, Object> params)throws Exception;
	
	public void updateOrderEarnestInfoService(Map<String, Object> params)throws Exception;
	public void updateOrderEarnestStatusService(Map<String, Object> params)throws Exception;
	
	public void updateOrderBaseRemarkService(Map<String, Object> params)throws Exception;
	
	public List<Map<String, Object>> searchOrderPriceService(Map<String, Object> params);
	public List<Map<String, Object>> searchOrderPriceMainService(Map<String, Object> params);
	
	public void updateStartConfirmByIdService(Map<String, Object> params)throws Exception;
	
	public void cancelRenewOrderService()throws Exception;
	
	public void updateRenewStatusService(Map<String, Object> params)throws Exception;
	
	public void morePayCheckService(Map<String, Object> params)throws Exception;
	
	public void updateOrderInterFloatTempService(Map<String, Object> params)throws Exception;
	
	public void updateOrderInterFloatService(Map<String, Object> params)throws Exception;
	
	public void updateOrderSaleFloatService(Map<String, Object> params)throws Exception;
	
	public List<Map<String, Object>> listOrderOtherPriceService(Map<String, Object> params);
	
	public void updateOrderOtherAuditService(Map<String, Object> params)throws Exception;
	
	public void saveOrderOtherPriceService(Map<String, Object> params)throws Exception;
	public void delOrderOtherPriceService(Map<String, Object> params)throws Exception;
	
	public Map<String, Object> detailOrderService(Map<String, Object> params);
	
	public Map<String, Object> detailContractAgencyByNoService(Map<String, Object> params);
	
	public Map<String, Object> saveContractService(Map<String, Object> params)throws Exception;
	public Map<String, Object> saveService(Map<String, Object> params)throws Exception;
	public Map<String, Object> payService(Map<String, Object> params)throws Exception;
	public Map<String, Object> updateService(Map<String, Object> params)throws Exception;
	public Map<String, Object> cancelOrder(Map<String, Object> params)throws Exception;
	
	public List<Map<String, Object>> listService(Map<String, Object> params);
	public int countService(Map<String, Object> params);
	
	public List<Map<String, Object>> listOrderPriceService(Map<String, Object> params);
	public List<Map<String, Object>> listOrderStepService(Map<String, Object> params);
	public List<Map<String, Object>> listOrderFundsService(Map<String, Object> params);
	public Map<String, Object> saveOrderFundsService(Map<String, Object> params)throws Exception;
	public Map<String, Object> refundService(Map<String, Object> params)throws Exception;
	
	public Map<String, Object> saveRouteOrderRenewService(Map<String, Object> params)throws Exception;
	public Map<String, Object> updateRouteOrderRenewService(Map<String, Object> params)throws Exception;
	
	public Map<String, Object> saveRouteOrderService(Map<String, Object> params)throws Exception;
	public Map<String, Object> updateRouteOrderService(Map<String, Object> params)throws Exception;
	
//	public Map<String, Object> saveOrderPriceMainService(Map<String, Object> params);
//	public void saveOrderTrafficService(Map<String, Object> params)throws Exception;
//	public void saveOrderTrafficDetailService(Map<String, Object> params)throws Exception;
	
	public List<Map<String, Object>> listOrderTrafficService(Map<String, Object> params);
	public List<Map<String, Object>> listOrderTrafficDetailService(Map<String, Object> params);
	
	public List<Map<String, Object>> listRouteOrderService(Map<String, Object> params);
	public int countRouteOrderService(Map<String, Object> params);
	
	public List<Map<String, Object>> listRefundsService(Map<String, Object> params);
	
	public void saveContractLogService(Map<String, Object> params)throws Exception;
	
	public List<Map<String, Object>> listOrderService(Map<String, Object> params);
	
	
	public void saveOrderContractSerivce(Map<String, Object> params)throws Exception;
	
	public void updateOrderContractSerivce(Map<String, Object> params)throws Exception;
	
	public void saveOrderBankService(Map<String, Object> params)throws Exception;
	public void updateOrderBankService(Map<String, Object> params)throws Exception;
	
	public Map<String, Object> onlineSeat(Map<String, Object> params)throws Exception;
	
	public int onlinePayCallBackService(Map<String, Object> params);
	
	public void reSeatOnlineService(String orderId, String remark) throws Exception;
	public void reSeatService() throws Exception;
	public void reSeatYYService() throws Exception;
	
	public void reSeatByEntityIdService(Map<String, Object> params) throws Exception;
	
	public void updateOrderBaseService(Map<String, Object> params)throws Exception;
	
	public void saveOrderStepService(Map<String, Object> params)throws Exception;
	
	public int editLostService(Map<String, Object> params)throws Exception;
	
	public void updateAccountStatus(Map<String, Object> params, String ORDER_ID)throws Exception;
	
	public double saveDiscountInfo(String DISCOUNT_ID, String PLATFROM, String PAY_WAY, String ORDER_ID, double INTER_AMOUNT, String PRO_ID, String SITE_COMPANY_ID, String SITE_RELA_ID, Map<String, Object> p, Map<String, Object> order) throws Exception;
	
	public List<Map<String, Object>> searchOrderRouteTraffic(Map<String, Object> params);
}
