package cn.sd.power;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.sd.core.util.CommonUtils;

public class CascadePower {
	
	private CascadePower(){}
	
	public static Map<String, Object> getPower(HttpServletRequest request, HttpServletResponse response, String modId, String modName,Map<String, Object> params){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		/**
		 * 管理员看所有数据
		 */
		if(!"admin".equals((String)user.get("USER_NAME"))){
			if("0D8AE92D48203E81E050007F0100E920".equals((String)user.get("ROLE_ID")) || (CommonUtils.checkString(params.get("ROLE")) && params.get("ROLE").equals("MANAGER")) || CommonUtils.checkList((List<Map<String, Object>>)params.get("MANAGER_SITE"))){
				List<Map<String, Object>> sites = (List<Map<String, Object>>)params.get("MANAGER_SITE");
				if(CommonUtils.checkList(sites)){
					StringBuffer cityIds = new StringBuffer();
					int i = 0;
					for (Map<String, Object> site : sites) {
						if(i != 0){
							cityIds.append(",");
						}
						i++;
						cityIds.append("'"+(String)site.get("CITY_ID")+"'");
					}
					params.put("CITY_IDS", cityIds);
				}else{
					params.put("CITY_IDS", null);
				}
			}else{
				if(modName.equals("route-list") || modName.equals("news-list")){
					if(CommonUtils.checkString(user.get("IS_ALONE")) && user.get("IS_ALONE").toString().equals("1")){
						params.remove("IS_ALONE");
					}
					params.put("CITY_ID", (String)user.get("CITY_ID"));
				}
				String USER_ID = (String)user.get("ID"), 
						COMPANY_USER_ID = (String)user.get("COMPANY_USER_ID"),
						COMPANY_TYPE = (String)user.get("COMPANY_TYPE");
				if(CommonUtils.checkString(COMPANY_TYPE) && COMPANY_TYPE.equals("1") && CommonUtils.checkString(params.get("supply-power")) && params.get("supply-power").toString().equals("depart")){
					if(USER_ID.equals(COMPANY_USER_ID)){
						params.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
					}else{
						String DEPART_IDS = (String)user.get("DEPART_IDS");
						if(CommonUtils.checkString(DEPART_IDS)){
							DEPART_IDS =DEPART_IDS +","+user.get("DEPART_ID");
							params.put("SALE_DEPART_IDS", DEPART_IDS.split(","));
						}else{
							params.put("SALE_DEPART_ID", user.get("DEPART_ID"));
						}
					}
				}else{
					params.put("COMPANY_ID", (String)user.get("COMPANY_ID"));
				}
			}
		}
		return params;
	}
	
	public static Map<String, Object> getSiteAndCompanyPower(HttpServletRequest request, HttpServletResponse response, String modId, String modName,Map<String, Object> params){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		//当前用户不是admin，只查询自己的公司信息
		String TYPE = (String)params.get("TYPE");
		if(!user.get("USER_NAME").equals("admin") && (!CommonUtils.checkString(TYPE) && !CommonUtils.checkString(params.get("CITY_ID")))){
			params.put("COMPANY_ID", user.get("COMPANY_ID"));
		}
		if(CommonUtils.checkString(TYPE)){
			if(TYPE.equals("1")){
				//供应商
				params.put("COMPANY_TYPE", TYPE);
//				mr.pm.put("ROLE_ID", "0D8AE92D48213E81E050007F0100E920");
			}else{
				params.put("COMPANY_TYPE", "2,3,4,5,6,7");
				//旅行社 TYPE:2、3、4、5、6
//				mr.pm.put("ROLE_ID", "0D8AE92D48223E81E050007F0100E920");
			}
		}else{
			//站长
			params.put("ROLE_ID", "0D8AE92D48203E81E050007F0100E920");
			List<Map<String, Object>> sites = (List<Map<String, Object>>)params.get("MANAGER_SITE");
			if(CommonUtils.checkList(sites)){
				StringBuffer cityIds = new StringBuffer();
				int i = 0;
				for (Map<String, Object> site : sites) {
					if(i != 0){
						cityIds.append(",");
					}
					i++;
					cityIds.append("'"+(String)site.get("CITY_ID")+"'");
				}
				params.put("CITY_IDS", cityIds);
			}else{
				params.put("CITY_IDS", null);
			}
			params.put("COMPANY_TYPE", "0");
		}
		return params;
	}
	
