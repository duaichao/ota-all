package cn.sd.service.job;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import cn.sd.core.util.CountDownSafe;
import cn.sd.core.util.sms.SMSSender;

public class SendThread extends Thread{
	private Map<String, Object> sendInfo = new HashMap<String, Object>();
	public SendThread(Map<String, Object> sendInfo){
		this.sendInfo = sendInfo;
	}

	public void run(){
		try {
			SMSSender.batchSendSMS(sendInfo.get("MOBILE").toString(), sendInfo.get("CONTENT").toString(), sendInfo.get("ID").toString(), "");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}finally{
			CountDownSafe.release();
		}
	}

}
