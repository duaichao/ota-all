package cn.sd.service.route;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sd.core.util.CommonUtils;
import cn.sd.core.util.DateUtil;
import cn.sd.dao.route.IRouteDao;
import cn.sd.dao.traffic.ITrafficDao;
import cn.sd.entity.RouteEntity;
import cn.sd.entity.produce.RouteDayDetail;

@Service("routeService")
public class RouteService implements IRouteService {

	@Resource
	private IRouteDao routeDao;
	@Resource
	private ITrafficDao trafficDao;
	
	public List<Map<String, Object>> searchRecommendCity(Map<String, Object> params){
		return this.routeDao.searchRecommendCity(params);
	}
	
	public RouteEntity routeDetailEntity(Map<String, Object> params){
		return this.routeDao.routeDetailEntity(params);
	}
	
	@Override
	public List<Map<String, Object>> searchWebRecommend(Map<String, Object> params){
		return this.routeDao.searchWebRecommend(params);
	}
	
	@Override
	public int countRoute(Map<String, Object> params) {
		return this.routeDao.countRoute(params);
	}
	
	@Override
	public List<Map<String, Object>> searchRoute(Map<String, Object> params) {
		return this.routeDao.searchRoute(params);
	}

	public List<Map<String, Object>> searchRouteGroupByEndCity(Map<String, Object> params){
		return this.routeDao.searchRouteGroupByEndCity(params);
	}
	public List<Map<String, Object>> searchRouteLabelAndCity(Map<String, Object> params){
		return this.routeDao.searchRouteLabelAndCity(params);
	}
	public List<Map<String, Object>> searchRouteOther(Map<String, Object> params){
		return this.routeDao.searchRouteOther(params);
	}
	public List<Map<String, Object>> searchRouteCity(Map<String, Object> params){
		return this.routeDao.searchRouteCity(params);
	}
	public List<Map<String, Object>> searchRouteDay(Map<String, Object> params){
		return this.routeDao.searchRouteDay(params);
	}
	public List<RouteDayDetail> searchRouteDayDetail(Map<String, Object> params){
		return this.routeDao.searchRouteDayDetail(params);
	}
	public List<Map<String, Object>> searchRouteScenic(Map<String, Object> params){
		return this.routeDao.searchRouteScenic(params);
	}
	public List<Map<String, Object>> saerchRouteAlbum(Map<String, Object> params){
		return this.routeDao.saerchRouteAlbum(params);
	}
	public List<Map<String, Object>> searchCalendar(Map<String, Object> params){
		return this.routeDao.searchCalendar(params);
	}
	public List<Map<String, Object>> searchRouteTraffic(Map<String, Object> params){
		return this.routeDao.searchRouteTraffic(params);
	}
	
	public List<Map<String,String>> searchRouteTrafficInfo(Map<String,Object> params){
		List<Map<String, Object>> routeCalendars = this.listRouteCalendarService(params);
		List<Map<String, Object>> ids = this.analyzeIds(routeCalendars);
		for (int i = 0; i < ids.size(); i++) {
			Map<String, Object> _ids = ids.get(i);
			
			String PLANID = (String)_ids.get("planId");
			if(PLANID.equals(params.get("PLAN_ID"))){
				Map<String, Object> searInfo = this.usedSeatByRouteOrder((String)params.get("RQ"), PLANID, (String)_ids.get("ruleIds"));
				if(CommonUtils.checkMap(searInfo)){
					return (List<Map<String,String>>)searInfo.get("seatInfo");
				}
				break;
			}
		}
		return null;
	}
	
