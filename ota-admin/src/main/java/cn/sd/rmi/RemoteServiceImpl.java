package cn.sd.rmi;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.CountDownSafe;
import cn.sd.core.util.DateUtil;
import cn.sd.core.util.lock.LockContainer;
import cn.sd.dao.order.IOrderDao;
import cn.sd.dao.produce.ITrafficDao;
import cn.sd.security.SecurityContext;
import cn.sd.service.account.IAccountService;
import cn.sd.web.Constants;

public class RemoteServiceImpl implements FacadeService { 

	private final static Object lock = new String("");
	
	private static volatile int reSeatlock=0;
	
	@Resource
	private IOrderDao trafficOrderDao;
	@Resource
	private ITrafficDao produceTrafficDao;
	@Resource
	private IAccountService accountService;

	
//	private static Map<Object, Object> s = new HashMap<Object, Object>();
//	private static Map<Object, Object> c = new HashMap<Object, Object>();
//	private static Map<Object, Object> u = new HashMap<Object, Object>();
//	private static Map<Object, Object> t = new HashMap<Object, Object>();
	
	public Map<Object, Object> getS() {
		return LoginInfo.getS();
	}

	/**
	 * sso---------------------------------------------------- 
     */
	public void saveLoginInfo(String uid, String userName, String sid, Map<String, Object> m, Map<String, Object> cookie){
		synchronized (LoginInfo.o) {
			String cv = String.valueOf(cookie.get("cv"));
			
			LoginInfo.getT().put(cv, sid);
			LoginInfo.getC().put(cv, cookie);
			LoginInfo.getU().put(cv, uid);
			LoginInfo.getS().put(cv, m);
			System.out.println("save---("+sid+"---sid)**("+uid+"---uid)***("+cookie.get("cc")+"---cid)***("+cookie.get("cv")+"---cv)***("+cookie.get("cn")+"---cn)***("+userName+"---un)");
			
			/**
			 * 重新登录,原登陆的信息还在远程服务器,应该先删掉,并重新保存一份记录
			 */
			this.removeLoginInfo(cv);
		}
		
	}
	
	/**
	 * 删除重复的登录信息
	 * @param cv
	 */
	public void removeLoginInfo(String cv){
			String sid = "", _uid = "", cid = "", _cv = "", cn = "", un = "";
			String uid = (String)LoginInfo.getU().get(cv);
			Map<String, String> r = new HashMap<String, String>();
			
			for (Map.Entry<Object, Object> _u : LoginInfo.getU().entrySet()) {
				if(_u.getValue().equals(uid)){
					if(!cv.equals(_u.getKey())){
						sid = (String)LoginInfo.getT().get(_u.getKey());
						_uid = (String)LoginInfo.getU().get(_u.getKey());
						Map<String, Object> _c = (Map<String, Object>)LoginInfo.getC().get(_u.getKey());
						cid =  String.valueOf(_c.get("cc"));
						cn = String.valueOf(_c.get("cn"));
						_cv = (String)_u.getKey();
						
						Map<String, Object> ss = (Map<String, Object>)LoginInfo.getS().get(_u.getKey());
						un = (String)((Map<String, Object>)ss.get(Constants.SESSION_USER_KEY)).get("USER_NAME");
						
						
						r.put((String)_u.getKey(), (String)_u.getValue());
						
						System.out.println("remove---("+sid+"---sid)**("+_uid+"---uid)***("+cid+"---cid)***("+_cv+"---cv)***("+cn+"---cn)***("+un+"---un)");
					}
						
				}
				
			}
			
			System.out.println("remove--begin");
			for (Map.Entry<String, String> _r : r.entrySet()) {
				System.out.println("-1111remove--begin1111-");
				LoginInfo.getC().remove(_r.getKey());
				LoginInfo.getU().remove(_r.getKey());
				LoginInfo.getS().remove(_r.getKey());
				LoginInfo.getT().remove(_r.getKey());
				
			}
			
			System.out.println("remove--end");
			r.clear();
	}
	
