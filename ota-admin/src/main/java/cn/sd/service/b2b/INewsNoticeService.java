package cn.sd.service.b2b;

import java.util.List;
import java.util.Map;

import cn.sd.entity.b2b.NewsNoticeEntity;

public interface INewsNoticeService {
	
	public List<Map<String, Object>> listService(Map<String,Object> params);
	
	public int countService(Map<String,Object> params);
	
	public void saveService(Map<String,Object> params);
	
	public void editService(Map<String,Object> params);
	
	public NewsNoticeEntity detailService(Map<String,Object> params);
	
}
