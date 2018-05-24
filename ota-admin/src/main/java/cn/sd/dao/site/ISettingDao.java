package cn.sd.dao.site;

import java.util.List;
import java.util.Map;

public interface ISettingDao {
	
	public List<Map<String, Object>> listDao(Map<String,Object> params);
	
	public void saveSiteDao(Map<String,Object> params)throws Exception;
	
	public void editSiteUseStatusDao(Map<String,Object> params)throws Exception;
	
	public List<Map<String, Object>> usableSitesDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listSiteAttrDao(Map<String,Object> params);
	
	public void saveSiteAttrDao(Map<String,Object> params)throws Exception;
	
	public void updateSiteAttrDao(Map<String,Object> params)throws Exception;
	
}
