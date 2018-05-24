package cn.sd.rmi;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import cn.sd.security.SecurityContext;
import cn.sd.security.SecurityContextHolder;
import cn.sd.core.config.ConfigLoader;

public class ServiceProxyFactory {
	
	public final static String SSO_TOKEN_NAME = "SD_TOKEN";
	
	private ServiceProxyFactory(){}
	
	
	private static FacadeService proxySerivce;
	
	private static int flag = Integer.parseInt(ConfigLoader.config.getStringConfigDetail("remote_rmi"));

	public static void setFlag(int t) throws Exception{
		if(flag==0 && t==1){
			SecurityContextHolder.getContext().logoutUserAll();
		}
		flag = t;
	}
	
	public static int getFlag(){
		return flag;
	}
	
	public static FacadeService getProxy(){
		if(flag == 0){
			ServiceProxyFactory.proxySerivce = ServiceProxyFactory.LocalProxy.getInstance();
		}else{
			try{
				ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-rmi-client.xml");
				ServiceProxyFactory.proxySerivce = context.getBean("rmiService", FacadeService.class);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
		}
		return proxySerivce;
	}
	
	
	
	
	
	public static boolean loginIntercHandler(HttpServletRequest request,HttpServletResponse response) throws Exception{
		return false;
	}
	
	
	
	public static boolean autoLoginApp(HttpServletRequest request,HttpServletResponse response) throws Exception{
		return true;
	}
	
	
	
	
	public static boolean changeLocal(){
		if(ServiceProxyFactory.getFlag()==1 && CheckRemote.isFlag()==false){
			try {
				ServiceProxyFactory.setFlag(0);
				ServiceProxyFactory.getProxy();
				return true;
			} catch (Exception e) {
				return false;
			}
		}else{
			return true;
		}
	}
	
	public static Map<String,Map<String, Object>> getOnLineUsers(){
		return new SecurityContext().getOnLineUsers();
		
	}
	
	public static void test() throws Exception{
		try{
			ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-rmi-client.xml");
		}catch(Exception ex){
			ex.printStackTrace();
			throw new Exception("Connection refused: connect");
		}
	}
	
	public static void reload() throws Exception{
		ServiceProxyFactory.test();
		RmiProxy.getInstanceReload();
	}
	
	@Component 
	private static class LocalProxy { 
		private static FacadeService localService = null;
		@Autowired
	    public void setSysLogService(FacadeService localService) {  
	        this.localService = localService;  
	    }  
		private LocalProxy(){}
		public static synchronized FacadeService getInstance(){
			//if(localService == null)localService = new LocalServiceImpl();
			return localService;
		}
    }
	
	
	private static class RmiProxy { 
		private static FacadeService rmiService = null;
		private RmiProxy(){}
		public static synchronized FacadeService getInstance(){
			try{
				if(rmiService == null){
					ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-rmi-client.xml");
					rmiService = context.getBean("rmiService", FacadeService.class);
				}
				return rmiService;
			}catch(Exception ex){
				//ex.printStackTrace();
				return null;
			}
		}
		
		public static void getInstanceReload(){
			rmiService = null;
		}
    }
	
	
}
