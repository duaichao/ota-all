package cn.sd.rmi;

import java.util.Map;

public interface FacadeService {
	
	
	public Map<Object, Object> getS();
	
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
	public int usedSeat(Map<String,Object> params) throws Exception;
	public int usedSeatByRouteOrder(String rq,String plan_id,String order_id,String sale_seat) throws Exception;
	
	/**
	 * 余额付款
	 * DEPART_ID
	   MONEY
	   USER_ID
	   NOTE
	   ORDER_NO
	   PRODUCE_TYPE 产品类型 1线路2交通3酒店4景点5保险
	 * */
	public String balancePayService(Map<String,Object> params);
	
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
	public String orderRefundService(Map<String,Object> params);
	
	/**
	 * 还款 
	 * */
	public String overdraftRefundService(Map<String,Object> params);
	
	/**
	 * 手动充值 退款 
	 * */
	public String refundService(Map<String,Object> params);
	
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
	/**
	 * 用户登陆时,保存登陆信息到远程服务器,用户多服务器之间用户信息同步
	 * @param r 
	 * @param c 
	 */
	public void saveLoginInfo(String uid, String userName, String sid, Map<String, Object> m, Map<String, Object> cookie);
	/**
	 * 删除重复的登录信息
	 * @param cv COOKIE_VALUE
	 */
	public void removeLoginInfo(String cv);
	/**
	 * 打印已登录用户UID
	 */
	public void printLoginInfo();
	
	/**
	 * cookie在远程服务器是否存在
	 * @param cv COOKIE_VALUE
	 * @param sid SESSION_ID
	 * @return
	 */
	public Map<String, Object> existCookie(String cv, String sid);
	
	/**
	 * 删除登录的用户
	 * @param uid
	 */
	public void removeLoginInfobyUserId(String userId);
	
	
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
