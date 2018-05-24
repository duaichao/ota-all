package cn.sd.core.filter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;

public class LockContext {
	public static Object obj = new Object();
	public static ConcurrentHashMap<String,CountDownLatch> conditionLocksString = new ConcurrentHashMap<String,CountDownLatch>();

	
	public static void addLock(String sessionId,CountDownLatch condition){
		conditionLocksString.put(sessionId, condition);
	}
	
	public static CountDownLatch getLock(String sessionId){
		return conditionLocksString.get(sessionId);
	}
	
	public static void signalAndDelLock(String sessionId){
		if(conditionLocksString.get(sessionId) == null) return;
		CountDownLatch condition = conditionLocksString.get(sessionId);
		conditionLocksString.remove(sessionId);
		condition.countDown();
	}
	
	public static void signalLock(String sessionId){
		if(conditionLocksString.get(sessionId) == null) return;
		CountDownLatch condition = conditionLocksString.get(sessionId);
		condition.countDown();
	}
	
	public static void waitLock(String sessionId){
		CountDownLatch condition = conditionLocksString.get(sessionId);
		try {
			condition.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void hanlder(String sessionId){
		/**
		 *避免多余 CountDownLatch
		 * */
		synchronized(obj){
			CountDownLatch condition= LockContext.getLock(sessionId);
	   	 	if(condition!=null){
	   	 		LockContext.signalLock(sessionId);
	   	 	}
	   	 	
	   	 	condition=new CountDownLatch(1);
	 		LockContext.addLock(sessionId,condition);
		}
		
   	 	LockContext.waitLock(sessionId);
	}
	
	//LockContext.hanlder(sessionId);
}
