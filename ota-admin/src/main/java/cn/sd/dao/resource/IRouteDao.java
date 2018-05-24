package cn.sd.dao.resource;

import java.util.List;
import java.util.Map;

import cn.sd.entity.RouteEntity;
import cn.sd.entity.produce.RouteDayDetail;

public interface IRouteDao {
	
	
	public void updateRouteEarnestDao(Map<String,Object> params);
	
	public void saveProContactDao(Map<String,Object> params);
	public void delProContactDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listProContactDao(Map<String,Object> params);
	
	public void copyRouteDayDao(Map<String,Object> params);
	public void copyRouteScenicDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listEndCityOfRouteByCompanyIdAndCityNameDao(Map<String,Object> params);
	
	public String checkPastDateDao(Map<String,Object> params); 
	
	public List<Map<String, Object>> routeTrafficByRouteIdDao(Map<String,Object> params);
	
	public RouteEntity routeDetailDao(Map<String,Object> params);
	
	public Map<String, Object> routeSingleRoomDao(Map<String,Object> params);
	
	public void updateSingleRoomDao(Map<String,Object> params);
	
	public int trafficPastDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listDao(Map<String,Object> params);
	
	public int countDao(Map<String,Object> params);
	
	public void saveDao(Map<String,Object> params);
	
	public void updateDao(Map<String,Object> params);
	
	public List<Map<String, Object>> routeTitleISExistDao(Map<String,Object> params);
	
	public List<Map<String, Object>> listRouteCityDao(Map<String,Object> params);
	public void saveRouteCityDao(Map<String,Object> params);
	public void updateRouteCityDao(Map<String,Object> params);
	public void delRouteCityDao(Map<String,Object> params);
	public List<String> getRouteIdByruleIdDao(String rule_id);
	public int getOutTimeByruleIdDao(String rule_id);
	public int getOutTimeByruleIdBeginDao(String rule_id);
	
	public List<Map<String, Object>> listRouteGrantDao(Map<String,Object> params);
	public void saveRouteGrantDao(Map<String,Object> params);
	public void delRouteGrantDao(Map<String,Object> params);
	

	public List<Map<String, Object>> listRouteDayDao(Map<String,Object> params);
	public void saveRouteDayDao(Map<String,Object> params);
	public void updateRouteDayDao(Map<String,Object> params);
	public void updateRouteAllDayNOAddDao(Map<String,Object> params);
	public void updateRouteAllDayNOMinusDao(Map<String,Object> params);
	public void updateRouteAllDayNOUpDao(Map<String,Object> params);
	public void updateRouteAllDayNODownDao(Map<String,Object> params);
	public void delRouteDayByRouteCityIdDao(Map<String,Object> params);

	
	public List<RouteDayDetail> listRouteDayDetailDao(Map<String,Object> params);
	public void saveRouteDayDetailDao(Map<String,Object> params);
	public void delRouteDayDetailDao(Map<String,Object> params);
	

	public List<Map<String, Object>> listRouteScenicDao(Map<String,Object> params);
	public void saveRouteScenicDao(Map<String,Object> params);
	public void delRouteScenicDao(Map<String,Object> params);
	
	
	public List<Map<String, Object>> listRouteOtherDao(Map<String,Object> params);
	public void saveRouteOtherDao(Map<String,Object> params);
	public void delRouteOtherDao(Map<String,Object> params);
	

	public List<Map<String, Object>> listRouteAlbumDao(Map<String,Object> params);
	public void saveRouteAlbumDao(Map<String,Object> params);
	public void delRouteAlbumDao(Map<String,Object> params);
	

	public List<String> listRouteTagDao(Map<String,Object> params);
	public void saveRouteTagDao(Map<String,Object> params);
	public void delRouteTagDao(Map<String,Object> params);
	
	public Map routeMinDateDao(String route_id);
	public List<Map<String, Object>> routeRulebyPlanDao(Map<String,Object> params);
	public List<Map<String, Object>> routePlanAllDao(String route_id);
	public void routeCalendarDao(String sql);
	public void delRouteCalendarDao(String route_id);
	
	

	public List<Map<String, Object>> listScenicDao(Map<String,Object> params);
	public int countScenicDao(Map<String,Object> params);
	
	public List<Map<String, Object>> countRouteDayNum(Map<String,Object> params);
	
	public void setDefaultFaceDao(Map<String,Object> params);
	public void resetFaceDao(Map<String,Object> params);
	
	public List<Map<String, Object>> calendarDao(Map<String,Object> params);
	
	
	public List<Map<String, Object>> listRouteTrafficDao(Map<String,Object> params);
	public void saveRouteTrafficDao(Map<String,Object> params);
	public void updateRouteTrafficDao(Map<String,Object> params);
	public void delRouteTrafficDao(Map<String,Object> params);
	public void updateRouteTrafficNOMinusDao(Map<String,Object> params);
	 
	
	public List<Map<String, Object>> listRouteTrafficDetailDao(Map<String,Object> params);
	public int countRouteTrafficDetailDao(Map<String,Object> params);
	public void saveRouteTrafficDetailDao(Map<String,Object> params);
	public int delRouteTrafficDetailDao(Map<String,Object> params);
	public void updateRouteTrafficDetailDao(Map<String,Object> params);
	public void delRouteTrafficDetailByPlanIdDao(Map<String,Object> params);
	
	public int maxTrafficNODao(Map<String,Object> params);
	
	public List<Map<String, Object>> listRouteCalendarDao(Map<String,Object> params);
	
	public List<Map<String, Object>> sumPricesDao(Map<String,Object> params);
	
	public int countEndCityDao(Map<String,Object> params);
	
	public void delRouteDao(Map<String,Object> params);
	
	public void delRouteStartCityByRouteIdDao(Map<String,Object> params);
	
	
}
