package cn.sd.core.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.sd.core.filter.city.IUserCityReader;
import cn.sd.core.filter.city.UserCity;
import cn.sd.core.util.WebUtils;

public class CityFilter implements Filter {
	private String className=null;
	private String defaultCity = null;
	private String defaultCityPinYin = null;
	private boolean autoGetCity = false;
	private String webSite = null;
	private final String cookieName = "LUOTUO-15141-CITY";
	private static IUserCityReader cityReader = null;

	private static ThreadLocal<UserCity> cityHolder = new ThreadLocal<UserCity>();
	
	private static void clearCityContext() {
		cityHolder.set(null);
	}

	private static UserCity getCityContext() {
		if (cityHolder.get() == null) {
			cityHolder.set(new UserCity());
		}
		return (UserCity) cityHolder.get();
	}

	private static void setCityContext(UserCity context) {
		cityHolder.set(context);
	}
	
	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String localCity = null;
		String localCityPinYin = null;
		
		HttpServletResponse res = (HttpServletResponse)response;
		HttpServletRequest  req = (HttpServletRequest)request;
		
		HttpSession session = req.getSession();
		//将当前用户绑定到当前线程中
		if(session != null){
			UserCity context = (UserCity)session.getAttribute(cookieName);   
            setCityContext(context);   
        } else {
        	clearCityContext();
        }
		
		String cityName = null;
		String cookieValue = null;
		
		if(autoGetCity){
			String www = req.getServerName().toLowerCase();
			if(webSite!=null && !"".equals(webSite)){
				webSite = webSite.toLowerCase();
				if(www.endsWith("." + webSite)){
					String subHost = www.substring(0,www.length() - webSite.length() - 1);
					if(!"www".equals(subHost)){
						if(cityReader==null){
							/*Class userCls = null;
							try {
								userCls = Class.forName(className);
								if(userCls!=null){
									cityReader = (IUserCityReader)userCls.newInstance();
								}
							} catch (Exception e) {
								userCls = null;
								cityReader = null;
							}*/
							//cityReader = (IUserCityReader)cn.sd.core.util.SpringApplicationContext.getService(className);
						}else{
							cityName = cityReader.getCityByPinYin(subHost);
						}
						localCityPinYin = subHost;
					}
				}
			}
			
			//System.out.println("cityName=" + cityName);
			
			if(cityName==null || "".equals(cityName)){
				cookieValue = WebUtils.getCookieValue(req,cookieName);
				cityName = cookieValue;
			}
			if(cityName==null || "".equals(cityName)){
				if(className!=null && !className.equals("")){
					if(cityReader==null){
						/*Class userCls = null;
						try {
							userCls = Class.forName(className);
							if(userCls!=null){
								cityReader = (IUserCityReader)userCls.newInstance();
							}
						} catch (Exception e) {
							userCls = null;
							cityReader = null;
						}*/
						//cityReader = (IUserCityReader)cn.jiaoda.core.util.SpringApplicationContext.getService(className);
					}
					if(cityReader!=null){
						if(cityName==null || "".equals(cityName)){
							cityName = cityReader.getCityInSession(req.getSession());
						}
						if(cityName==null || "".equals(cityName)){
							String ipaddress = request.getRemoteAddr();
							//ipaddress = "61.134.1.4";
							cityName = cityReader.getCityByIpAddress(ipaddress);
						}
					}
				}
			}
		}
		
		if(cityName==null || "".equals(cityName)){
			cityName = "西安";
			localCityPinYin = "xian";
		}
		localCity = "西安";
		
		
		
		if(!localCity.equals(cookieValue)){
			WebUtils.saveCookieValue(res, cookieName, cityName, 0);
		}
		
		UserCity uc = getCityContext();
		uc.setLocalCity(localCity);
		uc.setLocalCityPinYin(localCityPinYin);
		setCityContext(uc);
		session.setAttribute(cookieName, uc);
		
		chain.doFilter(request, response);
	}
	
	/**
	 * 读取初始化参数
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		className = filterConfig.getInitParameter("className");
		defaultCity = filterConfig.getInitParameter("defaultCity");
		webSite = filterConfig.getInitParameter("www");
		defaultCityPinYin = filterConfig.getInitParameter("defaultCityPinYin");
		String strAutoGetCity = filterConfig.getInitParameter("autoGetCity");
		if(strAutoGetCity!=null && !strAutoGetCity.equals("")){
			try{
				autoGetCity = Boolean.valueOf(strAutoGetCity);
			} catch (Exception e){}
		}
	}
	
	@Override
	public void destroy() {
	}
	
	public static String getLocalCity() {
		return getCityContext().getLocalCity();
	}
	
	public static String getLocalCityPinYin() {
		return getCityContext().getLocalCityPinYin();
	}
	
	public static void main(String[] args){
		String webSite = "15141.cn";
		String www = "xianyang.15141.cn";
		if(webSite!=null && !"".equals(webSite)){
			webSite = webSite.toLowerCase();
			if(www.endsWith("." + webSite)){
				String subHost = www.substring(0,www.length() - webSite.length() - 1);
			}
		}
	}
}