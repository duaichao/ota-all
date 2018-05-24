package cn.sd.dao.account;

import java.util.List;
import java.util.Map;

public interface IAccountDao {
	
	/**
	 * 账户
	 * */
	public List<Map<String, Object>> listDao(Map<String,Object> params);
	public int countDao(Map<String,Object> params);
	public Map<String,String> statDao(Map<String,Object> params);
	public String getAccountIdDao(String depart_id);
	public void createAccountDao(Map<String, Object> params) throws Exception;
	public void updateStateDao(Map<String, Object> params);
	
	/**
	 * 余额总额
	 * */
	public String yeStatDao(String DEPART_ID);
	/**
	 * 账户禁用状态
	 * */
	public String accountStateDao(String DEPART_ID);
	
	/**
	 * 余额支付
	 * */
	public void addBalancePayDao(Map<String,Object> params);
	public String isBalancePayDao(Map<String,Object> params);
	/**
	 * 订单退款
	 * */
	public void orderRefundDao(Map<String,Object> params);
	
	/**
	 * 在线充值
	 * */
	public void addOnLineRechargeDao(Map<String,Object> params);
	/**
	 * 手动充值
	 * */
	public void addLineRechargeDao(Map<String,Object> params);
	
	
	/**
	 * 退款
	 * */
	public void addRefundDao(Map<String,Object> params);
	public Map<String,Object> isRefundDao(Map<String,Object> params);
	
	/**
	 * 透支
	 * */
	public void addOverdraftDao(Map<String,Object> params);
	
	/**
	 * 还款
	 * */
	public void addOverdraftRefundDao(Map<String,Object> params);
	public String isOverdraftRefundDao(Map<String,Object> params);
	
	public String getCompanyIdDao(String DEPART_ID);
	
	public void saveAccountLogDao(Map<String,Object> params);
	public List<Map<String, Object>> listAccountLogDao(Map<String,Object> params);
	
	public void updateAccountLogDao(Map<String,Object> params);
	
	public void updateAccountLogStatusDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listAccountDetailDao(Map<String,Object> params);
	public double sumAccountDetailMoneyDao(Map<String,Object> params);
	
	
}