	public List<Map<String, Object>> analyzeIds(List<Map<String, Object>> params){
		List<Map<String, Object>> IDS = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> p : params) {
			String[] RULEIDGROUP = p.get("RULEIDGROUP").toString().split("@");
			for (int i = 0; i < RULEIDGROUP.length; i++) {
				Map<String, Object> ids = new HashMap<String, Object>();
				if(CommonUtils.checkString(RULEIDGROUP[i])){
					String[] RULEIDGROUPS = RULEIDGROUP[i].split("-");
					String[] RULE_IDS = RULEIDGROUPS[1].toString().split(",");
					StringBuffer _IDS = new StringBuffer();
					for (int j = 0; j < RULE_IDS.length; j++) {
						_IDS.append("'"+RULE_IDS[j]+"',");
					}
					ids.put("ruleIds", _IDS.toString().substring(0, _IDS.toString().length()-1));
					ids.put("planId", RULEIDGROUPS[0]);
				}
				if(!ids.isEmpty()){
					IDS.add(ids);
				}
			}
		}
		return IDS;
	}
	public List<Map<String, Object>> listRouteCalendarService(Map<String,Object> params){
		return this.routeDao.listRouteCalendarDao(params);
	}
	
	/**
	 * rq = 20140101 
	 * rules = 'id','id'
	 * Map
	 * 	key:isSeat	 value:1 有座位 0没有座位
	 *  key:seatInfo value:List<String>=TRAFFIC_NAME-DATE-STAY_CNT-SEAT
	 *  key:seat 有效座位数
	 * */
	public Map<String,Object> usedSeatByRouteOrder(String rq,String plan_id,String ruleids){
		int flag=1;
		Map<String,Map<String,String>> rs = new HashMap<String,Map<String,String>>();
		
		List<String> keys = new ArrayList<String>();
		
		int stay_cnt = 0;
		int cnt_for = 0;
		int seat_min = 0;
		
		Date dd = DateUtil.subDate(rq);
		
		List<Map<String, Object>> trafficStayList = this.trafficDao.getPlanTraffic(plan_id);
		if(trafficStayList.size()==0) return null;
		
		StringBuffer sb = new StringBuffer();
		for(Map<String, Object> map : trafficStayList){
			Map<String,String> temp = new HashMap<String,String>();
			if(cnt_for==0){
				temp.put("TRAFFIC_NAME", map.get("TITLE").toString());
				temp.put("DATE", rq);
				temp.put("STAY_CNT", map.get("STAY_CNT").toString());
				temp.put("TRAFFIC_ID", map.get("TRAFFIC_ID").toString());
				rs.put(map.get("TRAFFIC_ID").toString(),temp);
				cnt_for = cnt_for + 1;
			}else{
				temp.put("TRAFFIC_NAME", map.get("TITLE").toString());
				temp.put("DATE", DateUtil.getNowDateDAY(dd,stay_cnt).replaceAll("-", ""));
				temp.put("STAY_CNT", map.get("STAY_CNT").toString());
				temp.put("TRAFFIC_ID", map.get("TRAFFIC_ID").toString());
				rs.put(map.get("TRAFFIC_ID").toString(),temp);
			}
			stay_cnt = stay_cnt + Integer.valueOf(map.get("STAY_CNT").toString());
		}
		
		List<Map<String, Object>> list = this.trafficDao.getRidTraffidIdByRuleId(ruleids);
		Map<String, String> params = new HashMap<String, String>();
		for(Map<String, Object> map : list){
			int seat_min_temp = 0;
			String traffic_id = map.get("TRAFFIC_ID").toString();
			String rule_id = map.get("ID").toString();
			params.put("RULE_ID", rule_id);
			Map<String,String> temp  = (Map<String,String>)rs.get(traffic_id);
			params.put("PARAM_DATE", temp.get("DATE").toString());
			Map<String, Object> dsr = this.trafficDao.getDateSeatByRuleId(params);
			if(dsr==null){
				seat_min_temp = 0;
				temp.put("SEAT", "0");
				flag=0;
			}else{
				Object obj  = dsr.get("ACTUAL_SEAT");
				if(null==obj || "".equals(obj)|| "0".equals(obj.toString())){
					temp.put("SEAT", "0");
					seat_min_temp = 0;
					flag=0;
				}else{
					temp.put("SEAT", obj.toString());
					seat_min_temp = Integer.parseInt(obj.toString());
				}
			}
			
			if(seat_min > seat_min_temp){
				seat_min = seat_min_temp;
			}
		}
		
		List<Map<String,String>> rs_list = new LinkedList<Map<String,String>>();
		for(Map<String, Object> map : trafficStayList){
			rs_list.add(rs.get(map.get("TRAFFIC_ID").toString()));
		}
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("isSeat", flag);
		map.put("seatInfo", rs_list);
		map.put("seat", seat_min);
		
		return map;
	}
	
	public List<Map<String, Object>> listPlanPricesService(Map<String, Object> params){
		Map<String, Object> _params = new HashMap<String, Object>();
		Map<String, Object> attrInfo = this.getAttrInfo(params);
		List<Map<String, Object>> routeCalendars = this.listRouteCalendarService(params);
		List<Map<String, Object>> ids = this.analyzeIds(routeCalendars);
		_params.put("attrInfo", attrInfo);
		_params.put("routeCalendars", routeCalendars);
		_params.put("PLAN_ID", params.get("PLAN_ID"));
		_params.put("ROUTE_ID", params.get("ROUTE_ID"));
		_params.put("ids", ids);
		String sql = this.getPriceSql(_params);
		_params.clear();
		_params.put("sql", sql);
		return this.sumPricesService(_params);
	}
	
	private Map<String, Object> getAttrInfo(Map<String, Object> params){
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> priceAttrs = this.trafficDao.searchPriceAttr(params);
		
		Map<String, Object> attr = new HashMap<String, Object>();
		
		StringBuffer traffic_sql = new StringBuffer();
		StringBuffer route_sql = new StringBuffer();
		StringBuffer item_sql = new StringBuffer();
		
		/**
		 * 整理线路和交通SQL
		 */
		for (Map<String, Object> priceAttr : priceAttrs) {
			String ID = (String)priceAttr.get("ID");
			item_sql.append("a."+priceAttr.get("CON_NAME")+" + b."+priceAttr.get("CON_NAME") +" as "+priceAttr.get("CON_NAME")+", ");
			traffic_sql.append(" ,sum(decode(c_order_by,"+priceAttr.get("ORDER_BY").toString()+",price,0)) as "+priceAttr.get("CON_NAME").toString()+" ");
			route_sql.append(" ,sum(decode(c_order_by,"+priceAttr.get("ORDER_BY").toString()+",price,0)) as "+priceAttr.get("CON_NAME").toString()+" ");
			attr.put((String)priceAttr.get("CON_NAME"), (String)priceAttr.get("TITLE"));
			if(ID.equals("0FA5123749D08C87E050007F0100BCAD")){
				params.put("PID", ID);
				List<Map<String, Object>> childs = this.trafficDao.searchPriceAttr(params);
				for (Map<String, Object> child : childs) {
					item_sql.append("a."+child.get("CON_NAME")+" + b."+child.get("CON_NAME") +" as "+child.get("CON_NAME")+", ");
					attr.put((String)child.get("CON_NAME"), (String)child.get("TITLE"));
					traffic_sql.append(" ,sum(decode(c_order_by,2,price,0)) as "+child.get("CON_NAME").toString()+" ");
					route_sql.append(" ,sum(decode(c_order_by,"+child.get("ORDER_BY").toString()+",price,0)) as "+child.get("CON_NAME").toString()+" ");
				}
			}
		}
		result.put("attr", attr);
		result.put("item_sql", item_sql.toString().substring(0, item_sql.toString().length()-1));
		result.put("traffic_sql", traffic_sql.toString());
		result.put("route_sql", route_sql.toString());
		return result;
	}
	
	private String getPriceSql(Map<String, Object> params){
		Map<String, Object> _params = new HashMap<String, Object>();
		List<Map<String, Object>> routeCalendars = (List<Map<String, Object>>)params.get("routeCalendars");
		List<Map<String, Object>> ids = (List<Map<String, Object>>)params.get("ids");
		Map<String, Object> attrInfo = (Map<String, Object>)params.get("attrInfo");
		StringBuffer priceSql = new StringBuffer();
		for (int i = 0; i < ids.size(); i++) {
			Map<String, Object> _ids = ids.get(i);
			
			String PLANID = (String)_ids.get("planId");
			if(PLANID.equals(params.get("PLAN_ID"))){
				String RULE_IDS = (String)_ids.get("ruleIds");
				StringBuffer sql = new StringBuffer();
				sql.append("select a.*, b.title as PLAN_TITLE from ");
				
				sql.append("(select "+attrInfo.get("item_sql")+" a.id,a.title,a.plan_id from ");
				
				/**
				 * 交通规则价格sql
				 */
				_params.clear();
				_params.put("sql", attrInfo.get("traffic_sql"));
				_params.put("PLANID", PLANID);
				_params.put("RULE_IDS", RULE_IDS);
				_params.put("temp_name", "a");
				String traffic_sql = this.getRuleSql(_params);
				sql.append(traffic_sql);

				/**
				 * 线路规则价格sql
				 */
				_params.put("sql", attrInfo.get("route_sql"));
				_params.put("RULE_IDS", "'"+params.get("ROUTE_ID")+"'");
				_params.put("temp_name", "b");
				String route_sql = this.getRuleSql(_params);
				sql.append(","+route_sql);
				sql.append(" where a.plan_id = b.plan_id and a.id = b.id and a.title=b.title ");
				
				sql.append(" ) a, sd_pro_route_traffic b where a.plan_id = b.id ");
				
				priceSql.append(sql.toString());
			}
		}
		return priceSql.toString();
		
	}
	
	private String getRuleSql(Map<String, Object> params){
		StringBuffer sql = new StringBuffer();
		String temp_name = (String)params.get("temp_name");
		sql.append(" (");
		sql.append(" select a.b_order_by,a.type_id as id,a.type_name as title, '"+params.get("PLANID")+"' as plan_id");
		sql.append(params.get("sql"));
		sql.append(" from (");
		sql.append(" select a.*,b.order_by as b_order_by,c.order_by as c_order_by from sd_pro_base_price a,SD_PRO_PRICE_TYPE b,SD_PRO_PRICE_ATTR c");
		sql.append(" where a.TYPE_ID = b.id and a.ATTR_ID=c.id ");
		sql.append(" and a.entity_id in ("+params.get("RULE_IDS")+")");
		sql.append(" ) a group by a.b_order_by,a.type_id,a.type_name");
		sql.append(" )  "+temp_name);
		
		return sql.toString();
	}
	
	public List<Map<String, Object>> sumPricesService(Map<String,Object> params){
		return this.routeDao.sumPricesDao(params);
	}
	
	public List<Map<String, Object>> searchWapPrice(Map<String,Object> params){
		return this.routeDao.searchWapPrice(params);
	}
	
	public List<Map<String, Object>> searchRouteDetail(Map<String,Object> params){
		return this.routeDao.searchRouteDetail(params);
	}
}
