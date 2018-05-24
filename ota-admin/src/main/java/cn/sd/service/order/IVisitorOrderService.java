package cn.sd.service.order;

import java.util.List;
import java.util.Map;

public interface IVisitorOrderService {

	public void saveVisitorService(Map<String, Object> params)throws Exception;
	public void saveBatchVisitorService(List<Map<String, Object>> params, String ORDER_ID)throws Exception;
	
	public void delOrderVisitorByIDService(Map<String, Object> params)throws Exception;
	public void delOrderVisitorByOrderIDService(Map<String, Object> params)throws Exception;
	
	public void updateOrderVisitorService(Map<String, Object> params)throws Exception;
	
	public List<Map<String, Object>> listVisitorService(Map<String, Object> params);
	public int countVisitorService(Map<String, Object> params);
	
	public List<Map<String, Object>> listOrderVisitorService(Map<String, Object> params);
	public int countOrderVisitorService(Map<String, Object> params);
	
	public List<Map<String, Object>> listVisitorGroupByTypeService(Map<String, Object> params);
	
	public List<Map<String, Object>> listOrderVisitorAndTrafficService(Map<String, Object> params);
}
