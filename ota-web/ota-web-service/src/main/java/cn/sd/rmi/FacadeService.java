package cn.sd.rmi;

import java.util.Map;

public interface FacadeService {
	/**
	 * 交通订单编号
	 * -1 供应商没有设置前缀
	 * */
	public String getTrafficOrderNoDao(String company_id) throws Exception;
	
	/**
	 * 线路编号
	 * -1 供应商没有设置前缀
	 * */
	public String getRouteNoDao(String company_id) throws Exception;
	
	/**
	 * 线路订单编号
	 * -1 供应商没有设置前缀
	 * */
	public String getRouteOrderNoDao(String company_id) throws Exception;
	
	/**
	 * 在线充值订单编号
	 * */
	public String getOnLineNoDao(String module) throws Exception;
	
	/**
	 * 扣座位
	 * -1 占座失败
	 *  0 占座成功 
	 * */
	public int usedSeat(Map<String, Object> params) throws Exception;
	public int usedSeatByRouteOrder(String rq, String plan_id, String order_id, String sale_seat) throws Exception;
	
	/**
	 * 余额付款
	 * DEPART_ID
	   MONEY
	   USER_ID
	   NOTE
	   ORDER_NO
	   PRODUCE_TYPE 产品类型 1线路2交通3酒店4景点5保险
	 * */
	public String balancePayService(Map<String, Object> params);
	
	/**
	 * 订单退款 
	 * DEPART_ID
	   MONEY
	   USER_ID
	   NOTE 订单退款+订单编号
	   ORDER_NO
	   PRODUCE_TYPE 产品类型 1线路2交通3酒店4景点5保险
	   return : "-1" 失败
	 * */
	public String orderRefundService(Map<String, Object> params);
	
	/**
	 * 还款 
	 * */
	public String overdraftRefundService(Map<String, Object> params);
	
	/**
	 * 手动充值 退款 
	 * */
	public String refundService(Map<String, Object> params);
	
	/**
	 * threadInfo
	 * */
	public String lockContainerInfo();
	public String countDownSafeInfo();
	public String securityContextInfo();
	public void accountThreadClear();
	public void hr();
	
	
	/**
	 * sso
	 * */
	public boolean isExist(String token);
	public String getToken(String token);
	public Map<String,Object> getUser(String userid);
	public Object getCity(String userid);
	public void setToken(String token, String userid, Map<String, Object> user, Object city);
	public void removeByToken(String token);
	public void removeByUseId(String userId);
	public String serviceInfo();
	public Map<String,Map<String,Object>> getOnLineUsers();
	
	/**
	 * 订单对账完成状态同步锁
	 * */
	public Object getLock();
	
	/**
	 * return int
	 * -1 没有获取锁
	 * 1  获取锁
	 * */
	public int getLock(String useKey) throws InterruptedException;
	public void removeLock(String useKey);
	
	public  int getReSeatlock();
	public  void setReSeatlock(int reSeatlock);
}
