package cn.sd.service.b2b;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.CountDownSafe;
import cn.sd.dao.b2b.ISMSDao;
import cn.sd.service.job.SendThread;

@Service("smsService")
public class SMSService implements ISMSService{
	
	@Resource
	private ISMSDao smsDao;
	
	public List<Map<String, Object>> listUserSmsService(Map<String,Object> params){
		return this.smsDao.listUserSmsDao(params);
	}
	
	public int expenseSMS(Map<String,Object> params){
		double useCount = Double.parseDouble(String.valueOf(params.get("useCount")));
		List<Map<String, Object>> data = this.smsDao.listUserSmsDao(params);
		//不存在充值充值记录,不能发送短信
		if(!CommonUtils.checkString(data) || data.size() != 1){
			return -1;
		}
		
		Map<String, Object> d = data.get(0);
		double enableCount = Double.parseDouble(String.valueOf(d.get("ENABLE_COUNT")));
		//可使用短信条数小于需要使用的条数.不能发送短信
		if(enableCount < useCount){
			return -2;
		}
		params.put("SMS_NUM", useCount);
		this.smsDao.usersmsGroupDao(params);
		return 0;
	}
	
	@Transactional(rollbackFor={Exception.class})
	public int onlinePayCallBackService(Map<String,Object> params){
		try {
			Map<String, Object> p = new HashMap<String, Object>();
			String no = (String)params.get("NO");
			p.put("NO", no);
			p.put("PAY_STATUS", 1);
			this.smsDao.updateUserSmsLogStatusDao(p);
			
			p.put("start", 1);
			p.put("end", 1);
			List<Map<String, Object>> data = this.smsDao.listUserSmsLogDao(p);
			Map<String, Object> d = data.get(0);
			
			String cnt = String.valueOf(d.get("COUNT"));
			String COMPANY_ID = (String)d.get("COMPANY_ID");

			p.clear();
			p.put("COMPANY_ID", COMPANY_ID);
			data = this.smsDao.listUserSmsDao(p);
			if(CommonUtils.checkList(data)){
				d = data.get(0);
				String ID = (String)d.get("ID");
				p.clear();
				p.put("SMS_COUNT", cnt);
				p.put("ID", ID);
				this.smsDao.updateUserSmsCountDao(p);
			}else{
				p.clear();
				p.put("ID", CommonUtils.uuid());
				p.put("SMS_COUNT", cnt);
				p.put("USE_COUNT", 0);
				p.put("COMPANY_ID", COMPANY_ID);
				this.smsDao.saveUserSmsDao(p);
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}	
	}
	
	public void saveUserSmsLogService(Map<String,Object> params){
		this.smsDao.saveUserSmsLogDao(params);
	}
	
	public int countUserSmsLogService(Map<String,Object> params){
		return this.smsDao.countUserSmsLogDao(params);
	}
	
	public List<Map<String, Object>> listUserSmsLogService(Map<String,Object> params){
		return this.smsDao.listUserSmsLogDao(params);
	}
	
	public List<Map<String, Object>> listService(Map<String,Object> params){
		return this.smsDao.listDao(params);
	}
	
	public int countService(Map<String,Object> params){
		return this.smsDao.countDao(params);
	}
	
	public Set<String> iniSend(List<Map<String, Object>> data){
		Set<String> send = new HashSet<String>();
		Map<String, Object> temp = new HashMap<String, Object>();
		for(int i=0;i<data.size();i++){
			temp = (Map<String, Object>)data.get(i);
			if(temp.get("MOBILE") != null && !"".equals(temp.get("MOBILE").toString()) && send.contains(temp.get("MOBILE").toString()) == false){
				send.add(temp.get("MOBILE").toString());
			}
		}
		return send;
	}
	
	public void iniTask(Set<String> send, String content, String user_id, String STATUS, String TYPE, String SITE_ID, String CREATE_USER){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("CONTENT", content);
		params.put("CREATE_USER", CREATE_USER);
		params.put("SITE_ID", SITE_ID);
		params.put("STATUS", STATUS);
		params.put("TYPE", TYPE);
		params.put("SMS_TYPE", "2");
		int start=0;
		int step=0;
		StringBuffer sb = new StringBuffer();
		for(Object mobile : send){
			step = step +1;
			sb.append(mobile.toString());
			if(start!=50){
				if(step == send.size()){
					params.put("MOBILE",sb.toString());
					this.smsDao.saveDao(params);
					return;
				}
				sb.append(",");
				start=start+1;
			}else{
				start=0;
				params.put("MOBILE",sb.toString());
				this.smsDao.saveDao(params);
				sb=null;
				sb = new StringBuffer();
			}
		}
	}
	
	public void sendGroupSmsService(){
		Map<String, Object> params = new HashMap<String, Object>();
		//20条
		
		params.put("STATUS", 6);
		params.put("TYPE", 4);
		params.put("start", 1);
		params.put("end", CountDownSafe.availablePermits());
		
		this.smsDao.eidtStatusDao(params);

		List<Map<String, Object>> list = this.smsDao.listDao(params);
		if(list.size()==0) {
			return;
		}
		for(int i=0;i < list.size();i++){
			if(CountDownSafe.acquire()){
				SendThread st = new SendThread((Map<String, Object>)list.get(i));
				st.start();
			}
		}
	}
	
	public void saveService(Map<String,Object> params){
		this.smsDao.saveDao(params);
	}

	public int smsSendStatusService(Map<String,Object> params){
		return this.smsDao.smsSendStatusDao(params);
	}
	
	public int smsSendStatusGroupService(Map<String,Object> params){
		return this.smsDao.smsSendStatusGroupDao(params);
	}
	
	public void usersmsGroupService(Map<String,Object> params){
		this.smsDao.usersmsGroupDao(params);
	}
	
	public boolean voild(String Mobile, String Content, String type){
		
		if(type.equals("4")){
			String[] mobiles = Mobile.split(",");
			for (String mobile : mobiles) {
				boolean b = this.voidHanle(mobile,Content,type);
				if(!b) return false;
			}
		}else{
			boolean b = this.voidHanle(Mobile,Content,type);
			if(!b) return false;
		}
		return true;
	}
	
	/**
	 * 插入日志之前调用此方法
	 * @param Mobile
	 * @param Content
	 * @param sms_type
	 * @return
	 */
	private boolean voidHanle(String Mobile, String Content, String type){
		Map<String, Object> params = new HashMap<String, Object>();
		
		//群发
		String hour = "1/24";
		String day = "1";
		int cnt = 0;
		int fun = 1;
		if(type.equals("4")){
			fun=1;
		}else{
			if(type.equals("1") || type.equals("2")){
				fun=2;
			}else{
				fun=1;
			}
		}
		
		params.put("MOBILE", Mobile);
		params.put("CONTENT", Content);
		if(fun==1){
			cnt = this.smsDao.searchhalfDayCntDao(params);
			if(cnt >= 1){
				return false;
			}
			params.put("TIME", day);
			cnt = this.smsDao.searchDayCntDao(params);
			if(cnt >= 100){
				return false;
			}
		}else{
			cnt = this.smsDao.searchhalfHourCntDao(params);
			if(cnt >= 1){
				return false;
			}
			params.put("TIME", hour);
			cnt = this.smsDao.searchHourCntDao(params);
			if(cnt >= 5){
				return false;
			}
		}
		
		return true;
	}
}