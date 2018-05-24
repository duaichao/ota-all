package cn.sd.dao.resource;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import cn.sd.core.dao.BaseDaoImpl;
import cn.sd.core.util.CommonUtils;
import cn.sd.entity.RouteEntity;
import cn.sd.entity.produce.RouteDayDetail;

@Repository
@Service("routeDao")
public class RouteDao extends BaseDaoImpl implements IRouteDao{
	
	public void updateRouteEarnestDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("route.updateRouteEarnestDao", params);
	}
	
	public void saveProContactDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("route.saveProContactDao", params);
	}
	public void delProContactDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().delete("route.delProContactDao", params);
	}
	
	public List<Map<String, Object>> listProContactDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.listProContactDao", params);
	}
	
	public void copyRouteDayDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("route.copyRouteDayDao", params);
	}
	
	public void copyRouteScenicDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("route.copyRouteScenicDao", params);
	}
	
	public List<Map<String, Object>> listEndCityOfRouteByCompanyIdAndCityNameDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.listEndCityOfRouteByCompanyIdAndCityNameDao", params);
	}
	
	public String checkPastDateDao(Map<String,Object> params){
		return (String)this.getSqlMapClientTemplate().queryForObject("route.checkPastDateDao", params);
	} 
	
	public List<Map<String, Object>> routeTrafficByRouteIdDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.routeTrafficByRouteIdDao", params);
	}
	
	public RouteEntity routeDetailDao(Map<String,Object> params){
		List<RouteEntity> data = this.getSqlMapClientTemplate().queryForList("route.routeDetailDao", params);
		if(CommonUtils.checkList(data) && data.size() == 1){
			return data.get(0);
		}
		return null;
	}
	
	public Map<String, Object> routeSingleRoomDao(Map<String,Object> params){
		List<Map<String, Object>> data = this.getSqlMapClientTemplate().queryForList("route.routeSingleRoomDao", params);
		if(CommonUtils.checkList(data) && data.size() == 1){
			return data.get(0);
		}
		return null;
	}
	
	public void updateSingleRoomDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("route.updateSingleRoomDao", params);
	}
	
	public int trafficPastDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("route.trafficPastDao", params);
	}
	
	public List<Map<String, Object>> listDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.listDao", params);
	}
	
	public int countDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("route.countDao", params);
	}
	
	public void saveDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("route.saveDao", params);
	}
	
	public void updateDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("route.updateDao", params);
	}
	
	public List<Map<String, Object>> routeTitleISExistDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.routeTitleISExistDao", params);
	}
	
	public List<Map<String, Object>> listRouteCityDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.listRouteCityDao", params);
	}
	
	public void saveRouteCityDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("route.saveRouteCityDao", params);
	}
	
	public void updateRouteCityDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("route.updateRouteCityDao", params);
	}
	
	public void delRouteCityDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().delete("route.delRouteCityDao", params);
	}
	

	public List<Map<String, Object>> listRouteGrantDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.listDao", params);
	}
	public void saveRouteGrantDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("route.saveRouteGrantDao", params);
	}
	public void delRouteGrantDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().delete("route.delRouteGrantDao", params);
	}
	

	public List<Map<String, Object>> listRouteDayDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.listRouteDayDao", params);
	}
	public void saveRouteDayDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("route.saveRouteDayDao", params);
	}
	public void updateRouteDayDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("route.updateRouteDayDao", params);
	}
	
	public void updateRouteAllDayNOAddDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("route.updateRouteAllDayNOAddDao", params);
	}
	
	public void updateRouteAllDayNOMinusDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("route.updateRouteAllDayNOMinusDao", params);
	}
	
	public void updateRouteAllDayNOUpDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("route.updateRouteAllDayNOUpDao", params);
	}
	
	public void updateRouteAllDayNODownDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("route.updateRouteAllDayNODownDao", params);
	}
	
	public void delRouteDayByRouteCityIdDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("route.delRouteDayByRouteCityIdDao", params);
	}

	public List<RouteDayDetail> listRouteDayDetailDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.listRouteDayDetailDao", params);
	}
	public void saveRouteDayDetailDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("route.saveRouteDayDetailDao", params);
	}
	public void delRouteDayDetailDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().delete("route.delRouteDayDetailDao", params);
	}
	

	public List<Map<String, Object>> listRouteScenicDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.listRouteScenicDao", params);
	}
	public void saveRouteScenicDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("route.saveRouteScenicDao", params);
	}
	public void delRouteScenicDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().delete("route.delRouteScenicDao", params);
	}
	
	public List<Map<String, Object>> listRouteOtherDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.listRouteOtherDao", params);
	}
	public void saveRouteOtherDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("route.saveRouteOtherDao", params);
	}
	public void delRouteOtherDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().delete("route.delRouteOtherDao", params);
	}
	

	public List<Map<String, Object>> listRouteAlbumDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.listRouteAlbumDao", params);
	}
	public void saveRouteAlbumDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("route.saveRouteAlbumDao", params);
	}
	public void delRouteAlbumDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().delete("route.delRouteAlbumDao", params);
	}
	

	public List<String> listRouteTagDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.listRouteTagDao", params);
	}
	public void saveRouteTagDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("route.saveRouteTagDao", params);
	}
	public void delRouteTagDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().delete("route.delRouteTagDao", params);
	}
	
	public Map routeMinDateDao(String route_id){
		return (Map)this.getSqlMapClientTemplate().queryForObject("route.routeMinDateDao", route_id);
	}
	public List<Map<String, Object>> routeRulebyPlanDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.routeRulebyPlanDao", params);
	}
	public List<Map<String, Object>> routePlanAllDao(String route_id){
		return (List<Map<String, Object>>)this.getSqlMapClientTemplate().queryForList("route.routePlanAllDao", route_id);
	}
	public void routeCalendarDao(String sql){
		this.getSqlMapClientTemplate().insert("route.routeCalendarDao", sql);
	}
	public void delRouteCalendarDao(String route_id){
		this.getSqlMapClientTemplate().delete("route.delRouteCalendarDao", route_id);
	}
	public List<String> getRouteIdByruleIdDao(String rule_id){
		return (List<String>)this.getSqlMapClientTemplate().queryForList("route.getRouteIdByruleIdDao", rule_id);
	}
	public int getOutTimeByruleIdDao(String rule_id){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("route.getOutTimeByruleIdDao", rule_id);
	}
	public int getOutTimeByruleIdBeginDao(String rule_id){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("route.getOutTimeByruleIdBeginDao", rule_id);
	}
	
	public List<Map<String, Object>> listScenicDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.listScenicDao", params);
	}
	
	public int countScenicDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("route.countScenicDao", params);
	}
	
	public List<Map<String, Object>> countRouteDayNum(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.countRouteDayNum", params);
	}
	
	public void setDefaultFaceDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("route.setDefaultFaceDao", params);
	}
	
	public void resetFaceDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("route.resetFaceDao", params);
	}
	
	public List<Map<String, Object>> calendarDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.calendarDao", params);
	}
	
	
	public List<Map<String, Object>> listRouteTrafficDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.listRouteTrafficDao", params);
	}
	public void saveRouteTrafficDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("route.saveRouteTrafficDao", params);
	}
	public void updateRouteTrafficDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("route.updateRouteTrafficDao", params);
	}
	public void delRouteTrafficDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("route.delRouteTrafficDao", params);
	}
	public void updateRouteTrafficNOMinusDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("route.updateRouteTrafficNOMinusDao", params);
	}
	
	public List<Map<String, Object>> listRouteTrafficDetailDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.listRouteTrafficDetailDao", params);
	}
	public int countRouteTrafficDetailDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("route.countRouteTrafficDetailDao", params);
	}
	public void saveRouteTrafficDetailDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().insert("route.saveRouteTrafficDetailDao", params);
	}
	public int delRouteTrafficDetailDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().update("route.delRouteTrafficDetailDao", params);
	}
	public void updateRouteTrafficDetailDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("route.updateRouteTrafficDetailDao", params);
	}
	
	public void delRouteTrafficDetailByPlanIdDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("route.delRouteTrafficDetailByPlanIdDao", params);
	}
	
	public int maxTrafficNODao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("route.maxTrafficNODao", params);
	}
	
	public List<Map<String, Object>> listRouteCalendarDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.listRouteCalendarDao", params);
	}
	
	public List<Map<String, Object>> sumPricesDao(Map<String,Object> params){
		return this.getSqlMapClientTemplate().queryForList("route.sumPricesDao", params);
	}
	public int countEndCityDao(Map<String,Object> params){
		return (Integer)this.getSqlMapClientTemplate().queryForObject("route.countEndCityDao", params);
	}
	
	public void delRouteDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().update("route.delRouteDao", params);
	}
	
	public void delRouteStartCityByRouteIdDao(Map<String,Object> params){
		this.getSqlMapClientTemplate().delete("route.delRouteStartCityByRouteIdDao", params);
	}
	
}
