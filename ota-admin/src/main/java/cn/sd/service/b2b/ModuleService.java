package cn.sd.service.b2b;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import cn.sd.dao.b2b.IModuleDao;
import cn.sd.entity.b2b.ModuleEntity;



@Service("moduleService")
public class ModuleService implements IModuleService {
	private static Log log = LogFactory.getLog(ModuleService.class);
	@Resource
	private IModuleDao moduleDao;
	
	public void syncRoleModuleService(Map<String,Object> params)throws Exception{
		this.moduleDao.syncRoleModuleDao(params);
	}
	
	@Override
	public List<ModuleEntity> listService(Map<String,Object> params){
		List<ModuleEntity> fir = moduleDao.listDao(params);
		List<ModuleEntity> returnList = new ArrayList<ModuleEntity>();
		for (ModuleEntity item : fir) { 
			if(!item.isLeaf()){
				params.put("PID", item.getId());
				List<ModuleEntity> sec = moduleDao.listDao(params);
				if(sec.size()>0)
				item.setChildren(sec);
			}
			returnList.add(item);
        }  
		return returnList;
	}
	
	@Override
	public List<java.lang.String> listAllService(){
		List<java.lang.String> rs = moduleDao.listAllDao();
		return rs;
	}

	@Override
	public int countService(Map<String, Object> params) {
		return moduleDao.countDao(params);
	}

	@Override
	public List<String> roleModuleService(String roleId) {
		return moduleDao.roldModuleDao(roleId);
	}

	@Override
	public List<ModuleEntity> userRoleModuleService(Map<String, Object> params) {
		List<ModuleEntity> fir = moduleDao.userRoleModuleDao(params);
		List<ModuleEntity> returnList = new ArrayList<ModuleEntity>();
		for (ModuleEntity item : fir) { 
			if(!item.isLeaf()){
				params.put("PID", item.getId());
				List<ModuleEntity> sec = moduleDao.userRoleModuleDao(params);
				if(sec.size()>0)
				item.setChildren(sec);
			}
			returnList.add(item);
        }  
		return returnList;
	}
	
	public List<String> userRoleModuleByStringService(String roleId) {
		 
		return moduleDao.userRoleModuleByStringDao(roleId);
	}
	
	
	public List<ModuleEntity> listRoleOfModuleService(Map<String,Object> params){
		
		List<ModuleEntity> fir = moduleDao.listRoleOfModuleDao(params);
		List<ModuleEntity> returnList = new ArrayList<ModuleEntity>();
		for (ModuleEntity item : fir) { 
			if(!item.isLeaf()){
				params.put("PID", item.getId());
				List<ModuleEntity> sec = moduleDao.listRoleOfModuleDao(params);
				if(sec.size()>0)
				item.setChildren(sec);
			}
			returnList.add(item);
        }  
		return returnList;
	}
	
	public List<String> listUserOfModuleIDService(Map<String, Object> params){
		return this.moduleDao.listUserOfModuleIDDao(params);
	}
	
	@Override
	public List<ModuleEntity> userModuleService(Map<String, Object> params) {
		List<ModuleEntity> fir = moduleDao.userModuleDao(params);
		List<ModuleEntity> returnList = new ArrayList<ModuleEntity>();
		for (ModuleEntity item : fir) { 
			if(!item.isLeaf()){
				params.put("PID", item.getId());
				List<ModuleEntity> sec = moduleDao.userModuleDao(params);
				if(sec.size()>0)
				item.setChildren(sec);
			}
			returnList.add(item);
        }  
		return returnList;
	}
}
