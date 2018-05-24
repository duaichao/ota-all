package cn.sd.service.site;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.encode.MD5;
import cn.sd.dao.b2b.IModuleDao;
import cn.sd.dao.b2b.IPowerDao;
import cn.sd.dao.b2b.IUserDao;
import cn.sd.dao.produce.IRouteDao;
import cn.sd.dao.site.ICompanyDao;
import cn.sd.entity.site.WapSettingEntity;

@Service("companyService")
public class CompanyService implements ICompanyService{
	
	@Resource
	private ICompanyDao companyDao;
	@Resource
	private IUserDao userDao;
	@Resource
	private IModuleDao moduleDao;
	@Resource
	private IPowerDao powerDao;
	@Resource
	private IRouteDao produceRouteDao;
	
	public void setAccountUserService(Map<String,Object> params)throws Exception{
		this.companyDao.setAccountUserDao(params);
	}
	
	public void delWebRecommendByWapIdDao(Map<String,Object> params)throws Exception{
		this.companyDao.delWebRecommendByWapIdDao(params);
	}

	public void synCompanyWebDataService(Map<String,Object> params)throws Exception{
		this.companyDao.synCompanyWebDataDao(params);
	}
	
	public void saveManagerDepartService(Map<String,Object> params)throws Exception{
		this.companyDao.delManagerDepartDao(params);
		List<String> DEPART_IDS = (List<String>)params.get("DEPART_IDS");
		if(DEPART_IDS != null && DEPART_IDS.size() > 0){
			this.companyDao.saveManagerDepartDao(params);
		}
	}
	
	public List<Map<String, Object>> searchManagerDepartService(Map<String,Object> params){
		return this.companyDao.searchManagerDepartDao(params);
	}
	
	public void saveCompanyStartCityService(Map<String,Object> params)throws Exception{
		this.companyDao.delCompanyStartCityDao(params);
		List<Map<String, Object>> citys = (List<Map<String, Object>>)params.get("citys");
		if(citys != null && citys.size() > 0){
			this.companyDao.saveCompanyStartCityDao(params);
		}
	}
	public List<Map<String, Object>> searchCompanyStartCityService(Map<String,Object> params)throws Exception{
		return this.companyDao.searchCompanyStartCityDao(params);
	}
	
	public void resetPayErrorCntService(Map<String,Object> params)throws Exception{
		this.companyDao.resetPayErrorCntDao(params);
	}
	
	public void updatePayErrorCntService(Map<String,Object> params)throws Exception{
		this.companyDao.updatePayErrorCntDao(params);
	}
	
	public void initPayPwdService(Map<String,Object> params)throws Exception{
		this.companyDao.initPayPwdDao(params);
	}
	public void updatePayPwdService(Map<String,Object> params)throws Exception{
		this.companyDao.updatePayPwdDao(params);
	}
	public List<Map<String, Object>> searchWebRecommendSupplyService(Map<String,Object> params){
		return this.companyDao.searchWebRecommendSupplyDao(params);
	}
	
	public List<Map<String, Object>> searchWebRouteSupplyService(Map<String,Object> params){
		return this.companyDao.searchWebRouteSupplyDao(params);
	}
	
	public void saveCompanyGrantService(Map<String,Object> params)throws Exception{
		this.companyDao.saveCompanyGrantDao(params);
	}
	
	public List<Map<String, Object>> searchCompanyBankService(Map<String,Object> params){
		return this.companyDao.searchCompanyBankDao(params);
	}
	
	public List<Map<String, Object>> searchAuditRouteService(Map<String,Object> params){
		return this.companyDao.searchAuditRouteDao(params);
	}
	public int countAuditRouteService(Map<String,Object> params){
		return this.companyDao.countAuditRouteDao(params);
	}
	
	public List<Map<String, Object>> listSaleRouteByCompanyIdService(Map<String,Object> params){
		return this.companyDao.listSaleRouteByCompanyIdDao(params);
	}
	
	public void saveSaleRouteService(Map<String,Object> params)throws Exception{
		this.companyDao.saveSaleRouteDao(params);
	}
	
