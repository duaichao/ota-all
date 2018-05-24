package cn.sd.dao.order;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;

@Repository
@Service("visitorOrderDao")
public class VisitorOrderDao extends BaseDaoImpl implements IVisitorOrderDao{
	
	public void saveVisitorDao(Map<String, Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("visitorOrder.saveVisitorDao", params);
	}
	
	public void delOrderVisitorByIDDao(Map<String, Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("visitorOrder.delOrderVisitorByIDDao", params);
	}
	
	public void delOrderVisitorByOrderIDDao(Map<String, Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("visitorOrder.delOrderVisitorByOrderIDDao", params);
	}
	
	public void saveOrderVisitorDao(Map<String, Object> params)throws Exception{
		this.getSqlMapClientTemplate().insert("visitorOrder.saveOrderVisitorDao", params);
	}
	
	public void updateOrderVisitorDao(Map<String, Object> params)throws Exception{
		this.getSqlMapClientTemplate().update("visitorOrder.updateOrderVisitorDao", params);
	}
	
	public List<Map<String, Object>> listVisitorDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("visitorOrder.listVisitorDao", params);
	}
	public int countVisitorDao(Map<String, Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("trafficOrder.countVisitorDao", params);
	}
	
	public List<Map<String, Object>> listOrderVisitorDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("visitorOrder.listOrderVisitorDao", params);
	}
	public int countOrderVisitorDao(Map<String, Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("visitorOrder.countOrderVisitorDao", params);
	}
	
	public List<Map<String, Object>> listTrafficVisitorDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("visitorOrder.listTrafficVisitorDao", params);
	}
	
	public List<Map<String, Object>> listVisitorGroupByTypeDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("visitorOrder.listVisitorGroupByTypeDao", params);
	}
	
	public List<Map<String, Object>> listOrderVisitorAndTrafficDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("visitorOrder.listOrderVisitorAndTrafficDao", params);
	}
	
	
}
