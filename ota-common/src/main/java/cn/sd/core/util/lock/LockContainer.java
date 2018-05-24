package cn.sd.core.util.lock;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

import cn.sd.core.util.DateUtil;

public class LockContainer {
	
	private static int size=100;
	private static int overSize=0;
	private static int stepSize=10;
	private static long lazyTime = 30*1000000000l;
	private static final long InterruptedTime = 5*1000000000l;//5
	private static AtomicInteger waitInt = new AtomicInteger(0);
	public static final Semaphore semaphore = new Semaphore(1);
	private static final Lock lockApi = new ReentrantLock();
	private static Map<String,SdLock> lockMap = new HashMap<String,SdLock>();
	private static int  failureCount = 0;
	
	public static final ConcurrentMap<String, String> dynamicLock = new ConcurrentHashMap<String, String>();
	
	//获得一个锁
	public static Object getLock(String lockId){
		//System.out.println("getLock="+lockId);
		boolean a = acquireLockStateByCount();
		if(a==false){
			failureCount = failureCount +1;
			return null;
		}
		try{
			lockApi.lockInterruptibly();
			SdLock lock = null;
			boolean b = isExistLock(lockId);
			if(b){
				lock = lockMap.get(lockId);
				reSetLockTime(lock);
			}else{
				lock = createLock();
				lockMap.put(lockId, lock);
				boolean c = isOverSize();
				if(c){
					clearLock();
					c = isOverSize();
					if(c) overSize = overSize+stepSize;
				}
				
			}
			return lock.getLock();
		} catch (InterruptedException e) { 
			e.printStackTrace(); 
		}finally {
			lockApi.unlock();
			semaphore.release();
		}
		return null;
		
		
	}
	//获取锁等待时间查过时间 中断等待
	private static boolean acquireLockState(){
		long start = System.nanoTime();
		for(;;){
			if(!isLockState()){
				if(System.nanoTime() - start > InterruptedTime){
                    LockContainer.stopLock();
				}
				try {
					Thread.sleep((long) (1 * 100));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			}else{
				return true;
			}
		}
	}
	
	private static boolean acquireLockStateByCount(){
		waitInt.incrementAndGet();
		boolean a = acquireLockState();
		waitInt.decrementAndGet();
		return a;
	}
	
	//中断锁
	public static void stopLock(){
		Thread.currentThread().interrupt();
	}
	
	//创建一个锁
	private static SdLock createLock(){
		return new SdLock();
	}
	
	private static void reSetLockTime(SdLock lock){
		lock.lastUseTime = System.nanoTime();
	}
	
	//当前锁的数量
	private static int getLockSize(){
		return lockMap.size();
	}
	//判断是否超出容量
	private static boolean isOverSize(){
		return getLockSize() > size+overSize;
	}
	//锁是否超时
	private static boolean isTimeOutLock(String lockId){
		SdLock lock = lockMap.get(lockId);
		long end = System.nanoTime();
		if(end-lock.lastUseTime >= lazyTime){
			return true;
		}else{
			return false;
		}
	}
	//清理超时锁
	private static List<String> beforeClearLock(){
		Set<String> keys = lockMap.keySet();
		List<String> a = null;
		boolean b = true;
		boolean c = true;
		for(String lockId:keys){
			b = isTimeOutLock(lockId);
			if(b){
				if(c==true){
					a = new LinkedList<String>();
					c=false;
				}
				a.add(lockId);
			}
		}
		return a;
		
	}
	private static void clearLock(){
		List<String> lockIds = beforeClearLock();
		if(lockIds==null) return;
		
		for(String lockId:lockIds){
			lockMap.remove(lockId);
		}
	}
	//锁是否存在
	private static  boolean isExistLock(String lockId){
		SdLock lock = lockMap.get(lockId);
		if(lock == null){
			return false;
		}else{
			return true;
		}
	}
	private static boolean isLockState() {
		return LockContainer.semaphore.tryAcquire();
	}
	//等待数量
	private static int getWaitInt(){
		return waitInt.get();
	}
	
	
	
	public static String toPrint(){
		/**
		 * 时间
		 * 等待获取锁定的数量
		 * 当前锁的数量
		 * */
		StringBuffer sb = new StringBuffer();
		sb.append(DateUtil.getNowDate()+" "+DateUtil.getNowTime());
		sb.append("    "+"actual_size="+getLockSize());
		sb.append("\t"+"wait_size="+getWaitInt());
		sb.append("\t"+"max_size="+(size+overSize));
		sb.append("\t"+"failure_size="+(failureCount));
		
		return sb.toString();
	}
	
	//清除
	public static void clear(){
		size=100;
		overSize=0;
		stepSize=10;
		waitInt.set(0);
		lockMap.clear();
		failureCount = 0;
	}
	
	
	
	public static void main(String[] args){
	
//		ExecutorService service = Executors.newCachedThreadPool();  
//		
//		final int count = 4000;  
//		for (int i = 0; i < count; i++) {
//			final int j = i;
//			Runnable runnable = new Runnable() {  
//				public void run() {  
//					System.out.println("线程" + Thread.currentThread().getName() +" = " + j + "进入");
//					LockContainer.getLock(j+""); 
//					System.out.println("线程" + Thread.currentThread().getName() +" = " + j + "退出"); 
//					System.out.println(j+"***"+LockContainer.toPrint());
//				}  
//			};  
//			service.execute(runnable);  
//		}  
//		service.shutdown(); 
	}
}
