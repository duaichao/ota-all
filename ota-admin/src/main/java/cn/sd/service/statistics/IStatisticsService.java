package cn.sd.service.statistics;

import java.util.List;
import java.util.Map;


public interface IStatisticsService {

	public List<Map<String, Object>> totalOrderRetailService(Map<String, Object> params);
	public List<Map<String, Object>> totalCompanyOfOrderService(Map<String, Object> params);
	public List<Map<String, Object>> totalCityOfOrderService(Map<String, Object> params);
	public List<Map<String, Object>> companyProduceService(Map<String, Object> params);
	public List<Map<String, Object>> cntCompanyProduceService(Map<String, Object> params);
	public List<Map<String, Object>> totalCompanyRouteCityService(Map<String, Object> params);
	public List<Map<String, Object>> companyOfCityService(Map<String, Object> params);
	public List<Map<String, Object>> totalCompanyOfCityService(Map<String, Object> params);
	public List<Map<String, Object>> totalInfoService(Map<String, Object> params);
	public List<Map<String, Object>> todayTotalInfoService(Map<String, Object> params);
	public List<Map<String, Object>> todaySupplyService(Map<String, Object> params);
	public List<Map<String, Object>> companyService(Map<String, Object> params);
	public List<Map<String, Object>> parentCompanyService(Map<String, Object> params);
	public List<Map<String, Object>> supplyCompanyService(Map<String, Object> params);
	public List<Map<String, Object>> siteCompanyService(Map<String, Object> params);
	public List<Map<String, Object>> startService(Map<String, Object> params);
	public List<Map<String, Object>> listOrderService(Map<String, Object> params);
	public int countOrderService(Map<String, Object> params);
	public List<Map<String, Object>> siteCompanyGroupByWeiDuService(Map<String, Object> params);
	public List<Map<String, Object>> parentCompanyGroupByWeiDuService(Map<String, Object> params);
	public List<Map<String, Object>> supplyCompanyGroupByWeiDuService(Map<String, Object> params);
	
	public List<Map<String, Object>> listOrderGroupByCompanyService(Map<String, Object> params);
	public List<Map<String, Object>> listOrderGroupByProduceService(Map<String, Object> params);
	public List<Map<String, Object>> listRouteGroupByEndCityService(Map<String, Object> params);
}