	/**
	 * 打印已登录用户信息
	 */
	public void printLoginInfo(){
		
		for (Map.Entry<Object, Object> e : LoginInfo.getU().entrySet()) {
			String uid = (String)e.getValue(),
					cv = (String)e.getKey(),
			cid = "", cn = "", un = "";
			for (Map.Entry<Object, Object> _c : LoginInfo.getC().entrySet()) {
				if(_c.getKey().equals(cv)){
					
					Map<String, Object> cookie = (Map<String, Object>)_c.getValue();
					cid = String.valueOf(cookie.get("cc"));
					cv = String.valueOf(cookie.get("cv"));
					cn = String.valueOf(cookie.get("cn"));
					
					break;
				}
			}

			Map<String, Object> m = (Map<String, Object>)LoginInfo.getS().get(cv);
			if(m.get(Constants.SESSION_USER_KEY) != null){
				un = (String)((Map<String, Object>)m.get(Constants.SESSION_USER_KEY)).get("USER_NAME");
			}
			String sid = (String)LoginInfo.getT().get(cv);
			System.out.println("print---("+sid+"---sid)**("+uid+"---uid)***("+cid+"---cid)***("+cv+"---cv)***("+cn+"---cn)***("+un+"---un)");
			
		}
	}
	
	/**
	 * 重置用户登录信息
	 * @param uid
	 */
	public static void clearLoginInfo(){
		System.out.println("-----------------------clearLoginInfo------------begin-----");
		LoginInfo.getC().clear();
		LoginInfo.getU().clear();
		LoginInfo.getS().clear();
		LoginInfo.getT().clear();
//		System.out.println("-----------------------clearLoginInfo------------end-----");
	}
	
	/**
	 * cookie在远程服务器是否存在
	 * @param cv COOKIE_VALUE
	 * @param sid SESSION_ID
	 * @return
	 */
	public Map<String, Object> existCookie(String cv, String sid){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("exist", false);
		for (Map.Entry<Object, Object> _c : LoginInfo.getC().entrySet()) {
			Map<String, Object> cookie = (Map<String, Object>)_c.getValue();
			String _cv = (String)cookie.get("cv");
			if(_cv.equals(cv)){
				
				result.put("exist", true);
				result.put("uid", LoginInfo.getU().get(_c.getKey()));
				result.put("s", LoginInfo.getS().get(_c.getKey()));
				
				return result;
			}
		}
		return result;
	}
	
	/**
	 * 删除登录的用户
	 * @param uid
	 */
	public void removeLoginInfobyUserId(String userId){
		synchronized (LoginInfo.o) {
			Map<String, String> r = new HashMap<String, String>();
			for (Map.Entry<Object, Object> _u : LoginInfo.getU().entrySet()) {
				if(_u != null && CommonUtils.checkString(_u.getValue()) && CommonUtils.checkString(userId) && _u.getValue().equals(userId)){
					r.put((String)_u.getKey(), (String)_u.getValue());
				}
			}
			
			for (Map.Entry<String, String> _r : r.entrySet()) {
				LoginInfo.getC().remove(_r.getKey());
				LoginInfo.getU().remove(_r.getKey());
				LoginInfo.getS().remove(_r.getKey());
				LoginInfo.getT().remove(_r.getKey());
				
			}
			r.clear();
		}
		
	}
	
	/**
	 * sso---------------------------------------------------- 
     */
	
	/**
	 * 余额付款
	 * DEPART_ID
	   MONEY
	   USER_ID
	   NOTE
	   ORDER_NO
	   PRODUCE_TYPE 产品类型 1线路2交通3酒店4景点5保险
	 * */
	public String balancePayService(Map<String,Object> params){
		return accountService.balancePayService(params);
	}
	
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
	public String orderRefundService(Map<String,Object> params){
		return accountService.orderRefundService(params);
	}
	