	public void delSaleRouteService(Map<String,Object> params)throws Exception{
		this.companyDao.delSaleRouteDao(params);
	}
	
	public void updateWebRecommendTopOrderByService(Map<String,Object> params){
		this.companyDao.updateWebRecommendTopOrderByDao(params);
	}
	public void updateWebRecommendIsTopService(Map<String,Object> params){
		this.companyDao.updateWebRecommendIsTopDao(params);
	}
	
	public void batchtSaveWebCategoryService(Map<String,Object> params)throws Exception{
		this.companyDao.batchtSaveWebCategoryDao(params);
	}
	public void updateWebCategoryByIdService(Map<String,Object> params)throws Exception{
		this.companyDao.updateWebCategoryByIdDao(params);
	}
	public List<Map<String, Object>> listWebCategoryByCompanyIdService(Map<String,Object> params){
		return this.companyDao.listWebCategoryByCompanyIdDao(params);
	}
	
	public void updateCompanyIsShowByIdsService(Map<String,Object> params)throws Exception{
		this.companyDao.updateCompanyIsShowByIdsDao(params);
	}
	
	public void updateCompanyOrderByIdService(Map<String,Object> params)throws Exception{
		this.companyDao.updateCompanyOrderByIdDao(params);
	}
	
	public Map<String, Object> getCompanyMoneyByDepartIdService(Map<String,Object> params){
		return this.companyDao.getCompanyMoneyByDepartIdDao(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void updateUserCompanyMoneyService(Map<String,Object> params)throws Exception{
		/**
		 * 1.修改金额
		 * 2.添加日志
		 */
		this.companyDao.updateUserCompanyMoneyDao(params);
		params.put("ID", CommonUtils.uuid());
		this.companyDao.saveUserCompanyFundsLogDao(params);
	}
	
	public List<Map<String, Object>> listUserCompanyFundsLogService(Map<String,Object> params){
		return this.companyDao.listUserCompanyFundsLogDao(params);
	}
	
	public void saveCompanyUserRoleService(Map<String,Object> params)throws Exception{
		this.companyDao.deleteCompanyUserRoleDao(params);
		params.put("ID", CommonUtils.uuid());
		this.companyDao.saveCompanyUserRoleDao(params);
	}
	
	public void saveCompanyRoleService(Map<String,Object> params)throws Exception{
		this.companyDao.saveCompanyRoleDao(params);
	}
	public void updateCompanyRoleService(Map<String,Object> params)throws Exception{
		this.companyDao.updateCompanyRoleDao(params);
	}
	public List<Map<String, Object>> listCompanyRoleService(Map<String,Object> params){
		return this.companyDao.listCompanyRoleDao(params);
	}
	
	public void updateCompanyRouteTypeService(Map<String,Object> params)throws Exception{
		this.companyDao.updateCompanyRouteTypeDao(params);
	}
	
	public Map<String, Object> companyDetailService(Map<String,Object> params){
		return this.companyDao.companyDetailDao(params);
	}
	
	public void updateFilialeService(Map<String,Object> params)throws Exception{
		this.companyDao.updateFilialeDao(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void saveWebRecommendService(Map<String,Object> params)throws Exception{
		this.companyDao.saveWebRecommendDao(params);
		String city_name = (String)params.get("END_CITY");
		if(CommonUtils.checkString(city_name)){
			String[] cityNames = city_name.split(",");
			for (int i = 0; i < cityNames.length; i++) {
				params.put("ID", CommonUtils.uuid());
				params.put("CITY_NAME", cityNames[i]);
				this.produceRouteDao.saveWapRecommendCityDao(params);
			}
		}
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void delWebRecommendService(Map<String,Object> params)throws Exception{
		this.companyDao.delWebRecommendDao(params);
		this.produceRouteDao.delWapRecommendCityDao(params);
	}
	
	public void updateWebRecommendOrderService(Map<String,Object> params)throws Exception{
		this.companyDao.updateWebRecommendOrderDao(params);
	}
	
	public int countWebRecommendService(Map<String,Object> params){
		return this.companyDao.countWebRecommendDao(params);
	}
	
	public List<Map<String, Object>> searchWebRecommendService(Map<String,Object> params){
		return this.companyDao.searchWebRecommendDao(params);
	}
	
	public void balanceContractAgencyService(Map<String,Object> params)throws Exception{
		this.companyDao.balanceContractAgencyDao(params);
	}
	
	public void delContractAgencyService(Map<String,Object> params)throws Exception{
		this.companyDao.delContractAgencyDao(params);
	}
	
	public void saveOrderContractLogService(Map<String,Object> params)throws Exception{
		this.companyDao.saveOrderContractLogDao(params);
	}
	
	public int countOrderContractAgencyService(Map<String,Object> params){
		return this.companyDao.countOrderContractAgencyDao(params);
	}
	
	public List<Map<String, Object>> searchOrderContractAgencyService(Map<String,Object> params){
		return this.companyDao.searchOrderContractAgencyDao(params);
	}
	
	public void betchSaveOrderContractAgencyService(Map<String,Object> params) throws Exception{
		this.companyDao.betchSaveOrderContractAgencyDao(params);
	}
	
	public void betchSaveOrderContractLogService(Map<String,Object> params) throws Exception{
		this.companyDao.betchSaveOrderContractLogDao(params);
	}
	
	public List<Map<String, Object>> listService(Map<String,Object> params){
		return this.companyDao.listDao(params);
	}
	
	public int countService(Map<String,Object> params){
		return this.companyDao.countDao(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public String saveSiteManagerService(Map<String,Object> params) throws Exception{
		String USER_ID = CommonUtils.uuid();
		String statusCode = this.validationCompanyInfo(params, "add");
		if(statusCode.equals("0")){
			Map<String, Object> parameters = new HashMap<String, Object>();
			
			String TYPE = (String)params.get("TYPE");
			
			String COMPANY_ID = (String)params.get("ID");
			
			/**
			 * 创建子公司 独立状态/对账方式 与总公司相同
			 */
			if(!params.get("PID").toString().equals("-1") && !TYPE.equals("0") && !TYPE.equals("1")){
				parameters.put("ID", params.get("PID"));
				parameters.put("start", 1);
				parameters.put("end", 10);
				List<Map<String, Object>> d = this.companyDao.listCompanyDao(parameters);
				if(CommonUtils.checkList(d) && d.size() == 1){
					params.put("IS_ALONE", d.get(0).get("IS_ALONE"));
					params.put("ACCOUNT_WAY", d.get(0).get("ACCOUNT_WAY"));
					params.put("OLD_DELAY_DAY", d.get(0).get("OLD_DELAY_DAY"));
					params.put("OLD_AUDIT_DAY", d.get(0).get("OLD_AUDIT_DAY"));
				}else{
					params.put("IS_ALONE", "0");
				}
				
				/**
				 * 总公司已同步的角色,同步给新创建的子公司
				 * 1.已同步的角色集合
				 */
				parameters.clear();
				parameters.put("COMPANY_PID", params.get("PID"));
				parameters.put("PIDISNOTNULL", "YES");
				List<Map<String, Object>> companyRoles = this.companyDao.listCompanyRoleDao(parameters);
				StringBuffer companyRolePIds = new StringBuffer();
				for (Map<String, Object> companyRole : companyRoles) {
					String companyRoleId = (String)companyRole.get("ID");
					String companyRolePId = (String)companyRole.get("PID");
					if(companyRolePIds.toString().indexOf(companyRolePId) == -1){
						companyRolePIds.append(""+companyRolePId+",");
						String _companyRoleId = CommonUtils.uuid();
						companyRole.put("ID", _companyRoleId);
						companyRole.put("COMPANY_ID", COMPANY_ID);
						/**
						 * 同步角色
						 * 同步模块
						 * 同步权限
						 */
						this.companyDao.saveCompanyRoleDao(companyRole);
						
						parameters.clear();
						
						parameters.put("ROLE_ID", _companyRoleId);
						parameters.put("SYNC_ROLE_ID", companyRoleId);
						this.moduleDao.syncRoleModuleDao(parameters);
						
						this.powerDao.syncRolePowerDao(parameters);
						
					}
				}
			}else{
				params.put("IS_ALONE", "0");
			}
			
			if(params.get("ACCOUNT_WAY") == null){
				params.put("ACCOUNT_WAY", "0");
			}
			
			if(params.get("OLD_DELAY_DAY") == null){
				params.put("OLD_DELAY_DAY", "0");
			}
			
			if(params.get("OLD_AUDIT_DAY") == null){
				params.put("OLD_AUDIT_DAY", "0");
			}
			
//			{text:'旅行社',value:2},
//	        /*{text:'门市',value:3},*/
//	        {text:'分公司',value:4},
//	        {text:'门市部',value:5},
//	        {text:'旅游顾问',value:6}
	        
			
			String USER_PWD = (String)params.get("USER_PWD");
			
			
			
			parameters.clear();
			parameters.put("ID", USER_ID);
			parameters.put("USER_NAME", (String)params.get("USER_NAME"));
			parameters.put("USER_PWD", MD5.toMD5(MD5.toMD5((USER_PWD))));
			parameters.put("CREATE_USER", (String)params.get("CREATE_ID"));
			parameters.put("UPDATE_USER", (String)params.get("CREATE_ID"));
			parameters.put("CITY_ID", (String)params.get("CITY_ID"));
			parameters.put("CITY_NAME", (String)params.get("CITY_NAME"));
			parameters.put("COMPANY_ID", COMPANY_ID);
			
			parameters.put("USER_TYPE", (String)params.get("USER_TYPE"));
			parameters.put("IS_USE", (String)params.get("IS_USE"));
			this.userDao.addDao(parameters);
			
			/** 
			 * 保存用户角色
			 */
			
			parameters.clear();
			parameters.put("USER_ID", USER_ID);
			String ROLE_ID = (String)params.get("ROLE_ID");
			if(CommonUtils.checkString(ROLE_ID)){
				parameters.put("ROLE_ID", ROLE_ID);
			}else{
				//0D8AE92D48203E81E050007F0100E920 站长角色 ID
				ROLE_ID = "0D8AE92D48203E81E050007F0100E920";
				parameters.put("ROLE_ID", ROLE_ID);
			}
			//角色ID等于1时，该用户没有角色（如：子公司用户）
			if(!"1".equals(ROLE_ID)){
				this.userDao.setRoleDao(parameters);
			}

			params.put("ID", COMPANY_ID);
			params.put("USER_ID", USER_ID);
			String REQUEST_SOURCE = (String)params.get("REQUEST_SOURCE");
			if(TYPE.equals("0")){
				params.put("AUDIT_STATUS", 1);	
				params.put("AUDIT_USER", params.get("CREATE_USER_NAME"));
				params.put("AUDIT_USER_ID", params.get("CREATE_ID"));
			}else if((TYPE.equals("3") || TYPE.equals("4") || TYPE.equals("7")) && CommonUtils.checkString(REQUEST_SOURCE)){
				params.put("AUDIT_STATUS", 1);	
				params.put("AUDIT_USER", params.get("CREATE_USER_NAME"));
				params.put("AUDIT_USER_ID", params.get("CREATE_ID"));
			}else{
				params.put("AUDIT_STATUS", 0);
				params.put("AUDIT_USER", "");
				params.put("AUDIT_USER_ID", "");
			}
			
			if(!CommonUtils.checkString(params.get("ACCOUNT_WAY"))){
				params.put("ACCOUNT_WAY", 0);
			}
			
			if(!CommonUtils.checkString(params.get("OLD_DELAY_DAY"))){
				params.put("OLD_DELAY_DAY", 0);
			}
			
			if(!CommonUtils.checkString(params.get("OLD_AUDIT_DAY"))){
				params.put("OLD_AUDIT_DAY", 0);
			}
			
			
			this.companyDao.saveUserCompanyDao(params);
			
			if(CommonUtils.checkString(TYPE) && TYPE.equals("0")){
				/**
				 * 保存站长与站之前的关系
				 */
				String[] SITE_IDS = null;
				if(params.get("SITE_IDS").getClass() == String[].class){
					SITE_IDS = (String[])params.get("SITE_IDS");
				}else{
					SITE_IDS = ((String)params.get("SITE_IDS")).split(",");
				}
				for (String SITE_ID : SITE_IDS) {
					parameters.clear();
					if(CommonUtils.checkString(SITE_ID) && CommonUtils.checkString(USER_ID)){
						parameters.put("ID", CommonUtils.uuid());
						parameters.put("SITE_ID", SITE_ID);
						parameters.put("USER_ID", USER_ID);
						this.companyDao.saveSiteManagerDao(parameters);
					}
				}
			}
			
		}
		return statusCode;
	}
	
	/**
	 * 公司名称不能重复（公司表【不包括当前公司】）
	 */
	@Transactional(rollbackFor={Exception.class})
	public String editUserCompanyService(Map<String,Object> params) throws Exception{
		String statusCode = this.validationCompanyInfo(params, "update");
		Map<String, Object> parameters = new HashMap<String, Object>();
		if(statusCode.equals("0")){
			String USER_ID = (String)params.get("USER_ID");
			String TYPE = (String)params.get("TYPE");
			
			if(params.get("ACCOUNT_WAY") == null){
				params.put("ACCOUNT_WAY", "0");
			}
			
			this.companyDao.editUserCompanyDao(params);
			
			List<Map<String, Object>> userOfCompanys = this.companyDao.userOfCompanyDao(params);
			Map<String, Object> userOfCompany = userOfCompanys.get(0); 
			
			/** 
			 * 保存用户角色
			 */
			String type = (String)userOfCompany.get("TYPE");
			if(!type.equals("3") && !type.equals("4")){
				parameters.clear();
				this.userDao.clearRoleDao(USER_ID);
				parameters.put("USER_ID", USER_ID);
				String ROLE_ID = (String)params.get("ROLE_ID");
				if(CommonUtils.checkString(ROLE_ID)){
					parameters.put("ROLE_ID", ROLE_ID);
				}else{
					//0D8AE92D48203E81E050007F0100E920 站长角色 ID
					if(type.equals("0")){
						ROLE_ID = "0D8AE92D48203E81E050007F0100E920";
						parameters.put("ROLE_ID", ROLE_ID);
					}
				}
				//角色ID等于1时，该用户没有角色（如：子公司用户）
				if(!"1".equals(ROLE_ID) && CommonUtils.checkString(ROLE_ID)){
					this.userDao.setRoleDao(parameters);
				}
			}
			
			/**
			 * 1.删除站长管理的所有站
			 * 2.修改站长管理的站的状态
			 * 3.查找站长管理的所有站
			 * 4.对比本次站是否有新站
			 * 5.添加新站
			 */
			parameters.put("USER_ID", USER_ID);
			parameters.put("IS_DEL", "1");
			this.companyDao.editSiteManagerDelStatusDao(parameters);
			if(params.get("SITE_IDS") != null && CommonUtils.checkString(TYPE) && TYPE.equals("0")){
				
				String[] SITE_IDS = null;
				if(params.get("SITE_IDS").getClass() == String[].class){
					SITE_IDS = (String[])params.get("SITE_IDS");
				}else{
					if(CommonUtils.checkString(params.get("SITE_IDS"))){
						SITE_IDS = ((String)params.get("SITE_IDS")).split(",");
					}
					
				}
				if(SITE_IDS != null){
					for (String SITE_ID : SITE_IDS) {
						parameters.clear();
						parameters.put("IS_DEL", "0");
						if(CommonUtils.checkString(SITE_ID) && CommonUtils.checkString(USER_ID)){
							parameters.put("SITE_ID", SITE_ID);
							parameters.put("USER_ID", USER_ID);
							this.companyDao.editSiteManagerDelStatusDao(parameters);
						}
					}
					parameters.clear();
					parameters.put("USER_ID", USER_ID);
					List<Map<String, Object>> data = this.companyDao.listSiteManagerDao(parameters);
					
					boolean b = true;
					String _SITE_ID = "";
					for(String SITE_ID : SITE_IDS){
						b = true;
						if(CommonUtils.checkList(data) && data.size() > 0){
							for (Map<String, Object> d: data){
								_SITE_ID= (String)d.get("SITE_ID");
								if(SITE_ID.equals(_SITE_ID)){
									b = false;
									break;
								}
							}
						}
						if(b){
							parameters.clear();
							if(CommonUtils.checkString(SITE_ID) && CommonUtils.checkString(USER_ID)){
								parameters.put("ID", CommonUtils.uuid());
								parameters.put("SITE_ID", SITE_ID);
								parameters.put("USER_ID", USER_ID);
								this.companyDao.saveSiteManagerDao(parameters);
							}
						}
					}
				}
				
			}
			
		}
		return statusCode;
	}
	
	public void editUser(Map<String,Object> params) throws Exception{
		this.userDao.editDao(params);
	}
	
	public Map<String, Object> detailService(Map<String,Object> params){
		List<Map<String, Object>> data = this.userDao.listDao(params);
		if(CommonUtils.checkList(data) && data.size() == 1){
			return data.get(0);
		}
		return null;
	}
	
	public List<Map<String, Object>> listSiteManagerService(Map<String,Object> params){
		return this.companyDao.listSiteManagerDao(params);
	}
	
	public void audiUserService(Map<String,Object> params) throws Exception{
		this.userDao.editDao(params);
	}
	
	@Transactional(rollbackFor={Exception.class})
	public void audiCompany(Map<String,Object> params) throws Exception{
		Map<String, Object> parameters = new HashMap<String, Object>();
		//修改公司的审核状态
		parameters.put("AUDIT_STATUS", params.get("AUDIT_STATUS"));
		parameters.put("AUDIT_TIME", params.get("AUDIT_TIME"));
		parameters.put("AUDIT_USER", params.get("AUDIT_USER"));
		parameters.put("AUDIT_USER_ID", params.get("AUDIT_USER_ID"));
		parameters.put("ID", params.get("ID"));
		
		this.companyDao.editUserCompanyDao(params);
		//修改用户的启用/禁用状态
		parameters.clear();
		parameters.put("IS_USE", 0);
		parameters.put("ID", params.get("USER_ID"));
		parameters.put("UPDATE_USER", params.get("AUDIT_USER_ID"));
		this.userDao.editDao(parameters);
	}
	
	public List<Map<String, Object>> listCompanyOfUserService(Map<String,Object> params){
		return this.companyDao.listDao(params);
	}
	
	private String validationCompanyInfo(Map<String, Object> params, String editType){
		/**
		 * 1 站长
		 * 	1.1 用户名不能重复
		 * 	1.2 公司名称不能重复
		 * 	1.3 公司代码不能重复
		 *  1.4 许可证号不能重复
		 * 
		 * 2 供应商
		 * 	2.1 用户名不能重复
		 * 	2.2 公司名不能重复
		 * 	2.3 公司代码不能重复
		 * 	2.4 许可证号不能重复
		 * 
		 * 3 旅行社
		 * 	3.1 用户名不能重复
		 * 	3.2 公司名不能重复
		 * 	3.3 公司代码不能重复
		 * 	3.4 许可证号不能重复
		 * 
		 * 4 其他
		 * 	4.1 用户密码
		 * 	4.2 用户信息不能重复
		 *  4.3 公司代码不能重复
		 *  4.4 许可证号不能重复
		 */
		Map<String, Object> parameters = new HashMap<String, Object>();
		if(editType.equals("add")){
			
			String USER_PWD = (String)params.get("USER_PWD");
			String aginPassword = (String)params.get("aginPassword");
			if(!CommonUtils.checkString(USER_PWD) || !CommonUtils.checkString(aginPassword) || !USER_PWD.equals(aginPassword)){
				//密码错误
				return "-3";
			}
			
			parameters.put("USER_NAME", params.get("USER_NAME"));
			int i  = this.userDao.countDao(parameters);
			if(i > 0){
				//用户已存在
				return "-2";
			}
			
		}
		
		parameters.clear();
		
		if(editType.equals("update")){
			parameters.put("notEqualsID", (String)params.get("ID"));
		}
		parameters.put("T_COMPANY", (String)params.get("COMPANY"));
//		parameters.put("T_LICENSE_NO", (String)params.get("LICENSE_NO"));
		parameters.put("T_CODE", (String)params.get("CODE"));
		parameters.put("T_USER_NAME", (String)params.get("USER_NAME"));
		parameters.put("NOTMANAGER", "YES");
		parameters.put("VALIDATION_INFO", "YES");
		int totalSize = this.companyDao.countDao(parameters);
		if(totalSize > 0){
			//公司信息已存在
			return "-1";
		}
		return "0";
	}
	
	public void editCompanyService(Map<String,Object> params) throws Exception{
		this.companyDao.editUserCompanyDao(params);
	}
	
	public List<Map<String, Object>> listCompanyService(Map<String,Object> params){
		return this.companyDao.listCompanyDao(params);
	}

	public int countCompanyService(Map<String,Object> params){
		return this.companyDao.countCompanyDao(params);
	}
	
	public List<Map<String, Object>> listOrderCompanyService(Map<String,Object> params){
		return this.companyDao.listOrderCompanyDao(params);
	}
	
	public List<Map<String, Object>> listAccountCompanyService(Map<String,Object> params){
		return this.companyDao.listAccountCompanyDao(params);
	}
	
	public List<Map<String, Object>> listAloneCompanyService(Map<String,Object> params){
		return this.companyDao.listAloneCompanyDao(params);
	}

	public List<Map<String, Object>> listSiteInfoService(Map<String,Object> params){
		return this.companyDao.listSiteInfoDao(params);
	}
	
	public String saveWapSettingService(Map<String,Object> params)throws Exception{
		this.companyDao.saveWapSettingDao(params);
		this.companyDao.batchtSaveWebCategoryDao(params);
		return "0";
	}
	public String updateWapSettingService(Map<String,Object> params)throws Exception{
		this.companyDao.updateWapSettingDao(params);
		return "0";
	}
	public List<WapSettingEntity> listWapSettingService(Map<String,Object> params){
		return this.companyDao.listWapSettingDao(params);
	}
	public int countWapSettingService(Map<String,Object> params){
		return this.companyDao.countWapSettingDao(params);
	}
	
	public void saveMiniShopService(Map<String,Object> params){
		this.companyDao.saveMiniShopDao(params);
	}
	
	public void updateMiniShopService(Map<String,Object> params){
		this.companyDao.updateMiniShopDao(params);
	}
	
	public void updateMiniShopUseStateDao(Map<String,Object> params){
		this.companyDao.updateMiniShopUseStateDao(params);
	}
	
	public String saveWapAdAttrService(Map<String,Object> params)throws Exception{
		this.companyDao.saveWapAdAttrDao(params);
		return "0";
	}
	public void delWapAdAttrService(Map<String,Object> params){
		this.companyDao.delWapAdAttrDao(params);
	}
	public List<Map<String, Object>> listWapAdAttrService(Map<String,Object> params){
		return this.companyDao.listWapAdAttrDao(params);
	}
	
	public void setPointService(Map<String,Object> params){
		this.companyDao.setPointDao(params);
	}
	
	public void saveBusinessSellerService(Map<String,Object> params){
		this.companyDao.saveBusinessSellerDao(params);
	}
	public void delBusinessSellerService(Map<String,Object> params){
		this.companyDao.delBusinessSellerDao(params);
	}
	public List<Map<String, Object>> listBusinessSellerService(Map<String,Object> params){
		return this.companyDao.listBusinessSellerDao(params);
	}
	public List<Map<String, Object>> listCompanySiteService(Map<String,Object> params){
		return this.companyDao.listCompanySiteDao(params);
	}
	
	public void updateChildCompanyAloneService(Map<String,Object> params)throws Exception{
		this.companyDao.updateChildCompanyAloneDao(params);
	}
}
