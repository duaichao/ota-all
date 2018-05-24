package cn.sd.dao.statistics;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
@Service("utilStatisticsDao")
public class UtilStatisticsDao implements IUtilStatisticsDao{
	
	@Resource
	private ILatestStatisticsDao latestStatisticsDao;
	
	private static String BASE_SQL  = "SELECT to_char(a.CREATE_TIME, 'yyyy-mm-dd HH24:mi:ss') as CREATE_TIME, "
	+"TO_CHAR(t.PAY_TIME, 'yyyy-mm-dd HH24:mi:ss') PAY_TIME, "
    +"a.STATUS, "
    +"a.START_DATE, "
    +"a.MAN_COUNT, "
    +"a.CHILD_COUNT, "
    +"a.SALE_AMOUNT, "
    +"a.INTER_AMOUNT, "
    +"-(NVL(t1.T_REFUND_AMOUNT, 0)) AS REFUND_AMOUNT, "
    +"NVL(t2.T_MARKETING_AMOUNT, 0) AS MARKETING_AMOUNT, "
    
	+"(a.INTER_AMOUNT + nvl(t1.T_REFUND_AMOUNT, 0) - nvl(t2.T_MARKETING_AMOUNT, 0)) AS ACTUAL_AMOUNT, "
    
    +"a.PRODUCE_TYPE, "
    +"t3.PRODUCE_NAME, "
    +"t3.type AS route_type, "
    +"a.SOURCES, "
    +"a.IS_ALONE, "
    +"t4.company AS buy_company, "
    +"t4.type AS buy_type, "
    +"t4.pid        AS pid, "
	+"t4.brand_name        AS buy_brand_name, "
	+"t4.brand_py        AS buy_brand_py, "
	+"t4.brand_jp        AS buy_brand_jp, "

    +"a.CREATE_COMPANY_ID buy_company_id, "
    +"a.CREATE_USER_ID buy_user_id, "
    
	+"t5.USER_NAME BUY_USER_NAME, "
	+"t5.MOBILE BUY_PHONE, "
    
    +"t5.depart_id AS buy_depart_id, "
    +"t6.IS_MANAGER buy_is_manager, "
    +"t7.type        AS sale_type, "
    +"t7.company        AS sale_company, "
    +"t7.brand_name        AS sale_brand_name, "
    +"t7.brand_py        AS sale_brand_py, "
    +"t7.brand_jp        AS sale_brand_jp, "
    
    +"a.COMPANY_ID      AS sale_company_id, "
    +"t3.CREATE_USER_ID AS sale_user_id, "
    +"t3.is_pub, "
    
    +"t8.user_name, "
    
    +"t8.depart_id      AS sale_depart_id, "
    +"t9.IS_MANAGER     AS sale_is_manager, "
    +"t11.id            AS site_id, "
    +"t11.sub_area      AS city_name, "
    +"a.SITE_RELA_ID, "
    +"a.ACCOUNT_ID, "
    +"a.ACCOUNT_DETAIL_NO, "
    +"a.COMPANY_PAY_ID, "
    
    +"a.PRODUCE_ID, "
	+"a.ACCOUNT_STATUS, "
	+"a.NO, "
    +"a.ID, "
    +"a.PAY_TYPE, "
    +"a.MUSTER_TIME, "
    +"a.MUSTER_PLACE, "
    +"a.VISITOR_CONCAT, "
    +"a.VISITOR_MOBILE, "
    +"a.PORDUCE_CONCAT, "
    +"a.PRODUCE_MOBILE, "
    +"a.COMPANY_NAME, "
    
    +"to_char(a.create_time, 'yyyy') as create_year, "
    +"to_char(a.create_time, 'yyyy-mm') as create_month, "
    +"to_char(a.create_time, 'yyyy-mm-dd') as create_day, "
    +"to_char(a.create_time, 'yyyy')||to_char(a.create_time, 'Q') as create_quarter, "
    
	+"a.start_city, "
	+"a.end_city, "
	
	
	+"t12.TITLE PLAN_NAME, "
	+"t12.ID PLAN_ID "
    
