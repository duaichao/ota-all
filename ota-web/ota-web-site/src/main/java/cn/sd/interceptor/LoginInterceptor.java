package cn.sd.interceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UrlPathHelper;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.WebUtils;
import cn.sd.entity.produce.CompanyDetail;
import cn.sd.service.common.ICommonService;
import cn.sd.service.route.IRouteService;
import cn.sd.service.user.IUserService;

public class LoginInterceptor implements HandlerInterceptor{
	
	@Resource
	private IUserService userService;
	@Resource
	private IRouteService routeService;
	@Resource
	private ICommonService commonService;
	
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
    static Map<String, String> filterURL = new HashMap<String, String>();
    static Map<String, String> filterNotURL = new HashMap<String, String>();
    
    static{
    	filterURL.put("/order", "订单模块");
    	filterURL.put("/user/sec", "会员安全链接");
    	filterURL.put("/user/center", "会员中心");
    	
    	filterNotURL.put("/order/to/save", "预定线路");
    	
	}
    
	static String loginUrl = "/user/to/login";
	static String errorUrl = "/commons/error.jsp";
	
	/**
	 * 在业务处理器处理请求之前被调用
	 */
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		boolean isFromMobile=false;  
		
		HttpSession session= request.getSession();  
		//检查是否已经记录访问方式（移动端或pc端）  
		if(null == session.getAttribute("USER_AGENT")){  
			try{  
			    //获取ua，用来判断是否为移动端访问  
				String userAgent = request.getHeader("USER-AGENT").toLowerCase();    
				if(null == userAgent){    
				    userAgent = "";    
				}  
				isFromMobile=CommonUtils.checkMobile(userAgent);
				//判断是否为移动端访问  
				if(isFromMobile){  
					session.setAttribute("USER_AGENT","mobile");  
				} else {  
					session.setAttribute("USER_AGENT","pc");  
				}  
		    }catch(Exception e){}  
		}else{  
		    isFromMobile = session.getAttribute("USER_AGENT").equals("mobile");
		}  
		
		String USER_AGENT = (String)session.getAttribute("USER_AGENT");
		
		UrlPathHelper urlPathHelper = new UrlPathHelper();
		String url = urlPathHelper.getLookupPathForRequest(request);
		
//		System.out.println(url+"***************************************************");
		Map<String, Object> p =  new HashMap<String, Object>();
		CompanyDetail company  = (CompanyDetail)request.getSession().getAttribute("company");

		if(url.contains(".")){
			return true;
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		String domain = request.getServerName();
		params.clear();
		if(domain.split("\\.").length == 2){
			domain = "www."+domain;
		}
		
		if(company == null){
			
			params.put("DOMAIN", domain);
			params.put("TYPE", 0);

			List<CompanyDetail> companys = this.userService.searchCompanyBuyDomain(params);
			
			if(!CommonUtils.checkList(companys)){
				//未查询到域名对应的旅行社公司,转向提示页面
				response.sendRedirect(request.getContextPath()+errorUrl);
				return false;
			}
			
			company = this.getCompanyDetail(companys, domain);
			
			/**
			 * 如果是手机访问公司网站并且公司开通了微店.跳转到公司的微店
			 */
			if(USER_AGENT.equals("mobile")){
				params.put("ENTITY_ID", company.getUSER_ID());
				params.put("TYPE", 1);
				List<Map<String, Object>> settings = (List<Map<String, Object>>)this.userService.searchWapSetting(params);
				if(CommonUtils.checkList(settings) && settings.size() == 1){
					System.out.println("http://"+(String)settings.get(0).get("DOMAIN"));
					response.sendRedirect("http://"+(String)settings.get(0).get("DOMAIN"));
					return false;
				}else{
					response.sendRedirect(request.getContextPath()+"/commons/mini-404.jsp");
					return false;
				}
			}
			
			if(company.getSETTING_TYPE().equals("1")){
				params.clear();
				params.put("COMPANY_ID", company.getID());
				params.put("TYPE", 0);
				List<Map<String, Object>> settings = this.userService.searchWapSetting(params);
				company.setPDOMAIN(settings.get(0).get("DOMAIN").toString().split(",")[0]);
			}
			
			if(company == null){
				//未查询到域名对应的旅行社公司,转向提示页面
				response.sendRedirect(request.getContextPath()+errorUrl);
				return false;
			}

			request.getSession().setAttribute("company", company);
			
			params.clear();
			params.put("COMPANY_ID", company.getID());
			List<Map<String, Object>> categorys = this.commonService.searchWebCategory(params);
			request.getSession().setAttribute("categorys", categorys);
			
			company.setCategorys(categorys);

			/**
			 * 顾问
			 */
			params.clear();
			params.put("STORE_ID", company.getID());
			params.put("COMPANY_ID", company.getID());
			List<Map<String, Object>> counselors = this.userService.searchCompanyCounselor(params);
			request.getSession().setAttribute("counselors", counselors);
			
		}
		
		List<String> begin_citys = (List<String>)request.getSession().getAttribute("begin_citys");
		if(company != null && (begin_citys == null || begin_citys.size() == 0)){
			begin_citys = new ArrayList<String>(); 
			String startCityName = company.getSTART_CITY_NAME();
			if(CommonUtils.checkString(startCityName)){
				String[] citys = startCityName.split(",");
				for (String city : citys) {
					begin_citys.add(city);
				}
			}
			request.getSession().setAttribute("begin_citys", begin_citys);
		}
		
		if(url.indexOf("logout")>=0){
			return true;
		}
		
		Map<String, Object> webUser = (Map<String, Object>)request.getSession().getAttribute("webUser");
		String autoLogin = WebUtils.getCookieValue(request, "136LYAUTOLOGIN");
		if(CommonUtils.checkString(autoLogin) && !CommonUtils.checkMap(webUser)){
			p.put("id", autoLogin);
			List<Map<String, Object>> webUsers = this.userService.searchWebUser(p);
			if(CommonUtils.checkList(webUsers) && webUsers.size() == 1){
				request.getSession().setAttribute("webUser", webUsers.get(0));
			}
		}
		
		boolean isNofilter = LoginInterceptor.isNofilterURL(url);
		if(isNofilter){
			return true;
		}else {
			if(!CommonUtils.checkMap(webUser)){
				response.sendRedirect(request.getContextPath()+loginUrl);
				return false;
			}
			return true;
		}
	}
	
	/**
	 * 不需要登录的模块
	 * true:不需要
	 * false:需要
	 * */
	public static boolean isNofilterURL(String url){
		for(String s: filterNotURL.keySet())   {
			if(url.indexOf(s) >= 0){
				return true;
			}
		} 
		for(String s: filterURL.keySet())   {
			if(url.indexOf(s) >= 0){
				return false;
			}
		}  
		return true;
	}
	

	public boolean isLogin(HttpServletRequest request) {
		if (request != null && request.getSession().getAttribute("S_USER_SESSION_KEY") != null) {
			return true;
		}
		return false;
	}
	
	public CompanyDetail getCompanyDetail(List<CompanyDetail> companys, String domain){
		for (CompanyDetail companyDetail : companys) {
			String[] domains = companyDetail.getDOMAIN().split(",");
			for (int i = 0; i < domains.length; i++) {
				if(domain.equals(domains[i])){
					return companyDetail;
				}
			}
		}
		return null;
	}

}
