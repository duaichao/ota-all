package cn.sd.service.user;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sd.dao.user.IUserDao;
import cn.sd.entity.produce.CompanyDetail;

@Service("userService")
public class UserService implements IUserService{

	@Resource
	private IUserDao userDao;
	
	public List<Map<String, Object>> searchWapSetting(Map<String, Object> params){
		return this.userDao.searchWapSetting(params);
	}
	
	public void updateUserPwdByID(Map<String, Object> params)throws Exception{
		this.userDao.updateUserPwdByID(params);
	}
	
	public List<Map<String, Object>> searchWebCategoryByCompanyId(Map<String, Object> params){
		return this.userDao.searchWebCategoryByCompanyId(params);
	}
	public List<CompanyDetail> searchCompanyBuyDomain(Map<String, Object> params){
		return this.userDao.searchCompanyBuyDomain(params);
	}
	
	public List<Map<String, Object>> searchCompanyCounselor(Map<String, Object> params){
		return this.userDao.searchCompanyCounselor(params);
	}
	
	public List<Map<String, Object>> searchWebUser(Map<String, Object> params){
		return this.userDao.searchWebUser(params);
	}
	public void saveWebUser(Map<String, Object> params)throws Exception{
		this.userDao.saveWebUser(params);
	}
	
	public List<Map<String,Object>> searchUser(Map<String,Object> params){
		return this.userDao.searchUser(params);
	}
	
	public void updateWebUserPwd(Map<String,Object> params)throws Exception{
		this.userDao.updateWebUserPwd(params);
	}
	
	public void updateWebUserInfo(Map<String,Object> params)throws Exception{
		this.userDao.updateWebUserInfo(params);
	}
	
	public void updateWebUserMobile(Map<String,Object> params)throws Exception{
		this.userDao.updateWebUserMobile(params);
	}
	
	public void updateWebUserFace(Map<String,Object> params)throws Exception{
		this.userDao.updateWebUserFace(params);
	}
	public void updateUserPwdByMobile(Map<String,Object> params){
		this.userDao.updateUserPwdByMobile(params);
	}
	
}