	public static Map<String, Object> getOrderPower(HttpServletRequest request, HttpServletResponse response, String modId, String modName,Map<String, Object> params){
		Map<String,Object> user = (Map<String,Object>)request.getSession().getAttribute("S_USER_SESSION_KEY");
		String USER_ID = (String)user.get("ID"),
		COMPANY_USER_ID = (String)user.get("COMPANY_USER_ID"),
		COMPANY_ID = (String)user.get("COMPANY_ID"),
		COMPANY_TYPE = (String)user.get("COMPANY_TYPE"),
		USER_NAME = (String)user.get("USER_NAME"),
		orderTrade = (String)params.get("orderTrade");
		if("0".equals(COMPANY_TYPE)){
			params.put("T_USER_ID", USER_ID);
			params.put("T_COMPANY_ID", COMPANY_ID);
			params.put("HAS_CITY", "YES");
			List<Map<String, Object>> siteManagers = (List<Map<String, Object>>)params.get("sites");
			if(CommonUtils.checkList(siteManagers)){
				StringBuffer siteIds = new StringBuffer();
				for (Map<String, Object> siteManager : siteManagers) {
					String id = (String)siteManager.get("ID");
					siteIds.append("'"+id+"',");
				}
				params.put("SITE_RELA_ID", siteIds.substring(0, siteIds.toString().length()-1));
			}
		}else if(!USER_NAME.equals("admin")){
			/**
			 * 默认0 平台管理公司 1供应商公司2分销商公司3门市部4子公司5同行6商务中心
			 * 公司管理员看公司订单
			 * 	旅行社员工查询自己下的订单
			 * 	供应商员工查询自己发的产品订单
			 */
			if("1".equals(COMPANY_TYPE)){
				if(CommonUtils.checkString(orderTrade) && orderTrade.equals("2")){
					if(USER_ID.equals(COMPANY_USER_ID)){
						params.put("COMPANY_ID", COMPANY_ID);
					}else{
						
						//部门经理看部门员工的所有订单
						String IS_MANAGER = (String)user.get("IS_MANAGER");
						if(CommonUtils.checkString(IS_MANAGER) && IS_MANAGER.equals("1")){
							params.put("SALE_DEPART_ID", user.get("DEPART_ID"));
						}else{
							params.put("PUB_USER_ID", USER_ID);
						}
						
					}
				}else{
					if(USER_ID.equals(COMPANY_USER_ID)){
						params.put("CREATE_COMPANY_ID", COMPANY_ID);
					}else{
						
						//部门经理看部门员工的所有订单
						String IS_MANAGER = (String)user.get("IS_MANAGER");
						if(CommonUtils.checkString(IS_MANAGER) && IS_MANAGER.equals("1")){
							params.put("SALE_DEPART_ID", user.get("DEPART_ID"));
						}else{
							params.put("CREATE_USER_ID", USER_ID);
						}
						
					}
				}
			}else{
				if(USER_ID.equals(COMPANY_USER_ID)){
					params.put("CREATE_COMPANY_ID", COMPANY_ID);
				}else{
					
					//部门经理看部门员工的所有订单
					String IS_MANAGER = (String)user.get("IS_MANAGER");
					if(CommonUtils.checkString(IS_MANAGER) && IS_MANAGER.equals("1")){
						params.put("BUY_DEPART_ID", user.get("DEPART_ID"));
					}else{
						params.put("CREATE_USER_ID", USER_ID);
					}
					
				}
			}
		}
		
		return params;
	}
	
	
}