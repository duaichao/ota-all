package cn.sd.dao.b2b;

import java.util.List;
import java.util.Map;

public interface ISMSDao {
	
	
	public void saveUserSmsLogDao(Map<String,Object> params);
	
	public void updateUserSmsLogStatusDao(Map<String,Object> params);
	
	public void saveUserSmsDao(Map<String,Object> params);
	
	public void updateUserSmsCountDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listUserSmsDao(Map<String,Object> params);

	public int countUserSmsLogDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listUserSmsLogDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listDao(Map<String,Object> params);

	public int countDao(Map<String,Object> params);
	
	public void eidtStatusDao(Map<String,Object> params);
	
	public void saveDao(Map<String,Object> params);
	
	public int smsSendStatusDao(Map<String,Object> params);
	
	public int smsSendStatusGroupDao(Map<String,Object> params);
	
	public void usersmsGroupDao(Map<String,Object> params);

	public int searchhalfHourCntDao(Map<String,Object> params);
	public int searchHourCntDao(Map<String,Object> params);
	public int searchhalfDayCntDao(Map<String,Object> params);
	public int searchDayCntDao(Map<String,Object> params);
	
}
