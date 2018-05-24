package cn.sd.dao.finance;

import java.util.List;
import java.util.Map;

public interface IFinanceDao {

	public List<Map<String, Object>> listCalcDao(Map<String, Object> params);
	
	public List<Map<String, Object>> supplyCalcDao(Map<String, Object> params);
	public List<Map<String, Object>> managerCalcDao(Map<String, Object> params);
	public List<Map<String, Object>> saleCalcDao(Map<String, Object> params);
	
	public void updateOrderAccountStatusDao(Map<String, Object> params);
	
	public void updateOrderAccountInfoDao(Map<String, Object> params);
	public void saveOrderAccountDao(Map<String, Object> params);
	public void updateOrderAccountDao(Map<String, Object> params);
	public void saveOrderAccountDetailDao(Map<String, Object> params);
	public void saveOrderAccountDetailNODao(Map<String, Object> params);
	
	public int countOrderUnCalcDao(Map<String, Object> params);
	
	public List<Map<String, Object>> listOrderAccountDao(Map<String, Object> params);
	public int countOrderAccountDao(Map<String, Object> params);
	
	public List<Map<String, Object>> listOrderAccountSynDao(Map<String, Object> params);
	public void saveOrderAccountSynDao(Map<String, Object> params);
	public void delOrderAccountSynByUserIdDao(Map<String, Object> params);
	
	public List<Map<String, Object>> listOrderAccountDetailDao(Map<String, Object> params);
	
	public void updateAccountDetailPayStatusDao(Map<String, Object> params);
	
	public List<Map<String, Object>> listOrderAccountDetailPayFinishDao(Map<String, Object> params);
	
	public List<Map<String, Object>> listOrderAccountPayFinishDao(Map<String, Object> params);
	
	public void updateOrderBaseAccountStatusByAccountIdAndNoOao(Map<String, Object> params);
	
	public void updateOrderAccountByIDSDao(Map<String, Object> params);

	public void updateOrderAccountDetailAttrDao(Map<String, Object> params);
	
	public List<Map<String, Object>> listOrderAccountHistoryDao(Map<String, Object> params);
	
	public List<Map<String, Object>> listAccountInfoDao(Map<String, Object> params);
	
	public List<Map<String, Object>> listSaleOrderAccountHistoryDao(Map<String, Object> params);
	
	public List<Map<String, Object>> listAloneOrderDao(Map<String, Object> params);
	
	public List<Map<String, Object>> listRechargeDao(Map<String, Object> params);
	
	public void saveCompanyPayDao(Map<String, Object> params);
	
	public void updateOrderCompanyPayStatusDao(Map<String, Object> params);
	
	public void updateRechargeCompanyPayStatusDao(Map<String, Object> params);

	
	public List<Map<String, Object>> listParentUnaccountCompanyDao(Map<String, Object> params);
	public int countParentUnaccountCompanyDao(Map<String, Object> params);
	public List<Map<String, Object>> listUnaccountCompanyDao(Map<String, Object> params);
	public int countUnaccountCompanyDao(Map<String, Object> params);
	
	
}
