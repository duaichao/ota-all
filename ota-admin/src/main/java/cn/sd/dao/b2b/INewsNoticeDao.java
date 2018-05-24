package cn.sd.dao.b2b;

import java.util.List;
import java.util.Map;

import cn.sd.entity.b2b.NewsNoticeEntity;

public interface INewsNoticeDao {
	public List<Map<String, Object>> listDao(Map<String,Object> params);

	public int countDao(Map<String,Object> params);
	
	public void saveDao(Map<String,Object> params);
	
	public void editDao(Map<String,Object> params);
	
	public NewsNoticeEntity detailDao(Map<String,Object> params);
}
