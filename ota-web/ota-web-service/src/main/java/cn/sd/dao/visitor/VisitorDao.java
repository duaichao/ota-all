package cn.sd.dao.visitor;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;

@Repository
@Service("visitorDao")
public class VisitorDao extends BaseDaoImpl implements IVisitorDao {

	public void saveWebVisitor(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("visitor.saveWebVisitor", params);
	}
	
	public void delWebVisitor(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("visitor.delWebVisitor", params);
	}
	
	public void updateWebVisitor(Map<String, Object> params){
		this.getSqlMapClientTemplate().update("visitor.updateWebVisitor", params);
	}
	
	public int countWebVisitor(Map<String, Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("visitor.countWebVisitor", params);
	}
	
	public List<Map<String, Object>> searchWebVisitor(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("visitor.searchWebVisitor", params);
	}
	
	public void saveVisitor(Map<String, Object> params){
		this.getSqlMapClientTemplate().insert("visitor.saveVisitor", params);
	}
	
	public void saveOrderVisitor(Map<String, Object> params){
		this.getSqlMapClientTemplate().insert("visitor.saveOrderVisitor", params);
	}
	
	public List<Map<String, Object>> searchVisitorGroupByType(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("visitor.searchVisitorGroupByType", params);
	}
	
	public List<Map<String, Object>> searchVisitor(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("visitor.searchVisitor", params);
	}
	
	public int countWebUserVisitor(Map<String, Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("visitor.countWebUserVisitor", params);
	}
	
	public List<Map<String, Object>> searchWebUserVisitor(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("visitor.searchWebUserVisitor", params);
	}
}
