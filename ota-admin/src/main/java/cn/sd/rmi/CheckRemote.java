package cn.sd.rmi;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.Random;

import javax.servlet.http.HttpServlet;

import cn.sd.core.config.ConfigLoader;
import cn.sd.core.util.sms.SMSSender;

public class CheckRemote extends HttpServlet{
	public static String str = "在基于语言的编程中,我们经常碰到汉字的处理及显示的问题,一大堆看不懂的乱码肯定不是我们愿意看到的显示效果,怎样才能够让那些汉字正确显示呢,语言默认的编码方式是";
	private static boolean flag = true;
	private static int cnt = 0;
	private static int status = 0;
	static{
		CheckRemote.Remote cr = new CheckRemote.Remote();
		Thread thread = new Thread(cr); 
		thread.start();
	}

	public static class Remote implements Runnable {
	    public void run() {
	    	
	    	flag=true;
	    	cnt=0;
	    	status=0;
	    	
	    	while(true){
	    		if(ServiceProxyFactory.getFlag()!=0){
		    		try{
		    			if(flag==false) break;
		    			ServiceProxyFactory.getProxy().hr();
		    			Thread.currentThread().sleep(1000l);
		    		}catch(Exception ex){
//		    			ex.printStackTrace();
		    			cnt = cnt+1;
		    			if(cnt==6){
		    				//SecurityContextHolder.getContext().logoutUserAll();
			    			flag=false;
			    			status=2;
			    			break;
		    			}
		    		}
		    	}else{
		    		flag=false;
		    		break;
		    	}
	    	}
	    	
//	    	System.out.println(flag+"="+status);
	    	if(flag==false && status==2){
//	    		ServiceProxyFactory.changeLocal();
	    		CheckRemote.sendInfo("service服务中断");
	    	}else if(flag==false && status==1){
	    		CheckRemote.sendInfo("client手动中断与service服务连接");
	    	}
	    }  
	}
	
	private static void sendInfo(String info){
//		System.out.println(info+"="+ConfigLoader.config.getStringConfigDetail("remote_sms"));
		
        Random random = new Random();
        int s = random.nextInt(78);
		if(ConfigLoader.config.getStringConfigDetail("remote_sms").equals("1")){
//			System.out.println("发送。。");
			try {
				new SMSSender().sendSMS("13279292237",info+str.charAt(s),"","","","","", "0", "", "", 0);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
	}

	public static boolean isFlag() {
		return flag;
	}

	public static void setFlag(boolean flag) {
		CheckRemote.flag = flag;
	}

	public static void setStatus(int status) {
		CheckRemote.status = status;
	}

}
