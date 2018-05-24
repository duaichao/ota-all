package cn.sd.core.util.lock;

public class SdLock {
	protected long lastUseTime;
	private Object lock;
	public SdLock(){
		System.out.println("lock init");
		this.lastUseTime= System.nanoTime();
		this.lock = new Object();
	}
	public Object getLock() {
		return lock;
	}

}
