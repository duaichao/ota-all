package cn.sd.service.visitor;

import java.util.List;
import java.util.Map;

public interface IVisitorService {
	
	public void saveWebVisitor(Map<String, Object> params);
	
	public void delWebVisitor(Map<String, Object> params);
	
	public void updateWebVisitor(Map<String, Object> params);
	
	public int countWebVisitor(Map<String, Object> params);
	public List<Map<String, Object>> searchWebVisitor(Map<String, Object> params);
	
	public List<Map<String, Object>> searchVisitor(Map<String, Object> params);	
	
	public int countWebUserVisitor(Map<String, Object> params);
	public List<Map<String, Object>> searchWebUserVisitor(Map<String, Object> params);
}

