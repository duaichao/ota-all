package cn.sd.dao.order;

import java.util.List;
import java.util.Map;

public interface IVisitorOrderDao {
	
	public void saveVisitorDao(Map<String, Object> params)throws Exception;
	public void delOrderVisitorByIDDao(Map<String, Object> params)throws Exception;
	public void delOrderVisitorByOrderIDDao(Map<String, Object> params)throws Exception;
	public void saveOrderVisitorDao(Map<String, Object> params)throws Exception;
	public void updateOrderVisitorDao(Map<String, Object> params)throws Exception;
	
	public List<Map<String, Object>> listVisitorDao(Map<String, Object> params);
	public int countVisitorDao(Map<String, Object> params);
	public List<Map<String, Object>> listOrderVisitorDao(Map<String, Object> params);
	public int countOrderVisitorDao(Map<String, Object> params);
	
	public List<Map<String, Object>> listTrafficVisitorDao(Map<String, Object> params);
	
	public List<Map<String, Object>> listVisitorGroupByTypeDao(Map<String, Object> params);
	public List<Map<String, Object>> listOrderVisitorAndTrafficDao(Map<String, Object> params);
	
}