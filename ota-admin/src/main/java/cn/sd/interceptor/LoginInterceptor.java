package cn.sd.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;

import cn.sd.security.SecurityContext;
import cn.sd.security.SecurityContextHolder;
import cn.sd.web.Constants;
import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.DateUtil;
import cn.sd.core.util.WebUtils;
import cn.sd.core.util.lock.WebController;

import cn.sd.rmi.ServiceProxyFactory;

public class LoginInterceptor implements HandlerInterceptor{
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	// 类级别的判断
    static Map<String, String> nofilterURL = new HashMap<String, String>();
    static{
		nofilterURL.put("/swfupload", "附件模块");
		nofilterURL.put("/upload/ad/b2b", "广告模块");
		nofilterURL.put("/pay/alipay/notify", "支付宝回调");
		nofilterURL.put("/pay/alipay/return", "支付宝状态");
		
	}
    static String vistiorFailed = "/commons/vistior-failed.jsp";
	static String logoutUrl = "/logout";
	static String indexUrl = "/user/index";
	
	static String applogoutUrl = "/applogout";
	/**
	 * 在业务处理器处理请求之前被调用
	 */
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		UrlPathHelper urlPathHelper = new UrlPathHelper();
		String url = urlPathHelper.getLookupPathForRequest(request);
		boolean b = WebController.ipAttack(request.getRequestURI(),WebUtils.getIpAddress(request));
		
		
		
		//---------------------------------------------------------------------------------------
		if(CommonUtils.checkString(request.getRequestURL()) && request.getRequestURL().toString().indexOf("img.136ly.com") != -1){
			response.sendRedirect(request.getContextPath()+"/commons/empty.jsp");
			return false;
		}
		//---------------------------------------------------------------------------------------
		if(!b){
			response.sendRedirect(request.getContextPath()+vistiorFailed);
			return false;
		}
		//不过滤 静态文件 资源
		if(isNotNull( url) && url.contains(".")){
			return true;
		}
		
		HttpSession session =  request.getSession();
		//将当前用户绑定到当前线程中
 		if(session != null){
             SecurityContext context = (SecurityContext)session.getAttribute(SecurityContext.SECURITY_CONTEXT_KEY);   
             SecurityContextHolder.setContext(context);   
         } else {
         	SecurityContextHolder.clearContext();
         }
 		
 		//不需要登录的模块
 		boolean isNofilter = LoginInterceptor.isNofilterURL(url);
		if(isNofilter){
			//System.out.println(url+"==============不需要登录============"+url.split("/").length);
			return true;
		}else {
			//System.out.println(url+"==============必须要登录============"+url.split("/").length);
			
			//远程服务器断开连接后,踢掉已登录用户
			try {
				ServiceProxyFactory.getProxy().hr();
			} catch (Exception e) {
				System.out.println("service服务中断过滤器");
				if(request.getParameter("appName") != null){
					response.sendRedirect(request.getContextPath()+applogoutUrl);
				}else{
					response.sendRedirect(request.getContextPath()+logoutUrl);
				}
				return true;
			}
			
			//需要登录
				
			Map<String, Object> user = SecurityContextHolder.getContext().getCurrentUser();
			Cookie cookie = WebUtils.getCookie(request, ServiceProxyFactory.SSO_TOKEN_NAME);
			
			/**
			 * 未通过登录页面,直接访问,cookie位空转到登录页面,创建cookie
			 */
			if(cookie == null){
				if(request.getParameter("appName") != null){
					response.sendRedirect(request.getContextPath()+applogoutUrl);
				}else{
					response.sendRedirect(request.getContextPath()+logoutUrl);
				}
				return true;
			}
			Map<String, Object>	result = ServiceProxyFactory.getProxy().existCookie(cookie.getValue(), request.getSession().getId());
			boolean exist = (Boolean)result.get("exist");
			if(user != null && exist){
				boolean userKicked = SecurityContextHolder.getContext().userKicked(user.get("ID").toString(),session.getId());
				if(userKicked){
					session.invalidate();//使Session失效
					if(request.getParameter("appName") != null){
						response.sendRedirect(request.getContextPath()+applogoutUrl);
					}
					return false;
				}else{
					return true;
				}
			}else{
				if(exist){
					Map<String, Object> s = (Map<String, Object>)result.get("s");
					Map<String, Object> _user = (Map<String, Object>)s.get(Constants.SESSION_USER_KEY);
					for (Map.Entry<String, Object> _s: s.entrySet()) {
						request.getSession().setAttribute(_s.getKey(), _s.getValue());
					}
					SecurityContextHolder.getContext().loginUser(_user, session.getId(), true, response, request);
					_user.put("success", true);
					_user.put("LOGIN_TIME", DateUtil.getNowDateTimeString());
					
					
					user = SecurityContextHolder.getContext().getCurrentUser();
					if(user!=null){
						boolean userKicked = SecurityContextHolder.getContext().userKicked(user.get("ID").toString(),session.getId());
						if(userKicked){
							session.invalidate();//使Session失效
							if(request.getParameter("appName") != null){
								response.sendRedirect(request.getContextPath()+applogoutUrl);
							}
							return false;
						}else{
							return true;
						}
					}
					
				}else{
					if(request.getParameter("appName") != null){
						response.sendRedirect(request.getContextPath()+applogoutUrl);
					}else{
						response.sendRedirect(request.getContextPath()+logoutUrl);
					}
					return true;
				}
					
			}
			return true;
				
				
//				if(!isLogin(request)){
//					if(request.getParameter("appName") != null){
//						response.sendRedirect(request.getContextPath()+applogoutUrl);
//					}else{
//						response.sendRedirect(request.getContextPath()+logoutUrl);
//					}
//					return true;
//					
//				}else {
////				检查用户是否已被踢掉
//					if(user!=null){
//						boolean userKicked = SecurityContextHolder.getContext().userKicked(user.get("ID").toString(),session.getId());
//						if(userKicked){
//							session.invalidate();//使Session失效
//							if(request.getParameter("appName") != null){
//								response.sendRedirect(request.getContextPath()+applogoutUrl);
//							}
//							return false;
//						}else{
//							return true;
//						}
//					}
//					return true;
//				}
				
				
				
		}
	}
	
	/**
	 * 不需要登录的模块
	 * true:不需要
	 * false:需要
	 * */
	public static boolean isNofilterURL(String url){
		if(isNotNull(url) && nofilterURL.containsKey(url) || url.split("/").length==2){
			//System.out.println(url+"==============不需要登录============"+url.split("/").length);
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 字符串为空判断
	 */
	public static boolean isNotNull(String str) {
		if (str != null && str.length() > 0) {
			return true;
		} else {
			return false;
		}
	}
	public boolean isLogin(HttpServletRequest request) {
		if (request != null && request.getSession().getAttribute("S_USER_SESSION_KEY") != null) {
			return true;
		}
		return false;
	}

}
