package cn.sd.service.site;

import java.util.List;
import java.util.Map;

public interface IDepartService {
	
	public List<Map<String, Object>> listService(Map<String,Object> params);

	public int countService(Map<String,Object> params);
	
	public String saveService(Map<String,Object> params)throws Exception;
	
	public String editService(Map<String,Object> params)throws Exception;
	
	public void delService(Map<String,Object> params)throws Exception;
	
}
