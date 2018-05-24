package cn.sd.service.site;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sd.core.util.CommonUtils;
import cn.sd.dao.site.IDepartDao;

@Service("departService")
public class DepartService implements IDepartService{
	
	@Resource
	private IDepartDao departDao;
	
	public List<Map<String, Object>> listService(Map<String,Object> params){
		return this.departDao.listDao(params);
	}

	public int countService(Map<String,Object> params){
		return this.departDao.countDao(params);
	}
	
	public String saveService(Map<String,Object> params)throws Exception{
		String statusCode = "0";
		params.put("ID", CommonUtils.uuid());
		this.departDao.saveDao(params);
		return statusCode;
	}
	
	public String editService(Map<String,Object> params)throws Exception{
		String statusCode = "0";
		this.departDao.editDao(params);
		return statusCode;
	}
	
	public void delService(Map<String,Object> params)throws Exception{
		this.departDao.delDao(params);
	}
}
