package cn.sd.service.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.util.CommonUtils;
import cn.sd.dao.order.IVisitorOrderDao;

@Repository
@Service("visitorOrderService")

public class VisitorOrderService implements IVisitorOrderService {

	@Resource
	private IVisitorOrderDao visitorOrderDao;
	
	public void saveVisitorService(Map<String, Object> params)throws Exception{
		this.visitorOrderDao.saveVisitorDao(params);
	}
	
	public void saveBatchVisitorService(List<Map<String, Object>> visitors, String ORDER_ID)throws Exception{
		Map<String, Object> params = new HashMap<String, Object>();
		if(CommonUtils.checkList(visitors) && visitors.size() > 0){
			for (Map<String, Object> visitor : visitors) {
				this.visitorOrderDao.saveVisitorDao(visitor);
				params.clear();
				params.put("ID", CommonUtils.uuid());
				params.put("ORDER_ID", ORDER_ID);
				params.put("VISITOR_ID", visitor.get("ID"));
				this.visitorOrderDao.saveOrderVisitorDao(params);
			}
		}
	}
	
	public void delOrderVisitorByIDService(Map<String, Object> params)throws Exception{
		this.visitorOrderDao.delOrderVisitorByIDDao(params);
	}
	
	public void delOrderVisitorByOrderIDService(Map<String, Object> params)throws Exception{
		this.visitorOrderDao.delOrderVisitorByOrderIDDao(params);
	}
	
	public void updateOrderVisitorService(Map<String, Object> params)throws Exception{
		this.visitorOrderDao.updateOrderVisitorDao(params);
	}
	
	public List<Map<String, Object>> listVisitorService(Map<String, Object> params){
		return this.visitorOrderDao.listVisitorDao(params);
	}
	
	public int countVisitorService(Map<String, Object> params){
		return this.visitorOrderDao.countVisitorDao(params);
	}
	
	public List<Map<String, Object>> listOrderVisitorService(Map<String, Object> params){
		return this.visitorOrderDao.listOrderVisitorDao(params);
	}
	
	public int countOrderVisitorService(Map<String, Object> params){
		return this.visitorOrderDao.countOrderVisitorDao(params);
	}
	
	public List<Map<String, Object>> listVisitorGroupByTypeService(Map<String, Object> params){
		return this.visitorOrderDao.listVisitorGroupByTypeDao(params);
	}
	
	public List<Map<String, Object>> listOrderVisitorAndTrafficService(Map<String, Object> params){
		return this.visitorOrderDao.listOrderVisitorAndTrafficDao(params);
	}
}
