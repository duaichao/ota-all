package cn.sd.service.site;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.sd.core.filter.city.UserCity;

public interface ISettingService {
	
	public List<Map<String, Object>> listService(Map<String,Object> params);
	
	public void saveSiteService(Map<String,Object> params)throws Exception;
	
	public void editSiteUseStatusService(Map<String,Object> params)throws Exception;
	
	public List<Map<String, Object>> usableSitesService(Map<String,Object> params);
	
	public UserCity initCityManager(HttpServletResponse response,HttpServletRequest request);
	
	public List<Map<String, Object>> listSiteAttrService(Map<String,Object> params);
	
	public void saveSiteAttrService(Map<String,Object> params)throws Exception;
	
	public void updateSiteAttrService(Map<String,Object> params)throws Exception;
}
