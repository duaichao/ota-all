package cn.sd.service.b2b;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sd.dao.b2b.IADDao;

@Service("adService")
public class ADService implements IADService{
	
	@Resource
	private IADDao adDao;
	
	public List<Map<String, Object>> listService(Map<String,Object> params){
		return this.adDao.listDao(params);
	}

	public int countService(Map<String,Object> params){
		return this.adDao.countDao(params);
	}
	
	public void saveService(Map<String,Object> params)throws Exception{
		this.adDao.saveDao(params);
	}
	
	public void delService(Map<String,Object> params)throws Exception{
		this.adDao.delDao(params);
	}
}
