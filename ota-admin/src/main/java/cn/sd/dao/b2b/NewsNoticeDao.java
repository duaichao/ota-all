package cn.sd.dao.b2b;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;
import cn.sd.entity.b2b.NewsNoticeEntity;

@Repository
@Service("newsNoticeDao")
public class NewsNoticeDao extends BaseDaoImpl implements INewsNoticeDao{
	
	public List<Map<String, Object>> listDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("newsNotice.listDao", params);
	}
	
	public int countDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("newsNotice.countDao", params);
	}
	
	public void saveDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("newsNotice.saveDao", params);
	}
	
	public void editDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("newsNotice.editDao", params);
	}
	
	public NewsNoticeEntity detailDao(Map<String,Object> params){
		return (NewsNoticeEntity)this.getSqlMapClientTemplate().queryForObject("newsNotice.detailDao", params);
	}
}
