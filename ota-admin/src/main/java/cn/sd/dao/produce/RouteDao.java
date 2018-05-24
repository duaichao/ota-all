package cn.sd.dao.produce;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;

@Repository
@Service("produceRouteDao")
public class RouteDao extends BaseDaoImpl implements IRouteDao{
	
	public void saveWapRecommendCityDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().insert("produceRoute.saveWapRecommendCityDao", params);
	}
	
	public void delWapRecommendCityDao(Map<String, Object> params){
		this.getSqlMapClientTemplate().delete("produceRoute.delWapRecommendCityDao", params);
	}
	
	public List<Map<String, Object>> listRenewDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("produceRoute.listRenewDao", params);
	}
	
	public int countRenewDao(Map<String, Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("produceRoute.countRenewDao", params);
	}
	
	public List<Map<String, Object>> reSeatOrderListDao(Map<String, Object> params){
		return this.getSqlMapClientTemplate().queryForList("produceRoute.reSeatOrderListDao", params);
	}
	
	public List<Map<String, Object>> listDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("produceRoute.listDao", params);
	}
	
	public int countDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("produceRoute.countDao", params);
	}
	
	public List<Map<String, Object>> listCompanyDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("produceRoute.listCompanyDao", params);
	}
	
	public List<Map<String, Object>> listLabelDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("produceRoute.listLabelDao", params);
	}
	
	public List<Map<String, Object>> listRouteTrafficMusterDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("produceRoute.listRouteTrafficMusterDao", params);
	}
	
	public List<Map<String, Object>> listSingleRouteDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("produceRoute.listSingleRouteDao", params);
	}

	public int countSingleRouteDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("produceRoute.countSingleRouteDao", params);
	}
	
	public void saveWapPriceDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("produceRoute.saveWapPriceDao", params);
	}
	public void delWapPriceDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().delete("produceRoute.delWapPriceDao", params);
	}
	public List<Map<String, Object>> listWapPriceDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("produceRoute.listWapPriceDao", params);
	}
}
