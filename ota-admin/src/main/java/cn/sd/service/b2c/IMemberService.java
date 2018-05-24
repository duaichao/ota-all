package cn.sd.service.b2c;

import java.util.List;
import java.util.Map;

public interface IMemberService {

	public int countService(Map<String, Object> params);
	public List<Map<String, Object>> listService(Map<String, Object> params);
}
