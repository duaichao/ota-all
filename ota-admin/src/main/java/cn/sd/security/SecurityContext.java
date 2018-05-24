package cn.sd.security;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.sd.core.filter.LockContext;
import cn.sd.core.util.DateUtil;
import cn.sd.rmi.ServiceProxyFactory;

public class SecurityContext {
	
	static Log log = LogFactory.getLog(SecurityContext.class);
	public static String SECURITY_CONTEXT_KEY = "sgdac.SecurityContext";
	//在线用户列表
	private static Map<String,Map<String, Object>> onLineUser = new HashMap<String,Map<String, Object>>();	//在线用户
	private static Map<String,String> sessions = new HashMap<String,String>();	//sessionId 和 用户Id的对应
	private Map<String, Object> localUser = null;
	
	/**
	 * 得到当前应用的所有Session
	 * @return
	public static java.util.HashMap<String, HttpSession> getSessions() {
		return SecurityListener.getSessions();
	}
	*/
	
	
	/**
	 * 得到在线用户列表
	 * @return
	 */
	public Map<String,Map<String, Object>> getOnLineUsers(){
		return onLineUser;
	}
	
	public Map<String, Object> getCurrentUser(){
		return localUser;
	}
	
	/**
	 * 判断 用户是否已被踢掉或已不在线
	 * @param userId
	 * @param sessionId
	 * @return
	 */
	public boolean userKicked(String userId,String sessionId){
		boolean returnValue = true;
		String sId = getSessionIdByUserId(userId);
		if(sId!=null && sessionId.equals(sId)){
			returnValue = false;
		}
		return returnValue;
	}
	
	/**
	 * 通过用户ID得到用户登录的SessionId
	 * @param uid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getSessionIdByUserId(String uid){
		String returnValue = null;
		if(sessions!=null && sessions.size()>0){
			Iterator it = sessions.entrySet().iterator();    
			while (it.hasNext()){    
				Map.Entry pairs = (Map.Entry)it.next();    
				String sessionId = pairs.getKey().toString();
				String userId = pairs.getValue().toString();
				if(uid.equals(userId)){
					returnValue = sessionId;
					break;
				}
			}
		}
		return returnValue;
	}
	
	/**
	 * 将一个用户登录到系统中，不验证用户的任何信息。
	 * 不允许用户帐号登录多次。会踢掉这个用户的上一次登录。
	 * @param user
	 * @param sessionId
	 * @return pressure 踢下去
	 */
	static boolean notNull(String str) {
		return str != null && str.length() > 0;
	}
	public boolean loginUser(Map<String, Object> user,String sessionId,boolean pressure,HttpServletResponse response,HttpServletRequest request){
		boolean returnValue = false;
		if(user!=null){
			if(pressure){ 
				//踢掉这个用户的上一次登录
				String oldSessionId = getSessionIdByUserId(user.get("ID")+"");
				if(oldSessionId != null && notNull(oldSessionId)){
					logoutUserBySession(oldSessionId);
				}
				//将当前用户登录到系统
				if(sessionId!=null && sessionId.length()>0){
				
				  user.put("LOGINTIME", DateUtil.getNowDateTimeString());
				  
				  sessions.put(sessionId,user.get("ID")+"");
				  onLineUser.put(user.get("ID")+"", user);
				  localUser = user;
				  //设置Session 属性值
				  HttpSession session = request.getSession();
				  session.setAttribute(SecurityContext.SECURITY_CONTEXT_KEY,SecurityContextHolder.getContext());
				  returnValue = true;
				}
			} else {
				returnValue = false;
			}
			log.info("登录完成-------------------------userName="+user.get("NAME"));
			log.info("当前登录的人数为："+this.getOnLineUsers().size());
		}
		return returnValue;
	}
	
	/**
	 * 按SessionId将用户登出。
	 * @param sessionId
	 */
	public void logoutUserBySession(String sessionId){
		String userId = sessions.get(sessionId);
		sessions.remove(sessionId);
		onLineUser.remove(userId);
		localUser = null;
		LockContext.signalAndDelLock(sessionId);
		log.info("-----------------session 结束 或者 被踢下线 sessionId="+sessionId+"         userId="+userId);
		
		
	}
	public void logoutUserByUser(String userId){
		String sessionId = getSessionIdByUserId(userId);
		sessions.remove(sessionId);
		onLineUser.remove(userId);
		localUser = null;
		LockContext.signalAndDelLock(sessionId);
		log.info("-----------------session 结束 或者 被踢下线 sessionId="+sessionId+"         userId="+userId);
		
	}
	
	public void logoutUserAll(){
		for(Map.Entry<String, String> entry: sessions.entrySet()) {
			String userId = sessions.get(entry.getKey());
			sessions.remove(entry.getKey());
			onLineUser.remove(entry.getValue());
			localUser = null;
			LockContext.signalAndDelLock(entry.getKey());
//			try{
//				ServiceProxyFactory.getProxy().removeByUseId(entry.getValue());
//			}catch(Exception ex){}
		}
		
	}
	
	public static String printStatus(){
		return "sessions ="+sessions.size()+"  onLineUser="+onLineUser.size();
	}
}
