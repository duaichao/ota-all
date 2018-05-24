package cn.sd.dao.statistics;

import java.util.List;
import java.util.Map;


public interface IUtilStatisticsDao {
	
	public List<Map<String, Object>> totalOrderRetailDao(Map<String, Object> params);
	public List<Map<String, Object>> totalCompanyOfOrderDao(Map<String, Object> params);
	public List<Map<String, Object>> totalCityOfOrderDao(Map<String, Object> params);
	public List<Map<String, Object>> companyProduceDao(Map<String, Object> params);
	public List<Map<String, Object>> cntCompanyProduceDao(Map<String, Object> params);
	public List<Map<String, Object>> totalCompanyRouteCityDao(Map<String, Object> params);
	public List<Map<String, Object>> companyOfCityDao(Map<String, Object> params);
	public List<Map<String, Object>> totalCompanyOfCityDao(Map<String, Object> params);
	public List<Map<String, Object>> totalInfoDao(Map<String, Object> params);
	public List<Map<String, Object>> todayTotalInfoDao(Map<String, Object> params);
	public List<Map<String, Object>> todaySupplyDao(Map<String, Object> params);
	public List<Map<String, Object>> companyDao(Map<String, Object> params);
	public List<Map<String, Object>> parentCompanyDao(Map<String, Object> params);
	public List<Map<String, Object>> supplyCompanyDao(Map<String, Object> params);
	public List<Map<String, Object>> siteCompanyDao(Map<String, Object> params);
	public List<Map<String, Object>> startDao(Map<String, Object> params);
	public List<Map<String, Object>> listOrderDao(Map<String, Object> params);
	public int countOrderDao(Map<String, Object> params);
	public List<Map<String, Object>> siteCompanyGroupByWeiDuDao(Map<String, Object> params);
	public List<Map<String, Object>> parentCompanyGroupByWeiDuDao(Map<String, Object> params);
	public List<Map<String, Object>> supplyCompanyGroupByWeiDuDao(Map<String, Object> params);
	
	public List<Map<String, Object>> listOrderGroupByCompanyDao(Map<String, Object> params);
	public List<Map<String, Object>> listOrderGroupByProduceDao(Map<String, Object> params);
	public List<Map<String, Object>> listRouteGroupByEndCityDao(Map<String, Object> params);
}
