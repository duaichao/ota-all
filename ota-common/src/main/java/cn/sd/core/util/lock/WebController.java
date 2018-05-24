package cn.sd.core.util.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class WebController {
	private final static Map<String,UrlController> collectionByUrl = new HashMap<String,UrlController>();
	static{
//		collectionByUrl.put("/produce/traffic/list", new UrlController("/produce/traffic/list",30,60000000000l,60000000000l,120000000000l));
//		collectionByUrl.put("/order/traffic/list", new UrlController("/order/traffic/list",30,60000000000l,60000000000l,120000000000l));
	}
	
	private static WebController webController = new WebController();
	private WebController(){}
	public static WebController getInstance(){ return webController; }
	
	private static UrlController getUrlContro(String url){
		return collectionByUrl.get(url);
	}
	/**
	 * 判断Ip是否可用
	 * true：可用 
	 * false：不可用
	 * */
	public static boolean ipAttack(String url,String ip){
		UrlController urlContro = WebController.getUrlContro(url);
		if(urlContro == null) return true;
		
		try{  
			boolean b = urlContro.getLock().tryLock(100000000l,TimeUnit.NANOSECONDS);
			if(b==false) return false;
			
			urlContro.cleanBlack();
			urlContro.clean();
			
			return handler(urlContro,url,ip,urlContro.getLock());
			
	     }catch (InterruptedException e){  
	    	 return false; 
	     }
	}
	
	private static boolean handler(UrlController urlContro,String url,String ip,Lock lock){
	
		try{
			boolean isExistBlack = urlContro.containBlack(ip);
			if(isExistBlack==true){
				boolean timeOutBalck = urlContro.timeOutByBlack(ip);
				if(timeOutBalck==true){
					urlContro.removeBlack(ip);
				}else{
					return false;
				}
			}
			
			boolean isExist = urlContro.contain(ip);
			if(isExist==true){
				boolean timeOut = urlContro.timeOut(ip);
				if(timeOut==true){
					urlContro.reset(ip);
					return true;
				}else{
					boolean cntOut = urlContro.cntOut(ip);
					if(cntOut==true){
						urlContro.putBlack(ip);
						urlContro.remove(ip);
						return false;
					}else{
						urlContro.Increasing(ip);
						return true;
					}
				}
			}else{
				urlContro.put(ip);
				return true;
			}
		}finally{
			lock.unlock();
		}
		
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		Set<String> set = this.collectionByUrl.keySet();
		for(String url : set){
			sb.append(this.getUrlContro(url).toString());
			sb.append("\r\n");
		}
		return sb.toString();
	}
	
	public static void main(String[] args){
		

	}
	
}
