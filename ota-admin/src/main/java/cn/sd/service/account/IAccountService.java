package cn.sd.service.account;

import java.util.List;
import java.util.Map;

public interface IAccountService {
	
	public List<Map<String, Object>> listService(Map<String,Object> params);
	public int countService(Map<String,Object> params);
	public Map<String,String> statService(Map<String,Object> params);
	public void updateStateService(Map<String, Object> params);
	/**
	 * 获得账户
	 * */
	public String createAccountService(Map<String,Object> params)  throws Exception;
	
	/**
	 * 余额总额
	 * */
	public String yeStatService(String DEPART_ID);
	
	/**
	 * 账户禁用状态
	 * */
	public String accountStateService(String DEPART_ID);
	
	/**
	 * 在线充值
	 * */
	public void addOnLineRechargeService(Map<String,Object> params) throws Exception;
	
	/**
	 * 手动充值
	 * */
	public void addLineRechargeService(Map<String,Object> params) throws Exception;
	
	/**
	 * 退款
	 * */
	public String refundService(Map<String,Object> params);
	
	/**
	 * 透支
	 * */
	public void addOverdraftService(Map<String,Object> params);
	
	/**
	 * 还款
	 * */
	public String overdraftRefundService(Map<String,Object> params);
	
	/**
	 * 余额付款
	 * */
	public String balancePayService(Map<String,Object> params);
	/**
	 * 订单退款
	 * */
	public String orderRefundService(Map<String,Object> params);
	
	public String getCompanyIdService(String DEPART_ID);
	
	public void saveAccountLogService(Map<String,Object> params)throws Exception;
	
	public List<Map<String, Object>> listAccountLogService(Map<String,Object> params);
	
	public void updateAccountLogService(Map<String,Object> params);
	
	public int onlineCZCallBackService(Map<String,Object> p, String order_no, String trade_no, int payWay, String total_fee) throws Exception ;
	
	public void updateAccountLogStatusService(Map<String,Object> params)throws Exception;
	
	public List<Map<String, Object>> listAccountDetailService(Map<String,Object> params);
	
	public double sumAccountDetailMoneyService(Map<String,Object> params);
}
