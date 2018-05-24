package cn.sd.service.produce;

import java.util.List;
import java.util.Map;

public interface ITrafficService {
	public List<Map<String, Object>> listService(Map<String,Object> params);

	public int countService(Map<String,Object> params);
	
}
