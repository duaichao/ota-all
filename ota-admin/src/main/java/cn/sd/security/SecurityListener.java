package cn.sd.security;


import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SecurityListener implements HttpSessionListener{
	//private static java.util.HashMap<String, HttpSession> sessions= new java.util.HashMap<String, HttpSession>();
	private static final Log logger = LogFactory.getLog(SecurityListener.class);
	
	private static long userCount = 0;
	
	@Override
	public void sessionCreated(HttpSessionEvent se) {
//		userCount = userCount + 1;
//		logger.info("上线在线用户数:" + userCount);
//		logger.info("上线已登录用户:" + SecurityContextHolder.getContext().getOnLineUsers().size());
	}
	
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
//		userCount = userCount>0?userCount - 1:0;
		System.out.println("下线在线用户数");
		SecurityContextHolder.getContext().logoutUserBySession(se.getSession().getId());
//		logger.info("下线在线用户数:" + userCount);
//		logger.info("下线已登录用户:" + SecurityContextHolder.getContext().getOnLineUsers().size());
	}
 
}