	/**
	 * 扣座位
	 * -1 占座失败
	 *  0 占座成功 
	 *  RULE_ID
	 *  SALE_DATE
	 *  SALE_SEAT
	 * */
	@Transactional(rollbackFor={Exception.class})
	public int usedSeat(Map<String, Object> params) throws Exception{
		String key =null;
		int cnt = 1;
		try{
			String RULE_ID = params.get("RULE_ID").toString();
			String SALE_DATE = params.get("SALE_DATE").toString();
			key = RULE_ID+SALE_DATE;
			
//			System.out.println(RULE_ID+":"+SALE_DATE+"=="+Thread.currentThread().getName()+"begin");
			while(true){
				
				boolean bl = LockContainer.dynamicLock.containsKey(key);
				if(bl == true){
					if(cnt == 5){
						return -1;
					}
					Thread.currentThread().sleep(150l);
					cnt=cnt+1;
//					System.out.println(RULE_ID+":"+SALE_DATE+"=1="+Thread.currentThread().getName()+"wark="+DateUtil.getNowTime());
				}else{
					LockContainer.dynamicLock.put(key, key);
					break;
				}
			}
//			System.out.println(RULE_ID+":"+SALE_DATE+"=2="+Thread.currentThread().getName()+"work="+DateUtil.getNowTime());
//			Thread.currentThread().sleep(10000l);
			String sql = this.produceTrafficDao.seatSql(RULE_ID);
			if(sql==null){
				LockContainer.dynamicLock.remove(key,key);
				return -1;
			}
			
			params.put("RULE_SQL", sql);
			int num = this.produceTrafficDao.isSeatNum(params);
			if(num == 0){
				LockContainer.dynamicLock.remove(key,key);
				return -1;
			}
			this.produceTrafficDao.saveTrafficSeat(params);
			LockContainer.dynamicLock.remove(key,key);
//			System.out.println(RULE_ID+":"+SALE_DATE+"=="+Thread.currentThread().getName()+"end="+DateUtil.getNowTime());
			return 0;
		}catch(Exception ex){
			ex.printStackTrace();
			LockContainer.dynamicLock.remove(key,key);
			throw ex;
		}
		

	}
	
