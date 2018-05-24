package cn.sd.rmi;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ServiceProxyByCFactory {
	
	private ServiceProxyByCFactory(){}
	private static FacadeService proxySerivce;
	
	public static FacadeService getProxy(){
		ServiceProxyByCFactory.proxySerivce = RmiProxy.getInstance();
		return proxySerivce;
	}
	
	public static void test() throws Exception{
		try{
			ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-rmi-client.xml");
		}catch(Exception ex){
			ex.printStackTrace();
			throw new Exception("Connection refused: connect");
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