    +"FROM sd_order_base a, "
    +"(SELECT a.order_id funds_order_id, "
    +"  MAX(a.create_time) pay_time "
    +"FROM sd_order_funds a "
    +"WHERE a.type = 2 "
    +"GROUP BY a.order_id "
    +") t, "
    +"(SELECT SUM(a.AMOUNT) AS T_REFUND_AMOUNT, "
    +"  a.ORDER_ID "
    +"FROM SD_ORDER_FUNDS a "
    +"WHERE a.TYPE = 6 "
    +"GROUP BY order_id "
    +") t1, "
    +"(SELECT sum(a.AMOUNT) AS T_MARKETING_AMOUNT, "
    +"  a.ORDER_ID "
    +"FROM SD_ORDER_FUNDS a "
    +"WHERE a.TYPE = 7 GROUP BY a.order_id"
    +") t2, "
    +"( "
    
	+"SELECT a.id, "
	+"TO_CHAR(a.type) AS type, "
	+"a.CREATE_USER_ID, "
	+"a.TITLE AS PRODUCE_NAME, "
	+"a.IS_PUB, "
	+"b.begin_city, "
	+"c.end_city "
	+"FROM sd_pro_route a, "
	+"(SELECT route_id, "
	+"LTRIM(MAX(SYS_CONNECT_BY_PATH(CITY_NAME, ',')), ',') begin_city "
	+"FROM "
	+"(SELECT CITY_NAME, "
	+"route_id, "
	+"ROW_NUMBER() OVER(PARTITION BY route_id ORDER BY CITY_NAME DESC) RN "
	+"FROM SD_PRO_ROUTE_CITY "
	+"WHERE type = 1 "
	+") "
	+"START WITH RN     = 1 "
	+"CONNECT BY RN - 1 = PRIOR RN "
	+"AND route_id        = PRIOR route_id "
	+"GROUP BY route_id "
	+")b, "
	+"(SELECT route_id, "
	+"LTRIM(MAX(SYS_CONNECT_BY_PATH(CITY_NAME, ',')), ',') end_city "
	+"FROM "
	+"(SELECT CITY_NAME, "
	+"route_id, "
	+"ROW_NUMBER() OVER(PARTITION BY route_id ORDER BY CITY_NAME DESC) RN "
	+"FROM SD_PRO_ROUTE_CITY "
	+"WHERE type = 2 "
	+")"
	+"START WITH RN     = 1 "
	+"CONNECT BY RN - 1 = PRIOR RN "
	+"AND route_id        = PRIOR route_id "
	+"GROUP BY route_id "
	+")c "
	+"WHERE a.id = b.route_id "
	+"AND a.id   = c.route_id "
    
    +"UNION ALL "
    +"SELECT b.id, '100' AS type, b.CREATE_USER_ID, b.TITLE AS PRODUCE_NAME, b.IS_PUB, '' as begin_city, '' as end_city FROM sd_pro_traffic b "
    +") t3, "
    +"(select a.id, a.company, decode(a.pid, '-1', a.id, a.pid) as pid, a.brand_name, a.brand_py, a.brand_jp, a.type from sd_user_company a) t4, "
    +"sd_user t5 ,"
    +"SD_USER_PLUS t6, "
    +"sd_user_company t7, "
    +"sd_user t8, "
    +"SD_USER_PLUS t9, "
    +"sd_site_manager t10, "
    +"sd_site t11, "
    +"(select a.* from SD_ORDER_ROUTE_TRAFFIC a where a.is_del = 0) t12 "
    
    	  		
    +"WHERE a.id                              = t.funds_order_id(+) "
    +"AND a.ID                                = t1.ORDER_ID(+) "
    +"AND a.ID                                = t2.ORDER_ID(+) "
    +"AND a.PRODUCE_ID                        = t3.id "
    +"AND a.CREATE_COMPANY_ID                 = t4.id "
    +"AND a.CREATE_USER_ID                    = t5.id "
    +"AND a.CREATE_USER_ID                    = t6.user_id(+) "
    +"AND a.COMPANY_ID                        = t7.id "
    +"AND t3.CREATE_USER_ID                   = t8.id "
    +"AND t3.CREATE_USER_ID                   = t9.user_id(+) "
    +"AND a.SITE_RELA_ID                      = t10.id "
    +"AND t10.site_id                         = t11.id "
	+"AND a.ID                                = t12.ORDER_ID(+) ";
	
	public List<Map<String, Object>> totalOrderRetailDao(Map<String, Object> params){
		params.put("base_sql", this.BASE_SQL);
		return this.latestStatisticsDao.totalOrderRetailDao(params);
	}
	
	public List<Map<String, Object>> totalCompanyOfOrderDao(Map<String, Object> params){
		params.put("base_sql", this.BASE_SQL);
		return this.latestStatisticsDao.totalCompanyOfOrderDao(params);
	}
	
