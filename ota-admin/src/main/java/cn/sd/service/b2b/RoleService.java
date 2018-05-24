package cn.sd.service.b2b;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.sd.core.util.CommonUtils;
import cn.sd.dao.b2b.IModuleDao;
import cn.sd.dao.b2b.IPowerDao;
import cn.sd.dao.b2b.IRoleDao;
import cn.sd.dao.site.ICompanyDao;

@Service("roleService")
public class RoleService implements IRoleService {
	private static Log log = LogFactory.getLog(RoleService.class);
	@Resource
	private IRoleDao roleDao;
	@Resource
	private ICompanyDao companyDao;
	@Resource
	private IModuleDao moduleDao;
	
	@Resource
	private IPowerDao powerDao;
	
	@Transactional(rollbackFor={Exception.class})
	public void syncRoleService(Map<String,Object> params)throws Exception{
		/**
		 * 1.角色详情
		 * 2.已同步过角色的子公司
		 * 3.所有需要同步的子公司,过滤已经同步过的公司
		 */
		Map<String, Object> p = new HashMap<String, Object>();
		String PARENT_ROLE_ID = params.get("PARENT_ROLE_ID").toString();
		p.put("ID", PARENT_ROLE_ID);
		Map<String, Object> companyRole = this.companyDao.listCompanyRoleDao(p).get(0);
		p.clear();
		p.put("PID", PARENT_ROLE_ID);
		List<Map<String, Object>> role_companys = this.companyDao.listCompanyRoleDao(p);
		StringBuffer ids = new StringBuffer();
		for (Map<String, Object> role_company : role_companys) {
			ids.append("'"+role_company.get("COMPANY_ID").toString()+"',");
			
			this.roleDao.clearModuleDao(role_company.get("ID").toString());
			
			p.clear();
			p.put("ROLE_ID", role_company.get("ID").toString());
			p.put("SYNC_ROLE_ID", PARENT_ROLE_ID);
			this.moduleDao.syncRoleModuleDao(p);
			
			this.powerDao.clearRolePowerDao(p);
			this.powerDao.syncRolePowerDao(p);
		}
		p.clear();
		p.put("PID", params.get("PARENT_COMPANY_ID"));
		if(ids.length() > 0){
			p.put("NOTINIDS", ids.substring(0, ids.length() - 1).toString());
		}
		p.put("start", 1);
		p.put("end", 1000);
		List<Map<String, Object>> companys = this.companyDao.listCompanyDao(p);
		for (Map<String, Object> company : companys) {
			String companyId = (String)company.get("ID");
			String role_id = CommonUtils.uuid();
			companyRole.put("ID", role_id);
			companyRole.put("PID", PARENT_ROLE_ID);
			companyRole.put("COMPANY_ID", companyId);
			this.companyDao.saveCompanyRoleDao(companyRole);
			
			p.clear();
			p.put("ROLE_ID", role_id);
			p.put("SYNC_ROLE_ID", PARENT_ROLE_ID);
			this.moduleDao.syncRoleModuleDao(p);
			
			this.powerDao.clearRolePowerDao(p);
			this.powerDao.syncRolePowerDao(p);
			
		}
	}
	
	@Override
	public List<Map<String,Object>> listService(Map<String,Object> params){
		return roleDao.listDao(params);
	}
	@Override
	public int countService(Map<String, Object> params) {
		return roleDao.countDao(params);
	}
	@Override
	public int saveService(Map<String, Object> params) throws Exception {
		if(CommonUtils.checkString(params.get("ROLE_NAME"))){
			Map<String,Object> tp = new HashMap<String,Object>();
			tp.put("ROLE_NAME", params.get("ROLE_NAME"));
			if(params.get("ID")!=null){
				tp.put("ID", params.get("ID"));
			}else{
				tp.put("ID","-1");
			}
			if(roleDao.existDao(tp)>0){
				log.info("角色已存在");
				return -1;
			}
			tp.clear();
		}
		if(!CommonUtils.checkString(params.get("ID"))){
			roleDao.addDao(params);
		}else{
			roleDao.editDao(params);
		}
		return 1;
	}
	@Override
	public int delService(String id) throws Exception {
		roleDao.delDao(id);
		return 0;
	}
	@Override
	public int setModuleService(Map<String, Object> params) throws Exception {
		roleDao.setModuleDao(params);
		return 1;
	}
	@Override
	public int setPowerService(Map<String, Object> params) throws Exception {
		roleDao.setPowerDao(params);
		return 1;
	}
	@Override
	public void clearModuleService(String id) throws Exception {
		roleDao.clearModuleDao(id);
	}
	@Override
	public void clearPowerService(Map<String,Object> params) throws Exception {
		roleDao.clearPowerDao(params);
	}
	@Override
	public List<String> userRoleService(String userId) {
		return roleDao.userRoleDao(userId);
	}
}
