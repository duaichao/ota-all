package cn.sd.interceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.util.UrlPathHelper;

import cn.sd.service.b2b.IModuleService;
import cn.sd.service.b2b.IRoleService;

public class ModuleInterceptor {


	private static IModuleService moduleService;

	private static  IRoleService roleService;
	
	private static List<java.lang.String> ALLModule = new ArrayList<java.lang.String>();
	
	public static boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		UrlPathHelper urlPathHelper = new UrlPathHelper();
		String url = urlPathHelper.getLookupPathForRequest(request);
		
		Map<String,Object> user = (HashMap<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		if(user==null || user.get("USER_NAME") == null) return true;
		
		String USER_NAME = user.get("USER_NAME").toString();
		if(USER_NAME.equals("admin")){
				return true;
		}
		
		ApplicationContext context = RequestContextUtils.getWebApplicationContext(request);
		moduleService = (IModuleService)context.getBean("moduleService");
		roleService = (IRoleService)context.getBean("roleService");
		if(ALLModule.size()==0){
			ALLModule = moduleService.listAllService();
		}
		
//		System.out.println(ALLModule.contains(url));
//		System.out.println(ModuleInterceptor.isVail(ALLModule,url));
//		
		if(ALLModule.contains(url) && ModuleInterceptor.isVail(ALLModule,url)==1){
			List<String> userRole = roleService.userRoleService(user.get("ID").toString());
			String roleId = userRole.get(0);
			List<String> userModuleAll= moduleService.userRoleModuleByStringService(roleId);
			if(userModuleAll.contains(url)|| ModuleInterceptor.isVail(userModuleAll,url)==1){
				return true;
			}else{
				response.sendRedirect(request.getContextPath()+"/invalid");
				return false;
			}
		}else{
			return true;
		}
		
	}
	
	private static int isVail(List<String> userModuleAll,String url){
		for(String tmp  : userModuleAll){
			if(url.indexOf(tmp, 0)==0) return 1;
		}
		return 0;
	}
}