	public List<Map<String, Object>> totalCityOfOrderDao(Map<String, Object> params){
		params.put("base_sql", this.BASE_SQL);
		return this.latestStatisticsDao.totalCityOfOrderDao(params);
	}
	
	public List<Map<String, Object>> companyProduceDao(Map<String, Object> params){
		return this.latestStatisticsDao.companyProduceDao(params);
	}
	
	public List<Map<String, Object>> cntCompanyProduceDao(Map<String, Object> params){
		return this.latestStatisticsDao.cntCompanyProduceDao(params);
	}
	
	public List<Map<String, Object>> totalCompanyRouteCityDao(Map<String, Object> params){
		return this.latestStatisticsDao.totalCompanyRouteCityDao(params);
	}
	
	public List<Map<String, Object>> companyOfCityDao(Map<String, Object> params){
		return this.latestStatisticsDao.companyOfCityDao(params);
	}
	
	public List<Map<String, Object>> totalCompanyOfCityDao(Map<String, Object> params){
		return this.latestStatisticsDao.totalCompanyOfCityDao(params);
	}
	
	public List<Map<String, Object>> totalInfoDao(Map<String, Object> params){
		params.put("base_sql", this.BASE_SQL);
		return this.latestStatisticsDao.totalInfoDao(params);
	}
	
	public List<Map<String, Object>> todayTotalInfoDao(Map<String, Object> params){
		params.put("base_sql", this.BASE_SQL);
		return this.latestStatisticsDao.todayTotalInfoDao(params);
	}
	public List<Map<String, Object>> companyDao(Map<String, Object> params){
		params.put("base_sql", this.BASE_SQL);
		return this.latestStatisticsDao.companyDao(params);
	}
	public List<Map<String, Object>> todaySupplyDao(Map<String, Object> params){
		params.put("base_sql", this.BASE_SQL);
		return this.latestStatisticsDao.todaySupplyDao(params);
	}
	public List<Map<String, Object>> parentCompanyDao(Map<String, Object> params){
		params.put("base_sql", this.BASE_SQL);
		return this.latestStatisticsDao.parentCompanyDao(params);
	}
	public List<Map<String, Object>> supplyCompanyDao(Map<String, Object> params){
		params.put("base_sql", this.BASE_SQL);
		return this.latestStatisticsDao.supplyCompanyDao(params);
	}
	public List<Map<String, Object>> siteCompanyDao(Map<String, Object> params){
		params.put("base_sql", this.BASE_SQL);
		return this.latestStatisticsDao.siteCompanyDao(params);
	}
	public List<Map<String, Object>> startDao(Map<String, Object> params){
		params.put("base_sql", this.BASE_SQL);
		return this.latestStatisticsDao.startDao(params);
	}
	public List<Map<String, Object>> listOrderDao(Map<String, Object> params){
		params.put("base_sql", this.BASE_SQL);
		return this.latestStatisticsDao.listOrderDao(params);
	}
	public int countOrderDao(Map<String, Object> params){
		params.put("base_sql", this.BASE_SQL);
		return this.latestStatisticsDao.countOrderDao(params);
	}
	public List<Map<String, Object>> siteCompanyGroupByWeiDuDao(Map<String, Object> params){
		params.put("base_sql", this.BASE_SQL);
		return this.latestStatisticsDao.siteCompanyGroupByWeiDuDao(params);
	}
	public List<Map<String, Object>> parentCompanyGroupByWeiDuDao(Map<String, Object> params){
		params.put("base_sql", this.BASE_SQL);
		return this.latestStatisticsDao.parentCompanyGroupByWeiDuDao(params);
	}
	public List<Map<String, Object>> supplyCompanyGroupByWeiDuDao(Map<String, Object> params){
		params.put("base_sql", this.BASE_SQL);
		return this.latestStatisticsDao.supplyCompanyGroupByWeiDuDao(params);
	}
	
	public List<Map<String, Object>> listOrderGroupByCompanyDao(Map<String, Object> params){
		params.put("base_sql", this.BASE_SQL);
		return this.latestStatisticsDao.listOrderGroupByCompanyDao(params);
	}
	
	public List<Map<String, Object>> listOrderGroupByProduceDao(Map<String, Object> params){
		params.put("base_sql", this.BASE_SQL);
		return this.latestStatisticsDao.listOrderGroupByProduceDao(params);
	}
	
	public List<Map<String, Object>> listRouteGroupByEndCityDao(Map<String, Object> params){
		params.put("base_sql", this.BASE_SQL);
		return this.latestStatisticsDao.listRouteGroupByEndCityDao(params);
	}
}
