package cn.sd.dao.statistics;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;

@Repository
@Service("latestStatisticsDao")
public class LatestStatisticsDao extends BaseDaoImpl implements ILatestStatisticsDao{
	
	public List<Map<String, Object>> totalOrderRetailDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("latest_statistics.totalOrderRetailDao", params);
	}
	
	public List<Map<String, Object>> totalCompanyOfOrderDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("latest_statistics.totalCompanyOfOrderDao", params);
	}
	
	public List<Map<String, Object>> totalCityOfOrderDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("latest_statistics.totalCityOfOrderDao", params);
	}
	
	public List<Map<String, Object>> companyProduceDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("latest_statistics.companyProduceDao", params);
	}
	
	public List<Map<String, Object>> cntCompanyProduceDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("latest_statistics.cntCompanyProduceDao", params);
	}
	
	public List<Map<String, Object>> totalCompanyRouteCityDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("latest_statistics.totalCompanyRouteCityDao", params);
	}
	
	public List<Map<String, Object>> companyOfCityDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("latest_statistics.companyOfCityDao", params);
	}
	public List<Map<String, Object>> totalCompanyOfCityDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("latest_statistics.totalCompanyOfCityDao", params);
	}
	
	public List<Map<String, Object>> totalInfoDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("latest_statistics.totalInfoDao", params);
	}
	
	public List<Map<String, Object>> todayTotalInfoDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("latest_statistics.todayTotalInfoDao", params);
	}
	public List<Map<String, Object>> companyDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("latest_statistics.companyDao", params);
	}
	public List<Map<String, Object>> todaySupplyDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("latest_statistics.todaySupplyDao", params);
	}
	public List<Map<String, Object>> parentCompanyDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("latest_statistics.parentCompanyDao", params);
	}
	public List<Map<String, Object>> supplyCompanyDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("latest_statistics.supplyCompanyDao", params);
	}
	public List<Map<String, Object>> siteCompanyDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("latest_statistics.siteCompanyDao", params);
	}
	public List<Map<String, Object>> startDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("latest_statistics.startDao", params);
	}
	public List<Map<String, Object>> listOrderDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("latest_statistics.listOrderDao", params);
	}
	public int countOrderDao(Map<String, Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("latest_statistics.countOrderDao", params);
	}
	
	public List<Map<String, Object>> siteCompanyGroupByWeiDuDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("latest_statistics.siteCompanyGroupByWeiDuDao", params);
	}
	public List<Map<String, Object>> parentCompanyGroupByWeiDuDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("latest_statistics.parentCompanyGroupByWeiDuDao", params);
	}
	public List<Map<String, Object>> supplyCompanyGroupByWeiDuDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("latest_statistics.supplyCompanyGroupByWeiDuDao", params);
	}
	
	public List<Map<String, Object>> listOrderGroupByCompanyDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("latest_statistics.listOrderGroupByCompanyDao", params);
	}
	public List<Map<String, Object>> listOrderGroupByProduceDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("latest_statistics.listOrderGroupByProduceDao", params);
	}
	public List<Map<String, Object>> listRouteGroupByEndCityDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("latest_statistics.listRouteGroupByEndCityDao", params);
	}
}
