package cn.sd.service.site;

import java.util.List;
import java.util.Map;

import cn.sd.entity.site.WapSettingEntity;

public interface ICompanyService {
	
	public void setAccountUserService(Map<String,Object> params)throws Exception;
	
	public void delWebRecommendByWapIdDao(Map<String,Object> params)throws Exception;
	
	public void synCompanyWebDataService(Map<String,Object> params)throws Exception;
	
	public void saveManagerDepartService(Map<String,Object> params)throws Exception;
	
	public List<Map<String, Object>> searchManagerDepartService(Map<String,Object> params);
	
	public void saveCompanyStartCityService(Map<String,Object> params)throws Exception;
	public List<Map<String, Object>> searchCompanyStartCityService(Map<String,Object> params)throws Exception;
	
	public void resetPayErrorCntService(Map<String,Object> params)throws Exception;
	
	public void updatePayErrorCntService(Map<String,Object> params)throws Exception;
	
	public void initPayPwdService(Map<String,Object> params)throws Exception;
	public void updatePayPwdService(Map<String,Object> params)throws Exception;
	public List<Map<String, Object>> searchWebRecommendSupplyService(Map<String,Object> params);
	
	public List<Map<String, Object>> searchWebRouteSupplyService(Map<String,Object> params);
	
	public void saveCompanyGrantService(Map<String,Object> params)throws Exception;
	public List<Map<String, Object>> searchCompanyBankService(Map<String,Object> params);
	
	public List<Map<String, Object>> searchAuditRouteService(Map<String,Object> params);
	public int countAuditRouteService(Map<String,Object> params);
	
	public List<Map<String, Object>> listSaleRouteByCompanyIdService(Map<String,Object> params);
	
	public void saveSaleRouteService(Map<String,Object> params)throws Exception;
	public void delSaleRouteService(Map<String,Object> params)throws Exception;
	
	public void updateWebRecommendTopOrderByService(Map<String,Object> params);
	public void updateWebRecommendIsTopService(Map<String,Object> params);
	
	public void batchtSaveWebCategoryService(Map<String,Object> params)throws Exception;
	public void updateWebCategoryByIdService(Map<String,Object> params)throws Exception;
	public List<Map<String, Object>> listWebCategoryByCompanyIdService(Map<String,Object> params);
	
	public void updateCompanyIsShowByIdsService(Map<String,Object> params)throws Exception;
	public void updateCompanyOrderByIdService(Map<String,Object> params)throws Exception;
	
	public Map<String, Object> getCompanyMoneyByDepartIdService(Map<String,Object> params);
	public void updateUserCompanyMoneyService(Map<String,Object> params)throws Exception;
	public List<Map<String, Object>> listUserCompanyFundsLogService(Map<String,Object> params);
	
	public void saveCompanyUserRoleService(Map<String,Object> params)throws Exception;
	
	public void saveCompanyRoleService(Map<String,Object> params)throws Exception;
	public void updateCompanyRoleService(Map<String,Object> params)throws Exception;
	public List<Map<String, Object>> listCompanyRoleService(Map<String,Object> params);
	
	public void updateCompanyRouteTypeService(Map<String,Object> params)throws Exception;
	
	public Map<String, Object> companyDetailService(Map<String,Object> params);
	
	public void updateFilialeService(Map<String,Object> params)throws Exception;
	
	public void saveWebRecommendService(Map<String,Object> params)throws Exception;
	
	public void delWebRecommendService(Map<String,Object> params)throws Exception;
	
	public void updateWebRecommendOrderService(Map<String,Object> params)throws Exception;
	
	public int countWebRecommendService(Map<String,Object> params);
	
	public List<Map<String, Object>> searchWebRecommendService(Map<String,Object> params);
	
	public void balanceContractAgencyService(Map<String,Object> params)throws Exception;
	
	public void delContractAgencyService(Map<String,Object> params)throws Exception;
	
	public void saveOrderContractLogService(Map<String,Object> params)throws Exception;
	
	public int countOrderContractAgencyService(Map<String,Object> params);
	
	public List<Map<String, Object>> searchOrderContractAgencyService(Map<String,Object> params);
	
	public void betchSaveOrderContractAgencyService(Map<String,Object> params) throws Exception;

	public void betchSaveOrderContractLogService(Map<String,Object> params) throws Exception;
	
	public List<Map<String, Object>> listService(Map<String,Object> params);
	
	public int countService(Map<String,Object> params);
	
	public String saveSiteManagerService(Map<String,Object> params) throws Exception;
	
	public String editUserCompanyService(Map<String,Object> params) throws Exception;
	
	public void editUser(Map<String,Object> params) throws Exception;
	
	public Map<String, Object> detailService(Map<String,Object> params);
	
	public List<Map<String, Object>> listSiteManagerService(Map<String, Object> parameters);
	
	public void audiUserService(Map<String,Object> params) throws Exception;
	
	public void audiCompany(Map<String,Object> params) throws Exception;
	
	public List<Map<String, Object>> listCompanyOfUserService(Map<String,Object> params);
	
	public void editCompanyService(Map<String,Object> params) throws Exception;
	
	public List<Map<String, Object>> listCompanyService(Map<String,Object> params);

	public int countCompanyService(Map<String,Object> params);
	
	public List<Map<String, Object>> listOrderCompanyService(Map<String,Object> params);
	
	public List<Map<String, Object>> listAccountCompanyService(Map<String,Object> params);
	
	public List<Map<String, Object>> listAloneCompanyService(Map<String,Object> params);
	
	public List<Map<String, Object>> listSiteInfoService(Map<String,Object> params);
	
	public String saveWapSettingService(Map<String,Object> params)throws Exception;
	public String updateWapSettingService(Map<String,Object> params)throws Exception;
	public List<WapSettingEntity> listWapSettingService(Map<String,Object> params);
	public int countWapSettingService(Map<String,Object> params);
	
	public void saveMiniShopService(Map<String,Object> params);
	public void updateMiniShopUseStateDao(Map<String,Object> params);
	public void updateMiniShopService(Map<String,Object> params);
	
	
	public String saveWapAdAttrService(Map<String,Object> params)throws Exception;
	public void delWapAdAttrService(Map<String,Object> params)throws Exception;
	public List<Map<String, Object>> listWapAdAttrService(Map<String,Object> params);
	
	public void setPointService(Map<String,Object> params);
	
	
	public void saveBusinessSellerService(Map<String,Object> params);
	public void delBusinessSellerService(Map<String,Object> params);
	public List<Map<String, Object>> listBusinessSellerService(Map<String,Object> params);
	
	public List<Map<String, Object>> listCompanySiteService(Map<String,Object> params);
	
	public void updateChildCompanyAloneService(Map<String,Object> params)throws Exception;
}
