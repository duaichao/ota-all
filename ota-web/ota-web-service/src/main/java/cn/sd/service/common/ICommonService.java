package cn.sd.service.common;

import java.util.List;
import java.util.Map;

public interface ICommonService {

	public void saveWebCollect(Map<String, Object> params);
	
	public void delWebCollect(Map<String, Object> params);
	
	public List<Map<String, Object>> searchWebCollect(Map<String, Object> params);
	
	public int countWebCollect(Map<String, Object> params);
	
	public int expenseSMS(Map<String, Object> params);
	
	public List<Map<String, Object>> searchWebCategory(Map<String, Object> params);
	
	public List<Map<String, Object>> serachADATTR(Map<String, Object> params);
	
	public List<Map<String, Object>> searchSiteManager(Map<String, Object> params);
}
