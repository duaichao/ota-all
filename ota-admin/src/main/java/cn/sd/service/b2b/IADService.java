package cn.sd.service.b2b;

import java.util.List;
import java.util.Map;

public interface IADService {
	
	public List<Map<String, Object>> listService(Map<String,Object> params);

	public int countService(Map<String,Object> params);
	
	public void saveService(Map<String,Object> params)throws Exception;
	
	public void delService(Map<String,Object> params)throws Exception;
	
}
