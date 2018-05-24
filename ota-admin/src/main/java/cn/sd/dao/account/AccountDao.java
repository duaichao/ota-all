package cn.sd.dao.account;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;
import cn.sd.entity.b2b.ModuleEntity;

@Repository
@Service("accountDao")
@SuppressWarnings("all")
public class AccountDao extends BaseDaoImpl implements IAccountDao{
	
	public List<Map<String, Object>> listDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("account.listDao", params);
	}
	
	public int countDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("account.countDao", params);
	}
	
	public Map<String,String> statDao(Map<String,Object> params){
		return (Map<String,String>)this.getSqlMapClientTemplate().queryForObject("account.statDao", params);
	}

	@Override
	public String accountStateDao(String DEPART_ID) {
		// TODO Auto-generated method stub
		return (String)this.getSqlMapClientTemplate().queryForObject("account.accountStateDao", DEPART_ID);
	}
	
	@Override
	public String yeStatDao(String DEPART_ID) {
		// TODO Auto-generated method stub
		return (String)this.getSqlMapClientTemplate().queryForObject("account.yeStatDao", DEPART_ID);
	}

	@Override
	public void addBalancePayDao(Map<String, Object> params) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().insert("account.addBalancePayDao", params);
	}

	@Override
	public String isBalancePayDao(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return (String)this.getSqlMapClientTemplate().queryForObject("account.isBalancePayDao", params);
	}

	@Override
	public void addLineRechargeDao(Map<String, Object> params) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().insert("account.addLineRechargeDao", params);
	}

	@Override
	public void addRefundDao(Map<String, Object> params) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().insert("account.addRefundDao", params);
	}

	@Override
	public Map<String, Object> isRefundDao(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return (Map<String, Object>)this.getSqlMapClientTemplate().queryForObject("account.isRefundDao", params);
	}

	@Override
	public void addOverdraftDao(Map<String, Object> params) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().insert("account.addOverdraftDao", params);
	}

	@Override
	public void addOverdraftRefundDao(Map<String, Object> params) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().insert("account.addOverdraftRefundDao", params);
	}

	@Override
	public String isOverdraftRefundDao(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return (String)this.getSqlMapClientTemplate().queryForObject("account.isOverdraftRefundDao", params);
	}

	@Override
	public void addOnLineRechargeDao(Map<String, Object> params) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().insert("account.addOnLineRechargeDao", params);
	}

	@Override
	public void createAccountDao(Map<String, Object> params)  throws Exception {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().insert("account.createAccountDao", params);
	}

	@Override
	public String getAccountIdDao(String depart_id) {
		// TODO Auto-generated method stub
		return (String)this.getSqlMapClientTemplate().queryForObject("account.getAccountIdDao", depart_id);
	}

	@Override
	public void updateStateDao(Map<String, Object> params) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().update("account.updateStateDao", params);
	}
	
	@Override
	public void orderRefundDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("account.orderRefundDao", params);
	}
	
	@Override
	public String getCompanyIdDao(String DEPART_ID) {
		// TODO Auto-generated method stub
		return (String)this.getSqlMapClientTemplate().queryForObject("account.getCompanyIdDao", DEPART_ID);
	}

	public void saveAccountLogDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("account.saveAccountLogDao", params);
	}
	
	public List<Map<String, Object>> listAccountLogDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("account.listAccountLogDao", params);
	}
	
	public void updateAccountLogDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("account.updateAccountLogDao", params);
	}
	
	public void updateAccountLogStatusDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("account.updateAccountLogStatusDao", params);
	}
	public List<Map<String, Object>> listAccountDetailDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("account.listAccountDetailDao", params);
	}
	
	public double sumAccountDetailMoneyDao(Map<String,Object> params){
		return (Double)this.getSqlMapClientTemplate().queryForObject("account.sumAccountDetailMoneyDao", params);
	}
}
