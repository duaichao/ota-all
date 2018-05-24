package cn.sd.service.b2b;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ISMSService {
	
	public List<Map<String, Object>> listUserSmsService(Map<String,Object> params);
	
	public int expenseSMS(Map<String,Object> params);
	
	public int onlinePayCallBackService(Map<String,Object> params);
	
	public void saveUserSmsLogService(Map<String,Object> params);
	
	public int countUserSmsLogService(Map<String,Object> params);
	
	public List<Map<String, Object>> listUserSmsLogService(Map<String,Object> params);
	
	public List<Map<String, Object>> listService(Map<String,Object> params);
	
	public int countService(Map<String,Object> params);
	
	public Set<String> iniSend(List<Map<String, Object>> data);
	
	public void iniTask(Set<String> send, String content, String user_id, String STATUS, String TYPE, String SITE_ID, String CREATE_USER);
	
	public void sendGroupSmsService();
	
	public void saveService(Map<String,Object> params);
	
	public int smsSendStatusService(Map<String,Object> params);
	
	public int smsSendStatusGroupService(Map<String,Object> params);
	
	public void usersmsGroupService(Map<String,Object> params);
	
	public boolean voild(String Mobile, String Content, String type);
}
