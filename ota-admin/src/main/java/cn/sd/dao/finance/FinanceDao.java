package cn.sd.dao.finance;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;

@Repository
@Service("financeDao")
public class FinanceDao extends BaseDaoImpl implements IFinanceDao {

	public List<Map<String, Object>> listCalcDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("finance.listCalcDao", params);
	}
	
	public List<Map<String, Object>> supplyCalcDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("finance.supplyCalcDao", params);
	}
	public List<Map<String, Object>> managerCalcDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("finance.managerCalcDao", params);
	}
	public List<Map<String, Object>> saleCalcDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("finance.saleCalcDao", params);
	}
	
	public void updateOrderAccountStatusDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("finance.updateOrderAccountStatusDao", params);
	}
	public void updateOrderAccountInfoDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("finance.updateOrderAccountInfoDao", params);
	}
	public void saveOrderAccountDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("finance.saveOrderAccountDao", params);
	}
	public void updateOrderAccountDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("finance.updateOrderAccountDao", params);
	}
	public void saveOrderAccountDetailDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("finance.saveOrderAccountDetailDao", params);
	}
	public void saveOrderAccountDetailNODao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("finance.saveOrderAccountDetailNODao", params);
	}
	public int countOrderUnCalcDao(Map<String, Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("finance.countOrderUnCalcDao", params);
	}
	
	public List<Map<String, Object>> listOrderAccountDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("finance.listOrderAccountDao", params);
	}
	
	public int countOrderAccountDao(Map<String, Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("finance.countOrderAccountDao", params);
	}
	
	public List<Map<String, Object>> listOrderAccountSynDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("finance.listOrderAccountSynDao", params);
	}
	public void saveOrderAccountSynDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().insert("finance.saveOrderAccountSynDao", params);
	}
	public void delOrderAccountSynByUserIdDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().delete("finance.delOrderAccountSynByUserIdDao", params);
	}
	
	public List<Map<String, Object>> listOrderAccountDetailDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("finance.listOrderAccountDetailDao", params);
	}
	
	public void updateAccountDetailPayStatusDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("finance.updateAccountDetailPayStatusDao", params);
	}
	
	public List<Map<String, Object>> listOrderAccountDetailPayFinishDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("finance.listOrderAccountDetailPayFinishDao", params);
	}
	
	public List<Map<String, Object>> listOrderAccountPayFinishDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("finance.listOrderAccountPayFinishDao", params);
	}
	
	public void updateOrderBaseAccountStatusByAccountIdAndNoOao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("finance.updateOrderBaseAccountStatusByAccountIdAndNoOao", params);
	}
	
	public void updateOrderAccountByIDSDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("finance.updateOrderAccountByIDSDao", params);
	}
	
	public void updateOrderAccountDetailAttrDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("finance.updateOrderAccountDetailAttrDao", params);
	}
	
	public List<Map<String, Object>> listOrderAccountHistoryDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("finance.listOrderAccountHistoryDao", params);
	}
	
	public List<Map<String, Object>> listAccountInfoDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("finance.listAccountInfoDao", params);
	}
	
	public List<Map<String, Object>> listSaleOrderAccountHistoryDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("finance.listSaleOrderAccountHistoryDao", params);
	}
	
	public List<Map<String, Object>> listAloneOrderDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("finance.listAloneOrderDao", params);
	}
	
	public List<Map<String, Object>> listRechargeDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("finance.listRechargeDao", params);
	}
	
	public void saveCompanyPayDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().insert("finance.saveCompanyPayDao", params);
	}
	
	public void updateOrderCompanyPayStatusDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("finance.updateOrderCompanyPayStatusDao", params);
	}
	
	public void updateRechargeCompanyPayStatusDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("finance.updateRechargeCompanyPayStatusDao", params);
	}
	
	public List<Map<String, Object>> listParentUnaccountCompanyDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("finance.listParentUnaccountCompanyDao", params);
	}
	
	public int countParentUnaccountCompanyDao(Map<String, Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("finance.countParentUnaccountCompanyDao", params);
	}
	
	public List<Map<String, Object>> listUnaccountCompanyDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("finance.listUnaccountCompanyDao", params);
	}
	public int countUnaccountCompanyDao(Map<String, Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("finance.countUnaccountCompanyDao", params);
	}
}
