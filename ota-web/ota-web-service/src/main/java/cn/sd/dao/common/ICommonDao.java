package cn.sd.dao.common;

import java.util.List;
import java.util.Map;

import cn.sd.core.dao.IBaseDao;

public interface ICommonDao extends IBaseDao{

	public void saveWebCollect(Map<String, Object> params);
	
	public void delWebCollect(Map<String, Object> params);
	
	public List<Map<String, Object>> searchWebCollect(Map<String, Object> params);
	
	public int countWebCollect(Map<String, Object> params);
	
	public void usersmsGroupDao(Map<String, Object> params);
	
	public List<Map<String, Object>> listUserSmsDao(Map<String, Object> params);
	
	public List<Map<String, Object>> searchWebCategory(Map<String, Object> params);
	
	public List<Map<String, Object>> serachADATTR(Map<String, Object> params);
	
	public List<Map<String, Object>> searchSiteManager(Map<String, Object> params);
	
}
