package cn.sd.dao.b2c;

import java.util.List;
import java.util.Map;


public interface IMemberDao {
	
	public int countDao(Map<String, Object> params);
	public List<Map<String, Object>> listDao(Map<String, Object> params);
	
}
