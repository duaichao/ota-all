package cn.sd.core.util.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 访问url的Ip(IpInner)单位contro_time时间内不能超过cnt
 * 单位contro_time计算：第一次访问url为开始时间 第cnt次访问url为结束时间
 * */
public class UrlController {
	
	private String url;
	private int cnt = 0;
	private long contro_time = 0l;
	private long black_time = 0l;
	private long black_begin = 0l;
	private long black_reset_time = 0l;
	private long begin = 0l;
	private long reset_time = 120000000000l;
	private Lock lock = new ReentrantLock(); 
	private Map<String,IpInner> collectionByIp = new HashMap<String,IpInner>();
	private Map<String,IpBlack> collectionByIpBlack = new HashMap<String,IpBlack>();
	
	public UrlController(String url,int cnt,long contro_time,long black_time,long black_reset_time){
		this.url = url;
		this.cnt = cnt;
		this.contro_time = contro_time;
		this.black_time = black_time;
		this.black_reset_time = black_reset_time;
	} 
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	public long getContro_time() {
		return contro_time;
	}
	public void setContro_time(long contro_time) {
		this.contro_time = contro_time;
	}
	
	public Lock getLock() {
		return lock;
	}
	
	public Map getCollectionByIp() {
		return collectionByIp;
	}
	public void setCollectionByIp(Map collectionByIp) {
		this.collectionByIp = collectionByIp;
	}
	
	public Map<String, IpBlack> getCollectionByIpBlack() {
		return collectionByIpBlack;
	}

	public void setCollectionByIpBlack(Map<String, IpBlack> collectionByIpBlack) {
		this.collectionByIpBlack = collectionByIpBlack;
	}
	
	private class IpInner { 
        private String ip;
        private int cnt;
        private long time;
        
        public IpInner(String ip,int cnt,long time){
        	this.ip = ip;
        	this.cnt = cnt;
        	this.time = time;
        }
        
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
		public int getCnt() {
			return cnt;
		}
		public void setCnt(int cnt) {
			this.cnt = cnt;
		}
		public long getTime() {
			return time;
		}
		public void setTime(long time) {
			this.time = time;
		}
    }
	
	public class IpBlack{
		private String ip;
		private long lazy_time;
		public IpBlack(String ip,long lazy_time){
			this.ip = ip;
			this.lazy_time = lazy_time;
		}
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
		public long getLazy_time() {
			return lazy_time;
		}
		public void setLazy_time(long lazy_time) {
			this.lazy_time = lazy_time;
		}
	}
	
	
	
	//判断一个ip是否超时
	public boolean timeOut(String ip){
		IpInner ipInner = this.get(ip);
		long b = ipInner.getTime();
		if(System.nanoTime()-b>=contro_time){
			return true;
		}else{
			return false;
		}
	}
	//判断一个ip是否超出次数
	public boolean cntOut(String ip){
		IpInner ipInner = this.get(ip);
		int b = ipInner.getCnt();
		if(b>=this.getCnt()){
			return true;
		}else{
			return false;
		}
	}
	//判断ip是否存在
	public boolean contain(String ip){
		return collectionByIp.containsKey(ip);
	}
	//添加一个ip
	public void put(String ip){
		IpInner ipInner = new IpInner(ip,1,System.nanoTime());
		collectionByIp.put(ip, ipInner);
	}
	//重置一个ip
	public void reset(String ip){
		IpInner ipInner = this.get(ip);
		ipInner.setCnt(1);
		ipInner.setTime(System.nanoTime());
	}
	//获得一个ip
	public IpInner get(String ip){
		return collectionByIp.get(ip);
	}
	//删除一个ip
	public void remove(String ip){
		collectionByIp.remove(ip);
	}
	//清理ip
	public void cleanIp(){
		Set<String> set = this.getCollectionByIp().keySet();
		for(String ip : set){
			if(this.timeOut(ip)) this.remove(ip);
		}
	}
	public void clean(){
		
		if(this.getCollectionByIp().size()==0) return;
		
		if(begin==0l){
			begin = System.nanoTime();
		}else{
			if(System.nanoTime()-begin>=reset_time){
				cleanIp();
				begin = System.nanoTime();
			}
		}
	}
	//ip访问次数加1
	public void Increasing(String ip){
		IpInner ipInner = this.get(ip);
		ipInner.setCnt(ipInner.getCnt()+1);
	}
	
	/**************************************************
	 ************************************************** 
	 * 黑名单操作
	 * ************************************************
	 * ************************************************
	 * */
	//加入黑名单
	public void putBlack(String ip){
		IpBlack ib = new IpBlack(ip,System.nanoTime());
		collectionByIpBlack.put(ip, ib);
	}
	//判断黑名单是否存在ip
	public boolean containBlack(String ip){
		return collectionByIpBlack.containsKey(ip);
	}
	//获得一个限制ip
	public IpBlack getBlack(String ip){
		return collectionByIpBlack.get(ip);
	}
	//删除一个限制ip
	public void removeBlack(String ip){
		collectionByIpBlack.remove(ip);
	}
	//限制ip是否超时
	public boolean timeOutByBlack(String ip){
		IpBlack ib = this.getBlack(ip);
		long b = ib.getLazy_time();
		if(System.nanoTime()-b>=black_time){
			return true;
		}else{
			return false;
		}
	}
	public void cleanBlack(){
		
		if(this.getCollectionByIpBlack().size()==0) return;
		
		if(black_begin==0l){
			black_begin = System.nanoTime();
		}else{
			if(System.nanoTime()-black_begin>=black_reset_time){
				Set<String> set = this.getCollectionByIpBlack().keySet();
				for(String ip : set){
					if(this.timeOutByBlack(ip)) this.removeBlack(ip);
				}
				black_begin = System.nanoTime();
			}
		}
	}
	
	public String toString(){
		return "url="+url+"\t ip="+collectionByIp.size()+"\t black="+collectionByIpBlack.size();
	}
	
}
