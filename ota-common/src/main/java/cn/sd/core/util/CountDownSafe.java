package cn.sd.core.util;

import java.util.concurrent.atomic.AtomicInteger;

public class CountDownSafe {
	private static final AtomicInteger ai = new AtomicInteger(0);
	private static final int total=20;
	private static CountDownSafe cds=null;
	private CountDownSafe(){}
	
	public static synchronized CountDownSafe getInstance(){
		if(cds == null) cds = new CountDownSafe();
		return cds;
	}
	
	private static int incrementAndGet(){
		return CountDownSafe.ai.incrementAndGet();
	}
	
	public static void release(){
		CountDownSafe.ai.decrementAndGet();
	}
	
	public synchronized static boolean acquire(){
		int a = incrementAndGet();
		if(a==CountDownSafe.total+1){
			release();
			return false;
		}else{
			return true;
		}
	}
	public static int availablePermits(){
		if(CountDownSafe.total-CountDownSafe.ai.intValue()<=0){
			return 0;
		}else{
			return CountDownSafe.total-CountDownSafe.ai.intValue();
		}
	}
	public static String toPrint(){
		StringBuffer sb = new StringBuffer();
		sb.append(DateUtil.getNowDate()+" "+DateUtil.getNowTime());
		sb.append("    "+"actual_size="+ai.get());
		sb.append("\t"+"max_size="+total);
		
		return sb.toString();
	}
	
	public static void main(String[] args){
		for(int i=0;i<30;i++){
			System.out.print(CountDownSafe.acquire());
			CountDownSafe.availablePermits();
		}
		
	}
}
