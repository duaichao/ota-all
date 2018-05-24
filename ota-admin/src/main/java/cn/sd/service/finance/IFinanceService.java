package cn.sd.service.finance;

import java.util.List;
import java.util.Map;

public interface IFinanceService {

	public List<Map<String, Object>> listCalcService(Map<String, Object> params);
	
	public List<Map<String, Object>> supplyCalcService(Map<String, Object> params);
	public List<Map<String, Object>> managerCalcService(Map<String, Object> params);
	public List<Map<String, Object>> saleCalcService(Map<String, Object> params);
	
	public void updateOrderAccountStatusService(Map<String, Object> params) throws Exception;
	
	public void updateOrderAccountInfoService(Map<String, Object> params)throws Exception;
	public void saveOrderAccountService(Map<String, Object> params)throws Exception;
	public void updateOrderAccountService(Map<String, Object> params)throws Exception;
	public void saveOrderAccountDetailNOService(Map<String, Object> params)throws Exception;
	
	public Map<String, Object> confirmOrderAccountService(Map<String, Object> params)throws Exception;
	
	public Map<String, Object> dateInterval(Map<String, Object> params, String beginMonth, String beginType);
	
	public List<Map<String, Object>> listOrderAccountService(Map<String, Object> params);
	public int countOrderAccountSerivce(Map<String, Object> params);
	
	
	public List<Map<String, Object>> listOrderAccountSynService(Map<String, Object> params);
	public void saveOrderAccountSynService(Map<String, Object> params) throws Exception;
	public void delOrderAccountSynByUserIdService(Map<String, Object> params) throws Exception;
	
	public List<Map<String, Object>> listOrderAccountDetailService(Map<String, Object> params);
	
	public void updateAccountDetailPayStatusService(Map<String, Object> params) throws Exception;

	public void updateAccountStatusService(Map<String, Object> params) throws Exception;

	public void updateOrderBaseAccountStatusService(Map<String, Object> params) throws Exception;
	
	public void updateOrderAccountDetailAttrService(Map<String, Object> params) throws Exception;
	
	public void editOrderAccountService(Map<String, Object> params)throws Exception;
	
	public List<Map<String, Object>> listOrderAccountHistoryService(Map<String, Object> params);
	
	public List<Map<String, Object>> listAccountInfoService(Map<String, Object> params);
	
	public List<Map<String, Object>> listSaleOrderAccountHistoryService(Map<String, Object> params);
	
	public List<Map<String, Object>> listAloneOrderService(Map<String, Object> params);
	
	public List<Map<String, Object>> listRechargeService(Map<String, Object> params);
	
	public void saveCompanyPayService(Map<String, Object> params);
	
	public List<Map<String, Object>> listParentUnaccountCompanyService(Map<String, Object> params);
	public int countParentUnaccountCompanyService(Map<String, Object> params);
	
	public List<Map<String, Object>> listUnaccountCompanyService(Map<String, Object> params);
	public int countUnaccountCompanyService(Map<String, Object> params);
}
