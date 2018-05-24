package cn.sd.service.b2b;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sd.dao.b2b.INewsNoticeDao;
import cn.sd.entity.b2b.NewsNoticeEntity;

@Service("newsNoticeService")
public class NewsNoticeService implements INewsNoticeService{
	
	@Resource
	private INewsNoticeDao newsNoticeDao;
	
	public List<Map<String, Object>> listService(Map<String,Object> params){
		return this.newsNoticeDao.listDao(params);
	}
	
	public int countService(Map<String,Object> params){
		return this.newsNoticeDao.countDao(params);
	}
	
	public void saveService(Map<String,Object> params){
		this.newsNoticeDao.saveDao(params);
	}
	
	public void editService(Map<String,Object> params){
		this.newsNoticeDao.editDao(params);
	}
	
	public NewsNoticeEntity detailService(Map<String,Object> params){
		return this.newsNoticeDao.detailDao(params);
	}
}
