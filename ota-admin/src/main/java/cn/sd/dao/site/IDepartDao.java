package cn.sd.dao.site;

import java.util.List;
import java.util.Map;

public interface IDepartDao {
	public List<Map<String, Object>> listDao(Map<String,Object> params);

	public int countDao(Map<String,Object> params);
	
	public void saveDao(Map<String,Object> params)throws Exception;
	
	public void editDao(Map<String,Object> params)throws Exception;
	
	public void delDao(Map<String,Object> params)throws Exception;

}