	/**
	 * rq = 20140101 
	 * */
	@Transactional(rollbackFor={Exception.class})
	public int usedSeatByRouteOrder(String rq,String plan_id,String order_id,String sale_seat) throws Exception{
		
		List<String> keys = new ArrayList<String>();
		
		int stay_cnt = 0;
		int cnt_for = 0;
		Date dd = DateUtil.subDate(rq);
		Map trafficStayMap = null;
		try{
			List<Map<String, Object>> trafficStayList = this.produceTrafficDao.getPlanTraffic(plan_id);
			if(trafficStayList.size()==0) return -1;
			
			StringBuffer sb = new StringBuffer();
			for(Map<String, Object> map : trafficStayList){
				if(cnt_for==0){
					sb.append(" select '"+map.get("TRAFFIC_ID").toString()+"' as traffic_id, '"+rq.replaceAll("-", "")+"' as dt from dual");
					cnt_for = cnt_for + 1;
				}else{
					sb.append(" union all select '"+map.get("TRAFFIC_ID").toString()+"' as traffic_id, '"+DateUtil.getNowDateDAY(dd,stay_cnt).replaceAll("-", "")+"' as dt from dual");
				}
				stay_cnt = stay_cnt + Integer.valueOf(map.get("STAY_CNT").toString());
			}
			
			List<Map<String, Object>> ruleids = this.produceTrafficDao.getRuleIdByDate(sb.toString());
			if(ruleids.size()==0) return -1;
			
			
			for(Map<String, Object> map : ruleids){
				keys.add(map.get("RULE_ID").toString()+map.get("SALE_DATE").toString());
			}
			
			int cnt = 0;
			while(true){
				
				if(cnt == 5){
					return -1;
				}
				
				boolean flag = true;
				for(String key_temp : keys){
					boolean bl = LockContainer.dynamicLock.containsKey(key_temp);
					if(bl == true){
						flag =false;
						cnt=cnt+1;
						Thread.currentThread().sleep(150l);
						break;
					}
				}
				
				if(flag==true){
					for(String key_temp : keys){
						LockContainer.dynamicLock.put(key_temp, key_temp);
					}
					break;
				}
			}
			
			boolean flag = true;
			for(Map<String, Object> map : ruleids){
				map.put("SALE_SEAT", sale_seat);
				int num = this.produceTrafficDao.isSeatNum(map);
				if(num == 0){
					flag = false;
					break;
				}
				map.put("ENTITY_ID", order_id);
				map.put("TYPE", "1");
			}
			if(flag==false){
				for(String key_temp : keys){
					LockContainer.dynamicLock.remove(key_temp,key_temp);
				}
				return -1;
			}
			
			
			for(Map<String, Object> map : ruleids){
				this.produceTrafficDao.saveTrafficSeat(map);
			}
			for(String key_temp : keys){
				LockContainer.dynamicLock.remove(key_temp,key_temp);
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
			for(String key_temp : keys){
				LockContainer.dynamicLock.remove(key_temp,key_temp);
			}
			throw ex;
		}
		
		return 0;
	}
	
	/**
	 * 交通订单编号
	 * -1 供应商没有设置前缀
	 * */
	public String getTrafficOrderNoDao(String company_id) throws Exception{
		return getNoByMoudle(company_id,"A");
	}
	
	/**
	 * 线路编号
	 * -1 供应商没有设置前缀
	 * */
	public String getRouteNoDao(String company_id) throws Exception{
		return getNoByMoudle(company_id,"B");
	}
	
	/**
	 * 线路订单编号
	 * -1 供应商没有设置前缀
	 * */
	public String getRouteOrderNoDao(String company_id) throws Exception{
		return getNoByMoudle(company_id,"BA");
	}
	
	
	/**
	 * 在线充值订单编号
	 * */
	public String getOnLineNoDao(String module) throws Exception{
		return getNo(module,"CZ");
	}
	
	private String getNo(String company_id,String moudle) throws Exception{
		String prefix = company_id;
		int cnt = 1;
		try{
			
			prefix = prefix+moudle;
			while(true){
				boolean bl = LockContainer.dynamicLock.containsKey(prefix);
				if(bl == true){
					if(cnt == 5){
						return "-1";
					}
					Thread.currentThread().sleep(150l);
					cnt = cnt +1;
				}else{
					LockContainer.dynamicLock.put(prefix, prefix);
					break;
				}
			}
			
			String no = this.trafficOrderDao.getOrderNoDao(prefix);
			LockContainer.dynamicLock.remove(prefix,prefix);
			
			return no;
		}catch(Exception ex){
			ex.printStackTrace();
			LockContainer.dynamicLock.remove(prefix,prefix);
			throw ex;
		}
	}
	
	/**************************************************************************************/
	/**************************************************************************************/
	/**************************************************************************************/
	
	private String getNoByMoudle(String company_id,String moudle) throws Exception{
		String prefix = null;
		int cnt = 1;
		try{
			prefix = this.trafficOrderDao.getPrefix(company_id);
			if(prefix ==null) return "-1";
			
			prefix = prefix+moudle;
			while(true){
				boolean bl = LockContainer.dynamicLock.containsKey(prefix);
				if(bl == true){
					if(cnt == 5){
						return "-1";
					}
					Thread.currentThread().sleep(150l);
					cnt = cnt +1;
				}else{
					LockContainer.dynamicLock.put(prefix, prefix);
					break;
				}
			}
			
			String no = this.trafficOrderDao.getOrderNoDao(prefix);
			LockContainer.dynamicLock.remove(prefix,prefix);
			
			return no;
		}catch(Exception ex){
			ex.printStackTrace();
			LockContainer.dynamicLock.remove(prefix,prefix);
			throw ex;
		}
	}

	@Override
	public String overdraftRefundService(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return this.accountService.overdraftRefundService(params);
	}

	@Override
	public String refundService(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return this.accountService.refundService(params);
	}
	
	public String lockContainerInfo(){
		return LockContainer.toPrint();
	}
	
	public String countDownSafeInfo(){
		return CountDownSafe.toPrint();
	}
	
	public String securityContextInfo(){
		return SecurityContext.printStatus();
	}
	
	public void accountThreadClear(){
		LockContainer.clear();
	}
	
	public void hr(){
	}
	
	
	
	public Object getLock() {
		return lock;
	}
	
	/**
	 * return int
	 * -1 没有获取锁
	 * 1  获取锁
	 * */
	public int getLock(String useKey) throws InterruptedException{
		String key = useKey;
		int cnt = 1;
		
		while(true){
			
			boolean bl = LockContainer.dynamicLock.containsKey(key);
			if(bl == true){
				if(cnt == 5){
					return -1;
				}
				Thread.currentThread().sleep(150l);
				cnt=cnt+1;
//				System.out.println(RULE_ID+":"+SALE_DATE+"=1="+Thread.currentThread().getName()+"wark="+DateUtil.getNowTime());
			}else{
				LockContainer.dynamicLock.put(key, key);
				break;
			}
		}
		
		return 1;
	}
	
	public void removeLock(String useKey){
		LockContainer.dynamicLock.remove(useKey,useKey);
	}

	public synchronized int getReSeatlock() {
		return reSeatlock;
	}

	public synchronized void setReSeatlock(int reSeatlock) {
		RemoteServiceImpl.reSeatlock = reSeatlock;
	}
	
	

}