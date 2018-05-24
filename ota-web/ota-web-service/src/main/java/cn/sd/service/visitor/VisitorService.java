package cn.sd.service.visitor;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sd.dao.visitor.IVisitorDao;

@Service("visitorService")
public class VisitorService implements IVisitorService{

	@Resource
	private IVisitorDao visitorDao;
	
	public void saveWebVisitor(Map<String, Object> params){
		this.visitorDao.saveWebVisitor(params);
	}
	
	public void delWebVisitor(Map<String, Object> params){
		this.visitorDao.delWebVisitor(params);
	}
	
	public void updateWebVisitor(Map<String, Object> params){
		this.visitorDao.updateWebVisitor(params);
	}
	
	public int countWebVisitor(Map<String, Object> params){
		return this.visitorDao.countWebVisitor(params);
	}
	
	public List<Map<String, Object>> searchWebVisitor(Map<String, Object> params){
		return this.visitorDao.searchWebVisitor(params);
	}
	
	public List<Map<String, Object>> searchVisitor(Map<String, Object> params){
		return this.visitorDao.searchVisitor(params);
	}
	
	public int countWebUserVisitor(Map<String, Object> params){
		return this.visitorDao.countWebUserVisitor(params);
	}
	
	public List<Map<String, Object>> searchWebUserVisitor(Map<String, Object> params){
		return this.visitorDao.searchWebUserVisitor(params);
	}
}
